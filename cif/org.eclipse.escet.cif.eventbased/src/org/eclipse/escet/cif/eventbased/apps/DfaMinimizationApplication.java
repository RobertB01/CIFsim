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
import static org.eclipse.escet.common.java.Strings.makeInitialUppercase;

import java.util.List;

import org.eclipse.escet.cif.eventbased.DfaMinimize;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertFromEventBased;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.options.AddStateAnnosOption;
import org.eclipse.escet.cif.eventbased.apps.options.ResultNameOption;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
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
import org.eclipse.escet.common.java.exceptions.ApplicationException;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;

/** DFA minimization application. */
public class DfaMinimizationApplication extends Application<IOutputComponent> {
    /** Name of the function being performed. */
    private final String app = "DFA minimization";

    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        DfaMinimizationApplication app = new DfaMinimizationApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link DfaMinimizationApplication} class. */
    public DfaMinimizationApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link DfaMinimizationApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public DfaMinimizationApplication(AppStreams streams) {
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
        options.add(Options.getInstance(OutputFileOption.class));
        options.add(Options.getInstance(ResultNameOption.class));
        options.add(Options.getInstance(AddStateAnnosOption.class));
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
                String msg = fmt("DFA minimization can be applied to only one automaton, found %s automata.",
                        cte.automata.size());
                throw new InvalidInputException(msg);
            }

            OutputProvider.dbg("Applying " + app + "....");
            DfaMinimize.preCheck(cte.automata.get(0));
            if (isTerminationRequested()) {
                return 0;
            }

            Automaton result = DfaMinimize.minimize(cte.automata.get(0));
            if (isTerminationRequested()) {
                return 0;
            }

            // Convert result to CIF.
            OutputProvider.dbg("Converting from internal representation...");
            String resultName = "minimal";
            resultName = ResultNameOption.getRsltName(resultName);
            boolean doAddStateAnnos = AddStateAnnosOption.getStateAnnotationsEnabled();
            ConvertFromEventBased cfe = new ConvertFromEventBased();
            spec = cfe.convertAutomaton(result, resultName, doAddStateAnnos);
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
            String msg = fmt("Failed to apply %s for CIF file \"%s\".", app, InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }
    }

    @Override
    public String getAppName() {
        return "CIF " + app + " tool";
    }

    @Override
    public String getAppDescription() {
        return "Constructs a DFA with the equivalent language but with a minimal number of locations.";
    }
}
