/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
 * A representation of the model object '<em><b>Cast Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.CastExpression#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.CastExpression#getCastType <em>Cast Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCastExpression()
 * @model
 * @generated
 */
public interface CastExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression</em>' containment reference.
     * @see #setExpression(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCastExpression_Expression()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getExpression();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.CastExpression#getExpression <em>Expression</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Expression</em>' containment reference.
     * @see #getExpression()
     * @generated
     */
    void setExpression(Expression value);

    /**
     * Returns the value of the '<em><b>Cast Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cast Type</em>' containment reference.
     * @see #setCastType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getCastExpression_CastType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getCastType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.CastExpression#getCastType <em>Cast Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cast Type</em>' containment reference.
     * @see #getCastType()
     * @generated
     */
    void setCastType(Type value);

} // CastExpression
