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
package org.eclipse.escet.cif.metamodel.cif.types.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;

import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.types.impl.EnumTypeImpl#getEnum <em>Enum</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnumTypeImpl extends CifTypeImpl implements EnumType
{
    /**
     * The cached value of the '{@link #getEnum() <em>Enum</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEnum()
     * @generated
     * @ordered
     */
    protected EnumDecl enum_;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EnumTypeImpl()
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
        return TypesPackage.Literals.ENUM_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EnumDecl getEnum()
    {
        if (enum_ != null && enum_.eIsProxy())
        {
            InternalEObject oldEnum = (InternalEObject)enum_;
            enum_ = (EnumDecl)eResolveProxy(oldEnum);
            if (enum_ != oldEnum)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, TypesPackage.ENUM_TYPE__ENUM, oldEnum, enum_));
            }
        }
        return enum_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EnumDecl basicGetEnum()
    {
        return enum_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setEnum(EnumDecl newEnum)
    {
        EnumDecl oldEnum = enum_;
        enum_ = newEnum;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TypesPackage.ENUM_TYPE__ENUM, oldEnum, enum_));
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
            case TypesPackage.ENUM_TYPE__ENUM:
                if (resolve) return getEnum();
                return basicGetEnum();
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
            case TypesPackage.ENUM_TYPE__ENUM:
                setEnum((EnumDecl)newValue);
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
            case TypesPackage.ENUM_TYPE__ENUM:
                setEnum((EnumDecl)null);
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
            case TypesPackage.ENUM_TYPE__ENUM:
                return enum_ != null;
        }
        return super.eIsSet(featureID);
    }

} //EnumTypeImpl
