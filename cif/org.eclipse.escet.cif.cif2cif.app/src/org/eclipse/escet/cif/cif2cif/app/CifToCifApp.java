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

package org.eclipse.escet.cif.cif2cif.app;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.cif2cif.CifToCifPreconditionException;
import org.eclipse.escet.cif.cif2cif.CifToCifTransformation;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;

/** CIF to CIF transformation application. */
public class CifToCifApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifToCifApp app = new CifToCifApp();
        app.run(args, true);
    }

    /** Constructor for the {@link CifToCifApp} class. */
    public CifToCifApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifToCifApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public CifToCifApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF transformer";
    }

    @Override
    public String getAppDescription() {
        return "Transforms CIF files by applying some CIF to CIF transformations.";
    }

    @Override
    protected int runInternal() {
        // Read CIF specification.
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Apply transformations.
        List<CifToCifTransformation> transformations;
        transformations = CifToCifTransOption.getTransformations();
        for (CifToCifTransformation trans: transformations) {
            try {
                trans.transform(spec);
                if (isTerminationRequested()) {
                    return 0;
                }
            } catch (CifToCifPreconditionException ex) {
                String msg = fmt("File \"%s\": Unsatisfied precondition for CIF to CIF transformation.",
                        InputFileOption.getPath());
                throw new InvalidInputException(msg, ex);
            }
        }

        // Write output file.
        String outPath = OutputFileOption.getDerivedPath(".cif", ".transformed.cif");
        outPath = Paths.resolve(outPath);
        CifWriter.writeCifSpec(spec, outPath, cifReader.getAbsDirPath());

        // All done.
        return 0;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected OptionCategory getAllOptions() {
        OptionCategory generalOpts = getGeneralOptionCategory();

        OptionCategory transOpts = new OptionCategory("Transformations", "Transformation options.", list(),
                list(Options.getInstance(InputFileOption.class), Options.getInstance(OutputFileOption.class),
                        Options.getInstance(CifToCifTransOption.class)));

        OptionCategory options = new OptionCategory("CIF Transformer Options", "All options for the CIF transformer.",
                list(generalOpts, transOpts), list());

        return options;
    }
}
