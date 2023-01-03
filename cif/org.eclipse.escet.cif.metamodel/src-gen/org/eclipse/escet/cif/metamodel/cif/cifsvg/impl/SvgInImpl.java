/**
 * Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif.cifsvg.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Svg In</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInImpl#getSvgFile <em>Svg File</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgInImpl#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SvgInImpl extends IoDeclImpl implements SvgIn
{
    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected Expression id;

    /**
     * The cached value of the '{@link #getSvgFile() <em>Svg File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSvgFile()
     * @generated
     * @ordered
     */
    protected SvgFile svgFile;

    /**
     * The cached value of the '{@link #getEvent() <em>Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEvent()
     * @generated
     * @ordered
     */
    protected SvgInEvent event;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SvgInImpl()
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
        return CifsvgPackage.Literals.SVG_IN;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getId()
    {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetId(Expression newId, NotificationChain msgs)
    {
        Expression oldId = id;
        id = newId;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_IN__ID, oldId, newId);
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
    public void setId(Expression newId)
    {
        if (newId != id)
        {
            NotificationChain msgs = null;
            if (id != null)
                msgs = ((InternalEObject)id).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_IN__ID, null, msgs);
            if (newId != null)
                msgs = ((InternalEObject)newId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_IN__ID, null, msgs);
            msgs = basicSetId(newId, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_IN__ID, newId, newId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgFile getSvgFile()
    {
        return svgFile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSvgFile(SvgFile newSvgFile, NotificationChain msgs)
    {
        SvgFile oldSvgFile = svgFile;
        svgFile = newSvgFile;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_IN__SVG_FILE, oldSvgFile, newSvgFile);
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
    public void setSvgFile(SvgFile newSvgFile)
    {
        if (newSvgFile != svgFile)
        {
            NotificationChain msgs = null;
            if (svgFile != null)
                msgs = ((InternalEObject)svgFile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_IN__SVG_FILE, null, msgs);
            if (newSvgFile != null)
                msgs = ((InternalEObject)newSvgFile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_IN__SVG_FILE, null, msgs);
            msgs = basicSetSvgFile(newSvgFile, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_IN__SVG_FILE, newSvgFile, newSvgFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgInEvent getEvent()
    {
        return event;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEvent(SvgInEvent newEvent, NotificationChain msgs)
    {
        SvgInEvent oldEvent = event;
        event = newEvent;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_IN__EVENT, oldEvent, newEvent);
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
    public void setEvent(SvgInEvent newEvent)
    {
        if (newEvent != event)
        {
            NotificationChain msgs = null;
            if (event != null)
                msgs = ((InternalEObject)event).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_IN__EVENT, null, msgs);
            if (newEvent != null)
                msgs = ((InternalEObject)newEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_IN__EVENT, null, msgs);
            msgs = basicSetEvent(newEvent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_IN__EVENT, newEvent, newEvent));
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
            case CifsvgPackage.SVG_IN__ID:
                return basicSetId(null, msgs);
            case CifsvgPackage.SVG_IN__SVG_FILE:
                return basicSetSvgFile(null, msgs);
            case CifsvgPackage.SVG_IN__EVENT:
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
            case CifsvgPackage.SVG_IN__ID:
                return getId();
            case CifsvgPackage.SVG_IN__SVG_FILE:
                return getSvgFile();
            case CifsvgPackage.SVG_IN__EVENT:
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
            case CifsvgPackage.SVG_IN__ID:
                setId((Expression)newValue);
                return;
            case CifsvgPackage.SVG_IN__SVG_FILE:
                setSvgFile((SvgFile)newValue);
                return;
            case CifsvgPackage.SVG_IN__EVENT:
                setEvent((SvgInEvent)newValue);
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
            case CifsvgPackage.SVG_IN__ID:
                setId((Expression)null);
                return;
            case CifsvgPackage.SVG_IN__SVG_FILE:
                setSvgFile((SvgFile)null);
                return;
            case CifsvgPackage.SVG_IN__EVENT:
                setEvent((SvgInEvent)null);
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
            case CifsvgPackage.SVG_IN__ID:
                return id != null;
            case CifsvgPackage.SVG_IN__SVG_FILE:
                return svgFile != null;
            case CifsvgPackage.SVG_IN__EVENT:
                return event != null;
        }
        return super.eIsSet(featureID);
    }

} //SvgInImpl
