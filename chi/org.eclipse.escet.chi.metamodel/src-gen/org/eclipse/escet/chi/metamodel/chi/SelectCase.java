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
package org.eclipse.escet.chi.metamodel.chi;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Select Case</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.SelectCase#getBody <em>Body</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.SelectCase#getGuard <em>Guard</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getSelectCase()
 * @model
 * @generated
 */
public interface SelectCase extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Body</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.Statement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Body</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getSelectCase_Body()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Statement> getBody();

    /**
     * Returns the value of the '<em><b>Guard</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Guard</em>' containment reference.
     * @see #setGuard(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getSelectCase_Guard()
     * @model containment="true"
     * @generated
     */
    Expression getGuard();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.SelectCase#getGuard <em>Guard</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Guard</em>' containment reference.
     * @see #getGuard()
     * @generated
     */
    void setGuard(Expression value);

} // SelectCase
