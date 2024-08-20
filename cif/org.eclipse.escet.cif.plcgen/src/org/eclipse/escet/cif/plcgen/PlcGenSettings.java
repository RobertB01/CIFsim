//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.output.WarnOutput;

/** PLC code generator configuration. */
public class PlcGenSettings {
    /** Name of the PLC project. */
    public final String projectName;

    /** Name of the PLC configuration. */
    public final String configurationName;

    /** Name of the PLC resource. */
    public final String resourceName;

    /** Name of the PLC task. */
    public final String taskName;

    /** Cycle time of the PLC task, in milliseconds. Set to {@code 0} to disable periodic task scheduling. */
    public final int taskCycleTime;

    /** Priority of the PLC task. Must be in the range [0..65535]. */
    public final int taskPriority;

    /**
     * Maximum number of iterations for performing uncontrollable events in a single cycle, or {@code null} if
     * unrestricted.
     */
    public final Integer maxUncontrollableLimit;

    /**
     * Maximum number of iterations for performing controllable events in a single cycle, or {@code null} if
     * unrestricted.
     */
    public final Integer maxControllableLimit;

    /** Paths to the CIF specification for which to generate PLC code. */
    public final PathPair inputPaths;

    /** Paths to write the generated code. Depending on the target can be either a file or a directory path. */
    public final PathPair outputPaths;

    /** Paths to the I/O table file, may not exist. */
    public final PathPair ioTablePaths;

    /**
     * Text lines of the PLC program header. Text should be printable ASCII only (ASCII characters 32 through 126), and
     * not contain any {@code "(*"} or {@code "*)"}.
     */
    public final List<String> programHeaderTextLines;

    /** User-defined integer type size to use by the PLC. */
    public final PlcNumberBits intTypeSize;

    /** User-defined real type size to used by the PLC. */
    public final PlcNumberBits realTypeSize;

    /** Whether to simplify values during pre-processing. */
    public boolean simplifyValues;

    /** How to treat enumerations. */
    public final ConvertEnums enumConversion;

    /** Cooperative termination query function. */
    public final Termination termination;

    /** Whether to warn the user when renaming CIF identifiers. */
    public final boolean warnOnRename;

    /** Callback to send warnings to the user. */
    public final WarnOutput warnOutput;

    /**
     * Constructor of the {@link PlcGenSettings} class.
     *
     * @param projectName Name of the PLC project.
     * @param configurationName Name of the PLC configuration.
     * @param resourceName Name of the PLC resource.
     * @param taskName Name of the PLC task.
     * @param taskCycleTime Cycle time of the PLC task, in milliseconds. Set to {@code 0} to disable periodic task
     *     scheduling.
     * @param taskPriority Priority of the PLC task. Must be in the range [0..65535].
     * @param maxUncontrollableLimit Maximum number of iterations for performing uncontrollable events in a single
     *     cycle, or {@code null} if unrestricted.
     * @param maxControllableLimit Maximum number of iterations for performing controllable events in a single cycle, or
     *     {@code null} if unrestricted.
     * @param inputPaths Paths to the CIF specification for which to generate PLC code.
     * @param outputPaths Paths to write the generated code. Depending on the target can be either a file or a directory
     *     path.
     * @param ioTablePaths Paths to the I/O table file, may not exist.
     * @param programHeaderTextLines Text lines of the PLC program header. Text should be printable ASCII only (ASCII
     *     characters 32 through 126), and not contain any {@code "(*"} or {@code "*)"}.
     * @param intTypeSize User-defined integer type size to use by the PLC.
     * @param realTypeSize User-defined real type size to used by the PLC.
     * @param simplifyValues Whether to simplify values during pre-processing.
     * @param enumConversion How to treat enumerations.
     * @param termination Cooperative termination query function.
     * @param warnOnRename Whether to warn the user when renaming CIF identifiers.
     * @param warnOutput Callback to send warnings to the user.
     */
    public PlcGenSettings(String projectName, String configurationName, String resourceName, String taskName,
            int taskCycleTime, int taskPriority, Integer maxUncontrollableLimit, Integer maxControllableLimit,
            PathPair inputPaths, PathPair outputPaths, PathPair ioTablePaths, List<String> programHeaderTextLines,
            PlcNumberBits intTypeSize, PlcNumberBits realTypeSize, boolean simplifyValues, ConvertEnums enumConversion,
            Termination termination, boolean warnOnRename, WarnOutput warnOutput)
    {
        this.projectName = projectName;
        this.configurationName = configurationName;
        this.resourceName = resourceName;
        this.taskName = taskName;
        this.taskCycleTime = taskCycleTime;
        this.taskPriority = taskPriority;
        this.maxUncontrollableLimit = maxUncontrollableLimit;
        this.maxControllableLimit = maxControllableLimit;
        this.inputPaths = inputPaths;
        this.outputPaths = outputPaths;
        this.ioTablePaths = ioTablePaths;
        this.programHeaderTextLines = programHeaderTextLines;
        this.intTypeSize = intTypeSize;
        this.realTypeSize = realTypeSize;
        this.simplifyValues = simplifyValues;
        this.enumConversion = enumConversion;
        this.termination = termination;
        this.warnOnRename = warnOnRename;
        this.warnOutput = warnOutput;
    }
}
