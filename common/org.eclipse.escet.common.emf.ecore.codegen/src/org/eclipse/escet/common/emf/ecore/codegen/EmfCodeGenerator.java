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

package org.eclipse.escet.common.emf.ecore.codegen;

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/** Base class for EMF code generators, with useful helper methods. */
public abstract class EmfCodeGenerator {
    /**
     * Loads an {@link EPackage} via reflection.
     *
     * @param pkgClassName The full name of the {@link EPackage} Java class. For instance, {@code
     *      "bla.somelang.v1x0x0.metamodel.somelang.SomeLangPackage"}.
     * @param path The path to add to the Java class path to allow the main package Java class to be found. For
     *     instance, {@code "bin"}. It is allowed to add multiple paths. For instance, {@code "bin|../bin"}.
     * @return The loaded {@link EPackage}.
     *
     * @throws MalformedURLException If the {@code path} is invalid.
     * @throws ClassNotFoundException If the class can not be found.
     * @throws NoSuchFieldException If the {@code eINSTANCE} field is not found in the class.
     * @throws IllegalAccessException If the {@code eINSTANCE} field is inaccessible.
     */
    protected static EPackage loadEPackage(String pkgClassName, String path)
            throws MalformedURLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException
    {
        // Get class path extensions.
        String[] paths = StringUtils.split(path, '|');
        URL[] cpExtPaths = new URL[paths.length];
        for (int i = 0; i < paths.length; i++) {
            File cpExtPath = new File(paths[i]).getAbsoluteFile();
            cpExtPaths[i] = cpExtPath.toURI().toURL();

            // Make sure the path exists.
            if (!cpExtPath.exists()) {
                String msg = "Invalid class path entry: " + cpExtPath;
                throw new IllegalArgumentException(msg);
            }
        }

        // Construct derived class loader.
        ClassLoader loader1 = ClassLoader.getSystemClassLoader();
        @SuppressWarnings("resource")
        URLClassLoader loader2 = new URLClassLoader(cpExtPaths, loader1);

        // Load class, and its eINSTANCE field, which is the EPackage.
        Class<?> mainPkgClass = loader2.loadClass(pkgClassName);
        Field instField = mainPkgClass.getDeclaredField("eINSTANCE");
        EPackage pkg = (EPackage)instField.get(null);
        return pkg;
    }

    /**
     * Returns the name of the root package of the package in which the given classifier is defined.
     *
     * @param cls The classifier to return the result for.
     * @return The name of the root package.
     */
    protected static String getRootPackageName(EClassifier cls) {
        return getRootPackageName(cls.getEPackage());
    }

    /**
     * Returns the name of the root package of the given package.
     *
     * @param pkg The package to return the result for.
     * @return The name of the root package.
     */
    protected static String getRootPackageName(EPackage pkg) {
        return getRootPackage(pkg).getName();
    }

    /**
     * Returns the root package of the package in which the given classifier is defined.
     *
     * @param cls The classifier to return the result for.
     * @return The root package.
     */
    protected static EPackage getRootPackage(EClassifier cls) {
        return getRootPackage(cls.getEPackage());
    }

    /**
     * Returns the root package of the given package.
     *
     * @param pkg The package to return the result for.
     * @return The root package.
     */
    protected static EPackage getRootPackage(EPackage pkg) {
        return (pkg.getESuperPackage() == null) ? pkg : getRootPackage(pkg.getESuperPackage());
    }

    /**
     * Returns the {@link EFactory EFactories} associated with the given package, and all of its sub-packages
     * (recursively).
     *
     * @param pkg The {@link EPackage} at the root of the package hierarchy for which to add all the {@link EFactory
     *     EFactories}.
     * @return The {@link EFactory EFactories} that were found.
     */
    protected static List<EFactory> getFactories(EPackage pkg) {
        List<EFactory> facts = list();
        List<EPackage> pkgs = list();
        pkgs.add(pkg);
        while (!pkgs.isEmpty()) {
            EPackage p = pkgs.remove(0);
            pkgs.addAll(p.getESubpackages());
            facts.add(p.getEFactoryInstance());
        }
        return facts;
    }

    /**
     * Returns the direct derived classes for the given {@link EClass}. The resulting list is sorted on class names.
     *
     * @param cls The {@link EClass} to return the direct derived classes for.
     * @param classes The candidate derived classes.
     * @return The direct derived classes.
     */
    protected static List<EClass> getDerivedClasses(EClass cls, List<EClass> classes) {
        List<EClass> rslt = list();
        for (EClass c: classes) {
            if (c.getESuperTypes().contains(cls)) {
                rslt.add(c);
            }
        }
        Collections.sort(rslt, new EClassSorter());
        return rslt;
    }

