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
 * A representation of the model object '<em><b>Matrix Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.MatrixType#getRowSize <em>Row Size</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.MatrixType#getColumnSize <em>Column Size</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getMatrixType()
 * @model
 * @generated
 */
public interface MatrixType extends Type
{
    /**
     * Returns the value of the '<em><b>Row Size</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Row Size</em>' containment reference.
     * @see #setRowSize(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getMatrixType_RowSize()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getRowSize();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.MatrixType#getRowSize <em>Row Size</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Row Size</em>' containment reference.
     * @see #getRowSize()
     * @generated
     */
    void setRowSize(Expression value);

    /**
     * Returns the value of the '<em><b>Column Size</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Column Size</em>' containment reference.
     * @see #setColumnSize(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getMatrixType_ColumnSize()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getColumnSize();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.MatrixType#getColumnSize <em>Column Size</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Column Size</em>' containment reference.
     * @see #getColumnSize()
     * @generated
     */
    void setColumnSize(Expression value);

} // MatrixType
