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
package org.eclipse.escet.cif.metamodel.cif.automata.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>If Update</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.IfUpdateImpl#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.IfUpdateImpl#getThens <em>Thens</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.IfUpdateImpl#getElifs <em>Elifs</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.IfUpdateImpl#getElses <em>Elses</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IfUpdateImpl extends UpdateImpl implements IfUpdate
{
    /**
     * The cached value of the '{@link #getGuards() <em>Guards</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGuards()
     * @generated
     * @ordered
     */
    protected EList<Expression> guards;

    /**
     * The cached value of the '{@link #getThens() <em>Thens</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getThens()
     * @generated
     * @ordered
     */
    protected EList<Update> thens;

    /**
     * The cached value of the '{@link #getElifs() <em>Elifs</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElifs()
     * @generated
     * @ordered
     */
    protected EList<ElifUpdate> elifs;

    /**
     * The cached value of the '{@link #getElses() <em>Elses</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElses()
     * @generated
     * @ordered
     */
    protected EList<Update> elses;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IfUpdateImpl()
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
        return AutomataPackage.Literals.IF_UPDATE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getGuards()
    {
        if (guards == null)
        {
            guards = new EObjectContainmentEList<Expression>(Expression.class, this, AutomataPackage.IF_UPDATE__GUARDS);
        }
        return guards;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Update> getThens()
    {
        if (thens == null)
        {
            thens = new EObjectContainmentEList<Update>(Update.class, this, AutomataPackage.IF_UPDATE__THENS);
        }
        return thens;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<ElifUpdate> getElifs()
    {
        if (elifs == null)
        {
            elifs = new EObjectContainmentEList<ElifUpdate>(ElifUpdate.class, this, AutomataPackage.IF_UPDATE__ELIFS);
        }
        return elifs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Update> getElses()
    {
        if (elses == null)
        {
            elses = new EObjectContainmentEList<Update>(Update.class, this, AutomataPackage.IF_UPDATE__ELSES);
        }
        return elses;
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
            case AutomataPackage.IF_UPDATE__GUARDS:
                return ((InternalEList<?>)getGuards()).basicRemove(otherEnd, msgs);
            case AutomataPackage.IF_UPDATE__THENS:
                return ((InternalEList<?>)getThens()).basicRemove(otherEnd, msgs);
            case AutomataPackage.IF_UPDATE__ELIFS:
                return ((InternalEList<?>)getElifs()).basicRemove(otherEnd, msgs);
            case AutomataPackage.IF_UPDATE__ELSES:
                return ((InternalEList<?>)getElses()).basicRemove(otherEnd, msgs);
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
            case AutomataPackage.IF_UPDATE__GUARDS:
                return getGuards();
            case AutomataPackage.IF_UPDATE__THENS:
                return getThens();
            case AutomataPackage.IF_UPDATE__ELIFS:
                return getElifs();
            case AutomataPackage.IF_UPDATE__ELSES:
                return getElses();
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
            case AutomataPackage.IF_UPDATE__GUARDS:
                getGuards().clear();
                getGuards().addAll((Collection<? extends Expression>)newValue);
                return;
            case AutomataPackage.IF_UPDATE__THENS:
                getThens().clear();
                getThens().addAll((Collection<? extends Update>)newValue);
                return;
            case AutomataPackage.IF_UPDATE__ELIFS:
                getElifs().clear();
                getElifs().addAll((Collection<? extends ElifUpdate>)newValue);
                return;
            case AutomataPackage.IF_UPDATE__ELSES:
                getElses().clear();
                getElses().addAll((Collection<? extends Update>)newValue);
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
            case AutomataPackage.IF_UPDATE__GUARDS:
                getGuards().clear();
                return;
            case AutomataPackage.IF_UPDATE__THENS:
                getThens().clear();
                return;
            case AutomataPackage.IF_UPDATE__ELIFS:
                getElifs().clear();
                return;
            case AutomataPackage.IF_UPDATE__ELSES:
                getElses().clear();
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
            case AutomataPackage.IF_UPDATE__GUARDS:
                return guards != null && !guards.isEmpty();
            case AutomataPackage.IF_UPDATE__THENS:
                return thens != null && !thens.isEmpty();
            case AutomataPackage.IF_UPDATE__ELIFS:
                return elifs != null && !elifs.isEmpty();
            case AutomataPackage.IF_UPDATE__ELSES:
                return elses != null && !elses.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //IfUpdateImpl
