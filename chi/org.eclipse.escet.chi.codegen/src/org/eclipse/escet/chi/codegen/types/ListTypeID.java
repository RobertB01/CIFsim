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
import static org.eclipse.escet.chi.codegen.Constants.CHI_SIMULATOR_EXCEPTION_FQC;
import static org.eclipse.escet.chi.codegen.Constants.INDENT_SIZE;
import static org.eclipse.escet.chi.codegen.Constants.INDEXABLE_DEQUE_FQC;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTupleTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.dropTypeReferences;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.makeBooleanTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.makeFunctionTypeID;
import static org.eclipse.escet.common.java.Assert.check;
import static org.eclipse.escet.common.java.Assert.fail;
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
import org.eclipse.escet.chi.metamodel.chi.ListExpression;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.SliceExpression;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.common.box.VBox;

/** Class representing a list type. TODO For long literal lists/sets, we may want to construct an array. */
public class ListTypeID extends CoordObjectTypeID {
    /** Class name of the list type. */
    private String className;

    /**
     * Constructor of the {@link ListTypeID} class.
     *
     * @param elmTid Type id of the element type.
     * @param ctxt Code generator context.
     */
    public ListTypeID(TypeID elmTid, CodeGeneratorContext ctxt) {
        super(TypeKind.LIST, list(elmTid));

        if (!ctxt.hasTypeName(this)) {
            className = ctxt.makeUniqueName("ListType");
            ctxt.addTypeName(this, className);
            addSelf(ctxt);
        } else {
            className = ctxt.getTypeName(this);
        }
    }

    @Override
    public String getTypeText() {
        return "list " + first(subTypes).getTypeText();
    }

    @Override
    public String getJavaClassType() {
        return className;
    }

