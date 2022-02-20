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
package org.eclipse.escet.cif.metamodel.cif.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.CifFactory;
import org.eclipse.escet.cif.metamodel.cif.CifPackage;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Parameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;

import org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage;

import org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;

import org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage;

import org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.print.PrintPackage;

import org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.types.TypesPackage;

import org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.util.CifValidator;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CifPackageImpl extends EPackageImpl implements CifPackage
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass groupEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentDefEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentInstEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass complexComponentEClass = null;

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
    private EClass parameterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass eventParameterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass locationParameterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass algParameterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentParameterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass equationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ioDeclEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass invariantEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum supKindEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum invKindEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType cifIdentifierEDataType = null;

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
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private CifPackageImpl()
    {
        super(eNS_URI, CifFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link CifPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static CifPackage init()
    {
        if (isInited) return (CifPackage)EPackage.Registry.INSTANCE.getEPackage(CifPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredCifPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        CifPackageImpl theCifPackage = registeredCifPackage instanceof CifPackageImpl ? (CifPackageImpl)registeredCifPackage : new CifPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        PositionPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeclarationsPackage.eNS_URI);
        DeclarationsPackageImpl theDeclarationsPackage = (DeclarationsPackageImpl)(registeredPackage instanceof DeclarationsPackageImpl ? registeredPackage : DeclarationsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AutomataPackage.eNS_URI);
        AutomataPackageImpl theAutomataPackage = (AutomataPackageImpl)(registeredPackage instanceof AutomataPackageImpl ? registeredPackage : AutomataPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        TypesPackageImpl theTypesPackage = (TypesPackageImpl)(registeredPackage instanceof TypesPackageImpl ? registeredPackage : TypesPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);
        ExpressionsPackageImpl theExpressionsPackage = (ExpressionsPackageImpl)(registeredPackage instanceof ExpressionsPackageImpl ? registeredPackage : ExpressionsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(FunctionsPackage.eNS_URI);
        FunctionsPackageImpl theFunctionsPackage = (FunctionsPackageImpl)(registeredPackage instanceof FunctionsPackageImpl ? registeredPackage : FunctionsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(CifsvgPackage.eNS_URI);
        CifsvgPackageImpl theCifsvgPackage = (CifsvgPackageImpl)(registeredPackage instanceof CifsvgPackageImpl ? registeredPackage : CifsvgPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(PrintPackage.eNS_URI);
        PrintPackageImpl thePrintPackage = (PrintPackageImpl)(registeredPackage instanceof PrintPackageImpl ? registeredPackage : PrintPackage.eINSTANCE);

        // Create package meta-data objects
        theCifPackage.createPackageContents();
        theDeclarationsPackage.createPackageContents();
        theAutomataPackage.createPackageContents();
        theTypesPackage.createPackageContents();
        theExpressionsPackage.createPackageContents();
        theFunctionsPackage.createPackageContents();
        theCifsvgPackage.createPackageContents();
        thePrintPackage.createPackageContents();

        // Initialize created meta-data
        theCifPackage.initializePackageContents();
        theDeclarationsPackage.initializePackageContents();
        theAutomataPackage.initializePackageContents();
        theTypesPackage.initializePackageContents();
        theExpressionsPackage.initializePackageContents();
        theFunctionsPackage.initializePackageContents();
        theCifsvgPackage.initializePackageContents();
        thePrintPackage.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (theCifPackage,
             new EValidator.Descriptor()
             {
                 @Override
                 public EValidator getEValidator()
                 {
                     return CifValidator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        theCifPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(CifPackage.eNS_URI, theCifPackage);
        return theCifPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComponent()
    {
        return componentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getComponent_Name()
    {
        return (EAttribute)componentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getGroup()
    {
        return groupEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getGroup_Definitions()
    {
        return (EReference)groupEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getGroup_Components()
    {
        return (EReference)groupEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComponentDef()
    {
        return componentDefEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComponentDef_Body()
    {
        return (EReference)componentDefEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComponentDef_Parameters()
    {
        return (EReference)componentDefEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComponentInst()
    {
        return componentInstEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComponentInst_Definition()
    {
        return (EReference)componentInstEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComponentInst_Parameters()
    {
        return (EReference)componentInstEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComplexComponent()
    {
        return complexComponentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComplexComponent_Declarations()
    {
        return (EReference)complexComponentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComplexComponent_Markeds()
    {
        return (EReference)complexComponentEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComplexComponent_Invariants()
    {
        return (EReference)complexComponentEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComplexComponent_Initials()
    {
        return (EReference)complexComponentEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComplexComponent_Equations()
    {
        return (EReference)complexComponentEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComplexComponent_IoDecls()
    {
        return (EReference)complexComponentEClass.getEStructuralFeatures().get(5);
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
    public EClass getParameter()
    {
        return parameterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEventParameter()
    {
        return eventParameterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEventParameter_Event()
    {
        return (EReference)eventParameterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEventParameter_SendFlag()
    {
        return (EAttribute)eventParameterEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEventParameter_RecvFlag()
    {
        return (EAttribute)eventParameterEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEventParameter_SyncFlag()
    {
        return (EAttribute)eventParameterEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLocationParameter()
    {
        return locationParameterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getLocationParameter_Location()
    {
        return (EReference)locationParameterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAlgParameter()
    {
        return algParameterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAlgParameter_Variable()
    {
        return (EReference)algParameterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComponentParameter()
    {
        return componentParameterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComponentParameter_Type()
    {
        return (EReference)componentParameterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getComponentParameter_Name()
    {
        return (EAttribute)componentParameterEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEquation()
    {
        return equationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEquation_Derivative()
    {
        return (EAttribute)equationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEquation_Value()
    {
        return (EReference)equationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEquation_Variable()
    {
        return (EReference)equationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIoDecl()
    {
        return ioDeclEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInvariant()
    {
        return invariantEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getInvariant_SupKind()
    {
        return (EAttribute)invariantEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getInvariant_Predicate()
    {
        return (EReference)invariantEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getInvariant_InvKind()
    {
        return (EAttribute)invariantEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getInvariant_Event()
    {
        return (EReference)invariantEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getSupKind()
    {
        return supKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getInvKind()
    {
        return invKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getCifIdentifier()
    {
        return cifIdentifierEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CifFactory getCifFactory()
    {
        return (CifFactory)getEFactoryInstance();
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
        componentEClass = createEClass(COMPONENT);
        createEAttribute(componentEClass, COMPONENT__NAME);

        groupEClass = createEClass(GROUP);
        createEReference(groupEClass, GROUP__DEFINITIONS);
        createEReference(groupEClass, GROUP__COMPONENTS);

        componentDefEClass = createEClass(COMPONENT_DEF);
        createEReference(componentDefEClass, COMPONENT_DEF__BODY);
        createEReference(componentDefEClass, COMPONENT_DEF__PARAMETERS);

        componentInstEClass = createEClass(COMPONENT_INST);
        createEReference(componentInstEClass, COMPONENT_INST__DEFINITION);
        createEReference(componentInstEClass, COMPONENT_INST__PARAMETERS);

        complexComponentEClass = createEClass(COMPLEX_COMPONENT);
        createEReference(complexComponentEClass, COMPLEX_COMPONENT__DECLARATIONS);
        createEReference(complexComponentEClass, COMPLEX_COMPONENT__MARKEDS);
        createEReference(complexComponentEClass, COMPLEX_COMPONENT__INVARIANTS);
        createEReference(complexComponentEClass, COMPLEX_COMPONENT__INITIALS);
        createEReference(complexComponentEClass, COMPLEX_COMPONENT__EQUATIONS);
        createEReference(complexComponentEClass, COMPLEX_COMPONENT__IO_DECLS);

        specificationEClass = createEClass(SPECIFICATION);

        parameterEClass = createEClass(PARAMETER);

        eventParameterEClass = createEClass(EVENT_PARAMETER);
        createEReference(eventParameterEClass, EVENT_PARAMETER__EVENT);
        createEAttribute(eventParameterEClass, EVENT_PARAMETER__SEND_FLAG);
        createEAttribute(eventParameterEClass, EVENT_PARAMETER__RECV_FLAG);
        createEAttribute(eventParameterEClass, EVENT_PARAMETER__SYNC_FLAG);

        locationParameterEClass = createEClass(LOCATION_PARAMETER);
        createEReference(locationParameterEClass, LOCATION_PARAMETER__LOCATION);

        algParameterEClass = createEClass(ALG_PARAMETER);
        createEReference(algParameterEClass, ALG_PARAMETER__VARIABLE);

        componentParameterEClass = createEClass(COMPONENT_PARAMETER);
        createEReference(componentParameterEClass, COMPONENT_PARAMETER__TYPE);
        createEAttribute(componentParameterEClass, COMPONENT_PARAMETER__NAME);

        equationEClass = createEClass(EQUATION);
        createEAttribute(equationEClass, EQUATION__DERIVATIVE);
        createEReference(equationEClass, EQUATION__VALUE);
        createEReference(equationEClass, EQUATION__VARIABLE);

        ioDeclEClass = createEClass(IO_DECL);

        invariantEClass = createEClass(INVARIANT);
        createEAttribute(invariantEClass, INVARIANT__SUP_KIND);
        createEReference(invariantEClass, INVARIANT__PREDICATE);
        createEAttribute(invariantEClass, INVARIANT__INV_KIND);
        createEReference(invariantEClass, INVARIANT__EVENT);

        // Create enums
        supKindEEnum = createEEnum(SUP_KIND);
        invKindEEnum = createEEnum(INV_KIND);

        // Create data types
        cifIdentifierEDataType = createEDataType(CIF_IDENTIFIER);
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
        AutomataPackage theAutomataPackage = (AutomataPackage)EPackage.Registry.INSTANCE.getEPackage(AutomataPackage.eNS_URI);
        TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        ExpressionsPackage theExpressionsPackage = (ExpressionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);
        FunctionsPackage theFunctionsPackage = (FunctionsPackage)EPackage.Registry.INSTANCE.getEPackage(FunctionsPackage.eNS_URI);
        CifsvgPackage theCifsvgPackage = (CifsvgPackage)EPackage.Registry.INSTANCE.getEPackage(CifsvgPackage.eNS_URI);
        PrintPackage thePrintPackage = (PrintPackage)EPackage.Registry.INSTANCE.getEPackage(PrintPackage.eNS_URI);
        PositionPackage thePositionPackage = (PositionPackage)EPackage.Registry.INSTANCE.getEPackage(PositionPackage.eNS_URI);

        // Add subpackages
        getESubpackages().add(theDeclarationsPackage);
        getESubpackages().add(theAutomataPackage);
        getESubpackages().add(theTypesPackage);
        getESubpackages().add(theExpressionsPackage);
        getESubpackages().add(theFunctionsPackage);
        getESubpackages().add(theCifsvgPackage);
        getESubpackages().add(thePrintPackage);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        componentEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        groupEClass.getESuperTypes().add(this.getComplexComponent());
        componentDefEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        componentInstEClass.getESuperTypes().add(this.getComponent());
        complexComponentEClass.getESuperTypes().add(this.getComponent());
        specificationEClass.getESuperTypes().add(this.getGroup());
        parameterEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        eventParameterEClass.getESuperTypes().add(this.getParameter());
        locationParameterEClass.getESuperTypes().add(this.getParameter());
        algParameterEClass.getESuperTypes().add(this.getParameter());
        componentParameterEClass.getESuperTypes().add(this.getParameter());
        equationEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        ioDeclEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        invariantEClass.getESuperTypes().add(thePositionPackage.getPositionObject());

        // Initialize classes, features, and operations; add parameters
        initEClass(componentEClass, Component.class, "Component", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getComponent_Name(), this.getCifIdentifier(), "name", null, 1, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(groupEClass, Group.class, "Group", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGroup_Definitions(), this.getComponentDef(), null, "definitions", null, 0, -1, Group.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGroup_Components(), this.getComponent(), null, "components", null, 0, -1, Group.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentDefEClass, ComponentDef.class, "ComponentDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentDef_Body(), this.getComplexComponent(), null, "body", null, 1, 1, ComponentDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponentDef_Parameters(), this.getParameter(), null, "parameters", null, 0, -1, ComponentDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentInstEClass, ComponentInst.class, "ComponentInst", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentInst_Definition(), theTypesPackage.getCifType(), null, "definition", null, 1, 1, ComponentInst.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponentInst_Parameters(), theExpressionsPackage.getExpression(), null, "parameters", null, 0, -1, ComponentInst.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(complexComponentEClass, ComplexComponent.class, "ComplexComponent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComplexComponent_Declarations(), theDeclarationsPackage.getDeclaration(), null, "declarations", null, 0, -1, ComplexComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComplexComponent_Markeds(), theExpressionsPackage.getExpression(), null, "markeds", null, 0, -1, ComplexComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComplexComponent_Invariants(), this.getInvariant(), null, "invariants", null, 0, -1, ComplexComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComplexComponent_Initials(), theExpressionsPackage.getExpression(), null, "initials", null, 0, -1, ComplexComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComplexComponent_Equations(), this.getEquation(), null, "equations", null, 0, -1, ComplexComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComplexComponent_IoDecls(), this.getIoDecl(), null, "ioDecls", null, 0, -1, ComplexComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(specificationEClass, Specification.class, "Specification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(parameterEClass, Parameter.class, "Parameter", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(eventParameterEClass, EventParameter.class, "EventParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEventParameter_Event(), theDeclarationsPackage.getEvent(), null, "event", null, 1, 1, EventParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEventParameter_SendFlag(), ecorePackage.getEBoolean(), "sendFlag", null, 1, 1, EventParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEventParameter_RecvFlag(), ecorePackage.getEBoolean(), "recvFlag", null, 1, 1, EventParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEventParameter_SyncFlag(), ecorePackage.getEBoolean(), "syncFlag", null, 1, 1, EventParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(locationParameterEClass, LocationParameter.class, "LocationParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLocationParameter_Location(), theAutomataPackage.getLocation(), null, "location", null, 1, 1, LocationParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(algParameterEClass, AlgParameter.class, "AlgParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAlgParameter_Variable(), theDeclarationsPackage.getAlgVariable(), null, "variable", null, 1, 1, AlgParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentParameterEClass, ComponentParameter.class, "ComponentParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentParameter_Type(), theTypesPackage.getCifType(), null, "type", null, 1, 1, ComponentParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponentParameter_Name(), this.getCifIdentifier(), "name", null, 1, 1, ComponentParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(equationEClass, Equation.class, "Equation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEquation_Derivative(), ecorePackage.getEBoolean(), "derivative", null, 1, 1, Equation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEquation_Value(), theExpressionsPackage.getExpression(), null, "value", null, 1, 1, Equation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEquation_Variable(), theDeclarationsPackage.getDeclaration(), null, "variable", null, 1, 1, Equation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ioDeclEClass, IoDecl.class, "IoDecl", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(invariantEClass, Invariant.class, "Invariant", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInvariant_SupKind(), this.getSupKind(), "supKind", null, 1, 1, Invariant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInvariant_Predicate(), theExpressionsPackage.getExpression(), null, "predicate", null, 1, 1, Invariant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInvariant_InvKind(), this.getInvKind(), "invKind", null, 1, 1, Invariant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInvariant_Event(), theExpressionsPackage.getExpression(), null, "event", null, 0, 1, Invariant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(supKindEEnum, SupKind.class, "SupKind");
        addEEnumLiteral(supKindEEnum, SupKind.NONE);
        addEEnumLiteral(supKindEEnum, SupKind.PLANT);
        addEEnumLiteral(supKindEEnum, SupKind.REQUIREMENT);
        addEEnumLiteral(supKindEEnum, SupKind.SUPERVISOR);

        initEEnum(invKindEEnum, InvKind.class, "InvKind");
        addEEnumLiteral(invKindEEnum, InvKind.STATE);
        addEEnumLiteral(invKindEEnum, InvKind.EVENT_NEEDS);
        addEEnumLiteral(invKindEEnum, InvKind.EVENT_DISABLES);

        // Initialize data types
        initEDataType(cifIdentifierEDataType, String.class, "CifIdentifier", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

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
          (cifIdentifierEDataType,
           source,
           new String[]
           {
               "name", "CifIdentifier",
               "baseType", "http://www.eclipse.org/emf/2002/Ecore#EString",
               "pattern", "[A-Za-z_][A-Za-z0-9_]*"
           });
    }

} //CifPackageImpl
