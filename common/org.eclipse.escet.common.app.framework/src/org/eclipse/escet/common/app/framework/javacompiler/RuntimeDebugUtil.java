//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.javacompiler;

/** Runtime compiler debug utility. */
public class RuntimeDebugUtil {
    /** Constructor for the {@link RuntimeDebugUtil} class. */
    private RuntimeDebugUtil() {
        // Static class.
    }

    /** Print stack trace to stdout, for debugging. */
    public static void printStackTrace() {
        int[] i = {0};
        StackWalker.getInstance().forEach(f -> {
            i[0]++;
            System.out.format("(%d) %s\n", i[0], f.toStackTraceElement().toString());
        });
    }
}
