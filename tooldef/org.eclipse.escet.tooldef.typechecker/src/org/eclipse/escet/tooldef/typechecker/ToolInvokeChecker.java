//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.makeTupleType;
import static org.eclipse.escet.tooldef.typechecker.TypeMatcher.computeSubType;
import static org.eclipse.escet.tooldef.typechecker.TypeMatcher.substitute;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/** Tool invocation expression checker. */
public class ToolInvokeChecker {
    /** Constructor for the {@link ToolInvokeChecker} class. */
    private ToolInvokeChecker() {
        // Static class.
    }

    /**
     * Type check a tool invocation expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     * @param asExpr Whether to check the invocation as expression ({@code true}) or as statement ({@code false}).
     */
    public static void tcheck(ToolInvokeExpression expr, CheckerContext ctxt, TypeHints hints, boolean asExpr) {
        // Type check the tool reference.
        boolean builtin = expr.getTool().isBuiltin();
        String toolRefTxt = expr.getTool().getName();
        Position toolRefPos = expr.getTool().getPosition();
        List<ToolRef> toolRefs;
        if (builtin) {
            toolRefs = ctxt.resolveBuiltin(toolRefTxt, toolRefPos);
        } else {
            toolRefs = ctxt.resolveTool(toolRefTxt, toolRefPos);
        }

        // Wrap tool overloads to track additional information.
        List<ToolOverload> tools = listc(toolRefs.size());
        for (ToolRef toolRef: toolRefs) {
            tools.add(new ToolOverload(toolRef));
        }

        // Get tool name.
        String toolName = first(tools).tool.getName();

        // Make sure the named arguments all use different argument names.
        Map<String, Position> argNameMap = map();
        for (ToolArgument arg: expr.getArguments()) {
            // Get name.
            String name = arg.getName();
            if (name == null) {
                continue;
            }

            // Check duplicate.
            Position prevPos = argNameMap.get(name);
            if (prevPos == null) {
                argNameMap.put(name, arg.getPosition());
            } else {
                Position argPos = arg.getPosition();
                ctxt.addProblem(Message.INVOKE_DUPL_NAMED_ARG, prevPos, name);
                ctxt.addProblem(Message.INVOKE_DUPL_NAMED_ARG, argPos, name);
                throw new SemanticException();
            }
        }

        // Make sure there are no positional arguments after named arguments.
        ToolArgument namedArg = null;
        for (ToolArgument arg: expr.getArguments()) {
            // Find first named parameter.
            if (arg.getName() != null && namedArg == null) {
                namedArg = arg;
            }

            // Find positional argument after a named argument.
            if (arg.getName() == null && namedArg != null) {
                ctxt.addProblem(Message.INVOKE_POS_ARG_AFTER_NAMED_ARG, arg.getPosition(), namedArg.getName());
                throw new SemanticException();
            }
        }

        // Check for return type, if used in an expression context.
        if (asExpr) {
            for (ToolOverload tool: tools) {
                tool.checkHasReturnType();
            }
        }

        // Process arguments, one at a time.
        for (ToolArgument arg: expr.getArguments()) {
            // Get matching tool parameters.
            List<ToolParameter> params = listc(tools.size());
            for (ToolOverload tool: tools) {
                ToolParameter param = tool.matchNextArgument(arg);
                params.add(param);
            }
            if (allOverloadsFailed(tools)) {
                break;
            }

            // Get hints for the type argument.
            TypeHints argHints = new TypeHints();
            for (int i = 0; i < tools.size(); i++) {
                ToolParameter param = params.get(i);
                if (param == null) {
                    continue;
                }

                ToolOverload tool = tools.get(i);
                argHints.add(tool.computeArgHint(arg, param));
            }

            // Type check the argument value.
            ExprsChecker.tcheck(arg.getValue(), ctxt, argHints);

            // Check for matching parameter/argument types.
            for (int i = 0; i < tools.size(); i++) {
                ToolParameter param = params.get(i);
                if (param == null) {
                    continue;
                }

                ToolOverload tool = tools.get(i);
                tool.checkTypeMatch(arg, param);
            }
            if (allOverloadsFailed(tools)) {
                break;
            }
        }

        // Make sure all mandatory parameters are matched.
        for (ToolOverload tool: tools) {
            tool.checkComplete();
        }

        // Make sure we have at least one matching overload.
        if (allOverloadsFailed(tools)) {
            ctxt.addProblem(Message.INVOKE_NO_MATCH, expr.getPosition(), toolName, str(tools.size()),
                    (tools.size() == 1) ? "" : "s", getFailureReasons(tools));
            throw new SemanticException();
        }

        // Determine the parameter types (with type parameters filled in) for
        // the matching overloads.
        for (ToolOverload tool: tools) {
            if (!tool.hasFailed()) {
                tool.computeParamTypes();
            }
        }

        // Sort matching overloads from narrowest to widest.
        List<ToolOverload> matches = list();
        for (ToolOverload tool: tools) {
            if (!tool.hasFailed()) {
                matches.add(tool);
            }
        }
        Collections.sort(matches);

        // Choose narrowest matching overload and set it.
        ToolOverload chosenTool = first(matches);
        expr.setTool(chosenTool.toolRef);

        // Set type of the result of the invocation, if tool has a return type.
        Assert.implies(chosenTool.returnType == null, !asExpr);
        if (chosenTool.returnType != null) {
            expr.setType(deepclone(chosenTool.computeReturnType()));
        }

        // Additional checking for format patterns.
        if (chosenTool.toolRef.isBuiltin()) {
            switch (toolName) {
                case "fmt":
                case "out":
                case "outln":
                case "err":
                case "errln":
                    FormatPatternChecker.tcheck(expr, ctxt);
                    break;
            }
        }
    }

