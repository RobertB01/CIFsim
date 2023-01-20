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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.INT_TYPE;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcConfiguration;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcDerivedType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcGlobalVarList;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPou;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPouInstance;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcPouType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcResource;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcStructType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTask;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcValue;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.cif2plc.writers.OutputTypeWriter;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;

/** Stores and writes generated PLC code. */
public class PlcCodeStorage {
    /** PLC target to generate code for. */
    private PlcBaseTarget target;

    /** Destination for the generated code. */
    private String outputPath;

    /** Project with PLC code. */
    protected PlcProject project;

    /** Task running the PLC program. */
    protected PlcTask task;

    /** Global variable list for input variables. */
    protected PlcGlobalVarList globalInputs;

    /** Global variable list for constants, is {@code null} if constants are not expected to be used. */
    protected PlcGlobalVarList globalConsts;

    /** Structure definition for the state variables. */
    protected PlcStructType stateStruct;

    /**
     * Constructor of the {@link PlcCodeStorage} class.
     *
     * @param target PLC target to generate code for.
     */
    public PlcCodeStorage(PlcBaseTarget target) {
        this.target = target;
    }

    /**
     * Setup the generator.
     *
     * @param settings Configuration of the application.
     */
    public void setup(PlcGenSettings settings) {
        this.outputPath = settings.outputPath;

        // Create project, configuration, resource, and task.
        project = new PlcProject(settings.projectName);
        PlcConfiguration config = new PlcConfiguration(settings.configurationName);
        PlcResource resource = new PlcResource(settings.resourceName);
        task = new PlcTask(settings.taskName, settings.taskCycleTime,
                settings.taskPriority);

        project.configurations.add(config);
        config.resources.add(resource);
        resource.tasks.add(task);

        // Global variable/constant lists.
        globalInputs = new PlcGlobalVarList("INPUTS", false);
        resource.globalVarLists.add(globalInputs);

        boolean constantsAllowed = target.supportsConstants();
        if (constantsAllowed) {
            globalConsts = new PlcGlobalVarList("CONSTS", true);
            resource.globalVarLists.add(globalConsts);
        }

        // Timer global variable list.
        PlcGlobalVarList globalTimers = new PlcGlobalVarList("TIMERS", false);
        resource.globalVarLists.add(globalTimers);
        PlcType tonType = new PlcDerivedType("TON");
        globalTimers.variables.add(new PlcVariable("timer0", tonType));
        globalTimers.variables.add(new PlcVariable("timer1", tonType));
        globalTimers.variables.add(new PlcVariable("curTimer", INT_TYPE, null, new PlcValue("0")));
    }

    /** Construct the main program. */
    public void generateProgram() {
        // Create code file for program, with header etc.
        PlcPou main = new PlcPou("MAIN", PlcPouType.PROGRAM, null);
        project.pous.add(main);

        // Add program to task.
        task.pouInstances.add(new PlcPouInstance("MAIN", main));
    }

    /** Write the project to the output. */
    public void writeOutput() {
        OutputTypeWriter writer = target.getPlcCodeWriter();

        // Depending on the actual write implementation a single file or a directory may be written.
        writer.write(project, outputPath);
    }
}
