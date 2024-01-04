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

import java.util.List;

import org.eclipse.escet.cif.eventbased.NonConflictingCheck;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.options.ReportFileOption;
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

/** Class for performing the nonconflicting check on CIF files. */
public class NonconflictingCheckApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        NonconflictingCheckApplication app = new NonconflictingCheckApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link NonconflictingCheckApplication} class. */
    public NonconflictingCheckApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link NonconflictingCheckApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public NonconflictingCheckApplication(AppStreams streams) {
        super(streams);
    }

    /**
     * Construct an option page to set the nonconflicting check specific options.
     *
     * @return The option page.
     */
    private OptionCategory getTransformationOptionPage() {
        List<OptionCategory> subPages = list();
        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(ReportFileOption.class));
        return new OptionCategory("Nonconflicting check", "CIF event-based nonconflicting check options.", subPages,
                options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        String optDesc = "All options for the event-based nonconflicting check tool.";
        return new OptionCategory("Event-based nonconflicting check options", optDesc, subPages, options);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
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

            int autCount = cte.automata.size();
            if (autCount < 2) {
                String msg = fmt("CIF input file contains %d automat%s, while the nonconflicting check expects a "
                        + "file with at least 2 automata.", autCount, (autCount == 1) ? "on" : "a");
                throw new InvalidInputException(msg);
            }

            OutputProvider.dbg("Applying nonconflicting check...");
            NonConflictingCheck.nonconflictingPreCheck(cte.automata);
            if (isTerminationRequested()) {
                return 0;
            }

            List<Location> conflicts;
            conflicts = NonConflictingCheck.nonconflictingCheck(cte.automata);
            if (isTerminationRequested()) {
                return 0;
            }

            // Write result.
            outPath = "_conflicts.txt";
            outPath = ReportFileOption.getDerivedPath(".cif", outPath);
            OutputProvider.dbg("Writing result to \"%s\"...", outPath);
            String absOutPath = Paths.resolve(outPath);

            exitCode = conflicts.isEmpty() ? 0 : 1;
            String result = (exitCode == 0) ? "HOLDS" : "FAILS";
            rsltMsg = fmt("Nonconflicting check %s in file \"%s\". See \"%s\" for details.", result,
                    InputFileOption.getPath(), outPath);

            AppStream stream = new FileAppStream(absOutPath);
            OutputProvider.dbg(rsltMsg);
            stream.printf("Nonconflicting check %s in file \"%s\".\n\n", result, InputFileOption.getPath());

            if (conflicts.isEmpty()) {
                stream.printf("No conflicts found!\n");
            } else if (conflicts.size() == 1) {
                stream.printf("Found 1 conflict:\n");
            } else {
                stream.printf("Found %d conflicts:\n", conflicts.size());
            }

            for (Location loc: conflicts) {
                stream.printf("  %s\n", loc.toString());
            }

            stream.close();
        } catch (ApplicationException e) {
            String msg = fmt("Failed to apply nonconflicting check for CIF file \"%s\".", InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }

        if (exitCode == 0) {
            return 0;
        }
        throw new InvalidInputException(rsltMsg);
    }

    @Override
    public String getAppName() {
        return "CIF nonconflicting check tool";
    }

    @Override
    public String getAppDescription() {
        return "Verifies whether automata are conflicting, i.e. together could lead to non-coreachable states.";
    }
}
