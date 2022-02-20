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
package org.eclipse.escet.tooldef.metamodel.tooldef.types;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage
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
    TypesFactory eINSTANCE = org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesFactoryImpl.init();

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
     * Returns a new object of class '<em>Long Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Long Type</em>'.
     * @generated
     */
    LongType createLongType();

    /**
     * Returns a new object of class '<em>Double Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Double Type</em>'.
     * @generated
     */
    DoubleType createDoubleType();

    /**
     * Returns a new object of class '<em>String Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>String Type</em>'.
     * @generated
     */
    StringType createStringType();

    /**
     * Returns a new object of class '<em>Object Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Object Type</em>'.
     * @generated
     */
    ObjectType createObjectType();

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
     * Returns a new object of class '<em>Map Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Map Type</em>'.
     * @generated
     */
    MapType createMapType();

    /**
     * Returns a new object of class '<em>Tuple Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Tuple Type</em>'.
     * @generated
     */
    TupleType createTupleType();

    /**
     * Returns a new object of class '<em>Type Ref</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Type Ref</em>'.
     * @generated
     */
    TypeRef createTypeRef();

    /**
     * Returns a new object of class '<em>Type Param Ref</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Type Param Ref</em>'.
     * @generated
     */
    TypeParamRef createTypeParamRef();

    /**
     * Returns a new object of class '<em>Unresolved Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Unresolved Type</em>'.
     * @generated
     */
    UnresolvedType createUnresolvedType();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    TypesPackage getTypesPackage();

} //TypesFactory
