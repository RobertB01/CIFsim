//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Event order option. */
public class EventOrderOption extends StringOption {
    /** Constructor for the {@link EventOrderOption} class. */
    public EventOrderOption() {
        super(
                // name
                "Event order",

                // description
                "The event ordering. Specify " +

                        "\"model\" (default) for model ordering, " +

                        "\"reverse-model\" for reverse model ordering, " +

                        "\"random\" for random order (with random seed), " +

                        "\"random:SEED\" for random order (with \"SEED\" as seed, in range [0..2^64-1]), " +

                        "or specify a custom ordering. Custom orders consist of names of events and input variables. "
                        + "The \"*\" character can be used as wildcard in names, and indicates zero or more "
                        + "characters. Separate events and input variables with \",\".",

                // cmdShort
                'u',

                // cmdLong
                "event-order",

                // cmdValue
                "EVENTORDER",

                // defaultValue
                "model",

                // emptyAsNull
                false,

                // showInDialog
                true,

                // optDialogDescr
                "The event odering. Specify " +

                        "\"model\" for model ordering, with input variables sorted as in the variable ordering, " +

                        "\"reverse-model\" for reverse model ordering, with input variables reverse sorted as in the " +
                        "variable ordering, " +

                        "\"random\" for random order (with random seed), " +

                        "\"random:SEED\" for random order (with \"SEED\" as seed, in range [0..2^64-1]), " +

                        "or specify a custom ordering. Custom orders consist of names of events and input variables. "
                        + "The \"*\" character can be used as wildcard in names, and indicates zero or more characters "
                        + "characters. Separate events and input variables with \",\".",

                // optDialogLabelText
                "Event order:");
    }

    /**
     * Returns the value of the {@link EventOrderOption} option.
     *
     * @return The value of the {@link EventOrderOption} option.
     */
    public static String getOrder() {
        return Options.get(EventOrderOption.class);
    }
}
