/**
 * Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif.print;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;

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
 * @see org.eclipse.escet.cif.metamodel.cif.print.PrintFactory
 * @model kind="package"
 * @generated
 */
public interface PrintPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "print";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/cif/print";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "print";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    PrintPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintFileImpl <em>File</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintFileImpl
     * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl#getPrintFile()
     * @generated
     */
    int PRINT_FILE = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FILE__POSITION = CifPackage.IO_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FILE__PATH = CifPackage.IO_DECL_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>File</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FILE_FEATURE_COUNT = CifPackage.IO_DECL_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>File</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FILE_OPERATION_COUNT = CifPackage.IO_DECL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl <em>Print</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl
     * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl#getPrint()
     * @generated
     */
    int PRINT = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT__POSITION = CifPackage.IO_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Txt Pre</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT__TXT_PRE = CifPackage.IO_DECL_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Txt Post</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT__TXT_POST = CifPackage.IO_DECL_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>When Pre</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT__WHEN_PRE = CifPackage.IO_DECL_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>When Post</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT__WHEN_POST = CifPackage.IO_DECL_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT__FILE = CifPackage.IO_DECL_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Fors</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT__FORS = CifPackage.IO_DECL_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Print</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FEATURE_COUNT = CifPackage.IO_DECL_FEATURE_COUNT + 6;

    /**
     * The number of operations of the '<em>Print</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_OPERATION_COUNT = CifPackage.IO_DECL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintForImpl <em>For</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintForImpl
     * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl#getPrintFor()
     * @generated
     */
    int PRINT_FOR = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FOR__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Kind</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FOR__KIND = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FOR__EVENT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>For</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FOR_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>For</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRINT_FOR_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintForKind <em>For Kind</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintForKind
     * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl#getPrintForKind()
     * @generated
     */
    int PRINT_FOR_KIND = 3;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFile <em>File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>File</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintFile
     * @generated
     */
    EClass getPrintFile();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFile#getPath <em>Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Path</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintFile#getPath()
     * @see #getPrintFile()
     * @generated
     */
    EAttribute getPrintFile_Path();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.print.Print <em>Print</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Print</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.Print
     * @generated
     */
    EClass getPrint();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getTxtPre <em>Txt Pre</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Txt Pre</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.Print#getTxtPre()
     * @see #getPrint()
     * @generated
     */
    EReference getPrint_TxtPre();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getTxtPost <em>Txt Post</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Txt Post</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.Print#getTxtPost()
     * @see #getPrint()
     * @generated
     */
    EReference getPrint_TxtPost();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getWhenPre <em>When Pre</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>When Pre</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.Print#getWhenPre()
     * @see #getPrint()
     * @generated
     */
    EReference getPrint_WhenPre();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getWhenPost <em>When Post</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>When Post</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.Print#getWhenPost()
     * @see #getPrint()
     * @generated
     */
    EReference getPrint_WhenPost();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getFile <em>File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>File</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.Print#getFile()
     * @see #getPrint()
     * @generated
     */
    EReference getPrint_File();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getFors <em>Fors</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Fors</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.Print#getFors()
     * @see #getPrint()
     * @generated
     */
    EReference getPrint_Fors();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFor <em>For</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>For</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintFor
     * @generated
     */
    EClass getPrintFor();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFor#getKind <em>Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Kind</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintFor#getKind()
     * @see #getPrintFor()
     * @generated
     */
    EAttribute getPrintFor_Kind();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFor#getEvent <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintFor#getEvent()
     * @see #getPrintFor()
     * @generated
     */
    EReference getPrintFor_Event();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintForKind <em>For Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>For Kind</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintForKind
     * @generated
     */
    EEnum getPrintForKind();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    PrintFactory getPrintFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintFileImpl <em>File</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintFileImpl
         * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl#getPrintFile()
         * @generated
         */
        EClass PRINT_FILE = eINSTANCE.getPrintFile();

        /**
         * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PRINT_FILE__PATH = eINSTANCE.getPrintFile_Path();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl <em>Print</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl
         * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl#getPrint()
         * @generated
         */
        EClass PRINT = eINSTANCE.getPrint();

        /**
         * The meta object literal for the '<em><b>Txt Pre</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRINT__TXT_PRE = eINSTANCE.getPrint_TxtPre();

        /**
         * The meta object literal for the '<em><b>Txt Post</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRINT__TXT_POST = eINSTANCE.getPrint_TxtPost();

        /**
         * The meta object literal for the '<em><b>When Pre</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRINT__WHEN_PRE = eINSTANCE.getPrint_WhenPre();

        /**
         * The meta object literal for the '<em><b>When Post</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRINT__WHEN_POST = eINSTANCE.getPrint_WhenPost();

        /**
         * The meta object literal for the '<em><b>File</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRINT__FILE = eINSTANCE.getPrint_File();

        /**
         * The meta object literal for the '<em><b>Fors</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRINT__FORS = eINSTANCE.getPrint_Fors();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintForImpl <em>For</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintForImpl
         * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl#getPrintFor()
         * @generated
         */
        EClass PRINT_FOR = eINSTANCE.getPrintFor();

        /**
         * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PRINT_FOR__KIND = eINSTANCE.getPrintFor_Kind();

        /**
         * The meta object literal for the '<em><b>Event</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRINT_FOR__EVENT = eINSTANCE.getPrintFor_Event();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintForKind <em>For Kind</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.print.PrintForKind
         * @see org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl#getPrintForKind()
         * @generated
         */
        EEnum PRINT_FOR_KIND = eINSTANCE.getPrintForKind();

    }

} //PrintPackage
