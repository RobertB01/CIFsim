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
package org.eclipse.escet.cif.metamodel.cif.automata.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Edge</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl#getUpdates <em>Updates</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeImpl#isUrgent <em>Urgent</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EdgeImpl extends PositionObjectImpl implements Edge
{
    /**
     * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTarget()
     * @generated
     * @ordered
     */
    protected Location target;

    /**
     * The cached value of the '{@link #getEvents() <em>Events</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEvents()
     * @generated
     * @ordered
     */
    protected EList<EdgeEvent> events;

    /**
     * The cached value of the '{@link #getGuards() <em>Guards</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGuards()
     * @generated
     * @ordered
     */
    protected EList<Expression> guards;

    /**
     * The cached value of the '{@link #getUpdates() <em>Updates</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpdates()
     * @generated
     * @ordered
     */
    protected EList<Update> updates;

    /**
     * The default value of the '{@link #isUrgent() <em>Urgent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUrgent()
     * @generated
     * @ordered
     */
    protected static final boolean URGENT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUrgent() <em>Urgent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUrgent()
     * @generated
     * @ordered
     */
    protected boolean urgent = URGENT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EdgeImpl()
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
        return AutomataPackage.Literals.EDGE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Location getTarget()
    {
        if (target != null && target.eIsProxy())
        {
            InternalEObject oldTarget = (InternalEObject)target;
            target = (Location)eResolveProxy(oldTarget);
            if (target != oldTarget)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, AutomataPackage.EDGE__TARGET, oldTarget, target));
            }
        }
        return target;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Location basicGetTarget()
    {
        return target;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTarget(Location newTarget)
    {
        Location oldTarget = target;
        target = newTarget;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.EDGE__TARGET, oldTarget, target));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<EdgeEvent> getEvents()
    {
        if (events == null)
        {
            events = new EObjectContainmentEList<EdgeEvent>(EdgeEvent.class, this, AutomataPackage.EDGE__EVENTS);
        }
        return events;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getGuards()
    {
        if (guards == null)
        {
            guards = new EObjectContainmentEList<Expression>(Expression.class, this, AutomataPackage.EDGE__GUARDS);
        }
        return guards;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Update> getUpdates()
    {
        if (updates == null)
        {
            updates = new EObjectContainmentEList<Update>(Update.class, this, AutomataPackage.EDGE__UPDATES);
        }
        return updates;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isUrgent()
    {
        return urgent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setUrgent(boolean newUrgent)
    {
        boolean oldUrgent = urgent;
        urgent = newUrgent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.EDGE__URGENT, oldUrgent, urgent));
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
            case AutomataPackage.EDGE__EVENTS:
                return ((InternalEList<?>)getEvents()).basicRemove(otherEnd, msgs);
            case AutomataPackage.EDGE__GUARDS:
                return ((InternalEList<?>)getGuards()).basicRemove(otherEnd, msgs);
            case AutomataPackage.EDGE__UPDATES:
                return ((InternalEList<?>)getUpdates()).basicRemove(otherEnd, msgs);
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
            case AutomataPackage.EDGE__TARGET:
                if (resolve) return getTarget();
                return basicGetTarget();
            case AutomataPackage.EDGE__EVENTS:
                return getEvents();
            case AutomataPackage.EDGE__GUARDS:
                return getGuards();
            case AutomataPackage.EDGE__UPDATES:
                return getUpdates();
            case AutomataPackage.EDGE__URGENT:
                return isUrgent();
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
            case AutomataPackage.EDGE__TARGET:
                setTarget((Location)newValue);
                return;
            case AutomataPackage.EDGE__EVENTS:
                getEvents().clear();
                getEvents().addAll((Collection<? extends EdgeEvent>)newValue);
                return;
            case AutomataPackage.EDGE__GUARDS:
                getGuards().clear();
                getGuards().addAll((Collection<? extends Expression>)newValue);
                return;
            case AutomataPackage.EDGE__UPDATES:
                getUpdates().clear();
                getUpdates().addAll((Collection<? extends Update>)newValue);
                return;
            case AutomataPackage.EDGE__URGENT:
                setUrgent((Boolean)newValue);
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
            case AutomataPackage.EDGE__TARGET:
                setTarget((Location)null);
                return;
            case AutomataPackage.EDGE__EVENTS:
                getEvents().clear();
                return;
            case AutomataPackage.EDGE__GUARDS:
                getGuards().clear();
                return;
            case AutomataPackage.EDGE__UPDATES:
                getUpdates().clear();
                return;
            case AutomataPackage.EDGE__URGENT:
                setUrgent(URGENT_EDEFAULT);
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
            case AutomataPackage.EDGE__TARGET:
                return target != null;
            case AutomataPackage.EDGE__EVENTS:
                return events != null && !events.isEmpty();
            case AutomataPackage.EDGE__GUARDS:
                return guards != null && !guards.isEmpty();
            case AutomataPackage.EDGE__UPDATES:
                return updates != null && !updates.isEmpty();
            case AutomataPackage.EDGE__URGENT:
                return urgent != URGENT_EDEFAULT;
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
        result.append(" (urgent: ");
        result.append(urgent);
        result.append(')');
        return result.toString();
    }

} //EdgeImpl
