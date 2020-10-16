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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage
 * @generated
 */
public interface CifsvgFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    CifsvgFactory eINSTANCE = org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Svg File</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Svg File</em>'.
     * @generated
     */
    SvgFile createSvgFile();

    /**
     * Returns a new object of class '<em>Svg Out</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Svg Out</em>'.
     * @generated
     */
    SvgOut createSvgOut();

    /**
     * Returns a new object of class '<em>Svg In</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Svg In</em>'.
     * @generated
     */
    SvgIn createSvgIn();

    /**
     * Returns a new object of class '<em>Svg In Event Single</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Svg In Event Single</em>'.
     * @generated
     */
    SvgInEventSingle createSvgInEventSingle();

    /**
     * Returns a new object of class '<em>Svg In Event If</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Svg In Event If</em>'.
     * @generated
     */
    SvgInEventIf createSvgInEventIf();

    /**
     * Returns a new object of class '<em>Svg In Event If Entry</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Svg In Event If Entry</em>'.
     * @generated
     */
    SvgInEventIfEntry createSvgInEventIfEntry();

    /**
     * Returns a new object of class '<em>Svg Copy</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Svg Copy</em>'.
     * @generated
     */
    SvgCopy createSvgCopy();

    /**
     * Returns a new object of class '<em>Svg Move</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Svg Move</em>'.
     * @generated
     */
    SvgMove createSvgMove();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    CifsvgPackage getCifsvgPackage();

} //CifsvgFactory
