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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
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
import org.eclipse.escet.common.box.CodeBox;

/** CIF to mCRL2 transformation application. */
public class CifToMcrl2Application extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command-line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifToMcrl2Application cmApp = new CifToMcrl2Application();
        cmApp.run(args, true);
    }

    /** Constructor for the {@link CifToMcrl2Application} class. */
    public CifToMcrl2Application() {
        // Nothing to do.
    }

    /**
     * Constructor of the {@link CifToMcrl2Application} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public CifToMcrl2Application(AppStreams streams) {
        super(streams);
    }

    @Override
    protected int runInternal() {
        // Read CIF input.
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        String absSpecPath = Paths.resolve(InputFileOption.getPath());
        if (isTerminationRequested()) {
            return 0;
        }

        // Transform.
        String valueActionPatterns = GenerateValueActionsOption.getValue();
        CifToMcrl2Transformer transformer = new CifToMcrl2Transformer(() -> isTerminationRequested(),
                OutputProvider.getWarningOutputStream());
        CodeBox code = transformer.transform(spec, absSpecPath, valueActionPatterns);

        // Write mCRL2 file.
        String outPath = OutputFileOption.getDerivedPath(".cif", ".mcrl2");
        String absOutPath = Paths.resolve(outPath);
        code.writeToFile(outPath, absOutPath);

        // Done.
        return 0;
    }

    @Override
    public String getAppName() {
        return "CIF to mCRL2 transformer";
    }

    @Override
    public String getAppDescription() {
        return "Convert CIF specification to mCRL2.";
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    /**
     * Options specific to the CIF to mCRL2 transformer.
     *
     * @return Options specific to the CIF to mCRL2 transformer.
     */
    private OptionCategory getConvertOptionsCategory() {
        List<OptionCategory> subPages = list();

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        options.add(Options.getInstance(InputFileOption.class));
        options.add(Options.getInstance(GenerateValueActionsOption.class));
        options.add(Options.getInstance(OutputFileOption.class));
        return new OptionCategory("CIF to mCRL2 options", "Options for converting a CIF specification to mCRL2.",
                subPages, options);
    }

    @Override
    protected OptionCategory getAllOptions() {
        List<OptionCategory> subPages = list();
        subPages.add(getGeneralOptionCategory());
        subPages.add(getConvertOptionsCategory());

        @SuppressWarnings("rawtypes")
        List<Option> options = list();
        String optDesc = "All options for the conversion from CIF to mCRL2.";
        return new OptionCategory("CIF to mCRL2 conversion options", optDesc, subPages, options);
    }
}
