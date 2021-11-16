//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.datasynth.bdd.BddUtils;
import org.eclipse.escet.cif.datasynth.conversion.CifToSynthesisConverter;
import org.eclipse.escet.cif.datasynth.conversion.SynthesisToCifConverter;
import org.eclipse.escet.cif.datasynth.options.BddDebugMaxNodesOption;
import org.eclipse.escet.cif.datasynth.options.BddDebugMaxPathsOption;
import org.eclipse.escet.cif.datasynth.options.BddForceVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddInitNodeTableSizeOption;
import org.eclipse.escet.cif.datasynth.options.BddOpCacheRatioOption;
import org.eclipse.escet.cif.datasynth.options.BddOpCacheSizeOption;
import org.eclipse.escet.cif.datasynth.options.BddOutputNamePrefixOption;
import org.eclipse.escet.cif.datasynth.options.BddOutputOption;
import org.eclipse.escet.cif.datasynth.options.BddSimplifyOption;
import org.eclipse.escet.cif.datasynth.options.BddSlidingWindowSizeOption;
import org.eclipse.escet.cif.datasynth.options.BddSlidingWindowVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddVariableOrderOption;
import org.eclipse.escet.cif.datasynth.options.EventWarnOption;
import org.eclipse.escet.cif.datasynth.options.ForwardReachOption;
import org.eclipse.escet.cif.datasynth.options.SupervisorNameOption;
import org.eclipse.escet.cif.datasynth.options.SupervisorNamespaceOption;
import org.eclipse.escet.cif.datasynth.options.SynthesisStatistics;
import org.eclipse.escet.cif.datasynth.options.SynthesisStatisticsOption;
import org.eclipse.escet.cif.datasynth.spec.SynthesisAutomaton;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.box.GridBox;

import com.github.javabdd.BDDFactory;
import com.github.javabdd.BDDFactory.ContinuousStats;
import com.github.javabdd.JFactory;

