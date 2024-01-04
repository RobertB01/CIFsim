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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.FieldReference;
import org.eclipse.escet.chi.metamodel.chi.TupleField;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Field Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.FieldReferenceImpl#getField <em>Field</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FieldReferenceImpl extends ExpressionImpl implements FieldReference
{
    /**
     * The cached value of the '{@link #getField() <em>Field</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getField()
     * @generated
     * @ordered
     */
    protected TupleField field;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FieldReferenceImpl()
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
        return ChiPackage.Literals.FIELD_REFERENCE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TupleField getField()
    {
        if (field != null && field.eIsProxy())
        {
            InternalEObject oldField = (InternalEObject)field;
            field = (TupleField)eResolveProxy(oldField);
            if (field != oldField)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChiPackage.FIELD_REFERENCE__FIELD, oldField, field));
            }
        }
        return field;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TupleField basicGetField()
    {
        return field;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setField(TupleField newField)
    {
        TupleField oldField = field;
        field = newField;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.FIELD_REFERENCE__FIELD, oldField, field));
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
            case ChiPackage.FIELD_REFERENCE__FIELD:
                if (resolve) return getField();
                return basicGetField();
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
            case ChiPackage.FIELD_REFERENCE__FIELD:
                setField((TupleField)newValue);
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
            case ChiPackage.FIELD_REFERENCE__FIELD:
                setField((TupleField)null);
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
            case ChiPackage.FIELD_REFERENCE__FIELD:
                return field != null;
        }
        return super.eIsSet(featureID);
    }

} //FieldReferenceImpl
