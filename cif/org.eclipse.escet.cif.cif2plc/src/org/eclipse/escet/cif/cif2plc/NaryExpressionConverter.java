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

package org.eclipse.escet.cif.cif2plc;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionImpl;

/** N-ary expression converter. */
public class NaryExpressionConverter {
    /** Constructor for the {@link NaryExpressionConverter} class. */
    private NaryExpressionConverter() {
        // Static class.
    }

    /**
     * Converter that can convert expressions to n-ary expressions. Only top-level 'and'/'or' expressions are
     * recursively collected into n-ary expressions. As long as 'and'/'or' expressions are found, the conversion
     * continues. As soon as a different kind of expression is found, the recursion ends. That is, 'and' in 'or' in
     * 'and' is converted, but an 'or' in a 'not' in an 'and' is not converted.
     *
     * <p>
     * For instance, consider the following expression:
     *
     * <pre>
     *    and
     *   /   \________
     *  and           \
     * /   \          or
     * a   or        /  \
     *    /  \       d  or
     *    b  c         /  \
     *                and  not(g or h)
     *               /   \
     *               e   f
     * </pre>
     *
     * It becomes:
     *
     * <pre>
     *  and
     * / | \___
     * a or    \
     *  /  \   or___
     *  b  c  / |   \
     *        d and  not(g or h)
     *         /   \
     *         e   f
     * </pre>
     * </p>
     *
     * @param expr The expression to convert.
     * @return The conversion result.
     */
    public static Expression convert(Expression expr) {
        // If not 'and'/'or', no conversion.
        if (!(expr instanceof BinaryExpression)) {
            return expr;
        }
        BinaryExpression bexpr = (BinaryExpression)expr;
        BinaryOperator op = bexpr.getOperator();
        if (op != BinaryOperator.CONJUNCTION && op != BinaryOperator.DISJUNCTION) {
            return expr;
        }

        // Convert children.
        Expression left = convert(bexpr.getLeft());
        Expression right = convert(bexpr.getRight());

        // Create result.
        NaryExpression rslt = new NaryExpression(op);

        if (left instanceof NaryExpression && ((NaryExpression)left).operator == op) {
            // Merge.
            rslt.children.addAll(((NaryExpression)left).children);
        } else {
            // Add.
            rslt.children.add(left);
        }

        if (right instanceof NaryExpression && ((NaryExpression)right).operator == op) {
            // Merge.
            rslt.children.addAll(((NaryExpression)right).children);
        } else {
            // Add.
            rslt.children.add(right);
        }

        return rslt;
    }

    /** N-ary CIF expression. */
    public static class NaryExpression extends ExpressionImpl {
        /** The child expressions. May be modified in-place. */
        public List<Expression> children = list();

        /** The operator of the n-ary expression. */
        public final BinaryOperator operator;

        /**
         * Constructor for the {@link NaryExpression} class.
         *
         * @param operator The operator of the n-ary expression.
         */
        public NaryExpression(BinaryOperator operator) {
            this.operator = operator;
        }
    }
}
