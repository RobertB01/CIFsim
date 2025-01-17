/**
 * Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
 * 
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 * 
 * This program and the accompanying materials are made available under the terms
 * of the MIT License which is available at https://opensource.org/licenses/MIT
 * 
 * SPDX-License-Identifier: MIT
 * 
 * Disable Eclipse Java formatter for generated code file:
 * @formatter:off
 */
package org.eclipse.escet.cif.metamodel.cif.expressions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Std Lib Function</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getStdLibFunction()
 * @model
 * @generated
 */
public enum StdLibFunction implements Enumerator
{
    /**
     * The '<em><b>Minimum</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINIMUM_VALUE
     * @generated
     * @ordered
     */
    MINIMUM(1, "Minimum", "Minimum"),

    /**
     * The '<em><b>Maximum</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MAXIMUM_VALUE
     * @generated
     * @ordered
     */
    MAXIMUM(2, "Maximum", "Maximum"),

    /**
     * The '<em><b>Power</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POWER_VALUE
     * @generated
     * @ordered
     */
    POWER(3, "Power", "Power"),

    /**
     * The '<em><b>Sign</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SIGN_VALUE
     * @generated
     * @ordered
     */
    SIGN(4, "Sign", "Sign"),

    /**
     * The '<em><b>Cbrt</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CBRT_VALUE
     * @generated
     * @ordered
     */
    CBRT(5, "Cbrt", "Cbrt"),

    /**
     * The '<em><b>Ceil</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CEIL_VALUE
     * @generated
     * @ordered
     */
    CEIL(6, "Ceil", "Ceil"),

    /**
     * The '<em><b>Delete</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DELETE_VALUE
     * @generated
     * @ordered
     */
    DELETE(7, "Delete", "Delete"),

    /**
     * The '<em><b>Empty</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EMPTY_VALUE
     * @generated
     * @ordered
     */
    EMPTY(8, "Empty", "Empty"),

    /**
     * The '<em><b>Exp</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EXP_VALUE
     * @generated
     * @ordered
     */
    EXP(9, "Exp", "Exp"),

    /**
     * The '<em><b>Floor</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FLOOR_VALUE
     * @generated
     * @ordered
     */
    FLOOR(10, "Floor", "Floor"),

    /**
     * The '<em><b>Ln</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LN_VALUE
     * @generated
     * @ordered
     */
    LN(15, "Ln", "Ln"),

    /**
     * The '<em><b>Log</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LOG_VALUE
     * @generated
     * @ordered
     */
    LOG(16, "Log", "Log"),

    /**
     * The '<em><b>Pop</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POP_VALUE
     * @generated
     * @ordered
     */
    POP(17, "Pop", "Pop"),

    /**
     * The '<em><b>Round</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ROUND_VALUE
     * @generated
     * @ordered
     */
    ROUND(19, "Round", "Round"),

    /**
     * The '<em><b>Size</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SIZE_VALUE
     * @generated
     * @ordered
     */
    SIZE(13, "Size", "Size"),

    /**
     * The '<em><b>Sqrt</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SQRT_VALUE
     * @generated
     * @ordered
     */
    SQRT(22, "Sqrt", "Sqrt"),

    /**
     * The '<em><b>Acosh</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ACOSH_VALUE
     * @generated
     * @ordered
     */
    ACOSH(23, "Acosh", "Acosh"),

    /**
     * The '<em><b>Acos</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ACOS_VALUE
     * @generated
     * @ordered
     */
    ACOS(24, "Acos", "Acos"),

    /**
     * The '<em><b>Asinh</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ASINH_VALUE
     * @generated
     * @ordered
     */
    ASINH(25, "Asinh", "Asinh"),

    /**
     * The '<em><b>Asin</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ASIN_VALUE
     * @generated
     * @ordered
     */
    ASIN(26, "Asin", "Asin"),

    /**
     * The '<em><b>Atanh</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ATANH_VALUE
     * @generated
     * @ordered
     */
    ATANH(27, "Atanh", "Atanh"),

    /**
     * The '<em><b>Atan</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ATAN_VALUE
     * @generated
     * @ordered
     */
    ATAN(28, "Atan", "Atan"),

