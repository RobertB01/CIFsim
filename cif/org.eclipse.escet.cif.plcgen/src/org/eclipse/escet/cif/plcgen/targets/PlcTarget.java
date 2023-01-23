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
    /** PLC target type for code generation. */
    protected final PlcTargetType targetType;

    /**
     * Constructor of the {@link PlcTarget} class.
     *
     * @param targetType PLC target type for code generation.
     */
    public PlcTarget(PlcTargetType targetType) {
        this.targetType = targetType;
    }

    /**
     * Generate and write the PLC code.
     *
     * @param settings Configuration to use.
     */
    public void generate(PlcGenSettings settings) {
        CifProcessor cifProcessor = new CifProcessor();
        PlcCodeStorage codeStorage = new PlcCodeStorage(this, settings);

        cifProcessor.process(settings);
        if (settings.shouldTerminate.get()) {
            return;
        }

        codeStorage.finishPlcProgram();
        if (settings.shouldTerminate.get()) {
            return;
        }

        codeStorage.writeOutput();
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
