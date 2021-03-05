//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.common;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newBoolType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newDoubleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newIntType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newListType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newLongType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newObjectType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSetType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newStringType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTupleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeParamRef;

import java.util.Comparator;
import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression;
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
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.UnresolvedType;

/** ToolDef type utility methods. */
public class ToolDefTypeUtils {
    /** Constructor for the {@link ToolDefTypeUtils} class. */
    private ToolDefTypeUtils() {
        // Static class.
    }

    /**
     * Could the given ToolDef type be nullable? Explicitly nullable types such as 'int?' are definitely nullable. Type
     * parameters may be nullable or non-nullable, depending on the type that is given for the type parameter.
     *
     * @param type The type to check.
     * @return {@code true} if the given type could be nullable, {@code false} otherwise.
     */
    public static boolean mayBeNullable(ToolDefType type) {
        type = normalizeType(type);
        if (type.isNullable()) {
            return true;
        }
        return type instanceof TypeParamRef;
    }

    /**
     * Checks two types for equality.
     *
     * <p>
     * This method should only be used to remove duplicates from sets of types, etc. It should not be used to check
     * whether one type fits within another (use {@link #isSubType} or {@link #isSuperType} for that), or whether two
     * types are distinguishable (use {@link #areDistinguishableTypes} for that).
     * </p>
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param t1 The first type.
     * @param t2 The second type.
     * @return Whether the two types are equal ({@code true}) or not ({@code false}).
     */
    public static boolean areEqualTypes(ToolDefType t1, ToolDefType t2) {
        // Unsupported.
        Assert.check(!(t1 instanceof UnresolvedType));
        Assert.check(!(t2 instanceof UnresolvedType));

        // Normalize types.
        t1 = normalizeType(t1);
        t2 = normalizeType(t2);

        // Nullable.
        if (t1.isNullable() != t2.isNullable()) {
            return false;
        }

        // Simple types.
        if (t1 instanceof BoolType && t2 instanceof BoolType) {
            return true;
        }
        if (t1 instanceof IntType && t2 instanceof IntType) {
            return true;
        } else if (t1 instanceof LongType && t2 instanceof LongType) {
            return true;
        } else if (t1 instanceof DoubleType && t2 instanceof DoubleType) {
            return true;
        } else if (t1 instanceof StringType && t2 instanceof StringType) {
            return true;
        } else if (t1 instanceof ObjectType && t2 instanceof ObjectType) {
            return true;
        }

        // Container types.
        if (t1 instanceof ListType && t2 instanceof ListType) {
            ListType lt1 = (ListType)t1;
            ListType lt2 = (ListType)t2;
            return areEqualTypes(lt1.getElemType(), lt2.getElemType());
        } else if (t1 instanceof SetType && t2 instanceof SetType) {
            SetType st1 = (SetType)t1;
            SetType st2 = (SetType)t2;
            return areEqualTypes(st1.getElemType(), st2.getElemType());
        } else if (t1 instanceof MapType && t2 instanceof MapType) {
            MapType mt1 = (MapType)t1;
            MapType mt2 = (MapType)t2;
            return areEqualTypes(mt1.getKeyType(), mt2.getKeyType())
                    && areEqualTypes(mt1.getValueType(), mt2.getValueType());
        } else if (t1 instanceof TupleType && t2 instanceof TupleType) {
            List<ToolDefType> fields1 = ((TupleType)t1).getFields();
            List<ToolDefType> fields2 = ((TupleType)t2).getFields();
            if (fields1.size() != fields2.size()) {
                return false;
            }
            for (int i = 0; i < fields1.size(); i++) {
                if (!areEqualTypes(fields1.get(i), fields2.get(i))) {
                    return false;
                }
            }
            return true;
        }

        // Type parameters.
        if (t1 instanceof TypeParamRef && t2 instanceof TypeParamRef) {
            // Equal if they refer to the same type parameter.
            return ((TypeParamRef)t1).getType() == ((TypeParamRef)t2).getType();
        }

        // Unequal.
        return false;
    }

