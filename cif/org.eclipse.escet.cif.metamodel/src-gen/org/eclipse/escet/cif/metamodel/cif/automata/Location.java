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
package org.eclipse.escet.cif.metamodel.cif.automata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Invariant;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Location</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getInitials <em>Initials</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getInvariants <em>Invariants</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getEdges <em>Edges</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getMarkeds <em>Markeds</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#isUrgent <em>Urgent</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getEquations <em>Equations</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getLocation()
 * @model
 * @generated
 */
public interface Location extends AnnotatedObject
{
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getLocation_Name()
     * @model dataType="org.eclipse.escet.cif.metamodel.cif.CifIdentifier"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Initials</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Initials</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getLocation_Initials()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getInitials();

    /**
     * Returns the value of the '<em><b>Invariants</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.Invariant}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Invariants</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getLocation_Invariants()
     * @model containment="true"
     * @generated
     */
    EList<Invariant> getInvariants();

    /**
     * Returns the value of the '<em><b>Edges</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.automata.Edge}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Edges</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getLocation_Edges()
     * @model containment="true"
     * @generated
     */
    EList<Edge> getEdges();

    /**
     * Returns the value of the '<em><b>Markeds</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Markeds</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getLocation_Markeds()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getMarkeds();

    /**
     * Returns the value of the '<em><b>Urgent</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Urgent</em>' attribute.
     * @see #setUrgent(boolean)
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getLocation_Urgent()
     * @model required="true"
     * @generated
     */
    boolean isUrgent();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#isUrgent <em>Urgent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Urgent</em>' attribute.
     * @see #isUrgent()
     * @generated
     */
    void setUrgent(boolean value);

    /**
     * Returns the value of the '<em><b>Equations</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.Equation}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Equations</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getLocation_Equations()
     * @model containment="true"
     * @generated
     */
    EList<Equation> getEquations();

} // Location
