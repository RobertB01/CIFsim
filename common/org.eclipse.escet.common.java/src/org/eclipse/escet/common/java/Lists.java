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

package org.eclipse.escet.common.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/** {@link List}s helper methods. */
public final class Lists {
    /** Constructor for the {@link Lists} class. */
    private Lists() {
        // Static class.
    }

    /**
     * Adds an element to the list (in-place), and returns the list, to allow for chaining.
     *
     * @param <T> The type of the elements of the list.
     * @param lst The input list, to be modified in-place.
     * @param elem The element to add to the list.
     * @return The input list.
     */
    public static <T> List<T> add(List<T> lst, T elem) {
        lst.add(elem);
        return lst;
    }

    /**
     * Filter a list of elements to only include the elements of a given type. Elements not of the given type are not
     * included in the result.
     *
     * @param <TI> The type of the elements of the input list.
     * @param <TR> The type of the elements of the resulting list.
     * @param lst The list to filter.
     * @param rsltType The class of 'TR'.
     * @return The filtered list.
     */
    @SuppressWarnings("unchecked")
    public static <TI, TR extends TI> List<TR> filter(List<TI> lst, Class<TR> rsltType) {
        List<TR> rslt = list();
        for (TI elem: lst) {
            if (rsltType.isInstance(elem)) {
                rslt.add((TR)elem);
            }
        }
        return rslt;
    }

    /**
     * Creates and returns a shallow copy of a list.
     *
     * @param <TI> The type of the elements of the input list.
     * @param <TR> The type of the elements of the copied list.
     * @param lst The list to copy.
     * @return The shallow copy of the list.
     */
    public static <TR, TI extends TR> List<TR> copy(List<TI> lst) {
        List<TR> rslt = listc(lst.size());
        for (TI elem: lst) {
            rslt.add(elem);
        }
        return rslt;
    }

    /**
     * Returns the given list, but helps Java to convince a {@code List<Integer>} fits within a {@code List<Object>},
     * eliminating the need for a cast and warning suppression.
     *
     * @param <TI> The type of the elements of the given list.
     * @param <TR> The type of the elements of the returned list.
     * @param lst The list.
     * @return The given list.
     */
    @SuppressWarnings("unchecked")
    public static <TR, TI extends TR> List<TR> cast(List<TI> lst) {
        return (List<TR>)lst;
    }

    /**
     * Returns the concatenation of several lists.
     *
     * @param <T> The type of the elements of the resulting list.
     * @param lsts The lists.
     * @return The concatenated lists.
     */
    @SafeVarargs
    public static <T> List<T> concat(List<? extends T>... lsts) {
        int cnt = 0;
        for (List<? extends T> lst: lsts) {
            cnt += lst.size();
        }

        List<T> rslt = listc(cnt);
        for (List<? extends T> lst: lsts) {
            rslt.addAll(lst);
        }
        return rslt;
    }

    /**
     * Returns the concatenation of two lists.
     *
     * @param <TR> The type of the elements of the resulting list.
     * @param <T1> The type of the elements of the first list.
     * @param <T2> The type of the elements of the second list.
     * @param l1 The first list.
     * @param l2 The second list.
     * @return The concatenated list.
     */
    public static <TR, T1 extends TR, T2 extends TR> List<TR> concat(List<T1> l1, List<T2> l2) {
        List<TR> rslt = listc(l1.size() + l2.size());
        rslt.addAll(l1);
        rslt.addAll(l2);
        return rslt;
    }

    /**
     * Returns the concatenation of a single element with a list.
     *
     * @param <TR> The type of the elements of the resulting list.
     * @param <TE> The type of the single element.
     * @param <TL> The type of the input list.
     * @param elem The element.
     * @param lst The list.
     * @return A new list, consisting of the original list with the element put in from of it.
     */
    public static <TR, TE extends TR, TL extends TR> List<TR> concat(TE elem, List<TL> lst) {
        List<TR> rslt = listc(lst.size() + 1);
        rslt.add(elem);
        rslt.addAll(lst);
        return rslt;
    }

    /**
     * Returns the concatenation of a list with a single element.
     *
     * @param <TR> The type of the elements of the resulting list.
     * @param <TE> The type of the single element.
     * @param <TL> The type of the input list.
     * @param lst The list.
     * @param elem The element.
     * @return A new list, consisting of the original list with the element put after it.
     */
    public static <TR, TE extends TR, TL extends TR> List<TR> concat(List<TL> lst, TE elem) {
        List<TR> rslt = listc(lst.size() + 1);
        rslt.addAll(lst);
        rslt.add(elem);
        return rslt;
    }

    /**
     * Creates an {@link ArrayList} with the given elements.
     *
     * @param <TR> The type of the elements of the resulting list.
     * @param <TI> The type of the input elements.
     * @param elems The elements of the new list.
     * @return The newly created {@link ArrayList}.
     */
    @SafeVarargs
    public static <TR, TI extends TR> List<TR> list(TI... elems) {
        List<TR> rslt = listc(elems.length);
        for (TI elem: elems) {
            rslt.add(elem);
        }
        return rslt;
    }

    /**
     * Creates an {@link ArrayList} with the given element.
     *
     * @param <TR> The type of the elements of the resulting list.
     * @param <TI> The type of the input element.
     * @param elem The element of the new list.
     * @return The newly created {@link ArrayList}.
     */
    public static <TR, TI extends TR> List<TR> list(TI elem) {
        List<TR> rslt = listc(1);
        rslt.add(elem);
        return rslt;
    }

