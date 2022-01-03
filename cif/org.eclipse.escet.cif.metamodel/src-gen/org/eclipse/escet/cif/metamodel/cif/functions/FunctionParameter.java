/**
 * Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter#getParameter <em>Parameter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getFunctionParameter()
 * @model
 * @generated
 */
public interface FunctionParameter extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter</em>' containment reference.
     * @see #setParameter(DiscVariable)
     * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage#getFunctionParameter_Parameter()
     * @model containment="true" required="true"
     * @generated
     */
    DiscVariable getParameter();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter#getParameter <em>Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter</em>' containment reference.
     * @see #getParameter()
     * @generated
     */
    void setParameter(DiscVariable value);

} // FunctionParameter
