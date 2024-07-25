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

package org.eclipse.escet.cif.plcgen.targets;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor;
import org.eclipse.escet.cif.plcgen.generators.ContinuousVariablesGenerator;
import org.eclipse.escet.cif.plcgen.generators.NameGenerator;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.TransitionGenerator;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.VariableStorage;
import org.eclipse.escet.cif.plcgen.generators.io.DefaultIoAddress;
import org.eclipse.escet.cif.plcgen.generators.io.IoAddress;
import org.eclipse.escet.cif.plcgen.generators.io.IoDirection;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.statements.PlcFuncApplStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;

/** Code generator 'interface' for a {@link PlcBaseTarget}. */
public abstract class PlcTarget {
    /**
     * Obtain the target type of the target.
     *
     * @return The target type of the target.
     */
    public abstract PlcTargetType getTargetType();

    /**
     * Retrieve the converter of the PLC model classes to their textual equivalent.
     *
     * @return The text generator for the PLC model classes.
     */
    public abstract ModelTextGenerator getModelTextGenerator();

    /**
     * Retrieve the CIF processor.
     *
     * @return The CIF processor of the target.
     */
    public abstract CifProcessor getCifProcessor();

    /**
     * Retrieve the transition generator.
     *
     * @return The transition generator of the target.
     */
    public abstract TransitionGenerator getTransitionGenerator();

    /**
     * Retrieve the continuous variables generator.
     *
     * @return The continuous variables generator of the target.
     */
    public abstract ContinuousVariablesGenerator getContinuousVariablesGenerator();

    /**
     * Retrieve the variable storage.
     *
     * @return The variable storage of the target.
     */
    public abstract VariableStorage getVarStorage();

    /**
     * Retrieve the type generator.
     *
     * @return The type generator of the target.
     */
    public abstract TypeGenerator getTypeGenerator();

    /**
     * Retrieve the PLC code storage.
     *
     * @return The PLC code storage of the target.
     */
    public abstract PlcCodeStorage getCodeStorage();

    /**
     * Retrieve the name generator.
     *
     * @return The name generator of the target.
     */
    public abstract NameGenerator getNameGenerator();

    /**
     * Get the prefix string for state variables.
     *
     * @return The prefix string for state variables.
     */
    public abstract String getStateVariablePrefix();

    /**
     * Get the name to use to call the {@code TON} function within the instance variable of the block function.
     *
     * @return The name to use to call the {@code TON} function within the instance variable of the block function.
     */
    public abstract String getTonFuncBlockCallName();

    /**
     * Returns whether the target supports arrays.
     *
     * @return Whether arrays are supported.
     */
    public abstract boolean supportsArrays();

    /**
     * Returns whether or not the PLC target type supports the given constant.
     *
     * @param constant The constant to consider.
     * @return Whether the constant is supported.
     */
    public abstract boolean supportsConstant(Constant constant);

    /**
     * Return how to convert enumerations.
     *
     * @return The desired conversion to enumerations. Never returns {@link ConvertEnums#AUTO}.
     */
    public abstract ConvertEnums getActualEnumerationsConversion();

    /**
     * Does the target support the given semantic operation?
     *
     * @param funcOper Semantics operation being queried.
     * @param numArgs Number of supplied arguments to the applied function.
     * @return Whether the target supports the given operation.
     * @see #getSupportedFuncNotations
     */
    public final boolean supportsOperation(PlcFuncOperation funcOper, int numArgs) {
        return !getSupportedFuncNotations(funcOper, numArgs).isEmpty();
    }

    /**
     * Get the set of supported function-call notations for the given semantic operation.
     *
     * <p>
     * Notes:
     * <ul>
     * <li>Remove operations by not having a notation for them. This only works if the code generator can fallback to
     * another way to express the needed functionality. Currently that is only implemented for LOG.</li>
     * <li>The {@link PlcFuncApplStatement} class needs a function in prefix notation.</li>
     * </ul>
     * </p>
     *
     * @param funcOper Semantics operation being queried.
     * @param numArgs Number of supplied arguments to the applied function.
     * @return The set of supported function-call notations for the operation.
     * @see #supportsOperation
     */
    public abstract EnumSet<PlcFuncNotation> getSupportedFuncNotations(PlcFuncOperation funcOper, int numArgs);

    /**
     * Retrieve the supported integer types of the target, ordered by increasing size.
     *
     * @return The supported integer types of the target, ordered by increasing size.
     */
    public abstract List<PlcElementaryType> getSupportedIntegerTypes();

    /**
     * Get the size of the largest supported integer type.
     *
     * @return Number of bits used for storing the largest supported integer type.
     */
    public abstract int getMaxIntegerTypeSize();

    /**
     * Get the type of a standard integer value in the PLC.
     *
     * <p>
     * As CIF uses signed 32 bit integer, a {@code DINT} is recommended.
     * </p>
     *
     * @return The type of a standard integer value in the PLC.
     */
    public abstract PlcElementaryType getStdIntegerType();

    /**
     * Construct a new standard integer literal with the given value.
     *
     * @param value Value of the new standard integer literal.
     * @return The created literal.
     */
    public PlcIntLiteral makeStdInteger(int value) {
        return new PlcIntLiteral(value, getStdIntegerType());
    }

    /**
     * Retrieve the supported real types of the target, ordered by increasing size.
     *
     * @return The supported real types of the target, ordered by increasing size.
     */
    public abstract List<PlcElementaryType> getSupportedRealTypes();

    /**
     * Get the size of the largest supported real type.
     *
     * @return Number of bits used for storing the largest supported real type.
     */
    public abstract int getMaxRealTypeSize();

    /**
     * Get the type of a standard real value in the PLC.
     *
     * <p>
     * As CIF uses 64 bit reals, an {@code LREAL} is recommended.
     * </p>
     *
     * @return The type of a standard real value in the PLC.
     */
    public abstract PlcElementaryType getStdRealType();

    /**
     * Construct a new standard real literal with the given value.
     *
     * @param value Value of the new standard real literal.
     * @return The created literal.
     */
    public PlcRealLiteral makeStdReal(String value) {
        return new PlcRealLiteral(value, getStdRealType());
    }

    /**
     * Parse a PLC I/O address.
     *
     * @param plcAddressText Text to parse.
     * @return The parsed address information and its properties or {@code null} if the text cannot be parsed.
     */
    public IoAddress parseIoAddress(String plcAddressText) {
        return DefaultIoAddress.parseAddress(plcAddressText);
    }

    /**
     * Verify that the given I/O table entry is acceptable to the target.
     *
     * <p>
     * If the entry is not acceptable, it should be reported to the user with an {@link InvalidInputException}.
     * </p>
     *
     * @param address The I/O address to verify.
     * @param plcTableType Type of the I/O data being transferred.
     * @param directionFromCif Direction of the I/O table entry.
     * @param tableLinePositionText Text describing the table line for this entry, to use for reporting an error. The
     *     text is {@code "at line ... of I/O table file \"...\""}.
     * @throws InputOutputException If the provided entry is not acceptable to the target.
     */
    public abstract void verifyIoTableEntry(IoAddress address, PlcType plcTableType, IoDirection directionFromCif,
            String tableLinePositionText);

    /**
     * Get replacement string for the CIF input file extension including dot, used to derive an output path.
     *
     * @return The replacement string.
     */
    public abstract String getPathSuffixReplacement();

    /**
     * Write the project to the output.
     *
     * @param project Project to write.
     * @note Depending on the actual write implementation a single file or a directory may be written.
     */
    public abstract void writeOutput(PlcProject project);
}
