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
package org.eclipse.escet.tooldef.metamodel.tooldef;

import org.eclipse.escet.setext.runtime.Token;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Java Import</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getPluginName <em>Plugin Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getAsName <em>As Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getJavaImport()
 * @model
 * @generated
 */
public interface JavaImport extends Import
{
    /**
     * Returns the value of the '<em><b>Plugin Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Plugin Name</em>' attribute.
     * @see #setPluginName(Token)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getJavaImport_PluginName()
     * @model dataType="org.eclipse.escet.tooldef.metamodel.tooldef.Token"
     * @generated
     */
    Token getPluginName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getPluginName <em>Plugin Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Plugin Name</em>' attribute.
     * @see #getPluginName()
     * @generated
     */
    void setPluginName(Token value);

    /**
     * Returns the value of the '<em><b>Method Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Method Name</em>' attribute.
     * @see #setMethodName(Token)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getJavaImport_MethodName()
     * @model dataType="org.eclipse.escet.tooldef.metamodel.tooldef.Token" required="true"
     * @generated
     */
    Token getMethodName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getMethodName <em>Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method Name</em>' attribute.
     * @see #getMethodName()
     * @generated
     */
    void setMethodName(Token value);

    /**
     * Returns the value of the '<em><b>As Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>As Name</em>' attribute.
     * @see #setAsName(Token)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getJavaImport_AsName()
     * @model dataType="org.eclipse.escet.tooldef.metamodel.tooldef.Token"
     * @generated
     */
    Token getAsName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getAsName <em>As Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>As Name</em>' attribute.
     * @see #getAsName()
     * @generated
     */
    void setAsName(Token value);

} // JavaImport