    /**
     * The '<em><b>Cosh</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #COSH_VALUE
     * @generated
     * @ordered
     */
    COSH(29, "Cosh", "Cosh"),

    /**
     * The '<em><b>Cos</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #COS_VALUE
     * @generated
     * @ordered
     */
    COS(30, "Cos", "Cos"),

    /**
     * The '<em><b>Sinh</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SINH_VALUE
     * @generated
     * @ordered
     */
    SINH(31, "Sinh", "Sinh"),

    /**
     * The '<em><b>Sin</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SIN_VALUE
     * @generated
     * @ordered
     */
    SIN(32, "Sin", "Sin"),

    /**
     * The '<em><b>Tanh</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TANH_VALUE
     * @generated
     * @ordered
     */
    TANH(33, "Tanh", "Tanh"),

    /**
     * The '<em><b>Tan</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TAN_VALUE
     * @generated
     * @ordered
     */
    TAN(34, "Tan", "Tan"),

    /**
     * The '<em><b>Abs</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ABS_VALUE
     * @generated
     * @ordered
     */
    ABS(0, "Abs", "Abs"),

    /**
     * The '<em><b>Bernoulli</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BERNOULLI_VALUE
     * @generated
     * @ordered
     */
    BERNOULLI(35, "Bernoulli", "Bernoulli"),

    /**
     * The '<em><b>Beta</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BETA_VALUE
     * @generated
     * @ordered
     */
    BETA(36, "Beta", "Beta"),

    /**
     * The '<em><b>Binomial</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BINOMIAL_VALUE
     * @generated
     * @ordered
     */
    BINOMIAL(37, "Binomial", "Binomial"),

    /**
     * The '<em><b>Constant</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONSTANT_VALUE
     * @generated
     * @ordered
     */
    CONSTANT(38, "Constant", "Constant"),

    /**
     * The '<em><b>Erlang</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ERLANG_VALUE
     * @generated
     * @ordered
     */
    ERLANG(39, "Erlang", "Erlang"),

    /**
     * The '<em><b>Exponential</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EXPONENTIAL_VALUE
     * @generated
     * @ordered
     */
    EXPONENTIAL(40, "Exponential", "Exponential"),

    /**
     * The '<em><b>Gamma</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GAMMA_VALUE
     * @generated
     * @ordered
     */
    GAMMA(41, "Gamma", "Gamma"),

    /**
     * The '<em><b>Geometric</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GEOMETRIC_VALUE
     * @generated
     * @ordered
     */
    GEOMETRIC(42, "Geometric", "Geometric"),

    /**
     * The '<em><b>Log Normal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LOG_NORMAL_VALUE
     * @generated
     * @ordered
     */
    LOG_NORMAL(43, "LogNormal", "LogNormal"),

    /**
     * The '<em><b>Normal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NORMAL_VALUE
     * @generated
     * @ordered
     */
    NORMAL(44, "Normal", "Normal"),

    /**
     * The '<em><b>Poisson</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POISSON_VALUE
     * @generated
     * @ordered
     */
    POISSON(45, "Poisson", "Poisson"),

    /**
     * The '<em><b>Random</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RANDOM_VALUE
     * @generated
     * @ordered
     */
    RANDOM(46, "Random", "Random"),

    /**
     * The '<em><b>Triangle</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TRIANGLE_VALUE
     * @generated
     * @ordered
     */
    TRIANGLE(47, "Triangle", "Triangle"),

    /**
     * The '<em><b>Uniform</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNIFORM_VALUE
     * @generated
     * @ordered
     */
    UNIFORM(48, "Uniform", "Uniform"),

    /**
     * The '<em><b>Weibull</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #WEIBULL_VALUE
     * @generated
     * @ordered
     */
    WEIBULL(49, "Weibull", "Weibull"),

    /**
     * The '<em><b>Format</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FORMAT_VALUE
     * @generated
     * @ordered
     */
    FORMAT(11, "Format", "Format"),

    /**
     * The '<em><b>Scale</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SCALE_VALUE
     * @generated
     * @ordered
     */
    SCALE(12, "Scale", "Scale");

