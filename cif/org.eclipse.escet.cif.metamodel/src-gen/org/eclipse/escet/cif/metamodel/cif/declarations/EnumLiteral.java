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
package org.eclipse.escet.cif.metamodel.cif.declarations;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getEnumLiteral()
 * @model
 * @generated
 */
public interface EnumLiteral extends AnnotatedObject
{
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getEnumLiteral_Name()
     * @model dataType="org.eclipse.escet.cif.metamodel.cif.CifIdentifier" required="true"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // EnumLiteral
