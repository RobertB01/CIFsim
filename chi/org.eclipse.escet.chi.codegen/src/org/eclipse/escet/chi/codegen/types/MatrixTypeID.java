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

import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.common.java.Assert.check;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.CodeExpression;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.IntNumber;
import org.eclipse.escet.chi.metamodel.chi.MatrixExpression;
import org.eclipse.escet.chi.metamodel.chi.MatrixRow;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;
import org.eclipse.escet.common.java.Assert;

/** Class representing a matrix type in the code generator. */
public class MatrixTypeID extends StateLessObjectTypeID {
    /** Number of rows in the matrix type. */
    private final int rowSize;

    /** Number of columns in the matrix type. */
    private final int columnSize;

    /**
     * Constructor for the {@link MatrixTypeID} class.
     *
     * @param m Matrix type to represent by this object.
     */
    public MatrixTypeID(MatrixType m) {
        super(false, TypeKind.MATRIX);
        rowSize = getValue(m.getRowSize());
        columnSize = getValue(m.getColumnSize());
    }

    @Override
    public String getTypeText() {
        return fmt("matrix(%d, %d)", rowSize, columnSize);
    }

    @Override
    public String getJavaClassType() {
        return Constants.MATRIX_FQC;
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        String classPath = getJavaClassType();
        int idx = classPath.lastIndexOf('.');
        if (idx != -1) {
            jf.addImport(classPath, false);
            classPath = classPath.substring(idx + 1);
        }
        return fmt("new %s(%d, %d)", classPath, rowSize, columnSize);
    }

    @Override
    public String getReadName(String stream, JavaFile jf) {
        Assert.check(isPrintable());

        String classPath = getJavaClassType();
        int idx = classPath.lastIndexOf('.');
        if (idx != -1) {
            jf.addImport(classPath + ".read", true);
        }
        return fmt("read(chiCoordinator, %d, %d, %s)", rowSize, columnSize, stream);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MatrixTypeID)) {
            return false;
        }
        MatrixTypeID other = (MatrixTypeID)obj;
        return rowSize == other.rowSize && columnSize == other.columnSize;
    }

    @Override
    public int hashCode() {
        return TypeKind.MATRIX.hashCode() + rowSize * 13 + columnSize * 23;
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        String classPath = getJavaClassType();
        int idx = classPath.lastIndexOf('.');
        String className;
        if (idx != -1) {
            currentFile.addImport(classPath, false);
            className = classPath.substring(idx + 1);
        } else {
            className = classPath;
        }

        // Paranoia check, is the type of the expression the same as the local
        // TypeID 'this'.
        MatrixType mType = (MatrixType)expr.getType();
        check(rowSize == getValue(mType.getRowSize()));
        check(columnSize == getValue(mType.getColumnSize()));

        if (expr instanceof MatrixExpression) {
            MatrixExpression me = (MatrixExpression)expr;
            List<String> lines = list();
            String line;
            String varName = ctxt.makeUniqueName("mat");

            line = fmt("%s %s = new %s(%d, %d);", className, varName, className, rowSize, columnSize);
            lines.add(line);
            for (int r = 0; r < me.getRows().size(); r++) {
                MatrixRow mr = me.getRows().get(r);
                for (int c = 0; c < mr.getElements().size(); c++) {
                    Expression eVal = mr.getElements().get(c);
                    ExpressionBase valCode = convertExpression(eVal, ctxt, currentFile);
                    lines.addAll(valCode.getCode());
                    line = fmt("%s.set(%d, %d, %s);", varName, r, c, valCode.getValue());
                    lines.add(line);
                }
            }
            return new CodeExpression(lines, varName, expr);
        }
        Assert.fail("Unexpected type of expression node for MatrixTypeID: " + expr.toString());
        return null;
    }

    /**
     * Derive the value of a constant expression.
     *
     * <p>
     * Note that the compiler should have reduced the expression to a simple number already, so no need to do
     * complicated calculations here.
     * </p>
     *
     * @param val Expression to evaluate.
     * @return Integer value.
     */
    public static int getValue(Expression val) {
        Assert.check(val instanceof IntNumber);
        IntNumber num = (IntNumber)val;
        return Integer.parseInt(num.getValue());
    }
}
