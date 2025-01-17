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
package org.eclipse.escet.cif.metamodel.cif.functions;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Internal Function</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction#getVariables <em>Variables</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction#getStatements <em>Statements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getInternalFunction()
 * @model
 * @generated
 */
public interface InternalFunction extends Function
{
    /**
     * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Variables</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getInternalFunction_Variables()
     * @model containment="true"
     * @generated
     */
    EList<DiscVariable> getVariables();

    /**
     * Returns the value of the '<em><b>Statements</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Statements</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getInternalFunction_Statements()
     * @model containment="true" required="true"
     * @generated
     */
    EList<FunctionStatement> getStatements();

} // InternalFunction
