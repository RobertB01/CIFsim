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
package org.eclipse.escet.cif.metamodel.cif.annotations.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.cif.metamodel.cif.annotations.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AnnotationsFactoryImpl extends EFactoryImpl implements AnnotationsFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static AnnotationsFactory init()
    {
        try
        {
            AnnotationsFactory theAnnotationsFactory = (AnnotationsFactory)EPackage.Registry.INSTANCE.getEFactory(AnnotationsPackage.eNS_URI);
            if (theAnnotationsFactory != null)
            {
                return theAnnotationsFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new AnnotationsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnnotationsFactoryImpl()
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
            case AnnotationsPackage.ANNOTATION: return createAnnotation();
            case AnnotationsPackage.ANNOTATION_ARGUMENT: return createAnnotationArgument();
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
    public Annotation createAnnotation()
    {
        AnnotationImpl annotation = new AnnotationImpl();
        return annotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AnnotationArgument createAnnotationArgument()
    {
        AnnotationArgumentImpl annotationArgument = new AnnotationArgumentImpl();
        return annotationArgument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AnnotationsPackage getAnnotationsPackage()
    {
        return (AnnotationsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static AnnotationsPackage getPackage()
    {
        return AnnotationsPackage.eINSTANCE;
    }

} //AnnotationsFactoryImpl
