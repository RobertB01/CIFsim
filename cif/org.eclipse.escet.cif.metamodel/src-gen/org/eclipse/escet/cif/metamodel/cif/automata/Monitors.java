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
package org.eclipse.escet.cif.metamodel.cif.automata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Monitors</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Monitors#getEvents <em>Events</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getMonitors()
 * @model
 * @generated
 */
public interface Monitors extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Events</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Events</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getMonitors_Events()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getEvents();

} // Monitors
