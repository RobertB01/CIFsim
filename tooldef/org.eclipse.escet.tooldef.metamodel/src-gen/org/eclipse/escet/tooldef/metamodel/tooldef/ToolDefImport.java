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

import org.eclipse.escet.setext.runtime.Token;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tool Def Import</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getOrigName <em>Orig Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getAsName <em>As Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolDefImport()
 * @model
 * @generated
 */
public interface ToolDefImport extends Import
{
    /**
     * Returns the value of the '<em><b>Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' attribute.
     * @see #setSource(Token)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolDefImport_Source()
     * @model dataType="org.eclipse.escet.tooldef.metamodel.tooldef.Token" required="true"
     * @generated
     */
    Token getSource();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getSource <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' attribute.
     * @see #getSource()
     * @generated
     */
    void setSource(Token value);

    /**
     * Returns the value of the '<em><b>Orig Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Orig Name</em>' attribute.
     * @see #setOrigName(Token)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolDefImport_OrigName()
     * @model dataType="org.eclipse.escet.tooldef.metamodel.tooldef.Token"
     * @generated
     */
    Token getOrigName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getOrigName <em>Orig Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Orig Name</em>' attribute.
     * @see #getOrigName()
     * @generated
     */
    void setOrigName(Token value);

    /**
     * Returns the value of the '<em><b>As Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>As Name</em>' attribute.
     * @see #setAsName(Token)
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolDefImport_AsName()
     * @model dataType="org.eclipse.escet.tooldef.metamodel.tooldef.Token"
     * @generated
     */
    Token getAsName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getAsName <em>As Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>As Name</em>' attribute.
     * @see #getAsName()
     * @generated
     */
    void setAsName(Token value);

} // ToolDefImport
