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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>While Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement#getCondition <em>Condition</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement#getStatements <em>Statements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getWhileStatement()
 * @model
 * @generated
 */
public interface WhileStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Condition</em>' containment reference.
     * @see #setCondition(Expression)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getWhileStatement_Condition()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getCondition();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement#getCondition <em>Condition</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Condition</em>' containment reference.
     * @see #getCondition()
     * @generated
     */
    void setCondition(Expression value);

    /**
     * Returns the value of the '<em><b>Statements</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Statements</em>' containment reference list.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getWhileStatement_Statements()
     * @model containment="true"
     * @generated
     */
    EList<Statement> getStatements();

} // WhileStatement
