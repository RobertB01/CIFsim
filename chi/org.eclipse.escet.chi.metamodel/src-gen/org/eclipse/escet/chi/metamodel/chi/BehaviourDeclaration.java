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
 * A representation of the model object '<em><b>Behaviour Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration#getVariables <em>Variables</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration#getStatements <em>Statements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getBehaviourDeclaration()
 * @model abstract="true"
 * @generated
 */
public interface BehaviourDeclaration extends Declaration
{
    /**
     * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Variables</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getBehaviourDeclaration_Variables()
     * @model containment="true"
     * @generated
     */
    EList<VariableDeclaration> getVariables();

    /**
     * Returns the value of the '<em><b>Statements</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.Statement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Statements</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getBehaviourDeclaration_Statements()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Statement> getStatements();

} // BehaviourDeclaration
