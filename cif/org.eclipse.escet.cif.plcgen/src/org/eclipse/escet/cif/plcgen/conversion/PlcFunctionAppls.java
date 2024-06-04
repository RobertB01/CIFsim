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

package org.eclipse.escet.cif.plcgen.conversion;

import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.BOOL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.TIME_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcGenericType.ANY_ELEMENTARY_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcGenericType.ANY_NUM_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcGenericType.ANY_REAL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcGenericType.ANY_TYPE;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncBlockAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.ExprBinding;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcParamDirection;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcParameterDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcCastFunctionDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcSemanticFuncDescription;
import org.eclipse.escet.cif.plcgen.model.types.PlcAbstractType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcFuncBlockType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;

/** Elementary function application construction methods for a target. */
public class PlcFunctionAppls {
    /** PLC to generate code for. */
    private final PlcTarget target;

    /** The type of a TON function block instance variable. Lazily created. */
    private PlcFuncBlockType tonBlockType = null;

    /**
     * Constructor of the {@link PlcFunctionAppls} class.
     *
     * @param target PLC to generate code for.
     */
    public PlcFunctionAppls(PlcTarget target) {
        this.target = target;
    }

    /**
     * Construct a function application for a negation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl negateFuncAppl(PlcExpression in) {
        // Assumed to be the same as subtraction.
        return funcAppl(PlcFuncOperation.NEGATE_OP, null, "-", ExprBinding.UNARY_EXPR, ANY_NUM_TYPE, in, ANY_NUM_TYPE);
    }

    /**
     * Construct a power function application ({@code base ** exponent}).
     *
     * @param in1 Base value argument of the function.
     * @param in2 Exponent argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl powerFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.POWER_OP, "EXPT", "**", ExprBinding.POWER_EXPR,
                new PlcAbstractType[]
                {ANY_REAL_TYPE, ANY_NUM_TYPE}, new PlcExpression[] {in1, in2}, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a multiplication.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl multiplyFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.MULTIPLY_OP, "MUL", "*", ExprBinding.MUL_EXPR, ANY_NUM_TYPE, inN,
                ANY_NUM_TYPE);
    }

    /**
     * Construct a function application for a division.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl divideFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.DIVIDE_OP, "DIV", "/", ExprBinding.MUL_EXPR, ANY_NUM_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_NUM_TYPE);
    }

    /**
     * Construct a function application for a modulus.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl moduloFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.MODULO_OP, "MOD", "MOD", ExprBinding.MUL_EXPR, ANY_NUM_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_NUM_TYPE);
    }

    /**
     * Construct a function application for an addition.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl addFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.ADD_OP, "ADD", "+", ExprBinding.ADD_EXPR, ANY_NUM_TYPE, inN, ANY_NUM_TYPE);
    }

    /**
     * Construct a function application for a subtraction.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl subtractFuncAppl(PlcExpression in1, PlcExpression in2) {
        // The PLC type allows more types.
        return funcAppl(PlcFuncOperation.SUBTRACT_OP, "SUB", "-", ExprBinding.MUL_EXPR, ANY_NUM_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_NUM_TYPE);
    }

    /**
     * Construct a function application for a less-than comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl lessThanFuncAppl(PlcExpression in1, PlcExpression in2) {
        // The PLC function allows more than two parameters.
        return funcAppl(PlcFuncOperation.LESS_THAN_OP, "LT", "<", ExprBinding.ORDER_EXPR, ANY_ELEMENTARY_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_ELEMENTARY_TYPE);
    }

    /**
     * Construct a function application for a less-or-equal comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl lessEqualFuncAppl(PlcExpression in1, PlcExpression in2) {
        // The PLC function allows more than two parameters.
        return funcAppl(PlcFuncOperation.LESS_EQUAL_OP, "LE", "<=", ExprBinding.ORDER_EXPR, ANY_ELEMENTARY_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_ELEMENTARY_TYPE);
    }

    /**
     * Construct a function application for a greater-than comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl greaterThanFuncAppl(PlcExpression in1, PlcExpression in2) {
        // The PLC function allows more than two parameters.
        return funcAppl(PlcFuncOperation.GREATER_THAN_OP, "GT", ">", ExprBinding.ORDER_EXPR, ANY_ELEMENTARY_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_ELEMENTARY_TYPE);
    }

    /**
     * Construct a function application for a greater-or-equal comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl greaterEqualFuncAppl(PlcExpression in1, PlcExpression in2) {
        // The PLC function allows more than two parameters.
        return funcAppl(PlcFuncOperation.GREATER_EQUAL_OP, "GE", ">=", ExprBinding.ORDER_EXPR, ANY_ELEMENTARY_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_ELEMENTARY_TYPE);
    }

    /**
     * Construct a function application for an equality comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl equalFuncAppl(PlcExpression in1, PlcExpression in2) {
        // The PLC function allows more than two parameters.
        return funcAppl(PlcFuncOperation.EQUAL_OP, "EQ", "=", ExprBinding.EQUAL_EXPR, ANY_ELEMENTARY_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_ELEMENTARY_TYPE);
    }

    /**
     * Construct a function application for an inequality comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl unEqualFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.UNEQUAL_OP, "NE", "<>", ExprBinding.EQUAL_EXPR, ANY_ELEMENTARY_TYPE,
                new PlcExpression[]
                {in1, in2}, ANY_ELEMENTARY_TYPE);
    }

    /**
     * Construct a function application for a complement.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl complementFuncAppl(PlcExpression in) {
        // Infix literal needs the trailing space due to the "single parameter infix notation" behavior in
        // ModelTextGenerator.
        return funcAppl(PlcFuncOperation.COMPLEMENT_OP, "NOT", "NOT ", ExprBinding.UNARY_EXPR, BOOL_TYPE, in,
                BOOL_TYPE);
    }

    /**
     * Construct a function application for a conjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl andFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.AND_OP, "AND", "AND", ExprBinding.CONJUNCT_EXPR, BOOL_TYPE, inN, BOOL_TYPE);
    }

    /**
     * Construct a function application for an exclusive-disjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl xorFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.XOR_OP, "XOR", "XOR", ExprBinding.EXCL_DISJUNCT_EXPR, BOOL_TYPE, inN,
                BOOL_TYPE);
    }

    /**
     * Construct a function application for a disjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl orFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.OR_OP, "OR", "OR", ExprBinding.DISJUNCT_EXPR, BOOL_TYPE, inN, BOOL_TYPE);
    }

    /**
     * Construct a function application for casting.
     *
     * @param in The input argument of the function.
     * @param inType The type of the input value.
     * @param outType The type of the output value.
     * @return The constructed function application.
     */
    public PlcFuncAppl castFunctionAppl(PlcExpression in, PlcElementaryType inType, PlcElementaryType outType) {
        PlcFuncOperation operation = PlcFuncOperation.CAST_OP;
        Assert.check(target.supportsOperation(operation, 1));

        PlcBasicFuncDescription func = new PlcCastFunctionDescription(inType, outType);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a tertiary operation.
     *
     * @param g Selection guard.
     * @param in0 Value to return if the guard does not hold.
     * @param in1 Value to return if the guard holds.
     * @return The constructed function application.
     */
    public PlcFuncAppl selFuncAppl(PlcExpression g, PlcExpression in0, PlcExpression in1) {
        PlcFuncOperation operation = PlcFuncOperation.SEL_OP;
        Assert.check(target.supportsOperation(operation, 3));

        PlcParameterDescription[] params = new PlcParameterDescription[] {
                new PlcParameterDescription("G", PlcParamDirection.INPUT_ONLY, BOOL_TYPE),
                new PlcParameterDescription("IN0", PlcParamDirection.INPUT_ONLY, ANY_TYPE),
                new PlcParameterDescription("IN1", PlcParamDirection.INPUT_ONLY, ANY_TYPE)};
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, "SEL", params,
                target.getSupportedFuncNotations(operation, 3), ANY_TYPE);
        return new PlcFuncAppl(func,
                List.of(new PlcNamedValue("G", g), new PlcNamedValue("IN0", in0), new PlcNamedValue("IN1", in1)));
    }

