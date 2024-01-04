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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

/** Class loader for loading classes from JAR files. */
public class UncachedUrlClassLoader extends URLClassLoader {
    /**
     * Constructor for the {@link UncachedUrlClassLoader} class.
     *
     * @param urls The URLs (directories and JAR files) to use as search path.
     * @throws OpenUrlException If an URL can't be opened.
     */
    public UncachedUrlClassLoader(URL[] urls) throws OpenUrlException {
        super(urls);
        initializeConnections(urls);
    }

    /**
     * Constructor for the {@link UncachedUrlClassLoader} class.
     *
     * @param urls The URLs (directories and JAR files) to use as search path.
     * @param parent The parent class loader.
     * @throws OpenUrlException If an URL can't be opened.
     */
    public UncachedUrlClassLoader(URL[] urls, ClassLoader parent) throws OpenUrlException {
        super(urls, parent);
        initializeConnections(urls);
    }

    /**
     * Initialize the connections, disabling URL caches.
     *
     * @param urls The URLs (directories and JAR files) to use as search path.
     * @throws OpenUrlException If an URL can't be opened.
     */
    private void initializeConnections(URL[] urls) throws OpenUrlException {
        for (URL url: urls) {
            // Open connection to URL.
            URLConnection connection;
            try {
                connection = url.openConnection();
            } catch (IOException ex) {
                throw new OpenUrlException(url, ex);
            }

            // Don't cache the connection, so we get a fresh connection each
            // time, and don't end up with old stuff.
            connection.setDefaultUseCaches(false);
            connection.setUseCaches(false);
        }
    }

    /** Error opening a URL. */
    public static class OpenUrlException extends IOException {
        /** The URL that could not be opened. */
        public final URL url;

        /**
         * Constructor for the {@link OpenUrlException} class.
         *
         * @param url The URL that could not be opened.
         * @param cause The cause. Indicates why the URL could not be opened.
         */
        public OpenUrlException(URL url, IOException cause) {
            super(cause);
            this.url = url;
        }

        @Override
        public String getMessage() {
            return fmt("Could not open URL \"%s\".", url);
        }
    }
}
