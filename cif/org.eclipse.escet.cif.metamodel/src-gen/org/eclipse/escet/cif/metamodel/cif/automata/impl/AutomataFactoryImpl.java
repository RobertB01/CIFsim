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
package org.eclipse.escet.cif.metamodel.cif.automata.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.cif.metamodel.cif.automata.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AutomataFactoryImpl extends EFactoryImpl implements AutomataFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static AutomataFactory init()
    {
        try
        {
            AutomataFactory theAutomataFactory = (AutomataFactory)EPackage.Registry.INSTANCE.getEFactory(AutomataPackage.eNS_URI);
            if (theAutomataFactory != null)
            {
                return theAutomataFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new AutomataFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AutomataFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case AutomataPackage.AUTOMATON: return createAutomaton();
            case AutomataPackage.LOCATION: return createLocation();
            case AutomataPackage.EDGE: return createEdge();
            case AutomataPackage.ASSIGNMENT: return createAssignment();
            case AutomataPackage.IF_UPDATE: return createIfUpdate();
            case AutomataPackage.ELIF_UPDATE: return createElifUpdate();
            case AutomataPackage.ALPHABET: return createAlphabet();
            case AutomataPackage.EDGE_EVENT: return createEdgeEvent();
            case AutomataPackage.MONITORS: return createMonitors();
            case AutomataPackage.EDGE_SEND: return createEdgeSend();
            case AutomataPackage.EDGE_RECEIVE: return createEdgeReceive();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Automaton createAutomaton()
    {
        AutomatonImpl automaton = new AutomatonImpl();
        return automaton;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Location createLocation()
    {
        LocationImpl location = new LocationImpl();
        return location;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Edge createEdge()
    {
        EdgeImpl edge = new EdgeImpl();
        return edge;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Assignment createAssignment()
    {
        AssignmentImpl assignment = new AssignmentImpl();
        return assignment;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IfUpdate createIfUpdate()
    {
        IfUpdateImpl ifUpdate = new IfUpdateImpl();
        return ifUpdate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ElifUpdate createElifUpdate()
    {
        ElifUpdateImpl elifUpdate = new ElifUpdateImpl();
        return elifUpdate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Alphabet createAlphabet()
    {
        AlphabetImpl alphabet = new AlphabetImpl();
        return alphabet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EdgeEvent createEdgeEvent()
    {
        EdgeEventImpl edgeEvent = new EdgeEventImpl();
        return edgeEvent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Monitors createMonitors()
    {
        MonitorsImpl monitors = new MonitorsImpl();
        return monitors;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EdgeSend createEdgeSend()
    {
        EdgeSendImpl edgeSend = new EdgeSendImpl();
        return edgeSend;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EdgeReceive createEdgeReceive()
    {
        EdgeReceiveImpl edgeReceive = new EdgeReceiveImpl();
        return edgeReceive;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AutomataPackage getAutomataPackage()
    {
        return (AutomataPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static AutomataPackage getPackage()
    {
        return AutomataPackage.eINSTANCE;
    }

} //AutomataFactoryImpl
