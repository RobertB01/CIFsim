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

package org.eclipse.escet.cif.cif2plc;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.cif2plc.options.ConvertEnumsOption;
import org.eclipse.escet.cif.cif2plc.options.ElimEnumsOption;
import org.eclipse.escet.cif.cif2plc.options.PlcConfigurationNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcFormalFuncInvokeArgOption;
import org.eclipse.escet.cif.cif2plc.options.PlcFormalFuncInvokeFuncOption;
import org.eclipse.escet.cif.cif2plc.options.PlcMaxIterOption;
import org.eclipse.escet.cif.cif2plc.options.PlcNumberBitsOption;
import org.eclipse.escet.cif.cif2plc.options.PlcOutputType;
import org.eclipse.escet.cif.cif2plc.options.PlcOutputTypeOption;
import org.eclipse.escet.cif.cif2plc.options.PlcProjectNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcResourceNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskCycleTimeOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskPriorityOption;
import org.eclipse.escet.cif.cif2plc.options.RenameWarningsOption;
import org.eclipse.escet.cif.cif2plc.options.SimplifyValuesOption;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.writers.Iec611313Writer;
import org.eclipse.escet.cif.cif2plc.writers.PlcOpenXmlWriter;
import org.eclipse.escet.cif.cif2plc.writers.S7Writer;
import org.eclipse.escet.cif.cif2plc.writers.TwinCatWriter;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
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

/** CIF PLC code generator application. */
public class CifToPlcApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifToPlcApp app = new CifToPlcApp();
        app.run(args);
    }

    /** Constructor for the {@link CifToPlcApp} class. */
    public CifToPlcApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifToPlcApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifToPlcApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "CIF PLC code generator";
    }

    @Override
    public String getAppDescription() {
        return "Generates PLC code for CIF files.";
    }

    @Override
    protected int runInternal() {
        // Read CIF specification.
        Specification spec = new CifReader().init().read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Get output type.
        PlcOutputType outType = PlcOutputTypeOption.getPlcOutputType();

        // Get output path.
        String outPath = OutputFileOption.getDerivedPath(".cif", outType.outFileExtOrDirPostfix);
        Assert.notNull(outPath);
        outPath = Paths.resolve(outPath);

        // Generate PLC code.
        PlcProject project;
        try {
            project = CifToPlcTrans.transform(spec);
            if (isTerminationRequested()) {
                return 0;
            }
        } catch (UnsupportedException ex) {
            String msg = fmt("PLC code generation failed for CIF file \"%s\".", InputFileOption.getPath());
            throw new UnsupportedException(msg, ex);
        }

        // Write PLC code.
        switch (outType) {
            case PLC_OPEN_XML:
                new PlcOpenXmlWriter().write(project, outPath);
                break;

            case IEC_61131_3:
                new Iec611313Writer().write(project, outPath);
                break;

            case TWINCAT:
                new TwinCatWriter().write(project, outPath);
                break;

            case S7_1500:
            case S7_1200:
            case S7_400:
            case S7_300:
                new S7Writer(outType).write(project, outPath);
                break;

            default:
                throw new RuntimeException("Unknown output type: " + outType);
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

        List<Option> genOpts = list();
        genOpts.add(Options.getInstance(InputFileOption.class));
        genOpts.add(Options.getInstance(OutputFileOption.class));
        genOpts.add(Options.getInstance(PlcOutputTypeOption.class));
        genOpts.add(Options.getInstance(PlcTaskNameOption.class));
        genOpts.add(Options.getInstance(PlcTaskCycleTimeOption.class));
        genOpts.add(Options.getInstance(PlcTaskPriorityOption.class));
        genOpts.add(Options.getInstance(PlcProjectNameOption.class));
        genOpts.add(Options.getInstance(PlcConfigurationNameOption.class));
        genOpts.add(Options.getInstance(PlcResourceNameOption.class));
        genOpts.add(Options.getInstance(PlcNumberBitsOption.class));
        genOpts.add(Options.getInstance(PlcMaxIterOption.class));
        genOpts.add(Options.getInstance(PlcFormalFuncInvokeArgOption.class));
        genOpts.add(Options.getInstance(PlcFormalFuncInvokeFuncOption.class));
        genOpts.add(Options.getInstance(ElimEnumsOption.class)); // Deprecated.
        genOpts.add(Options.getInstance(ConvertEnumsOption.class));
        genOpts.add(Options.getInstance(SimplifyValuesOption.class));
        genOpts.add(Options.getInstance(RenameWarningsOption.class));
        List<OptionCategory> genSubCats = list();
        OptionCategory genCat = new OptionCategory("Generator", "Generator options.", genSubCats, genOpts);

        List<OptionCategory> cats = list(generalCat, genCat);
        OptionCategory options = new OptionCategory("CIF PLC Code Generator Options",
                "All options for the CIF PLC code generator.", cats, list());

        return options;
    }
}
