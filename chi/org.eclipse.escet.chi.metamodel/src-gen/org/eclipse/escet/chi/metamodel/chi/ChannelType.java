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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Channel Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ChannelType#getElementType <em>Element Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ChannelType#getOps <em>Ops</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getChannelType()
 * @model
 * @generated
 */
public interface ChannelType extends Type
{
    /**
     * Returns the value of the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Type</em>' containment reference.
     * @see #setElementType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getChannelType_ElementType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getElementType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ChannelType#getElementType <em>Element Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Type</em>' containment reference.
     * @see #getElementType()
     * @generated
     */
    void setElementType(Type value);

    /**
     * Returns the value of the '<em><b>Ops</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.chi.metamodel.chi.ChannelOps}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ops</em>' attribute.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelOps
     * @see #setOps(ChannelOps)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getChannelType_Ops()
     * @model required="true"
     * @generated
     */
    ChannelOps getOps();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ChannelType#getOps <em>Ops</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ops</em>' attribute.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelOps
     * @see #getOps()
     * @generated
     */
    void setOps(ChannelOps value);

} // ChannelType
