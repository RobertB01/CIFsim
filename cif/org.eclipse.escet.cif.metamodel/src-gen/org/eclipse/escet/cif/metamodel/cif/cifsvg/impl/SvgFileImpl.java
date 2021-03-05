/**
 * Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;

import org.eclipse.escet.cif.metamodel.cif.impl.IoDeclImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Svg File</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.SvgFileImpl#getPath <em>Path</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SvgFileImpl extends IoDeclImpl implements SvgFile
{
    /**
     * The default value of the '{@link #getPath() <em>Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPath()
     * @generated
     * @ordered
     */
    protected static final String PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPath() <em>Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPath()
     * @generated
     * @ordered
     */
    protected String path = PATH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SvgFileImpl()
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
        return CifsvgPackage.Literals.SVG_FILE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getPath()
    {
        return path;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setPath(String newPath)
    {
        String oldPath = path;
        path = newPath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CifsvgPackage.SVG_FILE__PATH, oldPath, path));
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
            case CifsvgPackage.SVG_FILE__PATH:
                return getPath();
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
            case CifsvgPackage.SVG_FILE__PATH:
                setPath((String)newValue);
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
            case CifsvgPackage.SVG_FILE__PATH:
                setPath(PATH_EDEFAULT);
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
            case CifsvgPackage.SVG_FILE__PATH:
                return PATH_EDEFAULT == null ? path != null : !PATH_EDEFAULT.equals(path);
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
        result.append(" (path: ");
        result.append(path);
        result.append(')');
        return result.toString();
    }

} //SvgFileImpl
