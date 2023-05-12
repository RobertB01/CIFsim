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
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.writers.OutputTypeWriter;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor;
import org.eclipse.escet.cif.plcgen.generators.DefaultNameGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultTypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultVariableStorage;
import org.eclipse.escet.cif.plcgen.generators.NameGenerator;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.VariableStorage;
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

    /** Conversion of PLC models to text for the target. */
    private final ModelTextGenerator modelTextGenerator = new ModelTextGenerator();

    /** Extracts information from the CIF input file, to be used during PLC code generation. */
    private CifProcessor cifProcessor;

    /** Handles storage and retrieval of globally used variables in the PLC program. */
    private VariableStorage varStorage;

    /** Handles type storage and conversions. */
    private TypeGenerator typeGenerator;

    /** Stores and writes generated PLC code. */
    private PlcCodeStorage codeStorage;

    /** Generate clash-free names in the generated code. */
    private NameGenerator nameGenerator;

    /**
     * Constructor of the {@link PlcBaseTarget} class.
     *
     * @param targetType PLC target type for code generation.
     */
    public PlcBaseTarget(PlcTargetType targetType) {
        this.targetType = targetType;
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
        if (settings.intTypeSize.getTypeSize(CIF_INTEGER_SIZE) < CIF_INTEGER_SIZE) {
            settings.warnOutput.warn(
                    "Configured integer type size is less than the CIF integer type size. Some values in the program "
                            + "may be truncated.");
        } else if (getMaxIntegerTypeSize() < CIF_INTEGER_SIZE) {
            settings.warnOutput
                    .warn("Maximum integer type size supported by the PLC is less than the CIF integer type size. Some "
                            + "values in the program may be truncated.");
        }

        // Warn the user about getting a possibly too small real type size.
        if (settings.realTypeSize.getTypeSize(CIF_REAL_SIZE) < CIF_REAL_SIZE) {
            settings.warnOutput
                    .warn("Configured real type size is less than the CIF real type size. Some values in the program "
                            + "may be truncated.");
        } else if (getMaxRealTypeSize() < CIF_REAL_SIZE) {
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

        nameGenerator = new DefaultNameGenerator(settings);
        codeStorage = new PlcCodeStorage(this, settings);
        typeGenerator = new DefaultTypeGenerator(this, settings);
        varStorage = new DefaultVariableStorage(this);
        cifProcessor = new CifProcessor(this, settings);

        // Check and normalize the CIF specification, and extract relevant information from it.
        cifProcessor.process();
        if (settings.shouldTerminate.get()) {
            return;
        }

        // Make the globally used variables ready for use in the PLC code.
        varStorage.process();
        if (settings.shouldTerminate.get()) {
            return;
        }

        // Prepare the PLC program for getting saved to the file system.
        codeStorage.finishPlcProgram();
        if (settings.shouldTerminate.get()) {
            return;
        }

        // And write it.
        codeStorage.writeOutput();
    }

    /**
     * Get the writer for writing the generated PLC code to the file system.
     *
     * @return The requested PLC code writer.
     */
    protected abstract OutputTypeWriter getPlcCodeWriter();

    @Override
    public PlcTargetType getTargetType() {
        return targetType;
    }

    @Override
    public ModelTextGenerator getModelTextGenerator() {
        return modelTextGenerator;
    }

    @Override
    public CifProcessor getCifProcessor() {
        return cifProcessor;
    }

    @Override
    public VariableStorage getVarStorage() {
        return varStorage;
    }

    @Override
    public TypeGenerator getTypeGenerator() {
        return typeGenerator;
    }

    @Override
    public PlcCodeStorage getCodeStorage() {
        return codeStorage;
    }

    @Override
    public NameGenerator getNameGenerator() {
        return nameGenerator;
    }

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
