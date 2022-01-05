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
package org.eclipse.escet.cif.metamodel.cif.functions.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assignment Func Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.AssignmentFuncStatementImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.AssignmentFuncStatementImpl#getAddressable <em>Addressable</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssignmentFuncStatementImpl extends FunctionStatementImpl implements AssignmentFuncStatement
{
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
     * The cached value of the '{@link #getAddressable() <em>Addressable</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAddressable()
     * @generated
     * @ordered
     */
    protected Expression addressable;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AssignmentFuncStatementImpl()
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
        return FunctionsPackage.Literals.ASSIGNMENT_FUNC_STATEMENT;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE, oldValue, newValue);
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
                msgs = ((InternalEObject)value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE, null, msgs);
            if (newValue != null)
                msgs = ((InternalEObject)newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE, null, msgs);
            msgs = basicSetValue(newValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE, newValue, newValue));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE, oldAddressable, newAddressable);
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
                msgs = ((InternalEObject)addressable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE, null, msgs);
            if (newAddressable != null)
                msgs = ((InternalEObject)newAddressable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE, null, msgs);
            msgs = basicSetAddressable(newAddressable, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE, newAddressable, newAddressable));
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
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE:
                return basicSetValue(null, msgs);
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE:
                return basicSetAddressable(null, msgs);
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
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE:
                return getValue();
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE:
                return getAddressable();
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
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE:
                setValue((Expression)newValue);
                return;
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE:
                setAddressable((Expression)newValue);
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
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE:
                setValue((Expression)null);
                return;
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE:
                setAddressable((Expression)null);
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
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__VALUE:
                return value != null;
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT__ADDRESSABLE:
                return addressable != null;
        }
        return super.eIsSet(featureID);
    }

} //AssignmentFuncStatementImpl
