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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/** {@link Set}s helper methods. */
public class Sets {
    /** Constructor for the {@link Sets} class. */
    private Sets() {
        // Static class.
    }

    /**
     * Adds an element to the set (in-place), and returns the set, to allow for chaining.
     *
     * @param <T> The type of the elements of the set.
     * @param set The input set, to be modified in-place.
     * @param elem The element to add to the set.
     * @return The input set.
     */
    public static <T> Set<T> add(Set<T> set, T elem) {
        set.add(elem);
        return set;
    }

    /**
     * Filter a set of elements to only include the elements of a given type. Elements not of the given type are not
     * included in the result.
     *
     * @param <TI> The type of the elements of the input set.
     * @param <TR> The type of the elements of the resulting set.
     * @param set The set to filter.
     * @param rsltType The class of 'TR'.
     * @return The filtered set.
     */
    @SuppressWarnings("unchecked")
    public static <TI, TR extends TI> Set<TR> filter(Set<TI> set, Class<TR> rsltType) {
        Set<TR> rslt = set();
        for (TI elem: set) {
            if (rsltType.isInstance(elem)) {
                rslt.add((TR)elem);
            }
        }
        return rslt;
    }

    /**
     * Returns the union of two sets.
     *
     * @param <T1> The type of the elements of the first set.
     * @param <T2> The type of the elements of the second set.
     * @param <TR> The type of the elements of the resulting set.
     * @param set1 The first set.
     * @param set2 The second set.
     * @return The union of the two sets.
     */
    public static <TR, T1 extends TR, T2 extends TR> Set<TR> union(Set<T1> set1, Set<T2> set2) {
        Set<TR> rslt = set();
        rslt.addAll(set1);
        rslt.addAll(set2);
        return rslt;
    }

    /**
     * Returns the union of sets.
     *
     * @param <TR> The type of the elements of the resulting set.
     * @param <TI> The type of the elements of the input sets.
     * @param sets The sets.
     * @return The union of the sets.
     */
    @SafeVarargs
    public static <TR, TI extends TR> Set<TR> union(Set<? extends TI>... sets) {
        Set<TR> rslt = set();
        for (Set<? extends TI> set: sets) {
            rslt.addAll(set);
        }
        return rslt;
    }

