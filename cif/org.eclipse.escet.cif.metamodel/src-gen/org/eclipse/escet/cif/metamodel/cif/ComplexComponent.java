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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Complex Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getDeclarations <em>Declarations</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getMarkeds <em>Markeds</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getInvariants <em>Invariants</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getInitials <em>Initials</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getEquations <em>Equations</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.ComplexComponent#getIoDecls <em>Io Decls</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComplexComponent()
 * @model abstract="true"
 * @generated
 */
public interface ComplexComponent extends Component
{
    /**
     * Returns the value of the '<em><b>Declarations</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.declarations.Declaration}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Declarations</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComplexComponent_Declarations()
     * @model containment="true"
     * @generated
     */
    EList<Declaration> getDeclarations();

    /**
     * Returns the value of the '<em><b>Markeds</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Markeds</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComplexComponent_Markeds()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getMarkeds();

    /**
     * Returns the value of the '<em><b>Invariants</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.Invariant}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Invariants</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComplexComponent_Invariants()
     * @model containment="true"
     * @generated
     */
    EList<Invariant> getInvariants();

    /**
     * Returns the value of the '<em><b>Initials</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.expressions.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Initials</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComplexComponent_Initials()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getInitials();

    /**
     * Returns the value of the '<em><b>Equations</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.Equation}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Equations</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComplexComponent_Equations()
     * @model containment="true"
     * @generated
     */
    EList<Equation> getEquations();

    /**
     * Returns the value of the '<em><b>Io Decls</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.IoDecl}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Io Decls</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getComplexComponent_IoDecls()
     * @model containment="true"
     * @generated
     */
    EList<IoDecl> getIoDecls();

} // ComplexComponent
