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

package org.eclipse.escet.cif.plcgen.model.functions;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.types.PlcAbstractType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcGenericType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.common.java.Assert;

/**
 * Base class describing the prefix function notation, and the parameters of such a function application. It also
 * contains allowed notation forms of the function application.
 */
public abstract class PlcBasicFuncDescription {
    /** The semantic operation performed by the function. */
    public final PlcFuncOperation operation;

    /**
     * Name of the function in prefix notation, the empty string if the function name should not be used, or
     * {@code null} if the prefix form does not exist.
     */
    public final String prefixFuncName;

    /** Parameters of the function. */
    public final PlcParameterDescription[] parameters;

    /** Notations of the function that are supported by the target. */
    public final EnumSet<PlcFuncNotation> notations;

    /** Type of the result of the function. */
    public final PlcAbstractType resultType;

    /** Condition for appending a {@code _TYPE} extension to a prefix function name. */
    public final PlcFuncTypeExtension typeExtension;

    /**
     * Constructor of the {@link PlcBasicFuncDescription} class.
     *
     * @param operation The semantic operation performed by the function.
     * @param prefixFuncName Name of the function in prefix notation, the empty string if the function name should not
     *     be used, or {@code null} if the prefix form does not exist.
     * @param parameters Parameters of the function.
     * @param notations Notations of the function that are supported by the target. May get restricted based on
     *     available infix and prefix function names.
     * @param resultType Type of the result of the function.
     * @param typeExtension Condition for appending a {@code _TYPE} extension to a prefix function name.
     */
    public PlcBasicFuncDescription(PlcFuncOperation operation, String prefixFuncName,
            PlcParameterDescription[] parameters, EnumSet<PlcFuncNotation> notations, PlcAbstractType resultType,
            PlcFuncTypeExtension typeExtension)
    {
        Assert.check(!notations.isEmpty());

        // Verify that parameter names are unique.
        long numUnique = Arrays.stream(parameters).map(param -> param.name).distinct().count();
        Assert.areEqual(Math.toIntExact(numUnique), parameters.length);

        this.operation = operation;
        this.prefixFuncName = prefixFuncName;
        this.parameters = parameters;
        this.notations = notations;
        this.resultType = resultType;
        this.typeExtension = typeExtension;
    }

    /**
     * Get a function name description.
     *
     * @return A description of the function name.
     */
    public String getFuncName() {
        if (prefixFuncName == null) {
            return "prefix-N/A";
        } else if (prefixFuncName.isEmpty()) {
            return "prefix-dont_use";
        } else {
            return "prefix-\"" + prefixFuncName + "\"";
        }
    }

    /**
     * Compute the result type of a function application with the given arguments.
     *
     * @param argumentList Arguments of the function call to examine.
     * @return The result type of that function call.
     */
    public PlcType deriveResultType(List<PlcNamedValue> argumentList) {
        // Make arguments available by name.
        Map<String, PlcNamedValue> arguments = mapc(argumentList.size());
        for (PlcNamedValue arg: argumentList) {
            arguments.put(arg.name, arg);
        }

        // Verify there is at least one argument and no duplicates.
        Assert.check(!argumentList.isEmpty()); // PLCs don't support calls without arguments.
        Assert.areEqual(arguments.size(), argumentList.size());

        // All supplied arguments should have a matching parameter.
        long paramMatches = Arrays.stream(parameters).filter(arg -> arguments.containsKey(arg.name)).count();
        Assert.areEqual(Math.toIntExact(paramMatches), argumentList.size());

        // Verify argument types against parameter types. Generic types get resolved as well.
        Map<PlcGenericType, PlcType> genericTypeMap = map(); // Mapping of generic types to their concrete type.

        // Check the argument types while resolving generic parameter types.
        for (PlcParameterDescription paramDesc: parameters) {
            PlcNamedValue argument = arguments.get(paramDesc.name);
            if (argument == null) {
                // Argument is not supplied, ignore the parameter.
                continue;
            }

            if (paramDesc.type instanceof PlcType) {
                // Concrete types must match.
                Assert.areEqual(paramDesc.type, argument.value.type,
                        fmt("Parameter type %s does not match argument type %s for argument \"%s\".",
                                paramDesc.type, argument.value.type, paramDesc.name));

            } else if (paramDesc.type instanceof PlcGenericType genericType) {
                // Check that the concrete type is allowed in the generic type.
                Assert.check(genericType.checkMatch(argument.value.type),
                        fmt("Concrete type %s does not fit in generic type %s.", argument.value.type, genericType));

                // The same generic type must be replaced by the same concrete type everywhere in the call.
                PlcType mappedType = genericTypeMap.computeIfAbsent(genericType, t -> argument.value.type);
                Assert.areEqual(mappedType, argument.value.type);
            } else {
                throw new AssertionError("Unexpected parameter type found: " + paramDesc.type);
            }
        }

        // Derive the result type and check it exists.
        PlcType concreteResultType;
        if (resultType instanceof PlcGenericType genericType) {
            concreteResultType = genericTypeMap.get(genericType);
        } else {
            concreteResultType = (PlcType)resultType;
        }
        Assert.notNull(concreteResultType);
        return concreteResultType;
    }

