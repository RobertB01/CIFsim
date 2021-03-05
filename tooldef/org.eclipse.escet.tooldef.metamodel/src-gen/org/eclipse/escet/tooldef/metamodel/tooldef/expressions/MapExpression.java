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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Map Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression#getEntries <em>Entries</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getMapExpression()
 * @model
 * @generated
 */
public interface MapExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Entries</em>' containment reference list.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getMapExpression_Entries()
     * @model containment="true"
     * @generated
     */
    EList<MapEntry> getEntries();

} // MapExpression
