//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.updates;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;

/**
 * Wrapper for CIF declarations, with additions for storing the 'time' expression, and differentiate between continuous
 * variable and its derivative expression.
 */
public class VariableWrapper {
    /** Wrapped declaration, {@code null} means 'time'. */
    public final Declaration decl;

    /** For continuous variables, is this object the derivative? */
    public final boolean isDerivative;

    /**
     * Constructor for {@link VariableWrapper} class.
     *
     * @param decl Wrapped declaration, {@code null} means 'time'.
     * @param isDerivative For continuous variables, is this object the derivative?
     */
    public VariableWrapper(Declaration decl, boolean isDerivative) {
        this.decl = decl;
        this.isDerivative = (decl instanceof ContVariable) && isDerivative;
    }

    /**
     * Does this wrapped variable represent an algebraic variable?
     *
     * @return {@code true} if the variable represents an algebraic variable, else {@code false}.
     */
    public boolean isAlgebraic() {
        return decl instanceof AlgVariable;
    }

    /**
     * Does this wrapped variable represent a derivative?
     *
     * @return {@code true} if the variable represents a derivative, else {@code false}.
     */
    public boolean isDerivative() {
        // instanceof check is not really needed, as only continuous variables
        // may have a true isDerivative.
        return isDerivative && decl instanceof ContVariable;
    }

    @Override
    public int hashCode() {
        if (decl == null) {
            return 381;
        }

        // 'isDerivative ' forced to 0 for non-continuous declarations.
        return decl.hashCode() + (isDerivative ? 1 : 0);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof VariableWrapper)) {
            return false;
        }
        VariableWrapper otherVw = (VariableWrapper)other;
        if (decl == null || otherVw.decl == null) {
            return decl == null && otherVw.decl == null;
        }

        return decl == otherVw.decl && isDerivative == otherVw.isDerivative;
    }

    /**
     * Construct a wrapped variable for time, discrete, algebraic, and continuous variable expressions.
     *
     * @param expr Expression to convert.
     * @return The created wrapped variable.
     */
    public static VariableWrapper makeVariableWrapper(Expression expr) {
        if (expr instanceof DiscVariableExpression) {
            DiscVariableExpression varExpr = (DiscVariableExpression)expr;
            return new VariableWrapper(varExpr.getVariable(), false);
        }

        if (expr instanceof AlgVariableExpression) {
            AlgVariableExpression varExpr = (AlgVariableExpression)expr;
            return new VariableWrapper(varExpr.getVariable(), false);
        }

        if (expr instanceof ContVariableExpression) {
            ContVariableExpression varExpr = (ContVariableExpression)expr;
            return new VariableWrapper(varExpr.getVariable(), varExpr.isDerivative());
        }

        if (expr instanceof TimeExpression) {
            return new VariableWrapper(null, false);
        }

        throw new RuntimeException("Unexpected variable expression node " + str(expr));
    }

    @Override
    public String toString() {
        if (decl instanceof ContVariable) {
            return fmt("Wrapped(\"%s\", %s)", decl.getName(), isDerivative);
        } else {
            return fmt("Wrapped(\"%s\")", decl.getName());
        }
    }
}
