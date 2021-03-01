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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Pair.pair;

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.PairTextComparer;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF sort utility methods. */
public class CifSortUtils {
    /** Constructor for the {@link CifSortUtils} class. */
    private CifSortUtils() {
        // Static class.
    }

    /**
     * Sorts the given CIF objects in ascending order based on their absolute names (without escaping).
     *
     * <p>
     * Uses {@link Strings#SORTER} to compare the texts.
     * </p>
     *
     * @param <T> The type of CIF objects to sort.
     * @param objs The objects to sort. Is modified in-place.
     */
    public static <T extends PositionObject> void sortCifObjects(List<T> objs) {
        // Add names.
        List<Pair<String, T>> namedObjs = listc(objs.size());
        for (T obj: objs) {
            namedObjs.add(pair(CifTextUtils.getAbsName(obj, false), obj));
        }

        // Sort.
        Collections.sort(namedObjs, new PairTextComparer<T>());

        // Update objects in-place to sorted order.
        for (int i = 0; i < objs.size(); i++) {
            objs.set(i, namedObjs.get(i).right);
        }
    }
}
