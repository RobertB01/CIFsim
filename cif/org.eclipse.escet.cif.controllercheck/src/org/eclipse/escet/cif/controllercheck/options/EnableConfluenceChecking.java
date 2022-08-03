//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

/** Enable confluence checking option. */
public class EnableConfluenceChecking extends BooleanOption {
    /** Constructor for the {@link EnableConfluenceChecking} class. */
    public EnableConfluenceChecking() {
        super(// name.
                "Enable confluence checking",

                // description.
                "Whether to perform confluence checking (BOOL=yes) or not (BOOL=no). [DEFAULT=yes]",

                // cmdShort.
                null,

                // cmdLong.
                "confluence-check",

                // cmdValue.
                "BOOL",

                // defaultValue.
                true,

                // showInDialog.
                true,

                // optDialogDescr.
                "Enable confluence checking.",

                // optDialogCheckboxText.
                "Perform confluence check.");
    }

    /**
     * Should confluence be checked?
     *
     * @return {@code true} if confluence should be checked, else {@code false}.
     */
    public static boolean checkConfluence() {
        return Options.get(EnableConfluenceChecking.class);
    }
}
