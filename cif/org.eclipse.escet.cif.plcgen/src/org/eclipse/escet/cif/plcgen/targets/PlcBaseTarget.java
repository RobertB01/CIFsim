//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import java.util.EnumSet;

import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor;
import org.eclipse.escet.cif.plcgen.generators.ContinuousVariablesGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultContinuousVariablesGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultNameGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultTransitionGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultTypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultVariableStorage;
import org.eclipse.escet.cif.plcgen.generators.InputOutputGenerator;
import org.eclipse.escet.cif.plcgen.generators.NameGenerator;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.TransitionGenerator;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.VariableStorage;
import org.eclipse.escet.cif.plcgen.generators.io.IoAddress;
import org.eclipse.escet.cif.plcgen.generators.io.IoDirection;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.ConvertEnumsOption;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.output.WarnOutput;

/** Base class for generating a {@link PlcProject}. */
public abstract class PlcBaseTarget extends PlcTarget {
    /** Size of an integer value in a CIF specification. */
    public static final int CIF_INTEGER_SIZE = 32;

    /** Size of a real value in a CIF specification. */
    public static final int CIF_REAL_SIZE = 64;

    /** PLC target type for code generation. */
    public final PlcTargetType targetType;

    /** The prefix string for state variables. */
    protected final String stateVariablePrefix;

    /** Suffix text to append after the block instance variable name to call the TON function block. */
    protected final String tonFuncBlockCallSuffix;

    /** User-defined integer type size to use by the PLC. */
    private PlcNumberBits intTypeSize;

    /** User-defined real type size to use by the PLC. */
    private PlcNumberBits realTypeSize;

    /** How to convert enumerations when the {@link ConvertEnumsOption} is set to {@link ConvertEnums#AUTO}. */
    private final ConvertEnums autoEnumConversion;

    /** How to convert enumerations. */
    private ConvertEnums selectedEnumConversion;

    /** Absolute base path to which to write the generated code. */
    private String absOutputPath;

    /** Callback to send warnings to the user. */
    private WarnOutput warnOutput;

    /** Conversion of PLC models to text for the target. */
    private final ModelTextGenerator modelTextGenerator = new ModelTextGenerator();

    /** Extracts information from the CIF input file, to be used during PLC code generation. */
    protected CifProcessor cifProcessor;

    /** Generates PLC code for performing CIF event transitions. */
    protected TransitionGenerator transitionGenerator;

    /** Code generator for handling continuous behavior. */
    private ContinuousVariablesGenerator continuousVariablesGenerator;

    /** Generator that creates input and output PLC code. */
    protected InputOutputGenerator ioGenerator;

    /** Handles storage and retrieval of globally used variables in the PLC program. */
    protected VariableStorage varStorage;

    /** Handles type storage and conversions. */
    protected TypeGenerator typeGenerator;

    /** Stores and writes generated PLC code. */
    protected PlcCodeStorage codeStorage;

    /** Generate clash-free names in the generated code. */
    protected NameGenerator nameGenerator;

    /**
     * Constructor of the {@link PlcBaseTarget} class, with empty prefix string for state variables.
     *
     * @param targetType PLC target type for code generation.
     * @param autoEnumConversion How to convert enumerations when the user selects {@link ConvertEnums#AUTO}. This
     *     should not be {@link ConvertEnums#AUTO}.
     */
    public PlcBaseTarget(PlcTargetType targetType, ConvertEnums autoEnumConversion) {
        this(targetType, autoEnumConversion, "", "");
    }

    /**
     * Constructor of the {@link PlcBaseTarget} class.
     *
     * @param targetType PLC target type for code generation.
     * @param autoEnumConversion How to convert enumerations when the user selects {@link ConvertEnums#AUTO}. This
     *     should not be {@link ConvertEnums#AUTO}.
     * @param stateVariablePrefix The prefix string for state variables.
     * @param tonFuncBlockCallSuffix Suffix text to append after the block instance variable name to call the TON
     *     function block.
     */
    public PlcBaseTarget(PlcTargetType targetType, ConvertEnums autoEnumConversion, String stateVariablePrefix,
            String tonFuncBlockCallSuffix)
    {
        this.targetType = targetType;
        this.autoEnumConversion = autoEnumConversion;
        this.stateVariablePrefix = stateVariablePrefix;
        this.tonFuncBlockCallSuffix = tonFuncBlockCallSuffix;

        // Selecting "auto" by the user should result in a concrete preference of the target.
        Assert.check(autoEnumConversion != ConvertEnums.AUTO);
    }

