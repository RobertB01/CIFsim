//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.chi.codegen.EnumCodeGenerator.transEnumDeclaration;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.EnumValueReference;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.common.java.Assert;

/** Class representing an enumeration type of a Chi specification. */
public class EnumTypeID extends StateLessObjectTypeID {
    /** Enumeration declaration. */
    private final EnumDeclaration decl;

    /** Name of the generated Chi implementation. */
    private final String enumName;

    /**
     * Constructor for the {@link EnumDeclaration} class.
     *
     * @param decl Enumeration declaration represented by this object.
     * @param ctxt Code generator context.
     */
    public EnumTypeID(EnumDeclaration decl, CodeGeneratorContext ctxt) {
        super(false, TypeKind.ENUM);
        this.decl = decl;
        if (!ctxt.hasTypeName(this)) {
            transEnumDeclaration(this, decl, ctxt);
        }
        enumName = ctxt.getTypeName(this);
    }

    @Override
    public String getTypeText() {
        return decl.getName();
    }

    @Override
    public String getJavaClassType() {
        return enumName;
    }

    @Override
    public String getReadName(String stream, JavaFile jf) {
        Assert.check(isPrintable());
        return fmt("%s.read(chiCoordinator, %s)", getJavaClassType(), stream);
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        String classPath = getJavaClassType();
        String val = "EV_" + decl.getValues().get(0).getName();
        return classPath + "." + val;
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        if (expr instanceof EnumValueReference) {
            EnumValueReference eval = (EnumValueReference)expr;
            String txt = getJavaClassType() + ".EV_" + eval.getValue().getName();
            return new SimpleExpression(txt, expr);
        }
        Assert.fail("Unknown expression node for enumeration type: " + expr.toString());
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EnumTypeID)) {
            return false;
        }
        return decl.equals(((EnumTypeID)obj).decl);
    }

    @Override
    public int hashCode() {
        return decl.hashCode();
    }
}
