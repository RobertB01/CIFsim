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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Matrix Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.MatrixTypeImpl#getRowSize <em>Row Size</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.MatrixTypeImpl#getColumnSize <em>Column Size</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MatrixTypeImpl extends TypeImpl implements MatrixType
{
    /**
     * The cached value of the '{@link #getRowSize() <em>Row Size</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRowSize()
     * @generated
     * @ordered
     */
    protected Expression rowSize;

    /**
     * The cached value of the '{@link #getColumnSize() <em>Column Size</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColumnSize()
     * @generated
     * @ordered
     */
    protected Expression columnSize;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MatrixTypeImpl()
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
        return ChiPackage.Literals.MATRIX_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getRowSize()
    {
        return rowSize;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRowSize(Expression newRowSize, NotificationChain msgs)
    {
        Expression oldRowSize = rowSize;
        rowSize = newRowSize;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.MATRIX_TYPE__ROW_SIZE, oldRowSize, newRowSize);
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
    public void setRowSize(Expression newRowSize)
    {
        if (newRowSize != rowSize)
        {
            NotificationChain msgs = null;
            if (rowSize != null)
                msgs = ((InternalEObject)rowSize).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.MATRIX_TYPE__ROW_SIZE, null, msgs);
            if (newRowSize != null)
                msgs = ((InternalEObject)newRowSize).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.MATRIX_TYPE__ROW_SIZE, null, msgs);
            msgs = basicSetRowSize(newRowSize, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.MATRIX_TYPE__ROW_SIZE, newRowSize, newRowSize));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getColumnSize()
    {
        return columnSize;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetColumnSize(Expression newColumnSize, NotificationChain msgs)
    {
        Expression oldColumnSize = columnSize;
        columnSize = newColumnSize;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.MATRIX_TYPE__COLUMN_SIZE, oldColumnSize, newColumnSize);
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
    public void setColumnSize(Expression newColumnSize)
    {
        if (newColumnSize != columnSize)
        {
            NotificationChain msgs = null;
            if (columnSize != null)
                msgs = ((InternalEObject)columnSize).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.MATRIX_TYPE__COLUMN_SIZE, null, msgs);
            if (newColumnSize != null)
                msgs = ((InternalEObject)newColumnSize).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.MATRIX_TYPE__COLUMN_SIZE, null, msgs);
            msgs = basicSetColumnSize(newColumnSize, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.MATRIX_TYPE__COLUMN_SIZE, newColumnSize, newColumnSize));
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
            case ChiPackage.MATRIX_TYPE__ROW_SIZE:
                return basicSetRowSize(null, msgs);
            case ChiPackage.MATRIX_TYPE__COLUMN_SIZE:
                return basicSetColumnSize(null, msgs);
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
            case ChiPackage.MATRIX_TYPE__ROW_SIZE:
                return getRowSize();
            case ChiPackage.MATRIX_TYPE__COLUMN_SIZE:
                return getColumnSize();
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
            case ChiPackage.MATRIX_TYPE__ROW_SIZE:
                setRowSize((Expression)newValue);
                return;
            case ChiPackage.MATRIX_TYPE__COLUMN_SIZE:
                setColumnSize((Expression)newValue);
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
            case ChiPackage.MATRIX_TYPE__ROW_SIZE:
                setRowSize((Expression)null);
                return;
            case ChiPackage.MATRIX_TYPE__COLUMN_SIZE:
                setColumnSize((Expression)null);
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
            case ChiPackage.MATRIX_TYPE__ROW_SIZE:
                return rowSize != null;
            case ChiPackage.MATRIX_TYPE__COLUMN_SIZE:
                return columnSize != null;
        }
        return super.eIsSet(featureID);
    }

} //MatrixTypeImpl
