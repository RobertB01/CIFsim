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

import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTupleTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.dropTypeReferences;
import static org.eclipse.escet.common.java.Assert.check;
import static org.eclipse.escet.common.java.Assert.fail;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.BinaryOperators;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FieldReference;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;

/** Class representing a tuple type in the code generator. */
public class TupleTypeID extends CoordObjectTypeID {
    /** Class name of the tuple type. */
    private String className;

    /** Field names. */
    private List<String> fieldNames;

    /**
     * Constructor of the {@link TupleTypeID} class.
     *
     * @param fieldNames Names of all fields.
     * @param tids Types of fields of the tuple.
     * @param ctxt Code generator context.
     */
    public TupleTypeID(List<String> fieldNames, List<TypeID> tids, CodeGeneratorContext ctxt) {
        super(TypeKind.TUPLE, tids);
        this.fieldNames = fieldNames;

        if (!ctxt.hasTypeName(this)) {
            className = ctxt.makeUniqueName("TupleType");
            ctxt.addTypeName(this, className);
            addSelf(tids, ctxt);
        } else {
            className = ctxt.getTypeName(this);
        }
    }

    @Override
    public String getTypeText() {
        String args = "";
        for (int i = 0; i < subTypes.size(); i++) {
            TypeID tid = subTypes.get(i);
            if (!args.isEmpty()) {
                args += ", ";
            }
            args += tid.getTypeText() + "v" + String.valueOf(i);
        }
        return "tuple(" + args + ")";
    }

    @Override
    public String getJavaClassType() {
        return className;
    }

    /**
     * Is the initial value of the given type the same as the empty value?
     *
     * @param tp Type to examine.
     * @return Whether the initial value is equal to the empty value.
     */
    private static boolean isInitialValueIsEmpty(Type tp) {
        tp = dropTypeReferences(tp);

        if (tp instanceof TupleType) {
            TupleType ttp = (TupleType)tp;
            for (TupleField tf: ttp.getFields()) {
                if (!isInitialValueIsEmpty(tf.getType())) {
                    return false;
                }
            }
            return true;
        }

        if (tp instanceof ListType) {
            ListType lt = (ListType)tp;
            return lt.getInitialLength() == null;
        }
        return true;
    }

