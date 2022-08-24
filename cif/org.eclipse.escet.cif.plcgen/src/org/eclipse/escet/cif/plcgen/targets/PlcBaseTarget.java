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

package org.eclipse.escet.cif.plcgen.targets;

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
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcResource;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcStructType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTask;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcValue;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;

/** Base class for generating a {@link PlcProject}. */
public abstract class PlcBaseTarget extends PlcCodeGenSettings {
    /** PLC target type for code generation. */
    protected final PlcTargetType targetType;

    /** Project with PLC code. */
    protected PlcProject project;

    /** Global variable list for input variables. */
    protected PlcGlobalVarList globalInputs;

    /** Global variable list for constants, is {@code null} if constants are not expected to be used. */
    protected PlcGlobalVarList globalConsts;

    /** Structure definition for the state variables. */
    protected PlcStructType stateStruct;

    /**
     * Constructor of the {@link PlcBaseTarget} class.
     *
     * @param targetType PLC target type for code generation.
     */
    public PlcBaseTarget(PlcTargetType targetType) {
        this.targetType = targetType;
    }

    /** Create and initialize the PLC project for storing generated code. */
    public void initProject() {
        // Create project, configuration, resource, and task.
        project = new PlcProject(PlcProjectNameOption.getProjName());
        PlcConfiguration config = new PlcConfiguration(PlcConfigurationNameOption.getCfgName());
        PlcResource resource = new PlcResource(PlcResourceNameOption.getResName());
        PlcTask task = new PlcTask(PlcTaskNameOption.getTaskName(), PlcTaskCycleTimeOption.getTaskCycleTime(),
                PlcTaskPriorityOption.getTaskPrio());

        project.configurations.add(config);
        config.resources.add(resource);
        resource.tasks.add(task);

        // Global variable/constant lists.
        globalInputs = new PlcGlobalVarList("INPUTS", false);
        resource.globalVarLists.add(globalInputs);

        boolean constantsAllowed = getSupportconstants();
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

    /**
     * Write the project to the output file.
     *
     * @param outputPath Path to write to.
     */
    public abstract void writeOutput(String outputPath);
}