    /**
     * Returns the {@link EClass EClasses} that are part of the given {@link EPackage}, and optionally those part of any
     * sub-packages (recursively). The resulting list is sorted on class names.
     *
     * @param pkg The {@link EPackage} at the root of the package hierarchy for which to return all the defined classes.
     * @param recursive Whether to get the classes recursively.
     * @return The {@link EClass EClasses} that are part of the given {@link EPackage}.
     */
    protected static List<EClass> getClasses(EPackage pkg, boolean recursive) {
        List<EClass> classes = list();
        getClassesFromEPackage(pkg, classes, recursive);
        Collections.sort(classes, new EClassSorter());
        return classes;
    }

    /**
     * Fills the given list with the {@link EClass EClasses} that are part of the given {@link EPackage}, and optionally
     * those part of any sub-packages (recursively).
     *
     * @param pkg The {@link EPackage} at the root of the package hierarchy for which to add all the defined classes.
     * @param recursive Whether to get the classes recursively.
     * @param classes The list to which to add the {@link EClass EClasses} that are part of the given {@link EPackage}.
     */
    private static void getClassesFromEPackage(EPackage pkg, List<EClass> classes, boolean recursive) {
        if (recursive) {
            for (EPackage subpkg: pkg.getESubpackages()) {
                getClassesFromEPackage(subpkg, classes, recursive);
            }
        }
        classes.addAll(filter(pkg.getEClassifiers(), EClass.class));
    }

    /**
     * Returns the structural features of the given {@link EClass} (excluding all super classes), as a list sorted on
     * feature names.
     *
     * @param cls The {@link EClass} to return the structural features for.
     * @return The sorted list of structural features.
     */
    protected static List<EStructuralFeature> getStructuralFeatures(EClass cls) {
        List<EStructuralFeature> feats = copy(cls.getEStructuralFeatures());
        Collections.sort(feats, new EStructuralFeatureSorter());
        return feats;
    }

    /**
     * Returns all structural features of the given {@link EClass} (including all super classes, recursively), as a list
     * sorted on feature names.
     *
     * @param cls The {@link EClass} to return the structural features for.
     * @return The sorted list of structural features.
     */
    protected static List<EStructuralFeature> getAllStructuralFeatures(EClass cls) {
        List<EStructuralFeature> feats = copy(cls.getEAllStructuralFeatures());
        Collections.sort(feats, new EStructuralFeatureSorter());
        return feats;
    }

    /**
     * Returns all containment structural features of the given {@link EClass} (including all super classes,
     * recursively), as a list sorted on feature names.
     *
     * @param cls The {@link EClass} to return the containment structural features for.
     * @return The sorted list of containment structural features.
     */
    protected static List<EReference> getAllContainmentStructuralFeatures(EClass cls) {
        List<EReference> feats = list();
        for (EStructuralFeature feat: cls.getEAllStructuralFeatures()) {
            if (feat instanceof EReference && ((EReference)feat).isContainment()) {
                feats.add((EReference)feat);
            }
        }
        Collections.sort(feats, new EReferenceSorter());
        return feats;
    }

    /**
     * Returns the {@link EDataType data types} that are part of the given {@link EPackage} (non-recursive), excluding
     * enumerations, sorted on data type names.
     *
     * @param pkg The {@link EPackage} for which to return the data types.
     * @return The {@link EDataType data types} that are part of the given {@link EPackage}.
     */
    protected static List<EDataType> getDataTypes(EPackage pkg) {
        List<EDataType> dts = filter(pkg.getEClassifiers(), EDataType.class);
        Iterator<EDataType> iter = dts.iterator();
        while (iter.hasNext()) {
            EDataType dt = iter.next();
            if (dt instanceof EEnum) {
                iter.remove();
            }
        }
        Collections.sort(dts, new EDataTypeSorter());
        return dts;
    }

    /**
     * Returns the {@link EEnum enumerations} that are part of the given {@link EPackage} (non-recursive), sorted on
     * literal names.
     *
     * @param pkg The {@link EPackage} for which to return the enumerations.
     * @return The {@link EEnum enumerations} that are part of the given {@link EPackage}.
     */
    protected static List<EEnum> getEnums(EPackage pkg) {
        List<EEnum> enums = filter(pkg.getEClassifiers(), EEnum.class);
        Collections.sort(enums, new EDataTypeSorter());
        return enums;
    }

