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

package org.eclipse.escet.common.app.framework.javacompiler;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.apache.commons.io.IOUtils;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;

/** URL stream handler for {@link InMemoryJarClassLoader}. */
public class InMemoryJarClassLoaderUrlStreamHandler extends URLStreamHandler {
    /** The absolute or relative path to the JAR file. */
    protected final String jarPath;

    /** Mapping of JAR entry paths to their content. */
    protected final Map<String, byte[]> entries = new HashMap<>();

    /** JAR file manifest. May be {@code null} if JAR file has no manifest. */
    protected final Manifest manifest;

    /**
     * Constructor for the {@link InMemoryJarClassLoaderUrlStreamHandler} class.
     *
     * @param jarPath The absolute or relative path to the JAR file.
     */
    public InMemoryJarClassLoaderUrlStreamHandler(String jarPath) {
        // Store JAR path.
        this.jarPath = jarPath;

        // Read JAR file into memory.
        String absJarPath = Paths.resolve(jarPath);
        try (InputStream fis = new BufferedInputStream(new FileInputStream(absJarPath));
             JarInputStream jis = new JarInputStream(fis))
        {
            for (JarEntry entry = jis.getNextJarEntry(); entry != null; entry = jis.getNextJarEntry()) {
                byte[] content = IOUtils.toByteArray(jis);
                entries.put("/" + entry.getName(), content);
            }
            manifest = jis.getManifest();
        } catch (IOException e) {
            String msg = fmt("Could not open or read JAR file \"%s\".", jarPath);
            throw new InputOutputException(msg, e);
        }
    }

    /**
     * Returns the JAR file manifest, or {@code null} if JAR file has no manifest.
     *
     * @return JAR file manifest, or {@code null}.
     */
    public Manifest getManifest() {
        return manifest;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        // Look up the entry.
        String path = u.getFile().toString();
        byte[] content = entries.get(path);
        if (content == null) {
            throw new FileNotFoundException(path);
        }

        // Return connection to provide the input stream for the content of the JAR entry.
        return new URLConnection(u) {
            @Override
            public void connect() throws IOException {
                // In-memory, so no need to connect.
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(content);
            }
        };
    }
}
