//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.datasynth.options.EdgeOrderDuplicateEventsOption.EdgeOrderDuplicateEvents;
import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Edge order duplicate events option. */
public class EdgeOrderDuplicateEventsOption extends EnumOption<EdgeOrderDuplicateEvents> {
    /** Constructor for the {@link EdgeOrderDuplicateEventsOption} class. */
    public EdgeOrderDuplicateEventsOption() {
        super(
                // name
                "Edge order duplicate events",

                // description
                "Specify whether duplicate events are allowed for custom edge orders. "
                        + "Specify \"disallowed\" (default) to disallow duplicate events, "
                        + "or \"allowed\" to allow duplicate events.",

                // cmdShort
                null,

                // cmdLong
                "edge-order-duplicate-events",

                // cmdValue
                "ALLOWANCE",

                // defaultValue
                EdgeOrderDuplicateEvents.DISALLOWED,

                // showInDialog
                true,

                // optDialogDescr
                "Specify whether duplicate events are allowed for custom edge orders.");
    }

    @Override
    protected String getDialogText(EdgeOrderDuplicateEvents duplicateEvents) {
        switch (duplicateEvents) {
            case DISALLOWED:
                return "Duplicate events are disallowed";
            case ALLOWED:
                return "Duplicate events are allowed";
        }
        throw new RuntimeException("Unknown duplicate events allowance: " + duplicateEvents);
    }

    /**
     * Returns the value of the {@link EdgeOrderDuplicateEventsOption} option.
     *
     * @return The value of the {@link EdgeOrderDuplicateEventsOption} option.
     */
    public static EdgeOrderDuplicateEvents getDuplicateEvents() {
        return Options.get(EdgeOrderDuplicateEventsOption.class);
    }

    /** Edge order duplicate event allowance. */
    public static enum EdgeOrderDuplicateEvents {
        /** Duplicate events are allowed. */
        ALLOWED,

        /** Duplicate events are disallowed. */
        DISALLOWED;
    }
}
