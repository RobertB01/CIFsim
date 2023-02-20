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

package org.eclipse.escet.cif.cif2supremica;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.Locale;

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
import org.eclipse.escet.common.java.Strings;
import org.w3c.dom.Document;

/** CIF to Supremica transformation application. */
public class CifToSupremicaApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifToSupremicaApp app = new CifToSupremicaApp();
        app.run(args);
    }

    /** Constructor for the {@link CifToSupremicaApp} class. */
    public CifToSupremicaApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifToSupremicaApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifToSupremicaApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF to Supremica transformer";
    }

    @Override
    public String getAppDescription() {
        return "Transforms CIF files to Supremica (*.wmod) files.";
    }

    @Override
    protected int runInternal() {
        // Read CIF specification.
        Specification spec = new CifReader().init().read();
        String absSpecPath = Paths.resolve(InputFileOption.getPath());
        if (isTerminationRequested()) {
            return 0;
        }

        // Get output file path.
        String outPath = OutputFileOption.getDerivedPath(".cif", ".wmod");
        outPath = Paths.resolve(outPath);

        // Get Supremica module name.
        String moduleName = ModuleNameOption.getModuleName();
        if (moduleName == null) {
            // Derive Supremica module name from output file name.
            moduleName = Paths.getFileName(outPath);
            if (moduleName.toLowerCase(Locale.US).endsWith(".wmod")) {
                moduleName = Strings.slice(moduleName, 0, -".wmod".length());
            }
        }

        // Should enumerations be eliminated?
        boolean elimEnums = ElimEnumsOption.elimEnums();

        // Perform transformation to Supremica.
        Document doc = CifToSupremica.transform(spec, absSpecPath, moduleName, elimEnums);
        if (isTerminationRequested()) {
            return 0;
        }

        // Write Supremica file.
        XmlSupport.writeFile(doc, "Supremica", outPath);

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
        transOpts.add(Options.getInstance(ModuleNameOption.class));
        transOpts.add(Options.getInstance(ElimEnumsOption.class));
        List<OptionCategory> transSubCats = list();
        OptionCategory transCat = new OptionCategory("Transformation", "Transformation options.", transSubCats,
                transOpts);

        List<OptionCategory> cats = list(generalCat, transCat);
        OptionCategory options = new OptionCategory("CIF to Supremica Transformer Options",
                "All options for the CIF to Supremica transformer.", cats, list());

        return options;
    }
}
