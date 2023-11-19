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
package org.eclipse.escet.cif.metamodel.cif.automata.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;

import org.eclipse.escet.cif.metamodel.cif.automata.*;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage
 * @generated
 */
public class AutomataSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static AutomataPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AutomataSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = AutomataPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage)
    {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject)
    {
        switch (classifierID)
        {
            case AutomataPackage.AUTOMATON:
            {
                Automaton automaton = (Automaton)theEObject;
                T result = caseAutomaton(automaton);
                if (result == null) result = caseComplexComponent(automaton);
                if (result == null) result = caseComponent(automaton);
                if (result == null) result = casePositionObject(automaton);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.LOCATION:
            {
                Location location = (Location)theEObject;
                T result = caseLocation(location);
                if (result == null) result = caseAnnotatedObject(location);
                if (result == null) result = casePositionObject(location);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.EDGE:
            {
                Edge edge = (Edge)theEObject;
                T result = caseEdge(edge);
                if (result == null) result = casePositionObject(edge);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.UPDATE:
            {
                Update update = (Update)theEObject;
                T result = caseUpdate(update);
                if (result == null) result = casePositionObject(update);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.ASSIGNMENT:
            {
                Assignment assignment = (Assignment)theEObject;
                T result = caseAssignment(assignment);
                if (result == null) result = caseUpdate(assignment);
                if (result == null) result = casePositionObject(assignment);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.IF_UPDATE:
            {
                IfUpdate ifUpdate = (IfUpdate)theEObject;
                T result = caseIfUpdate(ifUpdate);
                if (result == null) result = caseUpdate(ifUpdate);
                if (result == null) result = casePositionObject(ifUpdate);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.ELIF_UPDATE:
            {
                ElifUpdate elifUpdate = (ElifUpdate)theEObject;
                T result = caseElifUpdate(elifUpdate);
                if (result == null) result = casePositionObject(elifUpdate);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.ALPHABET:
            {
                Alphabet alphabet = (Alphabet)theEObject;
                T result = caseAlphabet(alphabet);
                if (result == null) result = casePositionObject(alphabet);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.EDGE_EVENT:
            {
                EdgeEvent edgeEvent = (EdgeEvent)theEObject;
                T result = caseEdgeEvent(edgeEvent);
                if (result == null) result = casePositionObject(edgeEvent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.MONITORS:
            {
                Monitors monitors = (Monitors)theEObject;
                T result = caseMonitors(monitors);
                if (result == null) result = casePositionObject(monitors);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.EDGE_SEND:
            {
                EdgeSend edgeSend = (EdgeSend)theEObject;
                T result = caseEdgeSend(edgeSend);
                if (result == null) result = caseEdgeEvent(edgeSend);
                if (result == null) result = casePositionObject(edgeSend);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case AutomataPackage.EDGE_RECEIVE:
            {
                EdgeReceive edgeReceive = (EdgeReceive)theEObject;
                T result = caseEdgeReceive(edgeReceive);
                if (result == null) result = caseEdgeEvent(edgeReceive);
                if (result == null) result = casePositionObject(edgeReceive);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Automaton</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Automaton</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAutomaton(Automaton object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Location</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Location</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLocation(Location object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Edge</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Edge</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEdge(Edge object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Update</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Update</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUpdate(Update object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assignment</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assignment</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssignment(Assignment object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Update</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Update</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfUpdate(IfUpdate object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Elif Update</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Elif Update</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElifUpdate(ElifUpdate object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Alphabet</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Alphabet</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAlphabet(Alphabet object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Edge Event</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Edge Event</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEdgeEvent(EdgeEvent object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Monitors</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Monitors</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMonitors(Monitors object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Edge Send</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Edge Send</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEdgeSend(EdgeSend object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Edge Receive</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Edge Receive</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEdgeReceive(EdgeReceive object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePositionObject(PositionObject object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponent(Component object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Complex Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Complex Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComplexComponent(ComplexComponent object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Annotated Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Annotated Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnnotatedObject(AnnotatedObject object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object)
    {
        return null;
    }

} //AutomataSwitch
