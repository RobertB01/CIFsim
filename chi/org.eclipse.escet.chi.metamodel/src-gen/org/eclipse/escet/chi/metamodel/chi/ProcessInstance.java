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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ProcessInstance#getCall <em>Call</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ProcessInstance#getVar <em>Var</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getProcessInstance()
 * @model
 * @generated
 */
public interface ProcessInstance extends CreateCase
{
    /**
     * Returns the value of the '<em><b>Call</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Call</em>' containment reference.
     * @see #setCall(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getProcessInstance_Call()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getCall();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ProcessInstance#getCall <em>Call</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Call</em>' containment reference.
     * @see #getCall()
     * @generated
     */
    void setCall(Expression value);

    /**
     * Returns the value of the '<em><b>Var</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Var</em>' containment reference.
     * @see #setVar(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getProcessInstance_Var()
     * @model containment="true"
     * @generated
     */
    Expression getVar();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ProcessInstance#getVar <em>Var</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Var</em>' containment reference.
     * @see #getVar()
     * @generated
     */
    void setVar(Expression value);

} // ProcessInstance
