//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Enable non-blocking under control checking option. */
public class EnableNonBlockingUnderControlChecking extends BooleanOption {
    /** Constructor for the {@link EnableNonBlockingUnderControlChecking} class. */
    public EnableNonBlockingUnderControlChecking() {
        super(// name.
                "Enable non-blocking under control checking",

                // description.
                "Whether to perform non-blocking under control checking (BOOL=yes) or not (BOOL=no). [DEFAULT=yes]",

                // cmdShort.
                null,

                // cmdLong.
                "non-blocking-under-control-check",

                // cmdValue.
                "BOOL",

                // defaultValue.
                true,

                // showInDialog.
                true,

                // optDialogDescr.
                "Whether to perform non-blocking under control checking.",

                // optDialogCheckboxText.
                "Enable non-blocking under control checking");
    }

    /**
     * Should non-blocking under control be checked?
     *
     * @return {@code true} if non-blocking under control should be checked, else {@code false}.
     */
    public static boolean checkNonBlockingUnderControl() {
        return Options.get(EnableNonBlockingUnderControlChecking.class);
    }
}