    /**
     * Checks two types for distinguishability. Distinguishability is almost identical to {@link #areEqualTypes
     * equality}, except that a type parameter can't be distinguished from any other type.
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param t1 The first type.
     * @param t2 The second type.
     * @return Whether the two types are equal ({@code true}) or not ({@code false}).
     */
    public static boolean areDistinguishableTypes(ToolDefType t1, ToolDefType t2) {
        // Unsupported.
        Assert.check(!(t1 instanceof UnresolvedType));
        Assert.check(!(t2 instanceof UnresolvedType));

        // Normalize types.
        t1 = normalizeType(t1);
        t2 = normalizeType(t2);

        // Type parameter can't be distinguished from anything else.
        if (t1 instanceof TypeParamRef) {
            return false;
        } else if (t2 instanceof TypeParamRef) {
            return false;
        }

        // Nullable.
        if (t1.isNullable() != t2.isNullable()) {
            return true;
        }

        // Simple types.
        if (t1 instanceof BoolType && t2 instanceof BoolType) {
            return false;
        } else if (t1 instanceof IntType && t2 instanceof IntType) {
            return false;
        } else if (t1 instanceof LongType && t2 instanceof LongType) {
            return false;
        } else if (t1 instanceof DoubleType && t2 instanceof DoubleType) {
            return false;
        } else if (t1 instanceof StringType && t2 instanceof StringType) {
            return false;
        } else if (t1 instanceof ObjectType && t2 instanceof ObjectType) {
            return false;
        }

        // Container types.
        if (t1 instanceof ListType && t2 instanceof ListType) {
            ListType lt1 = (ListType)t1;
            ListType lt2 = (ListType)t2;
            return areDistinguishableTypes(lt1.getElemType(), lt2.getElemType());
        } else if (t1 instanceof SetType && t2 instanceof SetType) {
            SetType st1 = (SetType)t1;
            SetType st2 = (SetType)t2;
            return areDistinguishableTypes(st1.getElemType(), st2.getElemType());
        } else if (t1 instanceof MapType && t2 instanceof MapType) {
            MapType mt1 = (MapType)t1;
            MapType mt2 = (MapType)t2;
            return areDistinguishableTypes(mt1.getKeyType(), mt2.getKeyType())
                    || areDistinguishableTypes(mt1.getValueType(), mt2.getValueType());
        } else if (t1 instanceof TupleType && t2 instanceof TupleType) {
            List<ToolDefType> fields1 = ((TupleType)t1).getFields();
            List<ToolDefType> fields2 = ((TupleType)t2).getFields();
            if (fields1.size() != fields2.size()) {
                return true;
            }
            for (int i = 0; i < fields1.size(); i++) {
                if (areDistinguishableTypes(fields1.get(i), fields2.get(i))) {
                    return true;
                }
            }
            return false;
        }

        // Distinguishable.
        return true;
    }

    /**
     * Checks whether the first given type is a sub type of the second given type. In other words, checks whether a
     * value of the first type always fits in the second type. You can think of this function as returning
     * '{@code first <= second}'. Two equal types are always a sub type of each other. Incomparable types are never a
     * sub type of each other.
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param t1 The first type (the 'sub' type).
     * @param t2 The second type (the 'super' type).
     * @return Whether the first type is a sub type of the second type ({@code true}) or not ({@code false}).
     */
    public static boolean isSubType(ToolDefType t1, ToolDefType t2) {
        // Unsupported.
        Assert.check(!(t1 instanceof UnresolvedType));
        Assert.check(!(t2 instanceof UnresolvedType));

        // Normalize types.
        t1 = normalizeType(t1);
        t2 = normalizeType(t2);

        // Type parameters.
        if (t1 instanceof TypeParamRef && t2 instanceof TypeParamRef) {
            TypeParam param1 = ((TypeParamRef)t1).getType();
            TypeParam param2 = ((TypeParamRef)t2).getType();
            return param1 == param2;
        }

        if (t1 instanceof TypeParamRef) {
            // T < 'object?'
            return t2 instanceof ObjectType && t2.isNullable();
        } else if (t2 instanceof TypeParamRef) {
            // t1 might be smaller than t2, but it might also not be.
            return false;
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
            return isSubType(lt1.getElemType(), lt2.getElemType());
        } else if (t1 instanceof SetType && t2 instanceof SetType) {
            SetType st1 = (SetType)t1;
            SetType st2 = (SetType)t2;
            return isSubType(st1.getElemType(), st2.getElemType());
        } else if (t1 instanceof MapType && t2 instanceof MapType) {
            MapType mt1 = (MapType)t1;
            MapType mt2 = (MapType)t2;
            return isSubType(mt1.getKeyType(), mt2.getKeyType()) && isSubType(mt1.getValueType(), mt2.getValueType());
        } else if (t1 instanceof TupleType && t2 instanceof TupleType) {
            List<ToolDefType> fields1 = ((TupleType)t1).getFields();
            List<ToolDefType> fields2 = ((TupleType)t2).getFields();
            if (fields1.size() != fields2.size()) {
                return false;
            }
            for (int i = 0; i < fields1.size(); i++) {
                if (!isSubType(fields1.get(i), fields2.get(i))) {
                    return false;
                }
            }
            return true;
        }

        // Not a sub type.
        return false;
    }

