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

package org.eclipse.escet.tooldef.tests;

import org.eclipse.escet.tooldef.interpreter.ToolDefBasedPluginUnitTest;
import org.junit.jupiter.api.Test;

/**
 * Copies all actual output after running {@link ToolDefTest} over the expected output. This allows to easily update the
 * expected test output in case of changes.
 */
public class ToolDefTestCopyAll extends ToolDefBasedPluginUnitTest {
    /** Copy all. */
    @Test
    public void copyAll() {
        test("tests/copy_all.tooldef");
    }
}
