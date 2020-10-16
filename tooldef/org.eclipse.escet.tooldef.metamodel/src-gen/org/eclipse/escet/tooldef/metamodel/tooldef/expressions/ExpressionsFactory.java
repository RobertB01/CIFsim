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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage
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
    ExpressionsFactory eINSTANCE = org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Tool Invoke Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tool Invoke Expression</em>'.
     * @generated
     */
    ToolInvokeExpression createToolInvokeExpression();

    /**
     * Returns a new object of class '<em>Tool Ref</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tool Ref</em>'.
     * @generated
     */
    ToolRef createToolRef();

    /**
     * Returns a new object of class '<em>Bool Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Bool Expression</em>'.
     * @generated
     */
    BoolExpression createBoolExpression();

    /**
     * Returns a new object of class '<em>Number Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Number Expression</em>'.
     * @generated
     */
    NumberExpression createNumberExpression();

    /**
     * Returns a new object of class '<em>Null Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Null Expression</em>'.
     * @generated
     */
    NullExpression createNullExpression();

    /**
     * Returns a new object of class '<em>Double Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Double Expression</em>'.
     * @generated
     */
    DoubleExpression createDoubleExpression();

    /**
     * Returns a new object of class '<em>Cast Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Cast Expression</em>'.
     * @generated
     */
    CastExpression createCastExpression();

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
     * Returns a new object of class '<em>Map Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Map Expression</em>'.
     * @generated
     */
    MapExpression createMapExpression();

    /**
     * Returns a new object of class '<em>Map Entry</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Map Entry</em>'.
     * @generated
     */
    MapEntry createMapEntry();

    /**
     * Returns a new object of class '<em>Empty Set Map Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Empty Set Map Expression</em>'.
     * @generated
     */
    EmptySetMapExpression createEmptySetMapExpression();

    /**
     * Returns a new object of class '<em>Unresolved Ref Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Unresolved Ref Expression</em>'.
     * @generated
     */
    UnresolvedRefExpression createUnresolvedRefExpression();

    /**
     * Returns a new object of class '<em>Tool Argument</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tool Argument</em>'.
     * @generated
     */
    ToolArgument createToolArgument();

    /**
     * Returns a new object of class '<em>Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Variable Expression</em>'.
     * @generated
     */
    VariableExpression createVariableExpression();

    /**
     * Returns a new object of class '<em>String Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>String Expression</em>'.
     * @generated
     */
    StringExpression createStringExpression();

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
     * Returns a new object of class '<em>Tuple Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tuple Expression</em>'.
     * @generated
     */
    TupleExpression createTupleExpression();

    /**
     * Returns a new object of class '<em>Tool Param Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tool Param Expression</em>'.
     * @generated
     */
    ToolParamExpression createToolParamExpression();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ExpressionsPackage getExpressionsPackage();

} //ExpressionsFactory
