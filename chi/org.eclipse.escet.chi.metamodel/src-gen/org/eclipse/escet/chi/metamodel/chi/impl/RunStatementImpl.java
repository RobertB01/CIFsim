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
import org.eclipse.escet.chi.metamodel.chi.CreateCase;
import org.eclipse.escet.chi.metamodel.chi.RunStatement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Run Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.RunStatementImpl#getCases <em>Cases</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.RunStatementImpl#isStartOnly <em>Start Only</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RunStatementImpl extends StatementImpl implements RunStatement
{
    /**
     * The cached value of the '{@link #getCases() <em>Cases</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCases()
     * @generated
     * @ordered
     */
    protected EList<CreateCase> cases;

    /**
     * The default value of the '{@link #isStartOnly() <em>Start Only</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStartOnly()
     * @generated
     * @ordered
     */
    protected static final boolean START_ONLY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStartOnly() <em>Start Only</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStartOnly()
     * @generated
     * @ordered
     */
    protected boolean startOnly = START_ONLY_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RunStatementImpl()
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
        return ChiPackage.Literals.RUN_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<CreateCase> getCases()
    {
        if (cases == null)
        {
            cases = new EObjectContainmentEList<CreateCase>(CreateCase.class, this, ChiPackage.RUN_STATEMENT__CASES);
        }
        return cases;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isStartOnly()
    {
        return startOnly;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setStartOnly(boolean newStartOnly)
    {
        boolean oldStartOnly = startOnly;
        startOnly = newStartOnly;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.RUN_STATEMENT__START_ONLY, oldStartOnly, startOnly));
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
            case ChiPackage.RUN_STATEMENT__CASES:
                return ((InternalEList<?>)getCases()).basicRemove(otherEnd, msgs);
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
            case ChiPackage.RUN_STATEMENT__CASES:
                return getCases();
            case ChiPackage.RUN_STATEMENT__START_ONLY:
                return isStartOnly();
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
            case ChiPackage.RUN_STATEMENT__CASES:
                getCases().clear();
                getCases().addAll((Collection<? extends CreateCase>)newValue);
                return;
            case ChiPackage.RUN_STATEMENT__START_ONLY:
                setStartOnly((Boolean)newValue);
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
            case ChiPackage.RUN_STATEMENT__CASES:
                getCases().clear();
                return;
            case ChiPackage.RUN_STATEMENT__START_ONLY:
                setStartOnly(START_ONLY_EDEFAULT);
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
            case ChiPackage.RUN_STATEMENT__CASES:
                return cases != null && !cases.isEmpty();
            case ChiPackage.RUN_STATEMENT__START_ONLY:
                return startOnly != START_ONLY_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (startOnly: ");
        result.append(startOnly);
        result.append(')');
        return result.toString();
    }

} //RunStatementImpl
