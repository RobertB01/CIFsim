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
package org.eclipse.escet.cif.metamodel.cif.declarations;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsFactory
 * @model kind="package"
 * @generated
 */
public interface DeclarationsPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "declarations";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/cif/declarations";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "declarations";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    DeclarationsPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationImpl <em>Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getDeclaration()
     * @generated
     */
    int DECLARATION = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION__POSITION = AnnotationsPackage.ANNOTATED_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION__ANNOTATIONS = AnnotationsPackage.ANNOTATED_OBJECT__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION__NAME = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION_FEATURE_COUNT = AnnotationsPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION_OPERATION_COUNT = AnnotationsPackage.ANNOTATED_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.AlgVariableImpl <em>Alg Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.AlgVariableImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getAlgVariable()
     * @generated
     */
    int ALG_VARIABLE = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE__ANNOTATIONS = DECLARATION__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE__VALUE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE__TYPE = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Alg Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Alg Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALG_VARIABLE_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.EventImpl <em>Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.EventImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getEvent()
     * @generated
     */
    int EVENT = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT__ANNOTATIONS = DECLARATION__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Controllable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT__CONTROLLABLE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT__TYPE = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EVENT_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.EnumDeclImpl <em>Enum Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.EnumDeclImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getEnumDecl()
     * @generated
     */
    int ENUM_DECL = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECL__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECL__ANNOTATIONS = DECLARATION__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECL__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Literals</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECL__LITERALS = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Enum Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECL_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Enum Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECL_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.TypeDeclImpl <em>Type Decl</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.TypeDeclImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getTypeDecl()
     * @generated
     */
    int TYPE_DECL = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL__ANNOTATIONS = DECLARATION__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL__TYPE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Type Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Type Decl</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECL_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.EnumLiteralImpl <em>Enum Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.EnumLiteralImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getEnumLiteral()
     * @generated
     */
    int ENUM_LITERAL = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Enum Literal</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Enum Literal</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_LITERAL_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.DiscVariableImpl <em>Disc Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DiscVariableImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getDiscVariable()
     * @generated
     */
    int DISC_VARIABLE = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE__ANNOTATIONS = DECLARATION__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE__TYPE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE__VALUE = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Disc Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Disc Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISC_VARIABLE_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.VariableValueImpl <em>Variable Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.VariableValueImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getVariableValue()
     * @generated
     */
    int VARIABLE_VALUE = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_VALUE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_VALUE__VALUES = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Variable Value</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_VALUE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Variable Value</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_VALUE_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.ConstantImpl <em>Constant</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.ConstantImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getConstant()
     * @generated
     */
    int CONSTANT = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT__ANNOTATIONS = DECLARATION__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT__TYPE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT__VALUE = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Constant</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Constant</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.ContVariableImpl <em>Cont Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.ContVariableImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getContVariable()
     * @generated
     */
    int CONT_VARIABLE = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE__ANNOTATIONS = DECLARATION__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE__VALUE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Derivative</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE__DERIVATIVE = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Cont Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Cont Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONT_VARIABLE_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.InputVariableImpl <em>Input Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.InputVariableImpl
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getInputVariable()
     * @generated
     */
    int INPUT_VARIABLE = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE__ANNOTATIONS = DECLARATION__ANNOTATIONS;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE__TYPE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Input Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Input Variable</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_VARIABLE_OPERATION_COUNT = DECLARATION_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Declaration <em>Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Declaration</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Declaration
     * @generated
     */
    EClass getDeclaration();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Declaration#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Declaration#getName()
     * @see #getDeclaration()
     * @generated
     */
    EAttribute getDeclaration_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable <em>Alg Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Alg Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable
     * @generated
     */
    EClass getAlgVariable();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable#getValue()
     * @see #getAlgVariable()
     * @generated
     */
    EReference getAlgVariable_Value();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable#getType()
     * @see #getAlgVariable()
     * @generated
     */
    EReference getAlgVariable_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Event <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Event
     * @generated
     */
    EClass getEvent();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Event#getControllable <em>Controllable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Controllable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Event#getControllable()
     * @see #getEvent()
     * @generated
     */
    EAttribute getEvent_Controllable();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Event#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Event#getType()
     * @see #getEvent()
     * @generated
     */
    EReference getEvent_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl <em>Enum Decl</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Enum Decl</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl
     * @generated
     */
    EClass getEnumDecl();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl#getLiterals <em>Literals</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Literals</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl#getLiterals()
     * @see #getEnumDecl()
     * @generated
     */
    EReference getEnumDecl_Literals();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl <em>Type Decl</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Decl</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl
     * @generated
     */
    EClass getTypeDecl();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl#getType()
     * @see #getTypeDecl()
     * @generated
     */
    EReference getTypeDecl_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral <em>Enum Literal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Enum Literal</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral
     * @generated
     */
    EClass getEnumLiteral();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral#getName()
     * @see #getEnumLiteral()
     * @generated
     */
    EAttribute getEnumLiteral_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable <em>Disc Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Disc Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable
     * @generated
     */
    EClass getDiscVariable();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable#getType()
     * @see #getDiscVariable()
     * @generated
     */
    EReference getDiscVariable_Type();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable#getValue()
     * @see #getDiscVariable()
     * @generated
     */
    EReference getDiscVariable_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue <em>Variable Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Variable Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue
     * @generated
     */
    EClass getVariableValue();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Values</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue#getValues()
     * @see #getVariableValue()
     * @generated
     */
    EReference getVariableValue_Values();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Constant <em>Constant</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Constant</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Constant
     * @generated
     */
    EClass getConstant();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Constant#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Constant#getType()
     * @see #getConstant()
     * @generated
     */
    EReference getConstant_Type();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Constant#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Constant#getValue()
     * @see #getConstant()
     * @generated
     */
    EReference getConstant_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable <em>Cont Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Cont Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable
     * @generated
     */
    EClass getContVariable();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable#getValue()
     * @see #getContVariable()
     * @generated
     */
    EReference getContVariable_Value();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable#getDerivative <em>Derivative</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Derivative</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable#getDerivative()
     * @see #getContVariable()
     * @generated
     */
    EReference getContVariable_Derivative();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable <em>Input Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Input Variable</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable
     * @generated
     */
    EClass getInputVariable();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable#getType()
     * @see #getInputVariable()
     * @generated
     */
    EReference getInputVariable_Type();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    DeclarationsFactory getDeclarationsFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationImpl <em>Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getDeclaration()
         * @generated
         */
        EClass DECLARATION = eINSTANCE.getDeclaration();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DECLARATION__NAME = eINSTANCE.getDeclaration_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.AlgVariableImpl <em>Alg Variable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.AlgVariableImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getAlgVariable()
         * @generated
         */
        EClass ALG_VARIABLE = eINSTANCE.getAlgVariable();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALG_VARIABLE__VALUE = eINSTANCE.getAlgVariable_Value();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALG_VARIABLE__TYPE = eINSTANCE.getAlgVariable_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.EventImpl <em>Event</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.EventImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getEvent()
         * @generated
         */
        EClass EVENT = eINSTANCE.getEvent();

        /**
         * The meta object literal for the '<em><b>Controllable</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EVENT__CONTROLLABLE = eINSTANCE.getEvent_Controllable();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EVENT__TYPE = eINSTANCE.getEvent_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.EnumDeclImpl <em>Enum Decl</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.EnumDeclImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getEnumDecl()
         * @generated
         */
        EClass ENUM_DECL = eINSTANCE.getEnumDecl();

        /**
         * The meta object literal for the '<em><b>Literals</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ENUM_DECL__LITERALS = eINSTANCE.getEnumDecl_Literals();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.TypeDeclImpl <em>Type Decl</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.TypeDeclImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getTypeDecl()
         * @generated
         */
        EClass TYPE_DECL = eINSTANCE.getTypeDecl();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TYPE_DECL__TYPE = eINSTANCE.getTypeDecl_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.EnumLiteralImpl <em>Enum Literal</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.EnumLiteralImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getEnumLiteral()
         * @generated
         */
        EClass ENUM_LITERAL = eINSTANCE.getEnumLiteral();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ENUM_LITERAL__NAME = eINSTANCE.getEnumLiteral_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.DiscVariableImpl <em>Disc Variable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DiscVariableImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getDiscVariable()
         * @generated
         */
        EClass DISC_VARIABLE = eINSTANCE.getDiscVariable();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISC_VARIABLE__TYPE = eINSTANCE.getDiscVariable_Type();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISC_VARIABLE__VALUE = eINSTANCE.getDiscVariable_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.VariableValueImpl <em>Variable Value</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.VariableValueImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getVariableValue()
         * @generated
         */
        EClass VARIABLE_VALUE = eINSTANCE.getVariableValue();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference VARIABLE_VALUE__VALUES = eINSTANCE.getVariableValue_Values();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.ConstantImpl <em>Constant</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.ConstantImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getConstant()
         * @generated
         */
        EClass CONSTANT = eINSTANCE.getConstant();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONSTANT__TYPE = eINSTANCE.getConstant_Type();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONSTANT__VALUE = eINSTANCE.getConstant_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.ContVariableImpl <em>Cont Variable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.ContVariableImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getContVariable()
         * @generated
         */
        EClass CONT_VARIABLE = eINSTANCE.getContVariable();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONT_VARIABLE__VALUE = eINSTANCE.getContVariable_Value();

        /**
         * The meta object literal for the '<em><b>Derivative</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONT_VARIABLE__DERIVATIVE = eINSTANCE.getContVariable_Derivative();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.InputVariableImpl <em>Input Variable</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.InputVariableImpl
         * @see org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl#getInputVariable()
         * @generated
         */
        EClass INPUT_VARIABLE = eINSTANCE.getInputVariable();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_VARIABLE__TYPE = eINSTANCE.getInputVariable_Type();

    }

} //DeclarationsPackage
