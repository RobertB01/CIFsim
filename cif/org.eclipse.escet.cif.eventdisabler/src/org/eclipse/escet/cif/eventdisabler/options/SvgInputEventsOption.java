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

package org.eclipse.escet.cif.eventdisabler.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** SVG input events option. */
public class SvgInputEventsOption extends BooleanOption {
    /** Constructor for the {@link SvgInputEventsOption} class. */
    public SvgInputEventsOption() {
        super("SVG input events",
                "Specifies whether to use the SVG input events (BOOL=yes), or not (BOOL=no). "
                        + "How the events are used depends on the 'Event usage' option. [DEFAULT=no]",
                's', "svg-input-events", "BOOL", false, true, "Enable this option to use the SVG input events. "
                        + "How the events are used depends on the 'Event usage' option.",
                "Use SVG input events");
    }

    /**
     * Should the SVG input events be used?
     *
     * @return {@code true} if the SVG input events be used, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(SvgInputEventsOption.class);
    }
}
