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

import org.eclipse.escet.cif.cif2plc.options.PlcNumberBits;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.writers.OutputTypeWriter;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor;
import org.eclipse.escet.cif.plcgen.generators.NameSanitizer;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;

/** Base class for generating a {@link PlcProject}. */
public abstract class PlcTarget {
    /** Size of an integer value in a CIF specification. */
    private static final int CIF_INTEGER_SIZE = 32;

    /** Size of a floating point value in a CIF specification. */
    private static final int CIF_FLOAT_SIZE = 64;

    /** PLC target type for code generation. */
    public final PlcTargetType targetType;

    /** User-defined integer type size to use by the PLC. */
    private PlcNumberBits intTypeSize;

    /** User-defined floating point type size to use by the PLC. */
    private PlcNumberBits floatTypeSize;

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
        intTypeSize = settings.intTypeSize;
        floatTypeSize = settings.floatTypeSize;

        // Construct the generators.
        NameSanitizer nameSanitizer = new NameSanitizer(settings);
        PlcCodeStorage codeStorage = new PlcCodeStorage(this, settings);
        TypeGenerator typeGen = new TypeGenerator(this, settings, nameSanitizer, codeStorage);
        CifProcessor cifProcessor = new CifProcessor(this, settings, typeGen, codeStorage, nameSanitizer);

        // Perform the conversion.
        cifProcessor.process();
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
     * Returns whether the target supports arrays.
     *
     * @return Whether arrays are supported.
     */
    public abstract boolean supportsArrays();

    /**
     * Returns whether or not the PLC target type supports named constants.
     *
     * @return Whether named constants are supported.
     */
    public abstract boolean supportsConstants();

    /**
     * Return whether the target support enumeration types.
     *
     * @return Whether enumeration types are supported.
     */
    public abstract boolean supportsEnumerations();

    /**
     * Get the size of the largest supported integer type.
     *
     * @return Number of bits used for storing the largest supported integer type.
     */
    protected abstract int getMaxIntegerTypeSize();

    /**
     * Get the type of a normal integer value in the PLC. Is large enough to represent all integer values of a CIF
     * specification.
     *
     * @return The type of a normal integer value in the PLC.
     */
    public PlcElementaryType getIntegerType() {
        int generatorBestIntSize = Math.min(CIF_INTEGER_SIZE, getMaxIntegerTypeSize());
        int userSpecifiedIntSize = intTypeSize.getTypeSize(generatorBestIntSize);
        return PlcElementaryType.getIntTypeBySize(userSpecifiedIntSize);
    }

    /**
     * Get the size of the largest supported floating point type. Is large enough to represent all floating point values
     * of a CIF specification.
     *
     * @return Number of bits used for storing the largest supported floating point type.
     */
    protected abstract int getMaxFloatTypeSize();

    /**
     * Get the type of a normal integer value in the PLC.
     *
     * @return The type of a normal integer value in the PLC.
     */
    public PlcElementaryType getFloatType() {
        int generatorBestFloatSize = Math.min(CIF_FLOAT_SIZE, getMaxFloatTypeSize());
        int userSpecifiedFloatSize = floatTypeSize.getTypeSize(generatorBestFloatSize);
        return PlcElementaryType.getFloatTypeBySize(userSpecifiedFloatSize);
    }

    /**
     * Get replacement string for the CIF input file extension including dot, used to derive an output path.
     *
     * @return The replacement string.
     */
    public abstract String getPathSuffixReplacement();
}
