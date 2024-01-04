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

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unwind</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.Unwind#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.Unwind#getVariables <em>Variables</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getUnwind()
 * @model
 * @generated
 */
public interface Unwind extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' containment reference.
     * @see #setSource(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getUnwind_Source()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getSource();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.Unwind#getSource <em>Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' containment reference.
     * @see #getSource()
     * @generated
     */
    void setSource(Expression value);

    /**
     * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Variables</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getUnwind_Variables()
     * @model containment="true" required="true"
     * @generated
     */
    EList<VariableDeclaration> getVariables();

} // Unwind
