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

package org.eclipse.escet.cif.eventdisabler.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Event names option. */
public class EventNamesOption extends StringOption {
    /** Constructor for the {@link EventNamesOption} class. */
    public EventNamesOption() {
        super("Event names",
                "Specifies the absolute names of the events. How these supplied events are used depends on the "
                        + "'Event usage' option. Multiple events may be specified, separated by commas and/or spaces.",
                'e', "events", "NAMES", null, true, true,
                "The absolute names of the events. How these supplied events are used depends on the 'Event usage' "
                        + "option. Multiple events may be specified, separated by commas and/or spaces.",
                "Event names:");
    }

    /**
     * Returns the event names, or {@code null} if not specified.
     *
     * @return The event names, or {@code null}.
     */
    public static String getEventNames() {
        return Options.get(EventNamesOption.class);
    }
}
