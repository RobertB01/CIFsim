/**
 * Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
 * A representation of the model object '<em><b>Binary Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getRight <em>Right</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getOp <em>Op</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getBinaryExpression()
 * @model
 * @generated
 */
public interface BinaryExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Left</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Left</em>' containment reference.
     * @see #setLeft(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getBinaryExpression_Left()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getLeft();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getLeft <em>Left</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Left</em>' containment reference.
     * @see #getLeft()
     * @generated
     */
    void setLeft(Expression value);

    /**
     * Returns the value of the '<em><b>Right</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Right</em>' containment reference.
     * @see #setRight(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getBinaryExpression_Right()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getRight();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getRight <em>Right</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Right</em>' containment reference.
     * @see #getRight()
     * @generated
     */
    void setRight(Expression value);

    /**
     * Returns the value of the '<em><b>Op</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.chi.metamodel.chi.BinaryOperators}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Op</em>' attribute.
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryOperators
     * @see #setOp(BinaryOperators)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getBinaryExpression_Op()
     * @model required="true"
     * @generated
     */
    BinaryOperators getOp();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getOp <em>Op</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Op</em>' attribute.
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryOperators
     * @see #getOp()
     * @generated
     */
    void setOp(BinaryOperators value);

} // BinaryExpression
