//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2plc.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Simplify values option. */
public class SimplifyValuesOption extends BooleanOption {
    /** Constructor for the {@link SimplifyValuesOption} class. */
    public SimplifyValuesOption() {
        super("Simplify values", "Whether the simplify values (BOOL=yes), or keep them as is (BOOL=no). [DEFAULT=yes]",
                null, "simplify-values", "BOOL", true, true, "Should values be simplified?", "Simplify values");
    }

    /**
     * Should values be simplified?
     *
     * @return {@code true} if values should be simplified, {@code false} otherwise.
     */
    public static boolean simplifyValues() {
        return Options.get(SimplifyValuesOption.class);
    }
}
