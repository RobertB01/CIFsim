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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Group#getDefinitions <em>Definitions</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Group#getComponents <em>Components</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getGroup()
 * @model
 * @generated
 */
public interface Group extends ComplexComponent
{
    /**
     * Returns the value of the '<em><b>Definitions</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.ComponentDef}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definitions</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getGroup_Definitions()
     * @model containment="true"
     * @generated
     */
    EList<ComponentDef> getDefinitions();

    /**
     * Returns the value of the '<em><b>Components</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.Component}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Components</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getGroup_Components()
     * @model containment="true"
     * @generated
     */
    EList<Component> getComponents();

} // Group
