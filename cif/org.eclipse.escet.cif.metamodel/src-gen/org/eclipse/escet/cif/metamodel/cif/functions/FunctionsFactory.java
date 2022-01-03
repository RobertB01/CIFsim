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
package org.eclipse.escet.cif.metamodel.cif.functions;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage
 * @generated
 */
public interface FunctionsFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    FunctionsFactory eINSTANCE = org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Function Parameter</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Function Parameter</em>'.
     * @generated
     */
    FunctionParameter createFunctionParameter();

    /**
     * Returns a new object of class '<em>Internal Function</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Internal Function</em>'.
     * @generated
     */
    InternalFunction createInternalFunction();

    /**
     * Returns a new object of class '<em>External Function</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>External Function</em>'.
     * @generated
     */
    ExternalFunction createExternalFunction();

    /**
     * Returns a new object of class '<em>Break Func Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Break Func Statement</em>'.
     * @generated
     */
    BreakFuncStatement createBreakFuncStatement();

    /**
     * Returns a new object of class '<em>Continue Func Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Continue Func Statement</em>'.
     * @generated
     */
    ContinueFuncStatement createContinueFuncStatement();

    /**
     * Returns a new object of class '<em>Assignment Func Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Assignment Func Statement</em>'.
     * @generated
     */
    AssignmentFuncStatement createAssignmentFuncStatement();

    /**
     * Returns a new object of class '<em>While Func Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>While Func Statement</em>'.
     * @generated
     */
    WhileFuncStatement createWhileFuncStatement();

    /**
     * Returns a new object of class '<em>If Func Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>If Func Statement</em>'.
     * @generated
     */
    IfFuncStatement createIfFuncStatement();

    /**
     * Returns a new object of class '<em>Elif Func Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Elif Func Statement</em>'.
     * @generated
     */
    ElifFuncStatement createElifFuncStatement();

    /**
     * Returns a new object of class '<em>Return Func Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Return Func Statement</em>'.
     * @generated
     */
    ReturnFuncStatement createReturnFuncStatement();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    FunctionsPackage getFunctionsPackage();

} //FunctionsFactory
