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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** External user-defined functions asynchronous execution option. */
public class ExtFuncAsyncOption extends BooleanOption {
    /** Constructor for the {@link ExtFuncAsyncOption} class. */
    public ExtFuncAsyncOption() {
        super("External functions asynchronous execution",
                "Whether to execute external user-defined functions asynchronously (BOOL=yes), or synchronously "
                        + "(BOOL=no). [DEFAULT=yes]",
                null, "extfunc-async", "BOOL", true, true,
                "Should external user-defined functions be executed asynchronously instead of synchronously?",
                "Execute external user-defined functions asynchronously");
    }

    /**
     * Should external user-defined functions be executed asynchronously instead of synchronously?
     *
     * @return {@code true} if they should be executed asynchronously, {@code false} if they should be executed
     *     synchronously.
     */
    public static boolean execAsync() {
        return Options.get(ExtFuncAsyncOption.class);
    }
}
