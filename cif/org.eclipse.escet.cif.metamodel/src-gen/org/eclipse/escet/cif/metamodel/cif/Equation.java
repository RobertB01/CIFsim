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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Equation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Equation#isDerivative <em>Derivative</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Equation#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Equation#getVariable <em>Variable</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEquation()
 * @model
 * @generated
 */
public interface Equation extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Derivative</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Derivative</em>' attribute.
     * @see #setDerivative(boolean)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEquation_Derivative()
     * @model required="true"
     * @generated
     */
    boolean isDerivative();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.Equation#isDerivative <em>Derivative</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Derivative</em>' attribute.
     * @see #isDerivative()
     * @generated
     */
    void setDerivative(boolean value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEquation_Value()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getValue();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.Equation#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(Expression value);

    /**
     * Returns the value of the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Variable</em>' reference.
     * @see #setVariable(Declaration)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEquation_Variable()
     * @model required="true"
     * @generated
     */
    Declaration getVariable();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.Equation#getVariable <em>Variable</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Variable</em>' reference.
     * @see #getVariable()
     * @generated
     */
    void setVariable(Declaration value);

} // Equation