    /**
     * Returns the default {@link EEnumLiteral}, for the given {@link EEnum}.
     *
     * @param e The {@link EEnum} for which to return the default literal.
     * @return The default literal.
     */
    protected static EEnumLiteral getEnumDefaultLiteral(EEnum e) {
        return first(e.getELiterals());
    }

    /**
     * Returns the value of a detail of a data type, from the annotations describing the details, given a data type and
     * the name of the requested detail. If the detail is not found, {@code null} is returned.
     *
     * @param dt The data type.
     * @param detail The detail name.
     * @return The detail value, or {@code null} if not present.
     */
    protected static String getDataTypeDetail(EDataType dt, String detail) {
        for (EAnnotation anno: dt.getEAnnotations()) {
            String src = anno.getSource();
            String emd = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
            if (src.equals(emd)) {
                return anno.getDetails().get(detail);
            }
        }
        return null;
    }

    /**
     * Returns a textual representation of the bounds of a structural feature. Examples: "{@code 0..*}", "{@code 0..1}",
     * "{@code 1}" (quotes not part of the result).
     *
     * @param feat The structural feature.
     * @return The textual representation of the bounds of the structural feature.
     */
    protected static String getBoundsText(EStructuralFeature feat) {
        return (feat.getLowerBound() == feat.getUpperBound()) ? getBoundText(feat.getLowerBound())
                : getBoundText(feat.getLowerBound()) + ".." + getBoundText(feat.getUpperBound());
    }

    /**
     * Returns a textual representation of the bound of a structural feature. Simply converts the bound number to a
     * string. The {@code -1} value however, gets mapped to {@code *}.
     *
     * @param bound The bound value.
     * @return The textual representation of the bound value.
     */
    protected static String getBoundText(int bound) {
        return (bound == -1) ? "*" : String.valueOf(bound);
    }

    /**
     * Returns the textual representation of the kind of a structural feature.
     *
     * @param feat The structural feature.
     * @return {@code "attr"} for attributes, {@code "cont"} for containment references, and {@code "ref"} for
     *     non-containment references.
     */
    protected static String getStructFeatKindName(EStructuralFeature feat) {
        return (feat instanceof EAttribute) ? "attr" : ((EReference)feat).isContainment() ? "cont" : "ref";
    }

    /**
     * Returns the {@link EEnumLiteral ELiterals} that are part of the given {@link EEnum}, sorted on literal names.
     *
     * @param e The {@link EEnum} for which to return the literals.
     * @return The {@link EEnumLiteral ELiterals} that are part of the given {@link EEnum}.
     */
    protected static List<EEnumLiteral> getLiterals(EEnum e) {
        List<EEnumLiteral> lits = list();
        lits.addAll(e.getELiterals());
        Collections.sort(lits, new EEnumLiteralSorter());
        return lits;
    }

    /** Class for sorting {@link EClass} instances on class names. */
    public static class EClassSorter implements Comparator<EClass> {
        @Override
        public int compare(EClass arg0, EClass arg1) {
            return arg0.getName().compareTo(arg1.getName());
        }
    }

    /** Class for sorting {@link EEnum} instances on data type names (also works for enumerations). */
    public static class EDataTypeSorter implements Comparator<EDataType> {
        @Override
        public int compare(EDataType arg0, EDataType arg1) {
            return arg0.getName().compareTo(arg1.getName());
        }
    }

    /** Class for sorting {@link EStructuralFeature} instances on feature names. */
    public static class EStructuralFeatureSorter implements Comparator<EStructuralFeature> {
        @Override
        public int compare(EStructuralFeature arg0, EStructuralFeature arg1) {
            return arg0.getName().compareTo(arg1.getName());
        }
    }

    /** Class for sorting {@link EReference} instances on reference names. */
    public static class EReferenceSorter implements Comparator<EReference> {
        @Override
        public int compare(EReference arg0, EReference arg1) {
            return arg0.getName().compareTo(arg1.getName());
        }
    }

    /** Class for sorting {@link EEnumLiteral} instances on literal names. */
    public static class EEnumLiteralSorter implements Comparator<EEnumLiteral> {
        @Override
        public int compare(EEnumLiteral arg0, EEnumLiteral arg1) {
            return arg0.getName().compareTo(arg1.getName());
        }
    }
}
