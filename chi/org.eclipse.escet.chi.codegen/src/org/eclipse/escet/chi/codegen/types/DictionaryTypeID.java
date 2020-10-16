//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.chi.codegen.OutputPosition.genCurrentPositionStatement;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTupleTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.makeListTypeID;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictionaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictionaryPair;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.common.java.Assert;

/** Class representing a dictionary type in the code generator. */
public class DictionaryTypeID extends CoordObjectTypeID {
    /** Class name of the dictionary type. */
    private String className;

    /**
     * Constructor of the {@link DictionaryTypeID} class.
     *
     * @param keyType Type of the keys.
     * @param valueType Type of the values.
     * @param ctxt Code generator context.
     */
    public DictionaryTypeID(TypeID keyType, TypeID valueType, CodeGeneratorContext ctxt) {
        super(TypeKind.DICTIONARY, list(keyType, valueType));

        if (!ctxt.hasTypeName(this)) {
            className = ctxt.makeUniqueName("DictType");
            ctxt.addTypeName(this, className);
            addSelf(ctxt);
        } else {
            className = ctxt.getTypeName(this);
        }
    }

    @Override
    public String getTypeText() {
        return fmt("dict(%s : %s)", subTypes.get(0).getTypeText(), subTypes.get(1).getTypeText());
    }

    @Override
    public String getJavaClassType() {
        return className;
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentfile) {
        if (expr instanceof DictionaryExpression) {
            List<String> lines = list();
            String line;
            DictionaryExpression de = (DictionaryExpression)expr;
            String dest = ctxt.makeUniqueName("dict");
            line = fmt("%s %s = new %s(chiCoordinator, %d);", className, dest, className, de.getPairs().size());
            lines.add(line);
            for (DictionaryPair dp: de.getPairs()) {
                ExpressionBase ke, ve;
                ke = convertExpression(dp.getKey(), ctxt, currentfile);
                ve = convertExpression(dp.getValue(), ctxt, currentfile);
                lines.addAll(ke.getCode());
                lines.addAll(ve.getCode());
                line = fmt("%s.put(%s, %s);", dest, ke.getValue(), ve.getValue());
                lines.add(line);
            }
            line = genCurrentPositionStatement(expr);
            if (line != null) {
                lines.add(line);
            }
            line = fmt(
                    "if (%s.size() != %d) throw new ChiSimulatorException(\"Literal dictionary "
                            + "has \" + String.valueOf(%d - %s.size()) + \" duplicate key(s).\");",
                    dest, de.getPairs().size(), de.getPairs().size(), dest);
            lines.add(line);
            currentfile.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
            return makeExpression(lines, dest, null);
        }

        if (expr instanceof BinaryExpression) {
            BinaryExpression binexp = (BinaryExpression)expr;
            ExpressionBase left, right;
            left = convertExpression(binexp.getLeft(), ctxt, currentfile);
            right = convertExpression(binexp.getRight(), ctxt, currentfile);
            String text;
            switch (binexp.getOp()) {
                case ADDITION: {
                    List<String> lines = list();
                    String dest = ctxt.makeUniqueName("dict");
                    text = fmt("%s %s = new %s(%s, false);", className, dest, className, left.getValue());
                    lines.addAll(left.getCode());
                    lines.add(text);

                    lines.addAll(right.getCode());
                    text = fmt("%s.putAll(%s);", dest, right.getValue());
                    lines.add(text);
                    return makeExpression(lines, dest, null);
                }

                case PROJECTION: { // RHS projection.
                    List<String> lines = list();
                    String dest = ctxt.makeUniqueName("value");
                    lines.addAll(left.getCode());
                    lines.addAll(right.getCode());
                    String rhsVar = ctxt.makeUniqueName("right");
                    text = fmt("%s %s = %s;", subTypes.get(0).getJavaType(), rhsVar, right.getValue());
                    lines.add(text);
                    text = fmt("%s %s = (%s).get(%s);", subTypes.get(1).getJavaClassType(), dest, left.getValue(),
                            rhsVar);
                    lines.add(text);
                    text = genCurrentPositionStatement(expr);
                    if (text != null) {
                        lines.add(text);
                    }
                    if (subTypes.get(0).isPrintable()) {
                        // For failed access where the key is printable,
                        // construct a nice error message.
                        text = "throw new ChiSimulatorException(\"Key \\\"\" + "
                                + subTypes.get(0).getToString(rhsVar, currentfile)
                                + " + \"\\\" does not exist in the dictionary.\");";
                        lines.add("if (" + dest + " == null) {");
                        lines.add("    " + text);
                        lines.add("}");
                    } else {
                        text = "if (" + dest + " == null) throw new "
                                + "ChiSimulatorException(\"Key does not exist in the dictionary.\");";
                        lines.add(text);
                    }
                    currentfile.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
                    return makeExpression(lines, dest, null);
                }

                case SUBTRACTION: {
                    List<String> lines = list();
                    String dest = ctxt.makeUniqueName("dict");
                    text = fmt("%s %s = new %s(%s, false);", className, dest, className, left.getValue());
                    lines.addAll(left.getCode());
                    lines.add(text);

                    lines.addAll(right.getCode());
                    text = genCurrentPositionStatement(expr);
                    if (text != null) {
                        lines.add(text);
                    }
                    text = fmt("%s.removeAll(%s);", dest, right.getValue());
                    lines.add(text);
                    return makeExpression(lines, dest, null);
                }
                case ELEMENT_TEST: { // Note that right and left are swapped.
                    List<String> lines = list();
                    lines.addAll(left.getCode());
                    lines.addAll(right.getCode());
                    text = genCurrentPositionStatement(expr);
                    if (text != null) {
                        lines.add(text);
                    }
                    text = fmt("(%s).containsKey(%s)", right.getValue(), left.getValue());
                    return makeExpression(lines, text, null);
                }

                default:
                    Assert.fail("Unimplemented binary dictionary operator " + expr.toString());
                    return null;
            }
        }

        Assert.fail("Implement " + expr.toString() + " in DictionaryTypeID.convertExprNode");
        return null;
    }