    /**
     * The '<em><b>Minimum</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINIMUM
     * @model name="Minimum"
     * @generated
     * @ordered
     */
    public static final int MINIMUM_VALUE = 1;

    /**
     * The '<em><b>Maximum</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MAXIMUM
     * @model name="Maximum"
     * @generated
     * @ordered
     */
    public static final int MAXIMUM_VALUE = 2;

    /**
     * The '<em><b>Power</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POWER
     * @model name="Power"
     * @generated
     * @ordered
     */
    public static final int POWER_VALUE = 3;

    /**
     * The '<em><b>Sign</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SIGN
     * @model name="Sign"
     * @generated
     * @ordered
     */
    public static final int SIGN_VALUE = 4;

    /**
     * The '<em><b>Cbrt</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CBRT
     * @model name="Cbrt"
     * @generated
     * @ordered
     */
    public static final int CBRT_VALUE = 5;

    /**
     * The '<em><b>Ceil</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CEIL
     * @model name="Ceil"
     * @generated
     * @ordered
     */
    public static final int CEIL_VALUE = 6;

    /**
     * The '<em><b>Delete</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DELETE
     * @model name="Delete"
     * @generated
     * @ordered
     */
    public static final int DELETE_VALUE = 7;

    /**
     * The '<em><b>Empty</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EMPTY
     * @model name="Empty"
     * @generated
     * @ordered
     */
    public static final int EMPTY_VALUE = 8;

    /**
     * The '<em><b>Exp</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EXP
     * @model name="Exp"
     * @generated
     * @ordered
     */
    public static final int EXP_VALUE = 9;

    /**
     * The '<em><b>Floor</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FLOOR
     * @model name="Floor"
     * @generated
     * @ordered
     */
    public static final int FLOOR_VALUE = 10;

    /**
     * The '<em><b>Ln</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LN
     * @model name="Ln"
     * @generated
     * @ordered
     */
    public static final int LN_VALUE = 15;

    /**
     * The '<em><b>Log</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LOG
     * @model name="Log"
     * @generated
     * @ordered
     */
    public static final int LOG_VALUE = 16;

    /**
     * The '<em><b>Pop</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POP
     * @model name="Pop"
     * @generated
     * @ordered
     */
    public static final int POP_VALUE = 17;

    /**
     * The '<em><b>Round</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ROUND
     * @model name="Round"
     * @generated
     * @ordered
     */
    public static final int ROUND_VALUE = 19;

    /**
     * The '<em><b>Size</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SIZE
     * @model name="Size"
     * @generated
     * @ordered
     */
    public static final int SIZE_VALUE = 13;

    /**
     * The '<em><b>Sqrt</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SQRT
     * @model name="Sqrt"
     * @generated
     * @ordered
     */
    public static final int SQRT_VALUE = 22;

    /**
     * The '<em><b>Acosh</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ACOSH
     * @model name="Acosh"
     * @generated
     * @ordered
     */
    public static final int ACOSH_VALUE = 23;

    /**
     * The '<em><b>Acos</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ACOS
     * @model name="Acos"
     * @generated
     * @ordered
     */
    public static final int ACOS_VALUE = 24;

    /**
     * The '<em><b>Asinh</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ASINH
     * @model name="Asinh"
     * @generated
     * @ordered
     */
    public static final int ASINH_VALUE = 25;

    /**
     * The '<em><b>Asin</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ASIN
     * @model name="Asin"
     * @generated
     * @ordered
     */
    public static final int ASIN_VALUE = 26;

    /**
     * The '<em><b>Atanh</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ATANH
     * @model name="Atanh"
     * @generated
     * @ordered
     */
    public static final int ATANH_VALUE = 27;

    /**
     * The '<em><b>Atan</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ATAN
     * @model name="Atan"
     * @generated
     * @ordered
     */
    public static final int ATAN_VALUE = 28;

    /**
     * The '<em><b>Cosh</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #COSH
     * @model name="Cosh"
     * @generated
     * @ordered
     */
    public static final int COSH_VALUE = 29;

