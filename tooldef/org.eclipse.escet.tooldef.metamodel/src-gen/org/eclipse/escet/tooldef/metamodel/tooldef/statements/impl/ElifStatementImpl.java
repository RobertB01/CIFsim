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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Elif Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ElifStatementImpl#getCondition <em>Condition</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ElifStatementImpl#getThens <em>Thens</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ElifStatementImpl extends PositionObjectImpl implements ElifStatement
{
    /**
     * The cached value of the '{@link #getCondition() <em>Condition</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCondition()
     * @generated
     * @ordered
     */
    protected Expression condition;

    /**
     * The cached value of the '{@link #getThens() <em>Thens</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getThens()
     * @generated
     * @ordered
     */
    protected EList<Statement> thens;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ElifStatementImpl()
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
        return StatementsPackage.Literals.ELIF_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getCondition()
    {
        return condition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCondition(Expression newCondition, NotificationChain msgs)
    {
        Expression oldCondition = condition;
        condition = newCondition;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StatementsPackage.ELIF_STATEMENT__CONDITION, oldCondition, newCondition);
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
    public void setCondition(Expression newCondition)
    {
        if (newCondition != condition)
        {
            NotificationChain msgs = null;
            if (condition != null)
                msgs = ((InternalEObject)condition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StatementsPackage.ELIF_STATEMENT__CONDITION, null, msgs);
            if (newCondition != null)
                msgs = ((InternalEObject)newCondition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StatementsPackage.ELIF_STATEMENT__CONDITION, null, msgs);
            msgs = basicSetCondition(newCondition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StatementsPackage.ELIF_STATEMENT__CONDITION, newCondition, newCondition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Statement> getThens()
    {
        if (thens == null)
        {
            thens = new EObjectContainmentEList<Statement>(Statement.class, this, StatementsPackage.ELIF_STATEMENT__THENS);
        }
        return thens;
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
            case StatementsPackage.ELIF_STATEMENT__CONDITION:
                return basicSetCondition(null, msgs);
            case StatementsPackage.ELIF_STATEMENT__THENS:
                return ((InternalEList<?>)getThens()).basicRemove(otherEnd, msgs);
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
            case StatementsPackage.ELIF_STATEMENT__CONDITION:
                return getCondition();
            case StatementsPackage.ELIF_STATEMENT__THENS:
                return getThens();
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
            case StatementsPackage.ELIF_STATEMENT__CONDITION:
                setCondition((Expression)newValue);
                return;
            case StatementsPackage.ELIF_STATEMENT__THENS:
                getThens().clear();
                getThens().addAll((Collection<? extends Statement>)newValue);
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
            case StatementsPackage.ELIF_STATEMENT__CONDITION:
                setCondition((Expression)null);
                return;
            case StatementsPackage.ELIF_STATEMENT__THENS:
                getThens().clear();
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
            case StatementsPackage.ELIF_STATEMENT__CONDITION:
                return condition != null;
            case StatementsPackage.ELIF_STATEMENT__THENS:
                return thens != null && !thens.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ElifStatementImpl
