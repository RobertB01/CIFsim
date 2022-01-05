//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventdisabler.options;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Event usage option. */
public class EventUsageOption extends EnumOption<EventUsage> {
    /** Constructor for the {@link EventUsageOption} class. */
    public EventUsageOption() {
        super("Event usage",
                "Specifies how the supplied events are to be used. That is, it specifies which events to disable. "
                        + "Specify \"disable\" the supplied events, regardless of the alphabet of the input "
                        + "specification, or \"alphabet\" (default) to disable all supplied events, "
                        + "that are not in the alphabet of the input specification.",
                'u', "event-usage", "USAGE", EventUsage.ALPHABET, true,
                "How are the supplied events to be used? That is, which events should be disabled?");
    }

    @Override
    protected String getDialogText(EventUsage value) {
        switch (value) {
            case DISABLE:
                return "Disable the supplied events, regardless of the alphabet of the input specification";

            case ALPHABET:
                return "Disable all supplied events, that are not in the alphabet of the input specification";

            default:
                throw new RuntimeException("Unknown event usage: " + value);
        }
    }

    /**
     * Returns the event usage.
     *
     * @return The event usage.
     */
    public static EventUsage getUsage() {
        return Options.get(EventUsageOption.class);
    }
}
