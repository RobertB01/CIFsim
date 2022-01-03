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
package org.eclipse.escet.cif.metamodel.cif.declarations;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage
 * @generated
 */
public interface DeclarationsFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    DeclarationsFactory eINSTANCE = org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Alg Variable</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Alg Variable</em>'.
     * @generated
     */
    AlgVariable createAlgVariable();

    /**
     * Returns a new object of class '<em>Event</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Event</em>'.
     * @generated
     */
    Event createEvent();

    /**
     * Returns a new object of class '<em>Enum Decl</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Enum Decl</em>'.
     * @generated
     */
    EnumDecl createEnumDecl();

    /**
     * Returns a new object of class '<em>Type Decl</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Type Decl</em>'.
     * @generated
     */
    TypeDecl createTypeDecl();

    /**
     * Returns a new object of class '<em>Enum Literal</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Enum Literal</em>'.
     * @generated
     */
    EnumLiteral createEnumLiteral();

    /**
     * Returns a new object of class '<em>Disc Variable</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Disc Variable</em>'.
     * @generated
     */
    DiscVariable createDiscVariable();

    /**
     * Returns a new object of class '<em>Variable Value</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Variable Value</em>'.
     * @generated
     */
    VariableValue createVariableValue();

    /**
     * Returns a new object of class '<em>Constant</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Constant</em>'.
     * @generated
     */
    Constant createConstant();

    /**
     * Returns a new object of class '<em>Cont Variable</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Cont Variable</em>'.
     * @generated
     */
    ContVariable createContVariable();

    /**
     * Returns a new object of class '<em>Input Variable</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Input Variable</em>'.
     * @generated
     */
    InputVariable createInputVariable();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    DeclarationsPackage getDeclarationsPackage();

} //DeclarationsFactory
