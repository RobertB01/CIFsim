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

import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Literal Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression#getLiteral <em>Literal</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getEnumLiteralExpression()
 * @model
 * @generated
 */
public interface EnumLiteralExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Literal</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Literal</em>' reference.
     * @see #setLiteral(EnumLiteral)
     * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage#getEnumLiteralExpression_Literal()
     * @model required="true"
     * @generated
     */
    EnumLiteral getLiteral();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression#getLiteral <em>Literal</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Literal</em>' reference.
     * @see #getLiteral()
     * @generated
     */
    void setLiteral(EnumLiteral value);

} // EnumLiteralExpression
