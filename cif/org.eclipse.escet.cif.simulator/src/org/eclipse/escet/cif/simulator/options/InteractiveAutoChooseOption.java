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

package org.eclipse.escet.cif.simulator.options;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/**
 * Automatic transition choice option, for the interactive input mode. Can be used in interactive input mode to make it
 * semi-automatic, by automatically choosing certain transitions.
 *
 * <p>
 * If none of the automatic choices are available, the user is still asked to choose interactively. If multiple
 * transitions can be automatically chosen, the choice is made using the automatic input mode.
 * </p>
 *
 * <p>
 * There are several types of automatic choices, each of which enables the semi-automatic mode:
 * <ul>
 * <li>Automatically choose event transitions for events whose names match one of the given filter patterns. The
 * absolute names of events are used, without escaping.</li>
 * <li>Automatically choose event transitions for urgent events, if "urgent" is one of the filter patterns.</li>
 * <li>Automatically choose time transitions, if "time" is one of the filter patterns.</li>
 * <li>Automatically choose the delay duration for time transitions, if "timedur" is one of the filter patterns.</li>
 * <li>Automatically choose a transition if it is the only transition possible, and if "1" is one of the filter
 * patterns.</li>
 * </ul>
 * </p>
 *
 * @see InputModeOption
 */
public class InteractiveAutoChooseOption extends StringOption {
    /** The regular expression pattern to use to check automatic transition choice filters for validity. */
    private static final Pattern FILTER_PATTERN = Pattern
            .compile("[a-zA-Z_*][a-zA-Z0-9_*]*(\\.[a-zA-Z_*][a-zA-Z0-9_*]*)*");

    /** Constructor for the {@link InteractiveAutoChooseOption} class. */
    public InteractiveAutoChooseOption() {
        super("Interactive mode automatic transition choice",
                "Option to use in interactive input mode, to make it semi-automatic, by automatically choosing "
                        + "certain transitions. If no automatic choices are available, the user is still asked to "
                        + "choose interactively. Specify comma separated filters for AUTOC. The filters may be "
                        + "absolute names of events, \"urgent\", \"time\", \"timedur\", or \"1\". The \"*\" character "
                        + "can be used as wildcard in event names, and indicates zero or more characters. Use "
                        + "\"urgent\" to automatically choose transitions for all urgent events. Use \"time\" to "
                        + "automatically choose time transitions. Use \"timedur\" to automatically choose the delay "
                        + "duration for time transitions. Use \"1\" to automatically choose the one available "
                        + "transition in case it is the only one possible. If multiple transitions can be "
                        + "automatically chosen, the choice is made using the automatic input mode. [DEFAULT=\"\"]",
                null, "auto-choose", "AUTOC", "", false, true,
                "Option to use in interactive input mode, to make it semi-automatic, by automatically choosing "
                        + "certain transitions. If no automatic choices are available, the user is still asked to "
                        + "choose interactively. Specify comma separated filters for AUTOC. The filters may be "
                        + "absolute names of events, \"urgent\", \"time\", \"timedur\", or \"1\". The \"*\" character "
                        + "can be used as wildcard in event names, and indicates zero or more characters. Use "
                        + "\"urgent\" to automatically choose transitions for all urgent events. Use \"time\" to "
                        + "automatically choose time transitions. Use \"timedur\" to automatically choose the delay "
                        + "duration for time transitions. Use \"1\" to automatically choose the one available "
                        + "transition in case it is the only one possible. If multiple transitions can be "
                        + "automatically chosen, the choice is made using the automatic input mode.",
                null);
    }

    /**
     * Is the option specified? This method does not distinguish between the option not having been specified, and the
     * default explicitly being specified.
     *
     * @return {@code true} if the option is specified, {@code false} if it is not specified, or if the default is
     *     explicitly specified.
     */
    public static boolean isSpecified() {
        String value = Options.get(InteractiveAutoChooseOption.class);
        return !value.isEmpty();
    }

