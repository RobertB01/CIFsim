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

package org.eclipse.escet.chi.codegen.statements.seq.assignment;

import static org.eclipse.escet.chi.codegen.Constants.CHI_SIMULATOR_EXCEPTION_FQC;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.statements.seq.assignment.AssignmentFunctions.expressionCollectVariables;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.java.Assert;

/** Lhs dictionary projection class. */
public class DictionaryProjection extends ChainedAsgNode {
    /** Type of the dictionary. */
    public final DictType type;

    /** Expression denoting the key into the dictionary. */
    public final Expression keyExpr;

    /** Name of the variable holding the key value. */
    private String keyName = null;

    /** Whether initialization has been performed. */
    private boolean initDone = false;

    /**
     * Constructor of the {@link DictionaryProjection} class.
     *
     * @param type Type of the dictionary.
     * @param keyExpr Expression denoting the key into the dictionary.
     * @param child Child assignment.
     */
    public DictionaryProjection(DictType type, Expression keyExpr, AssignmentNode child) {
        super(child);
        this.type = type;
        this.keyExpr = keyExpr;
    }

    @Override
    public boolean performsFullAssignment() {
        return false;
    }

    @Override
    public void saveUsedValues(boolean oneAssignment, Set<VariableDeclaration> assigneds, CodeGeneratorContext ctxt,
            JavaFile currentClass, List<String> lines)
    {
        Assert.check(!initDone);
        initDone = true;

        Set<VariableDeclaration> useds = set();
        expressionCollectVariables(keyExpr, useds);
        if (!isEmptyIntersection(useds, assigneds)) {
            keyName = ctxt.makeUniqueName("key");
            ExpressionBase exprCode = convertExpression(keyExpr, ctxt, currentClass);
            exprCode.setCurrentPositionStatement(lines);
            lines.addAll(exprCode.getCode());
            TypeID keyTid = createTypeID(type.getKeyType(), ctxt);
            lines.add(fmt("%s %s = %s;", keyTid.getJavaType(), keyName, exprCode.getValue()));
        }

        child.saveUsedValues(oneAssignment, assigneds, ctxt, currentClass, lines);
    }

    @Override
    public void assignValue(String lhsVar, List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines)
    {
        String keyValue = keyName;
        if (keyValue == null) {
            ExpressionBase exprCode = convertExpression(keyExpr, ctxt, currentClass);
            exprCode.setCurrentPositionStatement(lines);
            lines.addAll(exprCode.getCode());
            keyValue = exprCode.getValue();
        }

        TypeID elmTid = createTypeID(type.getValueType(), ctxt);
        String tmpName = ctxt.makeUniqueName("tmp");

        if (child.performsFullAssignment()) {
            lines.add(fmt("%s %s;", elmTid.getJavaType(), tmpName));
        } else {
            lines.add(fmt("%s %s = %s.get(%s);", elmTid.getJavaType(), tmpName, lhsVar, keyValue));

            // With dicts, 'get' pulls from the java Map, which may return 'null'. Abort when this happens.
            TypeID keyTid = createTypeID(type.getKeyType(), ctxt);
            final String excClass = "ChiSimulatorException";
            final String doesNotExist = "does not exist in the dictionary";

            if (keyTid.isPrintable()) {
                // We can output the key-value.
                final String keyText = keyTid.getToString(keyValue, currentClass);

                lines.add(fmt("if (%s == null) {", tmpName));
                lines.add(fmt("    throw new %s(\"Key \\\"\" + %s + \"\\\" %s.\");", excClass, keyText, doesNotExist));
                lines.add("}");
            } else {
                // Key-value is not printable, use a generic message.
                lines.add(fmt("if (%s == null) throw new %s(\"Key %s.\");", tmpName, excClass, doesNotExist));
            }
            currentClass.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        }

        child.assignValue(tmpName, rhsIndices, ctxt, currentClass, lines);

        lines.add(fmt("%s = %s.modify(%s, %s);", lhsVar, lhsVar, keyValue, tmpName));
    }
}
