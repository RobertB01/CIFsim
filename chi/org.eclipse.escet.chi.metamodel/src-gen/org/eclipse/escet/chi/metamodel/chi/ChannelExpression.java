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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Channel Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ChannelExpression#getElementType <em>Element Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getChannelExpression()
 * @model
 * @generated
 */
public interface ChannelExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Type</em>' containment reference.
     * @see #setElementType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getChannelExpression_ElementType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getElementType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ChannelExpression#getElementType <em>Element Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Type</em>' containment reference.
     * @see #getElementType()
     * @generated
     */
    void setElementType(Type value);

} // ChannelExpression
