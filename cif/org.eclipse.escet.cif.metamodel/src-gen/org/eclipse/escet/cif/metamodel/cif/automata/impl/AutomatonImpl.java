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

import org.eclipse.escet.cif.metamodel.cif.SupKind;

import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;

import org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Automaton</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomatonImpl#getLocations <em>Locations</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomatonImpl#getAlphabet <em>Alphabet</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomatonImpl#getMonitors <em>Monitors</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomatonImpl#getKind <em>Kind</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AutomatonImpl extends ComplexComponentImpl implements Automaton
{
    /**
     * The cached value of the '{@link #getLocations() <em>Locations</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLocations()
     * @generated
     * @ordered
     */
    protected EList<Location> locations;

    /**
     * The cached value of the '{@link #getAlphabet() <em>Alphabet</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlphabet()
     * @generated
     * @ordered
     */
    protected Alphabet alphabet;

    /**
     * The cached value of the '{@link #getMonitors() <em>Monitors</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMonitors()
     * @generated
     * @ordered
     */
    protected Monitors monitors;

    /**
     * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected static final SupKind KIND_EDEFAULT = SupKind.NONE;

    /**
     * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected SupKind kind = KIND_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AutomatonImpl()
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
        return AutomataPackage.Literals.AUTOMATON;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Location> getLocations()
    {
        if (locations == null)
        {
            locations = new EObjectContainmentEList<Location>(Location.class, this, AutomataPackage.AUTOMATON__LOCATIONS);
        }
        return locations;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Alphabet getAlphabet()
    {
        return alphabet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAlphabet(Alphabet newAlphabet, NotificationChain msgs)
    {
        Alphabet oldAlphabet = alphabet;
        alphabet = newAlphabet;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AutomataPackage.AUTOMATON__ALPHABET, oldAlphabet, newAlphabet);
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
    public void setAlphabet(Alphabet newAlphabet)
    {
        if (newAlphabet != alphabet)
        {
            NotificationChain msgs = null;
            if (alphabet != null)
                msgs = ((InternalEObject)alphabet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AutomataPackage.AUTOMATON__ALPHABET, null, msgs);
            if (newAlphabet != null)
                msgs = ((InternalEObject)newAlphabet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AutomataPackage.AUTOMATON__ALPHABET, null, msgs);
            msgs = basicSetAlphabet(newAlphabet, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.AUTOMATON__ALPHABET, newAlphabet, newAlphabet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Monitors getMonitors()
    {
        return monitors;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMonitors(Monitors newMonitors, NotificationChain msgs)
    {
        Monitors oldMonitors = monitors;
        monitors = newMonitors;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AutomataPackage.AUTOMATON__MONITORS, oldMonitors, newMonitors);
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
    public void setMonitors(Monitors newMonitors)
    {
        if (newMonitors != monitors)
        {
            NotificationChain msgs = null;
            if (monitors != null)
                msgs = ((InternalEObject)monitors).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AutomataPackage.AUTOMATON__MONITORS, null, msgs);
            if (newMonitors != null)
                msgs = ((InternalEObject)newMonitors).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AutomataPackage.AUTOMATON__MONITORS, null, msgs);
            msgs = basicSetMonitors(newMonitors, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.AUTOMATON__MONITORS, newMonitors, newMonitors));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SupKind getKind()
    {
        return kind;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setKind(SupKind newKind)
    {
        SupKind oldKind = kind;
        kind = newKind == null ? KIND_EDEFAULT : newKind;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AutomataPackage.AUTOMATON__KIND, oldKind, kind));
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
            case AutomataPackage.AUTOMATON__LOCATIONS:
                return ((InternalEList<?>)getLocations()).basicRemove(otherEnd, msgs);
            case AutomataPackage.AUTOMATON__ALPHABET:
                return basicSetAlphabet(null, msgs);
            case AutomataPackage.AUTOMATON__MONITORS:
                return basicSetMonitors(null, msgs);
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
            case AutomataPackage.AUTOMATON__LOCATIONS:
                return getLocations();
            case AutomataPackage.AUTOMATON__ALPHABET:
                return getAlphabet();
            case AutomataPackage.AUTOMATON__MONITORS:
                return getMonitors();
            case AutomataPackage.AUTOMATON__KIND:
                return getKind();
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
            case AutomataPackage.AUTOMATON__LOCATIONS:
                getLocations().clear();
                getLocations().addAll((Collection<? extends Location>)newValue);
                return;
            case AutomataPackage.AUTOMATON__ALPHABET:
                setAlphabet((Alphabet)newValue);
                return;
            case AutomataPackage.AUTOMATON__MONITORS:
                setMonitors((Monitors)newValue);
                return;
            case AutomataPackage.AUTOMATON__KIND:
                setKind((SupKind)newValue);
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
            case AutomataPackage.AUTOMATON__LOCATIONS:
                getLocations().clear();
                return;
            case AutomataPackage.AUTOMATON__ALPHABET:
                setAlphabet((Alphabet)null);
                return;
            case AutomataPackage.AUTOMATON__MONITORS:
                setMonitors((Monitors)null);
                return;
            case AutomataPackage.AUTOMATON__KIND:
                setKind(KIND_EDEFAULT);
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
            case AutomataPackage.AUTOMATON__LOCATIONS:
                return locations != null && !locations.isEmpty();
            case AutomataPackage.AUTOMATON__ALPHABET:
                return alphabet != null;
            case AutomataPackage.AUTOMATON__MONITORS:
                return monitors != null;
            case AutomataPackage.AUTOMATON__KIND:
                return kind != KIND_EDEFAULT;
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
        result.append(" (kind: ");
        result.append(kind);
        result.append(')');
        return result.toString();
    }

} //AutomatonImpl
