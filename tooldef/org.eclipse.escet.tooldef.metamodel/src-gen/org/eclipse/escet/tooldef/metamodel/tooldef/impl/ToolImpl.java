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
package org.eclipse.escet.tooldef.metamodel.tooldef.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tool</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolImpl#getReturnTypes <em>Return Types</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolImpl#getTypeParams <em>Type Params</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.impl.ToolImpl#getParameters <em>Parameters</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ToolImpl extends DeclarationImpl implements Tool
{
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
     * The cached value of the '{@link #getReturnTypes() <em>Return Types</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReturnTypes()
     * @generated
     * @ordered
     */
    protected EList<ToolDefType> returnTypes;

    /**
     * The cached value of the '{@link #getTypeParams() <em>Type Params</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeParams()
     * @generated
     * @ordered
     */
    protected EList<TypeParam> typeParams;

    /**
     * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameters()
     * @generated
     * @ordered
     */
    protected EList<ToolParameter> parameters;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ToolImpl()
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
        return TooldefPackage.Literals.TOOL;
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
            eNotify(new ENotificationImpl(this, Notification.SET, TooldefPackage.TOOL__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<ToolDefType> getReturnTypes()
    {
        if (returnTypes == null)
        {
            returnTypes = new EObjectContainmentEList<ToolDefType>(ToolDefType.class, this, TooldefPackage.TOOL__RETURN_TYPES);
        }
        return returnTypes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<TypeParam> getTypeParams()
    {
        if (typeParams == null)
        {
            typeParams = new EObjectContainmentEList<TypeParam>(TypeParam.class, this, TooldefPackage.TOOL__TYPE_PARAMS);
        }
        return typeParams;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<ToolParameter> getParameters()
    {
        if (parameters == null)
        {
            parameters = new EObjectContainmentEList<ToolParameter>(ToolParameter.class, this, TooldefPackage.TOOL__PARAMETERS);
        }
        return parameters;
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
            case TooldefPackage.TOOL__RETURN_TYPES:
                return ((InternalEList<?>)getReturnTypes()).basicRemove(otherEnd, msgs);
            case TooldefPackage.TOOL__TYPE_PARAMS:
                return ((InternalEList<?>)getTypeParams()).basicRemove(otherEnd, msgs);
            case TooldefPackage.TOOL__PARAMETERS:
                return ((InternalEList<?>)getParameters()).basicRemove(otherEnd, msgs);
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
            case TooldefPackage.TOOL__NAME:
                return getName();
            case TooldefPackage.TOOL__RETURN_TYPES:
                return getReturnTypes();
            case TooldefPackage.TOOL__TYPE_PARAMS:
                return getTypeParams();
            case TooldefPackage.TOOL__PARAMETERS:
                return getParameters();
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
            case TooldefPackage.TOOL__NAME:
                setName((String)newValue);
                return;
            case TooldefPackage.TOOL__RETURN_TYPES:
                getReturnTypes().clear();
                getReturnTypes().addAll((Collection<? extends ToolDefType>)newValue);
                return;
            case TooldefPackage.TOOL__TYPE_PARAMS:
                getTypeParams().clear();
                getTypeParams().addAll((Collection<? extends TypeParam>)newValue);
                return;
            case TooldefPackage.TOOL__PARAMETERS:
                getParameters().clear();
                getParameters().addAll((Collection<? extends ToolParameter>)newValue);
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
            case TooldefPackage.TOOL__NAME:
                setName(NAME_EDEFAULT);
                return;
            case TooldefPackage.TOOL__RETURN_TYPES:
                getReturnTypes().clear();
                return;
            case TooldefPackage.TOOL__TYPE_PARAMS:
                getTypeParams().clear();
                return;
            case TooldefPackage.TOOL__PARAMETERS:
                getParameters().clear();
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
            case TooldefPackage.TOOL__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case TooldefPackage.TOOL__RETURN_TYPES:
                return returnTypes != null && !returnTypes.isEmpty();
            case TooldefPackage.TOOL__TYPE_PARAMS:
                return typeParams != null && !typeParams.isEmpty();
            case TooldefPackage.TOOL__PARAMETERS:
                return parameters != null && !parameters.isEmpty();
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
        result.append(" (name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }

} //ToolImpl
