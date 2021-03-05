//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.stateviz;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** State visualization option. */
public class StateVisualizationOption extends BooleanOption {
    /** Constructor for the {@link StateVisualizationOption} class. */
    public StateVisualizationOption() {
        super("State visualization",
                "Whether to textually visualize the state of the specification in a table, during simulation "
                        + "(BOOL=yes), or not (BOOL=no). [DEFAULT=no]",
                null, "stateviz", "BOOL", false, true, "Enable this option to textually visualize the state of the "
                        + "specification in a table, during simulation.",
                "Visualize the state");
    }

    /**
     * Should state visualization be used?
     *
     * @return {@code true} if state visualization should be used, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(StateVisualizationOption.class);
    }
}
