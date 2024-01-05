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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.TypeEqHashWrap;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Default value code generator. */
public class DefaultValueCodeGenerator {
    /** Constructor for the {@link DefaultValueCodeGenerator} class. */
    private DefaultValueCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the default values of the more complex types of the specification.
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeDefaultValues(CifCompilerContext ctxt) {
        // Check if we need to generate anything.
        Map<TypeEqHashWrap, String> mapping = ctxt.getDefaultMethodNames();
        if (mapping.isEmpty()) {
            return;
        }

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("DefaultValues");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Creation of default values of container types. */");
        h.add("public final class DefaultValues {");

        // Add body.
        CodeBox c = file.body;

        // Add methods for the container types.
        List<ExprCodeGeneratorResult> exprResults = list();
        for (Entry<TypeEqHashWrap, String> entry: mapping.entrySet()) {
            if (!c.isEmpty()) {
                c.add();
            }
            CifType type = entry.getKey().type;
            c.add("// %s", CifTextUtils.typeToStr(type));
            c.add("public static %s %s() {", TypeCodeGenerator.gencodeType(type, ctxt), entry.getValue());
            c.indent();

            if (type instanceof DictType) {
                c.add("return new LinkedHashMap<>(0);");
            } else if (type instanceof ListType) {
                ListType ltype = (ListType)type;
                CifType etype = ltype.getElementType();
                int cnt = CifTypeUtils.getLowerBound(ltype);
                c.add("%s rslt = listc(%d);", gencodeType(type, ctxt), cnt);
                if (cnt > 0) {
                    ExprCodeGeneratorResult result = getDefaultValueCode(etype, ctxt);
                    c.add("%s elem = %s;", gencodeType(etype, ctxt), result);
                    exprResults.add(result);
                    c.add("for (int i = 0; i < %d; i++) {", cnt);
                    c.indent();
                    c.add("rslt.add(elem);");
                    c.dedent();
                    c.add("}");
                }
                c.add("return rslt;");
            } else if (type instanceof SetType) {
                c.add("return new LinkedHashSet<>(0);");
            } else if (type instanceof TupleType) {
                TupleType ttype = (TupleType)type;
                List<String> argTxts = listc(ttype.getFields().size());
                for (Field field: ttype.getFields()) {
                    ExprCodeGeneratorResult result = getDefaultValueCode(field.getType(), ctxt);
                    argTxts.add(result.toString());
                    exprResults.add(result);
                }
                c.add("return new %s(%s);", ctxt.getTupleTypeClassName(ttype), String.join(", ", argTxts));
            } else {
                throw new RuntimeException("Unknown container type: " + type);
            }

            c.dedent();
            c.add("}");
        }

        // Add potential extra value expression evaluation methods.
        for (ExprCodeGeneratorResult exprResult: exprResults) {
            exprResult.addExtraMethods(c);
        }
    }

    /**
     * Returns Java code to use for the default value of a CIF type.
     *
     * @param type The CIF type.
     * @param ctxt The compiler context to use.
     * @return The Java code to for the default value of the given CIF type.
     */
    public static ExprCodeGeneratorResult getDefaultValueCode(CifType type, CifCompilerContext ctxt) {
        // Handle non-container types.
        type = CifTypeUtils.normalizeType(type);
        if (!CifTypeUtils.isContainerType(type)) {
            Expression defaultValue = ctxt.getDefaultValue(type);
            return ExprCodeGenerator.gencodeExpr(defaultValue, ctxt, null);
        }

        // Handle container types.
        String name = ctxt.getDefaultValueMethodName(type);
        Assert.notNull(name);
        return new ExprCodeGeneratorResult(fmt("DefaultValues.%s()", name), type);
    }
}
