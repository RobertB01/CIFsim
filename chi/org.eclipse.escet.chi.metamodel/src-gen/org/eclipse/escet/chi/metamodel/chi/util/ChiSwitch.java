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
package org.eclipse.escet.chi.metamodel.chi.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.chi.metamodel.chi.*;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage
 * @generated
 */
public class ChiSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ChiPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ChiSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = ChiPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage)
    {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject)
    {
        switch (classifierID)
        {
            case ChiPackage.TYPE:
            {
                Type type = (Type)theEObject;
                T result = caseType(type);
                if (result == null) result = casePositionObject(type);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.VOID_TYPE:
            {
                VoidType voidType = (VoidType)theEObject;
                T result = caseVoidType(voidType);
                if (result == null) result = caseType(voidType);
                if (result == null) result = casePositionObject(voidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.BOOL_TYPE:
            {
                BoolType boolType = (BoolType)theEObject;
                T result = caseBoolType(boolType);
                if (result == null) result = caseType(boolType);
                if (result == null) result = casePositionObject(boolType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.INSTANCE_TYPE:
            {
                InstanceType instanceType = (InstanceType)theEObject;
                T result = caseInstanceType(instanceType);
                if (result == null) result = caseType(instanceType);
                if (result == null) result = casePositionObject(instanceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.INT_TYPE:
            {
                IntType intType = (IntType)theEObject;
                T result = caseIntType(intType);
                if (result == null) result = caseType(intType);
                if (result == null) result = casePositionObject(intType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.STRING_TYPE:
            {
                StringType stringType = (StringType)theEObject;
                T result = caseStringType(stringType);
                if (result == null) result = caseType(stringType);
                if (result == null) result = casePositionObject(stringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.REAL_TYPE:
            {
                RealType realType = (RealType)theEObject;
                T result = caseRealType(realType);
                if (result == null) result = caseType(realType);
                if (result == null) result = casePositionObject(realType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.FILE_TYPE:
            {
                FileType fileType = (FileType)theEObject;
                T result = caseFileType(fileType);
                if (result == null) result = caseType(fileType);
                if (result == null) result = casePositionObject(fileType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.SET_TYPE:
            {
                SetType setType = (SetType)theEObject;
                T result = caseSetType(setType);
                if (result == null) result = caseType(setType);
                if (result == null) result = casePositionObject(setType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.LIST_TYPE:
            {
                ListType listType = (ListType)theEObject;
                T result = caseListType(listType);
                if (result == null) result = caseType(listType);
                if (result == null) result = casePositionObject(listType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.DICT_TYPE:
            {
                DictType dictType = (DictType)theEObject;
                T result = caseDictType(dictType);
                if (result == null) result = caseType(dictType);
                if (result == null) result = casePositionObject(dictType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.MATRIX_TYPE:
            {
                MatrixType matrixType = (MatrixType)theEObject;
                T result = caseMatrixType(matrixType);
                if (result == null) result = caseType(matrixType);
                if (result == null) result = casePositionObject(matrixType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.TUPLE_TYPE:
            {
                TupleType tupleType = (TupleType)theEObject;
                T result = caseTupleType(tupleType);
                if (result == null) result = caseType(tupleType);
                if (result == null) result = casePositionObject(tupleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.TUPLE_FIELD:
            {
                TupleField tupleField = (TupleField)theEObject;
                T result = caseTupleField(tupleField);
                if (result == null) result = casePositionObject(tupleField);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.DISTRIBUTION_TYPE:
            {
                DistributionType distributionType = (DistributionType)theEObject;
                T result = caseDistributionType(distributionType);
                if (result == null) result = caseType(distributionType);
                if (result == null) result = casePositionObject(distributionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.ENUM_TYPE_REFERENCE:
            {
                EnumTypeReference enumTypeReference = (EnumTypeReference)theEObject;
                T result = caseEnumTypeReference(enumTypeReference);
                if (result == null) result = caseType(enumTypeReference);
                if (result == null) result = casePositionObject(enumTypeReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CHANNEL_TYPE:
            {
                ChannelType channelType = (ChannelType)theEObject;
                T result = caseChannelType(channelType);
                if (result == null) result = caseType(channelType);
                if (result == null) result = casePositionObject(channelType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.FUNCTION_TYPE:
            {
                FunctionType functionType = (FunctionType)theEObject;
                T result = caseFunctionType(functionType);
                if (result == null) result = caseType(functionType);
                if (result == null) result = casePositionObject(functionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.TYPE_REFERENCE:
            {
                TypeReference typeReference = (TypeReference)theEObject;
                T result = caseTypeReference(typeReference);
                if (result == null) result = caseType(typeReference);
                if (result == null) result = casePositionObject(typeReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.EXPRESSION:
            {
                Expression expression = (Expression)theEObject;
                T result = caseExpression(expression);
                if (result == null) result = casePositionObject(expression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.BOOL_LITERAL:
            {
                BoolLiteral boolLiteral = (BoolLiteral)theEObject;
                T result = caseBoolLiteral(boolLiteral);
                if (result == null) result = caseExpression(boolLiteral);
                if (result == null) result = casePositionObject(boolLiteral);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.INT_NUMBER:
            {
                IntNumber intNumber = (IntNumber)theEObject;
                T result = caseIntNumber(intNumber);
                if (result == null) result = caseExpression(intNumber);
                if (result == null) result = casePositionObject(intNumber);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.REAL_NUMBER:
            {
                RealNumber realNumber = (RealNumber)theEObject;
                T result = caseRealNumber(realNumber);
                if (result == null) result = caseExpression(realNumber);
                if (result == null) result = casePositionObject(realNumber);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.STRING_LITERAL:
            {
                StringLiteral stringLiteral = (StringLiteral)theEObject;
                T result = caseStringLiteral(stringLiteral);
                if (result == null) result = caseExpression(stringLiteral);
                if (result == null) result = casePositionObject(stringLiteral);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.TUPLE_EXPRESSION:
            {
                TupleExpression tupleExpression = (TupleExpression)theEObject;
                T result = caseTupleExpression(tupleExpression);
                if (result == null) result = caseExpression(tupleExpression);
                if (result == null) result = casePositionObject(tupleExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.LIST_EXPRESSION:
            {
                ListExpression listExpression = (ListExpression)theEObject;
                T result = caseListExpression(listExpression);
                if (result == null) result = caseExpression(listExpression);
                if (result == null) result = casePositionObject(listExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.SET_EXPRESSION:
            {
                SetExpression setExpression = (SetExpression)theEObject;
                T result = caseSetExpression(setExpression);
                if (result == null) result = caseExpression(setExpression);
                if (result == null) result = casePositionObject(setExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.MATRIX_EXPRESSION:
            {
                MatrixExpression matrixExpression = (MatrixExpression)theEObject;
                T result = caseMatrixExpression(matrixExpression);
                if (result == null) result = caseExpression(matrixExpression);
                if (result == null) result = casePositionObject(matrixExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.MATRIX_ROW:
            {
                MatrixRow matrixRow = (MatrixRow)theEObject;
                T result = caseMatrixRow(matrixRow);
                if (result == null) result = casePositionObject(matrixRow);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.DICTIONARY_EXPRESSION:
            {
                DictionaryExpression dictionaryExpression = (DictionaryExpression)theEObject;
                T result = caseDictionaryExpression(dictionaryExpression);
                if (result == null) result = caseExpression(dictionaryExpression);
                if (result == null) result = casePositionObject(dictionaryExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.DICTIONARY_PAIR:
            {
                DictionaryPair dictionaryPair = (DictionaryPair)theEObject;
                T result = caseDictionaryPair(dictionaryPair);
                if (result == null) result = casePositionObject(dictionaryPair);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.VARIABLE_REFERENCE:
            {
                VariableReference variableReference = (VariableReference)theEObject;
                T result = caseVariableReference(variableReference);
                if (result == null) result = caseExpression(variableReference);
                if (result == null) result = casePositionObject(variableReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CONSTANT_REFERENCE:
            {
                ConstantReference constantReference = (ConstantReference)theEObject;
                T result = caseConstantReference(constantReference);
                if (result == null) result = caseExpression(constantReference);
                if (result == null) result = casePositionObject(constantReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.TIME_LITERAL:
            {
                TimeLiteral timeLiteral = (TimeLiteral)theEObject;
                T result = caseTimeLiteral(timeLiteral);
                if (result == null) result = caseExpression(timeLiteral);
                if (result == null) result = casePositionObject(timeLiteral);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.UNARY_EXPRESSION:
            {
                UnaryExpression unaryExpression = (UnaryExpression)theEObject;
                T result = caseUnaryExpression(unaryExpression);
                if (result == null) result = caseExpression(unaryExpression);
                if (result == null) result = casePositionObject(unaryExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.BINARY_EXPRESSION:
            {
                BinaryExpression binaryExpression = (BinaryExpression)theEObject;
                T result = caseBinaryExpression(binaryExpression);
                if (result == null) result = caseExpression(binaryExpression);
                if (result == null) result = casePositionObject(binaryExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CALL_EXPRESSION:
            {
                CallExpression callExpression = (CallExpression)theEObject;
                T result = caseCallExpression(callExpression);
                if (result == null) result = caseExpression(callExpression);
                if (result == null) result = casePositionObject(callExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.FUNCTION_REFERENCE:
            {
                FunctionReference functionReference = (FunctionReference)theEObject;
                T result = caseFunctionReference(functionReference);
                if (result == null) result = caseBaseFunctionReference(functionReference);
                if (result == null) result = caseExpression(functionReference);
                if (result == null) result = casePositionObject(functionReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.BASE_FUNCTION_REFERENCE:
            {
                BaseFunctionReference baseFunctionReference = (BaseFunctionReference)theEObject;
                T result = caseBaseFunctionReference(baseFunctionReference);
                if (result == null) result = caseExpression(baseFunctionReference);
                if (result == null) result = casePositionObject(baseFunctionReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.STD_LIB_FUNCTION_REFERENCE:
            {
                StdLibFunctionReference stdLibFunctionReference = (StdLibFunctionReference)theEObject;
                T result = caseStdLibFunctionReference(stdLibFunctionReference);
                if (result == null) result = caseBaseFunctionReference(stdLibFunctionReference);
                if (result == null) result = caseExpression(stdLibFunctionReference);
                if (result == null) result = casePositionObject(stdLibFunctionReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.SLICE_EXPRESSION:
            {
                SliceExpression sliceExpression = (SliceExpression)theEObject;
                T result = caseSliceExpression(sliceExpression);
                if (result == null) result = caseExpression(sliceExpression);
                if (result == null) result = casePositionObject(sliceExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.FIELD_REFERENCE:
            {
                FieldReference fieldReference = (FieldReference)theEObject;
                T result = caseFieldReference(fieldReference);
                if (result == null) result = caseExpression(fieldReference);
                if (result == null) result = casePositionObject(fieldReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.STATEMENT:
            {
                Statement statement = (Statement)theEObject;
                T result = caseStatement(statement);
                if (result == null) result = casePositionObject(statement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.BREAK_STATEMENT:
            {
                BreakStatement breakStatement = (BreakStatement)theEObject;
                T result = caseBreakStatement(breakStatement);
                if (result == null) result = caseStatement(breakStatement);
                if (result == null) result = casePositionObject(breakStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CONTINUE_STATEMENT:
            {
                ContinueStatement continueStatement = (ContinueStatement)theEObject;
                T result = caseContinueStatement(continueStatement);
                if (result == null) result = caseStatement(continueStatement);
                if (result == null) result = casePositionObject(continueStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.PASS_STATEMENT:
            {
                PassStatement passStatement = (PassStatement)theEObject;
                T result = casePassStatement(passStatement);
                if (result == null) result = caseStatement(passStatement);
                if (result == null) result = casePositionObject(passStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.EXIT_STATEMENT:
            {
                ExitStatement exitStatement = (ExitStatement)theEObject;
                T result = caseExitStatement(exitStatement);
                if (result == null) result = caseStatement(exitStatement);
                if (result == null) result = casePositionObject(exitStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.RETURN_STATEMENT:
            {
                ReturnStatement returnStatement = (ReturnStatement)theEObject;
                T result = caseReturnStatement(returnStatement);
                if (result == null) result = caseStatement(returnStatement);
                if (result == null) result = casePositionObject(returnStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.DELAY_STATEMENT:
            {
                DelayStatement delayStatement = (DelayStatement)theEObject;
                T result = caseDelayStatement(delayStatement);
                if (result == null) result = caseStatement(delayStatement);
                if (result == null) result = casePositionObject(delayStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.WHILE_STATEMENT:
            {
                WhileStatement whileStatement = (WhileStatement)theEObject;
                T result = caseWhileStatement(whileStatement);
                if (result == null) result = caseStatement(whileStatement);
                if (result == null) result = casePositionObject(whileStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.IF_STATEMENT:
            {
                IfStatement ifStatement = (IfStatement)theEObject;
                T result = caseIfStatement(ifStatement);
                if (result == null) result = caseStatement(ifStatement);
                if (result == null) result = casePositionObject(ifStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.IF_CASE:
            {
                IfCase ifCase = (IfCase)theEObject;
                T result = caseIfCase(ifCase);
                if (result == null) result = casePositionObject(ifCase);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.WRITE_STATEMENT:
            {
                WriteStatement writeStatement = (WriteStatement)theEObject;
                T result = caseWriteStatement(writeStatement);
                if (result == null) result = caseStatement(writeStatement);
                if (result == null) result = casePositionObject(writeStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.ASSIGNMENT_STATEMENT:
            {
                AssignmentStatement assignmentStatement = (AssignmentStatement)theEObject;
                T result = caseAssignmentStatement(assignmentStatement);
                if (result == null) result = caseStatement(assignmentStatement);
                if (result == null) result = casePositionObject(assignmentStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.COMMUNICATION_STATEMENT:
            {
                CommunicationStatement communicationStatement = (CommunicationStatement)theEObject;
                T result = caseCommunicationStatement(communicationStatement);
                if (result == null) result = caseStatement(communicationStatement);
                if (result == null) result = casePositionObject(communicationStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.FOR_STATEMENT:
            {
                ForStatement forStatement = (ForStatement)theEObject;
                T result = caseForStatement(forStatement);
                if (result == null) result = caseStatement(forStatement);
                if (result == null) result = casePositionObject(forStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CREATE_CASE:
            {
                CreateCase createCase = (CreateCase)theEObject;
                T result = caseCreateCase(createCase);
                if (result == null) result = casePositionObject(createCase);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.RUN_STATEMENT:
            {
                RunStatement runStatement = (RunStatement)theEObject;
                T result = caseRunStatement(runStatement);
                if (result == null) result = caseStatement(runStatement);
                if (result == null) result = casePositionObject(runStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.SELECT_STATEMENT:
            {
                SelectStatement selectStatement = (SelectStatement)theEObject;
                T result = caseSelectStatement(selectStatement);
                if (result == null) result = caseStatement(selectStatement);
                if (result == null) result = casePositionObject(selectStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.SELECT_CASE:
            {
                SelectCase selectCase = (SelectCase)theEObject;
                T result = caseSelectCase(selectCase);
                if (result == null) result = casePositionObject(selectCase);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.ITERATED_CREATE_CASE:
            {
                IteratedCreateCase iteratedCreateCase = (IteratedCreateCase)theEObject;
                T result = caseIteratedCreateCase(iteratedCreateCase);
                if (result == null) result = caseCreateCase(iteratedCreateCase);
                if (result == null) result = casePositionObject(iteratedCreateCase);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.ITERATED_SELECT_CASE:
            {
                IteratedSelectCase iteratedSelectCase = (IteratedSelectCase)theEObject;
                T result = caseIteratedSelectCase(iteratedSelectCase);
                if (result == null) result = caseSelectCase(iteratedSelectCase);
                if (result == null) result = casePositionObject(iteratedSelectCase);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.SPECIFICATION:
            {
                Specification specification = (Specification)theEObject;
                T result = caseSpecification(specification);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.DECLARATION:
            {
                Declaration declaration = (Declaration)theEObject;
                T result = caseDeclaration(declaration);
                if (result == null) result = casePositionObject(declaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.TYPE_DECLARATION:
            {
                TypeDeclaration typeDeclaration = (TypeDeclaration)theEObject;
                T result = caseTypeDeclaration(typeDeclaration);
                if (result == null) result = caseDeclaration(typeDeclaration);
                if (result == null) result = casePositionObject(typeDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CONSTANT_DECLARATION:
            {
                ConstantDeclaration constantDeclaration = (ConstantDeclaration)theEObject;
                T result = caseConstantDeclaration(constantDeclaration);
                if (result == null) result = caseDeclaration(constantDeclaration);
                if (result == null) result = casePositionObject(constantDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.PROCESS_DECLARATION:
            {
                ProcessDeclaration processDeclaration = (ProcessDeclaration)theEObject;
                T result = caseProcessDeclaration(processDeclaration);
                if (result == null) result = caseComputeDeclaration(processDeclaration);
                if (result == null) result = caseBehaviourDeclaration(processDeclaration);
                if (result == null) result = caseDeclaration(processDeclaration);
                if (result == null) result = casePositionObject(processDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.FUNCTION_DECLARATION:
            {
                FunctionDeclaration functionDeclaration = (FunctionDeclaration)theEObject;
                T result = caseFunctionDeclaration(functionDeclaration);
                if (result == null) result = caseBehaviourDeclaration(functionDeclaration);
                if (result == null) result = caseDeclaration(functionDeclaration);
                if (result == null) result = casePositionObject(functionDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.MODEL_DECLARATION:
            {
                ModelDeclaration modelDeclaration = (ModelDeclaration)theEObject;
                T result = caseModelDeclaration(modelDeclaration);
                if (result == null) result = caseComputeDeclaration(modelDeclaration);
                if (result == null) result = caseBehaviourDeclaration(modelDeclaration);
                if (result == null) result = caseDeclaration(modelDeclaration);
                if (result == null) result = casePositionObject(modelDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.VARIABLE_DECLARATION:
            {
                VariableDeclaration variableDeclaration = (VariableDeclaration)theEObject;
                T result = caseVariableDeclaration(variableDeclaration);
                if (result == null) result = casePositionObject(variableDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.BEHAVIOUR_DECLARATION:
            {
                BehaviourDeclaration behaviourDeclaration = (BehaviourDeclaration)theEObject;
                T result = caseBehaviourDeclaration(behaviourDeclaration);
                if (result == null) result = caseDeclaration(behaviourDeclaration);
                if (result == null) result = casePositionObject(behaviourDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.RECEIVE_STATEMENT:
            {
                ReceiveStatement receiveStatement = (ReceiveStatement)theEObject;
                T result = caseReceiveStatement(receiveStatement);
                if (result == null) result = caseCommunicationStatement(receiveStatement);
                if (result == null) result = caseStatement(receiveStatement);
                if (result == null) result = casePositionObject(receiveStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.SEND_STATEMENT:
            {
                SendStatement sendStatement = (SendStatement)theEObject;
                T result = caseSendStatement(sendStatement);
                if (result == null) result = caseCommunicationStatement(sendStatement);
                if (result == null) result = caseStatement(sendStatement);
                if (result == null) result = casePositionObject(sendStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.ENUM_VALUE_REFERENCE:
            {
                EnumValueReference enumValueReference = (EnumValueReference)theEObject;
                T result = caseEnumValueReference(enumValueReference);
                if (result == null) result = caseExpression(enumValueReference);
                if (result == null) result = casePositionObject(enumValueReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.READ_CALL_EXPRESSION:
            {
                ReadCallExpression readCallExpression = (ReadCallExpression)theEObject;
                T result = caseReadCallExpression(readCallExpression);
                if (result == null) result = caseExpression(readCallExpression);
                if (result == null) result = casePositionObject(readCallExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.UNWIND:
            {
                Unwind unwind = (Unwind)theEObject;
                T result = caseUnwind(unwind);
                if (result == null) result = casePositionObject(unwind);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.PROCESS_INSTANCE:
            {
                ProcessInstance processInstance = (ProcessInstance)theEObject;
                T result = caseProcessInstance(processInstance);
                if (result == null) result = caseCreateCase(processInstance);
                if (result == null) result = casePositionObject(processInstance);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.PROCESS_TYPE:
            {
                ProcessType processType = (ProcessType)theEObject;
                T result = caseProcessType(processType);
                if (result == null) result = caseComputeType(processType);
                if (result == null) result = caseType(processType);
                if (result == null) result = casePositionObject(processType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.PROCESS_REFERENCE:
            {
                ProcessReference processReference = (ProcessReference)theEObject;
                T result = caseProcessReference(processReference);
                if (result == null) result = caseExpression(processReference);
                if (result == null) result = casePositionObject(processReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.UNRESOLVED_REFERENCE:
            {
                UnresolvedReference unresolvedReference = (UnresolvedReference)theEObject;
                T result = caseUnresolvedReference(unresolvedReference);
                if (result == null) result = caseExpression(unresolvedReference);
                if (result == null) result = casePositionObject(unresolvedReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.UNRESOLVED_TYPE:
            {
                UnresolvedType unresolvedType = (UnresolvedType)theEObject;
                T result = caseUnresolvedType(unresolvedType);
                if (result == null) result = caseType(unresolvedType);
                if (result == null) result = casePositionObject(unresolvedType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.TIMER_TYPE:
            {
                TimerType timerType = (TimerType)theEObject;
                T result = caseTimerType(timerType);
                if (result == null) result = caseType(timerType);
                if (result == null) result = casePositionObject(timerType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.ENUM_DECLARATION:
            {
                EnumDeclaration enumDeclaration = (EnumDeclaration)theEObject;
                T result = caseEnumDeclaration(enumDeclaration);
                if (result == null) result = caseDeclaration(enumDeclaration);
                if (result == null) result = casePositionObject(enumDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.ENUM_VALUE:
            {
                EnumValue enumValue = (EnumValue)theEObject;
                T result = caseEnumValue(enumValue);
                if (result == null) result = casePositionObject(enumValue);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CHANNEL_EXPRESSION:
            {
                ChannelExpression channelExpression = (ChannelExpression)theEObject;
                T result = caseChannelExpression(channelExpression);
                if (result == null) result = caseExpression(channelExpression);
                if (result == null) result = casePositionObject(channelExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CAST_EXPRESSION:
            {
                CastExpression castExpression = (CastExpression)theEObject;
                T result = caseCastExpression(castExpression);
                if (result == null) result = caseExpression(castExpression);
                if (result == null) result = casePositionObject(castExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.CLOSE_STATEMENT:
            {
                CloseStatement closeStatement = (CloseStatement)theEObject;
                T result = caseCloseStatement(closeStatement);
                if (result == null) result = caseStatement(closeStatement);
                if (result == null) result = casePositionObject(closeStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.FINISH_STATEMENT:
            {
                FinishStatement finishStatement = (FinishStatement)theEObject;
                T result = caseFinishStatement(finishStatement);
                if (result == null) result = caseStatement(finishStatement);
                if (result == null) result = casePositionObject(finishStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.MODEL_REFERENCE:
            {
                ModelReference modelReference = (ModelReference)theEObject;
                T result = caseModelReference(modelReference);
                if (result == null) result = caseExpression(modelReference);
                if (result == null) result = casePositionObject(modelReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.XPER_DECLARATION:
            {
                XperDeclaration xperDeclaration = (XperDeclaration)theEObject;
                T result = caseXperDeclaration(xperDeclaration);
                if (result == null) result = caseBehaviourDeclaration(xperDeclaration);
                if (result == null) result = caseDeclaration(xperDeclaration);
                if (result == null) result = casePositionObject(xperDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.COMPUTE_DECLARATION:
            {
                ComputeDeclaration computeDeclaration = (ComputeDeclaration)theEObject;
                T result = caseComputeDeclaration(computeDeclaration);
                if (result == null) result = caseBehaviourDeclaration(computeDeclaration);
                if (result == null) result = caseDeclaration(computeDeclaration);
                if (result == null) result = casePositionObject(computeDeclaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.COMPUTE_TYPE:
            {
                ComputeType computeType = (ComputeType)theEObject;
                T result = caseComputeType(computeType);
                if (result == null) result = caseType(computeType);
                if (result == null) result = casePositionObject(computeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ChiPackage.MODEL_TYPE:
            {
                ModelType modelType = (ModelType)theEObject;
                T result = caseModelType(modelType);
                if (result == null) result = caseComputeType(modelType);
                if (result == null) result = caseType(modelType);
                if (result == null) result = casePositionObject(modelType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseType(Type object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Void Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Void Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVoidType(VoidType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bool Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bool Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBoolType(BoolType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Instance Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Instance Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInstanceType(InstanceType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Int Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Int Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIntType(IntType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>String Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>String Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStringType(StringType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Real Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Real Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRealType(RealType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>File Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>File Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFileType(FileType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Set Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Set Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSetType(SetType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>List Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>List Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseListType(ListType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dict Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dict Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDictType(DictType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Matrix Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Matrix Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMatrixType(MatrixType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tuple Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tuple Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTupleType(TupleType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tuple Field</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tuple Field</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTupleField(TupleField object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Distribution Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Distribution Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDistributionType(DistributionType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enum Type Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enum Type Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumTypeReference(EnumTypeReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Channel Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Channel Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseChannelType(ChannelType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionType(FunctionType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTypeReference(TypeReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExpression(Expression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bool Literal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bool Literal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBoolLiteral(BoolLiteral object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Int Number</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Int Number</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIntNumber(IntNumber object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Real Number</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Real Number</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRealNumber(RealNumber object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>String Literal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>String Literal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStringLiteral(StringLiteral object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tuple Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tuple Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTupleExpression(TupleExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>List Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>List Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseListExpression(ListExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Set Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Set Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSetExpression(SetExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Matrix Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Matrix Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMatrixExpression(MatrixExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Matrix Row</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Matrix Row</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMatrixRow(MatrixRow object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dictionary Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dictionary Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDictionaryExpression(DictionaryExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dictionary Pair</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dictionary Pair</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDictionaryPair(DictionaryPair object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Variable Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Variable Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVariableReference(VariableReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constant Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constant Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstantReference(ConstantReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Literal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Literal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeLiteral(TimeLiteral object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unary Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unary Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnaryExpression(UnaryExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBinaryExpression(BinaryExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Call Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Call Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCallExpression(CallExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionReference(FunctionReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Base Function Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Base Function Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBaseFunctionReference(BaseFunctionReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Std Lib Function Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Std Lib Function Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStdLibFunctionReference(StdLibFunctionReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Slice Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Slice Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSliceExpression(SliceExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Field Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Field Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFieldReference(FieldReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStatement(Statement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Break Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Break Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBreakStatement(BreakStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Continue Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Continue Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContinueStatement(ContinueStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pass Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pass Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePassStatement(PassStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Exit Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Exit Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExitStatement(ExitStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Return Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Return Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReturnStatement(ReturnStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Delay Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Delay Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDelayStatement(DelayStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>While Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>While Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWhileStatement(WhileStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfStatement(IfStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Case</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Case</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfCase(IfCase object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Write Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Write Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWriteStatement(WriteStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assignment Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assignment Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssignmentStatement(AssignmentStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Communication Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Communication Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCommunicationStatement(CommunicationStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>For Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>For Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForStatement(ForStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Create Case</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Create Case</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCreateCase(CreateCase object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Run Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Run Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRunStatement(RunStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Select Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Select Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSelectStatement(SelectStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Select Case</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Select Case</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSelectCase(SelectCase object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Iterated Create Case</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Iterated Create Case</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIteratedCreateCase(IteratedCreateCase object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Iterated Select Case</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Iterated Select Case</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIteratedSelectCase(IteratedSelectCase object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Specification</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Specification</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecification(Specification object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDeclaration(Declaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTypeDeclaration(TypeDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constant Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constant Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstantDeclaration(ConstantDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProcessDeclaration(ProcessDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionDeclaration(FunctionDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModelDeclaration(ModelDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Variable Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Variable Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVariableDeclaration(VariableDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Behaviour Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Behaviour Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBehaviourDeclaration(BehaviourDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Receive Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Receive Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReceiveStatement(ReceiveStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Send Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Send Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSendStatement(SendStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enum Value Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enum Value Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumValueReference(EnumValueReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Read Call Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Read Call Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReadCallExpression(ReadCallExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unwind</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unwind</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnwind(Unwind object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Instance</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Instance</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProcessInstance(ProcessInstance object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProcessType(ProcessType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProcessReference(ProcessReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unresolved Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unresolved Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnresolvedReference(UnresolvedReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unresolved Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unresolved Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnresolvedType(UnresolvedType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Timer Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Timer Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimerType(TimerType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enum Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enum Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumDeclaration(EnumDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enum Value</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enum Value</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumValue(EnumValue object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Channel Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Channel Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseChannelExpression(ChannelExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cast Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cast Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCastExpression(CastExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Close Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Close Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCloseStatement(CloseStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Finish Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Finish Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFinishStatement(FinishStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModelReference(ModelReference object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Xper Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Xper Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseXperDeclaration(XperDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Compute Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Compute Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComputeDeclaration(ComputeDeclaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Compute Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Compute Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComputeType(ComputeType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModelType(ModelType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePositionObject(PositionObject object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object)
    {
        return null;
    }

} //ChiSwitch
