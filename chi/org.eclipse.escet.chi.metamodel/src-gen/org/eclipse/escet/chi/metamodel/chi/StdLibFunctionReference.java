/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Std Lib Function Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference#getFunction <em>Function</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getStdLibFunctionReference()
 * @model
 * @generated
 */
public interface StdLibFunctionReference extends BaseFunctionReference
{
    /**
     * Returns the value of the '<em><b>Function</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctions}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' attribute.
     * @see org.eclipse.escet.chi.metamodel.chi.StdLibFunctions
     * @see #setFunction(StdLibFunctions)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getStdLibFunctionReference_Function()
     * @model required="true"
     * @generated
     */
    StdLibFunctions getFunction();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference#getFunction <em>Function</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function</em>' attribute.
     * @see org.eclipse.escet.chi.metamodel.chi.StdLibFunctions
     * @see #getFunction()
     * @generated
     */
    void setFunction(StdLibFunctions value);

} // StdLibFunctionReference
