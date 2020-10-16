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
package org.eclipse.escet.cif.metamodel.cif.declarations;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Decl</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl#getLiterals <em>Literals</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getEnumDecl()
 * @model
 * @generated
 */
public interface EnumDecl extends Declaration
{
    /**
     * Returns the value of the '<em><b>Literals</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Literals</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getEnumDecl_Literals()
     * @model containment="true" required="true"
     * @generated
     */
    EList<EnumLiteral> getLiterals();

} // EnumDecl
