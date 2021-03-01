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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/**
 * Box that combines sub-boxes horizontally. Aligns the top lines of the sub-boxes with the bottom lines of their
 * predecessor.
 */
public class HBox extends Box {
    /** Sub-boxes. */
    private final List<Box> subBoxes = list();

    /** Separator text to use between boxes. */
    private final String separator;

    /** Constructor for the {@link HBox} class. No separator is used. */
    public HBox() {
        this("");
    }

    /**
     * Constructor for the {@link HBox} class.
     *
     * @param separator Separator text to use between boxes.
     */
    public HBox(String separator) {
        this.separator = separator;
    }

    /**
     * Constructor for the {@link HBox} class. No separator is used.
     *
     * @param items The items that make up the box. At least one item is required. If an item is a {@link Box}, it is
     *     added directly. Otherwise, {@link Object#toString} is called to get a textual representation, which is
     *     wrapped in a {@link TextBox}.
     */
    public HBox(Object... items) {
        Assert.check(items.length > 0);
        this.separator = "";
        for (Object item: items) {
            Assert.notNull(item);
            if (item instanceof Box) {
                add((Box)item);
            } else {
                add(new TextBox(item.toString()));
            }
        }
    }

    /**
     * Add a box to the end of this {@link HBox}.
     *
     * @param box The box to add.
     */
    public void add(Box box) {
        Assert.notNull(box);
        subBoxes.add(box);
    }

    /**
     * Add a text line to the end of this {@link HBox}.
     *
     * @param line The line of text to add. It may contain new line characters.
     */
    public void add(String line) {
        Assert.notNull(line);
        add(new TextBox(line));
    }

    /**
     * Adds the given items to this {@link HBox}.
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
        List<String> rslt = null;
        for (Box b: subBoxes) {
            List<String> bLines = b.getLines();
            rslt = (rslt == null) ? bLines : concatLines(rslt, bLines);
        }
        if (rslt == null) {
            rslt = list();
        }
        return rslt;
    }

    /**
     * Concatenates lines from two sub-boxes horizontally.
     *
     * @param lines1 The lines from the left sub-box.
     * @param lines2 The lines from the right sub-box.
     * @return The combined lines.
     */
    private List<String> concatLines(List<String> lines1, List<String> lines2) {
        if (lines1.isEmpty()) {
            return lines2;
        }
        if (lines2.isEmpty()) {
            return lines1;
        }

        int indent = last(lines1).length() + separator.length();
        String indentTxt = null;

        List<String> rslt = list();
        for (int i = 0; i < lines1.size() - 1; i++) {
            rslt.add(lines1.get(i));
        }
        rslt.add(last(lines1) + separator + first(lines2));
        for (int i = 1; i < lines2.size(); i++) {
            String line = lines2.get(i);
            if (indentTxt == null) {
                indentTxt = Strings.spaces(indent);
            }
            rslt.add(indentTxt + line);
        }
        return rslt;
    }
}
