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
     * Generate model walker class code for an {@link EPackage}, including all its sub-packages (recursively). Note that
     * structural features that have types outside of the supplied package result in the exploration of additional
     * packages.
     *
     * @param args Application arguments:
     *     <ul>
     *     <li>The full name of the main {@link EPackage} Java class. For instance, {@code
     *           "bla.somelang.v1x0x0.metamodel.somelang.SomeLangPackage"}.</li>
     *     <li>The path to add to the Java class path to allow the main package Java class to be found. For instance,
     *     {@code "bin"}. It is allowed to add multiple paths. For instance, {@code "bin|../bin"}.</li>
     *     <li>The output file name path. For instance, {@code "src/bla/somelang/v1x0x0/metamodel/java"}.</li>
     *     <li>The output class name. For instance, {@code "SomeLangWalker"}.</li>
     *     <li>The output package name. For instance, {@code "bla.somelang.v1x0x0.metamodel.java"}.</li>
     *     <li>The name of the {@link EClass} that is the root of all instances of the metamodel.</li>
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
        Assert.check(args.length == 6);
        String mainPkgClassName = args[0];
        String binPath = args[1];
        String outputPath = args[2];
        String outputClassName = args[3];
        String outputPackageName = args[4];
        String rootClassName = args[5];

        // Resolve the package.
        EPackage mainPkg = loadEPackage(mainPkgClassName, binPath);

        // Generate code for the package.
        CodeBox box = generate(mainPkg, rootClassName, outputClassName, outputPackageName);

        // Try to write the code to a file.
        String outputFilePath = new File(new File(outputPath), outputClassName + ".java").getAbsolutePath();
        box.writeToFile(outputFilePath);

        // We are done.
        System.out.print("Code written to: " + outputFilePath);
    }

    /**
     * Generate model walker class code for an {@link EPackage}.
     *
     * @param startPackage The {@link EPackage} to generate the code for.
     * @param rootClassName The name of the {@link EClass} that is the root of all instances of the metamodel.
     * @param genClassName The name of the Java class to generate.
     * @param genPackageName The name of the package that the generated Java class will be a part of.
     * @return The generated code.
     */
    private static CodeBox generate(EPackage startPackage, String rootClassName, String genClassName,
            String genPackageName)
    {
        CodeBox box = new MemoryCodeBox();

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

        box.add("//////////////////////////////////////////////////////////////////////////////");
        box.add("// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation");
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

        List<String> imports = listc(classes.size());
        imports.add("java.util.List");
        imports.add("org.eclipse.escet.common.java.Assert");

        for (EClass impClass: classes) {
            imports.add(getJavaTypeName(impClass));
            if (!impClass.isAbstract()) {
                if (!getDerivedClasses(impClass, classes).isEmpty()) {
                    imports.add(getImplClassName(impClass));
                }
            }
        }

        for (String imp: JavaCodeUtils.formatImports(imports, genPackageName)) {
            box.add(imp);
        }
        box.add();

        box.add("/**");
        box.add(" * A walker for \"%s\" models.", startPackage.getName());
        box.add(" *");
        box.add(" * <p>The walker works as follows:");
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
        box.add(" * <p>By default, the pre-processing and post-processing methods do nothing");
        box.add(" * (they have an empty implementation). It is up to derived classes to");
        box.add(" * override methods and provide actual implementations. They may also override");
        box.add(" * walk and crawl methods, if desired.</p>");
        box.add(" *");
        box.add(" * <p>This abstract walker class has no public methods. It is up to the derived");
        box.add(" * classes to add a public method as entry method. They can decide which");
        box.add(" * classes are to be used as starting point, and they can give the public");
        box.add(" * method a proper name, parameters, etc. They may even allow multiple public");
        box.add(" * methods to allow starting from multiple classes.</p>");
        box.add(" */");
        box.add("public abstract class %s {", genClassName);
        boolean first = true;
        for (EClass cls: classes) {
            if (!first) {
                box.add();
            }
            genModelWalkerClass(cls, classes, box);
            first = false;
        }
        box.add("}");
        return box;
    }

    /**
     * Generate model walker class code for an {@link EClass}.
     *
     * @param cls The {@link EClass} to generate the code for.
     * @param classes The candidate derived classes for the given 'cls'.
     * @param box The generated code (appended in-place).
     */
    private static void genModelWalkerClass(EClass cls, List<EClass> classes, CodeBox box) {
        List<EClass> derivedClasses = getDerivedClasses(cls, classes);

        // Walking function.
        box.indent();
        box.add("/**");
        box.add(" * Walking function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to walk over.");
        box.add(" */");
        box.add("protected void walk%s(%s obj) {", cls.getName(), cls.getName());
        box.indent();

        if (derivedClasses.size() > 0) {
            for (EClass derived: derivedClasses) {
                box.add("if (obj instanceof %s) {", derived.getName());

                box.indent();
                box.add("walk%s((%s)obj);", derived.getName(), derived.getName());
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
            box.add("precrawl%s(obj);", cls.getName());
            List<EReference> feats = getAllContainmentStructuralFeatures(cls);
            for (EReference feat: feats) {
                String methodName = feat.getName();
                methodName = "get" + methodName.substring(0, 1).toUpperCase(Locale.US) + methodName.substring(1);

                if (feat.isMany()) {
                    // Case: [?..(>1)]
                    box.add("List<%s> _%s = obj.%s();", feat.getEReferenceType().getName(), feat.getName(), methodName);
                    box.add("for (%s x: _%s) {", feat.getEReferenceType().getName(), feat.getName());
                    box.indent();
                    box.add("walk%s(x);", feat.getEReferenceType().getName());
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
                        box.add("walk%s(_%s);", feat.getEReferenceType().getName(), feat.getName());
                        box.dedent();
                        box.add("}");
                    } else {
                        // Case: [1]
                        Assert.check(feat.getLowerBound() == 1);
                        box.add("walk%s(_%s);", feat.getEReferenceType().getName(), feat.getName());
                    }
                }
            }
            box.add("postcrawl%s(obj);", cls.getName());
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
        box.add(" */");
        box.add("protected void precrawl%s(%s obj) {", cls.getName(), cls.getName());

        box.indent();
        for (EClass superClass: superClasses) {
            box.add("precrawl%s(obj);", superClass.getName());
        }
        box.add("preprocess%s(obj);", cls.getName());
        box.dedent();

        box.add("}");
        box.add();

        // Post-crawling function.
        box.add("/**");
        box.add(" * Post-crawling function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to crawl over.");
        box.add(" */");
        box.add("protected void postcrawl%s(%s obj) {", cls.getName(), cls.getName());

        box.indent();
        box.add("postprocess%s(obj);", cls.getName());
        for (EClass superClass: superClasses) {
            box.add("postcrawl%s(obj);", superClass.getName());
        }
        box.dedent();

        box.add("}");
        box.add();

        // Pre-processing function.
        box.add("/**");
        box.add(" * Pre-processing function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to pre-process.");
        box.add(" */");
        box.add("protected void preprocess%s(%s obj) {", cls.getName(), cls.getName());
        box.add("    // Derived classes may override this method to do actual processing.");
        box.add("}");
        box.add();

        // Post-processing function.
        box.add("/**");
        box.add(" * Post-processing function for the {@link %s} class.", cls.getName());
        box.add(" *");
        box.add(" * @param obj The object to post-process.");
        box.add(" */");
        box.add("protected void postprocess%s(%s obj) {", cls.getName(), cls.getName());
        box.add("    // Derived classes may override this method to do actual processing.");
        box.add("}");
        box.dedent();
    }
}
