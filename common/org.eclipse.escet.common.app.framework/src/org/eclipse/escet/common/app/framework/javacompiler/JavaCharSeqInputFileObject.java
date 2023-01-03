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

/** Run-time Java compiler input file object that uses an in-memory character sequence to store Java source files. */
public class JavaCharSeqInputFileObject extends JavaInputFileObject {
    /** The Java source code (source file contents). */
    private final CharSequence code;

    /**
     * Constructor for the {@link JavaCharSeqInputFileObject} class.
     *
     * @param absClassName The absolute class name.
     * @param code The Java source code (source file contents).
     */
    public JavaCharSeqInputFileObject(String absClassName, CharSequence code) {
        super(absClassName);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JavaCharSeqInputFileObject)) {
            return false;
        }
        JavaCharSeqInputFileObject other = (JavaCharSeqInputFileObject)obj;
        return this.absClassName.equals(other.absClassName);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ absClassName.hashCode();
    }
}
