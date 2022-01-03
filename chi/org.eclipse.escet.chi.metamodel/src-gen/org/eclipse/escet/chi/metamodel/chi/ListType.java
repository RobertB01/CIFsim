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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ListType#getElementType <em>Element Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ListType#getInitialLength <em>Initial Length</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getListType()
 * @model
 * @generated
 */
public interface ListType extends Type
{
    /**
     * Returns the value of the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Type</em>' containment reference.
     * @see #setElementType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getListType_ElementType()
     * @model containment="true" required="true"
     * @generated
     */
    Type getElementType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ListType#getElementType <em>Element Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Type</em>' containment reference.
     * @see #getElementType()
     * @generated
     */
    void setElementType(Type value);

    /**
     * Returns the value of the '<em><b>Initial Length</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Initial Length</em>' containment reference.
     * @see #setInitialLength(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getListType_InitialLength()
     * @model containment="true"
     * @generated
     */
    Expression getInitialLength();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ListType#getInitialLength <em>Initial Length</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Initial Length</em>' containment reference.
     * @see #getInitialLength()
     * @generated
     */
    void setInitialLength(Expression value);

} // ListType
