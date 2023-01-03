//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

/**
 * Generator that can be used to generate LaTeX documentation skeletons from Ecore models. Generates a skeleton .tex
 * file that can be used to provide the details for the .tex file generated by the
 * {@link EmfLatexDocSkeletonDetailGenerator} class. Copy the file, remove the {@code "_generated"} part from the file
 * name of the copy, and modify the copy. Don't modify the generated file itself.
 *
 * <p>
 * The following Ecore features are currently not supported, and may lead to invalid code:
 * <ul>
 * <li>Multiple inheritance.</li>
 * <li>Interfaces.</li>
 * <li>Advanced Ecore properties (like unsettable, derived, etc).</li>
 * </ul>
 * </p>
 *
 * @see EmfLatexDocSkeletonGenerator
 */
public class EmfLatexDocSkeletonDetailGenerator extends EmfLatexCodeGenerator {
    /** LaTeX text code that indicates it is a description place holder. */
    private static final String TODO_TEXT = "TODO: describe this.";

    /**
     * Application that can generate a LaTeX documentation skeleton for an {@link EPackage}, including all its
     * sub-packages (recursively).
     *
     * @param args Application arguments:
     *     <ul>
     *     <li>The full name of the main {@link EPackage} Java class. For instance, {@code
     *           "bla.somelang.metamodel.somelang.SomeLangPackage"}.</li>
     *     <li>The path to add to the Java class path to allow the main package Java class to be found. For instance,
     *     {@code "bin"}. It is allowed to add multiple paths. For instance, {@code "bin|../bin"}.</li>
     *     <li>The output directory path. For instance, {@code "docs"}.</li>
     *     </ul>
     * @throws IOException In case the code could not be written to a file; or in case the java class path extension
     *     path is invalid.
     * @throws ClassNotFoundException If the class can not be found.
     * @throws NoSuchFieldException If the {@code eINSTANCE} field is not found in the class.
     * @throws IllegalAccessException If the {@code eINSTANCE} field is inaccessible.
     */
    public static void main(String[] args)
            throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException
    {
        // Process arguments.
        String mainPkgClassName = args[0];
        String binPath = args[1];
        String outputPath = args[2];

        // Resolve the package.
        EPackage mainPkg = loadEPackage(mainPkgClassName, binPath);

        // Generate code for the package.
        CodeBox box = generate(mainPkg);

        // Try to write the code to a file.
        String langName = getRootPackageName(mainPkg);
        String outputFilePath = new File(new File(outputPath), langName + "_ecore_doc_details_generated.tex")
                .getAbsolutePath();
        box.writeToFile(outputFilePath);

        // We are done.
        System.out.printf("Code written to: %s%n", outputFilePath);
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EPackage}, including all its sub-packages
     * (recursively).
     *
     * @param pkg The {@link EPackage} to generate the LaTeX code for.
     * @return The generated code.
     */
    private static CodeBox generate(EPackage pkg) {
        CodeBox box = new MemoryCodeBox();
        box.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        box.add("%% Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation");
        box.add("%%");
        box.add("%% See the NOTICE file(s) distributed with this work for additional");
        box.add("%% information regarding copyright ownership.");
        box.add("%%");
        box.add("%% This program and the accompanying materials are made available under the terms");
        box.add("%% of the MIT License which is available at https://opensource.org/licenses/MIT");
        box.add("%%");
        box.add("%% SPDX-License-Identifier: MIT");
        box.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        box.add();
        genEPackage(pkg, box);
        return box;
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EPackage}, including all its sub-packages
     * (recursively).
     *
     * @param pkg The {@link EPackage} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEPackage(EPackage pkg, CodeBox box) {
        box.add("%% Package %s", pkg.getName());
        box.add("\\newcommand{\\pkgdocu%s}{", pkg.getName());
        box.add(TODO_TEXT);
        box.add("}");
        box.add();

        List<EDataType> dataTypes = getDataTypes(pkg);
        for (EDataType dataType: dataTypes) {
            genEDataType(dataType, box);
        }

        List<EEnum> enums = getEnums(pkg);
        for (EEnum e: enums) {
            genEEnum(e, box);
        }

        List<EClass> classes = getClasses(pkg, false);
        for (EClass cls: classes) {
            genEClass(cls, box);
        }

        box.add();
        for (EPackage subPkg: pkg.getESubpackages()) {
            genEPackage(subPkg, box);
        }
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EDataType}.
     *
     * @param dt The {@link EDataType} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEDataType(EDataType dt, CodeBox box) {
        genEClassifier(dt, box);
        box.add("\\newcommand{\\dtypedocu%s}{", dt.getName());
        box.add(TODO_TEXT);
        box.add("}");
        box.add();
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EEnum}.
     *
     * @param e The {@link EEnum} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEEnum(EEnum e, CodeBox box) {
        genEClassifier(e, box);
        box.add("\\newcommand{\\enumdocu%s}{", e.getName());
        box.add(TODO_TEXT);
        box.add("}");
        box.add();

        List<EEnumLiteral> lits = getLiterals(e);
        for (EEnumLiteral lit: lits) {
            genELiteral(lit, e, box);
        }
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EClass}. Does not support classes with multiple
     * inheritance.
     *
     * @param cls The {@link EClass} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEClass(EClass cls, CodeBox box) {
        Assert.check(cls.getESuperTypes().size() <= 1);
        genEClassifier(cls, box);
        box.add("\\newcommand{\\clsdocu%s}{", cls.getName());
        box.add(TODO_TEXT);
        box.add("}");
        box.add();

        List<EStructuralFeature> feats = getStructuralFeatures(cls);
        for (EStructuralFeature feat: feats) {
            genEStructuralFeature(feat, cls, box);
        }
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EClassifier}.
     *
     * @param cls The {@link EClassifier} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEClassifier(EClassifier cls, CodeBox box) {
        box.add("%% %s (%s)", cls.getName(), getClassifierTypeName(cls));
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EStructuralFeature}.
     *
     * @param cls The {@link EClass} in which the structural feature is defined.
     * @param feat The {@link EStructuralFeature} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEStructuralFeature(EStructuralFeature feat, EClass cls, CodeBox box) {
        box.add("\\newcommand{\\featdocu%s%s}{", cls.getName(), feat.getName());
        box.add(TODO_TEXT);
        box.add("}");
        box.add();
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EEnumLiteral}.
     *
     * @param lit The {@link EEnumLiteral} to generate the LaTeX code for.
     * @param e The {@link EEnum} that the literal is a part of.
     * @param box The generated code (appended in-place).
     */
    private static void genELiteral(EEnumLiteral lit, EEnum e, CodeBox box) {
        box.add("\\newcommand{\\enumlitdocu%s%s}{", e.getName(), nameToLatex(lit.getName()));
        box.add(TODO_TEXT);
        box.add("}");
        box.add();
    }
}
