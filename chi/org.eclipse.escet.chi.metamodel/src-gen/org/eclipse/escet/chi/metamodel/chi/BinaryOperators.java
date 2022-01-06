/**
 * Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.chi.metamodel.chi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Binary Operators</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getBinaryOperators()
 * @model
 * @generated
 */
public enum BinaryOperators implements Enumerator
{
    /**
     * The '<em><b>Addition</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ADDITION_VALUE
     * @generated
     * @ordered
     */
    ADDITION(0, "Addition", "Addition"),

    /**
     * The '<em><b>Conjunction</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONJUNCTION_VALUE
     * @generated
     * @ordered
     */
    CONJUNCTION(2, "Conjunction", "Conjunction"),

    /**
     * The '<em><b>Disjunction</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DISJUNCTION_VALUE
     * @generated
     * @ordered
     */
    DISJUNCTION(3, "Disjunction", "Disjunction"),

    /**
     * The '<em><b>Division</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DIVISION_VALUE
     * @generated
     * @ordered
     */
    DIVISION(4, "Division", "Division"),

    /**
     * The '<em><b>Element Test</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ELEMENT_TEST_VALUE
     * @generated
     * @ordered
     */
    ELEMENT_TEST(5, "ElementTest", "ElementTest"),

    /**
     * The '<em><b>Equal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EQUAL_VALUE
     * @generated
     * @ordered
     */
    EQUAL(6, "Equal", "Equal"),

    /**
     * The '<em><b>Field Projection</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FIELD_PROJECTION_VALUE
     * @generated
     * @ordered
     */
    FIELD_PROJECTION(1, "FieldProjection", "FieldProjection"),

    /**
     * The '<em><b>Floor Division</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FLOOR_DIVISION_VALUE
     * @generated
     * @ordered
     */
    FLOOR_DIVISION(7, "FloorDivision", "FloorDivision"),

    /**
     * The '<em><b>Greater Equal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GREATER_EQUAL_VALUE
     * @generated
     * @ordered
     */
    GREATER_EQUAL(8, "GreaterEqual", "GreaterEqual"),

    /**
     * The '<em><b>Greater Than</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GREATER_THAN_VALUE
     * @generated
     * @ordered
     */
    GREATER_THAN(9, "GreaterThan", "GreaterThan"),

    /**
     * The '<em><b>Less Than</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LESS_THAN_VALUE
     * @generated
     * @ordered
     */
    LESS_THAN(12, "LessThan", "LessThan"),

    /**
     * The '<em><b>Less Equal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LESS_EQUAL_VALUE
     * @generated
     * @ordered
     */
    LESS_EQUAL(13, "LessEqual", "LessEqual"),

    /**
     * The '<em><b>Modulus</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MODULUS_VALUE
     * @generated
     * @ordered
     */
    MODULUS(17, "Modulus", "Modulus"),

    /**
     * The '<em><b>Multiplication</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MULTIPLICATION_VALUE
     * @generated
     * @ordered
     */
    MULTIPLICATION(18, "Multiplication", "Multiplication"),

    /**
     * The '<em><b>Not Equal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NOT_EQUAL_VALUE
     * @generated
     * @ordered
     */
    NOT_EQUAL(19, "NotEqual", "NotEqual"),

    /**
     * The '<em><b>Power</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POWER_VALUE
     * @generated
     * @ordered
     */
    POWER(20, "Power", "Power"),

    /**
     * The '<em><b>Projection</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PROJECTION_VALUE
     * @generated
     * @ordered
     */
    PROJECTION(25, "Projection", "Projection"),

    /**
     * The '<em><b>Subset</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBSET_VALUE
     * @generated
     * @ordered
     */
    SUBSET(22, "Subset", "Subset"),

    /**
     * The '<em><b>Subtraction</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBTRACTION_VALUE
     * @generated
     * @ordered
     */
    SUBTRACTION(23, "Subtraction", "Subtraction");

    /**
     * The '<em><b>Addition</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ADDITION
     * @model name="Addition"
     * @generated
     * @ordered
     */
    public static final int ADDITION_VALUE = 0;

    /**
     * The '<em><b>Conjunction</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONJUNCTION
     * @model name="Conjunction"
     * @generated
     * @ordered
     */
    public static final int CONJUNCTION_VALUE = 2;

    /**
     * The '<em><b>Disjunction</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DISJUNCTION
     * @model name="Disjunction"
     * @generated
     * @ordered
     */
    public static final int DISJUNCTION_VALUE = 3;

    /**
     * The '<em><b>Division</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DIVISION
     * @model name="Division"
     * @generated
     * @ordered
     */
    public static final int DIVISION_VALUE = 4;

    /**
     * The '<em><b>Element Test</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ELEMENT_TEST
     * @model name="ElementTest"
     * @generated
     * @ordered
     */
    public static final int ELEMENT_TEST_VALUE = 5;

    /**
     * The '<em><b>Equal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EQUAL
     * @model name="Equal"
     * @generated
     * @ordered
     */
    public static final int EQUAL_VALUE = 6;

