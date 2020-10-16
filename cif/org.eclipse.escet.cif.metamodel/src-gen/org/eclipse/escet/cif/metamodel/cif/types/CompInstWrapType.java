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
package org.eclipse.escet.cif.metamodel.cif.types;

import org.eclipse.escet.cif.metamodel.cif.ComponentInst;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Comp Inst Wrap Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType#getInstantiation <em>Instantiation</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getCompInstWrapType()
 * @model
 * @generated
 */
public interface CompInstWrapType extends CifType
{
    /**
     * Returns the value of the '<em><b>Instantiation</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Instantiation</em>' reference.
     * @see #setInstantiation(ComponentInst)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getCompInstWrapType_Instantiation()
     * @model required="true"
     * @generated
     */
    ComponentInst getInstantiation();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType#getInstantiation <em>Instantiation</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Instantiation</em>' reference.
     * @see #getInstantiation()
     * @generated
     */
    void setInstantiation(ComponentInst value);

    /**
     * Returns the value of the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference</em>' containment reference.
     * @see #setReference(CifType)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getCompInstWrapType_Reference()
     * @model containment="true" required="true"
     * @generated
     */
    CifType getReference();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType#getReference <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' containment reference.
     * @see #getReference()
     * @generated
     */
    void setReference(CifType value);

} // CompInstWrapType
