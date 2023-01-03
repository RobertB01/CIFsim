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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exit Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement#getExitCode <em>Exit Code</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getExitStatement()
 * @model
 * @generated
 */
public interface ExitStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Exit Code</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Exit Code</em>' containment reference.
     * @see #setExitCode(Expression)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getExitStatement_ExitCode()
     * @model containment="true"
     * @generated
     */
    Expression getExitCode();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement#getExitCode <em>Exit Code</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exit Code</em>' containment reference.
     * @see #getExitCode()
     * @generated
     */
    void setExitCode(Expression value);

} // ExitStatement
