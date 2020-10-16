//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.runtime;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
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

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
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
import org.eclipse.escet.tooldef.runtime.builtins.BuiltInDataTools;

/** ToolDef runtime utility methods. */
public class ToolDefRuntimeUtils {
    /** Constructor for the {@link ToolDefRuntimeUtils} class. */
    private ToolDefRuntimeUtils() {
        // Static class.
    }

    /**
     * Do the given ToolDef values represent equal values?
     *
     * @param v1 The first value.
     * @param v2 The second value.
     * @return {@code true} if the given ToolDef values represent equal values, {@code false} otherwise.
     * @see #hashValue
     * @see #compareValues
     */
    public static boolean equalValues(Object v1, Object v2) {
        // Null values.
        if ((v1 == null) != (v2 == null)) {
            return false;
        }
        if (v1 == null || v2 == null) {
            return true;
        }

        // Simple values.
        if (v1 instanceof Boolean && v2 instanceof Boolean) {
            boolean b1 = (boolean)v1;
            boolean b2 = (boolean)v2;
            return b1 == b2;
        } else if (v1 instanceof Integer && v2 instanceof Integer) {
            int i1 = (int)v1;
            int i2 = (int)v2;
            return i1 == i2;
        } else if (v1 instanceof Integer && v2 instanceof Long) {
            int i1 = (int)v1;
            long l1 = i1;
            long l2 = (long)v2;
            return l1 == l2;
        } else if (v1 instanceof Integer && v2 instanceof Double) {
            int i1 = (int)v1;
            double d1 = i1;
            double d2 = (double)v2;
            return d1 == d2;
        } else if (v1 instanceof Long && v2 instanceof Integer) {
            long l1 = (long)v1;
            int i2 = (int)v2;
            long l2 = i2;
            return l1 == l2;
        } else if (v1 instanceof Long && v2 instanceof Long) {
            long l1 = (long)v1;
            long l2 = (long)v2;
            return l1 == l2;
        } else if (v1 instanceof Long && v2 instanceof Double) {
            long l1 = (long)v1;
            double d1 = l1;
            double d2 = (double)v2;
            return d1 == d2;
        } else if (v1 instanceof Double && v2 instanceof Integer) {
            double d1 = (double)v1;
            int i2 = (int)v2;
            double d2 = i2;
            return d1 == d2;
        } else if (v1 instanceof Double && v2 instanceof Long) {
            double d1 = (double)v1;
            long l2 = (long)v2;
            double d2 = l2;
            return d1 == d2;
        } else if (v1 instanceof Double && v2 instanceof Double) {
            double d1 = (double)v1;
            double d2 = (double)v2;
            return d1 == d2;
        } else if (v1 instanceof String && v2 instanceof String) {
            return v1.equals(v2);
        }

        // Container values.
        if (v1 instanceof ToolDefList && v2 instanceof ToolDefList) {
            return v1.equals(v2);
        } else if (v1 instanceof ToolDefSet && v2 instanceof ToolDefSet) {
            return v1.equals(v2);
        } else if (v1 instanceof ToolDefMap && v2 instanceof ToolDefMap) {
            return v1.equals(v2);
        } else if (v1 instanceof ToolDefTuple && v2 instanceof ToolDefTuple) {
            return v1.equals(v2);
        }

        // Other values (other combinations).
        return false;
    }

    /**
     * Hashes the given ToolDef value. This method computes hashes in a manner that is compatible with
     * {@link #equalValues}.
     *
     * @param v The value.
     * @return The hash code of the value.
     * @see #equalValues
     * @see #compareValues
     */
    public static int hashValue(Object v) {
        // Null value.
        if (v == null) {
            return 0;
        }

        // Simple values.
        if (v instanceof Boolean) {
            return ((Boolean)v).hashCode();
        } else if (v instanceof Integer) {
            // Integers and doubles can be equal, so we need to ensure their
            // hash code is equal in such cases as well.
            double d = (Integer)v;
            return ((Double)d).hashCode();
        } else if (v instanceof Long) {
            // Integers and doubles can be equal, so we need to ensure their
            // hash code is equal in such cases as well.
            double d = (Long)v;
            return ((Double)d).hashCode();
        } else if (v instanceof Double) {
            return ((Double)v).hashCode();
        } else if (v instanceof String) {
            return ((String)v).hashCode();
        }

        // Container values.
        if (v instanceof ToolDefList) {
            return ((ToolDefList<?>)v).hashCode();
        } else if (v instanceof ToolDefSet) {
            return ((ToolDefSet<?>)v).hashCode();
        } else if (v instanceof ToolDefMap) {
            return ((ToolDefMap<?, ?>)v).hashCode();
        } else if (v instanceof ToolDefTuple) {
            return ((ToolDefTuple)v).hashCode();
        }

        // Unknown value.
        throw new RuntimeException("Unknown value: " + v);
    }

