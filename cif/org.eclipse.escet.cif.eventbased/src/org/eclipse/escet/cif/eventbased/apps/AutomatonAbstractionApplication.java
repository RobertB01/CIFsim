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

package org.eclipse.escet.cif.eventbased.apps;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.eventbased.AutomatonAbstraction;
import org.eclipse.escet.cif.eventbased.apps.conversion.ApplicationHelper;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertFromEventBased;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBasedPreChecker;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBasedPreChecker.ExpectedNumberOfAutomata;
import org.eclipse.escet.cif.eventbased.apps.options.AddStateAnnosOption;
import org.eclipse.escet.cif.eventbased.apps.options.ObservedEventsOption;
import org.eclipse.escet.cif.eventbased.apps.options.ResultNameOption;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.exceptions.ApplicationException;

/** Application class for computing the automaton abstraction for a set of events. */
public class AutomatonAbstractionApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        AutomatonAbstractionApplication app = new AutomatonAbstractionApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link AutomatonAbstractionApplication} class. */
    public AutomatonAbstractionApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link AutomatonAbstractionApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public AutomatonAbstractionApplication(AppStreams streams) {
        super(streams);
    }

    /**
     * Construct an option page to set the automaton abstraction specific options.
     *
     * @return The option page.
     */
    private OptionCategory getTransformationOptionPage() {
        List<OptionCategory> subPages = list();
        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(ObservedEventsOption.class));
        options.add(Options.getInstance(OutputFileOption.class));
        options.add(Options.getInstance(ResultNameOption.class));
        options.add(Options.getInstance(AddStateAnnosOption.class));
        return new OptionCategory("Automaton abstraction", "CIF event-based automaton abstraction options.", subPages,
                options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        String optDesc = "All options for the event-based automaton abstraction tool.";
        return new OptionCategory("Event-based automaton abstraction options", optDesc, subPages, options);
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
            String absSpecPath = Paths.resolve(InputFileOption.getPath());
            Specification spec = cifReader.read();
            if (isTerminationRequested()) {
                return 0;
            }

            // Preprocessing.
            new ElimComponentDefInst().transform(spec);

            // Check preconditions.
            boolean allowPlainEvents = true;
            boolean allowNonDeterminism = true;
            ExpectedNumberOfAutomata expectedNumberOfAutomata = ExpectedNumberOfAutomata.EXACTLY_ONE_AUTOMATON;
            EnumSet<SupKind> disallowedAutSupKinds = EnumSet.noneOf(SupKind.class);
            boolean requireAutHasInitLoc = false;
            boolean requireReqSubsetPlantAlphabet = false;
            boolean requireAutMarkedAndNonMarked = true;
            Termination termination = () -> isTerminationRequested();
            CifPreconditionChecker checker = new ConvertToEventBasedPreChecker(allowPlainEvents, allowNonDeterminism,
                    expectedNumberOfAutomata, disallowedAutSupKinds, requireAutHasInitLoc,
                    requireReqSubsetPlantAlphabet, requireAutMarkedAndNonMarked, termination);
            checker.reportPreconditionViolations(spec, absSpecPath, getAppName());

            // Convert from CIF.
            OutputProvider.dbg("Converting to internal representation...");
            ConvertToEventBased cte = new ConvertToEventBased();
            cte.convertSpecification(spec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Perform abstraction.
            OutputProvider.dbg("Abstracting automaton...");
            Automaton aut = cte.automata.get(0);
            String[] obsNms = ObservedEventsOption.getEvents();
            Set<Event> obses;
            obses = ApplicationHelper.selectEvents(obsNms, aut.alphabet);

            AutomatonAbstraction.automatonAbstractionPreCheck(aut, obses);
            if (isTerminationRequested()) {
                return 0;
            }

            aut = AutomatonAbstraction.automatonAbstraction(aut, obses);
            if (isTerminationRequested()) {
                return 0;
            }

            // Convert result to CIF.
            OutputProvider.dbg("Converting from internal representation...");
            String resultName = "abstracted";
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
            String absOutPath = Paths.resolve(outPath);
            CifWriter.writeCifSpec(spec, new PathPair(outPath, absOutPath), cifReader.getAbsDirPath());
            return 0;
        } catch (ApplicationException e) {
            String msg = fmt("Failed to compute automaton abstraction for CIF file \"%s\".", InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }
    }

    @Override
    public String getAppName() {
        return "CIF automaton abstraction tool";
    }

    @Override
    public String getAppDescription() {
        return "Abstracts an automaton to a set of observable events.";
    }
}
