/**
 * Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif.declarations;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cont Variable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable#getDerivative <em>Derivative</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getContVariable()
 * @model
 * @generated
 */
public interface ContVariable extends Declaration
{
    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getContVariable_Value()
     * @model containment="true"
     * @generated
     */
    Expression getValue();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(Expression value);

    /**
     * Returns the value of the '<em><b>Derivative</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Derivative</em>' containment reference.
     * @see #setDerivative(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getContVariable_Derivative()
     * @model containment="true"
     * @generated
     */
    Expression getDerivative();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable#getDerivative <em>Derivative</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Derivative</em>' containment reference.
     * @see #getDerivative()
     * @generated
     */
    void setDerivative(Expression value);

} // ContVariable
