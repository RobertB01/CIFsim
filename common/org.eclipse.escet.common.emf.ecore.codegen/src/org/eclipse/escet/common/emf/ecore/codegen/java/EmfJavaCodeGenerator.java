//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.escet.common.emf.ecore.codegen.EmfCodeGenerator;
import org.eclipse.escet.common.java.Assert;

/** Base class for EMF code generators (that generate Java output), with useful helper methods. */
public abstract class EmfJavaCodeGenerator extends EmfCodeGenerator {
    /**
     * Returns the full name to use in Java to refer to the type of the given {@link EClassifier}.
     *
     * @param cls The {@link EClassifier}.
     * @return The full name to use in Java to refer to the type of the given classifier.
     */
    protected static String getJavaTypeName(EClassifier cls) {
        String name1 = cls.getInstanceClassName();
        String name2 = cls.getInstanceTypeName();
        Assert.notNull(name1);
        Assert.notNull(name2);
        Assert.check(name1.equals(name2));
        return name1;
    }

    /**
     * Returns the full name to use in Java to refer to the type of the given {@link EFactory}.
     *
     * @param fact The {@link EFactory}.
     * @return The full name to use in Java to refer to the type of the given {@link EFactory}.
     */
    protected static String getJavaTypeName(EFactory fact) {
        Class<?>[] factoryInterfaces = fact.getClass().getInterfaces();
        Assert.check(factoryInterfaces.length == 1);
        Class<?> factoryInterface = factoryInterfaces[0];
        return factoryInterface.getName();
    }

    /**
     * Returns the full name of the EMF generated implementation class for the given {@link EClass}. Only works for
     * non-abstract classes.
     *
     * @param cls The {@link EClass} to return the full name of the EMF generated implementation class for.
     * @return The full name of the EMF generated implementation class.
     */
    protected static String getImplClassName(EClass cls) {
        Assert.check(!cls.isAbstract());
        EObject obj = cls.getEPackage().getEFactoryInstance().create(cls);
        return obj.getClass().getName();
    }

    /**
     * Returns the simple name of the EMF generated implementation class for the given {@link EClass}. Only works for
     * non-abstract classes.
     *
     * @param cls The {@link EClass} to return the simple name of the EMF generated implementation class for.
     * @return The simple name of the EMF generated implementation class.
     */
    protected static String getImplClassSimpleName(EClass cls) {
        Assert.check(!cls.isAbstract());
        EObject obj = cls.getEPackage().getEFactoryInstance().create(cls);
        return obj.getClass().getSimpleName();
    }

    /**
     * Returns the Java implementation type name of the type of the given structural feature. For the given 'classes',
     * the simple (relative) name of the class is used. For all other types, the full absolute Java class name is used.
     *
     * @param feat The structural feature for which to get the Java implementation class name of type of the feature.
     * @param classes Classes that are to be resolved to their simple (relative) name.
     * @return The Java implementation type name of the type of the structural feature.
     */
    protected static String getStructuralFeatureTypeText(EStructuralFeature feat, List<EClass> classes) {
        EClassifier type = feat.getEType();
        String tName = CLASS_MAP.get(type.getInstanceClass());
        if (tName == null) {
            tName = type.getName();
        }
        return (feat.isMany()) ? fmt("List<%s>", tName) : tName;
    }

    /** Mapping from Java types to textual representations, used by the {@link #getStructuralFeatureTypeText} method. */
    private static final Map<Class<?>, String> CLASS_MAP;

    static {
        CLASS_MAP = map();

        CLASS_MAP.put(Boolean.class, "Boolean");
        CLASS_MAP.put(boolean.class, "Boolean");
        CLASS_MAP.put(Byte.class, "Byte");
        CLASS_MAP.put(byte.class, "Byte");
        CLASS_MAP.put(Character.class, "Character");
        CLASS_MAP.put(char.class, "Character");
        CLASS_MAP.put(Double.class, "Double");
        CLASS_MAP.put(double.class, "Double");
        CLASS_MAP.put(Float.class, "Float");
        CLASS_MAP.put(float.class, "Float");
        CLASS_MAP.put(Integer.class, "Integer");
        CLASS_MAP.put(int.class, "Integer");
        CLASS_MAP.put(Long.class, "Long");
        CLASS_MAP.put(long.class, "Long");
        CLASS_MAP.put(Short.class, "Short");
        CLASS_MAP.put(short.class, "Short");
        CLASS_MAP.put(String.class, "String");
    }

    /**
     * Returns the name of the Java method that implements the setter for the given structural feature. For features
     * with an upper-bound larger than one, or an infinite upper-bound, the getter is used, in combination with adding.
     *
     * <p>
     * For instance, for a feature with name "value", and with upper-bound one, this method returns "getValue". For a
     * feature with name "values", and with infinite upper-bound, this method returns "getValues().addAll".
     * </p>
     *
     * @param feat The structural feature for which to return the setter name.
     * @return The name of the Java method that implements the setter.
     */
    protected static String getSetterName(EStructuralFeature feat) {
        String methodSetName = feat.getName();
        if (feat.isMany()) {
            methodSetName = "get" + methodSetName.substring(0, 1).toUpperCase(Locale.US) + methodSetName.substring(1)
                    + "().addAll";
        } else {
            methodSetName = "set" + methodSetName.substring(0, 1).toUpperCase(Locale.US) + methodSetName.substring(1);
        }
        return methodSetName;
    }
}
