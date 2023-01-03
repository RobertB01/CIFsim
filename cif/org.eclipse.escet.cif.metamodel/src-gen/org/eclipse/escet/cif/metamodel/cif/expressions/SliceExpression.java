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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slice Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getBegin <em>Begin</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getEnd <em>End</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getChild <em>Child</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getSliceExpression()
 * @model
 * @generated
 */
public interface SliceExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Begin</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Begin</em>' containment reference.
     * @see #setBegin(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getSliceExpression_Begin()
     * @model containment="true"
     * @generated
     */
    Expression getBegin();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getBegin <em>Begin</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Begin</em>' containment reference.
     * @see #getBegin()
     * @generated
     */
    void setBegin(Expression value);

    /**
     * Returns the value of the '<em><b>End</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>End</em>' containment reference.
     * @see #setEnd(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getSliceExpression_End()
     * @model containment="true"
     * @generated
     */
    Expression getEnd();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getEnd <em>End</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End</em>' containment reference.
     * @see #getEnd()
     * @generated
     */
    void setEnd(Expression value);

    /**
     * Returns the value of the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Child</em>' containment reference.
     * @see #setChild(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getSliceExpression_Child()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getChild();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getChild <em>Child</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Child</em>' containment reference.
     * @see #getChild()
     * @generated
     */
    void setChild(Expression value);

} // SliceExpression
