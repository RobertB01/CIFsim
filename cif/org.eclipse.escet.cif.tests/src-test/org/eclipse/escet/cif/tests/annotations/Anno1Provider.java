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

package org.eclipse.escet.cif.tests.annotations;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProblemReporter;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProvider;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** Annotation provider for the 'cif:typechecker:tests:anno1' annotation. */
public class Anno1Provider extends AnnotationProvider {
    /** The number of annotations in the specification that are handled by this annotation provider. */
    private int count = 0;

    /**
     * Constructor for the {@link Anno1Provider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public Anno1Provider(String annotationName) {
        super(annotationName);
    }

    @Override
    public void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // Count the number of annotations.
        count++;

        // Check for exactly one argument. Tests reporting an error.
        if (annotation.getArguments().size() != 1) {
            reporter.reportProblem(annotation,
                    fmt("the annotation must have exactly 1 argument, but has %d.", annotation.getArguments().size()),
                    annotation.getPosition(), SemanticProblemSeverity.ERROR);
            // Non-fatal problem.
            return;
        }
        AnnotationArgument arg = annotation.getArguments().get(0);

        // Check for argument named 'arg'. Tests reporting a warning.
        if (!arg.getName().equals("arg")) {
            reporter.reportProblem(annotation, "annotation argument must be named \"arg\".", arg.getPosition(),
                    SemanticProblemSeverity.WARNING);
            // Non-fatal problem.
        }

        // Check for argument having a boolean value. Tests reporting a second warning.
        CifType valueType = CifTypeUtils.normalizeType(arg.getValue().getType());
        if (!(valueType instanceof BoolType)) {
            reporter.reportProblem(annotation,
                    fmt("argument \"arg\" must have a value of type \"bool\", " + "but has a value of type \"%s\".",
                            CifTextUtils.typeToStr(valueType)),
                    arg.getValue().getPosition(), SemanticProblemSeverity.WARNING);
            // Non-fatal problem.
        }
    }

    @Override
    public void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Check for at most no more than 3 annotations per model. Tests reporting a global problem.
        if (count > 3) {
            reporter.reportProblem(annotationName,
                    fmt("the specification contains more than 3 such annotations, namely %d.", count),
                    spec.getPosition(), SemanticProblemSeverity.WARNING);
            // Non-fatal problem.
        }
    }
}
