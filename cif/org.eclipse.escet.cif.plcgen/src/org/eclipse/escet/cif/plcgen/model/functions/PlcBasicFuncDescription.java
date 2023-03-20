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

package org.eclipse.escet.cif.plcgen.model.functions;

import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.java.Assert;

/**
 * Base class describing the parameters of a function application, as well as providing information to convert it to
 * text.
 */
public abstract class PlcBasicFuncDescription {
    /** Name of the function in prefix notation, or {@code null} if the prefix form does not exist. */
    public final String prefixFuncName;

    /** Mapping of names of the function parameters to their prefix notation properties. */
    public final Map<String, PlcParameterDescription> prefixParameters;

    /** Name of the function in infix notation, {@code null} if infix form does not exist. */
    public final String infixFuncName;

    /**
     * Binding of the function application for laying out the infix notation. Use {@link ExprBinding#NO_PRIORITY} for
     * functions that have no infix notation.
     */
    public final ExprBinding infixBinding;

    /**
     * Constructor of the {@link PlcBasicFuncDescription} class.
     *
     * @param prefixFuncName Name of the function in prefix notation, or {@code null} if the prefix form does not exist.
     * @param prefixParameters Prefix notation properties of the function parameters.
     * @param infixFuncName Name of the function in infix notation, {@code null} if infix form does not exist.
     * @param infixBinding Binding of the function application for laying out the infix notation. Use
     *     {@link ExprBinding#NO_PRIORITY} for functions that have no infix notation.
     */
    public PlcBasicFuncDescription(String prefixFuncName, PlcParameterDescription[] prefixParameters,
            String infixFuncName, ExprBinding infixBinding)
    {
        this(prefixFuncName, Arrays.asList(prefixParameters), infixFuncName, infixBinding);
    }

    /**
     * Constructor of the {@link PlcBasicFuncDescription} class.
     *
     * @param prefixFuncName Name of the function in prefix notation, or {@code null} if the prefix form does not exist.
     * @param prefixParameters Prefix notation properties of the function parameters.
     * @param infixFuncName Name of the function in infix notation, {@code null} if infix form does not exist.
     * @param infixBinding Binding of the function application for laying out the infix notation. Use
     *     {@link ExprBinding#NO_PRIORITY} for functions that have no infix notation.
     */
    public PlcBasicFuncDescription(String prefixFuncName, List<PlcParameterDescription> prefixParameters,
            String infixFuncName, ExprBinding infixBinding)
    {
        this.prefixFuncName = prefixFuncName;
        this.infixFuncName = infixFuncName;
        this.infixBinding = infixBinding;

        this.prefixParameters = mapc(prefixParameters.size());
        for (PlcParameterDescription prefixParam: prefixParameters) {
            this.prefixParameters.put(prefixParam.name, prefixParam);
        }
        Assert.areEqual(this.prefixParameters.size(), prefixParameters.size());
    }

    /** Operator priority and associativity of an expression node. */
    public static enum ExprBinding {
        /** Unary expression binding. */
        UNARY_EXPR(1, ExprAssociativity.RIGHT),

        /** Exponentiation expression binding. */
        EXPT_EXPR(2, ExprAssociativity.ALWAYS),

        /** Multiplicative expression binding. */
        MUL_EXPR(3, ExprAssociativity.LEFT),

        /** Additive expression binding. */
        ADD_EXPR(4, ExprAssociativity.LEFT),

        /** Ordering comparisons binding. */
        ORDER_EXPR(5, ExprAssociativity.ALWAYS),

        /** Equality comparisons binding. */
        EQUAL_EXPR(6, ExprAssociativity.ALWAYS),

        /** Conjunction binding. */
        CONJUNCT_EXPR(7, ExprAssociativity.LEFT),

        /** Exclusive disjunction binding. */
        EXCL_DISJUNCT_EXPR(8, ExprAssociativity.LEFT),

        /** Disjunction binding. */
        DISJUNCT_EXPR(9, ExprAssociativity.LEFT),

        /** Bottom-most level, never binds with anything. */
        NO_PRIORITY(Integer.MAX_VALUE, ExprAssociativity.NONE);

        /** Priority level of the expression, smaller value binds stronger. */
        public final int priority;

        /** Associativity of an expression. */
        public final ExprAssociativity associativity;

        /**
         * Constructor of the {@link ExprBinding} class.
         *
         * @param priority Priority level of the expression, smaller value binds stronger.
         * @param associativity Associativity of an expression.
         */
        private ExprBinding(int priority, ExprAssociativity associativity) {
            this.priority = priority;
            this.associativity = associativity;
        }

        /**
         * Decide whether a child expression should add parentheses around its textual representation to ensure its
         * proper evaluation order in the parent context.
         *
         * @param parentBinding Binding of the parent.
         * @param atLeft Whether the child expression is at the far left side of the parent.
         * @param atRight Whether the child expression is at the far right side of the parent.
         * @return Whether the child should protect its evaluation order in the parent context by surrounding its
         *     textual output with parentheses.
         */
        public boolean needsParentheses(ExprBinding parentBinding, boolean atLeft, boolean atRight) {
            if (parentBinding.priority != priority) {
                return parentBinding.priority < priority;
            }
            return parentBinding.associativity.needsParentheses(atLeft, atRight);
        }

        /**
         * Decide whether a child expression should add parentheses around its textual representation to ensure its
         * proper evaluation order in the parent context while ignoring associativity of the parent.
         *
         * @param parentBinding Binding of the parent.
         * @return Whether the child should protect its evaluation order in the parent context by surrounding its
         *     textual output with parentheses.
         */
        public boolean needsParentheses(ExprBinding parentBinding) {
            return needsParentheses(parentBinding, false, false);
        }
    }

    /** Associativity of an expression, used to decide when priority values are equal. */
    public static enum ExprAssociativity {
        /** Expression is left associative, that is, {@code ((a OP b) OP c)} is the natural binding. */
        LEFT,

        /** Expression is right associative, that is, {@code (a OP (b OP c))} is the natural binding. */
        RIGHT,

        /** Expression has no associativity, never use parentheses at equal priority. */
        NONE,

        /** Expression has no associativity, always use parentheses at equal priority. */
        ALWAYS;

        /**
         * Are parentheses around an expression required in the given situation?
         *
         * @param atLeft Whether the expression is at the far left side.
         * @param atRight Whether the expression is at the far right side.
         * @return Whether parentheses are needed.
         */
        boolean needsParentheses(boolean atLeft, boolean atRight) {
            return (this == ALWAYS) || (this == LEFT && !atLeft) || (this == RIGHT && !atRight);
        }
    }

    /** Properties of a function parameter. */
    public static class PlcParameterDescription {
        /** Name of the parameter. */
        public final String name;

        /** Communication direction of the parameter. */
        public final PlcParamDirection direction;

        /**
         * Constructor of the {@link PlcParameterDescription} class.
         *
         * @param name Name of the parameter.
         * @param direction Communication direction of the parameter.
         */
        public PlcParameterDescription(String name, PlcParamDirection direction) {
            this.name = name;
            this.direction = direction;
        }
    }

    /** Direction of communication of the parameter as seen from the callee. */
    public static enum PlcParamDirection {
        /** Parameter can be written only by the callee. */
        OUTPUT_ONLY,

        /** Parameter can be both read and written. */
        INPUT_OUTPUT,

        /** Parameter is read only for the callee. */
        INPUT_ONLY;
    }
}