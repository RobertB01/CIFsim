//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.cif.common.checkers;

import static org.eclipse.escet.cif.common.CifTextUtils.getNamedSelfOrAncestor;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentDefType;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check that does not allow certain types. */
public class NoSpecificTypesCheck extends CifCheck {
    /** The types, or sub-types, to disallow. */
    private final EnumSet<NoSpecificType> disalloweds;

    /**
     * Constructor for the {@link NoSpecificTypesCheck} class.
     *
     * @param disalloweds The types, or sub-types, to disallow.
     */
    public NoSpecificTypesCheck(EnumSet<NoSpecificType> disalloweds) {
        this.disalloweds = disalloweds;
    }

    @Override
    protected void preprocessComponentDefType(ComponentDefType compDefType) {
        if (disalloweds.contains(NoSpecificType.COMP_DEF_TYPES)) {
            addTypeViolation(compDefType, "component definition type");
        }
    }

    @Override
    protected void preprocessComponentType(ComponentType compType) {
        if (disalloweds.contains(NoSpecificType.COMP_TYPES)) {
            addTypeViolation(compType, "component type");
        }
    }

    @Override
    protected void preprocessDictType(DictType dictType) {
        if (disalloweds.contains(NoSpecificType.DICT_TYPES)) {
            addTypeViolation(dictType, "dictionary type");
        }
    }

    @Override
    protected void preprocessDistType(DistType distType) {
        if (disalloweds.contains(NoSpecificType.DIST_TYPES)) {
            addTypeViolation(distType, "distribution type");
        }
    }

    @Override
    protected void preprocessEnumType(EnumType enumType) {
        if (disalloweds.contains(NoSpecificType.ENUM_TYPES)) {
            addTypeViolation(enumType, "enumeration type");
        }
    }

    @Override
    protected void preprocessFuncType(FuncType funcType) {
        if (disalloweds.contains(NoSpecificType.FUNC_TYPES)) {
            addTypeViolation(funcType, "function type");
        }
    }

    @Override
    protected void preprocessIntType(IntType intType) {
        if (disalloweds.contains(NoSpecificType.INT_TYPES)) {
            addTypeViolation(intType, "integer type");
        } else if (disalloweds.contains(NoSpecificType.INT_TYPES_RANGELESS) && CifTypeUtils.isRangeless(intType)) {
            addTypeViolation(intType, "rangeless integer type");
        }
    }

    @Override
    protected void preprocessListType(ListType listType) {
        if (disalloweds.contains(NoSpecificType.LIST_TYPES)) {
            addTypeViolation(listType, "list type");
        } else if (disalloweds.contains(NoSpecificType.LIST_TYPES_NON_ARRAY) && !CifTypeUtils.isArrayType(listType)) {
            addTypeViolation(listType, "non-array list type");
        }
    }

    @Override
    protected void preprocessRealType(RealType realType) {
        if (disalloweds.contains(NoSpecificType.REAL_TYPES)) {
            addTypeViolation(realType, "real type");
        }
    }

    @Override
    protected void preprocessSetType(SetType setType) {
        if (disalloweds.contains(NoSpecificType.SET_TYPES)) {
            addTypeViolation(setType, "set type");
        }
    }

    @Override
    protected void preprocessStringType(StringType stringType) {
        if (disalloweds.contains(NoSpecificType.STRING_TYPES)) {
            addTypeViolation(stringType, "string type");
        }
    }

    @Override
    protected void preprocessTupleType(TupleType tupleType) {
        if (disalloweds.contains(NoSpecificType.TUPLE_TYPES)) {
            addTypeViolation(tupleType, "tuple type");
        }
    }

    @Override
    protected void preprocessVoidType(VoidType voidType) {
        if (disalloweds.contains(NoSpecificType.VOID_TYPES)) {
            addTypeViolation(voidType, "void type");
        }
    }

    @Override
    protected void addViolation(PositionObject typeObj, String message) {
        throw new UnsupportedOperationException(); // Use addTypeViolation.
    }

    /**
     * Add a violation for the given type.
     *
     * @param type The type.
     * @param description The description of the type.
     */
    private void addTypeViolation(CifType type, String description) {
        super.addViolation(getNamedSelfOrAncestor(type), fmt("uses %s \"%s\"", description, typeToStr(type)));
    }

    /** The type, or sub-type, to disallow. */
    public static enum NoSpecificType {
        /** Disallow component definition types. */
        COMP_DEF_TYPES,

        /** Disallow component types. */
        COMP_TYPES,

        /** Disallow dictionary types. */
        DICT_TYPES,

        /** Disallow distribution types. */
        DIST_TYPES,

        /** Disallow enumeration types. */
        ENUM_TYPES,

        /** Disallow function types. Note that this includes standard library function types. */
        FUNC_TYPES,

        /** Disallow all integer types (ranged and rangeless ones). */
        INT_TYPES,

        /** Disallow rangeless integer types. */
        INT_TYPES_RANGELESS,

        /** Disallow all list types (array and non-array ones). */
        LIST_TYPES,

        /** Disallow non-array list types. */
        LIST_TYPES_NON_ARRAY,

        /** Disallow real types. */
        REAL_TYPES,

        /** Disallow set types. */
        SET_TYPES,

        /** Disallow string types. */
        STRING_TYPES,

        /** Disallow tuple types. Note that tuple types are also used for multi-assignments. */
        TUPLE_TYPES,

        /** Disallow void types (of channels). */
        VOID_TYPES,
    }
}
