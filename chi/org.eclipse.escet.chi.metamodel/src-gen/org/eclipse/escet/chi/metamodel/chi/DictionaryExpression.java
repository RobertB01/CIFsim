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
 * A representation of the model object '<em><b>Dictionary Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.DictionaryExpression#getPairs <em>Pairs</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDictionaryExpression()
 * @model
 * @generated
 */
public interface DictionaryExpression extends Expression
{
    /**
     * Returns the value of the '<em><b>Pairs</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.chi.metamodel.chi.DictionaryPair}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pairs</em>' containment reference list.
     * @see org.eclipse.escet.chi.metamodel.chi.ChiPackage#getDictionaryExpression_Pairs()
     * @model containment="true"
     * @generated
     */
    EList<DictionaryPair> getPairs();

} // DictionaryExpression