    /**
     * Construct the type of an item value (the key/value pair tuple).
     *
     * @param ctxt Code generator context.
     * @return Type of the item pair.
     */
    private TypeID getItemType(CodeGeneratorContext ctxt) {
        List<String> names = list("key", "value");
        Assert.check(subTypes.size() == 2);
        return createTupleTypeID(names, subTypes, ctxt);
    }

    /**
     * Generate a class file for the dictionary type in run-time, and add it to the output.
     *
     * @param ctxt Code generator context.
     */
    private void addSelf(CodeGeneratorContext ctxt) {
        TypeID keyTid = subTypes.get(0);
        String keyClassname = keyTid.getJavaClassType();

        TypeID valTid = subTypes.get(1);
        String valClassname = valTid.getJavaClassType();

        TypeID itemTid = getItemType(ctxt);
        String itemClsName = itemTid.getJavaClassType();

        String baseCls = "LinkedHashMap<" + keyClassname + ", " + valClassname + ">";
        List<String> ifaces = list("Iterable<" + itemClsName + ">");
        JavaClass cls = new JavaClass("", false, className, baseCls, ifaces);
        cls.addImport("java.util.LinkedHashMap", false);

        cls.addVariable("private final ChiCoordinator chiCoordinator;");
        cls.addImport(Constants.COORDINATOR_FQC, false);

        addConstructors(cls, ctxt);
        addDictMethods(cls, ctxt);
        addReadWrite(cls);
        addIterator(cls, ctxt);
        ctxt.addClass(cls);
    }

