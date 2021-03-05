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
package org.eclipse.escet.cif.metamodel.cif.cifsvg;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Svg In Event If Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry#getGuard <em>Guard</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgInEventIfEntry()
 * @model
 * @generated
 */
public interface SvgInEventIfEntry extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Guard</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Guard</em>' containment reference.
     * @see #setGuard(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgInEventIfEntry_Guard()
     * @model containment="true"
     * @generated
     */
    Expression getGuard();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry#getGuard <em>Guard</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Guard</em>' containment reference.
     * @see #getGuard()
     * @generated
     */
    void setGuard(Expression value);

    /**
     * Returns the value of the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Event</em>' containment reference.
     * @see #setEvent(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgInEventIfEntry_Event()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getEvent();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry#getEvent <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Event</em>' containment reference.
     * @see #getEvent()
     * @generated
     */
    void setEvent(Expression value);

} // SvgInEventIfEntry
