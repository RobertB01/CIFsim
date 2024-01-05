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

package org.eclipse.escet.common.app.framework.javacompiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.tools.SimpleJavaFileObject;

/** Run-time Java compiler file object that uses in-memory data storage to store generated Java class files. */
public class JavaClassFileObject extends SimpleJavaFileObject {
    /** The generated byte code. */
    private ByteArrayOutputStream byteCode = new ByteArrayOutputStream();

    /** The absolute Java class name. */
    protected final String absClassName;

    /**
     * Constructor for the {@link JavaClassFileObject} class.
     *
     * @param absClassName The absolute class name.
     */
    public JavaClassFileObject(String absClassName) {
        super(RuntimeJavaCompiler.createMemoryURI(absClassName, Kind.CLASS), Kind.CLASS);
        absClassName = RuntimeJavaFileManager.normalizeName(absClassName);
        this.absClassName = absClassName;
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("JavaClassFileObject.constructor: name=" + absClassName);
        }
    }

    @Override
    public String getName() {
        return absClassName.replace(".", "/") + ".class";
    }

    @Override
    public InputStream openInputStream() {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("JavaClassFileObject.openInputStream");
        }
        return new ByteArrayInputStream(getByteCodeArray());
    }

    @Override
    public OutputStream openOutputStream() {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("JavaClassFileObject.openOutputStream");
        }
        return byteCode;
    }

    /**
     * Returns the generated byte code.
     *
     * @return The generated byte code.
     */
    public OutputStream getByteCode() {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("JavaClassFileObject.getByteCode");
        }
        return byteCode;
    }

    /**
     * Returns the generated byte code.
     *
     * @return The generated byte code.
     */
    public byte[] getByteCodeArray() {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("JavaClassFileObject.getByteCodeArray");
        }
        return byteCode.toByteArray();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JavaClassFileObject)) {
            return false;
        }
        JavaClassFileObject other = (JavaClassFileObject)obj;
        return this.absClassName.equals(other.absClassName);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ absClassName.hashCode();
    }
}
