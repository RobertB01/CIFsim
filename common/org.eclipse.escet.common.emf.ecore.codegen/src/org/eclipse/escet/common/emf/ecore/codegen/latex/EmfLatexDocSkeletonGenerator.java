//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

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
 * Generator that can be used to generate LaTeX documentation skeletons from Ecore models. Generates a .tex file with
 * place holders, to be filled in with details (see {@link EmfLatexDocSkeletonDetailGenerator} class). The output of
 * this generator is not to be modified.
 *
 * <p>
 * The following Ecore features are currently not supported, and may lead to invalid code:
 * <ul>
 * <li>Multiple inheritance.</li>
 * <li>Interfaces.</li>
 * <li>Advanced Ecore properties (like unsettable, derived, etc).</li>
 * </ul>
 * </p>
 */
public class EmfLatexDocSkeletonGenerator extends EmfLatexCodeGenerator {
    /**
     * Application that can generate a LaTeX documentation skeleton for an {@link EPackage}, including all its
     * sub-packages (recursively).
     *
     * @param args Application arguments:
     *     <ul>
     *     <li>The full name of the main {@link EPackage} Java class. For instance, {@code
     *           "bla.somelang.v1x0x0.metamodel.somelang.SomeLangPackage"}.</li>
     *     <li>The path to add to the Java class path to allow the main package Java class to be found. For instance,
     *     {@code "bin"}. It is allowed to add multiple paths. For instance, {@code "bin|../bin"}.</li>
     *     <li>The output file name path. For instance, {@code "docs"}.</li>
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
        String outputFilePath = new File(new File(outputPath), langName + "_ecore_doc_generated.tex").getAbsolutePath();
        box.writeToFile(outputFilePath);

        // We are done.
        System.out.print("Code written to: " + outputFilePath);
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EPackage}, including all its sub-packages
     * (recursively).
     *
     * @param pkg The {@link EPackage} to generate the LaTeX code for.
     * @return The generated code.
     */
    private static CodeBox generate(EPackage pkg) {
        // Generate header.
        CodeBox box = new MemoryCodeBox();
        box.add("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        box.add("%% Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation");
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
        box.add("%%\\newcommand{\\%spkg}[1]{\\textit{#1} (Section~\\ref{%spkg:#1})}", pkg.getName(), pkg.getName());
        box.add("%%\\newcommand{\\%sclass}[1]{\\textit{#1} (Section~\\ref{%sclass:#1})}", pkg.getName(), pkg.getName());
        box.add("%%\\newcommand{\\%sattr}[1]{\\textit{#1} (Section~\\ref{%sattr:#1})}", pkg.getName(), pkg.getName());
        box.add("%%\\newcommand{\\%senumlit}[1]{\\textit{#1} (Section~\\ref{%senumlit:#1})}", pkg.getName(),
                pkg.getName());
        box.add("%%\\newcommand{\\%sclassDetail}[7]{\\item {#1} \\textbf{#2} {#3} :", pkg.getName());
        box.add("%    {\\textit{#4}} {#5} {#6} \\hfill \\\\ {#7}}");
        box.add("%%\\newcommand{\\%senumlitDetail}[4]{\\item literal \\textbf{#1} {#2} {#3}", pkg.getName());
        box.add("%    \\hfill \\\\ {#4}}");
        box.add("%\\newcommand{\\hook}{\\hspace*{5pt}\\raisebox{2.5pt}{$\\llcorner$}}");
        box.add("%\\newcommand{\\hookindent}{\\hspace*{15pt}}");
        box.add();

        // Generate code for package.
        genEPackage(pkg, box);

        // Return generated code.
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
        box.add("\\section{Package %s}\\label{%spkg:%s}", pkg.getName(), getRootPackageName(pkg), pkg.getName());
        box.add("\\pkgdocu" + pkg.getName());
        box.add();

        box.add("\\begin{description}");
        box.add("\\item[Package URI] %s", pkg.getNsURI());
        box.add("\\item[Namespace prefix] %s", pkg.getNsPrefix());
        if (pkg.getESubpackages().isEmpty()) {
            box.add("\\item[Sub-packages] none");
        } else {
            List<String> lst1 = list();
            for (EPackage subPkg: pkg.getESubpackages()) {
                lst1.add(getEPackageRef(subPkg));
            }
            box.add("\\item[Sub-packages] " + String.join(", ", lst1));
        }
        box.add("\\end{description}");
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
     * Generates a LaTeX documentation command for the given {@link EPackage}.
     *
     * @param pkg The {@link EPackage} to generate the LaTeX command for.
     * @return The LaTeX command.
     */
    private static String getEPackageRef(EPackage pkg) {
        return fmt("\\%spkg{%s}", getRootPackageName(pkg), pkg.getName());
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EDataType}.
     *
     * @param dt The {@link EDataType} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEDataType(EDataType dt, CodeBox box) {
        genEClassifier(dt, box);
        box.add("\\dtypedocu%s", dt.getName());
        box.add();
        String name = getDataTypeDetail(dt, "name");
        String baseType = getDataTypeDetail(dt, "baseType");
        String pattern = getDataTypeDetail(dt, "pattern");

        box.add("\\begin{description}");
        if (name != null) {
            box.add("\\item[Name] %s", name);
        }
        box.add("\\item[Instance class name] %s", dt.getInstanceClassName());
        if (baseType != null) {
            baseType = baseType.replace("#", "\\#");
            box.add("\\item[Basetype] %s", baseType);
        }
        if (pattern != null) {
            box.add("\\item[Pattern] \\verb@%s@", pattern);
        }
        box.add("\\end{description}");
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
        box.add("\\enumdocu%s", e.getName());
        box.add();
        List<EEnumLiteral> lits = getLiterals(e);
        if (!lits.isEmpty()) {
            box.add("\\begin{description}");
            for (EEnumLiteral lit: lits) {
                genELiteral(lit, e, box);
            }
            box.add("\\end{description}");
        }
        box.add();
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
        box.add("\\clsdocu%s", cls.getName());
        box.add();

        box.add("~\\\\ \\noindent \\emph{EObject} \\\\");
        genInheritanceHierarchy(cls, box, getInheritanceHierarchyDepth(cls), true);
        box.add();

        box.add("~\\\\ \\noindent Direct derived classes:");
        List<EClass> classes = getClasses(getRootPackage(cls), true);
        List<EClass> deriveds = getDerivedClasses(cls, classes);
        if (deriveds.isEmpty()) {
            box.add("none");
        } else {
            List<String> derivedTxts = list();
            for (EClass derived: deriveds) {
                derivedTxts.add(fmt("\\%sclass{%s}", getRootPackageName(derived), derived.getName()));
            }
            box.add(String.join(", ", derivedTxts));
        }
        box.add();

        if (!cls.getEAllStructuralFeatures().isEmpty()) {
            box.add("\\begin{description}");
            genEStructuralFeatures(cls, cls, box);
            box.add("\\end{description}");
        }
        box.add();
        box.add();
    }

    /**
     * Returns the inheritance hierarchy depth of the given class. For instance, assume an {@link EClass} {@code B} that
     * extends an {@link EClass} {@code A}, which does not extend any {@link EClass}. The depth of {@code B} is
     * {@code 1}, and the depth of {@code A} is {@code 0}.
     *
     * <p>
     * Supports only single inheritance.
     * </p>
     *
     * @param cls The class to return the inheritance hierarchy depth for.
     * @return The inheritance hierarchy depth.
     */
    protected static int getInheritanceHierarchyDepth(EClass cls) {
        return (cls.getESuperTypes().size() == 0) ? 0 : 1 + getInheritanceHierarchyDepth(first(cls.getESuperTypes()));
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EClassifier}.
     *
     * @param cls The {@link EClassifier} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEClassifier(EClassifier cls, CodeBox box) {
        box.add("\\subsection{%s (%s)}\\label{%sclass:%s}", cls.getName(), getClassifierTypeName(cls),
                getRootPackageName(cls), cls.getName());
    }

    /**
     * Generates a LaTeX documentation class hierarchy for the given {@link EClass}.
     *
     * @param cls The {@link EClass} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     * @param indent Indentation level.
     * @param last Whether this class is the last one in the hierarchy.
     */
    private static void genInheritanceHierarchy(EClass cls, CodeBox box, int indent, boolean last) {
        if (!cls.getESuperTypes().isEmpty()) {
            Assert.check(cls.getESuperTypes().size() == 1);
            genInheritanceHierarchy(first(cls.getESuperTypes()), box, indent - 1, false);
        }

        String text = getIndentText(indent);

        if (last) {
            box.add(text + "\\hook~\\emph{%s}", cls.getName());
        } else {
            box.add(text + "\\hook~\\%sclass{%s} \\\\", getRootPackageName(cls), cls.getName());
        }
    }

    /**
     * Returns a string with {@code indent} types the {@code "\hookindent"} text.
     *
     * @param indent Indentation level.
     * @return The newly created indentation string.
     */
    private static String getIndentText(int indent) {
        Assert.check(indent >= 0);
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            b.append("\\hookindent");
        }
        return b.toString();
    }

    /**
     * Generates a LaTeX documentation skeleton for the structural features of the given {@link EClass}.
     *
     * @param cls The {@link EClass} to generate the LaTeX code for. This is either {@code realClass} or one its super
     *     classes.
     * @param realClass The {@link EClass} that we are actually generating code from.
     * @param box The generated code (appended in-place).
     */
    private static void genEStructuralFeatures(EClass cls, EClass realClass, CodeBox box) {
        for (EClass superClass: cls.getESuperTypes()) {
            genEStructuralFeatures(superClass, realClass, box);
        }
        for (EStructuralFeature feat: getStructuralFeatures(cls)) {
            genEStructuralFeature(realClass, cls, feat, box);
        }
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EStructuralFeature}.
     *
     * @param realClass The {@link EClass} we are actually generating the LaTeX code for.
     * @param actualClass The {@link EClass} in which the structural feature is actually defined. Is either
     *     {@code realClass} or one of its super classes.
     * @param feat The {@link EStructuralFeature} to generate the LaTeX code for.
     * @param box The generated code (appended in-place).
     */
    private static void genEStructuralFeature(EClass realClass, EClass actualClass, EStructuralFeature feat,
            CodeBox box)
    {
        String line = fmt("\\%sclassDetail{%s}{%s}{{[%s]}}{%s}", getRootPackage(feat.getEContainingClass()).getName(),
                getStructFeatKindName(feat), feat.getName(), getBoundsText(feat), feat.getEType().getName());
        line += "{";
        if (actualClass != realClass) {
            line += fmt("(inherited from \\textit{%s})", actualClass.getName());
        }

        box.add("%s}{\\label{%sattr:%s.%s}}", line, getRootPackageName(feat.getEContainingClass()), realClass.getName(),
                feat.getName());
        box.add("{\\featdocu%s%s}", actualClass.getName(), feat.getName());
    }

    /**
     * Generates a LaTeX documentation skeleton for the given {@link EEnumLiteral}.
     *
     * @param lit The {@link EEnumLiteral} to generate the LaTeX code for.
     * @param e The {@link EEnum} that the literal is a part of.
     * @param box The generated code (appended in-place).
     */
    private static void genELiteral(EEnumLiteral lit, EEnum e, CodeBox box) {
        box.add("\\%senumlitDetail{%s}{%s}{\\label{%senumlit:%s.%s}}{\\enumlitdocu%s%s}", getRootPackageName(e),
                lit.getName(), (lit == getEnumDefaultLiteral(e)) ? "(default)" : "", getRootPackageName(e), e.getName(),
                lit.getName(), e.getName(), nameToLatex(lit.getName()));
    }
}
