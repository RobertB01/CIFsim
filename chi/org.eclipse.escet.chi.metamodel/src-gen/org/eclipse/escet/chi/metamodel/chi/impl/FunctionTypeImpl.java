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
import org.eclipse.escet.chi.metamodel.chi.FunctionType;
import org.eclipse.escet.chi.metamodel.chi.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionTypeImpl#getResultType <em>Result Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionTypeImpl#getParameterTypes <em>Parameter Types</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FunctionTypeImpl extends TypeImpl implements FunctionType
{
    /**
     * The cached value of the '{@link #getResultType() <em>Result Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResultType()
     * @generated
     * @ordered
     */
    protected Type resultType;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FunctionTypeImpl()
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
        return ChiPackage.Literals.FUNCTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getResultType()
    {
        return resultType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetResultType(Type newResultType, NotificationChain msgs)
    {
        Type oldResultType = resultType;
        resultType = newResultType;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.FUNCTION_TYPE__RESULT_TYPE, oldResultType, newResultType);
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
    public void setResultType(Type newResultType)
    {
        if (newResultType != resultType)
        {
            NotificationChain msgs = null;
            if (resultType != null)
                msgs = ((InternalEObject)resultType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.FUNCTION_TYPE__RESULT_TYPE, null, msgs);
            if (newResultType != null)
                msgs = ((InternalEObject)newResultType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.FUNCTION_TYPE__RESULT_TYPE, null, msgs);
            msgs = basicSetResultType(newResultType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.FUNCTION_TYPE__RESULT_TYPE, newResultType, newResultType));
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
            parameterTypes = new EObjectContainmentEList<Type>(Type.class, this, ChiPackage.FUNCTION_TYPE__PARAMETER_TYPES);
        }
        return parameterTypes;
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
            case ChiPackage.FUNCTION_TYPE__RESULT_TYPE:
                return basicSetResultType(null, msgs);
            case ChiPackage.FUNCTION_TYPE__PARAMETER_TYPES:
                return ((InternalEList<?>)getParameterTypes()).basicRemove(otherEnd, msgs);
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
            case ChiPackage.FUNCTION_TYPE__RESULT_TYPE:
                return getResultType();
            case ChiPackage.FUNCTION_TYPE__PARAMETER_TYPES:
                return getParameterTypes();
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
            case ChiPackage.FUNCTION_TYPE__RESULT_TYPE:
                setResultType((Type)newValue);
                return;
            case ChiPackage.FUNCTION_TYPE__PARAMETER_TYPES:
                getParameterTypes().clear();
                getParameterTypes().addAll((Collection<? extends Type>)newValue);
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
            case ChiPackage.FUNCTION_TYPE__RESULT_TYPE:
                setResultType((Type)null);
                return;
            case ChiPackage.FUNCTION_TYPE__PARAMETER_TYPES:
                getParameterTypes().clear();
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
            case ChiPackage.FUNCTION_TYPE__RESULT_TYPE:
                return resultType != null;
            case ChiPackage.FUNCTION_TYPE__PARAMETER_TYPES:
                return parameterTypes != null && !parameterTypes.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //FunctionTypeImpl
