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
package org.eclipse.escet.cif.metamodel.cif.declarations.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.cif.metamodel.cif.declarations.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DeclarationsFactoryImpl extends EFactoryImpl implements DeclarationsFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static DeclarationsFactory init()
    {
        try
        {
            DeclarationsFactory theDeclarationsFactory = (DeclarationsFactory)EPackage.Registry.INSTANCE.getEFactory(DeclarationsPackage.eNS_URI);
            if (theDeclarationsFactory != null)
            {
                return theDeclarationsFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new DeclarationsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DeclarationsFactoryImpl()
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
            case DeclarationsPackage.ALG_VARIABLE: return createAlgVariable();
            case DeclarationsPackage.EVENT: return createEvent();
            case DeclarationsPackage.ENUM_DECL: return createEnumDecl();
            case DeclarationsPackage.TYPE_DECL: return createTypeDecl();
            case DeclarationsPackage.ENUM_LITERAL: return createEnumLiteral();
            case DeclarationsPackage.DISC_VARIABLE: return createDiscVariable();
            case DeclarationsPackage.VARIABLE_VALUE: return createVariableValue();
            case DeclarationsPackage.CONSTANT: return createConstant();
            case DeclarationsPackage.CONT_VARIABLE: return createContVariable();
            case DeclarationsPackage.INPUT_VARIABLE: return createInputVariable();
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
    public AlgVariable createAlgVariable()
    {
        AlgVariableImpl algVariable = new AlgVariableImpl();
        return algVariable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Event createEvent()
    {
        EventImpl event = new EventImpl();
        return event;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EnumDecl createEnumDecl()
    {
        EnumDeclImpl enumDecl = new EnumDeclImpl();
        return enumDecl;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypeDecl createTypeDecl()
    {
        TypeDeclImpl typeDecl = new TypeDeclImpl();
        return typeDecl;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EnumLiteral createEnumLiteral()
    {
        EnumLiteralImpl enumLiteral = new EnumLiteralImpl();
        return enumLiteral;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DiscVariable createDiscVariable()
    {
        DiscVariableImpl discVariable = new DiscVariableImpl();
        return discVariable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VariableValue createVariableValue()
    {
        VariableValueImpl variableValue = new VariableValueImpl();
        return variableValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Constant createConstant()
    {
        ConstantImpl constant = new ConstantImpl();
        return constant;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ContVariable createContVariable()
    {
        ContVariableImpl contVariable = new ContVariableImpl();
        return contVariable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public InputVariable createInputVariable()
    {
        InputVariableImpl inputVariable = new InputVariableImpl();
        return inputVariable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DeclarationsPackage getDeclarationsPackage()
    {
        return (DeclarationsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static DeclarationsPackage getPackage()
    {
        return DeclarationsPackage.eINSTANCE;
    }

} //DeclarationsFactoryImpl
