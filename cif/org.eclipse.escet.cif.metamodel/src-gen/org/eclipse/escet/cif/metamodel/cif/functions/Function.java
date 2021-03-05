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
package org.eclipse.escet.cif.metamodel.cif.functions;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.Function#getReturnTypes <em>Return Types</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.Function#getParameters <em>Parameters</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getFunction()
 * @model abstract="true"
 * @generated
 */
public interface Function extends Declaration
{
    /**
     * Returns the value of the '<em><b>Return Types</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.types.CifType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Return Types</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getFunction_ReturnTypes()
     * @model containment="true" required="true"
     * @generated
     */
    EList<CifType> getReturnTypes();

    /**
     * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameters</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getFunction_Parameters()
     * @model containment="true"
     * @generated
     */
    EList<FunctionParameter> getParameters();

} // Function
