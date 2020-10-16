//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("javadoc")
public class TestClass2 {
    public static int x = 1 / 0;

    public static int static_init_err() {
        return 5;
    }
}
