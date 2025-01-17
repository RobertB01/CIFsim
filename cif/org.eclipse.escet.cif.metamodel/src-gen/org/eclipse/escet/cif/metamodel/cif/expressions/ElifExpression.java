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

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Elif Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression#getThen <em>Then</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getElifExpression()
 * @model
 * @generated
 */
public interface ElifExpression extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Guards</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Guards</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getElifExpression_Guards()
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
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getElifExpression_Then()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getThen();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression#getThen <em>Then</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Then</em>' containment reference.
     * @see #getThen()
     * @generated
     */
    void setThen(Expression value);

} // ElifExpression
