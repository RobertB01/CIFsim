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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>List Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ListTypeImpl#getElementType <em>Element Type</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ListTypeImpl#getInitialLength <em>Initial Length</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ListTypeImpl extends TypeImpl implements ListType
{
    /**
     * The cached value of the '{@link #getElementType() <em>Element Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementType()
     * @generated
     * @ordered
     */
    protected Type elementType;

    /**
     * The cached value of the '{@link #getInitialLength() <em>Initial Length</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInitialLength()
     * @generated
     * @ordered
     */
    protected Expression initialLength;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ListTypeImpl()
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
        return ChiPackage.Literals.LIST_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getElementType()
    {
        return elementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetElementType(Type newElementType, NotificationChain msgs)
    {
        Type oldElementType = elementType;
        elementType = newElementType;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.LIST_TYPE__ELEMENT_TYPE, oldElementType, newElementType);
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
    public void setElementType(Type newElementType)
    {
        if (newElementType != elementType)
        {
            NotificationChain msgs = null;
            if (elementType != null)
                msgs = ((InternalEObject)elementType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.LIST_TYPE__ELEMENT_TYPE, null, msgs);
            if (newElementType != null)
                msgs = ((InternalEObject)newElementType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.LIST_TYPE__ELEMENT_TYPE, null, msgs);
            msgs = basicSetElementType(newElementType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.LIST_TYPE__ELEMENT_TYPE, newElementType, newElementType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getInitialLength()
    {
        return initialLength;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInitialLength(Expression newInitialLength, NotificationChain msgs)
    {
        Expression oldInitialLength = initialLength;
        initialLength = newInitialLength;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.LIST_TYPE__INITIAL_LENGTH, oldInitialLength, newInitialLength);
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
    public void setInitialLength(Expression newInitialLength)
    {
        if (newInitialLength != initialLength)
        {
            NotificationChain msgs = null;
            if (initialLength != null)
                msgs = ((InternalEObject)initialLength).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.LIST_TYPE__INITIAL_LENGTH, null, msgs);
            if (newInitialLength != null)
                msgs = ((InternalEObject)newInitialLength).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.LIST_TYPE__INITIAL_LENGTH, null, msgs);
            msgs = basicSetInitialLength(newInitialLength, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.LIST_TYPE__INITIAL_LENGTH, newInitialLength, newInitialLength));
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
            case ChiPackage.LIST_TYPE__ELEMENT_TYPE:
                return basicSetElementType(null, msgs);
            case ChiPackage.LIST_TYPE__INITIAL_LENGTH:
                return basicSetInitialLength(null, msgs);
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
            case ChiPackage.LIST_TYPE__ELEMENT_TYPE:
                return getElementType();
            case ChiPackage.LIST_TYPE__INITIAL_LENGTH:
                return getInitialLength();
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
            case ChiPackage.LIST_TYPE__ELEMENT_TYPE:
                setElementType((Type)newValue);
                return;
            case ChiPackage.LIST_TYPE__INITIAL_LENGTH:
                setInitialLength((Expression)newValue);
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
            case ChiPackage.LIST_TYPE__ELEMENT_TYPE:
                setElementType((Type)null);
                return;
            case ChiPackage.LIST_TYPE__INITIAL_LENGTH:
                setInitialLength((Expression)null);
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
            case ChiPackage.LIST_TYPE__ELEMENT_TYPE:
                return elementType != null;
            case ChiPackage.LIST_TYPE__INITIAL_LENGTH:
                return initialLength != null;
        }
        return super.eIsSet(featureID);
    }

} //ListTypeImpl
