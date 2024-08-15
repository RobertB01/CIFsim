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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;

/** Edge workset algorithm option. */
public class EdgeWorksetAlgoOption extends BooleanOption {
    /** Message to indicate the option is unsupported. */
    private static final String UNSUPPORTED_MESSAGE = "This option is no longer supported. "
            + "It will be removed in a future version of the tool. "
            + "Use the 'Exploration strategy for symbolic reachability computations' option instead.";

    /** Constructor for the {@link EdgeWorksetAlgoOption} class. */
    public EdgeWorksetAlgoOption() {
        super(
                // name
                "Edge workset algorithm",

                // description
                "Whether to use the edge workset algorithm. " + UNSUPPORTED_MESSAGE,

                // cmdShort
                null,

                // cmdLong
                "edge-workset",

                // cmdValue
                "BOOL",

                // defaultValue
                false,

                // showInDialog
                false,

                // optDialogDescr
                null,

                // optDialogLabelText
                null);
    }
}
