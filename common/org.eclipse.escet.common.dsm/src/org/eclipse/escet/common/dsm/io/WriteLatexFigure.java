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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.dsm.Dsm;
import org.eclipse.escet.common.dsm.Group;

/** Generate a LaTeX tikz figure showing the DSM and write it. */
public class WriteLatexFigure {
    /** Constructor of the {@link WriteLatexFigure} class. */
    private WriteLatexFigure() {
        // Static class.
    }

    /**
     * Write the clustered dsm and labels to the file in LaTeX tikz format.
     *
     * @param outHandle Handle to the output file.
     * @param dsm Dsm with adjacency information.
     */
    public static void writeMatrixLatexFigureToStream(AppStream outHandle, Dsm dsm) {
        CodeBox codeBox = createMatrixLatexFigure(dsm);
        codeBox.write(outHandle);
    }

    /**
     * Creates LaTeX code of a tikz figure of the clustered dsm and labels.
     *
     * @param dsm Dsm with adjacency information.
     * @return The generated text.
     */
    public static CodeBox createMatrixLatexFigure(Dsm dsm) {
        MemoryCodeBox codeBox = new MemoryCodeBox();

        // Construct a line of text for each row.
        int size = dsm.adjacencies.getRowDimension();

        // Begin tikz environment.
        codeBox.add("\\begin{tikzpicture}[every node/.style={font=\\huge}, scale=0.3, transform shape]");

        // Draw the grid.
        codeBox.indent();
        codeBox.add("\\draw[black!40!white] (-.9, 0) grid (%s, %s);", size, size + 0.9);

        writeClustersLatex(dsm, codeBox);
        codeBox.add();

        // Add the labels.
        codeBox.add("% Labels.");
        for (int i = 0; i < size; i++) {
            writeLabelLatex(dsm.labels[i].toString(), i, true, size, codeBox);
            codeBox.add();
        }

        // Add the adjacency elements.
        for (int row = 0; row < size; row++) {
            writeRowLatex(dsm, row, codeBox);
            codeBox.add();
        }

        // End tikz environment.
        codeBox.dedent();
        codeBox.add("\\end{tikzpicture}");
        return codeBox;
    }

    /**
     * Write the cluster outlines to the file in Latex tikz format.
     *
     * @param dsm The dsm to write the clusters for.
     * @param codeBox The codebox to write to.
     */
    private static void writeClustersLatex(Dsm dsm, CodeBox codeBox) {
        int size = dsm.adjacencies.getColumnDimension();

        // Print tikz line that defines the cluster lines properties.
        codeBox.add("% Cluster lines.");
        codeBox.add("\\draw[line width=1pt, line cap=rect]");

        // Draw outer cluster.
        codeBox.indent();
        codeBox.add("(0, 0) -- (0, %s) -- (%s, %s) -- (%s, 0) -- cycle", size, size, size, size);

        for (Group gr: dsm.rootGroup.childGroups) {
            writeGroupLatex(gr, size, codeBox);
        }

        // Print closing semicolon for the tikz \draw.
        codeBox.dedent();
        codeBox.add(";");
    }

    /**
     * Write the outline of the group and its children to the file in Latex tikz format.
     *
     * @param group Group to write (recursively).
     * @param sizeDsm The size of the matrix.
     * @param codeBox The codebox to write to.
     */
    private static void writeGroupLatex(Group group, int sizeDsm, CodeBox codeBox) {
        int base = group.getShuffledBase();
        int size = group.getShuffledSize();

        int x1 = base;
        int x2 = base + size;
        int y1 = sizeDsm - base;
        int y2 = sizeDsm - base - size;

        if (base >= 0) {
            codeBox.add("(%s, %s) -- (%s, %s) -- (%s, %s) -- (%s, %s) -- cycle", x1, y1, x1, y2, x2, y2, x2, y1);
        }

        for (Group child: group.childGroups) {
            writeGroupLatex(child, sizeDsm, codeBox);
        }
    }

    /**
     * Write the specified label in the row or the column of a dsm to the file in the LaTeX tikz format.
     *
     * @param label The label to print.
     * @param index The destination row / column index.
     * @param labelsOnRow Whether labels should be printed in the rows or columns. {@code true} means in row, while
     *     {@code false} means in column.
     * @param size The size of the matrix.
     * @param codeBox The codebox to write to.
     */
    private static void writeLabelLatex(String label, int index, boolean labelsOnRow, int size, CodeBox codeBox) {
        // In Latex the _ needs to be escaped in text.
        label = label.replace("_", "\\_");
        String labelRow = labelsOnRow ? fmt(label + " - %s", index + 1) : fmt("%s", index + 1);
        String labelColumn = !labelsOnRow ? fmt("%s - " + label, index + 1) : fmt("%s", index + 1);

        // Line with comment on which label is considered.
        codeBox.add("%% Label %s.", index + 1);

        // Line for row label.
        codeBox.add("\\node[anchor=east] at (-.1, %s) {" + labelRow + "};", size - 0.5 - index);

        // Line for column label.
        codeBox.add("\\node[rotate=90, anchor=west] at (%s, %s) {" + labelColumn + "};", 0.5 + index, size + 0.1);

        // Line for diagonal block.
        codeBox.add("\\node[fill=gray] at (%s, %s) {\\color{gray} $\\bullet$};", 0.5 + index, size - 0.5 - index);
    }

    /**
     * Write the specified row of a dsm to the file in the LaTeX tikz format.
     *
     * @param dsm Dsm with adjacency information.
     * @param row The index of the row to print.
     * @param codeBox The codebox to write to.
     */
    private static void writeRowLatex(Dsm dsm, int row, CodeBox codeBox) {
        // Add comment to indicate which row the next part provides.
        codeBox.add("%% Row %s.", row + 1);

        // Create foreach loop for only non-zero elements of the row.
        int size = dsm.adjacencies.getColumnDimension();
        List<Integer> nonZeroElems = list();
        for (int j = 0; j < size; j++) {
            if (row != j && dsm.adjacencies.getEntry(row, j) > 0) {
                nonZeroElems.add(j);
            }
        }
        String nonZeroElemsText = nonZeroElems.stream().map(String::valueOf).collect(Collectors.joining(","));
        codeBox.add("\\foreach \\x in {" + nonZeroElemsText + "}{");

        // Add content of the foreach loop creating node containing the adjacency entry.
        codeBox.indent();
        codeBox.add("\\node[fill=black] at (\\x + 0.5, %s) {$\\bullet$};", size - row - 0.5);

        // Finally, add closing bracket for the foreach loop.
        codeBox.dedent();
        codeBox.add("}");
    }
}
