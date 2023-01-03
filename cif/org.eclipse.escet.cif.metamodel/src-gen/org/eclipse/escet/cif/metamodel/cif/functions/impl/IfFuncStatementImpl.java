/**
 * Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>If Func Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.IfFuncStatementImpl#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.IfFuncStatementImpl#getThens <em>Thens</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.IfFuncStatementImpl#getElses <em>Elses</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.IfFuncStatementImpl#getElifs <em>Elifs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IfFuncStatementImpl extends FunctionStatementImpl implements IfFuncStatement
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
    protected EList<FunctionStatement> thens;

    /**
     * The cached value of the '{@link #getElses() <em>Elses</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElses()
     * @generated
     * @ordered
     */
    protected EList<FunctionStatement> elses;

    /**
     * The cached value of the '{@link #getElifs() <em>Elifs</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElifs()
     * @generated
     * @ordered
     */
    protected EList<ElifFuncStatement> elifs;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IfFuncStatementImpl()
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
        return FunctionsPackage.Literals.IF_FUNC_STATEMENT;
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
            guards = new EObjectContainmentEList<Expression>(Expression.class, this, FunctionsPackage.IF_FUNC_STATEMENT__GUARDS);
        }
        return guards;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<FunctionStatement> getThens()
    {
        if (thens == null)
        {
            thens = new EObjectContainmentEList<FunctionStatement>(FunctionStatement.class, this, FunctionsPackage.IF_FUNC_STATEMENT__THENS);
        }
        return thens;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<FunctionStatement> getElses()
    {
        if (elses == null)
        {
            elses = new EObjectContainmentEList<FunctionStatement>(FunctionStatement.class, this, FunctionsPackage.IF_FUNC_STATEMENT__ELSES);
        }
        return elses;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<ElifFuncStatement> getElifs()
    {
        if (elifs == null)
        {
            elifs = new EObjectContainmentEList<ElifFuncStatement>(ElifFuncStatement.class, this, FunctionsPackage.IF_FUNC_STATEMENT__ELIFS);
        }
        return elifs;
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
            case FunctionsPackage.IF_FUNC_STATEMENT__GUARDS:
                return ((InternalEList<?>)getGuards()).basicRemove(otherEnd, msgs);
            case FunctionsPackage.IF_FUNC_STATEMENT__THENS:
                return ((InternalEList<?>)getThens()).basicRemove(otherEnd, msgs);
            case FunctionsPackage.IF_FUNC_STATEMENT__ELSES:
                return ((InternalEList<?>)getElses()).basicRemove(otherEnd, msgs);
            case FunctionsPackage.IF_FUNC_STATEMENT__ELIFS:
                return ((InternalEList<?>)getElifs()).basicRemove(otherEnd, msgs);
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
            case FunctionsPackage.IF_FUNC_STATEMENT__GUARDS:
                return getGuards();
            case FunctionsPackage.IF_FUNC_STATEMENT__THENS:
                return getThens();
            case FunctionsPackage.IF_FUNC_STATEMENT__ELSES:
                return getElses();
            case FunctionsPackage.IF_FUNC_STATEMENT__ELIFS:
                return getElifs();
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
            case FunctionsPackage.IF_FUNC_STATEMENT__GUARDS:
                getGuards().clear();
                getGuards().addAll((Collection<? extends Expression>)newValue);
                return;
            case FunctionsPackage.IF_FUNC_STATEMENT__THENS:
                getThens().clear();
                getThens().addAll((Collection<? extends FunctionStatement>)newValue);
                return;
            case FunctionsPackage.IF_FUNC_STATEMENT__ELSES:
                getElses().clear();
                getElses().addAll((Collection<? extends FunctionStatement>)newValue);
                return;
            case FunctionsPackage.IF_FUNC_STATEMENT__ELIFS:
                getElifs().clear();
                getElifs().addAll((Collection<? extends ElifFuncStatement>)newValue);
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
            case FunctionsPackage.IF_FUNC_STATEMENT__GUARDS:
                getGuards().clear();
                return;
            case FunctionsPackage.IF_FUNC_STATEMENT__THENS:
                getThens().clear();
                return;
            case FunctionsPackage.IF_FUNC_STATEMENT__ELSES:
                getElses().clear();
                return;
            case FunctionsPackage.IF_FUNC_STATEMENT__ELIFS:
                getElifs().clear();
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
            case FunctionsPackage.IF_FUNC_STATEMENT__GUARDS:
                return guards != null && !guards.isEmpty();
            case FunctionsPackage.IF_FUNC_STATEMENT__THENS:
                return thens != null && !thens.isEmpty();
            case FunctionsPackage.IF_FUNC_STATEMENT__ELSES:
                return elses != null && !elses.isEmpty();
            case FunctionsPackage.IF_FUNC_STATEMENT__ELIFS:
                return elifs != null && !elifs.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //IfFuncStatementImpl
