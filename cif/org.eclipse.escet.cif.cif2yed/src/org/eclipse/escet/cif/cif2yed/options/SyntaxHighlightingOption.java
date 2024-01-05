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

package org.eclipse.escet.cif.cif2yed.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to apply syntax highlighting. */
public class SyntaxHighlightingOption extends BooleanOption {
    /** Constructor for the {@link SyntaxHighlightingOption} class. */
    public SyntaxHighlightingOption() {
        super(
                // name
                "Syntax highlighting",

                // description
                "Whether to apply syntax highlighting (BOOL=yes), or use plain text (BOOL=no). [DEFAULT=yes]",

                // cmdShort
                's',

                // cmdLong
                "syntax-highlighing",

                // cmdValue
                "BOOL",

                // defaultValue
                true,

                // showInDialog
                true,

                // optDialogDescr
                "Whether to apply syntax highlighting (option enabled), or use plain text (option disabled).",

                // optDialogCheckboxText
                "Apply syntax highlighting");
    }

    /**
     * Should syntax highlighting be applied?
     *
     * @return {@code true} to apply syntax highlighting, {@code false} to use plain text.
     */
    public static boolean applyHighlighting() {
        return Options.get(SyntaxHighlightingOption.class);
    }
}
