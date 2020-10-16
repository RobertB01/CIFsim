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
 * A representation of the model object '<em><b>Slice Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getStart <em>Start</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getEnd <em>End</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getStep <em>Step</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getSource <em>Source</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getSliceExpression()
 * @model
 * @generated
 */
public interface SliceExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Start</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start</em>' containment reference.
     * @see #setStart(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getSliceExpression_Start()
     * @model containment="true"
     * @generated
     */
    Expression getStart();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getStart <em>Start</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start</em>' containment reference.
     * @see #getStart()
     * @generated
     */
    void setStart(Expression value);

    /**
     * Returns the value of the '<em><b>End</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>End</em>' containment reference.
     * @see #setEnd(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getSliceExpression_End()
     * @model containment="true"
     * @generated
     */
    Expression getEnd();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getEnd <em>End</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End</em>' containment reference.
     * @see #getEnd()
     * @generated
     */
    void setEnd(Expression value);

    /**
     * Returns the value of the '<em><b>Step</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Step</em>' containment reference.
     * @see #setStep(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getSliceExpression_Step()
     * @model containment="true"
     * @generated
     */
    Expression getStep();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getStep <em>Step</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Step</em>' containment reference.
     * @see #getStep()
     * @generated
     */
    void setStep(Expression value);

    /**
     * Returns the value of the '<em><b>Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' containment reference.
     * @see #setSource(Expression)
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getSliceExpression_Source()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getSource();

    /**
     * Sets the value of the '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getSource <em>Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' containment reference.
     * @see #getSource()
     * @generated
     */
    void setSource(Expression value);

} // SliceExpression
