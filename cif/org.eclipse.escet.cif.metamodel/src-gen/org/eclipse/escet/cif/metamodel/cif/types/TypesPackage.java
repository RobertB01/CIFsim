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
package org.eclipse.escet.cif.metamodel.cif.types;

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
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesFactory
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
    String eNS_URI = "http://eclipse.org/escet/cif/types";

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
    TypesPackage eINSTANCE = org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.CifTypeImpl <em>Cif Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.CifTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getCifType()
     * @generated
     */
    int CIF_TYPE = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CIF_TYPE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Cif Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CIF_TYPE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Cif Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CIF_TYPE_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.BoolTypeImpl <em>Bool Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.BoolTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getBoolType()
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
    int BOOL_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Bool Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Bool Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.IntTypeImpl <em>Int Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.IntTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getIntType()
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
    int INT_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Lower</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE__LOWER = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Upper</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE__UPPER = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Int Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Int Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.TypeRefImpl <em>Type Ref</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypeRefImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getTypeRef()
     * @generated
     */
    int TYPE_REF = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF__TYPE = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Type Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Type Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REF_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.EnumTypeImpl <em>Enum Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.EnumTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getEnumType()
     * @generated
     */
    int ENUM_TYPE = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Enum</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_TYPE__ENUM = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Enum Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Enum Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.CompParamWrapTypeImpl <em>Comp Param Wrap Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.CompParamWrapTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getCompParamWrapType()
     * @generated
     */
    int COMP_PARAM_WRAP_TYPE = 5;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_TYPE__PARAMETER = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_TYPE__REFERENCE = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Comp Param Wrap Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Comp Param Wrap Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_PARAM_WRAP_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.CompInstWrapTypeImpl <em>Comp Inst Wrap Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.CompInstWrapTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getCompInstWrapType()
     * @generated
     */
    int COMP_INST_WRAP_TYPE = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Instantiation</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_TYPE__INSTANTIATION = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_TYPE__REFERENCE = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Comp Inst Wrap Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Comp Inst Wrap Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMP_INST_WRAP_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.ComponentTypeImpl <em>Component Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.ComponentTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getComponentType()
     * @generated
     */
    int COMPONENT_TYPE = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Component</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_TYPE__COMPONENT = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Component Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Component Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.ComponentDefTypeImpl <em>Component Def Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.ComponentDefTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getComponentDefType()
     * @generated
     */
    int COMPONENT_DEF_TYPE = 8;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF_TYPE__DEFINITION = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Component Def Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Component Def Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_DEF_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.RealTypeImpl <em>Real Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.RealTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getRealType()
     * @generated
     */
    int REAL_TYPE = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Real Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Real Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.StringTypeImpl <em>String Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.StringTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getStringType()
     * @generated
     */
    int STRING_TYPE = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The number of structural features of the '<em>String Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>String Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.ListTypeImpl <em>List Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.ListTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getListType()
     * @generated
     */
    int LIST_TYPE = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__ELEMENT_TYPE = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Lower</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__LOWER = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Upper</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__UPPER = CIF_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>List Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>List Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.SetTypeImpl <em>Set Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.SetTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getSetType()
     * @generated
     */
    int SET_TYPE = 12;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ELEMENT_TYPE = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.DictTypeImpl <em>Dict Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.DictTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getDictType()
     * @generated
     */
    int DICT_TYPE = 13;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Key Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE__KEY_TYPE = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE__VALUE_TYPE = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Dict Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Dict Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.TupleTypeImpl <em>Tuple Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TupleTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getTupleType()
     * @generated
     */
    int TUPLE_TYPE = 14;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Fields</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE__FIELDS = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tuple Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Tuple Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.FieldImpl <em>Field</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.FieldImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getField()
     * @generated
     */
    int FIELD = 15;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD__TYPE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Field</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Field</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_OPERATION_COUNT = PositionPackage.POSITION_OBJECT_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.FuncTypeImpl <em>Func Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.FuncTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getFuncType()
     * @generated
     */
    int FUNC_TYPE = 16;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNC_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Return Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNC_TYPE__RETURN_TYPE = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Param Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNC_TYPE__PARAM_TYPES = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Func Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNC_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Func Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNC_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.DistTypeImpl <em>Dist Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.DistTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getDistType()
     * @generated
     */
    int DIST_TYPE = 17;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIST_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Sample Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIST_TYPE__SAMPLE_TYPE = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Dist Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIST_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Dist Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIST_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.VoidTypeImpl <em>Void Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.VoidTypeImpl
     * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getVoidType()
     * @generated
     */
    int VOID_TYPE = 18;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VOID_TYPE__POSITION = CIF_TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Void Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VOID_TYPE_FEATURE_COUNT = CIF_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Void Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VOID_TYPE_OPERATION_COUNT = CIF_TYPE_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.CifType <em>Cif Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Cif Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.CifType
     * @generated
     */
    EClass getCifType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.BoolType <em>Bool Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bool Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.BoolType
     * @generated
     */
    EClass getBoolType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.IntType <em>Int Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Int Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.IntType
     * @generated
     */
    EClass getIntType();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.types.IntType#getLower <em>Lower</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lower</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.IntType#getLower()
     * @see #getIntType()
     * @generated
     */
    EAttribute getIntType_Lower();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.types.IntType#getUpper <em>Upper</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Upper</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.IntType#getUpper()
     * @see #getIntType()
     * @generated
     */
    EAttribute getIntType_Upper();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.TypeRef <em>Type Ref</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Ref</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypeRef
     * @generated
     */
    EClass getTypeRef();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.types.TypeRef#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.TypeRef#getType()
     * @see #getTypeRef()
     * @generated
     */
    EReference getTypeRef_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.EnumType <em>Enum Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Enum Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.EnumType
     * @generated
     */
    EClass getEnumType();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.types.EnumType#getEnum <em>Enum</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Enum</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.EnumType#getEnum()
     * @see #getEnumType()
     * @generated
     */
    EReference getEnumType_Enum();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType <em>Comp Param Wrap Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Comp Param Wrap Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType
     * @generated
     */
    EClass getCompParamWrapType();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parameter</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType#getParameter()
     * @see #getCompParamWrapType()
     * @generated
     */
    EReference getCompParamWrapType_Parameter();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType#getReference()
     * @see #getCompParamWrapType()
     * @generated
     */
    EReference getCompParamWrapType_Reference();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType <em>Comp Inst Wrap Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Comp Inst Wrap Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType
     * @generated
     */
    EClass getCompInstWrapType();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType#getInstantiation <em>Instantiation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Instantiation</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType#getInstantiation()
     * @see #getCompInstWrapType()
     * @generated
     */
    EReference getCompInstWrapType_Instantiation();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType#getReference()
     * @see #getCompInstWrapType()
     * @generated
     */
    EReference getCompInstWrapType_Reference();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.ComponentType <em>Component Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.ComponentType
     * @generated
     */
    EClass getComponentType();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.types.ComponentType#getComponent <em>Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Component</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.ComponentType#getComponent()
     * @see #getComponentType()
     * @generated
     */
    EReference getComponentType_Component();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType <em>Component Def Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Def Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType
     * @generated
     */
    EClass getComponentDefType();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType#getDefinition <em>Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Definition</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType#getDefinition()
     * @see #getComponentDefType()
     * @generated
     */
    EReference getComponentDefType_Definition();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.RealType <em>Real Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Real Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.RealType
     * @generated
     */
    EClass getRealType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.StringType <em>String Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>String Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.StringType
     * @generated
     */
    EClass getStringType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.ListType <em>List Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>List Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.ListType
     * @generated
     */
    EClass getListType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.ListType#getElementType <em>Element Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Element Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.ListType#getElementType()
     * @see #getListType()
     * @generated
     */
    EReference getListType_ElementType();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.types.ListType#getLower <em>Lower</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lower</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.ListType#getLower()
     * @see #getListType()
     * @generated
     */
    EAttribute getListType_Lower();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.types.ListType#getUpper <em>Upper</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Upper</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.ListType#getUpper()
     * @see #getListType()
     * @generated
     */
    EAttribute getListType_Upper();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.SetType <em>Set Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Set Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.SetType
     * @generated
     */
    EClass getSetType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.SetType#getElementType <em>Element Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Element Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.SetType#getElementType()
     * @see #getSetType()
     * @generated
     */
    EReference getSetType_ElementType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.DictType <em>Dict Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dict Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.DictType
     * @generated
     */
    EClass getDictType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.DictType#getKeyType <em>Key Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Key Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.DictType#getKeyType()
     * @see #getDictType()
     * @generated
     */
    EReference getDictType_KeyType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.DictType#getValueType <em>Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.DictType#getValueType()
     * @see #getDictType()
     * @generated
     */
    EReference getDictType_ValueType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.TupleType <em>Tuple Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tuple Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.TupleType
     * @generated
     */
    EClass getTupleType();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.types.TupleType#getFields <em>Fields</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Fields</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.TupleType#getFields()
     * @see #getTupleType()
     * @generated
     */
    EReference getTupleType_Fields();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.Field <em>Field</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Field</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.Field
     * @generated
     */
    EClass getField();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.cif.metamodel.cif.types.Field#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.Field#getName()
     * @see #getField()
     * @generated
     */
    EAttribute getField_Name();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.Field#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.Field#getType()
     * @see #getField()
     * @generated
     */
    EReference getField_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.FuncType <em>Func Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Func Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.FuncType
     * @generated
     */
    EClass getFuncType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.FuncType#getReturnType <em>Return Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Return Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.FuncType#getReturnType()
     * @see #getFuncType()
     * @generated
     */
    EReference getFuncType_ReturnType();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.cif.metamodel.cif.types.FuncType#getParamTypes <em>Param Types</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Param Types</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.FuncType#getParamTypes()
     * @see #getFuncType()
     * @generated
     */
    EReference getFuncType_ParamTypes();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.DistType <em>Dist Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dist Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.DistType
     * @generated
     */
    EClass getDistType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.cif.metamodel.cif.types.DistType#getSampleType <em>Sample Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Sample Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.DistType#getSampleType()
     * @see #getDistType()
     * @generated
     */
    EReference getDistType_SampleType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.cif.metamodel.cif.types.VoidType <em>Void Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Void Type</em>'.
     * @see org.eclipse.escet.cif.metamodel.cif.types.VoidType
     * @generated
     */
    EClass getVoidType();

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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.CifTypeImpl <em>Cif Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.CifTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getCifType()
         * @generated
         */
        EClass CIF_TYPE = eINSTANCE.getCifType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.BoolTypeImpl <em>Bool Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.BoolTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getBoolType()
         * @generated
         */
        EClass BOOL_TYPE = eINSTANCE.getBoolType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.IntTypeImpl <em>Int Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.IntTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getIntType()
         * @generated
         */
        EClass INT_TYPE = eINSTANCE.getIntType();

        /**
         * The meta object literal for the '<em><b>Lower</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INT_TYPE__LOWER = eINSTANCE.getIntType_Lower();

        /**
         * The meta object literal for the '<em><b>Upper</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INT_TYPE__UPPER = eINSTANCE.getIntType_Upper();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.TypeRefImpl <em>Type Ref</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypeRefImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getTypeRef()
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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.EnumTypeImpl <em>Enum Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.EnumTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getEnumType()
         * @generated
         */
        EClass ENUM_TYPE = eINSTANCE.getEnumType();

        /**
         * The meta object literal for the '<em><b>Enum</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ENUM_TYPE__ENUM = eINSTANCE.getEnumType_Enum();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.CompParamWrapTypeImpl <em>Comp Param Wrap Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.CompParamWrapTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getCompParamWrapType()
         * @generated
         */
        EClass COMP_PARAM_WRAP_TYPE = eINSTANCE.getCompParamWrapType();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_PARAM_WRAP_TYPE__PARAMETER = eINSTANCE.getCompParamWrapType_Parameter();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_PARAM_WRAP_TYPE__REFERENCE = eINSTANCE.getCompParamWrapType_Reference();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.CompInstWrapTypeImpl <em>Comp Inst Wrap Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.CompInstWrapTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getCompInstWrapType()
         * @generated
         */
        EClass COMP_INST_WRAP_TYPE = eINSTANCE.getCompInstWrapType();

        /**
         * The meta object literal for the '<em><b>Instantiation</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_INST_WRAP_TYPE__INSTANTIATION = eINSTANCE.getCompInstWrapType_Instantiation();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMP_INST_WRAP_TYPE__REFERENCE = eINSTANCE.getCompInstWrapType_Reference();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.ComponentTypeImpl <em>Component Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.ComponentTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getComponentType()
         * @generated
         */
        EClass COMPONENT_TYPE = eINSTANCE.getComponentType();

        /**
         * The meta object literal for the '<em><b>Component</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_TYPE__COMPONENT = eINSTANCE.getComponentType_Component();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.ComponentDefTypeImpl <em>Component Def Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.ComponentDefTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getComponentDefType()
         * @generated
         */
        EClass COMPONENT_DEF_TYPE = eINSTANCE.getComponentDefType();

        /**
         * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_DEF_TYPE__DEFINITION = eINSTANCE.getComponentDefType_Definition();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.RealTypeImpl <em>Real Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.RealTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getRealType()
         * @generated
         */
        EClass REAL_TYPE = eINSTANCE.getRealType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.StringTypeImpl <em>String Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.StringTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getStringType()
         * @generated
         */
        EClass STRING_TYPE = eINSTANCE.getStringType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.ListTypeImpl <em>List Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.ListTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getListType()
         * @generated
         */
        EClass LIST_TYPE = eINSTANCE.getListType();

        /**
         * The meta object literal for the '<em><b>Element Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LIST_TYPE__ELEMENT_TYPE = eINSTANCE.getListType_ElementType();

        /**
         * The meta object literal for the '<em><b>Lower</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LIST_TYPE__LOWER = eINSTANCE.getListType_Lower();

        /**
         * The meta object literal for the '<em><b>Upper</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LIST_TYPE__UPPER = eINSTANCE.getListType_Upper();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.SetTypeImpl <em>Set Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.SetTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getSetType()
         * @generated
         */
        EClass SET_TYPE = eINSTANCE.getSetType();

        /**
         * The meta object literal for the '<em><b>Element Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SET_TYPE__ELEMENT_TYPE = eINSTANCE.getSetType_ElementType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.DictTypeImpl <em>Dict Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.DictTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getDictType()
         * @generated
         */
        EClass DICT_TYPE = eINSTANCE.getDictType();

        /**
         * The meta object literal for the '<em><b>Key Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DICT_TYPE__KEY_TYPE = eINSTANCE.getDictType_KeyType();

        /**
         * The meta object literal for the '<em><b>Value Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DICT_TYPE__VALUE_TYPE = eINSTANCE.getDictType_ValueType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.TupleTypeImpl <em>Tuple Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TupleTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getTupleType()
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
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.FieldImpl <em>Field</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.FieldImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getField()
         * @generated
         */
        EClass FIELD = eINSTANCE.getField();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FIELD__NAME = eINSTANCE.getField_Name();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FIELD__TYPE = eINSTANCE.getField_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.FuncTypeImpl <em>Func Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.FuncTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getFuncType()
         * @generated
         */
        EClass FUNC_TYPE = eINSTANCE.getFuncType();

        /**
         * The meta object literal for the '<em><b>Return Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNC_TYPE__RETURN_TYPE = eINSTANCE.getFuncType_ReturnType();

        /**
         * The meta object literal for the '<em><b>Param Types</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNC_TYPE__PARAM_TYPES = eINSTANCE.getFuncType_ParamTypes();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.DistTypeImpl <em>Dist Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.DistTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getDistType()
         * @generated
         */
        EClass DIST_TYPE = eINSTANCE.getDistType();

        /**
         * The meta object literal for the '<em><b>Sample Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIST_TYPE__SAMPLE_TYPE = eINSTANCE.getDistType_SampleType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.cif.metamodel.cif.types.impl.VoidTypeImpl <em>Void Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.VoidTypeImpl
         * @see org.eclipse.escet.cif.metamodel.cif.types.impl.TypesPackageImpl#getVoidType()
         * @generated
         */
        EClass VOID_TYPE = eINSTANCE.getVoidType();

    }

} //TypesPackage
