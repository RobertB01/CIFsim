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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignment Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.AssignmentStatement#getLhs <em>Lhs</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.AssignmentStatement#getRhs <em>Rhs</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getAssignmentStatement()
 * @model
 * @generated
 */
public interface AssignmentStatement extends Statement
{
    /**
     * Returns the value of the '<em><b>Lhs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lhs</em>' containment reference.
     * @see #setLhs(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getAssignmentStatement_Lhs()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getLhs();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.AssignmentStatement#getLhs <em>Lhs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lhs</em>' containment reference.
     * @see #getLhs()
     * @generated
     */
    void setLhs(Expression value);

    /**
     * Returns the value of the '<em><b>Rhs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Rhs</em>' containment reference.
     * @see #setRhs(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getAssignmentStatement_Rhs()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getRhs();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.AssignmentStatement#getRhs <em>Rhs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rhs</em>' containment reference.
     * @see #getRhs()
     * @generated
     */
    void setRhs(Expression value);

} // AssignmentStatement
