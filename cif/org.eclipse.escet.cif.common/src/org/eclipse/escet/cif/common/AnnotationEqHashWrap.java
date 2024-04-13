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

package org.eclipse.escet.cif.common;

import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/** CIF annotation wrapper class, for proper hashing and equality. */
public class AnnotationEqHashWrap {
    /** The wrapped annotation. */
    public final Annotation annotation;

    /** Per annotation argument name , the evaluated argument value. */
    private final Map<String, Object> argNamesToValues;

    /** The values of the unnamed arguments of the annotation. */
    private final Multiset<Object> unnamedArgsValues;

    /** The cached hash code of this wrapper. */
    private final int hashCode;

    /**
     * Constructor for the {@link AnnotationEqHashWrap}.
     *
     * @param annotation The wrapped annotation.
     */
    public AnnotationEqHashWrap(Annotation annotation) {
        this.annotation = annotation;
        this.argNamesToValues = annotation.getArguments().stream().filter(arg -> arg.getName() != null)
                .collect(Collectors.toMap(arg -> arg.getName(), arg -> evalAnnoArgValue(arg.getValue())));
        this.unnamedArgsValues = annotation.getArguments().stream().filter(arg -> arg.getName() == null)
                .map(arg -> evalAnnoArgValue(arg.getValue()))
                .collect(Collectors.toCollection(() -> HashMultiset.create()));
        this.hashCode = annotation.getName().hashCode() + argNamesToValues.hashCode();
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

        // Check annotation name and arguments:
        // - We use a map of argument names as the order of the named arguments is not relevant.
        // - For unnamed arguments, we use a multiset since the order is not relevant, but the number of times that an
        //   argument is provided may be relevant.
        // - And we use evaluated argument values such that for instance values '{1, 2}' and '{2, 1}' are considered
        //   equal.
        return this.annotation.getName().equals(other.annotation.getName())
                && this.argNamesToValues.equals(other.argNamesToValues)
                && this.unnamedArgsValues.equals(other.unnamedArgsValues);
    }
}
