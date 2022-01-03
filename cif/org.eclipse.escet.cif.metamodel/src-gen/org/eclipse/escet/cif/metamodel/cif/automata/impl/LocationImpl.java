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

import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Invariant;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl#getInitials <em>Initials</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl#getInvariants <em>Invariants</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl#getEdges <em>Edges</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl#getMarkeds <em>Markeds</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl#isUrgent <em>Urgent</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.LocationImpl#getEquations <em>Equations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocationImpl extends PositionObjectImpl implements Location
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
     * The cached value of the '{@link #getInitials() <em>Initials</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInitials()
     * @generated
     * @ordered
     */
    protected EList<Expression> initials;

    /**
     * The cached value of the '{@link #getInvariants() <em>Invariants</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInvariants()
     * @generated
     * @ordered
     */
    protected EList<Invariant> invariants;

    /**
     * The cached value of the '{@link #getEdges() <em>Edges</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEdges()
     * @generated
     * @ordered
     */
    protected EList<Edge> edges;

    /**
     * The cached value of the '{@link #getMarkeds() <em>Markeds</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMarkeds()
     * @generated
     * @ordered
     */
    protected EList<Expression> markeds;

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
     * The cached value of the '{@link #getEquations() <em>Equations</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEquations()
     * @generated
     * @ordered
     */
    protected EList<Equation> equations;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LocationImpl()
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
        return AutomataPackage.Literals.LOCATION;
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
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.LOCATION__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getInitials()
    {
        if (initials == null)
        {
            initials = new EObjectContainmentEList<Expression>(Expression.class, this, AutomataPackage.LOCATION__INITIALS);
        }
        return initials;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Invariant> getInvariants()
    {
        if (invariants == null)
        {
            invariants = new EObjectContainmentEList<Invariant>(Invariant.class, this, AutomataPackage.LOCATION__INVARIANTS);
        }
        return invariants;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Edge> getEdges()
    {
        if (edges == null)
        {
            edges = new EObjectContainmentEList<Edge>(Edge.class, this, AutomataPackage.LOCATION__EDGES);
        }
        return edges;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getMarkeds()
    {
        if (markeds == null)
        {
            markeds = new EObjectContainmentEList<Expression>(Expression.class, this, AutomataPackage.LOCATION__MARKEDS);
        }
        return markeds;
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
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.LOCATION__URGENT, oldUrgent, urgent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Equation> getEquations()
    {
        if (equations == null)
        {
            equations = new EObjectContainmentEList<Equation>(Equation.class, this, AutomataPackage.LOCATION__EQUATIONS);
        }
        return equations;
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
            case AutomataPackage.LOCATION__INITIALS:
                return ((InternalEList<?>)getInitials()).basicRemove(otherEnd, msgs);
            case AutomataPackage.LOCATION__INVARIANTS:
                return ((InternalEList<?>)getInvariants()).basicRemove(otherEnd, msgs);
            case AutomataPackage.LOCATION__EDGES:
                return ((InternalEList<?>)getEdges()).basicRemove(otherEnd, msgs);
            case AutomataPackage.LOCATION__MARKEDS:
                return ((InternalEList<?>)getMarkeds()).basicRemove(otherEnd, msgs);
            case AutomataPackage.LOCATION__EQUATIONS:
                return ((InternalEList<?>)getEquations()).basicRemove(otherEnd, msgs);
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
            case AutomataPackage.LOCATION__NAME:
                return getName();
            case AutomataPackage.LOCATION__INITIALS:
                return getInitials();
            case AutomataPackage.LOCATION__INVARIANTS:
                return getInvariants();
            case AutomataPackage.LOCATION__EDGES:
                return getEdges();
            case AutomataPackage.LOCATION__MARKEDS:
                return getMarkeds();
            case AutomataPackage.LOCATION__URGENT:
                return isUrgent();
            case AutomataPackage.LOCATION__EQUATIONS:
                return getEquations();
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
            case AutomataPackage.LOCATION__NAME:
                setName((String)newValue);
                return;
            case AutomataPackage.LOCATION__INITIALS:
                getInitials().clear();
                getInitials().addAll((Collection<? extends Expression>)newValue);
                return;
            case AutomataPackage.LOCATION__INVARIANTS:
                getInvariants().clear();
                getInvariants().addAll((Collection<? extends Invariant>)newValue);
                return;
            case AutomataPackage.LOCATION__EDGES:
                getEdges().clear();
                getEdges().addAll((Collection<? extends Edge>)newValue);
                return;
            case AutomataPackage.LOCATION__MARKEDS:
                getMarkeds().clear();
                getMarkeds().addAll((Collection<? extends Expression>)newValue);
                return;
            case AutomataPackage.LOCATION__URGENT:
                setUrgent((Boolean)newValue);
                return;
            case AutomataPackage.LOCATION__EQUATIONS:
                getEquations().clear();
                getEquations().addAll((Collection<? extends Equation>)newValue);
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
            case AutomataPackage.LOCATION__NAME:
                setName(NAME_EDEFAULT);
                return;
            case AutomataPackage.LOCATION__INITIALS:
                getInitials().clear();
                return;
            case AutomataPackage.LOCATION__INVARIANTS:
                getInvariants().clear();
                return;
            case AutomataPackage.LOCATION__EDGES:
                getEdges().clear();
                return;
            case AutomataPackage.LOCATION__MARKEDS:
                getMarkeds().clear();
                return;
            case AutomataPackage.LOCATION__URGENT:
                setUrgent(URGENT_EDEFAULT);
                return;
            case AutomataPackage.LOCATION__EQUATIONS:
                getEquations().clear();
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
            case AutomataPackage.LOCATION__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case AutomataPackage.LOCATION__INITIALS:
                return initials != null && !initials.isEmpty();
            case AutomataPackage.LOCATION__INVARIANTS:
                return invariants != null && !invariants.isEmpty();
            case AutomataPackage.LOCATION__EDGES:
                return edges != null && !edges.isEmpty();
            case AutomataPackage.LOCATION__MARKEDS:
                return markeds != null && !markeds.isEmpty();
            case AutomataPackage.LOCATION__URGENT:
                return urgent != URGENT_EDEFAULT;
            case AutomataPackage.LOCATION__EQUATIONS:
                return equations != null && !equations.isEmpty();
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
        result.append(", urgent: ");
        result.append(urgent);
        result.append(')');
        return result.toString();
    }

} //LocationImpl
