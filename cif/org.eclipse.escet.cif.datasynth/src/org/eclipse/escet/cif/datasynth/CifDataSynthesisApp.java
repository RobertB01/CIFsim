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

package org.eclipse.escet.cif.datasynth;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.bdd.conversion.CifToBddConverter;
import org.eclipse.escet.cif.bdd.settings.AllowNonDeterminism;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.bdd.utils.BddUtils;
import org.eclipse.escet.cif.datasynth.conversion.SynthesisToCifConverter;
import org.eclipse.escet.cif.datasynth.options.BddAdvancedVariableOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddDcshVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddDebugMaxNodesOption;
import org.eclipse.escet.cif.datasynth.options.BddDebugMaxPathsOption;
import org.eclipse.escet.cif.datasynth.options.BddForceVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddHyperEdgeAlgoOption;
import org.eclipse.escet.cif.datasynth.options.BddInitNodeTableSizeOption;
import org.eclipse.escet.cif.datasynth.options.BddOpCacheRatioOption;
import org.eclipse.escet.cif.datasynth.options.BddOpCacheSizeOption;
import org.eclipse.escet.cif.datasynth.options.BddOutputNamePrefixOption;
import org.eclipse.escet.cif.datasynth.options.BddOutputOption;
import org.eclipse.escet.cif.datasynth.options.BddSimplifyOption;
import org.eclipse.escet.cif.datasynth.options.BddSlidingWindowSizeOption;
import org.eclipse.escet.cif.datasynth.options.BddSlidingWindowVarOrderOption;
import org.eclipse.escet.cif.datasynth.options.BddVariableOrderOption;
import org.eclipse.escet.cif.datasynth.options.ContinuousPerformanceStatisticsFileOption;
import org.eclipse.escet.cif.datasynth.options.EdgeGranularityOption;
import org.eclipse.escet.cif.datasynth.options.EdgeOrderBackwardOption;
import org.eclipse.escet.cif.datasynth.options.EdgeOrderDuplicateEventsOption;
import org.eclipse.escet.cif.datasynth.options.EdgeOrderForwardOption;
import org.eclipse.escet.cif.datasynth.options.EdgeOrderOption;
import org.eclipse.escet.cif.datasynth.options.EdgeWorksetAlgoOption;
import org.eclipse.escet.cif.datasynth.options.EventWarnOption;
import org.eclipse.escet.cif.datasynth.options.FixedPointComputationsOrderOption;
import org.eclipse.escet.cif.datasynth.options.ForwardReachOption;
import org.eclipse.escet.cif.datasynth.options.PlantsRefReqsWarnOption;
import org.eclipse.escet.cif.datasynth.options.StateReqInvEnforceOption;
import org.eclipse.escet.cif.datasynth.options.SupervisorNameOption;
import org.eclipse.escet.cif.datasynth.options.SupervisorNamespaceOption;
import org.eclipse.escet.cif.datasynth.options.SynthesisStatisticsOption;
import org.eclipse.escet.cif.datasynth.settings.CifDataSynthesisSettings;
import org.eclipse.escet.cif.datasynth.settings.SynthesisStatistics;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.typechecker.postchk.CifAnnotationsPostChecker;
import org.eclipse.escet.cif.typechecker.postchk.CifToolPostCheckEnv;
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
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.typechecker.SemanticException;

import com.github.javabdd.BDDFactory;

