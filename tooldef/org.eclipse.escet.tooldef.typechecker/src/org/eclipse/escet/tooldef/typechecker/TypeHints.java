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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.common.ToolDefTypeEqWrap;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/**
 * The type hints from the environment of an expression, i.e. the expected or allowed types of the expression. They can
 * be ignored if they don't fit.
 *
 * <p>
 * This class automatically removes {@link ToolDefTypeUtils#areEqualTypes duplicate} type hints, and stores the type
 * hints in {@link ToolDefTypeUtils#normalizeType normalized} and {@link ToolDefTypeUtils#compareTypes ordered} form.
 * </p>
 */
public class TypeHints implements Iterable<ToolDefType> {
    /** Singleton instance of {@link TypeHints}, without hints. Used for performance. Should not be modified. */
    public static final TypeHints NO_HINTS = new TypeHints();

    /** The type hints. */
    private TreeSet<ToolDefTypeEqWrap> hints = new TreeSet<>();

    /**
     * Adds a type hint.
     *
     * @param hint The type hint to add.
     * @return This {@link TypeHints} instance, to allow chaining {@link #add} calls.
     */
    public TypeHints add(ToolDefType hint) {
        hint = ToolDefTypeUtils.normalizeType(hint);
        hints.add(new ToolDefTypeEqWrap(hint));
        return this;
    }

    @Override
    public Iterator<ToolDefType> iterator() {
        return new TypeHintsIterator();
    }

    /** Type hints iterator. */
    private class TypeHintsIterator implements Iterator<ToolDefType> {
        /** The {@link TreeSet} iterator. */
        Iterator<ToolDefTypeEqWrap> iterator;

        /** Constructor for the {@link TypeHintsIterator} class. */
        public TypeHintsIterator() {
            iterator = hints.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public ToolDefType next() {
            return iterator.next().type;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        List<String> texts = listc(hints.size());
        for (ToolDefType hint: this) {
            texts.add(ToolDefTextUtils.typeToStr(hint, false));
        }
        return fmt("%s(%s)", getClass().getSimpleName(), String.join(", ", texts));
    }
}
