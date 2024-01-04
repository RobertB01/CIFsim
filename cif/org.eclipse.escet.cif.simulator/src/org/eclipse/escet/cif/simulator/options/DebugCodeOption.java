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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.cif.simulator.compiler.CifCompilerContext;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Load debug code option. */
public class DebugCodeOption extends StringOption {
    /** Constructor for the {@link DebugCodeOption} class. */
    public DebugCodeOption() {
        super("Load debug code",
                "Specifies the absolute or relative local file system path to use as Java classpath, to find the "
                        + "already compiled code for the specification being simulated. This is generally the "
                        + "\target/classes\" directory of the \"" + CifCompilerContext.DBG_PROJ_NAME
                        + "\" project. Specify \"\" (default) to generate fresh code and compile it in-memory.",
                null, "debug-code", "DBGCODE", null, true, true,
                "Specify the absolute or relative local file system path to use as Java classpath, to find the "
                        + "already compiled code for the specification being simulated. This is generally the "
                        + "\"target/classes\" directory of the \"" + CifCompilerContext.DBG_PROJ_NAME
                        + "\" project. Leave empty to generate fresh code and compile it in-memory.",
                "Classpath:");
    }

    /**
     * Returns the absolute or relative local file system path to use as Java classpath, to find the compiled code for
     * the specification being simulated, or {@code null} to generate fresh code and compile it in-memory.
     *
     * @return The classpath, or {@code null}.
     */
    public static String getClasspath() {
        return Options.get(DebugCodeOption.class);
    }
}
