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

import static org.eclipse.escet.common.java.Assert.fail;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Expression;

/** Class representing an instantiated model or process type in the Chi code generator. */
public class InstanceTypeID extends StateLessObjectTypeID {
    /** Constructor for the {@link InstanceTypeID} class. */
    public InstanceTypeID() {
        super(false, TypeKind.INSTANCE);
    }

    @Override
    public String getTypeText() {
        return "inst";
    }

    @Override
    public String getJavaClassType() {
        return Constants.COREPROCESS_FQC;
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentfile) {
        fail("Implement convertExprNode(" + expr.toString() + ") of InstanceTypeID");
        return null; // Never reached.
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        jf.addImport(Constants.DUMMY_PROCESS_FQC, false);
        return "DummyProcess.INSTANCE";
    }
}
