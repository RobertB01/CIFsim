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
package org.eclipse.escet.cif.metamodel.cif.functions;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage;

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
 * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsFactory
 * @model kind="package"
 * @generated
 */
public interface FunctionsPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "functions";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/cif/functions";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "functions";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    FunctionsPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionImpl <em>Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getFunction()
     * @generated
     */
    int FUNCTION = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION__POSITION = DeclarationsPackage.DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION__NAME = DeclarationsPackage.DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Return Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION__RETURN_TYPES = DeclarationsPackage.DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION__PARAMETERS = DeclarationsPackage.DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_FEATURE_COUNT = DeclarationsPackage.DECLARATION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_OPERATION_COUNT = DeclarationsPackage.DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionParameterImpl <em>Function Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionParameterImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getFunctionParameter()
     * @generated
     */
    int FUNCTION_PARAMETER = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_PARAMETER__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_PARAMETER__PARAMETER = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Function Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_PARAMETER_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Function Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_PARAMETER_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.InternalFunctionImpl <em>Internal Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.InternalFunctionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getInternalFunction()
     * @generated
     */
    int INTERNAL_FUNCTION = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERNAL_FUNCTION__POSITION = FUNCTION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERNAL_FUNCTION__NAME = FUNCTION__NAME;

    /**
     * The feature id for the '<em><b>Return Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERNAL_FUNCTION__RETURN_TYPES = FUNCTION__RETURN_TYPES;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERNAL_FUNCTION__PARAMETERS = FUNCTION__PARAMETERS;

    /**
     * The feature id for the '<em><b>Variables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERNAL_FUNCTION__VARIABLES = FUNCTION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERNAL_FUNCTION__STATEMENTS = FUNCTION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Internal Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERNAL_FUNCTION_FEATURE_COUNT = FUNCTION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Internal Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERNAL_FUNCTION_OPERATION_COUNT = FUNCTION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.ExternalFunctionImpl <em>External Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.ExternalFunctionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getExternalFunction()
     * @generated
     */
    int EXTERNAL_FUNCTION = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTERNAL_FUNCTION__POSITION = FUNCTION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTERNAL_FUNCTION__NAME = FUNCTION__NAME;

    /**
     * The feature id for the '<em><b>Return Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTERNAL_FUNCTION__RETURN_TYPES = FUNCTION__RETURN_TYPES;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTERNAL_FUNCTION__PARAMETERS = FUNCTION__PARAMETERS;

    /**
     * The feature id for the '<em><b>Function</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTERNAL_FUNCTION__FUNCTION = FUNCTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>External Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTERNAL_FUNCTION_FEATURE_COUNT = FUNCTION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>External Function</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTERNAL_FUNCTION_OPERATION_COUNT = FUNCTION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionStatementImpl <em>Function Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionStatementImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getFunctionStatement()
     * @generated
     */
    int FUNCTION_STATEMENT = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_STATEMENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Function Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_STATEMENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Function Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_STATEMENT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.BreakFuncStatementImpl <em>Break Func Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.BreakFuncStatementImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getBreakFuncStatement()
     * @generated
     */
    int BREAK_FUNC_STATEMENT = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BREAK_FUNC_STATEMENT__POSITION = FUNCTION_STATEMENT__POSITION;

    /**
     * The number of structural features of the '<em>Break Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BREAK_FUNC_STATEMENT_FEATURE_COUNT = FUNCTION_STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Break Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BREAK_FUNC_STATEMENT_OPERATION_COUNT = FUNCTION_STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.ContinueFuncStatementImpl <em>Continue Func Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.ContinueFuncStatementImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getContinueFuncStatement()
     * @generated
     */
    int CONTINUE_FUNC_STATEMENT = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTINUE_FUNC_STATEMENT__POSITION = FUNCTION_STATEMENT__POSITION;

    /**
     * The number of structural features of the '<em>Continue Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTINUE_FUNC_STATEMENT_FEATURE_COUNT = FUNCTION_STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Continue Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTINUE_FUNC_STATEMENT_OPERATION_COUNT = FUNCTION_STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.AssignmentFuncStatementImpl <em>Assignment Func Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.AssignmentFuncStatementImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getAssignmentFuncStatement()
     * @generated
     */
    int ASSIGNMENT_FUNC_STATEMENT = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_FUNC_STATEMENT__POSITION = FUNCTION_STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_FUNC_STATEMENT__VALUE = FUNCTION_STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Addressable</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE = FUNCTION_STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Assignment Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_FUNC_STATEMENT_FEATURE_COUNT = FUNCTION_STATEMENT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Assignment Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_FUNC_STATEMENT_OPERATION_COUNT = FUNCTION_STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.WhileFuncStatementImpl <em>While Func Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.WhileFuncStatementImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getWhileFuncStatement()
     * @generated
     */
    int WHILE_FUNC_STATEMENT = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_FUNC_STATEMENT__POSITION = FUNCTION_STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Guards</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_FUNC_STATEMENT__GUARDS = FUNCTION_STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_FUNC_STATEMENT__STATEMENTS = FUNCTION_STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>While Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_FUNC_STATEMENT_FEATURE_COUNT = FUNCTION_STATEMENT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>While Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_FUNC_STATEMENT_OPERATION_COUNT = FUNCTION_STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.IfFuncStatementImpl <em>If Func Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.IfFuncStatementImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getIfFuncStatement()
     * @generated
     */
    int IF_FUNC_STATEMENT = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_FUNC_STATEMENT__POSITION = FUNCTION_STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Guards</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_FUNC_STATEMENT__GUARDS = FUNCTION_STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Thens</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_FUNC_STATEMENT__THENS = FUNCTION_STATEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Elses</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_FUNC_STATEMENT__ELSES = FUNCTION_STATEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Elifs</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_FUNC_STATEMENT__ELIFS = FUNCTION_STATEMENT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>If Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_FUNC_STATEMENT_FEATURE_COUNT = FUNCTION_STATEMENT_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>If Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_FUNC_STATEMENT_OPERATION_COUNT = FUNCTION_STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.ElifFuncStatementImpl <em>Elif Func Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.ElifFuncStatementImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getElifFuncStatement()
     * @generated
     */
    int ELIF_FUNC_STATEMENT = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_FUNC_STATEMENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Guards</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_FUNC_STATEMENT__GUARDS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Thens</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_FUNC_STATEMENT__THENS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Elif Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_FUNC_STATEMENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Elif Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_FUNC_STATEMENT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.ReturnFuncStatementImpl <em>Return Func Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.ReturnFuncStatementImpl
     * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getReturnFuncStatement()
     * @generated
     */
    int RETURN_FUNC_STATEMENT = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_FUNC_STATEMENT__POSITION = FUNCTION_STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_FUNC_STATEMENT__VALUES = FUNCTION_STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Return Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_FUNC_STATEMENT_FEATURE_COUNT = FUNCTION_STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Return Func Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_FUNC_STATEMENT_OPERATION_COUNT = FUNCTION_STATEMENT_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.Function <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.Function
     * @generated
     */
    EClass getFunction();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.Function#getReturnTypes <em>Return Types</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Return Types</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.Function#getReturnTypes()
     * @see #getFunction()
     * @generated
     */
    EReference getFunction_ReturnTypes();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.Function#getParameters <em>Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameters</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.Function#getParameters()
     * @see #getFunction()
     * @generated
     */
    EReference getFunction_Parameters();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter <em>Function Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter
     * @generated
     */
    EClass getFunctionParameter();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter#getParameter()
     * @see #getFunctionParameter()
     * @generated
     */
    EReference getFunctionParameter_Parameter();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction <em>Internal Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Internal Function</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction
     * @generated
     */
    EClass getInternalFunction();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction#getVariables <em>Variables</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Variables</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction#getVariables()
     * @see #getInternalFunction()
     * @generated
     */
    EReference getInternalFunction_Variables();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction#getStatements <em>Statements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Statements</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction#getStatements()
     * @see #getInternalFunction()
     * @generated
     */
    EReference getInternalFunction_Statements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction <em>External Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>External Function</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction
     * @generated
     */
    EClass getExternalFunction();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Function</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction#getFunction()
     * @see #getExternalFunction()
     * @generated
     */
    EAttribute getExternalFunction_Function();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement <em>Function Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function Statement</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement
     * @generated
     */
    EClass getFunctionStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement <em>Break Func Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Break Func Statement</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement
     * @generated
     */
    EClass getBreakFuncStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement <em>Continue Func Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Continue Func Statement</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement
     * @generated
     */
    EClass getContinueFuncStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement <em>Assignment Func Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assignment Func Statement</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement
     * @generated
     */
    EClass getAssignmentFuncStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement#getValue()
     * @see #getAssignmentFuncStatement()
     * @generated
     */
    EReference getAssignmentFuncStatement_Value();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement#getAddressable <em>Addressable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Addressable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement#getAddressable()
     * @see #getAssignmentFuncStatement()
     * @generated
     */
    EReference getAssignmentFuncStatement_Addressable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement <em>While Func Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>While Func Statement</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement
     * @generated
     */
    EClass getWhileFuncStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement#getGuards <em>Guards</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Guards</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement#getGuards()
     * @see #getWhileFuncStatement()
     * @generated
     */
    EReference getWhileFuncStatement_Guards();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement#getStatements <em>Statements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Statements</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement#getStatements()
     * @see #getWhileFuncStatement()
     * @generated
     */
    EReference getWhileFuncStatement_Statements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement <em>If Func Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>If Func Statement</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement
     * @generated
     */
    EClass getIfFuncStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getGuards <em>Guards</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Guards</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getGuards()
     * @see #getIfFuncStatement()
     * @generated
     */
    EReference getIfFuncStatement_Guards();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getThens <em>Thens</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Thens</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getThens()
     * @see #getIfFuncStatement()
     * @generated
     */
    EReference getIfFuncStatement_Thens();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getElses <em>Elses</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elses</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getElses()
     * @see #getIfFuncStatement()
     * @generated
     */
    EReference getIfFuncStatement_Elses();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getElifs <em>Elifs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elifs</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement#getElifs()
     * @see #getIfFuncStatement()
     * @generated
     */
    EReference getIfFuncStatement_Elifs();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement <em>Elif Func Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Elif Func Statement</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement
     * @generated
     */
    EClass getElifFuncStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement#getGuards <em>Guards</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Guards</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement#getGuards()
     * @see #getElifFuncStatement()
     * @generated
     */
    EReference getElifFuncStatement_Guards();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement#getThens <em>Thens</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Thens</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement#getThens()
     * @see #getElifFuncStatement()
     * @generated
     */
    EReference getElifFuncStatement_Thens();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement <em>Return Func Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Return Func Statement</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement
     * @generated
     */
    EClass getReturnFuncStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Values</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement#getValues()
     * @see #getReturnFuncStatement()
     * @generated
     */
    EReference getReturnFuncStatement_Values();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    FunctionsFactory getFunctionsFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionImpl <em>Function</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getFunction()
         * @generated
         */
        EClass FUNCTION = eINSTANCE.getFunction();

        /**
         * The meta object literal for the '<em><b>Return Types</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION__RETURN_TYPES = eINSTANCE.getFunction_ReturnTypes();

        /**
         * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION__PARAMETERS = eINSTANCE.getFunction_Parameters();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionParameterImpl <em>Function Parameter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionParameterImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getFunctionParameter()
         * @generated
         */
        EClass FUNCTION_PARAMETER = eINSTANCE.getFunctionParameter();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_PARAMETER__PARAMETER = eINSTANCE.getFunctionParameter_Parameter();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.InternalFunctionImpl <em>Internal Function</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.InternalFunctionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getInternalFunction()
         * @generated
         */
        EClass INTERNAL_FUNCTION = eINSTANCE.getInternalFunction();

        /**
         * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INTERNAL_FUNCTION__VARIABLES = eINSTANCE.getInternalFunction_Variables();

        /**
         * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INTERNAL_FUNCTION__STATEMENTS = eINSTANCE.getInternalFunction_Statements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.ExternalFunctionImpl <em>External Function</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.ExternalFunctionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getExternalFunction()
         * @generated
         */
        EClass EXTERNAL_FUNCTION = eINSTANCE.getExternalFunction();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXTERNAL_FUNCTION__FUNCTION = eINSTANCE.getExternalFunction_Function();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionStatementImpl <em>Function Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionStatementImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getFunctionStatement()
         * @generated
         */
        EClass FUNCTION_STATEMENT = eINSTANCE.getFunctionStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.BreakFuncStatementImpl <em>Break Func Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.BreakFuncStatementImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getBreakFuncStatement()
         * @generated
         */
        EClass BREAK_FUNC_STATEMENT = eINSTANCE.getBreakFuncStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.ContinueFuncStatementImpl <em>Continue Func Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.ContinueFuncStatementImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getContinueFuncStatement()
         * @generated
         */
        EClass CONTINUE_FUNC_STATEMENT = eINSTANCE.getContinueFuncStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.AssignmentFuncStatementImpl <em>Assignment Func Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.AssignmentFuncStatementImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getAssignmentFuncStatement()
         * @generated
         */
        EClass ASSIGNMENT_FUNC_STATEMENT = eINSTANCE.getAssignmentFuncStatement();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSIGNMENT_FUNC_STATEMENT__VALUE = eINSTANCE.getAssignmentFuncStatement_Value();

        /**
         * The meta object literal for the '<em><b>Addressable</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE = eINSTANCE.getAssignmentFuncStatement_Addressable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.WhileFuncStatementImpl <em>While Func Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.WhileFuncStatementImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getWhileFuncStatement()
         * @generated
         */
        EClass WHILE_FUNC_STATEMENT = eINSTANCE.getWhileFuncStatement();

        /**
         * The meta object literal for the '<em><b>Guards</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WHILE_FUNC_STATEMENT__GUARDS = eINSTANCE.getWhileFuncStatement_Guards();

        /**
         * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WHILE_FUNC_STATEMENT__STATEMENTS = eINSTANCE.getWhileFuncStatement_Statements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.IfFuncStatementImpl <em>If Func Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.IfFuncStatementImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getIfFuncStatement()
         * @generated
         */
        EClass IF_FUNC_STATEMENT = eINSTANCE.getIfFuncStatement();

        /**
         * The meta object literal for the '<em><b>Guards</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_FUNC_STATEMENT__GUARDS = eINSTANCE.getIfFuncStatement_Guards();

        /**
         * The meta object literal for the '<em><b>Thens</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_FUNC_STATEMENT__THENS = eINSTANCE.getIfFuncStatement_Thens();

        /**
         * The meta object literal for the '<em><b>Elses</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_FUNC_STATEMENT__ELSES = eINSTANCE.getIfFuncStatement_Elses();

        /**
         * The meta object literal for the '<em><b>Elifs</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_FUNC_STATEMENT__ELIFS = eINSTANCE.getIfFuncStatement_Elifs();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.ElifFuncStatementImpl <em>Elif Func Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.ElifFuncStatementImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getElifFuncStatement()
         * @generated
         */
        EClass ELIF_FUNC_STATEMENT = eINSTANCE.getElifFuncStatement();

        /**
         * The meta object literal for the '<em><b>Guards</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ELIF_FUNC_STATEMENT__GUARDS = eINSTANCE.getElifFuncStatement_Guards();

        /**
         * The meta object literal for the '<em><b>Thens</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ELIF_FUNC_STATEMENT__THENS = eINSTANCE.getElifFuncStatement_Thens();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.ReturnFuncStatementImpl <em>Return Func Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.ReturnFuncStatementImpl
         * @see org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl#getReturnFuncStatement()
         * @generated
         */
        EClass RETURN_FUNC_STATEMENT = eINSTANCE.getReturnFuncStatement();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RETURN_FUNC_STATEMENT__VALUES = eINSTANCE.getReturnFuncStatement_Values();

    }

} //FunctionsPackage
