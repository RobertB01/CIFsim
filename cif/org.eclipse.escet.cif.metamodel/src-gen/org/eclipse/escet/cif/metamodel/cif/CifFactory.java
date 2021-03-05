/**
 * Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage
 * @generated
 */
public interface CifFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    CifFactory eINSTANCE = org.eclipse.escet.cif.metamodel.cif.impl.CifFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Group</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Group</em>'.
     * @generated
     */
    Group createGroup();

    /**
     * Returns a new object of class '<em>Component Def</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component Def</em>'.
     * @generated
     */
    ComponentDef createComponentDef();

    /**
     * Returns a new object of class '<em>Component Inst</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component Inst</em>'.
     * @generated
     */
    ComponentInst createComponentInst();

    /**
     * Returns a new object of class '<em>Specification</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Specification</em>'.
     * @generated
     */
    Specification createSpecification();

    /**
     * Returns a new object of class '<em>Event Parameter</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Event Parameter</em>'.
     * @generated
     */
    EventParameter createEventParameter();

    /**
     * Returns a new object of class '<em>Location Parameter</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Location Parameter</em>'.
     * @generated
     */
    LocationParameter createLocationParameter();

    /**
     * Returns a new object of class '<em>Alg Parameter</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Alg Parameter</em>'.
     * @generated
     */
    AlgParameter createAlgParameter();

    /**
     * Returns a new object of class '<em>Component Parameter</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component Parameter</em>'.
     * @generated
     */
    ComponentParameter createComponentParameter();

    /**
     * Returns a new object of class '<em>Equation</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Equation</em>'.
     * @generated
     */
    Equation createEquation();

    /**
     * Returns a new object of class '<em>Invariant</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Invariant</em>'.
     * @generated
     */
    Invariant createInvariant();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    CifPackage getCifPackage();

} //CifFactory