    /**
     * Checks whether the first given type is a super type of the second given type. In other words, checks whether a
     * value of the second type always fits in the first type. You can think of this function as returning
     * '{@code first >= second}'. Two equal types are always a super type of each other. Two incomparable types are
     * never a super type of each other.
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param t1 The first type (the 'super' type).
     * @param t2 The second type (the 'sub' type).
     * @return Whether the first type is a sub type of the second type ({@code true}) or not ({@code false}).
     */
    public static boolean isSuperType(ToolDefType t1, ToolDefType t2) {
        // Unsupported.
        Assert.check(!(t1 instanceof UnresolvedType));
        Assert.check(!(t2 instanceof UnresolvedType));

        // Normalize types.
        t1 = normalizeType(t1);
        t2 = normalizeType(t2);

        // Type parameters.
        if (t1 instanceof TypeParamRef && t2 instanceof TypeParamRef) {
            TypeParam param1 = ((TypeParamRef)t1).getType();
            TypeParam param2 = ((TypeParamRef)t2).getType();
            return param1 == param2;
        }

        if (t2 instanceof TypeParamRef) {
            // 'object?' > T
            return t1 instanceof ObjectType && t1.isNullable();
        } else if (t1 instanceof TypeParamRef) {
            // t1 might be bigger than t2, but it might also not be.
            return false;
        }

        // Nullable. If the 'super' type can't be 'null' and the 'sub' type
        // can, the 'null' value of the 'sub' type won't fit into the 'super'
        // type.
        if (!t1.isNullable() && t2.isNullable()) {
            return false;
        }

        // Everything fits within an object.
        if (t1 instanceof ObjectType) {
            return true;
        }

        // Numeric types.
        if (t1 instanceof IntType && t2 instanceof IntType) {
            return true;
        } else if (t1 instanceof LongType && t2 instanceof IntType) {
            return true;
        } else if (t1 instanceof DoubleType && t2 instanceof IntType) {
            return true;
        } else if (t1 instanceof LongType && t2 instanceof LongType) {
            return true;
        } else if (t1 instanceof DoubleType && t2 instanceof LongType) {
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
            return isSuperType(lt1.getElemType(), lt2.getElemType());
        } else if (t1 instanceof SetType && t2 instanceof SetType) {
            SetType st1 = (SetType)t1;
            SetType st2 = (SetType)t2;
            return isSuperType(st1.getElemType(), st2.getElemType());
        } else if (t1 instanceof MapType && t2 instanceof MapType) {
            MapType mt1 = (MapType)t1;
            MapType mt2 = (MapType)t2;
            return isSuperType(mt1.getKeyType(), mt2.getKeyType())
                    && isSuperType(mt1.getValueType(), mt2.getValueType());
        } else if (t1 instanceof TupleType && t2 instanceof TupleType) {
            List<ToolDefType> fields1 = ((TupleType)t1).getFields();
            List<ToolDefType> fields2 = ((TupleType)t2).getFields();
            if (fields1.size() != fields2.size()) {
                return false;
            }
            for (int i = 0; i < fields1.size(); i++) {
                if (!isSuperType(fields1.get(i), fields2.get(i))) {
                    return false;
                }
            }
            return true;
        }

        // Not a super type.
        return false;
    }

