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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Std Lib Function Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression#getFunction <em>Function</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getStdLibFunctionExpression()
 * @model
 * @generated
 */
public interface StdLibFunctionExpression extends BaseFunctionExpression
{
    /**
     * Returns the value of the '<em><b>Function</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction
     * @see #setFunction(StdLibFunction)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getStdLibFunctionExpression_Function()
     * @model required="true"
     * @generated
     */
    StdLibFunction getFunction();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression#getFunction <em>Function</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction
     * @see #getFunction()
     * @generated
     */
    void setFunction(StdLibFunction value);

} // StdLibFunctionExpression
