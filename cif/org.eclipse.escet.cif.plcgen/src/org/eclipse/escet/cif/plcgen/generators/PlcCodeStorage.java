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
import org.eclipse.escet.cif.cif2plc.plcdata.PlcTask;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcValue;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.cif2plc.writers.OutputTypeWriter;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;

/** Stores and writes generated PLC code. */
public class PlcCodeStorage {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Absolute base path to which to write the generated code. */
    private final String outputPath;

    /** Project with PLC code. */
    private final PlcProject project;

    /** PLC resource for the program. */
    private final PlcResource resource;

    /** Task running the PLC program. */
    private final PlcTask task;

    /** Global variable list for constants, lazily created. */
    private PlcGlobalVarList globalConstants = null;

    /** Global variable list for input variables, lazily created. */
    private PlcGlobalVarList globalInputs = null;

    /** Global variable list for output variables, lazily created. */
    private PlcGlobalVarList globalOutputs = null;

    /** Global variable list for state variables, lazily created. */
    private PlcGlobalVarList globalStateVars = null;

    /**
     * Constructor of the {@link PlcCodeStorage} class.
     *
     * @param target PLC target to generate code for.
     * @param settings Configuration to use.
     */
    public PlcCodeStorage(PlcTarget target, PlcGenSettings settings) {
        this.target = target;
        this.outputPath = settings.outputPath;

        // Create project, configuration, resource, and task.
        project = new PlcProject(settings.projectName);
        PlcConfiguration config = new PlcConfiguration(settings.configurationName);
        project.configurations.add(config);

        resource = new PlcResource(settings.resourceName);
        config.resources.add(resource);

        task = new PlcTask(settings.taskName, settings.taskCycleTime, settings.taskPriority);
        resource.tasks.add(task);
    }

    /**
     * Add a variable to the global input variable table.
     *
     * @param var Variable to add.
     */
    public void addInputVariable(PlcVariable var) {
        if (globalInputs == null) {
            globalInputs = new PlcGlobalVarList("INPUTS", false);
        }
        globalInputs.variables.add(var);
    }

    /**
     * Add a variable to the global output variable table.
     *
     * @param var Variable to add.
     */
    public void addOutputVariable(PlcVariable var) {
        if (globalOutputs == null) {
            globalOutputs = new PlcGlobalVarList("OUTPUTS", false);
        }
        globalOutputs.variables.add(var);
    }

    /**
     * Add a variable to the global output variable table.
     *
     * @param var Variable to add.
     */
    public void addConstant(PlcVariable var) {
        Assert.check(target.supportsConstants());

        if (globalConstants == null) {
            globalConstants = new PlcGlobalVarList("CONSTANTS", true);
        }
        globalConstants.variables.add(var);
    }

    /**
     * Add a variable to the global state variable table.
     *
     * @param var Variable to add.
     */
    public void addStateVariable(PlcVariable var) {
        if (globalStateVars == null) {
            globalStateVars = new PlcGlobalVarList("STATE", false);
        }
        globalStateVars.variables.add(var);
    }

    /** Perform any additional processing to make the generated PLC program ready. */
    public void finishPlcProgram() {
        // Add all created variable tables.
        addGlobalVariableTable(globalConstants);
        addGlobalVariableTable(globalInputs);
        addGlobalVariableTable(globalOutputs);
        addGlobalVariableTable(globalStateVars);

        // Create code file for program, with header etc.
        PlcPou main = new PlcPou("MAIN", PlcPouType.PROGRAM, null);
        project.pous.add(main);

        // Global variable list of the main program. Note that the cif2plc Siemens target requires the "TIMERS" name.
        PlcGlobalVarList globalTimers = new PlcGlobalVarList("TIMERS", false);
        addGlobalVariableTable(globalTimers);

        // Add main program variables.
        PlcType tonType = new PlcDerivedType("TON");
        globalTimers.variables.add(new PlcVariable("timer0", tonType));
        globalTimers.variables.add(new PlcVariable("timer1", tonType));
        globalTimers.variables.add(new PlcVariable("curTimer", INT_TYPE, null, new PlcValue("0")));

        // Add program to task.
        task.pouInstances.add(new PlcPouInstance("MAIN", main));
    }

    /**
     * Add the given variable table to the PLC code if the table exists.
     *
     * @param varTable Variable table to add if it exists.
     */
    private void addGlobalVariableTable(PlcGlobalVarList varTable) {
        if (varTable != null) {
            resource.globalVarLists.add(varTable);
        }
    }

    /**
     * Write the project to the output.
     *
     * @note Depending on the actual write implementation a single file or a directory may be written.
     */
    public void writeOutput() {
        OutputTypeWriter writer = target.getPlcCodeWriter();
        writer.write(project, outputPath);
    }
}
