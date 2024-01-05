//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.BaseFunctionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;
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
    /** {@link FunctionCallExpression}.{@link FunctionCallExpression#getFunction() function} metamodel feature. */
    private static final EReference FCE_FUNC_REF = ExpressionsPackage.eINSTANCE.getFunctionCallExpression_Function();

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
            violations.add(compDefType, "A component definition type is used");
        }
    }

    @Override
    protected void preprocessComponentType(ComponentType compType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.COMP_TYPES)) {
            violations.add(compType, "A component type is used");
        }
    }

    @Override
    protected void preprocessDictType(DictType dictType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.DICT_TYPES)) {
            violations.add(dictType, "A dictionary type is used");
        }
    }

    @Override
    protected void preprocessDistType(DistType distType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.DIST_TYPES)) {
            violations.add(distType, "A distribution type is used");
        }
    }

    @Override
    protected void preprocessEnumType(EnumType enumType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.ENUM_TYPES)) {
            violations.add(enumType, "An enumeration type is used");
        }
    }

    @Override
    protected void preprocessFuncType(FuncType funcType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.FUNC_TYPES)) {
            violations.add(funcType, "A function type is used");
        } else if (disalloweds.contains(NoSpecificType.FUNC_TYPES_AS_DATA) && !isUsedInFunctionCallContext(funcType)) {
            violations.add(funcType, "A function type is used to store functions or use functions as data values");
        }
    }

    /**
     * Is the provided function type used in a function call context?
     *
     * @param funcType Function type to check.
     * @return Whether the function expression is used in a function call context.
     */
    private boolean isUsedInFunctionCallContext(FuncType funcType) {
        if (!(funcType.eContainer() instanceof BaseFunctionExpression)) {
            return false;
        }
        BaseFunctionExpression baseFuncExpr = (BaseFunctionExpression)funcType.eContainer();
        return (baseFuncExpr.eContainer() instanceof FunctionCallExpression)
                && baseFuncExpr.eContainmentFeature() == FCE_FUNC_REF;
    }

    @Override
    protected void preprocessIntType(IntType intType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.INT_TYPES)) {
            violations.add(intType, "An integer type is used");
        } else if (disalloweds.contains(NoSpecificType.INT_TYPES_RANGELESS) && CifTypeUtils.isRangeless(intType)) {
            violations.add(intType, "A rangeless integer type is used");
        }
    }

    @Override
    protected void preprocessListType(ListType listType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.LIST_TYPES)) {
            violations.add(listType, "A list type is used");
        } else if (disalloweds.contains(NoSpecificType.LIST_TYPES_NON_ARRAY) && !CifTypeUtils.isArrayType(listType)) {
            violations.add(listType, "A non-array list type is used");
        }
    }

    @Override
    protected void preprocessRealType(RealType realType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.REAL_TYPES)) {
            violations.add(realType, "A real type is used");
        }
    }

    @Override
    protected void preprocessSetType(SetType setType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.SET_TYPES)) {
            violations.add(setType, "A set type is used");
        }
    }

    @Override
    protected void preprocessStringType(StringType stringType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.STRING_TYPES)) {
            violations.add(stringType, "A string type is used");
        }
    }

    @Override
    protected void preprocessTupleType(TupleType tupleType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.TUPLE_TYPES)) {
            violations.add(tupleType, "A tuple type is used");
        }
    }

    @Override
    protected void preprocessVoidType(VoidType voidType, CifCheckViolations violations) {
        if (disalloweds.contains(NoSpecificType.VOID_TYPES)) {
            violations.add(voidType, "A void type is used");
        }
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

        /**
         * Disallow function types outside call context. This includes storage of standard library distribution
         * functions in discrete variables.
         */
        FUNC_TYPES_AS_DATA,

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
