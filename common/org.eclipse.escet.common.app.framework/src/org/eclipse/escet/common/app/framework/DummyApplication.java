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

package org.eclipse.escet.common.app.framework;

import static org.eclipse.escet.common.java.Lists.list;

import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** Dummy application, useful for unit tests. */
public class DummyApplication extends Application<IOutputComponent> {
    /**
     * Constructor for the {@link DummyApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public DummyApplication(AppStreams streams) {
        super(streams);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected int runInternal() {
        // Does nothing.
        return 0;
    }

    @Override
    public String getAppName() {
        return "Dummy application";
    }

    @Override
    public String getAppDescription() {
        return "Dummy description.";
    }

    @Override
    protected OptionCategory getAllOptions() {
        OptionCategory generalOpts = getGeneralOptionCategory();

        OptionCategory options = new OptionCategory("Dummy Application Options",
                "All options for the Dummy application.", list(generalOpts), list());

        return options;
    }
}
