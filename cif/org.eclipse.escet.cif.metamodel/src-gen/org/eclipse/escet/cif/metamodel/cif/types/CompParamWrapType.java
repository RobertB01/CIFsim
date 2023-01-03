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
package org.eclipse.escet.cif.metamodel.cif.types;

import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Comp Param Wrap Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getCompParamWrapType()
 * @model
 * @generated
 */
public interface CompParamWrapType extends CifType
{
    /**
     * Returns the value of the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter</em>' reference.
     * @see #setParameter(ComponentParameter)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getCompParamWrapType_Parameter()
     * @model required="true"
     * @generated
     */
    ComponentParameter getParameter();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType#getParameter <em>Parameter</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter</em>' reference.
     * @see #getParameter()
     * @generated
     */
    void setParameter(ComponentParameter value);

    /**
     * Returns the value of the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference</em>' containment reference.
     * @see #setReference(CifType)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getCompParamWrapType_Reference()
     * @model containment="true" required="true"
     * @generated
     */
    CifType getReference();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType#getReference <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' containment reference.
     * @see #getReference()
     * @generated
     */
    void setReference(CifType value);

} // CompParamWrapType
