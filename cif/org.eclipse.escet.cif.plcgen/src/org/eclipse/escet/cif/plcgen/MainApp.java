//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.cif2plc.options.PlcConfigurationNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcProjectNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcResourceNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskCycleTimeOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskPriorityOption;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.plcgen.targets.AbbTarget;
import org.eclipse.escet.cif.plcgen.targets.Iec611313Target;
import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcOpenXmlTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.cif.plcgen.targets.SiemensS7Target;
import org.eclipse.escet.cif.plcgen.targets.TwinCatTarget;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;

/** Application entry point for the PLC code generator. */
public class MainApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.run(args);
    }

    /** Constructor for the {@link MainApp} class. */
    public MainApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link MainApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public MainApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF PLCgen";
    }

    @Override
    public String getAppDescription() {
        return "Generates PLC code from a suitable CIF specification.";
    }

    @Override
    protected int runInternal() {
        // Read CIF specification.
        new CifReader().init().read(); // Currently not used.
        if (isTerminationRequested()) {
            return 0;
        }

        try {
            // Construct the target code generator.
            PlcTargetType targetType = PlcTargetTypeOption.getPlcTargetType();
            PlcBaseTarget target;
            switch (targetType) {
                case ABB:
                    target = new AbbTarget();
                    break;
                case IEC_61131_3:
                    target = new Iec611313Target();
                    break;
                case PLC_OPEN_XML:
                    target = new PlcOpenXmlTarget();
                    break;
                case S7_1200:
                case S7_1500:
                case S7_300:
                case S7_400:
                    target = new SiemensS7Target(targetType);
                    break;
                case TWINCAT:
                    target = new TwinCatTarget();
                    break;
                default:
                    throw new RuntimeException("Unknown output type: " + targetType);
            }

            // Codegen settings should have been set now, lock them.
            target.lockCodeGenSettings();

            // Get output path.
            String outPath = OutputFileOption.getDerivedPath(".cif", target.getOutSuffixReplacement());
            Assert.notNull(outPath);
            outPath = Paths.resolve(outPath);

            // Construct the project
            target.initProject();
            if (isTerminationRequested()) {
                return 0;
            }

            // Write output.
            target.writeOutput(outPath);
        } catch (UnsupportedException ex) {
            String msg = fmt("PLC code generation failed for CIF file \"%s\".", InputFileOption.getPath());
            throw new UnsupportedException(msg, ex);
        }

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

        List<Option> applicationOpts = list();
        applicationOpts.add(Options.getInstance(InputFileOption.class));
        applicationOpts.add(Options.getInstance(OutputFileOption.class));
        applicationOpts.add(Options.getInstance(PlcTargetTypeOption.class));
        applicationOpts.add(Options.getInstance(PlcConfigurationNameOption.class));
        applicationOpts.add(Options.getInstance(PlcProjectNameOption.class));
        applicationOpts.add(Options.getInstance(PlcResourceNameOption.class));
        applicationOpts.add(Options.getInstance(PlcTaskCycleTimeOption.class));
        applicationOpts.add(Options.getInstance(PlcTaskNameOption.class));
        applicationOpts.add(Options.getInstance(PlcTaskPriorityOption.class));

        List<OptionCategory> generatorSubCats = list();
        OptionCategory generatorCat = new OptionCategory("PLCgen", "PLCgen options.", generatorSubCats, applicationOpts);

        List<OptionCategory> cats = list(generalCat, generatorCat);
        OptionCategory options = new OptionCategory("CIF PLCgen Options",
                "All options for the CIF PLCgen generator.", cats,
                list());

        return options;
    }
}