    /**
     * Add constructors of the dictionary.
     *
     * @param cls Dictionary class being generated.
     * @param ctxt Code generator context.
     */
    private void addConstructors(JavaFile cls, CodeGeneratorContext ctxt) {
        TypeID keyTid = subTypes.get(0);
        TypeID valTid = subTypes.get(1);
        String keyClassname = keyTid.getJavaClassType();
        String valueClassname = valTid.getJavaClassType();

        JavaMethod jm;

        // Default constructor.
        jm = new JavaMethod("public " + className + "(ChiCoordinator chiCoordinator)");
        jm.lines.add("this(chiCoordinator, 0);");
        cls.addMethod(jm);

        // Constructor for non-zero length dictionary.
        //
        // @param size Expected initial number of items.
        jm = new JavaMethod("public " + className + "(ChiCoordinator chiCoordinator, int size)");
        jm.lines.add("super(size);");
        jm.lines.add("this.chiCoordinator = chiCoordinator;");
        cls.addMethod(jm);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // Copy constructor, makes a shallow or deep copy of the original.
        //
        // @param orig Original dictionary to copy.
        // @param deepCopy Perform deep copy of the value.
        boolean hasDeepcopy = keyTid.hasDeepCopy() || valTid.hasDeepCopy();
        jm = new JavaMethod("public " + className + "(" + className + " orig, boolean deepCopy)");
        jm.lines.add("super(orig.size());");
        jm.lines.add("this.chiCoordinator = orig.chiCoordinator;");
        if (!hasDeepcopy) {
            jm.lines.add("putAll(orig);");
        } else {
            String keyCopy = "e.getKey()";
            String valCopy = "e.getValue()";
            if (keyTid.hasDeepCopy()) {
                keyCopy = keyTid.getDeepCopyName(keyCopy, cls, true);
            }
            if (valTid.hasDeepCopy()) {
                valCopy = valTid.getDeepCopyName(valCopy, cls, true);
            }

            jm.lines.add("if (!deepCopy) {");
            jm.lines.add("   putAll(orig);");
            jm.lines.add("} else {");
            jm.lines.add("    for (Map.Entry<%s, %s> e: orig.entrySet()) {", keyClassname, valueClassname);
            jm.lines.add("        put(%s, %s);", keyCopy, valCopy);
            jm.lines.add("    }");
            jm.lines.add("}");
        }
        cls.addMethod(jm);
    }

