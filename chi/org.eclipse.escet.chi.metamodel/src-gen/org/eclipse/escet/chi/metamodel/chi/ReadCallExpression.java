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
 * A representation of the model object '<em><b>Read Call Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ReadCallExpression#getFile <em>File</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ReadCallExpression#getLoadType <em>Load Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getReadCallExpression()
 * @model
 * @generated
 */
public interface ReadCallExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>File</em>' containment reference.
     * @see #setFile(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getReadCallExpression_File()
     * @model containment="true"
     * @generated
     */
    Expression getFile();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ReadCallExpression#getFile <em>File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File</em>' containment reference.
     * @see #getFile()
     * @generated
     */
    void setFile(Expression value);

    /**
     * Returns the value of the '<em><b>Load Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Load Type</em>' containment reference.
     * @see #setLoadType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getReadCallExpression_LoadType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getLoadType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ReadCallExpression#getLoadType <em>Load Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Load Type</em>' containment reference.
     * @see #getLoadType()
     * @generated
     */
    void setLoadType(Type value);

} // ReadCallExpression
