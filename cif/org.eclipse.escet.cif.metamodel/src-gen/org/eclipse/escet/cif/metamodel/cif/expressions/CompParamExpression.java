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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Comp Param Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression#getParameter <em>Parameter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getCompParamExpression()
 * @model
 * @generated
 */
public interface CompParamExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter</em>' reference.
     * @see #setParameter(ComponentParameter)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getCompParamExpression_Parameter()
     * @model required="true"
     * @generated
     */
    ComponentParameter getParameter();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression#getParameter <em>Parameter</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter</em>' reference.
     * @see #getParameter()
     * @generated
     */
    void setParameter(ComponentParameter value);

} // CompParamExpression
