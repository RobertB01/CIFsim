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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.chi.metamodel.chi.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChiFactoryImpl extends EFactoryImpl implements ChiFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ChiFactory init()
    {
        try
        {
            ChiFactory theChiFactory = (ChiFactory)EPackage.Registry.INSTANCE.getEFactory(ChiPackage.eNS_URI);
            if (theChiFactory != null)
            {
                return theChiFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ChiFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ChiFactoryImpl()
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
            case ChiPackage.VOID_TYPE: return createVoidType();
            case ChiPackage.BOOL_TYPE: return createBoolType();
            case ChiPackage.INSTANCE_TYPE: return createInstanceType();
            case ChiPackage.INT_TYPE: return createIntType();
            case ChiPackage.STRING_TYPE: return createStringType();
            case ChiPackage.REAL_TYPE: return createRealType();
            case ChiPackage.FILE_TYPE: return createFileType();
            case ChiPackage.SET_TYPE: return createSetType();
            case ChiPackage.LIST_TYPE: return createListType();
            case ChiPackage.DICT_TYPE: return createDictType();
            case ChiPackage.MATRIX_TYPE: return createMatrixType();
            case ChiPackage.TUPLE_TYPE: return createTupleType();
            case ChiPackage.TUPLE_FIELD: return createTupleField();
            case ChiPackage.DISTRIBUTION_TYPE: return createDistributionType();
            case ChiPackage.ENUM_TYPE_REFERENCE: return createEnumTypeReference();
            case ChiPackage.CHANNEL_TYPE: return createChannelType();
            case ChiPackage.FUNCTION_TYPE: return createFunctionType();
            case ChiPackage.TYPE_REFERENCE: return createTypeReference();
            case ChiPackage.BOOL_LITERAL: return createBoolLiteral();
            case ChiPackage.INT_NUMBER: return createIntNumber();
            case ChiPackage.REAL_NUMBER: return createRealNumber();
            case ChiPackage.STRING_LITERAL: return createStringLiteral();
            case ChiPackage.TUPLE_EXPRESSION: return createTupleExpression();
            case ChiPackage.LIST_EXPRESSION: return createListExpression();
            case ChiPackage.SET_EXPRESSION: return createSetExpression();
            case ChiPackage.MATRIX_EXPRESSION: return createMatrixExpression();
            case ChiPackage.MATRIX_ROW: return createMatrixRow();
            case ChiPackage.DICTIONARY_EXPRESSION: return createDictionaryExpression();
            case ChiPackage.DICTIONARY_PAIR: return createDictionaryPair();
            case ChiPackage.VARIABLE_REFERENCE: return createVariableReference();
            case ChiPackage.CONSTANT_REFERENCE: return createConstantReference();
            case ChiPackage.TIME_LITERAL: return createTimeLiteral();
            case ChiPackage.UNARY_EXPRESSION: return createUnaryExpression();
            case ChiPackage.BINARY_EXPRESSION: return createBinaryExpression();
            case ChiPackage.CALL_EXPRESSION: return createCallExpression();
            case ChiPackage.FUNCTION_REFERENCE: return createFunctionReference();
            case ChiPackage.STD_LIB_FUNCTION_REFERENCE: return createStdLibFunctionReference();
            case ChiPackage.SLICE_EXPRESSION: return createSliceExpression();
            case ChiPackage.FIELD_REFERENCE: return createFieldReference();
            case ChiPackage.BREAK_STATEMENT: return createBreakStatement();
            case ChiPackage.CONTINUE_STATEMENT: return createContinueStatement();
            case ChiPackage.PASS_STATEMENT: return createPassStatement();
            case ChiPackage.EXIT_STATEMENT: return createExitStatement();
            case ChiPackage.RETURN_STATEMENT: return createReturnStatement();
            case ChiPackage.DELAY_STATEMENT: return createDelayStatement();
            case ChiPackage.WHILE_STATEMENT: return createWhileStatement();
            case ChiPackage.IF_STATEMENT: return createIfStatement();
            case ChiPackage.IF_CASE: return createIfCase();
            case ChiPackage.WRITE_STATEMENT: return createWriteStatement();
            case ChiPackage.ASSIGNMENT_STATEMENT: return createAssignmentStatement();
            case ChiPackage.FOR_STATEMENT: return createForStatement();
            case ChiPackage.RUN_STATEMENT: return createRunStatement();
            case ChiPackage.SELECT_STATEMENT: return createSelectStatement();
            case ChiPackage.SELECT_CASE: return createSelectCase();
            case ChiPackage.ITERATED_CREATE_CASE: return createIteratedCreateCase();
            case ChiPackage.ITERATED_SELECT_CASE: return createIteratedSelectCase();
            case ChiPackage.SPECIFICATION: return createSpecification();
            case ChiPackage.TYPE_DECLARATION: return createTypeDeclaration();
            case ChiPackage.CONSTANT_DECLARATION: return createConstantDeclaration();
            case ChiPackage.PROCESS_DECLARATION: return createProcessDeclaration();
            case ChiPackage.FUNCTION_DECLARATION: return createFunctionDeclaration();
            case ChiPackage.MODEL_DECLARATION: return createModelDeclaration();
            case ChiPackage.VARIABLE_DECLARATION: return createVariableDeclaration();
            case ChiPackage.RECEIVE_STATEMENT: return createReceiveStatement();
            case ChiPackage.SEND_STATEMENT: return createSendStatement();
            case ChiPackage.ENUM_VALUE_REFERENCE: return createEnumValueReference();
            case ChiPackage.READ_CALL_EXPRESSION: return createReadCallExpression();
            case ChiPackage.UNWIND: return createUnwind();
            case ChiPackage.PROCESS_INSTANCE: return createProcessInstance();
            case ChiPackage.PROCESS_TYPE: return createProcessType();
            case ChiPackage.PROCESS_REFERENCE: return createProcessReference();
            case ChiPackage.UNRESOLVED_REFERENCE: return createUnresolvedReference();
            case ChiPackage.UNRESOLVED_TYPE: return createUnresolvedType();
            case ChiPackage.TIMER_TYPE: return createTimerType();
            case ChiPackage.ENUM_DECLARATION: return createEnumDeclaration();
            case ChiPackage.ENUM_VALUE: return createEnumValue();
            case ChiPackage.CHANNEL_EXPRESSION: return createChannelExpression();
            case ChiPackage.CAST_EXPRESSION: return createCastExpression();
            case ChiPackage.CLOSE_STATEMENT: return createCloseStatement();
            case ChiPackage.FINISH_STATEMENT: return createFinishStatement();
            case ChiPackage.MODEL_REFERENCE: return createModelReference();
            case ChiPackage.XPER_DECLARATION: return createXperDeclaration();
            case ChiPackage.MODEL_TYPE: return createModelType();
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
            case ChiPackage.CHANNEL_OPS:
                return createChannelOpsFromString(eDataType, initialValue);
            case ChiPackage.UNARY_OPERATORS:
                return createUnaryOperatorsFromString(eDataType, initialValue);
            case ChiPackage.BINARY_OPERATORS:
                return createBinaryOperatorsFromString(eDataType, initialValue);
            case ChiPackage.STD_LIB_FUNCTIONS:
                return createStdLibFunctionsFromString(eDataType, initialValue);
            case ChiPackage.CHI_IDENTIFIER:
                return createChiIdentifierFromString(eDataType, initialValue);
            case ChiPackage.CHI_REAL_NUMBER:
                return createChiRealNumberFromString(eDataType, initialValue);
            case ChiPackage.CHI_NUMBER:
                return createChiNumberFromString(eDataType, initialValue);
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
            case ChiPackage.CHANNEL_OPS:
                return convertChannelOpsToString(eDataType, instanceValue);
            case ChiPackage.UNARY_OPERATORS:
                return convertUnaryOperatorsToString(eDataType, instanceValue);
            case ChiPackage.BINARY_OPERATORS:
                return convertBinaryOperatorsToString(eDataType, instanceValue);
            case ChiPackage.STD_LIB_FUNCTIONS:
                return convertStdLibFunctionsToString(eDataType, instanceValue);
            case ChiPackage.CHI_IDENTIFIER:
                return convertChiIdentifierToString(eDataType, instanceValue);
            case ChiPackage.CHI_REAL_NUMBER:
                return convertChiRealNumberToString(eDataType, instanceValue);
            case ChiPackage.CHI_NUMBER:
                return convertChiNumberToString(eDataType, instanceValue);
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
    public VoidType createVoidType()
    {
        VoidTypeImpl voidType = new VoidTypeImpl();
        return voidType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public BoolType createBoolType()
    {
        BoolTypeImpl boolType = new BoolTypeImpl();
        return boolType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public InstanceType createInstanceType()
    {
        InstanceTypeImpl instanceType = new InstanceTypeImpl();
        return instanceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IntType createIntType()
    {
        IntTypeImpl intType = new IntTypeImpl();
        return intType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public StringType createStringType()
    {
        StringTypeImpl stringType = new StringTypeImpl();
        return stringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public RealType createRealType()
    {
        RealTypeImpl realType = new RealTypeImpl();
        return realType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FileType createFileType()
    {
        FileTypeImpl fileType = new FileTypeImpl();
        return fileType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SetType createSetType()
    {
        SetTypeImpl setType = new SetTypeImpl();
        return setType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ListType createListType()
    {
        ListTypeImpl listType = new ListTypeImpl();
        return listType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DictType createDictType()
    {
        DictTypeImpl dictType = new DictTypeImpl();
        return dictType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public MatrixType createMatrixType()
    {
        MatrixTypeImpl matrixType = new MatrixTypeImpl();
        return matrixType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TupleType createTupleType()
    {
        TupleTypeImpl tupleType = new TupleTypeImpl();
        return tupleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TupleField createTupleField()
    {
        TupleFieldImpl tupleField = new TupleFieldImpl();
        return tupleField;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DistributionType createDistributionType()
    {
        DistributionTypeImpl distributionType = new DistributionTypeImpl();
        return distributionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EnumTypeReference createEnumTypeReference()
    {
        EnumTypeReferenceImpl enumTypeReference = new EnumTypeReferenceImpl();
        return enumTypeReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ChannelType createChannelType()
    {
        ChannelTypeImpl channelType = new ChannelTypeImpl();
        return channelType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FunctionType createFunctionType()
    {
        FunctionTypeImpl functionType = new FunctionTypeImpl();
        return functionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypeReference createTypeReference()
    {
        TypeReferenceImpl typeReference = new TypeReferenceImpl();
        return typeReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public BoolLiteral createBoolLiteral()
    {
        BoolLiteralImpl boolLiteral = new BoolLiteralImpl();
        return boolLiteral;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IntNumber createIntNumber()
    {
        IntNumberImpl intNumber = new IntNumberImpl();
        return intNumber;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public RealNumber createRealNumber()
    {
        RealNumberImpl realNumber = new RealNumberImpl();
        return realNumber;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public StringLiteral createStringLiteral()
    {
        StringLiteralImpl stringLiteral = new StringLiteralImpl();
        return stringLiteral;
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
    public MatrixExpression createMatrixExpression()
    {
        MatrixExpressionImpl matrixExpression = new MatrixExpressionImpl();
        return matrixExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public MatrixRow createMatrixRow()
    {
        MatrixRowImpl matrixRow = new MatrixRowImpl();
        return matrixRow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DictionaryExpression createDictionaryExpression()
    {
        DictionaryExpressionImpl dictionaryExpression = new DictionaryExpressionImpl();
        return dictionaryExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DictionaryPair createDictionaryPair()
    {
        DictionaryPairImpl dictionaryPair = new DictionaryPairImpl();
        return dictionaryPair;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VariableReference createVariableReference()
    {
        VariableReferenceImpl variableReference = new VariableReferenceImpl();
        return variableReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstantReference createConstantReference()
    {
        ConstantReferenceImpl constantReference = new ConstantReferenceImpl();
        return constantReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TimeLiteral createTimeLiteral()
    {
        TimeLiteralImpl timeLiteral = new TimeLiteralImpl();
        return timeLiteral;
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
    public CallExpression createCallExpression()
    {
        CallExpressionImpl callExpression = new CallExpressionImpl();
        return callExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FunctionReference createFunctionReference()
    {
        FunctionReferenceImpl functionReference = new FunctionReferenceImpl();
        return functionReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public StdLibFunctionReference createStdLibFunctionReference()
    {
        StdLibFunctionReferenceImpl stdLibFunctionReference = new StdLibFunctionReferenceImpl();
        return stdLibFunctionReference;
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
    public FieldReference createFieldReference()
    {
        FieldReferenceImpl fieldReference = new FieldReferenceImpl();
        return fieldReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public BreakStatement createBreakStatement()
    {
        BreakStatementImpl breakStatement = new BreakStatementImpl();
        return breakStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ContinueStatement createContinueStatement()
    {
        ContinueStatementImpl continueStatement = new ContinueStatementImpl();
        return continueStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public PassStatement createPassStatement()
    {
        PassStatementImpl passStatement = new PassStatementImpl();
        return passStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ExitStatement createExitStatement()
    {
        ExitStatementImpl exitStatement = new ExitStatementImpl();
        return exitStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ReturnStatement createReturnStatement()
    {
        ReturnStatementImpl returnStatement = new ReturnStatementImpl();
        return returnStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DelayStatement createDelayStatement()
    {
        DelayStatementImpl delayStatement = new DelayStatementImpl();
        return delayStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public WhileStatement createWhileStatement()
    {
        WhileStatementImpl whileStatement = new WhileStatementImpl();
        return whileStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IfStatement createIfStatement()
    {
        IfStatementImpl ifStatement = new IfStatementImpl();
        return ifStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IfCase createIfCase()
    {
        IfCaseImpl ifCase = new IfCaseImpl();
        return ifCase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public WriteStatement createWriteStatement()
    {
        WriteStatementImpl writeStatement = new WriteStatementImpl();
        return writeStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AssignmentStatement createAssignmentStatement()
    {
        AssignmentStatementImpl assignmentStatement = new AssignmentStatementImpl();
        return assignmentStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ForStatement createForStatement()
    {
        ForStatementImpl forStatement = new ForStatementImpl();
        return forStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public RunStatement createRunStatement()
    {
        RunStatementImpl runStatement = new RunStatementImpl();
        return runStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SelectStatement createSelectStatement()
    {
        SelectStatementImpl selectStatement = new SelectStatementImpl();
        return selectStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SelectCase createSelectCase()
    {
        SelectCaseImpl selectCase = new SelectCaseImpl();
        return selectCase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IteratedCreateCase createIteratedCreateCase()
    {
        IteratedCreateCaseImpl iteratedCreateCase = new IteratedCreateCaseImpl();
        return iteratedCreateCase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IteratedSelectCase createIteratedSelectCase()
    {
        IteratedSelectCaseImpl iteratedSelectCase = new IteratedSelectCaseImpl();
        return iteratedSelectCase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Specification createSpecification()
    {
        SpecificationImpl specification = new SpecificationImpl();
        return specification;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypeDeclaration createTypeDeclaration()
    {
        TypeDeclarationImpl typeDeclaration = new TypeDeclarationImpl();
        return typeDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstantDeclaration createConstantDeclaration()
    {
        ConstantDeclarationImpl constantDeclaration = new ConstantDeclarationImpl();
        return constantDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ProcessDeclaration createProcessDeclaration()
    {
        ProcessDeclarationImpl processDeclaration = new ProcessDeclarationImpl();
        return processDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FunctionDeclaration createFunctionDeclaration()
    {
        FunctionDeclarationImpl functionDeclaration = new FunctionDeclarationImpl();
        return functionDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ModelDeclaration createModelDeclaration()
    {
        ModelDeclarationImpl modelDeclaration = new ModelDeclarationImpl();
        return modelDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VariableDeclaration createVariableDeclaration()
    {
        VariableDeclarationImpl variableDeclaration = new VariableDeclarationImpl();
        return variableDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ReceiveStatement createReceiveStatement()
    {
        ReceiveStatementImpl receiveStatement = new ReceiveStatementImpl();
        return receiveStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SendStatement createSendStatement()
    {
        SendStatementImpl sendStatement = new SendStatementImpl();
        return sendStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EnumValueReference createEnumValueReference()
    {
        EnumValueReferenceImpl enumValueReference = new EnumValueReferenceImpl();
        return enumValueReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ReadCallExpression createReadCallExpression()
    {
        ReadCallExpressionImpl readCallExpression = new ReadCallExpressionImpl();
        return readCallExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Unwind createUnwind()
    {
        UnwindImpl unwind = new UnwindImpl();
        return unwind;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ProcessInstance createProcessInstance()
    {
        ProcessInstanceImpl processInstance = new ProcessInstanceImpl();
        return processInstance;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ProcessType createProcessType()
    {
        ProcessTypeImpl processType = new ProcessTypeImpl();
        return processType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ProcessReference createProcessReference()
    {
        ProcessReferenceImpl processReference = new ProcessReferenceImpl();
        return processReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public UnresolvedReference createUnresolvedReference()
    {
        UnresolvedReferenceImpl unresolvedReference = new UnresolvedReferenceImpl();
        return unresolvedReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public UnresolvedType createUnresolvedType()
    {
        UnresolvedTypeImpl unresolvedType = new UnresolvedTypeImpl();
        return unresolvedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TimerType createTimerType()
    {
        TimerTypeImpl timerType = new TimerTypeImpl();
        return timerType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EnumDeclaration createEnumDeclaration()
    {
        EnumDeclarationImpl enumDeclaration = new EnumDeclarationImpl();
        return enumDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EnumValue createEnumValue()
    {
        EnumValueImpl enumValue = new EnumValueImpl();
        return enumValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ChannelExpression createChannelExpression()
    {
        ChannelExpressionImpl channelExpression = new ChannelExpressionImpl();
        return channelExpression;
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
    public CloseStatement createCloseStatement()
    {
        CloseStatementImpl closeStatement = new CloseStatementImpl();
        return closeStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FinishStatement createFinishStatement()
    {
        FinishStatementImpl finishStatement = new FinishStatementImpl();
        return finishStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ModelReference createModelReference()
    {
        ModelReferenceImpl modelReference = new ModelReferenceImpl();
        return modelReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public XperDeclaration createXperDeclaration()
    {
        XperDeclarationImpl xperDeclaration = new XperDeclarationImpl();
        return xperDeclaration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ModelType createModelType()
    {
        ModelTypeImpl modelType = new ModelTypeImpl();
        return modelType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ChannelOps createChannelOpsFromString(EDataType eDataType, String initialValue)
    {
        ChannelOps result = ChannelOps.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertChannelOpsToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UnaryOperators createUnaryOperatorsFromString(EDataType eDataType, String initialValue)
    {
        UnaryOperators result = UnaryOperators.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertUnaryOperatorsToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryOperators createBinaryOperatorsFromString(EDataType eDataType, String initialValue)
    {
        BinaryOperators result = BinaryOperators.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertBinaryOperatorsToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StdLibFunctions createStdLibFunctionsFromString(EDataType eDataType, String initialValue)
    {
        StdLibFunctions result = StdLibFunctions.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStdLibFunctionsToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createChiIdentifierFromString(EDataType eDataType, String initialValue)
    {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertChiIdentifierToString(EDataType eDataType, Object instanceValue)
    {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createChiRealNumberFromString(EDataType eDataType, String initialValue)
    {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertChiRealNumberToString(EDataType eDataType, Object instanceValue)
    {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createChiNumberFromString(EDataType eDataType, String initialValue)
    {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertChiNumberToString(EDataType eDataType, Object instanceValue)
    {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ChiPackage getChiPackage()
    {
        return (ChiPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ChiPackage getPackage()
    {
        return ChiPackage.eINSTANCE;
    }

} //ChiFactoryImpl