    /**
     * Normalizes a ToolDef type by recursively stripping off top level type references.
     *
     * <p>
     * This method is particularly useful when comparing the result to another type for type compatibility, or checking
     * the result for being an instance of a certain (non-referenced) type.
     * </p>
     *
     * @param type The ToolDef type to normalize.
     * @return The normalized ToolDef type.
     */
    public static ToolDefType normalizeType(ToolDefType type) {
        while (type instanceof TypeRef) {
            type = ((TypeRef)type).getType().getType();
        }
        return type;
    }

    /**
     * Hashes the given type.
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param type The type.
     * @return The hash of the type.
     * @see Object#hashCode
     */
    public static int hashType(ToolDefType type) {
        // Reference types.
        type = normalizeType(type);

        // Nullable.
        int rslt = type.isNullable() ? 1 << 0 : 0;

        // Simple types.
        if (type instanceof BoolType) {
            return rslt ^ (1 << 3);
        } else if (type instanceof IntType) {
            return rslt ^ (1 << 6);
        } else if (type instanceof LongType) {
            return rslt ^ (1 << 9);
        } else if (type instanceof DoubleType) {
            return rslt ^ (1 << 12);
        } else if (type instanceof StringType) {
            return rslt ^ (1 << 13);
        } else if (type instanceof ObjectType) {
            return rslt ^ (1 << 15);
        }

        // Container types.
        if (type instanceof ListType) {
            rslt = rslt ^ (1 << 18);
            return rslt + hashType(((ListType)type).getElemType());
        } else if (type instanceof SetType) {
            rslt = rslt ^ (1 << 21);
            return rslt + hashType(((SetType)type).getElemType());
        } else if (type instanceof MapType) {
            rslt = rslt ^ (1 << 24);
            return rslt + hashType(((MapType)type).getKeyType()) * 7 + hashType(((MapType)type).getValueType()) * 11;
        } else if (type instanceof TupleType) {
            rslt = rslt ^ (1 << 27);
            for (ToolDefType fieldType: ((TupleType)type).getFields()) {
                rslt += hashType(fieldType) * 7;
            }
            return rslt;
        }

        // Type parameters.
        if (type instanceof TypeParamRef) {
            rslt = rslt ^ (1 << 30);
            return rslt + ((TypeParamRef)type).getType().getName().hashCode();
        }

        // Unknown/unsupported.
        throw new RuntimeException("Unknown/unsupported type: " + type);
    }

