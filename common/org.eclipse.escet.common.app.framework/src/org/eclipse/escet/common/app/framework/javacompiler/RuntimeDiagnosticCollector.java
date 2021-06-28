//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;

import org.eclipse.escet.common.java.Assert;

/**
 * Diagnostics collector.
 *
 * @param <T> The type of diagnostics.
 */
public class RuntimeDiagnosticCollector<T> implements DiagnosticListener<T> {
    /** Collected diagnostics. */
    private final List<Diagnostic<? extends T>> diagnostics = Collections
            .synchronizedList(new ArrayList<Diagnostic<? extends T>>());

    @Override
    public void report(Diagnostic<? extends T> diagnostic) {
        Assert.notNull(diagnostic);
        diagnostics.add(diagnostic);
    }

    /**
     * Returns the collected diagnostics.
     *
     * @return The collected diagnostics.
     */
    public List<Diagnostic<? extends T>> getDiagnostics() {
        return new ArrayList<>(diagnostics);
    }
}
