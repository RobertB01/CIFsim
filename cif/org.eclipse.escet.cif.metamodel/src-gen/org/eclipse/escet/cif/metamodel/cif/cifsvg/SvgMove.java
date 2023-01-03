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
package org.eclipse.escet.cif.metamodel.cif.cifsvg;

import org.eclipse.escet.cif.metamodel.cif.IoDecl;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Svg Move</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getX <em>X</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getY <em>Y</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getSvgFile <em>Svg File</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgMove()
 * @model
 * @generated
 */
public interface SvgMove extends IoDecl
{
    /**
     * Returns the value of the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' containment reference.
     * @see #setId(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgMove_Id()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getId();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getId <em>Id</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' containment reference.
     * @see #getId()
     * @generated
     */
    void setId(Expression value);

    /**
     * Returns the value of the '<em><b>X</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>X</em>' containment reference.
     * @see #setX(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgMove_X()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getX();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getX <em>X</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>X</em>' containment reference.
     * @see #getX()
     * @generated
     */
    void setX(Expression value);

    /**
     * Returns the value of the '<em><b>Y</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Y</em>' containment reference.
     * @see #setY(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgMove_Y()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getY();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getY <em>Y</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Y</em>' containment reference.
     * @see #getY()
     * @generated
     */
    void setY(Expression value);

    /**
     * Returns the value of the '<em><b>Svg File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Svg File</em>' containment reference.
     * @see #setSvgFile(SvgFile)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgMove_SvgFile()
     * @model containment="true"
     * @generated
     */
    SvgFile getSvgFile();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove#getSvgFile <em>Svg File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Svg File</em>' containment reference.
     * @see #getSvgFile()
     * @generated
     */
    void setSvgFile(SvgFile value);

} // SvgMove
