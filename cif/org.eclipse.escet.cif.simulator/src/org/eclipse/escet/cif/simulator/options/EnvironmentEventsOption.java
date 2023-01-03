//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.options;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.simulator.output.svgviz.RuntimeCifSvgDecls;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Environment events option, to specify the non-urgent events. */
public class EnvironmentEventsOption extends StringOption {
    /** The regular expression pattern to use to check the environment event filters for validity. */
    private static final Pattern FILTER_PATTERN = Pattern
            .compile("[a-zA-Z_*][a-zA-Z0-9_*]*(\\.[a-zA-Z_*][a-zA-Z0-9_*]*)*");

    /** Constructor for the {@link EnvironmentEventsOption} class. */
    public EnvironmentEventsOption() {
        super("Environment events",
                "Option to specify the environment events. Specify comma separated absolute names of events, or "
                        + "\"tau\". The \"*\" character can be used as wildcard in event names, and indicates zero "
                        + "or more characters. Use \"svg\" to specify that all SVG interactive events should also "
                        + "be environment events. [DEFAULT=\"\"]",
                null, "env-events", "EVTS", "", false, true,
                "Option to specify the environment events. Specify comma separated absolute names of events, or "
                        + "\"tau\". The \"*\" character can be used as wildcard in event names, and indicates zero "
                        + "or more characters. Use \"svg\" to specify that all SVG interactive events should also be "
                        + "environment events.",
                "Event names:");
    }

    /**
     * Splits the textual value of the option into separate filters, trims them, and checks them for validity.
     *
     * @return The textual value of the option split into separate filters.
     */
    private static String[] getFilters() {
        // Get textual option value.
        String value = Options.get(EnvironmentEventsOption.class);

        // Split value into filters.
        String[] filters = StringUtils.split(value, ",");

        // Process and check each filter.
        for (int i = 0; i < filters.length; i++) {
            // Process the filter.
            filters[i] = filters[i].trim();

            // Check the filter.
            if (FILTER_PATTERN.matcher(filters[i]).matches()) {
                continue;
            }

            // Invalid syntax.
            String msg = fmt("Environment event filter \"%s\" has invalid syntax.", filters[i]);
            throw new InvalidOptionException(msg);
        }

        // Return the filters.
        return filters;
    }

    /**
     * Determines per event, whether or not it is urgent. Environment events are non-urgent, all other events are
     * urgent. Event 'tau' is treated as any other event. Special name 'svg' is used to refer to all SVG interactive
     * events.
     *
     * @param <S> The type of state objects to use.
     * @param spec The CIF specification. It has been mostly initialized, but not yet for urgency.
     * @return Per event, {@code true} if it is an urgent event, {@code false} otherwise.
     */
    public static <S extends RuntimeState> boolean[] getUrgentEvents(RuntimeSpec<?> spec) {
        // Get filters from option value.
        String[] filters = getFilters();

        // Initialize result (all urgent).
        boolean[] rslt = new boolean[spec.events.size()];
        Arrays.fill(rslt, true);

        // Optimize for no filters.
        if (filters.length == 0) {
            return rslt;
        }

        // Get event names, without '$'.
        String[] names = new String[spec.events.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = spec.events.get(i).name.replace("$", "");
        }

        // Process all filters.
        for (String filter: filters) {
            // Match events.
            boolean match = false;
            boolean effect = false;
            if (filter.equals("svg")) {
                // Match against SVG interactive/input events.
                boolean[] interactive = getSvgInteractiveEvents(spec);
                for (int i = 0; i < spec.events.size(); i++) {
                    if (interactive[i]) {
                        if (rslt[i]) {
                            effect = true;
                        }
                        rslt[i] = false;
                        match = true;
                    }
                }
            } else {
                // Create regular expression from filter.
                String regEx = filter.replace(".", "\\.");
                regEx = regEx.replace("*", ".*");
                Pattern pattern = Pattern.compile("^" + regEx + "$");

                // Match against regular expression.
                for (int i = 0; i < names.length; i++) {
                    if (pattern.matcher(names[i]).matches()) {
                        if (rslt[i]) {
                            effect = true;
                        }
                        rslt[i] = false;
                        match = true;
                    }
                }
            }

            // Warn about filters that have no effect.
            if (!match) {
                String msg = fmt(
                        "Environment event filter \"%s\" does not match any of the events in the specification.",
                        filter);
                if (filter.equals("svg")) {
                    msg += " The specification does not contain any SVG interactive events.";
                }
                warn(msg);
            } else if (!effect) {
                String msg = fmt("Environment event filter \"%s\" does not have any effect. The matched events were "
                        + "already made environment events by an earlier filter.", filter);
                warn(msg);
            }
        }

        // Return the information, per event.
        return rslt;
    }

    /**
     * Returns per event of the specification, whether the event is an SVG interactive event.
     *
     * @param spec The specification.
     * @return Per event of the specification, whether the event is an SVG interactive event.
     */
    private static boolean[] getSvgInteractiveEvents(RuntimeSpec<?> spec) {
        // Initialize to non-interactive.
        boolean[] interactive = new boolean[spec.events.size()];

        // Mark as interactive.
        for (RuntimeCifSvgDecls decls: spec.getCifSvgDecls()) {
            boolean[] declsInteractive = decls.getInteractiveEvents();
            for (int i = 0; i < interactive.length; i++) {
                interactive[i] |= declsInteractive[i];
            }
        }
        return interactive;
    }
}
