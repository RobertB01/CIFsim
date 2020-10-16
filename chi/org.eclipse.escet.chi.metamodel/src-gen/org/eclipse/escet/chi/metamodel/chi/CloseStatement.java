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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Close Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.CloseStatement#getHandle <em>Handle</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCloseStatement()
 * @model
 * @generated
 */
public interface CloseStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Handle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Handle</em>' containment reference.
     * @see #setHandle(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCloseStatement_Handle()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getHandle();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.CloseStatement#getHandle <em>Handle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' containment reference.
     * @see #getHandle()
     * @generated
     */
    void setHandle(Expression value);

} // CloseStatement
