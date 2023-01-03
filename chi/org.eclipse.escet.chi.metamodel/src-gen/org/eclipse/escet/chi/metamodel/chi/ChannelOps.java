/**
 * Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
 * A representation of the literals of the enumeration '<em><b>Channel Ops</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getChannelOps()
 * @model
 * @generated
 */
public enum ChannelOps implements Enumerator
{
    /**
     * The '<em><b>Receive</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RECEIVE_VALUE
     * @generated
     * @ordered
     */
    RECEIVE(1, "Receive", "Receive"),

    /**
     * The '<em><b>Send</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SEND_VALUE
     * @generated
     * @ordered
     */
    SEND(2, "Send", "Send"),

    /**
     * The '<em><b>Send Receive</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SEND_RECEIVE_VALUE
     * @generated
     * @ordered
     */
    SEND_RECEIVE(3, "SendReceive", "SendReceive");

    /**
     * The '<em><b>Receive</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RECEIVE
     * @model name="Receive"
     * @generated
     * @ordered
     */
    public static final int RECEIVE_VALUE = 1;

    /**
     * The '<em><b>Send</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SEND
     * @model name="Send"
     * @generated
     * @ordered
     */
    public static final int SEND_VALUE = 2;

    /**
     * The '<em><b>Send Receive</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SEND_RECEIVE
     * @model name="SendReceive"
     * @generated
     * @ordered
     */
    public static final int SEND_RECEIVE_VALUE = 3;

    /**
     * An array of all the '<em><b>Channel Ops</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final ChannelOps[] VALUES_ARRAY =
        new ChannelOps[]
        {
            RECEIVE,
            SEND,
            SEND_RECEIVE,
        };

    /**
     * A public read-only list of all the '<em><b>Channel Ops</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<ChannelOps> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Channel Ops</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static ChannelOps get(String literal)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            ChannelOps result = VALUES_ARRAY[i];
            if (result.toString().equals(literal))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Channel Ops</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static ChannelOps getByName(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            ChannelOps result = VALUES_ARRAY[i];
            if (result.getName().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Channel Ops</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static ChannelOps get(int value)
    {
        switch (value)
        {
            case RECEIVE_VALUE: return RECEIVE;
            case SEND_VALUE: return SEND;
            case SEND_RECEIVE_VALUE: return SEND_RECEIVE;
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
    private ChannelOps(int value, String name, String literal)
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
    
} //ChannelOps
