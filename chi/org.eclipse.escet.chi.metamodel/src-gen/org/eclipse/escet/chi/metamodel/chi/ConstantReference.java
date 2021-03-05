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
package org.eclipse.escet.chi.metamodel.chi;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constant Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.ConstantReference#getConstant <em>Constant</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getConstantReference()
 * @model
 * @generated
 */
public interface ConstantReference extends Expression
{
    /**
     * Returns the value of the '<em><b>Constant</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Constant</em>' reference.
     * @see #setConstant(ConstantDeclaration)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getConstantReference_Constant()
     * @model required="true"
     * @generated
     */
    ConstantDeclaration getConstant();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.ConstantReference#getConstant <em>Constant</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Constant</em>' reference.
     * @see #getConstant()
     * @generated
     */
    void setConstant(ConstantDeclaration value);

} // ConstantReference