    /** Operator priority and associativity of an expression node. */
    public static enum ExprBinding {
        /** Unary expression binding. */
        UNARY_EXPR(1, ExprAssociativity.RIGHT),

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

        /** Type of the parameter. */
        public final PlcAbstractType type;

        /**
         * Constructor of the {@link PlcParameterDescription} class.
         *
         * @param name Name of the parameter.
         * @param direction Communication direction of the parameter.
         * @param type Type of the parameter.
         */
        public PlcParameterDescription(String name, PlcParamDirection direction, PlcAbstractType type) {
            this.name = name;
            this.direction = direction;
            this.type = type;
        }

        @Override
        public String toString() {
            return fmt("PlcParameterDescription(\"%s\", %s)", name, type);
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

    /** Available notations of a function application. */
    public static enum PlcFuncNotation {
        /** Infix notation. */
        INFIX,

        /** Informal prefix notation. */
        INFORMAL,

        /** Formal prefix notation. */
        FORMAL;

        /** Unsupported function. */
        public static final EnumSet<PlcFuncNotation> UNSUPPORTED = EnumSet.noneOf(PlcFuncNotation.class);

        /** All infix notation forms. */
        public static final EnumSet<PlcFuncNotation> INFIX_ONLY = EnumSet.of(INFIX);

        /** All informal prefix notation forms. */
        public static final EnumSet<PlcFuncNotation> INFORMAL_ONLY = EnumSet.of(INFORMAL);

        /** All formal prefix notation forms. */
        public static final EnumSet<PlcFuncNotation> FORMAL_ONLY = EnumSet.of(FORMAL);

        /** All except infix notation forms. */
        public static final EnumSet<PlcFuncNotation> NOT_INFIX = EnumSet.of(INFORMAL, FORMAL);

        /** All except informal prefix notation forms. */
        public static final EnumSet<PlcFuncNotation> NOT_INFORMAL = EnumSet.of(INFIX, FORMAL);

        /** All except formal prefix notation forms. */
        public static final EnumSet<PlcFuncNotation> NOT_FORMAL = EnumSet.of(INFIX, INFORMAL);

        /** All notation forms. */
        public static final EnumSet<PlcFuncNotation> ALL = EnumSet.allOf(PlcFuncNotation.class);
    }

    /** Available conditions for appending a {@code _TYPE} extension to a prefix function name. */
    public static enum PlcFuncTypeExtension {
        /** Never extend the function name. */
        NEVER(t -> false),

        /** Extend the function when the result type is an elementary type that is not {@code BOOL}. */
        ELEMENTARY_NOT_BOOL(t -> t instanceof PlcElementaryType && t != PlcElementaryType.BOOL_TYPE);

        /** Predicate function that decides if a type extension is needed for the given type. */
        public final Predicate<PlcType> testFunction;

        /**
         * Constructor of the {@link PlcFuncTypeExtension} enumeration.
         *
         * @param testFunction Predicate function that decides if a type extension is needed for the given type.
         */
        private PlcFuncTypeExtension(Predicate<PlcType> testFunction) {
            this.testFunction = testFunction;
        }
    }
}
