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

/** Enable finite response checking option. */
public class EnableFiniteResponseChecking extends BooleanOption {
    /** Constructor for the {@link EnableFiniteResponseChecking} class. */
    public EnableFiniteResponseChecking() {
        super(// name.
                "Enable finite response checking",

                // description.
                "Whether to perform finite response checking (BOOL=yes) or not (BOOL=no). [DEFAULT=yes]",

                // cmdShort.
                null,

                // cmdLong.
                "finite-response-check",

                // cmdValue.
                "BOOL",

                // defaultValue.
                true,

                // showInDialog.
                true,

                // optDialogDescr.
                "Whether to perform finite response checking.",

                // optDialogCheckboxText.
                "Enable finite response checking");
    }

    /**
     * Should finite response be checked?
     *
     * @return {@code true} if finite response should be checked, else {@code false}.
     */
    public static boolean checkFiniteResponse() {
        return Options.get(EnableFiniteResponseChecking.class);
    }
}
