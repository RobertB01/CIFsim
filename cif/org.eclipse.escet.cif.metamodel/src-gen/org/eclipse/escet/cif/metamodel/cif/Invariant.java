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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Invariant</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getSupKind <em>Sup Kind</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getPredicate <em>Predicate</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getInvKind <em>Inv Kind</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getInvariant()
 * @model
 * @generated
 */
public interface Invariant extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getInvariant_Name()
     * @model dataType="org.eclipse.escet.cif.metamodel.cif.CifIdentifier"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Sup Kind</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.cif.metamodel.cif.SupKind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sup Kind</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.SupKind
     * @see #setSupKind(SupKind)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getInvariant_SupKind()
     * @model required="true"
     * @generated
     */
    SupKind getSupKind();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getSupKind <em>Sup Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sup Kind</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.SupKind
     * @see #getSupKind()
     * @generated
     */
    void setSupKind(SupKind value);

    /**
     * Returns the value of the '<em><b>Predicate</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Predicate</em>' containment reference.
     * @see #setPredicate(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getInvariant_Predicate()
     * @model containment="true" required="true"
     * @generated
     */
    Expression getPredicate();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getPredicate <em>Predicate</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Predicate</em>' containment reference.
     * @see #getPredicate()
     * @generated
     */
    void setPredicate(Expression value);

    /**
     * Returns the value of the '<em><b>Inv Kind</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.cif.metamodel.cif.InvKind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Inv Kind</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.InvKind
     * @see #setInvKind(InvKind)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getInvariant_InvKind()
     * @model required="true"
     * @generated
     */
    InvKind getInvKind();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getInvKind <em>Inv Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Inv Kind</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.InvKind
     * @see #getInvKind()
     * @generated
     */
    void setInvKind(InvKind value);

    /**
     * Returns the value of the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Event</em>' containment reference.
     * @see #setEvent(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getInvariant_Event()
     * @model containment="true"
     * @generated
     */
    Expression getEvent();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.Invariant#getEvent <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Event</em>' containment reference.
     * @see #getEvent()
     * @generated
     */
    void setEvent(Expression value);

} // Invariant