    /**
     * Generate a message describing the various reasons the given overloads failed to match the arguments. Identical
     * reasons for different overloads are aggregated.
     *
     * @param tools The overloads that failed to match.
     * @return The message.
     */
    private static String getFailureReasons(List<ToolOverload> tools) {
        // Get mapping from messages to counts.
        Map<String, Integer> msgMap = map();
        for (ToolOverload tool: tools) {
            Integer cnt = msgMap.get(tool.failureMsg);
            if (cnt == null) {
                cnt = 0;
            }
            cnt++;
            msgMap.put(tool.failureMsg, cnt);
        }

        // Sort messages and add the counts to them.
        List<String> msgs = sortedstrings(msgMap.keySet());
        for (int i = 0; i < msgs.size(); i++) {
            String msg = msgs.get(i);
            int cnt = msgMap.get(msg);
            if (cnt > 1) {
                msgs.set(i, msg + fmt(" (%d overloads)", cnt));
            }
        }

        // Return single message.
        return String.join(", ", msgs);
    }

    /**
     * Have all the given overloads failed to match?
     *
     * @param tools The tool overloads.
     * @return {@code true} if all overloads failed to match, {@code false} otherwise.
     */
    private static boolean allOverloadsFailed(List<ToolOverload> tools) {
        for (ToolOverload tool: tools) {
            if (!tool.hasFailed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * A single overload of a tool. Used to track additional information while matching arguments against different
     * overloads.
     */
    private static class ToolOverload implements Comparable<ToolOverload> {
        /** The resolved reference to the tool. */
        public final ToolRef toolRef;

        /** The tool. */
        public final Tool tool;

        /** The return type of the tool, or {@code null} if not applicable. */
        public final ToolDefType returnType;

        /** The parameters of the tool. */
        public final List<ToolParameter> params;

        /** The 0-based index of the variadic parameter, or {@code -1} if the tool has no variadic parameter. */
        public final int variadicIdx;

        /** The 0-based index of the next positional argument. */
        public int posIdx = 0;

        /**
         * Per {@link #params parameter}, whether it has been matched by an argument. For variadic parameters, it
         * indicates whether at least one argument matches the parameter.
         */
        public boolean[] matched;

        /** The type constraints collected so far. */
        public TypeConstraints constraints = new TypeConstraints();

        /**
         * The types of the parameters of the tool. Is {@code null} until computed, after it has been decided that the
         * overload matches, and all {@link #constraints} have been collected. The constraints are used to fill in as
         * much type parameters as possible.
         */
        public List<ToolDefType> paramTypes = null;

        /**
         * Message indicating why the arguments don't fit this tool overload, or {@code null} while the already
         * processed arguments still fit.
         */
        public String failureMsg = null;

        /**
         * Constructor for the {@link ToolOverload} class.
         *
         * @param toolRef The resolved reference to the tool.
         */
        public ToolOverload(ToolRef toolRef) {
            // Get tool (reference).
            this.toolRef = toolRef;
            tool = toolRef.getTool();

            // Get return type, if applicable.
            if (tool.getReturnTypes().isEmpty()) {
                returnType = null;
            } else {
                returnType = makeTupleType(deepclone(tool.getReturnTypes()));
            }

            // Get parameters.
            params = tool.getParameters();

            // Get variadic parameter index, if applicable.
            int vi = -1;
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i).isVariadic()) {
                    vi = i;
                    break;
                }
            }
            variadicIdx = vi;

            // No parameter matched yet.
            matched = new boolean[params.size()];
        }

        /**
         * Has this tool overload previously failed to match an argument?
         *
         * @return {@code true} if this tool overload previously failed to match an argument, {@code false} otherwise.
         */
        public boolean hasFailed() {
            return failureMsg != null;
        }

        /** Check that the tool has a return type. */
        public void checkHasReturnType() {
            // Skip this tool overload if it already failed previously.
            if (hasFailed()) {
                return;
            }

            // Check for missing return type(s).
            if (returnType == null) {
                failureMsg = "no return types present";
            }
        }

        /**
         * Try to match the given tool argument against this tool overload, not taking into account the types, but only
         * the names/positions and duplicate use.
         *
         * @param arg The tool argument. The {@link ToolParameter#getValue() value} of the argument is not used.
         * @return The tool parameter of this tool overload matching the given tool argument, or {@code null} if this or
         *     a previous argument didn't match.
         */
        public ToolParameter matchNextArgument(ToolArgument arg) {
            // Skip this tool overload if it already failed previously.
            if (hasFailed()) {
                return null;
            }

            // Check whether this argument can be matched.
            String argName = arg.getName();
            if (argName == null) {
                // Make sure positional argument exists.
                if (posIdx >= params.size() && variadicIdx == -1) {
                    failureMsg = fmt("no %s parameter found", Numbers.toOrdinal(posIdx + 1));
                    return null;
                }

                // Found a match.
                ToolParameter param = params.get(posIdx);
                matched[posIdx] = true;
                posIdx++;
                if (variadicIdx != -1 && posIdx == variadicIdx + 1) {
                    posIdx--;
                }
                return param;
            } else {
                // Look for matching named argument.
                for (int i = 0; i < params.size(); i++) {
                    ToolParameter param = params.get(i);

                    // Skip parameters with wrong name.
                    if (!param.getName().equals(argName)) {
                        continue;
                    }

                    // If found but already used as positional argument, can't
                    // use it a second time. Also, parameter names are unique
                    // so won't find a second match.
                    if (matched[i]) {
                        failureMsg = fmt("a positional and a named argument both match parameter \"%s\"", argName);
                        return null;
                    }

                    // Found a match.
                    matched[i] = true;
                    return param;
                }

                // No match found.
                failureMsg = fmt("no \"%s\" parameter found", argName);
                return null;
            }
        }

        /**
         * Computes the type hint for the given tool argument matching a given tool parameter. Type parameters are all
         * filled in, either with the current {@link #constraints} that are available, or with '{@code object?}'
         * otherwise. No type parameters from this tool overload are present in the result.
         *
         * @param arg The tool argument.
         * @param param The tool parameter.
         * @return The type hint.
         */
        public ToolDefType computeArgHint(ToolArgument arg, ToolParameter param) {
            ToolDefType paramType = getType(arg, param);
            return substitute(paramType, constraints, true);
        }

        /**
         * Returns the expected type for a given tool argument matching a given tool parameter. Type parameters are not
         * filled in.
         *
         * @param arg The tool argument.
         * @param param The tool parameter.
         * @return The expected type.
         */
        public static ToolDefType getType(ToolArgument arg, ToolParameter param) {
            // Get parameter type.
            ToolDefType type = param.getType();

            // For positional arguments matching variadic parameters, use the
            // element type, not the list type.
            if (param.isVariadic() && arg.getName() == null) {
                Assert.check(type instanceof ListType);
                type = ((ListType)type).getElemType();
            }

            // Return the expected type of the argument.
            return type;
        }

        /**
         * Try to match the given tool argument against the given parameter of this tool overload, with respect to their
         * types.
         *
         * @param arg The tool argument. Must be fully type checked.
         * @param param The tool parameter of this tool, to which the tool argument was previously matched by the
         *     {@link #matchNextArgument} method.
         */
        public void checkTypeMatch(ToolArgument arg, ToolParameter param) {
            Assert.check(!hasFailed());

            // Check for type match.
            ToolDefType argType = arg.getValue().getType();
            ToolDefType paramType = getType(arg, param);
            if (computeSubType(argType, paramType, constraints)) {
                return;
            }

            // Doesn't match.
            failureMsg = fmt("type \"%s\" doesn't fit parameter \"%s\"", ToolDefTextUtils.typeToStr(argType),
                    param.getName());
        }

        /** Check that all mandatory parameters of the tool overload been matched. */
        public void checkComplete() {
            // Skip this tool overload if it already failed previously.
            if (hasFailed()) {
                return;
            }

            // Check completeness of mandatory parameters.
            for (int i = 0; i < params.size(); i++) {
                // Skip variadic and optional parameters.
                ToolParameter param = params.get(i);
                if (param.isVariadic()) {
                    continue;
                }
                if (param.getValue() != null) {
                    continue;
                }

                // Check whether matched against argument.
                if (!matched[i]) {
                    failureMsg = fmt("no argument for mandatory parameter \"%s\"", param.getName());
                    return;
                }
            }
        }

        /** Computes and sets {@link #paramTypes}. */
        public void computeParamTypes() {
            // Precondition checking.
            Assert.check(!hasFailed());
            Assert.check(paramTypes == null);

            // Compute parameter types. Fill in as much type parameters as
            // possible. No need to take variadic parameters (and their
            // wrapping list type) into account here.
            paramTypes = listc(params.size());
            for (ToolParameter param: params) {
                ToolDefType paramType = param.getType();
                paramTypes.add(substitute(paramType, constraints, false));
            }
        }

        /**
         * Computes the return type of this tool, for the concrete arguments that were used.
         *
         * @return The return type of his tool, for the concrete arguments that were used.
         */
        public ToolDefType computeReturnType() {
            // Fill in all type parameters.
            Assert.notNull(returnType);
            return substitute(returnType, constraints, true);
        }

        @Override
        @SuppressWarnings("null")
        public int compareTo(ToolOverload other) {
            int cnt = Math.max(this.params.size(), other.params.size());
            for (int i = 0; i < cnt; i++) {
                // Check exists vs doesn't exist.
                ToolParameter thisParam = null;
                ToolParameter otherParam = null;
                if (i < this.params.size()) {
                    thisParam = this.params.get(i);
                }
                if (i < other.params.size()) {
                    otherParam = other.params.get(i);
                }
                if (thisParam == null && otherParam != null) {
                    return -1;
                }
                if (thisParam != null && otherParam == null) {
                    return 1;
                }

                // Both exist. Check types. Requires that the parameter types
                // have been computed, with type parameters filled in as much
                // as possible.
                Assert.notNull(thisParam);
                Assert.notNull(otherParam);
                ToolDefType thisType = this.paramTypes.get(i);
                ToolDefType otherType = other.paramTypes.get(i);
                int rslt = ToolDefTypeUtils.compareTypes(thisType, otherType);
                if (rslt != 0) {
                    return rslt;
                }

                // Check variadics.
                if (!thisParam.isVariadic() && otherParam.isVariadic()) {
                    return -1;
                }
                if (thisParam.isVariadic() && !otherParam.isVariadic()) {
                    return 1;
                }
            }
            return 0;
        }
    }
}
