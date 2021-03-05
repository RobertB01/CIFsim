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

package org.eclipse.escet.tooldef.tests;

import java.util.Deque;
import java.util.List;

import org.eclipse.escet.tooldef.runtime.ToolDefTuple;
import org.eclipse.escet.tooldef.runtime.ToolDefTuplePair;

/** Test class used for type checker tests. */
@SuppressWarnings("javadoc")
public class TypeCheckerTestClass {
    private TypeCheckerTestClass() {
        // Static class.
    }

    // Unsupported type parameter with bound/extends.
    public static <T, U extends T> T testTypeParamExtends(U u) {
        return u;
    }

    // Unsupported return type with wildcard.
    public static List<?> testWildcardReturn() {
        return null;
    }

    // Unsupported generic type java.
    public static Deque<String> testUnsupportedGenericType() {
        return null;
    }

    // Unsupported non-generic type java.
    public static float testUnsupportedNonGenericType() {
        return 0.0f;
    }

    // Unsupported tuple base class.
    public static ToolDefTuple testUnsupportedTupleBaseClass() {
        return new ToolDefTuplePair<>(1, 2);
    }

    // Unsupported generic array type java.
    public static List<Integer>[] testUnsupportedGenericArrayType() {
        return null;
    }

    // Unsupported non-generic array type java.
    public static int[] testUnsupportedNonGenericArrayType() {
        return null;
    }

    // Unsupported non-generic use of generic type.
    @SuppressWarnings("rawtypes")
    public static List testUnsupportedNonGenericUseGenericType() {
        return null;
    }
}
