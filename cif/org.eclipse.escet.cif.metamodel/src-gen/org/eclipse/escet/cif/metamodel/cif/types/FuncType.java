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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Func Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.FuncType#getReturnType <em>Return Type</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.FuncType#getParamTypes <em>Param Types</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getFuncType()
 * @model
 * @generated
 */
public interface FuncType extends CifType
{
    /**
     * Returns the value of the '<em><b>Return Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Return Type</em>' containment reference.
     * @see #setReturnType(CifType)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getFuncType_ReturnType()
     * @model containment="true" required="true"
     * @generated
     */
    CifType getReturnType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.FuncType#getReturnType <em>Return Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Return Type</em>' containment reference.
     * @see #getReturnType()
     * @generated
     */
    void setReturnType(CifType value);

    /**
     * Returns the value of the '<em><b>Param Types</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.types.CifType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Param Types</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getFuncType_ParamTypes()
     * @model containment="true"
     * @generated
     */
    EList<CifType> getParamTypes();

} // FuncType
