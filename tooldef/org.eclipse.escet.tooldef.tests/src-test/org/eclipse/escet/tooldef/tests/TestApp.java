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

package org.eclipse.escet.tooldef.tests;

import static org.eclipse.escet.common.java.Lists.list;

import java.io.IOException;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.exceptions.InputOutputException;

/** Test application. */
public class TestApp extends Application<IOutputComponent> {
    /**
     * Constructor for the {@link TestApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public TestApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "Test application.";
    }

    @Override
    public String getAppDescription() {
        return "An application to test the built-in \"app\" tool.";
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected int runInternal() {
        // We abuse the input file option to allow passing the exit code to
        // this application. We also don't check the provided value to make
        // sure it is a valid exit code and avoid parse failures.
        String input = InputFileOption.getPath();
        int exitCode = Integer.valueOf(input);

        // Read all stdin data and write it to the output, if the exit code
        // is 10.
        if (exitCode == 10) {
            while (true) {
                String line;
                try {
                    line = AppEnv.getStreams().in.readLine();
                } catch (IOException ex) {
                    String msg = "Failed to read from stdin.";
                    throw new InputOutputException(msg, ex);
                }

                if (line == null) {
                    break;
                }
                AppEnv.getStreams().out.println(line);
            }
        }

        // Write the exit code to stdout and stderr.
        AppEnv.getStreams().out.println(input);
        AppEnv.getStreams().err.println(input);

        // Return the exit code.
        return exitCode;
    }

    @Override
    protected OptionCategory getAllOptions() {
        OptionCategory generalOpts = getGeneralOptionCategory();

        OptionCategory testOpts = new OptionCategory("Test", "Test options.", list(),
                list(Options.getInstance(InputFileOption.class)));

        OptionCategory options = new OptionCategory("Test Application Options", "All options for Test Application.",
                list(generalOpts, testOpts), list());

        return options;
    }
}
