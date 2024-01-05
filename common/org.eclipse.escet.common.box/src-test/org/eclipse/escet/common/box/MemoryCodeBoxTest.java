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

package org.eclipse.escet.common.box;

/** Unit tests for the {@link MemoryCodeBox} class. */
public class MemoryCodeBoxTest extends CodeBoxTest {
    @Override
    protected CodeBox createCodeBox() {
        return new MemoryCodeBox();
    }

    @Override
    protected CodeBox createCodeBox(int indentAmount) {
        return new MemoryCodeBox(indentAmount);
    }
}
