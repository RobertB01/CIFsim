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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.BOOL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.DINT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.INT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.LINT_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.LREAL_TYPE;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.REAL_TYPE;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.WarnOutput;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;

/** Generator that creates input and output PLC code. */
public class InputOutputGenerator {
    /** Plc types that may be used for IO. */
    private static final Set<PlcElementaryType> FEASIBLE_IO_VAR_TYPES = set(BOOL_TYPE, INT_TYPE, DINT_TYPE, LINT_TYPE,
            REAL_TYPE, LREAL_TYPE);

    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** File path to the IO table file. File may not exist. */
    private final String ioTablePath;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /**
     * Constructor of the {@link InputOutputGenerator} class.
     *
     * @param target PLC target to generate code for.
     * @param settings Configuration to use.
     */
    public InputOutputGenerator(PlcTarget target, PlcGenSettings settings) {
        this.target = target;
        ioTablePath = settings.ioTablePath;
        warnOutput = settings.warnOutput;
    }

    /** Generate input/output code for communicating with the world outside the PLC. */
    public void process() {
    }
}
