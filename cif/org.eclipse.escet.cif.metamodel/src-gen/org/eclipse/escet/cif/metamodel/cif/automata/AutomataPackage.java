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
package org.eclipse.escet.cif.metamodel.cif.automata;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationsPackage;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.automata.AutomataFactory
 * @model kind="package"
 * @generated
 */
public interface AutomataPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "automata";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/cif/automata";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "automata";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    AutomataPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomatonImpl <em>Automaton</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomatonImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getAutomaton()
     * @generated
     */
    int AUTOMATON = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__POSITION = CifPackage.COMPLEX_COMPONENT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__NAME = CifPackage.COMPLEX_COMPONENT__NAME;

    /**
     * The feature id for the '<em><b>Declarations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__DECLARATIONS = CifPackage.COMPLEX_COMPONENT__DECLARATIONS;

    /**
     * The feature id for the '<em><b>Markeds</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__MARKEDS = CifPackage.COMPLEX_COMPONENT__MARKEDS;

    /**
     * The feature id for the '<em><b>Invariants</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__INVARIANTS = CifPackage.COMPLEX_COMPONENT__INVARIANTS;

    /**
     * The feature id for the '<em><b>Initials</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__INITIALS = CifPackage.COMPLEX_COMPONENT__INITIALS;

    /**
     * The feature id for the '<em><b>Equations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__EQUATIONS = CifPackage.COMPLEX_COMPONENT__EQUATIONS;

    /**
     * The feature id for the '<em><b>Io Decls</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__IO_DECLS = CifPackage.COMPLEX_COMPONENT__IO_DECLS;

    /**
     * The feature id for the '<em><b>Locations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__LOCATIONS = CifPackage.COMPLEX_COMPONENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Alphabet</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__ALPHABET = CifPackage.COMPLEX_COMPONENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Monitors</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__MONITORS = CifPackage.COMPLEX_COMPONENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Kind</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON__KIND = CifPackage.COMPLEX_COMPONENT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Automaton</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON_FEATURE_COUNT = CifPackage.COMPLEX_COMPONENT_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>Automaton</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AUTOMATON_OPERATION_COUNT = CifPackage.COMPLEX_COMPONENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl <em>Location</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getLocation()
     * @generated
     */
    int LOCATION = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__POSITION = AnnotationsPackage.ANNOTATED_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__ANNOTATIONS = AnnotationsPackage.ANNOTATED_OBJECT__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__NAME = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Initials</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__INITIALS = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Invariants</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__INVARIANTS = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Edges</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__EDGES = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Markeds</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__MARKEDS = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Urgent</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__URGENT = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Equations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION__EQUATIONS = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Location</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_FEATURE_COUNT = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 7;

    /**
     * The number of operations of the '<em>Location</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_OPERATION_COUNT = AnnotationsPackage.ANNOTATED_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl <em>Edge</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getEdge()
     * @generated
     */
    int EDGE = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE__TARGET = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Events</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE__EVENTS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Guards</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE__GUARDS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Updates</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE__UPDATES = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Urgent</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE__URGENT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Edge</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 5;

    /**
     * The number of operations of the '<em>Edge</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.UpdateImpl <em>Update</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.UpdateImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getUpdate()
     * @generated
     */
    int UPDATE = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPDATE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Update</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPDATE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Update</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPDATE_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AssignmentImpl <em>Assignment</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AssignmentImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getAssignment()
     * @generated
     */
    int ASSIGNMENT = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT__POSITION = UPDATE__POSITION;

    /**
     * The feature id for the '<em><b>Addressable</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT__ADDRESSABLE = UPDATE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT__VALUE = UPDATE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Assignment</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_FEATURE_COUNT = UPDATE_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Assignment</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_OPERATION_COUNT = UPDATE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.IfUpdateImpl <em>If Update</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.IfUpdateImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getIfUpdate()
     * @generated
     */
    int IF_UPDATE = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_UPDATE__POSITION = UPDATE__POSITION;

    /**
     * The feature id for the '<em><b>Guards</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_UPDATE__GUARDS = UPDATE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Thens</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_UPDATE__THENS = UPDATE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Elifs</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_UPDATE__ELIFS = UPDATE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Elses</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_UPDATE__ELSES = UPDATE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>If Update</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_UPDATE_FEATURE_COUNT = UPDATE_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>If Update</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_UPDATE_OPERATION_COUNT = UPDATE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.ElifUpdateImpl <em>Elif Update</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.ElifUpdateImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getElifUpdate()
     * @generated
     */
    int ELIF_UPDATE = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_UPDATE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Guards</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_UPDATE__GUARDS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Thens</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_UPDATE__THENS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Elif Update</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_UPDATE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Elif Update</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELIF_UPDATE_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AlphabetImpl <em>Alphabet</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AlphabetImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getAlphabet()
     * @generated
     */
    int ALPHABET = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALPHABET__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Events</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALPHABET__EVENTS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Alphabet</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALPHABET_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Alphabet</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALPHABET_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl <em>Edge Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getEdgeEvent()
     * @generated
     */
    int EDGE_EVENT = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_EVENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_EVENT__EVENT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Edge Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_EVENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Edge Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_EVENT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.MonitorsImpl <em>Monitors</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.MonitorsImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getMonitors()
     * @generated
     */
    int MONITORS = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONITORS__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Events</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONITORS__EVENTS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Monitors</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONITORS_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Monitors</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MONITORS_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeSendImpl <em>Edge Send</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeSendImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getEdgeSend()
     * @generated
     */
    int EDGE_SEND = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_SEND__POSITION = EDGE_EVENT__POSITION;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_SEND__EVENT = EDGE_EVENT__EVENT;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_SEND__VALUE = EDGE_EVENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Edge Send</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_SEND_FEATURE_COUNT = EDGE_EVENT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Edge Send</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_SEND_OPERATION_COUNT = EDGE_EVENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeReceiveImpl <em>Edge Receive</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeReceiveImpl
     * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getEdgeReceive()
     * @generated
     */
    int EDGE_RECEIVE = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_RECEIVE__POSITION = EDGE_EVENT__POSITION;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_RECEIVE__EVENT = EDGE_EVENT__EVENT;

    /**
     * The number of structural features of the '<em>Edge Receive</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_RECEIVE_FEATURE_COUNT = EDGE_EVENT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Edge Receive</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EDGE_RECEIVE_OPERATION_COUNT = EDGE_EVENT_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton <em>Automaton</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Automaton</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Automaton
     * @generated
     */
    EClass getAutomaton();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getLocations <em>Locations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Locations</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getLocations()
     * @see #getAutomaton()
     * @generated
     */
    EReference getAutomaton_Locations();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getAlphabet <em>Alphabet</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Alphabet</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getAlphabet()
     * @see #getAutomaton()
     * @generated
     */
    EReference getAutomaton_Alphabet();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getMonitors <em>Monitors</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Monitors</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getMonitors()
     * @see #getAutomaton()
     * @generated
     */
    EReference getAutomaton_Monitors();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getKind <em>Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Kind</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Automaton#getKind()
     * @see #getAutomaton()
     * @generated
     */
    EAttribute getAutomaton_Kind();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location <em>Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Location</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Location
     * @generated
     */
    EClass getLocation();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Location#getName()
     * @see #getLocation()
     * @generated
     */
    EAttribute getLocation_Name();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getInitials <em>Initials</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Initials</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Location#getInitials()
     * @see #getLocation()
     * @generated
     */
    EReference getLocation_Initials();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getInvariants <em>Invariants</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Invariants</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Location#getInvariants()
     * @see #getLocation()
     * @generated
     */
    EReference getLocation_Invariants();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getEdges <em>Edges</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Edges</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Location#getEdges()
     * @see #getLocation()
     * @generated
     */
    EReference getLocation_Edges();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getMarkeds <em>Markeds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Markeds</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Location#getMarkeds()
     * @see #getLocation()
     * @generated
     */
    EReference getLocation_Markeds();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#isUrgent <em>Urgent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Urgent</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Location#isUrgent()
     * @see #getLocation()
     * @generated
     */
    EAttribute getLocation_Urgent();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Location#getEquations <em>Equations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Equations</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Location#getEquations()
     * @see #getLocation()
     * @generated
     */
    EReference getLocation_Equations();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge <em>Edge</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Edge</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Edge
     * @generated
     */
    EClass getEdge();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getTarget <em>Target</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Edge#getTarget()
     * @see #getEdge()
     * @generated
     */
    EReference getEdge_Target();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getEvents <em>Events</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Events</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Edge#getEvents()
     * @see #getEdge()
     * @generated
     */
    EReference getEdge_Events();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getGuards <em>Guards</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Guards</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Edge#getGuards()
     * @see #getEdge()
     * @generated
     */
    EReference getEdge_Guards();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#getUpdates <em>Updates</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Updates</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Edge#getUpdates()
     * @see #getEdge()
     * @generated
     */
    EReference getEdge_Updates();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.automata.Edge#isUrgent <em>Urgent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Urgent</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Edge#isUrgent()
     * @see #getEdge()
     * @generated
     */
    EAttribute getEdge_Urgent();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.Update <em>Update</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Update</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Update
     * @generated
     */
    EClass getUpdate();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.Assignment <em>Assignment</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assignment</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Assignment
     * @generated
     */
    EClass getAssignment();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.automata.Assignment#getAddressable <em>Addressable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Addressable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Assignment#getAddressable()
     * @see #getAssignment()
     * @generated
     */
    EReference getAssignment_Addressable();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.automata.Assignment#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Assignment#getValue()
     * @see #getAssignment()
     * @generated
     */
    EReference getAssignment_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate <em>If Update</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>If Update</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate
     * @generated
     */
    EClass getIfUpdate();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate#getGuards <em>Guards</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Guards</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate#getGuards()
     * @see #getIfUpdate()
     * @generated
     */
    EReference getIfUpdate_Guards();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate#getThens <em>Thens</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Thens</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate#getThens()
     * @see #getIfUpdate()
     * @generated
     */
    EReference getIfUpdate_Thens();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate#getElifs <em>Elifs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elifs</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate#getElifs()
     * @see #getIfUpdate()
     * @generated
     */
    EReference getIfUpdate_Elifs();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate#getElses <em>Elses</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elses</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate#getElses()
     * @see #getIfUpdate()
     * @generated
     */
    EReference getIfUpdate_Elses();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate <em>Elif Update</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Elif Update</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate
     * @generated
     */
    EClass getElifUpdate();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate#getGuards <em>Guards</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Guards</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate#getGuards()
     * @see #getElifUpdate()
     * @generated
     */
    EReference getElifUpdate_Guards();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate#getThens <em>Thens</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Thens</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate#getThens()
     * @see #getElifUpdate()
     * @generated
     */
    EReference getElifUpdate_Thens();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.Alphabet <em>Alphabet</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Alphabet</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Alphabet
     * @generated
     */
    EClass getAlphabet();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Alphabet#getEvents <em>Events</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Events</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Alphabet#getEvents()
     * @see #getAlphabet()
     * @generated
     */
    EReference getAlphabet_Events();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent <em>Edge Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Edge Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent
     * @generated
     */
    EClass getEdgeEvent();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent#getEvent <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent#getEvent()
     * @see #getEdgeEvent()
     * @generated
     */
    EReference getEdgeEvent_Event();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.Monitors <em>Monitors</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Monitors</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Monitors
     * @generated
     */
    EClass getMonitors();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.automata.Monitors#getEvents <em>Events</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Events</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.Monitors#getEvents()
     * @see #getMonitors()
     * @generated
     */
    EReference getMonitors_Events();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend <em>Edge Send</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Edge Send</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend
     * @generated
     */
    EClass getEdgeSend();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend#getValue()
     * @see #getEdgeSend()
     * @generated
     */
    EReference getEdgeSend_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive <em>Edge Receive</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Edge Receive</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive
     * @generated
     */
    EClass getEdgeReceive();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    AutomataFactory getAutomataFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals
    {
        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomatonImpl <em>Automaton</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomatonImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getAutomaton()
         * @generated
         */
        EClass AUTOMATON = eINSTANCE.getAutomaton();

        /**
         * The meta object literal for the '<em><b>Locations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AUTOMATON__LOCATIONS = eINSTANCE.getAutomaton_Locations();

        /**
         * The meta object literal for the '<em><b>Alphabet</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AUTOMATON__ALPHABET = eINSTANCE.getAutomaton_Alphabet();

        /**
         * The meta object literal for the '<em><b>Monitors</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AUTOMATON__MONITORS = eINSTANCE.getAutomaton_Monitors();

        /**
         * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AUTOMATON__KIND = eINSTANCE.getAutomaton_Kind();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl <em>Location</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getLocation()
         * @generated
         */
        EClass LOCATION = eINSTANCE.getLocation();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LOCATION__NAME = eINSTANCE.getLocation_Name();

        /**
         * The meta object literal for the '<em><b>Initials</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LOCATION__INITIALS = eINSTANCE.getLocation_Initials();

        /**
         * The meta object literal for the '<em><b>Invariants</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LOCATION__INVARIANTS = eINSTANCE.getLocation_Invariants();

        /**
         * The meta object literal for the '<em><b>Edges</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LOCATION__EDGES = eINSTANCE.getLocation_Edges();

        /**
         * The meta object literal for the '<em><b>Markeds</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LOCATION__MARKEDS = eINSTANCE.getLocation_Markeds();

        /**
         * The meta object literal for the '<em><b>Urgent</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LOCATION__URGENT = eINSTANCE.getLocation_Urgent();

        /**
         * The meta object literal for the '<em><b>Equations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LOCATION__EQUATIONS = eINSTANCE.getLocation_Equations();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl <em>Edge</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getEdge()
         * @generated
         */
        EClass EDGE = eINSTANCE.getEdge();

        /**
         * The meta object literal for the '<em><b>Target</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EDGE__TARGET = eINSTANCE.getEdge_Target();

        /**
         * The meta object literal for the '<em><b>Events</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EDGE__EVENTS = eINSTANCE.getEdge_Events();

        /**
         * The meta object literal for the '<em><b>Guards</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EDGE__GUARDS = eINSTANCE.getEdge_Guards();

        /**
         * The meta object literal for the '<em><b>Updates</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EDGE__UPDATES = eINSTANCE.getEdge_Updates();

        /**
         * The meta object literal for the '<em><b>Urgent</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EDGE__URGENT = eINSTANCE.getEdge_Urgent();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.UpdateImpl <em>Update</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.UpdateImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getUpdate()
         * @generated
         */
        EClass UPDATE = eINSTANCE.getUpdate();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AssignmentImpl <em>Assignment</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AssignmentImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getAssignment()
         * @generated
         */
        EClass ASSIGNMENT = eINSTANCE.getAssignment();

        /**
         * The meta object literal for the '<em><b>Addressable</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSIGNMENT__ADDRESSABLE = eINSTANCE.getAssignment_Addressable();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSIGNMENT__VALUE = eINSTANCE.getAssignment_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.IfUpdateImpl <em>If Update</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.IfUpdateImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getIfUpdate()
         * @generated
         */
        EClass IF_UPDATE = eINSTANCE.getIfUpdate();

        /**
         * The meta object literal for the '<em><b>Guards</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_UPDATE__GUARDS = eINSTANCE.getIfUpdate_Guards();

        /**
         * The meta object literal for the '<em><b>Thens</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_UPDATE__THENS = eINSTANCE.getIfUpdate_Thens();

        /**
         * The meta object literal for the '<em><b>Elifs</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_UPDATE__ELIFS = eINSTANCE.getIfUpdate_Elifs();

        /**
         * The meta object literal for the '<em><b>Elses</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_UPDATE__ELSES = eINSTANCE.getIfUpdate_Elses();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.ElifUpdateImpl <em>Elif Update</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.ElifUpdateImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getElifUpdate()
         * @generated
         */
        EClass ELIF_UPDATE = eINSTANCE.getElifUpdate();

        /**
         * The meta object literal for the '<em><b>Guards</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ELIF_UPDATE__GUARDS = eINSTANCE.getElifUpdate_Guards();

        /**
         * The meta object literal for the '<em><b>Thens</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ELIF_UPDATE__THENS = eINSTANCE.getElifUpdate_Thens();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AlphabetImpl <em>Alphabet</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AlphabetImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getAlphabet()
         * @generated
         */
        EClass ALPHABET = eINSTANCE.getAlphabet();

        /**
         * The meta object literal for the '<em><b>Events</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALPHABET__EVENTS = eINSTANCE.getAlphabet_Events();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl <em>Edge Event</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getEdgeEvent()
         * @generated
         */
        EClass EDGE_EVENT = eINSTANCE.getEdgeEvent();

        /**
         * The meta object literal for the '<em><b>Event</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EDGE_EVENT__EVENT = eINSTANCE.getEdgeEvent_Event();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.MonitorsImpl <em>Monitors</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.MonitorsImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getMonitors()
         * @generated
         */
        EClass MONITORS = eINSTANCE.getMonitors();

        /**
         * The meta object literal for the '<em><b>Events</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MONITORS__EVENTS = eINSTANCE.getMonitors_Events();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeSendImpl <em>Edge Send</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeSendImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getEdgeSend()
         * @generated
         */
        EClass EDGE_SEND = eINSTANCE.getEdgeSend();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EDGE_SEND__VALUE = eINSTANCE.getEdgeSend_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeReceiveImpl <em>Edge Receive</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeReceiveImpl
         * @see org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl#getEdgeReceive()
         * @generated
         */
        EClass EDGE_RECEIVE = eINSTANCE.getEdgeReceive();

    }

} //AutomataPackage
