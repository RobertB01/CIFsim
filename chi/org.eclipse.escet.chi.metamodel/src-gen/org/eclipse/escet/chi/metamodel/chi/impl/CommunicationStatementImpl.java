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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.CommunicationStatement;
import org.eclipse.escet.chi.metamodel.chi.Expression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Communication Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.CommunicationStatementImpl#getChannel <em>Channel</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.CommunicationStatementImpl#getData <em>Data</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class CommunicationStatementImpl extends StatementImpl implements CommunicationStatement
{
    /**
     * The cached value of the '{@link #getChannel() <em>Channel</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChannel()
     * @generated
     * @ordered
     */
    protected Expression channel;

    /**
     * The cached value of the '{@link #getData() <em>Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getData()
     * @generated
     * @ordered
     */
    protected Expression data;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CommunicationStatementImpl()
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
        return ChiPackage.Literals.COMMUNICATION_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getChannel()
    {
        return channel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetChannel(Expression newChannel, NotificationChain msgs)
    {
        Expression oldChannel = channel;
        channel = newChannel;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.COMMUNICATION_STATEMENT__CHANNEL, oldChannel, newChannel);
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
    public void setChannel(Expression newChannel)
    {
        if (newChannel != channel)
        {
            NotificationChain msgs = null;
            if (channel != null)
                msgs = ((InternalEObject)channel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.COMMUNICATION_STATEMENT__CHANNEL, null, msgs);
            if (newChannel != null)
                msgs = ((InternalEObject)newChannel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.COMMUNICATION_STATEMENT__CHANNEL, null, msgs);
            msgs = basicSetChannel(newChannel, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.COMMUNICATION_STATEMENT__CHANNEL, newChannel, newChannel));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getData()
    {
        return data;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetData(Expression newData, NotificationChain msgs)
    {
        Expression oldData = data;
        data = newData;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.COMMUNICATION_STATEMENT__DATA, oldData, newData);
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
    public void setData(Expression newData)
    {
        if (newData != data)
        {
            NotificationChain msgs = null;
            if (data != null)
                msgs = ((InternalEObject)data).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.COMMUNICATION_STATEMENT__DATA, null, msgs);
            if (newData != null)
                msgs = ((InternalEObject)newData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.COMMUNICATION_STATEMENT__DATA, null, msgs);
            msgs = basicSetData(newData, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.COMMUNICATION_STATEMENT__DATA, newData, newData));
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
            case ChiPackage.COMMUNICATION_STATEMENT__CHANNEL:
                return basicSetChannel(null, msgs);
            case ChiPackage.COMMUNICATION_STATEMENT__DATA:
                return basicSetData(null, msgs);
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
            case ChiPackage.COMMUNICATION_STATEMENT__CHANNEL:
                return getChannel();
            case ChiPackage.COMMUNICATION_STATEMENT__DATA:
                return getData();
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
            case ChiPackage.COMMUNICATION_STATEMENT__CHANNEL:
                setChannel((Expression)newValue);
                return;
            case ChiPackage.COMMUNICATION_STATEMENT__DATA:
                setData((Expression)newValue);
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
            case ChiPackage.COMMUNICATION_STATEMENT__CHANNEL:
                setChannel((Expression)null);
                return;
            case ChiPackage.COMMUNICATION_STATEMENT__DATA:
                setData((Expression)null);
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
            case ChiPackage.COMMUNICATION_STATEMENT__CHANNEL:
                return channel != null;
            case ChiPackage.COMMUNICATION_STATEMENT__DATA:
                return data != null;
        }
        return super.eIsSet(featureID);
    }

} //CommunicationStatementImpl
