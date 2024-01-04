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
package org.eclipse.escet.cif.metamodel.cif.expressions.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationsPackage;

import org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;

import org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage;

import org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompParamWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsFactory;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SliceExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;

import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage;

import org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl;

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
    private EClass binaryExpressionEClass = null;

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
    private EClass boolExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass intExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionCallExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ifExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass discVariableExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass algVariableExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass eventExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass enumLiteralExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass locationExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass elifExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compParamWrapExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compInstWrapExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compParamExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass constantExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tauExpressionEClass = null;

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
    private EClass baseFunctionExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stdLibFunctionExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass realExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeExpressionEClass = null;

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
    private EClass dictExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dictPairEClass = null;

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
    private EClass castExpressionEClass = null;

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
    private EClass fieldExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass contVariableExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass inputVariableExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass receivedExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass selfExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass switchExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass switchCaseEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum unaryOperatorEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum binaryOperatorEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum stdLibFunctionEEnum = null;

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
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#eNS_URI
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
        Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(CifPackage.eNS_URI);
        CifPackageImpl theCifPackage = (CifPackageImpl)(registeredPackage instanceof CifPackageImpl ? registeredPackage : CifPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeclarationsPackage.eNS_URI);
        DeclarationsPackageImpl theDeclarationsPackage = (DeclarationsPackageImpl)(registeredPackage instanceof DeclarationsPackageImpl ? registeredPackage : DeclarationsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AutomataPackage.eNS_URI);
        AutomataPackageImpl theAutomataPackage = (AutomataPackageImpl)(registeredPackage instanceof AutomataPackageImpl ? registeredPackage : AutomataPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        TypesPackageImpl theTypesPackage = (TypesPackageImpl)(registeredPackage instanceof TypesPackageImpl ? registeredPackage : TypesPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(FunctionsPackage.eNS_URI);
        FunctionsPackageImpl theFunctionsPackage = (FunctionsPackageImpl)(registeredPackage instanceof FunctionsPackageImpl ? registeredPackage : FunctionsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(CifsvgPackage.eNS_URI);
        CifsvgPackageImpl theCifsvgPackage = (CifsvgPackageImpl)(registeredPackage instanceof CifsvgPackageImpl ? registeredPackage : CifsvgPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(PrintPackage.eNS_URI);
        PrintPackageImpl thePrintPackage = (PrintPackageImpl)(registeredPackage instanceof PrintPackageImpl ? registeredPackage : PrintPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnnotationsPackage.eNS_URI);
        AnnotationsPackageImpl theAnnotationsPackage = (AnnotationsPackageImpl)(registeredPackage instanceof AnnotationsPackageImpl ? registeredPackage : AnnotationsPackage.eINSTANCE);

        // Create package meta-data objects
        theExpressionsPackage.createPackageContents();
        theCifPackage.createPackageContents();
        theDeclarationsPackage.createPackageContents();
        theAutomataPackage.createPackageContents();
        theTypesPackage.createPackageContents();
        theFunctionsPackage.createPackageContents();
        theCifsvgPackage.createPackageContents();
        thePrintPackage.createPackageContents();
        theAnnotationsPackage.createPackageContents();

        // Initialize created meta-data
        theExpressionsPackage.initializePackageContents();
        theCifPackage.initializePackageContents();
        theDeclarationsPackage.initializePackageContents();
        theAutomataPackage.initializePackageContents();
        theTypesPackage.initializePackageContents();
        theFunctionsPackage.initializePackageContents();
        theCifsvgPackage.initializePackageContents();
        thePrintPackage.initializePackageContents();
        theAnnotationsPackage.initializePackageContents();

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
    public EAttribute getBinaryExpression_Operator()
    {
        return (EAttribute)binaryExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBinaryExpression_Left()
    {
        return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBinaryExpression_Right()
    {
        return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(2);
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
    public EAttribute getUnaryExpression_Operator()
    {
        return (EAttribute)unaryExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUnaryExpression_Child()
    {
        return (EReference)unaryExpressionEClass.getEStructuralFeatures().get(1);
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
    public EClass getIntExpression()
    {
        return intExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getIntExpression_Value()
    {
        return (EAttribute)intExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunctionCallExpression()
    {
        return functionCallExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunctionCallExpression_Arguments()
    {
        return (EReference)functionCallExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunctionCallExpression_Function()
    {
        return (EReference)functionCallExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIfExpression()
    {
        return ifExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfExpression_Guards()
    {
        return (EReference)ifExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfExpression_Then()
    {
        return (EReference)ifExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfExpression_Else()
    {
        return (EReference)ifExpressionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfExpression_Elifs()
    {
        return (EReference)ifExpressionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDiscVariableExpression()
    {
        return discVariableExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDiscVariableExpression_Variable()
    {
        return (EReference)discVariableExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAlgVariableExpression()
    {
        return algVariableExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAlgVariableExpression_Variable()
    {
        return (EReference)algVariableExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEventExpression()
    {
        return eventExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEventExpression_Event()
    {
        return (EReference)eventExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEnumLiteralExpression()
    {
        return enumLiteralExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEnumLiteralExpression_Literal()
    {
        return (EReference)enumLiteralExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLocationExpression()
    {
        return locationExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getLocationExpression_Location()
    {
        return (EReference)locationExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getElifExpression()
    {
        return elifExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElifExpression_Guards()
    {
        return (EReference)elifExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElifExpression_Then()
    {
        return (EReference)elifExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCompParamWrapExpression()
    {
        return compParamWrapExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompParamWrapExpression_Reference()
    {
        return (EReference)compParamWrapExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompParamWrapExpression_Parameter()
    {
        return (EReference)compParamWrapExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCompInstWrapExpression()
    {
        return compInstWrapExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompInstWrapExpression_Reference()
    {
        return (EReference)compInstWrapExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompInstWrapExpression_Instantiation()
    {
        return (EReference)compInstWrapExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComponentExpression()
    {
        return componentExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComponentExpression_Component()
    {
        return (EReference)componentExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCompParamExpression()
    {
        return compParamExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompParamExpression_Parameter()
    {
        return (EReference)compParamExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConstantExpression()
    {
        return constantExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConstantExpression_Constant()
    {
        return (EReference)constantExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTauExpression()
    {
        return tauExpressionEClass;
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
    public EReference getProjectionExpression_Index()
    {
        return (EReference)projectionExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getProjectionExpression_Child()
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
    public EReference getSliceExpression_Begin()
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
    public EReference getSliceExpression_Child()
    {
        return (EReference)sliceExpressionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBaseFunctionExpression()
    {
        return baseFunctionExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStdLibFunctionExpression()
    {
        return stdLibFunctionExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getStdLibFunctionExpression_Function()
    {
        return (EAttribute)stdLibFunctionExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRealExpression()
    {
        return realExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRealExpression_Value()
    {
        return (EAttribute)realExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTimeExpression()
    {
        return timeExpressionEClass;
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
    public EClass getDictExpression()
    {
        return dictExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictExpression_Pairs()
    {
        return (EReference)dictExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDictPair()
    {
        return dictPairEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictPair_Key()
    {
        return (EReference)dictPairEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictPair_Value()
    {
        return (EReference)dictPairEClass.getEStructuralFeatures().get(1);
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
    public EClass getFieldExpression()
    {
        return fieldExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFieldExpression_Field()
    {
        return (EReference)fieldExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunctionExpression()
    {
        return functionExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunctionExpression_Function()
    {
        return (EReference)functionExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getContVariableExpression()
    {
        return contVariableExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getContVariableExpression_Variable()
    {
        return (EReference)contVariableExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getContVariableExpression_Derivative()
    {
        return (EAttribute)contVariableExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInputVariableExpression()
    {
        return inputVariableExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getInputVariableExpression_Variable()
    {
        return (EReference)inputVariableExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReceivedExpression()
    {
        return receivedExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSelfExpression()
    {
        return selfExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSwitchExpression()
    {
        return switchExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSwitchExpression_Value()
    {
        return (EReference)switchExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSwitchExpression_Cases()
    {
        return (EReference)switchExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSwitchCase()
    {
        return switchCaseEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSwitchCase_Key()
    {
        return (EReference)switchCaseEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSwitchCase_Value()
    {
        return (EReference)switchCaseEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getUnaryOperator()
    {
        return unaryOperatorEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getBinaryOperator()
    {
        return binaryOperatorEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getStdLibFunction()
    {
        return stdLibFunctionEEnum;
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

        binaryExpressionEClass = createEClass(BINARY_EXPRESSION);
        createEAttribute(binaryExpressionEClass, BINARY_EXPRESSION__OPERATOR);
        createEReference(binaryExpressionEClass, BINARY_EXPRESSION__LEFT);
        createEReference(binaryExpressionEClass, BINARY_EXPRESSION__RIGHT);

        unaryExpressionEClass = createEClass(UNARY_EXPRESSION);
        createEAttribute(unaryExpressionEClass, UNARY_EXPRESSION__OPERATOR);
        createEReference(unaryExpressionEClass, UNARY_EXPRESSION__CHILD);

        boolExpressionEClass = createEClass(BOOL_EXPRESSION);
        createEAttribute(boolExpressionEClass, BOOL_EXPRESSION__VALUE);

        intExpressionEClass = createEClass(INT_EXPRESSION);
        createEAttribute(intExpressionEClass, INT_EXPRESSION__VALUE);

        functionCallExpressionEClass = createEClass(FUNCTION_CALL_EXPRESSION);
        createEReference(functionCallExpressionEClass, FUNCTION_CALL_EXPRESSION__ARGUMENTS);
        createEReference(functionCallExpressionEClass, FUNCTION_CALL_EXPRESSION__FUNCTION);

        ifExpressionEClass = createEClass(IF_EXPRESSION);
        createEReference(ifExpressionEClass, IF_EXPRESSION__GUARDS);
        createEReference(ifExpressionEClass, IF_EXPRESSION__THEN);
        createEReference(ifExpressionEClass, IF_EXPRESSION__ELSE);
        createEReference(ifExpressionEClass, IF_EXPRESSION__ELIFS);

        discVariableExpressionEClass = createEClass(DISC_VARIABLE_EXPRESSION);
        createEReference(discVariableExpressionEClass, DISC_VARIABLE_EXPRESSION__VARIABLE);

        algVariableExpressionEClass = createEClass(ALG_VARIABLE_EXPRESSION);
        createEReference(algVariableExpressionEClass, ALG_VARIABLE_EXPRESSION__VARIABLE);

        eventExpressionEClass = createEClass(EVENT_EXPRESSION);
        createEReference(eventExpressionEClass, EVENT_EXPRESSION__EVENT);

        enumLiteralExpressionEClass = createEClass(ENUM_LITERAL_EXPRESSION);
        createEReference(enumLiteralExpressionEClass, ENUM_LITERAL_EXPRESSION__LITERAL);

        locationExpressionEClass = createEClass(LOCATION_EXPRESSION);
        createEReference(locationExpressionEClass, LOCATION_EXPRESSION__LOCATION);

        elifExpressionEClass = createEClass(ELIF_EXPRESSION);
        createEReference(elifExpressionEClass, ELIF_EXPRESSION__GUARDS);
        createEReference(elifExpressionEClass, ELIF_EXPRESSION__THEN);

        compParamWrapExpressionEClass = createEClass(COMP_PARAM_WRAP_EXPRESSION);
        createEReference(compParamWrapExpressionEClass, COMP_PARAM_WRAP_EXPRESSION__REFERENCE);
        createEReference(compParamWrapExpressionEClass, COMP_PARAM_WRAP_EXPRESSION__PARAMETER);

        compInstWrapExpressionEClass = createEClass(COMP_INST_WRAP_EXPRESSION);
        createEReference(compInstWrapExpressionEClass, COMP_INST_WRAP_EXPRESSION__REFERENCE);
        createEReference(compInstWrapExpressionEClass, COMP_INST_WRAP_EXPRESSION__INSTANTIATION);

        componentExpressionEClass = createEClass(COMPONENT_EXPRESSION);
        createEReference(componentExpressionEClass, COMPONENT_EXPRESSION__COMPONENT);

        compParamExpressionEClass = createEClass(COMP_PARAM_EXPRESSION);
        createEReference(compParamExpressionEClass, COMP_PARAM_EXPRESSION__PARAMETER);

        constantExpressionEClass = createEClass(CONSTANT_EXPRESSION);
        createEReference(constantExpressionEClass, CONSTANT_EXPRESSION__CONSTANT);

        tauExpressionEClass = createEClass(TAU_EXPRESSION);

        projectionExpressionEClass = createEClass(PROJECTION_EXPRESSION);
        createEReference(projectionExpressionEClass, PROJECTION_EXPRESSION__INDEX);
        createEReference(projectionExpressionEClass, PROJECTION_EXPRESSION__CHILD);

        sliceExpressionEClass = createEClass(SLICE_EXPRESSION);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__BEGIN);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__END);
        createEReference(sliceExpressionEClass, SLICE_EXPRESSION__CHILD);

        baseFunctionExpressionEClass = createEClass(BASE_FUNCTION_EXPRESSION);

        stdLibFunctionExpressionEClass = createEClass(STD_LIB_FUNCTION_EXPRESSION);
        createEAttribute(stdLibFunctionExpressionEClass, STD_LIB_FUNCTION_EXPRESSION__FUNCTION);

        realExpressionEClass = createEClass(REAL_EXPRESSION);
        createEAttribute(realExpressionEClass, REAL_EXPRESSION__VALUE);

        timeExpressionEClass = createEClass(TIME_EXPRESSION);

        listExpressionEClass = createEClass(LIST_EXPRESSION);
        createEReference(listExpressionEClass, LIST_EXPRESSION__ELEMENTS);

        setExpressionEClass = createEClass(SET_EXPRESSION);
        createEReference(setExpressionEClass, SET_EXPRESSION__ELEMENTS);

        dictExpressionEClass = createEClass(DICT_EXPRESSION);
        createEReference(dictExpressionEClass, DICT_EXPRESSION__PAIRS);

        dictPairEClass = createEClass(DICT_PAIR);
        createEReference(dictPairEClass, DICT_PAIR__KEY);
        createEReference(dictPairEClass, DICT_PAIR__VALUE);

        tupleExpressionEClass = createEClass(TUPLE_EXPRESSION);
        createEReference(tupleExpressionEClass, TUPLE_EXPRESSION__FIELDS);

        castExpressionEClass = createEClass(CAST_EXPRESSION);
        createEReference(castExpressionEClass, CAST_EXPRESSION__CHILD);

        stringExpressionEClass = createEClass(STRING_EXPRESSION);
        createEAttribute(stringExpressionEClass, STRING_EXPRESSION__VALUE);

        fieldExpressionEClass = createEClass(FIELD_EXPRESSION);
        createEReference(fieldExpressionEClass, FIELD_EXPRESSION__FIELD);

        functionExpressionEClass = createEClass(FUNCTION_EXPRESSION);
        createEReference(functionExpressionEClass, FUNCTION_EXPRESSION__FUNCTION);

        contVariableExpressionEClass = createEClass(CONT_VARIABLE_EXPRESSION);
        createEReference(contVariableExpressionEClass, CONT_VARIABLE_EXPRESSION__VARIABLE);
        createEAttribute(contVariableExpressionEClass, CONT_VARIABLE_EXPRESSION__DERIVATIVE);

        inputVariableExpressionEClass = createEClass(INPUT_VARIABLE_EXPRESSION);
        createEReference(inputVariableExpressionEClass, INPUT_VARIABLE_EXPRESSION__VARIABLE);

        receivedExpressionEClass = createEClass(RECEIVED_EXPRESSION);

        selfExpressionEClass = createEClass(SELF_EXPRESSION);

        switchExpressionEClass = createEClass(SWITCH_EXPRESSION);
        createEReference(switchExpressionEClass, SWITCH_EXPRESSION__VALUE);
        createEReference(switchExpressionEClass, SWITCH_EXPRESSION__CASES);

        switchCaseEClass = createEClass(SWITCH_CASE);
        createEReference(switchCaseEClass, SWITCH_CASE__KEY);
        createEReference(switchCaseEClass, SWITCH_CASE__VALUE);

        // Create enums
        unaryOperatorEEnum = createEEnum(UNARY_OPERATOR);
        binaryOperatorEEnum = createEEnum(BINARY_OPERATOR);
        stdLibFunctionEEnum = createEEnum(STD_LIB_FUNCTION);
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
        DeclarationsPackage theDeclarationsPackage = (DeclarationsPackage)EPackage.Registry.INSTANCE.getEPackage(DeclarationsPackage.eNS_URI);
        AutomataPackage theAutomataPackage = (AutomataPackage)EPackage.Registry.INSTANCE.getEPackage(AutomataPackage.eNS_URI);
        CifPackage theCifPackage = (CifPackage)EPackage.Registry.INSTANCE.getEPackage(CifPackage.eNS_URI);
        FunctionsPackage theFunctionsPackage = (FunctionsPackage)EPackage.Registry.INSTANCE.getEPackage(FunctionsPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        expressionEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        binaryExpressionEClass.getESuperTypes().add(this.getExpression());
        unaryExpressionEClass.getESuperTypes().add(this.getExpression());
        boolExpressionEClass.getESuperTypes().add(this.getExpression());
        intExpressionEClass.getESuperTypes().add(this.getExpression());
        functionCallExpressionEClass.getESuperTypes().add(this.getExpression());
        ifExpressionEClass.getESuperTypes().add(this.getExpression());
        discVariableExpressionEClass.getESuperTypes().add(this.getExpression());
        algVariableExpressionEClass.getESuperTypes().add(this.getExpression());
        eventExpressionEClass.getESuperTypes().add(this.getExpression());
        enumLiteralExpressionEClass.getESuperTypes().add(this.getExpression());
        locationExpressionEClass.getESuperTypes().add(this.getExpression());
        elifExpressionEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        compParamWrapExpressionEClass.getESuperTypes().add(this.getExpression());
        compInstWrapExpressionEClass.getESuperTypes().add(this.getExpression());
        componentExpressionEClass.getESuperTypes().add(this.getExpression());
        compParamExpressionEClass.getESuperTypes().add(this.getExpression());
        constantExpressionEClass.getESuperTypes().add(this.getExpression());
        tauExpressionEClass.getESuperTypes().add(this.getExpression());
        projectionExpressionEClass.getESuperTypes().add(this.getExpression());
        sliceExpressionEClass.getESuperTypes().add(this.getExpression());
        baseFunctionExpressionEClass.getESuperTypes().add(this.getExpression());
        stdLibFunctionExpressionEClass.getESuperTypes().add(this.getBaseFunctionExpression());
        realExpressionEClass.getESuperTypes().add(this.getExpression());
        timeExpressionEClass.getESuperTypes().add(this.getExpression());
        listExpressionEClass.getESuperTypes().add(this.getExpression());
        setExpressionEClass.getESuperTypes().add(this.getExpression());
        dictExpressionEClass.getESuperTypes().add(this.getExpression());
        dictPairEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        tupleExpressionEClass.getESuperTypes().add(this.getExpression());
        castExpressionEClass.getESuperTypes().add(this.getExpression());
        stringExpressionEClass.getESuperTypes().add(this.getExpression());
        fieldExpressionEClass.getESuperTypes().add(this.getExpression());
        functionExpressionEClass.getESuperTypes().add(this.getBaseFunctionExpression());
        contVariableExpressionEClass.getESuperTypes().add(this.getExpression());
        inputVariableExpressionEClass.getESuperTypes().add(this.getExpression());
        receivedExpressionEClass.getESuperTypes().add(this.getExpression());
        selfExpressionEClass.getESuperTypes().add(this.getExpression());
        switchExpressionEClass.getESuperTypes().add(this.getExpression());
        switchCaseEClass.getESuperTypes().add(thePositionPackage.getPositionObject());

        // Initialize classes, features, and operations; add parameters
        initEClass(expressionEClass, Expression.class, "Expression", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExpression_Type(), theTypesPackage.getCifType(), null, "type", null, 1, 1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(binaryExpressionEClass, BinaryExpression.class, "BinaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBinaryExpression_Operator(), this.getBinaryOperator(), "operator", null, 1, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryExpression_Left(), this.getExpression(), null, "left", null, 1, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryExpression_Right(), this.getExpression(), null, "right", null, 1, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(unaryExpressionEClass, UnaryExpression.class, "UnaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getUnaryExpression_Operator(), this.getUnaryOperator(), "operator", null, 1, 1, UnaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnaryExpression_Child(), this.getExpression(), null, "child", null, 1, 1, UnaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(boolExpressionEClass, BoolExpression.class, "BoolExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBoolExpression_Value(), ecorePackage.getEBoolean(), "value", null, 1, 1, BoolExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(intExpressionEClass, IntExpression.class, "IntExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getIntExpression_Value(), ecorePackage.getEInt(), "value", null, 1, 1, IntExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(functionCallExpressionEClass, FunctionCallExpression.class, "FunctionCallExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFunctionCallExpression_Arguments(), this.getExpression(), null, "arguments", null, 0, -1, FunctionCallExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFunctionCallExpression_Function(), this.getExpression(), null, "function", null, 1, 1, FunctionCallExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ifExpressionEClass, IfExpression.class, "IfExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIfExpression_Guards(), this.getExpression(), null, "guards", null, 1, -1, IfExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfExpression_Then(), this.getExpression(), null, "then", null, 1, 1, IfExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfExpression_Else(), this.getExpression(), null, "else", null, 1, 1, IfExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfExpression_Elifs(), this.getElifExpression(), null, "elifs", null, 0, -1, IfExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(discVariableExpressionEClass, DiscVariableExpression.class, "DiscVariableExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDiscVariableExpression_Variable(), theDeclarationsPackage.getDiscVariable(), null, "variable", null, 1, 1, DiscVariableExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(algVariableExpressionEClass, AlgVariableExpression.class, "AlgVariableExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAlgVariableExpression_Variable(), theDeclarationsPackage.getAlgVariable(), null, "variable", null, 1, 1, AlgVariableExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(eventExpressionEClass, EventExpression.class, "EventExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEventExpression_Event(), theDeclarationsPackage.getEvent(), null, "event", null, 1, 1, EventExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(enumLiteralExpressionEClass, EnumLiteralExpression.class, "EnumLiteralExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEnumLiteralExpression_Literal(), theDeclarationsPackage.getEnumLiteral(), null, "literal", null, 1, 1, EnumLiteralExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(locationExpressionEClass, LocationExpression.class, "LocationExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLocationExpression_Location(), theAutomataPackage.getLocation(), null, "location", null, 1, 1, LocationExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(elifExpressionEClass, ElifExpression.class, "ElifExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getElifExpression_Guards(), this.getExpression(), null, "guards", null, 1, -1, ElifExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getElifExpression_Then(), this.getExpression(), null, "then", null, 1, 1, ElifExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(compParamWrapExpressionEClass, CompParamWrapExpression.class, "CompParamWrapExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCompParamWrapExpression_Reference(), this.getExpression(), null, "reference", null, 1, 1, CompParamWrapExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCompParamWrapExpression_Parameter(), theCifPackage.getComponentParameter(), null, "parameter", null, 1, 1, CompParamWrapExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(compInstWrapExpressionEClass, CompInstWrapExpression.class, "CompInstWrapExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCompInstWrapExpression_Reference(), this.getExpression(), null, "reference", null, 1, 1, CompInstWrapExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCompInstWrapExpression_Instantiation(), theCifPackage.getComponentInst(), null, "instantiation", null, 1, 1, CompInstWrapExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentExpressionEClass, ComponentExpression.class, "ComponentExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentExpression_Component(), theCifPackage.getComponent(), null, "component", null, 1, 1, ComponentExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(compParamExpressionEClass, CompParamExpression.class, "CompParamExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCompParamExpression_Parameter(), theCifPackage.getComponentParameter(), null, "parameter", null, 1, 1, CompParamExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(constantExpressionEClass, ConstantExpression.class, "ConstantExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConstantExpression_Constant(), theDeclarationsPackage.getConstant(), null, "constant", null, 1, 1, ConstantExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tauExpressionEClass, TauExpression.class, "TauExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(projectionExpressionEClass, ProjectionExpression.class, "ProjectionExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProjectionExpression_Index(), this.getExpression(), null, "index", null, 1, 1, ProjectionExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProjectionExpression_Child(), this.getExpression(), null, "child", null, 1, 1, ProjectionExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sliceExpressionEClass, SliceExpression.class, "SliceExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSliceExpression_Begin(), this.getExpression(), null, "begin", null, 0, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSliceExpression_End(), this.getExpression(), null, "end", null, 0, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSliceExpression_Child(), this.getExpression(), null, "child", null, 1, 1, SliceExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(baseFunctionExpressionEClass, BaseFunctionExpression.class, "BaseFunctionExpression", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(stdLibFunctionExpressionEClass, StdLibFunctionExpression.class, "StdLibFunctionExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStdLibFunctionExpression_Function(), this.getStdLibFunction(), "function", null, 1, 1, StdLibFunctionExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(realExpressionEClass, RealExpression.class, "RealExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRealExpression_Value(), ecorePackage.getEString(), "value", null, 1, 1, RealExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(timeExpressionEClass, TimeExpression.class, "TimeExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(listExpressionEClass, ListExpression.class, "ListExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getListExpression_Elements(), this.getExpression(), null, "elements", null, 0, -1, ListExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(setExpressionEClass, SetExpression.class, "SetExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSetExpression_Elements(), this.getExpression(), null, "elements", null, 0, -1, SetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dictExpressionEClass, DictExpression.class, "DictExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDictExpression_Pairs(), this.getDictPair(), null, "pairs", null, 0, -1, DictExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dictPairEClass, DictPair.class, "DictPair", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDictPair_Key(), this.getExpression(), null, "key", null, 1, 1, DictPair.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDictPair_Value(), this.getExpression(), null, "value", null, 1, 1, DictPair.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tupleExpressionEClass, TupleExpression.class, "TupleExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTupleExpression_Fields(), this.getExpression(), null, "fields", null, 2, -1, TupleExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(castExpressionEClass, CastExpression.class, "CastExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCastExpression_Child(), this.getExpression(), null, "child", null, 1, 1, CastExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(stringExpressionEClass, StringExpression.class, "StringExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStringExpression_Value(), ecorePackage.getEString(), "value", null, 1, 1, StringExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(fieldExpressionEClass, FieldExpression.class, "FieldExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFieldExpression_Field(), theTypesPackage.getField(), null, "field", null, 1, 1, FieldExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(functionExpressionEClass, FunctionExpression.class, "FunctionExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFunctionExpression_Function(), theFunctionsPackage.getFunction(), null, "function", null, 1, 1, FunctionExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(contVariableExpressionEClass, ContVariableExpression.class, "ContVariableExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getContVariableExpression_Variable(), theDeclarationsPackage.getContVariable(), null, "variable", null, 1, 1, ContVariableExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContVariableExpression_Derivative(), ecorePackage.getEBoolean(), "derivative", null, 1, 1, ContVariableExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(inputVariableExpressionEClass, InputVariableExpression.class, "InputVariableExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInputVariableExpression_Variable(), theDeclarationsPackage.getInputVariable(), null, "variable", null, 1, 1, InputVariableExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(receivedExpressionEClass, ReceivedExpression.class, "ReceivedExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(selfExpressionEClass, SelfExpression.class, "SelfExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(switchExpressionEClass, SwitchExpression.class, "SwitchExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSwitchExpression_Value(), this.getExpression(), null, "value", null, 1, 1, SwitchExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSwitchExpression_Cases(), this.getSwitchCase(), null, "cases", null, 1, -1, SwitchExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(switchCaseEClass, SwitchCase.class, "SwitchCase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSwitchCase_Key(), this.getExpression(), null, "key", null, 0, 1, SwitchCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSwitchCase_Value(), this.getExpression(), null, "value", null, 1, 1, SwitchCase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(unaryOperatorEEnum, UnaryOperator.class, "UnaryOperator");
        addEEnumLiteral(unaryOperatorEEnum, UnaryOperator.INVERSE);
        addEEnumLiteral(unaryOperatorEEnum, UnaryOperator.NEGATE);
        addEEnumLiteral(unaryOperatorEEnum, UnaryOperator.PLUS);
        addEEnumLiteral(unaryOperatorEEnum, UnaryOperator.SAMPLE);

        initEEnum(binaryOperatorEEnum, BinaryOperator.class, "BinaryOperator");
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.DISJUNCTION);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.IMPLICATION);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.BI_CONDITIONAL);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.CONJUNCTION);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.LESS_THAN);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.LESS_EQUAL);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.GREATER_THAN);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.GREATER_EQUAL);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.EQUAL);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.UNEQUAL);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.MODULUS);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.INTEGER_DIVISION);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.MULTIPLICATION);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.SUBTRACTION);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.ADDITION);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.SUBSET);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.ELEMENT_OF);
        addEEnumLiteral(binaryOperatorEEnum, BinaryOperator.DIVISION);

        initEEnum(stdLibFunctionEEnum, StdLibFunction.class, "StdLibFunction");
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.MINIMUM);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.MAXIMUM);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.POWER);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.SIGN);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.CBRT);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.CEIL);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.DELETE);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.EMPTY);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.EXP);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.FLOOR);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.LN);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.LOG);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.POP);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ROUND);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.SIZE);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.SQRT);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ACOSH);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ACOS);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ASINH);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ASIN);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ATANH);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ATAN);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.COSH);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.COS);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.SINH);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.SIN);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.TANH);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.TAN);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ABS);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.BERNOULLI);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.BETA);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.BINOMIAL);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.CONSTANT);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.ERLANG);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.EXPONENTIAL);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.GAMMA);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.GEOMETRIC);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.LOG_NORMAL);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.NORMAL);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.POISSON);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.RANDOM);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.TRIANGLE);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.UNIFORM);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.WEIBULL);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.FORMAT);
        addEEnumLiteral(stdLibFunctionEEnum, StdLibFunction.SCALE);
    }

} //ExpressionsPackageImpl
