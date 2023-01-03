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
 * A representation of the model object '<em><b>List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType#getElemType <em>Elem Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage#getListType()
 * @model
 * @generated
 */
public interface ListType extends ToolDefType
{
    /**
     * Returns the value of the '<em><b>Elem Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Elem Type</em>' containment reference.
     * @see #setElemType(ToolDefType)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage#getListType_ElemType()
     * @model containment="true" required="true"
     * @generated
     */
    ToolDefType getElemType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType#getElemType <em>Elem Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Elem Type</em>' containment reference.
     * @see #getElemType()
     * @generated
     */
    void setElemType(ToolDefType value);

} // ListType
