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
package org.eclipse.escet.cif.metamodel.cif.cifsvg.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Svg Move</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgMoveImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgMoveImpl#getX <em>X</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgMoveImpl#getY <em>Y</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgMoveImpl#getSvgFile <em>Svg File</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SvgMoveImpl extends IoDeclImpl implements SvgMove
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
     * The cached value of the '{@link #getX() <em>X</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getX()
     * @generated
     * @ordered
     */
    protected Expression x;

    /**
     * The cached value of the '{@link #getY() <em>Y</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getY()
     * @generated
     * @ordered
     */
    protected Expression y;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SvgMoveImpl()
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
        return CifsvgPackage.Literals.SVG_MOVE;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_MOVE__ID, oldId, newId);
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
                msgs = ((InternalEObject)id).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_MOVE__ID, null, msgs);
            if (newId != null)
                msgs = ((InternalEObject)newId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_MOVE__ID, null, msgs);
            msgs = basicSetId(newId, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_MOVE__ID, newId, newId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getX()
    {
        return x;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetX(Expression newX, NotificationChain msgs)
    {
        Expression oldX = x;
        x = newX;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_MOVE__X, oldX, newX);
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
    public void setX(Expression newX)
    {
        if (newX != x)
        {
            NotificationChain msgs = null;
            if (x != null)
                msgs = ((InternalEObject)x).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_MOVE__X, null, msgs);
            if (newX != null)
                msgs = ((InternalEObject)newX).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_MOVE__X, null, msgs);
            msgs = basicSetX(newX, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_MOVE__X, newX, newX));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getY()
    {
        return y;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetY(Expression newY, NotificationChain msgs)
    {
        Expression oldY = y;
        y = newY;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_MOVE__Y, oldY, newY);
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
    public void setY(Expression newY)
    {
        if (newY != y)
        {
            NotificationChain msgs = null;
            if (y != null)
                msgs = ((InternalEObject)y).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_MOVE__Y, null, msgs);
            if (newY != null)
                msgs = ((InternalEObject)newY).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_MOVE__Y, null, msgs);
            msgs = basicSetY(newY, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_MOVE__Y, newY, newY));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_MOVE__SVG_FILE, oldSvgFile, newSvgFile);
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
                msgs = ((InternalEObject)svgFile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_MOVE__SVG_FILE, null, msgs);
            if (newSvgFile != null)
                msgs = ((InternalEObject)newSvgFile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_MOVE__SVG_FILE, null, msgs);
            msgs = basicSetSvgFile(newSvgFile, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_MOVE__SVG_FILE, newSvgFile, newSvgFile));
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
            case CifsvgPackage.SVG_MOVE__ID:
                return basicSetId(null, msgs);
            case CifsvgPackage.SVG_MOVE__X:
                return basicSetX(null, msgs);
            case CifsvgPackage.SVG_MOVE__Y:
                return basicSetY(null, msgs);
            case CifsvgPackage.SVG_MOVE__SVG_FILE:
                return basicSetSvgFile(null, msgs);
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
            case CifsvgPackage.SVG_MOVE__ID:
                return getId();
            case CifsvgPackage.SVG_MOVE__X:
                return getX();
            case CifsvgPackage.SVG_MOVE__Y:
                return getY();
            case CifsvgPackage.SVG_MOVE__SVG_FILE:
                return getSvgFile();
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
            case CifsvgPackage.SVG_MOVE__ID:
                setId((Expression)newValue);
                return;
            case CifsvgPackage.SVG_MOVE__X:
                setX((Expression)newValue);
                return;
            case CifsvgPackage.SVG_MOVE__Y:
                setY((Expression)newValue);
                return;
            case CifsvgPackage.SVG_MOVE__SVG_FILE:
                setSvgFile((SvgFile)newValue);
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
            case CifsvgPackage.SVG_MOVE__ID:
                setId((Expression)null);
                return;
            case CifsvgPackage.SVG_MOVE__X:
                setX((Expression)null);
                return;
            case CifsvgPackage.SVG_MOVE__Y:
                setY((Expression)null);
                return;
            case CifsvgPackage.SVG_MOVE__SVG_FILE:
                setSvgFile((SvgFile)null);
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
            case CifsvgPackage.SVG_MOVE__ID:
                return id != null;
            case CifsvgPackage.SVG_MOVE__X:
                return x != null;
            case CifsvgPackage.SVG_MOVE__Y:
                return y != null;
            case CifsvgPackage.SVG_MOVE__SVG_FILE:
                return svgFile != null;
        }
        return super.eIsSet(featureID);
    }

} //SvgMoveImpl
