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
package org.eclipse.escet.tooldef.metamodel.tooldef.types;

import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage#getTypeRef()
 * @model
 * @generated
 */
public interface TypeRef extends ToolDefType
{
    /**
     * Returns the value of the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' reference.
     * @see #setType(TypeDecl)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage#getTypeRef_Type()
     * @model required="true"
     * @generated
     */
    TypeDecl getType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef#getType <em>Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' reference.
     * @see #getType()
     * @generated
     */
    void setType(TypeDecl value);

} // TypeRef