    /**
     * Compares two ToolDef values.
     *
     * <p>
     * This method uses a total order over all ToolDef values. The order used by this method is compatible with the
     * {@link #equalValues} and {@link #hashValue} methods, which use partial orders.
     * </p>
     *
     * <p>
     * The order used by this method is as follows:
     * <ul>
     * <li>{@code bool < int/long/double < string < tuple < list < set <
     *    map < null}</li>
     * <li>boolean value 'false' precedes boolean value 'true'</li>
     * <li>two int/long/double values are compared using the smallest common type</li>
     * </ul>
     * For types at different levels:
     * <ul>
     * <li>For list types, shorter lists are before longer lists, and for lists of equal size the elements are compared
     * recursively, from left to right.</li>
     * <li>For set types, smaller sets are before larger sets, and for sets of equal size the elements are compared
     * recursively, using sorted iteration order of the elements.</li>
     * <li>For map types, smaller maps are before larger maps, and for maps of equal size the entries are compared
     * recursively, using sorted iteration order of the keys, and with priority for the keys of the entries over the
     * values of the entries.</li>
     * <li>For tuple types, shorter tuples are before longer tuples, and for tuples of equal size the elements are
     * compared recursively, from left to right.</li>
     * </ul>
     * </p>
     *
     * @param v1 The first value.
     * @param v2 The second value.
     * @return A negative integer if the first value is smaller than the second value, zero if the values are equal, and
     *     a positive integer if the first value is larger than the second value.
     * @see #equalValues
     * @see #hashValue
     */
    public static int compareValues(Object v1, Object v2) {
        // Use basic sort orders. If same, continue to check value/children/etc.
        int o1 = getBasicSortOrder(v1);
        int o2 = getBasicSortOrder(v2);
        if (o1 < o2) {
            return -1;
        }
        if (o1 > o2) {
            return 1;
        }

        // Same sort order. Do actual compare, based on the type of values.
        if (v1 instanceof Boolean) {
            boolean b1 = (Boolean)v1;
            boolean b2 = (Boolean)v2;
            return Boolean.compare(b1, b2);
        } else if (v1 instanceof Integer && v2 instanceof Integer) {
            int i1 = (Integer)v1;
            int i2 = (Integer)v2;
            return Integer.compare(i1, i2);
        } else if (v1 instanceof Integer && v2 instanceof Long) {
            long l1 = (Integer)v1;
            long l2 = (Long)v2;
            return Long.compare(l1, l2);
        } else if (v1 instanceof Integer && v2 instanceof Double) {
            double d1 = (Integer)v1;
            double d2 = (Double)v2;
            return Double.compare(d1, d2);
        } else if (v1 instanceof Long && v2 instanceof Integer) {
            long l1 = (Long)v1;
            long l2 = (Integer)v2;
            return Long.compare(l1, l2);
        } else if (v1 instanceof Long && v2 instanceof Long) {
            long l1 = (Long)v1;
            long l2 = (Long)v2;
            return Long.compare(l1, l2);
        } else if (v1 instanceof Long && v2 instanceof Double) {
            double d1 = (Long)v1;
            double d2 = (Double)v2;
            return Double.compare(d1, d2);
        } else if (v1 instanceof Double && v2 instanceof Integer) {
            double d1 = (Double)v1;
            double d2 = (Integer)v2;
            return Double.compare(d1, d2);
        } else if (v1 instanceof Double && v2 instanceof Long) {
            double d1 = (Double)v1;
            double d2 = (Long)v2;
            return Double.compare(d1, d2);
        } else if (v1 instanceof Double && v2 instanceof Double) {
            double d1 = (Double)v1;
            double d2 = (Double)v2;
            return Double.compare(d1, d2);
        } else if (v1 instanceof String) {
            String s1 = (String)v1;
            String s2 = (String)v2;
            return Strings.SORTER.compare(s1, s2);
        } else if (v1 instanceof ToolDefTuple) {
            ToolDefTuple t1 = (ToolDefTuple)v1;
            ToolDefTuple t2 = (ToolDefTuple)v2;
            int size1 = t1.size();
            int size2 = t2.size();
            if (size1 < size2) {
                return -1;
            }
            if (size1 > size2) {
                return 1;
            }
            for (int i = 0; i < size1; i++) {
                int erslt = compareValues(t1.getValue(i), t2.getValue(i));
                if (erslt != 0) {
                    return erslt;
                }
            }
            return 0;
        } else if (v1 instanceof ToolDefList) {
            ToolDefList<?> l1 = (ToolDefList<?>)v1;
            ToolDefList<?> l2 = (ToolDefList<?>)v2;
            if (l1.size() < l2.size()) {
                return -1;
            }
            if (l1.size() > l2.size()) {
                return 1;
            }
            for (int i = 0; i < l1.size(); i++) {
                int erslt = compareValues(l1.get(i), l2.get(i));
                if (erslt != 0) {
                    return erslt;
                }
            }
            return 0;
        } else if (v1 instanceof ToolDefSet<?>) {
            ToolDefSet<?> s1 = (ToolDefSet<?>)v1;
            ToolDefSet<?> s2 = (ToolDefSet<?>)v2;
            if (s1.size() < s2.size()) {
                return -1;
            }
            if (s1.size() > s2.size()) {
                return 1;
            }

            ToolDefList<Object> l1 = new ToolDefList<>(s1);
            ToolDefList<Object> l2 = new ToolDefList<>(s2);
            Collections.sort(l1, ToolDefRuntimeUtils::compareValues);
            Collections.sort(l2, ToolDefRuntimeUtils::compareValues);
            return compareValues(l1, l2);
        } else if (v1 instanceof ToolDefMap<?, ?>) {
            @SuppressWarnings("unchecked")
            ToolDefMap<Object, Object> m1 = (ToolDefMap<Object, Object>)v1;
            @SuppressWarnings("unchecked")
            ToolDefMap<Object, Object> m2 = (ToolDefMap<Object, Object>)v2;
            if (m1.size() < m2.size()) {
                return -1;
            }
            if (m1.size() > m2.size()) {
                return 1;
            }

            List<ToolDefTuplePair<Object, Object>> l1 = BuiltInDataTools.entries(m1);
            List<ToolDefTuplePair<Object, Object>> l2 = BuiltInDataTools.entries(m2);
            Collections.sort(l1, ((a, b) -> compareValues(a.left, b.left)));
            Collections.sort(l2, ((a, b) -> compareValues(a.left, b.left)));
            return compareValues(l1, l2);
        } else if (v1 == null) {
            Assert.check(v2 == null);
            return 0;
        } else {
            String msg = fmt("Unknown runtime values: \"%s\" and \"%s\".", v1, v2);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Returns a numeric value to use to determine whether a given ToolDef value is ordered before another ToolDef
     * value. Only the type of the value is taken into account. This method is to be used by the {@link #compareValues}
     * method only.
     *
     * @param value The value.
     * @return A unique number for a value. Values with smaller numbers are ordered before values with larger numbers.
     */
    private static int getBasicSortOrder(Object value) {
        if (value instanceof Boolean) {
            return 1;
        } else if (value instanceof Integer) {
            return 2;
        } else if (value instanceof Long) {
            return 2;
        } else if (value instanceof Double) {
            return 2;
        } else if (value instanceof String) {
            return 3;
        } else if (value instanceof ToolDefTuple) {
            return 4;
        } else if (value instanceof ToolDefList) {
            return 5;
        } else if (value instanceof ToolDefSet) {
            return 6;
        } else if (value instanceof ToolDefMap) {
            return 7;
        } else if (value == null) {
            return 8;
        }

        throw new RuntimeException("Unsupported value: " + value);
    }

    /**
     * Returns the default value for a ToolDef type.
     *
     * @param type The type. Must have a default value (see {@link ToolDefTypeUtils#hasDefaultValue}).
     * @return The default value for the type.
     */
    public static Object getDefaultValue(ToolDefType type) {
        // Normalize type.
        type = ToolDefTypeUtils.normalizeType(type);

        // Simple types.
        if (type instanceof BoolType) {
            return false;
        } else if (type instanceof IntType) {
            return (int)0;
        } else if (type instanceof LongType) {
            return (long)0;
        } else if (type instanceof DoubleType) {
            return (double)0.0;
        } else if (type instanceof StringType) {
            return "";
        }

        if (type instanceof ObjectType) {
            Assert.check(type.isNullable());
            return null;
        }

        // Container types.
        if (type instanceof ListType) {
            return new ToolDefList<>();
        } else if (type instanceof SetType) {
            return new ToolDefSet<>();
        } else if (type instanceof MapType) {
            return new ToolDefMap<>();
        }

        if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;
            List<Object> elems = new ToolDefList<>(ttype.getFields().size());
            for (ToolDefType fieldType: ttype.getFields()) {
                elems.add(getDefaultValue(fieldType));
            }
            return makeTuple(elems);
        }

        // Unknown/unsupported type.
        throw new RuntimeException("Unknown/unsupported type: " + type);
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the ToolDef ASCII syntax.
     *
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static String valueToStr(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof Boolean) {
            boolean bvalue = (Boolean)value;
            return bvalue ? "true" : "false";
        } else if (value instanceof Integer) {
            int ivalue = (Integer)value;
            return Integer.toString(ivalue);
        } else if (value instanceof Long) {
            long lvalue = (Long)value;
            return Long.toString(lvalue);
        } else if (value instanceof Double) {
            double dvalue = (Double)value;
            Assert.check(!Double.isNaN(dvalue));
            Assert.check(!Double.isInfinite(dvalue));
            return Double.toString(dvalue).replace('E', 'e');
        } else if (value instanceof String) {
            String svalue = (String)value;
            return "\"" + Strings.escape(svalue) + "\"";
        } else if (value instanceof ToolDefTuple) {
            ToolDefTuple tvalue = (ToolDefTuple)value;
            return tvalue.toString();
        } else if (value instanceof ToolDefList) {
            ToolDefList<?> lvalue = (ToolDefList<?>)value;
            List<String> elems = listc(lvalue.size());
            for (Object elem: lvalue) {
                elems.add(valueToStr(elem));
            }
            return "[" + StringUtils.join(elems, ", ") + "]";
        } else if (value instanceof ToolDefSet<?>) {
            ToolDefSet<?> svalue = (ToolDefSet<?>)value;
            List<String> elems = listc(svalue.size());
            for (Object elem: svalue) {
                elems.add(valueToStr(elem));
            }
            return "{" + StringUtils.join(elems, ", ") + "}";
        } else if (value instanceof ToolDefMap<?, ?>) {
            ToolDefMap<?, ?> mvalue = (ToolDefMap<?, ?>)value;
            List<String> entries = listc(mvalue.size());
            for (Entry<?, ?> entry: mvalue.entrySet()) {
                entries.add(valueToStr(entry.getKey()) + ": " + valueToStr(entry.getValue()));
            }
            return "{" + StringUtils.join(entries, ", ") + "}";
        } else {
            throw new RuntimeException("Unknown runtime value: " + value);
        }
    }

    /**
     * Converts the given runtime value into a textual representation of the type of the value, closely resembling the
     * ToolDef ASCII syntax.
     *
     * @param value The runtime value.
     * @return The textual representation of the type of the value.
     */
    public static String valueToTypeStr(Object value) {
        return ToolDefTextUtils.typeToStr(valueToType(value));
    }

    /**
     * Converts the given runtime value into a ToolDef type.
     *
     * @param value The runtime value.
     * @return A ToolDef type.
     */
    public static ToolDefType valueToType(Object value) {
        // Null value. Can't derive a more specific type.
        if (value == null) {
            return newObjectType(true, null);
        }

        // Simple values.
        if (value instanceof Boolean) {
            return newBoolType(false, null);
        } else if (value instanceof Integer) {
            return newIntType(false, null);
        } else if (value instanceof Long) {
            return newLongType(false, null);
        } else if (value instanceof Double) {
            return newDoubleType(false, null);
        } else if (value instanceof String) {
            return newStringType(false, null);
        }

        // Container values.
        if (value instanceof ToolDefTupleNary) {
            ToolDefTupleNary<?, ?> naryValue = (ToolDefTupleNary<?, ?>)value;

            List<ToolDefType> fieldTypes = list();
            fieldTypes.add(valueToType(naryValue.prefix));

            ToolDefTuple cur = naryValue.remainder;
            while (cur instanceof ToolDefTupleNary) {
                ToolDefTupleNary<?, ?> nary = (ToolDefTupleNary<?, ?>)cur;
                fieldTypes.add(valueToType(nary.prefix));
                cur = nary.remainder;
            }

            Assert.check(cur instanceof ToolDefTuplePair<?, ?>);
            ToolDefTuplePair<?, ?> pair = (ToolDefTuplePair<?, ?>)cur;
            fieldTypes.add(valueToType(pair.left));
            fieldTypes.add(valueToType(pair.right));

            return newTupleType(fieldTypes, false, null);
        } else if (value instanceof ToolDefTuplePair) {
            ToolDefTuplePair<?, ?> pair = (ToolDefTuplePair<?, ?>)value;
            List<ToolDefType> fieldTypes = list();
            fieldTypes.add(valueToType(pair.left));
            fieldTypes.add(valueToType(pair.right));
            return newTupleType(fieldTypes, false, null);
        } else if (value instanceof ToolDefList<?>) {
            ToolDefList<?> lvalue = (ToolDefList<?>)value;
            ToolDefType mergedType = null;
            for (Object elem: lvalue) {
                ToolDefType elemType = valueToType(elem);
                mergedType = (mergedType == null) ? elemType : ToolDefTypeUtils.mergeTypes(mergedType, elemType);
            }
            if (mergedType == null) {
                mergedType = newObjectType(true, null);
            }
            return newListType(mergedType, false, null);
        } else if (value instanceof ToolDefSet<?>) {
            ToolDefSet<?> svalue = (ToolDefSet<?>)value;
            ToolDefType mergedType = null;
            for (Object elem: svalue) {
                ToolDefType elemType = valueToType(elem);
                mergedType = (mergedType == null) ? elemType : ToolDefTypeUtils.mergeTypes(mergedType, elemType);
            }
            if (mergedType == null) {
                mergedType = newObjectType(true, null);
            }
            return newSetType(mergedType, false, null);
        } else if (value instanceof ToolDefMap<?, ?>) {
            ToolDefMap<?, ?> mvalue = (ToolDefMap<?, ?>)value;
            ToolDefType mergedKeyType = null;
            ToolDefType mergedValueType = null;
            for (Entry<?, ?> entry: mvalue.entrySet()) {
                ToolDefType keyType = valueToType(entry.getKey());
                ToolDefType valueType = valueToType(entry.getValue());
                mergedKeyType = (mergedKeyType == null) ? keyType : ToolDefTypeUtils.mergeTypes(mergedKeyType, keyType);
                mergedValueType = (mergedValueType == null) ? valueType
                        : ToolDefTypeUtils.mergeTypes(mergedValueType, valueType);
            }
            if (mergedKeyType == null) {
                mergedKeyType = newObjectType(true, null);
            }
            if (mergedValueType == null) {
                mergedValueType = newObjectType(true, null);
            }
            return newMapType(mergedKeyType, false, null, mergedValueType);
        } else {
            throw new RuntimeException("Unknown runtime value: " + value);
        }
    }

    /**
     * Creates a tuple value from the given element values.
     *
     * @param values The element values. Must be at least two values.
     * @return The tuple value.
     */
    public static ToolDefTuple makeTuple(List<Object> values) {
        // Precondition check.
        Assert.check(values.size() >= 2);

        // First pair.
        Object v1 = values.get(values.size() - 2);
        Object v2 = values.get(values.size() - 1);

        // Additional n-ary wrappers for the remaining values.
        ToolDefTuple tuple = new ToolDefTuplePair<>(v1, v2);
        for (int i = values.size() - 3; i >= 0; i--) {
            tuple = new ToolDefTupleNary<>(values.get(i), tuple);
        }
        return tuple;
    }

    /**
     * Unpacks a ToolDef runtime tuple representation to the element values.
     *
     * @param tuple A ToolDef runtime tuple.
     * @return The element values of the tuple.
     */
    public static List<Object> unpackTuple(ToolDefTuple tuple) {
        List<Object> rslt = new ToolDefList<>();

        ToolDefTuple cur = tuple;
        while (cur instanceof ToolDefTupleNary) {
            ToolDefTupleNary<?, ?> nary = (ToolDefTupleNary<?, ?>)cur;
            rslt.add(nary.prefix);
            cur = nary.remainder;
        }

        Assert.check(cur instanceof ToolDefTuplePair);
        ToolDefTuplePair<?, ?> pair = (ToolDefTuplePair<?, ?>)cur;
        rslt.add(pair.left);
        rslt.add(pair.right);

        return rslt;
    }
}
