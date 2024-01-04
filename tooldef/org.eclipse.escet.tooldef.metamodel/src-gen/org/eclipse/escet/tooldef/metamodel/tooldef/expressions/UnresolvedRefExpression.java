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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unresolved Ref Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getUnresolvedRefExpression()
 * @model
 * @generated
 */
public interface UnresolvedRefExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getUnresolvedRefExpression_Name()
     * @model required="true"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // UnresolvedRefExpression
