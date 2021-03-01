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
package org.eclipse.escet.chi.metamodel.chi.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.WriteStatement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Write Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.WriteStatementImpl#getValues <em>Values</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.WriteStatementImpl#isAddNewline <em>Add Newline</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WriteStatementImpl extends StatementImpl implements WriteStatement
{
    /**
     * The cached value of the '{@link #getValues() <em>Values</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValues()
     * @generated
     * @ordered
     */
    protected EList<Expression> values;

    /**
     * The default value of the '{@link #isAddNewline() <em>Add Newline</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAddNewline()
     * @generated
     * @ordered
     */
    protected static final boolean ADD_NEWLINE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAddNewline() <em>Add Newline</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAddNewline()
     * @generated
     * @ordered
     */
    protected boolean addNewline = ADD_NEWLINE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected WriteStatementImpl()
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
        return ChiPackage.Literals.WRITE_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getValues()
    {
        if (values == null)
        {
            values = new EObjectContainmentEList<Expression>(Expression.class, this, ChiPackage.WRITE_STATEMENT__VALUES);
        }
        return values;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isAddNewline()
    {
        return addNewline;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAddNewline(boolean newAddNewline)
    {
        boolean oldAddNewline = addNewline;
        addNewline = newAddNewline;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.WRITE_STATEMENT__ADD_NEWLINE, oldAddNewline, addNewline));
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
            case ChiPackage.WRITE_STATEMENT__VALUES:
                return ((InternalEList<?>)getValues()).basicRemove(otherEnd, msgs);
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
            case ChiPackage.WRITE_STATEMENT__VALUES:
                return getValues();
            case ChiPackage.WRITE_STATEMENT__ADD_NEWLINE:
                return isAddNewline();
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
            case ChiPackage.WRITE_STATEMENT__VALUES:
                getValues().clear();
                getValues().addAll((Collection<? extends Expression>)newValue);
                return;
            case ChiPackage.WRITE_STATEMENT__ADD_NEWLINE:
                setAddNewline((Boolean)newValue);
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
            case ChiPackage.WRITE_STATEMENT__VALUES:
                getValues().clear();
                return;
            case ChiPackage.WRITE_STATEMENT__ADD_NEWLINE:
                setAddNewline(ADD_NEWLINE_EDEFAULT);
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
            case ChiPackage.WRITE_STATEMENT__VALUES:
                return values != null && !values.isEmpty();
            case ChiPackage.WRITE_STATEMENT__ADD_NEWLINE:
                return addNewline != ADD_NEWLINE_EDEFAULT;
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
        result.append(" (addNewline: ");
        result.append(addNewline);
        result.append(')');
        return result.toString();
    }

} //WriteStatementImpl
