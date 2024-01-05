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

package org.eclipse.escet.common.tests;

import org.eclipse.escet.tooldef.interpreter.ToolDefBasedPluginUnitTest;
import org.junit.jupiter.api.Test;

/** Common tools integration and regression tests. */
public class CommonTest extends ToolDefBasedPluginUnitTest {
    /** DSM clustering tests. */
    @Test
    public void testDsmClustering() {
        test("tests/test_dsmclustering.tooldef");
    }
}
