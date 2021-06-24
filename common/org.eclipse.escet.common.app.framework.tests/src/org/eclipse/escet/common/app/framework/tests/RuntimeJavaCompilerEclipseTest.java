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

package org.eclipse.escet.common.app.framework.tests;

/** {@link RuntimeJavaCompilerTest} with "eclipse" Java compiler. */
public class RuntimeJavaCompilerEclipseTest extends RuntimeJavaCompilerTest {
    @Override
    protected String getCompilerName() {
        return "eclipse";
    }
}
