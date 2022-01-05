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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getEventExpression()
 * @model
 * @generated
 */
public interface EventExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Event</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Event</em>' reference.
     * @see #setEvent(Event)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getEventExpression_Event()
     * @model required="true"
     * @generated
     */
    Event getEvent();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression#getEvent <em>Event</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Event</em>' reference.
     * @see #getEvent()
     * @generated
     */
    void setEvent(Event value);

} // EventExpression
