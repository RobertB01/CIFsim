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

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;

/**
 * CIF annotation provider. Provides functionality to perform additional type checking on a specific type of annotation.
 *
 * <p>
 * Per specification, per annotation (of a certain name), a single instance of an annotation provider is created. The
 * type checker uses that provider instance to check all the annotations with that annotation name, one by one, by
 * invoking {@link #checkAnnotation}. After each annotation has been checked individually, the type checker allows the
 * provider to check global constraints, by invoking {@link #checkGlobal}. If the specification does not contain any
 * annotations of a certain name, the provider for those annotations is not created, and thus also its
 * {@link #checkGlobal} is not invoked.
 * </p>
 *
 * <p>
 * The type checker will ensure that each annotated object is free of duplicate annotations. The type checker also
 * ensures that each annotation has unique arguments. Providers may thus assume these constraints to hold.
 * </p>
 *
 * <p>
 * The type checker only provides annotations to the provider after component definition/instantiation has been
 * eliminated. This ensures that only concrete objects are annotated with annotations, and that all such objects will
 * have their annotations checked.
 * </p>
 *
 * <p>
 * Each annotation provider must have a constructor with a single argument of type {@link String}, the name of the
 * annotations handled by this annotation provider.
 * </p>
 */
public abstract class AnnotationProvider {
    /** The name of the annotations handled by this annotation provider. */
    protected final String annotationName;

    /**
     * Constructor for the {@link AnnotationProvider} class.
     *
     * @param annotationName The name of the annotations handled by this annotation provider.
     */
    public AnnotationProvider(String annotationName) {
        this.annotationName = annotationName;
    }

    /**
     * Perform additional type checking on an annotation.
     *
     * @param annotation The annotation check.
     * @param reporter The reporter to use to report problems in the annotation.
     */
    public abstract void checkAnnotation(Annotation annotation, AnnotationProblemReporter reporter);

    /**
     * Perform additional global type checking (for the entire specification), for the type of annotation provided by
     * this provider.
     *
     * @param spec The specification check.
     * @param reporter The reporter to use to report problems in the specification.
     */
    public abstract void checkGlobal(Specification spec, AnnotationProblemReporter reporter);
}
