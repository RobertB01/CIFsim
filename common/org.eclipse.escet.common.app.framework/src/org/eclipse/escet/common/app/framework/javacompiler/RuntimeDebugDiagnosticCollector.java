//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

import javax.tools.Diagnostic;

/**
 * Diagnostics collector that prints a stack trace to stdout upon adding a diagnostic to it, for debugging.
 *
 * @param <T> The type of diagnostics.
 */
public class RuntimeDebugDiagnosticCollector<T> extends RuntimeDiagnosticCollector<T> {
    @Override
    public void report(Diagnostic<? extends T> diagnostic) {
        // Print stack trace for debugging.
        System.out.println(getClass().getSimpleName());
        RuntimeDebugUtil.printStackTrace();

        // Collect diagnostic.
        super.report(diagnostic);
    }
}
