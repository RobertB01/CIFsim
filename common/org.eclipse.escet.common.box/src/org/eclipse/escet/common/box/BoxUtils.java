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

package org.eclipse.escet.common.box;

import java.util.List;

/** Utility methods for boxing. */
public class BoxUtils {
    /** Constructor for the {@link BoxUtils} class. */
    private BoxUtils() {
        // Private constructor to turn class into static class.
    }

    /**
     * Joins boxed elements together into an {@link HBox}, using the given {@code separator}.
     *
     * @param <T> The type of boxes to use as elements.
     * @param elements The elements to join.
     * @param separator The separator to use.
     * @return The {@link HBox} with the joined elements.
     */
    public static <T extends Box> HBox join(List<T> elements, String separator) {
        HBox rslt = new HBox(separator);
        for (Box element: elements) {
            rslt.add(element);
        }
        return rslt;
    }

    /**
     * Joins boxed elements together into an {@link HBox}, using the given {@code prefix}, {@code separator}, and
     * {@code postfix}.
     *
     * @param <T> The type of boxes to use as elements.
     * @param elements The elements to join.
     * @param prefix The prefix to use. Must not contain new line characters.
     * @param separator The separator to use. Must not contain new line characters.
     * @param postfix The postfix to use. Must not contain new line characters.
     * @return The {@link HBox} with the joined elements.
     */
    public static <T extends Box> HBox join(List<T> elements, String prefix, String separator, String postfix) {
        HBox rslt = new HBox();
        rslt.add(prefix);
        boolean first = true;
        for (Box element: elements) {
            if (!first) {
                rslt.add(separator);
            }
            first = false;
            rslt.add(element);
        }
        rslt.add(postfix);
        return rslt;
    }

    /**
     * Boxes elements and joins them together into an {@link HBox}, using the given {@code separator}.
     *
     * @param <T> The type of elements to box.
     * @param boxer The {@link Boxer boxer} to use to box the elements.
     * @param elements The elements to join.
     * @param separator The separator to use.
     * @return The {@link HBox} with the joined elements.
     */
    public static <T> HBox boxAndJoin(Boxer<T> boxer, List<T> elements, String separator) {
        HBox rslt = new HBox(separator);
        for (T element: elements) {
            rslt.add(boxer.box(element));
        }
        return rslt;
    }

    /**
     * Boxes elements and joins them together into an {@link HBox}, using the given {@code prefix}, {@code separator},
     * and {@code postfix}.
     *
     * @param <T> The type of elements to box.
     * @param boxer The {@link Boxer boxer} to use to box the elements.
     * @param elements The elements to join.
     * @param prefix The prefix to use. Must not contain new line characters.
     * @param separator The separator to use. Must not contain new line characters.
     * @param postfix The postfix to use. Must not contain new line characters.
     * @return The {@link HBox} with the joined elements.
     */
    public static <T> HBox boxAndJoin(Boxer<T> boxer, List<T> elements, String prefix, String separator,
            String postfix)
    {
        HBox rslt = new HBox();
        rslt.add(prefix);
        boolean first = true;
        for (T element: elements) {
            if (!first) {
                rslt.add(separator);
            }
            first = false;
            rslt.add(boxer.box(element));
        }
        rslt.add(postfix);
        return rslt;
    }
}
