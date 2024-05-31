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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAnnotationArgument;

import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
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
     * Returns the annotation argument with the given name, for the given annotation, if present.
     *
     * @param anno The annotation.
     * @param argName The annotation argument name. Must not be {@code null}.
     * @return The annotation argument, if present.
     */
    public static Optional<AnnotationArgument> getArgument(Annotation anno, String argName) {
        Assert.notNull(argName);
        return anno.getArguments().stream().filter(arg -> argName.equals(arg.getName())).findFirst();
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
        Optional<AnnotationArgument> optArg = CifAnnotationUtils.getArgument(anno, argName);
        if (optArg.isEmpty()) {
            AnnotationArgument arg = newAnnotationArgument(argName, null, value);
            anno.getArguments().add(arg);
            return arg;
        } else {
            AnnotationArgument arg = optArg.get();
            arg.setValue(value);
            return arg;
        }
    }

    /**
     * Removes the annotation argument with the given name, for the given annotation, if present.
     *
     * @param anno The annotation.
     * @param argName The annotation argument name. Must not be {@code null}.
     * @return The annotation argument that was removed, if applicable.
     */
    public static Optional<AnnotationArgument> removeArgument(Annotation anno, String argName) {
        Assert.notNull(argName);
        Optional<AnnotationArgument> arg = CifAnnotationUtils.getArgument(anno, argName);
        if (arg.isPresent()) {
            EMFHelper.removeFromParentContainment(arg.get());
        }
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
