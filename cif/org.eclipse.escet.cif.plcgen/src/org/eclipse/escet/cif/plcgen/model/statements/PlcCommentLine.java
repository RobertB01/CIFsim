//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.statements;

import org.eclipse.escet.common.java.Assert;

/**
 * An empty line or a comment line in PLC code.
 *
 * <p>
 * Neither an empty line nor a pure comment line are statements. A code block with just empty and/or comment lines is
 * thus not correct PLC code, since a block should generally have at least one statement. If adding a statement is
 * complicated, the {@link PlcCommentLine} class can be configured to also generate an empty statement. (It will
 * generate an additional semi-colon {@code ';'} after the comment in that case.)
 * </p>
 */
public class PlcCommentLine extends PlcStatement {
    /**
     * If {@code null}, no comment or comment delimiters are generated. Otherwise the supplied text without {@code "(*"}
     * or {@code "*)"} delimiters is assumed to be one line of comment text although this is not enforced.
     */
    public final String commentText;

    /** Whether the comment line is also an empty statement. */
    public final boolean isEmptyStatement;

    /**
     * Constructor of the {@link PlcCommentLine} class.
     *
     * @param commentText If {@code null}, no comment or comment delimiters are generated. Otherwise the supplied text
     *     without {@code "(*"} or {@code "*)"} delimiters is assumed to be one line of comment text although this is
     *     not enforced.
     */
    public PlcCommentLine(String commentText) {
        this(commentText, false);
    }

    /**
     * Constructor of the {@link PlcCommentLine} class.
     *
     * @param commentText If {@code null}, no comment or comment delimiters are generated. Otherwise the supplied text
     *     without {@code "(*"} or {@code "*)"} delimiters is assumed to be one line of comment text although this is
     *     not enforced.
     * @param isEmptyStatement Whether the comment line is also an empty statement.
     */
    public PlcCommentLine(String commentText, boolean isEmptyStatement) {
        // Nesting of comments is not allowed.
        Assert.check(commentText == null || !commentText.contains("(*"));
        Assert.check(commentText == null || !commentText.contains("*)"));

        this.commentText = commentText;
        this.isEmptyStatement = isEmptyStatement;
    }

    @Override
    public PlcStatement copy() {
        return new PlcCommentLine(commentText, isEmptyStatement);
    }
}
