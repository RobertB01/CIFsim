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

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tool Def Tool</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool#getStatements <em>Statements</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolDefTool()
 * @model
 * @generated
 */
public interface ToolDefTool extends Tool
{
    /**
     * Returns the value of the '<em><b>Statements</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Statements</em>' containment reference list.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage#getToolDefTool_Statements()
     * @model containment="true"
     * @generated
     */
    EList<Statement> getStatements();

} // ToolDefTool
