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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Run Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.RunStatement#getCases <em>Cases</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.RunStatement#isStartOnly <em>Start Only</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getRunStatement()
 * @model
 * @generated
 */
public interface RunStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Cases</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.CreateCase}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cases</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getRunStatement_Cases()
     * @model containment="true" required="true"
     * @generated
     */
    EList<CreateCase> getCases();

    /**
     * Returns the value of the '<em><b>Start Only</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start Only</em>' attribute.
     * @see #setStartOnly(boolean)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getRunStatement_StartOnly()
     * @model required="true"
     * @generated
     */
    boolean isStartOnly();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.RunStatement#isStartOnly <em>Start Only</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Only</em>' attribute.
     * @see #isStartOnly()
     * @generated
     */
    void setStartOnly(boolean value);

} // RunStatement
