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

/** Class to return a match result. */
public class MatchResult {
    /** Index of the first matching character. */
    public final int matchStart;

    /** Index after the last matching character. */
    public final int matchEnd;

    /** Index of the first character of the matching group. */
    public final int groupStart;

    /** Index after the last character of the captured group. */
    public final int groupEnd;

    /**
     * Constructor of the {@link MatchResult} class.
     *
     * @param matchStart Index of the first matching character.
     * @param matchEnd Index after the last matching character.
     * @param groupStart Index of the first character of the matching group.
     * @param groupEnd Index after the last character of the captured group.
     */
    public MatchResult(int matchStart, int matchEnd, int groupStart, int groupEnd) {
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
        this.groupStart = groupStart;
        this.groupEnd = groupEnd;
    }
}
