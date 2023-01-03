/**
 * Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tool Invoke Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression#getTool <em>Tool</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getToolInvokeExpression()
 * @model
 * @generated
 */
public interface ToolInvokeExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Arguments</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arguments</em>' containment reference list.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getToolInvokeExpression_Arguments()
     * @model containment="true"
     * @generated
     */
    EList<ToolArgument> getArguments();

    /**
     * Returns the value of the '<em><b>Tool</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tool</em>' containment reference.
     * @see #setTool(ToolRef)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getToolInvokeExpression_Tool()
     * @model containment="true" required="true"
     * @generated
     */
    ToolRef getTool();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression#getTool <em>Tool</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tool</em>' containment reference.
     * @see #getTool()
     * @generated
     */
    void setTool(ToolRef value);

} // ToolInvokeExpression
