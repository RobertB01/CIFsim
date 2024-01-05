//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.common.java.Assert;

/**
 * ToolDef n-tuple.
 *
 * @param <P> The type of the prefix value (before the remainder).
 * @param <R> The type of the remainder.
 */
public class ToolDefTupleNary<P, R extends ToolDefTuple> extends ToolDefTuple {
    /** The prefix value. May be {@code null}. */
    public final P prefix;

    /** The remainder. May not be {@code null}. */
    public final R remainder;

    /**
     * Constructor for the {@link ToolDefTupleNary} class.
     *
     * @param prefix The prefix value. May be {@code null}.
     * @param remainder The remainder. May not be {@code null}.
     */
    public ToolDefTupleNary(P prefix, R remainder) {
        Assert.notNull(remainder);
        this.prefix = prefix;
        this.remainder = remainder;
    }

    @Override
    public int size() {
        return 1 + remainder.size();
    }

    @Override
    protected void collectValues(List<Object> values) {
        values.add(prefix);
        remainder.collectValues(values);
    }

    @Override
    public Object getValue(int idx) {
        if (idx == 0) {
            return prefix;
        }
        return remainder.getValue(idx - 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ToolDefTupleNary<?, ?>)) {
            return false;
        }
        ToolDefTupleNary<?, ?> other = (ToolDefTupleNary<?, ?>)obj;
        return ToolDefRuntimeUtils.equalValues(this.prefix, other.prefix)
                && ToolDefRuntimeUtils.equalValues(this.remainder, other.remainder);
    }

    @Override
    public int hashCode() {
        return ToolDefTupleNary.class.hashCode() ^ ToolDefRuntimeUtils.hashValue(prefix)
                ^ ToolDefRuntimeUtils.hashValue(remainder);
    }

    @Override
    public String toString() {
        List<String> txts = list();
        txts.add(ToolDefRuntimeUtils.valueToStr(prefix));

        ToolDefTuple cur = remainder;
        while (cur instanceof ToolDefTupleNary) {
            ToolDefTupleNary<?, ?> nary = (ToolDefTupleNary<?, ?>)cur;
            txts.add(ToolDefRuntimeUtils.valueToStr(nary.prefix));
            cur = nary.remainder;
        }

        Assert.check(cur instanceof ToolDefTuplePair<?, ?>);
        ToolDefTuplePair<?, ?> pair = (ToolDefTuplePair<?, ?>)cur;
        txts.add(ToolDefRuntimeUtils.valueToStr(pair.left));
        txts.add(ToolDefRuntimeUtils.valueToStr(pair.right));

        return "(" + String.join(", ", txts) + ")";
    }
}
