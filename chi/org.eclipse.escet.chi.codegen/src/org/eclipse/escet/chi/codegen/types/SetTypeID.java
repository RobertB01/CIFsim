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

import static org.eclipse.escet.chi.codegen.Constants.CHI_SIMULATOR_EXCEPTION_FQC;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTupleTypeID;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.CodeExpression;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.SetExpression;
import org.eclipse.escet.common.java.Assert;

/** Class representing a set type in the code generator. */
public class SetTypeID extends CoordObjectTypeID {
    /** Class name of the set type. */
    private String className;

    /**
     * Constructor of the {@link SetTypeID} class.
     *
     * @param elmTid Type id of the element type.
     * @param ctxt Code generator context.
     */
    public SetTypeID(TypeID elmTid, CodeGeneratorContext ctxt) {
        super(TypeKind.SET, list(elmTid));

        if (!ctxt.hasTypeName(this)) {
            className = ctxt.makeUniqueName("SetType");
            ctxt.addTypeName(this, className);
            addSelf(ctxt);
        } else {
            className = ctxt.getTypeName(this);
        }
    }

    @Override
    public String getTypeText() {
        return "set " + first(subTypes).getTypeText();
    }

    @Override
    public String getJavaClassType() {
        return className;
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        if (expr instanceof SetExpression) {
            SetExpression le = (SetExpression)expr;

            String setName = ctxt.makeUniqueName("set");
            List<String> lines = list();

            String clsName = getJavaClassType();
            String line = fmt("%s %s = new %s(chiCoordinator, %d);", clsName, setName, clsName,
                    le.getElements().size());
            lines.add(line);

            for (Expression elm: le.getElements()) {
                ExpressionBase eb = convertExpression(elm, ctxt, currentFile);
                lines.addAll(eb.getCode());
                line = fmt("%s.add(%s);", setName, eb.getValue());
                lines.add(line);
            }
            return makeExpression(lines, setName, expr);
        }

        if (expr instanceof BinaryExpression) {
            BinaryExpression binexp = (BinaryExpression)expr;
            ExpressionBase left, right;
            left = convertExpression(binexp.getLeft(), ctxt, currentFile);
            right = convertExpression(binexp.getRight(), ctxt, currentFile);
            String text;
            switch (binexp.getOp()) {
                case ADDITION:
                    text = fmt("%s.unionSet((%s), (%s))", className, left.getValue(), right.getValue());
                    break;

                case SUBTRACTION:
                    text = fmt("%s.subtractSet((%s), (%s))", className, left.getValue(), right.getValue());
                    break;

                case MULTIPLICATION:
                    text = fmt("%s.intersectSet((%s), (%s))", className, left.getValue(), right.getValue());
                    break;

                case ELEMENT_TEST: // Note that right and left are swapped.
                    text = fmt("(%s).contains(%s)", right.getValue(), left.getValue());
                    break;

                case SUBSET: // Note that right and left are swapped.
                    text = fmt("(%s).containsAll(%s)", right.getValue(), left.getValue());
                    break;

                default:
                    Assert.fail("Unimplemented binary set operator " + expr.toString());
                    return null;
            }
            if (!left.getCode().isEmpty() || !right.getCode().isEmpty()) {
                List<String> lines = list();
                lines.addAll(left.getCode());
                lines.addAll(right.getCode());
                return new CodeExpression(lines, text, expr);
            }
            return new SimpleExpression(text, expr);
        }

        Assert.fail("Implement " + expr.toString() + " in SetTypeID.convertExprNode");
        return null;
    }

    /**
     * Generate a class file for the set type in run-time, and add it to the output.
     *
     * @param ctxt Code generator context.
     */
    private void addSelf(CodeGeneratorContext ctxt) {
        TypeID elmTid = first(subTypes);
        String elmClassname = elmTid.getJavaClassType();

        String iface = "LinkedHashSet<" + elmClassname + ">";
        JavaClass cls = new JavaClass("", false, className, iface, null);
        cls.addImport("java.util.LinkedHashSet", false);

        cls.addVariable("private final ChiCoordinator chiCoordinator;");
        cls.addImport(Constants.COORDINATOR_FQC, false);

        addConstructors(cls, ctxt);
        addSetFunctions(cls);
        addReadWrite(cls);
        ctxt.addClass(cls);
    }

