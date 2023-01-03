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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChannelOps;
import org.eclipse.escet.chi.metamodel.chi.ChannelType;
import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Channel Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ChannelTypeImpl#getElementType <em>Element Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ChannelTypeImpl#getOps <em>Ops</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChannelTypeImpl extends TypeImpl implements ChannelType
{
    /**
     * The cached value of the '{@link #getElementType() <em>Element Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementType()
     * @generated
     * @ordered
     */
    protected Type elementType;

    /**
     * The default value of the '{@link #getOps() <em>Ops</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOps()
     * @generated
     * @ordered
     */
    protected static final ChannelOps OPS_EDEFAULT = ChannelOps.RECEIVE;

    /**
     * The cached value of the '{@link #getOps() <em>Ops</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOps()
     * @generated
     * @ordered
     */
    protected ChannelOps ops = OPS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ChannelTypeImpl()
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
        return ChiPackage.Literals.CHANNEL_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getElementType()
    {
        return elementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetElementType(Type newElementType, NotificationChain msgs)
    {
        Type oldElementType = elementType;
        elementType = newElementType;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE, oldElementType, newElementType);
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
    public void setElementType(Type newElementType)
    {
        if (newElementType != elementType)
        {
            NotificationChain msgs = null;
            if (elementType != null)
                msgs = ((InternalEObject)elementType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE, null, msgs);
            if (newElementType != null)
                msgs = ((InternalEObject)newElementType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE, null, msgs);
            msgs = basicSetElementType(newElementType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE, newElementType, newElementType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ChannelOps getOps()
    {
        return ops;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setOps(ChannelOps newOps)
    {
        ChannelOps oldOps = ops;
        ops = newOps == null ? OPS_EDEFAULT : newOps;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.CHANNEL_TYPE__OPS, oldOps, ops));
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
            case ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE:
                return basicSetElementType(null, msgs);
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
            case ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE:
                return getElementType();
            case ChiPackage.CHANNEL_TYPE__OPS:
                return getOps();
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
            case ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE:
                setElementType((Type)newValue);
                return;
            case ChiPackage.CHANNEL_TYPE__OPS:
                setOps((ChannelOps)newValue);
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
            case ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE:
                setElementType((Type)null);
                return;
            case ChiPackage.CHANNEL_TYPE__OPS:
                setOps(OPS_EDEFAULT);
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
            case ChiPackage.CHANNEL_TYPE__ELEMENT_TYPE:
                return elementType != null;
            case ChiPackage.CHANNEL_TYPE__OPS:
                return ops != OPS_EDEFAULT;
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
        result.append(" (ops: ");
        result.append(ops);
        result.append(')');
        return result.toString();
    }

} //ChannelTypeImpl