    /**
     * Initialize the target.
     *
     * @param settings Configuration to use.
     */
    public void setup(PlcGenSettings settings) {
        intTypeSize = settings.intTypeSize;
        realTypeSize = settings.realTypeSize;
        absOutputPath = settings.absOutputPath;
        warnOutput = settings.warnOutput;
        selectedEnumConversion = (settings.enumConversion == ConvertEnums.AUTO) ? autoEnumConversion
                : settings.enumConversion;

        // Warn the user about getting a possibly too small integer type size.
        if (settings.intTypeSize.getTypeSize(CIF_INTEGER_SIZE) < CIF_INTEGER_SIZE) {
            warnOutput.line(
                    "Configured integer type size is less than the CIF integer type size. Some values in the program "
                            + "may be truncated.");
        } else if (getMaxIntegerTypeSize() < CIF_INTEGER_SIZE) {
            warnOutput.line("Maximum integer type size supported by the PLC is less than the CIF integer type size. "
                    + "Some values in the program may be truncated.");
        }

        // Warn the user about getting a possibly too small real type size.
        if (settings.realTypeSize.getTypeSize(CIF_REAL_SIZE) < CIF_REAL_SIZE) {
            warnOutput.line("Configured real type size is less than the CIF real type size. Some values in the program "
                    + "may be truncated.");
        } else if (getMaxRealTypeSize() < CIF_REAL_SIZE) {
            warnOutput.line("Maximum real type size supported by the PLC is less than the CIF real type size. Some "
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
        transitionGenerator = new DefaultTransitionGenerator(this);
        ioGenerator = new InputOutputGenerator(this, settings);
        continuousVariablesGenerator = new DefaultContinuousVariablesGenerator(this);

        // Check and normalize the CIF specification, and extract relevant information from it.
        cifProcessor.process();
        if (settings.shouldTerminate.get()) {
            return;
        }

        // Add code and data to variable storage for the previously supplied continuous variables.
        continuousVariablesGenerator.process();
        if (settings.shouldTerminate.get()) {
            return;
        }

        // Generate input and output code.
        ioGenerator.process();
        if (settings.shouldTerminate.get()) {
            return;
        }

        // Make the globally used variables ready for use in the PLC code.
        varStorage.process();
        if (settings.shouldTerminate.get()) {
            return;
        }

        // Generate the event transition functions.
        transitionGenerator.generate();
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
    protected abstract Writer getPlcCodeWriter();

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
    public TransitionGenerator getTransitionGenerator() {
        return transitionGenerator;
    }

    @Override
    public ContinuousVariablesGenerator getContinuousVariablesGenerator() {
        return continuousVariablesGenerator;
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
    public ConvertEnums getActualEnumerationsConversion() {
        return selectedEnumConversion;
    }

    @Override
    public String getStateVariablePrefix() {
        return stateVariablePrefix;
    }

    @Override
    public String getTonFuncBlockCallSuffix() {
        return tonFuncBlockCallSuffix;
    }

    @Override
    public EnumSet<PlcFuncNotation> getSupportedFuncNotations(PlcFuncOperation funcOper, int numArgs) {
        // Notations get removed from the set when there is no infix or prefix name available.
        return PlcFuncNotation.ALL;
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
    public void verifyIoTableEntry(IoAddress parsedAddress, PlcType plcTableType, IoDirection directionFromCif,
            String tableLinePositionText)
    {
        if (parsedAddress.size() > getMaxIntegerTypeSize()) {
            // Accept everything, but give a warning if trouble may arise at runtime.
            warnOutput.line(
                    "Size of I/O address \"%s\" (of %d bits) exceeds the size of the largest supported integer type "
                            + "(of %d bits).",
                    parsedAddress.getAddress(), parsedAddress.size(), getMaxIntegerTypeSize());
        }
    }

    @Override
    public void writeOutput(PlcProject project) {
        Writer writer = getPlcCodeWriter();
        writer.write(project, absOutputPath);
    }
}
