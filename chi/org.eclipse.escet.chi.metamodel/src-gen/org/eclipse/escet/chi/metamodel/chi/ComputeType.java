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
package org.eclipse.escet.chi.metamodel.chi;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Compute Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ComputeType#getParameterTypes <em>Parameter Types</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ComputeType#getExitType <em>Exit Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getComputeType()
 * @model abstract="true"
 * @generated
 */
public interface ComputeType extends Type
{
    /**
     * Returns the value of the '<em><b>Parameter Types</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.Type}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Types</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getComputeType_ParameterTypes()
     * @model containment="true"
     * @generated
     */
    EList<Type> getParameterTypes();

    /**
     * Returns the value of the '<em><b>Exit Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Exit Type</em>' containment reference.
     * @see #setExitType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getComputeType_ExitType()
     * @model containment="true"
     * @generated
     */
    Type getExitType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ComputeType#getExitType <em>Exit Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exit Type</em>' containment reference.
     * @see #getExitType()
     * @generated
     */
    void setExitType(Type value);

} // ComputeType
