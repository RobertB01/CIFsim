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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ConstantReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constant Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ConstantReferenceImpl#getConstant <em>Constant</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConstantReferenceImpl extends ExpressionImpl implements ConstantReference
{
    /**
     * The cached value of the '{@link #getConstant() <em>Constant</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConstant()
     * @generated
     * @ordered
     */
    protected ConstantDeclaration constant;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConstantReferenceImpl()
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
        return ChiPackage.Literals.CONSTANT_REFERENCE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstantDeclaration getConstant()
    {
        if (constant != null && constant.eIsProxy())
        {
            InternalEObject oldConstant = (InternalEObject)constant;
            constant = (ConstantDeclaration)eResolveProxy(oldConstant);
            if (constant != oldConstant)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChiPackage.CONSTANT_REFERENCE__CONSTANT, oldConstant, constant));
            }
        }
        return constant;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConstantDeclaration basicGetConstant()
    {
        return constant;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setConstant(ConstantDeclaration newConstant)
    {
        ConstantDeclaration oldConstant = constant;
        constant = newConstant;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.CONSTANT_REFERENCE__CONSTANT, oldConstant, constant));
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
            case ChiPackage.CONSTANT_REFERENCE__CONSTANT:
                if (resolve) return getConstant();
                return basicGetConstant();
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
            case ChiPackage.CONSTANT_REFERENCE__CONSTANT:
                setConstant((ConstantDeclaration)newValue);
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
            case ChiPackage.CONSTANT_REFERENCE__CONSTANT:
                setConstant((ConstantDeclaration)null);
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
            case ChiPackage.CONSTANT_REFERENCE__CONSTANT:
                return constant != null;
        }
        return super.eIsSet(featureID);
    }

} //ConstantReferenceImpl
