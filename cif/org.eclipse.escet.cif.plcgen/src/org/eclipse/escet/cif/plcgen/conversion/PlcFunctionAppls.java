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

package org.eclipse.escet.cif.plcgen.conversion;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.ExprBinding;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcParamDirection;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcParameterDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcCastFunction;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcSemanticFuncDescription;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;

/** Elementary function application construction methods for a target. */
public class PlcFunctionAppls {
    /** PLC to generate code for. */
    private final PlcTarget target;

    /** Parameters for functions that take one input parameters. */
    private static final PlcParameterDescription[] ONE_INPUT_PARAMETER = new PlcParameterDescription[] {
            new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY),
            new PlcParameterDescription("OUT", PlcParamDirection.OUTPUT_ONLY)};

    /** Parameters for functions that take two input parameters. */
    private static final PlcParameterDescription[] TWO_INPUT_PARAMATERS = new PlcParameterDescription[] {
            new PlcParameterDescription("IN1", PlcParamDirection.INPUT_ONLY),
            new PlcParameterDescription("IN2", PlcParamDirection.INPUT_ONLY),
            new PlcParameterDescription("OUT", PlcParamDirection.OUTPUT_ONLY)};

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
        Assert.check(target.supportsOperation(PlcFuncOperation.NEGATE_OP));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.NEGATE_OP, null,
                ONE_INPUT_PARAMETER, "-", ExprBinding.UNARY_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a power function application ({@code base ** exponent}).
     *
     * @param in1 Base value argument of the function.
     * @param in2 Exponent argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl powerFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.POWER_OP));

        String infixText = target.supportsInfixNotation(PlcFuncOperation.POWER_OP) ? "**" : null;
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.POWER_OP, "EXPT",
                TWO_INPUT_PARAMATERS, infixText, ExprBinding.POWER_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a multiplication.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl multiplyFuncAppl(PlcExpression... inN) {
        Assert.check(target.supportsOperation(PlcFuncOperation.MULTIPLY_OP));
        Assert.check(inN.length > 1);

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.MULTIPLY_OP, "MUL",
                makeParamList(inN.length), "*", ExprBinding.MUL_EXPR);
        List<PlcNamedValue> arguments = IntStream.range(0, inN.length)
                .mapToObj(i -> new PlcNamedValue("IN" + String.valueOf(i + 1), inN[i])).collect(Collectors.toList());
        return new PlcFuncAppl(func, arguments);
    }

    /**
     * Construct a function application for a division.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl divideFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.DIVIDE_OP));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.DIVIDE_OP, "DIV",
                TWO_INPUT_PARAMATERS, "/", ExprBinding.MUL_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a modulus.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl moduloFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.MODULO_OP));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.MODULO_OP, "MOD",
                TWO_INPUT_PARAMATERS, null, ExprBinding.MUL_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for an addition.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl addFuncAppl(PlcExpression... inN) {
        Assert.check(target.supportsOperation(PlcFuncOperation.ADD_OP));
        Assert.check(inN.length > 1);

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.ADD_OP, "ADD",
                makeParamList(inN.length), "+", ExprBinding.ADD_EXPR);
        List<PlcNamedValue> arguments = IntStream.range(0, inN.length)
                .mapToObj(i -> new PlcNamedValue("IN" + String.valueOf(i + 1), inN[i])).collect(Collectors.toList());
        return new PlcFuncAppl(func, arguments);
    }

    /**
     * Construct a function application for a subtraction.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl subtractFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.SUBTRACT_OP));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.SUBTRACT_OP, "SUB",
                TWO_INPUT_PARAMATERS, "-", ExprBinding.ADD_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a less-than comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl lessThanFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.LESS_THAN_OP));

        // The PLC function allows more than two parameters.
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.LESS_THAN_OP, "LT",
                TWO_INPUT_PARAMATERS, "<", ExprBinding.ORDER_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a less-or-equal comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl lessEqualFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.LESS_EQUAL_OP));

        // The PLC function allows more than two parameters.
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.LESS_EQUAL_OP, "LE",
                TWO_INPUT_PARAMATERS, "<=", ExprBinding.ORDER_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a greater-than comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl greaterThanFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.GREATER_THAN_OP));

        // The PLC function allows more than two parameters.
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.GREATER_THAN_OP, "GT",
                TWO_INPUT_PARAMATERS, ">", ExprBinding.ORDER_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a greater-or-equal comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl greaterEqualFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.GREATER_EQUAL_OP));

        // The PLC function allows more than two parameters.
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.GREATER_EQUAL_OP, "GE",
                TWO_INPUT_PARAMATERS, ">=", ExprBinding.ORDER_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for an equality comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl equalFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.EQUAL_OP));

        // The PLC function allows more than two parameters.
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.EQUAL_OP, "EQ",
                TWO_INPUT_PARAMATERS, "=", ExprBinding.EQUAL_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for an inequality comparison.
     *
     * @param in1 First input arguments of the function.
     * @param in2 Second input arguments of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl unEqualFuncAppl(PlcExpression in1, PlcExpression in2) {
        Assert.check(target.supportsOperation(PlcFuncOperation.UNEQUAL_OP));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.UNEQUAL_OP, "NE",
                TWO_INPUT_PARAMATERS, "<>", ExprBinding.EQUAL_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a complement.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl complementFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.COMPLEMENT_OP));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.COMPLEMENT_OP, "NOT",
                ONE_INPUT_PARAMETER, null, ExprBinding.UNARY_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a conjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl andFuncAppl(PlcExpression... inN) {
        Assert.check(target.supportsOperation(PlcFuncOperation.AND_OP));
        Assert.check(inN.length > 1);

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.AND_OP, "AND",
                makeParamList(inN.length), "AND", ExprBinding.CONJUNCT_EXPR);
        List<PlcNamedValue> arguments = IntStream.range(0, inN.length)
                .mapToObj(i -> new PlcNamedValue("IN" + String.valueOf(i + 1), inN[i])).collect(Collectors.toList());
        return new PlcFuncAppl(func, arguments);
    }

    /**
     * Construct a function application for an exclusive-disjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl xorFuncAppl(PlcExpression... inN) {
        Assert.check(target.supportsOperation(PlcFuncOperation.XOR_OP));
        Assert.check(inN.length > 1);

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.XOR_OP, "XOR",
                makeParamList(inN.length), "XOR", ExprBinding.EXCL_DISJUNCT_EXPR);
        List<PlcNamedValue> arguments = IntStream.range(0, inN.length)
                .mapToObj(i -> new PlcNamedValue("IN" + String.valueOf(i + 1), inN[i])).collect(Collectors.toList());
        return new PlcFuncAppl(func, arguments);
    }

    /**
     * Construct a function application for a disjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl orFuncAppl(PlcExpression... inN) {
        Assert.check(target.supportsOperation(PlcFuncOperation.OR_OP));
        Assert.check(inN.length > 1);

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.OR_OP, "OR",
                makeParamList(inN.length), "OR", ExprBinding.DISJUNCT_EXPR);
        List<PlcNamedValue> arguments = IntStream.range(0, inN.length)
                .mapToObj(i -> new PlcNamedValue("IN" + String.valueOf(i + 1), inN[i])).collect(Collectors.toList());
        return new PlcFuncAppl(func, arguments);
    }

    /**
     * Construct a parameter list for {@code length} input parameters.
     *
     * @param length Number of parameters to create.
     * @return The constructed parameter list.
     */
    private static PlcParameterDescription[] makeParamList(int length) {
        PlcParameterDescription[] params = new PlcParameterDescription[length + 1];
        for (int i = 0; i < length; i++) {
            params[i] = new PlcParameterDescription("IN" + String.valueOf(i + 1), PlcParamDirection.INPUT_ONLY);
        }
        params[length] = new PlcParameterDescription("OUT", PlcParamDirection.OUTPUT_ONLY);
        return params;
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
        PlcBasicFuncDescription func = new PlcCastFunction(inType, outType);
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
        Assert.check(target.supportsOperation(PlcFuncOperation.SEL_OP));

        PlcParameterDescription[] params = new PlcParameterDescription[] {
                new PlcParameterDescription("G", PlcParamDirection.INPUT_ONLY),
                new PlcParameterDescription("IN0", PlcParamDirection.INPUT_ONLY),
                new PlcParameterDescription("IN1", PlcParamDirection.INPUT_ONLY)};
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.SEL_OP, "SEL", params);
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
        PlcExpression g = greaterEqualFuncAppl(indexExpr, new PlcIntLiteral(0));
        PlcExpression in0 = addFuncAppl(indexExpr, new PlcIntLiteral(arraySize));
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
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_ABS));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_ABS, "ABS",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for an exponential operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl expFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_EXP));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_EXP, "EXP",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a natural logarithm operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl lnFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_LN));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_LN, "LN",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a base 10 logarithm operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl logFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_LOG));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_LOG, "LOG",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a minimum operation.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl minFuncAppl(PlcExpression... inN) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_MIN));
        Assert.check(inN.length > 1);

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_MIN, "MIN",
                makeParamList(inN.length));
        List<PlcNamedValue> arguments = IntStream.range(0, inN.length)
                .mapToObj(i -> new PlcNamedValue("IN" + String.valueOf(i + 1), inN[i])).collect(Collectors.toList());
        return new PlcFuncAppl(func, arguments);
    }

    /**
     * Construct a function application for a maximum operation.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl maxFuncAppl(PlcExpression... inN) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_MAX));
        Assert.check(inN.length > 1);

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_MAX, "MAX",
                makeParamList(inN.length));
        List<PlcNamedValue> arguments = IntStream.range(0, inN.length)
                .mapToObj(i -> new PlcNamedValue("IN" + String.valueOf(i + 1), inN[i])).collect(Collectors.toList());
        return new PlcFuncAppl(func, arguments);
    }

    /**
     * Construct a function application for a square root operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl sqrtFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_SQRT));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_SQRT, "SQRT",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a arccosine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl acosFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_ACOS));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_ACOS, "ACOS",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a arcsine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl asinFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_ASIN));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_ASIN, "ASIN",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a arctangent operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl atanFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_ATAN));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_ATAN, "ATAN",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a cosine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl cosFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_COS));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_COS, "COS",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a sine operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl sinFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_SIN));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_SIN, "SIN",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a tangent operation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl tanFuncAppl(PlcExpression in) {
        Assert.check(target.supportsOperation(PlcFuncOperation.STDLIB_TAN));

        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.STDLIB_TAN, "TAN",
                ONE_INPUT_PARAMETER);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Perform a function application to a function block.
     *
     * @param funcBlkDesc Description of the instantiated function block.
     * @param arguments Arguments of the instantiated function block.
     * @return The constructed function application.
     */
    public PlcFuncAppl funcBlockAppl(PlcFunctionBlockDescription funcBlkDesc, List<PlcNamedValue> arguments) {
        // Do some sanity checks.
        Set<String> argumentNames = arguments.stream().map(a -> a.name).collect(Sets.toSet());
        Assert.check(argumentNames.size() == arguments.size()); // No duplicate named values.
        Assert.check(funcBlkDesc.prefixParameters.keySet().containsAll(argumentNames)); // All arguments are parameters.

        return new PlcFuncAppl(funcBlkDesc, arguments);
    }
}
