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

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Sets;

/** CIF annotation wrapper class, for proper hashing and equality. */
public class AnnotationEqHashWrap {
    /** The wrapped annotation. */
    public final Annotation annotation;

    /** Per annotation argument name, the evaluated argument value. */
    private final Map<String, Object> argNamesToValues;

    /** The cached hash code of this wrapper. */
    private final int hashCode;

    /**
     * Constructor for the {@link AnnotationEqHashWrap}.
     *
     * @param annotation The wrapped annotation.
     */
    public AnnotationEqHashWrap(Annotation annotation) {
        this.annotation = annotation;
        this.argNamesToValues = evalAnnoArgValues(annotation);
        this.hashCode = computeHashCode(annotation, argNamesToValues);
    }

    /**
     * Evaluate the annotation argument values.
     *
     * @param annotation The annotation.
     * @return Per annotation argument name, the evaluated argument value.
     */
    private static Map<String, Object> evalAnnoArgValues(Annotation annotation) {
        return annotation.getArguments().stream()
                .collect(Collectors.toMap(arg -> arg.getName(), arg -> evalAnnoArgValue(arg.getValue())));
    }

    /**
     * Evaluate an annotation argument value.
     *
     * @param value The argument value.
     * @return The evaluated argument value.
     */
    private static Object evalAnnoArgValue(Expression value) {
        try {
            return CifEvalUtils.eval(value, false);
        } catch (CifEvalException e) {
            // Type checker should have determined that it is safe to evaluate these literals.
            throw new RuntimeException("Failed to evaluate annotation argument value.", e);
        }
    }

    /**
     * Compute the hash code of an annotation.
     *
     * @param annotation The annotation.
     * @param argNamesToValues Per annotation argument name, the evaluated argument value.
     * @return The hash code of the annotation.
     */
    private static int computeHashCode(Annotation annotation, Map<String, Object> argNamesToValues) {
        int h = annotation.getName().hashCode();
        for (Entry<String, Object> entry: argNamesToValues.entrySet()) {
            h ^= entry.getKey().hashCode() + entry.getValue().hashCode();
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

        // Check annotation arguments. Note that the order of the arguments is not relevant.
        if (this.annotation.getArguments().size() != other.annotation.getArguments().size()) {
            return false;
        }
        for (String argName: Sets.union(this.argNamesToValues.keySet(), other.argNamesToValues.keySet())) {
            // Check that both have the argument.
            Object thisArgValue = this.argNamesToValues.get(argName);
            Object otherArgValue = other.argNamesToValues.get(argName);
            if (thisArgValue == null || otherArgValue == null) {
                return false;
            }

            // Check argument values. Use evaluated values such that for instance '{1, 2}' and '{2, 1}' are considered
            // equal.
            if (!thisArgValue.equals(otherArgValue)) {
                return false;
            }
        }
        return true;
    }
}
