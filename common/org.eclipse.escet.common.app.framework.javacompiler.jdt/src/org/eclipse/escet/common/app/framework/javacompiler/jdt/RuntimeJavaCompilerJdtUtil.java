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

package org.eclipse.escet.common.app.framework.javacompiler.jdt;

import javax.tools.JavaCompiler;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

/**
 * Utilities for dealing with the Eclipse Compiler for Java (ecj), which is part of the Eclipse Java Development Tools
 * (JDT).
 */
@SuppressWarnings("restriction")
public class RuntimeJavaCompilerJdtUtil {
    /** Constructor for the {@link RuntimeJavaCompilerJdtUtil} class. */
    private RuntimeJavaCompilerJdtUtil() {
        // Static class.
    }

    /**
     * Creates a new instance of {@link EclipseCompiler}.
     *
     * @return A new instance of {@link EclipseCompiler}.
     */
    public static final JavaCompiler createEclipseCompiler() {
        return new EclipseCompiler();
    }
}
