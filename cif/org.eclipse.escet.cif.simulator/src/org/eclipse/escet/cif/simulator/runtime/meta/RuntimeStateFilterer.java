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

package org.eclipse.escet.cif.simulator.runtime.meta;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;

/** Provides name-based filtering of runtime state object meta data. */
public class RuntimeStateFilterer {
    /** The regular expression pattern to use to check a single state object filter for validity. */
    private static final Pattern FILTER_PATTERN = Pattern
            .compile("\\-?[a-zA-Z_*][a-zA-Z0-9_*]*'?(\\.[a-zA-Z_*][a-zA-Z0-9_*]*'?)*");

    /** Constructor for the {@link RuntimeStateFilterer} class. */
    private RuntimeStateFilterer() {
        // Static class.
    }

    /**
     * Filters state objects, based on their absolute names. If no filters are given, no state objects are returned. The
     * order of the given state objects is preserved for the filtered result.
     *
     * <p>
     * This method {@link #warn warns} about filters that don't match any of the given state objects, or have no effect.
     * It also warns about the entire filtering not matching any variables.
     * </p>
     *
     * @param metas The state object meta data of the state objects to filter.
     * @param filtersTxt The text representing the filters.
     * @param errorMsgPrefix The prefix to use for error messages. Indicates the kind of filters used. For instance:
     *     {@code "Some visualizer"}.
     * @param errorMsgAction The text to use for error messages, to indicate the action that will be performed on the
     *     data. For instance: {@code "shown"} or {@code "included"}.
     * @return The state object meta data of the state objects resulting from filtering.
     * @throws InvalidOptionException If one of the filters has invalid syntax.
     **/
    public static List<RuntimeStateObjectMeta> filter(List<RuntimeStateObjectMeta> metas, String filtersTxt,
            String errorMsgPrefix, String errorMsgAction)
    {
        // Split text into individual filters.
        String[] filters = StringUtils.split(filtersTxt, ",");

        // Process and check each filter.
        for (int i = 0; i < filters.length; i++) {
            // Trim the filter.
            filters[i] = filters[i].trim();

            // Check the filter.
            if (FILTER_PATTERN.matcher(filters[i]).matches()) {
                continue;
            }

            // Invalid syntax.
            String msg = fmt("%s filter \"%s\" has invalid syntax.", errorMsgPrefix, filters[i]);
            throw new InvalidOptionException(msg);
        }

        // Optimize for no filters.
        if (filters.length == 0) {
            String msg = fmt("%s filter is empty.", errorMsgPrefix);
            throw new InvalidOptionException(msg);
        }

        // Initialize inclusion status.
        boolean[] inclusionStatus = new boolean[metas.size()];

        // Process all filters.
        for (String filter: filters) {
            // Check inclusion/exclusion.
            boolean inclusion = !filter.startsWith("-");

            // Create regular expression from filter.
            String filterPattern = inclusion ? filter : filter.substring(1);
            String regEx = filterPattern.replace(".", "\\.");
            regEx = regEx.replace("*", ".*");
            Pattern pattern = Pattern.compile("^" + regEx + "$");

            // Process all state objects.
            boolean match = false;
            boolean effect = false;
            for (int i = 0; i < metas.size(); i++) {
                RuntimeStateObjectMeta meta = metas.get(i);
                if (pattern.matcher(meta.plainName).matches()) {
                    // Match found.
                    match = true;

                    // Change inclusion status.
                    boolean modified = (inclusionStatus[i] != inclusion);
                    if (modified) {
                        inclusionStatus[i] = inclusion;
                        effect = true;
                    }
                }
            }

            // Warn about filters that don't match any state objects, or have
            // no effect.
            if (!match) {
                String msg = fmt("%s filter \"%s\" does not match any of the state objects.", errorMsgPrefix, filter);
                warn(msg);
            } else if (!effect) {
                String msg = fmt(
                        "%s filter \"%s\" has no effect, as it did not %s any state objects %s the filtered result.",
                        errorMsgPrefix, filter, inclusion ? "add" : "remove", inclusion ? "to" : "from");
                warn(msg);
            }
        }

        // Get inclusion count.
        int cnt = 0;
        for (boolean inclusion: inclusionStatus) {
            if (inclusion) {
                cnt++;
            }
        }
        if (cnt == 0) {
            String msg = fmt("%s filters \"%s\" %smatch no state objects. Nothing will be %s.", errorMsgPrefix,
                    filtersTxt, (filters.length > 1 ? "together " : ""), errorMsgAction);
            warn(msg);
        }

        // Return filtered result.
        List<RuntimeStateObjectMeta> rslt = listc(cnt);
        for (int i = 0; i < metas.size(); i++) {
            if (inclusionStatus[i]) {
                rslt.add(metas.get(i));
            }
        }
        return rslt;
    }
}
