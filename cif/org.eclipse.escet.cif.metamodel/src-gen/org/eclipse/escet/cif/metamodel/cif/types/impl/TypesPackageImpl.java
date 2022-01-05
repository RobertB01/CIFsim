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
package org.eclipse.escet.cif.metamodel.cif.types.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;

import org.eclipse.escet.cif.metamodel.cif.automata.impl.AutomataPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.impl.CifsvgPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage;

import org.eclipse.escet.cif.metamodel.cif.declarations.impl.DeclarationsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;

import org.eclipse.escet.cif.metamodel.cif.expressions.impl.ExpressionsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage;

import org.eclipse.escet.cif.metamodel.cif.functions.impl.FunctionsPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.impl.CifPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.print.PrintPackage;

import org.eclipse.escet.cif.metamodel.cif.print.impl.PrintPackageImpl;

import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.cif.types.TypesFactory;
import org.eclipse.escet.cif.metamodel.cif.types.TypesPackage;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TypesPackageImpl extends EPackageImpl implements TypesPackage
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cifTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass boolTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass intTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass typeRefEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass enumTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compParamWrapTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compInstWrapTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentDefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass realTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stringTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass listTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass setTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dictTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tupleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass fieldEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass funcTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass distTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass voidTypeEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private TypesPackageImpl()
    {
        super(eNS_URI, TypesFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     *
     * <p>This method is used to initialize {@link TypesPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static TypesPackage init()
    {
        if (isInited) return (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredTypesPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        TypesPackageImpl theTypesPackage = registeredTypesPackage instanceof TypesPackageImpl ? (TypesPackageImpl)registeredTypesPackage : new TypesPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        PositionPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(CifPackage.eNS_URI);
        CifPackageImpl theCifPackage = (CifPackageImpl)(registeredPackage instanceof CifPackageImpl ? registeredPackage : CifPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeclarationsPackage.eNS_URI);
        DeclarationsPackageImpl theDeclarationsPackage = (DeclarationsPackageImpl)(registeredPackage instanceof DeclarationsPackageImpl ? registeredPackage : DeclarationsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AutomataPackage.eNS_URI);
        AutomataPackageImpl theAutomataPackage = (AutomataPackageImpl)(registeredPackage instanceof AutomataPackageImpl ? registeredPackage : AutomataPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI);
        ExpressionsPackageImpl theExpressionsPackage = (ExpressionsPackageImpl)(registeredPackage instanceof ExpressionsPackageImpl ? registeredPackage : ExpressionsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(FunctionsPackage.eNS_URI);
        FunctionsPackageImpl theFunctionsPackage = (FunctionsPackageImpl)(registeredPackage instanceof FunctionsPackageImpl ? registeredPackage : FunctionsPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(CifsvgPackage.eNS_URI);
        CifsvgPackageImpl theCifsvgPackage = (CifsvgPackageImpl)(registeredPackage instanceof CifsvgPackageImpl ? registeredPackage : CifsvgPackage.eINSTANCE);
        registeredPackage = EPackage.Registry.INSTANCE.getEPackage(PrintPackage.eNS_URI);
        PrintPackageImpl thePrintPackage = (PrintPackageImpl)(registeredPackage instanceof PrintPackageImpl ? registeredPackage : PrintPackage.eINSTANCE);

        // Create package meta-data objects
        theTypesPackage.createPackageContents();
        theCifPackage.createPackageContents();
        theDeclarationsPackage.createPackageContents();
        theAutomataPackage.createPackageContents();
        theExpressionsPackage.createPackageContents();
        theFunctionsPackage.createPackageContents();
        theCifsvgPackage.createPackageContents();
        thePrintPackage.createPackageContents();

        // Initialize created meta-data
        theTypesPackage.initializePackageContents();
        theCifPackage.initializePackageContents();
        theDeclarationsPackage.initializePackageContents();
        theAutomataPackage.initializePackageContents();
        theExpressionsPackage.initializePackageContents();
        theFunctionsPackage.initializePackageContents();
        theCifsvgPackage.initializePackageContents();
        thePrintPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theTypesPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(TypesPackage.eNS_URI, theTypesPackage);
        return theTypesPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCifType()
    {
        return cifTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBoolType()
    {
        return boolTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIntType()
    {
        return intTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getIntType_Lower()
    {
        return (EAttribute)intTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getIntType_Upper()
    {
        return (EAttribute)intTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTypeRef()
    {
        return typeRefEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTypeRef_Type()
    {
        return (EReference)typeRefEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEnumType()
    {
        return enumTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEnumType_Enum()
    {
        return (EReference)enumTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCompParamWrapType()
    {
        return compParamWrapTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompParamWrapType_Parameter()
    {
        return (EReference)compParamWrapTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompParamWrapType_Reference()
    {
        return (EReference)compParamWrapTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCompInstWrapType()
    {
        return compInstWrapTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompInstWrapType_Instantiation()
    {
        return (EReference)compInstWrapTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCompInstWrapType_Reference()
    {
        return (EReference)compInstWrapTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComponentType()
    {
        return componentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComponentType_Component()
    {
        return (EReference)componentTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComponentDefType()
    {
        return componentDefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getComponentDefType_Definition()
    {
        return (EReference)componentDefTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRealType()
    {
        return realTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStringType()
    {
        return stringTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getListType()
    {
        return listTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getListType_ElementType()
    {
        return (EReference)listTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getListType_Lower()
    {
        return (EAttribute)listTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getListType_Upper()
    {
        return (EAttribute)listTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSetType()
    {
        return setTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSetType_ElementType()
    {
        return (EReference)setTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDictType()
    {
        return dictTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictType_KeyType()
    {
        return (EReference)dictTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDictType_ValueType()
    {
        return (EReference)dictTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTupleType()
    {
        return tupleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTupleType_Fields()
    {
        return (EReference)tupleTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getField()
    {
        return fieldEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getField_Name()
    {
        return (EAttribute)fieldEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getField_Type()
    {
        return (EReference)fieldEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFuncType()
    {
        return funcTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFuncType_ReturnType()
    {
        return (EReference)funcTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFuncType_ParamTypes()
    {
        return (EReference)funcTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDistType()
    {
        return distTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDistType_SampleType()
    {
        return (EReference)distTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVoidType()
    {
        return voidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TypesFactory getTypesFactory()
    {
        return (TypesFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents()
    {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        cifTypeEClass = createEClass(CIF_TYPE);

        boolTypeEClass = createEClass(BOOL_TYPE);

        intTypeEClass = createEClass(INT_TYPE);
        createEAttribute(intTypeEClass, INT_TYPE__LOWER);
        createEAttribute(intTypeEClass, INT_TYPE__UPPER);

        typeRefEClass = createEClass(TYPE_REF);
        createEReference(typeRefEClass, TYPE_REF__TYPE);

        enumTypeEClass = createEClass(ENUM_TYPE);
        createEReference(enumTypeEClass, ENUM_TYPE__ENUM);

        compParamWrapTypeEClass = createEClass(COMP_PARAM_WRAP_TYPE);
        createEReference(compParamWrapTypeEClass, COMP_PARAM_WRAP_TYPE__PARAMETER);
        createEReference(compParamWrapTypeEClass, COMP_PARAM_WRAP_TYPE__REFERENCE);

        compInstWrapTypeEClass = createEClass(COMP_INST_WRAP_TYPE);
        createEReference(compInstWrapTypeEClass, COMP_INST_WRAP_TYPE__INSTANTIATION);
        createEReference(compInstWrapTypeEClass, COMP_INST_WRAP_TYPE__REFERENCE);

        componentTypeEClass = createEClass(COMPONENT_TYPE);
        createEReference(componentTypeEClass, COMPONENT_TYPE__COMPONENT);

        componentDefTypeEClass = createEClass(COMPONENT_DEF_TYPE);
        createEReference(componentDefTypeEClass, COMPONENT_DEF_TYPE__DEFINITION);

        realTypeEClass = createEClass(REAL_TYPE);

        stringTypeEClass = createEClass(STRING_TYPE);

        listTypeEClass = createEClass(LIST_TYPE);
        createEReference(listTypeEClass, LIST_TYPE__ELEMENT_TYPE);
        createEAttribute(listTypeEClass, LIST_TYPE__LOWER);
        createEAttribute(listTypeEClass, LIST_TYPE__UPPER);

        setTypeEClass = createEClass(SET_TYPE);
        createEReference(setTypeEClass, SET_TYPE__ELEMENT_TYPE);

        dictTypeEClass = createEClass(DICT_TYPE);
        createEReference(dictTypeEClass, DICT_TYPE__KEY_TYPE);
        createEReference(dictTypeEClass, DICT_TYPE__VALUE_TYPE);

        tupleTypeEClass = createEClass(TUPLE_TYPE);
        createEReference(tupleTypeEClass, TUPLE_TYPE__FIELDS);

        fieldEClass = createEClass(FIELD);
        createEAttribute(fieldEClass, FIELD__NAME);
        createEReference(fieldEClass, FIELD__TYPE);

        funcTypeEClass = createEClass(FUNC_TYPE);
        createEReference(funcTypeEClass, FUNC_TYPE__RETURN_TYPE);
        createEReference(funcTypeEClass, FUNC_TYPE__PARAM_TYPES);

        distTypeEClass = createEClass(DIST_TYPE);
        createEReference(distTypeEClass, DIST_TYPE__SAMPLE_TYPE);

        voidTypeEClass = createEClass(VOID_TYPE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents()
    {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        PositionPackage thePositionPackage = (PositionPackage)EPackage.Registry.INSTANCE.getEPackage(PositionPackage.eNS_URI);
        DeclarationsPackage theDeclarationsPackage = (DeclarationsPackage)EPackage.Registry.INSTANCE.getEPackage(DeclarationsPackage.eNS_URI);
        CifPackage theCifPackage = (CifPackage)EPackage.Registry.INSTANCE.getEPackage(CifPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        cifTypeEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        boolTypeEClass.getESuperTypes().add(this.getCifType());
        intTypeEClass.getESuperTypes().add(this.getCifType());
        typeRefEClass.getESuperTypes().add(this.getCifType());
        enumTypeEClass.getESuperTypes().add(this.getCifType());
        compParamWrapTypeEClass.getESuperTypes().add(this.getCifType());
        compInstWrapTypeEClass.getESuperTypes().add(this.getCifType());
        componentTypeEClass.getESuperTypes().add(this.getCifType());
        componentDefTypeEClass.getESuperTypes().add(this.getCifType());
        realTypeEClass.getESuperTypes().add(this.getCifType());
        stringTypeEClass.getESuperTypes().add(this.getCifType());
        listTypeEClass.getESuperTypes().add(this.getCifType());
        setTypeEClass.getESuperTypes().add(this.getCifType());
        dictTypeEClass.getESuperTypes().add(this.getCifType());
        tupleTypeEClass.getESuperTypes().add(this.getCifType());
        fieldEClass.getESuperTypes().add(thePositionPackage.getPositionObject());
        funcTypeEClass.getESuperTypes().add(this.getCifType());
        distTypeEClass.getESuperTypes().add(this.getCifType());
        voidTypeEClass.getESuperTypes().add(this.getCifType());

        // Initialize classes, features, and operations; add parameters
        initEClass(cifTypeEClass, CifType.class, "CifType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(boolTypeEClass, BoolType.class, "BoolType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(intTypeEClass, IntType.class, "IntType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getIntType_Lower(), ecorePackage.getEIntegerObject(), "lower", null, 0, 1, IntType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIntType_Upper(), ecorePackage.getEIntegerObject(), "upper", null, 0, 1, IntType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(typeRefEClass, TypeRef.class, "TypeRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTypeRef_Type(), theDeclarationsPackage.getTypeDecl(), null, "type", null, 1, 1, TypeRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(enumTypeEClass, EnumType.class, "EnumType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEnumType_Enum(), theDeclarationsPackage.getEnumDecl(), null, "enum", null, 1, 1, EnumType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(compParamWrapTypeEClass, CompParamWrapType.class, "CompParamWrapType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCompParamWrapType_Parameter(), theCifPackage.getComponentParameter(), null, "parameter", null, 1, 1, CompParamWrapType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCompParamWrapType_Reference(), this.getCifType(), null, "reference", null, 1, 1, CompParamWrapType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(compInstWrapTypeEClass, CompInstWrapType.class, "CompInstWrapType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCompInstWrapType_Instantiation(), theCifPackage.getComponentInst(), null, "instantiation", null, 1, 1, CompInstWrapType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCompInstWrapType_Reference(), this.getCifType(), null, "reference", null, 1, 1, CompInstWrapType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentTypeEClass, ComponentType.class, "ComponentType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentType_Component(), theCifPackage.getComponent(), null, "component", null, 1, 1, ComponentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentDefTypeEClass, ComponentDefType.class, "ComponentDefType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentDefType_Definition(), theCifPackage.getComponentDef(), null, "definition", null, 1, 1, ComponentDefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(realTypeEClass, RealType.class, "RealType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(stringTypeEClass, StringType.class, "StringType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(listTypeEClass, ListType.class, "ListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getListType_ElementType(), this.getCifType(), null, "elementType", null, 1, 1, ListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getListType_Lower(), ecorePackage.getEIntegerObject(), "lower", null, 0, 1, ListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getListType_Upper(), ecorePackage.getEIntegerObject(), "upper", null, 0, 1, ListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(setTypeEClass, SetType.class, "SetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSetType_ElementType(), this.getCifType(), null, "elementType", null, 1, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dictTypeEClass, DictType.class, "DictType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDictType_KeyType(), this.getCifType(), null, "keyType", null, 1, 1, DictType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDictType_ValueType(), this.getCifType(), null, "valueType", null, 1, 1, DictType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tupleTypeEClass, TupleType.class, "TupleType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTupleType_Fields(), this.getField(), null, "fields", null, 2, -1, TupleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(fieldEClass, Field.class, "Field", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getField_Name(), theCifPackage.getCifIdentifier(), "name", null, 0, 1, Field.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getField_Type(), this.getCifType(), null, "type", null, 1, 1, Field.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(funcTypeEClass, FuncType.class, "FuncType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFuncType_ReturnType(), this.getCifType(), null, "returnType", null, 1, 1, FuncType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFuncType_ParamTypes(), this.getCifType(), null, "paramTypes", null, 0, -1, FuncType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(distTypeEClass, DistType.class, "DistType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDistType_SampleType(), this.getCifType(), null, "sampleType", null, 1, 1, DistType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(voidTypeEClass, VoidType.class, "VoidType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    }

} //TypesPackageImpl
