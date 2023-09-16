//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.annotations;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.common.app.framework.exceptions.InvalidModelException;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** Annotation provider for "doc" annotations. */
public class DocAnnotationProvider extends AnnotationProvider {
    /**
     * Constructor for the {@link DocAnnotationProvider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public DocAnnotationProvider(String annotationName) {
        super(annotationName);
    }

    @Override
    public final void checkAnnotation(Annotation annotation, AnnotationProblemReporter reporter) {
        // Must have exactly one argument, named 'text', of type 'string', that can be statically evaluated.
        if (annotation.getArguments().isEmpty()) {
            reporter.reportProblem(annotation, "missing mandatory \"text\" argument.", annotation.getPosition(),
                    SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
        }
        for (AnnotationArgument arg: annotation.getArguments()) {
            if (!arg.getName().equals("text")) {
                reporter.reportProblem(annotation, fmt("unsupported argument \"%s\".", arg.getName()),
                        arg.getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
            } else {
                boolean ok = true;

                // Check for argument having a boolean value. Tests reporting a second warning.
                CifType valueType = CifTypeUtils.normalizeType(arg.getValue().getType());
                if (!(valueType instanceof StringType)) {
                    reporter.reportProblem(annotation,
                            fmt("argument \"text\" must have a value of type \"string\", "
                                    + "but has a value of type \"%s\".", CifTextUtils.typeToStr(valueType)),
                            arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                    ok = false;
                }

                // Check that argument can be statically evaluated.
                if (!CifValueUtils.hasSingleValue(arg.getValue(), false, true)) {
                    reporter.reportProblem(annotation, "argument \"text\" cannot be evaluated statically.",
                            arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                    ok = false;
                }

                // Check for evaluation errors.
                if (ok) {
                    try {
                        getDoc(annotation);
                    } catch (InvalidModelException e) {
                        CifEvalException evalErr = (CifEvalException)e.getCause();
                        String evalErrMsg = evalErr.toString();
                        if (!evalErrMsg.endsWith(".")) {
                            evalErrMsg += ".";
                        }
                        reporter.reportProblem(annotation,
                                fmt("argument \"text\" cannot be evaluated statically, "
                                        + "as evaluating it results in an evaluation error: %s", evalErrMsg),
                                arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                        // Non-fatal problem.
                    }
                }
            }
        }
    }

    @Override
    public final void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Do nothing.
    }

    /**
     * Returns the documentation text of an annotated object, if any.
     *
     * @param obj The annotated object. Must be a named object.
     * @return The documentation text, or {@code null} if the object is not annotated with a documentation annotation.
     * @throws InvalidModelException If the documentation text can not be evaluated.
     */
    public static String getDoc(AnnotatedObject obj) {
        for (Annotation anno: obj.getAnnotations()) {
            if (anno.getName().equals("doc")) {
                return getDoc(anno);
            }
        }
        return null;
    }

    /**
     * Returns the documentation text of a documentation annotation.
     *
     * @param docAnno The documentation annotation.
     * @return The documentation text.
     * @throws InvalidModelException If the documentation text can not be evaluated.
     */
    public static String getDoc(Annotation docAnno) {
        try {
            Object value = CifEvalUtils.eval(docAnno.getArguments().get(0).getValue(), false);
            return (String)value;
        } catch (CifEvalException e) {
            AnnotatedObject annotatedObj = (AnnotatedObject)docAnno.eContainer();
            throw new InvalidModelException(
                    fmt("Failed to evaluate the \"text\" argument of the \"doc\" annotation of \"%s\".",
                            CifTextUtils.getAbsName(annotatedObj)),
                    e);
        }
    }
}
