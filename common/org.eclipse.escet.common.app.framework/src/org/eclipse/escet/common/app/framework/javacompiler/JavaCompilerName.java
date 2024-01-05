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

package org.eclipse.escet.common.app.framework.javacompiler;

/** Supported Java compiler. See {@link JavaCompilerOption}. */
public enum JavaCompilerName {
    /** Java compiler from the Java Development Kit (requires a JDK, a JRE is not sufficient). */
    JDK,

    /** Eclipse Compiler for Java (ecj), part of the Eclipse Java Development Tools (JDT). */
    ECLIPSE;
}