    /**
     * Construct a function application to normalize an index expression into an array.
     *
     * @param indexExpr Index expression to normalize.
     * @param arraySize Length of the array.
     * @return The constructed function application.
     */
    public PlcFuncAppl normalizeArrayIndex(PlcExpression indexExpr, int arraySize) {
        // TODO Decide if it is better to create a named function that links back to the CIF element that needs this.
        PlcExpression g = greaterEqualFuncAppl(indexExpr, target.makeStdInteger(0));
        PlcExpression in0 = addFuncAppl(indexExpr, target.makeStdInteger(arraySize));
        PlcExpression in1 = indexExpr;
        return selFuncAppl(g, in0, in1);
    }

    /**
     * Construct a function application for an absolute value operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl absFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_ABS, "ABS", ANY_NUM_TYPE, in, ANY_NUM_TYPE);
    }

    /**
     * Construct a function application for an exponential operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl expFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_EXP, "EXP", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a natural logarithm operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl lnFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_LN, "LN", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a base 10 logarithm operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl logFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_LOG, "LOG", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a minimum operation.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl minFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.STDLIB_MIN, "MIN", ANY_ELEMENTARY_TYPE, inN, ANY_ELEMENTARY_TYPE);
    }

    /**
     * Construct a function application for a maximum operation.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl maxFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.STDLIB_MAX, "MAX", ANY_ELEMENTARY_TYPE, inN, ANY_ELEMENTARY_TYPE);
    }

    /**
     * Construct a function application for a square root operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl sqrtFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_SQRT, "SQRT", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a arccosine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl acosFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_ACOS, "ACOS", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a arcsine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl asinFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_ASIN, "ASIN", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a arctangent operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl atanFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_ATAN, "ATAN", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a cosine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl cosFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_COS, "COS", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a sine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl sinFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_SIN, "SIN", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Construct a function application for a tangent operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl tanFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_TAN, "TAN", ANY_REAL_TYPE, in, ANY_REAL_TYPE);
    }

    /**
     * Obtain the function block description of a TON function block.
     *
     * @return The created or retrieved TON function block.
     */
    public PlcFuncBlockType getTonFuncBlockType() {
        if (tonBlockType == null) {
            // Define the parameters of the TON block.
            PlcParameterDescription[] params = {
                    // Use 'false' for reset, or 'true' for measuring time.
                    new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY, BOOL_TYPE),
                    // End time.
                    new PlcParameterDescription("PT", PlcParamDirection.INPUT_ONLY, TIME_TYPE),
                    // End time has been reached.
                    new PlcParameterDescription("Q", PlcParamDirection.OUTPUT_ONLY, BOOL_TYPE),
                    // Amount of time since last reset, caps at PT.
                    new PlcParameterDescription("ET", PlcParamDirection.OUTPUT_ONLY, TIME_TYPE)
            };

            // Derive whether an S7 target is used.
            Set<PlcTargetType> s7Targets = EnumSet.of(
                    PlcTargetType.S7_300, PlcTargetType.S7_400,
                    PlcTargetType.S7_1200, PlcTargetType.S7_1500);

            // Construct a fitting TON function block description for the target.
            PlcFunctionBlockDescription tonBlockDescr = s7Targets.contains(target.getTargetType())
                    ? new PlcFunctionBlockDescription("TON", "TON", params, TIME_TYPE)
                    : new PlcFunctionBlockDescription("TON", "", params, TIME_TYPE);

            // And construct the variable type.
            tonBlockType = new PlcFuncBlockType(tonBlockDescr);
        }
        return tonBlockType;
    }

    /**
     * Construct a function application for a function with a single parameter.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation.
     * @param paramType Type of the parameter.
     * @param in Argument of the function.
     * @param resultType Type of the result of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, PlcAbstractType paramType,
            PlcExpression in, PlcAbstractType resultType)
    {
        Assert.check(target.supportsOperation(operation, 1));

        PlcParameterDescription[] parameterDesc = new PlcParameterDescription[] {
                new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY, paramType)};
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText, parameterDesc,
                target.getSupportedFuncNotations(operation, 1), resultType);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a function with a single parameter.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation or {@code null} if not available.
     * @param infixText Text of the function in infix notation or {@code null} if not available.
     * @param exprBinding Binding strength of the function in the expression.
     * @param paramType Type of the parameter.
     * @param in Argument of the function.
     * @param resultType Type of the result of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, String infixText,
            ExprBinding exprBinding, PlcAbstractType paramType, PlcExpression in, PlcAbstractType resultType)
    {
        Assert.check(target.supportsOperation(operation, 1));

        PlcParameterDescription[] parameterDesc = new PlcParameterDescription[] {
                new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY, paramType)};
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText, parameterDesc,
                infixText, exprBinding, target.getSupportedFuncNotations(operation, 1), resultType);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a function with a varying number of parameters.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation.
     * @param paramType Type of all the parameters.
     * @param inN Arguments of the function.
     * @param resultType Type of the result of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, PlcAbstractType paramType,
            PlcExpression[] inN, PlcAbstractType resultType)
    {
        Assert.check(target.supportsOperation(operation, inN.length));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText,
                makeParamList(inN.length, paramType), target.getSupportedFuncNotations(operation, inN.length),
                resultType);
        return new PlcFuncAppl(func, makeArgumentList(inN));
    }

    /**
     * Construct a function application for a function with a varying number of parameters.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation or {@code null} if not available.
     * @param infixText Text of the function in infix notation or {@code null} if not available.
     * @param exprBinding Binding strength of the function in the expression.
     * @param paramType Type of all the parameters.
     * @param inN Arguments of the function.
     * @param resultType Type of the result of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, String infixText,
            ExprBinding exprBinding, PlcAbstractType paramType, PlcExpression[] inN, PlcAbstractType resultType)
    {
        Assert.check(target.supportsOperation(operation, inN.length));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText,
                makeParamList(inN.length, paramType), infixText, exprBinding,
                target.getSupportedFuncNotations(operation, inN.length), resultType);
        return new PlcFuncAppl(func, makeArgumentList(inN));
    }

    /**
     * Construct a function application for a function with a varying number of parameters.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation or {@code null} if not available.
     * @param infixText Text of the function in infix notation or {@code null} if not available.
     * @param exprBinding Binding strength of the function in the expression.
     * @param paramTypes Types of the parameters.
     * @param inN Arguments of the function.
     * @param resultType Type of the result of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, String infixText,
            ExprBinding exprBinding, PlcAbstractType[] paramTypes, PlcExpression[] inN, PlcAbstractType resultType)
    {
        Assert.check(target.supportsOperation(operation, inN.length));
        Assert.areEqual(inN.length, paramTypes.length);

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText,
                makeParamList(paramTypes), infixText, exprBinding,
                target.getSupportedFuncNotations(operation, inN.length), resultType);
        return new PlcFuncAppl(func, makeArgumentList(inN));
    }

    /**
     * Construct a parameter list for {@code length} input parameters.
     *
     * @param length Number of parameters to create.
     * @param paramType Type of all the parameters.
     * @return The constructed parameter list.
     */
    private static PlcParameterDescription[] makeParamList(int length, PlcAbstractType paramType) {
        return IntStream.range(0, length)
                .mapToObj(i -> new PlcParameterDescription("IN" + (i + 1), PlcParamDirection.INPUT_ONLY, paramType))
                .toArray(PlcParameterDescription[]::new);
    }

    /**
     * Construct a parameter list with the parameter types.
     *
     * @param paramTypes Types of the parameters.
     * @return The constructed parameter list.
     */
    private static PlcParameterDescription[] makeParamList(PlcAbstractType[] paramTypes) {
        return IntStream.range(0, paramTypes.length)
                .mapToObj(i -> new PlcParameterDescription("IN" + (i + 1), PlcParamDirection.INPUT_ONLY, paramTypes[i]))
                .toArray(PlcParameterDescription[]::new);
    }

    /**
     * Construct an argument list with the input parameter values.
     *
     * @param inN Values of the arguments.
     * @return The constructed arguments list.
     */
    private static List<PlcNamedValue> makeArgumentList(PlcExpression[] inN) {
        return IntStream.range(0, inN.length).mapToObj(i -> new PlcNamedValue("IN" + (i + 1), inN[i]))
                .collect(Lists.toList());
    }

    /**
     * Perform a function application to a function block.
     *
     * @param variable Variable containing the function block instance.
     * @param arguments Arguments of the instantiated function block.
     * @return The constructed function application.
     */
    public PlcFuncBlockAppl funcBlockAppl(PlcBasicVariable variable, List<PlcNamedValue> arguments) {
        return new PlcFuncBlockAppl(variable, arguments);
    }
}
