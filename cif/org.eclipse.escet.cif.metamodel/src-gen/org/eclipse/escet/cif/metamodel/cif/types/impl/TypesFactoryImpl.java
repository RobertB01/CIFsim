/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif.types.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.cif.metamodel.cif.types.*;

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
            case TypesPackage.TYPE_REF: return createTypeRef();
            case TypesPackage.ENUM_TYPE: return createEnumType();
            case TypesPackage.COMP_PARAM_WRAP_TYPE: return createCompParamWrapType();
            case TypesPackage.COMP_INST_WRAP_TYPE: return createCompInstWrapType();
            case TypesPackage.COMPONENT_TYPE: return createComponentType();
            case TypesPackage.COMPONENT_DEF_TYPE: return createComponentDefType();
            case TypesPackage.REAL_TYPE: return createRealType();
            case TypesPackage.STRING_TYPE: return createStringType();
            case TypesPackage.LIST_TYPE: return createListType();
            case TypesPackage.SET_TYPE: return createSetType();
            case TypesPackage.DICT_TYPE: return createDictType();
            case TypesPackage.TUPLE_TYPE: return createTupleType();
            case TypesPackage.FIELD: return createField();
            case TypesPackage.FUNC_TYPE: return createFuncType();
            case TypesPackage.DIST_TYPE: return createDistType();
            case TypesPackage.VOID_TYPE: return createVoidType();
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
    public EnumType createEnumType()
    {
        EnumTypeImpl enumType = new EnumTypeImpl();
        return enumType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CompParamWrapType createCompParamWrapType()
    {
        CompParamWrapTypeImpl compParamWrapType = new CompParamWrapTypeImpl();
        return compParamWrapType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public CompInstWrapType createCompInstWrapType()
    {
        CompInstWrapTypeImpl compInstWrapType = new CompInstWrapTypeImpl();
        return compInstWrapType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ComponentType createComponentType()
    {
        ComponentTypeImpl componentType = new ComponentTypeImpl();
        return componentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ComponentDefType createComponentDefType()
    {
        ComponentDefTypeImpl componentDefType = new ComponentDefTypeImpl();
        return componentDefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public RealType createRealType()
    {
        RealTypeImpl realType = new RealTypeImpl();
        return realType;
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
    public DictType createDictType()
    {
        DictTypeImpl dictType = new DictTypeImpl();
        return dictType;
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
    public Field createField()
    {
        FieldImpl field = new FieldImpl();
        return field;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FuncType createFuncType()
    {
        FuncTypeImpl funcType = new FuncTypeImpl();
        return funcType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public DistType createDistType()
    {
        DistTypeImpl distType = new DistTypeImpl();
        return distType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VoidType createVoidType()
    {
        VoidTypeImpl voidType = new VoidTypeImpl();
        return voidType;
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
