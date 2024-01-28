//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import static org.eclipse.escet.cif.common.CifTextUtils.functionToStr;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;

/**
 * CIF check that does not allow certain standard library functions.
 *
 * <p>
 * To disallow all standard library functions, use {@link ExprNoSpecificExprsCheck} with
 * {@link NoSpecificExpr#FUNC_REFS_STD_LIB}.
 * </p>
 */
public class FuncNoSpecificStdLibCheck extends CifCheck {
    /** The standard library functions, or groups of standard library functions, to disallow. */
    private final EnumSet<NoSpecificStdLib> disalloweds;

    /**
     * Constructor of the {@link FuncNoSpecificStdLibCheck} class.
     *
     * @param disalloweds The standard library functions, or groups of standard library functions, to disallow.
     */
    public FuncNoSpecificStdLibCheck(EnumSet<NoSpecificStdLib> disalloweds) {
        this.disalloweds = disalloweds;
    }

    /**
     * Constructor of the {@link FuncNoSpecificStdLibCheck} class.
     *
     * @param disalloweds The standard library functions, or groups of standard library functions, to disallow.
     */
    public FuncNoSpecificStdLibCheck(NoSpecificStdLib... disalloweds) {
        this(EnumSet.copyOf(Arrays.asList(disalloweds)));
    }

