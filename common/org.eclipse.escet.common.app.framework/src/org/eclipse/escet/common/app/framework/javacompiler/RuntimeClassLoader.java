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

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.tools.JavaFileObject.Kind;

import org.eclipse.escet.common.java.Assert;

/** Class loader used by the {@link RuntimeJavaCompiler}. */
@SuppressWarnings("restriction")
public class RuntimeClassLoader extends ClassLoader {
    /**
     * Absolute class name to generated class file object mapping. A thread safe map is used, as the map may be accessed
     * on one thread, while another compiler thread is using {@link #add} to add a new entry.
     */
    public final Map<String, JavaClassFileObject> generatedClasses = new ConcurrentHashMap<>();

    /**
     * Constructor for the {@link RuntimeClassLoader} class.
     *
     * @param parent Parent class loader, used to resolve external dependencies during run-time compilation. May be
     *     {@code null} if not available, to use {@link ClassLoader#getSystemClassLoader}.
     */
    public RuntimeClassLoader(ClassLoader parent) {
        super((parent == null) ? ClassLoader.getSystemClassLoader() : parent);
    }

    /**
     * Add a generated class.
     *
     * @param absClassName The absolute class name of the class to add.
     * @param file The file objects that represents the class file to add.
     */
    public void add(String absClassName, JavaClassFileObject file) {
        absClassName = RuntimeJavaFileManager.normalizeName(absClassName);
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeClassLoader.add: className=" + absClassName + " file=" + file);
        }
        generatedClasses.put(absClassName, file);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Assert.check(!name.contains("/"));
        Assert.check(!name.contains("\\"));

        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeClassLoader.findClass: name=" + name);
        }

        // See if we can find it in our own classes.
        JavaClassFileObject file = generatedClasses.get(name);
        if (file != null) {
            if (RuntimeJavaCompiler.DEBUG) {
                System.out.println("RuntimeClassLoader.findClass: define...");
            }
            byte[] byteCode = file.getByteCodeArray();
            return defineClass(name, byteCode, 0, byteCode.length);
        }

        // Defer request.
        try {
            if (RuntimeJavaCompiler.DEBUG) {
                System.out.println("RuntimeClassLoader.findClass: defer...");
            }
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            // Workaround for change in JDK6. See:
            // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6434149
            return Class.forName(name);
        }
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeClassLoader.loadClass: name=" + name + " resolve=" + resolve);
        }
        return super.loadClass(name, resolve);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeClassLoader.getResourceAsStream: name=" + name);
        }

        // Special case for our generated classes.
        if (name.endsWith(Kind.CLASS.extension)) {
            // Get class name from resource name.
            int extLength = Kind.CLASS.extension.length();
            String className = name.substring(0, name.length() - extLength).replace('/', '.');

            if (RuntimeJavaCompiler.DEBUG) {
                System.out.println("RuntimeClassLoader.getResourceAsStream: name=" + name + " className=" + className);
            }

            // Return generated class, if found.
            JavaClassFileObject file = generatedClasses.get(className);
            if (file != null) {
                if (RuntimeJavaCompiler.DEBUG) {
                    System.out.println("RuntimeClassLoader.getResourceAsStream: name=" + name + " className="
                            + className + "opening output class file:" + file);
                }
                return file.openInputStream();
            }
        }

        // Defer request.
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println(
                    "RuntimeClassLoader.getResourceAsStream: name=" + name + " defer to parent: " + getParent());
        }
        return getParent().getResourceAsStream(name);
    }
}
