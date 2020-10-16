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

import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/** Exception used by the {@link RuntimeJavaCompiler} to indicate compilation failure. */
public class RuntimeJavaCompilerException extends Exception {
    /**
     * The run-time Java compiler used to compile the sources. Can be used to obtain the compiler output and
     * diagnostics.
     */
    public final RuntimeJavaCompiler compiler;

    /**
     * Mapping from absolute class names to the corresponding input file object that was used to obtain the source code
     * for that class.
     */
    public final Map<String, JavaInputFileObject> sources;

    /**
     * Constructor for the {@link RuntimeJavaCompilerException} class.
     *
     * @param compiler The run-time Java compiler used to compile the sources. Can be used to obtain the compiler output
     *     and diagnostics.
     * @param sources Mapping from absolute class names to the corresponding input file object that was used to obtain
     *     the source code for that class.
     */
    public RuntimeJavaCompilerException(RuntimeJavaCompiler compiler, Map<String, JavaInputFileObject> sources) {
        this.compiler = compiler;
        this.sources = sources;
    }

    @Override
    public String getMessage() {
        StringBuilder b = new StringBuilder();
        b.append(fmt("Run-time Java code compilation failed (with %d source files):\n", sources.size()));
        String output = compiler.getOutput().toString();
        b.append(fmt("with output (%d):\n", output.length()));
        if (output.length() > 0) {
            for (String line: output.split("\\r?\\n")) {
                b.append("    " + line + "\n");
            }
        }
        List<Diagnostic<? extends JavaFileObject>> diags = compiler.getDiagnostics().getDiagnostics();
        b.append(fmt("with diagnostics (%d):\n", diags.size()));
        for (Diagnostic<? extends JavaFileObject> diag: diags) {
            b.append("    " + diagnosticToString(diag) + "\n");
        }
        return b.toString();
    }

    /**
     * Returns a textual representation of a compiler diagnostic message.
     *
     * @param diagnostic The diagnostic for which to return textual representation.
     * @return A textual representation of a compiler diagnostic message.
     */
    public static String diagnosticToString(Diagnostic<?> diagnostic) {
        return "line " + diagnostic.getLineNumber() + ", column " + diagnostic.getColumnNumber() + ": "
                + diagnostic.getKind() + ": " + diagnostic.getMessage(null);
    }
}
