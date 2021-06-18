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

package org.eclipse.escet.cif.controllercheck;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.Map;

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
import org.eclipse.escet.cif.controllercheck.finiteresponse.FiniteResponseChecker;
import org.eclipse.escet.cif.controllercheck.options.PrintOutputOption;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.common.app.framework.Application;
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

        // Pre-processing.
        // Cif automata structure normalization.
        new RemoveIoDecls(true, true).transform(spec);
        new ElimComponentDefInst().transform(spec);
        new ElimStateEvtExclInvs().transform(spec);
        new ElimMonitors().transform(spec);
        new ElimSelf().transform(spec);
        new ElimTypeDecls().transform(spec);

        final String varPrefix = "LP_";
        final String enumPrefix = "LOCS_";
        final String litPrefix = "LOC_";
        final boolean considerLocsForRename = true;
        final boolean addInitPreds = true;
        final boolean optimized = false;
        final Map<DiscVariable, String> absVarNamesMap = null;
        final boolean optInits = true;
        ElimLocRefExprs transformer = new ElimLocRefExprs(varPrefix, enumPrefix, litPrefix, considerLocsForRename,
                addInitPreds, optimized, absVarNamesMap, optInits);
        transformer.transform(spec);

        new EnumsToInts().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // CIF automaton edges normalization.
        new ElimIfUpdates().transform(spec);
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

        // Check for finite response.
        OutputProvider.dbg("Checking for finite response...");
        new FiniteResponseChecker().checkSystem(spec);

        return 0;
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
        checkOpts.add(Options.getInstance(PrintOutputOption.class));
        OptionCategory synthCat;
        synthCat = new OptionCategory("Controller properties checks", "Controller properties check options.", list(),
                checkOpts);

        List<OptionCategory> cats = list(generalCat, synthCat);
        OptionCategory options;
        options = new OptionCategory("CIF Controller properties check Options",
                "All options for the CIF controller properties check tool.", cats, list());
        return options;
    }
}
