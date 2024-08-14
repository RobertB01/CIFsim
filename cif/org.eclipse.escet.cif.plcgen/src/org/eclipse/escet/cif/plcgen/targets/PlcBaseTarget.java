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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor.CifProcessorResults;
import org.eclipse.escet.cif.plcgen.generators.ContinuousVariablesGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultContinuousVariablesGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultNameGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultTransitionGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultTypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.DefaultVariableStorage;
import org.eclipse.escet.cif.plcgen.generators.InputOutputGenerator;
import org.eclipse.escet.cif.plcgen.generators.NameGenerator;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.PlcVariablePurpose;
import org.eclipse.escet.cif.plcgen.generators.TransitionGenerator;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.VariableStorage;
import org.eclipse.escet.cif.plcgen.generators.io.IoAddress;
import org.eclipse.escet.cif.plcgen.generators.io.IoDirection;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.ConvertEnumsOption;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.output.WarnOutput;

/** Base class for generating a {@link PlcProject}. */
public abstract class PlcBaseTarget extends PlcTarget {
    /** Size of an integer value in a CIF specification. */
    public static final int CIF_INTEGER_SIZE = 32;

    /** Size of a real value in a CIF specification. */
    public static final int CIF_REAL_SIZE = 64;

    /** PLC target type for code generation. */
    public final PlcTargetType targetType;

    /** Name to use to call the {@code TON} function within the instance variable of the block function. */
    protected final String tonFuncBlockCallName;

    /** User-defined integer type size to use by the PLC. */
    private PlcNumberBits intTypeSize;

    /** User-defined real type size to use by the PLC. */
    private PlcNumberBits realTypeSize;

    /** How to convert enumerations when the {@link ConvertEnumsOption} is set to {@link ConvertEnums#AUTO}. */
    private final ConvertEnums autoEnumConversion;

    /** How to convert enumerations. */
    private ConvertEnums selectedEnumConversion;

