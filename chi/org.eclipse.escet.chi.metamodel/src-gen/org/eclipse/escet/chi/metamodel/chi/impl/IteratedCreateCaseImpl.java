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

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.CreateCase;
import org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase;
import org.eclipse.escet.chi.metamodel.chi.Unwind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Iterated Create Case</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.IteratedCreateCaseImpl#getUnwinds <em>Unwinds</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.IteratedCreateCaseImpl#getInstances <em>Instances</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IteratedCreateCaseImpl extends CreateCaseImpl implements IteratedCreateCase
{
    /**
     * The cached value of the '{@link #getUnwinds() <em>Unwinds</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUnwinds()
     * @generated
     * @ordered
     */
    protected EList<Unwind> unwinds;

    /**
     * The cached value of the '{@link #getInstances() <em>Instances</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInstances()
     * @generated
     * @ordered
     */
    protected EList<CreateCase> instances;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IteratedCreateCaseImpl()
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
        return ChiPackage.Literals.ITERATED_CREATE_CASE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Unwind> getUnwinds()
    {
        if (unwinds == null)
        {
            unwinds = new EObjectContainmentEList<Unwind>(Unwind.class, this, ChiPackage.ITERATED_CREATE_CASE__UNWINDS);
        }
        return unwinds;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<CreateCase> getInstances()
    {
        if (instances == null)
        {
            instances = new EObjectContainmentEList<CreateCase>(CreateCase.class, this, ChiPackage.ITERATED_CREATE_CASE__INSTANCES);
        }
        return instances;
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
            case ChiPackage.ITERATED_CREATE_CASE__UNWINDS:
                return ((InternalEList<?>)getUnwinds()).basicRemove(otherEnd, msgs);
            case ChiPackage.ITERATED_CREATE_CASE__INSTANCES:
                return ((InternalEList<?>)getInstances()).basicRemove(otherEnd, msgs);
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
            case ChiPackage.ITERATED_CREATE_CASE__UNWINDS:
                return getUnwinds();
            case ChiPackage.ITERATED_CREATE_CASE__INSTANCES:
                return getInstances();
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
            case ChiPackage.ITERATED_CREATE_CASE__UNWINDS:
                getUnwinds().clear();
                getUnwinds().addAll((Collection<? extends Unwind>)newValue);
                return;
            case ChiPackage.ITERATED_CREATE_CASE__INSTANCES:
                getInstances().clear();
                getInstances().addAll((Collection<? extends CreateCase>)newValue);
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
            case ChiPackage.ITERATED_CREATE_CASE__UNWINDS:
                getUnwinds().clear();
                return;
            case ChiPackage.ITERATED_CREATE_CASE__INSTANCES:
                getInstances().clear();
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
            case ChiPackage.ITERATED_CREATE_CASE__UNWINDS:
                return unwinds != null && !unwinds.isEmpty();
            case ChiPackage.ITERATED_CREATE_CASE__INSTANCES:
                return instances != null && !instances.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //IteratedCreateCaseImpl