    /**
     * Add constructors of the set.
     *
     * @param cls Set class being generated.
     * @param ctxt Code generator context.
     */
    private void addConstructors(JavaFile cls, CodeGeneratorContext ctxt) {
        TypeID elmTid = first(subTypes);
        String elmTypename = elmTid.getJavaType();
        String elmClassname = elmTid.getJavaClassType();

        JavaMethod jm;

        // Default constructor.
        jm = new JavaMethod("public " + className + "(ChiCoordinator chiCoordinator)");
        jm.lines.add("this(chiCoordinator, 0);");
        cls.addMethod(jm);

        // Constructor for non-zero length set.
        //
        // @param size Expected initial number of items.
        jm = new JavaMethod("public " + className + "(ChiCoordinator chiCoordinator, int size)");
        jm.lines.add("super(size);");
        jm.lines.add("this.chiCoordinator = chiCoordinator;");
        cls.addMethod(jm);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // Copy constructor, makes a shallow or deep copy of the original.
        //
        // @param orig Original set to copy.
        // @param deepCopy Perform deep copy of the value.
        boolean elmHasDeepcopy = elmTid.hasDeepCopy();
        jm = new JavaMethod("public " + className + "(" + className + " orig, boolean deepCopy)");
        jm.lines.add("super(orig.size());");
        jm.lines.add("this.chiCoordinator = orig.chiCoordinator;");
        if (!elmHasDeepcopy) {
            jm.lines.add("addAll(orig);");
        } else {
            String deepCopy = elmTid.getDeepCopyName("val", cls, true);
            jm.lines.add("if (!deepCopy) {");
            jm.lines.add("   addAll(orig);");
            jm.lines.add("} else {");
            jm.lines.add("    for (%s val: orig) {", elmTypename);
            jm.lines.add("        add(%s);", deepCopy);
            jm.lines.add("    }");
            jm.lines.add("}");
        }
        cls.addMethod(jm);

        // Poppin' on a set.
        List<String> fields = list("value", "set");
        List<TypeID> tids = list(elmTid, this);
        TypeID tupleTid = createTupleTypeID(fields, tids, ctxt);
        String tupName = tupleTid.getJavaClassType();

        jm = new JavaMethod("public " + tupName + " pop()");
        jm.lines.add("if (isEmpty()) throw new ChiSimulatorException(\"Cannot pop value from an empty set.\");");
        jm.lines.add("%s res = new %s(chiCoordinator);", tupName, tupName);
        jm.lines.add("res.var1 = new %s(this, false);", className);
        jm.lines.add("Iterator<%s> iter = res.var1.iterator();", elmClassname);
        jm.lines.add("res.var0 = iter.next();");
        jm.lines.add("iter.remove();");
        jm.lines.add("return res;");
        cls.addMethod(jm);
        cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        cls.addImport("java.util.Iterator", false);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // Delete element from set.
        jm = new JavaMethod(
                "public static " + className + " removeElement(" + className + " set, " + elmTypename + " value)");
        jm.lines.add(className + " res = new %s(set, false);", className);
        jm.lines.add("if (res.remove(value)) return res;");
        jm.lines.add("return set;");
        cls.addMethod(jm);
    }