    /**
     * The '<em><b>Cos</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #COS
     * @model name="Cos"
     * @generated
     * @ordered
     */
    public static final int COS_VALUE = 30;

    /**
     * The '<em><b>Sinh</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SINH
     * @model name="Sinh"
     * @generated
     * @ordered
     */
    public static final int SINH_VALUE = 31;

    /**
     * The '<em><b>Sin</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SIN
     * @model name="Sin"
     * @generated
     * @ordered
     */
    public static final int SIN_VALUE = 32;

    /**
     * The '<em><b>Tanh</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TANH
     * @model name="Tanh"
     * @generated
     * @ordered
     */
    public static final int TANH_VALUE = 33;

    /**
     * The '<em><b>Tan</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TAN
     * @model name="Tan"
     * @generated
     * @ordered
     */
    public static final int TAN_VALUE = 34;

    /**
     * The '<em><b>Abs</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ABS
     * @model name="Abs"
     * @generated
     * @ordered
     */
    public static final int ABS_VALUE = 0;

    /**
     * The '<em><b>Bernoulli</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BERNOULLI
     * @model name="Bernoulli"
     * @generated
     * @ordered
     */
    public static final int BERNOULLI_VALUE = 35;

    /**
     * The '<em><b>Beta</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BETA
     * @model name="Beta"
     * @generated
     * @ordered
     */
    public static final int BETA_VALUE = 36;

    /**
     * The '<em><b>Binomial</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BINOMIAL
     * @model name="Binomial"
     * @generated
     * @ordered
     */
    public static final int BINOMIAL_VALUE = 37;

    /**
     * The '<em><b>Constant</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONSTANT
     * @model name="Constant"
     * @generated
     * @ordered
     */
    public static final int CONSTANT_VALUE = 38;

    /**
     * The '<em><b>Erlang</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ERLANG
     * @model name="Erlang"
     * @generated
     * @ordered
     */
    public static final int ERLANG_VALUE = 39;

    /**
     * The '<em><b>Exponential</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EXPONENTIAL
     * @model name="Exponential"
     * @generated
     * @ordered
     */
    public static final int EXPONENTIAL_VALUE = 40;

    /**
     * The '<em><b>Gamma</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GAMMA
     * @model name="Gamma"
     * @generated
     * @ordered
     */
    public static final int GAMMA_VALUE = 41;

    /**
     * The '<em><b>Geometric</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GEOMETRIC
     * @model name="Geometric"
     * @generated
     * @ordered
     */
    public static final int GEOMETRIC_VALUE = 42;

    /**
     * The '<em><b>Log Normal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LOG_NORMAL
     * @model name="LogNormal"
     * @generated
     * @ordered
     */
    public static final int LOG_NORMAL_VALUE = 43;

    /**
     * The '<em><b>Normal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NORMAL
     * @model name="Normal"
     * @generated
     * @ordered
     */
    public static final int NORMAL_VALUE = 44;

    /**
     * The '<em><b>Poisson</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POISSON
     * @model name="Poisson"
     * @generated
     * @ordered
     */
    public static final int POISSON_VALUE = 45;

    /**
     * The '<em><b>Random</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RANDOM
     * @model name="Random"
     * @generated
     * @ordered
     */
    public static final int RANDOM_VALUE = 46;

    /**
     * The '<em><b>Triangle</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TRIANGLE
     * @model name="Triangle"
     * @generated
     * @ordered
     */
    public static final int TRIANGLE_VALUE = 47;

    /**
     * The '<em><b>Uniform</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNIFORM
     * @model name="Uniform"
     * @generated
     * @ordered
     */
    public static final int UNIFORM_VALUE = 48;

    /**
     * The '<em><b>Weibull</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #WEIBULL
     * @model name="Weibull"
     * @generated
     * @ordered
     */
    public static final int WEIBULL_VALUE = 49;

    /**
     * The '<em><b>Format</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FORMAT
     * @model name="Format"
     * @generated
     * @ordered
     */
    public static final int FORMAT_VALUE = 11;

    /**
     * The '<em><b>Scale</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SCALE
     * @model name="Scale"
     * @generated
     * @ordered
     */
    public static final int SCALE_VALUE = 12;

