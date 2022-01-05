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
package org.eclipse.escet.cif.metamodel.cif.automata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Elif Update</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate#getThens <em>Thens</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getElifUpdate()
 * @model
 * @generated
 */
public interface ElifUpdate extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Guards</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Guards</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getElifUpdate_Guards()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Expression> getGuards();

    /**
     * Returns the value of the '<em><b>Thens</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.automata.Update}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Thens</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getElifUpdate_Thens()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Update> getThens();

} // ElifUpdate
