/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.common.position.metamodel.position;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.PositionObject#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPositionObject()
 * @model abstract="true"
 * @generated
 */
public interface PositionObject extends EObject
{
    /**
     * Returns the value of the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Position</em>' containment reference.
     * @see #setPosition(Position)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPositionObject_Position()
     * @model containment="true"
     * @generated
     */
    Position getPosition();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.PositionObject#getPosition <em>Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Position</em>' containment reference.
     * @see #getPosition()
     * @generated
     */
    void setPosition(Position value);

} // PositionObject
