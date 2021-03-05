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

package org.eclipse.escet.cif.eventdisabler.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Event names file option. */
public class EventNamesFileOption extends StringOption {
    /** Constructor for the {@link EventNamesFileOption} class. */
    public EventNamesFileOption() {
        super("Event names file",
                "Specifies the absolute or relative local file system path of a file with the absolute names of "
                        + "the events. How these supplied events are used depends on the 'Event usage' "
                        + "option. Multiple events may be specified in the file, on separate lines.",
                'f', "events-file", "EFILE", null, true, true,
                "The absolute or relative local file system path of a file with the absolute names of the events. "
                        + "How these supplied events are used depends on the 'Event usage' option. "
                        + "Multiple events may be specified in the file, on separate lines.",
                "File path:");
    }

    /**
     * Returns the absolute or relative local file system path to the event names file, or {@code null} if not
     * specified.
     *
     * @return The absolute or relative local file system path to the event names file, or {@code null}.
     */
    public static String getFilePath() {
        return Options.get(EventNamesFileOption.class);
    }
}
