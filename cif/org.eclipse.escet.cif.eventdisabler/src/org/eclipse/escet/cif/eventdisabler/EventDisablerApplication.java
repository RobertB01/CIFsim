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

package org.eclipse.escet.cif.eventdisabler;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.eventdisabler.options.EventNamesFileOption;
import org.eclipse.escet.cif.eventdisabler.options.EventNamesOption;
import org.eclipse.escet.cif.eventdisabler.options.EventUsageOption;
import org.eclipse.escet.cif.eventdisabler.options.IncludeInputSpecOption;
import org.eclipse.escet.cif.eventdisabler.options.SvgInputEventsOption;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** CIF event disabler application. */
public class EventDisablerApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        EventDisablerApplication app = new EventDisablerApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link EventDisablerApplication} class. */
    public EventDisablerApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link EventDisablerApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public EventDisablerApplication(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF event disabler";
    }

    @Override
    public String getAppDescription() {
        return "Disables events of a CIF specification.";
    }

    @Override
    protected int runInternal() {
        try {
            // Read CIF specification.
            CifReader cifReader = new CifReader().init();
            Specification spec = cifReader.read();
            if (isTerminationRequested()) {
                return 0;
            }

            // Check supported.
            if (CifScopeUtils.hasCompDefInst(spec)) {
                String msg = "Specifications with component definitions are currently not supported.";
                throw new UnsupportedException(msg);
            }

            // Disable events.
            spec = EventDisabler.disableEvents(spec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Write output specification.
            String outPath = OutputFileOption.getDerivedPath(".cif", ".disabled.cif");
            outPath = Paths.resolve(outPath);
            CifWriter.writeCifSpec(spec, outPath, cifReader.getAbsDirPath());
        } catch (ApplicationException e) {
            String msg = fmt("Failed to disable events for CIF file \"%s\".", InputFileOption.getPath());
            throw new InvalidInputException(msg, e);
        }

        // All done.
        return 0;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> genOpts = list();
        genOpts.add(Options.getInstance(InputFileOption.class));
        genOpts.add(Options.getInstance(OutputFileOption.class));
        genOpts.add(Options.getInstance(EventNamesOption.class));
        genOpts.add(Options.getInstance(EventNamesFileOption.class));
        genOpts.add(Options.getInstance(SvgInputEventsOption.class));
        genOpts.add(Options.getInstance(EventUsageOption.class));
        genOpts.add(Options.getInstance(IncludeInputSpecOption.class));
        List<OptionCategory> genSubCats = list();
        OptionCategory genCat = new OptionCategory("Generator", "Generator options.", genSubCats, genOpts);

        List<OptionCategory> cats = list(generalCat, genCat);
        OptionCategory options = new OptionCategory("CIF Event Disabler Options",
                "All options for the CIF event disabler.", cats, list());

        return options;
    }
}
