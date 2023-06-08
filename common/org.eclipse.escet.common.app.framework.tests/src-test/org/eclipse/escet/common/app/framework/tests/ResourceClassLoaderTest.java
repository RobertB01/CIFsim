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

package org.eclipse.escet.common.app.framework.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.escet.common.app.framework.javacompiler.ResourceClassLoader;
import org.junit.Test;

/** Unit tests for the {@link ResourceClassLoader} class. */
@SuppressWarnings("javadoc")
public class ResourceClassLoaderTest {
    @Test
    public void testNormal() throws IOException {
        byte[] data1 = {1, 2, 5, 4};
        byte[] data2 = {9, 8, 1, 3};

        ResourceClassLoader classLoader = new ResourceClassLoader();
        classLoader.resources.put("a/data1.dat", data1);
        classLoader.resources.put("a/data2.dat", data2);

        InputStream stream1 = classLoader.getResourceAsStream("a/data1.dat");
        InputStream stream2 = classLoader.getResourceAsStream("a/data2.dat");
        assertNotNull(stream1);
        assertNotNull(stream2);

        byte[] data1b = IOUtils.toByteArray(stream1);
        byte[] data2b = IOUtils.toByteArray(stream2);

        assertArrayEquals(data1, data1b);
        assertArrayEquals(data2, data2b);
    }

    @Test
    public void testNotFound() {
        ResourceClassLoader classLoader = new ResourceClassLoader();
        InputStream s = classLoader.getResourceAsStream("does/not/exist.dat");
        assertNull(s);
    }
}
