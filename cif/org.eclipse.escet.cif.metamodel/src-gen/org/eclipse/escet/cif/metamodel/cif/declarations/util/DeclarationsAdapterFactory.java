/**
 * Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif.declarations.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;

import org.eclipse.escet.cif.metamodel.cif.declarations.*;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage
 * @generated
 */
public class DeclarationsAdapterFactory extends AdapterFactoryImpl
{
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static DeclarationsPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DeclarationsAdapterFactory()
    {
        if (modelPackage == null)
        {
            modelPackage = DeclarationsPackage.eINSTANCE;
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
    protected DeclarationsSwitch<Adapter> modelSwitch =
        new DeclarationsSwitch<Adapter>()
        {
            @Override
            public Adapter caseDeclaration(Declaration object)
            {
                return createDeclarationAdapter();
            }
            @Override
            public Adapter caseAlgVariable(AlgVariable object)
            {
                return createAlgVariableAdapter();
            }
            @Override
            public Adapter caseEvent(Event object)
            {
                return createEventAdapter();
            }
            @Override
            public Adapter caseEnumDecl(EnumDecl object)
            {
                return createEnumDeclAdapter();
            }
            @Override
            public Adapter caseTypeDecl(TypeDecl object)
            {
                return createTypeDeclAdapter();
            }
            @Override
            public Adapter caseEnumLiteral(EnumLiteral object)
            {
                return createEnumLiteralAdapter();
            }
            @Override
            public Adapter caseDiscVariable(DiscVariable object)
            {
                return createDiscVariableAdapter();
            }
            @Override
            public Adapter caseVariableValue(VariableValue object)
            {
                return createVariableValueAdapter();
            }
            @Override
            public Adapter caseConstant(Constant object)
            {
                return createConstantAdapter();
            }
            @Override
            public Adapter caseContVariable(ContVariable object)
            {
                return createContVariableAdapter();
            }
            @Override
            public Adapter caseInputVariable(InputVariable object)
            {
                return createInputVariableAdapter();
            }
            @Override
            public Adapter casePositionObject(PositionObject object)
            {
                return createPositionObjectAdapter();
            }
            @Override
            public Adapter caseAnnotatedObject(AnnotatedObject object)
            {
                return createAnnotatedObjectAdapter();
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
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Declaration <em>Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Declaration
     * @generated
     */
    public Adapter createDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable <em>Alg Variable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable
     * @generated
     */
    public Adapter createAlgVariableAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Event <em>Event</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Event
     * @generated
     */
    public Adapter createEventAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl <em>Enum Decl</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl
     * @generated
     */
    public Adapter createEnumDeclAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl <em>Type Decl</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl
     * @generated
     */
    public Adapter createTypeDeclAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral <em>Enum Literal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral
     * @generated
     */
    public Adapter createEnumLiteralAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable <em>Disc Variable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable
     * @generated
     */
    public Adapter createDiscVariableAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue <em>Variable Value</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue
     * @generated
     */
    public Adapter createVariableValueAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.Constant <em>Constant</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.Constant
     * @generated
     */
    public Adapter createConstantAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable <em>Cont Variable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable
     * @generated
     */
    public Adapter createContVariableAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable <em>Input Variable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable
     * @generated
     */
    public Adapter createInputVariableAdapter()
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

} //DeclarationsAdapterFactory
