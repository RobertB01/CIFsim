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

package org.eclipse.escet.cif.multilevel;

import java.util.List;

import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.RemoveCifSvgDecls;
import org.eclipse.escet.cif.cif2cif.RemovePrintDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesOptimized;
import org.eclipse.escet.cif.cif2cif.SwitchesToIfs;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.multilevel.ciftodmm.CifRelations;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** CIF multi-level synthesis application. */
public class MultilevelApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        MultilevelApp app = new MultilevelApp();
        app.run(args);
    }

    /** Constructor of the {@link MultilevelApp} class. */
    public MultilevelApp() {
        // Nothing to do.
    }

    /**
     * Constructor of the {@link MultilevelApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public MultilevelApp(AppStreams streams) {
        super(streams);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected int runInternal() {
        // Load the provided CIF specification.
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Specification transformations.
        new ElimComponentDefInst().transform(spec);
        new ElimSelf().transform(spec);
        new RemoveCifSvgDecls().transform(spec);
        new RemovePrintDecls().transform(spec);
        new SwitchesToIfs().transform(spec);
        new AddDefaultInitialValues().transform(spec);
        new SimplifyValuesOptimized().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Verify pre-conditions.
        // TODO: CifToDmm prechecker only verifies conditions needed for the CIF to DMM conversion, other parts of the
        // TODO: multilevel application need more or other checks.
        CifToDmm.checkSpec(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // CIF to DMM conversion.
        CifRelations cifRelations = CifToDmm.convertToDmms(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        if (WriteDMMsOption.writeDmms()) {
            String outPath = OutputFileOption.getDerivedPath(".cif", ".dmms");
            cifRelations.writeDmms(InputFileOption.getPath(), outPath);
        }
        if (isTerminationRequested()) {
            return 0;
        }

        OutputProvider.warn("Multi-level synthesis not yet implemented.");
        return 0;
    }

    @Override
    public String getAppName() {
        return "CIF multi-level synthesis";
    }

    @Override
    public String getAppDescription() {
        return "Performs synthesis by making several smaller co-operating supervisors.";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> programOpts = List.of(Options.getInstance(InputFileOption.class),
                Options.getInstance(OutputFileOption.class), Options.getInstance(WriteDMMsOption.class));
        OptionCategory programCat = new OptionCategory("Multi-level synthesis", "Multi-level synthesis options.",
                List.of(), programOpts);

        return new OptionCategory("CIF Multi-level Synthesis Options",
                "All options for the CIF multi-level synthesis tool.", List.of(generalCat, programCat), List.of());
    }
}
