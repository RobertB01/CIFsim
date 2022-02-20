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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.AssignmentStatement;
import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Expression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assignment Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.AssignmentStatementImpl#getLhs <em>Lhs</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.AssignmentStatementImpl#getRhs <em>Rhs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssignmentStatementImpl extends StatementImpl implements AssignmentStatement
{
    /**
     * The cached value of the '{@link #getLhs() <em>Lhs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLhs()
     * @generated
     * @ordered
     */
    protected Expression lhs;

    /**
     * The cached value of the '{@link #getRhs() <em>Rhs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRhs()
     * @generated
     * @ordered
     */
    protected Expression rhs;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AssignmentStatementImpl()
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
        return ChiPackage.Literals.ASSIGNMENT_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getLhs()
    {
        return lhs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLhs(Expression newLhs, NotificationChain msgs)
    {
        Expression oldLhs = lhs;
        lhs = newLhs;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.ASSIGNMENT_STATEMENT__LHS, oldLhs, newLhs);
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
    public void setLhs(Expression newLhs)
    {
        if (newLhs != lhs)
        {
            NotificationChain msgs = null;
            if (lhs != null)
                msgs = ((InternalEObject)lhs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.ASSIGNMENT_STATEMENT__LHS, null, msgs);
            if (newLhs != null)
                msgs = ((InternalEObject)newLhs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.ASSIGNMENT_STATEMENT__LHS, null, msgs);
            msgs = basicSetLhs(newLhs, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.ASSIGNMENT_STATEMENT__LHS, newLhs, newLhs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getRhs()
    {
        return rhs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRhs(Expression newRhs, NotificationChain msgs)
    {
        Expression oldRhs = rhs;
        rhs = newRhs;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.ASSIGNMENT_STATEMENT__RHS, oldRhs, newRhs);
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
    public void setRhs(Expression newRhs)
    {
        if (newRhs != rhs)
        {
            NotificationChain msgs = null;
            if (rhs != null)
                msgs = ((InternalEObject)rhs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.ASSIGNMENT_STATEMENT__RHS, null, msgs);
            if (newRhs != null)
                msgs = ((InternalEObject)newRhs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.ASSIGNMENT_STATEMENT__RHS, null, msgs);
            msgs = basicSetRhs(newRhs, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.ASSIGNMENT_STATEMENT__RHS, newRhs, newRhs));
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
            case ChiPackage.ASSIGNMENT_STATEMENT__LHS:
                return basicSetLhs(null, msgs);
            case ChiPackage.ASSIGNMENT_STATEMENT__RHS:
                return basicSetRhs(null, msgs);
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
            case ChiPackage.ASSIGNMENT_STATEMENT__LHS:
                return getLhs();
            case ChiPackage.ASSIGNMENT_STATEMENT__RHS:
                return getRhs();
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
            case ChiPackage.ASSIGNMENT_STATEMENT__LHS:
                setLhs((Expression)newValue);
                return;
            case ChiPackage.ASSIGNMENT_STATEMENT__RHS:
                setRhs((Expression)newValue);
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
            case ChiPackage.ASSIGNMENT_STATEMENT__LHS:
                setLhs((Expression)null);
                return;
            case ChiPackage.ASSIGNMENT_STATEMENT__RHS:
                setRhs((Expression)null);
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
            case ChiPackage.ASSIGNMENT_STATEMENT__LHS:
                return lhs != null;
            case ChiPackage.ASSIGNMENT_STATEMENT__RHS:
                return rhs != null;
        }
        return super.eIsSet(featureID);
    }

} //AssignmentStatementImpl
