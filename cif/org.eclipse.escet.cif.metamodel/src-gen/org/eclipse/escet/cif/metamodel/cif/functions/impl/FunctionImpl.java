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
package org.eclipse.escet.cif.metamodel.cif.functions.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationImpl;

import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionImpl#getReturnTypes <em>Return Types</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionImpl#getParameters <em>Parameters</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class FunctionImpl extends DeclarationImpl implements Function
{
    /**
     * The cached value of the '{@link #getReturnTypes() <em>Return Types</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReturnTypes()
     * @generated
     * @ordered
     */
    protected EList<CifType> returnTypes;

    /**
     * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameters()
     * @generated
     * @ordered
     */
    protected EList<FunctionParameter> parameters;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FunctionImpl()
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
        return FunctionsPackage.Literals.FUNCTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<CifType> getReturnTypes()
    {
        if (returnTypes == null)
        {
            returnTypes = new EObjectContainmentEList<CifType>(CifType.class, this, FunctionsPackage.FUNCTION__RETURN_TYPES);
        }
        return returnTypes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<FunctionParameter> getParameters()
    {
        if (parameters == null)
        {
            parameters = new EObjectContainmentEList<FunctionParameter>(FunctionParameter.class, this, FunctionsPackage.FUNCTION__PARAMETERS);
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
            case FunctionsPackage.FUNCTION__RETURN_TYPES:
                return ((InternalEList<?>)getReturnTypes()).basicRemove(otherEnd, msgs);
            case FunctionsPackage.FUNCTION__PARAMETERS:
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
            case FunctionsPackage.FUNCTION__RETURN_TYPES:
                return getReturnTypes();
            case FunctionsPackage.FUNCTION__PARAMETERS:
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
            case FunctionsPackage.FUNCTION__RETURN_TYPES:
                getReturnTypes().clear();
                getReturnTypes().addAll((Collection<? extends CifType>)newValue);
                return;
            case FunctionsPackage.FUNCTION__PARAMETERS:
                getParameters().clear();
                getParameters().addAll((Collection<? extends FunctionParameter>)newValue);
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
            case FunctionsPackage.FUNCTION__RETURN_TYPES:
                getReturnTypes().clear();
                return;
            case FunctionsPackage.FUNCTION__PARAMETERS:
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
            case FunctionsPackage.FUNCTION__RETURN_TYPES:
                return returnTypes != null && !returnTypes.isEmpty();
            case FunctionsPackage.FUNCTION__PARAMETERS:
                return parameters != null && !parameters.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //FunctionImpl
