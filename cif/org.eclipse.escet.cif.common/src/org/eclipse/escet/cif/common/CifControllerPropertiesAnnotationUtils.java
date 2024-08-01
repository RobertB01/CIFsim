//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.common.java.Assert;

/** CIF controller properties annotations utilities. */
public class CifControllerPropertiesAnnotationUtils {
    /** Constructor for the {@link CifControllerPropertiesAnnotationUtils} class. */
    private CifControllerPropertiesAnnotationUtils() {
        // Static class.
    }

    /**
     * Returns whether the given specification has bounded response, if known.
     *
     * @param spec The specification.
     * @return {@code true} if the specification has bounded response, {@code false} if it does not have bounded
     *     response, or {@code null} if it is not known whether the specification has bounded response.
     */
    public static Boolean hasBoundedResponse(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null : CifAnnotationUtils.tryGetArgument(anno, "boundedResponse");
        return (arg == null) ? null : ((BoolExpression)arg.getValue()).isValue();
    }

    /**
     * Returns whether the given specification has confluence, if known.
     *
     * @param spec The specification.
     * @return {@code true} if the specification has confluence for controllable events, {@code false} if it may not
     *     have confluence, or {@code null} if it is not known whether the specification has confluence.
     */
    public static Boolean hasConfluence(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null : CifAnnotationUtils.tryGetArgument(anno, "confluence");
        return (arg == null) ? null : ((BoolExpression)arg.getValue()).isValue();
    }

    /**
     * Returns whether the given specification has finite response, if known.
     *
     * @param spec The specification.
     * @return {@code true} if the specification has finite response, {@code false} if it may not have finite response,
     *     or {@code null} if it is not known whether the specification has finite response.
     */
    public static Boolean hasFiniteResponse(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null : CifAnnotationUtils.tryGetArgument(anno, "finiteResponse");
        return (arg == null) ? null : ((BoolExpression)arg.getValue()).isValue();
    }

    /**
     * Returns whether the given specification is non-blocking under control, if known.
     *
     * @param spec The specification.
     * @return {@code true} if the specification is non-blocking under control, {@code false} if it is not non-blocking
     *     under control, or {@code null} if it is not known whether the specification is non-blocking under control.
     */
    public static Boolean isNonBlockingUnderControl(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null
                : CifAnnotationUtils.tryGetArgument(anno, "nonBlockingUnderControl");
        return (arg == null) ? null : ((BoolExpression)arg.getValue()).isValue();
    }

    /**
     * Returns the uncontrollable events bound for the given specification, if known.
     *
     * @param spec The specification.
     * @return The non-negative bound if the specification has bounded response, {@code null} otherwise (it does not
     *     have bounded response, or it is not known whether it has bounded response).
     */
    public static Integer getUncontrollablesBound(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null
                : CifAnnotationUtils.tryGetArgument(anno, "uncontrollablesBound");
        return (arg == null) ? null : ((IntExpression)arg.getValue()).getValue();
    }

    /**
     * Returns the controllable events bound for the given specification, if known.
     *
     * @param spec The specification.
     * @return The non-negative bound if the specification has bounded response, {@code null} otherwise (it does not
     *     have bounded response, or it is not known whether it has bounded response).
     */
    public static Integer getControllablesBound(Specification spec) {
        Annotation anno = CifAnnotationUtils.tryGetSingleAnnotation(spec, "controller:properties");
        AnnotationArgument arg = (anno == null) ? null : CifAnnotationUtils.tryGetArgument(anno, "controllablesBound");
        return (arg == null) ? null : ((IntExpression)arg.getValue()).getValue();
    }

    /**
     * Set the bounded response property of the specification.
     *
     * @param spec The specification.
     * @param uncontrollablesBound The non-negative uncontrollable events bound, or {@code null} if the specification
     *     does not have bounded response. Must be {@code null} if and only if {@code controllablesBound} is
     *     {@code null}.
     * @param controllablesBound The non-negative controllable events bound, or {@code null} if the specification does
     *     not have bounded response. Must be {@code null} if and only if {@code uncontrollablesBound} is {@code null}.
     */
    @SuppressWarnings("null")
    public static void setBoundedResponse(Specification spec, Integer uncontrollablesBound,
            Integer controllablesBound)
    {
        // Sanity check.
        Assert.areEqual(controllablesBound == null, uncontrollablesBound == null);

        // Set 'boundedResponse'.
        boolean boundedResponse = controllablesBound != null;
        Annotation anno = CifAnnotationUtils.getOrCreateSingleAnnotation(spec, "controller:properties");
        CifAnnotationUtils.setArgument(anno, "boundedResponse", CifValueUtils.makeBool(boundedResponse));

        // Set or remove '(un)controllablesBound'.
        if (boundedResponse) {
            CifAnnotationUtils.setArgument(anno, "uncontrollablesBound", CifValueUtils.makeInt(uncontrollablesBound));
            CifAnnotationUtils.setArgument(anno, "controllablesBound", CifValueUtils.makeInt(controllablesBound));
        } else {
            CifAnnotationUtils.tryRemoveArgument(anno, "uncontrollablesBound");
            CifAnnotationUtils.tryRemoveArgument(anno, "controllablesBound");
        }
    }

    /**
     * Set the confluence property of the specification.
     *
     * @param spec The specification.
     * @param confluence Whether the specification has confluence ({@code true}) or may not have it ({@code false}).
     */
    public static void setConfluence(Specification spec, boolean confluence) {
        Annotation anno = CifAnnotationUtils.getOrCreateSingleAnnotation(spec, "controller:properties");
        CifAnnotationUtils.setArgument(anno, "confluence", CifValueUtils.makeBool(confluence));
    }

    /**
     * Set the finite response property of the specification.
     *
     * @param spec The specification.
     * @param finiteResponse Whether the specification has finite response ({@code true}) or may not have it
     *     ({@code false}).
     */
    public static void setFiniteResponse(Specification spec, boolean finiteResponse) {
        Annotation anno = CifAnnotationUtils.getOrCreateSingleAnnotation(spec, "controller:properties");
        CifAnnotationUtils.setArgument(anno, "finiteResponse", CifValueUtils.makeBool(finiteResponse));
    }

    /**
     * Set the non-blocking under control property of the specification.
     *
     * @param spec The specification.
     * @param nonBlockingUnderControl Whether the specification is non-blocking under control ({@code true}) or not
     *     ({@code false}).
     */
    public static void setNonBlockingUnderControl(Specification spec, boolean nonBlockingUnderControl) {
        Annotation anno = CifAnnotationUtils.getOrCreateSingleAnnotation(spec, "controller:properties");
        CifAnnotationUtils.setArgument(anno, "nonBlockingUnderControl",
                CifValueUtils.makeBool(nonBlockingUnderControl));
    }

    /**
     * Remove the controller property annotation of the given specification, if it exists.
     *
     * @param spec The specification.
     */
    public static void remove(Specification spec) {
        CifAnnotationUtils.removeAnnotations(spec, "controller:properties");
    }
}
