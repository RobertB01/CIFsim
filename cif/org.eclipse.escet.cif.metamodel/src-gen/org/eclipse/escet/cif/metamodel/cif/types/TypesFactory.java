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
package org.eclipse.escet.cif.metamodel.cif.types;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage
 * @generated
 */
public interface TypesFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TypesFactory eINSTANCE = org.eclipse.escet.cif.metamodel.cif.types.impl.TypesFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Bool Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Bool Type</em>'.
     * @generated
     */
    BoolType createBoolType();

    /**
     * Returns a new object of class '<em>Int Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Int Type</em>'.
     * @generated
     */
    IntType createIntType();

    /**
     * Returns a new object of class '<em>Type Ref</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Type Ref</em>'.
     * @generated
     */
    TypeRef createTypeRef();

    /**
     * Returns a new object of class '<em>Enum Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Enum Type</em>'.
     * @generated
     */
    EnumType createEnumType();

    /**
     * Returns a new object of class '<em>Comp Param Wrap Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Comp Param Wrap Type</em>'.
     * @generated
     */
    CompParamWrapType createCompParamWrapType();

    /**
     * Returns a new object of class '<em>Comp Inst Wrap Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Comp Inst Wrap Type</em>'.
     * @generated
     */
    CompInstWrapType createCompInstWrapType();

    /**
     * Returns a new object of class '<em>Component Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component Type</em>'.
     * @generated
     */
    ComponentType createComponentType();

    /**
     * Returns a new object of class '<em>Component Def Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component Def Type</em>'.
     * @generated
     */
    ComponentDefType createComponentDefType();

    /**
     * Returns a new object of class '<em>Real Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Real Type</em>'.
     * @generated
     */
    RealType createRealType();

    /**
     * Returns a new object of class '<em>String Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>String Type</em>'.
     * @generated
     */
    StringType createStringType();

    /**
     * Returns a new object of class '<em>List Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>List Type</em>'.
     * @generated
     */
    ListType createListType();

    /**
     * Returns a new object of class '<em>Set Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Set Type</em>'.
     * @generated
     */
    SetType createSetType();

    /**
     * Returns a new object of class '<em>Dict Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Dict Type</em>'.
     * @generated
     */
    DictType createDictType();

    /**
     * Returns a new object of class '<em>Tuple Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tuple Type</em>'.
     * @generated
     */
    TupleType createTupleType();

    /**
     * Returns a new object of class '<em>Field</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Field</em>'.
     * @generated
     */
    Field createField();

    /**
     * Returns a new object of class '<em>Func Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Func Type</em>'.
     * @generated
     */
    FuncType createFuncType();

    /**
     * Returns a new object of class '<em>Dist Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Dist Type</em>'.
     * @generated
     */
    DistType createDistType();

    /**
     * Returns a new object of class '<em>Void Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Void Type</em>'.
     * @generated
     */
    VoidType createVoidType();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    TypesPackage getTypesPackage();

} //TypesFactory
