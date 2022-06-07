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

import static org.eclipse.escet.cif.common.CifTextUtils.getNamedAncestorOrSelf;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

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
    /** Whether to disallow component definition types. */
    public boolean disallowCompDefTypes;

    /** Whether to disallow component types. */
    public boolean disallowCompTypes;

    /** Whether to disallow dictionary types. */
    public boolean disallowDictTypes;

    /** Whether to disallow distribution types. */
    public boolean disallowDistTypes;

    /** Whether to disallow enumeration types. */
    public boolean disallowEnumTypes;

    /** Whether to disallow function types. Note that this includes standard library function types. */
    public boolean disallowFuncTypes;

    /** Whether to disallow all integer types (ranged and rangeless ones). */
    public boolean disallowIntTypes;

    /** Whether to disallow rangeless integer types. */
    public boolean disallowIntTypesRangeless;

    /** Whether to disallow all list types (array and non-array ones). */
    public boolean disallowListTypes;

    /** Whether to disallow non-array list types. */
    public boolean disallowListTypesNonArray;

    /** Whether to disallow real types. */
    public boolean disallowRealTypes;

    /** Whether to disallow set types. */
    public boolean disallowSetTypes;

    /** Whether to disallow string types. */
    public boolean disallowStringTypes;

    /** Whether to disallow tuple types. Note that tuple types are also used for multi-assignments. */
    public boolean disallowTupleTypes;

    /** Whether to disallow void types (of channels). */
    public boolean disallowVoidTypes;

    @Override
    protected void preprocessComponentDefType(ComponentDefType compDefType) {
        if (disallowCompDefTypes) {
            addTypeViolation(compDefType, "component definition type");
        }
    }

    @Override
    protected void preprocessComponentType(ComponentType compType) {
        if (disallowCompTypes) {
            addTypeViolation(compType, "component type");
        }
    }

    @Override
    protected void preprocessDictType(DictType dictType) {
        if (disallowDictTypes) {
            addTypeViolation(dictType, "dictionary type");
        }
    }

    @Override
    protected void preprocessDistType(DistType distType) {
        if (disallowDistTypes) {
            addTypeViolation(distType, "distribution type");
        }
    }

    @Override
    protected void preprocessEnumType(EnumType enumType) {
        if (disallowEnumTypes) {
            addTypeViolation(enumType, "enumeration type");
        }
    }

    @Override
    protected void preprocessFuncType(FuncType funcType) {
        if (disallowFuncTypes) {
            addTypeViolation(funcType, "function type");
        }
    }

    @Override
    protected void preprocessIntType(IntType intType) {
        if (disallowIntTypes) {
            addTypeViolation(intType, "integer type");
        } else if (disallowIntTypesRangeless && CifTypeUtils.isRangeless(intType)) {
            addTypeViolation(intType, "rangeless integer type");
        }
    }

    @Override
    protected void preprocessListType(ListType listType) {
        if (disallowListTypes) {
            addTypeViolation(listType, "list type");
        } else if (disallowListTypesNonArray && !CifTypeUtils.isArrayType(listType)) {
            addTypeViolation(listType, "non-array list type");
        }
    }

    @Override
    protected void preprocessRealType(RealType realType) {
        if (disallowRealTypes) {
            addTypeViolation(realType, "real type");
        }
    }

    @Override
    protected void preprocessSetType(SetType setType) {
        if (disallowSetTypes) {
            addTypeViolation(setType, "set type");
        }
    }

    @Override
    protected void preprocessStringType(StringType stringType) {
        if (disallowStringTypes) {
            addTypeViolation(stringType, "string type");
        }
    }

    @Override
    protected void preprocessTupleType(TupleType tupleType) {
        if (disallowTupleTypes) {
            addTypeViolation(tupleType, "tuple type");
        }
    }

    @Override
    protected void preprocessVoidType(VoidType voidType) {
        if (disallowVoidTypes) {
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
        super.addViolation(getNamedAncestorOrSelf(type), fmt("uses %s \"%s\"", description, typeToStr(type)));
    }
}
