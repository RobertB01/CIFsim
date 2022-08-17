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

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.checkers.supportcode.CifCheck;
import org.eclipse.escet.cif.common.checkers.supportcode.CifCheckViolations;
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

/** CIF check that does not allow certain types. */
public class TypeNoSpecificTypesCheck extends CifCheck {
    /** The types, or sub-types, to disallow. */
    private final EnumSet<NoSpecificType> disalloweds;

    /**
     * Constructor for the {@link TypeNoSpecificTypesCheck} class.
     *
     * @param disalloweds The types, or sub-types, to disallow.
     */
    public TypeNoSpecificTypesCheck(NoSpecificType... disalloweds) {
        this(EnumSet.copyOf(Arrays.asList(disalloweds)));
    }

    /**
     * Constructor for the {@link TypeNoSpecificTypesCheck} class.
     *
     * @param disalloweds The types, or sub-types, to disallow.
     */
    public TypeNoSpecificTypesCheck(EnumSet<NoSpecificType> disalloweds) {
        this.disalloweds = disalloweds;
    }

    @Override
    protected void preprocessComponentDefType(ComponentDefType compDefType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.COMP_DEF_TYPES)) {
            addTypeViolation(compDefType, "component definition type", violations);
        }
    }

    @Override
    protected void preprocessComponentType(ComponentType compType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.COMP_TYPES)) {
            addTypeViolation(compType, "component type", violations);
        }
    }

    @Override
    protected void preprocessDictType(DictType dictType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.DICT_TYPES)) {
            addTypeViolation(dictType, "dictionary type", violations);
        }
    }

    @Override
    protected void preprocessDistType(DistType distType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.DIST_TYPES)) {
            addTypeViolation(distType, "distribution type", violations);
        }
    }

    @Override
    protected void preprocessEnumType(EnumType enumType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.ENUM_TYPES)) {
            addTypeViolation(enumType, "enumeration type", violations);
        }
    }

    @Override
    protected void preprocessFuncType(FuncType funcType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.FUNC_TYPES)) {
            addTypeViolation(funcType, "function type", violations);
        }
    }

    @Override
    protected void preprocessIntType(IntType intType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.INT_TYPES)) {
            addTypeViolation(intType, "integer type", violations);
        } else if (disalloweds.contains(NoSpecificType.INT_TYPES_RANGELESS) && CifTypeUtils.isRangeless(intType)) {
            addTypeViolation(intType, "rangeless integer type", violations);
        }
    }

    @Override
    protected void preprocessListType(ListType listType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.LIST_TYPES)) {
            addTypeViolation(listType, "list type", violations);
        } else if (disalloweds.contains(NoSpecificType.LIST_TYPES_NON_ARRAY) && !CifTypeUtils.isArrayType(listType)) {
            addTypeViolation(listType, "non-array list type", violations);
        }
    }

    @Override
    protected void preprocessRealType(RealType realType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.REAL_TYPES)) {
            addTypeViolation(realType, "real type", violations);
        }
    }

    @Override
    protected void preprocessSetType(SetType setType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.SET_TYPES)) {
            addTypeViolation(setType, "set type", violations);
        }
    }

    @Override
    protected void preprocessStringType(StringType stringType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.STRING_TYPES)) {
            addTypeViolation(stringType, "string type", violations);
        }
    }

    @Override
    protected void preprocessTupleType(TupleType tupleType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.TUPLE_TYPES)) {
            addTypeViolation(tupleType, "tuple type", violations);
        }
    }

    @Override
    protected void preprocessVoidType(VoidType voidType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.VOID_TYPES)) {
            addTypeViolation(voidType, "void type", violations);
        }
    }

    /**
     * Add a violation for the given type.
     *
     * @param type The type.
     * @param description The description of the type.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addTypeViolation(CifType type, String description, CifCheckViolations violations) {
        violations.add(getNamedSelfOrAncestor(type), fmt("uses %s \"%s\"", description, typeToStr(type)));
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
