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
package org.eclipse.escet.cif.metamodel.cif.annotations.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.escet.cif.metamodel.cif.annotations.*;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationsPackage
 * @generated
 */
public class AnnotationsAdapterFactory extends AdapterFactoryImpl
{
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static AnnotationsPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnnotationsAdapterFactory()
    {
        if (modelPackage == null)
        {
            modelPackage = AnnotationsPackage.eINSTANCE;
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
    protected AnnotationsSwitch<Adapter> modelSwitch =
        new AnnotationsSwitch<Adapter>()
        {
            @Override
            public Adapter caseAnnotation(Annotation object)
            {
                return createAnnotationAdapter();
            }
            @Override
            public Adapter caseAnnotationArgument(AnnotationArgument object)
            {
                return createAnnotationArgumentAdapter();
            }
            @Override
            public Adapter caseAnnotatedObject(AnnotatedObject object)
            {
                return createAnnotatedObjectAdapter();
            }
            @Override
            public Adapter casePositionObject(PositionObject object)
            {
                return createPositionObjectAdapter();
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
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.annotations.Annotation <em>Annotation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.Annotation
     * @generated
     */
    public Adapter createAnnotationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument <em>Annotation Argument</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument
     * @generated
     */
    public Adapter createAnnotationArgumentAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject <em>Annotated Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject
     * @generated
     */
    public Adapter createAnnotatedObjectAdapter()
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

} //AnnotationsAdapterFactory