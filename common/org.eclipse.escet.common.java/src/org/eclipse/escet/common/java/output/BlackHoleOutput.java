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
 * Output stream that is never enabled and never produces any output. Can be used for suppressing debug, normal, warning
 * and/or error output.
 */
public class BlackHoleOutput implements DebugNormalOutput, WarnOutput, ErrorOutput {
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
