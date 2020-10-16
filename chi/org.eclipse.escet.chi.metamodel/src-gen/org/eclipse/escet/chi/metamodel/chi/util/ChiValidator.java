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
package org.eclipse.escet.chi.metamodel.chi.util;

import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;

import org.eclipse.escet.chi.metamodel.chi.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage
 * @generated
 */
public class ChiValidator extends EObjectValidator
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final ChiValidator INSTANCE = new ChiValidator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "org.eclipse.escet.chi.metamodel.chi";

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ChiValidator()
    {
        super();
    }

    /**
     * Returns the package of this validator switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EPackage getEPackage()
    {
      return ChiPackage.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresponding classifier of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        switch (classifierID)
        {
            case ChiPackage.TYPE:
                return validateType((Type)value, diagnostics, context);
            case ChiPackage.VOID_TYPE:
                return validateVoidType((VoidType)value, diagnostics, context);
            case ChiPackage.BOOL_TYPE:
                return validateBoolType((BoolType)value, diagnostics, context);
            case ChiPackage.INSTANCE_TYPE:
                return validateInstanceType((InstanceType)value, diagnostics, context);
            case ChiPackage.INT_TYPE:
                return validateIntType((IntType)value, diagnostics, context);
            case ChiPackage.STRING_TYPE:
                return validateStringType((StringType)value, diagnostics, context);
            case ChiPackage.REAL_TYPE:
                return validateRealType((RealType)value, diagnostics, context);
            case ChiPackage.FILE_TYPE:
                return validateFileType((FileType)value, diagnostics, context);
            case ChiPackage.SET_TYPE:
                return validateSetType((SetType)value, diagnostics, context);
            case ChiPackage.LIST_TYPE:
                return validateListType((ListType)value, diagnostics, context);
            case ChiPackage.DICT_TYPE:
                return validateDictType((DictType)value, diagnostics, context);
            case ChiPackage.MATRIX_TYPE:
                return validateMatrixType((MatrixType)value, diagnostics, context);
            case ChiPackage.TUPLE_TYPE:
                return validateTupleType((TupleType)value, diagnostics, context);
            case ChiPackage.TUPLE_FIELD:
                return validateTupleField((TupleField)value, diagnostics, context);
            case ChiPackage.DISTRIBUTION_TYPE:
                return validateDistributionType((DistributionType)value, diagnostics, context);
            case ChiPackage.ENUM_TYPE_REFERENCE:
                return validateEnumTypeReference((EnumTypeReference)value, diagnostics, context);
            case ChiPackage.CHANNEL_TYPE:
                return validateChannelType((ChannelType)value, diagnostics, context);
            case ChiPackage.FUNCTION_TYPE:
                return validateFunctionType((FunctionType)value, diagnostics, context);
            case ChiPackage.TYPE_REFERENCE:
                return validateTypeReference((TypeReference)value, diagnostics, context);
            case ChiPackage.EXPRESSION:
                return validateExpression((Expression)value, diagnostics, context);
            case ChiPackage.BOOL_LITERAL:
                return validateBoolLiteral((BoolLiteral)value, diagnostics, context);
            case ChiPackage.INT_NUMBER:
                return validateIntNumber((IntNumber)value, diagnostics, context);
            case ChiPackage.REAL_NUMBER:
                return validateRealNumber((RealNumber)value, diagnostics, context);
            case ChiPackage.STRING_LITERAL:
                return validateStringLiteral((StringLiteral)value, diagnostics, context);
            case ChiPackage.TUPLE_EXPRESSION:
                return validateTupleExpression((TupleExpression)value, diagnostics, context);
            case ChiPackage.LIST_EXPRESSION:
                return validateListExpression((ListExpression)value, diagnostics, context);
            case ChiPackage.SET_EXPRESSION:
                return validateSetExpression((SetExpression)value, diagnostics, context);
            case ChiPackage.MATRIX_EXPRESSION:
                return validateMatrixExpression((MatrixExpression)value, diagnostics, context);
            case ChiPackage.MATRIX_ROW:
                return validateMatrixRow((MatrixRow)value, diagnostics, context);
            case ChiPackage.DICTIONARY_EXPRESSION:
                return validateDictionaryExpression((DictionaryExpression)value, diagnostics, context);
            case ChiPackage.DICTIONARY_PAIR:
                return validateDictionaryPair((DictionaryPair)value, diagnostics, context);
            case ChiPackage.VARIABLE_REFERENCE:
                return validateVariableReference((VariableReference)value, diagnostics, context);
            case ChiPackage.CONSTANT_REFERENCE:
                return validateConstantReference((ConstantReference)value, diagnostics, context);
            case ChiPackage.TIME_LITERAL:
                return validateTimeLiteral((TimeLiteral)value, diagnostics, context);
            case ChiPackage.UNARY_EXPRESSION:
                return validateUnaryExpression((UnaryExpression)value, diagnostics, context);
            case ChiPackage.BINARY_EXPRESSION:
                return validateBinaryExpression((BinaryExpression)value, diagnostics, context);
            case ChiPackage.CALL_EXPRESSION:
                return validateCallExpression((CallExpression)value, diagnostics, context);
            case ChiPackage.FUNCTION_REFERENCE:
                return validateFunctionReference((FunctionReference)value, diagnostics, context);
            case ChiPackage.BASE_FUNCTION_REFERENCE:
                return validateBaseFunctionReference((BaseFunctionReference)value, diagnostics, context);
            case ChiPackage.STD_LIB_FUNCTION_REFERENCE:
                return validateStdLibFunctionReference((StdLibFunctionReference)value, diagnostics, context);
            case ChiPackage.SLICE_EXPRESSION:
                return validateSliceExpression((SliceExpression)value, diagnostics, context);
            case ChiPackage.FIELD_REFERENCE:
                return validateFieldReference((FieldReference)value, diagnostics, context);
            case ChiPackage.STATEMENT:
                return validateStatement((Statement)value, diagnostics, context);
            case ChiPackage.BREAK_STATEMENT:
                return validateBreakStatement((BreakStatement)value, diagnostics, context);
            case ChiPackage.CONTINUE_STATEMENT:
                return validateContinueStatement((ContinueStatement)value, diagnostics, context);
            case ChiPackage.PASS_STATEMENT:
                return validatePassStatement((PassStatement)value, diagnostics, context);
            case ChiPackage.EXIT_STATEMENT:
                return validateExitStatement((ExitStatement)value, diagnostics, context);
            case ChiPackage.RETURN_STATEMENT:
                return validateReturnStatement((ReturnStatement)value, diagnostics, context);
            case ChiPackage.DELAY_STATEMENT:
                return validateDelayStatement((DelayStatement)value, diagnostics, context);
            case ChiPackage.WHILE_STATEMENT:
                return validateWhileStatement((WhileStatement)value, diagnostics, context);
            case ChiPackage.IF_STATEMENT:
                return validateIfStatement((IfStatement)value, diagnostics, context);
            case ChiPackage.IF_CASE:
                return validateIfCase((IfCase)value, diagnostics, context);
            case ChiPackage.WRITE_STATEMENT:
                return validateWriteStatement((WriteStatement)value, diagnostics, context);
            case ChiPackage.ASSIGNMENT_STATEMENT:
                return validateAssignmentStatement((AssignmentStatement)value, diagnostics, context);
            case ChiPackage.COMMUNICATION_STATEMENT:
                return validateCommunicationStatement((CommunicationStatement)value, diagnostics, context);
            case ChiPackage.FOR_STATEMENT:
                return validateForStatement((ForStatement)value, diagnostics, context);
            case ChiPackage.CREATE_CASE:
                return validateCreateCase((CreateCase)value, diagnostics, context);
            case ChiPackage.RUN_STATEMENT:
                return validateRunStatement((RunStatement)value, diagnostics, context);
            case ChiPackage.SELECT_STATEMENT:
                return validateSelectStatement((SelectStatement)value, diagnostics, context);
            case ChiPackage.SELECT_CASE:
                return validateSelectCase((SelectCase)value, diagnostics, context);
            case ChiPackage.ITERATED_CREATE_CASE:
                return validateIteratedCreateCase((IteratedCreateCase)value, diagnostics, context);
            case ChiPackage.ITERATED_SELECT_CASE:
                return validateIteratedSelectCase((IteratedSelectCase)value, diagnostics, context);
            case ChiPackage.SPECIFICATION:
                return validateSpecification((Specification)value, diagnostics, context);
            case ChiPackage.DECLARATION:
                return validateDeclaration((Declaration)value, diagnostics, context);
            case ChiPackage.TYPE_DECLARATION:
                return validateTypeDeclaration((TypeDeclaration)value, diagnostics, context);
            case ChiPackage.CONSTANT_DECLARATION:
                return validateConstantDeclaration((ConstantDeclaration)value, diagnostics, context);
            case ChiPackage.PROCESS_DECLARATION:
                return validateProcessDeclaration((ProcessDeclaration)value, diagnostics, context);
            case ChiPackage.FUNCTION_DECLARATION:
                return validateFunctionDeclaration((FunctionDeclaration)value, diagnostics, context);
            case ChiPackage.MODEL_DECLARATION:
                return validateModelDeclaration((ModelDeclaration)value, diagnostics, context);
            case ChiPackage.VARIABLE_DECLARATION:
                return validateVariableDeclaration((VariableDeclaration)value, diagnostics, context);
            case ChiPackage.BEHAVIOUR_DECLARATION:
                return validateBehaviourDeclaration((BehaviourDeclaration)value, diagnostics, context);
            case ChiPackage.RECEIVE_STATEMENT:
                return validateReceiveStatement((ReceiveStatement)value, diagnostics, context);
            case ChiPackage.SEND_STATEMENT:
                return validateSendStatement((SendStatement)value, diagnostics, context);
            case ChiPackage.ENUM_VALUE_REFERENCE:
                return validateEnumValueReference((EnumValueReference)value, diagnostics, context);
            case ChiPackage.READ_CALL_EXPRESSION:
                return validateReadCallExpression((ReadCallExpression)value, diagnostics, context);
            case ChiPackage.UNWIND:
                return validateUnwind((Unwind)value, diagnostics, context);
            case ChiPackage.PROCESS_INSTANCE:
                return validateProcessInstance((ProcessInstance)value, diagnostics, context);
            case ChiPackage.PROCESS_TYPE:
                return validateProcessType((ProcessType)value, diagnostics, context);
            case ChiPackage.PROCESS_REFERENCE:
                return validateProcessReference((ProcessReference)value, diagnostics, context);
            case ChiPackage.UNRESOLVED_REFERENCE:
                return validateUnresolvedReference((UnresolvedReference)value, diagnostics, context);
            case ChiPackage.UNRESOLVED_TYPE:
                return validateUnresolvedType((UnresolvedType)value, diagnostics, context);
            case ChiPackage.TIMER_TYPE:
                return validateTimerType((TimerType)value, diagnostics, context);
            case ChiPackage.ENUM_DECLARATION:
                return validateEnumDeclaration((EnumDeclaration)value, diagnostics, context);
            case ChiPackage.ENUM_VALUE:
                return validateEnumValue((EnumValue)value, diagnostics, context);
            case ChiPackage.CHANNEL_EXPRESSION:
                return validateChannelExpression((ChannelExpression)value, diagnostics, context);
            case ChiPackage.CAST_EXPRESSION:
                return validateCastExpression((CastExpression)value, diagnostics, context);
            case ChiPackage.CLOSE_STATEMENT:
                return validateCloseStatement((CloseStatement)value, diagnostics, context);
            case ChiPackage.FINISH_STATEMENT:
                return validateFinishStatement((FinishStatement)value, diagnostics, context);
            case ChiPackage.MODEL_REFERENCE:
                return validateModelReference((ModelReference)value, diagnostics, context);
            case ChiPackage.XPER_DECLARATION:
                return validateXperDeclaration((XperDeclaration)value, diagnostics, context);
            case ChiPackage.COMPUTE_DECLARATION:
                return validateComputeDeclaration((ComputeDeclaration)value, diagnostics, context);
            case ChiPackage.COMPUTE_TYPE:
                return validateComputeType((ComputeType)value, diagnostics, context);
            case ChiPackage.MODEL_TYPE:
                return validateModelType((ModelType)value, diagnostics, context);
            case ChiPackage.CHANNEL_OPS:
                return validateChannelOps((ChannelOps)value, diagnostics, context);
            case ChiPackage.UNARY_OPERATORS:
                return validateUnaryOperators((UnaryOperators)value, diagnostics, context);
            case ChiPackage.BINARY_OPERATORS:
                return validateBinaryOperators((BinaryOperators)value, diagnostics, context);
            case ChiPackage.STD_LIB_FUNCTIONS:
                return validateStdLibFunctions((StdLibFunctions)value, diagnostics, context);
            case ChiPackage.CHI_IDENTIFIER:
                return validateChiIdentifier((String)value, diagnostics, context);
            case ChiPackage.CHI_REAL_NUMBER:
                return validateChiRealNumber((String)value, diagnostics, context);
            case ChiPackage.CHI_NUMBER:
                return validateChiNumber((String)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateType(Type type, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(type, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVoidType(VoidType voidType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(voidType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBoolType(BoolType boolType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(boolType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateInstanceType(InstanceType instanceType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(instanceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIntType(IntType intType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(intType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStringType(StringType stringType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(stringType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRealType(RealType realType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(realType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFileType(FileType fileType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(fileType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSetType(SetType setType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(setType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateListType(ListType listType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(listType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDictType(DictType dictType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(dictType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMatrixType(MatrixType matrixType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(matrixType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTupleType(TupleType tupleType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(tupleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTupleField(TupleField tupleField, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(tupleField, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDistributionType(DistributionType distributionType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(distributionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEnumTypeReference(EnumTypeReference enumTypeReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(enumTypeReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateChannelType(ChannelType channelType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(channelType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFunctionType(FunctionType functionType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(functionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeReference(TypeReference typeReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(typeReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExpression(Expression expression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(expression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBoolLiteral(BoolLiteral boolLiteral, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(boolLiteral, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIntNumber(IntNumber intNumber, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(intNumber, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRealNumber(RealNumber realNumber, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(realNumber, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStringLiteral(StringLiteral stringLiteral, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(stringLiteral, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTupleExpression(TupleExpression tupleExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(tupleExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateListExpression(ListExpression listExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(listExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSetExpression(SetExpression setExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(setExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMatrixExpression(MatrixExpression matrixExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(matrixExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMatrixRow(MatrixRow matrixRow, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(matrixRow, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDictionaryExpression(DictionaryExpression dictionaryExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(dictionaryExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDictionaryPair(DictionaryPair dictionaryPair, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(dictionaryPair, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVariableReference(VariableReference variableReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(variableReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConstantReference(ConstantReference constantReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(constantReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeLiteral(TimeLiteral timeLiteral, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(timeLiteral, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnaryExpression(UnaryExpression unaryExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(unaryExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBinaryExpression(BinaryExpression binaryExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(binaryExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCallExpression(CallExpression callExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(callExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFunctionReference(FunctionReference functionReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(functionReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBaseFunctionReference(BaseFunctionReference baseFunctionReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(baseFunctionReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStdLibFunctionReference(StdLibFunctionReference stdLibFunctionReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(stdLibFunctionReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSliceExpression(SliceExpression sliceExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(sliceExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFieldReference(FieldReference fieldReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(fieldReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStatement(Statement statement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(statement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBreakStatement(BreakStatement breakStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(breakStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateContinueStatement(ContinueStatement continueStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(continueStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePassStatement(PassStatement passStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(passStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExitStatement(ExitStatement exitStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(exitStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReturnStatement(ReturnStatement returnStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(returnStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDelayStatement(DelayStatement delayStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(delayStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateWhileStatement(WhileStatement whileStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(whileStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIfStatement(IfStatement ifStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(ifStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIfCase(IfCase ifCase, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(ifCase, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateWriteStatement(WriteStatement writeStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(writeStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAssignmentStatement(AssignmentStatement assignmentStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(assignmentStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCommunicationStatement(CommunicationStatement communicationStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(communicationStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateForStatement(ForStatement forStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(forStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCreateCase(CreateCase createCase, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(createCase, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRunStatement(RunStatement runStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(runStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSelectStatement(SelectStatement selectStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(selectStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSelectCase(SelectCase selectCase, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(selectCase, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIteratedCreateCase(IteratedCreateCase iteratedCreateCase, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(iteratedCreateCase, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIteratedSelectCase(IteratedSelectCase iteratedSelectCase, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(iteratedSelectCase, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpecification(Specification specification, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(specification, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDeclaration(Declaration declaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(declaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeDeclaration(TypeDeclaration typeDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(typeDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConstantDeclaration(ConstantDeclaration constantDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(constantDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessDeclaration(ProcessDeclaration processDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(processDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFunctionDeclaration(FunctionDeclaration functionDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(functionDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateModelDeclaration(ModelDeclaration modelDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(modelDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVariableDeclaration(VariableDeclaration variableDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(variableDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBehaviourDeclaration(BehaviourDeclaration behaviourDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(behaviourDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReceiveStatement(ReceiveStatement receiveStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(receiveStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSendStatement(SendStatement sendStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(sendStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEnumValueReference(EnumValueReference enumValueReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(enumValueReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReadCallExpression(ReadCallExpression readCallExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(readCallExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnwind(Unwind unwind, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(unwind, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessInstance(ProcessInstance processInstance, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(processInstance, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessType(ProcessType processType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(processType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessReference(ProcessReference processReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(processReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnresolvedReference(UnresolvedReference unresolvedReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(unresolvedReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnresolvedType(UnresolvedType unresolvedType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(unresolvedType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimerType(TimerType timerType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(timerType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEnumDeclaration(EnumDeclaration enumDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(enumDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEnumValue(EnumValue enumValue, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(enumValue, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateChannelExpression(ChannelExpression channelExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(channelExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCastExpression(CastExpression castExpression, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(castExpression, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCloseStatement(CloseStatement closeStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(closeStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFinishStatement(FinishStatement finishStatement, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(finishStatement, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateModelReference(ModelReference modelReference, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(modelReference, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateXperDeclaration(XperDeclaration xperDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(xperDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComputeDeclaration(ComputeDeclaration computeDeclaration, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(computeDeclaration, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComputeType(ComputeType computeType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(computeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateModelType(ModelType modelType, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validate_EveryDefaultConstraint(modelType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateChannelOps(ChannelOps channelOps, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnaryOperators(UnaryOperators unaryOperators, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBinaryOperators(BinaryOperators binaryOperators, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStdLibFunctions(StdLibFunctions stdLibFunctions, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateChiIdentifier(String chiIdentifier, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateChiRealNumber(String chiRealNumber, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        boolean result = validateChiRealNumber_Pattern(chiRealNumber, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateChiRealNumber_Pattern
     */
    public static final  PatternMatcher [][] CHI_REAL_NUMBER__PATTERN__VALUES =
        new PatternMatcher [][]
        {
            new PatternMatcher []
            {
                XMLTypeUtil.createPatternMatcher("(0|([1-9][0-9]*))((\\.[0-9]+)|((\\.[0-9]+)?[eE][\\-\\+]?[0-9]+))")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Real Number</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateChiRealNumber_Pattern(String chiRealNumber, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validatePattern(ChiPackage.Literals.CHI_REAL_NUMBER, chiRealNumber, CHI_REAL_NUMBER__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateChiNumber(String chiNumber, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        boolean result = validateChiNumber_Pattern(chiNumber, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateChiNumber_Pattern
     */
    public static final  PatternMatcher [][] CHI_NUMBER__PATTERN__VALUES =
        new PatternMatcher [][]
        {
            new PatternMatcher []
            {
                XMLTypeUtil.createPatternMatcher("0|([1-9][0-9]*)")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Number</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateChiNumber_Pattern(String chiNumber, DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return validatePattern(ChiPackage.Literals.CHI_NUMBER, chiNumber, CHI_NUMBER__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator()
    {
        // TODO
        // Specialize this to return a resource locator for messages specific to this validator.
        // Ensure that you remove @generated or mark it @generated NOT
        return super.getResourceLocator();
    }

} //ChiValidator
