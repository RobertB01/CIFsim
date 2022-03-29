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

package org.eclipse.escet.setext.parser.ast.parser;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.parser.ast.SeTextObject;

/** SeText Java type representation. */
public class JavaType extends SeTextObject {
    /**
     * The name of the Java class for the Java type.
     *
     * <p>
     * After parsing, it can be:
     * <ul>
     * <li>The name of an imported Java type.</li>
     * <li>The name of a Java class in the default package.</li>
     * <li>The fully quantified Java class name for the Java type.</li>
     * <li>The fully quantified Java package name for a Java package.</li>
     * <li>The name of a Java type relative to an imported package. In this case the first part of the name refers to an
     * imported java package, and the remainder of this name is the remainder of the name of the fully quantified Java
     * class name for the Java type.</li>
     * </ul>
     * </p>
     *
     * <p>
     * After type checking, it is the fully quantified Java class name for the Java type.
     * </p>
     */
    public String className;

    /**
     * Generic type parameters of the Java type. Must be {@code null} if no parameters, and have at least one parameter
     * otherwise. Type checker may add generic type parameters from the import, if applicable.
     */
    public List<JavaType> genericTypeParams;

    /**
     * Constructor for the {@link JavaType} class.
     *
     * @param className The name of the Java class for the Java type. See also {@link #className}.
     * @param genericTypeParams Generic type parameters of the Java type. Must be {@code null} if no parameters, and
     *     have at least one parameter otherwise.
     * @param position Position information.
     */
    public JavaType(String className, List<JavaType> genericTypeParams, TextPosition position) {
        super(position);
        this.className = className;
        this.genericTypeParams = genericTypeParams;

        if (genericTypeParams != null) {
            Assert.check(!genericTypeParams.isEmpty());
        }
    }

    /**
     * Is this Java type a generic type?
     *
     * @return {@code true} if this Java type is a generic type, {@code false} otherwise.
     */
    public boolean isGeneric() {
        return genericTypeParams != null;
    }

    /**
     * Returns the simple class name of the Java type. That is, it returns the last part of {@link #className}.
     *
     * @return The simple class name of the Java type.
     */
    public String getSimpleClassName() {
        String rslt = className;
        int idx = rslt.lastIndexOf('.');
        if (idx != -1) {
            rslt = rslt.substring(idx + 1);
        }
        return rslt;
    }

    /**
     * Returns the package name of the Java type. That is, it returns all but the last part of {@link #className}.
     *
     * @return The package name of the Java type.
     */
    public String getPackageName() {
        int idx = className.lastIndexOf('.');
        return (idx == -1) ? "" : Strings.slice(className, null, idx);
    }

    /**
     * Returns the fully quantified names of the Java type and of its generic type parameters.
     *
     * @return The fully quantified names of the Java type and of its generic type parameters.
     */
    public Set<String> getNames() {
        Set<String> names = set(className);
        if (genericTypeParams != null) {
            for (JavaType param: genericTypeParams) {
                names.addAll(param.getNames());
            }
        }
        return names;
    }

    @Override
    public String toString() {
        StringBuilder rslt = new StringBuilder();
        rslt.append(className);
        if (genericTypeParams != null) {
            rslt.append("<");
            rslt.append(genericTypeParams.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            rslt.append(">");
        }
        return rslt.toString();
    }

    /**
     * Returns a textual representation of the Java type, using simple class names instead of fully quantified class
     * names.
     *
     * @return A textual representation of the Java type.
     *
     * @see #toString
     */
    public String toSimpleString() {
        StringBuilder rslt = new StringBuilder();
        rslt.append(getSimpleClassName());
        if (genericTypeParams != null) {
            rslt.append("<");
            List<String> paramTxts = list();
            for (JavaType param: genericTypeParams) {
                paramTxts.add(param.toSimpleString());
            }
            rslt.append(String.join(", ", paramTxts));
            rslt.append(">");
        }
        return rslt.toString();
    }
}