    /**
     * Creates an empty {@link ArrayList}.
     *
     * @param <T> The type of the elements of the resulting list.
     * @return The newly created {@link ArrayList}.
     */
    public static <T> List<T> list() {
        return new ArrayList<>();
    }

    /**
     * Creates an empty {@link ArrayList} with the given initial capacity.
     *
     * @param <T> The type of the elements of the list.
     * @param initialCapacity The initial capacity of the list.
     * @return The newly created {@link ArrayList}.
     * @throws IllegalArgumentException If the initial capacity is negative.
     */
    public static <T> List<T> listc(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

    /**
     * Get the last element of a list.
     *
     * @param <T> The type of the elements of the list.
     * @param lst The list.
     * @return The last element of the given list.
     * @throws IndexOutOfBoundsException If the list is empty.
     */
    public static <T> T last(List<T> lst) {
        return lst.get(lst.size() - 1);
    }

    /**
     * Get the first element of a list.
     *
     * @param <T> The type of the elements of the list.
     * @param lst The list.
     * @return The first element of the given list.
     * @throws IndexOutOfBoundsException If the list is empty.
     */
    public static <T> T first(List<T> lst) {
        return lst.get(0);
    }

    /**
     * Determines whether two lists are shifted copies of each other.
     *
     * @param <T> The type of the elements of the list.
     * @param lst1 The first list.
     * @param lst2 The second list.
     * @return {@code true} if the lists are shifted copies, {@code false} otherwise.
     */
    public static <T> boolean areEqualOrShifted(List<T> lst1, List<T> lst2) {
        if (lst1.size() != lst2.size()) {
            return false;
        }
        if (lst1 == lst2 || lst1.equals(lst2)) {
            return true;
        }

        // For all offsets i in lst1, compare the second part of lst1 with the first part of lst2, and
        // compare the first part of lst1 with the second part of lst2.
        int n = lst1.size();
        for (int i = 1; i < n; i++) {
            if (!lst1.subList(i, n).equals(lst2.subList(0, n - i))) {
                continue;
            }
            if (lst1.subList(0, i).equals(lst2.subList(n - i, n))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a reversed, shallow copy of a list.
     *
     * @param <TR> The type of the elements of the resulting list.
     * @param <TI> The type of the elements of the input list.
     * @param lst The list.
     * @return A reversed, shallow copy of the list.
     */
    public static <TR, TI extends TR> List<TR> reverse(List<TI> lst) {
        List<TR> rslt = copy(lst);
        Collections.reverse(rslt);
        return rslt;
    }

    /**
     * Constructs a list from the elements of the given set. That is, it converts a set to a list.
     *
     * @param <TL> The type of the elements of the list.
     * @param <TS> The type of the elements of the set.
     * @param set The set to convert.
     * @return The list constructed from the elements of the set.
     */
    public static <TL, TS extends TL> List<TL> set2list(Set<TS> set) {
        return new ArrayList<>(set);
    }

    /**
     * Slices the given {@link List}. A slice is a sub-list of the original (input) list. The begin and end indices can
     * be thought of as the range of elements that should be maintained. That is, in principle, the range of element
     * indexes {@code [beginIndex .. endIndex - 1]} is maintained.
     *
     * <p>
     * Note that:
     * <ul>
     * <li>{@code slice(x, null, 2) + slice(x, 2, null) == x}, for any {@code x}.</li>
     * <li>Out of range slice indices are handled gracefully. An index that is too large is replaced by the list size. A
     * lower bound larger than the upper bound results in an empty list. This applies to negative indices as well.</li>
     * <li>Indices may be negative numbers, to start counting from the right instead of from the left. That is,
     * {@code -1} can be used instead of {@code lst.length() - 1}, {@code -2} can be used instead of
     * {@code lst.length() - 2}, etc. However, since {@code -0} is really the same as {@code 0}, it does not count from
     * the right.</li>
     * <li>Similarly to the {@link List#subList} method, this method returns a view on the original list. That is, any
     * changes to the slice result in changes to the original list.</li>
     * </ul>
     * </p>
     *
     * @param <T> The type of the elements of the list.
     * @param lst The list to slice.
     * @param beginIndex The 0-based begin index (inclusive), or {@code null} for index {@code 0}.
     * @param endIndex The 0-based end index (exclusive), or {@code null} for the length of the input list.
     * @return The slice.
     */
    public static <T> List<T> slice(List<T> lst, Integer beginIndex, Integer endIndex) {
        // Get length once.
        int len = lst.size();

        // Replace 'null' by defaults.
        int b = (beginIndex == null) ? 0 : beginIndex;
        int e = (endIndex == null) ? len : endIndex;

        // Handle negative indices.
        if (b < 0) {
            b = len + b;
        }
        if (e < 0) {
            e = len + e;
        }

        // Handle out of range and empty interval.
        if (b < 0) {
            b = 0;
        }
        if (e < 0) {
            e = 0;
        }

        if (b > len) {
            b = len;
        }
        if (e > len) {
            e = len;
        }

        if (b > e) {
            b = e;
        }

        // Using normal List.subList method to get the view.
        return lst.subList(b, e);
    }
}
