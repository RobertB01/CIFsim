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

package org.eclipse.escet.cif.cif2uppaal;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.XmlSupport;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.w3c.dom.Document;

/** CIF to UPPAAL transformation application. */
public class CifToUppaalApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifToUppaalApp app = new CifToUppaalApp();
        app.run(args);
    }

    /** Constructor for the {@link CifToUppaalApp} class. */
    public CifToUppaalApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifToUppaalApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifToUppaalApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF to UPPAAL transformer";
    }

    @Override
    public String getAppDescription() {
        return "Transforms CIF files to UPPAAL (*.xml) files.";
    }

    @Override
    protected int runInternal() {
        // Read CIF specification.
        Specification spec = new CifReader().init().read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Get output file path.
        String outPath = OutputFileOption.getDerivedPath(".cif", ".xml");
        outPath = Paths.resolve(outPath);

        // Perform transformation to UPPAAL.
        Document doc = new CifToUppaal().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Write UPPAAL file.
        XmlSupport.writeFile(doc, "UPPAAL", outPath);

        // All done.
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

        List<Option> transOpts = list();
        transOpts.add(Options.getInstance(InputFileOption.class));
        transOpts.add(Options.getInstance(OutputFileOption.class));
        List<OptionCategory> transSubCats = list();
        OptionCategory transCat = new OptionCategory("Transformation", "Transformation options.", transSubCats,
                transOpts);

        List<OptionCategory> cats = list(generalCat, transCat);
        OptionCategory options = new OptionCategory("CIF to UPPAAL Transformer Options",
                "All options for the CIF to UPPAAL transformer.", cats, list());

        return options;
    }
}
