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

/** Enable bounded response checking option. */
public class EnableBoundedResponseChecking extends BooleanOption {
    /** Constructor for the {@link EnableBoundedResponseChecking} class. */
    public EnableBoundedResponseChecking() {
        super(// name.
                "Enable bounded response checking",

                // description.
                "Whether to perform bounded response checking (BOOL=yes) or not (BOOL=no). [DEFAULT=yes]",

                // cmdShort.
                null,

                // cmdLong.
                "bounded-response-check",

                // cmdValue.
                "BOOL",

                // defaultValue.
                true,

                // showInDialog.
                true,

                // optDialogDescr.
                "Whether to perform bounded response checking.",

                // optDialogCheckboxText.
                "Enable bounded response checking");
    }

    /**
     * Should bounded response be checked?
     *
     * @return {@code true} if bounded response should be checked, else {@code false}.
     */
    public static boolean checkBoundedResponse() {
        return Options.get(EnableBoundedResponseChecking.class);
    }
}