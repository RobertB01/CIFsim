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
package org.eclipse.escet.cif.metamodel.cif.print;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>For Kind</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrintForKind()
 * @model
 * @generated
 */
public enum PrintForKind implements Enumerator
{
    /**
     * The '<em><b>Event</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EVENT_VALUE
     * @generated
     * @ordered
     */
    EVENT(0, "Event", "Event"),

    /**
     * The '<em><b>Time</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TIME_VALUE
     * @generated
     * @ordered
     */
    TIME(1, "Time", "Time"),

    /**
     * The '<em><b>Name</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NAME_VALUE
     * @generated
     * @ordered
     */
    NAME(2, "Name", "Name"),

    /**
     * The '<em><b>Initial</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INITIAL_VALUE
     * @generated
     * @ordered
     */
    INITIAL(3, "Initial", "Initial"),

    /**
     * The '<em><b>Final</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FINAL_VALUE
     * @generated
     * @ordered
     */
    FINAL(4, "Final", "Final");

    /**
     * The '<em><b>Event</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EVENT
     * @model name="Event"
     * @generated
     * @ordered
     */
    public static final int EVENT_VALUE = 0;

    /**
     * The '<em><b>Time</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TIME
     * @model name="Time"
     * @generated
     * @ordered
     */
    public static final int TIME_VALUE = 1;

    /**
     * The '<em><b>Name</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NAME
     * @model name="Name"
     * @generated
     * @ordered
     */
    public static final int NAME_VALUE = 2;

    /**
     * The '<em><b>Initial</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INITIAL
     * @model name="Initial"
     * @generated
     * @ordered
     */
    public static final int INITIAL_VALUE = 3;

    /**
     * The '<em><b>Final</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FINAL
     * @model name="Final"
     * @generated
     * @ordered
     */
    public static final int FINAL_VALUE = 4;

    /**
     * An array of all the '<em><b>For Kind</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final PrintForKind[] VALUES_ARRAY =
        new PrintForKind[]
        {
            EVENT,
            TIME,
            NAME,
            INITIAL,
            FINAL,
        };

    /**
     * A public read-only list of all the '<em><b>For Kind</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<PrintForKind> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>For Kind</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static PrintForKind get(String literal)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            PrintForKind result = VALUES_ARRAY[i];
            if (result.toString().equals(literal))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>For Kind</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static PrintForKind getByName(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            PrintForKind result = VALUES_ARRAY[i];
            if (result.getName().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>For Kind</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static PrintForKind get(int value)
    {
        switch (value)
        {
            case EVENT_VALUE: return EVENT;
            case TIME_VALUE: return TIME;
            case NAME_VALUE: return NAME;
            case INITIAL_VALUE: return INITIAL;
            case FINAL_VALUE: return FINAL;
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
    private PrintForKind(int value, String name, String literal)
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
    
} //PrintForKind
