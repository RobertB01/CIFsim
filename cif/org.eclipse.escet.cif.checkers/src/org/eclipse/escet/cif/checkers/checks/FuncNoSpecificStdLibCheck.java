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
import java.util.List;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.common.CifAnnotationUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.common.java.Assert;

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

    /** Whether to disable checking the use of standard library functions in annotations. */
    private boolean ignoreAnnotations;

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

    /**
     * Disable checking the use of standard library functions in annotations.
     *
     * @return The check instance, for daisy-chaining.
     */
    public FuncNoSpecificStdLibCheck ignoreAnnotations() {
        return ignoreAnnotations(true);
    }

    /**
     * Configure whether to disable checking the use of standard library functions in annotations.
     *
     * @param ignore {@code true} to disable, {@code false} to enable.
     * @return The check instance, for daisy-chaining.
     */
    public FuncNoSpecificStdLibCheck ignoreAnnotations(boolean ignore) {
        this.ignoreAnnotations = ignore;
        return this;
    }

    @Override
    protected void preprocessStdLibFunctionExpression(StdLibFunctionExpression stdLibRef,
            CifCheckViolations violations)
    {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(stdLibRef)) {
            return;
        }

        // Do the check.
        StdLibFunction func = stdLibRef.getFunction();
        FunctionCallExpression funcCallExpr = (FunctionCallExpression)stdLibRef.eContainer();
        List<CifType> argTypes = funcCallExpr.getArguments().stream()
                .map(arg -> CifTypeUtils.normalizeType(arg.getType())).toList();
        switch (func) {
            case ABS:
                if (disalloweds.contains(NoSpecificStdLib.ABS)) {
                    addExprViolationFunction(stdLibRef, violations);
                } else {
                    Assert.areEqual(argTypes.size(), 1);
                    if (disalloweds.contains(NoSpecificStdLib.ABS_INT)) {
                        if (argTypes.get(0) instanceof IntType) {
                            addExprViolationArgument(stdLibRef, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificStdLib.ABS_INT_RANGED)
                                && argTypes.get(0) instanceof IntType itype && !CifTypeUtils.isRangeless(itype))
                        {
                            addExprViolationArgument(stdLibRef, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificStdLib.ABS_INT_RANGELESS)
                                && argTypes.get(0) instanceof IntType itype && CifTypeUtils.isRangeless(itype))
                        {
                            addExprViolationArgument(stdLibRef, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificStdLib.ABS_REAL) && argTypes.get(0) instanceof RealType) {
                        addExprViolationArgument(stdLibRef, "a real typed", violations);
                    }
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
                } else {
                    Assert.areEqual(argTypes.size(), 1);
                    if (disalloweds.contains(NoSpecificStdLib.CONSTANT_BOOL) && argTypes.get(0) instanceof BoolType) {
                        addExprViolationArgument(stdLibRef, "a boolean typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.CONSTANT_INT) && argTypes.get(0) instanceof IntType) {
                        addExprViolationArgument(stdLibRef, "an integer typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.CONSTANT_REAL) && argTypes.get(0) instanceof RealType) {
                        addExprViolationArgument(stdLibRef, "a real typed", violations);
                    }
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
                } else {
                    Assert.areEqual(argTypes.size(), 2);
                    if (disalloweds.contains(NoSpecificStdLib.DELETE_LIST_ARRAY)
                            && (argTypes.get(0) instanceof ListType ltype && CifTypeUtils.isArrayType(ltype)))
                    {
                        addExprViolationArgument(stdLibRef, "an array list typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.DELETE_LIST_NON_ARRAY)
                            && (argTypes.get(0) instanceof ListType ltype && !CifTypeUtils.isArrayType(ltype)))
                    {
                        addExprViolationArgument(stdLibRef, "a non-array list typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.DELETE_INT_RANGED)
                            && argTypes.get(1) instanceof IntType itype && !CifTypeUtils.isRangeless(itype))
                    {
                        addExprViolationArgument(stdLibRef, "a ranged integer typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.DELETE_INT_RANGELESS)
                            && argTypes.get(1) instanceof IntType itype && CifTypeUtils.isRangeless(itype))
                    {
                        addExprViolationArgument(stdLibRef, "a rangeless integer typed", violations);
                    }
                }
                return;

            case EMPTY:
                if (disalloweds.contains(NoSpecificStdLib.EMPTY)) {
                    addExprViolationFunction(stdLibRef, violations);
                } else {
                    Assert.areEqual(argTypes.size(), 1);
                    if (disalloweds.contains(NoSpecificStdLib.EMPTY_LIST)) {
                        if (argTypes.get(0) instanceof ListType) {
                            addExprViolationArgument(stdLibRef, "a list typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificStdLib.EMPTY_LIST_ARRAY)
                                && argTypes.get(0) instanceof ListType ltype && CifTypeUtils.isArrayType(ltype))
                        {
                            addExprViolationArgument(stdLibRef, "an array list typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificStdLib.EMPTY_LIST_NON_ARRAY)
                                && argTypes.get(0) instanceof ListType ltype && !CifTypeUtils.isArrayType(ltype))
                        {
                            addExprViolationArgument(stdLibRef, "a non-array list typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificStdLib.EMPTY_SET) && argTypes.get(0) instanceof SetType) {
                        addExprViolationArgument(stdLibRef, "a set typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.EMPTY_DICT) && argTypes.get(0) instanceof DictType) {
                        addExprViolationArgument(stdLibRef, "a dictionary typed", violations);
                    }
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
                } else {
                    Assert.areEqual(argTypes.size(), 2);
                    if (disalloweds.contains(NoSpecificStdLib.MAXIMUM_INT)) {
                        if (argTypes.get(0) instanceof IntType || argTypes.get(1) instanceof IntType) {
                            addExprViolationArgument(stdLibRef, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificStdLib.MAXIMUM_INT_RANGED)
                                && ((argTypes.get(0) instanceof IntType itype1 && !CifTypeUtils.isRangeless(itype1))
                                        || (argTypes.get(1) instanceof IntType itype2
                                                && !CifTypeUtils.isRangeless(itype2))))
                        {
                            addExprViolationArgument(stdLibRef, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificStdLib.MAXIMUM_INT_RANGELESS)
                                && ((argTypes.get(0) instanceof IntType itype1 && CifTypeUtils.isRangeless(itype1))
                                        || (argTypes.get(1) instanceof IntType itype2
                                                && CifTypeUtils.isRangeless(itype2))))
                        {
                            addExprViolationArgument(stdLibRef, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificStdLib.MAXIMUM_REAL)
                            && (argTypes.get(0) instanceof RealType || argTypes.get(1) instanceof RealType))
                    {
                        addExprViolationArgument(stdLibRef, "a real typed", violations);
                    }
                }
                return;

            case MINIMUM:
                if (disalloweds.contains(NoSpecificStdLib.MINIMUM)) {
                    addExprViolationFunction(stdLibRef, violations);
                } else {
                    Assert.areEqual(argTypes.size(), 2);
                    if (disalloweds.contains(NoSpecificStdLib.MINIMUM_INT)) {
                        if (argTypes.get(0) instanceof IntType || argTypes.get(1) instanceof IntType) {
                            addExprViolationArgument(stdLibRef, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificStdLib.MINIMUM_INT_RANGED)
                                && ((argTypes.get(0) instanceof IntType itype1 && !CifTypeUtils.isRangeless(itype1))
                                        || (argTypes.get(1) instanceof IntType itype2
                                                && !CifTypeUtils.isRangeless(itype2))))
                        {
                            addExprViolationArgument(stdLibRef, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificStdLib.MINIMUM_INT_RANGELESS)
                                && ((argTypes.get(0) instanceof IntType itype1 && CifTypeUtils.isRangeless(itype1))
                                        || (argTypes.get(1) instanceof IntType itype2
                                                && CifTypeUtils.isRangeless(itype2))))
                        {
                            addExprViolationArgument(stdLibRef, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificStdLib.MINIMUM_REAL)
                            && (argTypes.get(0) instanceof RealType || argTypes.get(1) instanceof RealType))
                    {
                        addExprViolationArgument(stdLibRef, "a real typed", violations);
                    }
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
                } else {
                    Assert.areEqual(argTypes.size(), 1);
                    if (disalloweds.contains(NoSpecificStdLib.POP_ARRAY) && argTypes.get(0) instanceof ListType ltype
                            && CifTypeUtils.isArrayType(ltype))
                    {
                        addExprViolationArgument(stdLibRef, "an array list typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.POP_NON_ARRAY)
                            && argTypes.get(0) instanceof ListType ltype && !CifTypeUtils.isArrayType(ltype))
                    {
                        addExprViolationArgument(stdLibRef, "a non-array list typed", violations);
                    }
                }
                return;

            case POWER:
                if (disalloweds.contains(NoSpecificStdLib.POWER)) {
                    addExprViolationFunction(stdLibRef, violations);
                } else {
                    Assert.areEqual(argTypes.size(), 2);
                    if (disalloweds.contains(NoSpecificStdLib.POWER_INT)) {
                        if (argTypes.get(0) instanceof IntType || argTypes.get(1) instanceof IntType) {
                            addExprViolationArgument(stdLibRef, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificStdLib.POWER_INT_RANGED)
                                && ((argTypes.get(0) instanceof IntType itype1 && !CifTypeUtils.isRangeless(itype1))
                                        || (argTypes.get(1) instanceof IntType itype2
                                                && !CifTypeUtils.isRangeless(itype2))))
                        {
                            addExprViolationArgument(stdLibRef, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificStdLib.POWER_INT_RANGELESS)
                                && ((argTypes.get(0) instanceof IntType itype1 && CifTypeUtils.isRangeless(itype1))
                                        || (argTypes.get(1) instanceof IntType itype2
                                                && CifTypeUtils.isRangeless(itype2))))
                        {
                            addExprViolationArgument(stdLibRef, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificStdLib.POWER_REAL)
                            && ((argTypes.get(0) instanceof RealType) || (argTypes.get(1) instanceof RealType)))
                    {
                        addExprViolationArgument(stdLibRef, "a real typed", violations);
                    }
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
                } else {
                    Assert.areEqual(argTypes.size(), 5);
                    if (disalloweds.contains(NoSpecificStdLib.SCALE_INT)) {
                        if (argTypes.get(0) instanceof IntType || argTypes.get(1) instanceof IntType
                                || argTypes.get(2) instanceof IntType || argTypes.get(3) instanceof IntType
                                || argTypes.get(4) instanceof IntType)
                        {
                            addExprViolationArgument(stdLibRef, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificStdLib.SCALE_INT_RANGED)
                                && ((argTypes.get(0) instanceof IntType itype0 && !CifTypeUtils.isRangeless(itype0))
                                        || (argTypes.get(1) instanceof IntType itype1
                                                && !CifTypeUtils.isRangeless(itype1))
                                        || (argTypes.get(2) instanceof IntType itype2
                                                && !CifTypeUtils.isRangeless(itype2))
                                        || (argTypes.get(3) instanceof IntType itype3
                                                && !CifTypeUtils.isRangeless(itype3))
                                        || (argTypes.get(4) instanceof IntType itype4
                                                && !CifTypeUtils.isRangeless(itype4))))
                        {
                            addExprViolationArgument(stdLibRef, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificStdLib.SCALE_INT_RANGELESS)
                                && ((argTypes.get(0) instanceof IntType itype0 && CifTypeUtils.isRangeless(itype0))
                                        || (argTypes.get(1) instanceof IntType itype1
                                                && CifTypeUtils.isRangeless(itype1))
                                        || (argTypes.get(2) instanceof IntType itype2
                                                && CifTypeUtils.isRangeless(itype2))
                                        || (argTypes.get(3) instanceof IntType itype3
                                                && CifTypeUtils.isRangeless(itype3))
                                        || (argTypes.get(4) instanceof IntType itype4
                                                && CifTypeUtils.isRangeless(itype4))))
                        {
                            addExprViolationArgument(stdLibRef, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificStdLib.SCALE_REAL) && (argTypes.get(0) instanceof RealType
                            || argTypes.get(1) instanceof RealType || argTypes.get(2) instanceof RealType
                            || argTypes.get(3) instanceof RealType || argTypes.get(4) instanceof RealType))
                    {
                        addExprViolationArgument(stdLibRef, "a real typed", violations);
                    }
                }
                return;

            case SIGN:
                if (disalloweds.contains(NoSpecificStdLib.SIGN)) {
                    addExprViolationFunction(stdLibRef, violations);
                } else {
                    Assert.areEqual(argTypes.size(), 1);
                    if (disalloweds.contains(NoSpecificStdLib.SIGN_INT)) {
                        if (argTypes.get(0) instanceof IntType) {
                            addExprViolationArgument(stdLibRef, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificStdLib.SIGN_INT_RANGED)
                                && argTypes.get(0) instanceof IntType itype && !CifTypeUtils.isRangeless(itype))
                        {
                            addExprViolationArgument(stdLibRef, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificStdLib.SIGN_INT_RANGELESS)
                                && argTypes.get(0) instanceof IntType itype && CifTypeUtils.isRangeless(itype))
                        {
                            addExprViolationArgument(stdLibRef, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificStdLib.SIGN_REAL) && argTypes.get(0) instanceof RealType) {
                        addExprViolationArgument(stdLibRef, "a real typed", violations);
                    }
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
                } else {
                    Assert.areEqual(argTypes.size(), 1);
                    if (disalloweds.contains(NoSpecificStdLib.SIZE_STRING) && argTypes.get(0) instanceof StringType) {
                        addExprViolationArgument(stdLibRef, "a string typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.SIZE_LIST)) {
                        if (argTypes.get(0) instanceof ListType) {
                            addExprViolationArgument(stdLibRef, "a list typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificStdLib.SIZE_LIST_ARRAY)
                                && argTypes.get(0) instanceof ListType ltype && CifTypeUtils.isArrayType(ltype))
                        {
                            addExprViolationArgument(stdLibRef, "an array list typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificStdLib.SIZE_LIST_NON_ARRAY)
                                && argTypes.get(0) instanceof ListType ltype && !CifTypeUtils.isArrayType(ltype))
                        {
                            addExprViolationArgument(stdLibRef, "a non-array list typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificStdLib.SIZE_SET) && argTypes.get(0) instanceof SetType) {
                        addExprViolationArgument(stdLibRef, "a set typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.SIZE_DICT) && argTypes.get(0) instanceof DictType) {
                        addExprViolationArgument(stdLibRef, "a dictionary typed", violations);
                    }
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
                } else {
                    Assert.areEqual(argTypes.size(), 2);
                    if (disalloweds.contains(NoSpecificStdLib.UNIFORM_INT)
                            && ((argTypes.get(0) instanceof IntType) || argTypes.get(1) instanceof IntType))
                    {
                        addExprViolationArgument(stdLibRef, "an integer typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificStdLib.UNIFORM_REAL)
                            && ((argTypes.get(0) instanceof RealType) || argTypes.get(1) instanceof RealType))
                    {
                        addExprViolationArgument(stdLibRef, "a real typed", violations);
                    }
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
     * Add a violation for a standard library function on a certain kind of argument.
     *
     * @param stdLibExpr The standard library function expression.
     * @param argTxt A text describing the kind of argument that is a violation.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolationArgument(StdLibFunctionExpression stdLibExpr, String argTxt,
            CifCheckViolations violations)
    {
        violations.add(stdLibExpr, "Standard library function \"%s\" is used on %s argument",
                functionToStr(stdLibExpr.getFunction()), argTxt);
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

        /** Disallow {@link StdLibFunction#ABS} on integer numbers. */
        ABS_INT,

        /** Disallow {@link StdLibFunction#ABS} on ranged integer numbers. */
        ABS_INT_RANGED,

        /** Disallow {@link StdLibFunction#ABS} on rangeless integer numbers. */
        ABS_INT_RANGELESS,

        /** Disallow {@link StdLibFunction#ABS} on real numbers. */
        ABS_REAL,

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

        /** Disallow {@link StdLibFunction#CONSTANT} on booleans. */
        CONSTANT_BOOL,

        /** Disallow {@link StdLibFunction#CONSTANT} on integers. */
        CONSTANT_INT,

        /** Disallow {@link StdLibFunction#CONSTANT} on reals. */
        CONSTANT_REAL,

        /** Disallow {@link StdLibFunction#COS}. */
        COS,

        /** Disallow {@link StdLibFunction#COSH}. */
        COSH,

        /** Disallow {@link StdLibFunction#DELETE}. */
        DELETE,

        /** Disallow {@link StdLibFunction#DELETE} on array lists. */
        DELETE_LIST_ARRAY,

        /** Disallow {@link StdLibFunction#DELETE} on non-array lists. */
        DELETE_LIST_NON_ARRAY,

        /** Disallow {@link StdLibFunction#DELETE} on ranged integer numbers. */
        DELETE_INT_RANGED,

        /** Disallow {@link StdLibFunction#DELETE} on rangeless integer numbers. */
        DELETE_INT_RANGELESS,

        /** Disallow {@link StdLibFunction#EMPTY}. */
        EMPTY,

        /** Disallow {@link StdLibFunction#EMPTY} on lists. */
        EMPTY_LIST,

        /** Disallow {@link StdLibFunction#EMPTY} on array lists. */
        EMPTY_LIST_ARRAY,

        /** Disallow {@link StdLibFunction#EMPTY} on non-array lists. */
        EMPTY_LIST_NON_ARRAY,

        /** Disallow {@link StdLibFunction#EMPTY} on sets. */
        EMPTY_SET,

        /** Disallow {@link StdLibFunction#EMPTY} on dictionaries. */
        EMPTY_DICT,

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

        /** Disallow {@link StdLibFunction#MAXIMUM} on integer numbers. */
        MAXIMUM_INT,

        /** Disallow {@link StdLibFunction#MAXIMUM} on ranged integer numbers. */
        MAXIMUM_INT_RANGED,

        /** Disallow {@link StdLibFunction#MAXIMUM} on rangeless integer numbers. */
        MAXIMUM_INT_RANGELESS,

        /** Disallow {@link StdLibFunction#MAXIMUM} on real numbers. */
        MAXIMUM_REAL,

        /** Disallow {@link StdLibFunction#MINIMUM}. */
        MINIMUM,

        /** Disallow {@link StdLibFunction#MINIMUM} on integer numbers. */
        MINIMUM_INT,

        /** Disallow {@link StdLibFunction#MINIMUM} on ranged integer numbers. */
        MINIMUM_INT_RANGED,

        /** Disallow {@link StdLibFunction#MINIMUM} on rangeless integer numbers. */
        MINIMUM_INT_RANGELESS,

        /** Disallow {@link StdLibFunction#MINIMUM} on real numbers. */
        MINIMUM_REAL,

        /** Disallow {@link StdLibFunction#NORMAL}. */
        NORMAL,

        /** Disallow {@link StdLibFunction#POISSON}. */
        POISSON,

        /** Disallow {@link StdLibFunction#POP}. */
        POP,

        /** Disallow {@link StdLibFunction#POP} on array lists. */
        POP_ARRAY,

        /** Disallow {@link StdLibFunction#POP} on non-array lists. */
        POP_NON_ARRAY,

        /** Disallow {@link StdLibFunction#POWER}. */
        POWER,

        /** Disallow {@link StdLibFunction#POWER} on integer numbers. */
        POWER_INT,

        /** Disallow {@link StdLibFunction#POWER} on ranged integer numbers. */
        POWER_INT_RANGED,

        /** Disallow {@link StdLibFunction#POWER} on rangeless integer numbers. */
        POWER_INT_RANGELESS,

        /** Disallow {@link StdLibFunction#POWER} on real numbers. */
        POWER_REAL,

        /** Disallow {@link StdLibFunction#RANDOM}. */
        RANDOM,

        /** Disallow {@link StdLibFunction#ROUND}. */
        ROUND,

        /** Disallow {@link StdLibFunction#SCALE}. */
        SCALE,

        /** Disallow {@link StdLibFunction#SCALE} on integer numbers. */
        SCALE_INT,

        /** Disallow {@link StdLibFunction#SCALE} on ranged integer numbers. */
        SCALE_INT_RANGED,

        /** Disallow {@link StdLibFunction#SCALE} on rangeless integer numbers. */
        SCALE_INT_RANGELESS,

        /** Disallow {@link StdLibFunction#SCALE} on real numbers. */
        SCALE_REAL,

        /** Disallow {@link StdLibFunction#SIGN}. */
        SIGN,

        /** Disallow {@link StdLibFunction#SIGN} on integer numbers. */
        SIGN_INT,

        /** Disallow {@link StdLibFunction#SIGN} on ranged integer numbers. */
        SIGN_INT_RANGED,

        /** Disallow {@link StdLibFunction#SIGN} on rangeless integer numbers. */
        SIGN_INT_RANGELESS,

        /** Disallow {@link StdLibFunction#SIGN} on real numbers. */
        SIGN_REAL,

        /** Disallow {@link StdLibFunction#SIN}. */
        SIN,

        /** Disallow {@link StdLibFunction#SINH}. */
        SINH,

        /** Disallow {@link StdLibFunction#SIZE}. */
        SIZE,

        /** Disallow {@link StdLibFunction#SIZE} on strings. */
        SIZE_STRING,

        /** Disallow {@link StdLibFunction#SIZE} on lists. */
        SIZE_LIST,

        /** Disallow {@link StdLibFunction#SIZE} on array lists. */
        SIZE_LIST_ARRAY,

        /** Disallow {@link StdLibFunction#SIZE} on non-array lists. */
        SIZE_LIST_NON_ARRAY,

        /** Disallow {@link StdLibFunction#SIZE} on sets. */
        SIZE_SET,

        /** Disallow {@link StdLibFunction#SIZE} on dictionaries. */
        SIZE_DICT,

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

        /** Disallow {@link StdLibFunction#UNIFORM} on integers. */
        UNIFORM_INT,

        /** Disallow {@link StdLibFunction#UNIFORM} on reals. */
        UNIFORM_REAL,

        /** Disallow {@link StdLibFunction#WEIBULL}. */
        WEIBULL,
    }
}
