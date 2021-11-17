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
package org.eclipse.escet.cif.metamodel.cif.expressions.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.escet.cif.metamodel.cif.expressions.*;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage
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
            public Adapter caseBinaryExpression(BinaryExpression object)
            {
                return createBinaryExpressionAdapter();
            }
            @Override
            public Adapter caseUnaryExpression(UnaryExpression object)
            {
                return createUnaryExpressionAdapter();
            }
            @Override
            public Adapter caseBoolExpression(BoolExpression object)
            {
                return createBoolExpressionAdapter();
            }
            @Override
            public Adapter caseIntExpression(IntExpression object)
            {
                return createIntExpressionAdapter();
            }
            @Override
            public Adapter caseFunctionCallExpression(FunctionCallExpression object)
            {
                return createFunctionCallExpressionAdapter();
            }
            @Override
            public Adapter caseIfExpression(IfExpression object)
            {
                return createIfExpressionAdapter();
            }
            @Override
            public Adapter caseDiscVariableExpression(DiscVariableExpression object)
            {
                return createDiscVariableExpressionAdapter();
            }
            @Override
            public Adapter caseAlgVariableExpression(AlgVariableExpression object)
            {
                return createAlgVariableExpressionAdapter();
            }
            @Override
            public Adapter caseEventExpression(EventExpression object)
            {
                return createEventExpressionAdapter();
            }
            @Override
            public Adapter caseEnumLiteralExpression(EnumLiteralExpression object)
            {
                return createEnumLiteralExpressionAdapter();
            }
            @Override
            public Adapter caseLocationExpression(LocationExpression object)
            {
                return createLocationExpressionAdapter();
            }
            @Override
            public Adapter caseElifExpression(ElifExpression object)
            {
                return createElifExpressionAdapter();
            }
            @Override
            public Adapter caseCompParamWrapExpression(CompParamWrapExpression object)
            {
                return createCompParamWrapExpressionAdapter();
            }
            @Override
            public Adapter caseCompInstWrapExpression(CompInstWrapExpression object)
            {
                return createCompInstWrapExpressionAdapter();
            }
            @Override
            public Adapter caseComponentExpression(ComponentExpression object)
            {
                return createComponentExpressionAdapter();
            }
            @Override
            public Adapter caseCompParamExpression(CompParamExpression object)
            {
                return createCompParamExpressionAdapter();
            }
            @Override
            public Adapter caseConstantExpression(ConstantExpression object)
            {
                return createConstantExpressionAdapter();
            }
            @Override
            public Adapter caseTauExpression(TauExpression object)
            {
                return createTauExpressionAdapter();
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
            public Adapter caseBaseFunctionExpression(BaseFunctionExpression object)
            {
                return createBaseFunctionExpressionAdapter();
            }
            @Override
            public Adapter caseStdLibFunctionExpression(StdLibFunctionExpression object)
            {
                return createStdLibFunctionExpressionAdapter();
            }
            @Override
            public Adapter caseRealExpression(RealExpression object)
            {
                return createRealExpressionAdapter();
            }
            @Override
            public Adapter caseTimeExpression(TimeExpression object)
            {
                return createTimeExpressionAdapter();
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
            public Adapter caseDictExpression(DictExpression object)
            {
                return createDictExpressionAdapter();
            }
            @Override
            public Adapter caseDictPair(DictPair object)
            {
                return createDictPairAdapter();
            }
            @Override
            public Adapter caseTupleExpression(TupleExpression object)
            {
                return createTupleExpressionAdapter();
            }
            @Override
            public Adapter caseCastExpression(CastExpression object)
            {
                return createCastExpressionAdapter();
            }
            @Override
            public Adapter caseStringExpression(StringExpression object)
            {
                return createStringExpressionAdapter();
            }
            @Override
            public Adapter caseFieldExpression(FieldExpression object)
            {
                return createFieldExpressionAdapter();
            }
            @Override
            public Adapter caseFunctionExpression(FunctionExpression object)
            {
                return createFunctionExpressionAdapter();
            }
            @Override
            public Adapter caseContVariableExpression(ContVariableExpression object)
            {
                return createContVariableExpressionAdapter();
            }
            @Override
            public Adapter caseInputVariableExpression(InputVariableExpression object)
            {
                return createInputVariableExpressionAdapter();
            }
            @Override
            public Adapter caseReceivedExpression(ReceivedExpression object)
            {
                return createReceivedExpressionAdapter();
            }
            @Override
            public Adapter caseSelfExpression(SelfExpression object)
            {
                return createSelfExpressionAdapter();
            }
            @Override
            public Adapter caseSwitchExpression(SwitchExpression object)
            {
                return createSwitchExpressionAdapter();
            }
            @Override
            public Adapter caseSwitchCase(SwitchCase object)
            {
                return createSwitchCaseAdapter();
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
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.Expression
     * @generated
     */
    public Adapter createExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression <em>Binary Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression
     * @generated
     */
    public Adapter createBinaryExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression <em>Unary Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression
     * @generated
     */
    public Adapter createUnaryExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression <em>Bool Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression
     * @generated
     */
    public Adapter createBoolExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression <em>Int Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression
     * @generated
     */
    public Adapter createIntExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression <em>Function Call Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression
     * @generated
     */
    public Adapter createFunctionCallExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression <em>If Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression
     * @generated
     */
    public Adapter createIfExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression <em>Disc Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression
     * @generated
     */
    public Adapter createDiscVariableExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression <em>Alg Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression
     * @generated
     */
    public Adapter createAlgVariableExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression <em>Event Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression
     * @generated
     */
    public Adapter createEventExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression <em>Enum Literal Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression
     * @generated
     */
    public Adapter createEnumLiteralExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression <em>Location Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression
     * @generated
     */
    public Adapter createLocationExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression <em>Elif Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression
     * @generated
     */
    public Adapter createElifExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression <em>Comp Param Wrap Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression
     * @generated
     */
    public Adapter createCompParamWrapExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression <em>Comp Inst Wrap Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression
     * @generated
     */
    public Adapter createCompInstWrapExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression <em>Component Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression
     * @generated
     */
    public Adapter createComponentExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression <em>Constant Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression
     * @generated
     */
    public Adapter createConstantExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression <em>Tau Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression
     * @generated
     */
    public Adapter createTauExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression <em>Projection Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression
     * @generated
     */
    public Adapter createProjectionExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression <em>Slice Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression
     * @generated
     */
    public Adapter createSliceExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression <em>Base Function Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression
     * @generated
     */
    public Adapter createBaseFunctionExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression <em>Std Lib Function Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression
     * @generated
     */
    public Adapter createStdLibFunctionExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression <em>Real Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression
     * @generated
     */
    public Adapter createRealExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression <em>Time Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression
     * @generated
     */
    public Adapter createTimeExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression <em>List Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression
     * @generated
     */
    public Adapter createListExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression <em>Set Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression
     * @generated
     */
    public Adapter createSetExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression <em>Dict Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression
     * @generated
     */
    public Adapter createDictExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.DictPair <em>Dict Pair</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.DictPair
     * @generated
     */
    public Adapter createDictPairAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression <em>Tuple Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression
     * @generated
     */
    public Adapter createTupleExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression <em>Cast Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression
     * @generated
     */
    public Adapter createCastExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression <em>String Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression
     * @generated
     */
    public Adapter createStringExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression <em>Field Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression
     * @generated
     */
    public Adapter createFieldExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression <em>Function Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression
     * @generated
     */
    public Adapter createFunctionExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression <em>Cont Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression
     * @generated
     */
    public Adapter createContVariableExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression <em>Input Variable Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression
     * @generated
     */
    public Adapter createInputVariableExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression <em>Received Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression
     * @generated
     */
    public Adapter createReceivedExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression <em>Self Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression
     * @generated
     */
    public Adapter createSelfExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression <em>Switch Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression
     * @generated
     */
    public Adapter createSwitchExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase <em>Switch Case</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase
     * @generated
     */
    public Adapter createSwitchCaseAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression <em>Comp Param Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression
     * @generated
     */
    public Adapter createCompParamExpressionAdapter()
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