    /**
     * Compares two types for order.
     *
     * <p>
     * This method uses a total order over all ToolDef types. The order is used primarily to make choices between
     * several types, and should not be used to check whether one type fits within another type. Use {@link #isSubType}
     * or {@link #isSuperType} instead, to check whether one type fits within another type.
     * </p>
     *
     * <p>
     * The order used by this method is compatible with the {@link #areEqualTypes}, {@link #isSubType} and
     * {@link #isSuperType} methods, which use partial orders.
     * </p>
     *
     * <p>
     * The order used by this method is as follows for types at the same level:
     * <ul>
     * <li>{@code bool < int < long < double < string < tuple < list < set <
     *    map < object}</li>
     * <li>{@code bool? < int? < long? < double? < string? < tuple? < list? <
     *    set? < map? < T < object?}, with {@code T} a type parameter</li>
     * <li>{@code t < t?} for every {@code t} in {@code [bool, int, long,
     *    double, string, tuple, list, set, map, object]}</li>
     * <li>{@code T < U} for all type parameters {@code T} and {@code U}, if {@code T} is lexicographically before
     * {@code U}</li>
     * </ul>
     * For types at different levels:
     * <ul>
     * <li>For list types, the element types are compared recursively.</li>
     * <li>For set types, the element types are compared recursively.</li>
     * <li>For map types, the key and value types are compared recursively, with the key types having priority over the
     * value types.</li>
     * <li>For tuple types, the field counts (smaller tuples are before larger tuples) and field types are compared
     * recursively, with the field counts having priority over the field types, and the field types are taken from left
     * to right.</li>
     * </ul>
     * </p>
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param t1 The first type.
     * @param t2 The second type.
     * @return A negative integer if the first type is ordered before the second type, a positive integer if the first
     *     type is ordered after the second type, and zero if the two types are equal.
     * @see Comparator
     * @see Comparator#compare
     */
    public static int compareTypes(ToolDefType t1, ToolDefType t2) {
        // Type references.
        t1 = normalizeType(t1);
        t2 = normalizeType(t2);

        // Use nullable.
        if (mayBeNullable(t1) && !mayBeNullable(t2)) {
            return 1;
        }
        if (!mayBeNullable(t1) && mayBeNullable(t2)) {
            return -1;
        }

        // Use basic sort orders. If same, continue to check children.
        int o1 = getBasicSortOrder(t1);
        int o2 = getBasicSortOrder(t2);
        if (o1 < o2) {
            return -1;
        }
        if (o1 > o2) {
            return 1;
        }

        // Simple types. Equal, as they have no children.
        if (t1 instanceof BoolType) {
            return 0;
        } else if (t1 instanceof IntType) {
            return 0;
        } else if (t1 instanceof LongType) {
            return 0;
        } else if (t1 instanceof DoubleType) {
            return 0;
        } else if (t1 instanceof StringType) {
            return 0;
        } else if (t1 instanceof ObjectType) {
            return 0;
        }

        // Container types. Check recursively.
        if (t1 instanceof ListType) {
            ListType lt1 = (ListType)t1;
            ListType lt2 = (ListType)t2;
            return compareTypes(lt1.getElemType(), lt2.getElemType());
        } else if (t1 instanceof SetType) {
            SetType st1 = (SetType)t1;
            SetType st2 = (SetType)t2;
            return compareTypes(st1.getElemType(), st2.getElemType());
        } else if (t1 instanceof MapType) {
            // Key has priority over value.
            MapType mt1 = (MapType)t1;
            MapType mt2 = (MapType)t2;
            int krslt = compareTypes(mt1.getKeyType(), mt2.getKeyType());
            if (krslt != 0) {
                return krslt;
            }
            return compareTypes(mt1.getValueType(), mt2.getValueType());
        } else if (t1 instanceof TupleType) {
            // Field count has priority, then fields from left to right.
            List<ToolDefType> fields1 = ((TupleType)t1).getFields();
            List<ToolDefType> fields2 = ((TupleType)t2).getFields();
            if (fields1.size() < fields2.size()) {
                return -1;
            }
            if (fields1.size() > fields2.size()) {
                return 1;
            }
            for (int i = 0; i < fields1.size(); i++) {
                int frslt = compareTypes(fields1.get(i), fields2.get(i));
                if (frslt != 0) {
                    return frslt;
                }
            }
            return 0;
        }

        // Type parameters. Smart sorting based on names.
        if (t1 instanceof TypeParamRef) {
            TypeParam param1 = ((TypeParamRef)t1).getType();
            TypeParam param2 = ((TypeParamRef)t2).getType();
            return Strings.SORTER.compare(param1.getName(), param2.getName());
        }

        // Unknown.
        throw new RuntimeException("Unknown type: " + t1);
    }

    /**
     * Returns a numeric value to use to determine whether a given type is ordered before another type. This method is
     * to be used by the {@link #compareTypes} method only.
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param type The type. Must be a {@link #normalizeType normalized} type.
     * @return A unique number for a type, ignoring nullability and children. Types with smaller numbers are ordered
     *     before types with larger numbers.
     */
    private static int getBasicSortOrder(ToolDefType type) {
        if (type instanceof BoolType) {
            return 1;
        } else if (type instanceof IntType) {
            return 2;
        } else if (type instanceof LongType) {
            return 3;
        } else if (type instanceof DoubleType) {
            return 4;
        } else if (type instanceof StringType) {
            return 5;
        } else if (type instanceof TupleType) {
            return 6;
        } else if (type instanceof ListType) {
            return 7;
        } else if (type instanceof SetType) {
            return 8;
        } else if (type instanceof MapType) {
            return 9;
        } else if (type instanceof TypeParamRef) {
            return 10;
        } else if (type instanceof ObjectType) {
            return 11;
        }

        throw new RuntimeException("Unsupported type: " + type);
    }

