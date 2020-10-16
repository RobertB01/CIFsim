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
package org.eclipse.escet.cif.metamodel.cif.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Invariant</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.InvariantImpl#getSupKind <em>Sup Kind</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.InvariantImpl#getPredicate <em>Predicate</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.InvariantImpl#getInvKind <em>Inv Kind</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.InvariantImpl#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InvariantImpl extends PositionObjectImpl implements Invariant
{
    /**
     * The default value of the '{@link #getSupKind() <em>Sup Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupKind()
     * @generated
     * @ordered
     */
    protected static final SupKind SUP_KIND_EDEFAULT = SupKind.NONE;

    /**
     * The cached value of the '{@link #getSupKind() <em>Sup Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupKind()
     * @generated
     * @ordered
     */
    protected SupKind supKind = SUP_KIND_EDEFAULT;

    /**
     * The cached value of the '{@link #getPredicate() <em>Predicate</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPredicate()
     * @generated
     * @ordered
     */
    protected Expression predicate;

    /**
     * The default value of the '{@link #getInvKind() <em>Inv Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInvKind()
     * @generated
     * @ordered
     */
    protected static final InvKind INV_KIND_EDEFAULT = InvKind.STATE;

    /**
     * The cached value of the '{@link #getInvKind() <em>Inv Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInvKind()
     * @generated
     * @ordered
     */
    protected InvKind invKind = INV_KIND_EDEFAULT;

    /**
     * The cached value of the '{@link #getEvent() <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEvent()
     * @generated
     * @ordered
     */
    protected Expression event;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InvariantImpl()
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
        return CifPackage.Literals.INVARIANT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SupKind getSupKind()
    {
        return supKind;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSupKind(SupKind newSupKind)
    {
        SupKind oldSupKind = supKind;
        supKind = newSupKind == null ? SUP_KIND_EDEFAULT : newSupKind;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifPackage.INVARIANT__SUP_KIND, oldSupKind, supKind));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getPredicate()
    {
        return predicate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPredicate(Expression newPredicate, NotificationChain msgs)
    {
        Expression oldPredicate = predicate;
        predicate = newPredicate;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifPackage.INVARIANT__PREDICATE, oldPredicate, newPredicate);
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
    public void setPredicate(Expression newPredicate)
    {
        if (newPredicate != predicate)
        {
            NotificationChain msgs = null;
            if (predicate != null)
                msgs = ((InternalEObject)predicate).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifPackage.INVARIANT__PREDICATE, null, msgs);
            if (newPredicate != null)
                msgs = ((InternalEObject)newPredicate).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifPackage.INVARIANT__PREDICATE, null, msgs);
            msgs = basicSetPredicate(newPredicate, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifPackage.INVARIANT__PREDICATE, newPredicate, newPredicate));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public InvKind getInvKind()
    {
        return invKind;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setInvKind(InvKind newInvKind)
    {
        InvKind oldInvKind = invKind;
        invKind = newInvKind == null ? INV_KIND_EDEFAULT : newInvKind;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifPackage.INVARIANT__INV_KIND, oldInvKind, invKind));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getEvent()
    {
        return event;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEvent(Expression newEvent, NotificationChain msgs)
    {
        Expression oldEvent = event;
        event = newEvent;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifPackage.INVARIANT__EVENT, oldEvent, newEvent);
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
    public void setEvent(Expression newEvent)
    {
        if (newEvent != event)
        {
            NotificationChain msgs = null;
            if (event != null)
                msgs = ((InternalEObject)event).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifPackage.INVARIANT__EVENT, null, msgs);
            if (newEvent != null)
                msgs = ((InternalEObject)newEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifPackage.INVARIANT__EVENT, null, msgs);
            msgs = basicSetEvent(newEvent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifPackage.INVARIANT__EVENT, newEvent, newEvent));
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
            case CifPackage.INVARIANT__PREDICATE:
                return basicSetPredicate(null, msgs);
            case CifPackage.INVARIANT__EVENT:
                return basicSetEvent(null, msgs);
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
            case CifPackage.INVARIANT__SUP_KIND:
                return getSupKind();
            case CifPackage.INVARIANT__PREDICATE:
                return getPredicate();
            case CifPackage.INVARIANT__INV_KIND:
                return getInvKind();
            case CifPackage.INVARIANT__EVENT:
                return getEvent();
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
            case CifPackage.INVARIANT__SUP_KIND:
                setSupKind((SupKind)newValue);
                return;
            case CifPackage.INVARIANT__PREDICATE:
                setPredicate((Expression)newValue);
                return;
            case CifPackage.INVARIANT__INV_KIND:
                setInvKind((InvKind)newValue);
                return;
            case CifPackage.INVARIANT__EVENT:
                setEvent((Expression)newValue);
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
            case CifPackage.INVARIANT__SUP_KIND:
                setSupKind(SUP_KIND_EDEFAULT);
                return;
            case CifPackage.INVARIANT__PREDICATE:
                setPredicate((Expression)null);
                return;
            case CifPackage.INVARIANT__INV_KIND:
                setInvKind(INV_KIND_EDEFAULT);
                return;
            case CifPackage.INVARIANT__EVENT:
                setEvent((Expression)null);
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
            case CifPackage.INVARIANT__SUP_KIND:
                return supKind != SUP_KIND_EDEFAULT;
            case CifPackage.INVARIANT__PREDICATE:
                return predicate != null;
            case CifPackage.INVARIANT__INV_KIND:
                return invKind != INV_KIND_EDEFAULT;
            case CifPackage.INVARIANT__EVENT:
                return event != null;
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
        result.append(" (supKind: ");
        result.append(supKind);
        result.append(", invKind: ");
        result.append(invKind);
        result.append(')');
        return result.toString();
    }

} //InvariantImpl
