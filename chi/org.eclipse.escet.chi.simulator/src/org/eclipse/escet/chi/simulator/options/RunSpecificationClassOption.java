//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.simulator.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option class for running a Chi specification class directly. */
public class RunSpecificationClassOption extends StringOption {
    /** Constructor of the {@link RunSpecificationClassOption} class. */
    public RunSpecificationClassOption() {
        super("Run specification", "Run a Chi specification directly.", null, "run-specification-class", "CLASS", null,
                true, true, "Chi specification class to run.", "Class name:");
    }

    /**
     * Returns the name of the Chi specification class to execute, or {@code null} if not provided.
     *
     * @return The name of the Chi specification class, or {@code null}.
     */
    public static String getPath() {
        return Options.get(RunSpecificationClassOption.class);
    }
}
