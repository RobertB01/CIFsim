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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsFactory
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
    String eNS_URI = "http://eclipse.org/escet/cif/expressions";

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
    ExpressionsPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionImpl <em>Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getExpression()
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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BinaryExpressionImpl <em>Binary Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.BinaryExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getBinaryExpression()
     * @generated
     */
    int BINARY_EXPRESSION = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Operator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__OPERATOR = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Left</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__LEFT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Right</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__RIGHT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Binary Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Binary Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.UnaryExpressionImpl <em>Unary Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.UnaryExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getUnaryExpression()
     * @generated
     */
    int UNARY_EXPRESSION = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Operator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION__OPERATOR = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION__CHILD = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Unary Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Unary Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BoolExpressionImpl <em>Bool Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.BoolExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getBoolExpression()
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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.IntExpressionImpl <em>Int Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.IntExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getIntExpression()
     * @generated
     */
    int INT_EXPRESSION = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Int Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Int Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionCallExpressionImpl <em>Function Call Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionCallExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getFunctionCallExpression()
     * @generated
     */
    int FUNCTION_CALL_EXPRESSION = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_CALL_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_CALL_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_CALL_EXPRESSION__ARGUMENTS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_CALL_EXPRESSION__FUNCTION = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Function Call Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_CALL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Function Call Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_CALL_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.IfExpressionImpl <em>If Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.IfExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getIfExpression()
     * @generated
     */
    int IF_EXPRESSION = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Guards</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_EXPRESSION__GUARDS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Then</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_EXPRESSION__THEN = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Else</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_EXPRESSION__ELSE = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Elifs</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_EXPRESSION__ELIFS = EXPRESSION_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>If Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>If Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.DiscVariableExpressionImpl <em>Disc Variable Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.DiscVariableExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getDiscVariableExpression()
     * @generated
     */
    int DISC_VARIABLE_EXPRESSION = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE_EXPRESSION__VARIABLE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Disc Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Disc Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.AlgVariableExpressionImpl <em>Alg Variable Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.AlgVariableExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getAlgVariableExpression()
     * @generated
     */
    int ALG_VARIABLE_EXPRESSION = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE_EXPRESSION__VARIABLE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Alg Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Alg Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.EventExpressionImpl <em>Event Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.EventExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getEventExpression()
     * @generated
     */
    int EVENT_EXPRESSION = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Event</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_EXPRESSION__EVENT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Event Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Event Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.EnumLiteralExpressionImpl <em>Enum Literal Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.EnumLiteralExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getEnumLiteralExpression()
     * @generated
     */
    int ENUM_LITERAL_EXPRESSION = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Literal</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL_EXPRESSION__LITERAL = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Enum Literal Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Enum Literal Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.LocationExpressionImpl <em>Location Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.LocationExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getLocationExpression()
     * @generated
     */
    int LOCATION_EXPRESSION = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Location</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_EXPRESSION__LOCATION = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Location Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Location Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ElifExpressionImpl <em>Elif Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ElifExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getElifExpression()
     * @generated
     */
    int ELIF_EXPRESSION = 12;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_EXPRESSION__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Guards</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_EXPRESSION__GUARDS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Then</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_EXPRESSION__THEN = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Elif Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_EXPRESSION_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Elif Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_EXPRESSION_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompParamWrapExpressionImpl <em>Comp Param Wrap Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompParamWrapExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getCompParamWrapExpression()
     * @generated
     */
    int COMP_PARAM_WRAP_EXPRESSION = 13;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_EXPRESSION__REFERENCE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_EXPRESSION__PARAMETER = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Comp Param Wrap Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Comp Param Wrap Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompInstWrapExpressionImpl <em>Comp Inst Wrap Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompInstWrapExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getCompInstWrapExpression()
     * @generated
     */
    int COMP_INST_WRAP_EXPRESSION = 14;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_EXPRESSION__REFERENCE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Instantiation</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_EXPRESSION__INSTANTIATION = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Comp Inst Wrap Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Comp Inst Wrap Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ComponentExpressionImpl <em>Component Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ComponentExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getComponentExpression()
     * @generated
     */
    int COMPONENT_EXPRESSION = 15;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Component</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_EXPRESSION__COMPONENT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Component Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Component Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompParamExpressionImpl <em>Comp Param Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompParamExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getCompParamExpression()
     * @generated
     */
    int COMP_PARAM_EXPRESSION = 16;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_EXPRESSION__PARAMETER = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Comp Param Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Comp Param Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ConstantExpressionImpl <em>Constant Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ConstantExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getConstantExpression()
     * @generated
     */
    int CONSTANT_EXPRESSION = 17;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Constant</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_EXPRESSION__CONSTANT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Constant Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Constant Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.TauExpressionImpl <em>Tau Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.TauExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getTauExpression()
     * @generated
     */
    int TAU_EXPRESSION = 18;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TAU_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TAU_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Tau Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TAU_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Tau Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TAU_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ProjectionExpressionImpl <em>Projection Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ProjectionExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getProjectionExpression()
     * @generated
     */
    int PROJECTION_EXPRESSION = 19;

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
     * The feature id for the '<em><b>Index</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECTION_EXPRESSION__INDEX = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROJECTION_EXPRESSION__CHILD = EXPRESSION_FEATURE_COUNT + 1;

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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SliceExpressionImpl <em>Slice Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SliceExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSliceExpression()
     * @generated
     */
    int SLICE_EXPRESSION = 20;

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
     * The feature id for the '<em><b>Begin</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__BEGIN = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>End</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__END = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__CHILD = EXPRESSION_FEATURE_COUNT + 2;

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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BaseFunctionExpressionImpl <em>Base Function Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.BaseFunctionExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getBaseFunctionExpression()
     * @generated
     */
    int BASE_FUNCTION_EXPRESSION = 21;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASE_FUNCTION_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASE_FUNCTION_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Base Function Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASE_FUNCTION_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Base Function Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASE_FUNCTION_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.StdLibFunctionExpressionImpl <em>Std Lib Function Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.StdLibFunctionExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getStdLibFunctionExpression()
     * @generated
     */
    int STD_LIB_FUNCTION_EXPRESSION = 22;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_EXPRESSION__POSITION = BASE_FUNCTION_EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_EXPRESSION__TYPE = BASE_FUNCTION_EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Function</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_EXPRESSION__FUNCTION = BASE_FUNCTION_EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Std Lib Function Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_EXPRESSION_FEATURE_COUNT = BASE_FUNCTION_EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Std Lib Function Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_EXPRESSION_OPERATION_COUNT = BASE_FUNCTION_EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.RealExpressionImpl <em>Real Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.RealExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getRealExpression()
     * @generated
     */
    int REAL_EXPRESSION = 23;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Real Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Real Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.TimeExpressionImpl <em>Time Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.TimeExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getTimeExpression()
     * @generated
     */
    int TIME_EXPRESSION = 24;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Time Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Time Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ListExpressionImpl <em>List Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ListExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getListExpression()
     * @generated
     */
    int LIST_EXPRESSION = 25;

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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SetExpressionImpl <em>Set Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SetExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSetExpression()
     * @generated
     */
    int SET_EXPRESSION = 26;

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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.DictExpressionImpl <em>Dict Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.DictExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getDictExpression()
     * @generated
     */
    int DICT_EXPRESSION = 27;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Pairs</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_EXPRESSION__PAIRS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Dict Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Dict Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.DictPairImpl <em>Dict Pair</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.DictPairImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getDictPair()
     * @generated
     */
    int DICT_PAIR = 28;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_PAIR__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Key</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_PAIR__KEY = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_PAIR__VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Dict Pair</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_PAIR_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Dict Pair</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_PAIR_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.TupleExpressionImpl <em>Tuple Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.TupleExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getTupleExpression()
     * @generated
     */
    int TUPLE_EXPRESSION = 29;

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
     * The feature id for the '<em><b>Fields</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION__FIELDS = EXPRESSION_FEATURE_COUNT + 0;

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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CastExpressionImpl <em>Cast Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.CastExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getCastExpression()
     * @generated
     */
    int CAST_EXPRESSION = 30;

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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.StringExpressionImpl <em>String Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.StringExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getStringExpression()
     * @generated
     */
    int STRING_EXPRESSION = 31;

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
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.FieldExpressionImpl <em>Field Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.FieldExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getFieldExpression()
     * @generated
     */
    int FIELD_EXPRESSION = 32;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Field</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_EXPRESSION__FIELD = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Field Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Field Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionExpressionImpl <em>Function Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getFunctionExpression()
     * @generated
     */
    int FUNCTION_EXPRESSION = 33;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_EXPRESSION__POSITION = BASE_FUNCTION_EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_EXPRESSION__TYPE = BASE_FUNCTION_EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Function</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_EXPRESSION__FUNCTION = BASE_FUNCTION_EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Function Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_EXPRESSION_FEATURE_COUNT = BASE_FUNCTION_EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Function Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_EXPRESSION_OPERATION_COUNT = BASE_FUNCTION_EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ContVariableExpressionImpl <em>Cont Variable Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ContVariableExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getContVariableExpression()
     * @generated
     */
    int CONT_VARIABLE_EXPRESSION = 34;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE_EXPRESSION__VARIABLE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Derivative</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE_EXPRESSION__DERIVATIVE = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Cont Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Cont Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.InputVariableExpressionImpl <em>Input Variable Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.InputVariableExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getInputVariableExpression()
     * @generated
     */
    int INPUT_VARIABLE_EXPRESSION = 35;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE_EXPRESSION__VARIABLE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Input Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Input Variable Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ReceivedExpressionImpl <em>Received Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ReceivedExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getReceivedExpression()
     * @generated
     */
    int RECEIVED_EXPRESSION = 36;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECEIVED_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECEIVED_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Received Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECEIVED_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Received Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECEIVED_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SelfExpressionImpl <em>Self Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SelfExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSelfExpression()
     * @generated
     */
    int SELF_EXPRESSION = 37;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELF_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELF_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Self Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELF_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Self Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELF_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SwitchExpressionImpl <em>Switch Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SwitchExpressionImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSwitchExpression()
     * @generated
     */
    int SWITCH_EXPRESSION = 38;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Cases</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_EXPRESSION__CASES = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Switch Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Switch Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_EXPRESSION_OPERATION_COUNT = EXPRESSION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SwitchCaseImpl <em>Switch Case</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SwitchCaseImpl
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSwitchCase()
     * @generated
     */
    int SWITCH_CASE = 39;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_CASE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Key</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_CASE__KEY = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_CASE__VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Switch Case</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_CASE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Switch Case</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SWITCH_CASE_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator <em>Unary Operator</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getUnaryOperator()
     * @generated
     */
    int UNARY_OPERATOR = 40;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator <em>Binary Operator</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getBinaryOperator()
     * @generated
     */
    int BINARY_OPERATOR = 41;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction <em>Std Lib Function</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getStdLibFunction()
     * @generated
     */
    int STD_LIB_FUNCTION = 42;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.Expression
     * @generated
     */
    EClass getExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.Expression#getType()
     * @see #getExpression()
     * @generated
     */
    EReference getExpression_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression <em>Binary Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Binary Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression
     * @generated
     */
    EClass getBinaryExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression#getOperator <em>Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Operator</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression#getOperator()
     * @see #getBinaryExpression()
     * @generated
     */
    EAttribute getBinaryExpression_Operator();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression#getLeft <em>Left</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Left</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression#getLeft()
     * @see #getBinaryExpression()
     * @generated
     */
    EReference getBinaryExpression_Left();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression#getRight <em>Right</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Right</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression#getRight()
     * @see #getBinaryExpression()
     * @generated
     */
    EReference getBinaryExpression_Right();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression <em>Unary Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unary Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression
     * @generated
     */
    EClass getUnaryExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression#getOperator <em>Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Operator</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression#getOperator()
     * @see #getUnaryExpression()
     * @generated
     */
    EAttribute getUnaryExpression_Operator();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression#getChild <em>Child</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Child</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression#getChild()
     * @see #getUnaryExpression()
     * @generated
     */
    EReference getUnaryExpression_Child();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression <em>Bool Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bool Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression
     * @generated
     */
    EClass getBoolExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression#isValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression#isValue()
     * @see #getBoolExpression()
     * @generated
     */
    EAttribute getBoolExpression_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression <em>Int Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Int Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression
     * @generated
     */
    EClass getIntExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression#getValue()
     * @see #getIntExpression()
     * @generated
     */
    EAttribute getIntExpression_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression <em>Function Call Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function Call Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression
     * @generated
     */
    EClass getFunctionCallExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression#getArguments <em>Arguments</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Arguments</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression#getArguments()
     * @see #getFunctionCallExpression()
     * @generated
     */
    EReference getFunctionCallExpression_Arguments();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Function</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression#getFunction()
     * @see #getFunctionCallExpression()
     * @generated
     */
    EReference getFunctionCallExpression_Function();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression <em>If Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>If Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression
     * @generated
     */
    EClass getIfExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getGuards <em>Guards</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Guards</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getGuards()
     * @see #getIfExpression()
     * @generated
     */
    EReference getIfExpression_Guards();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getThen <em>Then</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Then</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getThen()
     * @see #getIfExpression()
     * @generated
     */
    EReference getIfExpression_Then();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getElse <em>Else</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Else</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getElse()
     * @see #getIfExpression()
     * @generated
     */
    EReference getIfExpression_Else();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getElifs <em>Elifs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elifs</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression#getElifs()
     * @see #getIfExpression()
     * @generated
     */
    EReference getIfExpression_Elifs();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression <em>Disc Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Disc Variable Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression
     * @generated
     */
    EClass getDiscVariableExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression#getVariable()
     * @see #getDiscVariableExpression()
     * @generated
     */
    EReference getDiscVariableExpression_Variable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression <em>Alg Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Alg Variable Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression
     * @generated
     */
    EClass getAlgVariableExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression#getVariable()
     * @see #getAlgVariableExpression()
     * @generated
     */
    EReference getAlgVariableExpression_Variable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression <em>Event Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Event Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression
     * @generated
     */
    EClass getEventExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression#getEvent <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression#getEvent()
     * @see #getEventExpression()
     * @generated
     */
    EReference getEventExpression_Event();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression <em>Enum Literal Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Enum Literal Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression
     * @generated
     */
    EClass getEnumLiteralExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression#getLiteral <em>Literal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Literal</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression#getLiteral()
     * @see #getEnumLiteralExpression()
     * @generated
     */
    EReference getEnumLiteralExpression_Literal();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression <em>Location Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Location Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression
     * @generated
     */
    EClass getLocationExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression#getLocation <em>Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Location</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression#getLocation()
     * @see #getLocationExpression()
     * @generated
     */
    EReference getLocationExpression_Location();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression <em>Elif Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Elif Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression
     * @generated
     */
    EClass getElifExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression#getGuards <em>Guards</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Guards</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression#getGuards()
     * @see #getElifExpression()
     * @generated
     */
    EReference getElifExpression_Guards();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression#getThen <em>Then</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Then</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression#getThen()
     * @see #getElifExpression()
     * @generated
     */
    EReference getElifExpression_Then();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression <em>Comp Param Wrap Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Comp Param Wrap Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression
     * @generated
     */
    EClass getCompParamWrapExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression#getReference()
     * @see #getCompParamWrapExpression()
     * @generated
     */
    EReference getCompParamWrapExpression_Reference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression#getParameter()
     * @see #getCompParamWrapExpression()
     * @generated
     */
    EReference getCompParamWrapExpression_Parameter();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression <em>Comp Inst Wrap Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Comp Inst Wrap Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression
     * @generated
     */
    EClass getCompInstWrapExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression#getReference()
     * @see #getCompInstWrapExpression()
     * @generated
     */
    EReference getCompInstWrapExpression_Reference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression#getInstantiation <em>Instantiation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Instantiation</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression#getInstantiation()
     * @see #getCompInstWrapExpression()
     * @generated
     */
    EReference getCompInstWrapExpression_Instantiation();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression <em>Component Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression
     * @generated
     */
    EClass getComponentExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression#getComponent <em>Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Component</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression#getComponent()
     * @see #getComponentExpression()
     * @generated
     */
    EReference getComponentExpression_Component();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression <em>Comp Param Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Comp Param Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression
     * @generated
     */
    EClass getCompParamExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression#getParameter()
     * @see #getCompParamExpression()
     * @generated
     */
    EReference getCompParamExpression_Parameter();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression <em>Constant Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Constant Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression
     * @generated
     */
    EClass getConstantExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression#getConstant <em>Constant</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Constant</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression#getConstant()
     * @see #getConstantExpression()
     * @generated
     */
    EReference getConstantExpression_Constant();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression <em>Tau Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tau Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression
     * @generated
     */
    EClass getTauExpression();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression <em>Projection Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Projection Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression
     * @generated
     */
    EClass getProjectionExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression#getIndex <em>Index</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Index</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression#getIndex()
     * @see #getProjectionExpression()
     * @generated
     */
    EReference getProjectionExpression_Index();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression#getChild <em>Child</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Child</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression#getChild()
     * @see #getProjectionExpression()
     * @generated
     */
    EReference getProjectionExpression_Child();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression <em>Slice Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Slice Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression
     * @generated
     */
    EClass getSliceExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getBegin <em>Begin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Begin</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getBegin()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_Begin();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getEnd <em>End</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>End</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getEnd()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_End();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getChild <em>Child</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Child</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression#getChild()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_Child();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression <em>Base Function Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Base Function Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression
     * @generated
     */
    EClass getBaseFunctionExpression();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression <em>Std Lib Function Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Std Lib Function Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression
     * @generated
     */
    EClass getStdLibFunctionExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Function</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression#getFunction()
     * @see #getStdLibFunctionExpression()
     * @generated
     */
    EAttribute getStdLibFunctionExpression_Function();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression <em>Real Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Real Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression
     * @generated
     */
    EClass getRealExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression#getValue()
     * @see #getRealExpression()
     * @generated
     */
    EAttribute getRealExpression_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression <em>Time Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Time Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression
     * @generated
     */
    EClass getTimeExpression();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression <em>List Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>List Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression
     * @generated
     */
    EClass getListExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression#getElements()
     * @see #getListExpression()
     * @generated
     */
    EReference getListExpression_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression <em>Set Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Set Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression
     * @generated
     */
    EClass getSetExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression#getElements()
     * @see #getSetExpression()
     * @generated
     */
    EReference getSetExpression_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression <em>Dict Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dict Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression
     * @generated
     */
    EClass getDictExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression#getPairs <em>Pairs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Pairs</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression#getPairs()
     * @see #getDictExpression()
     * @generated
     */
    EReference getDictExpression_Pairs();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictPair <em>Dict Pair</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dict Pair</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DictPair
     * @generated
     */
    EClass getDictPair();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictPair#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Key</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DictPair#getKey()
     * @see #getDictPair()
     * @generated
     */
    EReference getDictPair_Key();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictPair#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DictPair#getValue()
     * @see #getDictPair()
     * @generated
     */
    EReference getDictPair_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression <em>Tuple Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tuple Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression
     * @generated
     */
    EClass getTupleExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression#getFields <em>Fields</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Fields</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression#getFields()
     * @see #getTupleExpression()
     * @generated
     */
    EReference getTupleExpression_Fields();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression <em>Cast Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Cast Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression
     * @generated
     */
    EClass getCastExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression#getChild <em>Child</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Child</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression#getChild()
     * @see #getCastExpression()
     * @generated
     */
    EReference getCastExpression_Child();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression <em>String Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>String Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression
     * @generated
     */
    EClass getStringExpression();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression#getValue()
     * @see #getStringExpression()
     * @generated
     */
    EAttribute getStringExpression_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression <em>Field Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Field Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression
     * @generated
     */
    EClass getFieldExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression#getField <em>Field</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Field</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression#getField()
     * @see #getFieldExpression()
     * @generated
     */
    EReference getFieldExpression_Field();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression <em>Function Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression
     * @generated
     */
    EClass getFunctionExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Function</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression#getFunction()
     * @see #getFunctionExpression()
     * @generated
     */
    EReference getFunctionExpression_Function();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression <em>Cont Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Cont Variable Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression
     * @generated
     */
    EClass getContVariableExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression#getVariable()
     * @see #getContVariableExpression()
     * @generated
     */
    EReference getContVariableExpression_Variable();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression#isDerivative <em>Derivative</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Derivative</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression#isDerivative()
     * @see #getContVariableExpression()
     * @generated
     */
    EAttribute getContVariableExpression_Derivative();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression <em>Input Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Input Variable Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression
     * @generated
     */
    EClass getInputVariableExpression();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression#getVariable()
     * @see #getInputVariableExpression()
     * @generated
     */
    EReference getInputVariableExpression_Variable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression <em>Received Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Received Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression
     * @generated
     */
    EClass getReceivedExpression();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression <em>Self Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Self Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression
     * @generated
     */
    EClass getSelfExpression();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression <em>Switch Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Switch Expression</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression
     * @generated
     */
    EClass getSwitchExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression#getValue()
     * @see #getSwitchExpression()
     * @generated
     */
    EReference getSwitchExpression_Value();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression#getCases <em>Cases</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Cases</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression#getCases()
     * @see #getSwitchExpression()
     * @generated
     */
    EReference getSwitchExpression_Cases();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase <em>Switch Case</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Switch Case</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase
     * @generated
     */
    EClass getSwitchCase();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Key</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase#getKey()
     * @see #getSwitchCase()
     * @generated
     */
    EReference getSwitchCase_Key();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase#getValue()
     * @see #getSwitchCase()
     * @generated
     */
    EReference getSwitchCase_Value();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator <em>Unary Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Unary Operator</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator
     * @generated
     */
    EEnum getUnaryOperator();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator <em>Binary Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Binary Operator</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator
     * @generated
     */
    EEnum getBinaryOperator();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction <em>Std Lib Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Std Lib Function</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction
     * @generated
     */
    EEnum getStdLibFunction();

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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionImpl <em>Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getExpression()
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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BinaryExpressionImpl <em>Binary Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.BinaryExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getBinaryExpression()
         * @generated
         */
        EClass BINARY_EXPRESSION = eINSTANCE.getBinaryExpression();

        /**
         * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_EXPRESSION__OPERATOR = eINSTANCE.getBinaryExpression_Operator();

        /**
         * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_EXPRESSION__LEFT = eINSTANCE.getBinaryExpression_Left();

        /**
         * The meta object literal for the '<em><b>Right</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_EXPRESSION__RIGHT = eINSTANCE.getBinaryExpression_Right();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.UnaryExpressionImpl <em>Unary Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.UnaryExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getUnaryExpression()
         * @generated
         */
        EClass UNARY_EXPRESSION = eINSTANCE.getUnaryExpression();

        /**
         * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNARY_EXPRESSION__OPERATOR = eINSTANCE.getUnaryExpression_Operator();

        /**
         * The meta object literal for the '<em><b>Child</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_EXPRESSION__CHILD = eINSTANCE.getUnaryExpression_Child();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BoolExpressionImpl <em>Bool Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.BoolExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getBoolExpression()
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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.IntExpressionImpl <em>Int Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.IntExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getIntExpression()
         * @generated
         */
        EClass INT_EXPRESSION = eINSTANCE.getIntExpression();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INT_EXPRESSION__VALUE = eINSTANCE.getIntExpression_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionCallExpressionImpl <em>Function Call Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionCallExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getFunctionCallExpression()
         * @generated
         */
        EClass FUNCTION_CALL_EXPRESSION = eINSTANCE.getFunctionCallExpression();

        /**
         * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_CALL_EXPRESSION__ARGUMENTS = eINSTANCE.getFunctionCallExpression_Arguments();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_CALL_EXPRESSION__FUNCTION = eINSTANCE.getFunctionCallExpression_Function();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.IfExpressionImpl <em>If Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.IfExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getIfExpression()
         * @generated
         */
        EClass IF_EXPRESSION = eINSTANCE.getIfExpression();

        /**
         * The meta object literal for the '<em><b>Guards</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_EXPRESSION__GUARDS = eINSTANCE.getIfExpression_Guards();

        /**
         * The meta object literal for the '<em><b>Then</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_EXPRESSION__THEN = eINSTANCE.getIfExpression_Then();

        /**
         * The meta object literal for the '<em><b>Else</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_EXPRESSION__ELSE = eINSTANCE.getIfExpression_Else();

        /**
         * The meta object literal for the '<em><b>Elifs</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_EXPRESSION__ELIFS = eINSTANCE.getIfExpression_Elifs();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.DiscVariableExpressionImpl <em>Disc Variable Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.DiscVariableExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getDiscVariableExpression()
         * @generated
         */
        EClass DISC_VARIABLE_EXPRESSION = eINSTANCE.getDiscVariableExpression();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISC_VARIABLE_EXPRESSION__VARIABLE = eINSTANCE.getDiscVariableExpression_Variable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.AlgVariableExpressionImpl <em>Alg Variable Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.AlgVariableExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getAlgVariableExpression()
         * @generated
         */
        EClass ALG_VARIABLE_EXPRESSION = eINSTANCE.getAlgVariableExpression();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALG_VARIABLE_EXPRESSION__VARIABLE = eINSTANCE.getAlgVariableExpression_Variable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.EventExpressionImpl <em>Event Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.EventExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getEventExpression()
         * @generated
         */
        EClass EVENT_EXPRESSION = eINSTANCE.getEventExpression();

        /**
         * The meta object literal for the '<em><b>Event</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EVENT_EXPRESSION__EVENT = eINSTANCE.getEventExpression_Event();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.EnumLiteralExpressionImpl <em>Enum Literal Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.EnumLiteralExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getEnumLiteralExpression()
         * @generated
         */
        EClass ENUM_LITERAL_EXPRESSION = eINSTANCE.getEnumLiteralExpression();

        /**
         * The meta object literal for the '<em><b>Literal</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ENUM_LITERAL_EXPRESSION__LITERAL = eINSTANCE.getEnumLiteralExpression_Literal();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.LocationExpressionImpl <em>Location Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.LocationExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getLocationExpression()
         * @generated
         */
        EClass LOCATION_EXPRESSION = eINSTANCE.getLocationExpression();

        /**
         * The meta object literal for the '<em><b>Location</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LOCATION_EXPRESSION__LOCATION = eINSTANCE.getLocationExpression_Location();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ElifExpressionImpl <em>Elif Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ElifExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getElifExpression()
         * @generated
         */
        EClass ELIF_EXPRESSION = eINSTANCE.getElifExpression();

        /**
         * The meta object literal for the '<em><b>Guards</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ELIF_EXPRESSION__GUARDS = eINSTANCE.getElifExpression_Guards();

        /**
         * The meta object literal for the '<em><b>Then</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ELIF_EXPRESSION__THEN = eINSTANCE.getElifExpression_Then();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompParamWrapExpressionImpl <em>Comp Param Wrap Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompParamWrapExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getCompParamWrapExpression()
         * @generated
         */
        EClass COMP_PARAM_WRAP_EXPRESSION = eINSTANCE.getCompParamWrapExpression();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_PARAM_WRAP_EXPRESSION__REFERENCE = eINSTANCE.getCompParamWrapExpression_Reference();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_PARAM_WRAP_EXPRESSION__PARAMETER = eINSTANCE.getCompParamWrapExpression_Parameter();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompInstWrapExpressionImpl <em>Comp Inst Wrap Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompInstWrapExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getCompInstWrapExpression()
         * @generated
         */
        EClass COMP_INST_WRAP_EXPRESSION = eINSTANCE.getCompInstWrapExpression();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_INST_WRAP_EXPRESSION__REFERENCE = eINSTANCE.getCompInstWrapExpression_Reference();

        /**
         * The meta object literal for the '<em><b>Instantiation</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_INST_WRAP_EXPRESSION__INSTANTIATION = eINSTANCE.getCompInstWrapExpression_Instantiation();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ComponentExpressionImpl <em>Component Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ComponentExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getComponentExpression()
         * @generated
         */
        EClass COMPONENT_EXPRESSION = eINSTANCE.getComponentExpression();

        /**
         * The meta object literal for the '<em><b>Component</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_EXPRESSION__COMPONENT = eINSTANCE.getComponentExpression_Component();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompParamExpressionImpl <em>Comp Param Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompParamExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getCompParamExpression()
         * @generated
         */
        EClass COMP_PARAM_EXPRESSION = eINSTANCE.getCompParamExpression();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_PARAM_EXPRESSION__PARAMETER = eINSTANCE.getCompParamExpression_Parameter();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ConstantExpressionImpl <em>Constant Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ConstantExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getConstantExpression()
         * @generated
         */
        EClass CONSTANT_EXPRESSION = eINSTANCE.getConstantExpression();

        /**
         * The meta object literal for the '<em><b>Constant</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONSTANT_EXPRESSION__CONSTANT = eINSTANCE.getConstantExpression_Constant();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.TauExpressionImpl <em>Tau Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.TauExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getTauExpression()
         * @generated
         */
        EClass TAU_EXPRESSION = eINSTANCE.getTauExpression();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ProjectionExpressionImpl <em>Projection Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ProjectionExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getProjectionExpression()
         * @generated
         */
        EClass PROJECTION_EXPRESSION = eINSTANCE.getProjectionExpression();

        /**
         * The meta object literal for the '<em><b>Index</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECTION_EXPRESSION__INDEX = eINSTANCE.getProjectionExpression_Index();

        /**
         * The meta object literal for the '<em><b>Child</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROJECTION_EXPRESSION__CHILD = eINSTANCE.getProjectionExpression_Child();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SliceExpressionImpl <em>Slice Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SliceExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSliceExpression()
         * @generated
         */
        EClass SLICE_EXPRESSION = eINSTANCE.getSliceExpression();

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
         * The meta object literal for the '<em><b>Child</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SLICE_EXPRESSION__CHILD = eINSTANCE.getSliceExpression_Child();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BaseFunctionExpressionImpl <em>Base Function Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.BaseFunctionExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getBaseFunctionExpression()
         * @generated
         */
        EClass BASE_FUNCTION_EXPRESSION = eINSTANCE.getBaseFunctionExpression();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.StdLibFunctionExpressionImpl <em>Std Lib Function Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.StdLibFunctionExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getStdLibFunctionExpression()
         * @generated
         */
        EClass STD_LIB_FUNCTION_EXPRESSION = eINSTANCE.getStdLibFunctionExpression();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STD_LIB_FUNCTION_EXPRESSION__FUNCTION = eINSTANCE.getStdLibFunctionExpression_Function();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.RealExpressionImpl <em>Real Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.RealExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getRealExpression()
         * @generated
         */
        EClass REAL_EXPRESSION = eINSTANCE.getRealExpression();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REAL_EXPRESSION__VALUE = eINSTANCE.getRealExpression_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.TimeExpressionImpl <em>Time Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.TimeExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getTimeExpression()
         * @generated
         */
        EClass TIME_EXPRESSION = eINSTANCE.getTimeExpression();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ListExpressionImpl <em>List Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ListExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getListExpression()
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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SetExpressionImpl <em>Set Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SetExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSetExpression()
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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.DictExpressionImpl <em>Dict Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.DictExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getDictExpression()
         * @generated
         */
        EClass DICT_EXPRESSION = eINSTANCE.getDictExpression();

        /**
         * The meta object literal for the '<em><b>Pairs</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DICT_EXPRESSION__PAIRS = eINSTANCE.getDictExpression_Pairs();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.DictPairImpl <em>Dict Pair</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.DictPairImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getDictPair()
         * @generated
         */
        EClass DICT_PAIR = eINSTANCE.getDictPair();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DICT_PAIR__KEY = eINSTANCE.getDictPair_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DICT_PAIR__VALUE = eINSTANCE.getDictPair_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.TupleExpressionImpl <em>Tuple Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.TupleExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getTupleExpression()
         * @generated
         */
        EClass TUPLE_EXPRESSION = eINSTANCE.getTupleExpression();

        /**
         * The meta object literal for the '<em><b>Fields</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TUPLE_EXPRESSION__FIELDS = eINSTANCE.getTupleExpression_Fields();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CastExpressionImpl <em>Cast Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.CastExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getCastExpression()
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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.StringExpressionImpl <em>String Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.StringExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getStringExpression()
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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.FieldExpressionImpl <em>Field Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.FieldExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getFieldExpression()
         * @generated
         */
        EClass FIELD_EXPRESSION = eINSTANCE.getFieldExpression();

        /**
         * The meta object literal for the '<em><b>Field</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FIELD_EXPRESSION__FIELD = eINSTANCE.getFieldExpression_Field();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionExpressionImpl <em>Function Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getFunctionExpression()
         * @generated
         */
        EClass FUNCTION_EXPRESSION = eINSTANCE.getFunctionExpression();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_EXPRESSION__FUNCTION = eINSTANCE.getFunctionExpression_Function();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ContVariableExpressionImpl <em>Cont Variable Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ContVariableExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getContVariableExpression()
         * @generated
         */
        EClass CONT_VARIABLE_EXPRESSION = eINSTANCE.getContVariableExpression();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONT_VARIABLE_EXPRESSION__VARIABLE = eINSTANCE.getContVariableExpression_Variable();

        /**
         * The meta object literal for the '<em><b>Derivative</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONT_VARIABLE_EXPRESSION__DERIVATIVE = eINSTANCE.getContVariableExpression_Derivative();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.InputVariableExpressionImpl <em>Input Variable Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.InputVariableExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getInputVariableExpression()
         * @generated
         */
        EClass INPUT_VARIABLE_EXPRESSION = eINSTANCE.getInputVariableExpression();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_VARIABLE_EXPRESSION__VARIABLE = eINSTANCE.getInputVariableExpression_Variable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ReceivedExpressionImpl <em>Received Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ReceivedExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getReceivedExpression()
         * @generated
         */
        EClass RECEIVED_EXPRESSION = eINSTANCE.getReceivedExpression();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SelfExpressionImpl <em>Self Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SelfExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSelfExpression()
         * @generated
         */
        EClass SELF_EXPRESSION = eINSTANCE.getSelfExpression();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SwitchExpressionImpl <em>Switch Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SwitchExpressionImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSwitchExpression()
         * @generated
         */
        EClass SWITCH_EXPRESSION = eINSTANCE.getSwitchExpression();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SWITCH_EXPRESSION__VALUE = eINSTANCE.getSwitchExpression_Value();

        /**
         * The meta object literal for the '<em><b>Cases</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SWITCH_EXPRESSION__CASES = eINSTANCE.getSwitchExpression_Cases();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.SwitchCaseImpl <em>Switch Case</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.SwitchCaseImpl
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getSwitchCase()
         * @generated
         */
        EClass SWITCH_CASE = eINSTANCE.getSwitchCase();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SWITCH_CASE__KEY = eINSTANCE.getSwitchCase_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SWITCH_CASE__VALUE = eINSTANCE.getSwitchCase_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator <em>Unary Operator</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getUnaryOperator()
         * @generated
         */
        EEnum UNARY_OPERATOR = eINSTANCE.getUnaryOperator();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator <em>Binary Operator</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getBinaryOperator()
         * @generated
         */
        EEnum BINARY_OPERATOR = eINSTANCE.getBinaryOperator();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction <em>Std Lib Function</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction
         * @see org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl#getStdLibFunction()
         * @generated
         */
        EEnum STD_LIB_FUNCTION = eINSTANCE.getStdLibFunction();

    }

} //ExpressionsPackage
