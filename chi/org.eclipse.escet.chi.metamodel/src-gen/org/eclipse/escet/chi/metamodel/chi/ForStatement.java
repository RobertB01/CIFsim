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
package org.eclipse.escet.chi.metamodel.chi;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>For Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ForStatement#getBody <em>Body</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ForStatement#getUnwinds <em>Unwinds</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getForStatement()
 * @model
 * @generated
 */
public interface ForStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Body</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.Statement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Body</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getForStatement_Body()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Statement> getBody();

    /**
     * Returns the value of the '<em><b>Unwinds</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.Unwind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Unwinds</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getForStatement_Unwinds()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Unwind> getUnwinds();

} // ForStatement
