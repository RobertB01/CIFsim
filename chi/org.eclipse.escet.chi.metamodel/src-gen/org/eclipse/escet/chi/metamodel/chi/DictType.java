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
 * A representation of the model object '<em><b>Dict Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.DictType#getKeyType <em>Key Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.DictType#getValueType <em>Value Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDictType()
 * @model
 * @generated
 */
public interface DictType extends Type
{
    /**
     * Returns the value of the '<em><b>Key Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Key Type</em>' containment reference.
     * @see #setKeyType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDictType_KeyType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getKeyType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.DictType#getKeyType <em>Key Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Key Type</em>' containment reference.
     * @see #getKeyType()
     * @generated
     */
    void setKeyType(Type value);

    /**
     * Returns the value of the '<em><b>Value Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Type</em>' containment reference.
     * @see #setValueType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDictType_ValueType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getValueType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.DictType#getValueType <em>Value Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Type</em>' containment reference.
     * @see #getValueType()
     * @generated
     */
    void setValueType(Type value);

} // DictType
