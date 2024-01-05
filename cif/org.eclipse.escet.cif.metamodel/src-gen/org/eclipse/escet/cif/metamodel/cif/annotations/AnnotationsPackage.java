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
package org.eclipse.escet.cif.metamodel.cif.annotations;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationsFactory
 * @model kind="package"
 * @generated
 */
public interface AnnotationsPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "annotations";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/cif/annotations";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "annotations";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    AnnotationsPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationImpl <em>Annotation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationImpl
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl#getAnnotation()
     * @generated
     */
    int ANNOTATION = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION__ARGUMENTS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationArgumentImpl <em>Annotation Argument</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationArgumentImpl
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl#getAnnotationArgument()
     * @generated
     */
    int ANNOTATION_ARGUMENT = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION_ARGUMENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION_ARGUMENT__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION_ARGUMENT__VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Annotation Argument</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION_ARGUMENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Annotation Argument</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION_ARGUMENT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotatedObjectImpl <em>Annotated Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotatedObjectImpl
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl#getAnnotatedObject()
     * @generated
     */
    int ANNOTATED_OBJECT = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATED_OBJECT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATED_OBJECT__ANNOTATIONS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Annotated Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATED_OBJECT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Annotated Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATED_OBJECT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.annotations.Annotation <em>Annotation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Annotation</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.Annotation
     * @generated
     */
    EClass getAnnotation();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.annotations.Annotation#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.Annotation#getName()
     * @see #getAnnotation()
     * @generated
     */
    EAttribute getAnnotation_Name();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.annotations.Annotation#getArguments <em>Arguments</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Arguments</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.Annotation#getArguments()
     * @see #getAnnotation()
     * @generated
     */
    EReference getAnnotation_Arguments();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument <em>Annotation Argument</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Annotation Argument</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument
     * @generated
     */
    EClass getAnnotationArgument();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument#getName()
     * @see #getAnnotationArgument()
     * @generated
     */
    EAttribute getAnnotationArgument_Name();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument#getValue()
     * @see #getAnnotationArgument()
     * @generated
     */
    EReference getAnnotationArgument_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject <em>Annotated Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Annotated Object</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject
     * @generated
     */
    EClass getAnnotatedObject();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject#getAnnotations <em>Annotations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Annotations</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject#getAnnotations()
     * @see #getAnnotatedObject()
     * @generated
     */
    EReference getAnnotatedObject_Annotations();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    AnnotationsFactory getAnnotationsFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationImpl <em>Annotation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationImpl
         * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl#getAnnotation()
         * @generated
         */
        EClass ANNOTATION = eINSTANCE.getAnnotation();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANNOTATION__NAME = eINSTANCE.getAnnotation_Name();

        /**
         * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ANNOTATION__ARGUMENTS = eINSTANCE.getAnnotation_Arguments();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationArgumentImpl <em>Annotation Argument</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationArgumentImpl
         * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl#getAnnotationArgument()
         * @generated
         */
        EClass ANNOTATION_ARGUMENT = eINSTANCE.getAnnotationArgument();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANNOTATION_ARGUMENT__NAME = eINSTANCE.getAnnotationArgument_Name();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ANNOTATION_ARGUMENT__VALUE = eINSTANCE.getAnnotationArgument_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotatedObjectImpl <em>Annotated Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotatedObjectImpl
         * @see org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsPackageImpl#getAnnotatedObject()
         * @generated
         */
        EClass ANNOTATED_OBJECT = eINSTANCE.getAnnotatedObject();

        /**
         * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ANNOTATED_OBJECT__ANNOTATIONS = eINSTANCE.getAnnotatedObject_Annotations();

    }

} //AnnotationsPackage
