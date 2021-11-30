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
package org.eclipse.escet.chi.metamodel.chi;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
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
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.chi.metamodel.chi.ChiFactory
 * @model kind="package"
 * @generated
 */
public interface ChiPackage extends EPackage
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "chi";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://eclipse.org/escet/chi";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "chi";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ChiPackage eINSTANCE = org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TypeImpl <em>Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.TypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getType()
     * @generated
     */
    int TYPE = 0;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.VoidTypeImpl <em>Void Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.VoidTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getVoidType()
     * @generated
     */
    int VOID_TYPE = 1;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VOID_TYPE__POSITION = TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Void Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VOID_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BoolTypeImpl <em>Bool Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.BoolTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBoolType()
     * @generated
     */
    int BOOL_TYPE = 2;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_TYPE__POSITION = TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Bool Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.InstanceTypeImpl <em>Instance Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.InstanceTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getInstanceType()
     * @generated
     */
    int INSTANCE_TYPE = 3;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSTANCE_TYPE__POSITION = TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Instance Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSTANCE_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IntTypeImpl <em>Int Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.IntTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIntType()
     * @generated
     */
    int INT_TYPE = 4;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE__POSITION = TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Int Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.StringTypeImpl <em>String Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.StringTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStringType()
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
    int STRING_TYPE__POSITION = TYPE__POSITION;

    /**
     * The number of structural features of the '<em>String Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.RealTypeImpl <em>Real Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.RealTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getRealType()
     * @generated
     */
    int REAL_TYPE = 6;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_TYPE__POSITION = TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Real Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FileTypeImpl <em>File Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.FileTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFileType()
     * @generated
     */
    int FILE_TYPE = 7;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TYPE__POSITION = TYPE__POSITION;

    /**
     * The number of structural features of the '<em>File Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILE_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SetTypeImpl <em>Set Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.SetTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSetType()
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
    int SET_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ELEMENT_TYPE = TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ListTypeImpl <em>List Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ListTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getListType()
     * @generated
     */
    int LIST_TYPE = 9;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__ELEMENT_TYPE = TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Initial Length</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE__INITIAL_LENGTH = TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>List Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DictTypeImpl <em>Dict Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.DictTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDictType()
     * @generated
     */
    int DICT_TYPE = 10;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Key Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE__KEY_TYPE = TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE__VALUE_TYPE = TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Dict Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICT_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.MatrixTypeImpl <em>Matrix Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.MatrixTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getMatrixType()
     * @generated
     */
    int MATRIX_TYPE = 11;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Row Size</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_TYPE__ROW_SIZE = TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Column Size</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_TYPE__COLUMN_SIZE = TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Matrix Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TupleTypeImpl <em>Tuple Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.TupleTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTupleType()
     * @generated
     */
    int TUPLE_TYPE = 12;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Fields</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE__FIELDS = TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tuple Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TupleFieldImpl <em>Tuple Field</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.TupleFieldImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTupleField()
     * @generated
     */
    int TUPLE_FIELD = 13;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_FIELD__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_FIELD__TYPE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_FIELD__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Tuple Field</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_FIELD_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DistributionTypeImpl <em>Distribution Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.DistributionTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDistributionType()
     * @generated
     */
    int DISTRIBUTION_TYPE = 14;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Result Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_TYPE__RESULT_TYPE = TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Distribution Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTION_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.EnumTypeReferenceImpl <em>Enum Type Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.EnumTypeReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getEnumTypeReference()
     * @generated
     */
    int ENUM_TYPE_REFERENCE = 15;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_TYPE_REFERENCE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_TYPE_REFERENCE__TYPE = TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Enum Type Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_TYPE_REFERENCE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ChannelTypeImpl <em>Channel Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChannelTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChannelType()
     * @generated
     */
    int CHANNEL_TYPE = 16;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANNEL_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANNEL_TYPE__ELEMENT_TYPE = TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Ops</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANNEL_TYPE__OPS = TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Channel Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANNEL_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionTypeImpl <em>Function Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.FunctionTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFunctionType()
     * @generated
     */
    int FUNCTION_TYPE = 17;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Result Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_TYPE__RESULT_TYPE = TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Parameter Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_TYPE__PARAMETER_TYPES = TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Function Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TypeReferenceImpl <em>Type Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.TypeReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTypeReference()
     * @generated
     */
    int TYPE_REFERENCE = 18;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REFERENCE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REFERENCE__TYPE = TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Type Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_REFERENCE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ExpressionImpl <em>Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getExpression()
     * @generated
     */
    int EXPRESSION = 19;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXPRESSION__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXPRESSION__TYPE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXPRESSION_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BoolLiteralImpl <em>Bool Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.BoolLiteralImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBoolLiteral()
     * @generated
     */
    int BOOL_LITERAL = 20;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_LITERAL__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_LITERAL__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_LITERAL__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Bool Literal</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOOL_LITERAL_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IntNumberImpl <em>Int Number</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.IntNumberImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIntNumber()
     * @generated
     */
    int INT_NUMBER = 21;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_NUMBER__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_NUMBER__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_NUMBER__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Int Number</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INT_NUMBER_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.RealNumberImpl <em>Real Number</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.RealNumberImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getRealNumber()
     * @generated
     */
    int REAL_NUMBER = 22;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_NUMBER__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_NUMBER__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_NUMBER__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Real Number</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REAL_NUMBER_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.StringLiteralImpl <em>String Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.StringLiteralImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStringLiteral()
     * @generated
     */
    int STRING_LITERAL = 23;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_LITERAL__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_LITERAL__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_LITERAL__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>String Literal</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_LITERAL_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TupleExpressionImpl <em>Tuple Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.TupleExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTupleExpression()
     * @generated
     */
    int TUPLE_EXPRESSION = 24;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Fields</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION__FIELDS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Tuple Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TUPLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ListExpressionImpl <em>List Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ListExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getListExpression()
     * @generated
     */
    int LIST_EXPRESSION = 25;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION__ELEMENTS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>List Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SetExpressionImpl <em>Set Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.SetExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSetExpression()
     * @generated
     */
    int SET_EXPRESSION = 26;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION__ELEMENTS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Set Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.MatrixExpressionImpl <em>Matrix Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.MatrixExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getMatrixExpression()
     * @generated
     */
    int MATRIX_EXPRESSION = 27;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Rows</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_EXPRESSION__ROWS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Matrix Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.MatrixRowImpl <em>Matrix Row</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.MatrixRowImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getMatrixRow()
     * @generated
     */
    int MATRIX_ROW = 28;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_ROW__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Elements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_ROW__ELEMENTS = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Matrix Row</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MATRIX_ROW_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DictionaryExpressionImpl <em>Dictionary Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.DictionaryExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDictionaryExpression()
     * @generated
     */
    int DICTIONARY_EXPRESSION = 29;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICTIONARY_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICTIONARY_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Pairs</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICTIONARY_EXPRESSION__PAIRS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Dictionary Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICTIONARY_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DictionaryPairImpl <em>Dictionary Pair</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.DictionaryPairImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDictionaryPair()
     * @generated
     */
    int DICTIONARY_PAIR = 30;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICTIONARY_PAIR__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Key</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICTIONARY_PAIR__KEY = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICTIONARY_PAIR__VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Dictionary Pair</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DICTIONARY_PAIR_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.VariableReferenceImpl <em>Variable Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.VariableReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getVariableReference()
     * @generated
     */
    int VARIABLE_REFERENCE = 31;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_REFERENCE__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_REFERENCE__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_REFERENCE__VARIABLE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Variable Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_REFERENCE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ConstantReferenceImpl <em>Constant Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ConstantReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getConstantReference()
     * @generated
     */
    int CONSTANT_REFERENCE = 32;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_REFERENCE__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_REFERENCE__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Constant</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_REFERENCE__CONSTANT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Constant Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_REFERENCE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TimeLiteralImpl <em>Time Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.TimeLiteralImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTimeLiteral()
     * @generated
     */
    int TIME_LITERAL = 33;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_LITERAL__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_LITERAL__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Time Literal</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_LITERAL_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.UnaryExpressionImpl <em>Unary Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.UnaryExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnaryExpression()
     * @generated
     */
    int UNARY_EXPRESSION = 34;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Child</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION__CHILD = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Op</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION__OP = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Unary Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BinaryExpressionImpl <em>Binary Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.BinaryExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBinaryExpression()
     * @generated
     */
    int BINARY_EXPRESSION = 35;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Left</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__LEFT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Right</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__RIGHT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Op</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION__OP = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Binary Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CallExpressionImpl <em>Call Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.CallExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCallExpression()
     * @generated
     */
    int CALL_EXPRESSION = 36;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CALL_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CALL_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CALL_EXPRESSION__FUNCTION = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CALL_EXPRESSION__ARGUMENTS = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CALL_EXPRESSION__NAME = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Call Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CALL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BaseFunctionReferenceImpl <em>Base Function Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.BaseFunctionReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBaseFunctionReference()
     * @generated
     */
    int BASE_FUNCTION_REFERENCE = 38;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASE_FUNCTION_REFERENCE__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASE_FUNCTION_REFERENCE__TYPE = EXPRESSION__TYPE;

    /**
     * The number of structural features of the '<em>Base Function Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASE_FUNCTION_REFERENCE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionReferenceImpl <em>Function Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.FunctionReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFunctionReference()
     * @generated
     */
    int FUNCTION_REFERENCE = 37;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_REFERENCE__POSITION = BASE_FUNCTION_REFERENCE__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_REFERENCE__TYPE = BASE_FUNCTION_REFERENCE__TYPE;

    /**
     * The feature id for the '<em><b>Function</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_REFERENCE__FUNCTION = BASE_FUNCTION_REFERENCE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Function Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_REFERENCE_FEATURE_COUNT = BASE_FUNCTION_REFERENCE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.StdLibFunctionReferenceImpl <em>Std Lib Function Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.StdLibFunctionReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStdLibFunctionReference()
     * @generated
     */
    int STD_LIB_FUNCTION_REFERENCE = 39;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_REFERENCE__POSITION = BASE_FUNCTION_REFERENCE__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_REFERENCE__TYPE = BASE_FUNCTION_REFERENCE__TYPE;

    /**
     * The feature id for the '<em><b>Function</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_REFERENCE__FUNCTION = BASE_FUNCTION_REFERENCE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Std Lib Function Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STD_LIB_FUNCTION_REFERENCE_FEATURE_COUNT = BASE_FUNCTION_REFERENCE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SliceExpressionImpl <em>Slice Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.SliceExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSliceExpression()
     * @generated
     */
    int SLICE_EXPRESSION = 40;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Start</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__START = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>End</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__END = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Step</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__STEP = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION__SOURCE = EXPRESSION_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Slice Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SLICE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FieldReferenceImpl <em>Field Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.FieldReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFieldReference()
     * @generated
     */
    int FIELD_REFERENCE = 41;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_REFERENCE__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_REFERENCE__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Field</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_REFERENCE__FIELD = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Field Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_REFERENCE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.StatementImpl <em>Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.StatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStatement()
     * @generated
     */
    int STATEMENT = 42;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATEMENT__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATEMENT_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BreakStatementImpl <em>Break Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.BreakStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBreakStatement()
     * @generated
     */
    int BREAK_STATEMENT = 43;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BREAK_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The number of structural features of the '<em>Break Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BREAK_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ContinueStatementImpl <em>Continue Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ContinueStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getContinueStatement()
     * @generated
     */
    int CONTINUE_STATEMENT = 44;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTINUE_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The number of structural features of the '<em>Continue Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTINUE_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.PassStatementImpl <em>Pass Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.PassStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getPassStatement()
     * @generated
     */
    int PASS_STATEMENT = 45;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASS_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The number of structural features of the '<em>Pass Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PASS_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ExitStatementImpl <em>Exit Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ExitStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getExitStatement()
     * @generated
     */
    int EXIT_STATEMENT = 46;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXIT_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXIT_STATEMENT__VALUE = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Exit Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXIT_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ReturnStatementImpl <em>Return Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ReturnStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getReturnStatement()
     * @generated
     */
    int RETURN_STATEMENT = 47;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_STATEMENT__VALUE = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Return Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DelayStatementImpl <em>Delay Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.DelayStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDelayStatement()
     * @generated
     */
    int DELAY_STATEMENT = 48;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELAY_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Length</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELAY_STATEMENT__LENGTH = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Delay Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELAY_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.WhileStatementImpl <em>While Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.WhileStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getWhileStatement()
     * @generated
     */
    int WHILE_STATEMENT = 49;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT__CONDITION = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Body</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT__BODY = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>While Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WHILE_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IfStatementImpl <em>If Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.IfStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIfStatement()
     * @generated
     */
    int IF_STATEMENT = 50;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Cases</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT__CASES = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>If Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IfCaseImpl <em>If Case</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.IfCaseImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIfCase()
     * @generated
     */
    int IF_CASE = 51;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_CASE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_CASE__CONDITION = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Body</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_CASE__BODY = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>If Case</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IF_CASE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.WriteStatementImpl <em>Write Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.WriteStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getWriteStatement()
     * @generated
     */
    int WRITE_STATEMENT = 52;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WRITE_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WRITE_STATEMENT__VALUES = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Add Newline</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WRITE_STATEMENT__ADD_NEWLINE = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Write Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WRITE_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.AssignmentStatementImpl <em>Assignment Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.AssignmentStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getAssignmentStatement()
     * @generated
     */
    int ASSIGNMENT_STATEMENT = 53;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Lhs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT__LHS = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Rhs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT__RHS = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Assignment Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSIGNMENT_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CommunicationStatementImpl <em>Communication Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.CommunicationStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCommunicationStatement()
     * @generated
     */
    int COMMUNICATION_STATEMENT = 54;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Channel</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_STATEMENT__CHANNEL = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_STATEMENT__DATA = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Communication Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMMUNICATION_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ForStatementImpl <em>For Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ForStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getForStatement()
     * @generated
     */
    int FOR_STATEMENT = 55;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Body</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT__BODY = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Unwinds</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT__UNWINDS = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>For Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FOR_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CreateCaseImpl <em>Create Case</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.CreateCaseImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCreateCase()
     * @generated
     */
    int CREATE_CASE = 56;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CREATE_CASE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The number of structural features of the '<em>Create Case</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CREATE_CASE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.RunStatementImpl <em>Run Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.RunStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getRunStatement()
     * @generated
     */
    int RUN_STATEMENT = 57;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RUN_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Cases</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RUN_STATEMENT__CASES = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Start Only</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RUN_STATEMENT__START_ONLY = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Run Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RUN_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SelectStatementImpl <em>Select Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.SelectStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSelectStatement()
     * @generated
     */
    int SELECT_STATEMENT = 58;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELECT_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Cases</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELECT_STATEMENT__CASES = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Select Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELECT_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SelectCaseImpl <em>Select Case</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.SelectCaseImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSelectCase()
     * @generated
     */
    int SELECT_CASE = 59;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELECT_CASE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Body</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELECT_CASE__BODY = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Guard</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELECT_CASE__GUARD = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Select Case</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SELECT_CASE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IteratedCreateCaseImpl <em>Iterated Create Case</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.IteratedCreateCaseImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIteratedCreateCase()
     * @generated
     */
    int ITERATED_CREATE_CASE = 60;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_CREATE_CASE__POSITION = CREATE_CASE__POSITION;

    /**
     * The feature id for the '<em><b>Unwinds</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_CREATE_CASE__UNWINDS = CREATE_CASE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Instances</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_CREATE_CASE__INSTANCES = CREATE_CASE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Iterated Create Case</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_CREATE_CASE_FEATURE_COUNT = CREATE_CASE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IteratedSelectCaseImpl <em>Iterated Select Case</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.IteratedSelectCaseImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIteratedSelectCase()
     * @generated
     */
    int ITERATED_SELECT_CASE = 61;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_SELECT_CASE__POSITION = SELECT_CASE__POSITION;

    /**
     * The feature id for the '<em><b>Body</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_SELECT_CASE__BODY = SELECT_CASE__BODY;

    /**
     * The feature id for the '<em><b>Guard</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_SELECT_CASE__GUARD = SELECT_CASE__GUARD;

    /**
     * The feature id for the '<em><b>Unwinds</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_SELECT_CASE__UNWINDS = SELECT_CASE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Iterated Select Case</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ITERATED_SELECT_CASE_FEATURE_COUNT = SELECT_CASE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SpecificationImpl <em>Specification</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.SpecificationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSpecification()
     * @generated
     */
    int SPECIFICATION = 62;

    /**
     * The feature id for the '<em><b>Declarations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION__DECLARATIONS = 0;

    /**
     * The number of structural features of the '<em>Specification</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIFICATION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DeclarationImpl <em>Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.DeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDeclaration()
     * @generated
     */
    int DECLARATION = 63;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DECLARATION_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TypeDeclarationImpl <em>Type Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.TypeDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTypeDeclaration()
     * @generated
     */
    int TYPE_DECLARATION = 64;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECLARATION__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECLARATION__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECLARATION__TYPE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Type Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TYPE_DECLARATION_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ConstantDeclarationImpl <em>Constant Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ConstantDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getConstantDeclaration()
     * @generated
     */
    int CONSTANT_DECLARATION = 65;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_DECLARATION__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_DECLARATION__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_DECLARATION__TYPE = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_DECLARATION__VALUE = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Constant Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONSTANT_DECLARATION_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BehaviourDeclarationImpl <em>Behaviour Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.BehaviourDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBehaviourDeclaration()
     * @generated
     */
    int BEHAVIOUR_DECLARATION = 70;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOUR_DECLARATION__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOUR_DECLARATION__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Variables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOUR_DECLARATION__VARIABLES = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOUR_DECLARATION__STATEMENTS = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Behaviour Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BEHAVIOUR_DECLARATION_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ComputeDeclarationImpl <em>Compute Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ComputeDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getComputeDeclaration()
     * @generated
     */
    int COMPUTE_DECLARATION = 90;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_DECLARATION__POSITION = BEHAVIOUR_DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_DECLARATION__NAME = BEHAVIOUR_DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Variables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_DECLARATION__VARIABLES = BEHAVIOUR_DECLARATION__VARIABLES;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_DECLARATION__STATEMENTS = BEHAVIOUR_DECLARATION__STATEMENTS;

    /**
     * The feature id for the '<em><b>Return Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_DECLARATION__RETURN_TYPE = BEHAVIOUR_DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Compute Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_DECLARATION_FEATURE_COUNT = BEHAVIOUR_DECLARATION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessDeclarationImpl <em>Process Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ProcessDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getProcessDeclaration()
     * @generated
     */
    int PROCESS_DECLARATION = 66;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DECLARATION__POSITION = COMPUTE_DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DECLARATION__NAME = COMPUTE_DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Variables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DECLARATION__VARIABLES = COMPUTE_DECLARATION__VARIABLES;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DECLARATION__STATEMENTS = COMPUTE_DECLARATION__STATEMENTS;

    /**
     * The feature id for the '<em><b>Return Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DECLARATION__RETURN_TYPE = COMPUTE_DECLARATION__RETURN_TYPE;

    /**
     * The number of structural features of the '<em>Process Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DECLARATION_FEATURE_COUNT = COMPUTE_DECLARATION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionDeclarationImpl <em>Function Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.FunctionDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFunctionDeclaration()
     * @generated
     */
    int FUNCTION_DECLARATION = 67;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_DECLARATION__POSITION = BEHAVIOUR_DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_DECLARATION__NAME = BEHAVIOUR_DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Variables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_DECLARATION__VARIABLES = BEHAVIOUR_DECLARATION__VARIABLES;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_DECLARATION__STATEMENTS = BEHAVIOUR_DECLARATION__STATEMENTS;

    /**
     * The feature id for the '<em><b>Return Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_DECLARATION__RETURN_TYPE = BEHAVIOUR_DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Function Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_DECLARATION_FEATURE_COUNT = BEHAVIOUR_DECLARATION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ModelDeclarationImpl <em>Model Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ModelDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getModelDeclaration()
     * @generated
     */
    int MODEL_DECLARATION = 68;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_DECLARATION__POSITION = COMPUTE_DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_DECLARATION__NAME = COMPUTE_DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Variables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_DECLARATION__VARIABLES = COMPUTE_DECLARATION__VARIABLES;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_DECLARATION__STATEMENTS = COMPUTE_DECLARATION__STATEMENTS;

    /**
     * The feature id for the '<em><b>Return Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_DECLARATION__RETURN_TYPE = COMPUTE_DECLARATION__RETURN_TYPE;

    /**
     * The number of structural features of the '<em>Model Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_DECLARATION_FEATURE_COUNT = COMPUTE_DECLARATION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.VariableDeclarationImpl <em>Variable Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.VariableDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getVariableDeclaration()
     * @generated
     */
    int VARIABLE_DECLARATION = 69;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_DECLARATION__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Initial Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_DECLARATION__INITIAL_VALUE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_DECLARATION__TYPE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_DECLARATION__PARAMETER = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_DECLARATION__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Variable Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VARIABLE_DECLARATION_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ReceiveStatementImpl <em>Receive Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ReceiveStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getReceiveStatement()
     * @generated
     */
    int RECEIVE_STATEMENT = 71;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECEIVE_STATEMENT__POSITION = COMMUNICATION_STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Channel</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECEIVE_STATEMENT__CHANNEL = COMMUNICATION_STATEMENT__CHANNEL;

    /**
     * The feature id for the '<em><b>Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECEIVE_STATEMENT__DATA = COMMUNICATION_STATEMENT__DATA;

    /**
     * The number of structural features of the '<em>Receive Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECEIVE_STATEMENT_FEATURE_COUNT = COMMUNICATION_STATEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SendStatementImpl <em>Send Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.SendStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSendStatement()
     * @generated
     */
    int SEND_STATEMENT = 72;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEND_STATEMENT__POSITION = COMMUNICATION_STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Channel</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEND_STATEMENT__CHANNEL = COMMUNICATION_STATEMENT__CHANNEL;

    /**
     * The feature id for the '<em><b>Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEND_STATEMENT__DATA = COMMUNICATION_STATEMENT__DATA;

    /**
     * The number of structural features of the '<em>Send Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEND_STATEMENT_FEATURE_COUNT = COMMUNICATION_STATEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.EnumValueReferenceImpl <em>Enum Value Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.EnumValueReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getEnumValueReference()
     * @generated
     */
    int ENUM_VALUE_REFERENCE = 73;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_VALUE_REFERENCE__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_VALUE_REFERENCE__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Value</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_VALUE_REFERENCE__VALUE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Enum Value Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_VALUE_REFERENCE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ReadCallExpressionImpl <em>Read Call Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ReadCallExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getReadCallExpression()
     * @generated
     */
    int READ_CALL_EXPRESSION = 74;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int READ_CALL_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int READ_CALL_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int READ_CALL_EXPRESSION__FILE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Load Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int READ_CALL_EXPRESSION__LOAD_TYPE = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Read Call Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int READ_CALL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.UnwindImpl <em>Unwind</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.UnwindImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnwind()
     * @generated
     */
    int UNWIND = 75;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNWIND__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNWIND__SOURCE = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Variables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNWIND__VARIABLES = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Unwind</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNWIND_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessInstanceImpl <em>Process Instance</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ProcessInstanceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getProcessInstance()
     * @generated
     */
    int PROCESS_INSTANCE = 76;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_INSTANCE__POSITION = CREATE_CASE__POSITION;

    /**
     * The feature id for the '<em><b>Call</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_INSTANCE__CALL = CREATE_CASE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Var</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_INSTANCE__VAR = CREATE_CASE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Process Instance</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_INSTANCE_FEATURE_COUNT = CREATE_CASE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ComputeTypeImpl <em>Compute Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ComputeTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getComputeType()
     * @generated
     */
    int COMPUTE_TYPE = 91;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Parameter Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_TYPE__PARAMETER_TYPES = TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Exit Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_TYPE__EXIT_TYPE = TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Compute Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPUTE_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessTypeImpl <em>Process Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ProcessTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getProcessType()
     * @generated
     */
    int PROCESS_TYPE = 77;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_TYPE__POSITION = COMPUTE_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Parameter Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_TYPE__PARAMETER_TYPES = COMPUTE_TYPE__PARAMETER_TYPES;

    /**
     * The feature id for the '<em><b>Exit Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_TYPE__EXIT_TYPE = COMPUTE_TYPE__EXIT_TYPE;

    /**
     * The number of structural features of the '<em>Process Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_TYPE_FEATURE_COUNT = COMPUTE_TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessReferenceImpl <em>Process Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ProcessReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getProcessReference()
     * @generated
     */
    int PROCESS_REFERENCE = 78;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_REFERENCE__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_REFERENCE__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Process</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_REFERENCE__PROCESS = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Process Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_REFERENCE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.UnresolvedReferenceImpl <em>Unresolved Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.UnresolvedReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnresolvedReference()
     * @generated
     */
    int UNRESOLVED_REFERENCE = 79;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REFERENCE__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REFERENCE__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REFERENCE__NAME = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Unresolved Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_REFERENCE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.UnresolvedTypeImpl <em>Unresolved Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.UnresolvedTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnresolvedType()
     * @generated
     */
    int UNRESOLVED_TYPE = 80;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_TYPE__POSITION = TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_TYPE__NAME = TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Unresolved Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNRESOLVED_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TimerTypeImpl <em>Timer Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.TimerTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTimerType()
     * @generated
     */
    int TIMER_TYPE = 81;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIMER_TYPE__POSITION = TYPE__POSITION;

    /**
     * The number of structural features of the '<em>Timer Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIMER_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.EnumDeclarationImpl <em>Enum Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.EnumDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getEnumDeclaration()
     * @generated
     */
    int ENUM_DECLARATION = 82;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECLARATION__POSITION = DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECLARATION__NAME = DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECLARATION__VALUES = DECLARATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Enum Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_DECLARATION_FEATURE_COUNT = DECLARATION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.EnumValueImpl <em>Enum Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.EnumValueImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getEnumValue()
     * @generated
     */
    int ENUM_VALUE = 83;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_VALUE__POSITION = PositionPackage.POSITION_OBJECT__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_VALUE__NAME = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Enum Value</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENUM_VALUE_FEATURE_COUNT = PositionPackage.POSITION_OBJECT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ChannelExpressionImpl <em>Channel Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChannelExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChannelExpression()
     * @generated
     */
    int CHANNEL_EXPRESSION = 84;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANNEL_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANNEL_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Element Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANNEL_EXPRESSION__ELEMENT_TYPE = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Channel Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANNEL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CastExpressionImpl <em>Cast Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.CastExpressionImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCastExpression()
     * @generated
     */
    int CAST_EXPRESSION = 85;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION__EXPRESSION = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Cast Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION__CAST_TYPE = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Cast Expression</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAST_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CloseStatementImpl <em>Close Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.CloseStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCloseStatement()
     * @generated
     */
    int CLOSE_STATEMENT = 86;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CLOSE_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Handle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CLOSE_STATEMENT__HANDLE = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Close Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CLOSE_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FinishStatementImpl <em>Finish Statement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.FinishStatementImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFinishStatement()
     * @generated
     */
    int FINISH_STATEMENT = 87;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FINISH_STATEMENT__POSITION = STATEMENT__POSITION;

    /**
     * The feature id for the '<em><b>Instances</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FINISH_STATEMENT__INSTANCES = STATEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Finish Statement</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FINISH_STATEMENT_FEATURE_COUNT = STATEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ModelReferenceImpl <em>Model Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ModelReferenceImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getModelReference()
     * @generated
     */
    int MODEL_REFERENCE = 88;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_REFERENCE__POSITION = EXPRESSION__POSITION;

    /**
     * The feature id for the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_REFERENCE__TYPE = EXPRESSION__TYPE;

    /**
     * The feature id for the '<em><b>Model</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_REFERENCE__MODEL = EXPRESSION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Model Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_REFERENCE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.XperDeclarationImpl <em>Xper Declaration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.XperDeclarationImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getXperDeclaration()
     * @generated
     */
    int XPER_DECLARATION = 89;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XPER_DECLARATION__POSITION = BEHAVIOUR_DECLARATION__POSITION;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XPER_DECLARATION__NAME = BEHAVIOUR_DECLARATION__NAME;

    /**
     * The feature id for the '<em><b>Variables</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XPER_DECLARATION__VARIABLES = BEHAVIOUR_DECLARATION__VARIABLES;

    /**
     * The feature id for the '<em><b>Statements</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XPER_DECLARATION__STATEMENTS = BEHAVIOUR_DECLARATION__STATEMENTS;

    /**
     * The number of structural features of the '<em>Xper Declaration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int XPER_DECLARATION_FEATURE_COUNT = BEHAVIOUR_DECLARATION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ModelTypeImpl <em>Model Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ModelTypeImpl
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getModelType()
     * @generated
     */
    int MODEL_TYPE = 92;

    /**
     * The feature id for the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_TYPE__POSITION = COMPUTE_TYPE__POSITION;

    /**
     * The feature id for the '<em><b>Parameter Types</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_TYPE__PARAMETER_TYPES = COMPUTE_TYPE__PARAMETER_TYPES;

    /**
     * The feature id for the '<em><b>Exit Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_TYPE__EXIT_TYPE = COMPUTE_TYPE__EXIT_TYPE;

    /**
     * The number of structural features of the '<em>Model Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_TYPE_FEATURE_COUNT = COMPUTE_TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.ChannelOps <em>Channel Ops</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelOps
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChannelOps()
     * @generated
     */
    int CHANNEL_OPS = 93;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.UnaryOperators <em>Unary Operators</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.UnaryOperators
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnaryOperators()
     * @generated
     */
    int UNARY_OPERATORS = 94;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.BinaryOperators <em>Binary Operators</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryOperators
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBinaryOperators()
     * @generated
     */
    int BINARY_OPERATORS = 95;

    /**
     * The meta object id for the '{@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctions <em>Std Lib Functions</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.escet.chi.metamodel.chi.StdLibFunctions
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStdLibFunctions()
     * @generated
     */
    int STD_LIB_FUNCTIONS = 96;

    /**
     * The meta object id for the '<em>Identifier</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChiIdentifier()
     * @generated
     */
    int CHI_IDENTIFIER = 97;

    /**
     * The meta object id for the '<em>Real Number</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChiRealNumber()
     * @generated
     */
    int CHI_REAL_NUMBER = 98;

    /**
     * The meta object id for the '<em>Number</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChiNumber()
     * @generated
     */
    int CHI_NUMBER = 99;


    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.Type <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Type
     * @generated
     */
    EClass getType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.VoidType <em>Void Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Void Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.VoidType
     * @generated
     */
    EClass getVoidType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.BoolType <em>Bool Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bool Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BoolType
     * @generated
     */
    EClass getBoolType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.InstanceType <em>Instance Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Instance Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.InstanceType
     * @generated
     */
    EClass getInstanceType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.IntType <em>Int Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Int Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IntType
     * @generated
     */
    EClass getIntType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.StringType <em>String Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>String Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.StringType
     * @generated
     */
    EClass getStringType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.RealType <em>Real Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Real Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.RealType
     * @generated
     */
    EClass getRealType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.FileType <em>File Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>File Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FileType
     * @generated
     */
    EClass getFileType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.SetType <em>Set Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Set Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SetType
     * @generated
     */
    EClass getSetType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.SetType#getElementType <em>Element Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Element Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SetType#getElementType()
     * @see #getSetType()
     * @generated
     */
    EReference getSetType_ElementType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ListType <em>List Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>List Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ListType
     * @generated
     */
    EClass getListType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ListType#getElementType <em>Element Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Element Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ListType#getElementType()
     * @see #getListType()
     * @generated
     */
    EReference getListType_ElementType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ListType#getInitialLength <em>Initial Length</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Initial Length</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ListType#getInitialLength()
     * @see #getListType()
     * @generated
     */
    EReference getListType_InitialLength();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.DictType <em>Dict Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dict Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DictType
     * @generated
     */
    EClass getDictType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.DictType#getKeyType <em>Key Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Key Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DictType#getKeyType()
     * @see #getDictType()
     * @generated
     */
    EReference getDictType_KeyType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.DictType#getValueType <em>Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DictType#getValueType()
     * @see #getDictType()
     * @generated
     */
    EReference getDictType_ValueType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.MatrixType <em>Matrix Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Matrix Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixType
     * @generated
     */
    EClass getMatrixType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.MatrixType#getRowSize <em>Row Size</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Row Size</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixType#getRowSize()
     * @see #getMatrixType()
     * @generated
     */
    EReference getMatrixType_RowSize();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.MatrixType#getColumnSize <em>Column Size</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Column Size</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixType#getColumnSize()
     * @see #getMatrixType()
     * @generated
     */
    EReference getMatrixType_ColumnSize();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.TupleType <em>Tuple Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tuple Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleType
     * @generated
     */
    EClass getTupleType();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.TupleType#getFields <em>Fields</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Fields</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleType#getFields()
     * @see #getTupleType()
     * @generated
     */
    EReference getTupleType_Fields();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.TupleField <em>Tuple Field</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tuple Field</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleField
     * @generated
     */
    EClass getTupleField();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.TupleField#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleField#getType()
     * @see #getTupleField()
     * @generated
     */
    EReference getTupleField_Type();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.TupleField#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleField#getName()
     * @see #getTupleField()
     * @generated
     */
    EAttribute getTupleField_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.DistributionType <em>Distribution Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Distribution Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DistributionType
     * @generated
     */
    EClass getDistributionType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.DistributionType#getResultType <em>Result Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Result Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DistributionType#getResultType()
     * @see #getDistributionType()
     * @generated
     */
    EReference getDistributionType_ResultType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.EnumTypeReference <em>Enum Type Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Enum Type Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumTypeReference
     * @generated
     */
    EClass getEnumTypeReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.EnumTypeReference#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumTypeReference#getType()
     * @see #getEnumTypeReference()
     * @generated
     */
    EReference getEnumTypeReference_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ChannelType <em>Channel Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Channel Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelType
     * @generated
     */
    EClass getChannelType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ChannelType#getElementType <em>Element Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Element Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelType#getElementType()
     * @see #getChannelType()
     * @generated
     */
    EReference getChannelType_ElementType();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.ChannelType#getOps <em>Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Ops</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelType#getOps()
     * @see #getChannelType()
     * @generated
     */
    EAttribute getChannelType_Ops();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.FunctionType <em>Function Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionType
     * @generated
     */
    EClass getFunctionType();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.FunctionType#getResultType <em>Result Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Result Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionType#getResultType()
     * @see #getFunctionType()
     * @generated
     */
    EReference getFunctionType_ResultType();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.FunctionType#getParameterTypes <em>Parameter Types</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter Types</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionType#getParameterTypes()
     * @see #getFunctionType()
     * @generated
     */
    EReference getFunctionType_ParameterTypes();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.TypeReference <em>Type Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TypeReference
     * @generated
     */
    EClass getTypeReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.TypeReference#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TypeReference#getType()
     * @see #getTypeReference()
     * @generated
     */
    EReference getTypeReference_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Expression
     * @generated
     */
    EClass getExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.Expression#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Expression#getType()
     * @see #getExpression()
     * @generated
     */
    EReference getExpression_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.BoolLiteral <em>Bool Literal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bool Literal</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BoolLiteral
     * @generated
     */
    EClass getBoolLiteral();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.BoolLiteral#isValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BoolLiteral#isValue()
     * @see #getBoolLiteral()
     * @generated
     */
    EAttribute getBoolLiteral_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.IntNumber <em>Int Number</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Int Number</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IntNumber
     * @generated
     */
    EClass getIntNumber();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.IntNumber#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IntNumber#getValue()
     * @see #getIntNumber()
     * @generated
     */
    EAttribute getIntNumber_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.RealNumber <em>Real Number</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Real Number</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.RealNumber
     * @generated
     */
    EClass getRealNumber();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.RealNumber#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.RealNumber#getValue()
     * @see #getRealNumber()
     * @generated
     */
    EAttribute getRealNumber_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.StringLiteral <em>String Literal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>String Literal</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.StringLiteral
     * @generated
     */
    EClass getStringLiteral();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.StringLiteral#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.StringLiteral#getValue()
     * @see #getStringLiteral()
     * @generated
     */
    EAttribute getStringLiteral_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.TupleExpression <em>Tuple Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tuple Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleExpression
     * @generated
     */
    EClass getTupleExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.TupleExpression#getFields <em>Fields</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Fields</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TupleExpression#getFields()
     * @see #getTupleExpression()
     * @generated
     */
    EReference getTupleExpression_Fields();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ListExpression <em>List Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>List Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ListExpression
     * @generated
     */
    EClass getListExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.ListExpression#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ListExpression#getElements()
     * @see #getListExpression()
     * @generated
     */
    EReference getListExpression_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.SetExpression <em>Set Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Set Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SetExpression
     * @generated
     */
    EClass getSetExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.SetExpression#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SetExpression#getElements()
     * @see #getSetExpression()
     * @generated
     */
    EReference getSetExpression_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.MatrixExpression <em>Matrix Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Matrix Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixExpression
     * @generated
     */
    EClass getMatrixExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.MatrixExpression#getRows <em>Rows</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Rows</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixExpression#getRows()
     * @see #getMatrixExpression()
     * @generated
     */
    EReference getMatrixExpression_Rows();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.MatrixRow <em>Matrix Row</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Matrix Row</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixRow
     * @generated
     */
    EClass getMatrixRow();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.MatrixRow#getElements <em>Elements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Elements</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.MatrixRow#getElements()
     * @see #getMatrixRow()
     * @generated
     */
    EReference getMatrixRow_Elements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.DictionaryExpression <em>Dictionary Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dictionary Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DictionaryExpression
     * @generated
     */
    EClass getDictionaryExpression();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.DictionaryExpression#getPairs <em>Pairs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Pairs</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DictionaryExpression#getPairs()
     * @see #getDictionaryExpression()
     * @generated
     */
    EReference getDictionaryExpression_Pairs();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.DictionaryPair <em>Dictionary Pair</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dictionary Pair</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DictionaryPair
     * @generated
     */
    EClass getDictionaryPair();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.DictionaryPair#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Key</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DictionaryPair#getKey()
     * @see #getDictionaryPair()
     * @generated
     */
    EReference getDictionaryPair_Key();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.DictionaryPair#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DictionaryPair#getValue()
     * @see #getDictionaryPair()
     * @generated
     */
    EReference getDictionaryPair_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.VariableReference <em>Variable Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Variable Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableReference
     * @generated
     */
    EClass getVariableReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.VariableReference#getVariable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Variable</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableReference#getVariable()
     * @see #getVariableReference()
     * @generated
     */
    EReference getVariableReference_Variable();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ConstantReference <em>Constant Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Constant Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ConstantReference
     * @generated
     */
    EClass getConstantReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.ConstantReference#getConstant <em>Constant</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Constant</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ConstantReference#getConstant()
     * @see #getConstantReference()
     * @generated
     */
    EReference getConstantReference_Constant();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.TimeLiteral <em>Time Literal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Time Literal</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TimeLiteral
     * @generated
     */
    EClass getTimeLiteral();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.UnaryExpression <em>Unary Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unary Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.UnaryExpression
     * @generated
     */
    EClass getUnaryExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.UnaryExpression#getChild <em>Child</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Child</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.UnaryExpression#getChild()
     * @see #getUnaryExpression()
     * @generated
     */
    EReference getUnaryExpression_Child();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.UnaryExpression#getOp <em>Op</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Op</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.UnaryExpression#getOp()
     * @see #getUnaryExpression()
     * @generated
     */
    EAttribute getUnaryExpression_Op();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression <em>Binary Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Binary Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryExpression
     * @generated
     */
    EClass getBinaryExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getLeft <em>Left</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Left</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getLeft()
     * @see #getBinaryExpression()
     * @generated
     */
    EReference getBinaryExpression_Left();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getRight <em>Right</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Right</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getRight()
     * @see #getBinaryExpression()
     * @generated
     */
    EReference getBinaryExpression_Right();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getOp <em>Op</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Op</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryExpression#getOp()
     * @see #getBinaryExpression()
     * @generated
     */
    EAttribute getBinaryExpression_Op();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.CallExpression <em>Call Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Call Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CallExpression
     * @generated
     */
    EClass getCallExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.CallExpression#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Function</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CallExpression#getFunction()
     * @see #getCallExpression()
     * @generated
     */
    EReference getCallExpression_Function();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.CallExpression#getArguments <em>Arguments</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Arguments</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CallExpression#getArguments()
     * @see #getCallExpression()
     * @generated
     */
    EReference getCallExpression_Arguments();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.CallExpression#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Name</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CallExpression#getName()
     * @see #getCallExpression()
     * @generated
     */
    EReference getCallExpression_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.FunctionReference <em>Function Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionReference
     * @generated
     */
    EClass getFunctionReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.FunctionReference#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Function</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionReference#getFunction()
     * @see #getFunctionReference()
     * @generated
     */
    EReference getFunctionReference_Function();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.BaseFunctionReference <em>Base Function Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Base Function Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BaseFunctionReference
     * @generated
     */
    EClass getBaseFunctionReference();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference <em>Std Lib Function Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Std Lib Function Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference
     * @generated
     */
    EClass getStdLibFunctionReference();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Function</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference#getFunction()
     * @see #getStdLibFunctionReference()
     * @generated
     */
    EAttribute getStdLibFunctionReference_Function();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression <em>Slice Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Slice Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SliceExpression
     * @generated
     */
    EClass getSliceExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getStart <em>Start</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Start</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SliceExpression#getStart()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_Start();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getEnd <em>End</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>End</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SliceExpression#getEnd()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_End();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getStep <em>Step</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Step</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SliceExpression#getStep()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_Step();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.SliceExpression#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Source</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SliceExpression#getSource()
     * @see #getSliceExpression()
     * @generated
     */
    EReference getSliceExpression_Source();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.FieldReference <em>Field Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Field Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FieldReference
     * @generated
     */
    EClass getFieldReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.FieldReference#getField <em>Field</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Field</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FieldReference#getField()
     * @see #getFieldReference()
     * @generated
     */
    EReference getFieldReference_Field();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.Statement <em>Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Statement
     * @generated
     */
    EClass getStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.BreakStatement <em>Break Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Break Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BreakStatement
     * @generated
     */
    EClass getBreakStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ContinueStatement <em>Continue Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Continue Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ContinueStatement
     * @generated
     */
    EClass getContinueStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.PassStatement <em>Pass Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Pass Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.PassStatement
     * @generated
     */
    EClass getPassStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ExitStatement <em>Exit Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Exit Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ExitStatement
     * @generated
     */
    EClass getExitStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ExitStatement#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ExitStatement#getValue()
     * @see #getExitStatement()
     * @generated
     */
    EReference getExitStatement_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ReturnStatement <em>Return Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Return Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ReturnStatement
     * @generated
     */
    EClass getReturnStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ReturnStatement#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ReturnStatement#getValue()
     * @see #getReturnStatement()
     * @generated
     */
    EReference getReturnStatement_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.DelayStatement <em>Delay Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Delay Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DelayStatement
     * @generated
     */
    EClass getDelayStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.DelayStatement#getLength <em>Length</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Length</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.DelayStatement#getLength()
     * @see #getDelayStatement()
     * @generated
     */
    EReference getDelayStatement_Length();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.WhileStatement <em>While Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>While Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.WhileStatement
     * @generated
     */
    EClass getWhileStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.WhileStatement#getCondition <em>Condition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Condition</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.WhileStatement#getCondition()
     * @see #getWhileStatement()
     * @generated
     */
    EReference getWhileStatement_Condition();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.WhileStatement#getBody <em>Body</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Body</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.WhileStatement#getBody()
     * @see #getWhileStatement()
     * @generated
     */
    EReference getWhileStatement_Body();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.IfStatement <em>If Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>If Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IfStatement
     * @generated
     */
    EClass getIfStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.IfStatement#getCases <em>Cases</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Cases</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IfStatement#getCases()
     * @see #getIfStatement()
     * @generated
     */
    EReference getIfStatement_Cases();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.IfCase <em>If Case</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>If Case</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IfCase
     * @generated
     */
    EClass getIfCase();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.IfCase#getCondition <em>Condition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Condition</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IfCase#getCondition()
     * @see #getIfCase()
     * @generated
     */
    EReference getIfCase_Condition();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.IfCase#getBody <em>Body</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Body</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IfCase#getBody()
     * @see #getIfCase()
     * @generated
     */
    EReference getIfCase_Body();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.WriteStatement <em>Write Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Write Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.WriteStatement
     * @generated
     */
    EClass getWriteStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.WriteStatement#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Values</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.WriteStatement#getValues()
     * @see #getWriteStatement()
     * @generated
     */
    EReference getWriteStatement_Values();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.WriteStatement#isAddNewline <em>Add Newline</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Add Newline</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.WriteStatement#isAddNewline()
     * @see #getWriteStatement()
     * @generated
     */
    EAttribute getWriteStatement_AddNewline();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.AssignmentStatement <em>Assignment Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assignment Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.AssignmentStatement
     * @generated
     */
    EClass getAssignmentStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.AssignmentStatement#getLhs <em>Lhs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Lhs</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.AssignmentStatement#getLhs()
     * @see #getAssignmentStatement()
     * @generated
     */
    EReference getAssignmentStatement_Lhs();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.AssignmentStatement#getRhs <em>Rhs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Rhs</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.AssignmentStatement#getRhs()
     * @see #getAssignmentStatement()
     * @generated
     */
    EReference getAssignmentStatement_Rhs();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.CommunicationStatement <em>Communication Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Communication Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CommunicationStatement
     * @generated
     */
    EClass getCommunicationStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.CommunicationStatement#getChannel <em>Channel</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Channel</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CommunicationStatement#getChannel()
     * @see #getCommunicationStatement()
     * @generated
     */
    EReference getCommunicationStatement_Channel();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.CommunicationStatement#getData <em>Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CommunicationStatement#getData()
     * @see #getCommunicationStatement()
     * @generated
     */
    EReference getCommunicationStatement_Data();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ForStatement <em>For Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>For Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ForStatement
     * @generated
     */
    EClass getForStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.ForStatement#getBody <em>Body</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Body</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ForStatement#getBody()
     * @see #getForStatement()
     * @generated
     */
    EReference getForStatement_Body();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.ForStatement#getUnwinds <em>Unwinds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Unwinds</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ForStatement#getUnwinds()
     * @see #getForStatement()
     * @generated
     */
    EReference getForStatement_Unwinds();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.CreateCase <em>Create Case</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Create Case</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CreateCase
     * @generated
     */
    EClass getCreateCase();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.RunStatement <em>Run Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Run Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.RunStatement
     * @generated
     */
    EClass getRunStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.RunStatement#getCases <em>Cases</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Cases</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.RunStatement#getCases()
     * @see #getRunStatement()
     * @generated
     */
    EReference getRunStatement_Cases();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.RunStatement#isStartOnly <em>Start Only</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Only</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.RunStatement#isStartOnly()
     * @see #getRunStatement()
     * @generated
     */
    EAttribute getRunStatement_StartOnly();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.SelectStatement <em>Select Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Select Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SelectStatement
     * @generated
     */
    EClass getSelectStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.SelectStatement#getCases <em>Cases</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Cases</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SelectStatement#getCases()
     * @see #getSelectStatement()
     * @generated
     */
    EReference getSelectStatement_Cases();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.SelectCase <em>Select Case</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Select Case</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SelectCase
     * @generated
     */
    EClass getSelectCase();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.SelectCase#getBody <em>Body</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Body</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SelectCase#getBody()
     * @see #getSelectCase()
     * @generated
     */
    EReference getSelectCase_Body();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.SelectCase#getGuard <em>Guard</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Guard</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SelectCase#getGuard()
     * @see #getSelectCase()
     * @generated
     */
    EReference getSelectCase_Guard();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase <em>Iterated Create Case</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Iterated Create Case</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase
     * @generated
     */
    EClass getIteratedCreateCase();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase#getUnwinds <em>Unwinds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Unwinds</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase#getUnwinds()
     * @see #getIteratedCreateCase()
     * @generated
     */
    EReference getIteratedCreateCase_Unwinds();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase#getInstances <em>Instances</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Instances</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IteratedCreateCase#getInstances()
     * @see #getIteratedCreateCase()
     * @generated
     */
    EReference getIteratedCreateCase_Instances();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase <em>Iterated Select Case</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Iterated Select Case</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase
     * @generated
     */
    EClass getIteratedSelectCase();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase#getUnwinds <em>Unwinds</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Unwinds</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.IteratedSelectCase#getUnwinds()
     * @see #getIteratedSelectCase()
     * @generated
     */
    EReference getIteratedSelectCase_Unwinds();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.Specification <em>Specification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Specification</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Specification
     * @generated
     */
    EClass getSpecification();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.Specification#getDeclarations <em>Declarations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Declarations</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Specification#getDeclarations()
     * @see #getSpecification()
     * @generated
     */
    EReference getSpecification_Declarations();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.Declaration <em>Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Declaration
     * @generated
     */
    EClass getDeclaration();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.Declaration#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Declaration#getName()
     * @see #getDeclaration()
     * @generated
     */
    EAttribute getDeclaration_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.TypeDeclaration <em>Type Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Type Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TypeDeclaration
     * @generated
     */
    EClass getTypeDeclaration();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.TypeDeclaration#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TypeDeclaration#getType()
     * @see #getTypeDeclaration()
     * @generated
     */
    EReference getTypeDeclaration_Type();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration <em>Constant Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Constant Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration
     * @generated
     */
    EClass getConstantDeclaration();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration#getType()
     * @see #getConstantDeclaration()
     * @generated
     */
    EReference getConstantDeclaration_Type();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration#getValue()
     * @see #getConstantDeclaration()
     * @generated
     */
    EReference getConstantDeclaration_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration <em>Process Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration
     * @generated
     */
    EClass getProcessDeclaration();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration <em>Function Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration
     * @generated
     */
    EClass getFunctionDeclaration();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration#getReturnType <em>Return Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Return Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration#getReturnType()
     * @see #getFunctionDeclaration()
     * @generated
     */
    EReference getFunctionDeclaration_ReturnType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ModelDeclaration <em>Model Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ModelDeclaration
     * @generated
     */
    EClass getModelDeclaration();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration <em>Variable Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Variable Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableDeclaration
     * @generated
     */
    EClass getVariableDeclaration();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getInitialValue <em>Initial Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Initial Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getInitialValue()
     * @see #getVariableDeclaration()
     * @generated
     */
    EReference getVariableDeclaration_InitialValue();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getType()
     * @see #getVariableDeclaration()
     * @generated
     */
    EReference getVariableDeclaration_Type();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#isParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Parameter</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#isParameter()
     * @see #getVariableDeclaration()
     * @generated
     */
    EAttribute getVariableDeclaration_Parameter();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.VariableDeclaration#getName()
     * @see #getVariableDeclaration()
     * @generated
     */
    EAttribute getVariableDeclaration_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration <em>Behaviour Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Behaviour Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration
     * @generated
     */
    EClass getBehaviourDeclaration();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration#getVariables <em>Variables</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Variables</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration#getVariables()
     * @see #getBehaviourDeclaration()
     * @generated
     */
    EReference getBehaviourDeclaration_Variables();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration#getStatements <em>Statements</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Statements</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration#getStatements()
     * @see #getBehaviourDeclaration()
     * @generated
     */
    EReference getBehaviourDeclaration_Statements();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ReceiveStatement <em>Receive Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Receive Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ReceiveStatement
     * @generated
     */
    EClass getReceiveStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.SendStatement <em>Send Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Send Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.SendStatement
     * @generated
     */
    EClass getSendStatement();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.EnumValueReference <em>Enum Value Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Enum Value Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumValueReference
     * @generated
     */
    EClass getEnumValueReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.EnumValueReference#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumValueReference#getValue()
     * @see #getEnumValueReference()
     * @generated
     */
    EReference getEnumValueReference_Value();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ReadCallExpression <em>Read Call Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Read Call Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ReadCallExpression
     * @generated
     */
    EClass getReadCallExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ReadCallExpression#getFile <em>File</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>File</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ReadCallExpression#getFile()
     * @see #getReadCallExpression()
     * @generated
     */
    EReference getReadCallExpression_File();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ReadCallExpression#getLoadType <em>Load Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Load Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ReadCallExpression#getLoadType()
     * @see #getReadCallExpression()
     * @generated
     */
    EReference getReadCallExpression_LoadType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.Unwind <em>Unwind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unwind</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Unwind
     * @generated
     */
    EClass getUnwind();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.Unwind#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Source</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Unwind#getSource()
     * @see #getUnwind()
     * @generated
     */
    EReference getUnwind_Source();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.Unwind#getVariables <em>Variables</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Variables</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.Unwind#getVariables()
     * @see #getUnwind()
     * @generated
     */
    EReference getUnwind_Variables();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ProcessInstance <em>Process Instance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Instance</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessInstance
     * @generated
     */
    EClass getProcessInstance();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ProcessInstance#getCall <em>Call</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Call</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessInstance#getCall()
     * @see #getProcessInstance()
     * @generated
     */
    EReference getProcessInstance_Call();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ProcessInstance#getVar <em>Var</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Var</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessInstance#getVar()
     * @see #getProcessInstance()
     * @generated
     */
    EReference getProcessInstance_Var();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ProcessType <em>Process Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessType
     * @generated
     */
    EClass getProcessType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ProcessReference <em>Process Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessReference
     * @generated
     */
    EClass getProcessReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.ProcessReference#getProcess <em>Process</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Process</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ProcessReference#getProcess()
     * @see #getProcessReference()
     * @generated
     */
    EReference getProcessReference_Process();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.UnresolvedReference <em>Unresolved Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unresolved Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.UnresolvedReference
     * @generated
     */
    EClass getUnresolvedReference();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.UnresolvedReference#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.UnresolvedReference#getName()
     * @see #getUnresolvedReference()
     * @generated
     */
    EAttribute getUnresolvedReference_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.UnresolvedType <em>Unresolved Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unresolved Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.UnresolvedType
     * @generated
     */
    EClass getUnresolvedType();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.UnresolvedType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.UnresolvedType#getName()
     * @see #getUnresolvedType()
     * @generated
     */
    EAttribute getUnresolvedType_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.TimerType <em>Timer Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Timer Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.TimerType
     * @generated
     */
    EClass getTimerType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.EnumDeclaration <em>Enum Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Enum Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumDeclaration
     * @generated
     */
    EClass getEnumDeclaration();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.EnumDeclaration#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Values</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumDeclaration#getValues()
     * @see #getEnumDeclaration()
     * @generated
     */
    EReference getEnumDeclaration_Values();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.EnumValue <em>Enum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Enum Value</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumValue
     * @generated
     */
    EClass getEnumValue();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.escet.chi.metamodel.chi.EnumValue#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.EnumValue#getName()
     * @see #getEnumValue()
     * @generated
     */
    EAttribute getEnumValue_Name();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ChannelExpression <em>Channel Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Channel Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelExpression
     * @generated
     */
    EClass getChannelExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ChannelExpression#getElementType <em>Element Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Element Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelExpression#getElementType()
     * @see #getChannelExpression()
     * @generated
     */
    EReference getChannelExpression_ElementType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.CastExpression <em>Cast Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Cast Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CastExpression
     * @generated
     */
    EClass getCastExpression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.CastExpression#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CastExpression#getExpression()
     * @see #getCastExpression()
     * @generated
     */
    EReference getCastExpression_Expression();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.CastExpression#getCastType <em>Cast Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Cast Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CastExpression#getCastType()
     * @see #getCastExpression()
     * @generated
     */
    EReference getCastExpression_CastType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.CloseStatement <em>Close Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Close Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CloseStatement
     * @generated
     */
    EClass getCloseStatement();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.CloseStatement#getHandle <em>Handle</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Handle</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.CloseStatement#getHandle()
     * @see #getCloseStatement()
     * @generated
     */
    EReference getCloseStatement_Handle();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.FinishStatement <em>Finish Statement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Finish Statement</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FinishStatement
     * @generated
     */
    EClass getFinishStatement();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.FinishStatement#getInstances <em>Instances</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Instances</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.FinishStatement#getInstances()
     * @see #getFinishStatement()
     * @generated
     */
    EReference getFinishStatement_Instances();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ModelReference <em>Model Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Reference</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ModelReference
     * @generated
     */
    EClass getModelReference();

    /**
     * Returns the meta object for the reference '{@link org.eclipse.escet.chi.metamodel.chi.ModelReference#getModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Model</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ModelReference#getModel()
     * @see #getModelReference()
     * @generated
     */
    EReference getModelReference_Model();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.XperDeclaration <em>Xper Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Xper Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.XperDeclaration
     * @generated
     */
    EClass getXperDeclaration();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ComputeDeclaration <em>Compute Declaration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Compute Declaration</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ComputeDeclaration
     * @generated
     */
    EClass getComputeDeclaration();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ComputeDeclaration#getReturnType <em>Return Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Return Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ComputeDeclaration#getReturnType()
     * @see #getComputeDeclaration()
     * @generated
     */
    EReference getComputeDeclaration_ReturnType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ComputeType <em>Compute Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Compute Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ComputeType
     * @generated
     */
    EClass getComputeType();

    /**
     * Returns the meta object for the containment reference list '{@link org.eclipse.escet.chi.metamodel.chi.ComputeType#getParameterTypes <em>Parameter Types</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter Types</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ComputeType#getParameterTypes()
     * @see #getComputeType()
     * @generated
     */
    EReference getComputeType_ParameterTypes();

    /**
     * Returns the meta object for the containment reference '{@link org.eclipse.escet.chi.metamodel.chi.ComputeType#getExitType <em>Exit Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Exit Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ComputeType#getExitType()
     * @see #getComputeType()
     * @generated
     */
    EReference getComputeType_ExitType();

    /**
     * Returns the meta object for class '{@link org.eclipse.escet.chi.metamodel.chi.ModelType <em>Model Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Type</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ModelType
     * @generated
     */
    EClass getModelType();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.chi.metamodel.chi.ChannelOps <em>Channel Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Channel Ops</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.ChannelOps
     * @generated
     */
    EEnum getChannelOps();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.chi.metamodel.chi.UnaryOperators <em>Unary Operators</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Unary Operators</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.UnaryOperators
     * @generated
     */
    EEnum getUnaryOperators();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.chi.metamodel.chi.BinaryOperators <em>Binary Operators</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Binary Operators</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.BinaryOperators
     * @generated
     */
    EEnum getBinaryOperators();

    /**
     * Returns the meta object for enum '{@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctions <em>Std Lib Functions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Std Lib Functions</em>'.
     * @see org.eclipse.escet.chi.metamodel.chi.StdLibFunctions
     * @generated
     */
    EEnum getStdLibFunctions();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Identifier</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     * @generated
     */
    EDataType getChiIdentifier();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Real Number</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Real Number</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='ChiRealNumber' baseType='http://www.eclipse.org/emf/2002/Ecore#EString' pattern='(0|([1-9][0-9]*))((\\.[0-9]+)|((\\.[0-9]+)?[eE][\\-\\+]?[0-9]+))'"
     * @generated
     */
    EDataType getChiRealNumber();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Number</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Number</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='ChiNumber' baseType='http://www.eclipse.org/emf/2002/Ecore#EString' pattern='0|([1-9][0-9]*)'"
     * @generated
     */
    EDataType getChiNumber();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ChiFactory getChiFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals
    {
        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TypeImpl <em>Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.TypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getType()
         * @generated
         */
        EClass TYPE = eINSTANCE.getType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.VoidTypeImpl <em>Void Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.VoidTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getVoidType()
         * @generated
         */
        EClass VOID_TYPE = eINSTANCE.getVoidType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BoolTypeImpl <em>Bool Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.BoolTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBoolType()
         * @generated
         */
        EClass BOOL_TYPE = eINSTANCE.getBoolType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.InstanceTypeImpl <em>Instance Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.InstanceTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getInstanceType()
         * @generated
         */
        EClass INSTANCE_TYPE = eINSTANCE.getInstanceType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IntTypeImpl <em>Int Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.IntTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIntType()
         * @generated
         */
        EClass INT_TYPE = eINSTANCE.getIntType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.StringTypeImpl <em>String Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.StringTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStringType()
         * @generated
         */
        EClass STRING_TYPE = eINSTANCE.getStringType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.RealTypeImpl <em>Real Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.RealTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getRealType()
         * @generated
         */
        EClass REAL_TYPE = eINSTANCE.getRealType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FileTypeImpl <em>File Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.FileTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFileType()
         * @generated
         */
        EClass FILE_TYPE = eINSTANCE.getFileType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SetTypeImpl <em>Set Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.SetTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSetType()
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
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ListTypeImpl <em>List Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ListTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getListType()
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
         * The meta object literal for the '<em><b>Initial Length</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LIST_TYPE__INITIAL_LENGTH = eINSTANCE.getListType_InitialLength();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DictTypeImpl <em>Dict Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.DictTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDictType()
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
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.MatrixTypeImpl <em>Matrix Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.MatrixTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getMatrixType()
         * @generated
         */
        EClass MATRIX_TYPE = eINSTANCE.getMatrixType();

        /**
         * The meta object literal for the '<em><b>Row Size</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATRIX_TYPE__ROW_SIZE = eINSTANCE.getMatrixType_RowSize();

        /**
         * The meta object literal for the '<em><b>Column Size</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATRIX_TYPE__COLUMN_SIZE = eINSTANCE.getMatrixType_ColumnSize();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TupleTypeImpl <em>Tuple Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.TupleTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTupleType()
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
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TupleFieldImpl <em>Tuple Field</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.TupleFieldImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTupleField()
         * @generated
         */
        EClass TUPLE_FIELD = eINSTANCE.getTupleField();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TUPLE_FIELD__TYPE = eINSTANCE.getTupleField_Type();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TUPLE_FIELD__NAME = eINSTANCE.getTupleField_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DistributionTypeImpl <em>Distribution Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.DistributionTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDistributionType()
         * @generated
         */
        EClass DISTRIBUTION_TYPE = eINSTANCE.getDistributionType();

        /**
         * The meta object literal for the '<em><b>Result Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISTRIBUTION_TYPE__RESULT_TYPE = eINSTANCE.getDistributionType_ResultType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.EnumTypeReferenceImpl <em>Enum Type Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.EnumTypeReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getEnumTypeReference()
         * @generated
         */
        EClass ENUM_TYPE_REFERENCE = eINSTANCE.getEnumTypeReference();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ENUM_TYPE_REFERENCE__TYPE = eINSTANCE.getEnumTypeReference_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ChannelTypeImpl <em>Channel Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChannelTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChannelType()
         * @generated
         */
        EClass CHANNEL_TYPE = eINSTANCE.getChannelType();

        /**
         * The meta object literal for the '<em><b>Element Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHANNEL_TYPE__ELEMENT_TYPE = eINSTANCE.getChannelType_ElementType();

        /**
         * The meta object literal for the '<em><b>Ops</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CHANNEL_TYPE__OPS = eINSTANCE.getChannelType_Ops();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionTypeImpl <em>Function Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.FunctionTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFunctionType()
         * @generated
         */
        EClass FUNCTION_TYPE = eINSTANCE.getFunctionType();

        /**
         * The meta object literal for the '<em><b>Result Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_TYPE__RESULT_TYPE = eINSTANCE.getFunctionType_ResultType();

        /**
         * The meta object literal for the '<em><b>Parameter Types</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_TYPE__PARAMETER_TYPES = eINSTANCE.getFunctionType_ParameterTypes();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TypeReferenceImpl <em>Type Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.TypeReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTypeReference()
         * @generated
         */
        EClass TYPE_REFERENCE = eINSTANCE.getTypeReference();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TYPE_REFERENCE__TYPE = eINSTANCE.getTypeReference_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ExpressionImpl <em>Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getExpression()
         * @generated
         */
        EClass EXPRESSION = eINSTANCE.getExpression();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXPRESSION__TYPE = eINSTANCE.getExpression_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BoolLiteralImpl <em>Bool Literal</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.BoolLiteralImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBoolLiteral()
         * @generated
         */
        EClass BOOL_LITERAL = eINSTANCE.getBoolLiteral();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOOL_LITERAL__VALUE = eINSTANCE.getBoolLiteral_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IntNumberImpl <em>Int Number</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.IntNumberImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIntNumber()
         * @generated
         */
        EClass INT_NUMBER = eINSTANCE.getIntNumber();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INT_NUMBER__VALUE = eINSTANCE.getIntNumber_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.RealNumberImpl <em>Real Number</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.RealNumberImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getRealNumber()
         * @generated
         */
        EClass REAL_NUMBER = eINSTANCE.getRealNumber();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REAL_NUMBER__VALUE = eINSTANCE.getRealNumber_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.StringLiteralImpl <em>String Literal</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.StringLiteralImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStringLiteral()
         * @generated
         */
        EClass STRING_LITERAL = eINSTANCE.getStringLiteral();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STRING_LITERAL__VALUE = eINSTANCE.getStringLiteral_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TupleExpressionImpl <em>Tuple Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.TupleExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTupleExpression()
         * @generated
         */
        EClass TUPLE_EXPRESSION = eINSTANCE.getTupleExpression();

        /**
         * The meta object literal for the '<em><b>Fields</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TUPLE_EXPRESSION__FIELDS = eINSTANCE.getTupleExpression_Fields();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ListExpressionImpl <em>List Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ListExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getListExpression()
         * @generated
         */
        EClass LIST_EXPRESSION = eINSTANCE.getListExpression();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LIST_EXPRESSION__ELEMENTS = eINSTANCE.getListExpression_Elements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SetExpressionImpl <em>Set Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.SetExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSetExpression()
         * @generated
         */
        EClass SET_EXPRESSION = eINSTANCE.getSetExpression();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SET_EXPRESSION__ELEMENTS = eINSTANCE.getSetExpression_Elements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.MatrixExpressionImpl <em>Matrix Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.MatrixExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getMatrixExpression()
         * @generated
         */
        EClass MATRIX_EXPRESSION = eINSTANCE.getMatrixExpression();

        /**
         * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATRIX_EXPRESSION__ROWS = eINSTANCE.getMatrixExpression_Rows();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.MatrixRowImpl <em>Matrix Row</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.MatrixRowImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getMatrixRow()
         * @generated
         */
        EClass MATRIX_ROW = eINSTANCE.getMatrixRow();

        /**
         * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MATRIX_ROW__ELEMENTS = eINSTANCE.getMatrixRow_Elements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DictionaryExpressionImpl <em>Dictionary Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.DictionaryExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDictionaryExpression()
         * @generated
         */
        EClass DICTIONARY_EXPRESSION = eINSTANCE.getDictionaryExpression();

        /**
         * The meta object literal for the '<em><b>Pairs</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DICTIONARY_EXPRESSION__PAIRS = eINSTANCE.getDictionaryExpression_Pairs();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DictionaryPairImpl <em>Dictionary Pair</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.DictionaryPairImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDictionaryPair()
         * @generated
         */
        EClass DICTIONARY_PAIR = eINSTANCE.getDictionaryPair();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DICTIONARY_PAIR__KEY = eINSTANCE.getDictionaryPair_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DICTIONARY_PAIR__VALUE = eINSTANCE.getDictionaryPair_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.VariableReferenceImpl <em>Variable Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.VariableReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getVariableReference()
         * @generated
         */
        EClass VARIABLE_REFERENCE = eINSTANCE.getVariableReference();

        /**
         * The meta object literal for the '<em><b>Variable</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference VARIABLE_REFERENCE__VARIABLE = eINSTANCE.getVariableReference_Variable();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ConstantReferenceImpl <em>Constant Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ConstantReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getConstantReference()
         * @generated
         */
        EClass CONSTANT_REFERENCE = eINSTANCE.getConstantReference();

        /**
         * The meta object literal for the '<em><b>Constant</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONSTANT_REFERENCE__CONSTANT = eINSTANCE.getConstantReference_Constant();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TimeLiteralImpl <em>Time Literal</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.TimeLiteralImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTimeLiteral()
         * @generated
         */
        EClass TIME_LITERAL = eINSTANCE.getTimeLiteral();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.UnaryExpressionImpl <em>Unary Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.UnaryExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnaryExpression()
         * @generated
         */
        EClass UNARY_EXPRESSION = eINSTANCE.getUnaryExpression();

        /**
         * The meta object literal for the '<em><b>Child</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_EXPRESSION__CHILD = eINSTANCE.getUnaryExpression_Child();

        /**
         * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNARY_EXPRESSION__OP = eINSTANCE.getUnaryExpression_Op();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BinaryExpressionImpl <em>Binary Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.BinaryExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBinaryExpression()
         * @generated
         */
        EClass BINARY_EXPRESSION = eINSTANCE.getBinaryExpression();

        /**
         * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_EXPRESSION__LEFT = eINSTANCE.getBinaryExpression_Left();

        /**
         * The meta object literal for the '<em><b>Right</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_EXPRESSION__RIGHT = eINSTANCE.getBinaryExpression_Right();

        /**
         * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_EXPRESSION__OP = eINSTANCE.getBinaryExpression_Op();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CallExpressionImpl <em>Call Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.CallExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCallExpression()
         * @generated
         */
        EClass CALL_EXPRESSION = eINSTANCE.getCallExpression();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CALL_EXPRESSION__FUNCTION = eINSTANCE.getCallExpression_Function();

        /**
         * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CALL_EXPRESSION__ARGUMENTS = eINSTANCE.getCallExpression_Arguments();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CALL_EXPRESSION__NAME = eINSTANCE.getCallExpression_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionReferenceImpl <em>Function Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.FunctionReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFunctionReference()
         * @generated
         */
        EClass FUNCTION_REFERENCE = eINSTANCE.getFunctionReference();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_REFERENCE__FUNCTION = eINSTANCE.getFunctionReference_Function();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BaseFunctionReferenceImpl <em>Base Function Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.BaseFunctionReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBaseFunctionReference()
         * @generated
         */
        EClass BASE_FUNCTION_REFERENCE = eINSTANCE.getBaseFunctionReference();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.StdLibFunctionReferenceImpl <em>Std Lib Function Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.StdLibFunctionReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStdLibFunctionReference()
         * @generated
         */
        EClass STD_LIB_FUNCTION_REFERENCE = eINSTANCE.getStdLibFunctionReference();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STD_LIB_FUNCTION_REFERENCE__FUNCTION = eINSTANCE.getStdLibFunctionReference_Function();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SliceExpressionImpl <em>Slice Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.SliceExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSliceExpression()
         * @generated
         */
        EClass SLICE_EXPRESSION = eINSTANCE.getSliceExpression();

        /**
         * The meta object literal for the '<em><b>Start</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SLICE_EXPRESSION__START = eINSTANCE.getSliceExpression_Start();

        /**
         * The meta object literal for the '<em><b>End</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SLICE_EXPRESSION__END = eINSTANCE.getSliceExpression_End();

        /**
         * The meta object literal for the '<em><b>Step</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SLICE_EXPRESSION__STEP = eINSTANCE.getSliceExpression_Step();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SLICE_EXPRESSION__SOURCE = eINSTANCE.getSliceExpression_Source();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FieldReferenceImpl <em>Field Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.FieldReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFieldReference()
         * @generated
         */
        EClass FIELD_REFERENCE = eINSTANCE.getFieldReference();

        /**
         * The meta object literal for the '<em><b>Field</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FIELD_REFERENCE__FIELD = eINSTANCE.getFieldReference_Field();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.StatementImpl <em>Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.StatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStatement()
         * @generated
         */
        EClass STATEMENT = eINSTANCE.getStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BreakStatementImpl <em>Break Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.BreakStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBreakStatement()
         * @generated
         */
        EClass BREAK_STATEMENT = eINSTANCE.getBreakStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ContinueStatementImpl <em>Continue Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ContinueStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getContinueStatement()
         * @generated
         */
        EClass CONTINUE_STATEMENT = eINSTANCE.getContinueStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.PassStatementImpl <em>Pass Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.PassStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getPassStatement()
         * @generated
         */
        EClass PASS_STATEMENT = eINSTANCE.getPassStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ExitStatementImpl <em>Exit Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ExitStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getExitStatement()
         * @generated
         */
        EClass EXIT_STATEMENT = eINSTANCE.getExitStatement();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXIT_STATEMENT__VALUE = eINSTANCE.getExitStatement_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ReturnStatementImpl <em>Return Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ReturnStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getReturnStatement()
         * @generated
         */
        EClass RETURN_STATEMENT = eINSTANCE.getReturnStatement();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RETURN_STATEMENT__VALUE = eINSTANCE.getReturnStatement_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DelayStatementImpl <em>Delay Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.DelayStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDelayStatement()
         * @generated
         */
        EClass DELAY_STATEMENT = eINSTANCE.getDelayStatement();

        /**
         * The meta object literal for the '<em><b>Length</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DELAY_STATEMENT__LENGTH = eINSTANCE.getDelayStatement_Length();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.WhileStatementImpl <em>While Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.WhileStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getWhileStatement()
         * @generated
         */
        EClass WHILE_STATEMENT = eINSTANCE.getWhileStatement();

        /**
         * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WHILE_STATEMENT__CONDITION = eINSTANCE.getWhileStatement_Condition();

        /**
         * The meta object literal for the '<em><b>Body</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WHILE_STATEMENT__BODY = eINSTANCE.getWhileStatement_Body();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IfStatementImpl <em>If Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.IfStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIfStatement()
         * @generated
         */
        EClass IF_STATEMENT = eINSTANCE.getIfStatement();

        /**
         * The meta object literal for the '<em><b>Cases</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_STATEMENT__CASES = eINSTANCE.getIfStatement_Cases();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IfCaseImpl <em>If Case</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.IfCaseImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIfCase()
         * @generated
         */
        EClass IF_CASE = eINSTANCE.getIfCase();

        /**
         * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_CASE__CONDITION = eINSTANCE.getIfCase_Condition();

        /**
         * The meta object literal for the '<em><b>Body</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IF_CASE__BODY = eINSTANCE.getIfCase_Body();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.WriteStatementImpl <em>Write Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.WriteStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getWriteStatement()
         * @generated
         */
        EClass WRITE_STATEMENT = eINSTANCE.getWriteStatement();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WRITE_STATEMENT__VALUES = eINSTANCE.getWriteStatement_Values();

        /**
         * The meta object literal for the '<em><b>Add Newline</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WRITE_STATEMENT__ADD_NEWLINE = eINSTANCE.getWriteStatement_AddNewline();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.AssignmentStatementImpl <em>Assignment Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.AssignmentStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getAssignmentStatement()
         * @generated
         */
        EClass ASSIGNMENT_STATEMENT = eINSTANCE.getAssignmentStatement();

        /**
         * The meta object literal for the '<em><b>Lhs</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSIGNMENT_STATEMENT__LHS = eINSTANCE.getAssignmentStatement_Lhs();

        /**
         * The meta object literal for the '<em><b>Rhs</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSIGNMENT_STATEMENT__RHS = eINSTANCE.getAssignmentStatement_Rhs();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CommunicationStatementImpl <em>Communication Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.CommunicationStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCommunicationStatement()
         * @generated
         */
        EClass COMMUNICATION_STATEMENT = eINSTANCE.getCommunicationStatement();

        /**
         * The meta object literal for the '<em><b>Channel</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMMUNICATION_STATEMENT__CHANNEL = eINSTANCE.getCommunicationStatement_Channel();

        /**
         * The meta object literal for the '<em><b>Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMMUNICATION_STATEMENT__DATA = eINSTANCE.getCommunicationStatement_Data();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ForStatementImpl <em>For Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ForStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getForStatement()
         * @generated
         */
        EClass FOR_STATEMENT = eINSTANCE.getForStatement();

        /**
         * The meta object literal for the '<em><b>Body</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FOR_STATEMENT__BODY = eINSTANCE.getForStatement_Body();

        /**
         * The meta object literal for the '<em><b>Unwinds</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FOR_STATEMENT__UNWINDS = eINSTANCE.getForStatement_Unwinds();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CreateCaseImpl <em>Create Case</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.CreateCaseImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCreateCase()
         * @generated
         */
        EClass CREATE_CASE = eINSTANCE.getCreateCase();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.RunStatementImpl <em>Run Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.RunStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getRunStatement()
         * @generated
         */
        EClass RUN_STATEMENT = eINSTANCE.getRunStatement();

        /**
         * The meta object literal for the '<em><b>Cases</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RUN_STATEMENT__CASES = eINSTANCE.getRunStatement_Cases();

        /**
         * The meta object literal for the '<em><b>Start Only</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RUN_STATEMENT__START_ONLY = eINSTANCE.getRunStatement_StartOnly();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SelectStatementImpl <em>Select Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.SelectStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSelectStatement()
         * @generated
         */
        EClass SELECT_STATEMENT = eINSTANCE.getSelectStatement();

        /**
         * The meta object literal for the '<em><b>Cases</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SELECT_STATEMENT__CASES = eINSTANCE.getSelectStatement_Cases();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SelectCaseImpl <em>Select Case</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.SelectCaseImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSelectCase()
         * @generated
         */
        EClass SELECT_CASE = eINSTANCE.getSelectCase();

        /**
         * The meta object literal for the '<em><b>Body</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SELECT_CASE__BODY = eINSTANCE.getSelectCase_Body();

        /**
         * The meta object literal for the '<em><b>Guard</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SELECT_CASE__GUARD = eINSTANCE.getSelectCase_Guard();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IteratedCreateCaseImpl <em>Iterated Create Case</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.IteratedCreateCaseImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIteratedCreateCase()
         * @generated
         */
        EClass ITERATED_CREATE_CASE = eINSTANCE.getIteratedCreateCase();

        /**
         * The meta object literal for the '<em><b>Unwinds</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ITERATED_CREATE_CASE__UNWINDS = eINSTANCE.getIteratedCreateCase_Unwinds();

        /**
         * The meta object literal for the '<em><b>Instances</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ITERATED_CREATE_CASE__INSTANCES = eINSTANCE.getIteratedCreateCase_Instances();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.IteratedSelectCaseImpl <em>Iterated Select Case</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.IteratedSelectCaseImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getIteratedSelectCase()
         * @generated
         */
        EClass ITERATED_SELECT_CASE = eINSTANCE.getIteratedSelectCase();

        /**
         * The meta object literal for the '<em><b>Unwinds</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ITERATED_SELECT_CASE__UNWINDS = eINSTANCE.getIteratedSelectCase_Unwinds();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SpecificationImpl <em>Specification</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.SpecificationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSpecification()
         * @generated
         */
        EClass SPECIFICATION = eINSTANCE.getSpecification();

        /**
         * The meta object literal for the '<em><b>Declarations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SPECIFICATION__DECLARATIONS = eINSTANCE.getSpecification_Declarations();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.DeclarationImpl <em>Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.DeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getDeclaration()
         * @generated
         */
        EClass DECLARATION = eINSTANCE.getDeclaration();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DECLARATION__NAME = eINSTANCE.getDeclaration_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TypeDeclarationImpl <em>Type Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.TypeDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTypeDeclaration()
         * @generated
         */
        EClass TYPE_DECLARATION = eINSTANCE.getTypeDeclaration();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TYPE_DECLARATION__TYPE = eINSTANCE.getTypeDeclaration_Type();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ConstantDeclarationImpl <em>Constant Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ConstantDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getConstantDeclaration()
         * @generated
         */
        EClass CONSTANT_DECLARATION = eINSTANCE.getConstantDeclaration();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONSTANT_DECLARATION__TYPE = eINSTANCE.getConstantDeclaration_Type();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONSTANT_DECLARATION__VALUE = eINSTANCE.getConstantDeclaration_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessDeclarationImpl <em>Process Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ProcessDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getProcessDeclaration()
         * @generated
         */
        EClass PROCESS_DECLARATION = eINSTANCE.getProcessDeclaration();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FunctionDeclarationImpl <em>Function Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.FunctionDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFunctionDeclaration()
         * @generated
         */
        EClass FUNCTION_DECLARATION = eINSTANCE.getFunctionDeclaration();

        /**
         * The meta object literal for the '<em><b>Return Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_DECLARATION__RETURN_TYPE = eINSTANCE.getFunctionDeclaration_ReturnType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ModelDeclarationImpl <em>Model Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ModelDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getModelDeclaration()
         * @generated
         */
        EClass MODEL_DECLARATION = eINSTANCE.getModelDeclaration();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.VariableDeclarationImpl <em>Variable Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.VariableDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getVariableDeclaration()
         * @generated
         */
        EClass VARIABLE_DECLARATION = eINSTANCE.getVariableDeclaration();

        /**
         * The meta object literal for the '<em><b>Initial Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference VARIABLE_DECLARATION__INITIAL_VALUE = eINSTANCE.getVariableDeclaration_InitialValue();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference VARIABLE_DECLARATION__TYPE = eINSTANCE.getVariableDeclaration_Type();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VARIABLE_DECLARATION__PARAMETER = eINSTANCE.getVariableDeclaration_Parameter();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VARIABLE_DECLARATION__NAME = eINSTANCE.getVariableDeclaration_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.BehaviourDeclarationImpl <em>Behaviour Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.BehaviourDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBehaviourDeclaration()
         * @generated
         */
        EClass BEHAVIOUR_DECLARATION = eINSTANCE.getBehaviourDeclaration();

        /**
         * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BEHAVIOUR_DECLARATION__VARIABLES = eINSTANCE.getBehaviourDeclaration_Variables();

        /**
         * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BEHAVIOUR_DECLARATION__STATEMENTS = eINSTANCE.getBehaviourDeclaration_Statements();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ReceiveStatementImpl <em>Receive Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ReceiveStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getReceiveStatement()
         * @generated
         */
        EClass RECEIVE_STATEMENT = eINSTANCE.getReceiveStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.SendStatementImpl <em>Send Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.SendStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getSendStatement()
         * @generated
         */
        EClass SEND_STATEMENT = eINSTANCE.getSendStatement();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.EnumValueReferenceImpl <em>Enum Value Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.EnumValueReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getEnumValueReference()
         * @generated
         */
        EClass ENUM_VALUE_REFERENCE = eINSTANCE.getEnumValueReference();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ENUM_VALUE_REFERENCE__VALUE = eINSTANCE.getEnumValueReference_Value();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ReadCallExpressionImpl <em>Read Call Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ReadCallExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getReadCallExpression()
         * @generated
         */
        EClass READ_CALL_EXPRESSION = eINSTANCE.getReadCallExpression();

        /**
         * The meta object literal for the '<em><b>File</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference READ_CALL_EXPRESSION__FILE = eINSTANCE.getReadCallExpression_File();

        /**
         * The meta object literal for the '<em><b>Load Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference READ_CALL_EXPRESSION__LOAD_TYPE = eINSTANCE.getReadCallExpression_LoadType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.UnwindImpl <em>Unwind</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.UnwindImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnwind()
         * @generated
         */
        EClass UNWIND = eINSTANCE.getUnwind();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNWIND__SOURCE = eINSTANCE.getUnwind_Source();

        /**
         * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNWIND__VARIABLES = eINSTANCE.getUnwind_Variables();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessInstanceImpl <em>Process Instance</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ProcessInstanceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getProcessInstance()
         * @generated
         */
        EClass PROCESS_INSTANCE = eINSTANCE.getProcessInstance();

        /**
         * The meta object literal for the '<em><b>Call</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_INSTANCE__CALL = eINSTANCE.getProcessInstance_Call();

        /**
         * The meta object literal for the '<em><b>Var</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_INSTANCE__VAR = eINSTANCE.getProcessInstance_Var();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessTypeImpl <em>Process Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ProcessTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getProcessType()
         * @generated
         */
        EClass PROCESS_TYPE = eINSTANCE.getProcessType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ProcessReferenceImpl <em>Process Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ProcessReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getProcessReference()
         * @generated
         */
        EClass PROCESS_REFERENCE = eINSTANCE.getProcessReference();

        /**
         * The meta object literal for the '<em><b>Process</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_REFERENCE__PROCESS = eINSTANCE.getProcessReference_Process();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.UnresolvedReferenceImpl <em>Unresolved Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.UnresolvedReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnresolvedReference()
         * @generated
         */
        EClass UNRESOLVED_REFERENCE = eINSTANCE.getUnresolvedReference();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNRESOLVED_REFERENCE__NAME = eINSTANCE.getUnresolvedReference_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.UnresolvedTypeImpl <em>Unresolved Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.UnresolvedTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnresolvedType()
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

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.TimerTypeImpl <em>Timer Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.TimerTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getTimerType()
         * @generated
         */
        EClass TIMER_TYPE = eINSTANCE.getTimerType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.EnumDeclarationImpl <em>Enum Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.EnumDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getEnumDeclaration()
         * @generated
         */
        EClass ENUM_DECLARATION = eINSTANCE.getEnumDeclaration();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ENUM_DECLARATION__VALUES = eINSTANCE.getEnumDeclaration_Values();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.EnumValueImpl <em>Enum Value</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.EnumValueImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getEnumValue()
         * @generated
         */
        EClass ENUM_VALUE = eINSTANCE.getEnumValue();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ENUM_VALUE__NAME = eINSTANCE.getEnumValue_Name();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ChannelExpressionImpl <em>Channel Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChannelExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChannelExpression()
         * @generated
         */
        EClass CHANNEL_EXPRESSION = eINSTANCE.getChannelExpression();

        /**
         * The meta object literal for the '<em><b>Element Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHANNEL_EXPRESSION__ELEMENT_TYPE = eINSTANCE.getChannelExpression_ElementType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CastExpressionImpl <em>Cast Expression</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.CastExpressionImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCastExpression()
         * @generated
         */
        EClass CAST_EXPRESSION = eINSTANCE.getCastExpression();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAST_EXPRESSION__EXPRESSION = eINSTANCE.getCastExpression_Expression();

        /**
         * The meta object literal for the '<em><b>Cast Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAST_EXPRESSION__CAST_TYPE = eINSTANCE.getCastExpression_CastType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.CloseStatementImpl <em>Close Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.CloseStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getCloseStatement()
         * @generated
         */
        EClass CLOSE_STATEMENT = eINSTANCE.getCloseStatement();

        /**
         * The meta object literal for the '<em><b>Handle</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CLOSE_STATEMENT__HANDLE = eINSTANCE.getCloseStatement_Handle();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.FinishStatementImpl <em>Finish Statement</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.FinishStatementImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getFinishStatement()
         * @generated
         */
        EClass FINISH_STATEMENT = eINSTANCE.getFinishStatement();

        /**
         * The meta object literal for the '<em><b>Instances</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FINISH_STATEMENT__INSTANCES = eINSTANCE.getFinishStatement_Instances();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ModelReferenceImpl <em>Model Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ModelReferenceImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getModelReference()
         * @generated
         */
        EClass MODEL_REFERENCE = eINSTANCE.getModelReference();

        /**
         * The meta object literal for the '<em><b>Model</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MODEL_REFERENCE__MODEL = eINSTANCE.getModelReference_Model();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.XperDeclarationImpl <em>Xper Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.XperDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getXperDeclaration()
         * @generated
         */
        EClass XPER_DECLARATION = eINSTANCE.getXperDeclaration();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ComputeDeclarationImpl <em>Compute Declaration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ComputeDeclarationImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getComputeDeclaration()
         * @generated
         */
        EClass COMPUTE_DECLARATION = eINSTANCE.getComputeDeclaration();

        /**
         * The meta object literal for the '<em><b>Return Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPUTE_DECLARATION__RETURN_TYPE = eINSTANCE.getComputeDeclaration_ReturnType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ComputeTypeImpl <em>Compute Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ComputeTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getComputeType()
         * @generated
         */
        EClass COMPUTE_TYPE = eINSTANCE.getComputeType();

        /**
         * The meta object literal for the '<em><b>Parameter Types</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPUTE_TYPE__PARAMETER_TYPES = eINSTANCE.getComputeType_ParameterTypes();

        /**
         * The meta object literal for the '<em><b>Exit Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPUTE_TYPE__EXIT_TYPE = eINSTANCE.getComputeType_ExitType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.impl.ModelTypeImpl <em>Model Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ModelTypeImpl
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getModelType()
         * @generated
         */
        EClass MODEL_TYPE = eINSTANCE.getModelType();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.ChannelOps <em>Channel Ops</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.ChannelOps
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChannelOps()
         * @generated
         */
        EEnum CHANNEL_OPS = eINSTANCE.getChannelOps();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.UnaryOperators <em>Unary Operators</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.UnaryOperators
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getUnaryOperators()
         * @generated
         */
        EEnum UNARY_OPERATORS = eINSTANCE.getUnaryOperators();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.BinaryOperators <em>Binary Operators</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.BinaryOperators
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getBinaryOperators()
         * @generated
         */
        EEnum BINARY_OPERATORS = eINSTANCE.getBinaryOperators();

        /**
         * The meta object literal for the '{@link org.eclipse.escet.chi.metamodel.chi.StdLibFunctions <em>Std Lib Functions</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.escet.chi.metamodel.chi.StdLibFunctions
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getStdLibFunctions()
         * @generated
         */
        EEnum STD_LIB_FUNCTIONS = eINSTANCE.getStdLibFunctions();

        /**
         * The meta object literal for the '<em>Identifier</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChiIdentifier()
         * @generated
         */
        EDataType CHI_IDENTIFIER = eINSTANCE.getChiIdentifier();

        /**
         * The meta object literal for the '<em>Real Number</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChiRealNumber()
         * @generated
         */
        EDataType CHI_REAL_NUMBER = eINSTANCE.getChiRealNumber();

        /**
         * The meta object literal for the '<em>Number</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see org.eclipse.escet.chi.metamodel.chi.impl.ChiPackageImpl#getChiNumber()
         * @generated
         */
        EDataType CHI_NUMBER = eINSTANCE.getChiNumber();

    }

} //ChiPackage
