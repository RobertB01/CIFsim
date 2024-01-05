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

import javax.tools.SimpleJavaFileObject;

/**
 * Run-time Java compiler input file object that uses in-memory data storage to store Java source files. The actual
 * storage representation is left as an implementation detail for derived classes.
 */
public abstract class JavaInputFileObject extends SimpleJavaFileObject {
    /** The absolute Java class name. */
    protected final String absClassName;

    /**
     * Constructor for the {@link JavaInputFileObject} class.
     *
     * @param absClassName The absolute class name.
     */
    public JavaInputFileObject(String absClassName) {
        super(RuntimeJavaCompiler.createMemoryURI(absClassName, Kind.SOURCE), Kind.SOURCE);
        absClassName = RuntimeJavaFileManager.normalizeName(absClassName);
        this.absClassName = absClassName;
    }

    @Override
    public String getName() {
        return absClassName.replace(".", "/") + ".java";
    }

    @Override
    public CharSequence getCharContent(boolean ignore) {
        return getCharContent();
    }

    /**
     * Returns the Java source file content.
     *
     * <p>
     * Derived classes may use {@link #absClassName} to obtain the absolute class name for the class that corresponds to
     * this input file, and for which the source code should be returned.
     * </p>
     *
     * @return The Java source file content.
     */
    public abstract CharSequence getCharContent();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
