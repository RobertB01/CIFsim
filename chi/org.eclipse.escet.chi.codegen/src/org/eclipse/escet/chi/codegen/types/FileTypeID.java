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

import static org.eclipse.escet.chi.codegen.Constants.CHI_FILE_HANDLE_FQC;
import static org.eclipse.escet.chi.codegen.Constants.DUMMY_FILE_HANDLE_FQC;
import static org.eclipse.escet.common.java.Assert.fail;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Expression;

/** Class representing a file type in the code generator. */
public class FileTypeID extends StateLessObjectTypeID {
    /** Constructor of the {@link FileTypeID} class. */
    public FileTypeID() {
        super(false, TypeKind.FILE);
    }

    @Override
    public String getTypeText() {
        return "file";
    }

    @Override
    public String getJavaClassType() {
        return CHI_FILE_HANDLE_FQC;
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile curentFile) {
        fail("Unhandled expression " + expr.toString() + " encountered");
        return null;
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        String dummyHandle = DUMMY_FILE_HANDLE_FQC;
        int idx = dummyHandle.lastIndexOf('.');
        if (idx != -1) {
            jf.addImport(dummyHandle, false);
        }
        return dummyHandle.substring(idx + 1) + ".INSTANCE";
    }
}
