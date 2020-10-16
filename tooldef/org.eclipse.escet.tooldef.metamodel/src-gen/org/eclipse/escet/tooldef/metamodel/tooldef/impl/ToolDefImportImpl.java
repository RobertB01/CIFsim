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
package org.eclipse.escet.tooldef.metamodel.tooldef.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.setext.runtime.Token;

import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport;
import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tool Def Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefImportImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefImportImpl#getOrigName <em>Orig Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolDefImportImpl#getAsName <em>As Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ToolDefImportImpl extends ImportImpl implements ToolDefImport
{
    /**
     * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected static final Token SOURCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected Token source = SOURCE_EDEFAULT;

    /**
     * The default value of the '{@link #getOrigName() <em>Orig Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrigName()
     * @generated
     * @ordered
     */
    protected static final Token ORIG_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOrigName() <em>Orig Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrigName()
     * @generated
     * @ordered
     */
    protected Token origName = ORIG_NAME_EDEFAULT;

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
    protected ToolDefImportImpl()
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
        return TooldefPackage.Literals.TOOL_DEF_IMPORT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Token getSource()
    {
        return source;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSource(Token newSource)
    {
        Token oldSource = source;
        source = newSource;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.TOOL_DEF_IMPORT__SOURCE, oldSource, source));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Token getOrigName()
    {
        return origName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setOrigName(Token newOrigName)
    {
        Token oldOrigName = origName;
        origName = newOrigName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.TOOL_DEF_IMPORT__ORIG_NAME, oldOrigName, origName));
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
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.TOOL_DEF_IMPORT__AS_NAME, oldAsName, asName));
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
            case TooldefPackage.TOOL_DEF_IMPORT__SOURCE:
                return getSource();
            case TooldefPackage.TOOL_DEF_IMPORT__ORIG_NAME:
                return getOrigName();
            case TooldefPackage.TOOL_DEF_IMPORT__AS_NAME:
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
            case TooldefPackage.TOOL_DEF_IMPORT__SOURCE:
                setSource((Token)newValue);
                return;
            case TooldefPackage.TOOL_DEF_IMPORT__ORIG_NAME:
                setOrigName((Token)newValue);
                return;
            case TooldefPackage.TOOL_DEF_IMPORT__AS_NAME:
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
            case TooldefPackage.TOOL_DEF_IMPORT__SOURCE:
                setSource(SOURCE_EDEFAULT);
                return;
            case TooldefPackage.TOOL_DEF_IMPORT__ORIG_NAME:
                setOrigName(ORIG_NAME_EDEFAULT);
                return;
            case TooldefPackage.TOOL_DEF_IMPORT__AS_NAME:
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
            case TooldefPackage.TOOL_DEF_IMPORT__SOURCE:
                return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
            case TooldefPackage.TOOL_DEF_IMPORT__ORIG_NAME:
                return ORIG_NAME_EDEFAULT == null ? origName != null : !ORIG_NAME_EDEFAULT.equals(origName);
            case TooldefPackage.TOOL_DEF_IMPORT__AS_NAME:
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
        result.append(" (source: ");
        result.append(source);
        result.append(", origName: ");
        result.append(origName);
        result.append(", asName: ");
        result.append(asName);
        result.append(')');
        return result.toString();
    }

} //ToolDefImportImpl
