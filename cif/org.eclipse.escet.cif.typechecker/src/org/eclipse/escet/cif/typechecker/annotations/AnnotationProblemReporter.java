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

package org.eclipse.escet.cif.typechecker.annotations;

import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;

/** Annotation problem reporter, for {@link AnnotationProvider annotation providers}. */
public interface AnnotationProblemReporter {
    /**
     * Report an annotation post-checking problem.
     *
     * @param annotation The annotation for which to report the problem.
     * @param message The violation message. Should be a full sentence, with a period at the end. Should <em>not</em>
     *     start with a capitalized first letter.
     * @param position The position on which to report the problem.
     * @param severity The severity of the problem.
     */
    public default void reportProblem(Annotation annotation, String message, Position position,
            SemanticProblemSeverity severity)
    {
        reportProblem(annotation.getName(), message, position, severity);
    }

    /**
     * Report an annotation post-checking problem.
     *
     * @param annotationName The name of the annotation for which to report the problem.
     * @param message The violation message. Should be a full sentence, with a period at the end. Should <em>not</em>
     *     start with a capitalized first letter.
     * @param position The position on which to report the problem.
     * @param severity The severity of the problem.
     */
    public void reportProblem(String annotationName, String message, Position position,
            SemanticProblemSeverity severity);
}
