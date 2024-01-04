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

import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;

/** Tuple type code generator. */
public class TupleTypeCodeGenerator {
    /** Constructor for the {@link TupleTypeCodeGenerator} class. */
    private TupleTypeCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the given tuple type.
     *
     * @param tupleType The tuple type.
     * @param className The name of the class to generate for this tuple type.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeTupleType(TupleType tupleType, String className, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile(className);

        // Add header.
        CodeBox h = file.header;
        h.add("/** Tuple type \"%s\". */", typeToStr(tupleType));
        h.add("public final class %s implements RuntimeToStringable {", className);

        // Add body.
        CodeBox c = file.body;

        // Determine field names.
        List<Field> fields = tupleType.getFields();
        List<String> names = listc(fields.size());
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            names.add(ctxt.getTupleTypeFieldFieldName(field));
        }

        // Add fields.
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            c.add("public %s %s;", gencodeType(field.getType(), ctxt), names.get(i));
        }

        // Generate constructor parameters code.
        List<String> paramTxts = listc(fields.size());
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            String typeTxt = gencodeType(field.getType(), ctxt);
            paramTxts.add(typeTxt + " " + names.get(i));
        }

        // Add constructor.
        c.add();
        c.add("public %s(%s) {", className, String.join(", ", paramTxts));
        c.indent();
        for (String name: names) {
            c.add("this.%s = %s;", name, name);
        }
        c.dedent();
        c.add("}");

        // Add 'copy' method, for use in tuple addressables of assignments.
        c.add();
        c.add("public %s copy() {", className);
        c.indent();
        c.add("return new %s(%s);", className, String.join(", ", names));
        c.dedent();
        c.add("}");

        // Add 'hashCode' method.
        c.add();
        c.add("@Override");
        c.add("public int hashCode() {");
        c.indent();
        for (int i = 0; i < fields.size(); i++) {
            c.add("%s hash(%s)%s", (i == 0) ? "return" : "      ", names.get(i), (i == fields.size() - 1) ? ";" : " ^");
        }
        c.dedent();
        c.add("}");

        // Add 'equals' method.
        c.add();
        c.add("@Override");
        c.add("public boolean equals(Object obj) {");
        c.indent();
        c.add("if (this == obj) return true;");
        c.add("%s other = (%s)obj;", className, className);
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = names.get(i);
            c.add("%s equal(this.%s, other.%s)%s", (i == 0) ? "return" : "      ", fieldName, fieldName,
                    (i == fields.size() - 1) ? ";" : " &&");
        }
        c.dedent();
        c.add("}");

        // Add 'toString' method.
        c.add();
        c.add("@Override");
        c.add("public String toString() {");
        c.indent();
        c.add("StringBuilder rslt = new StringBuilder();");
        c.add("rslt.append(\"(\");");
        for (int i = 0; i < names.size() - 1; i++) {
            c.add("rslt.append(runtimeToString(%s));", names.get(i));
            c.add("rslt.append(\", \");");
        }
        c.add("rslt.append(runtimeToString(%s));", last(names));
        c.add("rslt.append(\")\");");
        c.add("return rslt.toString();");
        c.dedent();
        c.add("}");

        // Add 'pop' method, for 'pop' standard library function. Note that
        // we generate this for all tuple types that have compatible fields,
        // even the ones for which it is not used.
        genPopMethod(tupleType, className, ctxt, c);
    }

    /**
     * Generate Java code for the 'pop' method of the given tuple type, if applicable.
     *
     * <p>
     * The 'pop' standard library function has the following signature:
     *
     * <pre>tuple(t, list t) pop(list t)</pre>
     * </p>
     *
     * @param tupleType The tuple type.
     * @param className The name of the class to generate for this tuple type.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     */
    public static void genPopMethod(TupleType tupleType, String className, CifCompilerContext ctxt, CodeBox c) {
        // Check for compatible fields. Need two fields.
        if (tupleType.getFields().size() != 2) {
            return;
        }

        // Second field must be a list type.
        Field field1 = tupleType.getFields().get(1);
        CifType ntype1 = normalizeType(field1.getType());
        if (!(ntype1 instanceof ListType)) {
            return;
        }

        // Field types must match.
        Field field0 = tupleType.getFields().get(0);
        CifType ntype0 = normalizeType(field0.getType());
        CifType netype1 = normalizeType(((ListType)ntype1).getElementType());
        boolean typeMatch = CifTypeUtils.checkTypeCompat(ntype0, netype1, RangeCompat.IGNORE);
        if (!typeMatch) {
            return;
        }

        // Compatible tuple type found. Add 'pop' method.
        c.add();
        c.add("public static %s pop(List<%s> lst) {", className, gencodeType(ntype0, ctxt, true));
        c.indent();
        c.add("if (lst.isEmpty()) {");
        c.indent();
        c.add("String msg = \"Invalid operation: pop([]).\";");
        c.add("throw new CifSimulatorException(msg);");
        c.dedent();
        c.add("}");
        c.add("return new %s(lst.get(0), slice(lst, 1, null));", className);
        c.dedent();
        c.add("}");
    }
}
