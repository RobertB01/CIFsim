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
package org.eclipse.escet.cif.metamodel.cif.functions;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignment Func Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement#getAddressable <em>Addressable</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getAssignmentFuncStatement()
 * @model
 * @generated
 */
public interface AssignmentFuncStatement extends FunctionStatement
{
    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getAssignmentFuncStatement_Value()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getValue();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(Expression value);

    /**
     * Returns the value of the '<em><b>Addressable</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Addressable</em>' containment reference.
     * @see #setAddressable(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getAssignmentFuncStatement_Addressable()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getAddressable();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement#getAddressable <em>Addressable</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Addressable</em>' containment reference.
     * @see #getAddressable()
     * @generated
     */
    void setAddressable(Expression value);

} // AssignmentFuncStatement
