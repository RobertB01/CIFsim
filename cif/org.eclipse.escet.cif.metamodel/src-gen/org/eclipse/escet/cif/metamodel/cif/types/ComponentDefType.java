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
package org.eclipse.escet.cif.metamodel.cif.types;

import org.eclipse.escet.cif.metamodel.cif.ComponentDef;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Def Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType#getDefinition <em>Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getComponentDefType()
 * @model
 * @generated
 */
public interface ComponentDefType extends CifType
{
    /**
     * Returns the value of the '<em><b>Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definition</em>' reference.
     * @see #setDefinition(ComponentDef)
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#getComponentDefType_Definition()
     * @model required="true"
     * @generated
     */
    ComponentDef getDefinition();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType#getDefinition <em>Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Definition</em>' reference.
     * @see #getDefinition()
     * @generated
     */
    void setDefinition(ComponentDef value);

} // ComponentDefType
