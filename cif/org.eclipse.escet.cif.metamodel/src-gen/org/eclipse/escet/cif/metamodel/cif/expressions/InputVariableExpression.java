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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Variable Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression#getVariable <em>Variable</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getInputVariableExpression()
 * @model
 * @generated
 */
public interface InputVariableExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Variable</em>' reference.
     * @see #setVariable(InputVariable)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getInputVariableExpression_Variable()
     * @model required="true"
     * @generated
     */
    InputVariable getVariable();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression#getVariable <em>Variable</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Variable</em>' reference.
     * @see #getVariable()
     * @generated
     */
    void setVariable(InputVariable value);

} // InputVariableExpression
