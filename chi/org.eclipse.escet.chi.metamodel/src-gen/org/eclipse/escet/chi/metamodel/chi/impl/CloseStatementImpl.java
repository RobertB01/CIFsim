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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.CloseStatement;
import org.eclipse.escet.chi.metamodel.chi.Expression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Close Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.CloseStatementImpl#getHandle <em>Handle</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CloseStatementImpl extends StatementImpl implements CloseStatement
{
    /**
     * The cached value of the '{@link #getHandle() <em>Handle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
    protected Expression handle;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CloseStatementImpl()
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
        return ChiPackage.Literals.CLOSE_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getHandle()
    {
        return handle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetHandle(Expression newHandle, NotificationChain msgs)
    {
        Expression oldHandle = handle;
        handle = newHandle;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.CLOSE_STATEMENT__HANDLE, oldHandle, newHandle);
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
    public void setHandle(Expression newHandle)
    {
        if (newHandle != handle)
        {
            NotificationChain msgs = null;
            if (handle != null)
                msgs = ((InternalEObject)handle).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.CLOSE_STATEMENT__HANDLE, null, msgs);
            if (newHandle != null)
                msgs = ((InternalEObject)newHandle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.CLOSE_STATEMENT__HANDLE, null, msgs);
            msgs = basicSetHandle(newHandle, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.CLOSE_STATEMENT__HANDLE, newHandle, newHandle));
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
            case ChiPackage.CLOSE_STATEMENT__HANDLE:
                return basicSetHandle(null, msgs);
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
            case ChiPackage.CLOSE_STATEMENT__HANDLE:
                return getHandle();
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
            case ChiPackage.CLOSE_STATEMENT__HANDLE:
                setHandle((Expression)newValue);
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
            case ChiPackage.CLOSE_STATEMENT__HANDLE:
                setHandle((Expression)null);
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
            case ChiPackage.CLOSE_STATEMENT__HANDLE:
                return handle != null;
        }
        return super.eIsSet(featureID);
    }

} //CloseStatementImpl
