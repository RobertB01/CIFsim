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

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Field</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.Field#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.Field#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getField()
 * @model
 * @generated
 */
public interface Field extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getField_Name()
     * @model dataType="org.eclipse.escet.cif.metamodel.cif.CifIdentifier"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.Field#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(CifType)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getField_Type()
     * @model containment="true" required="true"
     * @generated
     */
    CifType getType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.Field#getType <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' containment reference.
     * @see #getType()
     * @generated
     */
    void setType(CifType value);

} // Field
