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

package org.eclipse.escet.chi.codegen.java;

import static org.eclipse.escet.chi.codegen.java.JavaParameter.convertParamsToString;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;

/** Class representing a general generated Java method. */
public class JavaMethod {
    /** Method header. */
    protected final String header;

    /** Code lines. {@code null} if abstract method. */
    public CodeBox lines;

    /**
     * Java method constructor, if only Java was smart enough to understand generics.
     *
     * @param accessors Accessors of the method. The method recognizes 'abstract' for abstract methods.
     * @param retTid Return type, omit when creating a constructor.
     * @param name Name of the method.
     * @param arguments Formal parameters of the method ({@code null} means no parameters needed).
     * @param ctxt Code generator context for resolving the type ids.
     * @return Java method description.
     */
    public static JavaMethod makeJavaMethod(String accessors, TypeID retTid, String name,
            List<VariableDeclaration> arguments, CodeGeneratorContext ctxt)
    {
        return new JavaMethod(accessors, retTid, name, makeParameters(arguments, ctxt), ctxt);
    }

    /**
     * Java constructor method constructor, if only Java was smart enough to understand generics.
     *
     * @param accessors Accessors of the method. The method recognizes 'abstract' for abstract methods.
     * @param name Name of the method.
     * @param arguments Formal parameters of the method ({@code null} means no parameters needed).
     * @param ctxt Code generator context for resolving the type ids.
     * @return Java method description.
     */
    public static JavaMethod makeJavaMethod(String accessors, String name, List<VariableDeclaration> arguments,
            CodeGeneratorContext ctxt)
    {
        return new JavaMethod(accessors, null, name, makeParameters(arguments, ctxt), ctxt);
    }

    /**
     * Convert a sequence of variable declarations to the formal parameters of a method.
     *
     * @param arguments Variable declarations ({@code null} means no parameters needed).
     * @param ctxt Code generator context for resolving the type ids.
     * @return Sequence of java parameters, or {@code null} if no parameters.
     */
    public static List<JavaParameter> makeParameters(List<VariableDeclaration> arguments, CodeGeneratorContext ctxt) {
        if (arguments == null) {
            return null;
        }

        List<JavaParameter> res = list();
        for (VariableDeclaration vd: arguments) {
            if (!vd.isParameter()) {
                continue;
            }
            TypeID tid = createTypeID(vd.getType(), ctxt);
            res.add(new JavaParameter(tid, "p_" + vd.getName()));
        }
        return res;
    }

    /**
     * Constructor of a java method object.
     *
     * @param accessors Accessors of the method. The method recognizes 'abstract' for abstract methods.
     * @param retTid Return type, {@code null} for a constructor.
     * @param name Name of the method.
     * @param arguments Formal parameters of the method ({@code null} means no parameters needed).
     * @param ctxt Code generator context for resolving the type ids.
     */
    public JavaMethod(String accessors, TypeID retTid, String name, List<JavaParameter> arguments,
            CodeGeneratorContext ctxt)
    {
        this(accessors, (retTid == null) ? null : retTid.getJavaType(), name, convertParamsToString(arguments), null);
    }

    /**
     * Constructor of a java method object.
     *
     * @param accessors Accessors of the method. The method recognizes 'abstract' for abstract methods.
     * @param retType Return type, {@code null} for a constructor.
     * @param name Name of the method.
     * @param parameters Formal parameters of the method ({@code null} means no parameters needed).
     * @param exceptions Exceptions being thrown ({@code null} means no exceptions are thrown).
     */
    public JavaMethod(String accessors, String retType, String name, String parameters, String exceptions) {
        Assert.notNull(name);

        if (parameters == null) {
            parameters = "";
        }

        String header = accessors;
        if (retType != null) {
            header += " " + retType;
        }
        header += " " + name + "(" + parameters + ")";
        if (exceptions != null) {
            header += " throws " + exceptions;
        }
        this.header = header;

        if (accessors.contains("abstract")) {
            lines = null;
        } else {
            lines = new MemoryCodeBox();
            lines.indent();
        }
    }

    /**
     * Constructor of the {@link JavaMethod} class for a concrete method.
     *
     * @param header Header text of the method, without '{'.
     */
    public JavaMethod(String header) {
        this.header = header;
        lines = new MemoryCodeBox();
        lines.indent();
    }

    /**
     * Convert the method to a box.
     *
     * @return The method converted to a single box.
     */
    public Box generateBox() {
        // Abstract method (has no code).
        Assert.check(!header.contains("\n")); // No newlines allowed in TextBox.
        if (lines == null) {
            return new TextBox(header + ";");
        }

        VBox main = new VBox(0);
        main.add(header + " {");
        main.add(lines);
        main.add("}");
        return main;
    }
}
