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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.controllercheck.options.EnableBoundedResponseChecking;
import org.eclipse.escet.cif.controllercheck.options.EnableConfluenceChecking;
import org.eclipse.escet.cif.controllercheck.options.EnableFiniteResponseChecking;
import org.eclipse.escet.cif.controllercheck.options.EnableNonBlockingUnderControlChecking;
import org.eclipse.escet.cif.controllercheck.options.PrintControlLoopsOutputOption;
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
        // Configure settings.
        ControllerCheckerSettings settings = new ControllerCheckerSettings();
        settings.setCheckBoundedResponse(EnableBoundedResponseChecking.checkBoundedResponse());
        settings.setCheckConfluence(EnableConfluenceChecking.checkConfluence());
        settings.setCheckFiniteResponse(EnableFiniteResponseChecking.checkFiniteResponse());
        settings.setCheckNonBlockingUnderControl(EnableNonBlockingUnderControlChecking.checkNonBlockingUnderControl());
        settings.setPrintFiniteResponseControlLoops(PrintControlLoopsOutputOption.isPrintControlLoopsEnabled());
        settings.setTermination(() -> isTerminationRequested());
        settings.setNormalOutput(OutputProvider.getNormalOutputStream());
        settings.setDebugOutput(OutputProvider.getDebugOutputStream());
        settings.setWarnOutput(OutputProvider.getWarningOutputStream());

        // Check settings.
        settings.check();

        // Load specification.
        OutputProvider.dbg("Loading CIF specification \"%s\"...", InputFileOption.getPath());
        CifReader cifReader = new CifReader();
        Specification spec = cifReader.init().read();
        String specAbsPath = Paths.resolve(InputFileOption.getPath());
        if (isTerminationRequested()) {
            return 0;
        }

        // Perform checks.
        ControllerCheckerResult result = ControllerChecker.performChecks(spec, specAbsPath, settings);

        // Update the input specification based with the results of the checks. If a check was not performed, don't
        // update the annotation for that check, but keep the existing result. That way, we can do checks one by one, or
        // we can only redo a certain check.
        result.updateSpecification(spec);

        // Check CIF specification to output.
        CifToolPostCheckEnv env = new CifToolPostCheckEnv(cifReader.getAbsDirPath(), "output");
        try {
            new CifAnnotationsPostChecker(env).check(spec);
        } catch (SemanticException ex) {
            // Ignore.
        }
        env.throwUnsupportedExceptionIfAnyErrors("Checking the specification for the requested properties failed.");

        // Write the output file.
        String outPath = OutputFileOption.getDerivedPath(".cif", ".checked.cif");
        String absOutPath = Paths.resolve(outPath);
        CifWriter.writeCifSpec(spec, new PathPair(outPath, absOutPath), cifReader.getAbsDirPath());
        out();
        out("The model with the check results has been written to \"%s\".", outPath);

        // Return the application exit code, indicating whether the specification satisfies all the checks that were
        // performed, and thus none of the checks that were performed have failed.
        return result.noFailureFound() ? 0 : 1;
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
        checkOpts.add(Options.getInstance(EnableBoundedResponseChecking.class));
        checkOpts.add(Options.getInstance(EnableNonBlockingUnderControlChecking.class));
        checkOpts.add(Options.getInstance(EnableFiniteResponseChecking.class));
        checkOpts.add(Options.getInstance(PrintControlLoopsOutputOption.class));
        checkOpts.add(Options.getInstance(EnableConfluenceChecking.class));
        checkOpts.add(Options.getInstance(OutputFileOption.class));

        OptionCategory checksCat;
        checksCat = new OptionCategory("Checks", "Controller properties checker options.", list(), checkOpts);

        List<OptionCategory> cats = list(generalCat, checksCat);
        OptionCategory options;
        options = new OptionCategory("CIF Controller properties checker Options",
                "All options for the CIF controller properties checker tool.", cats, list());
        return options;
    }
}
