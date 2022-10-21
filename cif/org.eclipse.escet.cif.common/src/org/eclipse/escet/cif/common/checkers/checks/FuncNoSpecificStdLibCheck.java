//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks;

import static org.eclipse.escet.cif.common.CifTextUtils.functionToStr;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.common.java.Assert;

/**
 * CIF check that disallows usage of a specified collection of standard library functions.
 *
 * <p>
 * To disallow all standard library functions, use {@link ExprNoSpecificExprsCheck} with {@link NoSpecificExpr#FUNC_REFS_STD_LIB}.
 * </p>
 */
public class FuncNoSpecificStdLibCheck extends CifCheck {
    /** For each standard library function the collection of values to check in {@link #disAlloweds}. */
    private static final Map<StdLibFunction, EnumSet<NoSpecificStdLib>> FUNCTION_VALUES;

    /** The collection of disallowed standard library functions. */
    private final EnumSet<NoSpecificStdLib> disalloweds;

    /**
     * Constructor of the {@link FuncNoSpecificStdLibCheck} class.
     *
     * @param disalloweds The collection of disallowed standard library functions.
     */
    public FuncNoSpecificStdLibCheck(EnumSet<NoSpecificStdLib> disalloweds) {
        this.disalloweds = disalloweds;
    }

    /**
     * Constructor of the {@link FuncNoSpecificStdLibCheck} class.
     *
     * @param disalloweds The collection of disallowed standard library functions.
     */
    public FuncNoSpecificStdLibCheck(NoSpecificStdLib... disalloweds) {
        this(Arrays.stream(disalloweds).collect(Collectors.toCollection(() -> EnumSet.noneOf(NoSpecificStdLib.class))));
    }

