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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage
 * @generated
 */
public interface TooldefFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TooldefFactory eINSTANCE = org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Script</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Script</em>'.
     * @generated
     */
    Script createScript();

    /**
     * Returns a new object of class '<em>Type Decl</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Type Decl</em>'.
     * @generated
     */
    TypeDecl createTypeDecl();

    /**
     * Returns a new object of class '<em>Type Param</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Type Param</em>'.
     * @generated
     */
    TypeParam createTypeParam();

    /**
     * Returns a new object of class '<em>Tool Parameter</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tool Parameter</em>'.
     * @generated
     */
    ToolParameter createToolParameter();

    /**
     * Returns a new object of class '<em>Tool Def Import</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tool Def Import</em>'.
     * @generated
     */
    ToolDefImport createToolDefImport();

    /**
     * Returns a new object of class '<em>Java Import</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Java Import</em>'.
     * @generated
     */
    JavaImport createJavaImport();

    /**
     * Returns a new object of class '<em>Tool Def Tool</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tool Def Tool</em>'.
     * @generated
     */
    ToolDefTool createToolDefTool();

    /**
     * Returns a new object of class '<em>Java Tool</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Java Tool</em>'.
     * @generated
     */
    JavaTool createJavaTool();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    TooldefPackage getTooldefPackage();

} //TooldefFactory
