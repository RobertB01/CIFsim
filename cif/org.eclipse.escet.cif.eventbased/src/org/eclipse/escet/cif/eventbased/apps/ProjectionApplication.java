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
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.NfaToDfa;
import org.eclipse.escet.cif.eventbased.apps.conversion.ApplicationHelper;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertFromEventBased;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.options.AddStateAnnosOption;
import org.eclipse.escet.cif.eventbased.apps.options.PreservedEventsOption;
import org.eclipse.escet.cif.eventbased.apps.options.ResultNameOption;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** Application wrapper class for computing the projection of an automaton for a set of preserved events. */
public class ProjectionApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        ProjectionApplication app = new ProjectionApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link ProjectionApplication} class. */
    public ProjectionApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link ProjectionApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public ProjectionApplication(AppStreams streams) {
        super(streams);
    }

    /**
     * Construct an option page to set the projection specific options.
     *
     * @return The option page.
     */
    private OptionCategory getTransformationOptionPage() {
        List<OptionCategory> subPages = list();
        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(PreservedEventsOption.class));
        options.add(Options.getInstance(OutputFileOption.class));
        options.add(Options.getInstance(ResultNameOption.class));
        options.add(Options.getInstance(AddStateAnnosOption.class));
        return new OptionCategory("Automaton projection", "CIF event-based automaton projection options.", subPages,
                options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        return new OptionCategory("Event-based automaton projection options",
                "All options for the event-based automaton project tool.", subPages, options);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected int runInternal() {
        try {
            // Load CIF specification.
            OutputProvider.dbg("Loading CIF specification \"%s\"...", InputFileOption.getPath());
            CifReader cifReader = new CifReader().init();
            Specification spec = cifReader.read();
            if (isTerminationRequested()) {
                return 0;
            }

            // Convert from CIF.
            OutputProvider.dbg("Converting to internal representation...");
            ConvertToEventBased cte = new ConvertToEventBased();
            cte.convertSpecification(spec, true);
            if (isTerminationRequested()) {
                return 0;
            }

            if (cte.automata.size() != 1) {
                String msg = fmt(
                        "CIF input file contains %d automata, while projection requires exactly one automaton.",
                        cte.automata.size());
                throw new InvalidInputException(msg);
            }

            // Convert the preserved events to the event-based representation.
            String[] preserved = PreservedEventsOption.getEvents();
            Set<Event> preservedEvents;
            preservedEvents = ApplicationHelper.selectEvents(preserved, cte.automata.get(0).alphabet);
            if (isTerminationRequested()) {
                return 0;
            }

            // Compute the projection.
            OutputProvider.dbg("Computing projection...");
            Automaton aut = NfaToDfa.toDFA(cte.automata.get(0), preservedEvents);
            if (isTerminationRequested()) {
                return 0;
            }

            if (OutputProvider.dodbg()) {
                OutputProvider.dbg("Projection finished (%s).", AutomatonHelper.getAutStatistics(aut));
            }

            // Warn user about empty result.
            if (aut.initial == null) {
                OutputProvider.warn("Automaton projection is empty.");
            }

            // Convert result to CIF.
            OutputProvider.dbg("Converting from internal representation...");
            String resultName = "projected";
            resultName = ResultNameOption.getRsltName(resultName);
            boolean doAddStateAnnos = AddStateAnnosOption.getStateAnnotationsEnabled();
            ConvertFromEventBased cfe = new ConvertFromEventBased();
            spec = cfe.convertAutomaton(aut, resultName, doAddStateAnnos);
            if (isTerminationRequested()) {
                return 0;
            }

            // Write result.
            String outPath = "_" + resultName + ".cif";
            outPath = OutputFileOption.getDerivedPath(".cif", outPath);
            OutputProvider.dbg("Writing result to \"%s\"...", outPath);
            outPath = Paths.resolve(outPath);
            CifWriter.writeCifSpec(spec, outPath, cifReader.getAbsDirPath());
            return 0;
        } catch (ApplicationException e) {
            String msg = fmt("Failed to compute projection for CIF file \"%s\".", InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }
    }

    @Override
    public String getAppName() {
        return "CIF automaton projection tool";
    }

    @Override
    public String getAppDescription() {
        return "Creates the event-based projected automaton of an automaton in a CIF specification.";
    }
}
