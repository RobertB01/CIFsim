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
package org.eclipse.escet.cif.metamodel.cif.types;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Int Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.IntType#getLower <em>Lower</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.IntType#getUpper <em>Upper</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getIntType()
 * @model
 * @generated
 */
public interface IntType extends CifType
{
    /**
     * Returns the value of the '<em><b>Lower</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lower</em>' attribute.
     * @see #setLower(Integer)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getIntType_Lower()
     * @model
     * @generated
     */
    Integer getLower();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.IntType#getLower <em>Lower</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lower</em>' attribute.
     * @see #getLower()
     * @generated
     */
    void setLower(Integer value);

    /**
     * Returns the value of the '<em><b>Upper</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Upper</em>' attribute.
     * @see #setUpper(Integer)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getIntType_Upper()
     * @model
     * @generated
     */
    Integer getUpper();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.IntType#getUpper <em>Upper</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Upper</em>' attribute.
     * @see #getUpper()
     * @generated
     */
    void setUpper(Integer value);

} // IntType
