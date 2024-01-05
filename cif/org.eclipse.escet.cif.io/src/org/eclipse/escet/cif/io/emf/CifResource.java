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

package org.eclipse.escet.cif.io.emf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.parser.CifParser;
import org.eclipse.escet.cif.parser.ast.ASpecification;
import org.eclipse.escet.cif.prettyprinter.CifPrettyPrinter;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.OutputStreamAppStream;
import org.eclipse.escet.common.box.StreamCodeBox;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.typechecker.SemanticProblem;
import org.eclipse.escet.setext.runtime.SyntaxWarning;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;

/** CIF resource, providing EMF loading/saving from/to CIF ASCII files. */
public class CifResource extends ResourceImpl {
    /** Constructor for the {@link CifResource} class, without {@link URI}. */
    public CifResource() {
        super();
    }

    /**
     * Constructor for the {@link CifResource} class.
     *
     * @param uri The URI to be associated with the resource.
     */
    public CifResource(URI uri) {
        super(uri);
    }

    @Override
    protected void doLoad(InputStream stream, Map<?, ?> options) throws IOException {
        // Get location, if possible.
        String location = null;
        if (uri != null) {
            String path = uri.toFileString();
            if (path != null) {
                File file = new File(path);
                location = file.getAbsolutePath();
            } else {
                location = uri.toString();
                if (PlatformUriUtils.isPlatformUri(location)) {
                    location = PlatformUriUtils.normalizePlatformUri(location);
                }
            }
        }

        // Parse the input.
        CifParser parser = new CifParser();
        ASpecification specAst;
        try {
            specAst = parser.parseStream(stream, location);
        } catch (InputOutputException ex) {
            throw new IOException("Failed to read input.", ex);
        } catch (SyntaxException ex) {
            // Report syntax error.
            Diagnostic diagnostic = new CifSyntaxErrorDiagnostic(ex);
            if (errors == null) {
                getErrors();
            }
            errors.add(diagnostic);
            return;
        }

        // Report syntax warnings.
        for (SyntaxWarning warning: parser.getWarnings()) {
            Diagnostic diagnostic = new CifSyntaxWarningDiagnostic(warning);
            if (warnings == null) {
                getWarnings();
            }
            warnings.add(diagnostic);
        }

        // Type check the input.
        CifTypeChecker tchecker = new CifTypeChecker();
        if (location != null) {
            tchecker.setSourceFilePath(location);
        }
        Specification spec = tchecker.typeCheck(specAst);

        // Report semantic problems.
        for (SemanticProblem problem: tchecker.getProblems()) {
            Diagnostic diagnostic = new CifSemanticDiagnostic(problem);
            switch (problem.severity) {
                case ERROR:
                    if (errors == null) {
                        getErrors();
                    }
                    errors.add(diagnostic);
                    break;

                case WARNING:
                    if (warnings == null) {
                        getWarnings();
                    }
                    warnings.add(diagnostic);
                    break;
            }
        }

        // Add specification to the resource, if loading succeeded.
        if (spec != null) {
            getContents().add(spec);
        }
    }

    @Override
    protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
        // Get stream.
        AppStream appStream = new OutputStreamAppStream(outputStream);
        StreamCodeBox streamBox = new StreamCodeBox(appStream);

        // Pretty print.
        Specification spec = (Specification)getContents().get(0);
        CifPrettyPrinter.boxSpec(spec, streamBox);
    }

    /** EMF diagnostic wrapping a CIF syntax error. */
    public class CifSyntaxErrorDiagnostic implements Diagnostic {
        /** The CIF parser error. */
        public final SyntaxException error;

        /**
         * Constructor for the {@link CifSyntaxErrorDiagnostic}.
         *
         * @param error The syntax error.
         */
        public CifSyntaxErrorDiagnostic(SyntaxException error) {
            this.error = error;
        }

        @Override
        public String getMessage() {
            return error.toString();
        }

        @Override
        public String getLocation() {
            return (uri == null) ? null : uri.toString();
        }

        @Override
        public int getLine() {
            return error.getPosition().startLine;
        }

        @Override
        public int getColumn() {
            return error.getPosition().endLine;
        }

        @Override
        public String toString() {
            return error.toString();
        }
    }

    /** EMF diagnostic wrapping a CIF syntax warning. */
    public class CifSyntaxWarningDiagnostic implements Diagnostic {
        /** The CIF parser warning. */
        public final SyntaxWarning warning;

        /**
         * Constructor for the {@link CifSyntaxWarningDiagnostic}.
         *
         * @param warning The warning.
         */
        public CifSyntaxWarningDiagnostic(SyntaxWarning warning) {
            this.warning = warning;
        }

        @Override
        public String getMessage() {
            return warning.toString();
        }

        @Override
        public String getLocation() {
            return (uri == null) ? null : uri.toString();
        }

        @Override
        public int getLine() {
            return warning.position.startLine;
        }

        @Override
        public int getColumn() {
            return warning.position.endLine;
        }

        @Override
        public String toString() {
            return warning.toString();
        }
    }

    /** EMF diagnostic wrapping a CIF semantic problem. */
    public class CifSemanticDiagnostic implements Diagnostic {
        /** The CIF type checker problem. */
        public final SemanticProblem problem;

        /**
         * Constructor for the {@link CifSemanticDiagnostic}.
         *
         * @param problem The problem.
         */
        public CifSemanticDiagnostic(SemanticProblem problem) {
            this.problem = problem;
        }

        @Override
        public String getMessage() {
            return problem.toString();
        }

        @Override
        public String getLocation() {
            return (uri == null) ? null : uri.toString();
        }

        @Override
        public int getLine() {
            return problem.position.startLine;
        }

        @Override
        public int getColumn() {
            return problem.position.endLine;
        }

        @Override
        public String toString() {
            return problem.toString();
        }
    }
}
