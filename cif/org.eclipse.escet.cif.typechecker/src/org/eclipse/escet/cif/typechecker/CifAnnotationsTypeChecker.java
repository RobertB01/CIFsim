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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAnnotation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAnnotationArgument;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationArgument;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.parser.ast.annotations.AAnnotation;
import org.eclipse.escet.cif.parser.ast.annotations.AAnnotationArgument;
import org.eclipse.escet.cif.typechecker.scopes.SymbolScope;

/** Type checker for CIF annotations. */
public class CifAnnotationsTypeChecker {
    /** Constructor of the {@link CifAnnotationsTypeChecker} class. */
    private CifAnnotationsTypeChecker() {
        // Static class.
    }

    /**
     * Transforms annotations and performs type checking on them.
     *
     * @param astAnnos The CIF AST annotations to transform.
     * @param annotatedObject The symbol table entry for the object that is annotated with the annotations.
     * @param scope The scope in which to resolve references within the annotations, such as in the values of arguments.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel annotations.
     */
    public static List<Annotation> transAnnotations(List<AAnnotation> astAnnos, SymbolTableEntry annotatedObject,
            SymbolScope<?> scope, CifTypeChecker tchecker)
    {
        Supplier<String> descriptionSupplier = () -> fmt("\"%s\"", annotatedObject.getAbsName());
        return transAnnotations(astAnnos, descriptionSupplier, scope, tchecker);
    }

    /**
     * Transforms annotations and performs type checking on them.
     *
     * @param astAnnos The CIF AST annotations to transform.
     * @param descriptionSupplier Function to obtain a textual description of the annotated object for reporting type
     *     check problems.
     * @param scope The scope in which to resolve references within the annotations, such as in the values of arguments.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel annotations.
     */
    public static List<Annotation> transAnnotations(List<AAnnotation> astAnnos, Supplier<String> descriptionSupplier,
            SymbolScope<?> scope, CifTypeChecker tchecker)
    {
        // Type check each of the annotations separately.
        List<Annotation> mmAnnos = listc(astAnnos.size());
        for (AAnnotation astAnno: astAnnos) {
            mmAnnos.add(transAnnotation(astAnno, scope, tchecker));
        }

        // Return the annotation metamodel objects.
        return mmAnnos;
    }

    /**
     * Transforms an annotation and performs type checking on it.
     *
     * @param astAnno The CIF AST annotation to transform.
     * @param scope The scope in which to resolve references within the annotation, such as in the values of arguments.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel annotation.
     */
    private static Annotation transAnnotation(AAnnotation astAnno, SymbolScope<?> scope, CifTypeChecker tchecker) {
        // Get annotation name.
        String annoName = astAnno.name.text;
        while (annoName.startsWith("@")) {
            annoName = annoName.substring(1);
        }

        // Create annotation metamodel object.
        Annotation mmAnno = newAnnotation();
        mmAnno.setName(annoName);
        mmAnno.setPosition(astAnno.createPosition());

        // Create annotation argument metamodel objects, and add them to the annotation metamodel object.
        for (AAnnotationArgument astArg: astAnno.arguments) {
            // Type check the value expression.
            Expression mmValue = transExpression(astArg.value, null, scope, null, tchecker);

            // Construct annotation argument metamodel object.
            AnnotationArgument mmArg = newAnnotationArgument();
            mmArg.setName(astArg.name.text.replace("$", "")); // Remove keyword escaping.
            mmArg.setPosition(astArg.createPosition());
            mmArg.setValue(mmValue);

            // Add annotation argument to the annotation.
            mmAnno.getArguments().add(mmArg);
        }

        // Ensure all the arguments for the annotation are unique, based on their names.
        Map<String, AnnotationArgument> nameToArg = mapc(mmAnno.getArguments().size());
        for (AnnotationArgument mmArg: mmAnno.getArguments()) {
            AnnotationArgument prev = nameToArg.put(mmArg.getName(), mmArg);
            if (prev != null) {
                tchecker.addProblem(ErrMsg.ANNO_DUPL_ARG, prev.getPosition(), mmArg.getName(), mmAnno.getName());
                tchecker.addProblem(ErrMsg.ANNO_DUPL_ARG, mmArg.getPosition(), mmArg.getName(), mmAnno.getName());
                // Non-fatal error.
            }
        }

        // Return the annotation metamodel object.
        return mmAnno;
    }
}
