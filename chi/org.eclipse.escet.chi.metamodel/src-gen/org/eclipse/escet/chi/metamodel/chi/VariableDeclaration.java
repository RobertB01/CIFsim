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
package org.eclipse.escet.chi.metamodel.chi;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getInitialValue <em>Initial Value</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#isParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getVariableDeclaration()
 * @model
 * @generated
 */
public interface VariableDeclaration extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Initial Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Initial Value</em>' containment reference.
     * @see #setInitialValue(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getVariableDeclaration_InitialValue()
     * @model containment="true"
     * @generated
     */
    Expression getInitialValue();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getInitialValue <em>Initial Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Initial Value</em>' containment reference.
     * @see #getInitialValue()
     * @generated
     */
    void setInitialValue(Expression value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(Type)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getVariableDeclaration_Type()
     * @model containment="true"
     * @generated
     */
    Type getType();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getType <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' containment reference.
     * @see #getType()
     * @generated
     */
    void setType(Type value);

    /**
     * Returns the value of the '<em><b>Parameter</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter</em>' attribute.
     * @see #setParameter(boolean)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getVariableDeclaration_Parameter()
     * @model required="true"
     * @generated
     */
    boolean isParameter();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#isParameter <em>Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter</em>' attribute.
     * @see #isParameter()
     * @generated
     */
    void setParameter(boolean value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getVariableDeclaration_Name()
     * @model dataType="org.eclipse.escet.chi.metamodel.chi.ChiIdentifier" required="true"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // VariableDeclaration
