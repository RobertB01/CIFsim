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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.common.java.exceptions.InvalidModelException;

/** CIF 'doc' annotation utilities. */
public class CifDocAnnotationUtils {
    /** Constructor for the {@link CifDocAnnotationUtils} class. */
    private CifDocAnnotationUtils() {
        // Static class.
    }

    /**
     * Returns the documentation texts of an annotated object.
     *
     * @param obj The annotated object. Must be a named object.
     * @return The documentation texts.
     * @throws InvalidModelException If a documentation text can not be evaluated.
     */
    public static List<String> getDocs(AnnotatedObject obj) {
        return CifAnnotationUtils.getAnnotations(obj, "doc").map(a -> getDoc(a)).toList();
    }

    /**
     * Returns the documentation text of a documentation annotation.
     *
     * @param docAnno The documentation annotation.
     * @return The documentation text.
     * @throws InvalidModelException If the documentation text can not be evaluated.
     */
    public static String getDoc(Annotation docAnno) {
        return docAnno.getArguments().stream().map(arg -> getDoc(arg)).collect(Collectors.joining("\n"));
    }

    /**
     * Returns the documentation text of a documentation annotation argument.
     *
     * @param docAnnoArg The documentation annotation argument.
     * @return The documentation text.
     * @throws InvalidModelException If the documentation text can not be evaluated.
     */
    private static String getDoc(AnnotationArgument docAnnoArg) {
        try {
            Object value = CifEvalUtils.eval(docAnnoArg.getValue(), false);
            return (String)value;
        } catch (CifEvalException e) {
            AnnotatedObject annotatedObj = (AnnotatedObject)docAnnoArg.eContainer().eContainer();
            if (CifTextUtils.hasName(annotatedObj)) {
                String msg = fmt("Failed to evaluate an argument of the \"doc\" annotation of \"%s\".",
                        CifTextUtils.getAbsName(annotatedObj));
                throw new InvalidModelException(msg, e);
            } else {
                throw new InvalidModelException("Failed to evaluate an argument of a \"doc\" annotation.", e);
            }
        }
    }
}
