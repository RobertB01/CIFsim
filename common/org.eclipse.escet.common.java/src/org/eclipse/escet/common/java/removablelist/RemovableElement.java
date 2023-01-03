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

package org.eclipse.escet.common.java.removablelist;

import org.eclipse.escet.common.java.Assert;

/**
 * Base class of elements that can be put into {@link RemovableList}.
 *
 * @param <T> Concrete type of the list element.
 */
public class RemovableElement<T extends RemovableElement<T>> {
    /** Index in the {@link RemovableList}, or {@code -1} if not stored in a list. */
    private int listIndex = -1;

    /**
     * List containing the element, only valid if the {@link #listIndex} is non-negative. Should be {@code null}
     * otherwise.
     */
    private RemovableList<T> removableList = null;

    /**
     * Get the list index of the element.
     *
     * @return Index in the list.
     */
    public int getListIndex() {
        return listIndex;
    }

    /**
     * Set the index of the element in the removable list (used by the removable list when it moves the element).
     *
     * @param num New index in the list.
     */
    void setIndex(int num) {
        listIndex = num;
    }

    /**
     * Set the list and index of the element.
     *
     * @param lst The list containing the element.
     * @param idx Index position of the element in the list.
     */
    void setListIndex(RemovableList<T> lst, int idx) {
        Assert.check(listIndex < 0);
        removableList = lst;
        listIndex = idx;
    }

    /** Clear the containing list information. */
    void clearListIndex() {
        listIndex = -1;
        removableList = null;
    }

    /** Remove the element from the removable list it is in. */
    public void remove() {
        RemovableElement<T> elm = removableList.elementRemove(listIndex);
        Assert.check(elm == this);
        clearListIndex();
    }
}