    @Override
    public void assignInitialValue(String name, Type tp, VBox box, CodeGeneratorContext ctxt, JavaFile currentFile) {
        tp = dropTypeReferences(tp);
        if (isInitialValueIsEmpty(tp)) {
            box.add(name + " = " + getEmptyValue(currentFile) + ";");
            return;
        }

        String var = ctxt.makeUniqueName("tuple");
        box.add(fmt("%s %s = new %s(chiCoordinator);", className, var, className));

        TupleType ttp = (TupleType)tp;
        check(ttp.getFields().size() == subTypes.size());

        int i = 0;
        for (TupleField tf: ttp.getFields()) {
            tp = dropTypeReferences(tf.getType());
            String fieldName = var + ".var" + String.valueOf(i);
            subTypes.get(i).assignInitialValue(fieldName, tp, box, ctxt, currentFile);
            i++;
        }
        box.add(name + " = " + var + ";");
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        String classPath = getJavaClassType();
        int idx = classPath.lastIndexOf('.');
        if (idx != -1) {
            jf.addImport(classPath, false);
        }
        return classPath + ".makeDefault(spec, chiCoordinator)";
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        if (expr instanceof TupleExpression) {
            TupleExpression te = (TupleExpression)expr;

            // Construct a tuple typeID.
            List<Expression> exprs = te.getFields();
            List<TypeID> tids = listc(exprs.size());
            for (Expression e: exprs) {
                tids.add(createTypeID(e.getType(), ctxt));
            }
            // Using null here works, as packing never accesses the fields.
            TypeID tupleTid = createTupleTypeID(null, tids, ctxt);

            List<String> code = list();
            String vName = ctxt.makeUniqueName("tuple");
            String line = tupleTid.getJavaClassType();
            line = fmt("%s %s = new %s(chiCoordinator);", line, vName, line);
            code.add(line);
            for (int i = 0; i < exprs.size(); i++) {
                ExpressionBase fld = convertExpression(exprs.get(i), ctxt, currentFile);
                code.addAll(fld.getCode());
                line = fmt("%s.var%d = %s;", vName, i, fld.getValue());
                code.add(line);
            }
            return makeExpression(code, vName, expr);
        }

        if (expr instanceof BinaryExpression) {
            BinaryExpression bexpr = (BinaryExpression)expr;
            // tup.field projection
            if (bexpr.getOp().equals(BinaryOperators.PROJECTION)) {
                check(bexpr.getRight() instanceof FieldReference);
                FieldReference tf = (FieldReference)bexpr.getRight();
                String fldName = tf.getField().getName();
                int i;
                for (i = 0; i < fieldNames.size(); i++) {
                    if (fieldNames.get(i).equals(fldName)) {
                        break;
                    }
                }
                Assert.check(i < fieldNames.size(), "Cannot find tuple field.");

                ExpressionBase lhsExpr = convertExpression(bexpr.getLeft(), ctxt, currentFile);
                String varText = lhsExpr.getValue() + ".var" + String.valueOf(i);
                return makeExpression(lhsExpr.getCode(), varText, expr);
            }
            // tuple + tuple
            check(bexpr.getOp().equals(BinaryOperators.ADDITION));
            ExpressionBase lhsExpr, rhsExpr;
            lhsExpr = convertExpression(bexpr.getLeft(), ctxt, currentFile);
            rhsExpr = convertExpression(bexpr.getRight(), ctxt, currentFile);
            List<String> code = list();
            String vName = ctxt.makeUniqueName("tuple");
            TypeID tid = createTypeID(bexpr.getType(), ctxt);
            String line = tid.getJavaClassType();
            line = fmt("%s %s = new %s(chiCoordinator);", line, vName, line);
            code.add(line);

            int fldNum = 0;
            TupleType ttp = (TupleType)bexpr.getLeft().getType();
            code.addAll(lhsExpr.getCode());
            String value = ctxt.makeUniqueName("tuple");
            tid = createTypeID(bexpr.getLeft().getType(), ctxt);
            line = fmt("%s %s = %s;", tid.getJavaType(), value, lhsExpr.getValue());
            code.add(line);
            for (int i = 0; i < ttp.getFields().size(); i++) {
                line = fmt("%s.var%d = (%s).var%d;", vName, fldNum, value, i);
                code.add(line);
                fldNum++;
            }
            ttp = (TupleType)bexpr.getRight().getType();
            code.addAll(rhsExpr.getCode());
            value = ctxt.makeUniqueName("tuple");
            tid = createTypeID(bexpr.getRight().getType(), ctxt);
            line = fmt("%s %s = %s;", tid.getJavaType(), value, rhsExpr.getValue());
            code.add(line);
            for (int i = 0; i < ttp.getFields().size(); i++) {
                line = fmt("%s.var%d = (%s).var%d;", vName, fldNum, value, i);
                code.add(line);
                fldNum++;
            }
            return makeExpression(code, vName, expr);
        }
        fail("Implement handling of " + expr.toString() + " in TupleTypeID.convertExprNode.");
        return null;
    }

