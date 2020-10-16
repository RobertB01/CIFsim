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
package org.eclipse.escet.tooldef.metamodel.tooldef.types;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesFactory
 * @model kind="package"
 * @generated
 */
public interface TypesPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "types";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/tooldef/types";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "types";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TypesPackage eINSTANCE = org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ToolDefTypeImpl <em>Tool Def Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ToolDefTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getToolDefType()
     * @generated
     */
    int TOOL_DEF_TYPE = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TYPE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TYPE__NULLABLE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tool Def Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TYPE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Tool Def Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TOOL_DEF_TYPE_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.BoolTypeImpl <em>Bool Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.BoolTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getBoolType()
     * @generated
     */
    int BOOL_TYPE = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The number of structural features of the '<em>Bool Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Bool Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.IntTypeImpl <em>Int Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.IntTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getIntType()
     * @generated
     */
    int INT_TYPE = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The number of structural features of the '<em>Int Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Int Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.LongTypeImpl <em>Long Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.LongTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getLongType()
     * @generated
     */
    int LONG_TYPE = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LONG_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LONG_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The number of structural features of the '<em>Long Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LONG_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Long Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LONG_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.DoubleTypeImpl <em>Double Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.DoubleTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getDoubleType()
     * @generated
     */
    int DOUBLE_TYPE = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The number of structural features of the '<em>Double Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Double Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOUBLE_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.StringTypeImpl <em>String Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.StringTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getStringType()
     * @generated
     */
    int STRING_TYPE = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The number of structural features of the '<em>String Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>String Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ObjectTypeImpl <em>Object Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ObjectTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getObjectType()
     * @generated
     */
    int OBJECT_TYPE = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The number of structural features of the '<em>Object Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Object Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ListTypeImpl <em>List Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ListTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getListType()
     * @generated
     */
    int LIST_TYPE = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The feature id for the '<em><b>Elem Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__ELEM_TYPE = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>List Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>List Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.SetTypeImpl <em>Set Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.SetTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getSetType()
     * @generated
     */
    int SET_TYPE = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The feature id for the '<em><b>Elem Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ELEM_TYPE = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.MapTypeImpl <em>Map Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.MapTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getMapType()
     * @generated
     */
    int MAP_TYPE = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The feature id for the '<em><b>Key Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_TYPE__KEY_TYPE = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_TYPE__VALUE_TYPE = TOOL_DEF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Map Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Map Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MAP_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TupleTypeImpl <em>Tuple Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TupleTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getTupleType()
     * @generated
     */
    int TUPLE_TYPE = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The feature id for the '<em><b>Fields</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE__FIELDS = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tuple Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Tuple Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypeRefImpl <em>Type Ref</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypeRefImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getTypeRef()
     * @generated
     */
    int TYPE_REF = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF__TYPE = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Type Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Type Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypeParamRefImpl <em>Type Param Ref</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypeParamRefImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getTypeParamRef()
     * @generated
     */
    int TYPE_PARAM_REF = 12;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM_REF__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM_REF__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM_REF__TYPE = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Type Param Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM_REF_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Type Param Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_PARAM_REF_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.UnresolvedTypeImpl <em>Unresolved Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.UnresolvedTypeImpl
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getUnresolvedType()
     * @generated
     */
    int UNRESOLVED_TYPE = 13;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_TYPE__POSITION = TOOL_DEF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Nullable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_TYPE__NULLABLE = TOOL_DEF_TYPE__NULLABLE;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_TYPE__NAME = TOOL_DEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Unresolved Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_TYPE_FEATURE_COUNT = TOOL_DEF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Unresolved Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_TYPE_OPERATION_COUNT = TOOL_DEF_TYPE_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType <em>Tool Def Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tool Def Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType
     * @generated
     */
    EClass getToolDefType();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType#isNullable <em>Nullable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Nullable</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType#isNullable()
     * @see #getToolDefType()
     * @generated
     */
    EAttribute getToolDefType_Nullable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType <em>Bool Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bool Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType
     * @generated
     */
    EClass getBoolType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType <em>Int Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Int Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType
     * @generated
     */
    EClass getIntType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType <em>Long Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Long Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType
     * @generated
     */
    EClass getLongType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType <em>Double Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Double Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType
     * @generated
     */
    EClass getDoubleType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType <em>String Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>String Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType
     * @generated
     */
    EClass getStringType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.ObjectType <em>Object Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Object Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.ObjectType
     * @generated
     */
    EClass getObjectType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType <em>List Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>List Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType
     * @generated
     */
    EClass getListType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType#getElemType <em>Elem Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Elem Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType#getElemType()
     * @see #getListType()
     * @generated
     */
    EReference getListType_ElemType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType <em>Set Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Set Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType
     * @generated
     */
    EClass getSetType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType#getElemType <em>Elem Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Elem Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType#getElemType()
     * @see #getSetType()
     * @generated
     */
    EReference getSetType_ElemType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType <em>Map Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Map Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType
     * @generated
     */
    EClass getMapType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType#getKeyType <em>Key Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Key Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType#getKeyType()
     * @see #getMapType()
     * @generated
     */
    EReference getMapType_KeyType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType#getValueType <em>Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType#getValueType()
     * @see #getMapType()
     * @generated
     */
    EReference getMapType_ValueType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType <em>Tuple Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tuple Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType
     * @generated
     */
    EClass getTupleType();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType#getFields <em>Fields</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Fields</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType#getFields()
     * @see #getTupleType()
     * @generated
     */
    EReference getTupleType_Fields();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef <em>Type Ref</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Ref</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef
     * @generated
     */
    EClass getTypeRef();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef#getType()
     * @see #getTypeRef()
     * @generated
     */
    EReference getTypeRef_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef <em>Type Param Ref</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Param Ref</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef
     * @generated
     */
    EClass getTypeParamRef();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef#getType()
     * @see #getTypeParamRef()
     * @generated
     */
    EReference getTypeParamRef_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.UnresolvedType <em>Unresolved Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unresolved Type</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.UnresolvedType
     * @generated
     */
    EClass getUnresolvedType();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.UnresolvedType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.UnresolvedType#getName()
     * @see #getUnresolvedType()
     * @generated
     */
    EAttribute getUnresolvedType_Name();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    TypesFactory getTypesFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals
    {
        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ToolDefTypeImpl <em>Tool Def Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ToolDefTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getToolDefType()
         * @generated
         */
        EClass TOOL_DEF_TYPE = eINSTANCE.getToolDefType();

        /**
         * The meta object literal for the '<em><b>Nullable</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TOOL_DEF_TYPE__NULLABLE = eINSTANCE.getToolDefType_Nullable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.BoolTypeImpl <em>Bool Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.BoolTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getBoolType()
         * @generated
         */
        EClass BOOL_TYPE = eINSTANCE.getBoolType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.IntTypeImpl <em>Int Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.IntTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getIntType()
         * @generated
         */
        EClass INT_TYPE = eINSTANCE.getIntType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.LongTypeImpl <em>Long Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.LongTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getLongType()
         * @generated
         */
        EClass LONG_TYPE = eINSTANCE.getLongType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.DoubleTypeImpl <em>Double Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.DoubleTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getDoubleType()
         * @generated
         */
        EClass DOUBLE_TYPE = eINSTANCE.getDoubleType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.StringTypeImpl <em>String Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.StringTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getStringType()
         * @generated
         */
        EClass STRING_TYPE = eINSTANCE.getStringType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ObjectTypeImpl <em>Object Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ObjectTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getObjectType()
         * @generated
         */
        EClass OBJECT_TYPE = eINSTANCE.getObjectType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ListTypeImpl <em>List Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.ListTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getListType()
         * @generated
         */
        EClass LIST_TYPE = eINSTANCE.getListType();

        /**
         * The meta object literal for the '<em><b>Elem Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LIST_TYPE__ELEM_TYPE = eINSTANCE.getListType_ElemType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.SetTypeImpl <em>Set Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.SetTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getSetType()
         * @generated
         */
        EClass SET_TYPE = eINSTANCE.getSetType();

        /**
         * The meta object literal for the '<em><b>Elem Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SET_TYPE__ELEM_TYPE = eINSTANCE.getSetType_ElemType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.MapTypeImpl <em>Map Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.MapTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getMapType()
         * @generated
         */
        EClass MAP_TYPE = eINSTANCE.getMapType();

        /**
         * The meta object literal for the '<em><b>Key Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MAP_TYPE__KEY_TYPE = eINSTANCE.getMapType_KeyType();

        /**
         * The meta object literal for the '<em><b>Value Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MAP_TYPE__VALUE_TYPE = eINSTANCE.getMapType_ValueType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TupleTypeImpl <em>Tuple Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TupleTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getTupleType()
         * @generated
         */
        EClass TUPLE_TYPE = eINSTANCE.getTupleType();

        /**
         * The meta object literal for the '<em><b>Fields</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TUPLE_TYPE__FIELDS = eINSTANCE.getTupleType_Fields();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypeRefImpl <em>Type Ref</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypeRefImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getTypeRef()
         * @generated
         */
        EClass TYPE_REF = eINSTANCE.getTypeRef();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TYPE_REF__TYPE = eINSTANCE.getTypeRef_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypeParamRefImpl <em>Type Param Ref</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypeParamRefImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getTypeParamRef()
         * @generated
         */
        EClass TYPE_PARAM_REF = eINSTANCE.getTypeParamRef();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TYPE_PARAM_REF__TYPE = eINSTANCE.getTypeParamRef_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.UnresolvedTypeImpl <em>Unresolved Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.UnresolvedTypeImpl
         * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.impl.TypesPackageImpl#getUnresolvedType()
         * @generated
         */
        EClass UNRESOLVED_TYPE = eINSTANCE.getUnresolvedType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNRESOLVED_TYPE__NAME = eINSTANCE.getUnresolvedType_Name();

    }

} //TypesPackage
