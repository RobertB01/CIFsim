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

package org.eclipse.escet.setext.tests;

import org.eclipse.escet.tooldef.interpreter.ToolDefBasedPluginUnitTest;
import org.junit.Test;

/** SeText integration and regression tests. */
public class SeTextTest extends ToolDefBasedPluginUnitTest {
    /** Invalid specifications tests. */
    @Test
    public void testInvalid() {
        test("test_models/test_invalid.tooldef");
    }

    /** Valid specifications tests. */
    @Test
    public void testValid() {
        test("test_models/test_valid.tooldef");
    }
}
