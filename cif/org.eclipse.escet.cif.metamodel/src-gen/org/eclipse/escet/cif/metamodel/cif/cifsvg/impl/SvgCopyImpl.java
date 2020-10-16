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
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Svg Copy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgCopyImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgCopyImpl#getPre <em>Pre</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgCopyImpl#getPost <em>Post</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgCopyImpl#getSvgFile <em>Svg File</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SvgCopyImpl extends IoDeclImpl implements SvgCopy
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
     * The cached value of the '{@link #getPre() <em>Pre</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPre()
     * @generated
     * @ordered
     */
    protected Expression pre;

    /**
     * The cached value of the '{@link #getPost() <em>Post</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPost()
     * @generated
     * @ordered
     */
    protected Expression post;

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
    protected SvgCopyImpl()
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
        return CifsvgPackage.Literals.SVG_COPY;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_COPY__ID, oldId, newId);
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
                msgs = ((InternalEObject)id).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_COPY__ID, null, msgs);
            if (newId != null)
                msgs = ((InternalEObject)newId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_COPY__ID, null, msgs);
            msgs = basicSetId(newId, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_COPY__ID, newId, newId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getPre()
    {
        return pre;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPre(Expression newPre, NotificationChain msgs)
    {
        Expression oldPre = pre;
        pre = newPre;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_COPY__PRE, oldPre, newPre);
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
    public void setPre(Expression newPre)
    {
        if (newPre != pre)
        {
            NotificationChain msgs = null;
            if (pre != null)
                msgs = ((InternalEObject)pre).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_COPY__PRE, null, msgs);
            if (newPre != null)
                msgs = ((InternalEObject)newPre).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_COPY__PRE, null, msgs);
            msgs = basicSetPre(newPre, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_COPY__PRE, newPre, newPre));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getPost()
    {
        return post;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPost(Expression newPost, NotificationChain msgs)
    {
        Expression oldPost = post;
        post = newPost;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_COPY__POST, oldPost, newPost);
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
    public void setPost(Expression newPost)
    {
        if (newPost != post)
        {
            NotificationChain msgs = null;
            if (post != null)
                msgs = ((InternalEObject)post).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_COPY__POST, null, msgs);
            if (newPost != null)
                msgs = ((InternalEObject)newPost).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_COPY__POST, null, msgs);
            msgs = basicSetPost(newPost, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_COPY__POST, newPost, newPost));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_COPY__SVG_FILE, oldSvgFile, newSvgFile);
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
                msgs = ((InternalEObject)svgFile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_COPY__SVG_FILE, null, msgs);
            if (newSvgFile != null)
                msgs = ((InternalEObject)newSvgFile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CifsvgPackage.SVG_COPY__SVG_FILE, null, msgs);
            msgs = basicSetSvgFile(newSvgFile, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_COPY__SVG_FILE, newSvgFile, newSvgFile));
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
            case CifsvgPackage.SVG_COPY__ID:
                return basicSetId(null, msgs);
            case CifsvgPackage.SVG_COPY__PRE:
                return basicSetPre(null, msgs);
            case CifsvgPackage.SVG_COPY__POST:
                return basicSetPost(null, msgs);
            case CifsvgPackage.SVG_COPY__SVG_FILE:
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
            case CifsvgPackage.SVG_COPY__ID:
                return getId();
            case CifsvgPackage.SVG_COPY__PRE:
                return getPre();
            case CifsvgPackage.SVG_COPY__POST:
                return getPost();
            case CifsvgPackage.SVG_COPY__SVG_FILE:
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
            case CifsvgPackage.SVG_COPY__ID:
                setId((Expression)newValue);
                return;
            case CifsvgPackage.SVG_COPY__PRE:
                setPre((Expression)newValue);
                return;
            case CifsvgPackage.SVG_COPY__POST:
                setPost((Expression)newValue);
                return;
            case CifsvgPackage.SVG_COPY__SVG_FILE:
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
            case CifsvgPackage.SVG_COPY__ID:
                setId((Expression)null);
                return;
            case CifsvgPackage.SVG_COPY__PRE:
                setPre((Expression)null);
                return;
            case CifsvgPackage.SVG_COPY__POST:
                setPost((Expression)null);
                return;
            case CifsvgPackage.SVG_COPY__SVG_FILE:
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
            case CifsvgPackage.SVG_COPY__ID:
                return id != null;
            case CifsvgPackage.SVG_COPY__PRE:
                return pre != null;
            case CifsvgPackage.SVG_COPY__POST:
                return post != null;
            case CifsvgPackage.SVG_COPY__SVG_FILE:
                return svgFile != null;
        }
        return super.eIsSet(featureID);
    }

} //SvgCopyImpl
