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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Binary Operator</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getBinaryOperator()
 * @model
 * @generated
 */
public enum BinaryOperator implements Enumerator
{
    /**
     * The '<em><b>Disjunction</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DISJUNCTION_VALUE
     * @generated
     * @ordered
     */
    DISJUNCTION(0, "Disjunction", "Disjunction"),

    /**
     * The '<em><b>Implication</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #IMPLICATION_VALUE
     * @generated
     * @ordered
     */
    IMPLICATION(1, "Implication", "Implication"),

    /**
     * The '<em><b>Bi Conditional</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BI_CONDITIONAL_VALUE
     * @generated
     * @ordered
     */
    BI_CONDITIONAL(2, "BiConditional", "BiConditional"),

    /**
     * The '<em><b>Conjunction</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONJUNCTION_VALUE
     * @generated
     * @ordered
     */
    CONJUNCTION(3, "Conjunction", "Conjunction"),

    /**
     * The '<em><b>Less Than</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LESS_THAN_VALUE
     * @generated
     * @ordered
     */
    LESS_THAN(4, "LessThan", "LessThan"),

    /**
     * The '<em><b>Less Equal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LESS_EQUAL_VALUE
     * @generated
     * @ordered
     */
    LESS_EQUAL(5, "LessEqual", "LessEqual"),

    /**
     * The '<em><b>Greater Than</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GREATER_THAN_VALUE
     * @generated
     * @ordered
     */
    GREATER_THAN(6, "GreaterThan", "GreaterThan"),

    /**
     * The '<em><b>Greater Equal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GREATER_EQUAL_VALUE
     * @generated
     * @ordered
     */
    GREATER_EQUAL(7, "GreaterEqual", "GreaterEqual"),

    /**
     * The '<em><b>Equal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EQUAL_VALUE
     * @generated
     * @ordered
     */
    EQUAL(8, "Equal", "Equal"),

    /**
     * The '<em><b>Unequal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNEQUAL_VALUE
     * @generated
     * @ordered
     */
    UNEQUAL(9, "Unequal", "Unequal"),

    /**
     * The '<em><b>Modulus</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MODULUS_VALUE
     * @generated
     * @ordered
     */
    MODULUS(11, "Modulus", "Modulus"),

    /**
     * The '<em><b>Integer Division</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INTEGER_DIVISION_VALUE
     * @generated
     * @ordered
     */
    INTEGER_DIVISION(12, "IntegerDivision", "IntegerDivision"),

    /**
     * The '<em><b>Multiplication</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MULTIPLICATION_VALUE
     * @generated
     * @ordered
     */
    MULTIPLICATION(14, "Multiplication", "Multiplication"),

    /**
     * The '<em><b>Subtraction</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBTRACTION_VALUE
     * @generated
     * @ordered
     */
    SUBTRACTION(15, "Subtraction", "Subtraction"),

    /**
     * The '<em><b>Addition</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ADDITION_VALUE
     * @generated
     * @ordered
     */
    ADDITION(16, "Addition", "Addition"),

    /**
     * The '<em><b>Subset</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBSET_VALUE
     * @generated
     * @ordered
     */
    SUBSET(10, "Subset", "Subset"),

    /**
     * The '<em><b>Element Of</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ELEMENT_OF_VALUE
     * @generated
     * @ordered
     */
    ELEMENT_OF(13, "ElementOf", "ElementOf"),

    /**
     * The '<em><b>Division</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DIVISION_VALUE
     * @generated
     * @ordered
     */
    DIVISION(17, "Division", "Division");

    /**
     * The '<em><b>Disjunction</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DISJUNCTION
     * @model name="Disjunction"
     * @generated
     * @ordered
     */
    public static final int DISJUNCTION_VALUE = 0;

    /**
     * The '<em><b>Implication</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #IMPLICATION
     * @model name="Implication"
     * @generated
     * @ordered
     */
    public static final int IMPLICATION_VALUE = 1;

    /**
     * The '<em><b>Bi Conditional</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BI_CONDITIONAL
     * @model name="BiConditional"
     * @generated
     * @ordered
     */
    public static final int BI_CONDITIONAL_VALUE = 2;

    /**
     * The '<em><b>Conjunction</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONJUNCTION
     * @model name="Conjunction"
     * @generated
     * @ordered
     */
    public static final int CONJUNCTION_VALUE = 3;

    /**
     * The '<em><b>Less Than</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LESS_THAN
     * @model name="LessThan"
     * @generated
     * @ordered
     */
    public static final int LESS_THAN_VALUE = 4;

    /**
     * The '<em><b>Less Equal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LESS_EQUAL
     * @model name="LessEqual"
     * @generated
     * @ordered
     */
    public static final int LESS_EQUAL_VALUE = 5;

