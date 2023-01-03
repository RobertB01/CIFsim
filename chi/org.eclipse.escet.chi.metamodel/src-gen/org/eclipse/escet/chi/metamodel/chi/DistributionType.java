/**
 * Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Distribution Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.DistributionType#getResultType <em>Result Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDistributionType()
 * @model
 * @generated
 */
public interface DistributionType extends Type
{
    /**
     * Returns the value of the '<em><b>Result Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Result Type</em>' containment reference.
     * @see #setResultType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDistributionType_ResultType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getResultType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.DistributionType#getResultType <em>Result Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result Type</em>' containment reference.
     * @see #getResultType()
     * @generated
     */
    void setResultType(Type value);

} // DistributionType
