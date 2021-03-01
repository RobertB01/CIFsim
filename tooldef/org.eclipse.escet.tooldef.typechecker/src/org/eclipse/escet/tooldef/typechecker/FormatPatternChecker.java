//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.typeToStr;

import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/** Format pattern checker. */
public class FormatPatternChecker {
    /** Constructor for the {@link FormatPatternChecker} class. */
    private FormatPatternChecker() {
        // Static class.
    }

    /**
     * Performs type checking on a format pattern, for the invocation of a 'fmt', 'out', 'outln', 'err', or 'errln'
     * built-in tool.
     *
     * <p>
     * This static checking is only performed if certain conditions hold:
     * <ul>
     * <li>A format pattern must be given as argument. The 'outln' tool for instance, can also be invoked without any
     * arguments.</li>
     * <li>The format pattern must be given as a string literal (and not for instance a 'string' typed variable or other
     * computation).</li>
     * <li>The format arguments must be given using positional arguments, or a single named argument that is a list
     * literal (and not for instance a list typed variable or other computation).</li>
     * </ul>
     * If one of the conditions doesn't hold, the checking is skipped. Regardless of whether it is checked statically,
     * it is always checked at runtime.
     * </p>
     *
     * @param invoke The fully type checked tool invocation.
     * @param ctxt The type checker context.
     */
    public static void tcheck(ToolInvokeExpression invoke, CheckerContext ctxt) {
        // Precondition checking.
        switch (invoke.getTool().getName()) {
            case "fmt":
            case "out":
            case "outln":
            case "err":
            case "errln":
                // OK.
                break;

            default:
                throw new RuntimeException("Unexpected tool" + invoke.getTool());
        }

        // Check if at least one argument, as for instance 'outln' can be
        // invoked without arguments.
        if (invoke.getArguments().size() < 1) {
            return;
        }

        // Get argument matching the first parameter, as well as its value.
        ToolArgument patternArg = null;
        Expression value = null;
        if (invoke.getArguments().get(0).getName() == null) {
            // First argument is positional, and thus matches first parameter.
            patternArg = invoke.getArguments().get(0);
            value = patternArg.getValue();
        } else {
            // First argument is named, so no positional arguments. Look for
            // named 'pattern' argument.
            for (ToolArgument arg: invoke.getArguments()) {
                if (arg.getName() != null && arg.getName().equals("pattern")) {
                    patternArg = arg;
                    value = arg.getValue();
                    break;
                }
            }

            // If no named 'pattern' argument, we won't check it statically.
            if (patternArg == null) {
                return;
            }
        }

        // Check argument value for being a string literal.
        if (!(value instanceof StringExpression)) {
            return;
        }

        // Get values of the remaining arguments, which are the arguments of
        // the format pattern.
        List<Expression> args = listc(invoke.getArguments().size() - 1);
        for (ToolArgument arg: invoke.getArguments()) {
            // Skip 'pattern' argument.
            if (arg == patternArg) {
                continue;
            }

            // Collect positional arguments.
            if (arg.getName() == null) {
                args.add(arg.getValue());
                continue;
            }

            // There is only one possibility for a named argument here, as we
            // already skipped the 'pattern' argument. And it can only be given
            // once.
            Assert.check(arg.getName().equals("args"));

            // Check for list literal.
            Expression argsValue = arg.getValue();
            if (!(argsValue instanceof ListExpression)) {
                return;
            }

            // Add arguments from the list literal.
            args.addAll(((ListExpression)argsValue).getElements());
        }

        // Check pattern against arguments.
        tcheck((StringExpression)value, args, ctxt);
    }

