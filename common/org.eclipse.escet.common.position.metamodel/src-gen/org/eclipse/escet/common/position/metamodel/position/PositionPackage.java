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
package org.eclipse.escet.common.position.metamodel.position;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.eclipse.escet.common.position.metamodel.position.PositionFactory
 * @model kind="package"
 * @generated
 */
public interface PositionPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "position";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/position";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "position";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    PositionPackage eINSTANCE = org.eclipse.escet.common.position.metamodel.position.impl.PositionPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl <em>Position</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl
     * @see org.eclipse.escet.common.position.metamodel.position.impl.PositionPackageImpl#getPosition()
     * @generated
     */
    int POSITION = 0;

    /**
     * The feature id for the '<em><b>Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION__SOURCE = 0;

    /**
     * The feature id for the '<em><b>Start Line</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION__START_LINE = 1;

    /**
     * The feature id for the '<em><b>End Offset</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION__END_OFFSET = 2;

    /**
     * The feature id for the '<em><b>Start Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION__START_COLUMN = 3;

    /**
     * The feature id for the '<em><b>End Line</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION__END_LINE = 4;

    /**
     * The feature id for the '<em><b>End Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION__END_COLUMN = 5;

    /**
     * The feature id for the '<em><b>Start Offset</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION__START_OFFSET = 6;

    /**
     * The feature id for the '<em><b>Location</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION__LOCATION = 7;

    /**
     * The number of structural features of the '<em>Position</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION_FEATURE_COUNT = 8;

    /**
     * The number of operations of the '<em>Position</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl <em>Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl
     * @see org.eclipse.escet.common.position.metamodel.position.impl.PositionPackageImpl#getPositionObject()
     * @generated
     */
    int POSITION_OBJECT = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION_OBJECT__POSITION = 0;

    /**
     * The number of structural features of the '<em>Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION_OBJECT_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int POSITION_OBJECT_OPERATION_COUNT = 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.common.position.metamodel.position.Position <em>Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Position</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position
     * @generated
     */
    EClass getPosition();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.common.position.metamodel.position.Position#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Source</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position#getSource()
     * @see #getPosition()
     * @generated
     */
    EAttribute getPosition_Source();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartLine <em>Start Line</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Line</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position#getStartLine()
     * @see #getPosition()
     * @generated
     */
    EAttribute getPosition_StartLine();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndOffset <em>End Offset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Offset</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position#getEndOffset()
     * @see #getPosition()
     * @generated
     */
    EAttribute getPosition_EndOffset();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartColumn <em>Start Column</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Column</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position#getStartColumn()
     * @see #getPosition()
     * @generated
     */
    EAttribute getPosition_StartColumn();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndLine <em>End Line</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Line</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position#getEndLine()
     * @see #getPosition()
     * @generated
     */
    EAttribute getPosition_EndLine();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndColumn <em>End Column</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Column</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position#getEndColumn()
     * @see #getPosition()
     * @generated
     */
    EAttribute getPosition_EndColumn();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartOffset <em>Start Offset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Offset</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position#getStartOffset()
     * @see #getPosition()
     * @generated
     */
    EAttribute getPosition_StartOffset();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.common.position.metamodel.position.Position#getLocation <em>Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Location</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.Position#getLocation()
     * @see #getPosition()
     * @generated
     */
    EAttribute getPosition_Location();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.common.position.metamodel.position.PositionObject <em>Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Object</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.PositionObject
     * @generated
     */
    EClass getPositionObject();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.common.position.metamodel.position.PositionObject#getPosition <em>Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Position</em>'.
     * @see org.eclipse.escet.common.position.metamodel.position.PositionObject#getPosition()
     * @see #getPositionObject()
     * @generated
     */
    EReference getPositionObject_Position();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    PositionFactory getPositionFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl <em>Position</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl
         * @see org.eclipse.escet.common.position.metamodel.position.impl.PositionPackageImpl#getPosition()
         * @generated
         */
        EClass POSITION = eINSTANCE.getPosition();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSITION__SOURCE = eINSTANCE.getPosition_Source();

        /**
         * The meta object literal for the '<em><b>Start Line</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSITION__START_LINE = eINSTANCE.getPosition_StartLine();

        /**
         * The meta object literal for the '<em><b>End Offset</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSITION__END_OFFSET = eINSTANCE.getPosition_EndOffset();

        /**
         * The meta object literal for the '<em><b>Start Column</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSITION__START_COLUMN = eINSTANCE.getPosition_StartColumn();

        /**
         * The meta object literal for the '<em><b>End Line</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSITION__END_LINE = eINSTANCE.getPosition_EndLine();

        /**
         * The meta object literal for the '<em><b>End Column</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSITION__END_COLUMN = eINSTANCE.getPosition_EndColumn();

        /**
         * The meta object literal for the '<em><b>Start Offset</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSITION__START_OFFSET = eINSTANCE.getPosition_StartOffset();

        /**
         * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute POSITION__LOCATION = eINSTANCE.getPosition_Location();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl <em>Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl
         * @see org.eclipse.escet.common.position.metamodel.position.impl.PositionPackageImpl#getPositionObject()
         * @generated
         */
        EClass POSITION_OBJECT = eINSTANCE.getPositionObject();

        /**
         * The meta object literal for the '<em><b>Position</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference POSITION_OBJECT__POSITION = eINSTANCE.getPositionObject_Position();

    }

} //PositionPackage
