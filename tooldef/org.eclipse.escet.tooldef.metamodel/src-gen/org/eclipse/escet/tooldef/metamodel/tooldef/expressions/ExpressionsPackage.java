/**
 * Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsFactory
 * @model kind="package"
 * @generated
 */
public interface ExpressionsPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "expressions";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/tooldef/expressions";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "expressions";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ExpressionsPackage eINSTANCE = org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionImpl <em>Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getExpression()
     * @generated
     */
    int EXPRESSION = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXPRESSION__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXPRESSION__TYPE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXPRESSION_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXPRESSION_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolInvokeExpressionImpl <em>Tool Invoke Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolInvokeExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getToolInvokeExpression()
     * @generated
     */
    int TOOL_INVOKE_EXPRESSION = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_EXPRESSION__ARGUMENTS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Tool</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_EXPRESSION__TOOL = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Tool Invoke Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Tool Invoke Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_INVOKE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolRefImpl <em>Tool Ref</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolRefImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getToolRef()
     * @generated
     */
    int TOOL_REF = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_REF__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Builtin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_REF__BUILTIN = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_REF__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Tool</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_REF__TOOL = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Tool Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_REF_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Tool Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_REF_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.BoolExpressionImpl <em>Bool Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.BoolExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getBoolExpression()
     * @generated
     */
    int BOOL_EXPRESSION = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Bool Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Bool Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.NumberExpressionImpl <em>Number Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.NumberExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getNumberExpression()
     * @generated
     */
    int NUMBER_EXPRESSION = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NUMBER_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NUMBER_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NUMBER_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Number Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NUMBER_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Number Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NUMBER_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.NullExpressionImpl <em>Null Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.NullExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getNullExpression()
     * @generated
     */
    int NULL_EXPRESSION = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Null Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Null Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NULL_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.DoubleExpressionImpl <em>Double Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.DoubleExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getDoubleExpression()
     * @generated
     */
    int DOUBLE_EXPRESSION = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Double Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Double Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.CastExpressionImpl <em>Cast Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.CastExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getCastExpression()
     * @generated
     */
    int CAST_EXPRESSION = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION__CHILD = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Cast Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Cast Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ListExpressionImpl <em>List Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ListExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getListExpression()
     * @generated
     */
    int LIST_EXPRESSION = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION__ELEMENTS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>List Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>List Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.SetExpressionImpl <em>Set Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.SetExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getSetExpression()
     * @generated
     */
    int SET_EXPRESSION = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION__ELEMENTS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Set Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Set Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.MapExpressionImpl <em>Map Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.MapExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getMapExpression()
     * @generated
     */
    int MAP_EXPRESSION = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Entries</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_EXPRESSION__ENTRIES = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Map Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Map Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.MapEntryImpl <em>Map Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.MapEntryImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getMapEntry()
     * @generated
     */
    int MAP_ENTRY = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_ENTRY__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Key</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_ENTRY__KEY = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_ENTRY__VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Map Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_ENTRY_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Map Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_ENTRY_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.EmptySetMapExpressionImpl <em>Empty Set Map Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.EmptySetMapExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getEmptySetMapExpression()
     * @generated
     */
    int EMPTY_SET_MAP_EXPRESSION = 12;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_SET_MAP_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_SET_MAP_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Empty Set Map Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_SET_MAP_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Empty Set Map Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_SET_MAP_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.UnresolvedRefExpressionImpl <em>Unresolved Ref Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.UnresolvedRefExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getUnresolvedRefExpression()
     * @generated
     */
    int UNRESOLVED_REF_EXPRESSION = 13;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REF_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REF_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REF_EXPRESSION__NAME = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Unresolved Ref Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REF_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Unresolved Ref Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REF_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolArgumentImpl <em>Tool Argument</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolArgumentImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getToolArgument()
     * @generated
     */
    int TOOL_ARGUMENT = 14;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_ARGUMENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_ARGUMENT__VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_ARGUMENT__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Tool Argument</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_ARGUMENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Tool Argument</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_ARGUMENT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.VariableExpressionImpl <em>Variable Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.VariableExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getVariableExpression()
     * @generated
     */
    int VARIABLE_EXPRESSION = 15;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_EXPRESSION__VARIABLE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.StringExpressionImpl <em>String Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.StringExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getStringExpression()
     * @generated
     */
    int STRING_EXPRESSION = 16;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>String Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>String Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ProjectionExpressionImpl <em>Projection Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ProjectionExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getProjectionExpression()
     * @generated
     */
    int PROJECTION_EXPRESSION = 17;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECTION_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECTION_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECTION_EXPRESSION__CHILD = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Index</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECTION_EXPRESSION__INDEX = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Projection Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECTION_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Projection Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECTION_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.SliceExpressionImpl <em>Slice Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.SliceExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getSliceExpression()
     * @generated
     */
    int SLICE_EXPRESSION = 18;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__CHILD = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Begin</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__BEGIN = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>End</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__END = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Slice Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Slice Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.TupleExpressionImpl <em>Tuple Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.TupleExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getTupleExpression()
     * @generated
     */
    int TUPLE_EXPRESSION = 19;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION__ELEMENTS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tuple Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Tuple Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolParamExpressionImpl <em>Tool Param Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolParamExpressionImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getToolParamExpression()
     * @generated
     */
    int TOOL_PARAM_EXPRESSION = 20;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAM_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAM_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Param</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAM_EXPRESSION__PARAM = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tool Param Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAM_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Tool Param Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_PARAM_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression
     * @generated
     */
    EClass getExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression#getType()
     * @see #getExpression()
     * @generated
     */
    EReference getExpression_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression <em>Tool Invoke Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Invoke Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression
     * @generated
     */
    EClass getToolInvokeExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression#getArguments <em>Arguments</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Arguments</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression#getArguments()
     * @see #getToolInvokeExpression()
     * @generated
     */
    EReference getToolInvokeExpression_Arguments();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression#getTool <em>Tool</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Tool</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression#getTool()
     * @see #getToolInvokeExpression()
     * @generated
     */
    EReference getToolInvokeExpression_Tool();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef <em>Tool Ref</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Ref</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef
     * @generated
     */
    EClass getToolRef();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#isBuiltin <em>Builtin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Builtin</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#isBuiltin()
     * @see #getToolRef()
     * @generated
     */
    EAttribute getToolRef_Builtin();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#getName()
     * @see #getToolRef()
     * @generated
     */
    EAttribute getToolRef_Name();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#getTool <em>Tool</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Tool</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef#getTool()
     * @see #getToolRef()
     * @generated
     */
    EReference getToolRef_Tool();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression <em>Bool Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bool Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression
     * @generated
     */
    EClass getBoolExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression#isValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression#isValue()
     * @see #getBoolExpression()
     * @generated
     */
    EAttribute getBoolExpression_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression <em>Number Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Number Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression
     * @generated
     */
    EClass getNumberExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression#getValue()
     * @see #getNumberExpression()
     * @generated
     */
    EAttribute getNumberExpression_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NullExpression <em>Null Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Null Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NullExpression
     * @generated
     */
    EClass getNullExpression();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression <em>Double Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Double Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression
     * @generated
     */
    EClass getDoubleExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression#getValue()
     * @see #getDoubleExpression()
     * @generated
     */
    EAttribute getDoubleExpression_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression <em>Cast Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Cast Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression
     * @generated
     */
    EClass getCastExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression#getChild <em>Child</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Child</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression#getChild()
     * @see #getCastExpression()
     * @generated
     */
    EReference getCastExpression_Child();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression <em>List Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>List Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression
     * @generated
     */
    EClass getListExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression#getElements()
     * @see #getListExpression()
     * @generated
     */
    EReference getListExpression_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression <em>Set Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Set Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression
     * @generated
     */
    EClass getSetExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression#getElements()
     * @see #getSetExpression()
     * @generated
     */
    EReference getSetExpression_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression <em>Map Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Map Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression
     * @generated
     */
    EClass getMapExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression#getEntries <em>Entries</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Entries</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression#getEntries()
     * @see #getMapExpression()
     * @generated
     */
    EReference getMapExpression_Entries();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry <em>Map Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Map Entry</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry
     * @generated
     */
    EClass getMapEntry();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Key</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry#getKey()
     * @see #getMapEntry()
     * @generated
     */
    EReference getMapEntry_Key();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry#getValue()
     * @see #getMapEntry()
     * @generated
     */
    EReference getMapEntry_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.EmptySetMapExpression <em>Empty Set Map Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Empty Set Map Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.EmptySetMapExpression
     * @generated
     */
    EClass getEmptySetMapExpression();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression <em>Unresolved Ref Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unresolved Ref Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression
     * @generated
     */
    EClass getUnresolvedRefExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression#getName()
     * @see #getUnresolvedRefExpression()
     * @generated
     */
    EAttribute getUnresolvedRefExpression_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument <em>Tool Argument</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Argument</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument
     * @generated
     */
    EClass getToolArgument();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument#getValue()
     * @see #getToolArgument()
     * @generated
     */
    EReference getToolArgument_Value();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument#getName()
     * @see #getToolArgument()
     * @generated
     */
    EAttribute getToolArgument_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression <em>Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Variable Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression
     * @generated
     */
    EClass getVariableExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Variable</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression#getVariable()
     * @see #getVariableExpression()
     * @generated
     */
    EReference getVariableExpression_Variable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression <em>String Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>String Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression
     * @generated
     */
    EClass getStringExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression#getValue()
     * @see #getStringExpression()
     * @generated
     */
    EAttribute getStringExpression_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression <em>Projection Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Projection Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression
     * @generated
     */
    EClass getProjectionExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression#getChild <em>Child</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Child</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression#getChild()
     * @see #getProjectionExpression()
     * @generated
     */
    EReference getProjectionExpression_Child();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression#getIndex <em>Index</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Index</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression#getIndex()
     * @see #getProjectionExpression()
     * @generated
     */
    EReference getProjectionExpression_Index();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression <em>Slice Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Slice Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression
     * @generated
     */
    EClass getSliceExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression#getChild <em>Child</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Child</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression#getChild()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_Child();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression#getBegin <em>Begin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Begin</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression#getBegin()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_Begin();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression#getEnd <em>End</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>End</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression#getEnd()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_End();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression <em>Tuple Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tuple Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression
     * @generated
     */
    EClass getTupleExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression#getElements()
     * @see #getTupleExpression()
     * @generated
     */
    EReference getTupleExpression_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression <em>Tool Param Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Param Expression</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression
     * @generated
     */
    EClass getToolParamExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression#getParam <em>Param</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Param</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression#getParam()
     * @see #getToolParamExpression()
     * @generated
     */
    EReference getToolParamExpression_Param();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ExpressionsFactory getExpressionsFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionImpl <em>Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getExpression()
         * @generated
         */
        EClass EXPRESSION = eINSTANCE.getExpression();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXPRESSION__TYPE = eINSTANCE.getExpression_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolInvokeExpressionImpl <em>Tool Invoke Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolInvokeExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getToolInvokeExpression()
         * @generated
         */
        EClass TOOL_INVOKE_EXPRESSION = eINSTANCE.getToolInvokeExpression();

        /**
         * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_INVOKE_EXPRESSION__ARGUMENTS = eINSTANCE.getToolInvokeExpression_Arguments();

        /**
         * The meta object literal for the '<em><b>Tool</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_INVOKE_EXPRESSION__TOOL = eINSTANCE.getToolInvokeExpression_Tool();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolRefImpl <em>Tool Ref</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolRefImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getToolRef()
         * @generated
         */
        EClass TOOL_REF = eINSTANCE.getToolRef();

        /**
         * The meta object literal for the '<em><b>Builtin</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_REF__BUILTIN = eINSTANCE.getToolRef_Builtin();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_REF__NAME = eINSTANCE.getToolRef_Name();

        /**
         * The meta object literal for the '<em><b>Tool</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_REF__TOOL = eINSTANCE.getToolRef_Tool();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.BoolExpressionImpl <em>Bool Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.BoolExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getBoolExpression()
         * @generated
         */
        EClass BOOL_EXPRESSION = eINSTANCE.getBoolExpression();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOOL_EXPRESSION__VALUE = eINSTANCE.getBoolExpression_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.NumberExpressionImpl <em>Number Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.NumberExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getNumberExpression()
         * @generated
         */
        EClass NUMBER_EXPRESSION = eINSTANCE.getNumberExpression();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute NUMBER_EXPRESSION__VALUE = eINSTANCE.getNumberExpression_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.NullExpressionImpl <em>Null Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.NullExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getNullExpression()
         * @generated
         */
        EClass NULL_EXPRESSION = eINSTANCE.getNullExpression();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.DoubleExpressionImpl <em>Double Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.DoubleExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getDoubleExpression()
         * @generated
         */
        EClass DOUBLE_EXPRESSION = eINSTANCE.getDoubleExpression();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOUBLE_EXPRESSION__VALUE = eINSTANCE.getDoubleExpression_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.CastExpressionImpl <em>Cast Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.CastExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getCastExpression()
         * @generated
         */
        EClass CAST_EXPRESSION = eINSTANCE.getCastExpression();

        /**
         * The meta object literal for the '<em><b>Child</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAST_EXPRESSION__CHILD = eINSTANCE.getCastExpression_Child();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ListExpressionImpl <em>List Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ListExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getListExpression()
         * @generated
         */
        EClass LIST_EXPRESSION = eINSTANCE.getListExpression();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LIST_EXPRESSION__ELEMENTS = eINSTANCE.getListExpression_Elements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.SetExpressionImpl <em>Set Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.SetExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getSetExpression()
         * @generated
         */
        EClass SET_EXPRESSION = eINSTANCE.getSetExpression();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SET_EXPRESSION__ELEMENTS = eINSTANCE.getSetExpression_Elements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.MapExpressionImpl <em>Map Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.MapExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getMapExpression()
         * @generated
         */
        EClass MAP_EXPRESSION = eINSTANCE.getMapExpression();

        /**
         * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MAP_EXPRESSION__ENTRIES = eINSTANCE.getMapExpression_Entries();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.MapEntryImpl <em>Map Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.MapEntryImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getMapEntry()
         * @generated
         */
        EClass MAP_ENTRY = eINSTANCE.getMapEntry();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MAP_ENTRY__KEY = eINSTANCE.getMapEntry_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MAP_ENTRY__VALUE = eINSTANCE.getMapEntry_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.EmptySetMapExpressionImpl <em>Empty Set Map Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.EmptySetMapExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getEmptySetMapExpression()
         * @generated
         */
        EClass EMPTY_SET_MAP_EXPRESSION = eINSTANCE.getEmptySetMapExpression();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.UnresolvedRefExpressionImpl <em>Unresolved Ref Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.UnresolvedRefExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getUnresolvedRefExpression()
         * @generated
         */
        EClass UNRESOLVED_REF_EXPRESSION = eINSTANCE.getUnresolvedRefExpression();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNRESOLVED_REF_EXPRESSION__NAME = eINSTANCE.getUnresolvedRefExpression_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolArgumentImpl <em>Tool Argument</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolArgumentImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getToolArgument()
         * @generated
         */
        EClass TOOL_ARGUMENT = eINSTANCE.getToolArgument();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_ARGUMENT__VALUE = eINSTANCE.getToolArgument_Value();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_ARGUMENT__NAME = eINSTANCE.getToolArgument_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.VariableExpressionImpl <em>Variable Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.VariableExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getVariableExpression()
         * @generated
         */
        EClass VARIABLE_EXPRESSION = eINSTANCE.getVariableExpression();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference VARIABLE_EXPRESSION__VARIABLE = eINSTANCE.getVariableExpression_Variable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.StringExpressionImpl <em>String Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.StringExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getStringExpression()
         * @generated
         */
        EClass STRING_EXPRESSION = eINSTANCE.getStringExpression();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STRING_EXPRESSION__VALUE = eINSTANCE.getStringExpression_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ProjectionExpressionImpl <em>Projection Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ProjectionExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getProjectionExpression()
         * @generated
         */
        EClass PROJECTION_EXPRESSION = eINSTANCE.getProjectionExpression();

        /**
         * The meta object literal for the '<em><b>Child</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECTION_EXPRESSION__CHILD = eINSTANCE.getProjectionExpression_Child();

        /**
         * The meta object literal for the '<em><b>Index</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECTION_EXPRESSION__INDEX = eINSTANCE.getProjectionExpression_Index();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.SliceExpressionImpl <em>Slice Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.SliceExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getSliceExpression()
         * @generated
         */
        EClass SLICE_EXPRESSION = eINSTANCE.getSliceExpression();

        /**
         * The meta object literal for the '<em><b>Child</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SLICE_EXPRESSION__CHILD = eINSTANCE.getSliceExpression_Child();

        /**
         * The meta object literal for the '<em><b>Begin</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SLICE_EXPRESSION__BEGIN = eINSTANCE.getSliceExpression_Begin();

        /**
         * The meta object literal for the '<em><b>End</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SLICE_EXPRESSION__END = eINSTANCE.getSliceExpression_End();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.TupleExpressionImpl <em>Tuple Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.TupleExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getTupleExpression()
         * @generated
         */
        EClass TUPLE_EXPRESSION = eINSTANCE.getTupleExpression();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TUPLE_EXPRESSION__ELEMENTS = eINSTANCE.getTupleExpression_Elements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolParamExpressionImpl <em>Tool Param Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolParamExpressionImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl#getToolParamExpression()
         * @generated
         */
        EClass TOOL_PARAM_EXPRESSION = eINSTANCE.getToolParamExpression();

        /**
         * The meta object literal for the '<em><b>Param</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TOOL_PARAM_EXPRESSION__PARAM = eINSTANCE.getToolParamExpression_Param();

    }

} //ExpressionsPackage
