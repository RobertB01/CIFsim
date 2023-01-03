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
package org.eclipse.escet.cif.metamodel.cif.functions;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>If Func Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getThens <em>Thens</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getElses <em>Elses</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getElifs <em>Elifs</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getIfFuncStatement()
 * @model
 * @generated
 */
public interface IfFuncStatement extends FunctionStatement
{
    /**
     * Returns the value of the '<em><b>Guards</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Guards</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getIfFuncStatement_Guards()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Expression> getGuards();

    /**
     * Returns the value of the '<em><b>Thens</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Thens</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getIfFuncStatement_Thens()
     * @model containment="true" required="true"
     * @generated
     */
    EList<FunctionStatement> getThens();

    /**
     * Returns the value of the '<em><b>Elses</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Elses</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getIfFuncStatement_Elses()
     * @model containment="true"
     * @generated
     */
    EList<FunctionStatement> getElses();

    /**
     * Returns the value of the '<em><b>Elifs</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Elifs</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getIfFuncStatement_Elifs()
     * @model containment="true"
     * @generated
     */
    EList<ElifFuncStatement> getElifs();

} // IfFuncStatement
