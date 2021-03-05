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
package org.eclipse.escet.cif.metamodel.cif.print;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>For</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFor#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFor#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrintFor()
 * @model
 * @generated
 */
public interface PrintFor extends PositionObject
{
    /**
     * Returns the value of the '<em><b>Kind</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.escet.cif.metamodel.cif.print.PrintForKind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintForKind
     * @see #setKind(PrintForKind)
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrintFor_Kind()
     * @model required="true"
     * @generated
     */
    PrintForKind getKind();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFor#getKind <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintForKind
     * @see #getKind()
     * @generated
     */
    void setKind(PrintForKind value);

    /**
     * Returns the value of the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Event</em>' containment reference.
     * @see #setEvent(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrintFor_Event()
     * @model containment="true"
     * @generated
     */
    Expression getEvent();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.print.PrintFor#getEvent <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Event</em>' containment reference.
     * @see #getEvent()
     * @generated
     */
    void setEvent(Expression value);

} // PrintFor
