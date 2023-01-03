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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.common.java.Assert.fail;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Expression;

/** Class representing a timer type in the code generator. */
public class TimerTypeID extends StateLessObjectTypeID {
    /** Constructor of the {@link TimerTypeID} class. */
    public TimerTypeID() {
        super(true, TypeKind.TIMER);
    }

    @Override
    public String getTypeText() {
        return "timer";
    }

    @Override
    public String getJavaClassType() {
        return Constants.TIMER_FQC;
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        fail("Unexpected expression " + expr.toString() + " in TimerTypeID.convertExprNode()");
        return null;
    }
}
