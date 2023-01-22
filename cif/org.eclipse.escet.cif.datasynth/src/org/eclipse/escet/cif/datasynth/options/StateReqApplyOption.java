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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.cif.datasynth.options.StateReqApplyOption.StateReqApplyMode;
import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to specify how state requirement invariants are applied. */
public class StateReqApplyOption extends EnumOption<StateReqApplyMode> {
    /** Constructor for the {@link StateReqApplyOption} class. */
    public StateReqApplyOption() {
        super(
                // name
                "State requirement invariant application",

                // description
                "Specify how state requirement invariants are applied during synthesis. "
                        + "Specify \"ctrl-beh\" (default) to apply them all to the controlled behavior, "
                        + "or \"edge-guard-or-ctrl-beh\" to apply them per edge, to the guards for edges with "
                        + "controllable events, and to the controlled behavior for edges with uncontrollable events.",

                // cmdShort
                null,

                // cmdLong
                "state-req-invs",

                // cmdValue
                "MODE",

                // defaultValue
                StateReqApplyMode.CTRL_BEH,

                // showInDialog
                true,

                // optDialogDescr
                "Specify how state requirement invariants are applied during synthesis. "
                        + "Either apply them all to the controlled behavior, or apply them per edge, to the guards "
                        + "for edges with controllable events, and to the controlled behavior for edges with "
                        + "uncontrollable events.");
    }

    @Override
    protected String getDialogText(StateReqApplyMode mode) {
        switch (mode) {
            case CTRL_BEH:
                return "Controlled behavior";
            case EDGE_GUARD_OR_CTRL_BEH:
                return "Edge guard or controlled behavior";
        }
        throw new RuntimeException("Unknown mode: " + mode);
    }

    /**
     * Returns the value of the {@link StateReqApplyOption} option.
     *
     * @return The value of the {@link StateReqApplyOption} option.
     */
    public static StateReqApplyMode getMode() {
        return Options.get(StateReqApplyOption.class);
    }

    /** The way that state requirement invariants are applied. */
    public static enum StateReqApplyMode {
        /** Apply them all to the controlled behavior. */
        CTRL_BEH,

        /**
         * Apply them per edge, to the guards for edges with controllable events, and to the controlled behavior for
         * edges with uncontrollable events.
         */
        EDGE_GUARD_OR_CTRL_BEH;
    }
}
