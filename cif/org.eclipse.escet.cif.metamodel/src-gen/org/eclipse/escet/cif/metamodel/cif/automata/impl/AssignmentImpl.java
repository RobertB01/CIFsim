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
package org.eclipse.escet.cif.metamodel.cif.automata.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assignment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AssignmentImpl#getAddressable <em>Addressable</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AssignmentImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssignmentImpl extends UpdateImpl implements Assignment
{
    /**
     * The cached value of the '{@link #getAddressable() <em>Addressable</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAddressable()
     * @generated
     * @ordered
     */
    protected Expression addressable;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected Expression value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AssignmentImpl()
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
        return AutomataPackage.Literals.ASSIGNMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getAddressable()
    {
        return addressable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAddressable(Expression newAddressable, NotificationChain msgs)
    {
        Expression oldAddressable = addressable;
        addressable = newAddressable;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AutomataPackage.ASSIGNMENT__ADDRESSABLE, oldAddressable, newAddressable);
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
    public void setAddressable(Expression newAddressable)
    {
        if (newAddressable != addressable)
        {
            NotificationChain msgs = null;
            if (addressable != null)
                msgs = ((InternalEObject)addressable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AutomataPackage.ASSIGNMENT__ADDRESSABLE, null, msgs);
            if (newAddressable != null)
                msgs = ((InternalEObject)newAddressable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AutomataPackage.ASSIGNMENT__ADDRESSABLE, null, msgs);
            msgs = basicSetAddressable(newAddressable, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.ASSIGNMENT__ADDRESSABLE, newAddressable, newAddressable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getValue()
    {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValue(Expression newValue, NotificationChain msgs)
    {
        Expression oldValue = value;
        value = newValue;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AutomataPackage.ASSIGNMENT__VALUE, oldValue, newValue);
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
    public void setValue(Expression newValue)
    {
        if (newValue != value)
        {
            NotificationChain msgs = null;
            if (value != null)
                msgs = ((InternalEObject)value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AutomataPackage.ASSIGNMENT__VALUE, null, msgs);
            if (newValue != null)
                msgs = ((InternalEObject)newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AutomataPackage.ASSIGNMENT__VALUE, null, msgs);
            msgs = basicSetValue(newValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.ASSIGNMENT__VALUE, newValue, newValue));
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
            case AutomataPackage.ASSIGNMENT__ADDRESSABLE:
                return basicSetAddressable(null, msgs);
            case AutomataPackage.ASSIGNMENT__VALUE:
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
            case AutomataPackage.ASSIGNMENT__ADDRESSABLE:
                return getAddressable();
            case AutomataPackage.ASSIGNMENT__VALUE:
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
            case AutomataPackage.ASSIGNMENT__ADDRESSABLE:
                setAddressable((Expression)newValue);
                return;
            case AutomataPackage.ASSIGNMENT__VALUE:
                setValue((Expression)newValue);
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
            case AutomataPackage.ASSIGNMENT__ADDRESSABLE:
                setAddressable((Expression)null);
                return;
            case AutomataPackage.ASSIGNMENT__VALUE:
                setValue((Expression)null);
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
            case AutomataPackage.ASSIGNMENT__ADDRESSABLE:
                return addressable != null;
            case AutomataPackage.ASSIGNMENT__VALUE:
                return value != null;
        }
        return super.eIsSet(featureID);
    }

} //AssignmentImpl
