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
package org.eclipse.escet.tooldef.metamodel.tooldef.impl;

import java.lang.reflect.Method;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Java Tool</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaToolImpl#getPluginName <em>Plugin Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaToolImpl#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaToolImpl#getMethod <em>Method</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JavaToolImpl extends ToolImpl implements JavaTool
{
    /**
     * The default value of the '{@link #getPluginName() <em>Plugin Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPluginName()
     * @generated
     * @ordered
     */
    protected static final String PLUGIN_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPluginName() <em>Plugin Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPluginName()
     * @generated
     * @ordered
     */
    protected String pluginName = PLUGIN_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getMethodName() <em>Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethodName()
     * @generated
     * @ordered
     */
    protected static final String METHOD_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMethodName() <em>Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethodName()
     * @generated
     * @ordered
     */
    protected String methodName = METHOD_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getMethod() <em>Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethod()
     * @generated
     * @ordered
     */
    protected static final Method METHOD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMethod() <em>Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethod()
     * @generated
     * @ordered
     */
    protected Method method = METHOD_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected JavaToolImpl()
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
        return TooldefPackage.Literals.JAVA_TOOL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getPluginName()
    {
        return pluginName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setPluginName(String newPluginName)
    {
        String oldPluginName = pluginName;
        pluginName = newPluginName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.JAVA_TOOL__PLUGIN_NAME, oldPluginName, pluginName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMethodName(String newMethodName)
    {
        String oldMethodName = methodName;
        methodName = newMethodName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.JAVA_TOOL__METHOD_NAME, oldMethodName, methodName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Method getMethod()
    {
        return method;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMethod(Method newMethod)
    {
        Method oldMethod = method;
        method = newMethod;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.JAVA_TOOL__METHOD, oldMethod, method));
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
            case TooldefPackage.JAVA_TOOL__PLUGIN_NAME:
                return getPluginName();
            case TooldefPackage.JAVA_TOOL__METHOD_NAME:
                return getMethodName();
            case TooldefPackage.JAVA_TOOL__METHOD:
                return getMethod();
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
            case TooldefPackage.JAVA_TOOL__PLUGIN_NAME:
                setPluginName((String)newValue);
                return;
            case TooldefPackage.JAVA_TOOL__METHOD_NAME:
                setMethodName((String)newValue);
                return;
            case TooldefPackage.JAVA_TOOL__METHOD:
                setMethod((Method)newValue);
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
            case TooldefPackage.JAVA_TOOL__PLUGIN_NAME:
                setPluginName(PLUGIN_NAME_EDEFAULT);
                return;
            case TooldefPackage.JAVA_TOOL__METHOD_NAME:
                setMethodName(METHOD_NAME_EDEFAULT);
                return;
            case TooldefPackage.JAVA_TOOL__METHOD:
                setMethod(METHOD_EDEFAULT);
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
            case TooldefPackage.JAVA_TOOL__PLUGIN_NAME:
                return PLUGIN_NAME_EDEFAULT == null ? pluginName != null : !PLUGIN_NAME_EDEFAULT.equals(pluginName);
            case TooldefPackage.JAVA_TOOL__METHOD_NAME:
                return METHOD_NAME_EDEFAULT == null ? methodName != null : !METHOD_NAME_EDEFAULT.equals(methodName);
            case TooldefPackage.JAVA_TOOL__METHOD:
                return METHOD_EDEFAULT == null ? method != null : !METHOD_EDEFAULT.equals(method);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (pluginName: ");
        result.append(pluginName);
        result.append(", methodName: ");
        result.append(methodName);
        result.append(", method: ");
        result.append(method);
        result.append(')');
        return result.toString();
    }

} //JavaToolImpl
