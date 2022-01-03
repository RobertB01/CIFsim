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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.escet.cif.metamodel.cif.functions.Function;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression#getFunction <em>Function</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getFunctionExpression()
 * @model
 * @generated
 */
public interface FunctionExpression extends BaseFunctionExpression
{
    /**
     * Returns the value of the '<em><b>Function</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' reference.
     * @see #setFunction(Function)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getFunctionExpression_Function()
     * @model required="true"
     * @generated
     */
    Function getFunction();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression#getFunction <em>Function</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function</em>' reference.
     * @see #getFunction()
     * @generated
     */
    void setFunction(Function value);

} // FunctionExpression
