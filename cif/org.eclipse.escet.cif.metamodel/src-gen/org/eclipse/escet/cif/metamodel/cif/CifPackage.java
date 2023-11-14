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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.eclipse.escet.cif.metamodel.cif.CifFactory
 * @model kind="package"
 * @generated
 */
public interface CifPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "cif";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/cif";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "cif";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    CifPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComponentImpl <em>Component</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.ComponentImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComponent()
     * @generated
     */
    int COMPONENT = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl <em>Complex Component</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComplexComponent()
     * @generated
     */
    int COMPLEX_COMPONENT = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT__POSITION = COMPONENT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT__NAME = COMPONENT__NAME;

    /**
     * The feature id for the '<em><b>Declarations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT__DECLARATIONS = COMPONENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Markeds</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT__MARKEDS = COMPONENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Invariants</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT__INVARIANTS = COMPONENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Initials</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT__INITIALS = COMPONENT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Equations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT__EQUATIONS = COMPONENT_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Io Decls</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT__IO_DECLS = COMPONENT_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Complex Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT_FEATURE_COUNT = COMPONENT_FEATURE_COUNT + 6;

    /**
     * The number of operations of the '<em>Complex Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_COMPONENT_OPERATION_COUNT = COMPONENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.GroupImpl <em>Group</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.GroupImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getGroup()
     * @generated
     */
    int GROUP = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__POSITION = COMPLEX_COMPONENT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__NAME = COMPLEX_COMPONENT__NAME;

    /**
     * The feature id for the '<em><b>Declarations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__DECLARATIONS = COMPLEX_COMPONENT__DECLARATIONS;

    /**
     * The feature id for the '<em><b>Markeds</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__MARKEDS = COMPLEX_COMPONENT__MARKEDS;

    /**
     * The feature id for the '<em><b>Invariants</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__INVARIANTS = COMPLEX_COMPONENT__INVARIANTS;

    /**
     * The feature id for the '<em><b>Initials</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__INITIALS = COMPLEX_COMPONENT__INITIALS;

    /**
     * The feature id for the '<em><b>Equations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__EQUATIONS = COMPLEX_COMPONENT__EQUATIONS;

    /**
     * The feature id for the '<em><b>Io Decls</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__IO_DECLS = COMPLEX_COMPONENT__IO_DECLS;

    /**
     * The feature id for the '<em><b>Definitions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__DEFINITIONS = COMPLEX_COMPONENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Components</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP__COMPONENTS = COMPLEX_COMPONENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Group</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP_FEATURE_COUNT = COMPLEX_COMPONENT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Group</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GROUP_OPERATION_COUNT = COMPLEX_COMPONENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComponentDefImpl <em>Component Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.ComponentDefImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComponentDef()
     * @generated
     */
    int COMPONENT_DEF = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Body</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF__BODY = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF__PARAMETERS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Component Def</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Component Def</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComponentInstImpl <em>Component Inst</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.ComponentInstImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComponentInst()
     * @generated
     */
    int COMPONENT_INST = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INST__POSITION = COMPONENT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INST__NAME = COMPONENT__NAME;

    /**
     * The feature id for the '<em><b>Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INST__DEFINITION = COMPONENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INST__ARGUMENTS = COMPONENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Component Inst</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INST_FEATURE_COUNT = COMPONENT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Component Inst</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_INST_OPERATION_COUNT = COMPONENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.SpecificationImpl <em>Specification</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.SpecificationImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getSpecification()
     * @generated
     */
    int SPECIFICATION = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__POSITION = GROUP__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__NAME = GROUP__NAME;

    /**
     * The feature id for the '<em><b>Declarations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__DECLARATIONS = GROUP__DECLARATIONS;

    /**
     * The feature id for the '<em><b>Markeds</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__MARKEDS = GROUP__MARKEDS;

    /**
     * The feature id for the '<em><b>Invariants</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__INVARIANTS = GROUP__INVARIANTS;

    /**
     * The feature id for the '<em><b>Initials</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__INITIALS = GROUP__INITIALS;

    /**
     * The feature id for the '<em><b>Equations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__EQUATIONS = GROUP__EQUATIONS;

    /**
     * The feature id for the '<em><b>Io Decls</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__IO_DECLS = GROUP__IO_DECLS;

    /**
     * The feature id for the '<em><b>Definitions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__DEFINITIONS = GROUP__DEFINITIONS;

    /**
     * The feature id for the '<em><b>Components</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__COMPONENTS = GROUP__COMPONENTS;

    /**
     * The number of structural features of the '<em>Specification</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION_FEATURE_COUNT = GROUP_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Specification</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION_OPERATION_COUNT = GROUP_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ParameterImpl <em>Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.ParameterImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getParameter()
     * @generated
     */
    int PARAMETER = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.EventParameterImpl <em>Event Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.EventParameterImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getEventParameter()
     * @generated
     */
    int EVENT_PARAMETER = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_PARAMETER__POSITION = PARAMETER__POSITION;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_PARAMETER__EVENT = PARAMETER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Send Flag</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_PARAMETER__SEND_FLAG = PARAMETER_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Recv Flag</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_PARAMETER__RECV_FLAG = PARAMETER_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Sync Flag</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_PARAMETER__SYNC_FLAG = PARAMETER_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Event Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_PARAMETER_FEATURE_COUNT = PARAMETER_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>Event Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_PARAMETER_OPERATION_COUNT = PARAMETER_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.LocationParameterImpl <em>Location Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.LocationParameterImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getLocationParameter()
     * @generated
     */
    int LOCATION_PARAMETER = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_PARAMETER__POSITION = PARAMETER__POSITION;

    /**
     * The feature id for the '<em><b>Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_PARAMETER__LOCATION = PARAMETER_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Location Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_PARAMETER_FEATURE_COUNT = PARAMETER_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Location Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOCATION_PARAMETER_OPERATION_COUNT = PARAMETER_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.AlgParameterImpl <em>Alg Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.AlgParameterImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getAlgParameter()
     * @generated
     */
    int ALG_PARAMETER = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_PARAMETER__POSITION = PARAMETER__POSITION;

    /**
     * The feature id for the '<em><b>Variable</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_PARAMETER__VARIABLE = PARAMETER_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Alg Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_PARAMETER_FEATURE_COUNT = PARAMETER_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Alg Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_PARAMETER_OPERATION_COUNT = PARAMETER_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComponentParameterImpl <em>Component Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.ComponentParameterImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComponentParameter()
     * @generated
     */
    int COMPONENT_PARAMETER = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_PARAMETER__POSITION = PARAMETER__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_PARAMETER__TYPE = PARAMETER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_PARAMETER__NAME = PARAMETER_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Component Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_PARAMETER_FEATURE_COUNT = PARAMETER_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Component Parameter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_PARAMETER_OPERATION_COUNT = PARAMETER_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.EquationImpl <em>Equation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.EquationImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getEquation()
     * @generated
     */
    int EQUATION = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUATION__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Derivative</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUATION__DERIVATIVE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUATION__VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUATION__VARIABLE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Equation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUATION_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Equation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EQUATION_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl <em>Io Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getIoDecl()
     * @generated
     */
    int IO_DECL = 12;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IO_DECL__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Io Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IO_DECL_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Io Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IO_DECL_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.InvariantImpl <em>Invariant</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.impl.InvariantImpl
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getInvariant()
     * @generated
     */
    int INVARIANT = 13;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVARIANT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVARIANT__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Sup Kind</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVARIANT__SUP_KIND = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Predicate</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVARIANT__PREDICATE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Inv Kind</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVARIANT__INV_KIND = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVARIANT__EVENT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Invariant</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVARIANT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 5;

    /**
     * The number of operations of the '<em>Invariant</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INVARIANT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.SupKind <em>Sup Kind</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.SupKind
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getSupKind()
     * @generated
     */
    int SUP_KIND = 14;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.InvKind <em>Inv Kind</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.InvKind
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getInvKind()
     * @generated
     */
    int INV_KIND = 15;

    /**
     * The meta object id for the '<em>Identifier</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getCifIdentifier()
     * @generated
     */
    int CIF_IDENTIFIER = 16;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.Component <em>Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Component
     * @generated
     */
    EClass getComponent();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.Component#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Component#getName()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.Group <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Group</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Group
     * @generated
     */
    EClass getGroup();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.Group#getDefinitions <em>Definitions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Definitions</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Group#getDefinitions()
     * @see #getGroup()
     * @generated
     */
    EReference getGroup_Definitions();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.Group#getComponents <em>Components</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Components</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Group#getComponents()
     * @see #getGroup()
     * @generated
     */
    EReference getGroup_Components();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.ComponentDef <em>Component Def</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Def</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentDef
     * @generated
     */
    EClass getComponentDef();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.ComponentDef#getBody <em>Body</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Body</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentDef#getBody()
     * @see #getComponentDef()
     * @generated
     */
    EReference getComponentDef_Body();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.ComponentDef#getParameters <em>Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameters</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentDef#getParameters()
     * @see #getComponentDef()
     * @generated
     */
    EReference getComponentDef_Parameters();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.ComponentInst <em>Component Inst</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Inst</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentInst
     * @generated
     */
    EClass getComponentInst();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.ComponentInst#getDefinition <em>Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Definition</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentInst#getDefinition()
     * @see #getComponentInst()
     * @generated
     */
    EReference getComponentInst_Definition();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.ComponentInst#getArguments <em>Arguments</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Arguments</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentInst#getArguments()
     * @see #getComponentInst()
     * @generated
     */
    EReference getComponentInst_Arguments();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent <em>Complex Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Complex Component</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComplexComponent
     * @generated
     */
    EClass getComplexComponent();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getDeclarations <em>Declarations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Declarations</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getDeclarations()
     * @see #getComplexComponent()
     * @generated
     */
    EReference getComplexComponent_Declarations();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getMarkeds <em>Markeds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Markeds</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getMarkeds()
     * @see #getComplexComponent()
     * @generated
     */
    EReference getComplexComponent_Markeds();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getInvariants <em>Invariants</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Invariants</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getInvariants()
     * @see #getComplexComponent()
     * @generated
     */
    EReference getComplexComponent_Invariants();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getInitials <em>Initials</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Initials</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getInitials()
     * @see #getComplexComponent()
     * @generated
     */
    EReference getComplexComponent_Initials();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getEquations <em>Equations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Equations</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getEquations()
     * @see #getComplexComponent()
     * @generated
     */
    EReference getComplexComponent_Equations();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getIoDecls <em>Io Decls</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Io Decls</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getIoDecls()
     * @see #getComplexComponent()
     * @generated
     */
    EReference getComplexComponent_IoDecls();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.Specification <em>Specification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Specification</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Specification
     * @generated
     */
    EClass getSpecification();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.Parameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Parameter
     * @generated
     */
    EClass getParameter();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter <em>Event Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Event Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.EventParameter
     * @generated
     */
    EClass getEventParameter();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#getEvent <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.EventParameter#getEvent()
     * @see #getEventParameter()
     * @generated
     */
    EReference getEventParameter_Event();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isSendFlag <em>Send Flag</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Send Flag</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.EventParameter#isSendFlag()
     * @see #getEventParameter()
     * @generated
     */
    EAttribute getEventParameter_SendFlag();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isRecvFlag <em>Recv Flag</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Recv Flag</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.EventParameter#isRecvFlag()
     * @see #getEventParameter()
     * @generated
     */
    EAttribute getEventParameter_RecvFlag();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isSyncFlag <em>Sync Flag</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Flag</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.EventParameter#isSyncFlag()
     * @see #getEventParameter()
     * @generated
     */
    EAttribute getEventParameter_SyncFlag();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.LocationParameter <em>Location Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Location Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.LocationParameter
     * @generated
     */
    EClass getLocationParameter();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.LocationParameter#getLocation <em>Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Location</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.LocationParameter#getLocation()
     * @see #getLocationParameter()
     * @generated
     */
    EReference getLocationParameter_Location();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.AlgParameter <em>Alg Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Alg Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.AlgParameter
     * @generated
     */
    EClass getAlgParameter();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.AlgParameter#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.AlgParameter#getVariable()
     * @see #getAlgParameter()
     * @generated
     */
    EReference getAlgParameter_Variable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.ComponentParameter <em>Component Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentParameter
     * @generated
     */
    EClass getComponentParameter();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.ComponentParameter#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentParameter#getType()
     * @see #getComponentParameter()
     * @generated
     */
    EReference getComponentParameter_Type();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.ComponentParameter#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.ComponentParameter#getName()
     * @see #getComponentParameter()
     * @generated
     */
    EAttribute getComponentParameter_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.Equation <em>Equation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Equation</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Equation
     * @generated
     */
    EClass getEquation();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.Equation#isDerivative <em>Derivative</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Derivative</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Equation#isDerivative()
     * @see #getEquation()
     * @generated
     */
    EAttribute getEquation_Derivative();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.Equation#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Equation#getValue()
     * @see #getEquation()
     * @generated
     */
    EReference getEquation_Value();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.Equation#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Equation#getVariable()
     * @see #getEquation()
     * @generated
     */
    EReference getEquation_Variable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.IoDecl <em>Io Decl</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Io Decl</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.IoDecl
     * @generated
     */
    EClass getIoDecl();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.Invariant <em>Invariant</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Invariant</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Invariant
     * @generated
     */
    EClass getInvariant();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Invariant#getName()
     * @see #getInvariant()
     * @generated
     */
    EAttribute getInvariant_Name();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getSupKind <em>Sup Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sup Kind</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Invariant#getSupKind()
     * @see #getInvariant()
     * @generated
     */
    EAttribute getInvariant_SupKind();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getPredicate <em>Predicate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Predicate</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Invariant#getPredicate()
     * @see #getInvariant()
     * @generated
     */
    EReference getInvariant_Predicate();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getInvKind <em>Inv Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Inv Kind</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Invariant#getInvKind()
     * @see #getInvariant()
     * @generated
     */
    EAttribute getInvariant_InvKind();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getEvent <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.Invariant#getEvent()
     * @see #getInvariant()
     * @generated
     */
    EReference getInvariant_Event();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.cif.metamodel.cif.SupKind <em>Sup Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Sup Kind</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.SupKind
     * @generated
     */
    EEnum getSupKind();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.cif.metamodel.cif.InvKind <em>Inv Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Inv Kind</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.InvKind
     * @generated
     */
    EEnum getInvKind();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Identifier</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='CifIdentifier' baseType='http://www.eclipse.org/emf/2002/Ecore#EString' pattern='[A-Za-z_][A-Za-z0-9_]*'"
     * @generated
     */
    EDataType getCifIdentifier();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    CifFactory getCifFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComponentImpl <em>Component</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.ComponentImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComponent()
         * @generated
         */
        EClass COMPONENT = eINSTANCE.getComponent();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__NAME = eINSTANCE.getComponent_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.GroupImpl <em>Group</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.GroupImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getGroup()
         * @generated
         */
        EClass GROUP = eINSTANCE.getGroup();

        /**
         * The meta object literal for the '<em><b>Definitions</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GROUP__DEFINITIONS = eINSTANCE.getGroup_Definitions();

        /**
         * The meta object literal for the '<em><b>Components</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GROUP__COMPONENTS = eINSTANCE.getGroup_Components();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComponentDefImpl <em>Component Def</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.ComponentDefImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComponentDef()
         * @generated
         */
        EClass COMPONENT_DEF = eINSTANCE.getComponentDef();

        /**
         * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_DEF__BODY = eINSTANCE.getComponentDef_Body();

        /**
         * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_DEF__PARAMETERS = eINSTANCE.getComponentDef_Parameters();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComponentInstImpl <em>Component Inst</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.ComponentInstImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComponentInst()
         * @generated
         */
        EClass COMPONENT_INST = eINSTANCE.getComponentInst();

        /**
         * The meta object literal for the '<em><b>Definition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_INST__DEFINITION = eINSTANCE.getComponentInst_Definition();

        /**
         * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_INST__ARGUMENTS = eINSTANCE.getComponentInst_Arguments();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl <em>Complex Component</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComplexComponent()
         * @generated
         */
        EClass COMPLEX_COMPONENT = eINSTANCE.getComplexComponent();

        /**
         * The meta object literal for the '<em><b>Declarations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPLEX_COMPONENT__DECLARATIONS = eINSTANCE.getComplexComponent_Declarations();

        /**
         * The meta object literal for the '<em><b>Markeds</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPLEX_COMPONENT__MARKEDS = eINSTANCE.getComplexComponent_Markeds();

        /**
         * The meta object literal for the '<em><b>Invariants</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPLEX_COMPONENT__INVARIANTS = eINSTANCE.getComplexComponent_Invariants();

        /**
         * The meta object literal for the '<em><b>Initials</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPLEX_COMPONENT__INITIALS = eINSTANCE.getComplexComponent_Initials();

        /**
         * The meta object literal for the '<em><b>Equations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPLEX_COMPONENT__EQUATIONS = eINSTANCE.getComplexComponent_Equations();

        /**
         * The meta object literal for the '<em><b>Io Decls</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPLEX_COMPONENT__IO_DECLS = eINSTANCE.getComplexComponent_IoDecls();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.SpecificationImpl <em>Specification</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.SpecificationImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getSpecification()
         * @generated
         */
        EClass SPECIFICATION = eINSTANCE.getSpecification();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ParameterImpl <em>Parameter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.ParameterImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getParameter()
         * @generated
         */
        EClass PARAMETER = eINSTANCE.getParameter();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.EventParameterImpl <em>Event Parameter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.EventParameterImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getEventParameter()
         * @generated
         */
        EClass EVENT_PARAMETER = eINSTANCE.getEventParameter();

        /**
         * The meta object literal for the '<em><b>Event</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EVENT_PARAMETER__EVENT = eINSTANCE.getEventParameter_Event();

        /**
         * The meta object literal for the '<em><b>Send Flag</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EVENT_PARAMETER__SEND_FLAG = eINSTANCE.getEventParameter_SendFlag();

        /**
         * The meta object literal for the '<em><b>Recv Flag</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EVENT_PARAMETER__RECV_FLAG = eINSTANCE.getEventParameter_RecvFlag();

        /**
         * The meta object literal for the '<em><b>Sync Flag</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EVENT_PARAMETER__SYNC_FLAG = eINSTANCE.getEventParameter_SyncFlag();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.LocationParameterImpl <em>Location Parameter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.LocationParameterImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getLocationParameter()
         * @generated
         */
        EClass LOCATION_PARAMETER = eINSTANCE.getLocationParameter();

        /**
         * The meta object literal for the '<em><b>Location</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LOCATION_PARAMETER__LOCATION = eINSTANCE.getLocationParameter_Location();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.AlgParameterImpl <em>Alg Parameter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.AlgParameterImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getAlgParameter()
         * @generated
         */
        EClass ALG_PARAMETER = eINSTANCE.getAlgParameter();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALG_PARAMETER__VARIABLE = eINSTANCE.getAlgParameter_Variable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.ComponentParameterImpl <em>Component Parameter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.ComponentParameterImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getComponentParameter()
         * @generated
         */
        EClass COMPONENT_PARAMETER = eINSTANCE.getComponentParameter();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_PARAMETER__TYPE = eINSTANCE.getComponentParameter_Type();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT_PARAMETER__NAME = eINSTANCE.getComponentParameter_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.EquationImpl <em>Equation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.EquationImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getEquation()
         * @generated
         */
        EClass EQUATION = eINSTANCE.getEquation();

        /**
         * The meta object literal for the '<em><b>Derivative</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EQUATION__DERIVATIVE = eINSTANCE.getEquation_Derivative();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EQUATION__VALUE = eINSTANCE.getEquation_Value();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EQUATION__VARIABLE = eINSTANCE.getEquation_Variable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl <em>Io Decl</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getIoDecl()
         * @generated
         */
        EClass IO_DECL = eINSTANCE.getIoDecl();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.impl.InvariantImpl <em>Invariant</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.impl.InvariantImpl
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getInvariant()
         * @generated
         */
        EClass INVARIANT = eINSTANCE.getInvariant();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INVARIANT__NAME = eINSTANCE.getInvariant_Name();

        /**
         * The meta object literal for the '<em><b>Sup Kind</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INVARIANT__SUP_KIND = eINSTANCE.getInvariant_SupKind();

        /**
         * The meta object literal for the '<em><b>Predicate</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INVARIANT__PREDICATE = eINSTANCE.getInvariant_Predicate();

        /**
         * The meta object literal for the '<em><b>Inv Kind</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INVARIANT__INV_KIND = eINSTANCE.getInvariant_InvKind();

        /**
         * The meta object literal for the '<em><b>Event</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INVARIANT__EVENT = eINSTANCE.getInvariant_Event();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.SupKind <em>Sup Kind</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.SupKind
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getSupKind()
         * @generated
         */
        EEnum SUP_KIND = eINSTANCE.getSupKind();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.InvKind <em>Inv Kind</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.InvKind
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getInvKind()
         * @generated
         */
        EEnum INV_KIND = eINSTANCE.getInvKind();

        /**
         * The meta object literal for the '<em>Identifier</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl#getCifIdentifier()
         * @generated
         */
        EDataType CIF_IDENTIFIER = eINSTANCE.getCifIdentifier();

    }

} //CifPackage
