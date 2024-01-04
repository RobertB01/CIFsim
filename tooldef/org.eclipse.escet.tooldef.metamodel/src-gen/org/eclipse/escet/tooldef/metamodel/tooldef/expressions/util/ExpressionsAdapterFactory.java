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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage
 * @generated
 */
public class ExpressionsAdapterFactory extends AdapterFactoryImpl
{
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ExpressionsPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExpressionsAdapterFactory()
    {
        if (modelPackage == null)
        {
            modelPackage = ExpressionsPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object)
    {
        if (object == modelPackage)
        {
            return true;
        }
        if (object instanceof EObject)
        {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExpressionsSwitch<Adapter> modelSwitch =
        new ExpressionsSwitch<Adapter>()
        {
            @Override
            public Adapter caseExpression(Expression object)
            {
                return createExpressionAdapter();
            }
            @Override
            public Adapter caseToolInvokeExpression(ToolInvokeExpression object)
            {
                return createToolInvokeExpressionAdapter();
            }
            @Override
            public Adapter caseToolRef(ToolRef object)
            {
                return createToolRefAdapter();
            }
            @Override
            public Adapter caseBoolExpression(BoolExpression object)
            {
                return createBoolExpressionAdapter();
            }
            @Override
            public Adapter caseNumberExpression(NumberExpression object)
            {
                return createNumberExpressionAdapter();
            }
            @Override
            public Adapter caseNullExpression(NullExpression object)
            {
                return createNullExpressionAdapter();
            }
            @Override
            public Adapter caseDoubleExpression(DoubleExpression object)
            {
                return createDoubleExpressionAdapter();
            }
            @Override
            public Adapter caseCastExpression(CastExpression object)
            {
                return createCastExpressionAdapter();
            }
            @Override
            public Adapter caseListExpression(ListExpression object)
            {
                return createListExpressionAdapter();
            }
            @Override
            public Adapter caseSetExpression(SetExpression object)
            {
                return createSetExpressionAdapter();
            }
            @Override
            public Adapter caseMapExpression(MapExpression object)
            {
                return createMapExpressionAdapter();
            }
            @Override
            public Adapter caseMapEntry(MapEntry object)
            {
                return createMapEntryAdapter();
            }
            @Override
            public Adapter caseEmptySetMapExpression(EmptySetMapExpression object)
            {
                return createEmptySetMapExpressionAdapter();
            }
            @Override
            public Adapter caseUnresolvedRefExpression(UnresolvedRefExpression object)
            {
                return createUnresolvedRefExpressionAdapter();
            }
            @Override
            public Adapter caseToolArgument(ToolArgument object)
            {
                return createToolArgumentAdapter();
            }
            @Override
            public Adapter caseVariableExpression(VariableExpression object)
            {
                return createVariableExpressionAdapter();
            }
            @Override
            public Adapter caseStringExpression(StringExpression object)
            {
                return createStringExpressionAdapter();
            }
            @Override
            public Adapter caseProjectionExpression(ProjectionExpression object)
            {
                return createProjectionExpressionAdapter();
            }
            @Override
            public Adapter caseSliceExpression(SliceExpression object)
            {
                return createSliceExpressionAdapter();
            }
            @Override
            public Adapter caseTupleExpression(TupleExpression object)
            {
                return createTupleExpressionAdapter();
            }
            @Override
            public Adapter caseToolParamExpression(ToolParamExpression object)
            {
                return createToolParamExpressionAdapter();
            }
            @Override
            public Adapter casePositionObject(PositionObject object)
            {
                return createPositionObjectAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object)
            {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target)
    {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression
     * @generated
     */
    public Adapter createExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression <em>Tool Invoke Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression
     * @generated
     */
    public Adapter createToolInvokeExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef <em>Tool Ref</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef
     * @generated
     */
    public Adapter createToolRefAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression <em>Bool Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression
     * @generated
     */
    public Adapter createBoolExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression <em>Number Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression
     * @generated
     */
    public Adapter createNumberExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NullExpression <em>Null Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NullExpression
     * @generated
     */
    public Adapter createNullExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression <em>Double Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression
     * @generated
     */
    public Adapter createDoubleExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression <em>Cast Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression
     * @generated
     */
    public Adapter createCastExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression <em>List Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression
     * @generated
     */
    public Adapter createListExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression <em>Set Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression
     * @generated
     */
    public Adapter createSetExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression <em>Map Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression
     * @generated
     */
    public Adapter createMapExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry <em>Map Entry</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry
     * @generated
     */
    public Adapter createMapEntryAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.EmptySetMapExpression <em>Empty Set Map Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.EmptySetMapExpression
     * @generated
     */
    public Adapter createEmptySetMapExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression <em>Unresolved Ref Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression
     * @generated
     */
    public Adapter createUnresolvedRefExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument <em>Tool Argument</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument
     * @generated
     */
    public Adapter createToolArgumentAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression <em>Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression
     * @generated
     */
    public Adapter createVariableExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression <em>String Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression
     * @generated
     */
    public Adapter createStringExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression <em>Projection Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression
     * @generated
     */
    public Adapter createProjectionExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression <em>Slice Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression
     * @generated
     */
    public Adapter createSliceExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression <em>Tuple Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression
     * @generated
     */
    public Adapter createTupleExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression <em>Tool Param Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression
     * @generated
     */
    public Adapter createToolParamExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.common.position.metamodel.position.PositionObject <em>Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.common.position.metamodel.position.PositionObject
     * @generated
     */
    public Adapter createPositionObjectAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter()
    {
        return null;
    }

} //ExpressionsAdapterFactory
