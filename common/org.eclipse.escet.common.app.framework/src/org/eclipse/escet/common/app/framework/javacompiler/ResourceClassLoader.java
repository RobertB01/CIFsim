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

package org.eclipse.escet.common.app.framework.javacompiler;

import static org.eclipse.escet.common.java.Maps.map;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Class loader that allows loading of in-memory resources. Is particularly useful for wrapping a
 * {@link RuntimeClassLoader} returned by the {@link RuntimeJavaCompiler}, to allow loading of resources that are stored
 * in-memory, just like the runtime in-memory compiled Java classes.
 */
public class ResourceClassLoader extends ClassLoader {
    /** Mapping from absolute resource paths to resource contents. */
    public Map<String, byte[]> resources = map();

    /** Constructor for the {@link ResourceClassLoader}. */
    public ResourceClassLoader() {
        // Use system class loader as parent class loader.
    }

    /**
     * Constructor for the {@link ResourceClassLoader}.
     *
     * @param parent The parent class loader.
     */
    public ResourceClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        // Handle local resources.
        byte[] resourceData = resources.get(name);
        if (resourceData != null) {
            return new ByteArrayInputStream(resourceData);
        }

        // Defer to parent class loader.
        return getParent().getResourceAsStream(name);
    }
}
