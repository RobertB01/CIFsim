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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription.ExprBinding;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription.PlcParamDirection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription.PlcParameterDescription;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcCastFunction;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcSemanticFuncDescription;
import org.eclipse.escet.common.java.Assert;

/** Elementary function application construction methods for a target. */
public class PlcFunctionAppls {
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
     * Construct a function application for a negation.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl negateFuncAppl(PlcExpression in) {
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.NEGATE_OP, null,
                ONE_INPUT_PARAMETER, "-", ExprBinding.UNARY_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for a complement.
     *
     * @param in The input argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl complementFuncAppl(PlcExpression in) {
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.COMPLEMENT_OP, "NOT",
                ONE_INPUT_PARAMETER, null, ExprBinding.UNARY_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN", in)));
    }

    /**
     * Construct a function application for an exponentiation.
     *
     * @param in1 Base value argument of the function.
     * @param in2 Exponent argument of the function.
     * @return The constructed function application.
     */
    public PlcFuncAppl exptFuncAppl(PlcExpression in1, PlcExpression in2) {
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.EXP_OP, "EXPT",
                TWO_INPUT_PARAMATERS, "**", ExprBinding.EXPT_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a multiplication.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl multiplyFuncAppl(PlcExpression... inN) {
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
        PlcSemanticFuncDescription func = new PlcSemanticFuncDescription(PlcFuncOperation.UNEQUAL_OP, "NE",
                TWO_INPUT_PARAMATERS, "<>", ExprBinding.EQUAL_EXPR);
        return new PlcFuncAppl(func, List.of(new PlcNamedValue("IN1", in1), new PlcNamedValue("IN2", in2)));
    }

    /**
     * Construct a function application for a conjunction.
     *
     * @param inN Input arguments of the function, must have at least two arguments.
     * @return The constructed function application.
     */
    public PlcFuncAppl andFuncAppl(PlcExpression... inN) {
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
        PlcExpression g = greaterEqualFuncAppl(indexExpr, new PlcIntLiteral(0));
        PlcExpression in0 = addFuncAppl(indexExpr, new PlcIntLiteral(arraySize));
        PlcExpression in1 = indexExpr;
        return selFuncAppl(g, in0, in1);
    }
}
