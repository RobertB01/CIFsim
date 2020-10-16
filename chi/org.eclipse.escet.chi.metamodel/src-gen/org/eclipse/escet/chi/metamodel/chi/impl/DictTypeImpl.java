/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dict Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.DictTypeImpl#getKeyType <em>Key Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.DictTypeImpl#getValueType <em>Value Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DictTypeImpl extends TypeImpl implements DictType
{
    /**
     * The cached value of the '{@link #getKeyType() <em>Key Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeyType()
     * @generated
     * @ordered
     */
    protected Type keyType;

    /**
     * The cached value of the '{@link #getValueType() <em>Value Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueType()
     * @generated
     * @ordered
     */
    protected Type valueType;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DictTypeImpl()
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
        return ChiPackage.Literals.DICT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getKeyType()
    {
        return keyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetKeyType(Type newKeyType, NotificationChain msgs)
    {
        Type oldKeyType = keyType;
        keyType = newKeyType;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.DICT_TYPE__KEY_TYPE, oldKeyType, newKeyType);
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
    public void setKeyType(Type newKeyType)
    {
        if (newKeyType != keyType)
        {
            NotificationChain msgs = null;
            if (keyType != null)
                msgs = ((InternalEObject)keyType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.DICT_TYPE__KEY_TYPE, null, msgs);
            if (newKeyType != null)
                msgs = ((InternalEObject)newKeyType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.DICT_TYPE__KEY_TYPE, null, msgs);
            msgs = basicSetKeyType(newKeyType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.DICT_TYPE__KEY_TYPE, newKeyType, newKeyType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getValueType()
    {
        return valueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValueType(Type newValueType, NotificationChain msgs)
    {
        Type oldValueType = valueType;
        valueType = newValueType;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.DICT_TYPE__VALUE_TYPE, oldValueType, newValueType);
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
    public void setValueType(Type newValueType)
    {
        if (newValueType != valueType)
        {
            NotificationChain msgs = null;
            if (valueType != null)
                msgs = ((InternalEObject)valueType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.DICT_TYPE__VALUE_TYPE, null, msgs);
            if (newValueType != null)
                msgs = ((InternalEObject)newValueType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.DICT_TYPE__VALUE_TYPE, null, msgs);
            msgs = basicSetValueType(newValueType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.DICT_TYPE__VALUE_TYPE, newValueType, newValueType));
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
            case ChiPackage.DICT_TYPE__KEY_TYPE:
                return basicSetKeyType(null, msgs);
            case ChiPackage.DICT_TYPE__VALUE_TYPE:
                return basicSetValueType(null, msgs);
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
            case ChiPackage.DICT_TYPE__KEY_TYPE:
                return getKeyType();
            case ChiPackage.DICT_TYPE__VALUE_TYPE:
                return getValueType();
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
            case ChiPackage.DICT_TYPE__KEY_TYPE:
                setKeyType((Type)newValue);
                return;
            case ChiPackage.DICT_TYPE__VALUE_TYPE:
                setValueType((Type)newValue);
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
            case ChiPackage.DICT_TYPE__KEY_TYPE:
                setKeyType((Type)null);
                return;
            case ChiPackage.DICT_TYPE__VALUE_TYPE:
                setValueType((Type)null);
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
            case ChiPackage.DICT_TYPE__KEY_TYPE:
                return keyType != null;
            case ChiPackage.DICT_TYPE__VALUE_TYPE:
                return valueType != null;
        }
        return super.eIsSet(featureID);
    }

} //DictTypeImpl
