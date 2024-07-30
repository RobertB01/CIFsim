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
import org.eclipse.escet.cif.eventbased.ObserverCheck;
import org.eclipse.escet.cif.eventbased.apps.conversion.ApplicationHelper;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBasedPreChecker;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBasedPreChecker.ExpectedNumberOfAutomata;
import org.eclipse.escet.cif.eventbased.apps.options.ObservedEventsOption;
import org.eclipse.escet.cif.eventbased.apps.options.ReportFileOption;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.exceptions.ApplicationException;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;

/** Application class for verifying the observer check property of an automaton for a set of observable events. */
public class ObserverCheckApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        ObserverCheckApplication app = new ObserverCheckApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link ObserverCheckApplication} class. */
    public ObserverCheckApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link ObserverCheckApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public ObserverCheckApplication(AppStreams streams) {
        super(streams);
    }

    /**
     * Construct an option page to set the observer check specific options.
     *
     * @return The option page.
     */
    private OptionCategory getTransformationOptionPage() {
        List<OptionCategory> subPages = list();
        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(ObservedEventsOption.class));
        options.add(Options.getInstance(ReportFileOption.class));
        return new OptionCategory("Observer check", "CIF event-based observer check options.", subPages, options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        String optDesc = "All options for the event-based observer check tool.";
        return new OptionCategory("Event-based observer check options", optDesc, subPages, options);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected int runInternal() {
        int exitCode;
        String rsltMsg;

        try {
            // Load CIF specification.
            OutputProvider.dbg("Loading CIF specification \"%s\"...", InputFileOption.getPath());
            Specification spec = new CifReader().init().read();
            String absSpecPath = Paths.resolve(InputFileOption.getPath());
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
            Termination termination = () -> isTerminationRequested();
            CifPreconditionChecker checker = new ConvertToEventBasedPreChecker(allowPlainEvents, allowNonDeterminism,
                    expectedNumberOfAutomata, disallowedAutSupKinds, requireAutHasInitLoc, termination);
            checker.reportPreconditionViolations(spec, absSpecPath, getAppName());

            // Convert from CIF.
            OutputProvider.dbg("Converting to internal representation...");
            ConvertToEventBased cte = new ConvertToEventBased();
            cte.convertSpecification(spec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Perform the check.
            OutputProvider.dbg("Applying observer check...");
            Automaton aut = cte.automata.get(0);
            String[] obsNms = ObservedEventsOption.getEvents();
            Set<Event> observables, badEvents;
            observables = ApplicationHelper.selectEvents(obsNms, aut.alphabet);
            if (isTerminationRequested()) {
                return 0;
            }

            badEvents = ObserverCheck.doObserverCheck(aut, observables);
            if (isTerminationRequested()) {
                return 0;
            }

            // Write result.
            String outPath = "_observation.txt";
            outPath = ReportFileOption.getDerivedPath(".cif", outPath);
            OutputProvider.dbg("Writing result to \"%s\"...", outPath);
            String absOutPath = Paths.resolve(outPath);

            exitCode = badEvents.isEmpty() ? 0 : 1;
            String result = (exitCode == 0) ? "HOLDS" : "FAILS";
            rsltMsg = fmt("Observer check %s in file \"%s\". See \"%s\" for details.", result,
                    InputFileOption.getPath(), outPath);

            AppStream stream = new FileAppStream(outPath, absOutPath);
            OutputProvider.dbg(rsltMsg);
            stream.printf("Observer check %s in file \"%s\"\n", result, InputFileOption.getPath());
            stream.printf("for observable events");
            boolean first = true;
            for (Event evt: observables) {
                if (!first) {
                    stream.printf(",");
                }
                stream.printf(" \"%s\"", evt.name);
                first = false;
            }
            stream.printf(".\n");

            first = true;
            for (Event evt: badEvents) {
                if (first) {
                    if (badEvents.size() == 1) {
                        stream.printf("\nEvent that invalidates ");
                    } else {
                        stream.printf("\nEvents that invalidate ");
                    }
                    stream.printf("the observer property:");
                    first = false;
                } else {
                    stream.printf(",");
                }
                stream.printf(" \"%s\"", evt.name);
            }
            if (!first) {
                stream.printf(".\n");
            }

            stream.close();
        } catch (ApplicationException e) {
            String msg = fmt("Failed to apply observer check for CIF file \"%s\".", InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }

        if (exitCode == 0) {
            return 0;
        }
        throw new InvalidInputException(rsltMsg);
    }

    @Override
    public String getAppName() {
        return "CIF observer check tool";
    }

    @Override
    public String getAppDescription() {
        return "Verifies whether an automaton can act as an observer of occurrences of observable events.";
    }
}
