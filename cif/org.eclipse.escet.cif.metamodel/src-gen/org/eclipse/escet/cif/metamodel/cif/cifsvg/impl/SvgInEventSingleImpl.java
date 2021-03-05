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
package org.eclipse.escet.cif.metamodel.cif.cifsvg.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Svg In Event Single</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInEventSingleImpl#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SvgInEventSingleImpl extends SvgInEventImpl implements SvgInEventSingle
{
    /**
     * The cached value of the '{@link #getEvent() <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEvent()
     * @generated
     * @ordered
     */
    protected Expression event;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SvgInEventSingleImpl()
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
        return CifsvgPackage.Literals.SVG_IN_EVENT_SINGLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getEvent()
    {
        return event;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEvent(Expression newEvent, NotificationChain msgs)
    {
        Expression oldEvent = event;
        event = newEvent;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT, oldEvent, newEvent);
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
    public void setEvent(Expression newEvent)
    {
        if (newEvent != event)
        {
            NotificationChain msgs = null;
            if (event != null)
                msgs = ((InternalEObject)event).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT, null, msgs);
            if (newEvent != null)
                msgs = ((InternalEObject)newEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT, null, msgs);
            msgs = basicSetEvent(newEvent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT, newEvent, newEvent));
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
            case CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT:
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
            case CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT:
                return getEvent();
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
            case CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT:
                setEvent((Expression)newValue);
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
            case CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT:
                setEvent((Expression)null);
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
            case CifsvgPackage.SVG_IN_EVENT_SINGLE__EVENT:
                return event != null;
        }
        return super.eIsSet(featureID);
    }

} //SvgInEventSingleImpl
