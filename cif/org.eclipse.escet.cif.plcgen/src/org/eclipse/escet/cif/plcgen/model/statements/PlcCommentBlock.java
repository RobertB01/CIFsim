//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.common.java.Assert;

/**
 * A larger amount of comment text in the PLC code. It gets formatted like
 * <p>
 * <pre>
 * (*&lt;N times '*'&gt;
 *  * &lt;line 1&gt;
 *  * &lt;line 2&gt;
 *  * ...
 *  * &lt;last line&gt;
 *  *&lt;N times '*'&gt;)
 *  </pre>
 * </p>
 * <p>
 * Lines may not contain {@code (*} or {@code *)}.
 * </p>
 *
 * <p>
 * Note that this class is not generating a proper statement for the PLC. There must be at least one statement in the
 * scope to make it valid PLC code.
 * </p>
 */
public class PlcCommentBlock extends PlcStatement {
    /** Number of {@code *} characters to add above the first and after the last line. */
    public final int starCount;

    /**
     * Constructor of the {@link PlcCommentBlock} class.
     *
     * <p>
     * Produces a comment block with lines of 10 {@code *} characters above and below the provided text.
     * </p>
     *
     * @param lines The lines with text. Lines are taken as-is, they may not contain {@code (*} or {@code *)}.
     */
    public PlcCommentBlock(List<String> lines) {
        this(10, lines);
    }

    /** The lines with text. Lines are taken as-is, they may not contain {@code (*} or {@code *)}. */
    public final List<String> lines;

    /**
     * Constructor of the {@link PlcCommentBlock} class.
     *
     * @param starCount Number of {@code *} characters to add above the first and after the last line.
     * @param lines The lines with text. Lines are taken as-is, they may not contain {@code (*} or {@code *)}.
     */
    public PlcCommentBlock(int starCount, List<String> lines) {
        this.starCount = starCount;
        this.lines = lines;

        for (String line: lines) {
            Assert.check(!line.contains("(*"));
            Assert.check(!line.contains("*)"));
        }
    }

    @Override
    public PlcStatement copy() {
        return new PlcCommentBlock(starCount, lines);
    }
}
