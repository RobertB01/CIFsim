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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefFactory
 * @model kind="package"
 * @generated
 */
public interface TooldefPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "tooldef";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/tooldef";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "tooldef";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TooldefPackage eINSTANCE = org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.DeclarationImpl <em>Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.DeclarationImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getDeclaration()
     * @generated
     */
    int DECLARATION = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ScriptImpl <em>Script</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ScriptImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getScript()
     * @generated
     */
    int SCRIPT = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Declarations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT__DECLARATIONS = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT__NAME = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Script</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Script</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCRIPT_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ImportImpl <em>Import</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ImportImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getImport()
     * @generated
     */
    int IMPORT = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORT__POSITION = DECLARATION__POSITION;

    /**
     * The number of structural features of the '<em>Import</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORT_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Import</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORT_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.TypeDeclImpl <em>Type Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TypeDeclImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getTypeDecl()
     * @generated
     */
    int TYPE_DECL = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL__NAME = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL__TYPE = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Type Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Type Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolImpl <em>Tool</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getTool()
     * @generated
     */
    int TOOL = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL__NAME = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Return Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL__RETURN_TYPES = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Type Params</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL__TYPE_PARAMS = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL__PARAMETERS = DECLARATION_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Tool</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>Tool</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.TypeParamImpl <em>Type Param</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TypeParamImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getTypeParam()
     * @generated
     */
    int TYPE_PARAM = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Type Param</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Type Param</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolParameterImpl <em>Tool Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolParameterImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getToolParameter()
     * @generated
     */
    int TOOL_PARAMETER = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAMETER__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAMETER__TYPE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAMETER__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Variadic</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAMETER__VARIADIC = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAMETER__VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Tool Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAMETER_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>Tool Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAMETER_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefImportImpl <em>Tool Def Import</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefImportImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getToolDefImport()
     * @generated
     */
    int TOOL_DEF_IMPORT = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_IMPORT__POSITION = IMPORT__POSITION;

    /**
     * The feature id for the '<em><b>Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_IMPORT__SOURCE = IMPORT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Orig Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_IMPORT__ORIG_NAME = IMPORT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>As Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_IMPORT__AS_NAME = IMPORT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Tool Def Import</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_IMPORT_FEATURE_COUNT = IMPORT_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Tool Def Import</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_IMPORT_OPERATION_COUNT = IMPORT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaImportImpl <em>Java Import</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaImportImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getJavaImport()
     * @generated
     */
    int JAVA_IMPORT = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_IMPORT__POSITION = IMPORT__POSITION;

    /**
     * The feature id for the '<em><b>Plugin Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_IMPORT__PLUGIN_NAME = IMPORT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Method Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_IMPORT__METHOD_NAME = IMPORT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>As Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_IMPORT__AS_NAME = IMPORT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Java Import</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_IMPORT_FEATURE_COUNT = IMPORT_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Java Import</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_IMPORT_OPERATION_COUNT = IMPORT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefToolImpl <em>Tool Def Tool</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefToolImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getToolDefTool()
     * @generated
     */
    int TOOL_DEF_TOOL = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TOOL__POSITION = TOOL__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TOOL__NAME = TOOL__NAME;

    /**
     * The feature id for the '<em><b>Return Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TOOL__RETURN_TYPES = TOOL__RETURN_TYPES;

    /**
     * The feature id for the '<em><b>Type Params</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TOOL__TYPE_PARAMS = TOOL__TYPE_PARAMS;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TOOL__PARAMETERS = TOOL__PARAMETERS;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TOOL__STATEMENTS = TOOL_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tool Def Tool</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TOOL_FEATURE_COUNT = TOOL_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Tool Def Tool</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TOOL_OPERATION_COUNT = TOOL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaToolImpl <em>Java Tool</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaToolImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getJavaTool()
     * @generated
     */
    int JAVA_TOOL = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL__POSITION = TOOL__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL__NAME = TOOL__NAME;

    /**
     * The feature id for the '<em><b>Return Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL__RETURN_TYPES = TOOL__RETURN_TYPES;

    /**
     * The feature id for the '<em><b>Type Params</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL__TYPE_PARAMS = TOOL__TYPE_PARAMS;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL__PARAMETERS = TOOL__PARAMETERS;

    /**
     * The feature id for the '<em><b>Plugin Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL__PLUGIN_NAME = TOOL_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Method Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL__METHOD_NAME = TOOL_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Method</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL__METHOD = TOOL_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Java Tool</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL_FEATURE_COUNT = TOOL_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Java Tool</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int JAVA_TOOL_OPERATION_COUNT = TOOL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '<em>Token</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.setext.runtime.Token
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getToken()
     * @generated
     */
    int TOKEN = 11;

    /**
     * The meta object id for the '<em>Method</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.reflect.Method
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getMethod()
     * @generated
     */
    int METHOD = 12;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Script <em>Script</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Script</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Script
     * @generated
     */
    EClass getScript();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Script#getDeclarations <em>Declarations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Declarations</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Script#getDeclarations()
     * @see #getScript()
     * @generated
     */
    EReference getScript_Declarations();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Script#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Script#getName()
     * @see #getScript()
     * @generated
     */
    EAttribute getScript_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Declaration <em>Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Declaration</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Declaration
     * @generated
     */
    EClass getDeclaration();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Import <em>Import</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Import</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Import
     * @generated
     */
    EClass getImport();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl <em>Type Decl</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Decl</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl
     * @generated
     */
    EClass getTypeDecl();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl#getName()
     * @see #getTypeDecl()
     * @generated
     */
    EAttribute getTypeDecl_Name();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl#getType()
     * @see #getTypeDecl()
     * @generated
     */
    EReference getTypeDecl_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Tool <em>Tool</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Tool
     * @generated
     */
    EClass getTool();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Tool#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Tool#getName()
     * @see #getTool()
     * @generated
     */
    EAttribute getTool_Name();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Tool#getReturnTypes <em>Return Types</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Return Types</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Tool#getReturnTypes()
     * @see #getTool()
     * @generated
     */
    EReference getTool_ReturnTypes();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Tool#getTypeParams <em>Type Params</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Type Params</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Tool#getTypeParams()
     * @see #getTool()
     * @generated
     */
    EReference getTool_TypeParams();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Tool#getParameters <em>Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameters</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Tool#getParameters()
     * @see #getTool()
     * @generated
     */
    EReference getTool_Parameters();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam <em>Type Param</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Param</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam
     * @generated
     */
    EClass getTypeParam();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam#getName()
     * @see #getTypeParam()
     * @generated
     */
    EAttribute getTypeParam_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter <em>Tool Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Parameter</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter
     * @generated
     */
    EClass getToolParameter();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getType()
     * @see #getToolParameter()
     * @generated
     */
    EReference getToolParameter_Type();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getName()
     * @see #getToolParameter()
     * @generated
     */
    EAttribute getToolParameter_Name();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#isVariadic <em>Variadic</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Variadic</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#isVariadic()
     * @see #getToolParameter()
     * @generated
     */
    EAttribute getToolParameter_Variadic();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter#getValue()
     * @see #getToolParameter()
     * @generated
     */
    EReference getToolParameter_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport <em>Tool Def Import</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Def Import</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport
     * @generated
     */
    EClass getToolDefImport();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Source</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getSource()
     * @see #getToolDefImport()
     * @generated
     */
    EAttribute getToolDefImport_Source();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getOrigName <em>Orig Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Orig Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getOrigName()
     * @see #getToolDefImport()
     * @generated
     */
    EAttribute getToolDefImport_OrigName();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getAsName <em>As Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>As Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport#getAsName()
     * @see #getToolDefImport()
     * @generated
     */
    EAttribute getToolDefImport_AsName();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport <em>Java Import</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Java Import</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport
     * @generated
     */
    EClass getJavaImport();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getPluginName <em>Plugin Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Plugin Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getPluginName()
     * @see #getJavaImport()
     * @generated
     */
    EAttribute getJavaImport_PluginName();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getMethodName <em>Method Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Method Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getMethodName()
     * @see #getJavaImport()
     * @generated
     */
    EAttribute getJavaImport_MethodName();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getAsName <em>As Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>As Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport#getAsName()
     * @see #getJavaImport()
     * @generated
     */
    EAttribute getJavaImport_AsName();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool <em>Tool Def Tool</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Def Tool</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool
     * @generated
     */
    EClass getToolDefTool();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool#getStatements <em>Statements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Statements</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool#getStatements()
     * @see #getToolDefTool()
     * @generated
     */
    EReference getToolDefTool_Statements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool <em>Java Tool</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Java Tool</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool
     * @generated
     */
    EClass getJavaTool();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getPluginName <em>Plugin Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Plugin Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getPluginName()
     * @see #getJavaTool()
     * @generated
     */
    EAttribute getJavaTool_PluginName();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getMethodName <em>Method Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Method Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getMethodName()
     * @see #getJavaTool()
     * @generated
     */
    EAttribute getJavaTool_MethodName();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getMethod <em>Method</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Method</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool#getMethod()
     * @see #getJavaTool()
     * @generated
     */
    EAttribute getJavaTool_Method();

    /**
     * Returns the meta object for data type '{@link org.eclipse.escet.setext.runtime.Token <em>Token</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Token</em>'.
     * @see org.eclipse.escet.setext.runtime.Token
     * @model instanceClass="org.eclipse.escet.setext.runtime.Token"
     * @generated
     */
    EDataType getToken();

    /**
     * Returns the meta object for data type '{@link java.lang.reflect.Method <em>Method</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Method</em>'.
     * @see java.lang.reflect.Method
     * @model instanceClass="java.lang.reflect.Method"
     * @generated
     */
    EDataType getMethod();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    TooldefFactory getTooldefFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals
    {
        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ScriptImpl <em>Script</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ScriptImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getScript()
         * @generated
         */
        EClass SCRIPT = eINSTANCE.getScript();

        /**
         * The meta object literal for the '<em><b>Declarations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SCRIPT__DECLARATIONS = eINSTANCE.getScript_Declarations();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SCRIPT__NAME = eINSTANCE.getScript_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.DeclarationImpl <em>Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.DeclarationImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getDeclaration()
         * @generated
         */
        EClass DECLARATION = eINSTANCE.getDeclaration();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ImportImpl <em>Import</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ImportImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getImport()
         * @generated
         */
        EClass IMPORT = eINSTANCE.getImport();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.TypeDeclImpl <em>Type Decl</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TypeDeclImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getTypeDecl()
         * @generated
         */
        EClass TYPE_DECL = eINSTANCE.getTypeDecl();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TYPE_DECL__NAME = eINSTANCE.getTypeDecl_Name();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TYPE_DECL__TYPE = eINSTANCE.getTypeDecl_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolImpl <em>Tool</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getTool()
         * @generated
         */
        EClass TOOL = eINSTANCE.getTool();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL__NAME = eINSTANCE.getTool_Name();

        /**
         * The meta object literal for the '<em><b>Return Types</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL__RETURN_TYPES = eINSTANCE.getTool_ReturnTypes();

        /**
         * The meta object literal for the '<em><b>Type Params</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL__TYPE_PARAMS = eINSTANCE.getTool_TypeParams();

        /**
         * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL__PARAMETERS = eINSTANCE.getTool_Parameters();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.TypeParamImpl <em>Type Param</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TypeParamImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getTypeParam()
         * @generated
         */
        EClass TYPE_PARAM = eINSTANCE.getTypeParam();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TYPE_PARAM__NAME = eINSTANCE.getTypeParam_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolParameterImpl <em>Tool Parameter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolParameterImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getToolParameter()
         * @generated
         */
        EClass TOOL_PARAMETER = eINSTANCE.getToolParameter();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_PARAMETER__TYPE = eINSTANCE.getToolParameter_Type();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_PARAMETER__NAME = eINSTANCE.getToolParameter_Name();

        /**
         * The meta object literal for the '<em><b>Variadic</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_PARAMETER__VARIADIC = eINSTANCE.getToolParameter_Variadic();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_PARAMETER__VALUE = eINSTANCE.getToolParameter_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefImportImpl <em>Tool Def Import</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefImportImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getToolDefImport()
         * @generated
         */
        EClass TOOL_DEF_IMPORT = eINSTANCE.getToolDefImport();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_DEF_IMPORT__SOURCE = eINSTANCE.getToolDefImport_Source();

        /**
         * The meta object literal for the '<em><b>Orig Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_DEF_IMPORT__ORIG_NAME = eINSTANCE.getToolDefImport_OrigName();

        /**
         * The meta object literal for the '<em><b>As Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_DEF_IMPORT__AS_NAME = eINSTANCE.getToolDefImport_AsName();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaImportImpl <em>Java Import</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaImportImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getJavaImport()
         * @generated
         */
        EClass JAVA_IMPORT = eINSTANCE.getJavaImport();

        /**
         * The meta object literal for the '<em><b>Plugin Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JAVA_IMPORT__PLUGIN_NAME = eINSTANCE.getJavaImport_PluginName();

        /**
         * The meta object literal for the '<em><b>Method Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JAVA_IMPORT__METHOD_NAME = eINSTANCE.getJavaImport_MethodName();

        /**
         * The meta object literal for the '<em><b>As Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JAVA_IMPORT__AS_NAME = eINSTANCE.getJavaImport_AsName();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefToolImpl <em>Tool Def Tool</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefToolImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getToolDefTool()
         * @generated
         */
        EClass TOOL_DEF_TOOL = eINSTANCE.getToolDefTool();

        /**
         * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_DEF_TOOL__STATEMENTS = eINSTANCE.getToolDefTool_Statements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaToolImpl <em>Java Tool</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaToolImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getJavaTool()
         * @generated
         */
        EClass JAVA_TOOL = eINSTANCE.getJavaTool();

        /**
         * The meta object literal for the '<em><b>Plugin Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JAVA_TOOL__PLUGIN_NAME = eINSTANCE.getJavaTool_PluginName();

        /**
         * The meta object literal for the '<em><b>Method Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JAVA_TOOL__METHOD_NAME = eINSTANCE.getJavaTool_MethodName();

        /**
         * The meta object literal for the '<em><b>Method</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute JAVA_TOOL__METHOD = eINSTANCE.getJavaTool_Method();

        /**
         * The meta object literal for the '<em>Token</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.setext.runtime.Token
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getToken()
         * @generated
         */
        EDataType TOKEN = eINSTANCE.getToken();

        /**
         * The meta object literal for the '<em>Method</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.reflect.Method
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl#getMethod()
         * @generated
         */
        EDataType METHOD = eINSTANCE.getMethod();

    }

} //TooldefPackage
