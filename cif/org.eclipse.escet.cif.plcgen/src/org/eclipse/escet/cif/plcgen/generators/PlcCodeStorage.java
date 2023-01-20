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

import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.INT_TYPE;

import org.eclipse.escet.cif.cif2plc.options.PlcConfigurationNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcProjectNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcResourceNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskCycleTimeOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskNameOption;
import org.eclipse.escet.cif.cif2plc.options.PlcTaskPriorityOption;
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
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;

/** Stores generated PLC code. */
public class PlcCodeStorage {
    /** PLC target to generate code for. */
    private PlcBaseTarget target;

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

    /** Create and initialize the PLC project for storing generated code. */
    public void initProject() {
        // Create project, configuration, resource, and task.
        project = new PlcProject(PlcProjectNameOption.getProjName());
        PlcConfiguration config = new PlcConfiguration(PlcConfigurationNameOption.getCfgName());
        PlcResource resource = new PlcResource(PlcResourceNameOption.getResName());
        task = new PlcTask(PlcTaskNameOption.getTaskName(), PlcTaskCycleTimeOption.getTaskCycleTime(),
                PlcTaskPriorityOption.getTaskPrio());

        project.configurations.add(config);
        config.resources.add(resource);
        resource.tasks.add(task);

        // Global variable/constant lists.
        globalInputs = new PlcGlobalVarList("INPUTS", false);
        resource.globalVarLists.add(globalInputs);

        boolean constantsAllowed = supportsConstants();
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

    /**
     * Write the project to the output.
     *
     * @param outputPath Absolute base path to write to.
     * @note Depending on the actual implementation a single file or a directory may be written.
     */
    public void writeOutput(String outputPath) {
        OutputTypeWriter writer = getPlcCodeWriter();
        writer.write(project, outputPath);
    }
}
