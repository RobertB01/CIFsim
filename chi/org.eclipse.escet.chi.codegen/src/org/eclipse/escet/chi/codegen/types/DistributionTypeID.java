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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.common.java.Assert.fail;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Expression;

/** Class representing a distribution type. */
public class DistributionTypeID extends StateLessObjectTypeID {
    /**
     * Constructor of the {@link DistributionTypeID} class.
     *
     * @param tid Type id of the element type.
     */
    public DistributionTypeID(TypeID tid) {
        super(false, TypeKind.DISTRIBUTION, list(tid));
    }

    @Override
    public String getTypeText() {
        return "dist " + first(subTypes).getTypeText();
    }

    @Override
    public String getJavaClassType() {
        String clsName;
        switch (first(subTypes).kind) {
            case BOOL:
                clsName = Constants.BOOL_DISTRIBUTION_CLASSNAME;
                break;

            case INT:
                clsName = Constants.INT_DISTRIBUTION_CLASSNAME;
                break;

            case REAL:
                clsName = Constants.DOUBLE_DISTRIBUTION_CLASSNAME;
                break;

            default:
                fail("Unexpected distribution element type found.");
                return null; // Not reached.
        }
        return Constants.RANDOM_PKG + "." + clsName;
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        String clsName, value;
        switch (first(subTypes).kind) {
            case BOOL:
                clsName = Constants.CONSTANT_BOOL_DISTRIBUTION_CLASSNAME;
                value = "false";
                break;

            case INT:
                clsName = Constants.CONSTANT_INT_DISTRIBUTION_CLASSNAME;
                value = "0";
                break;

            case REAL:
                clsName = Constants.CONSTANT_DOUBLE_DISTRIBUTION_CLASSNAME;
                value = "0.0";
                break;

            default:
                fail("Unexpected distribution element type found.");
                return null; // Not reached.
        }
        jf.addImport(Constants.RANDOM_PKG + "." + clsName, false);

        return "new " + clsName + "(chiCoordinator, " + value + ")";
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        fail("Unexpected expression node " + expr.toString() + " encountered in convertExprNode.");
        return null; // Not reached.
    }
}
