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

package org.eclipse.escet.cif.eventbased.apps.options;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Preserved events option. */
public class PreservedEventsOption extends StringOption {
    /** Constant describing the option. */
    static final String OPTION_DESC = "Comma and/or whitespace separated absolute names of events that should "
            + "be preserved.";

    /**
     * Constructor for the {@link PreservedEventsOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public PreservedEventsOption() {
        super("Preserved events", // name.
                OPTION_DESC, // description.
                'p', // cmdShort.
                "preserve", // cmdLong.
                "EVENTS", // cmdValue.
                "", // defaultValue.
                false, // emptyAsNull.
                true, // showInDialog.
                OPTION_DESC, // optDialogDescr.
                "Events:"); // optDialogLabelText.
    }

    /**
     * Returns an array of entered event names.
     *
     * @return An array of entered event names.
     */
    public static String[] getEvents() {
        String evts = Options.get(PreservedEventsOption.class);
        return StringUtils.split(evts, " ,");
    }
}
