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
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.ProcessInstance;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessInstanceImpl#getCall <em>Call</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessInstanceImpl#getVar <em>Var</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessInstanceImpl extends CreateCaseImpl implements ProcessInstance
{
    /**
     * The cached value of the '{@link #getCall() <em>Call</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCall()
     * @generated
     * @ordered
     */
    protected Expression call;

    /**
     * The cached value of the '{@link #getVar() <em>Var</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVar()
     * @generated
     * @ordered
     */
    protected Expression var;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ProcessInstanceImpl()
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
        return ChiPackage.Literals.PROCESS_INSTANCE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getCall()
    {
        return call;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCall(Expression newCall, NotificationChain msgs)
    {
        Expression oldCall = call;
        call = newCall;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.PROCESS_INSTANCE__CALL, oldCall, newCall);
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
    public void setCall(Expression newCall)
    {
        if (newCall != call)
        {
            NotificationChain msgs = null;
            if (call != null)
                msgs = ((InternalEObject)call).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.PROCESS_INSTANCE__CALL, null, msgs);
            if (newCall != null)
                msgs = ((InternalEObject)newCall).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.PROCESS_INSTANCE__CALL, null, msgs);
            msgs = basicSetCall(newCall, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.PROCESS_INSTANCE__CALL, newCall, newCall));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getVar()
    {
        return var;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetVar(Expression newVar, NotificationChain msgs)
    {
        Expression oldVar = var;
        var = newVar;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.PROCESS_INSTANCE__VAR, oldVar, newVar);
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
    public void setVar(Expression newVar)
    {
        if (newVar != var)
        {
            NotificationChain msgs = null;
            if (var != null)
                msgs = ((InternalEObject)var).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.PROCESS_INSTANCE__VAR, null, msgs);
            if (newVar != null)
                msgs = ((InternalEObject)newVar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.PROCESS_INSTANCE__VAR, null, msgs);
            msgs = basicSetVar(newVar, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.PROCESS_INSTANCE__VAR, newVar, newVar));
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
            case ChiPackage.PROCESS_INSTANCE__CALL:
                return basicSetCall(null, msgs);
            case ChiPackage.PROCESS_INSTANCE__VAR:
                return basicSetVar(null, msgs);
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
            case ChiPackage.PROCESS_INSTANCE__CALL:
                return getCall();
            case ChiPackage.PROCESS_INSTANCE__VAR:
                return getVar();
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
            case ChiPackage.PROCESS_INSTANCE__CALL:
                setCall((Expression)newValue);
                return;
            case ChiPackage.PROCESS_INSTANCE__VAR:
                setVar((Expression)newValue);
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
            case ChiPackage.PROCESS_INSTANCE__CALL:
                setCall((Expression)null);
                return;
            case ChiPackage.PROCESS_INSTANCE__VAR:
                setVar((Expression)null);
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
            case ChiPackage.PROCESS_INSTANCE__CALL:
                return call != null;
            case ChiPackage.PROCESS_INSTANCE__VAR:
                return var != null;
        }
        return super.eIsSet(featureID);
    }

} //ProcessInstanceImpl
