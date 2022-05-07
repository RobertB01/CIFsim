//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.io;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.spaces;
import static org.eclipse.escet.common.java.Strings.trimRight;

import java.util.Arrays;

import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.dsm.Label;

/**
 * Write a DSM to a file.
 *
 * <p>
 * Can write the adjacency matrix and labels using {@link #writeMatrixFile} and write the groups structure using
 * {@link #writeGroups}.
 * </p>
 */
public class WriteMatrix {
    /** Constructor of the {@link WriteMatrix} class. */
    private WriteMatrix() {
        // Static class.
    }

    /**
     * Write the adjacency matrix and labels to the file.
     *
     * @param outHandle Handle to the output file.
     * @param adjacencies Matrix with adjacency information.
     * @param labels Names of each element in the matrix.
     */
    public static void writeMatrixFile(AppStream outHandle, RealMatrix adjacencies, Label[] labels) {
        // Construct a line of text for each row.
        int size = adjacencies.getRowDimension();
        StringBuilder[] lines = new StringBuilder[size];
        for (int i = 0; i < size; i++) {
            lines[i] = new StringBuilder(16 + size * 3);
        }

        String[] texts = new String[size];
        for (int i = 0; i < size; i++) {
            texts[i] = "\"" + labels[i].toString() + "\"";
        }
        appendLeftAlignedColumn(texts, lines);

        for (int col = 0; col < size; col++) {
            appendColumn(", ", lines);

            for (int i = 0; i < size; i++) {
                double value = adjacencies.getEntry(i, col);
                texts[i] = (value <= 0) ? "" : Double.toString(value);
            }
            appendRightAlignedColumn(texts, lines);
        }

        // Write the lines.
        for (int i = 0; i < size; i++) {
            outHandle.println(trimRight(lines[i].toString()));
        }
    }

    /**
     * Append a column of text to the lines.
     *
     * @param text Text to add to each row.
     * @param lines Lines created so far, are assumed to have equal length.
     */
    private static void appendColumn(String text, StringBuilder[] lines) {
        for (int i = 0; i < lines.length; i++) {
            lines[i].append(text);
        }
    }

    /**
     * Append a left-aligned column of texts to the lines.
     *
     * @param texts Text to add to each row.
     * @param lines Lines created so far. They are assumed to have equal length, and are modified in-place.
     */
    private static void appendLeftAlignedColumn(String[] texts, StringBuilder[] lines) {
        if (texts.length == 0) {
            return;
        }

        int maxLength = Arrays.stream(texts).map(String::length).reduce(Integer::max).get();
        for (int i = 0; i < texts.length; i++) {
            lines[i].append(texts[i] + spaces(maxLength - texts[i].length()));
        }
    }

    /**
     * Append a right-aligned column of texts to the lines.
     *
     * @param texts Text to add to each row.
     * @param lines Lines created so far. They are assumed to have equal length, and are modified in-place.
     */
    private static void appendRightAlignedColumn(String[] texts, StringBuilder[] lines) {
        if (texts.length == 0) {
            return;
        }

        int maxLength = Arrays.stream(texts).map(String::length).reduce(Integer::max).get();
        for (int i = 0; i < texts.length; i++) {
            lines[i].append(spaces(maxLength - texts[i].length()) + texts[i]);
        }
    }

    /**
     * Write bus and cluster group information.
     *
     * @param outHandle Handle to the output file.
     * @param rootGroup Group structure.
     */
    public static void writeGroups(AppStream outHandle, Group rootGroup) {
        outHandle.println();
        outHandle.println("Groups:");
        writeGroups(outHandle, rootGroup, "");
    }

    /**
     * Recursive function to write the nested group information to the output.
     *
     * @param outHandle Handle to the output file being written.
     * @param group Group to write (recursively).
     * @param indent Leading indent for the group.
     */
    private static void writeGroups(AppStream outHandle, Group group, String indent) {
        if (group.childGroups.isEmpty() && group.getShuffledSize() == 1) {
            return; // Don't print single nodes as group.
        }

        switch (group.groupType) {
            case BUS:
                outHandle.print(indent + "- bus");
                break;
            case CLUSTER:
                outHandle.print(indent + "- cluster");
                break;
            case COLLECTION:
                outHandle.print(indent + "- collection");
                break;
            default:
                throw new RuntimeException(fmt("Unexpected groupType \"%s\" found.", group.groupType));
        }

        int base = group.getShuffledBase();
        int size = group.getShuffledSize();
        if (base >= 0) {
            outHandle.printf(" from %d to %d%n", base + 1, base + size); // 1-based !
        }

        for (Group child: group.childGroups) {
            writeGroups(outHandle, child, indent + "- ");
        }
    }
}