    /**
     * Generate a class for the tuple type, and add it to the type names.
     *
     * @param tids Types of the fields.
     * @param ctxt Code generator context.
     */
    private void addSelf(List<TypeID> tids, CodeGeneratorContext ctxt) {
        JavaClass cls = ctxt.addJavaClass(className, false, null, null);
        cls.addVariable("private final ChiCoordinator chiCoordinator;");
        cls.addImport(Constants.COORDINATOR_FQC, false);

        for (int i = 0; i < tids.size(); i++) {
            TypeID tid = tids.get(i);
            String line = fmt("public %s var%d;", tid.getJavaType(), i);
            cls.addVariable(line);
        }

        JavaMethod jm;

        // Default constructor, produces non-initialized objects.
        jm = new JavaMethod("public " + className + "(ChiCoordinator chiCoordinator)");
        jm.lines.add("this.chiCoordinator = chiCoordinator;");
        // Does not initialize its fields!!
        cls.addMethod(jm);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // 'makeDefault' static method for creating an initialized tuple value.
        jm = new JavaMethod(
                "public static " + className + " makeDefault(Specification spec, ChiCoordinator chiCoordinator)");
        jm.lines.add(className + " tuple = new %s(chiCoordinator);", className);
        VBox b = new VBox();
        for (int i = 0; i < tids.size(); i++) {
            TypeID tid = tids.get(i);
            String vName = "tuple.var" + String.valueOf(i);
            tid.assignInitialValue(vName, null, b, ctxt, cls);
        }
        jm.lines.add(b);
        jm.lines.add("return tuple;");
        cls.addMethod(jm);

        // Copy constructor.
        jm = new JavaMethod("public " + className + "(" + className + " orig, boolean deepCopy)");
        jm.lines.add("this.chiCoordinator = orig.chiCoordinator;");
        for (int i = 0; i < tids.size(); i++) {
            TypeID tid = tids.get(i);
            String vName = "var" + String.valueOf(i);
            String line;
            if (tid.hasDeepCopy()) {
                line = fmt("%s = deepCopy ? (%s) : orig.%s;", vName, tid.getDeepCopyName(vName, cls, true), vName);
            } else {
                line = vName + " = orig." + vName + ";";
            }
            jm.lines.add(line);
        }
        cls.addMethod(jm);

        // Read method.
        if (isPrintable()) {
            jm = new JavaMethod(
                    "public static " + className + " read(ChiCoordinator chiCoordinator, ChiFileHandle stream)");
            jm.lines.add(className + " result = new %s(chiCoordinator);", className);
            jm.lines.add();
            jm.lines.add("stream.expectCharacter('(');");
            for (int i = 0; i < tids.size(); i++) {
                if (i > 0) {
                    jm.lines.add("stream.expectCharacter(',');");
                }
                TypeID tid = tids.get(i);
                String vName = "var" + String.valueOf(i);
                jm.lines.add("result.%s = %s;", vName, tid.getReadName("stream", cls));
            }
            jm.lines.add();
            jm.lines.add("stream.expectCharacter(')');");
            jm.lines.add("return result;");
            cls.addMethod(jm);
            cls.addImport(Constants.CHI_FILE_HANDLE_FQC, false);
            cls.addImport(Constants.COORDINATOR_FQC, false);
        }

        // Write method.
        if (isPrintable()) {
            jm = new JavaMethod("public void write(ChiFileHandle stream)");
            jm.lines.add("stream.write(\"(\");");
            for (int i = 0; i < tids.size(); i++) {
                if (i > 0) {
                    jm.lines.add("stream.write(\", \");");
                }
                TypeID tid = tids.get(i);
                String vName = "var" + String.valueOf(i);
                jm.lines.add(tid.getWriteName("stream", vName, cls));
            }
            jm.lines.add("stream.write(\")\");");
            cls.addMethod(jm);
            cls.addImport(Constants.CHI_FILE_HANDLE_FQC, false);

            jm = new JavaMethod("public String toString()");
            jm.lines.add("ChiWriteMemoryFile mem = new ChiWriteMemoryFile();");
            jm.lines.add("write(mem);");
            jm.lines.add("return mem.getData();");
            cls.addMethod(jm);
            cls.addImport(Constants.CHI_WRITE_MEMORY_FILE_FQC, false);
        }

        // Hashing
        jm = new JavaMethod("public int hashCode()");
        jm.lines.add("int hash = 743;");
        for (int i = 0; i < tids.size(); i++) {
            TypeID tid = tids.get(i);
            String vName = "var" + String.valueOf(i);
            jm.lines.add("hash = hash + %d * (%s);", i, tid.getHashCodeName(vName, cls));
        }
        jm.lines.add("return hash;");
        cls.addMethod(jm);

        // Equality.
        jm = new JavaMethod("public boolean equals(Object obj)");
        jm.lines.add("if (!(obj instanceof %s)) return false;", className);
        jm.lines.add(className + " other = (%s)obj;", className);
        for (int i = 0; i < tids.size(); i++) {
            TypeID tid = tids.get(i);
            String vName = "var" + String.valueOf(i);
            jm.lines.add("if (%s) return false;", tid.getUnequal(vName, "other." + vName));
        }
        jm.lines.add("return true;");
        cls.addMethod(jm);
    }
}
