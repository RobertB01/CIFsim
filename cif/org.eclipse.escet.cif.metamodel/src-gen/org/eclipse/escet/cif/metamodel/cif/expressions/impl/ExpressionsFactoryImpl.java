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
package org.eclipse.escet.cif.metamodel.cif.expressions.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.cif.metamodel.cif.expressions.*;

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
            case ExpressionsPackage.BINARY_EXPRESSION: return createBinaryExpression();
            case ExpressionsPackage.UNARY_EXPRESSION: return createUnaryExpression();
            case ExpressionsPackage.BOOL_EXPRESSION: return createBoolExpression();
            case ExpressionsPackage.INT_EXPRESSION: return createIntExpression();
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION: return createFunctionCallExpression();
            case ExpressionsPackage.IF_EXPRESSION: return createIfExpression();
            case ExpressionsPackage.DISC_VARIABLE_EXPRESSION: return createDiscVariableExpression();
            case ExpressionsPackage.ALG_VARIABLE_EXPRESSION: return createAlgVariableExpression();
            case ExpressionsPackage.EVENT_EXPRESSION: return createEventExpression();
            case ExpressionsPackage.ENUM_LITERAL_EXPRESSION: return createEnumLiteralExpression();
            case ExpressionsPackage.LOCATION_EXPRESSION: return createLocationExpression();
            case ExpressionsPackage.ELIF_EXPRESSION: return createElifExpression();
            case ExpressionsPackage.COMP_PARAM_WRAP_EXPRESSION: return createCompParamWrapExpression();
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION: return createCompInstWrapExpression();
            case ExpressionsPackage.COMPONENT_EXPRESSION: return createComponentExpression();
            case ExpressionsPackage.COMP_PARAM_EXPRESSION: return createCompParamExpression();
            case ExpressionsPackage.CONSTANT_EXPRESSION: return createConstantExpression();
            case ExpressionsPackage.TAU_EXPRESSION: return createTauExpression();
            case ExpressionsPackage.PROJECTION_EXPRESSION: return createProjectionExpression();
            case ExpressionsPackage.SLICE_EXPRESSION: return createSliceExpression();
            case ExpressionsPackage.STD_LIB_FUNCTION_EXPRESSION: return createStdLibFunctionExpression();
            case ExpressionsPackage.REAL_EXPRESSION: return createRealExpression();
            case ExpressionsPackage.TIME_EXPRESSION: return createTimeExpression();
            case ExpressionsPackage.LIST_EXPRESSION: return createListExpression();
            case ExpressionsPackage.SET_EXPRESSION: return createSetExpression();
            case ExpressionsPackage.DICT_EXPRESSION: return createDictExpression();
            case ExpressionsPackage.DICT_PAIR: return createDictPair();
            case ExpressionsPackage.TUPLE_EXPRESSION: return createTupleExpression();
            case ExpressionsPackage.CAST_EXPRESSION: return createCastExpression();
            case ExpressionsPackage.STRING_EXPRESSION: return createStringExpression();
            case ExpressionsPackage.FIELD_EXPRESSION: return createFieldExpression();
            case ExpressionsPackage.FUNCTION_EXPRESSION: return createFunctionExpression();
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION: return createContVariableExpression();
            case ExpressionsPackage.INPUT_VARIABLE_EXPRESSION: return createInputVariableExpression();
            case ExpressionsPackage.RECEIVED_EXPRESSION: return createReceivedExpression();
            case ExpressionsPackage.SELF_EXPRESSION: return createSelfExpression();
            case ExpressionsPackage.SWITCH_EXPRESSION: return createSwitchExpression();
            case ExpressionsPackage.SWITCH_CASE: return createSwitchCase();
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
    public Object createFromString(EDataType eDataType, String initialValue)
    {
        switch (eDataType.getClassifierID())
        {
            case ExpressionsPackage.UNARY_OPERATOR:
                return createUnaryOperatorFromString(eDataType, initialValue);
            case ExpressionsPackage.BINARY_OPERATOR:
                return createBinaryOperatorFromString(eDataType, initialValue);
            case ExpressionsPackage.STD_LIB_FUNCTION:
                return createStdLibFunctionFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue)
    {
        switch (eDataType.getClassifierID())
        {
            case ExpressionsPackage.UNARY_OPERATOR:
                return convertUnaryOperatorToString(eDataType, instanceValue);
            case ExpressionsPackage.BINARY_OPERATOR:
                return convertBinaryOperatorToString(eDataType, instanceValue);
            case ExpressionsPackage.STD_LIB_FUNCTION:
                return convertStdLibFunctionToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public BinaryExpression createBinaryExpression()
    {
        BinaryExpressionImpl binaryExpression = new BinaryExpressionImpl();
        return binaryExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public UnaryExpression createUnaryExpression()
    {
        UnaryExpressionImpl unaryExpression = new UnaryExpressionImpl();
        return unaryExpression;
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
    public IntExpression createIntExpression()
    {
        IntExpressionImpl intExpression = new IntExpressionImpl();
        return intExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FunctionCallExpression createFunctionCallExpression()
    {
        FunctionCallExpressionImpl functionCallExpression = new FunctionCallExpressionImpl();
        return functionCallExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IfExpression createIfExpression()
    {
        IfExpressionImpl ifExpression = new IfExpressionImpl();
        return ifExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DiscVariableExpression createDiscVariableExpression()
    {
        DiscVariableExpressionImpl discVariableExpression = new DiscVariableExpressionImpl();
        return discVariableExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AlgVariableExpression createAlgVariableExpression()
    {
        AlgVariableExpressionImpl algVariableExpression = new AlgVariableExpressionImpl();
        return algVariableExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EventExpression createEventExpression()
    {
        EventExpressionImpl eventExpression = new EventExpressionImpl();
        return eventExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EnumLiteralExpression createEnumLiteralExpression()
    {
        EnumLiteralExpressionImpl enumLiteralExpression = new EnumLiteralExpressionImpl();
        return enumLiteralExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public LocationExpression createLocationExpression()
    {
        LocationExpressionImpl locationExpression = new LocationExpressionImpl();
        return locationExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ElifExpression createElifExpression()
    {
        ElifExpressionImpl elifExpression = new ElifExpressionImpl();
        return elifExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CompParamWrapExpression createCompParamWrapExpression()
    {
        CompParamWrapExpressionImpl compParamWrapExpression = new CompParamWrapExpressionImpl();
        return compParamWrapExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CompInstWrapExpression createCompInstWrapExpression()
    {
        CompInstWrapExpressionImpl compInstWrapExpression = new CompInstWrapExpressionImpl();
        return compInstWrapExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ComponentExpression createComponentExpression()
    {
        ComponentExpressionImpl componentExpression = new ComponentExpressionImpl();
        return componentExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CompParamExpression createCompParamExpression()
    {
        CompParamExpressionImpl compParamExpression = new CompParamExpressionImpl();
        return compParamExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstantExpression createConstantExpression()
    {
        ConstantExpressionImpl constantExpression = new ConstantExpressionImpl();
        return constantExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TauExpression createTauExpression()
    {
        TauExpressionImpl tauExpression = new TauExpressionImpl();
        return tauExpression;
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
    public StdLibFunctionExpression createStdLibFunctionExpression()
    {
        StdLibFunctionExpressionImpl stdLibFunctionExpression = new StdLibFunctionExpressionImpl();
        return stdLibFunctionExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public RealExpression createRealExpression()
    {
        RealExpressionImpl realExpression = new RealExpressionImpl();
        return realExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TimeExpression createTimeExpression()
    {
        TimeExpressionImpl timeExpression = new TimeExpressionImpl();
        return timeExpression;
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
    public DictExpression createDictExpression()
    {
        DictExpressionImpl dictExpression = new DictExpressionImpl();
        return dictExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DictPair createDictPair()
    {
        DictPairImpl dictPair = new DictPairImpl();
        return dictPair;
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
    public FieldExpression createFieldExpression()
    {
        FieldExpressionImpl fieldExpression = new FieldExpressionImpl();
        return fieldExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FunctionExpression createFunctionExpression()
    {
        FunctionExpressionImpl functionExpression = new FunctionExpressionImpl();
        return functionExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ContVariableExpression createContVariableExpression()
    {
        ContVariableExpressionImpl contVariableExpression = new ContVariableExpressionImpl();
        return contVariableExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public InputVariableExpression createInputVariableExpression()
    {
        InputVariableExpressionImpl inputVariableExpression = new InputVariableExpressionImpl();
        return inputVariableExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ReceivedExpression createReceivedExpression()
    {
        ReceivedExpressionImpl receivedExpression = new ReceivedExpressionImpl();
        return receivedExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SelfExpression createSelfExpression()
    {
        SelfExpressionImpl selfExpression = new SelfExpressionImpl();
        return selfExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SwitchExpression createSwitchExpression()
    {
        SwitchExpressionImpl switchExpression = new SwitchExpressionImpl();
        return switchExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SwitchCase createSwitchCase()
    {
        SwitchCaseImpl switchCase = new SwitchCaseImpl();
        return switchCase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UnaryOperator createUnaryOperatorFromString(EDataType eDataType, String initialValue)
    {
        UnaryOperator result = UnaryOperator.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertUnaryOperatorToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryOperator createBinaryOperatorFromString(EDataType eDataType, String initialValue)
    {
        BinaryOperator result = BinaryOperator.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertBinaryOperatorToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StdLibFunction createStdLibFunctionFromString(EDataType eDataType, String initialValue)
    {
        StdLibFunction result = StdLibFunction.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStdLibFunctionToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
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
