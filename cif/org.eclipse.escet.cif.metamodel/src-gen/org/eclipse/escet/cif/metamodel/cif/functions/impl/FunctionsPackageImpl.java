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
package org.eclipse.escet.cif.metamodel.cif.functions.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;

import org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage;

import org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;

import org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsFactory;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;

import org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.print.PrintPackage;

import org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.types.TypesPackage;

import org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FunctionsPackageImpl extends EPackageImpl implements FunctionsPackage
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionParameterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass internalFunctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass externalFunctionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass breakFuncStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass continueFuncStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass assignmentFuncStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass whileFuncStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ifFuncStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass elifFuncStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass returnFuncStatementEClass = null;

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
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private FunctionsPackageImpl()
    {
        super(eNS_URI, FunctionsFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link FunctionsPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static FunctionsPackage init()
    {
        if (isInited) return (FunctionsPackage)EPackage.Registry.INSTANCE.getEPackage(FunctionsPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredFunctionsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        FunctionsPackageImpl theFunctionsPackage = registeredFunctionsPackage instanceof FunctionsPackageImpl ? (FunctionsPackageImpl)registeredFunctionsPackage : new FunctionsPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        PositionPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(CifPackage.eNS_URI);
        CifPackageImpl theCifPackage = (CifPackageImpl)(registeredPackage instanceof CifPackageImpl ? registeredPackage : CifPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeclarationsPackage.eNS_URI);
        DeclarationsPackageImpl theDeclarationsPackage = (DeclarationsPackageImpl)(registeredPackage instanceof DeclarationsPackageImpl ? registeredPackage : DeclarationsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AutomataPackage.eNS_URI);
        AutomataPackageImpl theAutomataPackage = (AutomataPackageImpl)(registeredPackage instanceof AutomataPackageImpl ? registeredPackage : AutomataPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        TypesPackageImpl theTypesPackage = (TypesPackageImpl)(registeredPackage instanceof TypesPackageImpl ? registeredPackage : TypesPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);
        ExpressionsPackageImpl theExpressionsPackage = (ExpressionsPackageImpl)(registeredPackage instanceof ExpressionsPackageImpl ? registeredPackage : ExpressionsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(CifsvgPackage.eNS_URI);
        CifsvgPackageImpl theCifsvgPackage = (CifsvgPackageImpl)(registeredPackage instanceof CifsvgPackageImpl ? registeredPackage : CifsvgPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(PrintPackage.eNS_URI);
        PrintPackageImpl thePrintPackage = (PrintPackageImpl)(registeredPackage instanceof PrintPackageImpl ? registeredPackage : PrintPackage.eINSTANCE);

        // Create package meta-data objects
        theFunctionsPackage.createPackageContents();
        theCifPackage.createPackageContents();
        theDeclarationsPackage.createPackageContents();
        theAutomataPackage.createPackageContents();
        theTypesPackage.createPackageContents();
        theExpressionsPackage.createPackageContents();
        theCifsvgPackage.createPackageContents();
        thePrintPackage.createPackageContents();

        // Initialize created meta-data
        theFunctionsPackage.initializePackageContents();
        theCifPackage.initializePackageContents();
        theDeclarationsPackage.initializePackageContents();
        theAutomataPackage.initializePackageContents();
        theTypesPackage.initializePackageContents();
        theExpressionsPackage.initializePackageContents();
        theCifsvgPackage.initializePackageContents();
        thePrintPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theFunctionsPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(FunctionsPackage.eNS_URI, theFunctionsPackage);
        return theFunctionsPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunction()
    {
        return functionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunction_ReturnTypes()
    {
        return (EReference)functionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunction_Parameters()
    {
        return (EReference)functionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunctionParameter()
    {
        return functionParameterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunctionParameter_Parameter()
    {
        return (EReference)functionParameterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInternalFunction()
    {
        return internalFunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getInternalFunction_Variables()
    {
        return (EReference)internalFunctionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getInternalFunction_Statements()
    {
        return (EReference)internalFunctionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getExternalFunction()
    {
        return externalFunctionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getExternalFunction_Function()
    {
        return (EAttribute)externalFunctionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunctionStatement()
    {
        return functionStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBreakFuncStatement()
    {
        return breakFuncStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getContinueFuncStatement()
    {
        return continueFuncStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssignmentFuncStatement()
    {
        return assignmentFuncStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignmentFuncStatement_Value()
    {
        return (EReference)assignmentFuncStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignmentFuncStatement_Addressable()
    {
        return (EReference)assignmentFuncStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getWhileFuncStatement()
    {
        return whileFuncStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getWhileFuncStatement_Guards()
    {
        return (EReference)whileFuncStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getWhileFuncStatement_Statements()
    {
        return (EReference)whileFuncStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIfFuncStatement()
    {
        return ifFuncStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfFuncStatement_Guards()
    {
        return (EReference)ifFuncStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfFuncStatement_Thens()
    {
        return (EReference)ifFuncStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfFuncStatement_Elses()
    {
        return (EReference)ifFuncStatementEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfFuncStatement_Elifs()
    {
        return (EReference)ifFuncStatementEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getElifFuncStatement()
    {
        return elifFuncStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElifFuncStatement_Guards()
    {
        return (EReference)elifFuncStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElifFuncStatement_Thens()
    {
        return (EReference)elifFuncStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReturnFuncStatement()
    {
        return returnFuncStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getReturnFuncStatement_Values()
    {
        return (EReference)returnFuncStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FunctionsFactory getFunctionsFactory()
    {
        return (FunctionsFactory)getEFactoryInstance();
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
        functionEClass = createEClass(FUNCTION);
        createEReference(functionEClass, FUNCTION__RETURN_TYPES);
        createEReference(functionEClass, FUNCTION__PARAMETERS);

        functionParameterEClass = createEClass(FUNCTION_PARAMETER);
        createEReference(functionParameterEClass, FUNCTION_PARAMETER__PARAMETER);

        internalFunctionEClass = createEClass(INTERNAL_FUNCTION);
        createEReference(internalFunctionEClass, INTERNAL_FUNCTION__VARIABLES);
        createEReference(internalFunctionEClass, INTERNAL_FUNCTION__STATEMENTS);

        externalFunctionEClass = createEClass(EXTERNAL_FUNCTION);
        createEAttribute(externalFunctionEClass, EXTERNAL_FUNCTION__FUNCTION);

        functionStatementEClass = createEClass(FUNCTION_STATEMENT);

        breakFuncStatementEClass = createEClass(BREAK_FUNC_STATEMENT);

        continueFuncStatementEClass = createEClass(CONTINUE_FUNC_STATEMENT);

        assignmentFuncStatementEClass = createEClass(ASSIGNMENT_FUNC_STATEMENT);
        createEReference(assignmentFuncStatementEClass, ASSIGNMENT_FUNC_STATEMENT__VALUE);
        createEReference(assignmentFuncStatementEClass, ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE);

        whileFuncStatementEClass = createEClass(WHILE_FUNC_STATEMENT);
        createEReference(whileFuncStatementEClass, WHILE_FUNC_STATEMENT__GUARDS);
        createEReference(whileFuncStatementEClass, WHILE_FUNC_STATEMENT__STATEMENTS);

        ifFuncStatementEClass = createEClass(IF_FUNC_STATEMENT);
        createEReference(ifFuncStatementEClass, IF_FUNC_STATEMENT__GUARDS);
        createEReference(ifFuncStatementEClass, IF_FUNC_STATEMENT__THENS);
        createEReference(ifFuncStatementEClass, IF_FUNC_STATEMENT__ELSES);
        createEReference(ifFuncStatementEClass, IF_FUNC_STATEMENT__ELIFS);

        elifFuncStatementEClass = createEClass(ELIF_FUNC_STATEMENT);
        createEReference(elifFuncStatementEClass, ELIF_FUNC_STATEMENT__GUARDS);
        createEReference(elifFuncStatementEClass, ELIF_FUNC_STATEMENT__THENS);

        returnFuncStatementEClass = createEClass(RETURN_FUNC_STATEMENT);
        createEReference(returnFuncStatementEClass, RETURN_FUNC_STATEMENT__VALUES);
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
        DeclarationsPackage theDeclarationsPackage = (DeclarationsPackage)EPackage.Registry.INSTANCE.getEPackage(DeclarationsPackage.eNS_URI);
        TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        PositionPackage thePositionPackage = (PositionPackage)EPackage.Registry.INSTANCE.getEPackage(PositionPackage.eNS_URI);
        ExpressionsPackage theExpressionsPackage = (ExpressionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        functionEClass.getESuperTypes().add(theDeclarationsPackage.getDeclaration());
        functionParameterEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        internalFunctionEClass.getESuperTypes().add(this.getFunction());
        externalFunctionEClass.getESuperTypes().add(this.getFunction());
        functionStatementEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        breakFuncStatementEClass.getESuperTypes().add(this.getFunctionStatement());
        continueFuncStatementEClass.getESuperTypes().add(this.getFunctionStatement());
        assignmentFuncStatementEClass.getESuperTypes().add(this.getFunctionStatement());
        whileFuncStatementEClass.getESuperTypes().add(this.getFunctionStatement());
        ifFuncStatementEClass.getESuperTypes().add(this.getFunctionStatement());
        elifFuncStatementEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        returnFuncStatementEClass.getESuperTypes().add(this.getFunctionStatement());

        // Initialize classes, features, and operations; add parameters
        initEClass(functionEClass, Function.class, "Function", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFunction_ReturnTypes(), theTypesPackage.getCifType(), null, "returnTypes", null, 1, -1, Function.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFunction_Parameters(), this.getFunctionParameter(), null, "parameters", null, 0, -1, Function.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(functionParameterEClass, FunctionParameter.class, "FunctionParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFunctionParameter_Parameter(), theDeclarationsPackage.getDiscVariable(), null, "parameter", null, 1, 1, FunctionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(internalFunctionEClass, InternalFunction.class, "InternalFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInternalFunction_Variables(), theDeclarationsPackage.getDiscVariable(), null, "variables", null, 0, -1, InternalFunction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInternalFunction_Statements(), this.getFunctionStatement(), null, "statements", null, 1, -1, InternalFunction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(externalFunctionEClass, ExternalFunction.class, "ExternalFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getExternalFunction_Function(), ecorePackage.getEString(), "function", null, 1, 1, ExternalFunction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(functionStatementEClass, FunctionStatement.class, "FunctionStatement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(breakFuncStatementEClass, BreakFuncStatement.class, "BreakFuncStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(continueFuncStatementEClass, ContinueFuncStatement.class, "ContinueFuncStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(assignmentFuncStatementEClass, AssignmentFuncStatement.class, "AssignmentFuncStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssignmentFuncStatement_Value(), theExpressionsPackage.getExpression(), null, "value", null, 1, 1, AssignmentFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAssignmentFuncStatement_Addressable(), theExpressionsPackage.getExpression(), null, "addressable", null, 1, 1, AssignmentFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(whileFuncStatementEClass, WhileFuncStatement.class, "WhileFuncStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getWhileFuncStatement_Guards(), theExpressionsPackage.getExpression(), null, "guards", null, 1, -1, WhileFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWhileFuncStatement_Statements(), this.getFunctionStatement(), null, "statements", null, 1, -1, WhileFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ifFuncStatementEClass, IfFuncStatement.class, "IfFuncStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIfFuncStatement_Guards(), theExpressionsPackage.getExpression(), null, "guards", null, 1, -1, IfFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfFuncStatement_Thens(), this.getFunctionStatement(), null, "thens", null, 1, -1, IfFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfFuncStatement_Elses(), this.getFunctionStatement(), null, "elses", null, 0, -1, IfFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfFuncStatement_Elifs(), this.getElifFuncStatement(), null, "elifs", null, 0, -1, IfFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(elifFuncStatementEClass, ElifFuncStatement.class, "ElifFuncStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getElifFuncStatement_Guards(), theExpressionsPackage.getExpression(), null, "guards", null, 1, -1, ElifFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getElifFuncStatement_Thens(), this.getFunctionStatement(), null, "thens", null, 1, -1, ElifFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(returnFuncStatementEClass, ReturnFuncStatement.class, "ReturnFuncStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getReturnFuncStatement_Values(), theExpressionsPackage.getExpression(), null, "values", null, 1, -1, ReturnFuncStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    }

} //FunctionsPackageImpl
