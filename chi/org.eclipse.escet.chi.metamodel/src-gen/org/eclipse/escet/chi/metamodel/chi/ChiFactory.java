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
package org.eclipse.escet.chi.metamodel.chi;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage
 * @generated
 */
public interface ChiFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ChiFactory eINSTANCE = org.eclipse.escet.chi.metamodel.chi.impl.ChiFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Void Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Void Type</em>'.
     * @generated
     */
    VoidType createVoidType();

    /**
     * Returns a new object of class '<em>Bool Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Bool Type</em>'.
     * @generated
     */
    BoolType createBoolType();

    /**
     * Returns a new object of class '<em>Instance Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Instance Type</em>'.
     * @generated
     */
    InstanceType createInstanceType();

    /**
     * Returns a new object of class '<em>Int Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Int Type</em>'.
     * @generated
     */
    IntType createIntType();

    /**
     * Returns a new object of class '<em>String Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>String Type</em>'.
     * @generated
     */
    StringType createStringType();

    /**
     * Returns a new object of class '<em>Real Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Real Type</em>'.
     * @generated
     */
    RealType createRealType();

    /**
     * Returns a new object of class '<em>File Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>File Type</em>'.
     * @generated
     */
    FileType createFileType();

    /**
     * Returns a new object of class '<em>Set Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Set Type</em>'.
     * @generated
     */
    SetType createSetType();

    /**
     * Returns a new object of class '<em>List Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>List Type</em>'.
     * @generated
     */
    ListType createListType();

    /**
     * Returns a new object of class '<em>Dict Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Dict Type</em>'.
     * @generated
     */
    DictType createDictType();

    /**
     * Returns a new object of class '<em>Matrix Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Matrix Type</em>'.
     * @generated
     */
    MatrixType createMatrixType();

    /**
     * Returns a new object of class '<em>Tuple Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tuple Type</em>'.
     * @generated
     */
    TupleType createTupleType();

    /**
     * Returns a new object of class '<em>Tuple Field</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tuple Field</em>'.
     * @generated
     */
    TupleField createTupleField();

    /**
     * Returns a new object of class '<em>Distribution Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Distribution Type</em>'.
     * @generated
     */
    DistributionType createDistributionType();

    /**
     * Returns a new object of class '<em>Enum Type Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Enum Type Reference</em>'.
     * @generated
     */
    EnumTypeReference createEnumTypeReference();

    /**
     * Returns a new object of class '<em>Channel Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Channel Type</em>'.
     * @generated
     */
    ChannelType createChannelType();

    /**
     * Returns a new object of class '<em>Function Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Function Type</em>'.
     * @generated
     */
    FunctionType createFunctionType();

    /**
     * Returns a new object of class '<em>Type Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Type Reference</em>'.
     * @generated
     */
    TypeReference createTypeReference();

    /**
     * Returns a new object of class '<em>Bool Literal</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Bool Literal</em>'.
     * @generated
     */
    BoolLiteral createBoolLiteral();

    /**
     * Returns a new object of class '<em>Int Number</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Int Number</em>'.
     * @generated
     */
    IntNumber createIntNumber();

    /**
     * Returns a new object of class '<em>Real Number</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Real Number</em>'.
     * @generated
     */
    RealNumber createRealNumber();

    /**
     * Returns a new object of class '<em>String Literal</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>String Literal</em>'.
     * @generated
     */
    StringLiteral createStringLiteral();

    /**
     * Returns a new object of class '<em>Tuple Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tuple Expression</em>'.
     * @generated
     */
    TupleExpression createTupleExpression();

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
     * Returns a new object of class '<em>Matrix Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Matrix Expression</em>'.
     * @generated
     */
    MatrixExpression createMatrixExpression();

    /**
     * Returns a new object of class '<em>Matrix Row</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Matrix Row</em>'.
     * @generated
     */
    MatrixRow createMatrixRow();

    /**
     * Returns a new object of class '<em>Dictionary Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Dictionary Expression</em>'.
     * @generated
     */
    DictionaryExpression createDictionaryExpression();

    /**
     * Returns a new object of class '<em>Dictionary Pair</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Dictionary Pair</em>'.
     * @generated
     */
    DictionaryPair createDictionaryPair();

    /**
     * Returns a new object of class '<em>Variable Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Variable Reference</em>'.
     * @generated
     */
    VariableReference createVariableReference();

    /**
     * Returns a new object of class '<em>Constant Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Constant Reference</em>'.
     * @generated
     */
    ConstantReference createConstantReference();

    /**
     * Returns a new object of class '<em>Time Literal</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Time Literal</em>'.
     * @generated
     */
    TimeLiteral createTimeLiteral();

    /**
     * Returns a new object of class '<em>Unary Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Unary Expression</em>'.
     * @generated
     */
    UnaryExpression createUnaryExpression();

    /**
     * Returns a new object of class '<em>Binary Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Binary Expression</em>'.
     * @generated
     */
    BinaryExpression createBinaryExpression();

    /**
     * Returns a new object of class '<em>Call Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Call Expression</em>'.
     * @generated
     */
    CallExpression createCallExpression();

    /**
     * Returns a new object of class '<em>Function Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Function Reference</em>'.
     * @generated
     */
    FunctionReference createFunctionReference();

    /**
     * Returns a new object of class '<em>Std Lib Function Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Std Lib Function Reference</em>'.
     * @generated
     */
    StdLibFunctionReference createStdLibFunctionReference();

    /**
     * Returns a new object of class '<em>Slice Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Slice Expression</em>'.
     * @generated
     */
    SliceExpression createSliceExpression();

    /**
     * Returns a new object of class '<em>Field Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Field Reference</em>'.
     * @generated
     */
    FieldReference createFieldReference();

    /**
     * Returns a new object of class '<em>Break Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Break Statement</em>'.
     * @generated
     */
    BreakStatement createBreakStatement();

    /**
     * Returns a new object of class '<em>Continue Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Continue Statement</em>'.
     * @generated
     */
    ContinueStatement createContinueStatement();

    /**
     * Returns a new object of class '<em>Pass Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Pass Statement</em>'.
     * @generated
     */
    PassStatement createPassStatement();

    /**
     * Returns a new object of class '<em>Exit Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Exit Statement</em>'.
     * @generated
     */
    ExitStatement createExitStatement();

    /**
     * Returns a new object of class '<em>Return Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Return Statement</em>'.
     * @generated
     */
    ReturnStatement createReturnStatement();

    /**
     * Returns a new object of class '<em>Delay Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Delay Statement</em>'.
     * @generated
     */
    DelayStatement createDelayStatement();

    /**
     * Returns a new object of class '<em>While Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>While Statement</em>'.
     * @generated
     */
    WhileStatement createWhileStatement();

    /**
     * Returns a new object of class '<em>If Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>If Statement</em>'.
     * @generated
     */
    IfStatement createIfStatement();

    /**
     * Returns a new object of class '<em>If Case</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>If Case</em>'.
     * @generated
     */
    IfCase createIfCase();

    /**
     * Returns a new object of class '<em>Write Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Write Statement</em>'.
     * @generated
     */
    WriteStatement createWriteStatement();

    /**
     * Returns a new object of class '<em>Assignment Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Assignment Statement</em>'.
     * @generated
     */
    AssignmentStatement createAssignmentStatement();

    /**
     * Returns a new object of class '<em>For Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>For Statement</em>'.
     * @generated
     */
    ForStatement createForStatement();

    /**
     * Returns a new object of class '<em>Run Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Run Statement</em>'.
     * @generated
     */
    RunStatement createRunStatement();

    /**
     * Returns a new object of class '<em>Select Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Select Statement</em>'.
     * @generated
     */
    SelectStatement createSelectStatement();

    /**
     * Returns a new object of class '<em>Select Case</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Select Case</em>'.
     * @generated
     */
    SelectCase createSelectCase();

    /**
     * Returns a new object of class '<em>Iterated Create Case</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Iterated Create Case</em>'.
     * @generated
     */
    IteratedCreateCase createIteratedCreateCase();

    /**
     * Returns a new object of class '<em>Iterated Select Case</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Iterated Select Case</em>'.
     * @generated
     */
    IteratedSelectCase createIteratedSelectCase();

    /**
     * Returns a new object of class '<em>Specification</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Specification</em>'.
     * @generated
     */
    Specification createSpecification();

    /**
     * Returns a new object of class '<em>Type Declaration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Type Declaration</em>'.
     * @generated
     */
    TypeDeclaration createTypeDeclaration();

    /**
     * Returns a new object of class '<em>Constant Declaration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Constant Declaration</em>'.
     * @generated
     */
    ConstantDeclaration createConstantDeclaration();

    /**
     * Returns a new object of class '<em>Process Declaration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Process Declaration</em>'.
     * @generated
     */
    ProcessDeclaration createProcessDeclaration();

    /**
     * Returns a new object of class '<em>Function Declaration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Function Declaration</em>'.
     * @generated
     */
    FunctionDeclaration createFunctionDeclaration();

    /**
     * Returns a new object of class '<em>Model Declaration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model Declaration</em>'.
     * @generated
     */
    ModelDeclaration createModelDeclaration();

    /**
     * Returns a new object of class '<em>Variable Declaration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Variable Declaration</em>'.
     * @generated
     */
    VariableDeclaration createVariableDeclaration();

    /**
     * Returns a new object of class '<em>Receive Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Receive Statement</em>'.
     * @generated
     */
    ReceiveStatement createReceiveStatement();

    /**
     * Returns a new object of class '<em>Send Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Send Statement</em>'.
     * @generated
     */
    SendStatement createSendStatement();

    /**
     * Returns a new object of class '<em>Enum Value Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Enum Value Reference</em>'.
     * @generated
     */
    EnumValueReference createEnumValueReference();

    /**
     * Returns a new object of class '<em>Read Call Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Read Call Expression</em>'.
     * @generated
     */
    ReadCallExpression createReadCallExpression();

    /**
     * Returns a new object of class '<em>Unwind</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Unwind</em>'.
     * @generated
     */
    Unwind createUnwind();

    /**
     * Returns a new object of class '<em>Process Instance</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Process Instance</em>'.
     * @generated
     */
    ProcessInstance createProcessInstance();

    /**
     * Returns a new object of class '<em>Process Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Process Type</em>'.
     * @generated
     */
    ProcessType createProcessType();

    /**
     * Returns a new object of class '<em>Process Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Process Reference</em>'.
     * @generated
     */
    ProcessReference createProcessReference();

    /**
     * Returns a new object of class '<em>Unresolved Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Unresolved Reference</em>'.
     * @generated
     */
    UnresolvedReference createUnresolvedReference();

    /**
     * Returns a new object of class '<em>Unresolved Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Unresolved Type</em>'.
     * @generated
     */
    UnresolvedType createUnresolvedType();

    /**
     * Returns a new object of class '<em>Timer Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Timer Type</em>'.
     * @generated
     */
    TimerType createTimerType();

    /**
     * Returns a new object of class '<em>Enum Declaration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Enum Declaration</em>'.
     * @generated
     */
    EnumDeclaration createEnumDeclaration();

    /**
     * Returns a new object of class '<em>Enum Value</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Enum Value</em>'.
     * @generated
     */
    EnumValue createEnumValue();

    /**
     * Returns a new object of class '<em>Channel Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Channel Expression</em>'.
     * @generated
     */
    ChannelExpression createChannelExpression();

    /**
     * Returns a new object of class '<em>Cast Expression</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Cast Expression</em>'.
     * @generated
     */
    CastExpression createCastExpression();

    /**
     * Returns a new object of class '<em>Close Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Close Statement</em>'.
     * @generated
     */
    CloseStatement createCloseStatement();

    /**
     * Returns a new object of class '<em>Finish Statement</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Finish Statement</em>'.
     * @generated
     */
    FinishStatement createFinishStatement();

    /**
     * Returns a new object of class '<em>Model Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model Reference</em>'.
     * @generated
     */
    ModelReference createModelReference();

    /**
     * Returns a new object of class '<em>Xper Declaration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Xper Declaration</em>'.
     * @generated
     */
    XperDeclaration createXperDeclaration();

    /**
     * Returns a new object of class '<em>Model Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model Type</em>'.
     * @generated
     */
    ModelType createModelType();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ChiPackage getChiPackage();

} //ChiFactory
