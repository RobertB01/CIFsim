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

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dict Pair</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictPair#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictPair#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getDictPair()
 * @model
 * @generated
 */
public interface DictPair extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Key</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Key</em>' containment reference.
     * @see #setKey(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getDictPair_Key()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getKey();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictPair#getKey <em>Key</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Key</em>' containment reference.
     * @see #getKey()
     * @generated
     */
    void setKey(Expression value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getDictPair_Value()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getValue();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictPair#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(Expression value);

} // DictPair
