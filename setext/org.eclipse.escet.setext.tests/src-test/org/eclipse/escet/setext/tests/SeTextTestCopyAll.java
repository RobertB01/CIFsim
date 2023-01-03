//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

/**
 * Copies all actual output after running {@link SeTextTest} over the expected output. This allows to easily update the
 * expected test output in case of changes.
 */
public class SeTextTestCopyAll extends ToolDefBasedPluginUnitTest {
    /** Copy all. */
    @Test
    public void copyAll() {
        test("test_models/copy_all.tooldef");
    }
}
