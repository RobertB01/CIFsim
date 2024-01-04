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
package org.eclipse.escet.cif.metamodel.cif.expressions.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.automata.Location;

import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Location Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.LocationExpressionImpl#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocationExpressionImpl extends ExpressionImpl implements LocationExpression
{
    /**
     * The cached value of the '{@link #getLocation() <em>Location</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLocation()
     * @generated
     * @ordered
     */
    protected Location location;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LocationExpressionImpl()
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
        return ExpressionsPackage.Literals.LOCATION_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Location getLocation()
    {
        if (location != null && location.eIsProxy())
        {
            InternalEObject oldLocation = (InternalEObject)location;
            location = (Location)eResolveProxy(oldLocation);
            if (location != oldLocation)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.LOCATION_EXPRESSION__LOCATION, oldLocation, location));
            }
        }
        return location;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Location basicGetLocation()
    {
        return location;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLocation(Location newLocation)
    {
        Location oldLocation = location;
        location = newLocation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.LOCATION_EXPRESSION__LOCATION, oldLocation, location));
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
            case ExpressionsPackage.LOCATION_EXPRESSION__LOCATION:
                if (resolve) return getLocation();
                return basicGetLocation();
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
            case ExpressionsPackage.LOCATION_EXPRESSION__LOCATION:
                setLocation((Location)newValue);
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
            case ExpressionsPackage.LOCATION_EXPRESSION__LOCATION:
                setLocation((Location)null);
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
            case ExpressionsPackage.LOCATION_EXPRESSION__LOCATION:
                return location != null;
        }
        return super.eIsSet(featureID);
    }

} //LocationExpressionImpl
