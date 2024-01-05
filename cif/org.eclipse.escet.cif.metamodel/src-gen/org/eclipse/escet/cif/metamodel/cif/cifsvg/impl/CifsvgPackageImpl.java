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
package org.eclipse.escet.cif.metamodel.cif.cifsvg.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationsPackage;

import org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;

import org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgFactory;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;

import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage;

import org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;

import org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl;

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
public class CifsvgPackageImpl extends EPackageImpl implements CifsvgPackage
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgFileEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgOutEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgInEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgInEventSingleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgInEventIfEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgInEventIfEntryEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgCopyEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgMoveEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass svgInEventEClass = null;

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
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private CifsvgPackageImpl()
    {
        super(eNS_URI, CifsvgFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link CifsvgPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static CifsvgPackage init()
    {
        if (isInited) return (CifsvgPackage)EPackage.Registry.INSTANCE.getEPackage(CifsvgPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredCifsvgPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        CifsvgPackageImpl theCifsvgPackage = registeredCifsvgPackage instanceof CifsvgPackageImpl ? (CifsvgPackageImpl)registeredCifsvgPackage : new CifsvgPackageImpl();

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
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(FunctionsPackage.eNS_URI);
        FunctionsPackageImpl theFunctionsPackage = (FunctionsPackageImpl)(registeredPackage instanceof FunctionsPackageImpl ? registeredPackage : FunctionsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(PrintPackage.eNS_URI);
        PrintPackageImpl thePrintPackage = (PrintPackageImpl)(registeredPackage instanceof PrintPackageImpl ? registeredPackage : PrintPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnnotationsPackage.eNS_URI);
        AnnotationsPackageImpl theAnnotationsPackage = (AnnotationsPackageImpl)(registeredPackage instanceof AnnotationsPackageImpl ? registeredPackage : AnnotationsPackage.eINSTANCE);

        // Create package meta-data objects
        theCifsvgPackage.createPackageContents();
        theCifPackage.createPackageContents();
        theDeclarationsPackage.createPackageContents();
        theAutomataPackage.createPackageContents();
        theTypesPackage.createPackageContents();
        theExpressionsPackage.createPackageContents();
        theFunctionsPackage.createPackageContents();
        thePrintPackage.createPackageContents();
        theAnnotationsPackage.createPackageContents();

        // Initialize created meta-data
        theCifsvgPackage.initializePackageContents();
        theCifPackage.initializePackageContents();
        theDeclarationsPackage.initializePackageContents();
        theAutomataPackage.initializePackageContents();
        theTypesPackage.initializePackageContents();
        theExpressionsPackage.initializePackageContents();
        theFunctionsPackage.initializePackageContents();
        thePrintPackage.initializePackageContents();
        theAnnotationsPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theCifsvgPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(CifsvgPackage.eNS_URI, theCifsvgPackage);
        return theCifsvgPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgFile()
    {
        return svgFileEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getSvgFile_Path()
    {
        return (EAttribute)svgFileEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgOut()
    {
        return svgOutEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgOut_Id()
    {
        return (EReference)svgOutEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getSvgOut_Attr()
    {
        return (EAttribute)svgOutEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgOut_SvgFile()
    {
        return (EReference)svgOutEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgOut_AttrTextPos()
    {
        return (EReference)svgOutEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgOut_Value()
    {
        return (EReference)svgOutEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgIn()
    {
        return svgInEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgIn_Id()
    {
        return (EReference)svgInEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgIn_SvgFile()
    {
        return (EReference)svgInEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgIn_Event()
    {
        return (EReference)svgInEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgIn_Updates()
    {
        return (EReference)svgInEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgInEventSingle()
    {
        return svgInEventSingleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgInEventSingle_Event()
    {
        return (EReference)svgInEventSingleEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgInEventIf()
    {
        return svgInEventIfEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgInEventIf_Entries()
    {
        return (EReference)svgInEventIfEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgInEventIfEntry()
    {
        return svgInEventIfEntryEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgInEventIfEntry_Guard()
    {
        return (EReference)svgInEventIfEntryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgInEventIfEntry_Event()
    {
        return (EReference)svgInEventIfEntryEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgCopy()
    {
        return svgCopyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgCopy_Id()
    {
        return (EReference)svgCopyEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgCopy_Pre()
    {
        return (EReference)svgCopyEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgCopy_Post()
    {
        return (EReference)svgCopyEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgCopy_SvgFile()
    {
        return (EReference)svgCopyEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgMove()
    {
        return svgMoveEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgMove_Id()
    {
        return (EReference)svgMoveEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgMove_X()
    {
        return (EReference)svgMoveEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgMove_Y()
    {
        return (EReference)svgMoveEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSvgMove_SvgFile()
    {
        return (EReference)svgMoveEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSvgInEvent()
    {
        return svgInEventEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CifsvgFactory getCifsvgFactory()
    {
        return (CifsvgFactory)getEFactoryInstance();
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
        svgFileEClass = createEClass(SVG_FILE);
        createEAttribute(svgFileEClass, SVG_FILE__PATH);

        svgOutEClass = createEClass(SVG_OUT);
        createEReference(svgOutEClass, SVG_OUT__ID);
        createEAttribute(svgOutEClass, SVG_OUT__ATTR);
        createEReference(svgOutEClass, SVG_OUT__SVG_FILE);
        createEReference(svgOutEClass, SVG_OUT__ATTR_TEXT_POS);
        createEReference(svgOutEClass, SVG_OUT__VALUE);

        svgInEClass = createEClass(SVG_IN);
        createEReference(svgInEClass, SVG_IN__ID);
        createEReference(svgInEClass, SVG_IN__SVG_FILE);
        createEReference(svgInEClass, SVG_IN__EVENT);
        createEReference(svgInEClass, SVG_IN__UPDATES);

        svgInEventSingleEClass = createEClass(SVG_IN_EVENT_SINGLE);
        createEReference(svgInEventSingleEClass, SVG_IN_EVENT_SINGLE__EVENT);

        svgInEventIfEClass = createEClass(SVG_IN_EVENT_IF);
        createEReference(svgInEventIfEClass, SVG_IN_EVENT_IF__ENTRIES);

        svgInEventIfEntryEClass = createEClass(SVG_IN_EVENT_IF_ENTRY);
        createEReference(svgInEventIfEntryEClass, SVG_IN_EVENT_IF_ENTRY__GUARD);
        createEReference(svgInEventIfEntryEClass, SVG_IN_EVENT_IF_ENTRY__EVENT);

        svgCopyEClass = createEClass(SVG_COPY);
        createEReference(svgCopyEClass, SVG_COPY__ID);
        createEReference(svgCopyEClass, SVG_COPY__PRE);
        createEReference(svgCopyEClass, SVG_COPY__POST);
        createEReference(svgCopyEClass, SVG_COPY__SVG_FILE);

        svgMoveEClass = createEClass(SVG_MOVE);
        createEReference(svgMoveEClass, SVG_MOVE__ID);
        createEReference(svgMoveEClass, SVG_MOVE__X);
        createEReference(svgMoveEClass, SVG_MOVE__Y);
        createEReference(svgMoveEClass, SVG_MOVE__SVG_FILE);

        svgInEventEClass = createEClass(SVG_IN_EVENT);
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
        CifPackage theCifPackage = (CifPackage)EPackage.Registry.INSTANCE.getEPackage(CifPackage.eNS_URI);
        ExpressionsPackage theExpressionsPackage = (ExpressionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);
        PositionPackage thePositionPackage = (PositionPackage)EPackage.Registry.INSTANCE.getEPackage(PositionPackage.eNS_URI);
        AutomataPackage theAutomataPackage = (AutomataPackage)EPackage.Registry.INSTANCE.getEPackage(AutomataPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        svgFileEClass.getESuperTypes().add(theCifPackage.getIoDecl());
        svgOutEClass.getESuperTypes().add(theCifPackage.getIoDecl());
        svgInEClass.getESuperTypes().add(theCifPackage.getIoDecl());
        svgInEventSingleEClass.getESuperTypes().add(this.getSvgInEvent());
        svgInEventIfEClass.getESuperTypes().add(this.getSvgInEvent());
        svgInEventIfEntryEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        svgCopyEClass.getESuperTypes().add(theCifPackage.getIoDecl());
        svgMoveEClass.getESuperTypes().add(theCifPackage.getIoDecl());
        svgInEventEClass.getESuperTypes().add(thePositionPackage.getPositionObject());

        // Initialize classes, features, and operations; add parameters
        initEClass(svgFileEClass, SvgFile.class, "SvgFile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSvgFile_Path(), ecorePackage.getEString(), "path", null, 1, 1, SvgFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(svgOutEClass, SvgOut.class, "SvgOut", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSvgOut_Id(), theExpressionsPackage.getExpression(), null, "id", null, 1, 1, SvgOut.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSvgOut_Attr(), ecorePackage.getEString(), "attr", null, 0, 1, SvgOut.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgOut_SvgFile(), this.getSvgFile(), null, "svgFile", null, 0, 1, SvgOut.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgOut_AttrTextPos(), thePositionPackage.getPosition(), null, "attrTextPos", null, 1, 1, SvgOut.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgOut_Value(), theExpressionsPackage.getExpression(), null, "value", null, 1, 1, SvgOut.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(svgInEClass, SvgIn.class, "SvgIn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSvgIn_Id(), theExpressionsPackage.getExpression(), null, "id", null, 1, 1, SvgIn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgIn_SvgFile(), this.getSvgFile(), null, "svgFile", null, 0, 1, SvgIn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgIn_Event(), this.getSvgInEvent(), null, "event", null, 0, 1, SvgIn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgIn_Updates(), theAutomataPackage.getUpdate(), null, "updates", null, 0, -1, SvgIn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(svgInEventSingleEClass, SvgInEventSingle.class, "SvgInEventSingle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSvgInEventSingle_Event(), theExpressionsPackage.getExpression(), null, "event", null, 1, 1, SvgInEventSingle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(svgInEventIfEClass, SvgInEventIf.class, "SvgInEventIf", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSvgInEventIf_Entries(), this.getSvgInEventIfEntry(), null, "entries", null, 2, -1, SvgInEventIf.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(svgInEventIfEntryEClass, SvgInEventIfEntry.class, "SvgInEventIfEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSvgInEventIfEntry_Guard(), theExpressionsPackage.getExpression(), null, "guard", null, 0, 1, SvgInEventIfEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgInEventIfEntry_Event(), theExpressionsPackage.getExpression(), null, "event", null, 1, 1, SvgInEventIfEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(svgCopyEClass, SvgCopy.class, "SvgCopy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSvgCopy_Id(), theExpressionsPackage.getExpression(), null, "id", null, 1, 1, SvgCopy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgCopy_Pre(), theExpressionsPackage.getExpression(), null, "pre", null, 0, 1, SvgCopy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgCopy_Post(), theExpressionsPackage.getExpression(), null, "post", null, 0, 1, SvgCopy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgCopy_SvgFile(), this.getSvgFile(), null, "svgFile", null, 0, 1, SvgCopy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(svgMoveEClass, SvgMove.class, "SvgMove", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSvgMove_Id(), theExpressionsPackage.getExpression(), null, "id", null, 1, 1, SvgMove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgMove_X(), theExpressionsPackage.getExpression(), null, "x", null, 1, 1, SvgMove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgMove_Y(), theExpressionsPackage.getExpression(), null, "y", null, 1, 1, SvgMove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSvgMove_SvgFile(), this.getSvgFile(), null, "svgFile", null, 0, 1, SvgMove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(svgInEventEClass, SvgInEvent.class, "SvgInEvent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    }

} //CifsvgPackageImpl
