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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tool Ref</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolRefImpl#isBuiltin <em>Builtin</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolRefImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ToolRefImpl#getTool <em>Tool</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ToolRefImpl extends PositionObjectImpl implements ToolRef
{
    /**
     * The default value of the '{@link #isBuiltin() <em>Builtin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isBuiltin()
     * @generated
     * @ordered
     */
    protected static final boolean BUILTIN_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isBuiltin() <em>Builtin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isBuiltin()
     * @generated
     * @ordered
     */
    protected boolean builtin = BUILTIN_EDEFAULT;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getTool() <em>Tool</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTool()
     * @generated
     * @ordered
     */
    protected Tool tool;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ToolRefImpl()
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
        return ExpressionsPackage.Literals.TOOL_REF;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isBuiltin()
    {
        return builtin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setBuiltin(boolean newBuiltin)
    {
        boolean oldBuiltin = builtin;
        builtin = newBuiltin;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.TOOL_REF__BUILTIN, oldBuiltin, builtin));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setName(String newName)
    {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.TOOL_REF__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Tool getTool()
    {
        if (tool != null && tool.eIsProxy())
        {
            InternalEObject oldTool = (InternalEObject)tool;
            tool = (Tool)eResolveProxy(oldTool);
            if (tool != oldTool)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.TOOL_REF__TOOL, oldTool, tool));
            }
        }
        return tool;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Tool basicGetTool()
    {
        return tool;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTool(Tool newTool)
    {
        Tool oldTool = tool;
        tool = newTool;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.TOOL_REF__TOOL, oldTool, tool));
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
            case ExpressionsPackage.TOOL_REF__BUILTIN:
                return isBuiltin();
            case ExpressionsPackage.TOOL_REF__NAME:
                return getName();
            case ExpressionsPackage.TOOL_REF__TOOL:
                if (resolve) return getTool();
                return basicGetTool();
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
            case ExpressionsPackage.TOOL_REF__BUILTIN:
                setBuiltin((Boolean)newValue);
                return;
            case ExpressionsPackage.TOOL_REF__NAME:
                setName((String)newValue);
                return;
            case ExpressionsPackage.TOOL_REF__TOOL:
                setTool((Tool)newValue);
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
            case ExpressionsPackage.TOOL_REF__BUILTIN:
                setBuiltin(BUILTIN_EDEFAULT);
                return;
            case ExpressionsPackage.TOOL_REF__NAME:
                setName(NAME_EDEFAULT);
                return;
            case ExpressionsPackage.TOOL_REF__TOOL:
                setTool((Tool)null);
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
            case ExpressionsPackage.TOOL_REF__BUILTIN:
                return builtin != BUILTIN_EDEFAULT;
            case ExpressionsPackage.TOOL_REF__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case ExpressionsPackage.TOOL_REF__TOOL:
                return tool != null;
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
        result.append(" (builtin: ");
        result.append(builtin);
        result.append(", name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }

} //ToolRefImpl