    @Override
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression stdLibRef,
            CifCheckViolations violations)
    {
        StdLibFunction func = stdLibRef.getFunction();
        EnumSet<NoSpecificStdLib> funcValues = FUNCTION_VALUES.get(func);
        Assert.notNull(funcValues);
        if (!isEmptyIntersection(disalloweds, funcValues)) {
            violations.add(stdLibRef, new ReportObjectTypeDescrMessage(),
                    new LiteralMessage("uses standard library function \"%s\"", functionToStr(func)));
        }
    }

    /** Values to specify the disallowed CIF standard library functions. */
    public static enum NoSpecificStdLib {
        /** Disallow all standard library stochastic distribution functions. */
        STD_LIB_DISTRIBUTION_GROUP(),

        /** Disallow all standard library trigonometry functions. */
        STD_LIB_TRIGONOMETRY_GROUP(),

        /** Disallow the standard library function {@link StdLibFunction#ABS}. */
        STD_LIB_ABS(true),

        /** Disallow the standard library function {@link StdLibFunction#CBRT}. */
        STD_LIB_CBRT(true),

        /** Disallow the standard library function {@link StdLibFunction#EXP}. */
        STD_LIB_EXP(true),

        /** Disallow the standard library function {@link StdLibFunction#LN}. */
        STD_LIB_LN(true),

        /** Disallow the standard library function {@link StdLibFunction#LOG}. */
        STD_LIB_LOG(true),

        /** Disallow the standard library function {@link StdLibFunction#MAXIMUM}. */
        STD_LIB_MAXIMUM(true),

        /** Disallow the standard library function {@link StdLibFunction#MINIMUM}. */
        STD_LIB_MINIMUM(true),

        /** Disallow the standard library function {@link StdLibFunction#POWER}. */
        STD_LIB_POWER(true),

        /** Disallow the standard library function {@link StdLibFunction#SQRT}. */
        STD_LIB_SQRT(true),

        /** Disallow the standard library function {@link StdLibFunction#ACOS}. */
        STD_LIB_ACOS(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#ASIN}. */
        STD_LIB_ASIN(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#ATAN}. */
        STD_LIB_ATAN(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#COS}. */
        STD_LIB_COS(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#SIN}. */
        STD_LIB_SIN(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#TAN}. */
        STD_LIB_TAN(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#CEIL}. */
        STD_LIB_CEIL(true),

        /** Disallow the standard library function {@link StdLibFunction#DELETE}. */
        STD_LIB_DELETE(true),

        /** Disallow the standard library function {@link StdLibFunction#EMPTY}. */
        STD_LIB_EMPTY(true),

        /** Disallow the standard library function {@link StdLibFunction#FLOOR}. */
        STD_LIB_FLOOR(true),

        /** Disallow the standard library function {@link StdLibFunction#FORMAT}. */
        STD_LIB_FORMAT(true),

        /** Disallow the standard library function {@link StdLibFunction#POP}. */
        STD_LIB_POP(true),

        /** Disallow the standard library function {@link StdLibFunction#ROUND}. */
        STD_LIB_ROUND(true),

        /** Disallow the standard library function {@link StdLibFunction#SCALE}. */
        STD_LIB_SCALE(true),

        /** Disallow the standard library function {@link StdLibFunction#SIGN}. */
        STD_LIB_SIGN(true),

        /** Disallow the standard library function {@link StdLibFunction#SIZE}. */
        STD_LIB_SIZE(true),

        /** Disallow the standard library function {@link StdLibFunction#ACOSH}. */
        STD_LIB_ACOSH(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#ASINH}. */
        STD_LIB_ASINH(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#ATANH}. */
        STD_LIB_ATANH(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#COSH}. */
        STD_LIB_COSH(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#SINH}. */
        STD_LIB_SINH(true, true, false),

        /** Disallow the standard library function {@link StdLibFunction#TANH}. */
        STD_LIB_TANH(true, true, false),

        // Distributions.

        /** Disallow the standard library function {@link StdLibFunction#BERNOULLI}. */
        STD_LIB_BERNOULLI(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#BETA}. */
        STD_LIB_BETA(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#BINOMIAL}. */
        STD_LIB_BINOMIAL(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#CONSTANT}. */
        STD_LIB_CONSTANT(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#ERLANG}. */
        STD_LIB_ERLANG(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#EXPONENTIAL}. */
        STD_LIB_EXPONENTIAL(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#GAMMA}. */
        STD_LIB_GAMMA(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#GEOMETRIC}. */
        STD_LIB_GEOMETRIC(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#LOG_NORMAL}. */
        STD_LIB_LOG_NORMAL(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#NORMAL}. */
        STD_LIB_NORMAL(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#POISSON}. */
        STD_LIB_POISSON(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#RANDOM}. */
        STD_LIB_RANDOM(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#TRIANGLE}. */
        STD_LIB_TRIANGLE(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#UNIFORM}. */
        STD_LIB_UNIFORM(true, false, true),

        /** Disallow the standard library function {@link StdLibFunction#WEIBULL}. */
        STD_LIB_WEIBULL(true, false, true);

        /** Whether the enum value is an actual CIF standard library function (and not a group of them). */
        private boolean isFunc;

        /** Whether the enum value is part of the group of trigonometry functions. */
        private boolean isTrigonometry;

        /** Whether the enum value is part of the group of stochastic distribution functions. */
        private boolean isDistribution;

        /**
         * Constructor of the {@link NoSpecificStdLib} class.
         *
         * <p>
         * Use this constructor for entries that represent a group of functions, for example the
         * {@link #STD_LIB_DISTRIBUTION_GROUP} group.
         * </p>
         */
        private NoSpecificStdLib() {
            this(false);
        }

        /**
         * Constructor of the {@link NoSpecificStdLib} class.
         *
         * <p>
         * Use this constructor for entries that represent singular CIF standard library functions that are not part of
         * a group of functions.
         * </p>
         *
         * @param isFunc Whether the enum value represents an actual CIF standard library function (and not a group of them).
         */
        private NoSpecificStdLib(boolean isFunc) {
            this(isFunc, false, false);
        }

        /**
         * Constructor of the {@link NoSpecificStdLib} class.
         *
         * <p>
         * Use this constructor for entries that represent existing CIF standard library functions that are part of one
         * or more groups.
         * </p>
         *
         * @param isFunc Whether the enum value represents an actual CIF standard library function (and not a group of them).
         * @param isTrigonometry Whether the enum value is part of the group of trigonometry functions.
         * @param isDistribution Whether the enum value is part of the group of stochastic distribution functions.
         *
         */
        private NoSpecificStdLib(boolean isFunc, boolean isTrigonometry, boolean isDistribution) {
            this.isFunc = isFunc;
            this.isTrigonometry = isTrigonometry;
            this.isDistribution = isDistribution;
        }

        /**
         * Construct the set of enum values that can be used to indicate disallowing the function.
         *
         * <p>
         * Method must be used with enum values that represent an existing standard library function (and not a group of them).
         * </p>
         *
         * @return The entire set of enum values covering the function.
         */
        public EnumSet<NoSpecificStdLib> computeValues() {
            Assert.check(this.isFunc);

            EnumSet<NoSpecificStdLib> values = EnumSet.of(this);
            if (this.isTrigonometry) {
                values.add(STD_LIB_TRIGONOMETRY_GROUP);
            }
            if (this.isDistribution) {
                values.add(STD_LIB_DISTRIBUTION_GROUP);
            }
            return values;
        }
    }

    static {
        // Construct the set of function values for each standard library function.
        FUNCTION_VALUES = mapc(StdLibFunction.values().length);

        FUNCTION_VALUES.put(StdLibFunction.ABS, NoSpecificStdLib.STD_LIB_ABS.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.CBRT, NoSpecificStdLib.STD_LIB_CBRT.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.EXP, NoSpecificStdLib.STD_LIB_EXP.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.LN, NoSpecificStdLib.STD_LIB_LN.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.LOG, NoSpecificStdLib.STD_LIB_LOG.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.MAXIMUM, NoSpecificStdLib.STD_LIB_MAXIMUM.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.MINIMUM, NoSpecificStdLib.STD_LIB_MINIMUM.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.POWER, NoSpecificStdLib.STD_LIB_POWER.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.SQRT, NoSpecificStdLib.STD_LIB_SQRT.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.ACOS, NoSpecificStdLib.STD_LIB_ACOS.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.ASIN, NoSpecificStdLib.STD_LIB_ASIN.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.ATAN, NoSpecificStdLib.STD_LIB_ATAN.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.COS, NoSpecificStdLib.STD_LIB_COS.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.SIN, NoSpecificStdLib.STD_LIB_SIN.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.TAN, NoSpecificStdLib.STD_LIB_TAN.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.CEIL, NoSpecificStdLib.STD_LIB_CEIL.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.DELETE, NoSpecificStdLib.STD_LIB_DELETE.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.EMPTY, NoSpecificStdLib.STD_LIB_EMPTY.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.FLOOR, NoSpecificStdLib.STD_LIB_FLOOR.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.FORMAT, NoSpecificStdLib.STD_LIB_FORMAT.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.POP, NoSpecificStdLib.STD_LIB_POP.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.ROUND, NoSpecificStdLib.STD_LIB_ROUND.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.SCALE, NoSpecificStdLib.STD_LIB_SCALE.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.SIGN, NoSpecificStdLib.STD_LIB_SIGN.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.SIZE, NoSpecificStdLib.STD_LIB_SIZE.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.ACOSH, NoSpecificStdLib.STD_LIB_ACOSH.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.ASINH, NoSpecificStdLib.STD_LIB_ASINH.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.ATANH, NoSpecificStdLib.STD_LIB_ATANH.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.COSH, NoSpecificStdLib.STD_LIB_COSH.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.SINH, NoSpecificStdLib.STD_LIB_SINH.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.TANH, NoSpecificStdLib.STD_LIB_TANH.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.BERNOULLI, NoSpecificStdLib.STD_LIB_BERNOULLI.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.BETA, NoSpecificStdLib.STD_LIB_BETA.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.BINOMIAL, NoSpecificStdLib.STD_LIB_BINOMIAL.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.CONSTANT, NoSpecificStdLib.STD_LIB_CONSTANT.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.ERLANG, NoSpecificStdLib.STD_LIB_ERLANG.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.EXPONENTIAL, NoSpecificStdLib.STD_LIB_EXPONENTIAL.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.GAMMA, NoSpecificStdLib.STD_LIB_GAMMA.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.GEOMETRIC, NoSpecificStdLib.STD_LIB_GEOMETRIC.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.LOG_NORMAL, NoSpecificStdLib.STD_LIB_LOG_NORMAL.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.NORMAL, NoSpecificStdLib.STD_LIB_NORMAL.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.POISSON, NoSpecificStdLib.STD_LIB_POISSON.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.RANDOM, NoSpecificStdLib.STD_LIB_RANDOM.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.TRIANGLE, NoSpecificStdLib.STD_LIB_TRIANGLE.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.UNIFORM, NoSpecificStdLib.STD_LIB_UNIFORM.computeValues());
        FUNCTION_VALUES.put(StdLibFunction.WEIBULL, NoSpecificStdLib.STD_LIB_WEIBULL.computeValues());
    }
}
