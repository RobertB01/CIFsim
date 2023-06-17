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
import static org.eclipse.escet.common.java.Maps.invert;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeInitialUppercase;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.eventbased.LanguageEquivalence;
import org.eclipse.escet.cif.eventbased.apps.conversion.CifOrigin;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.equivalence.CounterExample;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;

/** Language equivalence check application. */
public class LanguageEquivalenceCheckApplication extends Application<IOutputComponent> {
    /** Name of the function being performed. */
    private final String app = "language equivalence check";

    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        LanguageEquivalenceCheckApplication app = new LanguageEquivalenceCheckApplication();
        app.run(args);
    }

    /** Constructor for the {@link LanguageEquivalenceCheckApplication} class. */
    public LanguageEquivalenceCheckApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link LanguageEquivalenceCheckApplication} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public LanguageEquivalenceCheckApplication(AppStreams streams) {
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
        try {
            // Load CIF specification.
            OutputProvider.dbg("Loading CIF specification \"%s\"...", InputFileOption.getPath());
            Specification spec = new CifReader().init().read();
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

            OutputProvider.dbg("Applying " + app + "....");
            LanguageEquivalence.preCheck(cte.automata);
            if (isTerminationRequested()) {
                return 0;
            }

            Assert.check(cte.automata.size() == 2);
            CounterExample err = LanguageEquivalence.doLanguageEquivalenceCheck(cte.automata);
            if (isTerminationRequested()) {
                return 0;
            }

            if (err == null) {
                OutputProvider.out("Automata have the same language.");
                return 0;
            } else {
                // Convert counter example back to CIF and report the difference.

                org.eclipse.escet.cif.metamodel.cif.automata.Location loc0, loc1;
                loc0 = ((CifOrigin)err.locs[0].origin).cifLoc;
                loc1 = ((CifOrigin)err.locs[1].origin).cifLoc;
                Map<Event, org.eclipse.escet.cif.metamodel.cif.declarations.Event> eventsToCifEventsMap = invert(
                        cte.events);

                if (err.event == null) { // Markers of the locations are different.
                    if (!isMarked(loc0)) {
                        // 'loc0' should be the marked location, swap the locations.
                        org.eclipse.escet.cif.metamodel.cif.automata.Location locTmp = loc0;
                        loc0 = loc1;
                        loc1 = locTmp;
                    }
                    OutputProvider.out("Automata have a different language!");
                    OutputProvider.out(fmt("The %s is marked, but the equivalent %s is not marked.",
                            CifTextUtils.getLocationText1(loc0), CifTextUtils.getLocationText1(loc1)));
                    printPath(err.path, eventsToCifEventsMap);

                    return 1;
                }

                // Event can be performed at only one of the locations.

                org.eclipse.escet.cif.metamodel.cif.declarations.Event origEvent = eventsToCifEventsMap.get(err.event);
                Assert.notNull(origEvent);

                if (CifLocationUtils.getEdges(loc0, origEvent).isEmpty()) {
                    // 'loc0' should be the location that can do the event, swap the locations.
                    org.eclipse.escet.cif.metamodel.cif.automata.Location locTmp = loc0;
                    loc0 = loc1;
                    loc1 = locTmp;
                }
                OutputProvider.out("Automata have a different language!");
                OutputProvider.out(fmt(
                        "From %s, event \"%s\" can be performed, but from the equivalent %s, "
                                + "the event cannot be performed.",
                        CifTextUtils.getLocationText1(loc0), CifTextUtils.getAbsName(origEvent),
                        CifTextUtils.getLocationText1(loc1)));
                printPath(err.path, eventsToCifEventsMap);

                return 1;
            }
        } catch (ApplicationException e) {
            String msg = fmt("Failed to apply %s for CIF file \"%s\".", app, InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }
    }

    /**
     * Obtain the marker flag of the given location.
     *
     * <p>
     * This conversion is a simplified version of {@link ConvertToEventBased#getBooleanValue}. The marked property of
     * the location must have been successfully converted by that function before.
     * </p>
     *
     * @param loc Location to inspect.
     * @return {@code true} if the location is marked, else {@code false}.
     */
    private static boolean isMarked(org.eclipse.escet.cif.metamodel.cif.automata.Location loc) {
        List<Expression> preds = loc.getMarkeds();
        if (preds.isEmpty()) {
            return false; // Default of the 'marked' property.
        }
        Assert.check(preds.size() == 1);
        BoolExpression be = (BoolExpression)preds.get(0);
        return be.isValue();
    }

    /**
     * Prints the path of the counter example.
     *
     * @param path The counter example path.
     * @param events Mapping from event-based toolset events to CIF events.
     */
    private static void printPath(List<Event> path,
            Map<Event, org.eclipse.escet.cif.metamodel.cif.declarations.Event> events)
    {
        if (path == null || path.isEmpty()) {
            // Nothing to print.
            return;
        }

        OutputProvider
                .out("This state pair can be reached with the following sequence of events from the initial state:");
        int step = 1;
        for (Event pathEvent: path) {
            org.eclipse.escet.cif.metamodel.cif.declarations.Event origPathEvent = events.get(pathEvent);
            OutputProvider.out(fmt("%d. \"%s\"", step, CifTextUtils.getAbsName(origPathEvent)));
            step += 1;
        }
    }

    @Override
    public String getAppName() {
        return "CIF " + app + " tool";
    }

    @Override
    public String getAppDescription() {
        return "Verifies whether two automata are language equivalent, that is, whether both can produce the "
                + "same events in the same order.";
    }
}
