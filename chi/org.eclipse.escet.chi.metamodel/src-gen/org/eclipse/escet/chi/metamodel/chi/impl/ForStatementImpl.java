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
import org.eclipse.escet.chi.metamodel.chi.ForStatement;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.Unwind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>For Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ForStatementImpl#getBody <em>Body</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.ForStatementImpl#getUnwinds <em>Unwinds</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ForStatementImpl extends StatementImpl implements ForStatement
{
    /**
     * The cached value of the '{@link #getBody() <em>Body</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBody()
     * @generated
     * @ordered
     */
    protected EList<Statement> body;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ForStatementImpl()
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
        return ChiPackage.Literals.FOR_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Statement> getBody()
    {
        if (body == null)
        {
            body = new EObjectContainmentEList<Statement>(Statement.class, this, ChiPackage.FOR_STATEMENT__BODY);
        }
        return body;
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
            unwinds = new EObjectContainmentEList<Unwind>(Unwind.class, this, ChiPackage.FOR_STATEMENT__UNWINDS);
        }
        return unwinds;
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
            case ChiPackage.FOR_STATEMENT__BODY:
                return ((InternalEList<?>)getBody()).basicRemove(otherEnd, msgs);
            case ChiPackage.FOR_STATEMENT__UNWINDS:
                return ((InternalEList<?>)getUnwinds()).basicRemove(otherEnd, msgs);
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
            case ChiPackage.FOR_STATEMENT__BODY:
                return getBody();
            case ChiPackage.FOR_STATEMENT__UNWINDS:
                return getUnwinds();
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
            case ChiPackage.FOR_STATEMENT__BODY:
                getBody().clear();
                getBody().addAll((Collection<? extends Statement>)newValue);
                return;
            case ChiPackage.FOR_STATEMENT__UNWINDS:
                getUnwinds().clear();
                getUnwinds().addAll((Collection<? extends Unwind>)newValue);
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
            case ChiPackage.FOR_STATEMENT__BODY:
                getBody().clear();
                return;
            case ChiPackage.FOR_STATEMENT__UNWINDS:
                getUnwinds().clear();
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
            case ChiPackage.FOR_STATEMENT__BODY:
                return body != null && !body.isEmpty();
            case ChiPackage.FOR_STATEMENT__UNWINDS:
                return unwinds != null && !unwinds.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ForStatementImpl
