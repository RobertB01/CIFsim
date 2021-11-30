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
package org.eclipse.escet.cif.metamodel.cif.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.cif.metamodel.cif.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CifFactoryImpl extends EFactoryImpl implements CifFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static CifFactory init()
    {
        try
        {
            CifFactory theCifFactory = (CifFactory)EPackage.Registry.INSTANCE.getEFactory(CifPackage.eNS_URI);
            if (theCifFactory != null)
            {
                return theCifFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new CifFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CifFactoryImpl()
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
            case CifPackage.GROUP: return createGroup();
            case CifPackage.COMPONENT_DEF: return createComponentDef();
            case CifPackage.COMPONENT_INST: return createComponentInst();
            case CifPackage.SPECIFICATION: return createSpecification();
            case CifPackage.EVENT_PARAMETER: return createEventParameter();
            case CifPackage.LOCATION_PARAMETER: return createLocationParameter();
            case CifPackage.ALG_PARAMETER: return createAlgParameter();
            case CifPackage.COMPONENT_PARAMETER: return createComponentParameter();
            case CifPackage.EQUATION: return createEquation();
            case CifPackage.INVARIANT: return createInvariant();
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
    public Object createFromString(EDataType eDataType, String initialValue)
    {
        switch (eDataType.getClassifierID())
        {
            case CifPackage.SUP_KIND:
                return createSupKindFromString(eDataType, initialValue);
            case CifPackage.INV_KIND:
                return createInvKindFromString(eDataType, initialValue);
            case CifPackage.CIF_IDENTIFIER:
                return createCifIdentifierFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue)
    {
        switch (eDataType.getClassifierID())
        {
            case CifPackage.SUP_KIND:
                return convertSupKindToString(eDataType, instanceValue);
            case CifPackage.INV_KIND:
                return convertInvKindToString(eDataType, instanceValue);
            case CifPackage.CIF_IDENTIFIER:
                return convertCifIdentifierToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Group createGroup()
    {
        GroupImpl group = new GroupImpl();
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ComponentDef createComponentDef()
    {
        ComponentDefImpl componentDef = new ComponentDefImpl();
        return componentDef;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ComponentInst createComponentInst()
    {
        ComponentInstImpl componentInst = new ComponentInstImpl();
        return componentInst;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Specification createSpecification()
    {
        SpecificationImpl specification = new SpecificationImpl();
        return specification;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EventParameter createEventParameter()
    {
        EventParameterImpl eventParameter = new EventParameterImpl();
        return eventParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public LocationParameter createLocationParameter()
    {
        LocationParameterImpl locationParameter = new LocationParameterImpl();
        return locationParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AlgParameter createAlgParameter()
    {
        AlgParameterImpl algParameter = new AlgParameterImpl();
        return algParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ComponentParameter createComponentParameter()
    {
        ComponentParameterImpl componentParameter = new ComponentParameterImpl();
        return componentParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Equation createEquation()
    {
        EquationImpl equation = new EquationImpl();
        return equation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Invariant createInvariant()
    {
        InvariantImpl invariant = new InvariantImpl();
        return invariant;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SupKind createSupKindFromString(EDataType eDataType, String initialValue)
    {
        SupKind result = SupKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSupKindToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InvKind createInvKindFromString(EDataType eDataType, String initialValue)
    {
        InvKind result = InvKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertInvKindToString(EDataType eDataType, Object instanceValue)
    {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createCifIdentifierFromString(EDataType eDataType, String initialValue)
    {
        return (String)EcoreFactory.eINSTANCE.createFromString(EcorePackage.Literals.ESTRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCifIdentifierToString(EDataType eDataType, Object instanceValue)
    {
        return EcoreFactory.eINSTANCE.convertToString(EcorePackage.Literals.ESTRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CifPackage getCifPackage()
    {
        return (CifPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static CifPackage getPackage()
    {
        return CifPackage.eINSTANCE;
    }

} //CifFactoryImpl