    /**
     * Add set support functions.
     *
     * @param cls Set class being generated.
     */
    private void addSetFunctions(JavaFile cls) {
        TypeID elmTid = first(subTypes);
        String elmTypename = elmTid.getJavaType();
        String elmClassname = elmTid.getJavaClassType();

        JavaMethod jm;

        // Union of two sets.
        //
        // @param left First set.
        // @param right Second set.
        jm = new JavaMethod(
                "public static " + className + " unionSet(" + className + " left, " + className + " right)");
        jm.lines.add("%s result = new %s(left.chiCoordinator, left.size() + right.size());", className, className);
        jm.lines.add("result.addAll(left);");
        jm.lines.add("result.addAll(right);");
        jm.lines.add("return result;");
        cls.addMethod(jm);

        // Set subtraction.
        //
        // @param left First set.
        // @param right Second set.
        jm = new JavaMethod(
                "public static " + className + " subtractSet(" + className + " left, " + className + " right)");
        jm.lines.add("// Construct a result with an optimistic size.");
        jm.lines.add("%s result = new %s(left.chiCoordinator, left.size());", className, className);
        jm.lines.add();
        jm.lines.add("for (%s val: left) {", elmTypename);
        jm.lines.add("    if (!right.contains(val)) result.add(val);");
        jm.lines.add("}");
        jm.lines.add("return result;");
        cls.addMethod(jm);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // Set intersection.
        //
        // @param left First set.
        // @param right Second set.
        jm = new JavaMethod(
                "public static " + className + " intersectSet(" + className + " left, " + className + " right)");
        jm.lines.add("%s result;", className);
        jm.lines.add("int sz = left.size();");
        jm.lines.add("if (sz > right.size()) {");
        jm.lines.add("    sz = right.size();");
        jm.lines.add("    result = new %s(left.chiCoordinator, sz);", className);
        jm.lines.add("    for (%s val: right) {", elmTypename);
        jm.lines.add("        if (left.contains(val)) result.add(val);");
        jm.lines.add("    }");
        jm.lines.add("} else {");
        jm.lines.add("    result = new %s(left.chiCoordinator, sz);", className);
        jm.lines.add("    for (%s val: left) {", elmTypename);
        jm.lines.add("        if (right.contains(val)) result.add(val);");
        jm.lines.add("    }");
        jm.lines.add("}");
        jm.lines.add("return result;");
        cls.addMethod(jm);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // Minimum and maximum functions.
        String smallestText = null;
        String biggestText = null;
        if (elmTid.kind == TypeKind.INT || elmTid.kind == TypeKind.REAL) {
            smallestText = "result > value";
            biggestText = "result < value";
        } else if (elmTid.kind == TypeKind.STRING) {
            smallestText = "result.compareTo(value) > 0";
            biggestText = "result.compareTo(value) < 0";
        }
        if (smallestText != null) {
            jm = new JavaMethod("public " + elmTypename + " getMinimum()");
            jm.lines.add("%s result = null;", elmClassname);
            jm.lines.add("for (%s value: this) {", elmClassname);
            jm.lines.add("    if (result == null || %s) result = value;", smallestText);
            jm.lines.add("}");
            jm.lines.add("if (result == null) throw new ChiSimulatorException("
                    + "\"Cannot find a smallest value in an empty set.\");");
            jm.lines.add("return result;");
            cls.addMethod(jm);
            cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        }
        if (biggestText != null) {
            jm = new JavaMethod("public " + elmTypename + " getMaximum()");
            jm.lines.add("%s result = null;", elmClassname);
            jm.lines.add("for (%s value: this) {", elmClassname);
            jm.lines.add("    if (result == null || %s) result = value;", biggestText);
            jm.lines.add("}");
            jm.lines.add("if (result == null) throw new ChiSimulatorException("
                    + "\"Cannot find a biggest value in an empty set.\");");
            jm.lines.add("return result;");
            cls.addMethod(jm);
            cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        }
    }

    /**
     * Add read and write functions.
     *
     * @param cls Set class being generated.
     */
    private void addReadWrite(JavaFile cls) {
        if (!isPrintable()) {
            return;
        }

        TypeID elmTid = first(subTypes);
        String elmTypename = elmTid.getJavaType();
        String elmClassname = elmTid.getJavaClassType();

        JavaMethod jm;

        // Construct a new set by reading from an input stream.
        //
        // @param stream Input stream to read the set from.
        // @return The created set.
        jm = new JavaMethod(
                "public static " + className + " read(ChiCoordinator chiCoordinator, ChiFileHandle stream)");
        jm.lines.add(className + " result = new %s(chiCoordinator);", className);
        jm.lines.add();
        jm.lines.add("stream.expectCharacter('{');");
        jm.lines.add("for (;;) {");
        jm.lines.add("    %s elm = %s;", elmTypename, elmTid.getReadName("stream", cls));
        jm.lines.add("    result.add(elm);");
        jm.lines.add("    int ch = stream.expectCharacter(',', '}');");
        jm.lines.add("    if (ch == '}') break;");
        jm.lines.add("    if (ch == ',') continue;");
        jm.lines.add("}");
        jm.lines.add("return result;");
        cls.addMethod(jm);
        cls.addImport(Constants.CHI_FILE_HANDLE_FQC, false);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // Write the set to the output stream.
        //
        // @param stream Output stream to write the set to.
        jm = new JavaMethod("public void write(ChiFileHandle stream)");
        jm.lines.add("stream.write(\"{\");");
        jm.lines.add("boolean first = true;");
        jm.lines.add("for (%s val: this) {", elmClassname);
        jm.lines.add("    if (!first) stream.write(\", \");");
        jm.lines.add("    first = false;");
        jm.lines.add("    " + elmTid.getWriteName("stream", "val", cls));
        jm.lines.add("}");
        jm.lines.add("stream.write(\"}\");");
        cls.addMethod(jm);
        cls.addImport(Constants.CHI_FILE_HANDLE_FQC, false);

        jm = new JavaMethod("public String toString()");
        jm.lines.add("ChiWriteMemoryFile mem = new ChiWriteMemoryFile();");
        jm.lines.add("write(mem);");
        jm.lines.add("return mem.getData();");
        cls.addMethod(jm);
        cls.addImport(Constants.CHI_WRITE_MEMORY_FILE_FQC, false);
    }
}
