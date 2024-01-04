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
package org.eclipse.escet.chi.metamodel.chi;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.FunctionType#getResultType <em>Result Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.FunctionType#getParameterTypes <em>Parameter Types</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getFunctionType()
 * @model
 * @generated
 */
public interface FunctionType extends Type
{
    /**
     * Returns the value of the '<em><b>Result Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Result Type</em>' containment reference.
     * @see #setResultType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getFunctionType_ResultType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getResultType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.FunctionType#getResultType <em>Result Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result Type</em>' containment reference.
     * @see #getResultType()
     * @generated
     */
    void setResultType(Type value);

    /**
     * Returns the value of the '<em><b>Parameter Types</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.Type}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Types</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getFunctionType_ParameterTypes()
     * @model containment="true"
     * @generated
     */
    EList<Type> getParameterTypes();

} // FunctionType