    /**
     * Generate helper method of the dictionary class.
     *
     * @param cls Dictionary class to extend.
     * @param ctxt Code generator context.
     */
    private void addDictMethods(JavaFile cls, CodeGeneratorContext ctxt) {
        TypeID keyTid = first(subTypes);
        TypeID valueTid = subTypes.get(1);
        String keyTypename = keyTid.getJavaType();
        String keyClassname = keyTid.getJavaClassType();
        String valueTypename = valueTid.getJavaType();
        String valueClassname = valueTid.getJavaClassType();

        JavaMethod jm;

        // Poppin' on a dict.
        List<String> fields = list("key", "value", "dict");
        List<TypeID> tids = list(keyTid, valueTid, this);
        TypeID tupleTid = createTupleTypeID(fields, tids, ctxt);
        String tupName = tupleTid.getJavaClassType();

        jm = new JavaMethod("public " + tupName + " pop()");
        jm.lines.add("if (isEmpty()) throw new ChiSimulatorException(\"Cannot pop value from an empty dictionary.\");");
        jm.lines.add("%s res = new %s(chiCoordinator);", tupName, tupName);
        jm.lines.add("res.var2 = new %s(this, false);", className);
        jm.lines.add("Iterator<Map.Entry<%s, %s>> iter = res.var2.entrySet().iterator();", keyClassname,
                valueClassname);
        jm.lines.add("Map.Entry<%s, %s> entry = iter.next();", keyClassname, valueClassname);
        jm.lines.add("res.var0 = entry.getKey();");
        jm.lines.add("res.var1 = entry.getValue();");
        jm.lines.add("iter.remove();");
        jm.lines.add("return res;");
        cls.addMethod(jm);
        cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        cls.addImport("java.util.Iterator", false);
        cls.addImport("java.util.Map", false);

        jm = new JavaMethod(
                "public static " + className + " removeElement(" + className + " dict, " + keyTypename + " value)");
        jm.lines.add("%s res = new %s(dict, false);", className, className);
        jm.lines.add("if (res.remove(value) != null) return res;");
        jm.lines.add("return dict;");
        cls.addMethod(jm);

        // Minimum and maximum functions.
        String smallestText = null;
        String biggestText = null;
        if (keyTid.kind == TypeKind.INT || keyTid.kind == TypeKind.REAL) {
            smallestText = "result > value";
            biggestText = "result < value";
        } else if (keyTid.kind == TypeKind.STRING) {
            smallestText = "result.compareTo(value) > 0";
            biggestText = "result.compareTo(value) < 0";
        }
        if (smallestText != null) {
            jm = new JavaMethod("public " + keyTypename + " getMinimum()");
            jm.lines.add("%s result = null;", keyClassname);
            jm.lines.add("for (%s value: this.keySet()) {", keyClassname);
            jm.lines.add("    if (result == null || %s) result = value;", smallestText);
            jm.lines.add("}");
            jm.lines.add("if (result == null) throw new ChiSimulatorException("
                    + "\"Cannot find a smallest value in an empty dictionary.\");");
            jm.lines.add("return result;");
            cls.addMethod(jm);
            cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        }
        if (biggestText != null) {
            jm = new JavaMethod("public " + keyTypename + " getMaximum()");
            jm.lines.add("%s result = null;", keyClassname);
            jm.lines.add("for (%s value: this.keySet()) {", keyClassname);
            jm.lines.add("    if (result == null || %s) result = value;", biggestText);
            jm.lines.add("}");
            jm.lines.add("if (result == null) throw new ChiSimulatorException("
                    + "\"Cannot find a biggest value in an empty dictionary.\");");
            jm.lines.add("return result;");
            cls.addMethod(jm);
            cls.addImport(CHI_SIMULATOR_EXCEPTION_FQC, false);
        }

        // modify
        jm = new JavaMethod("public " + className + " modify(" + keyTypename + " key, " + valueTypename + " value)");
        jm.lines.add("%s result = new %s(this, false);", className, className);
        jm.lines.add("result.put(key, value);");
        jm.lines.add("return result;");
        cls.addMethod(jm);

        // removeAll (dict - dict)
        jm = new JavaMethod("public void removeAll(" + className + " d)");
        jm.lines.add("for (Map.Entry<%s, %s> e: d.entrySet()) {", keyClassname, valueClassname);
        jm.lines.add("    remove(e.getKey());");
        jm.lines.add("}");
        cls.addMethod(jm);

        // removeAll (dict - set)
        TypeID tid = TypeIDCreation.makeSetTypeID(keyTid, ctxt);
        String containerName = tid.getJavaClassType();
        jm = new JavaMethod("public void removeAll(" + containerName + " s)");
        jm.lines.add("for (%s e: s) {", keyClassname);
        jm.lines.add("    remove(e);");
        jm.lines.add("}");
        cls.addMethod(jm);

        // removeAll (dict - list)
        tid = TypeIDCreation.makeListTypeID(keyTid, ctxt);
        containerName = tid.getJavaClassType();
        jm = new JavaMethod("public void removeAll(" + containerName + " s)");
        jm.lines.add("for (%s e: s) {", keyClassname);
        jm.lines.add("    remove(e);");
        jm.lines.add("}");
        cls.addMethod(jm);

        // keylist
        {
            TypeID lstTid = makeListTypeID(keyTid, ctxt);
            String lstClassname = lstTid.getJavaClassType();
            jm = new JavaMethod("public " + lstClassname + " getKeyList()");
            jm.lines.add("%s result = new %s(chiCoordinator, this.size());", lstClassname, lstClassname);
            jm.lines.add("for (Map.Entry<%s, %s> e: this.entrySet()) {", keyClassname, valueClassname);
            jm.lines.add("    result.append(e.getKey());");
            jm.lines.add("}");
            jm.lines.add("return result;");
            cls.addMethod(jm);
        }

        // valuelist
        {
            TypeID lstTid = makeListTypeID(valueTid, ctxt);
            String lstClassname = lstTid.getJavaClassType();
            jm = new JavaMethod("public " + lstClassname + " getValueList()");
            jm.lines.add("%s result = new %s(chiCoordinator, this.size());", lstClassname, lstClassname);
            jm.lines.add("for (Map.Entry<%s, %s> e: this.entrySet()) {", keyClassname, valueClassname);
            jm.lines.add("    result.append(e.getValue());");
            jm.lines.add("}");
            jm.lines.add("return result;");
            cls.addMethod(jm);
        }
    }

