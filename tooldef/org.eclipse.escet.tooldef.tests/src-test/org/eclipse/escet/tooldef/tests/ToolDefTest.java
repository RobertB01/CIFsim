//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.tests;

import org.eclipse.escet.tooldef.interpreter.ToolDefBasedPluginUnitTest;
import org.junit.jupiter.api.Test;

/** ToolDef integration and regression tests. */
public class ToolDefTest extends ToolDefBasedPluginUnitTest {
    /** Type checker tests. */
    @Test
    public void testTypeChecker() {
        test("tests/test_tchecker.tooldef");
    }

    /** Interpreter tests. */
    @Test
    public void testInterpreter() {
        test("tests/test_interpreter.tooldef");
    }
}
