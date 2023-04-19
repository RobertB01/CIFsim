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
import org.eclipse.escet.cif.plcgen.generators.DefaultNameGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultTypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.NameGenerator;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;

/** Base class for generating a {@link PlcProject}. */
public abstract class PlcBaseTarget implements PlcTarget {
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

    /** Absolute base path to which to write the generated code. */
    private String outputPath;

    /**
     * Constructor of the {@link PlcBaseTarget} class.
     *
     * @param targetType PLC target type for code generation.
     */
    public PlcBaseTarget(PlcTargetType targetType) {
        this.targetType = targetType;
    }

    @Override
    public PlcTargetType getTargetType() {
        return targetType;
    }

    /**
     * Initialize the target.
     *
     * @param settings Configuration to use.
     */
    public void setup(PlcGenSettings settings) {
        intTypeSize = settings.intTypeSize;
        realTypeSize = settings.realTypeSize;
        outputPath = settings.outputPath;

        // Warn the user about getting a possibly too small integer type size.
        if (settings.intTypeSize.getTypeSize(PlcBaseTarget.CIF_INTEGER_SIZE) < PlcBaseTarget.CIF_INTEGER_SIZE) {
            settings.warnOutput.warn(
                    "Configured integer type size is less than the CIF integer type size. Some values in the program "
                            + "may be truncated.");
        } else if (getMaxIntegerTypeSize() < PlcBaseTarget.CIF_INTEGER_SIZE) {
            settings.warnOutput
                    .warn("Maximum integer type size supported by the PLC is less than the CIF integer type size. Some "
                            + "values in the program may be truncated.");
        }

        // Warn the user about getting a possibly too small real type size.
        if (settings.realTypeSize.getTypeSize(PlcBaseTarget.CIF_REAL_SIZE) < PlcBaseTarget.CIF_REAL_SIZE) {
            settings.warnOutput
                    .warn("Configured real type size is less than the CIF real type size. Some values in the program "
                            + "may be truncated.");
        } else if (getMaxRealTypeSize() < PlcBaseTarget.CIF_REAL_SIZE) {
            settings.warnOutput
                    .warn("Maximum real type size supported by the PLC is less than the CIF real type size. Some "
                            + "values in the program may be truncated.");
        }
    }

    /**
     * Generate and write the PLC code.
     *
     * @param settings Configuration to use.
     */
    public void generate(PlcGenSettings settings) {
        setup(settings);

        // Construct the generators.
        NameGenerator nameGenerator = new DefaultNameGenerator(settings);
        PlcCodeStorage codeStorage = new PlcCodeStorage(this, settings);
        TypeGenerator typeGen = new DefaultTypeGenerator(this, settings, nameGenerator, codeStorage);
        CifProcessor cifProcessor = new CifProcessor(this, settings, typeGen, codeStorage, nameGenerator);

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
    protected abstract OutputTypeWriter getPlcCodeWriter();

    @Override
    public abstract boolean supportsArrays();

    @Override
    public abstract boolean supportsConstants();

    @Override
    public abstract boolean supportsEnumerations();

    @Override
    public boolean supportsOperation(PlcFuncOperation funcOper) {
        // By default the operation is supported.
        return true;
    }

    @Override
    public boolean supportsInfixNotation(PlcFuncOperation funcOper) {
        return true;
    }

    @Override
    public boolean supportsPower(boolean baseIsInt, boolean exponentIsInt) {
        return !baseIsInt; // First parameter must always have a real type.
    }

    /**
     * Get the size of the largest supported integer type.
     *
     * @return Number of bits used for storing the largest supported integer type.
     */
    protected abstract int getMaxIntegerTypeSize();

    @Override
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

    @Override
    public PlcElementaryType getRealType() {
        int generatorBestRealSize = Math.min(CIF_REAL_SIZE, getMaxRealTypeSize());
        int userSpecifiedRealSize = realTypeSize.getTypeSize(generatorBestRealSize);
        return PlcElementaryType.getRealTypeBySize(userSpecifiedRealSize);
    }

    @Override
    public void writeOutput(PlcProject project) {
        OutputTypeWriter writer = getPlcCodeWriter();
        writer.write(project, outputPath);
    }
}