    @Override
    public void assignInitialValue(String name, Type tp, VBox box, CodeGeneratorContext ctxt, JavaFile currentfile) {
        Expression initialLength;
        if (tp == null) {
            initialLength = null;
        } else {
            ListType ltp = (ListType)tp;
            initialLength = ltp.getInitialLength();
            tp = dropTypeReferences(ltp.getElementType());
        }

        if (initialLength == null) {
            String line = fmt("%s = new %s(chiCoordinator);", name, className);
            box.add(line);
            return;
        }

        // Non-null initial length.

        // Code:
        //
        // dest = new ListClass();
        // int i = <initial-length value>
        // while (i > 0) {
        // ElementType val;
        // val = <assign default value of ElementType>
        // dest.add(val);
        // i--;
        // }

        ExpressionBase lenExpr = convertExpression(initialLength, ctxt, currentfile);
        box.add(lenExpr.getCode());
        String line;
        String locVar = ctxt.makeUniqueName("i");
        line = fmt("int %s = %s;", locVar, lenExpr.getValue());
        box.add(line);
        line = fmt("%s = new %s(chiCoordinator, %s);", name, className, locVar);
        box.add(line);

        line = fmt("while (%s > 0) {", locVar);
        box.add(line);

        VBox vb = new VBox(INDENT_SIZE);
        TypeID elmTid = first(subTypes);
        String locVal = ctxt.makeUniqueName("val");
        line = fmt("%s %s;", elmTid.getJavaType(), locVal);
        vb.add(line);

        elmTid.assignInitialValue(locVal, tp, vb, ctxt, currentfile);
        line = fmt("%s.append(%s);", name, locVal);
        vb.add(line);
        line = fmt("%s--;", locVar);
        vb.add(line);
        box.add(vb);

        box.add("}");
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        if (expr instanceof ListExpression) {
            ListExpression le = (ListExpression)expr;

            String lstName = ctxt.makeUniqueName("lst");
            List<String> lines = list();
            String line;

            String clsName = getJavaClassType();
            line = fmt("%s %s = new %s(chiCoordinator, %d);", clsName, lstName, clsName, le.getElements().size());
            lines.add(line);

            for (Expression elm: le.getElements()) {
                ExpressionBase eb = convertExpression(elm, ctxt, currentFile);
                lines.addAll(eb.getCode());
                line = fmt("%s.append(%s);", lstName, eb.getValue());
                lines.add(line);
            }
            return makeExpression(lines, lstName, expr);
        }

        if (expr instanceof SliceExpression) {
            SliceExpression se = (SliceExpression)expr;
            String lstName = ctxt.makeUniqueName("lst");
            List<String> lines = list();

            String line = getJavaClassType();
            line = fmt("%s %s = new %s(chiCoordinator);", line, lstName, line);
            lines.add(line);

            ExpressionBase eb;

            // Assign source to 'srcName'.
            eb = convertExpression(se.getSource(), ctxt, currentFile);
            lines.addAll(eb.getCode());
            String srcName = ctxt.makeUniqueName("src");
            line = fmt("%s %s = %s;", className, srcName, eb.getValue());
            lines.add(line);

            // Get step in 'stepName'.
            String stepName = ctxt.makeUniqueName("step");
            if (se.getStep() == null) {
                line = fmt("int %s = 1;", stepName);
                lines.add(line);
            } else {
                eb = convertExpression(se.getStep(), ctxt, currentFile);
                lines.addAll(eb.getCode());
                line = fmt("int %s = %s;", stepName, eb.getValue());
                lines.add(line);
            }

            // Get start in 'startName'.
            String startName = ctxt.makeUniqueName("start");
            if (se.getStart() == null) {
                line = fmt("int %s = (%s >= 0) ? 0 : %s.size()-1;", startName, stepName, srcName);
                lines.add(line);
            } else {
                eb = convertExpression(se.getStart(), ctxt, currentFile);
                lines.addAll(eb.getCode());
                line = fmt("int %s = %s;", startName, eb.getValue());
                lines.add(line);
                if (se.getStep() == null) {
                    // For xs[a:b] do index normalization.
                    line = fmt("if (%s < 0) %s = %s.size() + %s;", startName, startName, srcName, startName);
                    lines.add(line);
                }
            }

            // Get end in 'endName'.
            String endName = ctxt.makeUniqueName("end");
            if (se.getEnd() == null) {
                line = fmt("int %s = (%s >= 0) ? %s.size() : -1;", endName, stepName, srcName);
                lines.add(line);
            } else {
                eb = convertExpression(se.getEnd(), ctxt, currentFile);
                lines.addAll(eb.getCode());
                line = fmt("int %s = %s;", endName, eb.getValue());
                lines.add(line);
                if (se.getStep() == null) {
                    // For xs[a:b] do index normalization.
                    line = fmt("if (%s < 0) %s = %s.size() + %s;", endName, endName, srcName, endName);
                    lines.add(line);
                }
            }

            String elmType = first(subTypes).getJavaClassType();
            String iterVar = ctxt.makeUniqueName("iter");
            line = fmt("Iterator<%s> %s = %s.iterator(%s, %s, %s);", elmType, iterVar, srcName, startName, stepName,
                    endName);
            lines.add(line);
            currentFile.addImport("java.util.Iterator", false);
            line = fmt("while (%s.hasNext()) { %s.append(%s.next()); }", iterVar, lstName, iterVar);
            lines.add(line);
            return makeExpression(lines, lstName, expr);
        } else if (expr instanceof BinaryExpression) {
            BinaryExpression binexp = (BinaryExpression)expr;
            ExpressionBase left, right;
            left = convertExpression(binexp.getLeft(), ctxt, currentFile);
            right = convertExpression(binexp.getRight(), ctxt, currentFile);
            String text;
            switch (binexp.getOp()) {
                case ADDITION:
                    text = fmt("(%s).addList(%s)", left.getValue(), right.getValue());
                    break;

                case SUBTRACTION:
                    text = fmt("(%s).subtractList(%s)", left.getValue(), right.getValue());
                    break;

                case PROJECTION:
                    text = fmt("(%s).get(%s)", left.getValue(), right.getValue());
                    break;

                case ELEMENT_TEST: // Note that right and left are swapped.
                    text = fmt("(%s).contains(%s)", right.getValue(), left.getValue());
                    break;

                default:
                    fail("Unimplemented binary list operator " + expr.toString());
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

        fail("Implement " + expr.toString() + " in ListTypeID.convertExprNode");
        return null;
    }

    /**
     * Generate a class for the list type, and add it to the type names.
     *
     * @param ctxt Code generator context.
     */
    private void addSelf(CodeGeneratorContext ctxt) {
        TypeID elmTid = first(subTypes);
        String elmClassname = elmTid.getJavaClassType();

        String base = "IndexableDeque<" + elmClassname + ">";
        JavaClass cls = new JavaClass("", false, className, base, null);
        cls.addImport(INDEXABLE_DEQUE_FQC, false);

        addConstructors(cls);
        addSizeSupportCode(cls);
        addListMethods(cls, ctxt);
        addReadWrite(cls);

        if (elmTid.isIntTypeID()) {
            addRangeFunctions(cls);
        }

        ctxt.addClass(cls);
    }

    /**
     * Add constructors to the list class.
     *
     * @param cls List class being generated.
     */
    private void addConstructors(JavaClass cls) {
        TypeID elmTid = first(subTypes);
        String elmClassname = elmTid.getJavaClassType();

        cls.addVariable("private static final int MAX_SUBTRACTLENGTH = 5;");
        cls.addVariable("private final ChiCoordinator chiCoordinator;");
        cls.addImport(Constants.COORDINATOR_FQC, false);

        JavaMethod jm;

        // Default constructor.
        jm = new JavaMethod("public " + className + "(ChiCoordinator chiCoordinator)");
        jm.lines.add("super();");
        jm.lines.add("this.chiCoordinator = chiCoordinator;");
        cls.addMethod(jm);

        // Constructor for a non-zero length list.
        //
        // @param initLength Initial length of the list.
        jm = new JavaMethod("public " + className + "(ChiCoordinator chiCoordinator, int sz)");
        jm.lines.add("super(sz);");
        jm.lines.add("this.chiCoordinator = chiCoordinator;");
        cls.addMethod(jm);

        // Copy constructor, makes a shallow or deep copy of the original.
        //
        // @param orig Original list to copy.
        // @param deepCopy Perform deep copy of the value.
        boolean elmHasDeepcopy = elmTid.hasDeepCopy();
        jm = new JavaMethod("public " + className + "(" + className + " orig, boolean deepCopy)");
        jm.lines.add("super(orig.size());");
        jm.lines.add("this.chiCoordinator = orig.chiCoordinator;");
        jm.lines.add("for (%s val: orig) {", elmClassname);
        if (!elmHasDeepcopy) {
            jm.lines.add("    append(val);");
        } else {
            jm.lines.add("    append(deepCopy ? %s : val);", elmTid.getDeepCopyName("val", cls, true));
        }
        jm.lines.add("}");
        cls.addMethod(jm);
    }

    /**
     * Add functions for internal support.
     *
     * @param cls List class being generated,
     */
    private void addSizeSupportCode(JavaFile cls) {
        TypeID elmTid = first(subTypes);
        String elmTypename = elmTid.getJavaType();

        JavaMethod jm;

        // Modify an element of the list, returning a shallow copied list.
        //
        // @param index Index of the element to change.
        // @param newVal New value to assign.
        jm = new JavaMethod("public " + className + " modify(int index, " + elmTypename + " newVal)");
        jm.lines.add(className + " result = new %s(this, false);", className);
        jm.lines.add("result.set(index, newVal);");
        jm.lines.add("return result;");
        cls.addMethod(jm);
    }

    /**
     * Add list operations to the list class.
     *
     * @param cls Java list class being generated.
     * @param ctxt Code generator context.
     */
    private void addListMethods(JavaFile cls, CodeGeneratorContext ctxt) {
        TypeID elmTid = first(subTypes);
        String elmTypename = elmTid.getJavaType();
        String elmClassname = elmTid.getJavaClassType();
        String elmSimpleValue = elmTid.getSimplestJavaValue();

        JavaMethod jm;

        // Subtract a list from this list. If needed, the method makes a shallow copy of the list.
        //
        // @param sub List to subtract.
        // @return The resulting list.
        jm = new JavaMethod("public " + className + " subtractList(" + className + " sub)");
        jm.lines.add("int sz = sub.size();");
        jm.lines.add("// No elements to subtract -> trivially finished.");
        jm.lines.add("if (sz == 0) return this;");
        jm.lines.add();
        jm.lines.add("%s result = new %s(chiCoordinator, size() - sz / 2);", className, className);
        jm.lines.add();
        jm.lines.add("// Only a few elements to subtract.");
        jm.lines.add("if (sz < MAX_SUBTRACTLENGTH) {");
        jm.lines.add("    %s[] elms = new %s[MAX_SUBTRACTLENGTH];", elmClassname, elmClassname);
        jm.lines.add("    sub.copyToArray(elms);");
        jm.lines.add();
        jm.lines.add("    for (%s val: this) {", elmClassname);
        jm.lines.add("        int m = 0;");
        jm.lines.add("        while (m < sz && %s) m++;", elmTid.getUnequal("elms[m]", "val"));
        jm.lines.add("        if (m < sz) {");
        jm.lines.add("            // Found a match, remove the element and continue.");
        jm.lines.add("            sz--;");
        jm.lines.add("            if (m != sz) elms[m] = elms[sz];");
        jm.lines.add("            elms[sz] = %s;", elmSimpleValue);
        jm.lines.add("            continue;");
        jm.lines.add("        }");
        jm.lines.add("        // No match, copy the element.");
        jm.lines.add("        result.append(val);");
        jm.lines.add("    }");
        jm.lines.add("    return result;");
        jm.lines.add("}");
        jm.lines.add();
        jm.lines.add("// Many elements to subtract.");
        jm.lines.add("Map<%s, Integer> counts = new LinkedHashMap<%s, Integer>();", elmClassname, elmClassname);
        jm.lines.add("for (%s val: sub) {", elmClassname);
        jm.lines.add("    Integer cnt = counts.get(val);");
        jm.lines.add("    counts.put(val, ((cnt == null) ? 1 : cnt + 1));");
        jm.lines.add("}");
        jm.lines.add();
        jm.lines.add("for (%s val: this) {", elmClassname);
        jm.lines.add("     Integer cnt = counts.get(val);");
        jm.lines.add("     if (cnt == null || cnt <= 0) {");
        jm.lines.add("        result.append(val);");
        jm.lines.add("    } else {");
        jm.lines.add("        counts.put(val, cnt - 1);");
        jm.lines.add("    }");
        jm.lines.add("}");
        jm.lines.add("return result;");
        cls.addMethod(jm);
        cls.addImport("java.util.Map", false);
        cls.addImport("java.util.LinkedHashMap", false);

        // Concatenate the other list at the end of this list, making a shallow copy if considered necessary.
        jm = new JavaMethod("public " + className + " addList(" + className + " other)");
        jm.lines.add("if (isEmpty()) return other;");
        jm.lines.add();
        jm.lines.add("int sz = other.size();");
        jm.lines.add("if (sz == 0) return this;");
        jm.lines.add();
        jm.lines.add("%s result = new %s(chiCoordinator, size() + sz);", className, className);
        jm.lines.add("for (%s val: this) {", elmClassname);
        jm.lines.add("    result.append(val);");
        jm.lines.add("}");
        jm.lines.add("for (%s val: other) {", elmClassname);
        jm.lines.add("    result.append(val);");
        jm.lines.add("}");
        jm.lines.add("return result;");
        cls.addMethod(jm);

        // List poppin' method.
        List<String> fields = list("value", "list");
        List<TypeID> tids = list(elmTid, this);
        TypeID tupleTid = createTupleTypeID(fields, tids, ctxt);
        String tupName = tupleTid.getJavaClassType();

        jm = new JavaMethod("public " + tupName + " pop()");
        jm.lines.add(tupName + " res = new %s(chiCoordinator);", tupName);
        jm.lines.add("res.var1 = new %s(this, false);", className);
        jm.lines.add("res.var0 = res.var1.removeHead();");
        jm.lines.add("return res;");
        cls.addMethod(jm);

        // Insert function.
        List<TypeID> subs = list(elmTid, elmTid, makeBooleanTypeID());
        TypeID predTid = makeFunctionTypeID(subs, ctxt);
        String predClassName = predTid.getJavaClassType();

        jm = new JavaMethod("public static " + className + " insert(" + className + " lst, " + elmTypename + " elm, "
                + predClassName + " pred)");
        jm.lines.add("lst = new %s(lst, false);", className);
        jm.lines.add("lst.insert(elm, pred);");
        jm.lines.add("return lst;");
        cls.addMethod(jm);

        // Sort function.
        jm = new JavaMethod("public static " + className + " sort(" + className + " lst, " + predClassName + " pred)");
        jm.lines.add("lst = new %s(lst, false);", className);
        jm.lines.add("try {");
        jm.lines.add("    lst.sort(pred);");
        jm.lines.add("} catch (IllegalArgumentException e) {");
        jm.lines.add("    throw new ChiSimulatorException("
                + "\"Sort predicate function does not define a proper element order.\", e);");
        jm.lines.add("}");
        jm.lines.add("return lst;");
        cls.addMethod(jm);
        cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);

        // Remove function.
        jm = new JavaMethod("public static " + className + " removeElement(" + className + " lst, int idx)");
        jm.lines.add("lst = new %s(lst, false);", className);
        jm.lines.add("lst.remove(idx);");
        jm.lines.add("return lst;");
        cls.addMethod(jm);

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
                    + "\"Cannot find a smallest value in an empty list.\");");
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
                    + "\"Cannot find a biggest value in an empty list.\");");
            jm.lines.add("return result;");
            cls.addMethod(jm);
            cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        }
    }

    /**
     * Add read and write functions.
     *
     * @param cls List class being generated.
     */
    private void addReadWrite(JavaFile cls) {
        if (!isPrintable()) {
            return;
        }

        TypeID elmTid = first(subTypes);
        String elmTypename = elmTid.getJavaType();
        String elmClassname = elmTid.getJavaClassType();

        JavaMethod jm;

        // Construct a new list by reading from an input stream.
        //
        // @param stream Input stream to read the list from.
        // @return The created list.
        jm = new JavaMethod(
                "public static " + className + " read(ChiCoordinator chiCoordinator, ChiFileHandle stream)");
        jm.lines.add("%s result = new %s(chiCoordinator);", className, className);
        jm.lines.add();
        jm.lines.add("stream.expectCharacter('[');");
        jm.lines.add("for (;;) {");
        jm.lines.add("    %s elm = %s;", elmTypename, elmTid.getReadName("stream", cls));
        jm.lines.add("    result.append(elm);");
        jm.lines.add("    int ch = stream.expectCharacter(',', ']');");
        jm.lines.add("    if (ch == ']') break;");
        jm.lines.add("    if (ch == ',') continue;");
        jm.lines.add("}");
        jm.lines.add("return result;");
        cls.addMethod(jm);
        cls.addImport(CHI_FILE_HANDLE_FQC, false);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // Write the list to the output stream.
        //
        // @param stream Output stream to write the list to.
        jm = new JavaMethod("public void write(ChiFileHandle stream)");
        jm.lines.add("stream.write(\"[\");");
        jm.lines.add("boolean first = true;");
        jm.lines.add("for (%s val: this) {", elmClassname);
        jm.lines.add("    if (!first) stream.write(\", \");");
        jm.lines.add("    first = false;");
        jm.lines.add("    " + elmTid.getWriteName("stream", "val", cls));
        jm.lines.add("}");
        jm.lines.add("stream.write(\"]\");");
        cls.addMethod(jm);
        cls.addImport(CHI_FILE_HANDLE_FQC, false);

        jm = new JavaMethod("public String toString()");
        jm.lines.add("ChiWriteMemoryFile mem = new ChiWriteMemoryFile();");
        jm.lines.add("write(mem);");
        jm.lines.add("return mem.getData();");
        cls.addMethod(jm);
        cls.addImport(Constants.CHI_WRITE_MEMORY_FILE_FQC, false);
    }

    /**
     * Create static 'range' functions. Element type should be the integer data type.
     *
     * @param cls List class being generated.
     */
    private void addRangeFunctions(JavaFile cls) {
        TypeID elmTid = first(subTypes);
        check(elmTid.isIntTypeID());

        JavaMethod jm;
        String line;

        // range(n)
        line = "public static " + className + " range(ChiCoordinator chiCoordinator, int end)";
        jm = new JavaMethod(line);
        jm.lines.add("return range(chiCoordinator, 0, end);");
        cls.addMethod(jm);

        // range(s, n)
        line = "public static " + className + " range(ChiCoordinator chiCoordinator, int start, int end)";
        jm = new JavaMethod(line);
        jm.lines.add("return range(chiCoordinator, start, end, 1);");
        cls.addMethod(jm);

        // range(s, n, step)
        line = "public static " + className + " range(ChiCoordinator chiCoordinator, int start, int end, int step)";
        jm = new JavaMethod(line);

        jm.lines.add("if (step == 0) {");
        jm.lines.add("    throw new ChiSimulatorException(\"Step size must be non-zero for range.\");");
        jm.lines.add("}");
        jm.lines.add();
        jm.lines.add("int length = 0;");
        jm.lines.add("if (step > 0) {");
        jm.lines.add("    if (end > start) length = (end - start + step - 1) / step;");
        jm.lines.add("} else {");
        jm.lines.add("    if (end < start) length = (end - start + step - 1) / step;");
        jm.lines.add("}");
        jm.lines.add("%s res = new %s(chiCoordinator, length);", className, className);
        jm.lines.add();
        jm.lines.add("if (step > 0) {");
        jm.lines.add("    while (start < end) {");
        jm.lines.add("        res.append(start);");
        jm.lines.add("        start += step;");
        jm.lines.add("    }");
        jm.lines.add("} else {");
        jm.lines.add("    while (start > end) {");
        jm.lines.add("        res.append(start);");
        jm.lines.add("        start += step;");
        jm.lines.add("    }");
        jm.lines.add("}");
        jm.lines.add("return res;");
        cls.addMethod(jm);
        cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
    }
}
