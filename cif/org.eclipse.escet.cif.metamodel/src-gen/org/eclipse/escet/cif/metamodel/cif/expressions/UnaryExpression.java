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
package org.eclipse.escet.cif.metamodel.cif.expressions;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unary Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression#getOperator <em>Operator</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression#getChild <em>Child</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getUnaryExpression()
 * @model
 * @generated
 */
public interface UnaryExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Operator</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operator</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator
     * @see #setOperator(UnaryOperator)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getUnaryExpression_Operator()
     * @model required="true"
     * @generated
     */
    UnaryOperator getOperator();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression#getOperator <em>Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operator</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator
     * @see #getOperator()
     * @generated
     */
    void setOperator(UnaryOperator value);

    /**
     * Returns the value of the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Child</em>' containment reference.
     * @see #setChild(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getUnaryExpression_Child()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getChild();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression#getChild <em>Child</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Child</em>' containment reference.
     * @see #getChild()
     * @generated
     */
    void setChild(Expression value);

} // UnaryExpression
