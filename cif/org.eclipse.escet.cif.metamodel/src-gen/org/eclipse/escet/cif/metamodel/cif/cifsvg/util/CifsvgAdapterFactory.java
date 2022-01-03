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
package org.eclipse.escet.cif.metamodel.cif.cifsvg.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.escet.cif.metamodel.cif.IoDecl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.*;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage
 * @generated
 */
public class CifsvgAdapterFactory extends AdapterFactoryImpl
{
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static CifsvgPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CifsvgAdapterFactory()
    {
        if (modelPackage == null)
        {
            modelPackage = CifsvgPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object)
    {
        if (object == modelPackage)
        {
            return true;
        }
        if (object instanceof EObject)
        {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CifsvgSwitch<Adapter> modelSwitch =
        new CifsvgSwitch<Adapter>()
        {
            @Override
            public Adapter caseSvgFile(SvgFile object)
            {
                return createSvgFileAdapter();
            }
            @Override
            public Adapter caseSvgOut(SvgOut object)
            {
                return createSvgOutAdapter();
            }
            @Override
            public Adapter caseSvgIn(SvgIn object)
            {
                return createSvgInAdapter();
            }
            @Override
            public Adapter caseSvgInEventSingle(SvgInEventSingle object)
            {
                return createSvgInEventSingleAdapter();
            }
            @Override
            public Adapter caseSvgInEventIf(SvgInEventIf object)
            {
                return createSvgInEventIfAdapter();
            }
            @Override
            public Adapter caseSvgInEventIfEntry(SvgInEventIfEntry object)
            {
                return createSvgInEventIfEntryAdapter();
            }
            @Override
            public Adapter caseSvgCopy(SvgCopy object)
            {
                return createSvgCopyAdapter();
            }
            @Override
            public Adapter caseSvgMove(SvgMove object)
            {
                return createSvgMoveAdapter();
            }
            @Override
            public Adapter caseSvgInEvent(SvgInEvent object)
            {
                return createSvgInEventAdapter();
            }
            @Override
            public Adapter casePositionObject(PositionObject object)
            {
                return createPositionObjectAdapter();
            }
            @Override
            public Adapter caseIoDecl(IoDecl object)
            {
                return createIoDeclAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object)
            {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target)
    {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile <em>Svg File</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile
     * @generated
     */
    public Adapter createSvgFileAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut <em>Svg Out</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut
     * @generated
     */
    public Adapter createSvgOutAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn <em>Svg In</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn
     * @generated
     */
    public Adapter createSvgInAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle <em>Svg In Event Single</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle
     * @generated
     */
    public Adapter createSvgInEventSingleAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf <em>Svg In Event If</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf
     * @generated
     */
    public Adapter createSvgInEventIfAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry <em>Svg In Event If Entry</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry
     * @generated
     */
    public Adapter createSvgInEventIfEntryAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy <em>Svg Copy</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy
     * @generated
     */
    public Adapter createSvgCopyAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove <em>Svg Move</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove
     * @generated
     */
    public Adapter createSvgMoveAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent <em>Svg In Event</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent
     * @generated
     */
    public Adapter createSvgInEventAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.common.position.metamodel.position.PositionObject <em>Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.common.position.metamodel.position.PositionObject
     * @generated
     */
    public Adapter createPositionObjectAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.IoDecl <em>Io Decl</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.IoDecl
     * @generated
     */
    public Adapter createIoDeclAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter()
    {
        return null;
    }

} //CifsvgAdapterFactory
