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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;

/** CIF annotations utility methods. */
public class CifAnnotationUtils {
    /** Constructor for the {@link CifAnnotationUtils} class. */
    private CifAnnotationUtils() {
        // Static class.
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
