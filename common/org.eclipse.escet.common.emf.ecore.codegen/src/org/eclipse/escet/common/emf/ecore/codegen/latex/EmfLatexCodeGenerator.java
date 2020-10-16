//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.emf.ecore.codegen.latex;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.escet.common.emf.ecore.codegen.EmfCodeGenerator;

/** Base class for EMF code generators (that generate LaTeX output), with useful helper methods. */
public abstract class EmfLatexCodeGenerator extends EmfCodeGenerator {
    /**
     * Returns the textual representation of the name of the type of a classifier.
     *
     * @param cls The classifier.
     * @return The textual representation of the name of the type of the classifier.
     */
    protected static String getClassifierTypeName(EClassifier cls) {
        return (cls instanceof EClass) ? ((EClass)cls).isAbstract() ? "abstract class" : "class"
                : (cls instanceof EEnum) ? "enumeration" : "datatype";
    }

    /**
     * Returns a name that can be used as part of a LaTeX command name. The invalid characters (currently only numbers)
     * are simply removed. Note that this does not guarantee unique names!
     *
     * @param name The name to convert.
     * @return The converted name.
     */
    protected static String nameToLatex(String name) {
        return name.replaceAll("[0-9]", "");
    }
}
