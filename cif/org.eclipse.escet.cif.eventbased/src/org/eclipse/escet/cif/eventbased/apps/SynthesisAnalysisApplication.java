//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.apps;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.makeInitialUppercase;

import java.util.List;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;

/** Wrapper application for starting the synthesis analysis editor. */
public class SynthesisAnalysisApplication extends Application<IOutputComponent> {
    /** Name of the function being performed. */
    private final String app = "synthesis analysis";

    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        SynthesisAnalysisApplication app = new SynthesisAnalysisApplication();
        app.run(args);
    }

    /** Constructor for the {@link SynthesisAnalysisApplication} class. */
    public SynthesisAnalysisApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link SynthesisAnalysisApplication} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public SynthesisAnalysisApplication(AppStreams streams) {
        super(streams);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    /**
     * Construct an option page to set the application-specific options.
     *
     * @return The option page.
     */
    private OptionCategory getTransformationOptionPage() {
        List<OptionCategory> subPages = list();
        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        return new OptionCategory(makeInitialUppercase(app), "CIF event-based " + app + "options.", subPages, options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        String optDesc = "All options for the event-based " + app + " tool.";
        return new OptionCategory("Event-based " + app + " options", optDesc, subPages, options);
    }

    @Override
    protected int runInternal() {
        ControlEditor.show(InputFileOption.getPath(), SynthesisAnalysisEditor.class,
                "show the event-based synthesis analysis tool");
        return 0;
    }

    @Override
    public String getAppName() {
        return "CIF " + app + " tool";
    }

    @Override
    public String getAppDescription() {
        return "Starts the synthesis analysis tool for exploring reasons of removing states and edges from the "
                + "supervisor synthesis result.";
    }
}
