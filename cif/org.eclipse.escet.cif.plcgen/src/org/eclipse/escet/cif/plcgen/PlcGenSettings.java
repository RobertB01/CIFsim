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

import java.util.function.Supplier;

import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
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

    /** Maximum number of iterations for performing uncontrollable events in a single cycle, or {@code null} if unrestricted. */
    public final Integer maxUncontrollableLimit;

    /** Maximum number of iterations for performing controllable events in a single cycle, or {@code null} if unrestricted. */
    public final Integer maxControllableLimit;

    /** User-specified path to the CIF specification for which to generate PLC code. */
    public final String inputPath;

    /** Absolute path to the CIF specification for which to generate PLC code. */
    public final String absInputPath;

    /** Absolute base path to which to write the generated code. */
    public final String absOutputPath;

    /** File path to the I/O table file, may not exist. */
    public final String ioTablePath;

    /** Absolute file path to the I/O table file, may not exist. */
    public final String absIoTablePath;

    /** User-defined integer type size to use by the PLC. */
    public final PlcNumberBits intTypeSize;

    /** User-defined real type size to used by the PLC. */
    public final PlcNumberBits realTypeSize;

    /** Whether to simplify values during pre-processing. */
    public boolean simplifyValues;

    /** How to treat enumerations. */
    public final ConvertEnums enumConversion;

    /** Callback that indicates whether execution should be terminated on user request. */
    public final Supplier<Boolean> shouldTerminate;

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
     * @param inputPath User-specified path to the CIF specification for which to generate PLC code.
     * @param absInputPath Absolute path to the CIF specification for which to generate PLC code.
     * @param absOutputPath Absolute base path to which to write the generated code.
     * @param ioTablePath File path to the I/O table file, may not exist.
     * @param absIoTablePath Absolute file path to the I/O table file, may not exist.
     * @param intTypeSize User-defined integer type size to use by the PLC.
     * @param realTypeSize User-defined real type size to used by the PLC.
     * @param simplifyValues Whether to simplify values during pre-processing.
     * @param enumConversion How to treat enumerations.
     * @param shouldTerminate Callback that indicates whether execution should be terminated on user request.
     * @param warnOnRename Whether to warn the user when renaming CIF identifiers.
     * @param warnOutput Callback to send warnings to the user.
     */
    public PlcGenSettings(String projectName, String configurationName, String resourceName, String taskName,
            int taskCycleTime, int taskPriority, Integer maxUncontrollableLimit, Integer maxControllableLimit, String inputPath, String absInputPath,
            String absOutputPath, String ioTablePath, String absIoTablePath, PlcNumberBits intTypeSize,
            PlcNumberBits realTypeSize, boolean simplifyValues, ConvertEnums enumConversion,
            Supplier<Boolean> shouldTerminate, boolean warnOnRename, WarnOutput warnOutput)
    {
        this.projectName = projectName;
        this.configurationName = configurationName;
        this.resourceName = resourceName;
        this.taskName = taskName;
        this.taskCycleTime = taskCycleTime;
        this.taskPriority = taskPriority;
        this.maxUncontrollableLimit = maxUncontrollableLimit;
        this.maxControllableLimit = maxControllableLimit;
        this.inputPath = inputPath;
        this.absInputPath = absInputPath;
        this.absOutputPath = absOutputPath;
        this.ioTablePath = ioTablePath;
        this.absIoTablePath = absIoTablePath;
        this.intTypeSize = intTypeSize;
        this.realTypeSize = realTypeSize;
        this.simplifyValues = simplifyValues;
        this.enumConversion = enumConversion;
        this.shouldTerminate = shouldTerminate;
        this.warnOnRename = warnOnRename;
        this.warnOutput = warnOutput;
    }
}
