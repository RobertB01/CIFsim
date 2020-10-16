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
package org.eclipse.escet.chi.metamodel.chi;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Call Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.CallExpression#getFunction <em>Function</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.CallExpression#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.CallExpression#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCallExpression()
 * @model
 * @generated
 */
public interface CallExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' containment reference.
     * @see #setFunction(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCallExpression_Function()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getFunction();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.CallExpression#getFunction <em>Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function</em>' containment reference.
     * @see #getFunction()
     * @generated
     */
    void setFunction(Expression value);

    /**
     * Returns the value of the '<em><b>Arguments</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arguments</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCallExpression_Arguments()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getArguments();

    /**
     * Returns the value of the '<em><b>Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' containment reference.
     * @see #setName(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCallExpression_Name()
     * @model containment="true"
     * @generated
     */
    Expression getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.CallExpression#getName <em>Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' containment reference.
     * @see #getName()
     * @generated
     */
    void setName(Expression value);

} // CallExpression
