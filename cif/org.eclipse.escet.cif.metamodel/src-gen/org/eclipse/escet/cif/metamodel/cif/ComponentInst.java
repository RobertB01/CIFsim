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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Inst</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComponentInst#getDefinition <em>Definition</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComponentInst#getParameters <em>Parameters</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComponentInst()
 * @model
 * @generated
 */
public interface ComponentInst extends Component
{
    /**
     * Returns the value of the '<em><b>Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definition</em>' containment reference.
     * @see #setDefinition(CifType)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComponentInst_Definition()
     * @model containment="true" required="true"
     * @generated
     */
    CifType getDefinition();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.ComponentInst#getDefinition <em>Definition</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Definition</em>' containment reference.
     * @see #getDefinition()
     * @generated
     */
    void setDefinition(CifType value);

    /**
     * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameters</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComponentInst_Parameters()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getParameters();

} // ComponentInst
