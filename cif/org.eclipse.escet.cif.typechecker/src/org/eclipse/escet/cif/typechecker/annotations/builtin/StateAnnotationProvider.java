//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.annotations.builtin;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProblemReporter;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProvider;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/**
 * Annotation provider for "state" annotations.
 *
 * <p>
 * A "state" annotation adds state information to a location in an automaton:
 * <ul>
 * <li>A single location may have multiple state annotations, if that location represents multiple states of a state
 * space.</li>
 * <li>Each argument of a state annotation represents either an automaton with its current location, or a variable with
 * its current value.</li>
 * <li>There can be any number of arguments (including no arguments), as models may have any number of automata and
 * variables.</li>
 * <li>The name of the argument should be the absolute name of the corresponding automaton or variable. It is checked
 * that arguments have a name. It is not checked that the names correspond to automata or variables, as such automata
 * and variables typically do not exist in the current specification.</li>
 * <li>Argument values, and parts of argument values (in case of containers), must be literals of type {@code bool},
 * {@code int}, {@code real}, {@code string}, {@code tuple}, {@code list}, {@code set}, or {@code dict}. Hence, their
 * values must be represented exactly as the corresponding literal values are represented by the type checker. This
 * implies that the values must be statically evaluable, and that evaluating an argument value must not result in an
 * evaluation error.</li>
 * <li>For automata, the value of the argument should be a string literal with the name of the location, or {@code "*"}
 * for a nameless location. If the name is a keyword, it should not be escaped. This constraint is not checked, as such
 * string literals can not be distinguished from 'regular' string literals.</li>
 * <li>For enumeration literal values, a string literal with the non-escaped name of the literal should be used. If the
 * name is a keyword, it should not be escaped. This constraint is not checked, as such string literals can not be
 * distinguished from 'regular' string literals.</li>
 * <li>For functions used as values, a string literal with the non-escaped absolute name of the function should be used.
 * For functions that represent a default initial value of a variable with a function type, {@code "*"} should be used
 * as function name. Keywords in the absolute names should not be escaped. This constraint is not checked, as such
 * string literals can not be distinguished from 'regular' string literals.</li>
 * <li>If at least one location in an automaton has at least one state annotation, all other locations in that same
 * automaton must also have at least one state annotation, as all these locations should then represent a least one
 * state from the state space.</li>
 * <li>Different state annotations on the same or different locations of a single automaton must have the same number
 * of arguments, the arguments must have the same names, and the values of arguments with matching names must have
 * compatible types (ignoring ranges), as they should represent states from the same state space.</li>
 * </ul>
 * </p>
 */
