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
package org.eclipse.escet.cif.metamodel.cif.automata.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationsPackage;

import org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.AutomataFactory;
import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl;

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
public class AutomataPackageImpl extends EPackageImpl implements AutomataPackage
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass automatonEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass locationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass edgeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass updateEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass assignmentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ifUpdateEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass elifUpdateEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass alphabetEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass edgeEventEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass monitorsEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass edgeSendEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass edgeReceiveEClass = null;

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
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private AutomataPackageImpl()
    {
        super(eNS_URI, AutomataFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link AutomataPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static AutomataPackage init()
    {
        if (isInited) return (AutomataPackage)EPackage.Registry.INSTANCE.getEPackage(AutomataPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredAutomataPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        AutomataPackageImpl theAutomataPackage = registeredAutomataPackage instanceof AutomataPackageImpl ? (AutomataPackageImpl)registeredAutomataPackage : new AutomataPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        PositionPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(CifPackage.eNS_URI);
        CifPackageImpl theCifPackage = (CifPackageImpl)(registeredPackage instanceof CifPackageImpl ? registeredPackage : CifPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeclarationsPackage.eNS_URI);
        DeclarationsPackageImpl theDeclarationsPackage = (DeclarationsPackageImpl)(registeredPackage instanceof DeclarationsPackageImpl ? registeredPackage : DeclarationsPackage.eINSTANCE);
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
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnnotationsPackage.eNS_URI);
        AnnotationsPackageImpl theAnnotationsPackage = (AnnotationsPackageImpl)(registeredPackage instanceof AnnotationsPackageImpl ? registeredPackage : AnnotationsPackage.eINSTANCE);

        // Create package meta-data objects
        theAutomataPackage.createPackageContents();
        theCifPackage.createPackageContents();
        theDeclarationsPackage.createPackageContents();
        theTypesPackage.createPackageContents();
        theExpressionsPackage.createPackageContents();
        theFunctionsPackage.createPackageContents();
        theCifsvgPackage.createPackageContents();
        thePrintPackage.createPackageContents();
        theAnnotationsPackage.createPackageContents();

        // Initialize created meta-data
        theAutomataPackage.initializePackageContents();
        theCifPackage.initializePackageContents();
        theDeclarationsPackage.initializePackageContents();
        theTypesPackage.initializePackageContents();
        theExpressionsPackage.initializePackageContents();
        theFunctionsPackage.initializePackageContents();
        theCifsvgPackage.initializePackageContents();
        thePrintPackage.initializePackageContents();
        theAnnotationsPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theAutomataPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(AutomataPackage.eNS_URI, theAutomataPackage);
        return theAutomataPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAutomaton()
    {
        return automatonEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAutomaton_Locations()
    {
        return (EReference)automatonEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAutomaton_Alphabet()
    {
        return (EReference)automatonEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAutomaton_Monitors()
    {
        return (EReference)automatonEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getAutomaton_Kind()
    {
        return (EAttribute)automatonEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLocation()
    {
        return locationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLocation_Name()
    {
        return (EAttribute)locationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getLocation_Initials()
    {
        return (EReference)locationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getLocation_Invariants()
    {
        return (EReference)locationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getLocation_Edges()
    {
        return (EReference)locationEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getLocation_Markeds()
    {
        return (EReference)locationEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLocation_Urgent()
    {
        return (EAttribute)locationEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getLocation_Equations()
    {
        return (EReference)locationEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEdge()
    {
        return edgeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEdge_Target()
    {
        return (EReference)edgeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEdge_Events()
    {
        return (EReference)edgeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEdge_Guards()
    {
        return (EReference)edgeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEdge_Updates()
    {
        return (EReference)edgeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEdge_Urgent()
    {
        return (EAttribute)edgeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUpdate()
    {
        return updateEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssignment()
    {
        return assignmentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignment_Addressable()
    {
        return (EReference)assignmentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignment_Value()
    {
        return (EReference)assignmentEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIfUpdate()
    {
        return ifUpdateEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfUpdate_Guards()
    {
        return (EReference)ifUpdateEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfUpdate_Thens()
    {
        return (EReference)ifUpdateEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfUpdate_Elifs()
    {
        return (EReference)ifUpdateEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfUpdate_Elses()
    {
        return (EReference)ifUpdateEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getElifUpdate()
    {
        return elifUpdateEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElifUpdate_Guards()
    {
        return (EReference)elifUpdateEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElifUpdate_Thens()
    {
        return (EReference)elifUpdateEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAlphabet()
    {
        return alphabetEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAlphabet_Events()
    {
        return (EReference)alphabetEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEdgeEvent()
    {
        return edgeEventEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEdgeEvent_Event()
    {
        return (EReference)edgeEventEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMonitors()
    {
        return monitorsEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMonitors_Events()
    {
        return (EReference)monitorsEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEdgeSend()
    {
        return edgeSendEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEdgeSend_Value()
    {
        return (EReference)edgeSendEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEdgeReceive()
    {
        return edgeReceiveEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AutomataFactory getAutomataFactory()
    {
        return (AutomataFactory)getEFactoryInstance();
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
        automatonEClass = createEClass(AUTOMATON);
        createEReference(automatonEClass, AUTOMATON__LOCATIONS);
        createEReference(automatonEClass, AUTOMATON__ALPHABET);
        createEReference(automatonEClass, AUTOMATON__MONITORS);
        createEAttribute(automatonEClass, AUTOMATON__KIND);

        locationEClass = createEClass(LOCATION);
        createEAttribute(locationEClass, LOCATION__NAME);
        createEReference(locationEClass, LOCATION__INITIALS);
        createEReference(locationEClass, LOCATION__INVARIANTS);
        createEReference(locationEClass, LOCATION__EDGES);
        createEReference(locationEClass, LOCATION__MARKEDS);
        createEAttribute(locationEClass, LOCATION__URGENT);
        createEReference(locationEClass, LOCATION__EQUATIONS);

        edgeEClass = createEClass(EDGE);
        createEReference(edgeEClass, EDGE__TARGET);
        createEReference(edgeEClass, EDGE__EVENTS);
        createEReference(edgeEClass, EDGE__GUARDS);
        createEReference(edgeEClass, EDGE__UPDATES);
        createEAttribute(edgeEClass, EDGE__URGENT);

        updateEClass = createEClass(UPDATE);

        assignmentEClass = createEClass(ASSIGNMENT);
        createEReference(assignmentEClass, ASSIGNMENT__ADDRESSABLE);
        createEReference(assignmentEClass, ASSIGNMENT__VALUE);

        ifUpdateEClass = createEClass(IF_UPDATE);
        createEReference(ifUpdateEClass, IF_UPDATE__GUARDS);
        createEReference(ifUpdateEClass, IF_UPDATE__THENS);
        createEReference(ifUpdateEClass, IF_UPDATE__ELIFS);
        createEReference(ifUpdateEClass, IF_UPDATE__ELSES);

        elifUpdateEClass = createEClass(ELIF_UPDATE);
        createEReference(elifUpdateEClass, ELIF_UPDATE__GUARDS);
        createEReference(elifUpdateEClass, ELIF_UPDATE__THENS);

        alphabetEClass = createEClass(ALPHABET);
        createEReference(alphabetEClass, ALPHABET__EVENTS);

        edgeEventEClass = createEClass(EDGE_EVENT);
        createEReference(edgeEventEClass, EDGE_EVENT__EVENT);

        monitorsEClass = createEClass(MONITORS);
        createEReference(monitorsEClass, MONITORS__EVENTS);

        edgeSendEClass = createEClass(EDGE_SEND);
        createEReference(edgeSendEClass, EDGE_SEND__VALUE);

        edgeReceiveEClass = createEClass(EDGE_RECEIVE);
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
        PositionPackage thePositionPackage = (PositionPackage)EPackage.Registry.INSTANCE.getEPackage(PositionPackage.eNS_URI);
        ExpressionsPackage theExpressionsPackage = (ExpressionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        automatonEClass.getESuperTypes().add(theCifPackage.getComplexComponent());
        locationEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        edgeEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        updateEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        assignmentEClass.getESuperTypes().add(this.getUpdate());
        ifUpdateEClass.getESuperTypes().add(this.getUpdate());
        elifUpdateEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        alphabetEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        edgeEventEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        monitorsEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        edgeSendEClass.getESuperTypes().add(this.getEdgeEvent());
        edgeReceiveEClass.getESuperTypes().add(this.getEdgeEvent());

        // Initialize classes, features, and operations; add parameters
        initEClass(automatonEClass, Automaton.class, "Automaton", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAutomaton_Locations(), this.getLocation(), null, "locations", null, 1, -1, Automaton.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAutomaton_Alphabet(), this.getAlphabet(), null, "alphabet", null, 0, 1, Automaton.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAutomaton_Monitors(), this.getMonitors(), null, "monitors", null, 0, 1, Automaton.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAutomaton_Kind(), theCifPackage.getSupKind(), "kind", null, 1, 1, Automaton.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(locationEClass, Location.class, "Location", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLocation_Name(), theCifPackage.getCifIdentifier(), "name", null, 0, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLocation_Initials(), theExpressionsPackage.getExpression(), null, "initials", null, 0, -1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLocation_Invariants(), theCifPackage.getInvariant(), null, "invariants", null, 0, -1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLocation_Edges(), this.getEdge(), null, "edges", null, 0, -1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLocation_Markeds(), theExpressionsPackage.getExpression(), null, "markeds", null, 0, -1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLocation_Urgent(), ecorePackage.getEBoolean(), "urgent", null, 1, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLocation_Equations(), theCifPackage.getEquation(), null, "equations", null, 0, -1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(edgeEClass, Edge.class, "Edge", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEdge_Target(), this.getLocation(), null, "target", null, 0, 1, Edge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEdge_Events(), this.getEdgeEvent(), null, "events", null, 0, -1, Edge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEdge_Guards(), theExpressionsPackage.getExpression(), null, "guards", null, 0, -1, Edge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEdge_Updates(), this.getUpdate(), null, "updates", null, 0, -1, Edge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEdge_Urgent(), ecorePackage.getEBoolean(), "urgent", null, 1, 1, Edge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(updateEClass, Update.class, "Update", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(assignmentEClass, Assignment.class, "Assignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssignment_Addressable(), theExpressionsPackage.getExpression(), null, "addressable", null, 1, 1, Assignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAssignment_Value(), theExpressionsPackage.getExpression(), null, "value", null, 1, 1, Assignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ifUpdateEClass, IfUpdate.class, "IfUpdate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIfUpdate_Guards(), theExpressionsPackage.getExpression(), null, "guards", null, 1, -1, IfUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfUpdate_Thens(), this.getUpdate(), null, "thens", null, 1, -1, IfUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfUpdate_Elifs(), this.getElifUpdate(), null, "elifs", null, 0, -1, IfUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIfUpdate_Elses(), this.getUpdate(), null, "elses", null, 0, -1, IfUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(elifUpdateEClass, ElifUpdate.class, "ElifUpdate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getElifUpdate_Guards(), theExpressionsPackage.getExpression(), null, "guards", null, 1, -1, ElifUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getElifUpdate_Thens(), this.getUpdate(), null, "thens", null, 1, -1, ElifUpdate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(alphabetEClass, Alphabet.class, "Alphabet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAlphabet_Events(), theExpressionsPackage.getExpression(), null, "events", null, 0, -1, Alphabet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(edgeEventEClass, EdgeEvent.class, "EdgeEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEdgeEvent_Event(), theExpressionsPackage.getExpression(), null, "event", null, 1, 1, EdgeEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(monitorsEClass, Monitors.class, "Monitors", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMonitors_Events(), theExpressionsPackage.getExpression(), null, "events", null, 0, -1, Monitors.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(edgeSendEClass, EdgeSend.class, "EdgeSend", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEdgeSend_Value(), theExpressionsPackage.getExpression(), null, "value", null, 0, 1, EdgeSend.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(edgeReceiveEClass, EdgeReceive.class, "EdgeReceive", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    }

} //AutomataPackageImpl
