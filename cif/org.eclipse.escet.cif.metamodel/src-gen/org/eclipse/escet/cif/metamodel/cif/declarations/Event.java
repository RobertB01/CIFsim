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
package org.eclipse.escet.cif.metamodel.cif.declarations;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.Event#getControllable <em>Controllable</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.Event#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getEvent()
 * @model
 * @generated
 */
public interface Event extends Declaration
{
    /**
     * Returns the value of the '<em><b>Controllable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Controllable</em>' attribute.
     * @see #setControllable(Boolean)
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getEvent_Controllable()
     * @model
     * @generated
     */
    Boolean getControllable();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Event#getControllable <em>Controllable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Controllable</em>' attribute.
     * @see #getControllable()
     * @generated
     */
    void setControllable(Boolean value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(CifType)
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getEvent_Type()
     * @model containment="true"
     * @generated
     */
    CifType getType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Event#getType <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' containment reference.
     * @see #getType()
     * @generated
     */
    void setType(CifType value);

} // Event
