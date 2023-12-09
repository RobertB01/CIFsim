//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
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
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/**
 * Annotation provider for "state" annotations.
 *
 * <p>
 * A "state" annotation adds state information to a location in an automaton that represents a state space of another
 * model:
 * <ul>
 * <li>Each argument represents an automaton with its current location, or a variable with its current value.</li>
 * <li>There can be any number of arguments (including no arguments), as models may have any number of automata and
 * variables.</li>
 * <li>The name of the argument should be the absolute name of the corresponding automaton or variable. This constraint
 * is not checked, as such variables and automata typically do not exist in the current specification.</li>
 * <li>Argument values, or parts of argument values (in case of containers), must be literals of type {@code bool},
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
        }

        // Check the arguments.
        for (AnnotationArgument arg: annotation.getArguments()) {
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
        } else if (type instanceof TupleType ttype) {
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
        } else if (type instanceof ListType ltype) {
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
        } else if (type instanceof SetType stype) {
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
        } else if (type instanceof DictType dtype) {
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
        // Do nothing.
    }
}
