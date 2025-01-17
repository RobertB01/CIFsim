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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getExpression()
 * @model abstract="true"
 * @generated
 */
public interface Expression extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(ToolDefType)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getExpression_Type()
     * @model containment="true" required="true"
     * @generated
     */
    ToolDefType getType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression#getType <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' containment reference.
     * @see #getType()
     * @generated
     */
    void setType(ToolDefType value);

} // Expression
