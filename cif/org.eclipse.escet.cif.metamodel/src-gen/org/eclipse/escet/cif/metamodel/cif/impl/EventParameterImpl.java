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
package org.eclipse.escet.cif.metamodel.cif.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;

import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.EventParameterImpl#getEvent <em>Event</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.EventParameterImpl#isSendFlag <em>Send Flag</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.EventParameterImpl#isRecvFlag <em>Recv Flag</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.EventParameterImpl#isSyncFlag <em>Sync Flag</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EventParameterImpl extends ParameterImpl implements EventParameter
{
    /**
     * The cached value of the '{@link #getEvent() <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEvent()
     * @generated
     * @ordered
     */
    protected Event event;

    /**
     * The default value of the '{@link #isSendFlag() <em>Send Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSendFlag()
     * @generated
     * @ordered
     */
    protected static final boolean SEND_FLAG_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSendFlag() <em>Send Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSendFlag()
     * @generated
     * @ordered
     */
    protected boolean sendFlag = SEND_FLAG_EDEFAULT;

    /**
     * The default value of the '{@link #isRecvFlag() <em>Recv Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isRecvFlag()
     * @generated
     * @ordered
     */
    protected static final boolean RECV_FLAG_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isRecvFlag() <em>Recv Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isRecvFlag()
     * @generated
     * @ordered
     */
    protected boolean recvFlag = RECV_FLAG_EDEFAULT;

    /**
     * The default value of the '{@link #isSyncFlag() <em>Sync Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSyncFlag()
     * @generated
     * @ordered
     */
    protected static final boolean SYNC_FLAG_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSyncFlag() <em>Sync Flag</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSyncFlag()
     * @generated
     * @ordered
     */
    protected boolean syncFlag = SYNC_FLAG_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EventParameterImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass()
    {
        return CifPackage.Literals.EVENT_PARAMETER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Event getEvent()
    {
        return event;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEvent(Event newEvent, NotificationChain msgs)
    {
        Event oldEvent = event;
        event = newEvent;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifPackage.EVENT_PARAMETER__EVENT, oldEvent, newEvent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setEvent(Event newEvent)
    {
        if (newEvent != event)
        {
            NotificationChain msgs = null;
            if (event != null)
                msgs = ((InternalEObject)event).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifPackage.EVENT_PARAMETER__EVENT, null, msgs);
            if (newEvent != null)
                msgs = ((InternalEObject)newEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifPackage.EVENT_PARAMETER__EVENT, null, msgs);
            msgs = basicSetEvent(newEvent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifPackage.EVENT_PARAMETER__EVENT, newEvent, newEvent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isSendFlag()
    {
        return sendFlag;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSendFlag(boolean newSendFlag)
    {
        boolean oldSendFlag = sendFlag;
        sendFlag = newSendFlag;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifPackage.EVENT_PARAMETER__SEND_FLAG, oldSendFlag, sendFlag));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isRecvFlag()
    {
        return recvFlag;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setRecvFlag(boolean newRecvFlag)
    {
        boolean oldRecvFlag = recvFlag;
        recvFlag = newRecvFlag;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifPackage.EVENT_PARAMETER__RECV_FLAG, oldRecvFlag, recvFlag));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isSyncFlag()
    {
        return syncFlag;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSyncFlag(boolean newSyncFlag)
    {
        boolean oldSyncFlag = syncFlag;
        syncFlag = newSyncFlag;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifPackage.EVENT_PARAMETER__SYNC_FLAG, oldSyncFlag, syncFlag));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
    {
        switch (featureID)
        {
            case CifPackage.EVENT_PARAMETER__EVENT:
                return basicSetEvent(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType)
    {
        switch (featureID)
        {
            case CifPackage.EVENT_PARAMETER__EVENT:
                return getEvent();
            case CifPackage.EVENT_PARAMETER__SEND_FLAG:
                return isSendFlag();
            case CifPackage.EVENT_PARAMETER__RECV_FLAG:
                return isRecvFlag();
            case CifPackage.EVENT_PARAMETER__SYNC_FLAG:
                return isSyncFlag();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case CifPackage.EVENT_PARAMETER__EVENT:
                setEvent((Event)newValue);
                return;
            case CifPackage.EVENT_PARAMETER__SEND_FLAG:
                setSendFlag((Boolean)newValue);
                return;
            case CifPackage.EVENT_PARAMETER__RECV_FLAG:
                setRecvFlag((Boolean)newValue);
                return;
            case CifPackage.EVENT_PARAMETER__SYNC_FLAG:
                setSyncFlag((Boolean)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID)
    {
        switch (featureID)
        {
            case CifPackage.EVENT_PARAMETER__EVENT:
                setEvent((Event)null);
                return;
            case CifPackage.EVENT_PARAMETER__SEND_FLAG:
                setSendFlag(SEND_FLAG_EDEFAULT);
                return;
            case CifPackage.EVENT_PARAMETER__RECV_FLAG:
                setRecvFlag(RECV_FLAG_EDEFAULT);
                return;
            case CifPackage.EVENT_PARAMETER__SYNC_FLAG:
                setSyncFlag(SYNC_FLAG_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID)
    {
        switch (featureID)
        {
            case CifPackage.EVENT_PARAMETER__EVENT:
                return event != null;
            case CifPackage.EVENT_PARAMETER__SEND_FLAG:
                return sendFlag != SEND_FLAG_EDEFAULT;
            case CifPackage.EVENT_PARAMETER__RECV_FLAG:
                return recvFlag != RECV_FLAG_EDEFAULT;
            case CifPackage.EVENT_PARAMETER__SYNC_FLAG:
                return syncFlag != SYNC_FLAG_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (sendFlag: ");
        result.append(sendFlag);
        result.append(", recvFlag: ");
        result.append(recvFlag);
        result.append(", syncFlag: ");
        result.append(syncFlag);
        result.append(')');
        return result.toString();
    }

} //EventParameterImpl
