//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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
 * A comment line in PLC code.
 *
 * <p>
 * A pure comment line is not a statement. A code block with just comment lines is thus not correct PLC code. A proper
 * statement should generally be added to such a block. Alternatively, a comment line may be set as an empty statement.
 * </p>
 */
public class PlcCommentLine extends PlcStatement {
    /**
     * Text of the comment, may not contain {@code "(*"} or {@code "*)"}. The intention is that a single line of text is
     * provided although this is not enforced.
     */
    public final String commentText;

    /** Whether the comment line is also an empty statement. */
    public final boolean isEmptyStatement;

    /**
     * Constructor of the {@link PlcCommentLine} class.
     *
     * @param commentText Text of the comment, may not contain {@code "(*"} or {@code "*)"}. The intention is that a
     *     single line of text is provided although this is not enforced.
     */
    public PlcCommentLine(String commentText) {
        this(commentText, false);
    }

    /**
     * Constructor of the {@link PlcCommentLine} class.
     *
     * @param commentText Text of the comment, may not contain {@code "(*"} or {@code "*)"}. The intention is that a
     *     single line of text is provided although this is not enforced.
     * @param isEmptyStatement Whether the comment line is also an empty statement.
     */
    public PlcCommentLine(String commentText, boolean isEmptyStatement) {
        // Nesting of comments is not allowed.
        Assert.check(!commentText.contains("(*"));
        Assert.check(!commentText.contains("*)"));

        this.commentText = commentText;
        this.isEmptyStatement = isEmptyStatement;
    }
}
