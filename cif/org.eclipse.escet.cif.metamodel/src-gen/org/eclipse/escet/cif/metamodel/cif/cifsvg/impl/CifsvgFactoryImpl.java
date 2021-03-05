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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CifsvgFactoryImpl extends EFactoryImpl implements CifsvgFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static CifsvgFactory init()
    {
        try
        {
            CifsvgFactory theCifsvgFactory = (CifsvgFactory)EPackage.Registry.INSTANCE.getEFactory(CifsvgPackage.eNS_URI);
            if (theCifsvgFactory != null)
            {
                return theCifsvgFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new CifsvgFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CifsvgFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case CifsvgPackage.SVG_FILE: return createSvgFile();
            case CifsvgPackage.SVG_OUT: return createSvgOut();
            case CifsvgPackage.SVG_IN: return createSvgIn();
            case CifsvgPackage.SVG_IN_EVENT_SINGLE: return createSvgInEventSingle();
            case CifsvgPackage.SVG_IN_EVENT_IF: return createSvgInEventIf();
            case CifsvgPackage.SVG_IN_EVENT_IF_ENTRY: return createSvgInEventIfEntry();
            case CifsvgPackage.SVG_COPY: return createSvgCopy();
            case CifsvgPackage.SVG_MOVE: return createSvgMove();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgFile createSvgFile()
    {
        SvgFileImpl svgFile = new SvgFileImpl();
        return svgFile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgOut createSvgOut()
    {
        SvgOutImpl svgOut = new SvgOutImpl();
        return svgOut;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgIn createSvgIn()
    {
        SvgInImpl svgIn = new SvgInImpl();
        return svgIn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgInEventSingle createSvgInEventSingle()
    {
        SvgInEventSingleImpl svgInEventSingle = new SvgInEventSingleImpl();
        return svgInEventSingle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgInEventIf createSvgInEventIf()
    {
        SvgInEventIfImpl svgInEventIf = new SvgInEventIfImpl();
        return svgInEventIf;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgInEventIfEntry createSvgInEventIfEntry()
    {
        SvgInEventIfEntryImpl svgInEventIfEntry = new SvgInEventIfEntryImpl();
        return svgInEventIfEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgCopy createSvgCopy()
    {
        SvgCopyImpl svgCopy = new SvgCopyImpl();
        return svgCopy;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SvgMove createSvgMove()
    {
        SvgMoveImpl svgMove = new SvgMoveImpl();
        return svgMove;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CifsvgPackage getCifsvgPackage()
    {
        return (CifsvgPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static CifsvgPackage getPackage()
    {
        return CifsvgPackage.eINSTANCE;
    }

} //CifsvgFactoryImpl
