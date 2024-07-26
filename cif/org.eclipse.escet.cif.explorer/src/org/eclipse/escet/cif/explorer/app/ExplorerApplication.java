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

package org.eclipse.escet.cif.explorer.app;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.checkers.CifChecker;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.RemoveAnnotations;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesNoRefsOptimized;
import org.eclipse.escet.cif.explorer.CifAutomatonBuilder;
import org.eclipse.escet.cif.explorer.ExplorerPreChecker;
import org.eclipse.escet.cif.explorer.ExplorerStateFactory;
import org.eclipse.escet.cif.explorer.RequirementAsPlantChecker;
import org.eclipse.escet.cif.explorer.options.AddStateAnnosOption;
import org.eclipse.escet.cif.explorer.options.AutomatonNameOption;
import org.eclipse.escet.cif.explorer.options.EnableCifOutputOption;
import org.eclipse.escet.cif.explorer.options.EnableEdgeMinimizationOption;
import org.eclipse.escet.cif.explorer.options.EnableReportOption;
import org.eclipse.escet.cif.explorer.options.EnableStatisticsOption;
import org.eclipse.escet.cif.explorer.options.PrintProgressOption;
import org.eclipse.escet.cif.explorer.options.RemoveDuplicateTransitionsOption;
import org.eclipse.escet.cif.explorer.options.ReportFileOption;
import org.eclipse.escet.cif.explorer.runtime.BaseState;
import org.eclipse.escet.cif.explorer.runtime.ExplorationTerminatedException;
import org.eclipse.escet.cif.explorer.runtime.Explorer;
import org.eclipse.escet.cif.explorer.runtime.ExplorerBuilder;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
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
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.box.StreamCodeBox;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.Termination;

