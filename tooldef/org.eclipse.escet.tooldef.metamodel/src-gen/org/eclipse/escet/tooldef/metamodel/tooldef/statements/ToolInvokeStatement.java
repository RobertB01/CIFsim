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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tool Invoke Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement#getInvocation <em>Invocation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getToolInvokeStatement()
 * @model
 * @generated
 */
public interface ToolInvokeStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Invocation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Invocation</em>' containment reference.
     * @see #setInvocation(ToolInvokeExpression)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getToolInvokeStatement_Invocation()
     * @model containment="true" required="true"
     * @generated
     */
    ToolInvokeExpression getInvocation();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement#getInvocation <em>Invocation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Invocation</em>' containment reference.
     * @see #getInvocation()
     * @generated
     */
    void setInvocation(ToolInvokeExpression value);

} // ToolInvokeStatement
