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

package org.eclipse.escet.cif.plcgen.targets;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.writers.OutputTypeWriter;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;

/** Base class for generating a {@link PlcProject}. */
public abstract class PlcTarget {
    /** CIF file information extractor. */
    private CifProcessor cifProcessor;

    /** PLC code storage and writing. */
    private PlcCodeStorage codeStorage;

    /** PLC target type for code generation. */
    protected final PlcTargetType targetType;

    /**
     * Constructor of the {@link PlcTarget} class.
     *
     * @param targetType PLC target type for code generation.
     */
    public PlcTarget(PlcTargetType targetType) {
        this.targetType = targetType;

        cifProcessor = new CifProcessor();
        codeStorage = new PlcCodeStorage(this);
    }

    /**
     * Generate and write the PLC code.
     *
     * @param settings Configuration to use.
     * @return Whether generating the PLC code succeeded, termination request is not successful.
     */
    public boolean generate(PlcGenSettings settings) {
        codeStorage.setup(settings);
        if (settings.shouldTerminate.get()) {
            return false;
        }

        if (!cifProcessor.generate(settings)) {
            return false;
        }

        codeStorage.generateProgram();
        if (settings.shouldTerminate.get()) {
            return false;
        }

        codeStorage.writeOutput();
        return true;
    }

    /**
     * Get the writer for writing the generated PLC code to the file system.
     *
     * @return The requested PLC code writer.
     */
    public abstract OutputTypeWriter getPlcCodeWriter();

    /**
     * Returns whether or not the PLC target type supports named constants.
     *
     * @return Whether named constants are supported.
     */
    public abstract boolean supportsConstants();

    /**
     * Get replacement string for the CIF input file extension including dot, used to derive an output path.
     *
     * @return The replacement string.
     */
    public abstract String getPathSuffixReplacement();
}
