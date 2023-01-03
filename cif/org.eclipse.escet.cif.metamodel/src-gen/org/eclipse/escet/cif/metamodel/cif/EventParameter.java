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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#getEvent <em>Event</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isSendFlag <em>Send Flag</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isRecvFlag <em>Recv Flag</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isSyncFlag <em>Sync Flag</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEventParameter()
 * @model
 * @generated
 */
public interface EventParameter extends Parameter
{
    /**
     * Returns the value of the '<em><b>Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Event</em>' containment reference.
     * @see #setEvent(Event)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEventParameter_Event()
     * @model containment="true" required="true"
     * @generated
     */
    Event getEvent();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#getEvent <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Event</em>' containment reference.
     * @see #getEvent()
     * @generated
     */
    void setEvent(Event value);

    /**
     * Returns the value of the '<em><b>Send Flag</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Send Flag</em>' attribute.
     * @see #setSendFlag(boolean)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEventParameter_SendFlag()
     * @model required="true"
     * @generated
     */
    boolean isSendFlag();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isSendFlag <em>Send Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Send Flag</em>' attribute.
     * @see #isSendFlag()
     * @generated
     */
    void setSendFlag(boolean value);

    /**
     * Returns the value of the '<em><b>Recv Flag</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Recv Flag</em>' attribute.
     * @see #setRecvFlag(boolean)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEventParameter_RecvFlag()
     * @model required="true"
     * @generated
     */
    boolean isRecvFlag();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isRecvFlag <em>Recv Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Recv Flag</em>' attribute.
     * @see #isRecvFlag()
     * @generated
     */
    void setRecvFlag(boolean value);

    /**
     * Returns the value of the '<em><b>Sync Flag</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sync Flag</em>' attribute.
     * @see #setSyncFlag(boolean)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getEventParameter_SyncFlag()
     * @model required="true"
     * @generated
     */
    boolean isSyncFlag();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.EventParameter#isSyncFlag <em>Sync Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sync Flag</em>' attribute.
     * @see #isSyncFlag()
     * @generated
     */
    void setSyncFlag(boolean value);

} // EventParameter
