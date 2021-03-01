/**
 * Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Inv Kind</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getInvKind()
 * @model
 * @generated
 */
public enum InvKind implements Enumerator
{
    /**
     * The '<em><b>State</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #STATE_VALUE
     * @generated
     * @ordered
     */
    STATE(0, "State", "State"),

    /**
     * The '<em><b>Event Needs</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EVENT_NEEDS_VALUE
     * @generated
     * @ordered
     */
    EVENT_NEEDS(1, "EventNeeds", "EventNeeds"),

    /**
     * The '<em><b>Event Disables</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EVENT_DISABLES_VALUE
     * @generated
     * @ordered
     */
    EVENT_DISABLES(2, "EventDisables", "EventDisables");

    /**
     * The '<em><b>State</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #STATE
     * @model name="State"
     * @generated
     * @ordered
     */
    public static final int STATE_VALUE = 0;

    /**
     * The '<em><b>Event Needs</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EVENT_NEEDS
     * @model name="EventNeeds"
     * @generated
     * @ordered
     */
    public static final int EVENT_NEEDS_VALUE = 1;

    /**
     * The '<em><b>Event Disables</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EVENT_DISABLES
     * @model name="EventDisables"
     * @generated
     * @ordered
     */
    public static final int EVENT_DISABLES_VALUE = 2;

    /**
     * An array of all the '<em><b>Inv Kind</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final InvKind[] VALUES_ARRAY =
        new InvKind[]
        {
            STATE,
            EVENT_NEEDS,
            EVENT_DISABLES,
        };

    /**
     * A public read-only list of all the '<em><b>Inv Kind</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<InvKind> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Inv Kind</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static InvKind get(String literal)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            InvKind result = VALUES_ARRAY[i];
            if (result.toString().equals(literal))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Inv Kind</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static InvKind getByName(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            InvKind result = VALUES_ARRAY[i];
            if (result.getName().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Inv Kind</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static InvKind get(int value)
    {
        switch (value)
        {
            case STATE_VALUE: return STATE;
            case EVENT_NEEDS_VALUE: return EVENT_NEEDS;
            case EVENT_DISABLES_VALUE: return EVENT_DISABLES;
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
    private InvKind(int value, String name, String literal)
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
    
} //InvKind
