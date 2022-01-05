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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tool Invoke Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolInvokeExpressionImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolInvokeExpressionImpl#getTool <em>Tool</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ToolInvokeExpressionImpl extends ExpressionImpl implements ToolInvokeExpression
{
    /**
     * The cached value of the '{@link #getArguments() <em>Arguments</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getArguments()
     * @generated
     * @ordered
     */
    protected EList<ToolArgument> arguments;

    /**
     * The cached value of the '{@link #getTool() <em>Tool</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTool()
     * @generated
     * @ordered
     */
    protected ToolRef tool;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ToolInvokeExpressionImpl()
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
        return ExpressionsPackage.Literals.TOOL_INVOKE_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<ToolArgument> getArguments()
    {
        if (arguments == null)
        {
            arguments = new EObjectContainmentEList<ToolArgument>(ToolArgument.class, this, ExpressionsPackage.TOOL_INVOKE_EXPRESSION__ARGUMENTS);
        }
        return arguments;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolRef getTool()
    {
        return tool;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTool(ToolRef newTool, NotificationChain msgs)
    {
        ToolRef oldTool = tool;
        tool = newTool;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL, oldTool, newTool);
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
    public void setTool(ToolRef newTool)
    {
        if (newTool != tool)
        {
            NotificationChain msgs = null;
            if (tool != null)
                msgs = ((InternalEObject)tool).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL, null, msgs);
            if (newTool != null)
                msgs = ((InternalEObject)newTool).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL, null, msgs);
            msgs = basicSetTool(newTool, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL, newTool, newTool));
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
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__ARGUMENTS:
                return ((InternalEList<?>)getArguments()).basicRemove(otherEnd, msgs);
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL:
                return basicSetTool(null, msgs);
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
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__ARGUMENTS:
                return getArguments();
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL:
                return getTool();
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
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__ARGUMENTS:
                getArguments().clear();
                getArguments().addAll((Collection<? extends ToolArgument>)newValue);
                return;
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL:
                setTool((ToolRef)newValue);
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
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__ARGUMENTS:
                getArguments().clear();
                return;
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL:
                setTool((ToolRef)null);
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
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__ARGUMENTS:
                return arguments != null && !arguments.isEmpty();
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION__TOOL:
                return tool != null;
        }
        return super.eIsSet(featureID);
    }

} //ToolInvokeExpressionImpl
