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

import java.util.Comparator;

/**
 * Comparator that can compare pairs of text and arbitrary values, based on those texts, regardless of the type of the
 * values.
 *
 * <p>
 * A typical use case is to sort some values based on their textual representations. First, pairs of the textual
 * representations and the original values are created. Then, the pairs are sorted using this comparator. Then, the
 * values are extracted from the sorted pairs.
 * </p>
 *
 * <p>
 * Uses {@link Strings#SORTER} to compare the texts.
 * </p>
 *
 * @param <T> The type of the values.
 */
public class PairTextComparer<T> implements Comparator<Pair<String, T>> {
    @Override
    public int compare(Pair<String, T> t1, Pair<String, T> t2) {
        return Strings.SORTER.compare(t1.left, t2.left);
    }
}
