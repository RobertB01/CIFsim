//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newListType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newObjectType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSetType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTupleType;

import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ObjectType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.UnresolvedType;

/**
 * Type matcher. Can match one type against another type, to figure out for which assignment to the type variables they
 * can match, if any. Can also apply substitutions.
 */
public class TypeMatcher {
    /** Constructor for the {@link TypeMatcher} class. */
    private TypeMatcher() {
        // Static class.
    }

    /**
     * Computes for which types substituted for type parameters, the first given type can be a sub type of the second
     * given type. In other words, for which assignments to type parameters, does a value of the first type always fit
     * in the second type. You can think of this function as returning the types that must be able to fit the type
     * parameters, to ensure that '{@code first <= second}'. If the type for each type parameter is filled in to the
     * given types, then '{@code first <= second}' must hold as per {@link ToolDefTypeUtils#isSubType}.
     *
     * <p>
     * This method assumes that the two given types don't share any type parameters. It also assumes that we can't
     * constrain the type parameters of the first given type (the 'sub' type remains 'any' type).
     * </p>
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param t1 The first type (the 'sub' type).
     * @param t2 The second type (the 'super' type).
     * @param constraints The constraints for the type parameters occurring in the second type (the 'super' type), found
     *     so far. Is modified in-place.
     * @return Whether the first type can be a sub type of the second type ({@code true}) or not ({@code false}).
     */
    public static boolean computeSubType(ToolDefType t1, ToolDefType t2, TypeConstraints constraints) {
        // Unsupported.
        Assert.check(!(t1 instanceof UnresolvedType));
        Assert.check(!(t2 instanceof UnresolvedType));

        // Normalize types.
        t1 = ToolDefTypeUtils.normalizeType(t1);
        t2 = ToolDefTypeUtils.normalizeType(t2);

        // Type parameters.
        if (t2 instanceof TypeParamRef) {
            // For t1 = T, t2 = T, T <= T, we have no additional constraints.
            // However, the assumption (precondition) is that this can't occur.
            TypeParam typeParam2 = ((TypeParamRef)t2).getType();
            if (t1 instanceof TypeParamRef) {
                TypeParam typeParam1 = ((TypeParamRef)t1).getType();
                Assert.check(typeParam1 != typeParam2);
            }

            // t2 = T, t1 <= T, add/update constraint.
            ToolDefType constraint = constraints.get(typeParam2);
            if (constraint == null) {
                constraint = t1;
            } else {
                constraint = ToolDefTypeUtils.mergeTypes(constraint, t1);
            }
            constraints.put(typeParam2, constraint);
            return true;
        }

        if (t1 instanceof TypeParamRef) {
            // For t1 = T, we now by the precondition of this method that we
            // can't further constrain 'T'. Thus, we check whether 'T <= t2'
            // for 'T' being any possible type. Since 't2' can't be a type
            // parameter (already handled above, and also the sets of type
            // parameters used in 't1' and 't2' are disjoint), we know that the
            // only way to satisfy this, is if 't2' is 'object?'.
            return t2 instanceof ObjectType && t2.isNullable();
        }

        // Nullable. If the 'sub' type can be 'null' and the 'super' type
        // can't, the 'null' value of the 'sub' type won't fit into the 'super'
        // type.
        if (t1.isNullable() && !t2.isNullable()) {
            return false;
        }

        // Everything fits within an object.
        if (t2 instanceof ObjectType) {
            return true;
        }

        // Numeric types.
        if (t1 instanceof IntType && t2 instanceof IntType) {
            return true;
        } else if (t1 instanceof IntType && t2 instanceof LongType) {
            return true;
        } else if (t1 instanceof IntType && t2 instanceof DoubleType) {
            return true;
        } else if (t1 instanceof LongType && t2 instanceof LongType) {
            return true;
        } else if (t1 instanceof LongType && t2 instanceof DoubleType) {
            return true;
        } else if (t1 instanceof DoubleType && t2 instanceof DoubleType) {
            return true;
        }

        // Other simple types.
        if (t1 instanceof BoolType && t2 instanceof BoolType) {
            return true;
        } else if (t1 instanceof StringType && t2 instanceof StringType) {
            return true;
        }

        // Container types.
        if (t1 instanceof ListType && t2 instanceof ListType) {
            ListType lt1 = (ListType)t1;
            ListType lt2 = (ListType)t2;
            return computeSubType(lt1.getElemType(), lt2.getElemType(), constraints);
        } else if (t1 instanceof SetType && t2 instanceof SetType) {
            SetType st1 = (SetType)t1;
            SetType st2 = (SetType)t2;
            return computeSubType(st1.getElemType(), st2.getElemType(), constraints);
        } else if (t1 instanceof MapType && t2 instanceof MapType) {
            MapType mt1 = (MapType)t1;
            MapType mt2 = (MapType)t2;
            return computeSubType(mt1.getKeyType(), mt2.getKeyType(), constraints)
                    && computeSubType(mt1.getValueType(), mt2.getValueType(), constraints);
        } else if (t1 instanceof TupleType && t2 instanceof TupleType) {
            List<ToolDefType> fields1 = ((TupleType)t1).getFields();
            List<ToolDefType> fields2 = ((TupleType)t2).getFields();
            if (fields1.size() != fields2.size()) {
                return false;
            }
            for (int i = 0; i < fields1.size(); i++) {
                if (!computeSubType(fields1.get(i), fields2.get(i), constraints)) {
                    return false;
                }
            }
            return true;
        }

        // Not a sub type.
        return false;
    }

