//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor;
import org.eclipse.escet.cif.plcgen.generators.NameGenerator;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.TransitionGenerator;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.VariableStorage;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;

/** Code generator interface for a {@link PlcBaseTarget}. */
public interface PlcTarget {
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
     * @return The transition generator of the target
     */
    public abstract TransitionGenerator getTransitionGenerator();

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
    public abstract boolean supportsOperation(PlcFuncOperation funcOper);

    /**
     * Does the target support infix notation for the given semantic operation?
     *
     * @param funcOper Semantics operation being queried.
     * @return Whether the target support infix notation for the given operation.
     * @note The result is undefined for operations that are not supported by the target and for operations that do not
     *     have an infix notation.
     */
    public abstract boolean supportsInfixNotation(PlcFuncOperation funcOper);

    /**
     * Query whether the power function {@code base ** exponent} exists for a given combination of parameter types.
     *
     * @param baseIsInt If {@code true} the test queries for an integer typed base value. If {@code false} the test
     *     queries for a real typed base value.
     * @param exponentIsInt If {@code true} the test queries for an integer typed exponent value. If {@code false} the
     *     test queries for a real typed exponent value.
     * @return Whether the queried combination of base and exponent value types is supported by the PLC.
     * @note It is assumed that {@code supportsPower(false, false)} holds.
     */
    public abstract boolean supportsPower(boolean baseIsInt, boolean exponentIsInt);

    /**
     * Get the type of a standard integer value in the PLC.
     *
     * @return The type of a standard integer value in the PLC.
     */
    public abstract PlcElementaryType getIntegerType();

    /**
     * Get the type of a standard real value in the PLC.
     *
     * @return The type of a standard real value in the PLC.
     */
    public abstract PlcElementaryType getRealType();

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
    void writeOutput(PlcProject project);
}
