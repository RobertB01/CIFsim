/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Automaton</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getLocations <em>Locations</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getAlphabet <em>Alphabet</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getMonitors <em>Monitors</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getKind <em>Kind</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getAutomaton()
 * @model
 * @generated
 */
public interface Automaton extends ComplexComponent
{
    /**
     * Returns the value of the '<em><b>Locations</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.automata.Location}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Locations</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getAutomaton_Locations()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Location> getLocations();

    /**
     * Returns the value of the '<em><b>Alphabet</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Alphabet</em>' containment reference.
     * @see #setAlphabet(Alphabet)
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getAutomaton_Alphabet()
     * @model containment="true"
     * @generated
     */
    Alphabet getAlphabet();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getAlphabet <em>Alphabet</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Alphabet</em>' containment reference.
     * @see #getAlphabet()
     * @generated
     */
    void setAlphabet(Alphabet value);

    /**
     * Returns the value of the '<em><b>Monitors</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Monitors</em>' containment reference.
     * @see #setMonitors(Monitors)
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getAutomaton_Monitors()
     * @model containment="true"
     * @generated
     */
    Monitors getMonitors();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getMonitors <em>Monitors</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Monitors</em>' containment reference.
     * @see #getMonitors()
     * @generated
     */
    void setMonitors(Monitors value);

    /**
     * Returns the value of the '<em><b>Kind</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.cif.metamodel.cif.SupKind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.SupKind
     * @see #setKind(SupKind)
     * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage#getAutomaton_Kind()
     * @model required="true"
     * @generated
     */
    SupKind getKind();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getKind <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.SupKind
     * @see #getKind()
     * @generated
     */
    void setKind(SupKind value);

} // Automaton
