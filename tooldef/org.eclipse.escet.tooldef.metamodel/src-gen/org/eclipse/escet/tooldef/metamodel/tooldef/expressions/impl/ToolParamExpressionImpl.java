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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tool Param Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolParamExpressionImpl#getParam <em>Param</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ToolParamExpressionImpl extends ExpressionImpl implements ToolParamExpression
{
    /**
     * The cached value of the '{@link #getParam() <em>Param</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParam()
     * @generated
     * @ordered
     */
    protected ToolParameter param;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ToolParamExpressionImpl()
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
        return ExpressionsPackage.Literals.TOOL_PARAM_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolParameter getParam()
    {
        if (param != null && param.eIsProxy())
        {
            InternalEObject oldParam = (InternalEObject)param;
            param = (ToolParameter)eResolveProxy(oldParam);
            if (param != oldParam)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.TOOL_PARAM_EXPRESSION__PARAM, oldParam, param));
            }
        }
        return param;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ToolParameter basicGetParam()
    {
        return param;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setParam(ToolParameter newParam)
    {
        ToolParameter oldParam = param;
        param = newParam;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.TOOL_PARAM_EXPRESSION__PARAM, oldParam, param));
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
            case ExpressionsPackage.TOOL_PARAM_EXPRESSION__PARAM:
                if (resolve) return getParam();
                return basicGetParam();
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
            case ExpressionsPackage.TOOL_PARAM_EXPRESSION__PARAM:
                setParam((ToolParameter)newValue);
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
            case ExpressionsPackage.TOOL_PARAM_EXPRESSION__PARAM:
                setParam((ToolParameter)null);
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
            case ExpressionsPackage.TOOL_PARAM_EXPRESSION__PARAM:
                return param != null;
        }
        return super.eIsSet(featureID);
    }

} //ToolParamExpressionImpl
