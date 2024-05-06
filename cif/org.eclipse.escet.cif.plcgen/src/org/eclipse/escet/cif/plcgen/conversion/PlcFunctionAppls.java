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

import java.util.List;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.ExprBinding;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcParamDirection;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcParameterDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcCastFunctionDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcSemanticFuncDescription;
import org.eclipse.escet.cif.plcgen.model.types.PlcDerivedType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;

/** Elementary function application construction methods for a target. */
public class PlcFunctionAppls {
    /** PLC to generate code for. */
    private final PlcTarget target;

    /** Parameters for functions that take one input parameters. */
    private static final PlcParameterDescription[] ONE_INPUT_PARAMETER = new PlcParameterDescription[] {
            new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY)};

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
        return funcAppl(PlcFuncOperation.NEGATE_OP, null, "-", ExprBinding.UNARY_EXPR, in);
    }

    /**
     * Construct a power function application ({@code base ** exponent}).
     *
     * @param in1 Base value argument of the function.
     * @param in2 Exponent argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl powerFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.POWER_OP, "EXPT", "**", ExprBinding.POWER_EXPR, new PlcExpression[] {in1, in2});
    }

    /**
     * Construct a function application for a multiplication.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl multiplyFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.MULTIPLY_OP, "MUL", "*", ExprBinding.MUL_EXPR, inN);
    }

    /**
     * Construct a function application for a division.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl divideFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.DIVIDE_OP, "DIV", "/", ExprBinding.MUL_EXPR, new PlcExpression[] {in1, in2});
    }

    /**
     * Construct a function application for a modulus.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl moduloFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.MODULO_OP, "MOD", "MOD", ExprBinding.MUL_EXPR, new PlcExpression[] {in1, in2});
    }

    /**
     * Construct a function application for an addition.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl addFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.ADD_OP, "ADD", "+", ExprBinding.ADD_EXPR, inN);
    }

    /**
     * Construct a function application for a subtraction.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl subtractFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.SUBTRACT_OP, "SUB", "-", ExprBinding.MUL_EXPR, new PlcExpression[] {in1, in2});
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
        return funcAppl(PlcFuncOperation.LESS_THAN_OP, "LT", "<", ExprBinding.ORDER_EXPR, new PlcExpression[] {in1, in2});
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
        return funcAppl(PlcFuncOperation.LESS_EQUAL_OP, "LE", "<=", ExprBinding.ORDER_EXPR, new PlcExpression[] {in1, in2});
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
        return funcAppl(PlcFuncOperation.GREATER_THAN_OP, "GT", ">", ExprBinding.ORDER_EXPR, new PlcExpression[] {in1, in2});
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
        return funcAppl(PlcFuncOperation.GREATER_EQUAL_OP, "GE", ">=", ExprBinding.ORDER_EXPR, new PlcExpression[] {in1, in2});
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
        return funcAppl(PlcFuncOperation.EQUAL_OP, "EQ", "=", ExprBinding.EQUAL_EXPR, new PlcExpression[] {in1, in2});
    }

    /**
     * Construct a function application for an inequality comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl unEqualFuncAppl(PlcExpression in1, PlcExpression in2) {
        return funcAppl(PlcFuncOperation.UNEQUAL_OP, "NE", "<>", ExprBinding.EQUAL_EXPR, new PlcExpression[] {in1, in2});
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
        return funcAppl(PlcFuncOperation.COMPLEMENT_OP, "NOT", "NOT ", ExprBinding.UNARY_EXPR, in);
    }

    /**
     * Construct a function application for a conjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl andFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.AND_OP, "AND", "AND", ExprBinding.CONJUNCT_EXPR, inN);
    }

    /**
     * Construct a function application for an exclusive-disjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl xorFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.XOR_OP, "XOR", "XOR", ExprBinding.EXCL_DISJUNCT_EXPR, inN);
    }

    /**
     * Construct a function application for a disjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl orFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.OR_OP, "OR", "OR", ExprBinding.DISJUNCT_EXPR, inN);
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
                new PlcParameterDescription("G", PlcParamDirection.INPUT_ONLY),
                new PlcParameterDescription("IN0", PlcParamDirection.INPUT_ONLY),
                new PlcParameterDescription("IN1", PlcParamDirection.INPUT_ONLY)};
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, "SEL", params,
                target.getSupportedFuncNotations(operation, 3));
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
        return funcAppl(PlcFuncOperation.STDLIB_ABS, "ABS", in);
    }

    /**
     * Construct a function application for an exponential operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl expFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_EXP, "EXP", in);
    }

    /**
     * Construct a function application for a natural logarithm operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl lnFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_LN, "LN", in);
    }

    /**
     * Construct a function application for a base 10 logarithm operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl logFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_LOG, "LOG", in);
    }

    /**
     * Construct a function application for a minimum operation.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl minFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.STDLIB_MIN, "MIN", inN);
    }

    /**
     * Construct a function application for a maximum operation.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl maxFuncAppl(PlcExpression... inN) {
        return funcAppl(PlcFuncOperation.STDLIB_MAX, "MAX", inN);
    }

    /**
     * Construct a function application for a square root operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl sqrtFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_SQRT, "SQRT", in);
    }

    /**
     * Construct a function application for a arccosine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl acosFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_ACOS, "ACOS", in);
    }

    /**
     * Construct a function application for a arcsine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl asinFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_ASIN, "ASIN", in);
    }

    /**
     * Construct a function application for a arctangent operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl atanFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_ATAN, "ATAN", in);
    }

    /**
     * Construct a function application for a cosine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl cosFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_COS, "COS", in);
    }

    /**
     * Construct a function application for a sine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl sinFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_SIN, "SIN", in);
    }

    /**
     * Construct a function application for a tangent operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl tanFuncAppl(PlcExpression in) {
        return funcAppl(PlcFuncOperation.STDLIB_TAN, "TAN", in);
    }

    /**
     * Instantiate a TON function block.
     *
     * @param prefixFuncName Name of the function.
     * @return The function block instance description.
     */
    public PlcFunctionBlockDescription makeTonBlock(String prefixFuncName) {
        PlcParameterDescription[] params = { //
                new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY), // Boolean, false resets the timer,
                // true allows measuring time.
                new PlcParameterDescription("PT", PlcParamDirection.INPUT_ONLY), // Real, end-time, should be positive.
                new PlcParameterDescription("Q", PlcParamDirection.OUTPUT_ONLY), // Boolean whether end-time has been
                // reached.
                new PlcParameterDescription("ET", PlcParamDirection.OUTPUT_ONLY)}; // Real, amount of measured time
                                                                                   // since last reset, caps at PT.
        return new PlcFunctionBlockDescription(prefixFuncName + target.getTonFuncBlockCallSuffix(),
                new PlcDerivedType("TON"), params);
    }

    /**
     * Construct a function application for a function with a single parameter.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation.
     * @param in Argument of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, PlcExpression in) {
        Assert.check(target.supportsOperation(operation, 1));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText, ONE_INPUT_PARAMETER,
                target.getSupportedFuncNotations(operation, 1));
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a function with a single parameter.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation or {@code null} if not available.
     * @param infixText Text of the function in infix notation or {@code null} if not available.
     * @param exprBinding Binding strength of the function in the expression.
     * @param in Argument of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, String infixText,
            ExprBinding exprBinding, PlcExpression in)
    {
        Assert.check(target.supportsOperation(operation, 1));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText, ONE_INPUT_PARAMETER,
                infixText, exprBinding, target.getSupportedFuncNotations(operation, 1));
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a function with a varying number of parameters.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation.
     * @param inN Arguments of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, PlcExpression[] inN) {
        Assert.check(target.supportsOperation(operation, inN.length));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText,
                makeParamList(inN.length), target.getSupportedFuncNotations(operation, inN.length));
        return new PlcFuncAppl(func, makeArgumentList(inN));
    }

    /**
     * Construct a function application for a function with a varying number of parameters.
     *
     * @param operation The performed function.
     * @param prefixText Text of the function in prefix notation or {@code null} if not available.
     * @param infixText Text of the function in infix notation or {@code null} if not available.
     * @param exprBinding Binding strength of the function in the expression.
     * @param inN Arguments of the function.
     * @return The constructed function application.
     */
    private PlcFuncAppl funcAppl(PlcFuncOperation operation, String prefixText, String infixText,
            ExprBinding exprBinding, PlcExpression[] inN)
    {
        Assert.check(target.supportsOperation(operation, inN.length));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(operation, prefixText,
                makeParamList(inN.length), infixText, exprBinding,
                target.getSupportedFuncNotations(operation, inN.length));
        return new PlcFuncAppl(func, makeArgumentList(inN));
    }

    /**
     * Construct a parameter list for {@code length} input parameters.
     *
     * @param length Number of parameters to create.
     * @return The constructed parameter list.
     */
    private static PlcParameterDescription[] makeParamList(int length) {
        return IntStream.range(0, length)
                .mapToObj(i -> new PlcParameterDescription("IN" + (i + 1), PlcParamDirection.INPUT_ONLY))
                .toArray(PlcParameterDescription[]::new);
    }

    /**
     * Construct an argument list for the input parameters.
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
     * @param funcBlkDesc Description of the instantiated function block.
     * @param arguments Arguments of the instantiated function block.
     * @return The constructed function application.
     */
    public PlcFuncAppl funcBlockAppl(PlcFunctionBlockDescription funcBlkDesc, List<PlcNamedValue> arguments) {
        return new PlcFuncAppl(funcBlkDesc, arguments);
    }
}
