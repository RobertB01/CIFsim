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

package org.eclipse.escet.common.java.output;

/**
 * Output stream provider that is never enabled and never produces any output. Can be used for suppressing debug,
 * normal, warning and/or error output.
 */
public class BlackHoleOutputProvider implements WarnOutputProvider, ErrorOutputProvider, DebugNormalOutputProvider {
    /** The black hole that accepts all output. */
    private static final BlackHoleOutput BLACK_HOLE = new BlackHoleOutput();

    /** Black hole class that accepts all output. */
    private static class BlackHoleOutput implements DebugNormalOutput, WarnOutput, ErrorOutput {
        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void inc() {
            // Indentation is not changed.
        }

        @Override
        public void dec() {
            // Indentation is not changed.
        }

        @Override
        public void line(String message) {
            // Nothing is output.
        }

        @Override
        public void line() {
            // Nothing is output.
        }

        @Override
        public void line(String message, Object... args) {
            // Nothing is output.
        }
    }

    @Override
    public DebugNormalOutput getDebugOutput(String linePrefix) {
        return BLACK_HOLE;
    }

    @Override
    public DebugNormalOutput getNormalOutput(String linePrefix) {
        return BLACK_HOLE;
    }

    @Override
    public ErrorOutput getErrorOutput(String linePrefix) {
        return BLACK_HOLE;
    }

    @Override
    public WarnOutput getWarnOutput(String linePrefix) {
        return BLACK_HOLE;
    }

    @Override
    public String toString() {
        return "";
    }
}
