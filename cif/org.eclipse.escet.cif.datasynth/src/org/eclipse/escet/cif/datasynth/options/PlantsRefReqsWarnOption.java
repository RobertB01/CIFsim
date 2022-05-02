//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Plant referencing requirement warning option. */
public class PlantsRefReqsWarnOption extends BooleanOption {
    /** Constructor for the {@link PlantsRefReqsWarnOption} class. */
    public PlantsRefReqsWarnOption() {
        super(
                // name
                "Requirement reference warning",

                // description
                "Whether to warn for plant automata or plant invariants that reference requirement automata (BOOL=yes)"
                + "or don't warn (BOOL=no). [default=yes]",

                // cmdShort
                null,

                // cmdLong
                "req-ref-warn",

                // cmdValue
                "BOOL",

                // defaultValue
                true,

                // showInDialog
                true,

                // optDialogDescr
                "Whether to warn for plant automata or plant invariants that reference requirement automata.",

                // optDialogCheckboxText
                "Warn for plants that reference requirements.");
    }

    /**
     * Is warning for plants referencing requirements enabled?
     *
     * @return {@code true} if warning for plants referencing requirements is enabled, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(PlantsRefReqsWarnOption.class);
    }
}
