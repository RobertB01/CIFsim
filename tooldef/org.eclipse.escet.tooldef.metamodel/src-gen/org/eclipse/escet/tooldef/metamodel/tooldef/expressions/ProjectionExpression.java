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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Projection Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression#getChild <em>Child</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getProjectionExpression()
 * @model
 * @generated
 */
public interface ProjectionExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Child</em>' containment reference.
     * @see #setChild(Expression)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getProjectionExpression_Child()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getChild();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression#getChild <em>Child</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Child</em>' containment reference.
     * @see #getChild()
     * @generated
     */
    void setChild(Expression value);

    /**
     * Returns the value of the '<em><b>Index</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Index</em>' containment reference.
     * @see #setIndex(Expression)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getProjectionExpression_Index()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getIndex();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression#getIndex <em>Index</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Index</em>' containment reference.
     * @see #getIndex()
     * @generated
     */
    void setIndex(Expression value);

} // ProjectionExpression
