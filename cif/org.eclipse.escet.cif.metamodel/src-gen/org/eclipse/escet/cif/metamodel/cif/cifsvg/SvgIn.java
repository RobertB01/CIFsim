/**
 * Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.IoDecl;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Svg In</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getSvgFile <em>Svg File</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgIn()
 * @model
 * @generated
 */
public interface SvgIn extends IoDecl
{
    /**
     * Returns the value of the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' containment reference.
     * @see #setId(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgIn_Id()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getId();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getId <em>Id</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' containment reference.
     * @see #getId()
     * @generated
     */
    void setId(Expression value);

    /**
     * Returns the value of the '<em><b>Svg File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Svg File</em>' containment reference.
     * @see #setSvgFile(SvgFile)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgIn_SvgFile()
     * @model containment="true"
     * @generated
     */
    SvgFile getSvgFile();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getSvgFile <em>Svg File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Svg File</em>' containment reference.
     * @see #getSvgFile()
     * @generated
     */
    void setSvgFile(SvgFile value);

    /**
     * Returns the value of the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Event</em>' containment reference.
     * @see #setEvent(SvgInEvent)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgIn_Event()
     * @model containment="true" required="true"
     * @generated
     */
    SvgInEvent getEvent();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn#getEvent <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Event</em>' containment reference.
     * @see #getEvent()
     * @generated
     */
    void setEvent(SvgInEvent value);

} // SvgIn