/** CIF data-based supervisory controller synthesis application. */
public class CifDataSynthesisApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifDataSynthesisApp app = new CifDataSynthesisApp();
        app.run(args, true);
    }

    /** Constructor for the {@link CifDataSynthesisApp} class. */
    public CifDataSynthesisApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifDataSynthesisApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
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
        // Construct settings. Do it early, to validate settings early.
        //
        // Do not allow non-determinism for controllable events. An external supervisor can't force the correct edge to
        // be taken, if only the updates (includes location pointer variable assignment for target location) are
        // different. For uncontrollable events non-determinism is not a problem, as the supervisor won't restrict edges
        // for uncontrollable events.
        CifDataSynthesisSettings settings = new CifDataSynthesisSettings();

        settings.setTermination(() -> isTerminationRequested());
        settings.setDebugOutput(OutputProvider.getDebugOutputStream());
        settings.setNormalOutput(OutputProvider.getNormalOutputStream());
        settings.setWarnOutput(OutputProvider.getWarningOutputStream());
        settings.setIndentAmount(4);
        settings.setDoPlantsRefReqsWarn(PlantsRefReqsWarnOption.isEnabled());
        settings.setAllowNonDeterminism(AllowNonDeterminism.UNCONTROLLABLE);
        settings.setBddInitNodeTableSize(BddInitNodeTableSizeOption.getInitialSize());
        settings.setBddOpCacheRatio(BddOpCacheRatioOption.getCacheRatio());
        settings.setBddOpCacheSize(BddOpCacheSizeOption.getCacheSize());
        settings.setBddVarOrderInit(BddVariableOrderOption.getOrder());
        settings.setBddDcshEnabled(BddDcshVarOrderOption.isEnabled());
        settings.setBddForceEnabled(BddForceVarOrderOption.isEnabled());
        settings.setBddSlidingWindowEnabled(BddSlidingWindowVarOrderOption.isEnabled());
        settings.setBddSlidingWindowMaxLen(BddSlidingWindowSizeOption.getMaxLen());
        settings.setBddVarOrderAdvanced(BddAdvancedVariableOrderOption.getOrder());
        settings.setBddHyperEdgeAlgo(BddHyperEdgeAlgoOption.getAlgo());
        settings.setBddDebugMaxNodes(BddDebugMaxNodesOption.getMaximum());
        settings.setBddDebugMaxPaths(BddDebugMaxPathsOption.getMaximum());
        settings.setEdgeGranularity(EdgeGranularityOption.getGranularity());
        settings.setEdgeOrderBackward(EdgeOrderBackwardOption.getOrder());
        settings.setEdgeOrderForward(EdgeOrderForwardOption.getOrder());
        settings.setEdgeOrderAllowDuplicateEvents(EdgeOrderDuplicateEventsOption.getAllowance());
        settings.setDoUseEdgeWorksetAlgo(EdgeWorksetAlgoOption.isEnabled());
        settings.setCifBddStatistics(SynthesisStatistics.toCifBdd(SynthesisStatisticsOption.getStatistics()));

        settings.setDoNeverEnabledEventsWarn(EventWarnOption.isEnabled());
        settings.setStateReqInvEnforceMode(StateReqInvEnforceOption.getMode());
        settings.setFixedPointComputationsOrder(FixedPointComputationsOrderOption.getOrder());
        settings.setDoForwardReach(ForwardReachOption.isEnabled());
        settings.setSupervisorName(SupervisorNameOption.getSupervisorName());
        settings.setSupervisorNamespace(SupervisorNamespaceOption.getNamespace());
        settings.setBddOutputMode(BddOutputOption.getMode());
        settings.setBddOutputNamePrefix(BddOutputNamePrefixOption.getPrefix());
        settings.setBddSimplifications(BddSimplifyOption.getSimplifications());
        settings.setSynthesisStatistics(SynthesisStatisticsOption.getStatistics());
        settings.setContinuousPerformanceStatisticsFilePath(ContinuousPerformanceStatisticsFileOption.getPath());
        settings.setContinuousPerformanceStatisticsFileAbsPath(
                Paths.resolve(ContinuousPerformanceStatisticsFileOption.getPath()));

        settings.setModificationAllowed(false);

        // Initialize timing statistics.
        boolean doTiming = settings.getSynthesisStatistics().contains(SynthesisStatistics.TIMING);
        CifDataSynthesisTiming timing = new CifDataSynthesisTiming();

        // Do synthesis.
        if (doTiming) {
            timing.total.start();
        }
        try {
            doSynthesis(settings, doTiming, timing);
        } finally {
            // Print timing statistics.
            if (doTiming) {
                timing.total.stop();
                timing.print(settings.getDebugOutput(), settings.getNormalOutput());
            }
        }

        // All done.
        return 0;
    }

    /**
     * Perform synthesis.
     *
     * @param settings The settings to use.
     * @param doTiming Whether to collect timing statistics.
     * @param timing The timing statistics data. Is modified in-place.
     */
    private void doSynthesis(CifDataSynthesisSettings settings, boolean doTiming, CifDataSynthesisTiming timing) {
        // Initialize debugging.
        boolean dbgEnabled = settings.getDebugOutput().isEnabled();

        // Read CIF specification.
        String inputPath = InputFileOption.getPath();
        String absInputPath = Paths.resolve(inputPath);
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
        CifToBddConverter converter1 = new CifToBddConverter("Data-based supervisory controller synthesis");
        try {
            converter1.preprocess(spec, absInputPath, settings.getWarnOutput(),
                    settings.getDoPlantsRefReqsWarn(), () -> isTerminationRequested());
        } finally {
            if (doTiming) {
                timing.inputPreProcess.stop();
            }
        }

        if (isTerminationRequested()) {
            return;
        }

        // Create BDD factory.
        List<Long> continuousOpMisses = list();
        List<Integer> continuousUsedBddNodes = list();
        BDDFactory factory = CifToBddConverter.createFactory(settings, continuousOpMisses, continuousUsedBddNodes);

        // Perform synthesis.
        Specification rslt;
        try {
            // Convert CIF specification to a CIF/BDD representation, checking for precondition violations along the
            // way.
            if (dbgEnabled) {
                dbg("Converting CIF specification to internal format.");
            }

            CifBddSpec cifBddSpec;
            if (doTiming) {
                timing.inputConvert.start();
            }
            try {
                converter1.setNeedEmptyDebugLine();
                cifBddSpec = converter1.convert(spec, settings, factory);
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
            CifDataSynthesisResult synthResult = CifDataSynthesis.synthesize(cifBddSpec, settings, timing);
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
                rslt = converter2.convert(synthResult, spec);
            } finally {
                if (doTiming) {
                    timing.outputConvert.stop();
                }
            }

            if (isTerminationRequested()) {
                return;
            }

            // Print statistics before we clean up the factory.
            BddUtils.printStats(factory, settings, continuousOpMisses, continuousUsedBddNodes,
                    settings.getContinuousPerformanceStatisticsFilePath(),
                    settings.getContinuousPerformanceStatisticsFileAbsPath());

            if (isTerminationRequested()) {
                return;
            }
        } finally {
            // Always clean up the BDD factory.
            factory.done();
        }

        // Check CIF specification to output.
        CifToolPostCheckEnv env = new CifToolPostCheckEnv(cifReader.getAbsDirPath(), "synthesized");
        try {
            new CifAnnotationsPostChecker(env).check(spec);
        } catch (SemanticException ex) {
            // Ignore.
        }
        env.throwUnsupportedExceptionIfAnyErrors("Supervisory controller synthesis failed.");

        // Write output CIF specification.
        String outPath = OutputFileOption.getDerivedPath(".cif", ".ctrlsys.cif");
        if (dbgEnabled) {
            dbg("Writing output CIF file \"%s\".", outPath);
        }
        String absOutPath = Paths.resolve(outPath);

        if (doTiming) {
            timing.outputWrite.start();
        }
        try {
            CifWriter.writeCifSpec(rslt, new PathPair(outPath, absOutPath), cifReader.getAbsDirPath());
        } finally {
            if (doTiming) {
                timing.outputWrite.stop();
            }
        }

        if (isTerminationRequested()) {
            return;
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
        bddOpts.add(Options.getInstance(BddHyperEdgeAlgoOption.class));
        bddOpts.add(Options.getInstance(BddDcshVarOrderOption.class));
        bddOpts.add(Options.getInstance(BddForceVarOrderOption.class));
        bddOpts.add(Options.getInstance(BddSlidingWindowVarOrderOption.class));
        bddOpts.add(Options.getInstance(BddSlidingWindowSizeOption.class));
        bddOpts.add(Options.getInstance(BddAdvancedVariableOrderOption.class));
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
        synthOpts.add(Options.getInstance(FixedPointComputationsOrderOption.class));
        synthOpts.add(Options.getInstance(EdgeGranularityOption.class));
        synthOpts.add(Options.getInstance(EdgeOrderOption.class)); // No longer supported.
        synthOpts.add(Options.getInstance(EdgeOrderBackwardOption.class));
        synthOpts.add(Options.getInstance(EdgeOrderForwardOption.class));
        synthOpts.add(Options.getInstance(EdgeOrderDuplicateEventsOption.class));
        synthOpts.add(Options.getInstance(EdgeWorksetAlgoOption.class));
        synthOpts.add(Options.getInstance(StateReqInvEnforceOption.class));
        synthOpts.add(Options.getInstance(SynthesisStatisticsOption.class));
        synthOpts.add(Options.getInstance(ContinuousPerformanceStatisticsFileOption.class));
        synthOpts.add(Options.getInstance(EventWarnOption.class));
        synthOpts.add(Options.getInstance(PlantsRefReqsWarnOption.class));
        OptionCategory synthCat = new OptionCategory("Synthesis", "Synthesis options.", list(bddCat), synthOpts);

        List<OptionCategory> cats = list(generalCat, synthCat);
        OptionCategory options = new OptionCategory("CIF Data-based Synthesis Options",
                "All options for the CIF data-based supervisory controller synthesis tool.", cats, list());

        return options;
    }
}
