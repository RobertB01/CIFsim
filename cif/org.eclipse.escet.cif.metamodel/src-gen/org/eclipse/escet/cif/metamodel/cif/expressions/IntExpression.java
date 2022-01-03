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
package org.eclipse.escet.cif.metamodel.cif.expressions;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Int Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getIntExpression()
 * @model
 * @generated
 */
public interface IntExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(int)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getIntExpression_Value()
     * @model required="true"
     * @generated
     */
    int getValue();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(int value);

} // IntExpression
