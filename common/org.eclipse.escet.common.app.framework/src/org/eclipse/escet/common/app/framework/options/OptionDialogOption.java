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

package org.eclipse.escet.common.app.framework.options;

import org.eclipse.swt.widgets.Composite;

/** Option dialog option. Controls whether the option dialog should be shown after processing the options. */
public class OptionDialogOption extends BooleanOption {
    /**
     * Constructor for the {@link OptionDialogOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public OptionDialogOption() {
        super("Option dialog",
                "Whether to show the option dialog after the command line options have been processed (BOOL=yes), "
                        + "or not (BOOL=no). [DEFAULT=no]",
                null, "option-dialog", "BOOL", false, false, null, null);
    }

    /**
     * Returns a value indicating whether the option dialog should be shown after processing the command line options.
     *
     * @return A value indicating whether the option dialog should be shown after processing the command line options.
     */
    public static boolean isEnabled() {
        return Options.get(OptionDialogOption.class);
    }

    @Override
    public OptionGroup<Boolean> createOptionGroup(Composite page) {
        throw new UnsupportedOperationException();
    }
}
