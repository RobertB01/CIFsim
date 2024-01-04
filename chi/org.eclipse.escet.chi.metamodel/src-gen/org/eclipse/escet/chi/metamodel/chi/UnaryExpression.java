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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unary Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.UnaryExpression#getChild <em>Child</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.UnaryExpression#getOp <em>Op</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getUnaryExpression()
 * @model
 * @generated
 */
public interface UnaryExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Child</em>' containment reference.
     * @see #setChild(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getUnaryExpression_Child()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getChild();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.UnaryExpression#getChild <em>Child</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Child</em>' containment reference.
     * @see #getChild()
     * @generated
     */
    void setChild(Expression value);

    /**
     * Returns the value of the '<em><b>Op</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.chi.metamodel.chi.UnaryOperators}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Op</em>' attribute.
     * @see org.eclipse.escet.chi.metamodel.chi.UnaryOperators
     * @see #setOp(UnaryOperators)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getUnaryExpression_Op()
     * @model required="true"
     * @generated
     */
    UnaryOperators getOp();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.UnaryExpression#getOp <em>Op</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Op</em>' attribute.
     * @see org.eclipse.escet.chi.metamodel.chi.UnaryOperators
     * @see #getOp()
     * @generated
     */
    void setOp(UnaryOperators value);

} // UnaryExpression
