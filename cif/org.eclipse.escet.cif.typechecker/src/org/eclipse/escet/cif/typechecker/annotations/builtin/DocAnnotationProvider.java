//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.annotations.builtin;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifDocAnnotationUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProblemReporter;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProvider;
import org.eclipse.escet.common.java.exceptions.InvalidModelException;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/**
 * Annotation provider for "doc" annotations.
 *
 * <p>
 * A "doc" annotation adds documentation to a model element. It has at least one argument, and all arguments must be
 * statically-evaluable unnamed arguments of type 'string'.
 * </p>
 */
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
    public final void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // Check for existence of arguments.
        if (annotation.getArguments().isEmpty()) {
            reporter.reportProblem(annotation, "missing an argument.", annotation.getPosition(),
                    SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
        }

        // Check provided arguments.
        for (AnnotationArgument arg: annotation.getArguments()) {
            // 1) Check for unnamed argument.
            if (arg.getName() != null) {
                reporter.reportProblem(annotation, "unsupported named argument.", arg.getPosition(),
                        SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
            }

            // 2) Check argument value.
            boolean doEvaluationCheck = true;

            // 2a) Check for argument having a string-typed value.
            CifType valueType = CifTypeUtils.normalizeType(arg.getValue().getType());
            if (!(valueType instanceof StringType)) {
                reporter.reportProblem(annotation,
                        fmt("argument must have a value of type \"string\", but has a value of type \"%s\".",
                                CifTextUtils.typeToStr(valueType)),
                        arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
                doEvaluationCheck = false;
            }

            // 2b) Check that argument can be statically evaluated.
            if (!CifValueUtils.hasSingleValue(arg.getValue(), false, true)) {
                reporter.reportProblem(annotation, "argument cannot be evaluated statically.",
                        arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                // Non-fatal problem.
                doEvaluationCheck = false;
            }

            // 2c) Check for evaluation errors.
            if (doEvaluationCheck) {
                try {
                    CifDocAnnotationUtils.getDoc(annotation);
                } catch (InvalidModelException e) {
                    CifEvalException evalErr = (CifEvalException)e.getCause();
                    String evalErrMsg = evalErr.toString();
                    if (!evalErrMsg.endsWith(".")) {
                        evalErrMsg += ".";
                    }
                    reporter.reportProblem(annotation,
                            fmt("argument cannot be evaluated statically, "
                                    + "as evaluating it results in an evaluation error: %s", evalErrMsg),
                            arg.getValue().getPosition(), SemanticProblemSeverity.ERROR);
                    // Non-fatal problem.
                }
            }
        }
    }

    @Override
    public final void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Do nothing.
    }
}
