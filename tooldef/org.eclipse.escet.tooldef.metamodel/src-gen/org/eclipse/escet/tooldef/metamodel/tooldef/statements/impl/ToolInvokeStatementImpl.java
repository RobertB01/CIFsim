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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tool Invoke Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ToolInvokeStatementImpl#getInvocation <em>Invocation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ToolInvokeStatementImpl extends StatementImpl implements ToolInvokeStatement
{
    /**
     * The cached value of the '{@link #getInvocation() <em>Invocation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInvocation()
     * @generated
     * @ordered
     */
    protected ToolInvokeExpression invocation;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ToolInvokeStatementImpl()
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
        return StatementsPackage.Literals.TOOL_INVOKE_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolInvokeExpression getInvocation()
    {
        return invocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInvocation(ToolInvokeExpression newInvocation, NotificationChain msgs)
    {
        ToolInvokeExpression oldInvocation = invocation;
        invocation = newInvocation;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION, oldInvocation, newInvocation);
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
    public void setInvocation(ToolInvokeExpression newInvocation)
    {
        if (newInvocation != invocation)
        {
            NotificationChain msgs = null;
            if (invocation != null)
                msgs = ((InternalEObject)invocation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION, null, msgs);
            if (newInvocation != null)
                msgs = ((InternalEObject)newInvocation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION, null, msgs);
            msgs = basicSetInvocation(newInvocation, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION, newInvocation, newInvocation));
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
            case StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION:
                return basicSetInvocation(null, msgs);
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
            case StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION:
                return getInvocation();
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
            case StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION:
                setInvocation((ToolInvokeExpression)newValue);
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
            case StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION:
                setInvocation((ToolInvokeExpression)null);
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
            case StatementsPackage.TOOL_INVOKE_STATEMENT__INVOCATION:
                return invocation != null;
        }
        return super.eIsSet(featureID);
    }

} //ToolInvokeStatementImpl
