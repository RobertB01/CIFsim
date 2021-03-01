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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.chi.metamodel.chi.AssignmentStatement;
import org.eclipse.escet.chi.metamodel.chi.BaseFunctionReference;
import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.BinaryOperators;
import org.eclipse.escet.chi.metamodel.chi.BoolLiteral;
import org.eclipse.escet.chi.metamodel.chi.BoolType;
import org.eclipse.escet.chi.metamodel.chi.BreakStatement;
import org.eclipse.escet.chi.metamodel.chi.CallExpression;
import org.eclipse.escet.chi.metamodel.chi.CastExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelOps;
import org.eclipse.escet.chi.metamodel.chi.ChannelType;
import org.eclipse.escet.chi.metamodel.chi.ChiFactory;
import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.CloseStatement;
import org.eclipse.escet.chi.metamodel.chi.CommunicationStatement;
import org.eclipse.escet.chi.metamodel.chi.ComputeDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ComputeType;
import org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ConstantReference;
import org.eclipse.escet.chi.metamodel.chi.ContinueStatement;
import org.eclipse.escet.chi.metamodel.chi.CreateCase;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.DelayStatement;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.DictionaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictionaryPair;
import org.eclipse.escet.chi.metamodel.chi.DistributionType;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.EnumTypeReference;
import org.eclipse.escet.chi.metamodel.chi.EnumValue;
import org.eclipse.escet.chi.metamodel.chi.EnumValueReference;
import org.eclipse.escet.chi.metamodel.chi.ExitStatement;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FieldReference;
import org.eclipse.escet.chi.metamodel.chi.FileType;
import org.eclipse.escet.chi.metamodel.chi.FinishStatement;
import org.eclipse.escet.chi.metamodel.chi.ForStatement;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.FunctionReference;
import org.eclipse.escet.chi.metamodel.chi.FunctionType;
import org.eclipse.escet.chi.metamodel.chi.IfCase;
import org.eclipse.escet.chi.metamodel.chi.IfStatement;
import org.eclipse.escet.chi.metamodel.chi.InstanceType;
import org.eclipse.escet.chi.metamodel.chi.IntNumber;
import org.eclipse.escet.chi.metamodel.chi.IntType;
import org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase;
import org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase;
import org.eclipse.escet.chi.metamodel.chi.ListExpression;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.MatrixExpression;
import org.eclipse.escet.chi.metamodel.chi.MatrixRow;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ModelReference;
import org.eclipse.escet.chi.metamodel.chi.ModelType;
import org.eclipse.escet.chi.metamodel.chi.PassStatement;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessInstance;
import org.eclipse.escet.chi.metamodel.chi.ProcessReference;
import org.eclipse.escet.chi.metamodel.chi.ProcessType;
import org.eclipse.escet.chi.metamodel.chi.ReadCallExpression;
import org.eclipse.escet.chi.metamodel.chi.RealNumber;
import org.eclipse.escet.chi.metamodel.chi.RealType;
import org.eclipse.escet.chi.metamodel.chi.ReceiveStatement;
import org.eclipse.escet.chi.metamodel.chi.ReturnStatement;
import org.eclipse.escet.chi.metamodel.chi.RunStatement;
import org.eclipse.escet.chi.metamodel.chi.SelectCase;
import org.eclipse.escet.chi.metamodel.chi.SelectStatement;
import org.eclipse.escet.chi.metamodel.chi.SendStatement;
import org.eclipse.escet.chi.metamodel.chi.SetExpression;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.SliceExpression;
import org.eclipse.escet.chi.metamodel.chi.Specification;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctions;
import org.eclipse.escet.chi.metamodel.chi.StringLiteral;
import org.eclipse.escet.chi.metamodel.chi.StringType;
import org.eclipse.escet.chi.metamodel.chi.TimeLiteral;
import org.eclipse.escet.chi.metamodel.chi.TimerType;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.TypeDeclaration;
import org.eclipse.escet.chi.metamodel.chi.TypeReference;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.chi.metamodel.chi.UnaryOperators;
import org.eclipse.escet.chi.metamodel.chi.UnresolvedReference;
import org.eclipse.escet.chi.metamodel.chi.UnresolvedType;
import org.eclipse.escet.chi.metamodel.chi.Unwind;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VariableReference;
import org.eclipse.escet.chi.metamodel.chi.VoidType;
import org.eclipse.escet.chi.metamodel.chi.WhileStatement;
import org.eclipse.escet.chi.metamodel.chi.WriteStatement;
import org.eclipse.escet.chi.metamodel.chi.XperDeclaration;

