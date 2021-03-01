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

package org.eclipse.escet.chi.codegen.types;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessReference;
import org.eclipse.escet.common.java.Assert;

/** Class representing a Chi process type in the code generator. */
public class ProcessTypeID extends StateLessObjectTypeID {
    /** Class name of the process type. */
    private final String className;

    /**
     * Constructor for the {@link ProcessTypeID} class.
     *
     * @param args Formal parameters of the process type.
     * @param ctxt Code generator context.
     */
    public ProcessTypeID(List<TypeID> args, CodeGeneratorContext ctxt) {
        super(false, TypeKind.PROCESS, args);
        if (!ctxt.hasTypeName(this)) {
            className = ctxt.makeUniqueName("ProcessType");
            ctxt.addTypeName(this, className);
            ctxt.addProcessType(this);
        } else {
            className = ctxt.getTypeName(this);
        }
    }

    @Override
    public String getTypeText() {
        boolean first = true;
        String result = "proc (";
        for (TypeID tid: subTypes) {
            if (!first) {
                result += ", ";
            }
            result += tid.getTypeText();
            first = false;
        }
        return result + ")";
    }

    @Override
    public String getJavaClassType() {
        return className;
    }

    @Override
    public String getSimplestJavaValue() {
        return getJavaClassType() + ".PP_NONE";
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        if (expr instanceof ProcessReference) {
            ProcessDeclaration pd = ((ProcessReference)expr).getProcess();
            String pName = className + ".P_" + pd.getName();
            return new SimpleExpression(pName, expr);
        }

        Assert.fail("Unexpected expression in convertExprNode(" + expr.toString() + "): kind=" + kind.toString());
        return null;
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        return getJavaClassType() + ".PP_NONE";
    }
}
