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
package org.eclipse.escet.tooldef.metamodel.tooldef;

import java.lang.reflect.Method;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Java Tool</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getPluginName <em>Plugin Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getMethod <em>Method</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getJavaTool()
 * @model
 * @generated
 */
public interface JavaTool extends Tool
{
    /**
     * Returns the value of the '<em><b>Plugin Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Plugin Name</em>' attribute.
     * @see #setPluginName(String)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getJavaTool_PluginName()
     * @model
     * @generated
     */
    String getPluginName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getPluginName <em>Plugin Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Plugin Name</em>' attribute.
     * @see #getPluginName()
     * @generated
     */
    void setPluginName(String value);

    /**
     * Returns the value of the '<em><b>Method Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Method Name</em>' attribute.
     * @see #setMethodName(String)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getJavaTool_MethodName()
     * @model required="true"
     * @generated
     */
    String getMethodName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getMethodName <em>Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method Name</em>' attribute.
     * @see #getMethodName()
     * @generated
     */
    void setMethodName(String value);

    /**
     * Returns the value of the '<em><b>Method</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Method</em>' attribute.
     * @see #setMethod(Method)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getJavaTool_Method()
     * @model dataType="org.eclipse.escet.tooldef.metamodel.tooldef.Method" required="true"
     * @generated
     */
    Method getMethod();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getMethod <em>Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method</em>' attribute.
     * @see #getMethod()
     * @generated
     */
    void setMethod(Method value);

} // JavaTool
