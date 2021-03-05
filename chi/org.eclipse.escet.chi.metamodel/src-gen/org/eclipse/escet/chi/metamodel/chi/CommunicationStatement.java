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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Communication Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.CommunicationStatement#getChannel <em>Channel</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.CommunicationStatement#getData <em>Data</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCommunicationStatement()
 * @model abstract="true"
 * @generated
 */
public interface CommunicationStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Channel</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Channel</em>' containment reference.
     * @see #setChannel(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCommunicationStatement_Channel()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getChannel();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.CommunicationStatement#getChannel <em>Channel</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Channel</em>' containment reference.
     * @see #getChannel()
     * @generated
     */
    void setChannel(Expression value);

    /**
     * Returns the value of the '<em><b>Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data</em>' containment reference.
     * @see #setData(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCommunicationStatement_Data()
     * @model containment="true"
     * @generated
     */
    Expression getData();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.CommunicationStatement#getData <em>Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data</em>' containment reference.
     * @see #getData()
     * @generated
     */
    void setData(Expression value);

} // CommunicationStatement
