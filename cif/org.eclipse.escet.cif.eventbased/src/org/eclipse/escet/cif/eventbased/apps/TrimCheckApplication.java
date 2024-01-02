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
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.options.ReportFileOption;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
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
import org.eclipse.escet.common.java.exceptions.ApplicationException;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;

/** Application class for the trim check command. */
public class TrimCheckApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        TrimCheckApplication app = new TrimCheckApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link TrimCheckApplication} class. */
    public TrimCheckApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link TrimCheckApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public TrimCheckApplication(AppStreams streams) {
        super(streams);
    }

    /**
     * Construct an option page to set the trim check specific options.
     *
     * @return The option page.
     */
    private OptionCategory getTransformationOptionPage() {
        List<OptionCategory> subPages = list();
        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(ReportFileOption.class));
        return new OptionCategory("Trim check", "CIF event-based trim check options.", subPages, options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        String optDesc = "All options for the event-based trim check tool.";
        return new OptionCategory("Event-based trim check options", optDesc, subPages, options);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    /**
     * Output a report about locations of an automaton of a given type.
     *
     * @param aut Automaton to report.
     * @param locs Locations <strong>not</strong> to report.
     * @param locCount Number of locations to expect when all is fine.
     * @param locType Type of location set (either {@code "reachable"} or {@code "coreachable"}).
     * @param stream Stream to report to.
     */
    private void outputLocations(Automaton aut, Set<Location> locs, int locCount, String locType, AppStream stream) {
        locCount -= locs.size();
        if (locCount == 0) {
            stream.printf("- All locations are %s.\n", locType);
            return;
        }

        if (locCount == 1) {
            stream.printf("- The following location is not %s:\n", locType);
        } else {
            stream.printf("- The following %d locations are not %s:\n", locCount, locType);
        }
        for (Location loc: aut) {
            if (!locs.contains(loc)) {
                stream.printf("  %s.\n", loc.toString());
            }
        }
    }

    @Override
    protected int runInternal() {
        String outPath;
        int exitCode;
        String rsltMsg;

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

            // Apply trim check to each automaton.
            OutputProvider.dbg("Applying trim check...");
            List<Set<Location>> reachables = listc(cte.automata.size());
            List<Set<Location>> coreachables = listc(cte.automata.size());
            boolean ok = true;
            for (Automaton aut: cte.automata) {
                int locCount = aut.size();
                Set<Location> locs = AutomatonHelper.getReachables(aut);
                if (locCount != locs.size()) {
                    ok = false;
                }
                reachables.add(locs);
                if (isTerminationRequested()) {
                    return 0;
                }

                locs = set();
                AutomatonHelper.getNonCoreachableCount(aut, locs);
                if (locCount != locs.size()) {
                    ok = false;
                }
                coreachables.add(locs);
                if (isTerminationRequested()) {
                    return 0;
                }
            }

            // Write result.
            outPath = "_trimcheck.txt";
            outPath = ReportFileOption.getDerivedPath(".cif", outPath);
            OutputProvider.dbg("Writing result to \"%s\"...", outPath);
            String absOutPath = Paths.resolve(outPath);

            exitCode = ok ? 0 : 1;
            String result = (exitCode == 0) ? "HOLDS" : "FAILS";
            rsltMsg = fmt("Trim check %s in file \"%s\". See \"%s\" for details.", result, InputFileOption.getPath(),
                    outPath);

            AppStream stream = new FileAppStream(absOutPath);
            OutputProvider.dbg(rsltMsg);
            stream.printf("Trim check %s in file \"%s\".\n", result, InputFileOption.getPath());

            if (!ok) {
                // If one automaton fails in its requirements, dump
                // information of all automata.
                for (int i = 0; i < cte.automata.size(); i++) {
                    Automaton aut = cte.automata.get(i);
                    int locCount = aut.size();
                    Set<Location> rl = reachables.get(i);
                    Set<Location> crl = coreachables.get(i);
                    result = "HOLDS";
                    if (rl.size() != locCount || crl.size() != locCount) {
                        result = "FAILS";
                    }

                    stream.printf("\n");
                    stream.printf("Trim check %s for automaton \"%s\".\n", result, aut.name);
                    outputLocations(aut, rl, locCount, "reachable", stream);
                    outputLocations(aut, crl, locCount, "coreachable", stream);
                }
            }

            stream.close();
        } catch (ApplicationException e) {
            String msg = fmt("Failed to apply trim check for CIF file \"%s\".", InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }

        if (exitCode == 0) {
            return 0;
        }
        throw new InvalidInputException(rsltMsg);
    }

    @Override
    public String getAppName() {
        return "CIF trim check tool";
    }

    @Override
    public String getAppDescription() {
        return "Verifies whether the automata are trim, that is, in each automaton, the locations must be both "
                + "reachable and co-reachable.";
    }
}
