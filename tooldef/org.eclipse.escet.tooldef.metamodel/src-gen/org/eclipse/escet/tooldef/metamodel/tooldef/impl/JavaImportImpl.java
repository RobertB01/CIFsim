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
package org.eclipse.escet.tooldef.metamodel.tooldef.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.setext.runtime.Token;

import org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport;
import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Java Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaImportImpl#getPluginName <em>Plugin Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaImportImpl#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.JavaImportImpl#getAsName <em>As Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JavaImportImpl extends ImportImpl implements JavaImport
{
    /**
     * The default value of the '{@link #getPluginName() <em>Plugin Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPluginName()
     * @generated
     * @ordered
     */
    protected static final Token PLUGIN_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPluginName() <em>Plugin Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPluginName()
     * @generated
     * @ordered
     */
    protected Token pluginName = PLUGIN_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getMethodName() <em>Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethodName()
     * @generated
     * @ordered
     */
    protected static final Token METHOD_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMethodName() <em>Method Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethodName()
     * @generated
     * @ordered
     */
    protected Token methodName = METHOD_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getAsName() <em>As Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAsName()
     * @generated
     * @ordered
     */
    protected static final Token AS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAsName() <em>As Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAsName()
     * @generated
     * @ordered
     */
    protected Token asName = AS_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected JavaImportImpl()
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
        return TooldefPackage.Literals.JAVA_IMPORT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Token getPluginName()
    {
        return pluginName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setPluginName(Token newPluginName)
    {
        Token oldPluginName = pluginName;
        pluginName = newPluginName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.JAVA_IMPORT__PLUGIN_NAME, oldPluginName, pluginName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Token getMethodName()
    {
        return methodName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMethodName(Token newMethodName)
    {
        Token oldMethodName = methodName;
        methodName = newMethodName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.JAVA_IMPORT__METHOD_NAME, oldMethodName, methodName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Token getAsName()
    {
        return asName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAsName(Token newAsName)
    {
        Token oldAsName = asName;
        asName = newAsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.JAVA_IMPORT__AS_NAME, oldAsName, asName));
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
            case TooldefPackage.JAVA_IMPORT__PLUGIN_NAME:
                return getPluginName();
            case TooldefPackage.JAVA_IMPORT__METHOD_NAME:
                return getMethodName();
            case TooldefPackage.JAVA_IMPORT__AS_NAME:
                return getAsName();
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
            case TooldefPackage.JAVA_IMPORT__PLUGIN_NAME:
                setPluginName((Token)newValue);
                return;
            case TooldefPackage.JAVA_IMPORT__METHOD_NAME:
                setMethodName((Token)newValue);
                return;
            case TooldefPackage.JAVA_IMPORT__AS_NAME:
                setAsName((Token)newValue);
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
            case TooldefPackage.JAVA_IMPORT__PLUGIN_NAME:
                setPluginName(PLUGIN_NAME_EDEFAULT);
                return;
            case TooldefPackage.JAVA_IMPORT__METHOD_NAME:
                setMethodName(METHOD_NAME_EDEFAULT);
                return;
            case TooldefPackage.JAVA_IMPORT__AS_NAME:
                setAsName(AS_NAME_EDEFAULT);
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
            case TooldefPackage.JAVA_IMPORT__PLUGIN_NAME:
                return PLUGIN_NAME_EDEFAULT == null ? pluginName != null : !PLUGIN_NAME_EDEFAULT.equals(pluginName);
            case TooldefPackage.JAVA_IMPORT__METHOD_NAME:
                return METHOD_NAME_EDEFAULT == null ? methodName != null : !METHOD_NAME_EDEFAULT.equals(methodName);
            case TooldefPackage.JAVA_IMPORT__AS_NAME:
                return AS_NAME_EDEFAULT == null ? asName != null : !AS_NAME_EDEFAULT.equals(asName);
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
        result.append(", asName: ");
        result.append(asName);
        result.append(')');
        return result.toString();
    }

} //JavaImportImpl
