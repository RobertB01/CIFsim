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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Delay Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.DelayStatement#getLength <em>Length</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDelayStatement()
 * @model
 * @generated
 */
public interface DelayStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Length</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Length</em>' containment reference.
     * @see #setLength(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDelayStatement_Length()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getLength();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.DelayStatement#getLength <em>Length</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Length</em>' containment reference.
     * @see #getLength()
     * @generated
     */
    void setLength(Expression value);

} // DelayStatement