    /** Paths to write the generated code. Depending on the target can be either a file or a directory path. */
    private PathPair outputPaths;

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
     * Constructor of the {@link PlcBaseTarget} class.
     *
     * @param targetType PLC target type for code generation.
     * @param autoEnumConversion How to convert enumerations when the user selects {@link ConvertEnums#AUTO}. This
     *     should not be {@link ConvertEnums#AUTO}.
     */
    public PlcBaseTarget(PlcTargetType targetType, ConvertEnums autoEnumConversion) {
        this(targetType, autoEnumConversion, "");
    }

    /**
     * Constructor of the {@link PlcBaseTarget} class.
     *
     * @param targetType PLC target type for code generation.
     * @param autoEnumConversion How to convert enumerations when the user selects {@link ConvertEnums#AUTO}. This
     *     should not be {@link ConvertEnums#AUTO}.
     * @param tonFuncBlockCallName Name to use to call the {@code TON} function within the instance variable of the
     *     block function.
     */
    public PlcBaseTarget(PlcTargetType targetType, ConvertEnums autoEnumConversion, String tonFuncBlockCallName) {
        this.targetType = targetType;
        this.autoEnumConversion = autoEnumConversion;
        this.tonFuncBlockCallName = tonFuncBlockCallName;

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
        outputPaths = settings.outputPaths;
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

        // Preparation.
        //
        // Extend the set of reserved names in the name generator, to avoid conflicts.
        nameGenerator.addDisallowedNames(ioGenerator.getCustomIoNames());

        // Processing and code generation.
        //
        // Check and normalize the CIF specification, and extract relevant information from it.
        CifProcessorResults processorResults = cifProcessor.process();
        if (settings.termination.isRequested()) {
            return;
        }

        // Distribute results of the CIF processor.
        for (DiscVariable discVar: processorResults.discVariables()) {
            varStorage.addStateVariable(discVar, discVar.getType());
        }
        for (InputVariable inpVar: processorResults.inputVariables()) {
            varStorage.addStateVariable(inpVar, inpVar.getType());
        }
        for (EnumDecl enumDecl: processorResults.enumDecls()) {
            typeGenerator.convertEnumDecl(enumDecl);
        }
        for (ContVariable contVar: processorResults.contVariables()) {
            varStorage.addStateVariable(contVar, newRealType());
            continuousVariablesGenerator.addVariable(contVar);
        }
        for (Constant constant: processorResults.constants()) {
            varStorage.addConstant(constant);
        }
        codeStorage.addComponentDatas(processorResults.componentDatas());
        if (settings.termination.isRequested()) {
            return;
        }

        // Add code and data to variable storage for the previously supplied continuous variables.
        continuousVariablesGenerator.process();
        if (settings.termination.isRequested()) {
            return;
        }

        // Generate input and output code.
        ioGenerator.process(processorResults.cifObjectFinder());
        if (settings.termination.isRequested()) {
            return;
        }

        // Make the globally used variables ready for use in the PLC code.
        varStorage.process();
        if (settings.termination.isRequested()) {
            return;
        }

        // Generate the event transition functions.
        List<CifEventTransition> allCifEventTransitions = processorResults.cifEventTransitions();
        transitionGenerator.setup(allCifEventTransitions);

        // Split event transitions between controllable and uncontrollable events.
        List<List<CifEventTransition>> transLoops = List.of(
                allCifEventTransitions.stream().filter(cet -> !cet.event.getControllable()).toList(),
                allCifEventTransitions.stream().filter(cet -> cet.event.getControllable()).toList());

        // Generated the transition code.
        ExprGenerator exprGen = codeStorage.getExprGenerator();
        PlcBasicVariable isProgressVar = codeStorage.getIsProgressVariable();
        List<List<PlcStatement>> loopsStatements = transitionGenerator.generate(transLoops, exprGen, isProgressVar);
        if (settings.termination.isRequested()) {
            return;
        }

        // Prepare the PLC program for getting saved to the file system.
        codeStorage.addEventTransitions(loopsStatements.get(0), loopsStatements.get(1));
        codeStorage.finishPlcProgram();
        if (settings.termination.isRequested()) {
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
        Assert.notNull(modelTextGenerator);
        return modelTextGenerator;
    }

    @Override
    public CifProcessor getCifProcessor() {
        Assert.notNull(cifProcessor);
        return cifProcessor;
    }

    @Override
    public TransitionGenerator getTransitionGenerator() {
        Assert.notNull(transitionGenerator);
        return transitionGenerator;
    }

    @Override
    public ContinuousVariablesGenerator getContinuousVariablesGenerator() {
        Assert.notNull(continuousVariablesGenerator);
        return continuousVariablesGenerator;
    }

    @Override
    public VariableStorage getVarStorage() {
        Assert.notNull(varStorage);
        return varStorage;
    }

    @Override
    public TypeGenerator getTypeGenerator() {
        Assert.notNull(typeGenerator);
        return typeGenerator;
    }

    @Override
    public PlcCodeStorage getCodeStorage() {
        Assert.notNull(codeStorage);
        return codeStorage;
    }

    @Override
    public NameGenerator getNameGenerator() {
        Assert.notNull(nameGenerator);
        return nameGenerator;
    }

    @Override
    public ConvertEnums getActualEnumerationsConversion() {
        return selectedEnumConversion;
    }

    @Override
    public String getUsageVariableText(PlcVariablePurpose purpose, String varName) {
        return varName;
    }

    @Override
    public String getTonFuncBlockCallName() {
        return tonFuncBlockCallName;
    }

    /**
     * Common code that decides allowance for a small subset of constants.
     *
     * <p>
     * Allowed constants are values of boolean, integer, real and enumeration type.
     * </p>
     *
     * @param constant Constant to consider.
     * @return Whether the give constant is part of the subset.
     */
    protected static boolean commonSupportedConstants(Constant constant) {
        CifType tp = CifTypeUtils.unwrapType(constant.getType());
        return tp instanceof BoolType || tp instanceof IntType || tp instanceof RealType || tp instanceof EnumType;
    }

    @Override
    public EnumSet<PlcFuncNotation> getSupportedFuncNotations(PlcFuncOperation funcOper, int numArgs) {
        // Notations get removed from the set when there is no infix or prefix name available.
        return PlcFuncNotation.ALL;
    }

    @Override
    public int getMaxIntegerTypeSize() {
        return last(getSupportedIntegerTypes()).bitSize;
    }

    @Override
    public PlcElementaryType getStdIntegerType() {
        int generatorBestIntSize = Math.min(CIF_INTEGER_SIZE, getMaxIntegerTypeSize());
        int userSpecifiedIntSize = intTypeSize.getTypeSize(generatorBestIntSize);
        return PlcElementaryType.getTypeByRequiredBits(userSpecifiedIntSize, PlcElementaryType.INTEGER_TYPES_ALL);
    }

    @Override
    public int getMaxRealTypeSize() {
        return last(getSupportedRealTypes()).bitSize;
    }

    @Override
    public PlcElementaryType getStdRealType() {
        int generatorBestRealSize = Math.min(CIF_REAL_SIZE, getMaxRealTypeSize());
        int userSpecifiedRealSize = realTypeSize.getTypeSize(generatorBestRealSize);
        return PlcElementaryType.getTypeByRequiredBits(userSpecifiedRealSize, PlcElementaryType.REAL_TYPES_ALL);
    }

    @Override
    public void verifyIoTableEntry(IoAddress parsedAddress, PlcType plcTableType, IoDirection directionFromCif,
            String ioName, String tableLinePositionText)
    {
        // Get the maximum supported width for the type.
        int maxAvailableBits;
        String typeText;
        if (PlcElementaryType.isIntType(plcTableType)) {
            maxAvailableBits = getMaxIntegerTypeSize();
            typeText = "integer";
        } else if (PlcElementaryType.isRealType(plcTableType)) {
            maxAvailableBits = getMaxRealTypeSize();
            typeText = "real";
        } else if (plcTableType.equals(PlcElementaryType.BOOL_TYPE)) {
            maxAvailableBits = 1;
            typeText = "boolean";
        } else {
            throw new AssertionError("Unexpected PLC type \"" + plcTableType + "\" found.");
        }

        // Check that the address size is within supported limits. If not, give a warning.
        if (parsedAddress.size() > maxAvailableBits) {
            warnOutput.line(
                    "Size of I/O address \"%s\" (of %d bits) exceeds the size of the largest supported %s type "
                            + "(of %d bits).",
                    parsedAddress.getAddress(), parsedAddress.size(), typeText, maxAvailableBits);
        }

        // Check a supplied I/O variable name for being acceptable to the target.
        if (ioName != null && !checkIoVariableName(ioName)) {
            String msg = fmt("I/O variable name \"%s\" %s is not a valid name for the selected target.",
                    ioName, tableLinePositionText);
            throw new InvalidInputException(msg);
        }
    }

    @Override
    public boolean checkIoVariableName(String name) {
        // The generic implementation checks the name for being a regular ASCII identifier with a few limitations on
        // underscore character usage (not at start or end, and no consecutive underscore characters).
        Assert.notNull(name);

        Pattern p = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
        Matcher m = p.matcher(name);
        if (!m.matches()) {
            return false;
        }

        // Limit underscore characters to single underscores within the name.
        return !name.startsWith("_") && !name.endsWith("_") && !name.contains("__");
    }

    @Override
    public void writeOutput(PlcProject project) {
        Writer writer = getPlcCodeWriter();
        writer.write(project, outputPaths);
    }
}
