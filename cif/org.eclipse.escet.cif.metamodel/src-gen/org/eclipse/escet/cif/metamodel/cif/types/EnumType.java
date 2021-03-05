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
package org.eclipse.escet.cif.metamodel.cif.types;

import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.EnumType#getEnum <em>Enum</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getEnumType()
 * @model
 * @generated
 */
public interface EnumType extends CifType
{
    /**
     * Returns the value of the '<em><b>Enum</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Enum</em>' reference.
     * @see #setEnum(EnumDecl)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getEnumType_Enum()
     * @model required="true"
     * @generated
     */
    EnumDecl getEnum();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.EnumType#getEnum <em>Enum</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Enum</em>' reference.
     * @see #getEnum()
     * @generated
     */
    void setEnum(EnumDecl value);

} // EnumType
