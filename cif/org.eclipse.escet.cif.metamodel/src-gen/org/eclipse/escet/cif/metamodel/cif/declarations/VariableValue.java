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
package org.eclipse.escet.cif.metamodel.cif.declarations;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getVariableValue()
 * @model
 * @generated
 */
public interface VariableValue extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Values</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Values</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage#getVariableValue_Values()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getValues();

} // VariableValue
