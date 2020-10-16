/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;

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
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsFactory
 * @model kind="package"
 * @generated
 */
public interface StatementsPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "statements";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/tooldef/statements";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "statements";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    StatementsPackage eINSTANCE = org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementImpl <em>Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getStatement()
     * @generated
     */
    int STATEMENT = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATEMENT__POSITION = TooldefPackage.DECLARATION__POSITION;

    /**
     * The number of structural features of the '<em>Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATEMENT_FEATURE_COUNT = TooldefPackage.DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATEMENT_OPERATION_COUNT = TooldefPackage.DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.VariableImpl <em>Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.VariableImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getVariable()
     * @generated
     */
    int VARIABLE = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE__NAME = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE__TYPE = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE__VALUE = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.WhileStatementImpl <em>While Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.WhileStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getWhileStatement()
     * @generated
     */
    int WHILE_STATEMENT = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT__CONDITION = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT__STATEMENTS = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>While Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>While Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.BreakStatementImpl <em>Break Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.BreakStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getBreakStatement()
     * @generated
     */
    int BREAK_STATEMENT = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BREAK_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The number of structural features of the '<em>Break Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BREAK_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Break Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BREAK_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ContinueStatementImpl <em>Continue Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ContinueStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getContinueStatement()
     * @generated
     */
    int CONTINUE_STATEMENT = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTINUE_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The number of structural features of the '<em>Continue Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTINUE_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Continue Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTINUE_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ReturnStatementImpl <em>Return Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ReturnStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getReturnStatement()
     * @generated
     */
    int RETURN_STATEMENT = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_STATEMENT__VALUES = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Return Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Return Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ExitStatementImpl <em>Exit Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ExitStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getExitStatement()
     * @generated
     */
    int EXIT_STATEMENT = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXIT_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Exit Code</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXIT_STATEMENT__EXIT_CODE = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Exit Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXIT_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Exit Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXIT_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ForStatementImpl <em>For Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ForStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getForStatement()
     * @generated
     */
    int FOR_STATEMENT = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT__STATEMENTS = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT__SOURCE = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Addressables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT__ADDRESSABLES = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>For Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>For Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.IfStatementImpl <em>If Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.IfStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getIfStatement()
     * @generated
     */
    int IF_STATEMENT = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT__CONDITION = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Thens</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT__THENS = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Elifs</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT__ELIFS = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Elses</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT__ELSES = STATEMENT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>If Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>If Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.AssignmentStatementImpl <em>Assignment Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.AssignmentStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getAssignmentStatement()
     * @generated
     */
    int ASSIGNMENT_STATEMENT = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Addressables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT__ADDRESSABLES = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT__VALUES = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Assignment Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Assignment Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ToolInvokeStatementImpl <em>Tool Invoke Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ToolInvokeStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getToolInvokeStatement()
     * @generated
     */
    int TOOL_INVOKE_STATEMENT = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Invocation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_STATEMENT__INVOCATION = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tool Invoke Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Tool Invoke Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_STATEMENT_OPERATION_COUNT = STATEMENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ElifStatementImpl <em>Elif Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ElifStatementImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getElifStatement()
     * @generated
     */
    int ELIF_STATEMENT = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_STATEMENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_STATEMENT__CONDITION = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Thens</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_STATEMENT__THENS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Elif Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_STATEMENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Elif Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_STATEMENT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.AddressableDeclImpl <em>Addressable Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.AddressableDeclImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getAddressableDecl()
     * @generated
     */
    int ADDRESSABLE_DECL = 12;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADDRESSABLE_DECL__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Addressable Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADDRESSABLE_DECL_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Addressable Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADDRESSABLE_DECL_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.TupleAddressableDeclImpl <em>Tuple Addressable Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.TupleAddressableDeclImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getTupleAddressableDecl()
     * @generated
     */
    int TUPLE_ADDRESSABLE_DECL = 13;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_ADDRESSABLE_DECL__POSITION = ADDRESSABLE_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_ADDRESSABLE_DECL__ELEMENTS = ADDRESSABLE_DECL_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tuple Addressable Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_ADDRESSABLE_DECL_FEATURE_COUNT = ADDRESSABLE_DECL_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Tuple Addressable Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_ADDRESSABLE_DECL_OPERATION_COUNT = ADDRESSABLE_DECL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.VariableAddressableDeclImpl <em>Variable Addressable Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.VariableAddressableDeclImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getVariableAddressableDecl()
     * @generated
     */
    int VARIABLE_ADDRESSABLE_DECL = 14;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_ADDRESSABLE_DECL__POSITION = ADDRESSABLE_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Variable</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_ADDRESSABLE_DECL__VARIABLE = ADDRESSABLE_DECL_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Variable Addressable Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_ADDRESSABLE_DECL_FEATURE_COUNT = ADDRESSABLE_DECL_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Variable Addressable Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_ADDRESSABLE_DECL_OPERATION_COUNT = ADDRESSABLE_DECL_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement <em>Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement
     * @generated
     */
    EClass getStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Variable</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable
     * @generated
     */
    EClass getVariable();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable#getName()
     * @see #getVariable()
     * @generated
     */
    EAttribute getVariable_Name();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable#getType()
     * @see #getVariable()
     * @generated
     */
    EReference getVariable_Type();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable#getValue()
     * @see #getVariable()
     * @generated
     */
    EReference getVariable_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement <em>While Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>While Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement
     * @generated
     */
    EClass getWhileStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement#getCondition <em>Condition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Condition</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement#getCondition()
     * @see #getWhileStatement()
     * @generated
     */
    EReference getWhileStatement_Condition();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement#getStatements <em>Statements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Statements</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement#getStatements()
     * @see #getWhileStatement()
     * @generated
     */
    EReference getWhileStatement_Statements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement <em>Break Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Break Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement
     * @generated
     */
    EClass getBreakStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement <em>Continue Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Continue Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement
     * @generated
     */
    EClass getContinueStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement <em>Return Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Return Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement
     * @generated
     */
    EClass getReturnStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Values</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement#getValues()
     * @see #getReturnStatement()
     * @generated
     */
    EReference getReturnStatement_Values();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement <em>Exit Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Exit Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement
     * @generated
     */
    EClass getExitStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement#getExitCode <em>Exit Code</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Exit Code</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement#getExitCode()
     * @see #getExitStatement()
     * @generated
     */
    EReference getExitStatement_ExitCode();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement <em>For Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>For Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement
     * @generated
     */
    EClass getForStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement#getStatements <em>Statements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Statements</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement#getStatements()
     * @see #getForStatement()
     * @generated
     */
    EReference getForStatement_Statements();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Source</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement#getSource()
     * @see #getForStatement()
     * @generated
     */
    EReference getForStatement_Source();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement#getAddressables <em>Addressables</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Addressables</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement#getAddressables()
     * @see #getForStatement()
     * @generated
     */
    EReference getForStatement_Addressables();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement <em>If Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>If Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement
     * @generated
     */
    EClass getIfStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement#getCondition <em>Condition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Condition</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement#getCondition()
     * @see #getIfStatement()
     * @generated
     */
    EReference getIfStatement_Condition();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement#getThens <em>Thens</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Thens</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement#getThens()
     * @see #getIfStatement()
     * @generated
     */
    EReference getIfStatement_Thens();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement#getElifs <em>Elifs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elifs</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement#getElifs()
     * @see #getIfStatement()
     * @generated
     */
    EReference getIfStatement_Elifs();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement#getElses <em>Elses</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elses</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement#getElses()
     * @see #getIfStatement()
     * @generated
     */
    EReference getIfStatement_Elses();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement <em>Assignment Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assignment Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement
     * @generated
     */
    EClass getAssignmentStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement#getAddressables <em>Addressables</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Addressables</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement#getAddressables()
     * @see #getAssignmentStatement()
     * @generated
     */
    EReference getAssignmentStatement_Addressables();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Values</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement#getValues()
     * @see #getAssignmentStatement()
     * @generated
     */
    EReference getAssignmentStatement_Values();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement <em>Tool Invoke Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Invoke Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement
     * @generated
     */
    EClass getToolInvokeStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement#getInvocation <em>Invocation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Invocation</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement#getInvocation()
     * @see #getToolInvokeStatement()
     * @generated
     */
    EReference getToolInvokeStatement_Invocation();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement <em>Elif Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Elif Statement</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement
     * @generated
     */
    EClass getElifStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement#getCondition <em>Condition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Condition</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement#getCondition()
     * @see #getElifStatement()
     * @generated
     */
    EReference getElifStatement_Condition();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement#getThens <em>Thens</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Thens</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement#getThens()
     * @see #getElifStatement()
     * @generated
     */
    EReference getElifStatement_Thens();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl <em>Addressable Decl</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Addressable Decl</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl
     * @generated
     */
    EClass getAddressableDecl();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl <em>Tuple Addressable Decl</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tuple Addressable Decl</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl
     * @generated
     */
    EClass getTupleAddressableDecl();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl#getElements()
     * @see #getTupleAddressableDecl()
     * @generated
     */
    EReference getTupleAddressableDecl_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl <em>Variable Addressable Decl</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Variable Addressable Decl</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl
     * @generated
     */
    EClass getVariableAddressableDecl();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Variable</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl#getVariable()
     * @see #getVariableAddressableDecl()
     * @generated
     */
    EReference getVariableAddressableDecl_Variable();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    StatementsFactory getStatementsFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementImpl <em>Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getStatement()
         * @generated
         */
        EClass STATEMENT = eINSTANCE.getStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.VariableImpl <em>Variable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.VariableImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getVariable()
         * @generated
         */
        EClass VARIABLE = eINSTANCE.getVariable();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VARIABLE__NAME = eINSTANCE.getVariable_Name();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference VARIABLE__TYPE = eINSTANCE.getVariable_Type();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference VARIABLE__VALUE = eINSTANCE.getVariable_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.WhileStatementImpl <em>While Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.WhileStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getWhileStatement()
         * @generated
         */
        EClass WHILE_STATEMENT = eINSTANCE.getWhileStatement();

        /**
         * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WHILE_STATEMENT__CONDITION = eINSTANCE.getWhileStatement_Condition();

        /**
         * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WHILE_STATEMENT__STATEMENTS = eINSTANCE.getWhileStatement_Statements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.BreakStatementImpl <em>Break Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.BreakStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getBreakStatement()
         * @generated
         */
        EClass BREAK_STATEMENT = eINSTANCE.getBreakStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ContinueStatementImpl <em>Continue Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ContinueStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getContinueStatement()
         * @generated
         */
        EClass CONTINUE_STATEMENT = eINSTANCE.getContinueStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ReturnStatementImpl <em>Return Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ReturnStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getReturnStatement()
         * @generated
         */
        EClass RETURN_STATEMENT = eINSTANCE.getReturnStatement();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RETURN_STATEMENT__VALUES = eINSTANCE.getReturnStatement_Values();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ExitStatementImpl <em>Exit Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ExitStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getExitStatement()
         * @generated
         */
        EClass EXIT_STATEMENT = eINSTANCE.getExitStatement();

        /**
         * The meta object literal for the '<em><b>Exit Code</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXIT_STATEMENT__EXIT_CODE = eINSTANCE.getExitStatement_ExitCode();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ForStatementImpl <em>For Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ForStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getForStatement()
         * @generated
         */
        EClass FOR_STATEMENT = eINSTANCE.getForStatement();

        /**
         * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FOR_STATEMENT__STATEMENTS = eINSTANCE.getForStatement_Statements();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FOR_STATEMENT__SOURCE = eINSTANCE.getForStatement_Source();

        /**
         * The meta object literal for the '<em><b>Addressables</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FOR_STATEMENT__ADDRESSABLES = eINSTANCE.getForStatement_Addressables();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.IfStatementImpl <em>If Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.IfStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getIfStatement()
         * @generated
         */
        EClass IF_STATEMENT = eINSTANCE.getIfStatement();

        /**
         * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_STATEMENT__CONDITION = eINSTANCE.getIfStatement_Condition();

        /**
         * The meta object literal for the '<em><b>Thens</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_STATEMENT__THENS = eINSTANCE.getIfStatement_Thens();

        /**
         * The meta object literal for the '<em><b>Elifs</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_STATEMENT__ELIFS = eINSTANCE.getIfStatement_Elifs();

        /**
         * The meta object literal for the '<em><b>Elses</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_STATEMENT__ELSES = eINSTANCE.getIfStatement_Elses();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.AssignmentStatementImpl <em>Assignment Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.AssignmentStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getAssignmentStatement()
         * @generated
         */
        EClass ASSIGNMENT_STATEMENT = eINSTANCE.getAssignmentStatement();

        /**
         * The meta object literal for the '<em><b>Addressables</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSIGNMENT_STATEMENT__ADDRESSABLES = eINSTANCE.getAssignmentStatement_Addressables();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSIGNMENT_STATEMENT__VALUES = eINSTANCE.getAssignmentStatement_Values();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ToolInvokeStatementImpl <em>Tool Invoke Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ToolInvokeStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getToolInvokeStatement()
         * @generated
         */
        EClass TOOL_INVOKE_STATEMENT = eINSTANCE.getToolInvokeStatement();

        /**
         * The meta object literal for the '<em><b>Invocation</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_INVOKE_STATEMENT__INVOCATION = eINSTANCE.getToolInvokeStatement_Invocation();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ElifStatementImpl <em>Elif Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ElifStatementImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getElifStatement()
         * @generated
         */
        EClass ELIF_STATEMENT = eINSTANCE.getElifStatement();

        /**
         * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ELIF_STATEMENT__CONDITION = eINSTANCE.getElifStatement_Condition();

        /**
         * The meta object literal for the '<em><b>Thens</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ELIF_STATEMENT__THENS = eINSTANCE.getElifStatement_Thens();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.AddressableDeclImpl <em>Addressable Decl</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.AddressableDeclImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getAddressableDecl()
         * @generated
         */
        EClass ADDRESSABLE_DECL = eINSTANCE.getAddressableDecl();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.TupleAddressableDeclImpl <em>Tuple Addressable Decl</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.TupleAddressableDeclImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getTupleAddressableDecl()
         * @generated
         */
        EClass TUPLE_ADDRESSABLE_DECL = eINSTANCE.getTupleAddressableDecl();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TUPLE_ADDRESSABLE_DECL__ELEMENTS = eINSTANCE.getTupleAddressableDecl_Elements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.VariableAddressableDeclImpl <em>Variable Addressable Decl</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.VariableAddressableDeclImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl#getVariableAddressableDecl()
         * @generated
         */
        EClass VARIABLE_ADDRESSABLE_DECL = eINSTANCE.getVariableAddressableDecl();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference VARIABLE_ADDRESSABLE_DECL__VARIABLE = eINSTANCE.getVariableAddressableDecl_Variable();

    }

} //StatementsPackage
