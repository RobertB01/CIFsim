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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExpressionsFactoryImpl extends EFactoryImpl implements ExpressionsFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ExpressionsFactory init()
    {
        try
        {
            ExpressionsFactory theExpressionsFactory = (ExpressionsFactory)EPackage.Registry.INSTANCE.getEFactory(ExpressionsPackage.eNS_URI);
            if (theExpressionsFactory != null)
            {
                return theExpressionsFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ExpressionsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExpressionsFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION: return createToolInvokeExpression();
            case ExpressionsPackage.TOOL_REF: return createToolRef();
            case ExpressionsPackage.BOOL_EXPRESSION: return createBoolExpression();
            case ExpressionsPackage.NUMBER_EXPRESSION: return createNumberExpression();
            case ExpressionsPackage.NULL_EXPRESSION: return createNullExpression();
            case ExpressionsPackage.DOUBLE_EXPRESSION: return createDoubleExpression();
            case ExpressionsPackage.CAST_EXPRESSION: return createCastExpression();
            case ExpressionsPackage.LIST_EXPRESSION: return createListExpression();
            case ExpressionsPackage.SET_EXPRESSION: return createSetExpression();
            case ExpressionsPackage.MAP_EXPRESSION: return createMapExpression();
            case ExpressionsPackage.MAP_ENTRY: return createMapEntry();
            case ExpressionsPackage.EMPTY_SET_MAP_EXPRESSION: return createEmptySetMapExpression();
            case ExpressionsPackage.UNRESOLVED_REF_EXPRESSION: return createUnresolvedRefExpression();
            case ExpressionsPackage.TOOL_ARGUMENT: return createToolArgument();
            case ExpressionsPackage.VARIABLE_EXPRESSION: return createVariableExpression();
            case ExpressionsPackage.STRING_EXPRESSION: return createStringExpression();
            case ExpressionsPackage.PROJECTION_EXPRESSION: return createProjectionExpression();
            case ExpressionsPackage.SLICE_EXPRESSION: return createSliceExpression();
            case ExpressionsPackage.TUPLE_EXPRESSION: return createTupleExpression();
            case ExpressionsPackage.TOOL_PARAM_EXPRESSION: return createToolParamExpression();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolInvokeExpression createToolInvokeExpression()
    {
        ToolInvokeExpressionImpl toolInvokeExpression = new ToolInvokeExpressionImpl();
        return toolInvokeExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolRef createToolRef()
    {
        ToolRefImpl toolRef = new ToolRefImpl();
        return toolRef;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public BoolExpression createBoolExpression()
    {
        BoolExpressionImpl boolExpression = new BoolExpressionImpl();
        return boolExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NumberExpression createNumberExpression()
    {
        NumberExpressionImpl numberExpression = new NumberExpressionImpl();
        return numberExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NullExpression createNullExpression()
    {
        NullExpressionImpl nullExpression = new NullExpressionImpl();
        return nullExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DoubleExpression createDoubleExpression()
    {
        DoubleExpressionImpl doubleExpression = new DoubleExpressionImpl();
        return doubleExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CastExpression createCastExpression()
    {
        CastExpressionImpl castExpression = new CastExpressionImpl();
        return castExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ListExpression createListExpression()
    {
        ListExpressionImpl listExpression = new ListExpressionImpl();
        return listExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SetExpression createSetExpression()
    {
        SetExpressionImpl setExpression = new SetExpressionImpl();
        return setExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public MapExpression createMapExpression()
    {
        MapExpressionImpl mapExpression = new MapExpressionImpl();
        return mapExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public MapEntry createMapEntry()
    {
        MapEntryImpl mapEntry = new MapEntryImpl();
        return mapEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EmptySetMapExpression createEmptySetMapExpression()
    {
        EmptySetMapExpressionImpl emptySetMapExpression = new EmptySetMapExpressionImpl();
        return emptySetMapExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public UnresolvedRefExpression createUnresolvedRefExpression()
    {
        UnresolvedRefExpressionImpl unresolvedRefExpression = new UnresolvedRefExpressionImpl();
        return unresolvedRefExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolArgument createToolArgument()
    {
        ToolArgumentImpl toolArgument = new ToolArgumentImpl();
        return toolArgument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VariableExpression createVariableExpression()
    {
        VariableExpressionImpl variableExpression = new VariableExpressionImpl();
        return variableExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public StringExpression createStringExpression()
    {
        StringExpressionImpl stringExpression = new StringExpressionImpl();
        return stringExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ProjectionExpression createProjectionExpression()
    {
        ProjectionExpressionImpl projectionExpression = new ProjectionExpressionImpl();
        return projectionExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SliceExpression createSliceExpression()
    {
        SliceExpressionImpl sliceExpression = new SliceExpressionImpl();
        return sliceExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TupleExpression createTupleExpression()
    {
        TupleExpressionImpl tupleExpression = new TupleExpressionImpl();
        return tupleExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolParamExpression createToolParamExpression()
    {
        ToolParamExpressionImpl toolParamExpression = new ToolParamExpressionImpl();
        return toolParamExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ExpressionsPackage getExpressionsPackage()
    {
        return (ExpressionsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ExpressionsPackage getPackage()
    {
        return ExpressionsPackage.eINSTANCE;
    }

} //ExpressionsFactoryImpl
