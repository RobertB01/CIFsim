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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage
 * @generated
 */
public interface StatementsFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    StatementsFactory eINSTANCE = org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Variable</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Variable</em>'.
     * @generated
     */
    Variable createVariable();

    /**
     * Returns a new object of class '<em>While Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>While Statement</em>'.
     * @generated
     */
    WhileStatement createWhileStatement();

    /**
     * Returns a new object of class '<em>Break Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Break Statement</em>'.
     * @generated
     */
    BreakStatement createBreakStatement();

    /**
     * Returns a new object of class '<em>Continue Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Continue Statement</em>'.
     * @generated
     */
    ContinueStatement createContinueStatement();

    /**
     * Returns a new object of class '<em>Return Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Return Statement</em>'.
     * @generated
     */
    ReturnStatement createReturnStatement();

    /**
     * Returns a new object of class '<em>Exit Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Exit Statement</em>'.
     * @generated
     */
    ExitStatement createExitStatement();

    /**
     * Returns a new object of class '<em>For Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>For Statement</em>'.
     * @generated
     */
    ForStatement createForStatement();

    /**
     * Returns a new object of class '<em>If Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>If Statement</em>'.
     * @generated
     */
    IfStatement createIfStatement();

    /**
     * Returns a new object of class '<em>Assignment Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Assignment Statement</em>'.
     * @generated
     */
    AssignmentStatement createAssignmentStatement();

    /**
     * Returns a new object of class '<em>Tool Invoke Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tool Invoke Statement</em>'.
     * @generated
     */
    ToolInvokeStatement createToolInvokeStatement();

    /**
     * Returns a new object of class '<em>Elif Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Elif Statement</em>'.
     * @generated
     */
    ElifStatement createElifStatement();

    /**
     * Returns a new object of class '<em>Tuple Addressable Decl</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tuple Addressable Decl</em>'.
     * @generated
     */
    TupleAddressableDecl createTupleAddressableDecl();

    /**
     * Returns a new object of class '<em>Variable Addressable Decl</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Variable Addressable Decl</em>'.
     * @generated
     */
    VariableAddressableDecl createVariableAddressableDecl();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    StatementsPackage getStatementsPackage();

} //StatementsFactory
