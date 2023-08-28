//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.cif.controllercheck.confluence.ConfluenceChecker;
import org.eclipse.escet.cif.controllercheck.finiteresponse.FiniteResponseChecker;
import org.eclipse.escet.cif.controllercheck.options.EnableConfluenceChecking;
import org.eclipse.escet.cif.controllercheck.options.EnableFiniteResponseChecking;
import org.eclipse.escet.cif.controllercheck.options.PrintControlLoopsOutputOption;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** Application class for the controller properties check application. */
public class ControllerCheckApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        ControllerCheckApp app = new ControllerCheckApp();
        app.run(args);
    }

    /** Constructor for the {@link ControllerCheckApp} class. */
    public ControllerCheckApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link ControllerCheckApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public ControllerCheckApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF controller properties check tool";
    }

    @Override
    public String getAppDescription() {
        return "Verifies whether a set of CIF automata meet the requirements for a controller.";
    }

    @Override
    protected int runInternal() {
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

        // Pre-processing.
        // CIF automata structure normalization.
        new ElimComponentDefInst().transform(spec);
        new ElimStateEvtExclInvs().transform(spec);
        new ElimMonitors().transform(spec);
        new ElimSelf().transform(spec);
        new ElimTypeDecls().transform(spec);

        final Function<Automaton, String> varNamingFunction = a -> "LP_" + a.getName();
        final Function<Automaton, String> enumNamingFunction = a -> "LOCS_" + a.getName();
        final Function<Location, String> litNamingFunction = l -> "LOC_" + l.getName();
        final boolean considerLocsForRename = true;
        final boolean addInitPreds = true;
        final boolean optimized = false;
        final Map<DiscVariable, String> lpVarToAbsAutNameMap = null;
        final boolean optInits = true;
        final boolean addEdgeGuards = true;
        new ElimLocRefExprs(varNamingFunction, enumNamingFunction, litNamingFunction, considerLocsForRename,
                addInitPreds, optimized, lpVarToAbsAutNameMap, optInits, addEdgeGuards).transform(spec);

        new EnumsToInts().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Simplify expressions.
        new ElimAlgVariables().transform(spec);
        new ElimConsts().transform(spec);
        new SimplifyValues().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Pre-check.
        new ControllerCheckPreChecker().check(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Eliminate if updates, does not support multi-assignments or partial variable assignments.
        new ElimIfUpdates().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Non-determinism check.
        new ControllerCheckDeterminismChecker().check(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Determine checks to perform.
        boolean checkFiniteResponse = EnableFiniteResponseChecking.checkFiniteResponse();
        boolean checkConfluence = EnableConfluenceChecking.checkConfluence();

        // Ensure at least one check is enabled.
        if (!checkFiniteResponse && !checkConfluence) {
            throw new InvalidOptionException(
                    "No checks enabled. Enable one of the checks for the controller property checker to check.");
        }

        // Perform computations for both checkers.
        boolean computeGlobalGuardedUpdates = checkConfluence;
        PrepareChecks prepareChecks = new PrepareChecks(computeGlobalGuardedUpdates);
        if (!prepareChecks.compute(spec)) {
            return 0; // Termination requested.
        }

        // Warn if specification doesn't look very useful.
        if (prepareChecks.getAutomata().isEmpty()) {
            warn("The specification contains no automata.");
        } else if (prepareChecks.getControllableEvents().isEmpty()) {
            warn("The specification contains no used controllable events.");
        }

        // Check finite response.
        CheckConclusion finiteResponseConclusion = null;
        boolean finiteResponseHolds;
        if (checkFiniteResponse) {
            // Check the finite response property.
            OutputProvider.out("Checking for finite response...");
            finiteResponseConclusion = new FiniteResponseChecker().checkSystem(prepareChecks);
            if (finiteResponseConclusion == null || isTerminationRequested()) {
                return 0;
            }
            finiteResponseHolds = finiteResponseConclusion.propertyHolds();
        } else {
            finiteResponseHolds = true; // Don't invalidate confluence checking result.
        }

        // Check confluence.
        CheckConclusion confluenceConclusion = null;
        boolean confluenceHolds;
        if (checkConfluence) {
            // Check the confluence property.
            OutputProvider.out();
            OutputProvider.out("Checking for confluence...");
            confluenceConclusion = new ConfluenceChecker().checkSystem(prepareChecks);
            if (confluenceConclusion == null || isTerminationRequested()) {
                return 0;
            }
            confluenceHolds = confluenceConclusion.propertyHolds();
        } else {
            confluenceHolds = true; // Don't invalidate finite response checking result.
        }

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

        // Return the application exit code, indicating whether the specification satisfies the checks.
        return (finiteResponseHolds && confluenceHolds) ? 0 : 1;
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
        checksCat = new OptionCategory("Checks", "Controller properties check options.", list(), checkOpts);

        List<OptionCategory> cats = list(generalCat, checksCat);
        OptionCategory options;
        options = new OptionCategory("CIF Controller properties check Options",
                "All options for the CIF controller properties check tool.", cats, list());
        return options;
    }
}