public class StateAnnotationProvider extends AnnotationProvider {
    /**
     * Constructor for the {@link StateAnnotationProvider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public StateAnnotationProvider(String annotationName) {
        super(annotationName);
    }

    @Override
    public final void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // State annotations only on locations.
        if (!(annotatedObject instanceof Location)) {
            reporter.reportProblem(annotation, "state annotation on a non-location object.", annotation.getPosition(),
                    SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
            return;
        }

        // Check the arguments.
        for (AnnotationArgument arg: annotation.getArguments()) {
            // Check argument name.
            if (arg.getName() == null) {
                reporter.reportProblem(annotation, "unsupported unnamed argument.", arg.getPosition(),
                        SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
                continue;
            }

            // Check argument value.
            checkSupportedLiteral(annotation, arg, arg.getValue(), reporter);
        }
    }

    /**
     * Check an argument value, or a part of it, for being a literal value of a supported type.
     *
     * @param annotation The annotation to check.
     * @param arg The annotation argument.
     * @param value The value, or part of a value.
     * @param reporter The reporter to use to report problems in the annotation.
     */
    private void checkSupportedLiteral(Annotation annotation, AnnotationArgument arg, Expression value,
            AnnotationProblemReporter reporter)
    {
        // Get normalized type.
        CifType type = CifTypeUtils.normalizeType(value.getType());

        // Check based on the type.
        if (type instanceof BoolType) {
            // Must be a boolean literal.
            if (!(value instanceof BoolExpression)) {
                reportNonLiteral(annotation, arg, value, reporter);
            }
        } else if (type instanceof IntType) {
            // Must be an integer literal. However, integer literals only allow positive values, so negative integer
            // literals are represented as a unary negation of a positive integer literal. Furthermore, '-2,147,483,648'
            // is represented as '-2,147,483,647 - 1'. To easy the check, check that the value evaluates to itself.
            if (!evaluatesToItself(value)) {
                reportNonLiteral(annotation, arg, value, reporter);
            }
        } else if (type instanceof RealType) {
            // Must be a real literal. However, real literals only allow positive values, so negative real literals
            // are represented as a unary negation of a positive real literal. To easy the check, check that the value
            // evaluates to itself.
            if (!evaluatesToItself(value)) {
                reportNonLiteral(annotation, arg, value, reporter);
            }
        } else if (type instanceof StringType) {
            // Must be a string literal.
            if (!(value instanceof StringExpression)) {
                reportNonLiteral(annotation, arg, value, reporter);
            }
        } else if (type instanceof TupleType) {
            // Must be a tuple literal.
            if (!(value instanceof TupleExpression)) {
                reportNonLiteral(annotation, arg, value, reporter);
                return;
            }

            // Check recursively.
            TupleExpression tvalue = (TupleExpression)value;
            for (Expression field: tvalue.getFields()) {
                checkSupportedLiteral(annotation, arg, field, reporter);
            }
        } else if (type instanceof ListType) {
            // An empty list literal can have a cast expression wrapped around it.
            if (value instanceof CastExpression cexpr) {
                value = cexpr.getChild();
            }

            // Must be a list literal.
            if (!(value instanceof ListExpression)) {
                reportNonLiteral(annotation, arg, value, reporter);
                return;
            }

            // Check recursively.
            ListExpression lvalue = (ListExpression)value;
            for (Expression element: lvalue.getElements()) {
                checkSupportedLiteral(annotation, arg, element, reporter);
            }
        } else if (type instanceof SetType) {
            // An empty set literal can have a cast expression wrapped around it.
            if (value instanceof CastExpression cexpr) {
                value = cexpr.getChild();
            }

            // Must be a set literal.
            if (!(value instanceof SetExpression)) {
                reportNonLiteral(annotation, arg, value, reporter);
                return;
            }

            // Check recursively.
            SetExpression svalue = (SetExpression)value;
            for (Expression element: svalue.getElements()) {
                checkSupportedLiteral(annotation, arg, element, reporter);
            }
        } else if (type instanceof DictType) {
            // An empty dictionary literal can have a cast expression wrapped around it.
            if (value instanceof CastExpression cexpr) {
                value = cexpr.getChild();
            }

            // Must be a dictionary literal.
            if (!(value instanceof DictExpression)) {
                reportNonLiteral(annotation, arg, value, reporter);
                return;
            }

            // Check recursively.
            DictExpression dvalue = (DictExpression)value;
            for (DictPair pair: dvalue.getPairs()) {
                checkSupportedLiteral(annotation, arg, pair.getKey(), reporter);
                checkSupportedLiteral(annotation, arg, pair.getValue(), reporter);
            }
        } else {
            // Unsupported type.
            reporter.reportProblem(
                    annotation, fmt("unsupported value of type \"%s\" in argument \"%s\".",
                            CifTextUtils.typeToStr(value.getType()), arg.getName()),
                    value.getPosition(), SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
        }
    }

    /**
     * Check whether an expression evaluates to itself.
     *
     * @param expr The expression to check. Must be either an integer-typed or real-typed expression.
     * @return {@code true} if the expression evaluates to itself, or {@code false} if it either does not evaluate to
     *     itself or it could not be determined whether it evaluates to itself as it is not a statically evaluable
     *     literal.
     */
    private boolean evaluatesToItself(Expression expr) {
        // Check expression for being statically evaluable.
        if (!CifValueUtils.hasSingleValue(expr, false, true)) {
            return false;
        }

        // Evaluate the expression.
        Expression evaluationResult;
        try {
            evaluationResult = CifEvalUtils.evalAsExpr(expr, false);
        } catch (CifEvalException e) {
            return false; // Evaluating literals does not result in an evaluation error.
        }

        // Check that the expression evaluates to itself.
        return CifValueUtils.areStructurallySameExpression(expr, evaluationResult);
    }

    /**
     * Report a non-literal value in an annotation argument value.
     *
     * @param annotation The annotation.
     * @param arg The annotation argument.
     * @param value The value, or part of a value.
     * @param reporter The reporter to use to report the problem.
     */
    private void reportNonLiteral(Annotation annotation, AnnotationArgument arg, Expression value,
            AnnotationProblemReporter reporter)
    {
        reporter.reportProblem(annotation, fmt("non-literal value in argument \"%s\".", arg.getName()),
                value.getPosition(), SemanticProblemSeverity.ERROR);
        // Non-fatal problem.
    }

    @Override
    public final void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Check the following constraints:
        // - Different state annotations on the same or different locations of a single automaton must have the same
        //   arguments with the same names, and the values of matching arguments must have compatible types (ignoring
        //   ranges), as they should represent states from the same state space. Check this per automaton.
        //   Note that unnamed arguments are reported elsewhere, and are therefore ignored for this check.
        Map<String, AnnotationArgument> argNameToAnnoArg = map();
        for (Automaton aut: CifCollectUtils.collectAutomata(spec, list())) {
            // Get a mapping with per argument name the first-encountered argument with that name.
            boolean stateAnnoPresent = false;
            argNameToAnnoArg.clear();
            for (Location loc: aut.getLocations()) {
                for (Annotation anno: loc.getAnnotations()) {
                    if (anno.getName().equals("state")) {
                        stateAnnoPresent = true;
                        for (AnnotationArgument arg: anno.getArguments()) {
                            if (arg.getName() != null) {
                                argNameToAnnoArg.putIfAbsent(arg.getName(), arg);
                            }
                        }
                    }
                }
            }

            // If there are no state annotations in this automaton, there is nothing further to check.
            // We use a boolean variable, rather than the mapping, as there could be state annotations without
            // arguments.
            if (!stateAnnoPresent) {
                continue;
            }

            // Check all state annotations of the locations.
            for (Location loc: aut.getLocations()) {
                boolean locHasStateAnno = false;
                for (Annotation anno: loc.getAnnotations()) {
                    // Skip non-state annotations.
                    if (!(anno.getName().equals("state"))) {
                        continue;
                    }

                    // Location has a state annotation.
                    locHasStateAnno = true;

                    // Check missing named arguments.
                    List<String> annoArgNames = anno.getArguments().stream().filter(a -> a.getName() != null)
                            .map(a -> a.getName()).toList();
                    Set<String> missingArgNames = Sets.difference(argNameToAnnoArg.keySet(), annoArgNames);
                    if (!missingArgNames.isEmpty()) {
                        String text = missingArgNames.stream().map(n -> fmt("\"%s\"", n))
                                .collect(Collectors.joining(", "));
                        reporter.reportProblem(anno,
                                fmt("compared to other state annotations in the same automaton, "
                                        + "one or more arguments are missing: %s.", text),
                                anno.getPosition(), SemanticProblemSeverity.ERROR);
                        // Non-fatal problem.
                    }

                    // Check against the mapping for incompatible types.
                    for (AnnotationArgument arg: anno.getArguments()) {
                        if (arg.getName() == null) {
                            continue; // Ignore unnamed arguments. They are reported elsewhere.
                        }
                        AnnotationArgument otherArg = argNameToAnnoArg.get(arg.getName());
                        if (arg == otherArg) {
                            continue;
                        }
                        CifType argType = arg.getValue().getType();
                        CifType otherType = otherArg.getValue().getType();
                        if (!CifTypeUtils.checkTypeCompat(argType, otherType, RangeCompat.IGNORE)) {
                            // Incompatible types.
                            reporter.reportProblem(anno, fmt(
                                    "the \"%s\" type of the value of argument \"%s\" is incompatible with "
                                            + "the \"%s\" type of the value of the same argument "
                                            + "of another state annotation in the same automaton.",
                                    CifTextUtils.typeToStr(argType), arg.getName(), CifTextUtils.typeToStr(otherType)),
                                    arg.getPosition(), SemanticProblemSeverity.ERROR);
                            reporter.reportProblem(anno, fmt(
                                    "the \"%s\" type of the value of argument \"%s\" is incompatible with "
                                            + "the \"%s\" type of the value of the same argument "
                                            + "of another state annotation in the same automaton.",
                                    CifTextUtils.typeToStr(otherType), arg.getName(), CifTextUtils.typeToStr(argType)),
                                    otherArg.getPosition(), SemanticProblemSeverity.ERROR);
                            // Non-fatal problem.
                        }
                    }
                }

                // Ensure that location has a state annotation (as at least one location of the automaton has one).
                if (!locHasStateAnno) {
                    reporter.reportProblem("state",
                            fmt("%s must have a state annotation, as at least one other location in the same "
                                    + "automaton has a state annotation.", CifTextUtils.getLocationText2(loc)),
                            loc.getPosition(), SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                }
            }
        }
    }
}
