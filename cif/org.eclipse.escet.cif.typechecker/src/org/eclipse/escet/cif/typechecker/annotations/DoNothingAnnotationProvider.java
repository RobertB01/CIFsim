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

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;

/** Annotation provider that performs no checking. */
public final class DoNothingAnnotationProvider extends AnnotationProvider {
    /**
     * Constructor for the {@link DoNothingAnnotationProvider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public DoNothingAnnotationProvider(String annotationName) {
        super(annotationName);
    }

    @Override
    public final void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // Do nothing.
    }

    @Override
    public final void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Do nothing.
    }
}
