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

package org.eclipse.escet.chi.tests;

import org.eclipse.escet.tooldef.interpreter.ToolDefBasedPluginUnitTest;
import org.junit.Test;

/** Chi integration and regression tests. */
public class ChiTest extends ToolDefBasedPluginUnitTest {
    /** Chi tests. */
    @Test
    public void test() {
        test("tests/tests.tooldef");
    }
}
