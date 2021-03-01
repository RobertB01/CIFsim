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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.FunctionReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionReferenceImpl#getFunction <em>Function</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FunctionReferenceImpl extends BaseFunctionReferenceImpl implements FunctionReference
{
    /**
     * The cached value of the '{@link #getFunction() <em>Function</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFunction()
     * @generated
     * @ordered
     */
    protected FunctionDeclaration function;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FunctionReferenceImpl()
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
        return ChiPackage.Literals.FUNCTION_REFERENCE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FunctionDeclaration getFunction()
    {
        if (function != null && function.eIsProxy())
        {
            InternalEObject oldFunction = (InternalEObject)function;
            function = (FunctionDeclaration)eResolveProxy(oldFunction);
            if (function != oldFunction)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChiPackage.FUNCTION_REFERENCE__FUNCTION, oldFunction, function));
            }
        }
        return function;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FunctionDeclaration basicGetFunction()
    {
        return function;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setFunction(FunctionDeclaration newFunction)
    {
        FunctionDeclaration oldFunction = function;
        function = newFunction;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.FUNCTION_REFERENCE__FUNCTION, oldFunction, function));
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
            case ChiPackage.FUNCTION_REFERENCE__FUNCTION:
                if (resolve) return getFunction();
                return basicGetFunction();
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
            case ChiPackage.FUNCTION_REFERENCE__FUNCTION:
                setFunction((FunctionDeclaration)newValue);
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
            case ChiPackage.FUNCTION_REFERENCE__FUNCTION:
                setFunction((FunctionDeclaration)null);
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
            case ChiPackage.FUNCTION_REFERENCE__FUNCTION:
                return function != null;
        }
        return super.eIsSet(featureID);
    }

} //FunctionReferenceImpl
