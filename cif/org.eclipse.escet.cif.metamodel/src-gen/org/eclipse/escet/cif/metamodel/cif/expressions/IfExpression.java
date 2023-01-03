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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>If Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getThen <em>Then</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getElse <em>Else</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getElifs <em>Elifs</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getIfExpression()
 * @model
 * @generated
 */
public interface IfExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Guards</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Guards</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getIfExpression_Guards()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Expression> getGuards();

    /**
     * Returns the value of the '<em><b>Then</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Then</em>' containment reference.
     * @see #setThen(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getIfExpression_Then()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getThen();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getThen <em>Then</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Then</em>' containment reference.
     * @see #getThen()
     * @generated
     */
    void setThen(Expression value);

    /**
     * Returns the value of the '<em><b>Else</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Else</em>' containment reference.
     * @see #setElse(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getIfExpression_Else()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getElse();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getElse <em>Else</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Else</em>' containment reference.
     * @see #getElse()
     * @generated
     */
    void setElse(Expression value);

    /**
     * Returns the value of the '<em><b>Elifs</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Elifs</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getIfExpression_Elifs()
     * @model containment="true"
     * @generated
     */
    EList<ElifExpression> getElifs();

} // IfExpression
