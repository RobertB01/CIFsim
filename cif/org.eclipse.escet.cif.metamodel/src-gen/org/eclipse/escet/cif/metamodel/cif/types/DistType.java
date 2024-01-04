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
package org.eclipse.escet.cif.metamodel.cif.types;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dist Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.DistType#getSampleType <em>Sample Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getDistType()
 * @model
 * @generated
 */
public interface DistType extends CifType
{
    /**
     * Returns the value of the '<em><b>Sample Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sample Type</em>' containment reference.
     * @see #setSampleType(CifType)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getDistType_SampleType()
     * @model containment="true" required="true"
     * @generated
     */
    CifType getSampleType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.DistType#getSampleType <em>Sample Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sample Type</em>' containment reference.
     * @see #getSampleType()
     * @generated
     */
    void setSampleType(CifType value);

} // DistType
