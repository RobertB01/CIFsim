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

import static org.eclipse.escet.common.java.Assert.fail;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collections;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.ChannelExpression;
import org.eclipse.escet.chi.metamodel.chi.Expression;

/**
 * Representation of the channel data type. Note that the channel data type is not part of the type here, as it is not
 * of interest to the code generator.
 */
public class ChannelTypeID extends StateLessObjectTypeID {
    /**
     * Constructor of the {@link ChannelTypeID} class.
     *
     * @param tid Data type of data being transported. {@code null} means a 'void' channel.
     */
    public ChannelTypeID(TypeID tid) {
        super(false, TypeKind.CHANNEL, (tid == null) ? Collections.EMPTY_LIST : list(tid));
    }

    @Override
    public String getTypeText() {
        if (subTypes.isEmpty()) {
            return "chan void";
        }
        return "chan " + first(subTypes).getTypeText();
    }

    @Override
    public String getJavaClassType() {
        return Constants.CHANNEL_FQC;
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        if (expr instanceof ChannelExpression) {
            String path = getJavaClassType();
            currentFile.addImport(path, false);
            int j = path.lastIndexOf('.');
            if (j > -1) {
                path = path.substring(j + 1);
            }
            return new SimpleExpression("new " + path + "()", expr);
        }
        fail("No call expected at ChannelTypeID.convertExprNode()");
        return null;
    }
}