    /**
     * Add read and write functions.
     *
     * @param cls Dictionary class being generated.
     */
    private void addReadWrite(JavaFile cls) {
        // Don't generate read/write if not serializable.
        if (!isPrintable()) {
            return;
        }

        TypeID keyTid = subTypes.get(0);
        TypeID valTid = subTypes.get(1);
        String keyClassname = keyTid.getJavaClassType();
        String valueClassname = valTid.getJavaClassType();

        JavaMethod jm;

        // Construct a new dictionary by reading from an input stream.
        //
        // @param stream Input stream to read the dictionary from.
        // @return The created dictionary.
        jm = new JavaMethod(
                "public static " + className + " read(ChiCoordinator chiCoordinator, ChiFileHandle stream)");

        String rdKey = fmt("%s key = %s;", keyClassname, keyTid.getReadName("stream", cls));
        String rdVal = fmt("%s val = %s;", valueClassname, valTid.getReadName("stream", cls));

        jm.lines.add(className + " result = new " + className + "(chiCoordinator);");
        jm.lines.add();
        jm.lines.add("stream.expectCharacter('{');");
        jm.lines.add("for (;;) {");
        jm.lines.add("    " + rdKey);
        jm.lines.add("stream.expectCharacter(':');");
        jm.lines.add("    " + rdVal);
        jm.lines.add("    result.put(key, val);");
        jm.lines.add("    int ch = stream.expectCharacter(',', '}');");
        jm.lines.add("    if (ch == '}') break;");
        jm.lines.add("    if (ch == ',') continue;");
        jm.lines.add("}");
        jm.lines.add("return result;");
        cls.addMethod(jm);
        cls.addImport(Constants.CHI_FILE_HANDLE_FQC, false);
        cls.addImport(Constants.COORDINATOR_FQC, false);

        // Write the dictionary to the output stream.
        //
        // @param stream Output stream to write the dictionary to.
        jm = new JavaMethod("public void write(ChiFileHandle stream)");
        jm.lines.add("stream.write(\"{\");");
        jm.lines.add("boolean first = true;");
        jm.lines.add("for (Map.Entry<%s, %s> e: this.entrySet()) {", keyClassname, valueClassname);
        jm.lines.add("    if (!first) stream.write(\", \");");
        jm.lines.add("    first = false;");
        jm.lines.add("    " + keyTid.getWriteName("stream", "e.getKey()", cls));
        jm.lines.add("    stream.write(\" : \");");
        jm.lines.add("    " + valTid.getWriteName("stream", "e.getValue()", cls));
        jm.lines.add("}");
        jm.lines.add("stream.write(\"}\");");
        cls.addMethod(jm);
        cls.addImport(Constants.CHI_FILE_HANDLE_FQC, false);
        cls.addImport("java.util.Map", false);

        jm = new JavaMethod("public String toString()");
        jm.lines.add("ChiWriteMemoryFile mem = new ChiWriteMemoryFile();");
        jm.lines.add("write(mem);");
        jm.lines.add("return mem.getData();");
        cls.addMethod(jm);
        cls.addImport(Constants.CHI_WRITE_MEMORY_FILE_FQC, false);
    }

    /**
     * Add an iterable to the class for iterating over the dictionary.
     *
     * @param cls Dictionary class to add to.
     * @param ctxt Code generator context.
     */
    private void addIterator(JavaFile cls, CodeGeneratorContext ctxt) {
        TypeID keyTid = subTypes.get(0);
        TypeID valTid = subTypes.get(1);
        String keyClassname = keyTid.getJavaClassType();
        String valueClassname = valTid.getJavaClassType();
        String iterClsName = ctxt.makeUniqueName("DictIter");
        String itemClsName = getItemType(ctxt).getJavaClassType();

        String mapEntriesType = fmt("Map.Entry<%s, %s>", keyClassname, valueClassname);

        JavaMethod jm = new JavaMethod("class " + iterClsName + " implements Iterator<" + itemClsName + ">");
        jm.lines.add("private Iterator<%s> iter;", mapEntriesType);
        jm.lines.add();
        jm.lines.add("public %s(Set<%s> entries) {", iterClsName, mapEntriesType);
        jm.lines.add("    iter = entries.iterator();");
        jm.lines.add("}");
        jm.lines.add();
        jm.lines.add("public boolean hasNext() { return iter.hasNext(); }");
        jm.lines.add("public void remove() { iter.remove(); }");
        jm.lines.add();
        jm.lines.add("public %s next() {", itemClsName);
        jm.lines.add("    %s tup = new %s(chiCoordinator);", itemClsName, itemClsName);
        jm.lines.add("    %s entry = iter.next();", mapEntriesType);
        jm.lines.add("    tup.var0 = entry.getKey();");
        jm.lines.add("    tup.var1 = entry.getValue();");
        jm.lines.add("    return tup;");
        jm.lines.add("}");
        cls.addMethod(jm);
        cls.addImport("java.util.Set", false);

        jm = new JavaMethod("public " + iterClsName + " iterator()");
        jm.lines.add("return new %s(this.entrySet());", iterClsName);
        cls.addMethod(jm);
    }
}
