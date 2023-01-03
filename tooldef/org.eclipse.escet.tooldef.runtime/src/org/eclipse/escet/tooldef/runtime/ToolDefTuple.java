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

package org.eclipse.escet.tooldef.runtime;

import java.util.List;

/** ToolDef tuple value. */
public abstract class ToolDefTuple {
    /**
     * Returns the size of the tuple, as the number of elements.
     *
     * @return The size of the tuple.
     */
    public abstract int size();

    /**
     * Returns the values of all the elements.
     *
     * @return The values of all the elements.
     */
    public List<Object> getValues() {
        ToolDefList<Object> values = new ToolDefList<>(size());
        collectValues(values);
        return values;
    }

    /**
     * Collects the values of all the elements.
     *
     * @param values The values collected so far. Is extended in-place.
     */
    protected abstract void collectValues(List<Object> values);

    /**
     * Returns the value of the element with the given 0-based index.
     *
     * @param idx The 0-based index of the element for which to return the value.
     * @return The value.
     */
    public abstract Object getValue(int idx);

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
