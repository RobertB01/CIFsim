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
package org.eclipse.escet.cif.metamodel.cif.cifsvg;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgFactory
 * @model kind="package"
 * @generated
 */
public interface CifsvgPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "cifsvg";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/cif/cifsvg";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "cifsvg";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    CifsvgPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgFileImpl <em>Svg File</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgFileImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgFile()
     * @generated
     */
    int SVG_FILE = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_FILE__POSITION = CifPackage.IO_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_FILE__PATH = CifPackage.IO_DECL_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Svg File</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_FILE_FEATURE_COUNT = CifPackage.IO_DECL_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Svg File</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_FILE_OPERATION_COUNT = CifPackage.IO_DECL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl <em>Svg Out</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgOut()
     * @generated
     */
    int SVG_OUT = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_OUT__POSITION = CifPackage.IO_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_OUT__ID = CifPackage.IO_DECL_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Attr</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_OUT__ATTR = CifPackage.IO_DECL_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Svg File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_OUT__SVG_FILE = CifPackage.IO_DECL_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Attr Text Pos</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_OUT__ATTR_TEXT_POS = CifPackage.IO_DECL_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_OUT__VALUE = CifPackage.IO_DECL_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Svg Out</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_OUT_FEATURE_COUNT = CifPackage.IO_DECL_FEATURE_COUNT + 5;

    /**
     * The number of operations of the '<em>Svg Out</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_OUT_OPERATION_COUNT = CifPackage.IO_DECL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInImpl <em>Svg In</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgIn()
     * @generated
     */
    int SVG_IN = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN__POSITION = CifPackage.IO_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN__ID = CifPackage.IO_DECL_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Svg File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN__SVG_FILE = CifPackage.IO_DECL_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN__EVENT = CifPackage.IO_DECL_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Svg In</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_FEATURE_COUNT = CifPackage.IO_DECL_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Svg In</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_OPERATION_COUNT = CifPackage.IO_DECL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventImpl <em>Svg In Event</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgInEvent()
     * @generated
     */
    int SVG_IN_EVENT = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Svg In Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Svg In Event</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventSingleImpl <em>Svg In Event Single</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventSingleImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgInEventSingle()
     * @generated
     */
    int SVG_IN_EVENT_SINGLE = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_SINGLE__POSITION = SVG_IN_EVENT__POSITION;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_SINGLE__EVENT = SVG_IN_EVENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Svg In Event Single</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_SINGLE_FEATURE_COUNT = SVG_IN_EVENT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Svg In Event Single</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_SINGLE_OPERATION_COUNT = SVG_IN_EVENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventIfImpl <em>Svg In Event If</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventIfImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgInEventIf()
     * @generated
     */
    int SVG_IN_EVENT_IF = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF__POSITION = SVG_IN_EVENT__POSITION;

    /**
     * The feature id for the '<em><b>Entries</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF__ENTRIES = SVG_IN_EVENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Svg In Event If</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF_FEATURE_COUNT = SVG_IN_EVENT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Svg In Event If</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF_OPERATION_COUNT = SVG_IN_EVENT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventIfEntryImpl <em>Svg In Event If Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventIfEntryImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgInEventIfEntry()
     * @generated
     */
    int SVG_IN_EVENT_IF_ENTRY = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF_ENTRY__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Guard</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF_ENTRY__GUARD = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF_ENTRY__EVENT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Svg In Event If Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF_ENTRY_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Svg In Event If Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_IN_EVENT_IF_ENTRY_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgCopyImpl <em>Svg Copy</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgCopyImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgCopy()
     * @generated
     */
    int SVG_COPY = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_COPY__POSITION = CifPackage.IO_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_COPY__ID = CifPackage.IO_DECL_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Pre</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_COPY__PRE = CifPackage.IO_DECL_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Post</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_COPY__POST = CifPackage.IO_DECL_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Svg File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_COPY__SVG_FILE = CifPackage.IO_DECL_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Svg Copy</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_COPY_FEATURE_COUNT = CifPackage.IO_DECL_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>Svg Copy</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_COPY_OPERATION_COUNT = CifPackage.IO_DECL_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgMoveImpl <em>Svg Move</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgMoveImpl
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgMove()
     * @generated
     */
    int SVG_MOVE = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_MOVE__POSITION = CifPackage.IO_DECL__POSITION;

    /**
     * The feature id for the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_MOVE__ID = CifPackage.IO_DECL_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>X</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_MOVE__X = CifPackage.IO_DECL_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Y</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_MOVE__Y = CifPackage.IO_DECL_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Svg File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_MOVE__SVG_FILE = CifPackage.IO_DECL_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Svg Move</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_MOVE_FEATURE_COUNT = CifPackage.IO_DECL_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>Svg Move</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SVG_MOVE_OPERATION_COUNT = CifPackage.IO_DECL_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile <em>Svg File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg File</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile
     * @generated
     */
    EClass getSvgFile();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile#getPath <em>Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Path</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile#getPath()
     * @see #getSvgFile()
     * @generated
     */
    EAttribute getSvgFile_Path();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut <em>Svg Out</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg Out</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut
     * @generated
     */
    EClass getSvgOut();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Id</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getId()
     * @see #getSvgOut()
     * @generated
     */
    EReference getSvgOut_Id();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getAttr <em>Attr</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attr</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getAttr()
     * @see #getSvgOut()
     * @generated
     */
    EAttribute getSvgOut_Attr();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getSvgFile <em>Svg File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Svg File</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getSvgFile()
     * @see #getSvgOut()
     * @generated
     */
    EReference getSvgOut_SvgFile();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getAttrTextPos <em>Attr Text Pos</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Attr Text Pos</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getAttrTextPos()
     * @see #getSvgOut()
     * @generated
     */
    EReference getSvgOut_AttrTextPos();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getValue()
     * @see #getSvgOut()
     * @generated
     */
    EReference getSvgOut_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn <em>Svg In</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg In</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn
     * @generated
     */
    EClass getSvgIn();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Id</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getId()
     * @see #getSvgIn()
     * @generated
     */
    EReference getSvgIn_Id();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getSvgFile <em>Svg File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Svg File</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getSvgFile()
     * @see #getSvgIn()
     * @generated
     */
    EReference getSvgIn_SvgFile();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getEvent <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getEvent()
     * @see #getSvgIn()
     * @generated
     */
    EReference getSvgIn_Event();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle <em>Svg In Event Single</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg In Event Single</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle
     * @generated
     */
    EClass getSvgInEventSingle();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle#getEvent <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle#getEvent()
     * @see #getSvgInEventSingle()
     * @generated
     */
    EReference getSvgInEventSingle_Event();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf <em>Svg In Event If</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg In Event If</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf
     * @generated
     */
    EClass getSvgInEventIf();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf#getEntries <em>Entries</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Entries</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf#getEntries()
     * @see #getSvgInEventIf()
     * @generated
     */
    EReference getSvgInEventIf_Entries();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry <em>Svg In Event If Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg In Event If Entry</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry
     * @generated
     */
    EClass getSvgInEventIfEntry();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry#getGuard <em>Guard</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Guard</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry#getGuard()
     * @see #getSvgInEventIfEntry()
     * @generated
     */
    EReference getSvgInEventIfEntry_Guard();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry#getEvent <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry#getEvent()
     * @see #getSvgInEventIfEntry()
     * @generated
     */
    EReference getSvgInEventIfEntry_Event();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy <em>Svg Copy</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg Copy</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy
     * @generated
     */
    EClass getSvgCopy();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Id</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy#getId()
     * @see #getSvgCopy()
     * @generated
     */
    EReference getSvgCopy_Id();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy#getPre <em>Pre</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Pre</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy#getPre()
     * @see #getSvgCopy()
     * @generated
     */
    EReference getSvgCopy_Pre();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy#getPost <em>Post</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Post</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy#getPost()
     * @see #getSvgCopy()
     * @generated
     */
    EReference getSvgCopy_Post();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy#getSvgFile <em>Svg File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Svg File</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy#getSvgFile()
     * @see #getSvgCopy()
     * @generated
     */
    EReference getSvgCopy_SvgFile();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove <em>Svg Move</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg Move</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove
     * @generated
     */
    EClass getSvgMove();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Id</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getId()
     * @see #getSvgMove()
     * @generated
     */
    EReference getSvgMove_Id();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getX <em>X</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>X</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getX()
     * @see #getSvgMove()
     * @generated
     */
    EReference getSvgMove_X();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getY <em>Y</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Y</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getY()
     * @see #getSvgMove()
     * @generated
     */
    EReference getSvgMove_Y();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getSvgFile <em>Svg File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Svg File</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getSvgFile()
     * @see #getSvgMove()
     * @generated
     */
    EReference getSvgMove_SvgFile();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent <em>Svg In Event</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Svg In Event</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent
     * @generated
     */
    EClass getSvgInEvent();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    CifsvgFactory getCifsvgFactory();

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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgFileImpl <em>Svg File</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgFileImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgFile()
         * @generated
         */
        EClass SVG_FILE = eINSTANCE.getSvgFile();

        /**
         * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SVG_FILE__PATH = eINSTANCE.getSvgFile_Path();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl <em>Svg Out</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgOut()
         * @generated
         */
        EClass SVG_OUT = eINSTANCE.getSvgOut();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_OUT__ID = eINSTANCE.getSvgOut_Id();

        /**
         * The meta object literal for the '<em><b>Attr</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SVG_OUT__ATTR = eINSTANCE.getSvgOut_Attr();

        /**
         * The meta object literal for the '<em><b>Svg File</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_OUT__SVG_FILE = eINSTANCE.getSvgOut_SvgFile();

        /**
         * The meta object literal for the '<em><b>Attr Text Pos</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_OUT__ATTR_TEXT_POS = eINSTANCE.getSvgOut_AttrTextPos();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_OUT__VALUE = eINSTANCE.getSvgOut_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInImpl <em>Svg In</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgIn()
         * @generated
         */
        EClass SVG_IN = eINSTANCE.getSvgIn();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_IN__ID = eINSTANCE.getSvgIn_Id();

        /**
         * The meta object literal for the '<em><b>Svg File</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_IN__SVG_FILE = eINSTANCE.getSvgIn_SvgFile();

        /**
         * The meta object literal for the '<em><b>Event</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_IN__EVENT = eINSTANCE.getSvgIn_Event();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventSingleImpl <em>Svg In Event Single</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventSingleImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgInEventSingle()
         * @generated
         */
        EClass SVG_IN_EVENT_SINGLE = eINSTANCE.getSvgInEventSingle();

        /**
         * The meta object literal for the '<em><b>Event</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_IN_EVENT_SINGLE__EVENT = eINSTANCE.getSvgInEventSingle_Event();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventIfImpl <em>Svg In Event If</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventIfImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgInEventIf()
         * @generated
         */
        EClass SVG_IN_EVENT_IF = eINSTANCE.getSvgInEventIf();

        /**
         * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_IN_EVENT_IF__ENTRIES = eINSTANCE.getSvgInEventIf_Entries();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventIfEntryImpl <em>Svg In Event If Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventIfEntryImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgInEventIfEntry()
         * @generated
         */
        EClass SVG_IN_EVENT_IF_ENTRY = eINSTANCE.getSvgInEventIfEntry();

        /**
         * The meta object literal for the '<em><b>Guard</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_IN_EVENT_IF_ENTRY__GUARD = eINSTANCE.getSvgInEventIfEntry_Guard();

        /**
         * The meta object literal for the '<em><b>Event</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_IN_EVENT_IF_ENTRY__EVENT = eINSTANCE.getSvgInEventIfEntry_Event();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgCopyImpl <em>Svg Copy</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgCopyImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgCopy()
         * @generated
         */
        EClass SVG_COPY = eINSTANCE.getSvgCopy();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_COPY__ID = eINSTANCE.getSvgCopy_Id();

        /**
         * The meta object literal for the '<em><b>Pre</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_COPY__PRE = eINSTANCE.getSvgCopy_Pre();

        /**
         * The meta object literal for the '<em><b>Post</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_COPY__POST = eINSTANCE.getSvgCopy_Post();

        /**
         * The meta object literal for the '<em><b>Svg File</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_COPY__SVG_FILE = eINSTANCE.getSvgCopy_SvgFile();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgMoveImpl <em>Svg Move</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgMoveImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgMove()
         * @generated
         */
        EClass SVG_MOVE = eINSTANCE.getSvgMove();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_MOVE__ID = eINSTANCE.getSvgMove_Id();

        /**
         * The meta object literal for the '<em><b>X</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_MOVE__X = eINSTANCE.getSvgMove_X();

        /**
         * The meta object literal for the '<em><b>Y</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_MOVE__Y = eINSTANCE.getSvgMove_Y();

        /**
         * The meta object literal for the '<em><b>Svg File</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SVG_MOVE__SVG_FILE = eINSTANCE.getSvgMove_SvgFile();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventImpl <em>Svg In Event</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventImpl
         * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl#getSvgInEvent()
         * @generated
         */
        EClass SVG_IN_EVENT = eINSTANCE.getSvgInEvent();

    }

} //CifsvgPackage