    /**
     * The '<em><b>Field Projection</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FIELD_PROJECTION
     * @model name="FieldProjection"
     * @generated
     * @ordered
     */
    public static final int FIELD_PROJECTION_VALUE = 1;

    /**
     * The '<em><b>Floor Division</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FLOOR_DIVISION
     * @model name="FloorDivision"
     * @generated
     * @ordered
     */
    public static final int FLOOR_DIVISION_VALUE = 7;

    /**
     * The '<em><b>Greater Equal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GREATER_EQUAL
     * @model name="GreaterEqual"
     * @generated
     * @ordered
     */
    public static final int GREATER_EQUAL_VALUE = 8;

    /**
     * The '<em><b>Greater Than</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GREATER_THAN
     * @model name="GreaterThan"
     * @generated
     * @ordered
     */
    public static final int GREATER_THAN_VALUE = 9;

    /**
     * The '<em><b>Less Than</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LESS_THAN
     * @model name="LessThan"
     * @generated
     * @ordered
     */
    public static final int LESS_THAN_VALUE = 12;

    /**
     * The '<em><b>Less Equal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LESS_EQUAL
     * @model name="LessEqual"
     * @generated
     * @ordered
     */
    public static final int LESS_EQUAL_VALUE = 13;

    /**
     * The '<em><b>Modulus</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MODULUS
     * @model name="Modulus"
     * @generated
     * @ordered
     */
    public static final int MODULUS_VALUE = 17;

    /**
     * The '<em><b>Multiplication</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MULTIPLICATION
     * @model name="Multiplication"
     * @generated
     * @ordered
     */
    public static final int MULTIPLICATION_VALUE = 18;

    /**
     * The '<em><b>Not Equal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NOT_EQUAL
     * @model name="NotEqual"
     * @generated
     * @ordered
     */
    public static final int NOT_EQUAL_VALUE = 19;

    /**
     * The '<em><b>Power</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POWER
     * @model name="Power"
     * @generated
     * @ordered
     */
    public static final int POWER_VALUE = 20;

    /**
     * The '<em><b>Projection</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PROJECTION
     * @model name="Projection"
     * @generated
     * @ordered
     */
    public static final int PROJECTION_VALUE = 25;

    /**
     * The '<em><b>Subset</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBSET
     * @model name="Subset"
     * @generated
     * @ordered
     */
    public static final int SUBSET_VALUE = 22;

    /**
     * The '<em><b>Subtraction</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBTRACTION
     * @model name="Subtraction"
     * @generated
     * @ordered
     */
    public static final int SUBTRACTION_VALUE = 23;

    /**
     * An array of all the '<em><b>Binary Operators</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final BinaryOperators[] VALUES_ARRAY =
        new BinaryOperators[]
        {
            ADDITION,
            CONJUNCTION,
            DISJUNCTION,
            DIVISION,
            ELEMENT_TEST,
            EQUAL,
            FIELD_PROJECTION,
            FLOOR_DIVISION,
            GREATER_EQUAL,
            GREATER_THAN,
            LESS_THAN,
            LESS_EQUAL,
            MODULUS,
            MULTIPLICATION,
            NOT_EQUAL,
            POWER,
            PROJECTION,
            SUBSET,
            SUBTRACTION,
        };

    /**
     * A public read-only list of all the '<em><b>Binary Operators</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<BinaryOperators> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Binary Operators</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BinaryOperators get(String literal)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            BinaryOperators result = VALUES_ARRAY[i];
            if (result.toString().equals(literal))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Binary Operators</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BinaryOperators getByName(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            BinaryOperators result = VALUES_ARRAY[i];
            if (result.getName().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Binary Operators</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BinaryOperators get(int value)
    {
        switch (value)
        {
            case ADDITION_VALUE: return ADDITION;
            case CONJUNCTION_VALUE: return CONJUNCTION;
            case DISJUNCTION_VALUE: return DISJUNCTION;
            case DIVISION_VALUE: return DIVISION;
            case ELEMENT_TEST_VALUE: return ELEMENT_TEST;
            case EQUAL_VALUE: return EQUAL;
            case FIELD_PROJECTION_VALUE: return FIELD_PROJECTION;
            case FLOOR_DIVISION_VALUE: return FLOOR_DIVISION;
            case GREATER_EQUAL_VALUE: return GREATER_EQUAL;
            case GREATER_THAN_VALUE: return GREATER_THAN;
            case LESS_THAN_VALUE: return LESS_THAN;
            case LESS_EQUAL_VALUE: return LESS_EQUAL;
            case MODULUS_VALUE: return MODULUS;
            case MULTIPLICATION_VALUE: return MULTIPLICATION;
            case NOT_EQUAL_VALUE: return NOT_EQUAL;
            case POWER_VALUE: return POWER;
            case PROJECTION_VALUE: return PROJECTION;
            case SUBSET_VALUE: return SUBSET;
            case SUBTRACTION_VALUE: return SUBTRACTION;
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
    private BinaryOperators(int value, String name, String literal)
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
    
} //BinaryOperators