    /**
     * Check whether the intersection of two sets is empty.
     *
     * @param <T> The type of the elements of the sets.
     * @param set1 The first set.
     * @param set2 The second set.
     * @return Whether both sets have any common element.
     */
    public static <T> boolean isEmptyIntersection(Set<? extends T> set1, Set<? extends T> set2) {
        if (set1.isEmpty() || set2.isEmpty()) {
            return true;
        }
        if (set1.size() < set2.size()) {
            for (T s: set1) {
                if (set2.contains(s)) {
                    return false;
                }
            }
        } else {
            for (T s: set2) {
                if (set1.contains(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the intersection of two sets.
     *
     * @param <T1> The type of the elements of the first set.
     * @param <T2> The type of the elements of the second set.
     * @param <TR> The type of the elements of the resulting set.
     * @param set1 The first set.
     * @param set2 The second set.
     * @return The intersection of the two sets.
     */
    public static <TR, T1 extends TR, T2 extends TR> Set<TR> intersection(Set<T1> set1, Set<T2> set2) {
        Set<TR> rslt = copy(set1);
        rslt.retainAll(set2);
        return rslt;
    }

    /**
     * Returns the intersection of sets.
     *
     * @param <TR> The type of the elements of the resulting set.
     * @param <TI> The type of the elements of the input sets.
     * @param set The first set.
     * @param sets The remaining sets.
     * @return The intersection of the sets.
     */
    @SafeVarargs
    public static <TR, TI extends TR> Set<TR> intersection(Set<? extends TI> set, Set<? extends TI>... sets) {
        Set<TR> rslt = copy(set);
        for (Set<? extends TI> s: sets) {
            rslt.retainAll(s);
        }
        return rslt;
    }

    /**
     * Returns the difference of two sets.
     *
     * @param <T1> The type of the elements of the first set.
     * @param <T2> The type of the elements of the second set.
     * @param <TR> The type of the elements of the resulting set.
     * @param set1 The first set.
     * @param set2 The second set.
     * @return The difference of the two sets.
     */
    public static <TR, T1 extends TR, T2 extends TR> Set<TR> difference(Set<T1> set1, Set<T2> set2) {
        Set<TR> rslt = copy(set1);
        rslt.removeAll(set2);
        return rslt;
    }

    /**
     * Returns the difference of the first set with the sets, that is: 'set \ sets[0] \ sets[1] \ ... \ sets[n]'.
     *
     * @param <TR> The type of the elements of the resulting set.
     * @param <TI> The type of the elements of the input sets.
     * @param set The first set.
     * @param sets The rest of the sets.
     * @return The difference of the first set with the rest.
     */
    @SafeVarargs
    public static <TR, TI extends TR> Set<TR> difference(Set<? extends TI> set, Set<? extends TI>... sets) {
        Set<TR> rslt = copy(set);
        for (Set<? extends TI> s: sets) {
            rslt.removeAll(s);
        }
        return rslt;
    }

    /**
     * Returns a shallow copy of the given set.
     *
     * @param <TR> The type of the elements of the resulting set.
     * @param <TI> The type of the elements of the input set.
     * @param s The set to copy.
     *
     * @return A shallow copy of the given set.
     */
    public static <TR, TI extends TR> Set<TR> copy(Set<TI> s) {
        return new LinkedHashSet<>(s);
    }

    /**
     * Returns the given set, but helps Java to convince a {@code Set<Integer>} fits within a {@code Set<Object>},
     * eliminating the need for a cast and warning suppression.
     *
     * @param <TI> The type of the elements of the given set.
     * @param <TR> The type of the elements of the returned set.
     * @param lst The set.
     * @return The given set.
     */
    @SuppressWarnings("unchecked")
    public static <TR, TI extends TR> Set<TR> cast(Set<TI> lst) {
        return (Set<TR>)lst;
    }

    /**
     * Returns a deterministic, sorted string representation of a set.
     *
     * @param s The set to get a deterministic, sorted string representation for.
     * @return A deterministic, sorted string representation of a set.
     */
    public static String toString(Set<?> s) {
        List<String> l = listc(s.size());
        for (Object o: s) {
            l.add(o.toString());
        }
        Collections.sort(l, Strings.SORTER);
        return fmt("{%s}", StringUtils.join(l, ", "));
    }

    /**
     * Creates a {@link LinkedHashSet} with the given elements.
     *
     * @param <TR> The type of the elements of the resulting set.
     * @param <TI> The type of the input elements.
     * @param elems The elements of the new set.
     * @return The newly created {@link LinkedHashSet}.
     */
    @SafeVarargs
    public static <TR, TI extends TR> Set<TR> set(TI... elems) {
        Set<TR> rslt = setc(elems.length);
        for (TI elem: elems) {
            rslt.add(elem);
        }
        return rslt;
    }

    /**
     * Creates a {@link LinkedHashSet} with the given element.
     *
     * @param <TR> The type of the elements of the resulting set.
     * @param <TI> The type of the input element.
     * @param elem The element of the new set.
     * @return The newly created {@link LinkedHashSet}.
     */
    public static <TR, TI extends TR> Set<TR> set(TI elem) {
        Set<TR> rslt = setc(1);
        rslt.add(elem);
        return rslt;
    }

    /**
     * Creates an empty {@link LinkedHashSet}.
     *
     * @param <T> The type of the elements of the resulting set.
     * @return The newly created {@link LinkedHashSet}.
     */
    public static <T> Set<T> set() {
        return new LinkedHashSet<>();
    }

    /**
     * Creates an empty {@link LinkedHashSet} with the given initial capacity.
     *
     * @param <T> The type of the elements of the set.
     * @param initialCapacity The initial capacity of the set.
     * @return The newly created {@link LinkedHashSet}.
     * @throws IllegalArgumentException If the initial capacity is negative.
     */
    public static <T> Set<T> setc(int initialCapacity) {
        return new LinkedHashSet<>(initialCapacity);
    }

    /**
     * Constructs a set from the elements of the given list. That is, it converts a list to a set.
     *
     * @param <TS> The type of the elements of the set.
     * @param <TL> The type of the elements of the list.
     * @param list The list to convert.
     * @return The set constructed from the elements of the list.
     */
    public static <TS, TL extends TS> Set<TS> list2set(List<TL> list) {
        return new LinkedHashSet<>(list);
    }

    /**
     * Constructs a sorted list from the elements of the given set. That is, it converts a set to a list, and sorts that
     * list in-place.
     *
     * <p>
     * For sets of strings, use {@link #sortedstrings} instead.
     * </p>
     *
     * @param <TL> The type of the elements of the list.
     * @param <TS> The type of the elements of the set.
     * @param set The set to sort.
     * @return The sorted list constructed from the elements of the set.
     */
    public static <TL extends Comparable<? super TL>, TS extends TL> List<TL> sortedgeneric(Set<TS> set) {
        List<TL> lst = set2list(set);
        Collections.sort(lst);
        return lst;
    }

    /**
     * Constructs a sorted list from the elements of the given set. That is, it converts a set to a list, and sorts that
     * list in-place.
     *
     * <p>
     * For sets of strings, use {@link #sortedstrings} instead.
     * </p>
     *
     * @param <TL> The type of the elements of the list.
     * @param <TS> The type of the elements of the set.
     * @param set The set to sort.
     * @param cmp The comparator to use to determine the order, or {@code null} to use the natural/default order.
     * @return The sorted list constructed from the elements of the set.
     */
    public static <TL, TS extends TL> List<TL> sortedgeneric(Set<TS> set, Comparator<? super TL> cmp) {
        List<TL> lst = set2list(set);
        Collections.sort(lst, cmp);
        return lst;
    }

    /**
     * Constructs a sorted list from the strings of the given set. That is, it converts a set of strings to a list of
     * strings, and sorts that list in-place. Uses {@link Strings#SORTER smart string compare}.
     *
     * @param set The set to sort.
     * @return The sorted list of strings constructed from the set of strings.
     * @see Strings#SORTER
     */
    public static List<String> sortedstrings(Set<String> set) {
        return sortedgeneric(set, Strings.SORTER);
    }
}
