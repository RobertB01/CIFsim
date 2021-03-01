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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/** Helper class with exceptions related functionality. */
public final class Exceptions {
    /** Constructor for the {@link Exceptions} class. */
    private Exceptions() {
        // Static class.
    }

    /**
     * Returns a human readable version of the exception, including its causes. Uses platform specific new line
     * characters, to allow GUI display.
     *
     * @param ex The exception for which to get the human readable version.
     * @return A human readable version of the exception, including its causes.
     */
    public static String exToStr(Throwable ex) {
        List<String> rslt = list();
        String exMsg = ex.getMessage();
        if (exMsg == null) {
            exMsg = "(" + ex.getClass().getSimpleName() + ") <no error message available>";
        }
        rslt.add("ERROR: " + exMsg);
        if (ex.getCause() != null) {
            exToStrAddCauses(ex.getCause(), rslt);
        }
        return StringUtils.join(rslt, Strings.NL);
    }

    /**
     * Adds a human readable version of the exception cause, including its sub-causes.
     *
     * @param ex The exception cause for which to add the human readable version.
     * @param rslt The result to add the human readable version to. Is modified in-place.
     */
    private static void exToStrAddCauses(Throwable ex, List<String> rslt) {
        String exMsg = ex.getMessage();
        if (exMsg == null) {
            exMsg = "(" + ex.getClass().getSimpleName() + ") <cause details not available>";
        }
        rslt.add("CAUSE: " + exMsg);
        if (ex.getCause() != null) {
            exToStrAddCauses(ex.getCause(), rslt);
        }
    }

    /**
     * Returns a human readable version of the exception, including its causes, as a single line string.
     *
     * @param ex The exception for which to get the human readable version.
     * @return A human readable version of the exception, including its causes, as a single line string.
     */
    public static String exToLine(Throwable ex) {
        String exMsg = ex.getMessage();
        if (exMsg == null) {
            exMsg = "(" + ex.getClass().getSimpleName() + ") <no error message available>";
        }
        return (ex.getCause() == null) ? exMsg : exMsg + exToLineCauses(ex.getCause());
    }

    /**
     * Returns a human readable version of the exception cause, including its sub-causes, as a single line string.
     *
     * @param ex The exception cause for which to return the human readable version.
     * @return A human readable version of the exception cause, including its sub-causes, as a single line string.
     */
    private static String exToLineCauses(Throwable ex) {
        String exMsg = ex.getMessage();
        if (exMsg == null) {
            exMsg = "(" + ex.getClass().getSimpleName() + ") <cause details not available>";
        }
        exMsg = " / Cause: " + exMsg;
        return (ex.getCause() == null) ? exMsg : exMsg + exToLineCauses(ex.getCause());
    }
}
