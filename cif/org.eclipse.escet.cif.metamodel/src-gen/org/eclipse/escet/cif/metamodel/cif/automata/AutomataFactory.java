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
package org.eclipse.escet.cif.metamodel.cif.automata;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage
 * @generated
 */
public interface AutomataFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    AutomataFactory eINSTANCE = org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Automaton</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Automaton</em>'.
     * @generated
     */
    Automaton createAutomaton();

    /**
     * Returns a new object of class '<em>Location</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Location</em>'.
     * @generated
     */
    Location createLocation();

    /**
     * Returns a new object of class '<em>Edge</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Edge</em>'.
     * @generated
     */
    Edge createEdge();

    /**
     * Returns a new object of class '<em>Assignment</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Assignment</em>'.
     * @generated
     */
    Assignment createAssignment();

    /**
     * Returns a new object of class '<em>If Update</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>If Update</em>'.
     * @generated
     */
    IfUpdate createIfUpdate();

    /**
     * Returns a new object of class '<em>Elif Update</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Elif Update</em>'.
     * @generated
     */
    ElifUpdate createElifUpdate();

    /**
     * Returns a new object of class '<em>Alphabet</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Alphabet</em>'.
     * @generated
     */
    Alphabet createAlphabet();

    /**
     * Returns a new object of class '<em>Edge Event</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Edge Event</em>'.
     * @generated
     */
    EdgeEvent createEdgeEvent();

    /**
     * Returns a new object of class '<em>Monitors</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Monitors</em>'.
     * @generated
     */
    Monitors createMonitors();

    /**
     * Returns a new object of class '<em>Edge Send</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Edge Send</em>'.
     * @generated
     */
    EdgeSend createEdgeSend();

    /**
     * Returns a new object of class '<em>Edge Receive</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Edge Receive</em>'.
     * @generated
     */
    EdgeReceive createEdgeReceive();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    AutomataPackage getAutomataPackage();

} //AutomataFactory
