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
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.ReadCallExpression;
import org.eclipse.escet.chi.metamodel.chi.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Read Call Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ReadCallExpressionImpl#getFile <em>File</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ReadCallExpressionImpl#getLoadType <em>Load Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReadCallExpressionImpl extends ExpressionImpl implements ReadCallExpression
{
    /**
     * The cached value of the '{@link #getFile() <em>File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFile()
     * @generated
     * @ordered
     */
    protected Expression file;

    /**
     * The cached value of the '{@link #getLoadType() <em>Load Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoadType()
     * @generated
     * @ordered
     */
    protected Type loadType;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ReadCallExpressionImpl()
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
        return ChiPackage.Literals.READ_CALL_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getFile()
    {
        return file;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFile(Expression newFile, NotificationChain msgs)
    {
        Expression oldFile = file;
        file = newFile;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.READ_CALL_EXPRESSION__FILE, oldFile, newFile);
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
    public void setFile(Expression newFile)
    {
        if (newFile != file)
        {
            NotificationChain msgs = null;
            if (file != null)
                msgs = ((InternalEObject)file).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.READ_CALL_EXPRESSION__FILE, null, msgs);
            if (newFile != null)
                msgs = ((InternalEObject)newFile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.READ_CALL_EXPRESSION__FILE, null, msgs);
            msgs = basicSetFile(newFile, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.READ_CALL_EXPRESSION__FILE, newFile, newFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getLoadType()
    {
        return loadType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLoadType(Type newLoadType, NotificationChain msgs)
    {
        Type oldLoadType = loadType;
        loadType = newLoadType;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE, oldLoadType, newLoadType);
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
    public void setLoadType(Type newLoadType)
    {
        if (newLoadType != loadType)
        {
            NotificationChain msgs = null;
            if (loadType != null)
                msgs = ((InternalEObject)loadType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE, null, msgs);
            if (newLoadType != null)
                msgs = ((InternalEObject)newLoadType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE, null, msgs);
            msgs = basicSetLoadType(newLoadType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE, newLoadType, newLoadType));
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
            case ChiPackage.READ_CALL_EXPRESSION__FILE:
                return basicSetFile(null, msgs);
            case ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE:
                return basicSetLoadType(null, msgs);
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
            case ChiPackage.READ_CALL_EXPRESSION__FILE:
                return getFile();
            case ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE:
                return getLoadType();
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
            case ChiPackage.READ_CALL_EXPRESSION__FILE:
                setFile((Expression)newValue);
                return;
            case ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE:
                setLoadType((Type)newValue);
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
            case ChiPackage.READ_CALL_EXPRESSION__FILE:
                setFile((Expression)null);
                return;
            case ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE:
                setLoadType((Type)null);
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
            case ChiPackage.READ_CALL_EXPRESSION__FILE:
                return file != null;
            case ChiPackage.READ_CALL_EXPRESSION__LOAD_TYPE:
                return loadType != null;
        }
        return super.eIsSet(featureID);
    }

} //ReadCallExpressionImpl
