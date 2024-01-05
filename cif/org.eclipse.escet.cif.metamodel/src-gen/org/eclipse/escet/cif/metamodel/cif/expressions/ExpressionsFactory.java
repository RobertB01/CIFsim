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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage
 * @generated
 */
public interface ExpressionsFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ExpressionsFactory eINSTANCE = org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Binary Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Binary Expression</em>'.
     * @generated
     */
    BinaryExpression createBinaryExpression();

    /**
     * Returns a new object of class '<em>Unary Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Unary Expression</em>'.
     * @generated
     */
    UnaryExpression createUnaryExpression();

    /**
     * Returns a new object of class '<em>Bool Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Bool Expression</em>'.
     * @generated
     */
    BoolExpression createBoolExpression();

    /**
     * Returns a new object of class '<em>Int Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Int Expression</em>'.
     * @generated
     */
    IntExpression createIntExpression();

    /**
     * Returns a new object of class '<em>Function Call Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Function Call Expression</em>'.
     * @generated
     */
    FunctionCallExpression createFunctionCallExpression();

    /**
     * Returns a new object of class '<em>If Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>If Expression</em>'.
     * @generated
     */
    IfExpression createIfExpression();

    /**
     * Returns a new object of class '<em>Disc Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Disc Variable Expression</em>'.
     * @generated
     */
    DiscVariableExpression createDiscVariableExpression();

    /**
     * Returns a new object of class '<em>Alg Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Alg Variable Expression</em>'.
     * @generated
     */
    AlgVariableExpression createAlgVariableExpression();

    /**
     * Returns a new object of class '<em>Event Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Event Expression</em>'.
     * @generated
     */
    EventExpression createEventExpression();

    /**
     * Returns a new object of class '<em>Enum Literal Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Enum Literal Expression</em>'.
     * @generated
     */
    EnumLiteralExpression createEnumLiteralExpression();

    /**
     * Returns a new object of class '<em>Location Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Location Expression</em>'.
     * @generated
     */
    LocationExpression createLocationExpression();

    /**
     * Returns a new object of class '<em>Elif Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Elif Expression</em>'.
     * @generated
     */
    ElifExpression createElifExpression();

    /**
     * Returns a new object of class '<em>Comp Param Wrap Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Comp Param Wrap Expression</em>'.
     * @generated
     */
    CompParamWrapExpression createCompParamWrapExpression();

    /**
     * Returns a new object of class '<em>Comp Inst Wrap Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Comp Inst Wrap Expression</em>'.
     * @generated
     */
    CompInstWrapExpression createCompInstWrapExpression();

    /**
     * Returns a new object of class '<em>Component Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component Expression</em>'.
     * @generated
     */
    ComponentExpression createComponentExpression();

    /**
     * Returns a new object of class '<em>Comp Param Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Comp Param Expression</em>'.
     * @generated
     */
    CompParamExpression createCompParamExpression();

    /**
     * Returns a new object of class '<em>Constant Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Constant Expression</em>'.
     * @generated
     */
    ConstantExpression createConstantExpression();

    /**
     * Returns a new object of class '<em>Tau Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tau Expression</em>'.
     * @generated
     */
    TauExpression createTauExpression();

    /**
     * Returns a new object of class '<em>Projection Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Projection Expression</em>'.
     * @generated
     */
    ProjectionExpression createProjectionExpression();

    /**
     * Returns a new object of class '<em>Slice Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Slice Expression</em>'.
     * @generated
     */
    SliceExpression createSliceExpression();

    /**
     * Returns a new object of class '<em>Std Lib Function Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Std Lib Function Expression</em>'.
     * @generated
     */
    StdLibFunctionExpression createStdLibFunctionExpression();

    /**
     * Returns a new object of class '<em>Real Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Real Expression</em>'.
     * @generated
     */
    RealExpression createRealExpression();

    /**
     * Returns a new object of class '<em>Time Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Time Expression</em>'.
     * @generated
     */
    TimeExpression createTimeExpression();

    /**
     * Returns a new object of class '<em>List Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>List Expression</em>'.
     * @generated
     */
    ListExpression createListExpression();

    /**
     * Returns a new object of class '<em>Set Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Set Expression</em>'.
     * @generated
     */
    SetExpression createSetExpression();

    /**
     * Returns a new object of class '<em>Dict Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Dict Expression</em>'.
     * @generated
     */
    DictExpression createDictExpression();

    /**
     * Returns a new object of class '<em>Dict Pair</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Dict Pair</em>'.
     * @generated
     */
    DictPair createDictPair();

    /**
     * Returns a new object of class '<em>Tuple Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tuple Expression</em>'.
     * @generated
     */
    TupleExpression createTupleExpression();

    /**
     * Returns a new object of class '<em>Cast Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Cast Expression</em>'.
     * @generated
     */
    CastExpression createCastExpression();

    /**
     * Returns a new object of class '<em>String Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>String Expression</em>'.
     * @generated
     */
    StringExpression createStringExpression();

    /**
     * Returns a new object of class '<em>Field Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Field Expression</em>'.
     * @generated
     */
    FieldExpression createFieldExpression();

    /**
     * Returns a new object of class '<em>Function Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Function Expression</em>'.
     * @generated
     */
    FunctionExpression createFunctionExpression();

    /**
     * Returns a new object of class '<em>Cont Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Cont Variable Expression</em>'.
     * @generated
     */
    ContVariableExpression createContVariableExpression();

    /**
     * Returns a new object of class '<em>Input Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Input Variable Expression</em>'.
     * @generated
     */
    InputVariableExpression createInputVariableExpression();

    /**
     * Returns a new object of class '<em>Received Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Received Expression</em>'.
     * @generated
     */
    ReceivedExpression createReceivedExpression();

    /**
     * Returns a new object of class '<em>Self Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Self Expression</em>'.
     * @generated
     */
    SelfExpression createSelfExpression();

    /**
     * Returns a new object of class '<em>Switch Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Switch Expression</em>'.
     * @generated
     */
    SwitchExpression createSwitchExpression();

    /**
     * Returns a new object of class '<em>Switch Case</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Switch Case</em>'.
     * @generated
     */
    SwitchCase createSwitchCase();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ExpressionsPackage getExpressionsPackage();

} //ExpressionsFactory
