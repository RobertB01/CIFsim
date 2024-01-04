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
package org.eclipse.escet.cif.metamodel.cif.expressions;

import org.eclipse.escet.cif.metamodel.cif.types.Field;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Field Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression#getField <em>Field</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getFieldExpression()
 * @model
 * @generated
 */
public interface FieldExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Field</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Field</em>' reference.
     * @see #setField(Field)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getFieldExpression_Field()
     * @model required="true"
     * @generated
     */
    Field getField();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression#getField <em>Field</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Field</em>' reference.
     * @see #getField()
     * @generated
     */
    void setField(Field value);

} // FieldExpression
