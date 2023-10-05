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

import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertFromEventBased;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.options.ResultNameOption;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
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

/** Trim application. */
public class TrimApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        TrimApplication app = new TrimApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link TrimApplication} class. */
    public TrimApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor of the {@link TrimApplication} class.
     *
     * @param streams The streams to use for input, output, and errors.
     */
    public TrimApplication(AppStreams streams) {
        super(streams);
    }

    /**
     * Construct an option page to set the trim command specific options.
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
        return new OptionCategory("Trim", "CIF event-based trim options.", subPages, options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        String optDesc = "All options for the event-based trim tool.";
        return new OptionCategory("Event-based trim options", optDesc, subPages, options);
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

            // TODO: Generalize to making every automaton in the input trim.
            // This requires modification of the "ConvertFromEventBased" class
            // to allow writing more than a single automaton onto the output.
            if (cte.automata.size() != 1) {
                String msg = fmt("CIF input file contains %d automata, while trim requires a file with "
                        + "exactly one automaton.", cte.automata.size());
                throw new InvalidInputException(msg);
            }
            Automaton aut = cte.automata.get(0);

            // Compute the trim automaton.
            OutputProvider.dbg("Computing trim...");
            AutomatonHelper.computeTrim(aut);
            if (isTerminationRequested()) {
                return 0;
            }

            if (OutputProvider.dodbg()) {
                OutputProvider.dbg("Trim finished (%s).", AutomatonHelper.getAutStatistics(aut));
            }

            // Warn user about empty result.
            if (aut.initial == null) {
                OutputProvider.warn("Result is empty.");
            }

            // Convert result to CIF.
            OutputProvider.dbg("Converting from internal representation...");
            String resultName = "trim";
            resultName = ResultNameOption.getRsltName(resultName);
            ConvertFromEventBased cfe = new ConvertFromEventBased();
            spec = cfe.convertAutomaton(aut, resultName);
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
            String msg = fmt("Failed to compute trim for CIF file \"%s\".", InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }
    }

    @Override
    public String getAppName() {
        return "CIF trim tool";
    }

    @Override
    public String getAppDescription() {
        return "Removes all locations that are not reachable or coreachable.";
    }
}
