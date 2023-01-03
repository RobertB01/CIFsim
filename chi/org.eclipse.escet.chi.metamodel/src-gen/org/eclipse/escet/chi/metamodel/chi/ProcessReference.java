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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ProcessReference#getProcess <em>Process</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getProcessReference()
 * @model
 * @generated
 */
public interface ProcessReference extends Expression
{
    /**
     * Returns the value of the '<em><b>Process</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Process</em>' reference.
     * @see #setProcess(ProcessDeclaration)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getProcessReference_Process()
     * @model required="true"
     * @generated
     */
    ProcessDeclaration getProcess();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ProcessReference#getProcess <em>Process</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process</em>' reference.
     * @see #getProcess()
     * @generated
     */
    void setProcess(ProcessDeclaration value);

} // ProcessReference