import org.eclipse.escet.chi.metamodel.chi.util.ChiValidator;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChiPackageImpl extends EPackageImpl implements ChiPackage
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass typeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass voidTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass boolTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass instanceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass intTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stringTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass realTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass fileTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass setTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass listTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dictTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass matrixTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tupleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tupleFieldEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass distributionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass enumTypeReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass channelTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass typeReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass expressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass boolLiteralEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass intNumberEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass realNumberEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stringLiteralEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tupleExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass listExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass setExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass matrixExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass matrixRowEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dictionaryExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dictionaryPairEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass variableReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass constantReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeLiteralEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unaryExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass binaryExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass callExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass baseFunctionReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stdLibFunctionReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sliceExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass fieldReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass statementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass breakStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass continueStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass passStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass exitStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass returnStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass delayStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass whileStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ifStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ifCaseEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass writeStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass assignmentStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass communicationStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass forStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass createCaseEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass runStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass selectStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass selectCaseEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass iteratedCreateCaseEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass iteratedSelectCaseEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass specificationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass declarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass typeDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass constantDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass processDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass modelDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass variableDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass behaviourDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass receiveStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sendStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass enumValueReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass readCallExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unwindEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass processInstanceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass processTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass processReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unresolvedReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unresolvedTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timerTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass enumDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass enumValueEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass channelExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass castExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass closeStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass finishStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass modelReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass xperDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass computeDeclarationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass computeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass modelTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum channelOpsEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum unaryOperatorsEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum binaryOperatorsEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum stdLibFunctionsEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType chiIdentifierEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType chiRealNumberEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType chiNumberEDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ChiPackageImpl()
    {
        super(eNS_URI, ChiFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     *
     * <p>This method is used to initialize {@link ChiPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ChiPackage init()
    {
        if (isInited) return (ChiPackage)EPackage.Registry.INSTANCE.getEPackage(ChiPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredChiPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        ChiPackageImpl theChiPackage = registeredChiPackage instanceof ChiPackageImpl ? (ChiPackageImpl)registeredChiPackage : new ChiPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        PositionPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theChiPackage.createPackageContents();

        // Initialize created meta-data
        theChiPackage.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (theChiPackage,
             new EValidator.Descriptor()
             {
                 @Override
                 public EValidator getEValidator()
                 {
                     return ChiValidator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        theChiPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ChiPackage.eNS_URI, theChiPackage);
        return theChiPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getType()
    {
        return typeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVoidType()
    {
        return voidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBoolType()
    {
        return boolTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInstanceType()
    {
        return instanceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIntType()
    {
        return intTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStringType()
    {
        return stringTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRealType()
    {
        return realTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFileType()
    {
        return fileTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSetType()
    {
        return setTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSetType_ElementType()
    {
        return (EReference)setTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getListType()
    {
        return listTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getListType_ElementType()
    {
        return (EReference)listTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getListType_InitialLength()
    {
        return (EReference)listTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDictType()
    {
        return dictTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictType_KeyType()
    {
        return (EReference)dictTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictType_ValueType()
    {
        return (EReference)dictTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMatrixType()
    {
        return matrixTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMatrixType_RowSize()
    {
        return (EReference)matrixTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMatrixType_ColumnSize()
    {
        return (EReference)matrixTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTupleType()
    {
        return tupleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTupleType_Fields()
    {
        return (EReference)tupleTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTupleField()
    {
        return tupleFieldEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTupleField_Type()
    {
        return (EReference)tupleFieldEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getTupleField_Name()
    {
        return (EAttribute)tupleFieldEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDistributionType()
    {
        return distributionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDistributionType_ResultType()
    {
        return (EReference)distributionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEnumTypeReference()
    {
        return enumTypeReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEnumTypeReference_Type()
    {
        return (EReference)enumTypeReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getChannelType()
    {
        return channelTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChannelType_ElementType()
    {
        return (EReference)channelTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getChannelType_Ops()
    {
        return (EAttribute)channelTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunctionType()
    {
        return functionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunctionType_ResultType()
    {
        return (EReference)functionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunctionType_ParameterTypes()
    {
        return (EReference)functionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTypeReference()
    {
        return typeReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTypeReference_Type()
    {
        return (EReference)typeReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getExpression()
    {
        return expressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getExpression_Type()
    {
        return (EReference)expressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBoolLiteral()
    {
        return boolLiteralEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBoolLiteral_Value()
    {
        return (EAttribute)boolLiteralEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIntNumber()
    {
        return intNumberEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getIntNumber_Value()
    {
        return (EAttribute)intNumberEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRealNumber()
    {
        return realNumberEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRealNumber_Value()
    {
        return (EAttribute)realNumberEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStringLiteral()
    {
        return stringLiteralEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getStringLiteral_Value()
    {
        return (EAttribute)stringLiteralEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTupleExpression()
    {
        return tupleExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTupleExpression_Fields()
    {
        return (EReference)tupleExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getListExpression()
    {
        return listExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getListExpression_Elements()
    {
        return (EReference)listExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSetExpression()
    {
        return setExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSetExpression_Elements()
    {
        return (EReference)setExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMatrixExpression()
    {
        return matrixExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMatrixExpression_Rows()
    {
        return (EReference)matrixExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMatrixRow()
    {
        return matrixRowEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMatrixRow_Elements()
    {
        return (EReference)matrixRowEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDictionaryExpression()
    {
        return dictionaryExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictionaryExpression_Pairs()
    {
        return (EReference)dictionaryExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDictionaryPair()
    {
        return dictionaryPairEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictionaryPair_Key()
    {
        return (EReference)dictionaryPairEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictionaryPair_Value()
    {
        return (EReference)dictionaryPairEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVariableReference()
    {
        return variableReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVariableReference_Variable()
    {
        return (EReference)variableReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConstantReference()
    {
        return constantReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConstantReference_Constant()
    {
        return (EReference)constantReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTimeLiteral()
    {
        return timeLiteralEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUnaryExpression()
    {
        return unaryExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUnaryExpression_Child()
    {
        return (EReference)unaryExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getUnaryExpression_Op()
    {
        return (EAttribute)unaryExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBinaryExpression()
    {
        return binaryExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBinaryExpression_Left()
    {
        return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBinaryExpression_Right()
    {
        return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBinaryExpression_Op()
    {
        return (EAttribute)binaryExpressionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCallExpression()
    {
        return callExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCallExpression_Function()
    {
        return (EReference)callExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCallExpression_Arguments()
    {
        return (EReference)callExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCallExpression_Name()
    {
        return (EReference)callExpressionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunctionReference()
    {
        return functionReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunctionReference_Function()
    {
        return (EReference)functionReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBaseFunctionReference()
    {
        return baseFunctionReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStdLibFunctionReference()
    {
        return stdLibFunctionReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getStdLibFunctionReference_Function()
    {
        return (EAttribute)stdLibFunctionReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSliceExpression()
    {
        return sliceExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSliceExpression_Start()
    {
        return (EReference)sliceExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSliceExpression_End()
    {
        return (EReference)sliceExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSliceExpression_Step()
    {
        return (EReference)sliceExpressionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSliceExpression_Source()
    {
        return (EReference)sliceExpressionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFieldReference()
    {
        return fieldReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFieldReference_Field()
    {
        return (EReference)fieldReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStatement()
    {
        return statementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBreakStatement()
    {
        return breakStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getContinueStatement()
    {
        return continueStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPassStatement()
    {
        return passStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getExitStatement()
    {
        return exitStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getExitStatement_Value()
    {
        return (EReference)exitStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReturnStatement()
    {
        return returnStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getReturnStatement_Value()
    {
        return (EReference)returnStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDelayStatement()
    {
        return delayStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDelayStatement_Length()
    {
        return (EReference)delayStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getWhileStatement()
    {
        return whileStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getWhileStatement_Condition()
    {
        return (EReference)whileStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getWhileStatement_Body()
    {
        return (EReference)whileStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIfStatement()
    {
        return ifStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfStatement_Cases()
    {
        return (EReference)ifStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIfCase()
    {
        return ifCaseEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfCase_Condition()
    {
        return (EReference)ifCaseEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfCase_Body()
    {
        return (EReference)ifCaseEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getWriteStatement()
    {
        return writeStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getWriteStatement_Values()
    {
        return (EReference)writeStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getWriteStatement_AddNewline()
    {
        return (EAttribute)writeStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssignmentStatement()
    {
        return assignmentStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignmentStatement_Lhs()
    {
        return (EReference)assignmentStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignmentStatement_Rhs()
    {
        return (EReference)assignmentStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCommunicationStatement()
    {
        return communicationStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCommunicationStatement_Channel()
    {
        return (EReference)communicationStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCommunicationStatement_Data()
    {
        return (EReference)communicationStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getForStatement()
    {
        return forStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getForStatement_Body()
    {
        return (EReference)forStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getForStatement_Unwinds()
    {
        return (EReference)forStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCreateCase()
    {
        return createCaseEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRunStatement()
    {
        return runStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRunStatement_Cases()
    {
        return (EReference)runStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRunStatement_StartOnly()
    {
        return (EAttribute)runStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSelectStatement()
    {
        return selectStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSelectStatement_Cases()
    {
        return (EReference)selectStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSelectCase()
    {
        return selectCaseEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSelectCase_Body()
    {
        return (EReference)selectCaseEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSelectCase_Guard()
    {
        return (EReference)selectCaseEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIteratedCreateCase()
    {
        return iteratedCreateCaseEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIteratedCreateCase_Unwinds()
    {
        return (EReference)iteratedCreateCaseEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIteratedCreateCase_Instances()
    {
        return (EReference)iteratedCreateCaseEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIteratedSelectCase()
    {
        return iteratedSelectCaseEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIteratedSelectCase_Unwinds()
    {
        return (EReference)iteratedSelectCaseEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSpecification()
    {
        return specificationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSpecification_Declarations()
    {
        return (EReference)specificationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDeclaration()
    {
        return declarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDeclaration_Name()
    {
        return (EAttribute)declarationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTypeDeclaration()
    {
        return typeDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTypeDeclaration_Type()
    {
        return (EReference)typeDeclarationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConstantDeclaration()
    {
        return constantDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConstantDeclaration_Type()
    {
        return (EReference)constantDeclarationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConstantDeclaration_Value()
    {
        return (EReference)constantDeclarationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProcessDeclaration()
    {
        return processDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunctionDeclaration()
    {
        return functionDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunctionDeclaration_ReturnType()
    {
        return (EReference)functionDeclarationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getModelDeclaration()
    {
        return modelDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVariableDeclaration()
    {
        return variableDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVariableDeclaration_InitialValue()
    {
        return (EReference)variableDeclarationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVariableDeclaration_Type()
    {
        return (EReference)variableDeclarationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getVariableDeclaration_Parameter()
    {
        return (EAttribute)variableDeclarationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getVariableDeclaration_Name()
    {
        return (EAttribute)variableDeclarationEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBehaviourDeclaration()
    {
        return behaviourDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBehaviourDeclaration_Variables()
    {
        return (EReference)behaviourDeclarationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBehaviourDeclaration_Statements()
    {
        return (EReference)behaviourDeclarationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReceiveStatement()
    {
        return receiveStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSendStatement()
    {
        return sendStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEnumValueReference()
    {
        return enumValueReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEnumValueReference_Value()
    {
        return (EReference)enumValueReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReadCallExpression()
    {
        return readCallExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getReadCallExpression_File()
    {
        return (EReference)readCallExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getReadCallExpression_LoadType()
    {
        return (EReference)readCallExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUnwind()
    {
        return unwindEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUnwind_Source()
    {
        return (EReference)unwindEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUnwind_Variables()
    {
        return (EReference)unwindEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProcessInstance()
    {
        return processInstanceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getProcessInstance_Call()
    {
        return (EReference)processInstanceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getProcessInstance_Var()
    {
        return (EReference)processInstanceEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProcessType()
    {
        return processTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProcessReference()
    {
        return processReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getProcessReference_Process()
    {
        return (EReference)processReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUnresolvedReference()
    {
        return unresolvedReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getUnresolvedReference_Name()
    {
        return (EAttribute)unresolvedReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUnresolvedType()
    {
        return unresolvedTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getUnresolvedType_Name()
    {
        return (EAttribute)unresolvedTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTimerType()
    {
        return timerTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEnumDeclaration()
    {
        return enumDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEnumDeclaration_Values()
    {
        return (EReference)enumDeclarationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEnumValue()
    {
        return enumValueEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEnumValue_Name()
    {
        return (EAttribute)enumValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getChannelExpression()
    {
        return channelExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getChannelExpression_ElementType()
    {
        return (EReference)channelExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCastExpression()
    {
        return castExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCastExpression_Expression()
    {
        return (EReference)castExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCastExpression_CastType()
    {
        return (EReference)castExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCloseStatement()
    {
        return closeStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCloseStatement_Handle()
    {
        return (EReference)closeStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFinishStatement()
    {
        return finishStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFinishStatement_Instances()
    {
        return (EReference)finishStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getModelReference()
    {
        return modelReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getModelReference_Model()
    {
        return (EReference)modelReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getXperDeclaration()
    {
        return xperDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComputeDeclaration()
    {
        return computeDeclarationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComputeDeclaration_ReturnType()
    {
        return (EReference)computeDeclarationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComputeType()
    {
        return computeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComputeType_ParameterTypes()
    {
        return (EReference)computeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComputeType_ExitType()
    {
        return (EReference)computeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getModelType()
    {
        return modelTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getChannelOps()
    {
        return channelOpsEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getUnaryOperators()
    {
        return unaryOperatorsEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getBinaryOperators()
    {
        return binaryOperatorsEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getStdLibFunctions()
    {
        return stdLibFunctionsEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getChiIdentifier()
    {
        return chiIdentifierEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getChiRealNumber()
    {
        return chiRealNumberEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getChiNumber()
    {
        return chiNumberEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ChiFactory getChiFactory()
    {
        return (ChiFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents()
    {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        typeEClass = createEClass(TYPE);

        voidTypeEClass = createEClass(VOID_TYPE);

        boolTypeEClass = createEClass(BOOL_TYPE);

        instanceTypeEClass = createEClass(INSTANCE_TYPE);

        intTypeEClass = createEClass(INT_TYPE);

        stringTypeEClass = createEClass(STRING_TYPE);

        realTypeEClass = createEClass(REAL_TYPE);

        fileTypeEClass = createEClass(FILE_TYPE);

        setTypeEClass = createEClass(SET_TYPE);
        createEReference(setTypeEClass, SET_TYPE__ELEMENT_TYPE);

        listTypeEClass = createEClass(LIST_TYPE);
        createEReference(listTypeEClass, LIST_TYPE__ELEMENT_TYPE);
        createEReference(listTypeEClass, LIST_TYPE__INITIAL_LENGTH);

        dictTypeEClass = createEClass(DICT_TYPE);
        createEReference(dictTypeEClass, DICT_TYPE__KEY_TYPE);
        createEReference(dictTypeEClass, DICT_TYPE__VALUE_TYPE);

        matrixTypeEClass = createEClass(MATRIX_TYPE);
        createEReference(matrixTypeEClass, MATRIX_TYPE__ROW_SIZE);
        createEReference(matrixTypeEClass, MATRIX_TYPE__COLUMN_SIZE);

        tupleTypeEClass = createEClass(TUPLE_TYPE);
        createEReference(tupleTypeEClass, TUPLE_TYPE__FIELDS);

        tupleFieldEClass = createEClass(TUPLE_FIELD);
        createEReference(tupleFieldEClass, TUPLE_FIELD__TYPE);
        createEAttribute(tupleFieldEClass, TUPLE_FIELD__NAME);

        distributionTypeEClass = createEClass(DISTRIBUTION_TYPE);
        createEReference(distributionTypeEClass, DISTRIBUTION_TYPE__RESULT_TYPE);

        enumTypeReferenceEClass = createEClass(ENUM_TYPE_REFERENCE);
        createEReference(enumTypeReferenceEClass, ENUM_TYPE_REFERENCE__TYPE);

        channelTypeEClass = createEClass(CHANNEL_TYPE);
        createEReference(channelTypeEClass, CHANNEL_TYPE__ELEMENT_TYPE);
        createEAttribute(channelTypeEClass, CHANNEL_TYPE__OPS);

        functionTypeEClass = createEClass(FUNCTION_TYPE);
        createEReference(functionTypeEClass, FUNCTION_TYPE__RESULT_TYPE);
        createEReference(functionTypeEClass, FUNCTION_TYPE__PARAMETER_TYPES);

        typeReferenceEClass = createEClass(TYPE_REFERENCE);
        createEReference(typeReferenceEClass, TYPE_REFERENCE__TYPE);

        expressionEClass = createEClass(EXPRESSION);
        createEReference(expressionEClass, EXPRESSION__TYPE);

        boolLiteralEClass = createEClass(BOOL_LITERAL);
        createEAttribute(boolLiteralEClass, BOOL_LITERAL__VALUE);

        intNumberEClass = createEClass(INT_NUMBER);
        createEAttribute(intNumberEClass, INT_NUMBER__VALUE);

        realNumberEClass = createEClass(REAL_NUMBER);
        createEAttribute(realNumberEClass, REAL_NUMBER__VALUE);

        stringLiteralEClass = createEClass(STRING_LITERAL);
        createEAttribute(stringLiteralEClass, STRING_LITERAL__VALUE);

        tupleExpressionEClass = createEClass(TUPLE_EXPRESSION);
        createEReference(tupleExpressionEClass, TUPLE_EXPRESSION__FIELDS);

        listExpressionEClass = createEClass(LIST_EXPRESSION);
        createEReference(listExpressionEClass, LIST_EXPRESSION__ELEMENTS);

        setExpressionEClass = createEClass(SET_EXPRESSION);
        createEReference(setExpressionEClass, SET_EXPRESSION__ELEMENTS);

        matrixExpressionEClass = createEClass(MATRIX_EXPRESSION);
        createEReference(matrixExpressionEClass, MATRIX_EXPRESSION__ROWS);

        matrixRowEClass = createEClass(MATRIX_ROW);
        createEReference(matrixRowEClass, MATRIX_ROW__ELEMENTS);

        dictionaryExpressionEClass = createEClass(DICTIONARY_EXPRESSION);
        createEReference(dictionaryExpressionEClass, DICTIONARY_EXPRESSION__PAIRS);

        dictionaryPairEClass = createEClass(DICTIONARY_PAIR);
        createEReference(dictionaryPairEClass, DICTIONARY_PAIR__KEY);
        createEReference(dictionaryPairEClass, DICTIONARY_PAIR__VALUE);

        variableReferenceEClass = createEClass(VARIABLE_REFERENCE);
        createEReference(variableReferenceEClass, VARIABLE_REFERENCE__VARIABLE);

        constantReferenceEClass = createEClass(CONSTANT_REFERENCE);
        createEReference(constantReferenceEClass, CONSTANT_REFERENCE__CONSTANT);

        timeLiteralEClass = createEClass(TIME_LITERAL);

        unaryExpressionEClass = createEClass(UNARY_EXPRESSION);
        createEReference(unaryExpressionEClass, UNARY_EXPRESSION__CHILD);
        createEAttribute(unaryExpressionEClass, UNARY_EXPRESSION__OP);

        binaryExpressionEClass = createEClass(BINARY_EXPRESSION);
        createEReference(binaryExpressionEClass, BINARY_EXPRESSION__LEFT);
        createEReference(binaryExpressionEClass, BINARY_EXPRESSION__RIGHT);
        createEAttribute(binaryExpressionEClass, BINARY_EXPRESSION__OP);

        callExpressionEClass = createEClass(CALL_EXPRESSION);
        createEReference(callExpressionEClass, CALL_EXPRESSION__FUNCTION);
        createEReference(callExpressionEClass, CALL_EXPRESSION__ARGUMENTS);
        createEReference(callExpressionEClass, CALL_EXPRESSION__NAME);

        functionReferenceEClass = createEClass(FUNCTION_REFERENCE);
        createEReference(functionReferenceEClass, FUNCTION_REFERENCE__FUNCTION);

        baseFunctionReferenceEClass = createEClass(BASE_FUNCTION_REFERENCE);

        stdLibFunctionReferenceEClass = createEClass(STD_LIB_FUNCTION_REFERENCE);
        createEAttribute(stdLibFunctionReferenceEClass, STD_LIB_FUNCTION_REFERENCE__FUNCTION);

        sliceExpressionEClass = createEClass(SLICE_EXPRESSION);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__START);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__END);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__STEP);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__SOURCE);

        fieldReferenceEClass = createEClass(FIELD_REFERENCE);
        createEReference(fieldReferenceEClass, FIELD_REFERENCE__FIELD);

        statementEClass = createEClass(STATEMENT);

        breakStatementEClass = createEClass(BREAK_STATEMENT);

        continueStatementEClass = createEClass(CONTINUE_STATEMENT);

        passStatementEClass = createEClass(PASS_STATEMENT);

        exitStatementEClass = createEClass(EXIT_STATEMENT);
        createEReference(exitStatementEClass, EXIT_STATEMENT__VALUE);

        returnStatementEClass = createEClass(RETURN_STATEMENT);
        createEReference(returnStatementEClass, RETURN_STATEMENT__VALUE);

        delayStatementEClass = createEClass(DELAY_STATEMENT);
        createEReference(delayStatementEClass, DELAY_STATEMENT__LENGTH);

        whileStatementEClass = createEClass(WHILE_STATEMENT);
        createEReference(whileStatementEClass, WHILE_STATEMENT__CONDITION);
        createEReference(whileStatementEClass, WHILE_STATEMENT__BODY);

        ifStatementEClass = createEClass(IF_STATEMENT);
        createEReference(ifStatementEClass, IF_STATEMENT__CASES);

        ifCaseEClass = createEClass(IF_CASE);
        createEReference(ifCaseEClass, IF_CASE__CONDITION);
        createEReference(ifCaseEClass, IF_CASE__BODY);

        writeStatementEClass = createEClass(WRITE_STATEMENT);
        createEReference(writeStatementEClass, WRITE_STATEMENT__VALUES);
        createEAttribute(writeStatementEClass, WRITE_STATEMENT__ADD_NEWLINE);

        assignmentStatementEClass = createEClass(ASSIGNMENT_STATEMENT);
        createEReference(assignmentStatementEClass, ASSIGNMENT_STATEMENT__LHS);
        createEReference(assignmentStatementEClass, ASSIGNMENT_STATEMENT__RHS);

        communicationStatementEClass = createEClass(COMMUNICATION_STATEMENT);
        createEReference(communicationStatementEClass, COMMUNICATION_STATEMENT__CHANNEL);
        createEReference(communicationStatementEClass, COMMUNICATION_STATEMENT__DATA);

        forStatementEClass = createEClass(FOR_STATEMENT);
        createEReference(forStatementEClass, FOR_STATEMENT__BODY);
        createEReference(forStatementEClass, FOR_STATEMENT__UNWINDS);

        createCaseEClass = createEClass(CREATE_CASE);

        runStatementEClass = createEClass(RUN_STATEMENT);
        createEReference(runStatementEClass, RUN_STATEMENT__CASES);
        createEAttribute(runStatementEClass, RUN_STATEMENT__START_ONLY);

        selectStatementEClass = createEClass(SELECT_STATEMENT);
        createEReference(selectStatementEClass, SELECT_STATEMENT__CASES);

        selectCaseEClass = createEClass(SELECT_CASE);
        createEReference(selectCaseEClass, SELECT_CASE__BODY);
        createEReference(selectCaseEClass, SELECT_CASE__GUARD);

        iteratedCreateCaseEClass = createEClass(ITERATED_CREATE_CASE);
        createEReference(iteratedCreateCaseEClass, ITERATED_CREATE_CASE__UNWINDS);
        createEReference(iteratedCreateCaseEClass, ITERATED_CREATE_CASE__INSTANCES);

        iteratedSelectCaseEClass = createEClass(ITERATED_SELECT_CASE);
        createEReference(iteratedSelectCaseEClass, ITERATED_SELECT_CASE__UNWINDS);

        specificationEClass = createEClass(SPECIFICATION);
        createEReference(specificationEClass, SPECIFICATION__DECLARATIONS);

        declarationEClass = createEClass(DECLARATION);
        createEAttribute(declarationEClass, DECLARATION__NAME);

        typeDeclarationEClass = createEClass(TYPE_DECLARATION);
        createEReference(typeDeclarationEClass, TYPE_DECLARATION__TYPE);

        constantDeclarationEClass = createEClass(CONSTANT_DECLARATION);
        createEReference(constantDeclarationEClass, CONSTANT_DECLARATION__TYPE);
        createEReference(constantDeclarationEClass, CONSTANT_DECLARATION__VALUE);

        processDeclarationEClass = createEClass(PROCESS_DECLARATION);

        functionDeclarationEClass = createEClass(FUNCTION_DECLARATION);
        createEReference(functionDeclarationEClass, FUNCTION_DECLARATION__RETURN_TYPE);

        modelDeclarationEClass = createEClass(MODEL_DECLARATION);

        variableDeclarationEClass = createEClass(VARIABLE_DECLARATION);
        createEReference(variableDeclarationEClass, VARIABLE_DECLARATION__INITIAL_VALUE);
        createEReference(variableDeclarationEClass, VARIABLE_DECLARATION__TYPE);
        createEAttribute(variableDeclarationEClass, VARIABLE_DECLARATION__PARAMETER);
        createEAttribute(variableDeclarationEClass, VARIABLE_DECLARATION__NAME);

        behaviourDeclarationEClass = createEClass(BEHAVIOUR_DECLARATION);
        createEReference(behaviourDeclarationEClass, BEHAVIOUR_DECLARATION__VARIABLES);
        createEReference(behaviourDeclarationEClass, BEHAVIOUR_DECLARATION__STATEMENTS);

        receiveStatementEClass = createEClass(RECEIVE_STATEMENT);

        sendStatementEClass = createEClass(SEND_STATEMENT);

        enumValueReferenceEClass = createEClass(ENUM_VALUE_REFERENCE);
        createEReference(enumValueReferenceEClass, ENUM_VALUE_REFERENCE__VALUE);

        readCallExpressionEClass = createEClass(READ_CALL_EXPRESSION);
        createEReference(readCallExpressionEClass, READ_CALL_EXPRESSION__FILE);
        createEReference(readCallExpressionEClass, READ_CALL_EXPRESSION__LOAD_TYPE);

        unwindEClass = createEClass(UNWIND);
        createEReference(unwindEClass, UNWIND__SOURCE);
        createEReference(unwindEClass, UNWIND__VARIABLES);

        processInstanceEClass = createEClass(PROCESS_INSTANCE);
        createEReference(processInstanceEClass, PROCESS_INSTANCE__CALL);
        createEReference(processInstanceEClass, PROCESS_INSTANCE__VAR);

        processTypeEClass = createEClass(PROCESS_TYPE);

        processReferenceEClass = createEClass(PROCESS_REFERENCE);
        createEReference(processReferenceEClass, PROCESS_REFERENCE__PROCESS);

        unresolvedReferenceEClass = createEClass(UNRESOLVED_REFERENCE);
        createEAttribute(unresolvedReferenceEClass, UNRESOLVED_REFERENCE__NAME);

        unresolvedTypeEClass = createEClass(UNRESOLVED_TYPE);
        createEAttribute(unresolvedTypeEClass, UNRESOLVED_TYPE__NAME);

        timerTypeEClass = createEClass(TIMER_TYPE);

        enumDeclarationEClass = createEClass(ENUM_DECLARATION);
        createEReference(enumDeclarationEClass, ENUM_DECLARATION__VALUES);

        enumValueEClass = createEClass(ENUM_VALUE);
        createEAttribute(enumValueEClass, ENUM_VALUE__NAME);

        channelExpressionEClass = createEClass(CHANNEL_EXPRESSION);
        createEReference(channelExpressionEClass, CHANNEL_EXPRESSION__ELEMENT_TYPE);

        castExpressionEClass = createEClass(CAST_EXPRESSION);
        createEReference(castExpressionEClass, CAST_EXPRESSION__EXPRESSION);
        createEReference(castExpressionEClass, CAST_EXPRESSION__CAST_TYPE);

        closeStatementEClass = createEClass(CLOSE_STATEMENT);
        createEReference(closeStatementEClass, CLOSE_STATEMENT__HANDLE);

        finishStatementEClass = createEClass(FINISH_STATEMENT);
        createEReference(finishStatementEClass, FINISH_STATEMENT__INSTANCES);

        modelReferenceEClass = createEClass(MODEL_REFERENCE);
        createEReference(modelReferenceEClass, MODEL_REFERENCE__MODEL);

        xperDeclarationEClass = createEClass(XPER_DECLARATION);

        computeDeclarationEClass = createEClass(COMPUTE_DECLARATION);
        createEReference(computeDeclarationEClass, COMPUTE_DECLARATION__RETURN_TYPE);

        computeTypeEClass = createEClass(COMPUTE_TYPE);
        createEReference(computeTypeEClass, COMPUTE_TYPE__PARAMETER_TYPES);
        createEReference(computeTypeEClass, COMPUTE_TYPE__EXIT_TYPE);

        modelTypeEClass = createEClass(MODEL_TYPE);

        // Create enums
        channelOpsEEnum = createEEnum(CHANNEL_OPS);
        unaryOperatorsEEnum = createEEnum(UNARY_OPERATORS);
        binaryOperatorsEEnum = createEEnum(BINARY_OPERATORS);
        stdLibFunctionsEEnum = createEEnum(STD_LIB_FUNCTIONS);

        // Create data types
        chiIdentifierEDataType = createEDataType(CHI_IDENTIFIER);
        chiRealNumberEDataType = createEDataType(CHI_REAL_NUMBER);
        chiNumberEDataType = createEDataType(CHI_NUMBER);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents()
    {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        PositionPackage thePositionPackage = (PositionPackage)EPackage.Registry.INSTANCE.getEPackage(PositionPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        typeEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        voidTypeEClass.getESuperTypes().add(this.getType());
        boolTypeEClass.getESuperTypes().add(this.getType());
        instanceTypeEClass.getESuperTypes().add(this.getType());
        intTypeEClass.getESuperTypes().add(this.getType());
        stringTypeEClass.getESuperTypes().add(this.getType());
        realTypeEClass.getESuperTypes().add(this.getType());
        fileTypeEClass.getESuperTypes().add(this.getType());
        setTypeEClass.getESuperTypes().add(this.getType());
        listTypeEClass.getESuperTypes().add(this.getType());
        dictTypeEClass.getESuperTypes().add(this.getType());
        matrixTypeEClass.getESuperTypes().add(this.getType());
        tupleTypeEClass.getESuperTypes().add(this.getType());
        tupleFieldEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        distributionTypeEClass.getESuperTypes().add(this.getType());
        enumTypeReferenceEClass.getESuperTypes().add(this.getType());
        channelTypeEClass.getESuperTypes().add(this.getType());
        functionTypeEClass.getESuperTypes().add(this.getType());
        typeReferenceEClass.getESuperTypes().add(this.getType());
        expressionEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        boolLiteralEClass.getESuperTypes().add(this.getExpression());
        intNumberEClass.getESuperTypes().add(this.getExpression());
        realNumberEClass.getESuperTypes().add(this.getExpression());
        stringLiteralEClass.getESuperTypes().add(this.getExpression());
        tupleExpressionEClass.getESuperTypes().add(this.getExpression());
        listExpressionEClass.getESuperTypes().add(this.getExpression());
        setExpressionEClass.getESuperTypes().add(this.getExpression());
        matrixExpressionEClass.getESuperTypes().add(this.getExpression());
        matrixRowEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        dictionaryExpressionEClass.getESuperTypes().add(this.getExpression());
        dictionaryPairEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        variableReferenceEClass.getESuperTypes().add(this.getExpression());
        constantReferenceEClass.getESuperTypes().add(this.getExpression());
        timeLiteralEClass.getESuperTypes().add(this.getExpression());
        unaryExpressionEClass.getESuperTypes().add(this.getExpression());
        binaryExpressionEClass.getESuperTypes().add(this.getExpression());
        callExpressionEClass.getESuperTypes().add(this.getExpression());
        functionReferenceEClass.getESuperTypes().add(this.getBaseFunctionReference());
        baseFunctionReferenceEClass.getESuperTypes().add(this.getExpression());
        stdLibFunctionReferenceEClass.getESuperTypes().add(this.getBaseFunctionReference());
        sliceExpressionEClass.getESuperTypes().add(this.getExpression());
        fieldReferenceEClass.getESuperTypes().add(this.getExpression());
        statementEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        breakStatementEClass.getESuperTypes().add(this.getStatement());
        continueStatementEClass.getESuperTypes().add(this.getStatement());
        passStatementEClass.getESuperTypes().add(this.getStatement());
        exitStatementEClass.getESuperTypes().add(this.getStatement());
        returnStatementEClass.getESuperTypes().add(this.getStatement());
        delayStatementEClass.getESuperTypes().add(this.getStatement());
        whileStatementEClass.getESuperTypes().add(this.getStatement());
        ifStatementEClass.getESuperTypes().add(this.getStatement());
        ifCaseEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        writeStatementEClass.getESuperTypes().add(this.getStatement());
        assignmentStatementEClass.getESuperTypes().add(this.getStatement());
        communicationStatementEClass.getESuperTypes().add(this.getStatement());
        forStatementEClass.getESuperTypes().add(this.getStatement());
        createCaseEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        runStatementEClass.getESuperTypes().add(this.getStatement());
        selectStatementEClass.getESuperTypes().add(this.getStatement());
        selectCaseEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        iteratedCreateCaseEClass.getESuperTypes().add(this.getCreateCase());
        iteratedSelectCaseEClass.getESuperTypes().add(this.getSelectCase());
        declarationEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        typeDeclarationEClass.getESuperTypes().add(this.getDeclaration());
        constantDeclarationEClass.getESuperTypes().add(this.getDeclaration());
        processDeclarationEClass.getESuperTypes().add(this.getComputeDeclaration());
        functionDeclarationEClass.getESuperTypes().add(this.getBehaviourDeclaration());
        modelDeclarationEClass.getESuperTypes().add(this.getComputeDeclaration());
        variableDeclarationEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        behaviourDeclarationEClass.getESuperTypes().add(this.getDeclaration());
        receiveStatementEClass.getESuperTypes().add(this.getCommunicationStatement());
        sendStatementEClass.getESuperTypes().add(this.getCommunicationStatement());
        enumValueReferenceEClass.getESuperTypes().add(this.getExpression());
        readCallExpressionEClass.getESuperTypes().add(this.getExpression());
        unwindEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        processInstanceEClass.getESuperTypes().add(this.getCreateCase());
        processTypeEClass.getESuperTypes().add(this.getComputeType());
        processReferenceEClass.getESuperTypes().add(this.getExpression());
        unresolvedReferenceEClass.getESuperTypes().add(this.getExpression());
        unresolvedTypeEClass.getESuperTypes().add(this.getType());
        timerTypeEClass.getESuperTypes().add(this.getType());
        enumDeclarationEClass.getESuperTypes().add(this.getDeclaration());
        enumValueEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        channelExpressionEClass.getESuperTypes().add(this.getExpression());
        castExpressionEClass.getESuperTypes().add(this.getExpression());
        closeStatementEClass.getESuperTypes().add(this.getStatement());
        finishStatementEClass.getESuperTypes().add(this.getStatement());
        modelReferenceEClass.getESuperTypes().add(this.getExpression());
        xperDeclarationEClass.getESuperTypes().add(this.getBehaviourDeclaration());
        computeDeclarationEClass.getESuperTypes().add(this.getBehaviourDeclaration());
        computeTypeEClass.getESuperTypes().add(this.getType());
        modelTypeEClass.getESuperTypes().add(this.getComputeType());

        // Initialize classes and features; add operations and parameters
        initEClass(typeEClass, Type.class, "Type", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(voidTypeEClass, VoidType.class, "VoidType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(boolTypeEClass, BoolType.class, "BoolType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(instanceTypeEClass, InstanceType.class, "InstanceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(intTypeEClass, IntType.class, "IntType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(stringTypeEClass, StringType.class, "StringType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(realTypeEClass, RealType.class, "RealType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(fileTypeEClass, FileType.class, "FileType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(setTypeEClass, SetType.class, "SetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSetType_ElementType(), this.getType(), null, "elementType", null, 1, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(listTypeEClass, ListType.class, "ListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getListType_ElementType(), this.getType(), null, "elementType", null, 1, 1, ListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getListType_InitialLength(), this.getExpression(), null, "initialLength", null, 0, 1, ListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dictTypeEClass, DictType.class, "DictType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDictType_KeyType(), this.getType(), null, "keyType", null, 1, 1, DictType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDictType_ValueType(), this.getType(), null, "valueType", null, 1, 1, DictType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(matrixTypeEClass, MatrixType.class, "MatrixType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMatrixType_RowSize(), this.getExpression(), null, "rowSize", null, 1, 1, MatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMatrixType_ColumnSize(), this.getExpression(), null, "columnSize", null, 1, 1, MatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tupleTypeEClass, TupleType.class, "TupleType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTupleType_Fields(), this.getTupleField(), null, "fields", null, 2, -1, TupleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tupleFieldEClass, TupleField.class, "TupleField", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTupleField_Type(), this.getType(), null, "type", null, 1, 1, TupleField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTupleField_Name(), this.getChiIdentifier(), "name", null, 0, 1, TupleField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(distributionTypeEClass, DistributionType.class, "DistributionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDistributionType_ResultType(), this.getType(), null, "resultType", null, 1, 1, DistributionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(enumTypeReferenceEClass, EnumTypeReference.class, "EnumTypeReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEnumTypeReference_Type(), this.getEnumDeclaration(), null, "type", null, 1, 1, EnumTypeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(channelTypeEClass, ChannelType.class, "ChannelType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getChannelType_ElementType(), this.getType(), null, "elementType", null, 1, 1, ChannelType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getChannelType_Ops(), this.getChannelOps(), "ops", null, 1, 1, ChannelType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(functionTypeEClass, FunctionType.class, "FunctionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFunctionType_ResultType(), this.getType(), null, "resultType", null, 1, 1, FunctionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFunctionType_ParameterTypes(), this.getType(), null, "parameterTypes", null, 0, -1, FunctionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(typeReferenceEClass, TypeReference.class, "TypeReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTypeReference_Type(), this.getTypeDeclaration(), null, "type", null, 1, 1, TypeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(expressionEClass, Expression.class, "Expression", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExpression_Type(), this.getType(), null, "type", null, 0, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(boolLiteralEClass, BoolLiteral.class, "BoolLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBoolLiteral_Value(), ecorePackage.getEBoolean(), "value", null, 1, 1, BoolLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(intNumberEClass, IntNumber.class, "IntNumber", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getIntNumber_Value(), this.getChiNumber(), "value", null, 1, 1, IntNumber.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(realNumberEClass, RealNumber.class, "RealNumber", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRealNumber_Value(), this.getChiRealNumber(), "value", null, 1, 1, RealNumber.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(stringLiteralEClass, StringLiteral.class, "StringLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStringLiteral_Value(), ecorePackage.getEString(), "value", null, 1, 1, StringLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tupleExpressionEClass, TupleExpression.class, "TupleExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTupleExpression_Fields(), this.getExpression(), null, "fields", null, 2, -1, TupleExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(listExpressionEClass, ListExpression.class, "ListExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getListExpression_Elements(), this.getExpression(), null, "elements", null, 0, -1, ListExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(setExpressionEClass, SetExpression.class, "SetExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSetExpression_Elements(), this.getExpression(), null, "elements", null, 0, -1, SetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(matrixExpressionEClass, MatrixExpression.class, "MatrixExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMatrixExpression_Rows(), this.getMatrixRow(), null, "rows", null, 1, -1, MatrixExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(matrixRowEClass, MatrixRow.class, "MatrixRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMatrixRow_Elements(), this.getExpression(), null, "elements", null, 1, -1, MatrixRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dictionaryExpressionEClass, DictionaryExpression.class, "DictionaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDictionaryExpression_Pairs(), this.getDictionaryPair(), null, "pairs", null, 0, -1, DictionaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dictionaryPairEClass, DictionaryPair.class, "DictionaryPair", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDictionaryPair_Key(), this.getExpression(), null, "key", null, 1, 1, DictionaryPair.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDictionaryPair_Value(), this.getExpression(), null, "value", null, 1, 1, DictionaryPair.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(variableReferenceEClass, VariableReference.class, "VariableReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getVariableReference_Variable(), this.getVariableDeclaration(), null, "variable", null, 1, 1, VariableReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(constantReferenceEClass, ConstantReference.class, "ConstantReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConstantReference_Constant(), this.getConstantDeclaration(), null, "constant", null, 1, 1, ConstantReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(timeLiteralEClass, TimeLiteral.class, "TimeLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(unaryExpressionEClass, UnaryExpression.class, "UnaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUnaryExpression_Child(), this.getExpression(), null, "child", null, 1, 1, UnaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUnaryExpression_Op(), this.getUnaryOperators(), "op", null, 1, 1, UnaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(binaryExpressionEClass, BinaryExpression.class, "BinaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBinaryExpression_Left(), this.getExpression(), null, "left", null, 1, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryExpression_Right(), this.getExpression(), null, "right", null, 1, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryExpression_Op(), this.getBinaryOperators(), "op", null, 1, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(callExpressionEClass, CallExpression.class, "CallExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCallExpression_Function(), this.getExpression(), null, "function", null, 1, 1, CallExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCallExpression_Arguments(), this.getExpression(), null, "arguments", null, 0, -1, CallExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCallExpression_Name(), this.getExpression(), null, "name", null, 0, 1, CallExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(functionReferenceEClass, FunctionReference.class, "FunctionReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFunctionReference_Function(), this.getFunctionDeclaration(), null, "function", null, 1, 1, FunctionReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(baseFunctionReferenceEClass, BaseFunctionReference.class, "BaseFunctionReference", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(stdLibFunctionReferenceEClass, StdLibFunctionReference.class, "StdLibFunctionReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStdLibFunctionReference_Function(), this.getStdLibFunctions(), "function", null, 1, 1, StdLibFunctionReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sliceExpressionEClass, SliceExpression.class, "SliceExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSliceExpression_Start(), this.getExpression(), null, "start", null, 0, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSliceExpression_End(), this.getExpression(), null, "end", null, 0, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSliceExpression_Step(), this.getExpression(), null, "step", null, 0, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSliceExpression_Source(), this.getExpression(), null, "source", null, 1, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(fieldReferenceEClass, FieldReference.class, "FieldReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFieldReference_Field(), this.getTupleField(), null, "field", null, 1, 1, FieldReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(statementEClass, Statement.class, "Statement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(breakStatementEClass, BreakStatement.class, "BreakStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(continueStatementEClass, ContinueStatement.class, "ContinueStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(passStatementEClass, PassStatement.class, "PassStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(exitStatementEClass, ExitStatement.class, "ExitStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExitStatement_Value(), this.getExpression(), null, "value", null, 0, 1, ExitStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(returnStatementEClass, ReturnStatement.class, "ReturnStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getReturnStatement_Value(), this.getExpression(), null, "value", null, 1, 1, ReturnStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(delayStatementEClass, DelayStatement.class, "DelayStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDelayStatement_Length(), this.getExpression(), null, "length", null, 1, 1, DelayStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(whileStatementEClass, WhileStatement.class, "WhileStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getWhileStatement_Condition(), this.getExpression(), null, "condition", null, 1, 1, WhileStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWhileStatement_Body(), this.getStatement(), null, "body", null, 1, -1, WhileStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ifStatementEClass, IfStatement.class, "IfStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIfStatement_Cases(), this.getIfCase(), null, "cases", null, 1, -1, IfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ifCaseEClass, IfCase.class, "IfCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIfCase_Condition(), this.getExpression(), null, "condition", null, 0, 1, IfCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfCase_Body(), this.getStatement(), null, "body", null, 1, -1, IfCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(writeStatementEClass, WriteStatement.class, "WriteStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getWriteStatement_Values(), this.getExpression(), null, "values", null, 1, -1, WriteStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWriteStatement_AddNewline(), ecorePackage.getEBoolean(), "addNewline", null, 1, 1, WriteStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(assignmentStatementEClass, AssignmentStatement.class, "AssignmentStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssignmentStatement_Lhs(), this.getExpression(), null, "lhs", null, 1, 1, AssignmentStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAssignmentStatement_Rhs(), this.getExpression(), null, "rhs", null, 1, 1, AssignmentStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(communicationStatementEClass, CommunicationStatement.class, "CommunicationStatement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCommunicationStatement_Channel(), this.getExpression(), null, "channel", null, 1, 1, CommunicationStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCommunicationStatement_Data(), this.getExpression(), null, "data", null, 0, 1, CommunicationStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(forStatementEClass, ForStatement.class, "ForStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getForStatement_Body(), this.getStatement(), null, "body", null, 1, -1, ForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getForStatement_Unwinds(), this.getUnwind(), null, "unwinds", null, 1, -1, ForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(createCaseEClass, CreateCase.class, "CreateCase", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(runStatementEClass, RunStatement.class, "RunStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRunStatement_Cases(), this.getCreateCase(), null, "cases", null, 1, -1, RunStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRunStatement_StartOnly(), ecorePackage.getEBoolean(), "startOnly", null, 1, 1, RunStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(selectStatementEClass, SelectStatement.class, "SelectStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSelectStatement_Cases(), this.getSelectCase(), null, "cases", null, 1, -1, SelectStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(selectCaseEClass, SelectCase.class, "SelectCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSelectCase_Body(), this.getStatement(), null, "body", null, 1, -1, SelectCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSelectCase_Guard(), this.getExpression(), null, "guard", null, 0, 1, SelectCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(iteratedCreateCaseEClass, IteratedCreateCase.class, "IteratedCreateCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIteratedCreateCase_Unwinds(), this.getUnwind(), null, "unwinds", null, 1, -1, IteratedCreateCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIteratedCreateCase_Instances(), this.getCreateCase(), null, "instances", null, 1, -1, IteratedCreateCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(iteratedSelectCaseEClass, IteratedSelectCase.class, "IteratedSelectCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIteratedSelectCase_Unwinds(), this.getUnwind(), null, "unwinds", null, 1, -1, IteratedSelectCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(specificationEClass, Specification.class, "Specification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSpecification_Declarations(), this.getDeclaration(), null, "declarations", null, 0, -1, Specification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(declarationEClass, Declaration.class, "Declaration", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDeclaration_Name(), this.getChiIdentifier(), "name", null, 1, 1, Declaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(typeDeclarationEClass, TypeDeclaration.class, "TypeDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTypeDeclaration_Type(), this.getType(), null, "type", null, 1, 1, TypeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(constantDeclarationEClass, ConstantDeclaration.class, "ConstantDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConstantDeclaration_Type(), this.getType(), null, "type", null, 1, 1, ConstantDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConstantDeclaration_Value(), this.getExpression(), null, "value", null, 1, 1, ConstantDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processDeclarationEClass, ProcessDeclaration.class, "ProcessDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(functionDeclarationEClass, FunctionDeclaration.class, "FunctionDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFunctionDeclaration_ReturnType(), this.getType(), null, "returnType", null, 1, 1, FunctionDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(modelDeclarationEClass, ModelDeclaration.class, "ModelDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(variableDeclarationEClass, VariableDeclaration.class, "VariableDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getVariableDeclaration_InitialValue(), this.getExpression(), null, "initialValue", null, 0, 1, VariableDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getVariableDeclaration_Type(), this.getType(), null, "type", null, 0, 1, VariableDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getVariableDeclaration_Parameter(), ecorePackage.getEBoolean(), "parameter", null, 1, 1, VariableDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getVariableDeclaration_Name(), this.getChiIdentifier(), "name", null, 1, 1, VariableDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(behaviourDeclarationEClass, BehaviourDeclaration.class, "BehaviourDeclaration", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBehaviourDeclaration_Variables(), this.getVariableDeclaration(), null, "variables", null, 0, -1, BehaviourDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBehaviourDeclaration_Statements(), this.getStatement(), null, "statements", null, 1, -1, BehaviourDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(receiveStatementEClass, ReceiveStatement.class, "ReceiveStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(sendStatementEClass, SendStatement.class, "SendStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(enumValueReferenceEClass, EnumValueReference.class, "EnumValueReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEnumValueReference_Value(), this.getEnumValue(), null, "value", null, 1, 1, EnumValueReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(readCallExpressionEClass, ReadCallExpression.class, "ReadCallExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getReadCallExpression_File(), this.getExpression(), null, "file", null, 0, 1, ReadCallExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getReadCallExpression_LoadType(), this.getType(), null, "loadType", null, 1, 1, ReadCallExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(unwindEClass, Unwind.class, "Unwind", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUnwind_Source(), this.getExpression(), null, "source", null, 1, 1, Unwind.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnwind_Variables(), this.getVariableDeclaration(), null, "variables", null, 1, -1, Unwind.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processInstanceEClass, ProcessInstance.class, "ProcessInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProcessInstance_Call(), this.getExpression(), null, "call", null, 1, 1, ProcessInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessInstance_Var(), this.getExpression(), null, "var", null, 0, 1, ProcessInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processTypeEClass, ProcessType.class, "ProcessType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(processReferenceEClass, ProcessReference.class, "ProcessReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProcessReference_Process(), this.getProcessDeclaration(), null, "process", null, 1, 1, ProcessReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(unresolvedReferenceEClass, UnresolvedReference.class, "UnresolvedReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getUnresolvedReference_Name(), this.getChiIdentifier(), "name", null, 1, 1, UnresolvedReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(unresolvedTypeEClass, UnresolvedType.class, "UnresolvedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getUnresolvedType_Name(), this.getChiIdentifier(), "name", null, 1, 1, UnresolvedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(timerTypeEClass, TimerType.class, "TimerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(enumDeclarationEClass, EnumDeclaration.class, "EnumDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEnumDeclaration_Values(), this.getEnumValue(), null, "values", null, 1, -1, EnumDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(enumValueEClass, EnumValue.class, "EnumValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEnumValue_Name(), this.getChiIdentifier(), "name", null, 1, 1, EnumValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(channelExpressionEClass, ChannelExpression.class, "ChannelExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getChannelExpression_ElementType(), this.getType(), null, "elementType", null, 1, 1, ChannelExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(castExpressionEClass, CastExpression.class, "CastExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCastExpression_Expression(), this.getExpression(), null, "expression", null, 1, 1, CastExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCastExpression_CastType(), this.getType(), null, "castType", null, 1, 1, CastExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(closeStatementEClass, CloseStatement.class, "CloseStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCloseStatement_Handle(), this.getExpression(), null, "handle", null, 1, 1, CloseStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(finishStatementEClass, FinishStatement.class, "FinishStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFinishStatement_Instances(), this.getExpression(), null, "instances", null, 1, -1, FinishStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(modelReferenceEClass, ModelReference.class, "ModelReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getModelReference_Model(), this.getModelDeclaration(), null, "model", null, 1, 1, ModelReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(xperDeclarationEClass, XperDeclaration.class, "XperDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(computeDeclarationEClass, ComputeDeclaration.class, "ComputeDeclaration", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComputeDeclaration_ReturnType(), this.getType(), null, "returnType", null, 0, 1, ComputeDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(computeTypeEClass, ComputeType.class, "ComputeType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComputeType_ParameterTypes(), this.getType(), null, "parameterTypes", null, 0, -1, ComputeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComputeType_ExitType(), this.getType(), null, "exitType", null, 0, 1, ComputeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(modelTypeEClass, ModelType.class, "ModelType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Initialize enums and add enum literals
        initEEnum(channelOpsEEnum, ChannelOps.class, "ChannelOps");
        addEEnumLiteral(channelOpsEEnum, ChannelOps.RECEIVE);
        addEEnumLiteral(channelOpsEEnum, ChannelOps.SEND);
        addEEnumLiteral(channelOpsEEnum, ChannelOps.SEND_RECEIVE);

        initEEnum(unaryOperatorsEEnum, UnaryOperators.class, "UnaryOperators");
        addEEnumLiteral(unaryOperatorsEEnum, UnaryOperators.INVERSE);
        addEEnumLiteral(unaryOperatorsEEnum, UnaryOperators.NEGATE);
        addEEnumLiteral(unaryOperatorsEEnum, UnaryOperators.SAMPLE);
        addEEnumLiteral(unaryOperatorsEEnum, UnaryOperators.PLUS);

        initEEnum(binaryOperatorsEEnum, BinaryOperators.class, "BinaryOperators");
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.ADDITION);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.CONJUNCTION);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.DISJUNCTION);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.DIVISION);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.ELEMENT_TEST);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.EQUAL);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.FIELD_PROJECTION);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.FLOOR_DIVISION);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.GREATER_EQUAL);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.GREATER_THAN);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.LESS_THAN);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.LESS_EQUAL);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.MODULUS);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.MULTIPLICATION);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.NOT_EQUAL);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.POWER);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.PROJECTION);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.SUBSET);
        addEEnumLiteral(binaryOperatorsEEnum, BinaryOperators.SUBTRACTION);

        initEEnum(stdLibFunctionsEEnum, StdLibFunctions.class, "StdLibFunctions");
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ABS);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ACOS);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ACOSH);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ASIN);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ASINH);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ATAN);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ATANH);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.BERNOULLI);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.EMPTY);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ENUMERATE);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.EOF);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.EOL);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ERLANG);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.EXP);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.EXPONENTIAL);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.FINISHED);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.FLOOR);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.GAMMA);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.GEOMETRIC);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.INSERT);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.LN);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.LOG);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.LOG_NORMAL);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.MAX);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.MIN);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.NEWLINES);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.NORMAL);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.OPEN);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.POISSON);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.POP);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.RANDOM);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.RANGE);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.READY);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.ROUND);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.SIGN);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.SIN);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.SINH);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.SIZE);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.SORT);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.SQRT);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.TAN);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.TANH);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.TRIANGLE);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.UNIFORM);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.WEIBULL);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.BETA);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.BINOMIAL);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.CBRT);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.CEIL);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.CONSTANT);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.COS);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.COSH);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.DELETE);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.DICT_KEYS);
        addEEnumLiteral(stdLibFunctionsEEnum, StdLibFunctions.DICT_VALUES);

        // Initialize data types
        initEDataType(chiIdentifierEDataType, String.class, "ChiIdentifier", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(chiRealNumberEDataType, String.class, "ChiRealNumber", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(chiNumberEDataType, String.class, "ChiNumber", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createExtendedMetaDataAnnotations()
    {
        String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
        addAnnotation
          (chiRealNumberEDataType,
           source,
           new String[]
           {
               "name", "ChiRealNumber",
               "baseType", "string",
               "pattern", "(0|([1-9][0-9]*))((\\.[0-9]+)|((\\.[0-9]+)?[eE][\\-\\+]?[0-9]+))"
           });
        addAnnotation
          (chiNumberEDataType,
           source,
           new String[]
           {
               "name", "ChiNumber",
               "baseType", "string",
               "pattern", "0|([1-9][0-9]*)"
           });
    }

} //ChiPackageImpl
