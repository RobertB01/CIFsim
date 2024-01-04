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
package org.eclipse.escet.common.position.metamodel.position.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class PositionObjectImpl extends MinimalEObjectImpl.Container implements PositionObject
{
    /**
     * The cached value of the '{@link #getPosition() <em>Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPosition()
     * @generated
     * @ordered
     */
    protected Position position;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PositionObjectImpl()
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
        return PositionPackage.Literals.POSITION_OBJECT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Position getPosition()
    {
        return position;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPosition(Position newPosition, NotificationChain msgs)
    {
        Position oldPosition = position;
        position = newPosition;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION_OBJECT__POSITION, oldPosition, newPosition);
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
    public void setPosition(Position newPosition)
    {
        if (newPosition != position)
        {
            NotificationChain msgs = null;
            if (position != null)
                msgs = ((InternalEObject)position).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PositionPackage.POSITION_OBJECT__POSITION, null, msgs);
            if (newPosition != null)
                msgs = ((InternalEObject)newPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PositionPackage.POSITION_OBJECT__POSITION, null, msgs);
            msgs = basicSetPosition(newPosition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION_OBJECT__POSITION, newPosition, newPosition));
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
            case PositionPackage.POSITION_OBJECT__POSITION:
                return basicSetPosition(null, msgs);
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
            case PositionPackage.POSITION_OBJECT__POSITION:
                return getPosition();
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
            case PositionPackage.POSITION_OBJECT__POSITION:
                setPosition((Position)newValue);
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
            case PositionPackage.POSITION_OBJECT__POSITION:
                setPosition((Position)null);
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
            case PositionPackage.POSITION_OBJECT__POSITION:
                return position != null;
        }
        return super.eIsSet(featureID);
    }

} //PositionObjectImpl
