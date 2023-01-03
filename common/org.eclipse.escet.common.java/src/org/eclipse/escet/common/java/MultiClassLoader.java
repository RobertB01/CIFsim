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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Class loader that redirects to multiple other class loaders. Tries them one at a time until the first match, or uses
 * them one at a time and merges the results.
 */
public class MultiClassLoader extends ClassLoader {
    /** The class loaders to use. */
    private final List<ClassLoader> loaders;

    /**
     * Constructor for the {@link MultiClassLoader} class.
     *
     * @param loaders The class loaders to use.
     */
    public MultiClassLoader(List<ClassLoader> loaders) {
        this.loaders = loaders;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        for (ClassLoader loader: loaders) {
            try {
                // Can't pass the 'resolve' boolean along, as that overload is
                // protected. Best we can do, is just ignore it.
                return loader.loadClass(name);
            } catch (ClassNotFoundException ex) {
                // Ignore. Just try next one.
            }
        }

        throw new ClassNotFoundException();
    }

    @Override
    public URL getResource(String name) {
        for (ClassLoader loader: loaders) {
            URL resource = loader.getResource(name);
            if (resource != null) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> urls = list();
        for (ClassLoader loader: loaders) {
            Enumeration<URL> loaderUrls = loader.getResources(name);
            urls.addAll(Collections.list(loaderUrls));
        }
        return Collections.enumeration(urls);
    }
}
