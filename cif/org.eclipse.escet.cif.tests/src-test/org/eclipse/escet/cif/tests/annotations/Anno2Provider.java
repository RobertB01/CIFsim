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

package org.eclipse.escet.cif.tests.annotations;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProblemReporter;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProvider;

/** Annotation provider for the 'cif:typechecker:tests:anno2' annotation. */
public class Anno2Provider extends AnnotationProvider {
    /**
     * Constructor for the {@link Anno2Provider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public Anno2Provider(String annotationName) {
        super(annotationName);
    }

    @Override
    public void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // No additional checks.
    }

    @Override
    public void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // No additional checks.
    }
}
