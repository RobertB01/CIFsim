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
package org.eclipse.escet.chi.metamodel.chi.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.escet.chi.metamodel.chi.*;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage
 * @generated
 */
public class ChiAdapterFactory extends AdapterFactoryImpl
{
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ChiPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ChiAdapterFactory()
    {
        if (modelPackage == null)
        {
            modelPackage = ChiPackage.eINSTANCE;
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
    protected ChiSwitch<Adapter> modelSwitch =
        new ChiSwitch<Adapter>()
        {
            @Override
            public Adapter caseType(Type object)
            {
                return createTypeAdapter();
            }
            @Override
            public Adapter caseVoidType(VoidType object)
            {
                return createVoidTypeAdapter();
            }
            @Override
            public Adapter caseBoolType(BoolType object)
            {
                return createBoolTypeAdapter();
            }
            @Override
            public Adapter caseInstanceType(InstanceType object)
            {
                return createInstanceTypeAdapter();
            }
            @Override
            public Adapter caseIntType(IntType object)
            {
                return createIntTypeAdapter();
            }
            @Override
            public Adapter caseStringType(StringType object)
            {
                return createStringTypeAdapter();
            }
            @Override
            public Adapter caseRealType(RealType object)
            {
                return createRealTypeAdapter();
            }
            @Override
            public Adapter caseFileType(FileType object)
            {
                return createFileTypeAdapter();
            }
            @Override
            public Adapter caseSetType(SetType object)
            {
                return createSetTypeAdapter();
            }
            @Override
            public Adapter caseListType(ListType object)
            {
                return createListTypeAdapter();
            }
            @Override
            public Adapter caseDictType(DictType object)
            {
                return createDictTypeAdapter();
            }
            @Override
            public Adapter caseMatrixType(MatrixType object)
            {
                return createMatrixTypeAdapter();
            }
            @Override
            public Adapter caseTupleType(TupleType object)
            {
                return createTupleTypeAdapter();
            }
            @Override
            public Adapter caseTupleField(TupleField object)
            {
                return createTupleFieldAdapter();
            }
            @Override
            public Adapter caseDistributionType(DistributionType object)
            {
                return createDistributionTypeAdapter();
            }
            @Override
            public Adapter caseEnumTypeReference(EnumTypeReference object)
            {
                return createEnumTypeReferenceAdapter();
            }
            @Override
            public Adapter caseChannelType(ChannelType object)
            {
                return createChannelTypeAdapter();
            }
            @Override
            public Adapter caseFunctionType(FunctionType object)
            {
                return createFunctionTypeAdapter();
            }
            @Override
            public Adapter caseTypeReference(TypeReference object)
            {
                return createTypeReferenceAdapter();
            }
            @Override
            public Adapter caseExpression(Expression object)
            {
                return createExpressionAdapter();
            }
            @Override
            public Adapter caseBoolLiteral(BoolLiteral object)
            {
                return createBoolLiteralAdapter();
            }
            @Override
            public Adapter caseIntNumber(IntNumber object)
            {
                return createIntNumberAdapter();
            }
            @Override
            public Adapter caseRealNumber(RealNumber object)
            {
                return createRealNumberAdapter();
            }
            @Override
            public Adapter caseStringLiteral(StringLiteral object)
            {
                return createStringLiteralAdapter();
            }
            @Override
            public Adapter caseTupleExpression(TupleExpression object)
            {
                return createTupleExpressionAdapter();
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
            public Adapter caseMatrixExpression(MatrixExpression object)
            {
                return createMatrixExpressionAdapter();
            }
            @Override
            public Adapter caseMatrixRow(MatrixRow object)
            {
                return createMatrixRowAdapter();
            }
            @Override
            public Adapter caseDictionaryExpression(DictionaryExpression object)
            {
                return createDictionaryExpressionAdapter();
            }
            @Override
            public Adapter caseDictionaryPair(DictionaryPair object)
            {
                return createDictionaryPairAdapter();
            }
            @Override
            public Adapter caseVariableReference(VariableReference object)
            {
                return createVariableReferenceAdapter();
            }
            @Override
            public Adapter caseConstantReference(ConstantReference object)
            {
                return createConstantReferenceAdapter();
            }
            @Override
            public Adapter caseTimeLiteral(TimeLiteral object)
            {
                return createTimeLiteralAdapter();
            }
            @Override
            public Adapter caseUnaryExpression(UnaryExpression object)
            {
                return createUnaryExpressionAdapter();
            }
            @Override
            public Adapter caseBinaryExpression(BinaryExpression object)
            {
                return createBinaryExpressionAdapter();
            }
            @Override
            public Adapter caseCallExpression(CallExpression object)
            {
                return createCallExpressionAdapter();
            }
            @Override
            public Adapter caseFunctionReference(FunctionReference object)
            {
                return createFunctionReferenceAdapter();
            }
            @Override
            public Adapter caseBaseFunctionReference(BaseFunctionReference object)
            {
                return createBaseFunctionReferenceAdapter();
            }
            @Override
            public Adapter caseStdLibFunctionReference(StdLibFunctionReference object)
            {
                return createStdLibFunctionReferenceAdapter();
            }
            @Override
            public Adapter caseSliceExpression(SliceExpression object)
            {
                return createSliceExpressionAdapter();
            }
            @Override
            public Adapter caseFieldReference(FieldReference object)
            {
                return createFieldReferenceAdapter();
            }
            @Override
            public Adapter caseStatement(Statement object)
            {
                return createStatementAdapter();
            }
            @Override
            public Adapter caseBreakStatement(BreakStatement object)
            {
                return createBreakStatementAdapter();
            }
            @Override
            public Adapter caseContinueStatement(ContinueStatement object)
            {
                return createContinueStatementAdapter();
            }
            @Override
            public Adapter casePassStatement(PassStatement object)
            {
                return createPassStatementAdapter();
            }
            @Override
            public Adapter caseExitStatement(ExitStatement object)
            {
                return createExitStatementAdapter();
            }
            @Override
            public Adapter caseReturnStatement(ReturnStatement object)
            {
                return createReturnStatementAdapter();
            }
            @Override
            public Adapter caseDelayStatement(DelayStatement object)
            {
                return createDelayStatementAdapter();
            }
            @Override
            public Adapter caseWhileStatement(WhileStatement object)
            {
                return createWhileStatementAdapter();
            }
            @Override
            public Adapter caseIfStatement(IfStatement object)
            {
                return createIfStatementAdapter();
            }
            @Override
            public Adapter caseIfCase(IfCase object)
            {
                return createIfCaseAdapter();
            }
            @Override
            public Adapter caseWriteStatement(WriteStatement object)
            {
                return createWriteStatementAdapter();
            }
            @Override
            public Adapter caseAssignmentStatement(AssignmentStatement object)
            {
                return createAssignmentStatementAdapter();
            }
            @Override
            public Adapter caseCommunicationStatement(CommunicationStatement object)
            {
                return createCommunicationStatementAdapter();
            }
            @Override
            public Adapter caseForStatement(ForStatement object)
            {
                return createForStatementAdapter();
            }
            @Override
            public Adapter caseCreateCase(CreateCase object)
            {
                return createCreateCaseAdapter();
            }
            @Override
            public Adapter caseRunStatement(RunStatement object)
            {
                return createRunStatementAdapter();
            }
            @Override
            public Adapter caseSelectStatement(SelectStatement object)
            {
                return createSelectStatementAdapter();
            }
            @Override
            public Adapter caseSelectCase(SelectCase object)
            {
                return createSelectCaseAdapter();
            }
            @Override
            public Adapter caseIteratedCreateCase(IteratedCreateCase object)
            {
                return createIteratedCreateCaseAdapter();
            }
            @Override
            public Adapter caseIteratedSelectCase(IteratedSelectCase object)
            {
                return createIteratedSelectCaseAdapter();
            }
            @Override
            public Adapter caseSpecification(Specification object)
            {
                return createSpecificationAdapter();
            }
            @Override
            public Adapter caseDeclaration(Declaration object)
            {
                return createDeclarationAdapter();
            }
            @Override
            public Adapter caseTypeDeclaration(TypeDeclaration object)
            {
                return createTypeDeclarationAdapter();
            }
            @Override
            public Adapter caseConstantDeclaration(ConstantDeclaration object)
            {
                return createConstantDeclarationAdapter();
            }
            @Override
            public Adapter caseProcessDeclaration(ProcessDeclaration object)
            {
                return createProcessDeclarationAdapter();
            }
            @Override
            public Adapter caseFunctionDeclaration(FunctionDeclaration object)
            {
                return createFunctionDeclarationAdapter();
            }
            @Override
            public Adapter caseModelDeclaration(ModelDeclaration object)
            {
                return createModelDeclarationAdapter();
            }
            @Override
            public Adapter caseVariableDeclaration(VariableDeclaration object)
            {
                return createVariableDeclarationAdapter();
            }
            @Override
            public Adapter caseBehaviourDeclaration(BehaviourDeclaration object)
            {
                return createBehaviourDeclarationAdapter();
            }
            @Override
            public Adapter caseReceiveStatement(ReceiveStatement object)
            {
                return createReceiveStatementAdapter();
            }
            @Override
            public Adapter caseSendStatement(SendStatement object)
            {
                return createSendStatementAdapter();
            }
            @Override
            public Adapter caseEnumValueReference(EnumValueReference object)
            {
                return createEnumValueReferenceAdapter();
            }
            @Override
            public Adapter caseReadCallExpression(ReadCallExpression object)
            {
                return createReadCallExpressionAdapter();
            }
            @Override
            public Adapter caseUnwind(Unwind object)
            {
                return createUnwindAdapter();
            }
            @Override
            public Adapter caseProcessInstance(ProcessInstance object)
            {
                return createProcessInstanceAdapter();
            }
            @Override
            public Adapter caseProcessType(ProcessType object)
            {
                return createProcessTypeAdapter();
            }
            @Override
            public Adapter caseProcessReference(ProcessReference object)
            {
                return createProcessReferenceAdapter();
            }
            @Override
            public Adapter caseUnresolvedReference(UnresolvedReference object)
            {
                return createUnresolvedReferenceAdapter();
            }
            @Override
            public Adapter caseUnresolvedType(UnresolvedType object)
            {
                return createUnresolvedTypeAdapter();
            }
            @Override
            public Adapter caseTimerType(TimerType object)
            {
                return createTimerTypeAdapter();
            }
            @Override
            public Adapter caseEnumDeclaration(EnumDeclaration object)
            {
                return createEnumDeclarationAdapter();
            }
            @Override
            public Adapter caseEnumValue(EnumValue object)
            {
                return createEnumValueAdapter();
            }
            @Override
            public Adapter caseChannelExpression(ChannelExpression object)
            {
                return createChannelExpressionAdapter();
            }
            @Override
            public Adapter caseCastExpression(CastExpression object)
            {
                return createCastExpressionAdapter();
            }
            @Override
            public Adapter caseCloseStatement(CloseStatement object)
            {
                return createCloseStatementAdapter();
            }
            @Override
            public Adapter caseFinishStatement(FinishStatement object)
            {
                return createFinishStatementAdapter();
            }
            @Override
            public Adapter caseModelReference(ModelReference object)
            {
                return createModelReferenceAdapter();
            }
            @Override
            public Adapter caseXperDeclaration(XperDeclaration object)
            {
                return createXperDeclarationAdapter();
            }
            @Override
            public Adapter caseComputeDeclaration(ComputeDeclaration object)
            {
                return createComputeDeclarationAdapter();
            }
            @Override
            public Adapter caseComputeType(ComputeType object)
            {
                return createComputeTypeAdapter();
            }
            @Override
            public Adapter caseModelType(ModelType object)
            {
                return createModelTypeAdapter();
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
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.Type <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.Type
     * @generated
     */
    public Adapter createTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.VoidType <em>Void Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.VoidType
     * @generated
     */
    public Adapter createVoidTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.BoolType <em>Bool Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.BoolType
     * @generated
     */
    public Adapter createBoolTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.InstanceType <em>Instance Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.InstanceType
     * @generated
     */
    public Adapter createInstanceTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.IntType <em>Int Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.IntType
     * @generated
     */
    public Adapter createIntTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.StringType <em>String Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.StringType
     * @generated
     */
    public Adapter createStringTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.RealType <em>Real Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.RealType
     * @generated
     */
    public Adapter createRealTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.FileType <em>File Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.FileType
     * @generated
     */
    public Adapter createFileTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.SetType <em>Set Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.SetType
     * @generated
     */
    public Adapter createSetTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ListType <em>List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ListType
     * @generated
     */
    public Adapter createListTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.DictType <em>Dict Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.DictType
     * @generated
     */
    public Adapter createDictTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.MatrixType <em>Matrix Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixType
     * @generated
     */
    public Adapter createMatrixTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.TupleType <em>Tuple Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleType
     * @generated
     */
    public Adapter createTupleTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.TupleField <em>Tuple Field</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleField
     * @generated
     */
    public Adapter createTupleFieldAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.DistributionType <em>Distribution Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.DistributionType
     * @generated
     */
    public Adapter createDistributionTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.EnumTypeReference <em>Enum Type Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumTypeReference
     * @generated
     */
    public Adapter createEnumTypeReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ChannelType <em>Channel Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelType
     * @generated
     */
    public Adapter createChannelTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.FunctionType <em>Function Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionType
     * @generated
     */
    public Adapter createFunctionTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.TypeReference <em>Type Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.TypeReference
     * @generated
     */
    public Adapter createTypeReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.Expression
     * @generated
     */
    public Adapter createExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.BoolLiteral <em>Bool Literal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.BoolLiteral
     * @generated
     */
    public Adapter createBoolLiteralAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.IntNumber <em>Int Number</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.IntNumber
     * @generated
     */
    public Adapter createIntNumberAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.RealNumber <em>Real Number</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.RealNumber
     * @generated
     */
    public Adapter createRealNumberAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.StringLiteral <em>String Literal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.StringLiteral
     * @generated
     */
    public Adapter createStringLiteralAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.TupleExpression <em>Tuple Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleExpression
     * @generated
     */
    public Adapter createTupleExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ListExpression <em>List Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ListExpression
     * @generated
     */
    public Adapter createListExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.SetExpression <em>Set Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.SetExpression
     * @generated
     */
    public Adapter createSetExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.MatrixExpression <em>Matrix Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixExpression
     * @generated
     */
    public Adapter createMatrixExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.MatrixRow <em>Matrix Row</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixRow
     * @generated
     */
    public Adapter createMatrixRowAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.DictionaryExpression <em>Dictionary Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.DictionaryExpression
     * @generated
     */
    public Adapter createDictionaryExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.DictionaryPair <em>Dictionary Pair</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.DictionaryPair
     * @generated
     */
    public Adapter createDictionaryPairAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.VariableReference <em>Variable Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableReference
     * @generated
     */
    public Adapter createVariableReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ConstantReference <em>Constant Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ConstantReference
     * @generated
     */
    public Adapter createConstantReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.TimeLiteral <em>Time Literal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.TimeLiteral
     * @generated
     */
    public Adapter createTimeLiteralAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.UnaryExpression <em>Unary Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.UnaryExpression
     * @generated
     */
    public Adapter createUnaryExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression <em>Binary Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryExpression
     * @generated
     */
    public Adapter createBinaryExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.CallExpression <em>Call Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.CallExpression
     * @generated
     */
    public Adapter createCallExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.FunctionReference <em>Function Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionReference
     * @generated
     */
    public Adapter createFunctionReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.BaseFunctionReference <em>Base Function Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.BaseFunctionReference
     * @generated
     */
    public Adapter createBaseFunctionReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference <em>Std Lib Function Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference
     * @generated
     */
    public Adapter createStdLibFunctionReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression <em>Slice Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.SliceExpression
     * @generated
     */
    public Adapter createSliceExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.FieldReference <em>Field Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.FieldReference
     * @generated
     */
    public Adapter createFieldReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.Statement <em>Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.Statement
     * @generated
     */
    public Adapter createStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.BreakStatement <em>Break Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.BreakStatement
     * @generated
     */
    public Adapter createBreakStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ContinueStatement <em>Continue Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ContinueStatement
     * @generated
     */
    public Adapter createContinueStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.PassStatement <em>Pass Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.PassStatement
     * @generated
     */
    public Adapter createPassStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ExitStatement <em>Exit Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ExitStatement
     * @generated
     */
    public Adapter createExitStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ReturnStatement <em>Return Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ReturnStatement
     * @generated
     */
    public Adapter createReturnStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.DelayStatement <em>Delay Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.DelayStatement
     * @generated
     */
    public Adapter createDelayStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.WhileStatement <em>While Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.WhileStatement
     * @generated
     */
    public Adapter createWhileStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.IfStatement <em>If Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.IfStatement
     * @generated
     */
    public Adapter createIfStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.IfCase <em>If Case</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.IfCase
     * @generated
     */
    public Adapter createIfCaseAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.WriteStatement <em>Write Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.WriteStatement
     * @generated
     */
    public Adapter createWriteStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.AssignmentStatement <em>Assignment Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.AssignmentStatement
     * @generated
     */
    public Adapter createAssignmentStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.CommunicationStatement <em>Communication Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.CommunicationStatement
     * @generated
     */
    public Adapter createCommunicationStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ForStatement <em>For Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ForStatement
     * @generated
     */
    public Adapter createForStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.CreateCase <em>Create Case</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.CreateCase
     * @generated
     */
    public Adapter createCreateCaseAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.RunStatement <em>Run Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.RunStatement
     * @generated
     */
    public Adapter createRunStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.SelectStatement <em>Select Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.SelectStatement
     * @generated
     */
    public Adapter createSelectStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.SelectCase <em>Select Case</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.SelectCase
     * @generated
     */
    public Adapter createSelectCaseAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase <em>Iterated Create Case</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase
     * @generated
     */
    public Adapter createIteratedCreateCaseAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase <em>Iterated Select Case</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase
     * @generated
     */
    public Adapter createIteratedSelectCaseAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.Specification <em>Specification</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.Specification
     * @generated
     */
    public Adapter createSpecificationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.Declaration <em>Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.Declaration
     * @generated
     */
    public Adapter createDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.TypeDeclaration <em>Type Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.TypeDeclaration
     * @generated
     */
    public Adapter createTypeDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration <em>Constant Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration
     * @generated
     */
    public Adapter createConstantDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration <em>Process Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration
     * @generated
     */
    public Adapter createProcessDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration <em>Function Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration
     * @generated
     */
    public Adapter createFunctionDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ModelDeclaration <em>Model Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ModelDeclaration
     * @generated
     */
    public Adapter createModelDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration <em>Variable Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableDeclaration
     * @generated
     */
    public Adapter createVariableDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration <em>Behaviour Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration
     * @generated
     */
    public Adapter createBehaviourDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ReceiveStatement <em>Receive Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ReceiveStatement
     * @generated
     */
    public Adapter createReceiveStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.SendStatement <em>Send Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.SendStatement
     * @generated
     */
    public Adapter createSendStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.EnumValueReference <em>Enum Value Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumValueReference
     * @generated
     */
    public Adapter createEnumValueReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ReadCallExpression <em>Read Call Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ReadCallExpression
     * @generated
     */
    public Adapter createReadCallExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.Unwind <em>Unwind</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.Unwind
     * @generated
     */
    public Adapter createUnwindAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ProcessInstance <em>Process Instance</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessInstance
     * @generated
     */
    public Adapter createProcessInstanceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ProcessType <em>Process Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessType
     * @generated
     */
    public Adapter createProcessTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ProcessReference <em>Process Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessReference
     * @generated
     */
    public Adapter createProcessReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.UnresolvedReference <em>Unresolved Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.UnresolvedReference
     * @generated
     */
    public Adapter createUnresolvedReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.UnresolvedType <em>Unresolved Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.UnresolvedType
     * @generated
     */
    public Adapter createUnresolvedTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.TimerType <em>Timer Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.TimerType
     * @generated
     */
    public Adapter createTimerTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.EnumDeclaration <em>Enum Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumDeclaration
     * @generated
     */
    public Adapter createEnumDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.EnumValue <em>Enum Value</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumValue
     * @generated
     */
    public Adapter createEnumValueAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ChannelExpression <em>Channel Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelExpression
     * @generated
     */
    public Adapter createChannelExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.CastExpression <em>Cast Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.CastExpression
     * @generated
     */
    public Adapter createCastExpressionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.CloseStatement <em>Close Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.CloseStatement
     * @generated
     */
    public Adapter createCloseStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.FinishStatement <em>Finish Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.FinishStatement
     * @generated
     */
    public Adapter createFinishStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ModelReference <em>Model Reference</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ModelReference
     * @generated
     */
    public Adapter createModelReferenceAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.XperDeclaration <em>Xper Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.XperDeclaration
     * @generated
     */
    public Adapter createXperDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ComputeDeclaration <em>Compute Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ComputeDeclaration
     * @generated
     */
    public Adapter createComputeDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ComputeType <em>Compute Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ComputeType
     * @generated
     */
    public Adapter createComputeTypeAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.chi.metamodel.chi.ModelType <em>Model Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.chi.metamodel.chi.ModelType
     * @generated
     */
    public Adapter createModelTypeAdapter()
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

} //ChiAdapterFactory
