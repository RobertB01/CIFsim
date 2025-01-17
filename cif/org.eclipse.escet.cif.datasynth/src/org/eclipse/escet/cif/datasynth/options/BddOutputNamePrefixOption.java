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

import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.cif.datasynth.settings.CifDataSynthesisSettingsDefaults;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** BDD output name prefix option. */
public class BddOutputNamePrefixOption extends StringOption {
    /** Constructor for the {@link BddOutputNamePrefixOption} class. */
    public BddOutputNamePrefixOption() {
        super("BDD output name prefix", "The prefix to use for BDD related names in the output. [DEFAULT=\"bdd\"]", 'p',
                "bdd-prefix", "PREFIX", CifDataSynthesisSettingsDefaults.BDD_OUTPUT_NAME_PREFIX, false, true,
                "The prefix to use for BDD related names in the output.", "Prefix:");
    }

    /**
     * Returns the prefix to use for BDD related names in the output. Is a {@link CifValidationUtils#isValidIdentifier
     * valid} CIF identifier.
     *
     * @return The prefix to use for BDD related names in the output.
     */
    public static String getPrefix() {
        return Options.get(BddOutputNamePrefixOption.class);
    }
}
