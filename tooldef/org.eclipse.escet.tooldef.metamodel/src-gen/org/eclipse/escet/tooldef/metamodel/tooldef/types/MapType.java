/**
 * Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.tooldef.metamodel.tooldef.types;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Map Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType#getKeyType <em>Key Type</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType#getValueType <em>Value Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage#getMapType()
 * @model
 * @generated
 */
public interface MapType extends ToolDefType
{
    /**
     * Returns the value of the '<em><b>Key Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Key Type</em>' containment reference.
     * @see #setKeyType(ToolDefType)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage#getMapType_KeyType()
     * @model containment="true" required="true"
     * @generated
     */
    ToolDefType getKeyType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType#getKeyType <em>Key Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Key Type</em>' containment reference.
     * @see #getKeyType()
     * @generated
     */
    void setKeyType(ToolDefType value);

    /**
     * Returns the value of the '<em><b>Value Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Type</em>' containment reference.
     * @see #setValueType(ToolDefType)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage#getMapType_ValueType()
     * @model containment="true" required="true"
     * @generated
     */
    ToolDefType getValueType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType#getValueType <em>Value Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Type</em>' containment reference.
     * @see #getValueType()
     * @generated
     */
    void setValueType(ToolDefType value);

} // MapType
