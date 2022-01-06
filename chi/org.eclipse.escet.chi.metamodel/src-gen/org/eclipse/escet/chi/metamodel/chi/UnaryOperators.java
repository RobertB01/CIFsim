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
 * A representation of the literals of the enumeration '<em><b>Unary Operators</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getUnaryOperators()
 * @model
 * @generated
 */
public enum UnaryOperators implements Enumerator
{
    /**
     * The '<em><b>Inverse</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INVERSE_VALUE
     * @generated
     * @ordered
     */
    INVERSE(0, "Inverse", "Inverse"),

    /**
     * The '<em><b>Negate</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NEGATE_VALUE
     * @generated
     * @ordered
     */
    NEGATE(1, "Negate", "Negate"),

    /**
     * The '<em><b>Sample</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SAMPLE_VALUE
     * @generated
     * @ordered
     */
    SAMPLE(3, "Sample", "Sample"),

    /**
     * The '<em><b>Plus</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PLUS_VALUE
     * @generated
     * @ordered
     */
    PLUS(4, "Plus", "Plus");

    /**
     * The '<em><b>Inverse</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INVERSE
     * @model name="Inverse"
     * @generated
     * @ordered
     */
    public static final int INVERSE_VALUE = 0;

    /**
     * The '<em><b>Negate</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NEGATE
     * @model name="Negate"
     * @generated
     * @ordered
     */
    public static final int NEGATE_VALUE = 1;

    /**
     * The '<em><b>Sample</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SAMPLE
     * @model name="Sample"
     * @generated
     * @ordered
     */
    public static final int SAMPLE_VALUE = 3;

    /**
     * The '<em><b>Plus</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PLUS
     * @model name="Plus"
     * @generated
     * @ordered
     */
    public static final int PLUS_VALUE = 4;

    /**
     * An array of all the '<em><b>Unary Operators</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final UnaryOperators[] VALUES_ARRAY =
        new UnaryOperators[]
        {
            INVERSE,
            NEGATE,
            SAMPLE,
            PLUS,
        };

    /**
     * A public read-only list of all the '<em><b>Unary Operators</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<UnaryOperators> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Unary Operators</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static UnaryOperators get(String literal)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            UnaryOperators result = VALUES_ARRAY[i];
            if (result.toString().equals(literal))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Unary Operators</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static UnaryOperators getByName(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            UnaryOperators result = VALUES_ARRAY[i];
            if (result.getName().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Unary Operators</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static UnaryOperators get(int value)
    {
        switch (value)
        {
            case INVERSE_VALUE: return INVERSE;
            case NEGATE_VALUE: return NEGATE;
            case SAMPLE_VALUE: return SAMPLE;
            case PLUS_VALUE: return PLUS;
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
    private UnaryOperators(int value, String name, String literal)
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
    
} //UnaryOperators
