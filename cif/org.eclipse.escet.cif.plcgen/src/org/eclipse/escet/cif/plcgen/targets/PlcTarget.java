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
import org.eclipse.escet.cif.plcgen.generators.NameGenerator;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;

/** Base class for generating a {@link PlcProject}. */
public abstract class PlcTarget {
    /** Size of an integer value in a CIF specification. */
    public static final int CIF_INTEGER_SIZE = 32;

    /** Size of a real value in a CIF specification. */
    public static final int CIF_REAL_SIZE = 64;

    /** PLC target type for code generation. */
    public final PlcTargetType targetType;

    /** User-defined integer type size to use by the PLC. */
    private PlcNumberBits intTypeSize;

    /** User-defined real type size to use by the PLC. */
    private PlcNumberBits realTypeSize;

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
        realTypeSize = settings.realTypeSize;

        // Construct the generators.
        NameGenerator nameGenerator = new NameGenerator(settings);
        PlcCodeStorage codeStorage = new PlcCodeStorage(this, settings);
        TypeGenerator typeGen = new TypeGenerator(this, settings, nameGenerator, codeStorage);
        CifProcessor cifProcessor = new CifProcessor(this, settings, typeGen, codeStorage, nameGenerator);

        // Warn the user about getting a possibly too small integer type size.
        if (settings.intTypeSize.getTypeSize(PlcTarget.CIF_INTEGER_SIZE) < PlcTarget.CIF_INTEGER_SIZE) {
            settings.warnOutput.warn(
                    "Configured integer type size is less than the CIF integer type size. Some values in the program "
                            + "may be truncated.");
        } else if (getMaxIntegerTypeSize() < PlcTarget.CIF_INTEGER_SIZE) {
            settings.warnOutput
                    .warn("Maximum integer type size supported by the PLC is less than the CIF integer type size. Some "
                            + "values in the program may be truncated.");
        }

        // Warn the user about getting a possibly too small real type size.
        if (settings.realTypeSize.getTypeSize(PlcTarget.CIF_REAL_SIZE) < PlcTarget.CIF_REAL_SIZE) {
            settings.warnOutput
                    .warn("Configured real type size is less than the CIF real type size. Some values in the program "
                            + "may be truncated.");
        } else if (getMaxRealTypeSize() < PlcTarget.CIF_REAL_SIZE) {
            settings.warnOutput
                    .warn("Maximum real type size supported by the PLC is less than the CIF real type size. Some "
                            + "values in the program may be truncated.");
        }

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
     * Return whether the target supports enumeration types.
     *
     * @return Whether enumeration types are supported.
     */
    public abstract boolean supportsEnumerations();

    /**
     * Does the target support the given semantic operation?
     *
     * @param funcOper Semantics operation being queried.
     * @return Whether the target supports the given operation.
     */
    public boolean supportsOperation(PlcFuncOperation funcOper) {
        // By default the operation is supported.
        return true;
    }

    /**
     * Does the target support infix notation for the given semantic operation?
     *
     * @param funcOper Semantics operation being queried.
     * @return Whether the target support infix notation fot the given operation.
     */
    public boolean supportsInfixNotation(PlcFuncOperation funcOper) {
        // By default infix notation is supported if the standard supplies a notation for it.
        return true;
    }

    /**
     * Get the size of the largest supported integer type.
     *
     * @return Number of bits used for storing the largest supported integer type.
     */
    protected abstract int getMaxIntegerTypeSize();

    /**
     * Get the type of a standard integer value in the PLC.
     *
     * @return The type of a standard integer value in the PLC.
     */
    public PlcElementaryType getIntegerType() {
        int generatorBestIntSize = Math.min(CIF_INTEGER_SIZE, getMaxIntegerTypeSize());
        int userSpecifiedIntSize = intTypeSize.getTypeSize(generatorBestIntSize);
        return PlcElementaryType.getIntTypeBySize(userSpecifiedIntSize);
    }

    /**
     * Get the size of the largest supported real type.
     *
     * @return Number of bits used for storing the largest supported real type.
     */
    protected abstract int getMaxRealTypeSize();

    /**
     * Get the type of a standard real value in the PLC.
     *
     * @return The type of a standard real value in the PLC.
     */
    public PlcElementaryType getRealType() {
        int generatorBestRealSize = Math.min(CIF_REAL_SIZE, getMaxRealTypeSize());
        int userSpecifiedRealSize = realTypeSize.getTypeSize(generatorBestRealSize);
        return PlcElementaryType.getRealTypeBySize(userSpecifiedRealSize);
    }

    /**
     * Get replacement string for the CIF input file extension including dot, used to derive an output path.
     *
     * @return The replacement string.
     */
    public abstract String getPathSuffixReplacement();
}
