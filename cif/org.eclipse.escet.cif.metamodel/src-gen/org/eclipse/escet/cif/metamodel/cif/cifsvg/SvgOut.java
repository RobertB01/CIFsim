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

import org.eclipse.escet.common.position.metamodel.position.Position;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Svg Out</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getAttr <em>Attr</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getSvgFile <em>Svg File</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getAttrTextPos <em>Attr Text Pos</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgOut()
 * @model
 * @generated
 */
public interface SvgOut extends IoDecl
{
    /**
     * Returns the value of the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' containment reference.
     * @see #setId(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgOut_Id()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getId();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getId <em>Id</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' containment reference.
     * @see #getId()
     * @generated
     */
    void setId(Expression value);

    /**
     * Returns the value of the '<em><b>Attr</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Attr</em>' attribute.
     * @see #setAttr(String)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgOut_Attr()
     * @model
     * @generated
     */
    String getAttr();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getAttr <em>Attr</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Attr</em>' attribute.
     * @see #getAttr()
     * @generated
     */
    void setAttr(String value);

    /**
     * Returns the value of the '<em><b>Svg File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Svg File</em>' containment reference.
     * @see #setSvgFile(SvgFile)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgOut_SvgFile()
     * @model containment="true"
     * @generated
     */
    SvgFile getSvgFile();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getSvgFile <em>Svg File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Svg File</em>' containment reference.
     * @see #getSvgFile()
     * @generated
     */
    void setSvgFile(SvgFile value);

    /**
     * Returns the value of the '<em><b>Attr Text Pos</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Attr Text Pos</em>' containment reference.
     * @see #setAttrTextPos(Position)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgOut_AttrTextPos()
     * @model containment="true" required="true"
     * @generated
     */
    Position getAttrTextPos();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getAttrTextPos <em>Attr Text Pos</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Attr Text Pos</em>' containment reference.
     * @see #getAttrTextPos()
     * @generated
     */
    void setAttrTextPos(Position value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage#getSvgOut_Value()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getValue();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(Expression value);

} // SvgOut
