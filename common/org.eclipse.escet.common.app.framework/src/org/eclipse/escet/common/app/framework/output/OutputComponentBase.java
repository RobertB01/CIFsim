//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.output;

/**
 * Default implementation of an output component. Does nothing with the output it is given. When developing an output
 * component, inherit from this if component if you don't want to do anything with the output given via the
 * {@link IOutputComponent} interface. This would for instance be the case for output components that implement a
 * derived interface, and don't want to process the output of the default (base) interface. If you do want to process
 * the output of the default (base) interface, please implement the {@link IOutputComponent} interface directly.
 */
public abstract class OutputComponentBase implements IOutputComponent {
    @Override
    public final void dbg(String msg, int indent) {
        // We don't want to process this kind of output.
    }

    @Override
    public final void out(String msg, int indent) {
        // We don't want to process this kind of output.
    }

    @Override
    public final void warn(String msg, int indent) {
        // We don't want to process this kind of output.
    }

    @Override
    public final void err(String msg) {
        // We don't want to process this kind of output.
    }

    @Override
    public void initialize() {
        // By default, no initialization is necessary.
    }

    @Override
    public void cleanup() {
        // By default, no cleanup is necessary.
    }
}
