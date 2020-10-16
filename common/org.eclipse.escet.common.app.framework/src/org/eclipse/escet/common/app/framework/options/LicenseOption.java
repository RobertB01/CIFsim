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

package org.eclipse.escet.common.app.framework.options;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;

/** Option to print the license text of the application to the console. */
public class LicenseOption extends BooleanOption {
    /**
     * Constructor for the {@link LicenseOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public LicenseOption() {
        super("License", "Prints the license text of the application to the console.", null, "show-license", null,
                false, true, "Enable option to print the license text of the application to the console, "
                        + "and terminate the application.",
                "Print license text");
    }

    @Override
    public Boolean parseValue(String optName, String value) {
        AppEnv.getApplication().printHelpLicense(AppEnv.getStreams().out);
        throw new Application.SuccessfulExitException();
    }

    @Override
    public String[] getCmdLine(Object value) {
        return new String[] {};
    }
}
