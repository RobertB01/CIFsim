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
package org.eclipse.escet.tooldef.metamodel.tooldef;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tool Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#isVariadic <em>Variadic</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolParameter()
 * @model
 * @generated
 */
public interface ToolParameter extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(ToolDefType)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolParameter_Type()
     * @model containment="true" required="true"
     * @generated
     */
    ToolDefType getType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getType <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' containment reference.
     * @see #getType()
     * @generated
     */
    void setType(ToolDefType value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolParameter_Name()
     * @model required="true"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Variadic</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Variadic</em>' attribute.
     * @see #setVariadic(boolean)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolParameter_Variadic()
     * @model required="true"
     * @generated
     */
    boolean isVariadic();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#isVariadic <em>Variadic</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Variadic</em>' attribute.
     * @see #isVariadic()
     * @generated
     */
    void setVariadic(boolean value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(Expression)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolParameter_Value()
     * @model containment="true"
     * @generated
     */
    Expression getValue();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(Expression value);

} // ToolParameter
