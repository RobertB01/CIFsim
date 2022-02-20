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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.ComputeType;
import org.eclipse.escet.chi.metamodel.chi.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Compute Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ComputeTypeImpl#getParameterTypes <em>Parameter Types</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ComputeTypeImpl#getExitType <em>Exit Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ComputeTypeImpl extends TypeImpl implements ComputeType
{
    /**
     * The cached value of the '{@link #getParameterTypes() <em>Parameter Types</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterTypes()
     * @generated
     * @ordered
     */
    protected EList<Type> parameterTypes;

    /**
     * The cached value of the '{@link #getExitType() <em>Exit Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExitType()
     * @generated
     * @ordered
     */
    protected Type exitType;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ComputeTypeImpl()
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
        return ChiPackage.Literals.COMPUTE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Type> getParameterTypes()
    {
        if (parameterTypes == null)
        {
            parameterTypes = new EObjectContainmentEList<Type>(Type.class, this, ChiPackage.COMPUTE_TYPE__PARAMETER_TYPES);
        }
        return parameterTypes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getExitType()
    {
        return exitType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExitType(Type newExitType, NotificationChain msgs)
    {
        Type oldExitType = exitType;
        exitType = newExitType;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.COMPUTE_TYPE__EXIT_TYPE, oldExitType, newExitType);
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
    public void setExitType(Type newExitType)
    {
        if (newExitType != exitType)
        {
            NotificationChain msgs = null;
            if (exitType != null)
                msgs = ((InternalEObject)exitType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.COMPUTE_TYPE__EXIT_TYPE, null, msgs);
            if (newExitType != null)
                msgs = ((InternalEObject)newExitType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.COMPUTE_TYPE__EXIT_TYPE, null, msgs);
            msgs = basicSetExitType(newExitType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.COMPUTE_TYPE__EXIT_TYPE, newExitType, newExitType));
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
            case ChiPackage.COMPUTE_TYPE__PARAMETER_TYPES:
                return ((InternalEList<?>)getParameterTypes()).basicRemove(otherEnd, msgs);
            case ChiPackage.COMPUTE_TYPE__EXIT_TYPE:
                return basicSetExitType(null, msgs);
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
            case ChiPackage.COMPUTE_TYPE__PARAMETER_TYPES:
                return getParameterTypes();
            case ChiPackage.COMPUTE_TYPE__EXIT_TYPE:
                return getExitType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case ChiPackage.COMPUTE_TYPE__PARAMETER_TYPES:
                getParameterTypes().clear();
                getParameterTypes().addAll((Collection<? extends Type>)newValue);
                return;
            case ChiPackage.COMPUTE_TYPE__EXIT_TYPE:
                setExitType((Type)newValue);
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
            case ChiPackage.COMPUTE_TYPE__PARAMETER_TYPES:
                getParameterTypes().clear();
                return;
            case ChiPackage.COMPUTE_TYPE__EXIT_TYPE:
                setExitType((Type)null);
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
            case ChiPackage.COMPUTE_TYPE__PARAMETER_TYPES:
                return parameterTypes != null && !parameterTypes.isEmpty();
            case ChiPackage.COMPUTE_TYPE__EXIT_TYPE:
                return exitType != null;
        }
        return super.eIsSet(featureID);
    }

} //ComputeTypeImpl
