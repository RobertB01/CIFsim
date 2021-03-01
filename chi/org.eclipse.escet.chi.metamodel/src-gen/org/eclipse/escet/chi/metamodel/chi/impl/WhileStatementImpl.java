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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.WhileStatement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>While Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.WhileStatementImpl#getCondition <em>Condition</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.WhileStatementImpl#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WhileStatementImpl extends StatementImpl implements WhileStatement
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
     * The cached value of the '{@link #getBody() <em>Body</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBody()
     * @generated
     * @ordered
     */
    protected EList<Statement> body;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected WhileStatementImpl()
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
        return ChiPackage.Literals.WHILE_STATEMENT;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.WHILE_STATEMENT__CONDITION, oldCondition, newCondition);
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
                msgs = ((InternalEObject)condition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.WHILE_STATEMENT__CONDITION, null, msgs);
            if (newCondition != null)
                msgs = ((InternalEObject)newCondition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.WHILE_STATEMENT__CONDITION, null, msgs);
            msgs = basicSetCondition(newCondition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.WHILE_STATEMENT__CONDITION, newCondition, newCondition));
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
            body = new EObjectContainmentEList<Statement>(Statement.class, this, ChiPackage.WHILE_STATEMENT__BODY);
        }
        return body;
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
            case ChiPackage.WHILE_STATEMENT__CONDITION:
                return basicSetCondition(null, msgs);
            case ChiPackage.WHILE_STATEMENT__BODY:
                return ((InternalEList<?>)getBody()).basicRemove(otherEnd, msgs);
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
            case ChiPackage.WHILE_STATEMENT__CONDITION:
                return getCondition();
            case ChiPackage.WHILE_STATEMENT__BODY:
                return getBody();
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
            case ChiPackage.WHILE_STATEMENT__CONDITION:
                setCondition((Expression)newValue);
                return;
            case ChiPackage.WHILE_STATEMENT__BODY:
                getBody().clear();
                getBody().addAll((Collection<? extends Statement>)newValue);
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
            case ChiPackage.WHILE_STATEMENT__CONDITION:
                setCondition((Expression)null);
                return;
            case ChiPackage.WHILE_STATEMENT__BODY:
                getBody().clear();
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
            case ChiPackage.WHILE_STATEMENT__CONDITION:
                return condition != null;
            case ChiPackage.WHILE_STATEMENT__BODY:
                return body != null && !body.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //WhileStatementImpl
