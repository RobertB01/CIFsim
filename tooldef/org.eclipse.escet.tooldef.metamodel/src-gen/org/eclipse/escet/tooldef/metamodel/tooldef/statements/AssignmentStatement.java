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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignment Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement#getAddressables <em>Addressables</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getAssignmentStatement()
 * @model
 * @generated
 */
public interface AssignmentStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Addressables</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Addressables</em>' containment reference list.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getAssignmentStatement_Addressables()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Expression> getAddressables();

    /**
     * Returns the value of the '<em><b>Values</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Values</em>' containment reference list.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#getAssignmentStatement_Values()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Expression> getValues();

} // AssignmentStatement
