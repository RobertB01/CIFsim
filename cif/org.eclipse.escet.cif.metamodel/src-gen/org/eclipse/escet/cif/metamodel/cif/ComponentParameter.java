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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComponentParameter#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComponentParameter#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComponentParameter()
 * @model
 * @generated
 */
public interface ComponentParameter extends Parameter
{
    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(CifType)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComponentParameter_Type()
     * @model containment="true" required="true"
     * @generated
     */
    CifType getType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.ComponentParameter#getType <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' containment reference.
     * @see #getType()
     * @generated
     */
    void setType(CifType value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComponentParameter_Name()
     * @model dataType="org.eclipse.escet.cif.metamodel.cif.CifIdentifier" required="true"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.ComponentParameter#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // ComponentParameter
