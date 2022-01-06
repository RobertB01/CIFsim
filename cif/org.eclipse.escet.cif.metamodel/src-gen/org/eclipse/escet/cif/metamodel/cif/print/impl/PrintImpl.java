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
package org.eclipse.escet.cif.metamodel.cif.print.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl;

import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.print.PrintPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Print</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl#getTxtPre <em>Txt Pre</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl#getTxtPost <em>Txt Post</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl#getWhenPre <em>When Pre</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl#getWhenPost <em>When Post</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl#getFile <em>File</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.impl.PrintImpl#getFors <em>Fors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PrintImpl extends IoDeclImpl implements Print
{
    /**
     * The cached value of the '{@link #getTxtPre() <em>Txt Pre</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTxtPre()
     * @generated
     * @ordered
     */
    protected Expression txtPre;

    /**
     * The cached value of the '{@link #getTxtPost() <em>Txt Post</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTxtPost()
     * @generated
     * @ordered
     */
    protected Expression txtPost;

    /**
     * The cached value of the '{@link #getWhenPre() <em>When Pre</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWhenPre()
     * @generated
     * @ordered
     */
    protected Expression whenPre;

    /**
     * The cached value of the '{@link #getWhenPost() <em>When Post</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWhenPost()
     * @generated
     * @ordered
     */
    protected Expression whenPost;

    /**
     * The cached value of the '{@link #getFile() <em>File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFile()
     * @generated
     * @ordered
     */
    protected PrintFile file;

    /**
     * The cached value of the '{@link #getFors() <em>Fors</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFors()
     * @generated
     * @ordered
     */
    protected EList<PrintFor> fors;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PrintImpl()
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
        return PrintPackage.Literals.PRINT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getTxtPre()
    {
        return txtPre;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTxtPre(Expression newTxtPre, NotificationChain msgs)
    {
        Expression oldTxtPre = txtPre;
        txtPre = newTxtPre;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__TXT_PRE, oldTxtPre, newTxtPre);
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
    public void setTxtPre(Expression newTxtPre)
    {
        if (newTxtPre != txtPre)
        {
            NotificationChain msgs = null;
            if (txtPre != null)
                msgs = ((InternalEObject)txtPre).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__TXT_PRE, null, msgs);
            if (newTxtPre != null)
                msgs = ((InternalEObject)newTxtPre).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__TXT_PRE, null, msgs);
            msgs = basicSetTxtPre(newTxtPre, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__TXT_PRE, newTxtPre, newTxtPre));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getTxtPost()
    {
        return txtPost;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTxtPost(Expression newTxtPost, NotificationChain msgs)
    {
        Expression oldTxtPost = txtPost;
        txtPost = newTxtPost;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__TXT_POST, oldTxtPost, newTxtPost);
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
    public void setTxtPost(Expression newTxtPost)
    {
        if (newTxtPost != txtPost)
        {
            NotificationChain msgs = null;
            if (txtPost != null)
                msgs = ((InternalEObject)txtPost).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__TXT_POST, null, msgs);
            if (newTxtPost != null)
                msgs = ((InternalEObject)newTxtPost).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__TXT_POST, null, msgs);
            msgs = basicSetTxtPost(newTxtPost, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__TXT_POST, newTxtPost, newTxtPost));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getWhenPre()
    {
        return whenPre;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWhenPre(Expression newWhenPre, NotificationChain msgs)
    {
        Expression oldWhenPre = whenPre;
        whenPre = newWhenPre;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__WHEN_PRE, oldWhenPre, newWhenPre);
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
    public void setWhenPre(Expression newWhenPre)
    {
        if (newWhenPre != whenPre)
        {
            NotificationChain msgs = null;
            if (whenPre != null)
                msgs = ((InternalEObject)whenPre).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__WHEN_PRE, null, msgs);
            if (newWhenPre != null)
                msgs = ((InternalEObject)newWhenPre).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__WHEN_PRE, null, msgs);
            msgs = basicSetWhenPre(newWhenPre, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__WHEN_PRE, newWhenPre, newWhenPre));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getWhenPost()
    {
        return whenPost;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWhenPost(Expression newWhenPost, NotificationChain msgs)
    {
        Expression oldWhenPost = whenPost;
        whenPost = newWhenPost;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__WHEN_POST, oldWhenPost, newWhenPost);
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
    public void setWhenPost(Expression newWhenPost)
    {
        if (newWhenPost != whenPost)
        {
            NotificationChain msgs = null;
            if (whenPost != null)
                msgs = ((InternalEObject)whenPost).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__WHEN_POST, null, msgs);
            if (newWhenPost != null)
                msgs = ((InternalEObject)newWhenPost).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__WHEN_POST, null, msgs);
            msgs = basicSetWhenPost(newWhenPost, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__WHEN_POST, newWhenPost, newWhenPost));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public PrintFile getFile()
    {
        return file;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFile(PrintFile newFile, NotificationChain msgs)
    {
        PrintFile oldFile = file;
        file = newFile;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__FILE, oldFile, newFile);
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
    public void setFile(PrintFile newFile)
    {
        if (newFile != file)
        {
            NotificationChain msgs = null;
            if (file != null)
                msgs = ((InternalEObject)file).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__FILE, null, msgs);
            if (newFile != null)
                msgs = ((InternalEObject)newFile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PrintPackage.PRINT__FILE, null, msgs);
            msgs = basicSetFile(newFile, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrintPackage.PRINT__FILE, newFile, newFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<PrintFor> getFors()
    {
        if (fors == null)
        {
            fors = new EObjectContainmentEList<PrintFor>(PrintFor.class, this, PrintPackage.PRINT__FORS);
        }
        return fors;
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
            case PrintPackage.PRINT__TXT_PRE:
                return basicSetTxtPre(null, msgs);
            case PrintPackage.PRINT__TXT_POST:
                return basicSetTxtPost(null, msgs);
            case PrintPackage.PRINT__WHEN_PRE:
                return basicSetWhenPre(null, msgs);
            case PrintPackage.PRINT__WHEN_POST:
                return basicSetWhenPost(null, msgs);
            case PrintPackage.PRINT__FILE:
                return basicSetFile(null, msgs);
            case PrintPackage.PRINT__FORS:
                return ((InternalEList<?>)getFors()).basicRemove(otherEnd, msgs);
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
            case PrintPackage.PRINT__TXT_PRE:
                return getTxtPre();
            case PrintPackage.PRINT__TXT_POST:
                return getTxtPost();
            case PrintPackage.PRINT__WHEN_PRE:
                return getWhenPre();
            case PrintPackage.PRINT__WHEN_POST:
                return getWhenPost();
            case PrintPackage.PRINT__FILE:
                return getFile();
            case PrintPackage.PRINT__FORS:
                return getFors();
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
            case PrintPackage.PRINT__TXT_PRE:
                setTxtPre((Expression)newValue);
                return;
            case PrintPackage.PRINT__TXT_POST:
                setTxtPost((Expression)newValue);
                return;
            case PrintPackage.PRINT__WHEN_PRE:
                setWhenPre((Expression)newValue);
                return;
            case PrintPackage.PRINT__WHEN_POST:
                setWhenPost((Expression)newValue);
                return;
            case PrintPackage.PRINT__FILE:
                setFile((PrintFile)newValue);
                return;
            case PrintPackage.PRINT__FORS:
                getFors().clear();
                getFors().addAll((Collection<? extends PrintFor>)newValue);
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
            case PrintPackage.PRINT__TXT_PRE:
                setTxtPre((Expression)null);
                return;
            case PrintPackage.PRINT__TXT_POST:
                setTxtPost((Expression)null);
                return;
            case PrintPackage.PRINT__WHEN_PRE:
                setWhenPre((Expression)null);
                return;
            case PrintPackage.PRINT__WHEN_POST:
                setWhenPost((Expression)null);
                return;
            case PrintPackage.PRINT__FILE:
                setFile((PrintFile)null);
                return;
            case PrintPackage.PRINT__FORS:
                getFors().clear();
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
            case PrintPackage.PRINT__TXT_PRE:
                return txtPre != null;
            case PrintPackage.PRINT__TXT_POST:
                return txtPost != null;
            case PrintPackage.PRINT__WHEN_PRE:
                return whenPre != null;
            case PrintPackage.PRINT__WHEN_POST:
                return whenPost != null;
            case PrintPackage.PRINT__FILE:
                return file != null;
            case PrintPackage.PRINT__FORS:
                return fors != null && !fors.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //PrintImpl
