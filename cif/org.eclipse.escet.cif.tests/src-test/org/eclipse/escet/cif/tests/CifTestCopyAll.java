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

package org.eclipse.escet.cif.tests;

import org.eclipse.escet.tooldef.interpreter.ToolDefBasedPluginUnitTest;
import org.junit.Test;

/**
 * Copies all actual output after running {@link CifTest} over the expected output. This allows to easily update the
 * expected test output in case of changes.
 */
public class CifTestCopyAll extends ToolDefBasedPluginUnitTest {
    /** Copy all. */
    @Test
    public void copyAll() {
        test("tests/copy_all.tooldef");
    }
}
