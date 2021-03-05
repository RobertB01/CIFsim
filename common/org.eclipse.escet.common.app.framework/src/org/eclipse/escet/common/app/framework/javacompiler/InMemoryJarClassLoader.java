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

package org.eclipse.escet.common.app.framework.javacompiler;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;

/** Class loader for loading classes from JAR files. Loads the classes from the JAR file and stores them in-memory. */
public class InMemoryJarClassLoader extends URLClassLoader {
    /** The absolute or relative path to the JAR file. */
    protected final String jarPath;

    /** The URL stream handler that provides the connection to the in-memory JAR file. */
    protected final InMemoryJarClassLoaderUrlStreamHandler handler;

    /**
     * Constructor for the {@link InMemoryJarClassLoader} class. Does not use a parent class loader.
     *
     * @param jarPath The absolute or relative path to the JAR file.
     */
    public InMemoryJarClassLoader(String jarPath) {
        this(jarPath, null);
    }

    /**
     * Constructor for the {@link InMemoryJarClassLoader} class. Does not use a parent class loader.
     *
     * @param jarPath The absolute or relative path to the JAR file.
     * @param parent The parent class loader.
     */
    public InMemoryJarClassLoader(String jarPath, ClassLoader parent) {
        this(jarPath, parent, new InMemoryJarClassLoaderUrlStreamHandler(jarPath));
    }

    /**
     * Constructor for the {@link InMemoryJarClassLoader} class. Does not use a parent class loader.
     *
     * @param jarPath The absolute or relative path to the JAR file.
     * @param parent The parent class loader.
     * @param handler The URL stream handler that provides the connection to the in-memory JAR file.
     */
    public InMemoryJarClassLoader(String jarPath, ClassLoader parent, InMemoryJarClassLoaderUrlStreamHandler handler) {
        super(new URL[] {createUrl(handler)}, parent);
        this.jarPath = jarPath;
        this.handler = handler;
    }

    /**
     * Creates an URL for the URL stream handler that provides the connection to the in-memory JAR file.
     *
     * @param handler The URL stream handler.
     * @return The URL.
     */
    private static URL createUrl(InMemoryJarClassLoaderUrlStreamHandler handler) {
        try {
            return new URL("jar-in-memory", null, -1, "/", handler);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extract the name of the main class from the JAR.
     *
     * @return The name of the main class.
     */
    public String getMainClassName() {
        // Get JAR manifest.
        Manifest manifest = handler.getManifest();
        if (manifest == null) {
            String msg = fmt("Could not retrieve manifest for JAR file \"%s\".", jarPath);
            throw new InputOutputException(msg);
        }

        // Get main attributes.
        Attributes attrs = manifest.getMainAttributes();
        if (attrs == null) {
            String msg = fmt("Could not retrieve main attributes for manifest of JAR file \"%s\".", jarPath);
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
