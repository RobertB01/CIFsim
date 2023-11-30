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

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProblemReporter;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProvider;

/**
 * Annotation provider that crashes. Tests that if the annotation is not used, the provider is not created. Also tests
 * that if used, the error is properly reported.
 */
public class CrashingAnnoProvider extends AnnotationProvider {
    /**
     * Constructor for the {@link CrashingAnnoProvider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public CrashingAnnoProvider(String annotationName) {
        super(annotationName);
        throw new RuntimeException("Crash!");
    }

    @Override
    public void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // Not used.
    }

    @Override
    public void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Not used.
    }
}
