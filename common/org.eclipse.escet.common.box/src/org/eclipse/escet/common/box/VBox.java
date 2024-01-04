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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** Box that combines sub-boxes vertically. */
public class VBox extends Box {
    /** Sub-boxes. */
    private final List<Box> subBoxes;

    /** Indentation level in number of spaces. */
    private final int indent;

    /** Constructor for the {@link VBox} class. Constructs a vertical box, without any indentation. */
    public VBox() {
        this(0);
    }

    /**
     * Constructor for the {@link VBox} class. Constructs a vertical box, with the given amount of indentation.
     *
     * @param indent Indentation level in number of spaces.
     */
    public VBox(int indent) {
        this.subBoxes = list();
        this.indent = indent;
    }

    /**
     * Constructor for the {@link VBox} class. No indentation is used.
     *
     * @param items The items that make up the box. At least one item is required. If an item is a {@link Box}, it is
     *     added directly. Otherwise, {@link Object#toString} is called to get a textual representation, which is
     *     wrapped in a {@link TextBox}.
     */
    public VBox(Object... items) {
        this(0);
        Assert.check(items.length > 0);
        for (Object item: items) {
            Assert.notNull(item);
            if (item instanceof Box) {
                add((Box)item);
            } else {
                add(new TextBox(item.toString()));
            }
        }
    }

    /** Adds a single empty text to this {@link VBox}. */
    public void add() {
        add("", false);
    }

    /**
     * Add a box to the end of this {@link VBox}.
     *
     * @param box The box to add.
     */
    public void add(Box box) {
        Assert.notNull(box);
        add(box, false);
    }

    /**
     * Add a box to the end of this {@link VBox}.
     *
     * @param box The box to add.
     * @param addEmptyLine Whether to prefix the added box with an empty line if this {@link VBox} is non-empty.
     */
    public void add(Box box, boolean addEmptyLine) {
        Assert.notNull(box);
        if (addEmptyLine && !subBoxes.isEmpty()) {
            add("");
        }
        subBoxes.add(box);
    }

    /**
     * Add a text line to the end of this {@link VBox}.
     *
     * @param line The line of text to add. It must not contain new line characters.
     */
    public void add(String line) {
        Assert.notNull(line);
        add(line, false);
    }

    /**
     * Add a text line to the end of this {@link VBox}.
     *
     * @param line The line of text to add. It must not contain new line characters.
     * @param addEmptyLine Whether to prefix the added box with an empty line if this {@link VBox} is non-empty.
     */
    public void add(String line, boolean addEmptyLine) {
        Assert.notNull(line);
        if (addEmptyLine && !subBoxes.isEmpty()) {
            add("");
        }
        subBoxes.add(new TextBox(line));
    }

    /**
     * Add lines of text to the end of this {@link VBox}.
     *
     * @param lines The lines to add. The individual lines must not contain new line characters.
     */
    public void add(List<String> lines) {
        Assert.notNull(lines);
        add(lines, false);
    }

    /**
     * Add lines of text to the end of this {@link VBox}.
     *
     * @param lines The lines to add. The individual lines must not contain new line characters.
     * @param addEmptyLine Whether to prefix the added box with an empty line if this {@link VBox} is non-empty.
     */
    public void add(List<String> lines, boolean addEmptyLine) {
        Assert.notNull(lines);
        if (addEmptyLine && !subBoxes.isEmpty()) {
            add("");
        }
        for (String line: lines) {
            add(line);
        }
    }

    /**
     * Adds the given items to this {@link VBox}.
     *
     * @param items The items to add. If an item is a {@link Box}, it is added directly. Otherwise,
     *     {@link Object#toString} is called to get a textual representation, which is wrapped in a {@link TextBox}.
     */
    public void add(Object... items) {
        for (Object item: items) {
            Assert.notNull(item);
            if (item instanceof Box) {
                add((Box)item);
            } else {
                add(new TextBox(item.toString()));
            }
        }
    }

    @Override
    public List<String> getLines() {
        String prefix = Strings.spaces(indent);
        List<String> rslt = list();
        for (Box b: subBoxes) {
            for (String line: b.getLines()) {
                rslt.add(prefix + line);
            }
        }
        return rslt;
    }
}