    /**
     * Merges two types to get the union of the types. The union of two types is the smallest type that can contain
     * values of both the given types.
     *
     * <p>
     * The resulting types are either clones of (parts of) the input types, or they are freshly constructed (and have no
     * position information).
     * </p>
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param t1 The first type.
     * @param t2 The second type.
     * @return The merged type, which is the union of both types.
     */
    public static ToolDefType mergeTypes(ToolDefType t1, ToolDefType t2) {
        // Normalize both types.
        t1 = normalizeType(t1);
        t2 = normalizeType(t2);

        // Merge based on the mutual types, for compatible types.
        boolean nullable = mayBeNullable(t1) || mayBeNullable(t2);

        // Booleans.
        if (t1 instanceof BoolType && t2 instanceof BoolType) {
            return newBoolType(nullable, null);
        }

        // Numbers.
        if (t1 instanceof IntType && t2 instanceof IntType) {
            return newIntType(nullable, null);
        } else if (t1 instanceof IntType && t2 instanceof LongType) {
            return newLongType(nullable, null);
        } else if (t1 instanceof IntType && t2 instanceof DoubleType) {
            return newDoubleType(nullable, null);
        } else if (t1 instanceof LongType && t2 instanceof IntType) {
            return newLongType(nullable, null);
        } else if (t1 instanceof LongType && t2 instanceof LongType) {
            return newLongType(nullable, null);
        } else if (t1 instanceof LongType && t2 instanceof DoubleType) {
            return newDoubleType(nullable, null);
        } else if (t1 instanceof DoubleType && t2 instanceof IntType) {
            return newDoubleType(nullable, null);
        } else if (t1 instanceof DoubleType && t2 instanceof LongType) {
            return newDoubleType(nullable, null);
        } else if (t1 instanceof DoubleType && t2 instanceof DoubleType) {
            return newDoubleType(nullable, null);
        }

        // Strings.
        if (t1 instanceof StringType && t2 instanceof StringType) {
            return newStringType(nullable, null);
        }

        // Lists.
        if (t1 instanceof ListType && t2 instanceof ListType) {
            ListType lt1 = (ListType)t1;
            ListType lt2 = (ListType)t2;
            ToolDefType elemType = mergeTypes(lt1.getElemType(), lt2.getElemType());
            return newListType(elemType, nullable, null);
        }

        // Sets.
        if (t1 instanceof SetType && t2 instanceof SetType) {
            SetType st1 = (SetType)t1;
            SetType st2 = (SetType)t2;
            ToolDefType elemType = mergeTypes(st1.getElemType(), st2.getElemType());
            return newSetType(elemType, nullable, null);
        }

        // Maps.
        if (t1 instanceof MapType && t2 instanceof MapType) {
            MapType mt1 = (MapType)t1;
            MapType mt2 = (MapType)t2;
            ToolDefType keyType = mergeTypes(mt1.getKeyType(), mt2.getKeyType());
            ToolDefType valueType = mergeTypes(mt1.getValueType(), mt2.getValueType());
            return newMapType(keyType, nullable, null, valueType);
        }

        // Tuple.
        if (t1 instanceof TupleType && t2 instanceof TupleType) {
            TupleType tt1 = (TupleType)t1;
            TupleType tt2 = (TupleType)t2;

            int cnt1 = tt1.getFields().size();
            int cnt2 = tt2.getFields().size();
            if (cnt1 == cnt2) {
                TupleType rslt = newTupleType(null, nullable, null);
                for (int i = 0; i < cnt1; i++) {
                    ToolDefType fieldType1 = tt1.getFields().get(i);
                    ToolDefType fieldType2 = tt2.getFields().get(i);
                    rslt.getFields().add(mergeTypes(fieldType1, fieldType2));
                }
                return rslt;
            }
        }

        // Type parameters.
        if (t1 instanceof TypeParamRef && t2 instanceof TypeParamRef) {
            TypeParam param1 = ((TypeParamRef)t1).getType();
            TypeParam param2 = ((TypeParamRef)t2).getType();
            if (param1 == param2) {
                return newTypeParamRef(false, null, param1);
            }
        }

        // Unsupported types.
        Assert.check(!(t1 instanceof UnresolvedType));
        Assert.check(!(t2 instanceof UnresolvedType));

        // Incomparable types and/or object types. Use an object type.
        return newObjectType(nullable, null);
    }

