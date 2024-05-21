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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.common.java.Sets.list2set;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/** In-place transformation that removes annotations. */
public class RemoveAnnotations extends CifWalker implements CifToCifTransformation {
    /** The annotations to keep. */
    private final Set<String> annotationsToKeep;

    /** Constructor for the {@link RemoveAnnotations} class. Remove all annotations. */
    public RemoveAnnotations() {
        this(Collections.emptySet());
    }

    /**
     * Constructor for the {@link RemoveAnnotations} class.
     *
     * @param annotationsToKeep The names of the annotations to keep.
     */
    public RemoveAnnotations(String... annotationsToKeep) {
        this(list2set(Arrays.asList(annotationsToKeep)));
    }

    /**
     * Constructor for the {@link RemoveAnnotations} class.
     *
     * @param annotationsToKeep The names of the annotations to keep.
     */
    public RemoveAnnotations(Set<String> annotationsToKeep) {
        this.annotationsToKeep = annotationsToKeep;
    }

    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    @Override
    protected void preprocessAnnotatedObject(AnnotatedObject annotatedObj) {
        // Get the current annotations.
        List<Annotation> annos = annotatedObj.getAnnotations();

        // Remove annotations, keeping some if requested.
        if (annos.isEmpty()) {
            // Nothing to do.
        } else if (annotationsToKeep.isEmpty()) {
            // Remove all annotations.
            annos.clear();
        } else {
            // Get the annotations to keep.
            annos = annos.stream().filter(anno -> annotationsToKeep.contains(anno.getName())).toList();

            // Remove all annotations.
            annotatedObj.getAnnotations().clear();

            // Re-add the annotations that should be kept. By not removing them one by one, but rather re-adding the
            // ones to keep, we can prevent costly moves in the underlying array storage for the 'annotations' feature
            // of annotated objects.
            annotatedObj.getAnnotations().addAll(annos);
        }
    }
}
