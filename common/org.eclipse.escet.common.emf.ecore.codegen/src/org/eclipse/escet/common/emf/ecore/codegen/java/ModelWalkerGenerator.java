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

package org.eclipse.escet.common.emf.ecore.codegen.java;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.JavaCodeUtils;

/**
 * Java model walker class generator for Ecore models.
 *
 * <p>
 * Generates:
 * <ul>
 * <li>Model walkers that walks over the model, providing various methods to override and operate on the model.</li>
 * <li>Composite model walkers. They support combining multiple model walkers. This allows one to separate a large model
 * walker into separate smaller, simpler, easier-to-maintain model walkers and combining them to functionally obtain
 * back the original larger model walker.</li>
 * <li>Model walkers and composite model walkers where the methods have an extra argument of some specified type,
 * allowing to pass along some object while walking over the models.</li>
 * </ul>
 * </p>
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
public class ModelWalkerGenerator extends EmfJavaCodeGenerator {
    /**
     * Generate a model walker and composite model walker for an {@link EPackage}, including all its sub-packages
     * (recursively). Note that structural features that have types outside of the supplied package result in the
     * exploration of additional packages.
     *
     * @param args Application arguments:
     *     <ul>
     *     <li>The full name of the main {@link EPackage} Java class. For instance, {@code
     *           "bla.somelang.metamodel.somelang.SomeLangPackage"}.</li>
     *     <li>The path to add to the Java class path to allow the main package Java class to be found. For instance,
     *     {@code "bin"}. It is allowed to add multiple paths. For instance, {@code "bin|../bin"}.</li>
     *     <li>The output directory path. For instance, {@code "src/bla/somelang/metamodel/java"}.</li>
     *     <li>The model walker output class name. For instance, {@code "SomeLangWalker"}.</li>
     *     <li>The composite model walker output class name. For instance, {@code "CompositeSomeLangWalker"}.</li>
     *     <li>The extra-argument model walker output class name. For instance, {@code "SomeLangWithArgWalker"}.</li>
     *     <li>The extra-argument composite model walker output class name. For instance,
     *     {@code "CompositeSomeLangWithArgWalker"}.</li>
     *     <li>The output package name. For instance, {@code "bla.somelang.metamodel.java"}.</li>
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
        Assert.check(args.length == 8, args.length);
        String mainPkgClassName = args[0];
        String binPath = args[1];
        String outputPath = args[2];
        String outputClassNameWalker = args[3];
        String outputClassNameComposite = args[4];
        String outputClassNameWalkerWithArg = args[5];
        String outputClassNameCompositeWithArg = args[6];
        String outputPackageName = args[7];

        // Resolve the package.
        EPackage mainPkg = loadEPackage(mainPkgClassName, binPath);

        // Generate code for the package.
        CodeBox boxWalker = generateClass(false, mainPkg, outputClassNameWalker, outputClassNameComposite,
                outputPackageName, false);
        CodeBox boxComposite = generateClass(true, mainPkg, outputClassNameWalker, outputClassNameComposite,
                outputPackageName, false);
        CodeBox boxWalkerWithArg = generateClass(false, mainPkg, outputClassNameWalkerWithArg,
                outputClassNameCompositeWithArg, outputPackageName, true);
        CodeBox boxCompositeWithArg = generateClass(true, mainPkg, outputClassNameWalkerWithArg,
                outputClassNameCompositeWithArg, outputPackageName, true);

        // Try to write the code to a file.
        String outputFilePathWalker = new File(new File(outputPath), outputClassNameWalker + ".java").getAbsolutePath();
        String outputFilePathComposite = new File(new File(outputPath), outputClassNameComposite + ".java")
                .getAbsolutePath();
        String outputFilePathWalkerWithArg = new File(new File(outputPath), outputClassNameWalkerWithArg + ".java")
                .getAbsolutePath();
        String outputFilePathCompositeWithArg = new File(new File(outputPath),
                outputClassNameCompositeWithArg + ".java").getAbsolutePath();
        boxWalker.writeToFile(outputFilePathWalker);
        boxComposite.writeToFile(outputFilePathComposite);
        boxWalkerWithArg.writeToFile(outputFilePathWalkerWithArg);
        boxCompositeWithArg.writeToFile(outputFilePathCompositeWithArg);

        // We are done.
        System.out.printf("Walker code written to: %s%n", outputFilePathWalker);
        System.out.printf("Composite walker code written to: %s%n", outputFilePathComposite);
        System.out.printf("Extra-argument walker code written to: %s%n", outputFilePathWalkerWithArg);
        System.out.printf("Extra-argument composite walker code written to: %s%n", outputFilePathCompositeWithArg);
    }

    /**
     * Generate (composite) model walker class code for an {@link EPackage}.
     *
     * @param genComposite Whether to generate a composite model walker ({@code true}) or a model walker
     *     ({@code false}).
     * @param startPackage The {@link EPackage} to generate the code for.
     * @param genClassNameWalker The name of the model walker Java class to generate.
     * @param genClassNameComposite The name of the composite model walker Java class to generate.
     * @param genPackageName The name of the package that the generated Java class will be a part of.
     * @param withArg Whether to generate an extra-argument walker ({@code true}) or a regular walker ({@code false}).
     * @return The generated code.
     */
    private static CodeBox generateClass(boolean genComposite, EPackage startPackage, String genClassNameWalker,
            String genClassNameComposite, String genPackageName, boolean withArg)
    {
        // Initialize the code.
        CodeBox box = new MemoryCodeBox();

        // Collect the classes and packages to consider.
        List<EPackage> packages = list(startPackage);
        List<EClass> classes;
        while (true) {
            boolean done = true;
            Set<EClass> classSet = set();

            // Get all classes for the packages and their sub-packages.
            for (EPackage pkg: packages) {
                classSet.addAll(getClasses(pkg, true));
            }

            // Add all classes that are used as types of the features.
            Set<EClass> classSet2 = copy(classSet);
            for (EClass cls: classSet2) {
                for (EStructuralFeature feat: cls.getEAllStructuralFeatures()) {
                    if (feat.getEType() instanceof EClass) {
                        classSet.add((EClass)feat.getEType());
                    }
                }
            }

            // From the classes, get the root packages. If we have any new
            // ones, we are not yet done, as we need another iteration.
            for (EClass cls: classSet) {
                EPackage newPackage = getRootPackage(cls);
                if (!packages.contains(newPackage)) {
                    packages.add(newPackage);
                    done = false;
                }
            }

            // If we are done, save the classes.
            if (done) {
                classes = sortedgeneric(classSet, new EClassSorter());
                break;
            }
        }

        // Add the file header.
        box.add("//////////////////////////////////////////////////////////////////////////////");
        box.add("// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation");
        box.add("//");
        box.add("// See the NOTICE file(s) distributed with this work for additional");
        box.add("// information regarding copyright ownership.");
        box.add("//");
        box.add("// This program and the accompanying materials are made available");
        box.add("// under the terms of the MIT License which is available at");
        box.add("// https://opensource.org/licenses/MIT");
        box.add("//");
        box.add("// SPDX-License-Identifier: MIT");
        box.add("//////////////////////////////////////////////////////////////////////////////");
        box.add();
        box.add("// Generated using the \"org.eclipse.escet.common.emf.ecore.codegen\" project.");
        box.add();
        box.add("// Disable Eclipse Java formatter for generated code file:");
        box.add("// @formatter:off");
        box.add();
        box.add("package %s;", genPackageName);
        box.add();

        // Add the imports.
        List<String> imports = listc(classes.size());
        if (!genComposite) {
            imports.add("java.util.List");
            imports.add("org.eclipse.escet.common.java.Assert");
        }

        for (EClass impClass: classes) {
            imports.add(getJavaTypeName(impClass));
            if (!genComposite && !impClass.isAbstract()) {
                if (!getDerivedClasses(impClass, classes).isEmpty()) {
                    imports.add(getImplClassName(impClass));
                }
            }
        }

        for (String imp: JavaCodeUtils.formatImports(imports, genPackageName)) {
            box.add(imp);
        }
        box.add();

        // Add the class JavaDoc.
        box.add("/**");
        box.add(" * A %swalker for \"%s\" models.", genComposite ? "composite " : "", startPackage.getName());
        box.add(" *");
        box.add(" * <p>The %swalker works as follows:", genComposite ? "composite " : "");
        box.add(" * <ul>");
        box.add(" *  <li>Performs a top-down depth-first walk over the object tree, using the");
        box.add(" *   containment hierarchy.</li>");
        box.add(" *  <li>No particular order should be assumed for walking over the child");
        box.add(" *    features.</li>");
        box.add(" *  <li>For each object encountered, pre-processing is performed before walking");
        box.add(" *    over the children, and post-processing is performed after walking over");
        box.add(" *    the children.</li>");
        box.add(" *  <li>Pre-processing for objects is done by crawling up the inheritance");
        box.add(" *    hierarchy of the object, performing pre-processing for each of the");
        box.add(" *    types encountered in the type hierarchy. The pre-processing methods are");
        box.add(" *    invoked from most general to most specific class (super classes before");
        box.add(" *    base classes).</li>");
        box.add(" *  <li>Post-processing for objects is done by crawling up the inheritance");
        box.add(" *    hierarchy of the object, performing post-processing for each of the");
        box.add(" *    types encountered in the type hierarchy. The post-processing methods are");
        box.add(" *    invoked from most specific to most general class (base classes before");
        box.add(" *    super classes).</li>");
        box.add(" * </ul>");
        box.add(" * </p>");
        box.add(" *");
        if (genComposite) {
            box.add(" * <p>A composite walking does not perform any pre-processing or post-processing");
            box.add(" * by itself. It can be configured with any number of other (composite or");
            box.add(" * non-composite) model walkers. As pre-processing and post-processing the");
            box.add(" * composite model walker invokes pre-processing and post-processing on each");
            box.add(" * 'other' model walker, in the order they are supplied. The walking and");
            box.add(" * crawling methods of the non-composite of the 'other' model walkers are not");
            box.add(" * used.</p>");
        } else {
            box.add(" * <p>By default, the pre-processing and post-processing methods do nothing");
            box.add(" * (they have an empty implementation). It is up to derived classes to");
            box.add(" * override methods and provide actual implementations. They may also override");
            box.add(" * walk and crawl methods, if desired.</p>");
        }
        box.add(" *");
        box.add(" * <p>This abstract walker class has no public methods. It is up to the derived");
        box.add(" * classes to add a public method as entry method. They can decide which");
        box.add(" * classes are to be used as starting point, and they can give the public");
        box.add(" * method a proper name, parameters, etc. They may even allow multiple public");
        box.add(" * methods to allow starting from multiple classes.</p>");
        if (withArg) {
            box.add(" *");
            box.add(" * @param <T> The type of the extra argument provided to the walking, crawling");
            box.add(" *     and processing methods.");
        }
        box.add(" */");

        // Add the class header.
        if (genComposite) {
            box.add("public abstract class %s%s extends %s%s {", genClassNameComposite, withArg ? "<T>" : "",
                    genClassNameWalker, withArg ? "<T>" : "");
        } else {
            box.add("public abstract class %s%s {", genClassNameWalker, withArg ? "<T>" : "");
        }

        // Add the class body.
        if (genComposite) {
            // Add fields.
            box.indent();
            box.add("/** The walkers to be composed by this composite walker. */");
            box.add("private final %s%s[] walkers;", genClassNameWalker, withArg ? "<T>" : "");
            box.dedent();
            box.add();

            // Add constructor.
            box.indent();
            box.add("/**");
            box.add(" * Constructor of the {@link %s} class.", genClassNameComposite);
            box.add(" *");
            box.add(" * @param walkers The walkers to be composed by this composite walker.");
            box.add(" */");
            box.add("public %s(%s%s[] walkers) {", genClassNameComposite, genClassNameWalker, withArg ? "<T>" : "");
            box.add("    this.walkers = walkers;");
            box.add("}");
            box.dedent();

            // Add methods.
            for (EClass cls: classes) {
                generateCompositeWalkerMethods(cls, genClassNameWalker, withArg, box);
            }
        } else {
            // Add methods.
            boolean first = true;
            for (EClass cls: classes) {
                if (!first) {
                    box.add();
                }
                generateWalkerMethods(cls, classes, withArg, box);
                first = false;
            }
        }

        // Close the class.
        box.add("}");
        return box;
    }

    /**
     * Generate model walker methods code for an {@link EClass}.
     *
     * @param cls The {@link EClass} to generate the code for.
     * @param classes The candidate derived classes for the given 'cls'.
     * @param withArg Whether to generate an extra-argument methods ({@code true}) or regular methods ({@code false}).
     * @param box The generated code (appended in-place).
     */
    private static void generateWalkerMethods(EClass cls, List<EClass> classes, boolean withArg, CodeBox box) {
        List<EClass> derivedClasses = getDerivedClasses(cls, classes);

        // Walking function.
        box.indent();
        box.add("/**");
        box.add(" * Walking function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to walk over.");
        if (withArg) {
            box.add(" * @param arg The extra argument provided to the walking method.");
        }
        box.add(" */");
        box.add("protected void walk%s(%s obj%s) {", cls.getName(), cls.getName(), withArg ? ", T arg" : "");
        box.indent();

        if (derivedClasses.size() > 0) {
            for (EClass derived: derivedClasses) {
                box.add("if (obj instanceof %s) {", derived.getName());

                box.indent();
                box.add("walk%s((%s)obj%s);", derived.getName(), derived.getName(), withArg ? ", arg" : "");
                box.add("return;");
                box.dedent();

                box.add("}");
            }
            if (cls.isAbstract()) {
                box.add("String msg = \"No redirect; unexpected object type: \" + obj;");
                box.add("throw new IllegalArgumentException(msg);");
            }
        }
        if (!cls.isAbstract()) {
            if (derivedClasses.size() > 0) {
                box.add("Assert.check(obj.getClass() == %s.class);", getImplClassSimpleName(cls));
            }
            box.add("precrawl%s(obj%s);", cls.getName(), withArg ? ", arg" : "");
            List<EReference> feats = getAllContainmentStructuralFeatures(cls);
            for (EReference feat: feats) {
                String methodName = feat.getName();
                methodName = "get" + methodName.substring(0, 1).toUpperCase(Locale.US) + methodName.substring(1);

                if (feat.isMany()) {
                    // Case: [?..(>1)]
                    box.add("List<%s> _%s = obj.%s();", feat.getEReferenceType().getName(), feat.getName(), methodName);
                    box.add("for (%s x: _%s) {", feat.getEReferenceType().getName(), feat.getName());
                    box.indent();
                    box.add("walk%s(x%s);", feat.getEReferenceType().getName(), withArg ? ", arg" : "");
                    box.dedent();
                    box.add("}");
                } else {
                    // Case: [?..1]
                    Assert.check(feat.getUpperBound() == 1);
                    box.add("%s _%s = obj.%s();", feat.getEReferenceType().getName(), feat.getName(), methodName);
                    if (feat.getLowerBound() == 0) {
                        // Case: [0..1]
                        box.add("if (_%s != null) {", feat.getName());
                        box.indent();
                        box.add("walk%s(_%s%s);", feat.getEReferenceType().getName(), feat.getName(),
                                withArg ? ", arg" : "");
                        box.dedent();
                        box.add("}");
                    } else {
                        // Case: [1]
                        Assert.check(feat.getLowerBound() == 1);
                        box.add("walk%s(_%s%s);", feat.getEReferenceType().getName(), feat.getName(),
                                withArg ? ", arg" : "");
                    }
                }
            }
            box.add("postcrawl%s(obj%s);", cls.getName(), withArg ? ", arg" : "");
        }
        box.dedent();

        box.add("}");
        box.add();

        // Get super classes.
        List<EClass> superClasses = list();
        superClasses.addAll(cls.getESuperTypes());
        Collections.sort(superClasses, new EClassSorter());

        // Pre-crawling function.
        box.add("/**");
        box.add(" * Pre-crawling function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to crawl over.");
        if (withArg) {
            box.add(" * @param arg The extra argument provided to the pre-crawling method.");
        }
        box.add(" */");
        box.add("protected void precrawl%s(%s obj%s) {", cls.getName(), cls.getName(), withArg ? ", T arg" : "");

        box.indent();
        for (EClass superClass: superClasses) {
            box.add("precrawl%s(obj%s);", superClass.getName(), withArg ? ", arg" : "");
        }
        box.add("preprocess%s(obj%s);", cls.getName(), withArg ? ", arg" : "");
        box.dedent();

        box.add("}");
        box.add();

        // Post-crawling function.
        box.add("/**");
        box.add(" * Post-crawling function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to crawl over.");
        if (withArg) {
            box.add(" * @param arg The extra argument provided to the post-crawling method.");
        }
        box.add(" */");
        box.add("protected void postcrawl%s(%s obj%s) {", cls.getName(), cls.getName(), withArg ? ", T arg" : "");

        box.indent();
        box.add("postprocess%s(obj%s);", cls.getName(), withArg ? ", arg" : "");
        for (EClass superClass: superClasses) {
            box.add("postcrawl%s(obj%s);", superClass.getName(), withArg ? ", arg" : "");
        }
        box.dedent();

        box.add("}");
        box.add();

        // Pre-processing function.
        box.add("/**");
        box.add(" * Pre-processing function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to pre-process.");
        if (withArg) {
            box.add(" * @param arg The extra argument provided to the pre-processing method.");
        }
        box.add(" */");
        box.add("protected void preprocess%s(%s obj%s) {", cls.getName(), cls.getName(), withArg ? ", T arg" : "");
        box.add("    // Derived classes may override this method to do actual processing.");
        box.add("}");
        box.add();

        // Post-processing function.
        box.add("/**");
        box.add(" * Post-processing function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to post-process.");
        if (withArg) {
            box.add(" * @param arg The extra argument provided to the post-processing method.");
        }
        box.add(" */");
        box.add("protected void postprocess%s(%s obj%s) {", cls.getName(), cls.getName(), withArg ? ", T arg" : "");
        box.add("    // Derived classes may override this method to do actual processing.");
        box.add("}");
        box.dedent();
    }

    /**
     * Generate composite model walker methods code for an {@link EClass}.
     *
     * @param cls The {@link EClass} to generate the code for.
     * @param genClassNameWalker The name of the model walker Java class to generate.
     * @param withArg Whether to generate an extra-argument methods ({@code true}) or regular methods ({@code false}).
     * @param box The generated code (appended in-place).
     */
    private static void generateCompositeWalkerMethods(EClass cls, String genClassNameWalker, boolean withArg,
            CodeBox box)
    {
        // Pre-processing function.
        box.add();
        box.indent();
        box.add("@Override");
        box.add("protected void preprocess%s(%s obj%s) {", cls.getName(), cls.getName(), withArg ? ", T arg" : "");
        box.add("    for (%s%s walker: walkers) {", genClassNameWalker, withArg ? "<T>" : "");
        box.add("        walker.preprocess%s(obj%s);", cls.getName(), withArg ? ", arg" : "");
        box.add("    }");
        box.add("}");
        box.dedent();

        // Post-processing function.
        box.add();
        box.indent();
        box.add("@Override");
        box.add("protected void postprocess%s(%s obj%s) {", cls.getName(), cls.getName(), withArg ? ", T arg" : "");
        box.add("    for (%s%s walker: walkers) {", genClassNameWalker, withArg ? "<T>" : "");
        box.add("        walker.postprocess%s(obj%s);", cls.getName(), withArg ? ", arg" : "");
        box.add("    }");
        box.add("}");
        box.dedent();
    }
}
