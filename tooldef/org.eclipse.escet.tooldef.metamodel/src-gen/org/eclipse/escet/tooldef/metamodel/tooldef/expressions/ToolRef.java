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

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tool Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#isBuiltin <em>Builtin</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#getTool <em>Tool</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getToolRef()
 * @model
 * @generated
 */
public interface ToolRef extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Builtin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Builtin</em>' attribute.
     * @see #setBuiltin(boolean)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getToolRef_Builtin()
     * @model required="true"
     * @generated
     */
    boolean isBuiltin();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#isBuiltin <em>Builtin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Builtin</em>' attribute.
     * @see #isBuiltin()
     * @generated
     */
    void setBuiltin(boolean value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getToolRef_Name()
     * @model required="true"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Tool</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tool</em>' reference.
     * @see #setTool(Tool)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#getToolRef_Tool()
     * @model
     * @generated
     */
    Tool getTool();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#getTool <em>Tool</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tool</em>' reference.
     * @see #getTool()
     * @generated
     */
    void setTool(Tool value);

} // ToolRef
