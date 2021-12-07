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

package testpkg;

@SuppressWarnings("javadoc")
public class TestClass2 {
    private TestClass2() {
        // No body.
    }

    public static int x = 1 / 0;

    public static int staticInitErr() {
        return 5;
    }
}
