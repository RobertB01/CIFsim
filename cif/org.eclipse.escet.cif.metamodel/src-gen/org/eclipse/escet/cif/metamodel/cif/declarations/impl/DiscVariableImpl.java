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
package org.eclipse.escet.cif.metamodel.cif.declarations.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Disc Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.DiscVariableImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.declarations.impl.DiscVariableImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiscVariableImpl extends DeclarationImpl implements DiscVariable
{
    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected CifType type;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected VariableValue value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DiscVariableImpl()
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
        return DeclarationsPackage.Literals.DISC_VARIABLE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CifType getType()
    {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetType(CifType newType, NotificationChain msgs)
    {
        CifType oldType = type;
        type = newType;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DeclarationsPackage.DISC_VARIABLE__TYPE, oldType, newType);
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
    public void setType(CifType newType)
    {
        if (newType != type)
        {
            NotificationChain msgs = null;
            if (type != null)
                msgs = ((InternalEObject)type).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DeclarationsPackage.DISC_VARIABLE__TYPE, null, msgs);
            if (newType != null)
                msgs = ((InternalEObject)newType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DeclarationsPackage.DISC_VARIABLE__TYPE, null, msgs);
            msgs = basicSetType(newType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DeclarationsPackage.DISC_VARIABLE__TYPE, newType, newType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VariableValue getValue()
    {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValue(VariableValue newValue, NotificationChain msgs)
    {
        VariableValue oldValue = value;
        value = newValue;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DeclarationsPackage.DISC_VARIABLE__VALUE, oldValue, newValue);
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
    public void setValue(VariableValue newValue)
    {
        if (newValue != value)
        {
            NotificationChain msgs = null;
            if (value != null)
                msgs = ((InternalEObject)value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DeclarationsPackage.DISC_VARIABLE__VALUE, null, msgs);
            if (newValue != null)
                msgs = ((InternalEObject)newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DeclarationsPackage.DISC_VARIABLE__VALUE, null, msgs);
            msgs = basicSetValue(newValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DeclarationsPackage.DISC_VARIABLE__VALUE, newValue, newValue));
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
            case DeclarationsPackage.DISC_VARIABLE__TYPE:
                return basicSetType(null, msgs);
            case DeclarationsPackage.DISC_VARIABLE__VALUE:
                return basicSetValue(null, msgs);
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
            case DeclarationsPackage.DISC_VARIABLE__TYPE:
                return getType();
            case DeclarationsPackage.DISC_VARIABLE__VALUE:
                return getValue();
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
            case DeclarationsPackage.DISC_VARIABLE__TYPE:
                setType((CifType)newValue);
                return;
            case DeclarationsPackage.DISC_VARIABLE__VALUE:
                setValue((VariableValue)newValue);
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
            case DeclarationsPackage.DISC_VARIABLE__TYPE:
                setType((CifType)null);
                return;
            case DeclarationsPackage.DISC_VARIABLE__VALUE:
                setValue((VariableValue)null);
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
            case DeclarationsPackage.DISC_VARIABLE__TYPE:
                return type != null;
            case DeclarationsPackage.DISC_VARIABLE__VALUE:
                return value != null;
        }
        return super.eIsSet(featureID);
    }

} //DiscVariableImpl