    /**
     * Splits the textual value of the option into separate filters, trims them, and checks them for validity.
     *
     * @return The textual value of the option split into separate filters.
     */
    public static String[] getFilters() {
        // Get textual option value.
        String value = Options.get(InteractiveAutoChooseOption.class);

        // Split value into filters.
        String[] filters = StringUtils.split(value, ",");

        // Process and check each filter.
        for (int i = 0; i < filters.length; i++) {
            // Process the filter.
            filters[i] = filters[i].trim();

            // Check the filter.
            if (filters[i].equals("1")) {
                continue;
            }
            if (FILTER_PATTERN.matcher(filters[i]).matches()) {
                continue;
            }

            // Invalid syntax.
            String msg = fmt("Interactive mode automatic transition choice filter \"%s\" has invalid syntax.",
                    filters[i]);
            throw new InvalidOptionException(msg);
        }

        // Return the filters.
        return filters;
    }

    /**
     * Should the interactive input component automatically choose a transition if it is the only transition possible?
     *
     * @param filters The interactive mode automatic transition choice filters.
     * @return {@code true} if the interactive input component should automatically choose a transition if it is the
     *     only transition possible, {@code false} otherwise.
     */
    public static boolean autoChooseSingle(String[] filters) {
        return ArrayUtils.contains(filters, "1");
    }

    /**
     * Should the interactive input component automatically choose time transitions?
     *
     * @param filters The interactive mode automatic transition choice filters.
     * @return {@code true} if the interactive input component should automatically choose time transitions,
     *     {@code false} otherwise.
     */
    public static boolean autoChooseTime(String[] filters) {
        return ArrayUtils.contains(filters, "time");
    }

    /**
     * Should the interactive input component automatically choose the delay duration for time transitions?
     *
     * @param filters The interactive mode automatic transition choice filters.
     * @return {@code true} if the interactive input component should automatically choose the delay duration for time
     *     transitions, {@code false} otherwise.
     */
    public static boolean autoChooseTimeDur(String[] filters) {
        return ArrayUtils.contains(filters, "timedur");
    }

    /**
     * Determines per event, whether it should automatically be chosen by the interactive input component. Special
     * filter "urgent" matches all urgent events.
     *
     * @param <S> The type of state objects to use.
     * @param spec The specification.
     * @param filters The interactive mode automatic transition choice filters.
     * @return Per event, {@code true} if it should automatically be chosen, {@code false} otherwise.
     */
    public static <S extends RuntimeState> boolean[] autoChooseEvents(RuntimeSpec<?> spec, String[] filters) {
        // Initialize result (no event automatically chosen).
        boolean[] rslt = new boolean[spec.events.size()];

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
            // Skip non-event filters.
            if (filter.equals("1")) {
                continue;
            }
            if (filter.equals("time")) {
                continue;
            }
            if (filter.equals("timedur")) {
                continue;
            }

            // Match events.
            boolean match = false;
            boolean effect = false;
            if (filter.equals("urgent")) {
                // Match against urgent events.
                for (int i = 0; i < spec.events.size(); i++) {
                    if (spec.urgent[i]) {
                        if (!rslt[i]) {
                            effect = true;
                        }
                        rslt[i] = true;
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
                        if (!rslt[i]) {
                            effect = true;
                        }
                        rslt[i] = true;
                        match = true;
                    }
                }
            }

            // Warn about filters that have no effect.
            if (!match) {
                String msg = fmt("Interactive mode automatic transition choice filter \"%s\" does not match any "
                        + "of the events in the specification.", filter);
                if (filter.equals("urgent")) {
                    msg += " None of the events of the specification are simulated as urgent.";
                }
                warn(msg);
            } else if (!effect) {
                String msg = fmt("Interactive mode automatic transition choice filter \"%s\" does not have any "
                        + "effect. The matched events were already automatically chosen due to an earlier filter.",
                        filter);
                warn(msg);
            }
        }

        // Return the information, per event.
        return rslt;
    }
}