    /**
     * Applies substitutions to the given type. The result is completely deep cloned where needed to avoid changing
     * containment relations.
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param type The type to which to apply the substitutions.
     * @param constraints The constraints to apply as substitutions. Assumes that we don't have cycles in the
     *     constraints.
     * @param substMissing Whether to substitute 'object?' as type for type parameters not in the constraints
     *     ({@code true}) or leave them as is ({@code false}).
     * @return The type resulting from the substitutions being applied to the given type.
     */
    public static ToolDefType substitute(ToolDefType type, TypeConstraints constraints, boolean substMissing) {
        // Normalize type.
        type = ToolDefTypeUtils.normalizeType(type);

        // Simple types.
        if (type instanceof BoolType) {
            return type;
        } else if (type instanceof IntType) {
            return type;
        } else if (type instanceof LongType) {
            return type;
        } else if (type instanceof DoubleType) {
            return type;
        } else if (type instanceof StringType) {
            return type;
        } else if (type instanceof ObjectType) {
            return type;
        }

        // Container types.
        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            List<ToolDefType> origFieldTypes = ttype.getFields();
            List<ToolDefType> newFieldTypes = listc(origFieldTypes.size());
            for (ToolDefType origFieldType: origFieldTypes) {
                newFieldTypes.add(substitute(origFieldType, constraints, substMissing));
            }
            if (origFieldTypes.equals(newFieldTypes)) {
                return type;
            }
            return newTupleType(newFieldTypes, type.isNullable(), null);
        } else if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            ToolDefType origElemType = ltype.getElemType();
            ToolDefType newElemType = substitute(origElemType, constraints, substMissing);
            if (origElemType == newElemType) {
                return type;
            }
            return newListType(newElemType, ltype.isNullable(), null);
        } else if (type instanceof SetType) {
            SetType stype = (SetType)type;
            ToolDefType origElemType = stype.getElemType();
            ToolDefType newElemType = substitute(origElemType, constraints, substMissing);
            if (origElemType == newElemType) {
                return type;
            }
            return newSetType(newElemType, stype.isNullable(), null);
        } else if (type instanceof MapType) {
            MapType mtype = (MapType)type;
            ToolDefType origKeyType = mtype.getKeyType();
            ToolDefType newKeyType = substitute(origKeyType, constraints, substMissing);
            ToolDefType origValueType = mtype.getValueType();
            ToolDefType newValueType = substitute(origValueType, constraints, substMissing);
            if (origKeyType == newKeyType && origValueType == newValueType) {
                return type;
            }
            return newMapType(newKeyType, type.isNullable(), null, newValueType);
        }

        // Type parameters.
        if (type instanceof TypeParamRef) {
            TypeParam typeParam = ((TypeParamRef)type).getType();
            ToolDefType rslt = constraints.get(typeParam);
            if (rslt == null) {
                return substMissing ? newObjectType(true, null) : type;
            }

            // No need to apply recursively, as we can't have cycles here.
            return deepclone(rslt);
        }

        // Unknown/unsupported.
        throw new RuntimeException("Unknown/unsupported type: " + type);
    }
}
