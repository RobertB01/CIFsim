//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2022 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Strings.spaces;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.dsm.ClusterInputData;
import org.eclipse.escet.common.dsm.Dsm;
import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.dsm.Label;
import org.eclipse.escet.common.java.Assert;

/** Code for reading and writing a matrix. */
public class ReadWriteMatrix {
    /** Constructor of the static {@link ReadWriteMatrix} class. */
    private ReadWriteMatrix() {
        // Static class.
    }

    /**
     * Match a 'word' to a matrix input line.
     *
     * @param line Text line to analyze.
     * @param start Start index for analyzing. Should be a character in the line.
     * @return <code>null</code> if match failed, else a match result with various relevant indices.
     */
    private static MatchResult matchWord(String line, int start) {
        Assert.check(start >= 0 || start < line.length());

        // Find first non-space at or after 'start'.
        int index = start;
        while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= line.length()) {
            return null;
        }

        // Grab the next word from the string.
        int groupStart = index;
        if (line.charAt(index) == '"') {
            // Parse quoted word.
            index++;
            while (index < line.length() && line.charAt(index) != '"') {
                index++;
            }
            if (index < line.length()) { // Don't skip non-existing closing quote.
                index++;
            }
            return new MatchResult(start, index, groupStart + 1, index);
        } else {
            // Parse non-quoted word.
            while (index < line.length()) {
                if (Character.isWhitespace(line.charAt(index)) || line.charAt(index) == ',') {
                    break;
                }
                index++;
            }
            return new MatchResult(start, index, groupStart, index);
        }
    }

    /**
     * Match a separator to a matrix input line.
     *
     * @param line Text line to analyze.
     * @param start Start index for analyzing. Should point at a character in the line.
     * @return <code>null</code> if match failed, else a match result with various relevant indices.
     */
    private static MatchResult matchSep(String line, int start) {
        Assert.check(start >= 0 || start < line.length());

        int index = start;
        while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= line.length()) {
            return null;
        }

        if (line.charAt(index) == ',') {
            return new MatchResult(start, index + 1, index, index + 1);
        }
        return null;
    }

    /**
     * Read words from a line of text.
     *
     * @param line Line to read.
     * @return The read words.
     */
    private static List<String> parseLine(String line) {
        List<String> entries = list();

        int index = 0;
        while (true) {
            if (index >= line.length()) {
                break;
            }

            MatchResult matchResult = matchWord(line, index);
            if (matchResult == null) {
                break;
            }
            entries.add(line.substring(matchResult.groupStart, matchResult.groupEnd));

            index = matchResult.matchEnd;
            if (index >= line.length()) {
                break;
            }

            matchResult = matchSep(line, index);
            if (matchResult == null) {
                break;
            }
            index = matchResult.matchEnd;
        }
        return entries;
    }

    /**
     * Take the rows and columns of texts, and convert it to input data for the cluster algorithm.
     * <p>
     * Function may throw a {@link InputOutputException} if the received data cannot be parsed. Missing or negative
     * adjacency values are assumed to be <code>0</code>.
     * </p>
     *
     * @param matrixLines Read input, rows of columns of texts.
     * @return The found cluster input data (adjacency values and labels).
     */
    private static ClusterInputData convertToMatrix(List<List<String>> matrixLines) {
        // Decide on the size of the matrix.
        // Note that rows are expected to be one bigger, since the first column is labels.
        //
        int matRowCount = matrixLines.size();
        int matColcount = 0;
        for (List<String> row: matrixLines) {
            matColcount = Math.max(matColcount, row.size());
        }

        int firstDataLine; // First line containing data.
        if (matRowCount > 1 && matRowCount == matColcount) {
            firstDataLine = 1;
        } else if (matRowCount >= 1 && matRowCount + 1 == matColcount) {
            // First line is data.
            firstDataLine = 0;
        } else {
            String msg = "Matrix data is not square, found %d rows and %d columns, excluding first label column.";
            throw new InputOutputException(fmt(msg, matRowCount, matColcount - 1));
        }

        int size = matRowCount - firstDataLine;
        RealMatrix adjMat = new BlockRealMatrix(size, size);
        Label[] labels = new Label[size];

        // Copy data to matrix.
        for (int row = 0; row < size; row++) {
            List<String> line = matrixLines.get(firstDataLine + row);
            labels[row] = (line.size() > 0) ? new Label(line.get(0)) : new Label("");
            int col = 0;
            for (; col < size; col++) {
                if (line.size() <= col + 1) {
                    break;
                }

                double value;
                try {
                    value = Math.max(0, Double.parseDouble(line.get(col + 1)));
                } catch (NumberFormatException ex) {
                    value = 0;
                }
                adjMat.setEntry(row, col, value);
            }
            for (; col < size; col++) {
                adjMat.setEntry(row, col, 0);
            }
        }

        return new ClusterInputData(adjMat, labels);
    }

    /**
     * Read the matrix elements as rows and columns of texts.
     *
     * @param inp Input stream.
     * @return Rows of columns of texts. Note that the number of columns at each line may be different.
     *      Also, no checking is performed whether the number of lines and columns match.
     * @throws InputOutputException when a line of text could not be read.
     */
    private static List<List<String>> readMatrixLines(BufferedReader inp) {
        List<List<String>> matrixValues = list();
        while (true) {
            String line;
            try {
                line = inp.readLine();
            } catch (IOException ex) {
                throw new InputOutputException("Failed to read a text line.", ex);
            }
            if (line == null) {
                break; // EOF reached.
            }

            List<String> words = parseLine(line);
            matrixValues.add(words);
        }
        return matrixValues;
    }

    /**
     * Read the CSV-like adjacency and label data from the file with the provided name.
     *
     * <p>True CSV is a complicated format, so this code only does a subset.</p>
     * <p>It assumes NxN numeric (real) values, as N lines of N comma or pipe separated numbers at a line.
     * Before the first number at each row should be a label designating the name of the element of that row.
     * Optionally, above the first line of data may be a line of labels as well.</p>
     * <p>Example:
     * <pre>
     * "",  "A", "B"
     * "A",  1,   0
     * "B", 0.5, 0.1
     * </pre>shows a 2x2 adjacency matrix of elements A and B with an additional first line of labels.</p>
     *
     * @param filename Name of the file to read.
     * @return The read data.
     */
    public static ClusterInputData readMatrixFile(String filename) {
        BufferedReader handle = null;
        try {
            handle = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            throw new InputOutputException(fmt("Failed to open input file \"%s\".", filename), ex);
        }

        try {
            List<List<String>> matrixLines = readMatrixLines(handle);
            return convertToMatrix(matrixLines);
        } finally {
            try {
                handle.close();
            } catch (IOException ex) {
                throw new InputOutputException(fmt("Failed to close file \"%s\".", filename), ex);
            }
        }
    }

    /**
     * Append a left-aligned column of texts to the lines.
     *
     * @param texts Text to add to each row.
     * @param lines Lines created so far. They are assumed to have equal length, and are modified in-place
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
     * @param lines Lines created so far. They are assumed to have equal length, and are modified in-place
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
            outHandle.println(lines[i].toString());
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

        switch(group.groupType) {
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
            throw new RuntimeException(fmt("Unexpected groupType \"%s\" found."));
        }

        int base = group.getShuffledBase();
        int size = group.getShuffledSize();
        if (base >= 0) {
            outHandle.printf(" from %d to %d\n", base + 1, base + size); // 1-based !
        }

        for (Group child: group.childGroups) {
            writeGroups(outHandle, child, indent + "- ");
        }
    }

    /**
     * Write the clustered dsm and labels to the file in LaTeX tikz format.
     *
     * @param outHandle Handle to the output file.
     * @param dsm Dsm with adjacency information.
     */
    public static void writeMatrixLatexFigureToStream(AppStream outHandle, Dsm dsm) {
        MemoryCodeBox codeBox = new MemoryCodeBox();
        createMatrixLatexFigure(codeBox, dsm);
        codeBox.write(outHandle);
    }

    /**
     * Creates LaTeX code of a tikz figure of the clustered dsm and labels.
     *
     * @param codeBox CodeBox to put the LaTeX code in.
     * @param dsm Dsm with adjacency information.
     */
    public static void createMatrixLatexFigure(CodeBox codeBox, Dsm dsm) {
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

        for (Group gr : dsm.rootGroup.childGroups) {
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

        for (Group child : group.childGroups) {
            writeGroupLatex(child, sizeDsm, codeBox);
        }
    }

    /**
     * Write the specified label in the row or the column of a dsm to the file in the LaTeX tikz format.
     *
     * @param label The label to print.
     * @param index The destination row / column index.
     * @param labelsOnRow Whether labels should be printed in the rows or columns. {@code true} means in row, while
     *      {@code false} means in column.
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
