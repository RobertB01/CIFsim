//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.eventbased.ControllabilityCheck;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.options.ReportFileOption;
import org.eclipse.escet.cif.eventbased.automata.EventAtLocation;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** Controllability check algorithm execution. */
public class ControllabilityCheckApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        ControllabilityCheckApplication app = new ControllabilityCheckApplication();
        app.run(args);
    }

    /** Constructor for the {@link ControllabilityCheckApplication} class. */
    public ControllabilityCheckApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link ControllabilityCheckApplication} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public ControllabilityCheckApplication(AppStreams streams) {
        super(streams);
    }

    /**
     * Construct an option page to set the controllability check specific options.
     *
     * @return The option page.
     */
    private OptionCategory getTransformationOptionPage() {
        List<OptionCategory> subPages = list();
        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(ReportFileOption.class));
        return new OptionCategory("Controllability check", "CIF event-based controllability check options.", subPages,
                options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        String optDesc = "All options for the event-based controllability check tool.";
        return new OptionCategory("Event-based controllability check options", optDesc, subPages, options);
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
            cte.convertSpecification(spec, false);
            if (isTerminationRequested()) {
                return 0;
            }

            // Perform the controllability check.
            OutputProvider.dbg("Applying controllability check...");
            ControllabilityCheck.controllabilityCheckPreCheck(cte.automata);
            if (isTerminationRequested()) {
                return 0;
            }

            List<EventAtLocation> disableds;
            disableds = ControllabilityCheck.controllabilityCheck(cte.automata);
            if (isTerminationRequested()) {
                return 0;
            }

            // Write result.
            outPath = "_disableds.txt";
            outPath = ReportFileOption.getDerivedPath(".cif", outPath);
            OutputProvider.dbg("Writing result to \"%s\"...", outPath);
            String absOutPath = Paths.resolve(outPath);

            exitCode = (disableds == null || disableds.get(0).evt.isControllable()) ? 0 : 1;
            String result = (exitCode == 0) ? "HOLDS" : "FAILS";
            rsltMsg = fmt("Controllability check %s in file \"%s\". See \"%s\" for details.", result,
                    InputFileOption.getPath(), outPath);

            AppStream stream = new FileAppStream(absOutPath);
            OutputProvider.dbg(rsltMsg);
            stream.printf("Controllability check %s in file \"%s\".\n\n", result, InputFileOption.getPath());

            if (disableds == null) {
                stream.printf("No disabled events found.\n");
            } else if (disableds.get(0).evt.isControllable()) {
                // Only controllable events are disabled.
                stream.printf("No disabled uncontrollable event found.\n");
                stream.printf("\nDisabled controllable events:\n");
                for (EventAtLocation el: disableds) {
                    stream.printf("  %s\n", el.toString());
                }
            } else {
                stream.printf("Disabled uncontrollable events:\n");
                for (EventAtLocation el: disableds) {
                    stream.printf("  %s\n", el.toString());
                }
            }

            stream.close();
        } catch (ApplicationException e) {
            String msg = fmt("Failed to apply controllability check for CIF file \"%s\".", InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }

        if (exitCode == 0) {
            return 0;
        }
        throw new InvalidInputException(rsltMsg);
    }

    @Override
    public String getAppName() {
        return "CIF controllability check tool";
    }

    @Override
    public String getAppDescription() {
        return "Verifies whether the supervisor does not disable uncontrollable events of the plant.";
    }
}
