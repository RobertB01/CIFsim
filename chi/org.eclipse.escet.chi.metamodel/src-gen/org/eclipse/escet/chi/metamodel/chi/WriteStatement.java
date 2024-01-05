/**
 * Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Write Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.WriteStatement#getValues <em>Values</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.WriteStatement#isAddNewline <em>Add Newline</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getWriteStatement()
 * @model
 * @generated
 */
public interface WriteStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Values</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Values</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getWriteStatement_Values()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Expression> getValues();

    /**
     * Returns the value of the '<em><b>Add Newline</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Add Newline</em>' attribute.
     * @see #setAddNewline(boolean)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getWriteStatement_AddNewline()
     * @model required="true"
     * @generated
     */
    boolean isAddNewline();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.WriteStatement#isAddNewline <em>Add Newline</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Add Newline</em>' attribute.
     * @see #isAddNewline()
     * @generated
     */
    void setAddNewline(boolean value);

} // WriteStatement