    /**
     * Does the given type have a default value? Type parameters are considered not to have a default value, as they may
     * be given a type that does not have a default value.
     *
     * <p>
     * This method does not support {@link UnresolvedType}.
     * </p>
     *
     * @param type The type.
     * @return {@code true} if it has a default value, {@code false} otherwise.
     */
    public static boolean hasDefaultValue(ToolDefType type) {
        // Reference types.
        type = normalizeType(type);

        // Type parameters.
        if (type instanceof TypeParamRef) {
            return false;
        }

        // Nullable.
        if (type.isNullable()) {
            return true;
        }

        // Simple types.
        if (type instanceof BoolType) {
            return true;
        } else if (type instanceof IntType) {
            return true;
        } else if (type instanceof LongType) {
            return true;
        } else if (type instanceof DoubleType) {
            return true;
        } else if (type instanceof StringType) {
            return true;
        } else if (type instanceof ObjectType) {
            return false;
        }

        // Container types.
        if (type instanceof ListType) {
            return true;
        } else if (type instanceof SetType) {
            return true;
        } else if (type instanceof MapType) {
            return true;
        } else if (type instanceof TupleType) {
            for (ToolDefType fieldType: ((TupleType)type).getFields()) {
                if (!hasDefaultValue(fieldType)) {
                    return false;
                }
            }
            return true;
        }

        // Unknown/unsupported.
        throw new RuntimeException("Unknown/unsupported type: " + type);
    }

    /**
     * Creates a non-nullable tuple type for the given field types, if needed.
     *
     * <p>
     * The field types are <em>not</em> deep cloned, so there containment may change as a result of using this method.
     * </p>
     *
     * @param fieldTypes The field types. Must have at least one element.
     * @return If there is only one element, that element, or a tuple type with the given field types otherwise.
     */
    public static ToolDefType makeTupleType(List<ToolDefType> fieldTypes) {
        Assert.check(!fieldTypes.isEmpty());
        if (fieldTypes.size() == 1) {
            return first(fieldTypes);
        }
        return newTupleType(fieldTypes, false, null);
    }

    /**
     * Creates a ToolDef type (possibly a tuple type) for the given values.
     *
     * <p>
     * The types of the values <em>are</em> deep cloned, so that their containment does not change.
     * </p>
     *
     * @param values The values. Must have at least one value.
     * @return If there is only one value, the type of that value, or a tuple type with the types of the given values
     *     otherwise.
     */
    public static ToolDefType makeTupleTypeFromValues(List<Expression> values) {
        List<ToolDefType> types = listc(values.size());
        for (Expression value: values) {
            types.add(deepclone(value.getType()));
        }
        return makeTupleType(types);
    }

    /**
     * Returns the object referenced by the reference expression.
     *
     * <p>
     * This method does not support {@link UnresolvedRefExpression}.
     * </p>
     *
     * @param refExpr The reference expression.
     * @return The object referenced by the reference expression.
     */
    public static PositionObject getRefObjFromRef(Expression refExpr) {
        if (refExpr instanceof VariableExpression) {
            return ((VariableExpression)refExpr).getVariable();
        } else if (refExpr instanceof ToolParamExpression) {
            return ((ToolParamExpression)refExpr).getParam();
        } else {
            throw new RuntimeException("Unexpected ref expr: " + refExpr);
        }
    }
}
