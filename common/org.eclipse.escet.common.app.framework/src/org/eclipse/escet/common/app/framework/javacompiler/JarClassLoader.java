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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;

import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;

/** Class loader for loading classes from JAR files. */
public class JarClassLoader extends URLClassLoader {
    /** The absolute local file system path of the JAR file. */
    private final String jarPath;

    /** The {@link URL} to the JAR file. */
    private final URL jarUrl;

    /** The JAR URL connection. */
    private final JarURLConnection jarUrlConnection;

    /**
     * Constructor for the {@link JarClassLoader} class.
     *
     * @param jarPath The absolute local file system path of the JAR file.
     * @param parent Parent class loader, to use to resolve external dependencies (outside of the JAR file).
     */
    public JarClassLoader(String jarPath, ClassLoader parent) {
        super(new URL[] {constructJarUrl(jarPath)}, parent);
        this.jarPath = jarPath;
        this.jarUrl = constructJarUrl(jarPath);
        this.jarUrlConnection = initializeConnection();
    }

    /**
     * Construct a URL to the given JAR file.
     *
     * @param jarPath The absolute local file system path of the JAR file.
     * @return {@link URL} that can be used to access the JAR contents.
     */
    private static URL constructJarUrl(String jarPath) {
        URI uri = Paths.createJavaURI(jarPath);
        URL url;
        try {
            url = new URL("jar", "", uri.toString() + "!/");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid JAR file path: " + jarPath, e);
        }
        return url;
    }

    /**
     * Initialize the connection, disabling URL caches.
     *
     * @return The JAR URL connection.
     */
    private JarURLConnection initializeConnection() {
        // Open connection to JAR.
        JarURLConnection jarConnection;
        try {
            jarConnection = (JarURLConnection)jarUrl.openConnection();
        } catch (IOException e) {
            String msg = fmt("Could not open URL \"%s\".", jarUrl);
            throw new InputOutputException(msg, e);
        }

        // Don't cache the JAR file, so we can load it multiple times.
        jarConnection.setDefaultUseCaches(false);
        jarConnection.setUseCaches(false);

        // Return the connection.
        return jarConnection;
    }

    /**
     * Extract the name of the main class from the JAR.
     *
     * @return The name of the main class.
     */
    public String getMainClassName() {
        // Get main attributes.
        Attributes attrs;
        try {
            attrs = jarUrlConnection.getMainAttributes();
        } catch (IOException e) {
            String msg = fmt("Could not retrieve main class name for JAR file \"%s\".", jarPath);
            throw new InputOutputException(msg, e);
        }
        if (attrs == null) {
            String msg = fmt("Could not retrieve main class name for JAR file \"%s\".", jarPath);
            throw new InputOutputException(msg);
        }

        // Get main class name.
        String mainClass = attrs.getValue(Attributes.Name.MAIN_CLASS);
        if (mainClass == null || mainClass.isEmpty()) {
            String msg = fmt("The main class name in the manifest of JAR file \"%s\" is not valid.", jarPath);
            throw new InputOutputException(msg);
        }

        // Return the successfully obtained main class name.
        return mainClass;
    }

    /**
     * Load the named class from the JAR (or its parent class loader) into memory. Converts
     * {@link ClassNotFoundException}s to {@link InputOutputException}s.
     *
     * @param <T> The type of class to return.
     * @param absClassName The absolute class name of the class to load.
     * @return The loaded class.
     * @see #loadClass
     */
    public <T> Class<? extends T> loadClassByName(String absClassName) {
        try {
            @SuppressWarnings("unchecked")
            Class<T> rslt = (Class<T>)loadClass(absClassName, true);
            return rslt;
        } catch (ClassNotFoundException e) {
            String msg = fmt("Could not find class \"%s\" in JAR file \"%s\".", absClassName, jarPath);
            throw new InputOutputException(msg, e);
        }
    }
}
