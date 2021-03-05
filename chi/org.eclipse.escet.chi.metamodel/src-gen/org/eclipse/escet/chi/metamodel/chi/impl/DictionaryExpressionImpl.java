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
package org.eclipse.escet.chi.metamodel.chi.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.DictionaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictionaryPair;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dictionary Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.DictionaryExpressionImpl#getPairs <em>Pairs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DictionaryExpressionImpl extends ExpressionImpl implements DictionaryExpression
{
    /**
     * The cached value of the '{@link #getPairs() <em>Pairs</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPairs()
     * @generated
     * @ordered
     */
    protected EList<DictionaryPair> pairs;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DictionaryExpressionImpl()
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
        return ChiPackage.Literals.DICTIONARY_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<DictionaryPair> getPairs()
    {
        if (pairs == null)
        {
            pairs = new EObjectContainmentEList<DictionaryPair>(DictionaryPair.class, this, ChiPackage.DICTIONARY_EXPRESSION__PAIRS);
        }
        return pairs;
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
            case ChiPackage.DICTIONARY_EXPRESSION__PAIRS:
                return ((InternalEList<?>)getPairs()).basicRemove(otherEnd, msgs);
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
            case ChiPackage.DICTIONARY_EXPRESSION__PAIRS:
                return getPairs();
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
            case ChiPackage.DICTIONARY_EXPRESSION__PAIRS:
                getPairs().clear();
                getPairs().addAll((Collection<? extends DictionaryPair>)newValue);
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
            case ChiPackage.DICTIONARY_EXPRESSION__PAIRS:
                getPairs().clear();
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
            case ChiPackage.DICTIONARY_EXPRESSION__PAIRS:
                return pairs != null && !pairs.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //DictionaryExpressionImpl
