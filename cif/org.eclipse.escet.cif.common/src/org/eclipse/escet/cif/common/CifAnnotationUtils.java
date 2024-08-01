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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAnnotation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAnnotationArgument;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;

/** CIF annotations utility methods. */
public class CifAnnotationUtils {
    /** Constructor for the {@link CifAnnotationUtils} class. */
    private CifAnnotationUtils() {
        // Static class.
    }

    /**
     * Returns whether the given object has an annotation with the given name.
     *
     * @param annotatedObject The annotated objected.
     * @param annoName The name of the annotation.
     * @return {@code true} if the object has an annotation with the given name, {@code false} otherwise.
     */
    public static boolean hasAnnotation(AnnotatedObject annotatedObject, String annoName) {
        return getAnnotations(annotatedObject, annoName).findAny().isPresent();
    }

    /**
     * Returns the single annotation with the given name, from the given object, or {@code null} if there is none.
     *
     * @param annotatedObject The annotated objected.
     * @param annoName The name of the annotation.
     * @return The annotation, or {@code null}.
     * @throws IllegalArgumentException If the annotation is present multiple times on the object.
     */
    public static Annotation tryGetSingleAnnotation(AnnotatedObject annotatedObject, String annoName) {
        List<Annotation> annos = getAnnotations(annotatedObject, annoName).toList();
        if (annos.size() > 1) {
            throw new IllegalArgumentException(
                    fmt("%d annotations named \"%s\" on \"%s\".", annos.size(), annoName, annotatedObject));
        }
        return annos.isEmpty() ? null : first(annos);
    }

    /**
     * Returns the single annotation with the given name, from the given object.
     *
     * @param annotatedObject The annotated objected.
     * @param annoName The name of the annotation.
     * @return The annotation.
     * @throws IllegalArgumentException If the annotation is present multiple times on the object.
     */
    public static Annotation getSingleAnnotation(AnnotatedObject annotatedObject, String annoName) {
        List<Annotation> annos = getAnnotations(annotatedObject, annoName).toList();
        if (annos.size() != 1) {
            throw new IllegalArgumentException(
                    fmt("%d annotations named \"%s\" on \"%s\".", annos.size(), annoName, annotatedObject));
        }
        return first(annos);
    }

    /**
     * Returns the single annotation with the given name, from the given object. If it does not exist, create it and add
     * it to the object.
     *
     * @param annotatedObject The annotated objected.
     * @param annoName The name of the annotation.
     * @return The existing or newly created annotation.
     * @throws IllegalArgumentException If the annotation is present multiple times on the object.
     */
    public static Annotation getOrCreateSingleAnnotation(AnnotatedObject annotatedObject, String annoName) {
        Annotation anno = tryGetSingleAnnotation(annotatedObject, annoName);
        if (anno == null) {
            anno = newAnnotation();
            anno.setName(annoName);
            annotatedObject.getAnnotations().add(anno);
        }
        return anno;
    }

    /**
     * Returns a stream of annotations with the given name, from the given object.
     *
     * @param annotatedObject The annotated objected.
     * @param annoName The name of the annotation.
     * @return The stream of annotations.
     */
    public static Stream<Annotation> getAnnotations(AnnotatedObject annotatedObject, String annoName) {
        return annotatedObject.getAnnotations().stream().filter(anno -> anno.getName().equals(annoName));
    }

    /**
     * Removes all annotations of the given name from the given object.
     *
     * @param annotatedObject The annotated object.
     * @param annoName The name of the annotation.
     */
    public static void removeAnnotations(AnnotatedObject annotatedObject, String annoName) {
        if (hasAnnotation(annotatedObject, annoName)) {
            List<Annotation> annos = annotatedObject.getAnnotations().stream()
                    .filter(anno -> !anno.getName().equals(annoName)).toList();
            annotatedObject.getAnnotations().clear();
            annotatedObject.getAnnotations().addAll(annos);
        }
    }

    /**
     * Returns the annotation argument with the given name, for the given annotation, if present.
     *
     * @param anno The annotation.
     * @param argName The annotation argument name. Must not be {@code null}.
     * @return The annotation argument, or {@code null} if not present.
     */
    public static AnnotationArgument tryGetArgument(Annotation anno, String argName) {
        Assert.notNull(argName);
        return anno.getArguments().stream().filter(arg -> argName.equals(arg.getName())).findFirst().orElse(null);
    }

    /**
     * Returns the annotation argument with the given name, for the given annotation.
     *
     * @param anno The annotation.
     * @param argName The annotation argument name. Must not be {@code null}.
     * @return The annotation argument.
     * @throws IllegalArgumentException If the argument with the given name is not present on the given annotation.
     */
    public static AnnotationArgument getArgument(Annotation anno, String argName) {
        AnnotationArgument arg = tryGetArgument(anno, argName);
        if (arg == null) {
            throw new IllegalArgumentException(
                    fmt("Annotation argument \"%s\" not present on annotation named \"%s\".", argName, anno.getName()));
        }
        return arg;
    }

    /**
     * Sets the annotation argument with the given name, for the given annotation, adding the argument it if is not yet
     * present.
     *
     * @param anno The annotation.
     * @param argName The annotation argument name. Must not be {@code null}.
     * @param value The annotation argument value.
     * @return The annotation argument.
     */
    public static AnnotationArgument setArgument(Annotation anno, String argName, Expression value) {
        Assert.notNull(argName);
        AnnotationArgument arg = CifAnnotationUtils.tryGetArgument(anno, argName);
        if (arg == null) {
            arg = newAnnotationArgument(argName, null, value);
            anno.getArguments().add(arg);
        } else {
            arg.setValue(value);
        }
        return arg;
    }

    /**
     * Removes the annotation argument with the given name, for the given annotation, if present.
     *
     * @param anno The annotation.
     * @param argName The annotation argument name. Must not be {@code null}.
     * @return The annotation argument that was removed, or {@code null} if the argument was not present.
     */
    public static AnnotationArgument tryRemoveArgument(Annotation anno, String argName) {
        Assert.notNull(argName);
        AnnotationArgument arg = CifAnnotationUtils.tryGetArgument(anno, argName);
        if (arg != null) {
            EMFHelper.removeFromParentContainment(arg);
        }
        return arg;
    }

    /**
     * Removes the annotation argument with the given name, for the given annotation.
     *
     * @param anno The annotation.
     * @param argName The annotation argument name. Must not be {@code null}.
     * @return The annotation argument that was removed.
     * @throws IllegalArgumentException If the argument with the given name is not present on the given annotation.
     */
    public static AnnotationArgument removeArgument(Annotation anno, String argName) {
        Assert.notNull(argName);
        AnnotationArgument arg = CifAnnotationUtils.getArgument(anno, argName);
        EMFHelper.removeFromParentContainment(arg);
        return arg;
    }

    /**
     * Is the given object contained in an annotation?
     *
     * <p>
     * All relevant ancestors are considered, not only the direct parent.
     * </p>
     *
     * @param obj The given object.
     * @return {@code true} if it is contained in an annotation, {@code false} otherwise.
     */
    public static boolean isObjInAnnotation(EObject obj) {
        EObject ancestor = obj.eContainer();
        while (ancestor != null) {
            if (ancestor instanceof Annotation) {
                return true;
            }
            ancestor = ancestor.eContainer();
        }
        return false;
    }
}
