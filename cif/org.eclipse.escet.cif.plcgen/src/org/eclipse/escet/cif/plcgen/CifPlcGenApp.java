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

package org.eclipse.escet.cif.plcgen;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.function.Supplier;

import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.ConvertEnumsOption;
import org.eclipse.escet.cif.plcgen.options.PlcConfigurationNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcFormalFuncInvokeArgOption;
import org.eclipse.escet.cif.plcgen.options.PlcFormalFuncInvokeFuncOption;
import org.eclipse.escet.cif.plcgen.options.PlcIntTypeSizeOption;
import org.eclipse.escet.cif.plcgen.options.PlcMaxIterOption;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.options.PlcProjectNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcRealTypeSizeOption;
import org.eclipse.escet.cif.plcgen.options.PlcResourceNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcTargetTypeOption;
import org.eclipse.escet.cif.plcgen.options.PlcTaskCycleTimeOption;
import org.eclipse.escet.cif.plcgen.options.PlcTaskNameOption;
import org.eclipse.escet.cif.plcgen.options.PlcTaskPriorityOption;
import org.eclipse.escet.cif.plcgen.options.RenameWarningsOption;
import org.eclipse.escet.cif.plcgen.options.SimplifyValuesOption;
import org.eclipse.escet.cif.plcgen.targets.AbbTarget;
import org.eclipse.escet.cif.plcgen.targets.Iec611313Target;
import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcOpenXmlTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.cif.plcgen.targets.SiemensS7Target;
import org.eclipse.escet.cif.plcgen.targets.TwinCatTarget;
import org.eclipse.escet.common.app.framework.AppEnv;
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

/** PLC code generator application. */
public class CifPlcGenApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifPlcGenApp app = new CifPlcGenApp();
        app.run(args, true);
    }

    /** Constructor for the {@link CifPlcGenApp} class. */
    public CifPlcGenApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifPlcGenApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifPlcGenApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF PLC code generator";
    }

    @Override
    public String getAppDescription() {
        return "Generates PLC code from a suitable CIF specification.";
    }

    @Override
    protected int runInternal() {
        // Configure code generation.
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
                throw new RuntimeException("Unknown target type: " + targetType);
        }
        PlcGenSettings settings = makePlcGenSettings(target);

        // Generate PLC code and write it to the file system.
        target.generate(settings);

        // TODO Use these options, see also getAllOptions()
        //
        // PlcMaxIterOption
        // PlcFormalFuncInvokeArgOption
        // PlcFormalFuncInvokeFuncOption
        return 0;
    }

    /**
     * Construct settings for the PLC code generator.
     *
     * @param target The target to generate PLC code for.
     * @return The constructed settings instance.
     */
    private PlcGenSettings makePlcGenSettings(PlcTarget target) {
        String projectName = PlcProjectNameOption.getProjName();
        String configurationName = PlcConfigurationNameOption.getCfgName();
        String resourceName = PlcResourceNameOption.getResName();
        String plcTaskName = PlcTaskNameOption.getTaskName();
        int taskCyceTime = PlcTaskCycleTimeOption.getTaskCycleTime();
        int priority = PlcTaskPriorityOption.getTaskPrio();

        String inputPath = InputFileOption.getPath();
        String outputPath = Paths.resolve(OutputFileOption.getDerivedPath(".cif", target.getPathSuffixReplacement()));

        PlcNumberBits intSize = PlcIntTypeSizeOption.getNumberBits();
        PlcNumberBits realSize = PlcRealTypeSizeOption.getNumberBits();
        boolean simplifyValues = SimplifyValuesOption.simplifyValues();
        ConvertEnums enumConversion = ConvertEnumsOption.getValue();

        // Required invariant: Once it returns true, it must return true on subsequent calls.
        Supplier<Boolean> shouldTerminate = () -> AppEnv.isTerminationRequested();

        boolean warnOnRename = RenameWarningsOption.isEnabled();
        WarnOutput warnOutput = message -> OutputProvider.warn(message);

        return new PlcGenSettings(projectName, configurationName, resourceName, plcTaskName, taskCyceTime, priority,
                inputPath, Paths.resolve(inputPath), outputPath, intSize, realSize, simplifyValues, enumConversion,
                shouldTerminate, warnOnRename, warnOutput);
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
        applicationOpts.add(Options.getInstance(PlcIntTypeSizeOption.class));
        applicationOpts.add(Options.getInstance(PlcRealTypeSizeOption.class));
        applicationOpts.add(Options.getInstance(SimplifyValuesOption.class));
        applicationOpts.add(Options.getInstance(ConvertEnumsOption.class));
        applicationOpts.add(Options.getInstance(RenameWarningsOption.class));

        applicationOpts.add(Options.getInstance(PlcMaxIterOption.class)); // TODO Use its value.
        applicationOpts.add(Options.getInstance(PlcFormalFuncInvokeArgOption.class)); // TODO Use its value.
        applicationOpts.add(Options.getInstance(PlcFormalFuncInvokeFuncOption.class)); // TODO Use its value.

        List<OptionCategory> generatorSubCats = list();
        OptionCategory generatorCat = new OptionCategory("Generator", "Generator options.", generatorSubCats,
                applicationOpts);

        List<OptionCategory> cats = list(generalCat, generatorCat);
        OptionCategory options = new OptionCategory("CIF PLC Code Generator Options",
                "All options for the CIF PLC code generator.", cats, list());

        return options;
    }
}