/** CIF data-based supervisory controller synthesis application. */
public class CifDataSynthesisApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifDataSynthesisApp app = new CifDataSynthesisApp();
        app.run(args);
    }

    /** Constructor for the {@link CifDataSynthesisApp} class. */
    public CifDataSynthesisApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifDataSynthesisApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifDataSynthesisApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF data-based supervisory controller synthesis tool";
    }

    @Override
    public String getAppDescription() {
        return "Synthesizes a supervisory controller for a CIF specification with data.";
    }

    @Override
    protected int runInternal() {
        // Initialize timing statistics.
        Set<SynthesisStatistics> stats = SynthesisStatisticsOption.getStatistics();
        boolean doTiming = stats.contains(SynthesisStatistics.TIMING);
        CifDataSynthesisTiming timing = new CifDataSynthesisTiming();

        // Do synthesis.
        if (doTiming) {
            timing.total.start();
        }
        try {
            doSynthesis(doTiming, timing);
        } finally {
            // Print timing statistics.
            if (doTiming) {
                timing.total.stop();
                timing.print(AppEnv.getData());
            }
        }

        // All done.
        return 0;
    }

    /**
     * Perform synthesis.
     *
     * @param doTiming Whether to collect timing statistics.
     * @param timing The timing statistics data. Is modified in-place.
     */
    private void doSynthesis(boolean doTiming, CifDataSynthesisTiming timing) {
        // Read option value, to validate it early.
        String supName = SupervisorNameOption.getSupervisorName("sup");
        String supNamespace = SupervisorNamespaceOption.getNamespace();

        // Initialize debugging.
        boolean dbgEnabled = OutputModeOption.getOutputMode() == OutputMode.DEBUG;

        // Read CIF specification.
        String inputPath = InputFileOption.getPath();
        if (dbgEnabled) {
            dbg("Reading CIF file \"%s\".", inputPath);
        }

        CifReader cifReader = new CifReader().init();
        Specification spec;
        if (doTiming) {
            timing.inputRead.start();
        }
        try {
            spec = cifReader.read();
        } finally {
            if (doTiming) {
                timing.inputRead.stop();
            }
        }

        if (isTerminationRequested()) {
            return;
        }

        // Perform preprocessing.
        if (dbgEnabled) {
            dbg("Preprocessing CIF specification.");
        }

        if (doTiming) {
            timing.inputPreProcess.start();
        }
        try {
            // Remove/ignore I/O declarations, to increase the supported subset.
            RemoveIoDecls removeIoDecls = new RemoveIoDecls();
            removeIoDecls.transform(spec);
            if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
                warn("The specification contains CIF/SVG input declarations. These will be ignored.");
            }

            // Eliminate component definition/instantiation, to avoid having to handle them.
            new ElimComponentDefInst().transform(spec);
        } finally {
            if (doTiming) {
                timing.inputPreProcess.stop();
            }
        }

        if (isTerminationRequested()) {
            return;
        }

        // Create BDD factory.
        int bddTableSize = BddInitNodeTableSizeOption.getInitialSize();
        Integer bddCacheSize = BddOpCacheSizeOption.getCacheSize();
        double bddCacheRatio = BddOpCacheRatioOption.getCacheRatio();
        if (bddCacheSize == null) {
            // Initialize BDD cache size using cache ratio.
            bddCacheSize = (int)(bddTableSize * bddCacheRatio);
            if (bddCacheSize < 2) {
                bddCacheSize = 2;
            }
        } else {
            // Disable cache ratio.
            bddCacheRatio = -1;
        }

        BDDFactory factory = JFactory.init(bddTableSize, bddCacheSize);
        if (bddCacheRatio != -1) {
            factory.setCacheRatio(bddCacheRatio);
        }

        Set<SynthesisStatistics> stats = SynthesisStatisticsOption.getStatistics();
        boolean doGcStats = stats.contains(SynthesisStatistics.BDD_GC);
        boolean doResizeStats = stats.contains(SynthesisStatistics.BDD_RESIZE);
        BddUtils.setBddCallbacks(factory, doGcStats, doResizeStats);

        boolean doCacheStats = stats.contains(SynthesisStatistics.BDD_CACHE);
        boolean doContinuousNodesStats = stats.contains(SynthesisStatistics.BDD_CONTINUOUS_NODES);
        boolean doMaxBddNodesStats = stats.contains(SynthesisStatistics.BDD_MAX_NODES);
        if (doCacheStats || doContinuousNodesStats) {
            factory.getCacheStats().enableMeasurements();
        }
        if (doContinuousNodesStats) {
            factory.getContinuousStats().enableMeasurements();
        }
        if (doMaxBddNodesStats) {
            factory.getMaxUsedBddNodesStats().enableMeasurements();
        }

        // Perform synthesis.
        Specification rslt;
        try {
            // Convert CIF specification to synthesis format, checking for
            // precondition violations along the way.
            if (dbgEnabled) {
                dbg("Converting CIF specification to internal format.");
            }
            CifToSynthesisConverter converter1 = new CifToSynthesisConverter();

            SynthesisAutomaton aut;
            if (doTiming) {
                timing.inputConvert.start();
            }
            try {
                aut = converter1.convert(spec, factory, dbgEnabled);
            } finally {
                if (doTiming) {
                    timing.inputConvert.stop();
                }
            }

            if (isTerminationRequested()) {
                return;
            }

            // Perform synthesis.
            if (dbgEnabled) {
                dbg("Starting data-based synthesis.");
            }
            CifDataSynthesis.synthesize(aut, dbgEnabled, doTiming, timing);
            if (isTerminationRequested()) {
                return;
            }

            // Construct output CIF specification.
            if (dbgEnabled) {
                dbg("Constructing output CIF specification.");
            }
            SynthesisToCifConverter converter2 = new SynthesisToCifConverter();

            if (doTiming) {
                timing.outputConvert.start();
            }
            try {
                rslt = converter2.convert(aut, spec, supName, supNamespace);
            } finally {
                if (doTiming) {
                    timing.outputConvert.stop();
                }
            }

            if (isTerminationRequested()) {
                return;
            }

            // Print statistics before we clean up the factory.
            if (doCacheStats) {
                out();
                out(factory.getCacheStats().toString());
            }
            if (doContinuousNodesStats) {
                out();
                printContinuousNodesStats(factory.getContinuousStats());
            }
            if (doMaxBddNodesStats) {
                out();
                out(fmt("Maximum used BDD nodes: %d.", factory.getMaxUsedBddNodesStats().getMaxUsedBddNodes()));
            }

            if (isTerminationRequested()) {
                return;
            }
        } finally {
            // Always clean up the BDD factory.
            factory.done();
        }

        // Write output CIF specification.
        String outPath = OutputFileOption.getDerivedPath(".cif", ".ctrlsys.cif");
        if (dbgEnabled) {
            dbg("Writing output CIF file \"%s\".", outPath);
        }
        outPath = Paths.resolve(outPath);

        if (doTiming) {
            timing.outputWrite.start();
        }
        try {
            CifWriter.writeCifSpec(rslt, outPath, cifReader.getAbsDirPath());
        } finally {
            if (doTiming) {
                timing.outputWrite.stop();
            }
        }

        if (isTerminationRequested()) {
            return;
        }
    }

    /**
     * Print the continuous nodes statistics to the 'normal' output stream.
     *
     * @param cs The continuous statistics to print.
     */
    private void printContinuousNodesStats(ContinuousStats cs) {
        List<Long> operations = cs.getOperationsStats();
        List<Integer> nodes = cs.getNodesStats();
        int numberOfDataPoints = nodes.size();
        GridBox grid = new GridBox(numberOfDataPoints + 1, 2, 0, 1);

        grid.set(0, 0, "Operations");
        grid.set(0, 1, "Used BDD nodes");
        for (int i = 0; i < numberOfDataPoints; i++) {
            grid.set(i + 1, 0, operations.get(i).toString());
            grid.set(i + 1, 1, nodes.get(i).toString());
        }

        // Print the actual content.
        out("Continuous node statistics");
        out("--------------------------");
        for (String line : grid.getLines()) {
            out(line);
        }
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> bddOpts = list();
        bddOpts.add(Options.getInstance(BddOutputOption.class));
        bddOpts.add(Options.getInstance(BddOutputNamePrefixOption.class));
        bddOpts.add(Options.getInstance(BddVariableOrderOption.class));
        bddOpts.add(Options.getInstance(BddForceVarOrderOption.class));
        bddOpts.add(Options.getInstance(BddSlidingWindowVarOrderOption.class));
        bddOpts.add(Options.getInstance(BddSlidingWindowSizeOption.class));
        bddOpts.add(Options.getInstance(BddSimplifyOption.class));
        bddOpts.add(Options.getInstance(BddInitNodeTableSizeOption.class));
        bddOpts.add(Options.getInstance(BddOpCacheSizeOption.class));
        bddOpts.add(Options.getInstance(BddOpCacheRatioOption.class));
        bddOpts.add(Options.getInstance(BddDebugMaxNodesOption.class));
        bddOpts.add(Options.getInstance(BddDebugMaxPathsOption.class));
        OptionCategory bddCat = new OptionCategory("BDD", "BDD options.", list(), bddOpts);

        List<Option> synthOpts = list();
        synthOpts.add(Options.getInstance(InputFileOption.class));
        synthOpts.add(Options.getInstance(OutputFileOption.class));
        synthOpts.add(Options.getInstance(SupervisorNameOption.class));
        synthOpts.add(Options.getInstance(SupervisorNamespaceOption.class));
        synthOpts.add(Options.getInstance(ForwardReachOption.class));
        synthOpts.add(Options.getInstance(SynthesisStatisticsOption.class));
        synthOpts.add(Options.getInstance(EventWarnOption.class));
        OptionCategory synthCat = new OptionCategory("Synthesis", "Synthesis options.", list(bddCat), synthOpts);

        List<OptionCategory> cats = list(generalCat, synthCat);
        OptionCategory options = new OptionCategory("CIF Data-based Synthesis Options",
                "All options for the CIF data-based supervisory controller synthesis tool.", cats, list());

        return options;
    }
}
