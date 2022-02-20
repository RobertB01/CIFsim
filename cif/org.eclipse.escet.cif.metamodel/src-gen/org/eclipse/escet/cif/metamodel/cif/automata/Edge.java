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
package org.eclipse.escet.cif.metamodel.cif.automata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Edge</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getEvents <em>Events</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getUpdates <em>Updates</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#isUrgent <em>Urgent</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getEdge()
 * @model
 * @generated
 */
public interface Edge extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target</em>' reference.
     * @see #setTarget(Location)
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getEdge_Target()
     * @model
     * @generated
     */
    Location getTarget();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getTarget <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(Location value);

    /**
     * Returns the value of the '<em><b>Events</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Events</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getEdge_Events()
     * @model containment="true"
     * @generated
     */
    EList<EdgeEvent> getEvents();

    /**
     * Returns the value of the '<em><b>Guards</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Guards</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getEdge_Guards()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getGuards();

    /**
     * Returns the value of the '<em><b>Updates</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.automata.Update}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Updates</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getEdge_Updates()
     * @model containment="true"
     * @generated
     */
    EList<Update> getUpdates();

    /**
     * Returns the value of the '<em><b>Urgent</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Urgent</em>' attribute.
     * @see #setUrgent(boolean)
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getEdge_Urgent()
     * @model required="true"
     * @generated
     */
    boolean isUrgent();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#isUrgent <em>Urgent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Urgent</em>' attribute.
     * @see #isUrgent()
     * @generated
     */
    void setUrgent(boolean value);

} // Edge