    /**
     * The '<em><b>Greater Than</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GREATER_THAN
     * @model name="GreaterThan"
     * @generated
     * @ordered
     */
    public static final int GREATER_THAN_VALUE = 6;

    /**
     * The '<em><b>Greater Equal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GREATER_EQUAL
     * @model name="GreaterEqual"
     * @generated
     * @ordered
     */
    public static final int GREATER_EQUAL_VALUE = 7;

    /**
     * The '<em><b>Equal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EQUAL
     * @model name="Equal"
     * @generated
     * @ordered
     */
    public static final int EQUAL_VALUE = 8;

    /**
     * The '<em><b>Unequal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNEQUAL
     * @model name="Unequal"
     * @generated
     * @ordered
     */
    public static final int UNEQUAL_VALUE = 9;

    /**
     * The '<em><b>Modulus</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MODULUS
     * @model name="Modulus"
     * @generated
     * @ordered
     */
    public static final int MODULUS_VALUE = 11;

    /**
     * The '<em><b>Integer Division</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INTEGER_DIVISION
     * @model name="IntegerDivision"
     * @generated
     * @ordered
     */
    public static final int INTEGER_DIVISION_VALUE = 12;

    /**
     * The '<em><b>Multiplication</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MULTIPLICATION
     * @model name="Multiplication"
     * @generated
     * @ordered
     */
    public static final int MULTIPLICATION_VALUE = 14;

    /**
     * The '<em><b>Subtraction</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBTRACTION
     * @model name="Subtraction"
     * @generated
     * @ordered
     */
    public static final int SUBTRACTION_VALUE = 15;

    /**
     * The '<em><b>Addition</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ADDITION
     * @model name="Addition"
     * @generated
     * @ordered
     */
    public static final int ADDITION_VALUE = 16;

    /**
     * The '<em><b>Subset</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBSET
     * @model name="Subset"
     * @generated
     * @ordered
     */
    public static final int SUBSET_VALUE = 10;

    /**
     * The '<em><b>Element Of</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ELEMENT_OF
     * @model name="ElementOf"
     * @generated
     * @ordered
     */
    public static final int ELEMENT_OF_VALUE = 13;

    /**
     * The '<em><b>Division</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DIVISION
     * @model name="Division"
     * @generated
     * @ordered
     */
    public static final int DIVISION_VALUE = 17;

    /**
     * An array of all the '<em><b>Binary Operator</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final BinaryOperator[] VALUES_ARRAY =
        new BinaryOperator[]
        {
            DISJUNCTION,
            IMPLICATION,
            BI_CONDITIONAL,
            CONJUNCTION,
            LESS_THAN,
            LESS_EQUAL,
            GREATER_THAN,
            GREATER_EQUAL,
            EQUAL,
            UNEQUAL,
            MODULUS,
            INTEGER_DIVISION,
            MULTIPLICATION,
            SUBTRACTION,
            ADDITION,
            SUBSET,
            ELEMENT_OF,
            DIVISION,
        };

    /**
     * A public read-only list of all the '<em><b>Binary Operator</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<BinaryOperator> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Binary Operator</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BinaryOperator get(String literal)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            BinaryOperator result = VALUES_ARRAY[i];
            if (result.toString().equals(literal))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Binary Operator</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BinaryOperator getByName(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            BinaryOperator result = VALUES_ARRAY[i];
            if (result.getName().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Binary Operator</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BinaryOperator get(int value)
    {
        switch (value)
        {
            case DISJUNCTION_VALUE: return DISJUNCTION;
            case IMPLICATION_VALUE: return IMPLICATION;
            case BI_CONDITIONAL_VALUE: return BI_CONDITIONAL;
            case CONJUNCTION_VALUE: return CONJUNCTION;
            case LESS_THAN_VALUE: return LESS_THAN;
            case LESS_EQUAL_VALUE: return LESS_EQUAL;
            case GREATER_THAN_VALUE: return GREATER_THAN;
            case GREATER_EQUAL_VALUE: return GREATER_EQUAL;
            case EQUAL_VALUE: return EQUAL;
            case UNEQUAL_VALUE: return UNEQUAL;
            case MODULUS_VALUE: return MODULUS;
            case INTEGER_DIVISION_VALUE: return INTEGER_DIVISION;
            case MULTIPLICATION_VALUE: return MULTIPLICATION;
            case SUBTRACTION_VALUE: return SUBTRACTION;
            case ADDITION_VALUE: return ADDITION;
            case SUBSET_VALUE: return SUBSET;
            case ELEMENT_OF_VALUE: return ELEMENT_OF;
            case DIVISION_VALUE: return DIVISION;
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
    private BinaryOperator(int value, String name, String literal)
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
    
} //BinaryOperator