    /**
     * An array of all the '<em><b>Std Lib Function</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final StdLibFunction[] VALUES_ARRAY =
        new StdLibFunction[]
        {
            MINIMUM,
            MAXIMUM,
            POWER,
            SIGN,
            CBRT,
            CEIL,
            DELETE,
            EMPTY,
            EXP,
            FLOOR,
            LN,
            LOG,
            POP,
            ROUND,
            SIZE,
            SQRT,
            ACOSH,
            ACOS,
            ASINH,
            ASIN,
            ATANH,
            ATAN,
            COSH,
            COS,
            SINH,
            SIN,
            TANH,
            TAN,
            ABS,
            BERNOULLI,
            BETA,
            BINOMIAL,
            CONSTANT,
            ERLANG,
            EXPONENTIAL,
            GAMMA,
            GEOMETRIC,
            LOG_NORMAL,
            NORMAL,
            POISSON,
            RANDOM,
            TRIANGLE,
            UNIFORM,
            WEIBULL,
            FORMAT,
            SCALE,
        };

    /**
     * A public read-only list of all the '<em><b>Std Lib Function</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<StdLibFunction> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Std Lib Function</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static StdLibFunction get(String literal)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            StdLibFunction result = VALUES_ARRAY[i];
            if (result.toString().equals(literal))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Std Lib Function</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static StdLibFunction getByName(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            StdLibFunction result = VALUES_ARRAY[i];
            if (result.getName().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Std Lib Function</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static StdLibFunction get(int value)
    {
        switch (value)
        {
            case MINIMUM_VALUE: return MINIMUM;
            case MAXIMUM_VALUE: return MAXIMUM;
            case POWER_VALUE: return POWER;
            case SIGN_VALUE: return SIGN;
            case CBRT_VALUE: return CBRT;
            case CEIL_VALUE: return CEIL;
            case DELETE_VALUE: return DELETE;
            case EMPTY_VALUE: return EMPTY;
            case EXP_VALUE: return EXP;
            case FLOOR_VALUE: return FLOOR;
            case LN_VALUE: return LN;
            case LOG_VALUE: return LOG;
            case POP_VALUE: return POP;
            case ROUND_VALUE: return ROUND;
            case SIZE_VALUE: return SIZE;
            case SQRT_VALUE: return SQRT;
            case ACOSH_VALUE: return ACOSH;
            case ACOS_VALUE: return ACOS;
            case ASINH_VALUE: return ASINH;
            case ASIN_VALUE: return ASIN;
            case ATANH_VALUE: return ATANH;
            case ATAN_VALUE: return ATAN;
            case COSH_VALUE: return COSH;
            case COS_VALUE: return COS;
            case SINH_VALUE: return SINH;
            case SIN_VALUE: return SIN;
            case TANH_VALUE: return TANH;
            case TAN_VALUE: return TAN;
            case ABS_VALUE: return ABS;
            case BERNOULLI_VALUE: return BERNOULLI;
            case BETA_VALUE: return BETA;
            case BINOMIAL_VALUE: return BINOMIAL;
            case CONSTANT_VALUE: return CONSTANT;
            case ERLANG_VALUE: return ERLANG;
            case EXPONENTIAL_VALUE: return EXPONENTIAL;
            case GAMMA_VALUE: return GAMMA;
            case GEOMETRIC_VALUE: return GEOMETRIC;
            case LOG_NORMAL_VALUE: return LOG_NORMAL;
            case NORMAL_VALUE: return NORMAL;
            case POISSON_VALUE: return POISSON;
            case RANDOM_VALUE: return RANDOM;
            case TRIANGLE_VALUE: return TRIANGLE;
            case UNIFORM_VALUE: return UNIFORM;
            case WEIBULL_VALUE: return WEIBULL;
            case FORMAT_VALUE: return FORMAT;
            case SCALE_VALUE: return SCALE;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private StdLibFunction(int value, String name, String literal)
    {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getValue()
    {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getName()
    {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getLiteral()
    {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        return literal;
    }
    
} //StdLibFunction
