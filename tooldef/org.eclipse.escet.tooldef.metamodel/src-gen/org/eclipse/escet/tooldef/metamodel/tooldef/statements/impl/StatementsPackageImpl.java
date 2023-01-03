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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ExpressionsPackageImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsFactory;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StatementsPackageImpl extends EPackageImpl implements StatementsPackage
{
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
    private EClass variableEClass = null;

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
    private EClass returnStatementEClass = null;

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
    private EClass forStatementEClass = null;

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
    private EClass assignmentStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass toolInvokeStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass elifStatementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass addressableDeclEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tupleAddressableDeclEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass variableAddressableDeclEClass = null;

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
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private StatementsPackageImpl()
    {
        super(eNS_URI, StatementsFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link StatementsPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static StatementsPackage init()
    {
        if (isInited) return (StatementsPackage)EPackage.Registry.INSTANCE.getEPackage(StatementsPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredStatementsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        StatementsPackageImpl theStatementsPackage = registeredStatementsPackage instanceof StatementsPackageImpl ? (StatementsPackageImpl)registeredStatementsPackage : new StatementsPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        PositionPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TooldefPackage.eNS_URI);
        TooldefPackageImpl theTooldefPackage = (TooldefPackageImpl)(registeredPackage instanceof TooldefPackageImpl ? registeredPackage : TooldefPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);
        ExpressionsPackageImpl theExpressionsPackage = (ExpressionsPackageImpl)(registeredPackage instanceof ExpressionsPackageImpl ? registeredPackage : ExpressionsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        TypesPackageImpl theTypesPackage = (TypesPackageImpl)(registeredPackage instanceof TypesPackageImpl ? registeredPackage : TypesPackage.eINSTANCE);

        // Create package meta-data objects
        theStatementsPackage.createPackageContents();
        theTooldefPackage.createPackageContents();
        theExpressionsPackage.createPackageContents();
        theTypesPackage.createPackageContents();

        // Initialize created meta-data
        theStatementsPackage.initializePackageContents();
        theTooldefPackage.initializePackageContents();
        theExpressionsPackage.initializePackageContents();
        theTypesPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theStatementsPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(StatementsPackage.eNS_URI, theStatementsPackage);
        return theStatementsPackage;
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
    public EClass getVariable()
    {
        return variableEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getVariable_Name()
    {
        return (EAttribute)variableEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVariable_Type()
    {
        return (EReference)variableEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVariable_Value()
    {
        return (EReference)variableEClass.getEStructuralFeatures().get(2);
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
    public EReference getWhileStatement_Statements()
    {
        return (EReference)whileStatementEClass.getEStructuralFeatures().get(1);
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
    public EReference getReturnStatement_Values()
    {
        return (EReference)returnStatementEClass.getEStructuralFeatures().get(0);
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
    public EReference getExitStatement_ExitCode()
    {
        return (EReference)exitStatementEClass.getEStructuralFeatures().get(0);
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
    public EReference getForStatement_Statements()
    {
        return (EReference)forStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getForStatement_Source()
    {
        return (EReference)forStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getForStatement_Addressables()
    {
        return (EReference)forStatementEClass.getEStructuralFeatures().get(2);
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
    public EReference getIfStatement_Condition()
    {
        return (EReference)ifStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfStatement_Thens()
    {
        return (EReference)ifStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfStatement_Elifs()
    {
        return (EReference)ifStatementEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfStatement_Elses()
    {
        return (EReference)ifStatementEClass.getEStructuralFeatures().get(3);
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
    public EReference getAssignmentStatement_Addressables()
    {
        return (EReference)assignmentStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignmentStatement_Values()
    {
        return (EReference)assignmentStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getToolInvokeStatement()
    {
        return toolInvokeStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getToolInvokeStatement_Invocation()
    {
        return (EReference)toolInvokeStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getElifStatement()
    {
        return elifStatementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElifStatement_Condition()
    {
        return (EReference)elifStatementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElifStatement_Thens()
    {
        return (EReference)elifStatementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAddressableDecl()
    {
        return addressableDeclEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTupleAddressableDecl()
    {
        return tupleAddressableDeclEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTupleAddressableDecl_Elements()
    {
        return (EReference)tupleAddressableDeclEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVariableAddressableDecl()
    {
        return variableAddressableDeclEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVariableAddressableDecl_Variable()
    {
        return (EReference)variableAddressableDeclEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public StatementsFactory getStatementsFactory()
    {
        return (StatementsFactory)getEFactoryInstance();
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
        statementEClass = createEClass(STATEMENT);

        variableEClass = createEClass(VARIABLE);
        createEAttribute(variableEClass, VARIABLE__NAME);
        createEReference(variableEClass, VARIABLE__TYPE);
        createEReference(variableEClass, VARIABLE__VALUE);

        whileStatementEClass = createEClass(WHILE_STATEMENT);
        createEReference(whileStatementEClass, WHILE_STATEMENT__CONDITION);
        createEReference(whileStatementEClass, WHILE_STATEMENT__STATEMENTS);

        breakStatementEClass = createEClass(BREAK_STATEMENT);

        continueStatementEClass = createEClass(CONTINUE_STATEMENT);

        returnStatementEClass = createEClass(RETURN_STATEMENT);
        createEReference(returnStatementEClass, RETURN_STATEMENT__VALUES);

        exitStatementEClass = createEClass(EXIT_STATEMENT);
        createEReference(exitStatementEClass, EXIT_STATEMENT__EXIT_CODE);

        forStatementEClass = createEClass(FOR_STATEMENT);
        createEReference(forStatementEClass, FOR_STATEMENT__STATEMENTS);
        createEReference(forStatementEClass, FOR_STATEMENT__SOURCE);
        createEReference(forStatementEClass, FOR_STATEMENT__ADDRESSABLES);

        ifStatementEClass = createEClass(IF_STATEMENT);
        createEReference(ifStatementEClass, IF_STATEMENT__CONDITION);
        createEReference(ifStatementEClass, IF_STATEMENT__THENS);
        createEReference(ifStatementEClass, IF_STATEMENT__ELIFS);
        createEReference(ifStatementEClass, IF_STATEMENT__ELSES);

        assignmentStatementEClass = createEClass(ASSIGNMENT_STATEMENT);
        createEReference(assignmentStatementEClass, ASSIGNMENT_STATEMENT__ADDRESSABLES);
        createEReference(assignmentStatementEClass, ASSIGNMENT_STATEMENT__VALUES);

        toolInvokeStatementEClass = createEClass(TOOL_INVOKE_STATEMENT);
        createEReference(toolInvokeStatementEClass, TOOL_INVOKE_STATEMENT__INVOCATION);

        elifStatementEClass = createEClass(ELIF_STATEMENT);
        createEReference(elifStatementEClass, ELIF_STATEMENT__CONDITION);
        createEReference(elifStatementEClass, ELIF_STATEMENT__THENS);

        addressableDeclEClass = createEClass(ADDRESSABLE_DECL);

        tupleAddressableDeclEClass = createEClass(TUPLE_ADDRESSABLE_DECL);
        createEReference(tupleAddressableDeclEClass, TUPLE_ADDRESSABLE_DECL__ELEMENTS);

        variableAddressableDeclEClass = createEClass(VARIABLE_ADDRESSABLE_DECL);
        createEReference(variableAddressableDeclEClass, VARIABLE_ADDRESSABLE_DECL__VARIABLE);
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
        TooldefPackage theTooldefPackage = (TooldefPackage)EPackage.Registry.INSTANCE.getEPackage(TooldefPackage.eNS_URI);
        TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        ExpressionsPackage theExpressionsPackage = (ExpressionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);
        PositionPackage thePositionPackage = (PositionPackage)EPackage.Registry.INSTANCE.getEPackage(PositionPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        statementEClass.getESuperTypes().add(theTooldefPackage.getDeclaration());
        variableEClass.getESuperTypes().add(this.getStatement());
        whileStatementEClass.getESuperTypes().add(this.getStatement());
        breakStatementEClass.getESuperTypes().add(this.getStatement());
        continueStatementEClass.getESuperTypes().add(this.getStatement());
        returnStatementEClass.getESuperTypes().add(this.getStatement());
        exitStatementEClass.getESuperTypes().add(this.getStatement());
        forStatementEClass.getESuperTypes().add(this.getStatement());
        ifStatementEClass.getESuperTypes().add(this.getStatement());
        assignmentStatementEClass.getESuperTypes().add(this.getStatement());
        toolInvokeStatementEClass.getESuperTypes().add(this.getStatement());
        elifStatementEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        addressableDeclEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        tupleAddressableDeclEClass.getESuperTypes().add(this.getAddressableDecl());
        variableAddressableDeclEClass.getESuperTypes().add(this.getAddressableDecl());

        // Initialize classes, features, and operations; add parameters
        initEClass(statementEClass, Statement.class, "Statement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(variableEClass, Variable.class, "Variable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getVariable_Name(), ecorePackage.getEString(), "name", null, 1, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getVariable_Type(), theTypesPackage.getToolDefType(), null, "type", null, 1, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getVariable_Value(), theExpressionsPackage.getExpression(), null, "value", null, 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(whileStatementEClass, WhileStatement.class, "WhileStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getWhileStatement_Condition(), theExpressionsPackage.getExpression(), null, "condition", null, 1, 1, WhileStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWhileStatement_Statements(), this.getStatement(), null, "statements", null, 0, -1, WhileStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(breakStatementEClass, BreakStatement.class, "BreakStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(continueStatementEClass, ContinueStatement.class, "ContinueStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(returnStatementEClass, ReturnStatement.class, "ReturnStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getReturnStatement_Values(), theExpressionsPackage.getExpression(), null, "values", null, 0, -1, ReturnStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(exitStatementEClass, ExitStatement.class, "ExitStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExitStatement_ExitCode(), theExpressionsPackage.getExpression(), null, "exitCode", null, 0, 1, ExitStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(forStatementEClass, ForStatement.class, "ForStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getForStatement_Statements(), this.getStatement(), null, "statements", null, 0, -1, ForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getForStatement_Source(), theExpressionsPackage.getExpression(), null, "source", null, 1, 1, ForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getForStatement_Addressables(), this.getAddressableDecl(), null, "addressables", null, 1, -1, ForStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ifStatementEClass, IfStatement.class, "IfStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIfStatement_Condition(), theExpressionsPackage.getExpression(), null, "condition", null, 1, 1, IfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfStatement_Thens(), this.getStatement(), null, "thens", null, 0, -1, IfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfStatement_Elifs(), this.getElifStatement(), null, "elifs", null, 0, -1, IfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfStatement_Elses(), this.getStatement(), null, "elses", null, 0, -1, IfStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(assignmentStatementEClass, AssignmentStatement.class, "AssignmentStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssignmentStatement_Addressables(), theExpressionsPackage.getExpression(), null, "addressables", null, 1, -1, AssignmentStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAssignmentStatement_Values(), theExpressionsPackage.getExpression(), null, "values", null, 1, -1, AssignmentStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(toolInvokeStatementEClass, ToolInvokeStatement.class, "ToolInvokeStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getToolInvokeStatement_Invocation(), theExpressionsPackage.getToolInvokeExpression(), null, "invocation", null, 1, 1, ToolInvokeStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(elifStatementEClass, ElifStatement.class, "ElifStatement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getElifStatement_Condition(), theExpressionsPackage.getExpression(), null, "condition", null, 1, 1, ElifStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getElifStatement_Thens(), this.getStatement(), null, "thens", null, 0, -1, ElifStatement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(addressableDeclEClass, AddressableDecl.class, "AddressableDecl", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(tupleAddressableDeclEClass, TupleAddressableDecl.class, "TupleAddressableDecl", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTupleAddressableDecl_Elements(), this.getAddressableDecl(), null, "elements", null, 2, -1, TupleAddressableDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(variableAddressableDeclEClass, VariableAddressableDecl.class, "VariableAddressableDecl", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getVariableAddressableDecl_Variable(), this.getVariable(), null, "variable", null, 1, 1, VariableAddressableDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    }

} //StatementsPackageImpl
