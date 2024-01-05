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

import org.eclipse.escet.cif.eventbased.SupervisorSynthesis;
import org.eclipse.escet.cif.eventbased.analysis.SynthesisDummyDump;
import org.eclipse.escet.cif.eventbased.analysis.SynthesisDump;
import org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpInterface;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertFromEventBased;
import org.eclipse.escet.cif.eventbased.apps.conversion.ConvertToEventBased;
import org.eclipse.escet.cif.eventbased.apps.options.AddStateAnnosOption;
import org.eclipse.escet.cif.eventbased.apps.options.DumpFileEnableOption;
import org.eclipse.escet.cif.eventbased.apps.options.DumpFileOption;
import org.eclipse.escet.cif.eventbased.apps.options.ResultNameOption;
import org.eclipse.escet.cif.eventbased.apps.options.WarnDisjunctGroups;
import org.eclipse.escet.cif.eventbased.apps.options.WarnEmptyAlphabet;
import org.eclipse.escet.cif.eventbased.apps.options.WarnMarkedDeadlock;
import org.eclipse.escet.cif.eventbased.apps.options.WarnSingleUseControllable;
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
import org.eclipse.escet.common.java.exceptions.InvalidModelException;

/** Application wrapper for event-based supervisor synthesis computation. */
public class SupervisorSynthesisApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        SupervisorSynthesisApplication app = new SupervisorSynthesisApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link SupervisorSynthesisApplication} class. */
    public SupervisorSynthesisApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link SupervisorSynthesisApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public SupervisorSynthesisApplication(AppStreams streams) {
        super(streams);
    }

    /**
     * Construct an option page to set the supervisor synthesis specific options.
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
        options.add(Options.getInstance(DumpFileEnableOption.class));
        options.add(Options.getInstance(DumpFileOption.class));
        options.add(Options.getInstance(WarnDisjunctGroups.class));
        options.add(Options.getInstance(WarnEmptyAlphabet.class));
        options.add(Options.getInstance(WarnMarkedDeadlock.class));
        options.add(Options.getInstance(WarnSingleUseControllable.class));
        return new OptionCategory("Supervisor synthesis", "CIF Event-based supervisor synthesis options.", subPages,
                options);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getTransformationOptionPage());
        subPages.add(getGeneralOptionCategory());

        List<Option> options = list();
        String optDesc = "All options for the event-based supervisor synthesis tool.";
        return new OptionCategory("Event-based supervisor synthesis options", optDesc, subPages, options);
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
            cte.convertSpecification(spec, false);
            cte.sortAutomata();
            if (isTerminationRequested()) {
                return 0;
            }

            // Check supervisor pre-conditions.
            OutputProvider.dbg("Computing supervisor...");
            SupervisorSynthesis.synthesisPreCheck(cte.automata, WarnDisjunctGroups.isEnabled(),
                    WarnEmptyAlphabet.isEnabled(), WarnMarkedDeadlock.isEnabled(),
                    WarnSingleUseControllable.isEnabled());

            if (isTerminationRequested()) {
                return 0;
            }

            String dumpFilename;
            if (DumpFileOption.getPath() == null && !DumpFileEnableOption.isEnabled()) {
                dumpFilename = null;
            } else {
                dumpFilename = DumpFileOption.getDerivedPath(".cif", ".synth_dump");
                dumpFilename = Paths.resolve(dumpFilename);
            }

            // Compute the supervisor.
            SynthesisDumpInterface synDump = null;
            Automaton aut = null;
            try {
                if (dumpFilename == null) {
                    synDump = new SynthesisDummyDump();
                } else {
                    synDump = new SynthesisDump(dumpFilename);
                }
                aut = SupervisorSynthesis.synthesis(cte.automata, synDump);
            } finally {
                // Creation of the SynthesisDump object may fail due to failing to open a file.
                if (synDump != null) {
                    synDump.close();
                }
            }
            synDump = null; // Free the data of the dump object.

            if (aut.initial == null) {
                throw new InvalidModelException("Supervisor is empty.");
            }
            if (isTerminationRequested()) {
                return 0;
            }

            // Convert result to CIF.
            OutputProvider.dbg("Converting from internal representation...");
            String resultName = "sup";
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
            String msg = fmt("Failed to create supervisor for CIF file \"%s\".", InputFileOption.getPath());
            throw new ApplicationException(msg, e);
        }
    }

    @Override
    public String getAppName() {
        return "CIF supervisor synthesis tool";
    }

    @Override
    public String getAppDescription() {
        return "Constructs a maximal permissive supervisor automaton from deterministic event-based plant and "
                + "requirement automata.";
    }
}