/** Application implementing untimed unfolding of the state space of a CIF specification. */
public class ExplorerApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command-line arguments supplied to the application.
     */
    public static void main(String[] args) {
        ExplorerApplication app = new ExplorerApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link ExplorerApplication} class. */
    public ExplorerApplication() {
        // Nothing to do.
    }

    /**
     * Constructor of the {@link ExplorerApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public ExplorerApplication(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF untimed state space explorer";
    }

    @Override
    public String getAppDescription() {
        return "Explore a CIF specification to its untimed state space.";
    }

    @Override
    protected int runInternal() {
        // Read CIF file.
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        String absSpecPath = Paths.resolve(InputFileOption.getPath());
        if (isTerminationRequested()) {
            return 0;
        }

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warn("The specification contains CIF/SVG input declarations. These will be ignored.");
        }

        // Perform preprocessing. For value simplification, constants are
        // not inlined, and the optimized variant is used for performance
        // reasons.
        new RemoveAnnotations().transform(spec);
        new ElimComponentDefInst().transform(spec);
        new ElimSelf().transform(spec);
        new SimplifyValuesNoRefsOptimized().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Check preconditions.
        Termination termination = () -> isTerminationRequested();
        ExplorerPreChecker checker = new ExplorerPreChecker(termination);
        checker.reportPreconditionViolations(spec, absSpecPath, "CIF explorer");
        if (isTerminationRequested()) {
            return 0;
        }

        // Warn about features of the specification that may lead to an unexpected resulting state space.
        CifCheck[] checks = {new RequirementAsPlantChecker()};
        CifCheckViolations warnings = new CifChecker(termination, checks).check(spec, absSpecPath);
        if (warnings.hasViolations()) {
            String incompleteTxt = "";
            if (warnings.isIncomplete()) {
                incompleteTxt = " (checking was prematurely terminated, so the report below may be incomplete)";
            }
            warn(String.join("\n",
                    concat(fmt(
                            "The CIF specification has features that may cause an unexpected resulting state space%s:",
                            incompleteTxt), warnings.createReport())));
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Explore the state space.
        Explorer e;
        try {
            ExplorerBuilder builder = new ExplorerBuilder(spec);
            builder.collectData();
            e = builder.buildExplorer(new ExplorerStateFactory());
            explore(e);
        } catch (ExplorationTerminatedException ex) {
            return 0;
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Remove duplicate transitions of the state space, if requested.
        if (RemoveDuplicateTransitionsOption.isEnabled()) {
            e.removeDuplicateTransitions();
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Write output.
        writeStatisticsOutput(e);
        if (isTerminationRequested()) {
            return 0;
        }

        writeReportOutput(e);
        if (isTerminationRequested()) {
            return 0;
        }

        writeAutomatonOutput(e, spec, cifReader.getAbsDirPath(), "statespace");
        if (isTerminationRequested()) {
            return 0;
        }

        // Done.
        return 0;
    }

    /**
     * Explore the state space of the specification.
     *
     * @param explorer Explorer of the specification.
     */
    private void explore(Explorer explorer) {
        // Get initial states.
        List<BaseState> initials = explorer.getInitialStates(this);
        if (isTerminationRequested()) {
            return;
        }
        if (initials == null || initials.isEmpty()) {
            return;
        }

        // Initialize progress information.
        Integer progressCnt = PrintProgressOption.getProgressCount();
        if (progressCnt != null) {
            printProgress(explorer.states.size(), initials.size());
        }
        int cnt = 0;

        // Explore all reachable states.
        Queue<BaseState> queue = new ArrayDeque<>();
        queue.addAll(initials);
        while (!queue.isEmpty()) {
            // Detect termination request.
            if (isTerminationRequested()) {
                return;
            }

            // Process next state.
            BaseState state = queue.poll();
            queue.addAll(state.getNewSuccessorStates());

            // Show progress information, if requested.
            if (progressCnt != null) {
                cnt++;
                if (cnt == progressCnt) {
                    printProgress(explorer.states.size(), queue.size());
                    cnt = 0;
                }
            }
        }

        if (progressCnt != null) {
            printProgress(explorer.states.size(), 0);
        }

        // Make state numbers consecutive (although they already should be, in
        // this application).
        explorer.renumberStates();
    }

    /**
     * Print progress information.
     *
     * @param found The number of states found so far.
     * @param todo The number of states to process.
     */
    private void printProgress(int found, int todo) {
        if (!OutputProvider.doout()) {
            return;
        }
        OutputProvider.out("Found %,d state%s, %,d state%s to process.", found, (found == 1 ? "" : "s"), todo,
                (todo == 1 ? "" : "s"));
    }

    /**
     * Write statistics of the resulting state space to the console, if requested.
     *
     * @param explorer The explorer used to explore the state space.
     */
    private void writeStatisticsOutput(Explorer explorer) {
        // Skip if not requested.
        if (!EnableStatisticsOption.getStatistics()) {
            return;
        }

        // Get statistics.
        int stateCount = 0;
        int transitionCount = 0;
        if (explorer.states != null) {
            stateCount = explorer.states.size();
            for (BaseState state: explorer.states.keySet()) {
                transitionCount += state.getOutgoingTransitions().size();
            }
        }

        // Write statistics to the console.
        out("Number of states in state space: %,d.", stateCount);
        out("Number of transitions in state space: %,d.", transitionCount);
    }

    /**
     * Write the explored state space in the report format (a format useful for close inspection), if requested.
     *
     * @param explorer The explorer used to explore the state space.
     */
    public static void writeReportOutput(Explorer explorer) {
        // Skip if not requested.
        if (ReportFileOption.getPath() == null && !EnableReportOption.getReport()) {
            return;
        }

        // Get absolute output path.
        String path = ReportFileOption.getDerivedPath(".cif", "_report.txt");
        String absPath = Paths.resolve(path);

        // Write report file.
        AppStream outFile = null;
        try {
            outFile = new FileAppStream(path, absPath);
            if (explorer.states == null || explorer.states.isEmpty()) {
                outFile.println("No initial state found.");
            } else {
                StreamCodeBox box = new StreamCodeBox(outFile);
                boolean first = true;
                for (BaseState state: explorer.states.keySet()) {
                    if (!first) {
                        box.add();
                    }
                    first = false;
                    state.printDebug(box);
                }
            }
        } finally {
            if (outFile != null) {
                outFile.close();
            }
        }
    }

    /**
     * Write the explored state space as data-less CIF automaton, if requested.
     *
     * @param explorer The explorer used to explore the state space.
     * @param spec Original specification.
     * @param specPath The absolute path to the directory that contains the original specification.
     * @param extension Filename extension to use (without separator prefix and {@code .cif} suffix).
     */
    public static void writeAutomatonOutput(Explorer explorer, Specification spec, String specPath, String extension) {
        // Skip if not requested.
        if (OutputFileOption.getPath() == null && !EnableCifOutputOption.getCifOutput()) {
            return;
        }

        // Get absolute output path.
        String path;
        extension = "_" + extension + ".cif";
        path = OutputFileOption.getDerivedPath(".cif", extension);
        String absPath = Paths.resolve(path);

        // Create and write output CIF specification.
        CifAutomatonBuilder cab = new CifAutomatonBuilder();
        spec = cab.createAutomaton(explorer, spec);
        CifWriter.writeCifSpec(spec, new PathPair(path, absPath), specPath);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    /**
     * Options specific to the CIF explorer.
     *
     * @return Options specific to the CIF explorer.
     */
    private OptionCategory getExploreOptionsCategory() {
        List<OptionCategory> subPages = list();

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(EnableEdgeMinimizationOption.class));
        options.add(Options.getInstance(EnableStatisticsOption.class));
        options.add(Options.getInstance(EnableCifOutputOption.class));
        options.add(Options.getInstance(RemoveDuplicateTransitionsOption.class));
        options.add(Options.getInstance(AddStateAnnosOption.class));
        options.add(Options.getInstance(OutputFileOption.class));
        options.add(Options.getInstance(AutomatonNameOption.class));
        options.add(Options.getInstance(EnableReportOption.class));
        options.add(Options.getInstance(ReportFileOption.class));
        options.add(Options.getInstance(PrintProgressOption.class));
        return new OptionCategory("CIF explorer options",
                "Options for exploring a CIF specification to its untimed state space.", subPages, options);
    }

    @Override
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getGeneralOptionCategory());
        subPages.add(getExploreOptionsCategory());

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        String optDesc = "All options for the CIF explorer.";
        return new OptionCategory("CIF explorer options", optDesc, subPages, options);
    }
}
