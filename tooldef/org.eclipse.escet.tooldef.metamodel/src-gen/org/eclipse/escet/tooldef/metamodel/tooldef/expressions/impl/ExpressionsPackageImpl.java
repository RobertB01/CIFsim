/**
 * Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.EmptySetMapExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsFactory;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NullExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression;

import org.eclipse.escet.tooldef.metamodel.tooldef.impl.TooldefPackageImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.StatementsPackageImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExpressionsPackageImpl extends EPackageImpl implements ExpressionsPackage
{
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
    private EClass toolInvokeExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass toolRefEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass boolExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass numberExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nullExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass doubleExpressionEClass = null;

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
    private EClass mapExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass mapEntryEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass emptySetMapExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unresolvedRefExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass toolArgumentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass variableExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stringExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass projectionExpressionEClass = null;

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
    private EClass tupleExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass toolParamExpressionEClass = null;

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
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ExpressionsPackageImpl()
    {
        super(eNS_URI, ExpressionsFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link ExpressionsPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ExpressionsPackage init()
    {
        if (isInited) return (ExpressionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredExpressionsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        ExpressionsPackageImpl theExpressionsPackage = registeredExpressionsPackage instanceof ExpressionsPackageImpl ? (ExpressionsPackageImpl)registeredExpressionsPackage : new ExpressionsPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        PositionPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TooldefPackage.eNS_URI);
        TooldefPackageImpl theTooldefPackage = (TooldefPackageImpl)(registeredPackage instanceof TooldefPackageImpl ? registeredPackage : TooldefPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(StatementsPackage.eNS_URI);
        StatementsPackageImpl theStatementsPackage = (StatementsPackageImpl)(registeredPackage instanceof StatementsPackageImpl ? registeredPackage : StatementsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        TypesPackageImpl theTypesPackage = (TypesPackageImpl)(registeredPackage instanceof TypesPackageImpl ? registeredPackage : TypesPackage.eINSTANCE);

        // Create package meta-data objects
        theExpressionsPackage.createPackageContents();
        theTooldefPackage.createPackageContents();
        theStatementsPackage.createPackageContents();
        theTypesPackage.createPackageContents();

        // Initialize created meta-data
        theExpressionsPackage.initializePackageContents();
        theTooldefPackage.initializePackageContents();
        theStatementsPackage.initializePackageContents();
        theTypesPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theExpressionsPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ExpressionsPackage.eNS_URI, theExpressionsPackage);
        return theExpressionsPackage;
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
    public EClass getToolInvokeExpression()
    {
        return toolInvokeExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getToolInvokeExpression_Arguments()
    {
        return (EReference)toolInvokeExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getToolInvokeExpression_Tool()
    {
        return (EReference)toolInvokeExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getToolRef()
    {
        return toolRefEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getToolRef_Builtin()
    {
        return (EAttribute)toolRefEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getToolRef_Name()
    {
        return (EAttribute)toolRefEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getToolRef_Tool()
    {
        return (EReference)toolRefEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBoolExpression()
    {
        return boolExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getBoolExpression_Value()
    {
        return (EAttribute)boolExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNumberExpression()
    {
        return numberExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getNumberExpression_Value()
    {
        return (EAttribute)numberExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNullExpression()
    {
        return nullExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDoubleExpression()
    {
        return doubleExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDoubleExpression_Value()
    {
        return (EAttribute)doubleExpressionEClass.getEStructuralFeatures().get(0);
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
    public EReference getCastExpression_Child()
    {
        return (EReference)castExpressionEClass.getEStructuralFeatures().get(0);
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
    public EClass getMapExpression()
    {
        return mapExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMapExpression_Entries()
    {
        return (EReference)mapExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMapEntry()
    {
        return mapEntryEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMapEntry_Key()
    {
        return (EReference)mapEntryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMapEntry_Value()
    {
        return (EReference)mapEntryEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEmptySetMapExpression()
    {
        return emptySetMapExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUnresolvedRefExpression()
    {
        return unresolvedRefExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getUnresolvedRefExpression_Name()
    {
        return (EAttribute)unresolvedRefExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getToolArgument()
    {
        return toolArgumentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getToolArgument_Value()
    {
        return (EReference)toolArgumentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getToolArgument_Name()
    {
        return (EAttribute)toolArgumentEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVariableExpression()
    {
        return variableExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVariableExpression_Variable()
    {
        return (EReference)variableExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStringExpression()
    {
        return stringExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getStringExpression_Value()
    {
        return (EAttribute)stringExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getProjectionExpression()
    {
        return projectionExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getProjectionExpression_Child()
    {
        return (EReference)projectionExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getProjectionExpression_Index()
    {
        return (EReference)projectionExpressionEClass.getEStructuralFeatures().get(1);
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
    public EReference getSliceExpression_Child()
    {
        return (EReference)sliceExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSliceExpression_Begin()
    {
        return (EReference)sliceExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSliceExpression_End()
    {
        return (EReference)sliceExpressionEClass.getEStructuralFeatures().get(2);
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
    public EReference getTupleExpression_Elements()
    {
        return (EReference)tupleExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getToolParamExpression()
    {
        return toolParamExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getToolParamExpression_Param()
    {
        return (EReference)toolParamExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ExpressionsFactory getExpressionsFactory()
    {
        return (ExpressionsFactory)getEFactoryInstance();
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
        expressionEClass = createEClass(EXPRESSION);
        createEReference(expressionEClass, EXPRESSION__TYPE);

        toolInvokeExpressionEClass = createEClass(TOOL_INVOKE_EXPRESSION);
        createEReference(toolInvokeExpressionEClass, TOOL_INVOKE_EXPRESSION__ARGUMENTS);
        createEReference(toolInvokeExpressionEClass, TOOL_INVOKE_EXPRESSION__TOOL);

        toolRefEClass = createEClass(TOOL_REF);
        createEAttribute(toolRefEClass, TOOL_REF__BUILTIN);
        createEAttribute(toolRefEClass, TOOL_REF__NAME);
        createEReference(toolRefEClass, TOOL_REF__TOOL);

        boolExpressionEClass = createEClass(BOOL_EXPRESSION);
        createEAttribute(boolExpressionEClass, BOOL_EXPRESSION__VALUE);

        numberExpressionEClass = createEClass(NUMBER_EXPRESSION);
        createEAttribute(numberExpressionEClass, NUMBER_EXPRESSION__VALUE);

        nullExpressionEClass = createEClass(NULL_EXPRESSION);

        doubleExpressionEClass = createEClass(DOUBLE_EXPRESSION);
        createEAttribute(doubleExpressionEClass, DOUBLE_EXPRESSION__VALUE);

        castExpressionEClass = createEClass(CAST_EXPRESSION);
        createEReference(castExpressionEClass, CAST_EXPRESSION__CHILD);

        listExpressionEClass = createEClass(LIST_EXPRESSION);
        createEReference(listExpressionEClass, LIST_EXPRESSION__ELEMENTS);

        setExpressionEClass = createEClass(SET_EXPRESSION);
        createEReference(setExpressionEClass, SET_EXPRESSION__ELEMENTS);

        mapExpressionEClass = createEClass(MAP_EXPRESSION);
        createEReference(mapExpressionEClass, MAP_EXPRESSION__ENTRIES);

        mapEntryEClass = createEClass(MAP_ENTRY);
        createEReference(mapEntryEClass, MAP_ENTRY__KEY);
        createEReference(mapEntryEClass, MAP_ENTRY__VALUE);

        emptySetMapExpressionEClass = createEClass(EMPTY_SET_MAP_EXPRESSION);

        unresolvedRefExpressionEClass = createEClass(UNRESOLVED_REF_EXPRESSION);
        createEAttribute(unresolvedRefExpressionEClass, UNRESOLVED_REF_EXPRESSION__NAME);

        toolArgumentEClass = createEClass(TOOL_ARGUMENT);
        createEReference(toolArgumentEClass, TOOL_ARGUMENT__VALUE);
        createEAttribute(toolArgumentEClass, TOOL_ARGUMENT__NAME);

        variableExpressionEClass = createEClass(VARIABLE_EXPRESSION);
        createEReference(variableExpressionEClass, VARIABLE_EXPRESSION__VARIABLE);

        stringExpressionEClass = createEClass(STRING_EXPRESSION);
        createEAttribute(stringExpressionEClass, STRING_EXPRESSION__VALUE);

        projectionExpressionEClass = createEClass(PROJECTION_EXPRESSION);
        createEReference(projectionExpressionEClass, PROJECTION_EXPRESSION__CHILD);
        createEReference(projectionExpressionEClass, PROJECTION_EXPRESSION__INDEX);

        sliceExpressionEClass = createEClass(SLICE_EXPRESSION);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__CHILD);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__BEGIN);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__END);

        tupleExpressionEClass = createEClass(TUPLE_EXPRESSION);
        createEReference(tupleExpressionEClass, TUPLE_EXPRESSION__ELEMENTS);

        toolParamExpressionEClass = createEClass(TOOL_PARAM_EXPRESSION);
        createEReference(toolParamExpressionEClass, TOOL_PARAM_EXPRESSION__PARAM);
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
        TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        TooldefPackage theTooldefPackage = (TooldefPackage)EPackage.Registry.INSTANCE.getEPackage(TooldefPackage.eNS_URI);
        StatementsPackage theStatementsPackage = (StatementsPackage)EPackage.Registry.INSTANCE.getEPackage(StatementsPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        expressionEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        toolInvokeExpressionEClass.getESuperTypes().add(this.getExpression());
        toolRefEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        boolExpressionEClass.getESuperTypes().add(this.getExpression());
        numberExpressionEClass.getESuperTypes().add(this.getExpression());
        nullExpressionEClass.getESuperTypes().add(this.getExpression());
        doubleExpressionEClass.getESuperTypes().add(this.getExpression());
        castExpressionEClass.getESuperTypes().add(this.getExpression());
        listExpressionEClass.getESuperTypes().add(this.getExpression());
        setExpressionEClass.getESuperTypes().add(this.getExpression());
        mapExpressionEClass.getESuperTypes().add(this.getExpression());
        mapEntryEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        emptySetMapExpressionEClass.getESuperTypes().add(this.getExpression());
        unresolvedRefExpressionEClass.getESuperTypes().add(this.getExpression());
        toolArgumentEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        variableExpressionEClass.getESuperTypes().add(this.getExpression());
        stringExpressionEClass.getESuperTypes().add(this.getExpression());
        projectionExpressionEClass.getESuperTypes().add(this.getExpression());
        sliceExpressionEClass.getESuperTypes().add(this.getExpression());
        tupleExpressionEClass.getESuperTypes().add(this.getExpression());
        toolParamExpressionEClass.getESuperTypes().add(this.getExpression());

        // Initialize classes, features, and operations; add parameters
        initEClass(expressionEClass, Expression.class, "Expression", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExpression_Type(), theTypesPackage.getToolDefType(), null, "type", null, 1, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(toolInvokeExpressionEClass, ToolInvokeExpression.class, "ToolInvokeExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getToolInvokeExpression_Arguments(), this.getToolArgument(), null, "arguments", null, 0, -1, ToolInvokeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getToolInvokeExpression_Tool(), this.getToolRef(), null, "tool", null, 1, 1, ToolInvokeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(toolRefEClass, ToolRef.class, "ToolRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getToolRef_Builtin(), ecorePackage.getEBoolean(), "builtin", null, 1, 1, ToolRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getToolRef_Name(), ecorePackage.getEString(), "name", null, 1, 1, ToolRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getToolRef_Tool(), theTooldefPackage.getTool(), null, "tool", null, 0, 1, ToolRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(boolExpressionEClass, BoolExpression.class, "BoolExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBoolExpression_Value(), ecorePackage.getEBoolean(), "value", null, 1, 1, BoolExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(numberExpressionEClass, NumberExpression.class, "NumberExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getNumberExpression_Value(), ecorePackage.getEString(), "value", null, 1, 1, NumberExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(nullExpressionEClass, NullExpression.class, "NullExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(doubleExpressionEClass, DoubleExpression.class, "DoubleExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDoubleExpression_Value(), ecorePackage.getEString(), "value", null, 1, 1, DoubleExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(castExpressionEClass, CastExpression.class, "CastExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCastExpression_Child(), this.getExpression(), null, "child", null, 1, 1, CastExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(listExpressionEClass, ListExpression.class, "ListExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getListExpression_Elements(), this.getExpression(), null, "elements", null, 0, -1, ListExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(setExpressionEClass, SetExpression.class, "SetExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSetExpression_Elements(), this.getExpression(), null, "elements", null, 0, -1, SetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(mapExpressionEClass, MapExpression.class, "MapExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMapExpression_Entries(), this.getMapEntry(), null, "entries", null, 0, -1, MapExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(mapEntryEClass, MapEntry.class, "MapEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMapEntry_Key(), this.getExpression(), null, "key", null, 1, 1, MapEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMapEntry_Value(), this.getExpression(), null, "value", null, 1, 1, MapEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(emptySetMapExpressionEClass, EmptySetMapExpression.class, "EmptySetMapExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(unresolvedRefExpressionEClass, UnresolvedRefExpression.class, "UnresolvedRefExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getUnresolvedRefExpression_Name(), ecorePackage.getEString(), "name", null, 1, 1, UnresolvedRefExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(toolArgumentEClass, ToolArgument.class, "ToolArgument", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getToolArgument_Value(), this.getExpression(), null, "value", null, 1, 1, ToolArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getToolArgument_Name(), ecorePackage.getEString(), "name", null, 1, 1, ToolArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(variableExpressionEClass, VariableExpression.class, "VariableExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getVariableExpression_Variable(), theStatementsPackage.getVariable(), null, "variable", null, 1, 1, VariableExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(stringExpressionEClass, StringExpression.class, "StringExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStringExpression_Value(), ecorePackage.getEString(), "value", null, 1, 1, StringExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(projectionExpressionEClass, ProjectionExpression.class, "ProjectionExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProjectionExpression_Child(), this.getExpression(), null, "child", null, 1, 1, ProjectionExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProjectionExpression_Index(), this.getExpression(), null, "index", null, 1, 1, ProjectionExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sliceExpressionEClass, SliceExpression.class, "SliceExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSliceExpression_Child(), this.getExpression(), null, "child", null, 1, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSliceExpression_Begin(), this.getExpression(), null, "begin", null, 0, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSliceExpression_End(), this.getExpression(), null, "end", null, 0, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tupleExpressionEClass, TupleExpression.class, "TupleExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTupleExpression_Elements(), this.getExpression(), null, "elements", null, 0, -1, TupleExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(toolParamExpressionEClass, ToolParamExpression.class, "ToolParamExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getToolParamExpression_Param(), theTooldefPackage.getToolParameter(), null, "param", null, 1, 1, ToolParamExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    }

} //ExpressionsPackageImpl
