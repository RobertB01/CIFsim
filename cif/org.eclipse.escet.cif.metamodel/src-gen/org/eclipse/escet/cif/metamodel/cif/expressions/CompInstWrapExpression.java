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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.escet.cif.metamodel.cif.ComponentInst;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Comp Inst Wrap Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression#getReference <em>Reference</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression#getInstantiation <em>Instantiation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getCompInstWrapExpression()
 * @model
 * @generated
 */
public interface CompInstWrapExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference</em>' containment reference.
     * @see #setReference(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getCompInstWrapExpression_Reference()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getReference();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression#getReference <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' containment reference.
     * @see #getReference()
     * @generated
     */
    void setReference(Expression value);

    /**
     * Returns the value of the '<em><b>Instantiation</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Instantiation</em>' reference.
     * @see #setInstantiation(ComponentInst)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getCompInstWrapExpression_Instantiation()
     * @model required="true"
     * @generated
     */
    ComponentInst getInstantiation();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression#getInstantiation <em>Instantiation</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Instantiation</em>' reference.
     * @see #getInstantiation()
     * @generated
     */
    void setInstantiation(ComponentInst value);

} // CompInstWrapExpression
