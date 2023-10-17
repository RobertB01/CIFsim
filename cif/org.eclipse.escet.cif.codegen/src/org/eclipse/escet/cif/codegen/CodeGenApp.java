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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.codegen.c89.C89CodeGen;
import org.eclipse.escet.cif.codegen.c99.C99CodeGen;
import org.eclipse.escet.cif.codegen.java.JavaCodeGen;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptCodeGen;
import org.eclipse.escet.cif.codegen.options.CodePrefixOption;
import org.eclipse.escet.cif.codegen.options.JavaPackageOption;
import org.eclipse.escet.cif.codegen.options.OutputDirOption;
import org.eclipse.escet.cif.codegen.options.SimulinkOutputsOption;
import org.eclipse.escet.cif.codegen.options.SimulinkSampleOffsetOption;
import org.eclipse.escet.cif.codegen.options.SimulinkSampleTimeOption;
import org.eclipse.escet.cif.codegen.options.TargetLanguage;
import org.eclipse.escet.cif.codegen.options.TargetLanguageOption;
import org.eclipse.escet.cif.codegen.simulink.SimulinkCodeGen;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** CIF code generator application. */
public class CodeGenApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CodeGenApp app = new CodeGenApp();
        app.run(args);
    }

    /** Constructor for the {@link CodeGenApp} class. */
    public CodeGenApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CodeGenApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CodeGenApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF code generator";
    }

    @Override
    public String getAppDescription() {
        return "Generates implementation code from CIF specifications, for various languages and platforms.";
    }

    @Override
    protected int runInternal() {
        // Read CIF specification.
        Specification spec = new CifReader().init().read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Generate code.
        String path = OutputDirOption.getPath();
        TargetLanguage lang = TargetLanguageOption.getLanguage();
        try {
            switch (lang) {
                case JAVA:
                    new JavaCodeGen().generate(spec, path);
                    break;

                case JAVASCRIPT:
                    new JavaScriptCodeGen().generate(spec, path);
                    break;

                case C89:
                    new C89CodeGen().generate(spec, path);
                    break;

                case C99:
                    new C99CodeGen().generate(spec, path);
                    break;

                case SIMULINK:
                    new SimulinkCodeGen().generate(spec, path);
                    break;

                default:
                    throw new RuntimeException("Unknown target language");
            }
        } catch (UnsupportedException ex) {
            String msg = fmt("Code generation to %s failed for CIF file \"%s\".", lang.readableName,
                    InputFileOption.getPath());
            throw new UnsupportedException(msg, ex);
        }

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

        List<Option> javaOpts = list();
        javaOpts.add(Options.getInstance(JavaPackageOption.class));
        List<OptionCategory> javaSubCats = list();
        OptionCategory javaCat = new OptionCategory("Java", "Java code generation options.", javaSubCats, javaOpts);

        List<Option> javaScriptOpts = list();
        List<OptionCategory> javaScriptSubCats = list();
        OptionCategory javaScriptCat = new OptionCategory("JavaScript", "JavaScript code generation options.",
                javaScriptSubCats, javaScriptOpts);

        List<Option> simulinkOpts = list();
        simulinkOpts.add(Options.getInstance(SimulinkOutputsOption.class));
        simulinkOpts.add(Options.getInstance(SimulinkSampleTimeOption.class));
        simulinkOpts.add(Options.getInstance(SimulinkSampleOffsetOption.class));
        List<OptionCategory> simulinkSubCats = list();
        OptionCategory simulinkCat = new OptionCategory("Simulink", "Simulink code generation options.",
                simulinkSubCats, simulinkOpts);

        List<Option> genOpts = list();
        genOpts.add(Options.getInstance(InputFileOption.class));
        genOpts.add(Options.getInstance(OutputDirOption.class));
        genOpts.add(Options.getInstance(TargetLanguageOption.class));
        genOpts.add(Options.getInstance(CodePrefixOption.class));
        List<OptionCategory> genSubCats = list(simulinkCat, javaCat, javaScriptCat);
        OptionCategory genCat = new OptionCategory("Generation", "Generation options.", genSubCats, genOpts);

        List<OptionCategory> cats = list(generalCat, genCat);
        OptionCategory options = new OptionCategory("CIF Code Generator Options",
                "All options for the CIF code generator.", cats, list());

        return options;
    }
}
