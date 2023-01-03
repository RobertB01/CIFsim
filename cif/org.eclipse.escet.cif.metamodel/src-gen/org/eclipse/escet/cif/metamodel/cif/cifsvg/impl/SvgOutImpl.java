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
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl;

import org.eclipse.escet.common.position.metamodel.position.Position;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Svg Out</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl#getAttr <em>Attr</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl#getSvgFile <em>Svg File</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl#getAttrTextPos <em>Attr Text Pos</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgOutImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SvgOutImpl extends IoDeclImpl implements SvgOut
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
     * The default value of the '{@link #getAttr() <em>Attr</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttr()
     * @generated
     * @ordered
     */
    protected static final String ATTR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAttr() <em>Attr</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttr()
     * @generated
     * @ordered
     */
    protected String attr = ATTR_EDEFAULT;

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
     * The cached value of the '{@link #getAttrTextPos() <em>Attr Text Pos</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttrTextPos()
     * @generated
     * @ordered
     */
    protected Position attrTextPos;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected Expression value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SvgOutImpl()
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
        return CifsvgPackage.Literals.SVG_OUT;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__ID, oldId, newId);
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
                msgs = ((InternalEObject)id).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_OUT__ID, null, msgs);
            if (newId != null)
                msgs = ((InternalEObject)newId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_OUT__ID, null, msgs);
            msgs = basicSetId(newId, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__ID, newId, newId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getAttr()
    {
        return attr;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAttr(String newAttr)
    {
        String oldAttr = attr;
        attr = newAttr;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__ATTR, oldAttr, attr));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__SVG_FILE, oldSvgFile, newSvgFile);
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
                msgs = ((InternalEObject)svgFile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_OUT__SVG_FILE, null, msgs);
            if (newSvgFile != null)
                msgs = ((InternalEObject)newSvgFile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_OUT__SVG_FILE, null, msgs);
            msgs = basicSetSvgFile(newSvgFile, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__SVG_FILE, newSvgFile, newSvgFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Position getAttrTextPos()
    {
        return attrTextPos;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAttrTextPos(Position newAttrTextPos, NotificationChain msgs)
    {
        Position oldAttrTextPos = attrTextPos;
        attrTextPos = newAttrTextPos;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__ATTR_TEXT_POS, oldAttrTextPos, newAttrTextPos);
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
    public void setAttrTextPos(Position newAttrTextPos)
    {
        if (newAttrTextPos != attrTextPos)
        {
            NotificationChain msgs = null;
            if (attrTextPos != null)
                msgs = ((InternalEObject)attrTextPos).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_OUT__ATTR_TEXT_POS, null, msgs);
            if (newAttrTextPos != null)
                msgs = ((InternalEObject)newAttrTextPos).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_OUT__ATTR_TEXT_POS, null, msgs);
            msgs = basicSetAttrTextPos(newAttrTextPos, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__ATTR_TEXT_POS, newAttrTextPos, newAttrTextPos));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getValue()
    {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValue(Expression newValue, NotificationChain msgs)
    {
        Expression oldValue = value;
        value = newValue;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__VALUE, oldValue, newValue);
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
    public void setValue(Expression newValue)
    {
        if (newValue != value)
        {
            NotificationChain msgs = null;
            if (value != null)
                msgs = ((InternalEObject)value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_OUT__VALUE, null, msgs);
            if (newValue != null)
                msgs = ((InternalEObject)newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_OUT__VALUE, null, msgs);
            msgs = basicSetValue(newValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_OUT__VALUE, newValue, newValue));
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
            case CifsvgPackage.SVG_OUT__ID:
                return basicSetId(null, msgs);
            case CifsvgPackage.SVG_OUT__SVG_FILE:
                return basicSetSvgFile(null, msgs);
            case CifsvgPackage.SVG_OUT__ATTR_TEXT_POS:
                return basicSetAttrTextPos(null, msgs);
            case CifsvgPackage.SVG_OUT__VALUE:
                return basicSetValue(null, msgs);
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
            case CifsvgPackage.SVG_OUT__ID:
                return getId();
            case CifsvgPackage.SVG_OUT__ATTR:
                return getAttr();
            case CifsvgPackage.SVG_OUT__SVG_FILE:
                return getSvgFile();
            case CifsvgPackage.SVG_OUT__ATTR_TEXT_POS:
                return getAttrTextPos();
            case CifsvgPackage.SVG_OUT__VALUE:
                return getValue();
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
            case CifsvgPackage.SVG_OUT__ID:
                setId((Expression)newValue);
                return;
            case CifsvgPackage.SVG_OUT__ATTR:
                setAttr((String)newValue);
                return;
            case CifsvgPackage.SVG_OUT__SVG_FILE:
                setSvgFile((SvgFile)newValue);
                return;
            case CifsvgPackage.SVG_OUT__ATTR_TEXT_POS:
                setAttrTextPos((Position)newValue);
                return;
            case CifsvgPackage.SVG_OUT__VALUE:
                setValue((Expression)newValue);
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
            case CifsvgPackage.SVG_OUT__ID:
                setId((Expression)null);
                return;
            case CifsvgPackage.SVG_OUT__ATTR:
                setAttr(ATTR_EDEFAULT);
                return;
            case CifsvgPackage.SVG_OUT__SVG_FILE:
                setSvgFile((SvgFile)null);
                return;
            case CifsvgPackage.SVG_OUT__ATTR_TEXT_POS:
                setAttrTextPos((Position)null);
                return;
            case CifsvgPackage.SVG_OUT__VALUE:
                setValue((Expression)null);
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
            case CifsvgPackage.SVG_OUT__ID:
                return id != null;
            case CifsvgPackage.SVG_OUT__ATTR:
                return ATTR_EDEFAULT == null ? attr != null : !ATTR_EDEFAULT.equals(attr);
            case CifsvgPackage.SVG_OUT__SVG_FILE:
                return svgFile != null;
            case CifsvgPackage.SVG_OUT__ATTR_TEXT_POS:
                return attrTextPos != null;
            case CifsvgPackage.SVG_OUT__VALUE:
                return value != null;
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
        result.append(" (attr: ");
        result.append(attr);
        result.append(')');
        return result.toString();
    }

} //SvgOutImpl
