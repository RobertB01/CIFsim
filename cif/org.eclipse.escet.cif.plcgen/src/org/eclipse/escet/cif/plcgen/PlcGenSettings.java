//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import java.util.function.Supplier;

/** PlcGen configuration data. */
public class PlcGenSettings {
    /** Name of the generated project. */
    public final String projectName;

    /** Name of the PLC configuration. */
    public final String configurationName;

    /** Name of the PLC resource. */
    public final String resourceName;

    /** Name of the PLC task. */
    public final String taskName;

    /** Cycle time of the plc task. */
    public final int taskCycleTime;

    /** Priority of the plc task. */
    public final int taskPriority;

    /** Path to the CIF specification to transform. */
    public final String inputPath;

    /** Destination for the generated code. */
    public final String outputPath;

    /** Callback whether execution should be terminated on user request. */
    public final Supplier<Boolean> shouldTerminate;

    /**
     * Constructor of the {@link PlcGenSettings} class.
     *
     * @param projectName Name of the generated project.
     * @param configurationName Name of the PLC configuration.
     * @param resourceName Name of the PLC resource.
     * @param taskName Name of the PLC task.
     * @param taskCycleTime Cycle time of the plc task.
     * @param taskPriority Priority of the plc task.
     * @param inputPath Path to the CIF specification to transform.
     * @param outputPath Destination for the generated code.
     * @param shouldTerminate Callback whether execution should be terminated on user request.
     */
    PlcGenSettings(String projectName, String configurationName, String resourceName, String taskName,
            int taskCycleTime, int taskPriority, String inputPath, String outputPath, Supplier<Boolean> shouldTerminate)
    {
        this.projectName = projectName;
        this.configurationName = configurationName;
        this.resourceName = resourceName;
        this.taskName = taskName;
        this.taskCycleTime = taskCycleTime;
        this.taskPriority = taskPriority;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.shouldTerminate = shouldTerminate;
    }
}
