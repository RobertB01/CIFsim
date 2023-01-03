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
package org.eclipse.escet.tooldef.metamodel.tooldef.types.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TypesFactoryImpl extends EFactoryImpl implements TypesFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TypesFactory init()
    {
        try
        {
            TypesFactory theTypesFactory = (TypesFactory)EPackage.Registry.INSTANCE.getEFactory(TypesPackage.eNS_URI);
            if (theTypesFactory != null)
            {
                return theTypesFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new TypesFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypesFactoryImpl()
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
            case TypesPackage.BOOL_TYPE: return createBoolType();
            case TypesPackage.INT_TYPE: return createIntType();
            case TypesPackage.LONG_TYPE: return createLongType();
            case TypesPackage.DOUBLE_TYPE: return createDoubleType();
            case TypesPackage.STRING_TYPE: return createStringType();
            case TypesPackage.OBJECT_TYPE: return createObjectType();
            case TypesPackage.LIST_TYPE: return createListType();
            case TypesPackage.SET_TYPE: return createSetType();
            case TypesPackage.MAP_TYPE: return createMapType();
            case TypesPackage.TUPLE_TYPE: return createTupleType();
            case TypesPackage.TYPE_REF: return createTypeRef();
            case TypesPackage.TYPE_PARAM_REF: return createTypeParamRef();
            case TypesPackage.UNRESOLVED_TYPE: return createUnresolvedType();
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
    public BoolType createBoolType()
    {
        BoolTypeImpl boolType = new BoolTypeImpl();
        return boolType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IntType createIntType()
    {
        IntTypeImpl intType = new IntTypeImpl();
        return intType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public LongType createLongType()
    {
        LongTypeImpl longType = new LongTypeImpl();
        return longType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DoubleType createDoubleType()
    {
        DoubleTypeImpl doubleType = new DoubleTypeImpl();
        return doubleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public StringType createStringType()
    {
        StringTypeImpl stringType = new StringTypeImpl();
        return stringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ObjectType createObjectType()
    {
        ObjectTypeImpl objectType = new ObjectTypeImpl();
        return objectType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ListType createListType()
    {
        ListTypeImpl listType = new ListTypeImpl();
        return listType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SetType createSetType()
    {
        SetTypeImpl setType = new SetTypeImpl();
        return setType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public MapType createMapType()
    {
        MapTypeImpl mapType = new MapTypeImpl();
        return mapType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TupleType createTupleType()
    {
        TupleTypeImpl tupleType = new TupleTypeImpl();
        return tupleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypeRef createTypeRef()
    {
        TypeRefImpl typeRef = new TypeRefImpl();
        return typeRef;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypeParamRef createTypeParamRef()
    {
        TypeParamRefImpl typeParamRef = new TypeParamRefImpl();
        return typeParamRef;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public UnresolvedType createUnresolvedType()
    {
        UnresolvedTypeImpl unresolvedType = new UnresolvedTypeImpl();
        return unresolvedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypesPackage getTypesPackage()
    {
        return (TypesPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static TypesPackage getPackage()
    {
        return TypesPackage.eINSTANCE;
    }

} //TypesFactoryImpl