    /**
     * Performs type checking on a format pattern.
     *
     * @param pattern The format pattern string literal expression.
     * @param args The pattern arguments as expressions.
     * @param ctxt The type checker context.
     */
    private static void tcheck(StringExpression pattern, List<Expression> args, CheckerContext ctxt) {
        // Get the normalized types and positions of the values of the arguments.
        List<ToolDefType> valueTypes = listc(args.size());
        List<Position> valuePositions = listc(args.size());
        for (Expression arg: args) {
            valueTypes.add(ToolDefTypeUtils.normalizeType(arg.getType()));
            valuePositions.add(arg.getPosition());
        }

        // Decode format pattern.
        FormatDecoder decoder = new FormatDecoder();
        List<FormatDescription> parts = decoder.decode(pattern.getValue());

        // Handle errors.
        Position patternPos = pattern.getPosition();
        boolean decodingFailed = false;
        for (FormatDescription part: parts) {
            if (part.conversion != Conversion.ERROR) {
                continue;
            }

            decodingFailed = true;
            ctxt.addProblem(Message.FMT_PAT_DECODE_ERR, createSubPos(patternPos, part), part.text);
            // Non-fatal problem.
        }
        if (decodingFailed) {
            throw new SemanticException();
        }

        // Initialize for unused check.
        int valueCount = valueTypes.size();
        boolean[] used = new boolean[valueCount];

        // Check each specifier.
        int implicitIndex = 0;
        for (FormatDescription part: parts) {
            // Skip non-specifiers.
            if (part.conversion == Conversion.LITERAL) {
                continue;
            }

            // Get 1-based index.
            int idx1;
            if (!part.index.isEmpty()) {
                idx1 = part.getExplicitIndex();
                if (idx1 == -1) {
                    // Integer overflow.
                    ctxt.addProblem(Message.FMT_PAT_IDX_OVERFLOW, createSubPos(patternPos, part));
                    throw new SemanticException();
                }
            } else {
                implicitIndex++;
                idx1 = implicitIndex;
            }

            // Check 1-based index against available values.
            if (idx1 < 1 || idx1 > valueCount) {
                ctxt.addProblem(Message.FMT_PAT_IDX_OUT_OF_RANGE, createSubPos(patternPos, part),
                        Numbers.toOrdinal(idx1));
                throw new SemanticException();
            }

            // Set 0-based index and mark it used.
            int idx0 = idx1 - 1;
            used[idx0] = true;

            // Check type, based on type of conversion.
            ToolDefType type = valueTypes.get(idx0);

            switch (part.conversion) {
                case BOOLEAN:
                    if (!(type instanceof BoolType)) {
                        ctxt.addProblem(Message.FMT_PAT_WRONG_TYPE, createSubPos(patternPos, part), part.text,
                                "bool\" or \"bool?", Numbers.toOrdinal(idx1), typeToStr(type));
                        // Non-fatal error.
                    }
                    break;

                case INTEGER:
                    if (type.isNullable() || (!(type instanceof IntType) && !(type instanceof LongType))) {
                        ctxt.addProblem(Message.FMT_PAT_WRONG_TYPE, createSubPos(patternPos, part), part.text,
                                "int\" or \"long", Numbers.toOrdinal(idx1), typeToStr(type));
                        // Non-fatal error.
                    }
                    break;

                case REAL: {
                    if (type.isNullable() || (!(type instanceof IntType) && !(type instanceof LongType)
                            && !(type instanceof DoubleType)))
                    {
                        ctxt.addProblem(Message.FMT_PAT_WRONG_TYPE, createSubPos(patternPos, part), part.text,
                                "int\", \"long\", or \"double", Numbers.toOrdinal(idx1), typeToStr(type));
                        // Non-fatal error.
                    }
                    break;
                }

                case STRING:
                    // All types allowed.
                    break;

                case ERROR:
                case LITERAL:
                    String msg = "Unexpected: " + part.conversion;
                    throw new RuntimeException(msg);
            }
        }

        // Unused values?
        for (int i = 0; i < used.length; i++) {
            if (!used[i]) {
                ctxt.addProblem(Message.FMT_PAT_UNUSED_VALUE, valuePositions.get(i), Numbers.toOrdinal(i + 1));
                // Non-fatal problem.
            }
        }
    }

    /**
     * Creates a sub {@link Position} covering the given format description.
     *
     * @param pos The original position covering the entire string literal of the format pattern.
     * @param fd The format description for which to obtain the sub position.
     * @return The sub position.
     */
    private static Position createSubPos(Position pos, FormatDescription fd) {
        // Increase 'offset' by one, to account for double quote.
        return PositionUtils.getSubRange(pos, fd.offset + 1, fd.length);
    }
}
