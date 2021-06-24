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
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/** Run-time Java compiler file object that uses an OSGi loader to obtain compiled Java classes. */
public class OsgiClassFileObject extends SimpleJavaFileObject {
    /** The absolute name of the Java class. */
    protected final String absClassName;

    /** The OSGi URI to the Java class. */
    protected final URI osgiUri;

    /** The OSGi class loader that to use to obtain the class byte code. */
    private final ClassLoader osgiLoader;

    /**
     * Constructor for the {@link OsgiClassFileObject} class.
     *
     * @param absClassName The absolute name of the Java class.
     * @param uri The OSGi URI to the Java class.
     * @param osgiLoader The OSGi class loader that to use to obtain the class byte code.
     */
    public OsgiClassFileObject(String absClassName, URI uri, ClassLoader osgiLoader) {
        super(RuntimeJavaCompiler.createURI(absClassName), Kind.CLASS);
        absClassName = RuntimeJavaFileManager.normalizeName(absClassName);
        this.absClassName = absClassName;
        this.osgiUri = uri;
        this.osgiLoader = osgiLoader;
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("OsgiClassFileObject.constructor: name=" + absClassName);
        }
    }

    @Override
    public String getName() {
        return absClassName.replace(".", "/") + ".class";
    }

    @Override
    public InputStream openInputStream() {
        return osgiLoader.getResourceAsStream(getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OsgiClassFileObject)) {
            return false;
        }
        OsgiClassFileObject other = (OsgiClassFileObject)obj;
        return this.absClassName.equals(other.absClassName) && this.osgiUri.equals(other.osgiUri);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ absClassName.hashCode() ^ this.osgiUri.hashCode();
    }
}
