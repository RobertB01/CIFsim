//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.iout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.escet.cif.cif2cif.ElimAlgVariables;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimConsts;
import org.eclipse.escet.cif.cif2cif.ElimIfUpdates;
import org.eclipse.escet.cif.cif2cif.ElimLocRefExprs;
import org.eclipse.escet.cif.cif2cif.ElimMonitors;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.ElimStateEvtExclInvs;
import org.eclipse.escet.cif.cif2cif.ElimTypeDecls;
import org.eclipse.escet.cif.cif2cif.EnumsToInts;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.controllercheck.confluence.ConfluenceChecker;
import org.eclipse.escet.cif.controllercheck.finiteresponse.FiniteResponseChecker;
import org.eclipse.escet.cif.controllercheck.mdd.MddDeterminismChecker;
import org.eclipse.escet.cif.controllercheck.mdd.MddPreChecker;
import org.eclipse.escet.cif.controllercheck.mdd.MddPrepareChecks;
import org.eclipse.escet.cif.controllercheck.options.EnableConfluenceChecking;
import org.eclipse.escet.cif.controllercheck.options.EnableFiniteResponseChecking;
import org.eclipse.escet.cif.controllercheck.options.PrintControlLoopsOutputOption;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** Controller properties checker application. */
public class ControllerCheckerApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        ControllerCheckerApp app = new ControllerCheckerApp();
        app.run(args, true);
    }

    /** Constructor for the {@link ControllerCheckerApp} class. */
    public ControllerCheckerApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link ControllerCheckerApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public ControllerCheckerApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF controller properties checker tool";
    }

    @Override
    public String getAppDescription() {
        return "Checks whether a CIF specification meets certain properties for being a proper controller.";
    }

    @Override
    protected int runInternal() {
        // Determine checks to perform.
        boolean checkFiniteResponse = EnableFiniteResponseChecking.checkFiniteResponse();
        boolean checkConfluence = EnableConfluenceChecking.checkConfluence();
        boolean hasMddBasedChecks = checkFiniteResponse || checkConfluence;

        // Ensure at least one check is enabled.
        if (!checkFiniteResponse && !checkConfluence) {
            throw new InvalidOptionException(
                    "No checks enabled. Enable one of the checks for the controller properties checker to check.");
        }

        // Load specification.
        OutputProvider.dbg("Loading CIF specification \"%s\"...", InputFileOption.getPath());
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warn("The specification contains CIF/SVG input declarations. These will be ignored.");
        }

        // Warn if specification doesn't look very useful.
        Set<Event> specAlphabet = CifEventUtils.getAlphabet(spec);
        if (specAlphabet.stream().allMatch(e -> e.getControllable() != null && !e.getControllable())) {
            warn("The alphabet of the specification contains no controllable events.");
        }
        if (specAlphabet.stream().allMatch(e -> e.getControllable() != null && e.getControllable())) {
            warn("The alphabet of the specification contains no uncontrollable events.");
        }

        // Prepare for the checks.
        Specification mddSpec = null; // Used for MDD-based checks.
        MddPrepareChecks prepareChecks = null; // Used for MDD-based checks.

        // Preparations for MDD-based checks.
        if (hasMddBasedChecks) {
            OutputProvider.dbg("Preparing for MDD-based checks...");

            // Use a dedicated copy of the specification.
            mddSpec = EMFHelper.deepclone(spec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Pre-processing.
            // CIF automata structure normalization.
            new ElimComponentDefInst().transform(mddSpec);
            new ElimStateEvtExclInvs().transform(mddSpec);
            new ElimMonitors().transform(mddSpec);
            new ElimSelf().transform(mddSpec);
            new ElimTypeDecls().transform(mddSpec);

            final Function<Automaton, String> varNamingFunction = a -> "LP_" + a.getName();
            final Function<Automaton, String> enumNamingFunction = a -> "LOCS_" + a.getName();
            final Function<Location, String> litNamingFunction = l -> "LOC_" + l.getName();
            final boolean considerLocsForRename = true;
            final boolean addInitPreds = true;
            final boolean optimized = false;
            final Map<DiscVariable, String> lpVarToAbsAutNameMap = null;
            final boolean optInits = true;
            final boolean addEdgeGuards = true;
            final boolean copyAutAnnosToEnum = false;
            final boolean copyLocAnnosToEnumLits = false;
            new ElimLocRefExprs(varNamingFunction, enumNamingFunction, litNamingFunction, considerLocsForRename,
                    addInitPreds, optimized, lpVarToAbsAutNameMap, optInits, addEdgeGuards, copyAutAnnosToEnum,
                    copyLocAnnosToEnumLits).transform(mddSpec);

            new EnumsToInts().transform(mddSpec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Simplify expressions.
            new ElimAlgVariables().transform(mddSpec);
            new ElimConsts().transform(mddSpec);
            new SimplifyValues().transform(mddSpec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Pre-check.
            new MddPreChecker().check(mddSpec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Eliminate if updates, does not support multi-assignments or partial variable assignments.
            new ElimIfUpdates().transform(mddSpec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Non-determinism check.
            new MddDeterminismChecker().check(mddSpec);
            if (isTerminationRequested()) {
                return 0;
            }

            // Perform computations for both checkers.
            boolean computeGlobalGuardedUpdates = checkConfluence;
            prepareChecks = new MddPrepareChecks(computeGlobalGuardedUpdates);
            if (!prepareChecks.compute(mddSpec)) {
                return 0; // Termination requested.
            }

            // Warn if specification doesn't look very useful.
            if (prepareChecks.getAutomata().isEmpty()) {
                warn("The specification contains no automata.");
            }
        }

        // Common initialization for the checks.
        boolean dbgEnabled = OutputModeOption.getOutputMode() == OutputMode.DEBUG;
        int checksPerformed = 0;
        boolean allChecksHold = true;

        // Check finite response.
        CheckConclusion finiteResponseConclusion = null;
        boolean finiteResponseHolds = true; // Is true if it holds or was not checked, false otherwise.
        if (checkFiniteResponse) {
            // Check the finite response property.
            if (dbgEnabled || checksPerformed > 0) {
                OutputProvider.out();
            }
            OutputProvider.out("Checking for finite response...");
            finiteResponseConclusion = new FiniteResponseChecker().checkSystem(prepareChecks);
            checksPerformed++;
            if (finiteResponseConclusion == null || isTerminationRequested()) {
                return 0;
            }
            finiteResponseHolds = finiteResponseConclusion.propertyHolds();
        }
        allChecksHold &= finiteResponseHolds;

        // Check confluence.
        CheckConclusion confluenceConclusion = null;
        boolean confluenceHolds = true; // Is true if it holds or was not checked, false otherwise.
        if (checkConfluence) {
            // Check the confluence property.
            if (dbgEnabled || checksPerformed > 0) {
                OutputProvider.out();
            }
            OutputProvider.out("Checking for confluence...");
            confluenceConclusion = new ConfluenceChecker().checkSystem(prepareChecks);
            checksPerformed++;
            if (confluenceConclusion == null || isTerminationRequested()) {
                return 0;
            }
            confluenceHolds = confluenceConclusion.propertyHolds();
        }
        allChecksHold &= confluenceHolds;

        // Output the checker conclusions.
        out();
        out("CONCLUSION:");

        iout();
        if (finiteResponseConclusion != null) {
            finiteResponseConclusion.printDetails();
        } else {
            out("Finite response checking was disabled, finite response property is unknown.");
        }
        dout();

        if (!finiteResponseHolds || !confluenceHolds) {
            out(); // Empty line between conclusions if an error occurs.
        }

        iout();
        if (confluenceConclusion != null) {
            confluenceConclusion.printDetails();
        } else {
            out("Confluence checking was disabled, confluence property is unknown.");
        }
        dout();

        // Return the application exit code, indicating whether the specification satisfies all the checks that were
        // performed.
        return allChecksHold ? 0 : 1;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> checkOpts = list();
        checkOpts.add(Options.getInstance(InputFileOption.class));
        checkOpts.add(Options.getInstance(EnableFiniteResponseChecking.class));
        checkOpts.add(Options.getInstance(PrintControlLoopsOutputOption.class));
        checkOpts.add(Options.getInstance(EnableConfluenceChecking.class));

        OptionCategory checksCat;
        checksCat = new OptionCategory("Checks", "Controller properties checker options.", list(), checkOpts);

        List<OptionCategory> cats = list(generalCat, checksCat);
        OptionCategory options;
        options = new OptionCategory("CIF Controller properties checker Options",
                "All options for the CIF controller properties checker tool.", cats, list());
        return options;
    }
}
