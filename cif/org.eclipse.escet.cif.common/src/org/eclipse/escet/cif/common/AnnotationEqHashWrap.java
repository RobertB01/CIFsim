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

package org.eclipse.escet.cif.common;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;

/** CIF annotation wrapper class, for proper hashing and equality. */
public class AnnotationEqHashWrap {
    /** The wrapped annotation. */
    public final Annotation annotation;

    /** The evaluated annotation argument values. */
    private final List<Object> argValues;

    /** The cached hash code of this wrapper. */
    private final int hashCode;

    /**
     * Constructor for the {@link AnnotationEqHashWrap}.
     *
     * @param annotation The wrapped annotation.
     */
    public AnnotationEqHashWrap(Annotation annotation) {
        this.annotation = annotation;
        this.argValues = evalAnnoArgValues(annotation);
        this.hashCode = computeHashCode(annotation, argValues);
    }

    /**
     * Evaluate the annotation argument values.
     *
     * @param annotation The annotation.
     * @return The evaluated argument values.
     */
    private static List<Object> evalAnnoArgValues(Annotation annotation) {
        return annotation.getArguments().stream().map(arg -> {
            try {
                return CifEvalUtils.eval(arg.getValue(), false);
            } catch (CifEvalException e) {
                // Type checker should have determined that it is safe to evaluate these literals.
                throw new RuntimeException("Failed to evaluate annotation argument value.", e);
            }
        }).toList();
    }

    /**
     * Compute the hash code of an annotation.
     *
     * @param annotation The annotation.
     * @param argValues The evaluated annotation argument values.
     * @return The hash code of the annotation.
     */
    private static int computeHashCode(Annotation annotation, List<Object> argValues) {
        int h = annotation.getName().hashCode();
        for (int i = 0; i < argValues.size(); i++) {
            h ^= annotation.getArguments().get(i).getName().hashCode() + argValues.get(i).hashCode();
        }
        return h;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        // Check same object and same type of object.
        if (this == obj) {
            return true;
        }
        AnnotationEqHashWrap other = (AnnotationEqHashWrap)obj;

        // Check annotation name.
        if (!this.annotation.getName().equals(other.annotation.getName())) {
            return false;
        }

        // Check annotation arguments.
        if (this.annotation.getArguments().size() != other.annotation.getArguments().size()) {
            return false;
        }
        for (int i = 0; i < this.annotation.getArguments().size(); i++) {
            // Check argument names.
            String thisArgName = this.annotation.getArguments().get(i).getName();
            String otherArgName = other.annotation.getArguments().get(i).getName();
            if (!thisArgName.equals(otherArgName)) {
                return false;
            }

            // Check argument values. Use evaluated values such that for instance '{1, 2}' and '{2, 1}' are considered
            // equal.
            Object thisArgValue = this.argValues.get(i);
            Object otherArgValue = other.argValues.get(i);
            if (!thisArgValue.equals(otherArgValue)) {
                return false;
            }
        }
        return true;
    }
}