    @Override
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression stdLibRef,
            CifCheckViolations violations)
    {
        StdLibFunction func = stdLibRef.getFunction();
        switch (func) {
            case ABS:
                if (disalloweds.contains(NoSpecificStdLib.ABS)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case ACOS:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.ACOS))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case ACOSH:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.ACOSH))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case ASIN:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.ASIN))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case ASINH:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.ASINH))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case ATAN:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.ATAN))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case ATANH:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.ATANH))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case BERNOULLI:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.BERNOULLI))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case BETA:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.BETA))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case BINOMIAL:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.BINOMIAL))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case CBRT:
                if (disalloweds.contains(NoSpecificStdLib.CBRT)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case CEIL:
                if (disalloweds.contains(NoSpecificStdLib.CEIL)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case CONSTANT:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.CONSTANT))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case COS:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.COS))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case COSH:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.COSH))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case DELETE:
                if (disalloweds.contains(NoSpecificStdLib.DELETE)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case EMPTY:
                if (disalloweds.contains(NoSpecificStdLib.EMPTY)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case ERLANG:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.ERLANG))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case EXP:
                if (disalloweds.contains(NoSpecificStdLib.EXP)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case EXPONENTIAL:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.EXPONENTIAL))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case FLOOR:
                if (disalloweds.contains(NoSpecificStdLib.FLOOR)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case FORMAT:
                if (disalloweds.contains(NoSpecificStdLib.FORMAT)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case GAMMA:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.GAMMA))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case GEOMETRIC:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.GEOMETRIC))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case LN:
                if (disalloweds.contains(NoSpecificStdLib.LN)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case LOG:
                if (disalloweds.contains(NoSpecificStdLib.LOG)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case LOG_NORMAL:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.LOG_NORMAL))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case MAXIMUM:
                if (disalloweds.contains(NoSpecificStdLib.MAXIMUM)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case MINIMUM:
                if (disalloweds.contains(NoSpecificStdLib.MINIMUM)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case NORMAL:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.NORMAL))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case POISSON:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.POISSON))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case POP:
                if (disalloweds.contains(NoSpecificStdLib.POP)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case POWER:
                if (disalloweds.contains(NoSpecificStdLib.POWER)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case RANDOM:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.RANDOM))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case ROUND:
                if (disalloweds.contains(NoSpecificStdLib.ROUND)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case SCALE:
                if (disalloweds.contains(NoSpecificStdLib.SCALE)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case SIGN:
                if (disalloweds.contains(NoSpecificStdLib.SIGN)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case SIN:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.SIN))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case SINH:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.SINH))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case SIZE:
                if (disalloweds.contains(NoSpecificStdLib.SIZE)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case SQRT:
                if (disalloweds.contains(NoSpecificStdLib.SQRT)) {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case TAN:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.TAN))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case TANH:
                if (disalloweds.contains(NoSpecificStdLib.ALL_TRIGONOMETRY)
                        || disalloweds.contains(NoSpecificStdLib.TANH))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case TRIANGLE:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.TRIANGLE))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case UNIFORM:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.UNIFORM))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;

            case WEIBULL:
                if (disalloweds.contains(NoSpecificStdLib.ALL_STOCHASTIC)
                        || disalloweds.contains(NoSpecificStdLib.WEIBULL))
                {
                    addExprViolationFunction(stdLibRef, violations);
                }
                return;
        }

        throw new AssertionError("Unknown std lib function: " + func);
    }

    /**
     * Add a violation for a standard library function.
     *
     * @param stdLibExpr The standard library function expression.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolationFunction(StdLibFunctionExpression stdLibExpr, CifCheckViolations violations) {
        violations.add(stdLibExpr, "Standard library function \"%s\" is used", functionToStr(stdLibExpr.getFunction()));
    }

    /**
     * The standard library function, or group of standard library functions, to disallow.
     */
    public static enum NoSpecificStdLib {
        /** Disallow all standard library trigonometry functions. */
        ALL_TRIGONOMETRY,

        /** Disallow all standard library stochastic distribution functions. */
        ALL_STOCHASTIC,

        /** Disallow {@link StdLibFunction#ABS}. */
        ABS,

        /** Disallow {@link StdLibFunction#ACOS}. */
        ACOS,

        /** Disallow {@link StdLibFunction#ACOSH}. */
        ACOSH,

        /** Disallow {@link StdLibFunction#ASIN}. */
        ASIN,

        /** Disallow {@link StdLibFunction#ASINH}. */
        ASINH,

        /** Disallow {@link StdLibFunction#ATAN}. */
        ATAN,

        /** Disallow {@link StdLibFunction#ATANH}. */
        ATANH,

        /** Disallow {@link StdLibFunction#BERNOULLI}. */
        BERNOULLI,

        /** Disallow {@link StdLibFunction#BETA}. */
        BETA,

        /** Disallow {@link StdLibFunction#BINOMIAL}. */
        BINOMIAL,

        /** Disallow {@link StdLibFunction#CBRT}. */
        CBRT,

        /** Disallow {@link StdLibFunction#CEIL}. */
        CEIL,

        /** Disallow {@link StdLibFunction#CONSTANT}. */
        CONSTANT,

        /** Disallow {@link StdLibFunction#COS}. */
        COS,

        /** Disallow {@link StdLibFunction#COSH}. */
        COSH,

        /** Disallow {@link StdLibFunction#DELETE}. */
        DELETE,

        /** Disallow {@link StdLibFunction#EMPTY}. */
        EMPTY,

        /** Disallow {@link StdLibFunction#ERLANG}. */
        ERLANG,

        /** Disallow {@link StdLibFunction#EXP}. */
        EXP,

        /** Disallow {@link StdLibFunction#EXPONENTIAL}. */
        EXPONENTIAL,

        /** Disallow {@link StdLibFunction#FLOOR}. */
        FLOOR,

        /** Disallow {@link StdLibFunction#FORMAT}. */
        FORMAT,

        /** Disallow {@link StdLibFunction#GAMMA}. */
        GAMMA,

        /** Disallow {@link StdLibFunction#GEOMETRIC}. */
        GEOMETRIC,

        /** Disallow {@link StdLibFunction#LN}. */
        LN,

        /** Disallow {@link StdLibFunction#LOG}. */
        LOG,

        /** Disallow {@link StdLibFunction#LOG_NORMAL}. */
        LOG_NORMAL,

        /** Disallow {@link StdLibFunction#MAXIMUM}. */
        MAXIMUM,

        /** Disallow {@link StdLibFunction#MINIMUM}. */
        MINIMUM,

        /** Disallow {@link StdLibFunction#NORMAL}. */
        NORMAL,

        /** Disallow {@link StdLibFunction#POISSON}. */
        POISSON,

        /** Disallow {@link StdLibFunction#POP}. */
        POP,

        /** Disallow {@link StdLibFunction#POWER}. */
        POWER,

        /** Disallow {@link StdLibFunction#RANDOM}. */
        RANDOM,

        /** Disallow {@link StdLibFunction#ROUND}. */
        ROUND,

        /** Disallow {@link StdLibFunction#SCALE}. */
        SCALE,

        /** Disallow {@link StdLibFunction#SIGN}. */
        SIGN,

        /** Disallow {@link StdLibFunction#SIN}. */
        SIN,

        /** Disallow {@link StdLibFunction#SINH}. */
        SINH,

        /** Disallow {@link StdLibFunction#SIZE}. */
        SIZE,

        /** Disallow {@link StdLibFunction#SQRT}. */
        SQRT,

        /** Disallow {@link StdLibFunction#TAN}. */
        TAN,

        /** Disallow {@link StdLibFunction#TANH}. */
        TANH,

        /** Disallow {@link StdLibFunction#TRIANGLE}. */
        TRIANGLE,

        /** Disallow {@link StdLibFunction#UNIFORM}. */
        UNIFORM,

        /** Disallow {@link StdLibFunction#WEIBULL}. */
        WEIBULL,
    }
}
