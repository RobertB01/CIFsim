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

/** Second annotation provider for the 'cif:typechecker:tests:anno:multi' annotation. */
public class MultiAnnotationProvider2 extends AnnotationProvider {
    /**
     * Constructor for the {@link MultiAnnotationProvider2} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public MultiAnnotationProvider2(String annotationName) {
        super(annotationName);
    }

    @Override
    public void checkAnnotation(AnnotatedObject annotatedObject, Annotation annotation,
            AnnotationProblemReporter reporter)
    {
        // Check nothing.
    }

    @Override
    public void checkGlobal(Specification spec, AnnotationProblemReporter reporter) {
        // Check nothing.
    }
}
