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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifExtFuncUtils.splitExtJavaRef;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTypeUtils.getLowerBound;
import static org.eclipse.escet.cif.common.CifTypeUtils.getUpperBound;
import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Strings;

/** External user-defined Java function code generator. */
public class ExtJavaFuncCodeGenerator {
    /** Constructor for the {@link ExtJavaFuncCodeGenerator} class. */
    private ExtJavaFuncCodeGenerator() {
        // Static class.
    }

    /**
     * Generates class fields for the generated class for an external user-defined Java function.
     *
     * @param func The external user-defined function.
     * @param c The code box to which to add the code.
     */
    public static void gencodeClassFields(ExternalFunction func, CodeBox c) {
        c.add();
        c.add("private java.lang.reflect.Method method;");
    }

    /**
     * Generate Java code for the body of the evaluation method for the given external user-defined Java function.
     *
     * @param func The external user-defined function.
     * @param retType The return type of the function.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeBody(ExternalFunction func, CifType retType, CodeBox c, CifCompilerContext ctxt) {
        // Split external implementation reference.
        String[] parts = splitExtJavaRef(func.getFunction());
        String className = parts[1];
        String methodName = parts[2];
        String classPath = parts[3];

        // Get directory against which to resolve relative class path entries.
        String specFileDir = ctxt.getSpecFileDir();

        // Generate code for loading the method.
        c.add("if (method == null) {");
        c.indent();
        c.add("String cifFuncName = %s;", Strings.stringToJava(getAbsName(func)));
        c.add("String className = %s;", Strings.stringToJava(className));
        c.add("String methodName = %s;", Strings.stringToJava(methodName));
        c.add("String classPath = %s;", (classPath == null) ? "null" : Strings.stringToJava(classPath));
        c.add("String specFileDir = %s;", Strings.stringToJava(specFileDir));

        List<String> paramTypes = listc(func.getParameters().size());
        for (FunctionParameter param: func.getParameters()) {
            CifType paramType = param.getParameter().getType();
            paramTypes.add(typeToJavaClsStr(paramType, false));
        }
        c.add("Class<?>[] paramTypes = {%s};", StringUtils.join(paramTypes, ", "));

        c.add("Class<?> expReturnType = %s;", typeToJavaClsStr(retType, true));
        c.add("method = ExtFuncs.loadJavaMethod(cifFuncName, className, methodName, classPath, specFileDir, "
                + "paramTypes, expReturnType);");
        c.dedent();
        c.add("}");
        c.add();

        // Generate code for invoking the Java method with converted values.
        List<String> paramNames = listc(func.getParameters().size());
        for (FunctionParameter param: func.getParameters()) {
            DiscVariable var = param.getParameter();
            paramNames.add(ctxt.getFuncParamMethodParamName(var));
        }
        c.add("Object rslt = ExtFuncs.invokeJavaMethodAsync(SPEC.ctxt, method, cifToJava(%s));",
                StringUtils.join(paramNames, ", "));

        // Generate code for converting the method result back to CIF.
        c.add("return javaToCif(rslt);");
    }

    /**
     * Generates additional Java methods for the class generated for the given external user-defined Java function.
     *
     * @param func The external user-defined function.
     * @param retType The return type of the function.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeAdditionalMethods(ExternalFunction func, CifType retType, CodeBox c,
            CifCompilerContext ctxt)
    {
        gencodeCifToJava(func, c, ctxt);
        gencodeJavaToCif(func, retType, c, ctxt);
    }

    /**
     * Generate code for the conversion of CIF values for the parameters of an external user-defined Java function to
     * Java values.
     *
     * @param func The external user-defined function.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeCifToJava(ExternalFunction func, CodeBox c, CifCompilerContext ctxt) {
        // Generate function parameters.
        List<String> paramTxts = listc(func.getParameters().size());
        for (FunctionParameter param: func.getParameters()) {
            DiscVariable var = param.getParameter();
            String typeTxt = gencodeType(var.getType(), ctxt);
            String name = ctxt.getFuncParamMethodParamName(var);
            paramTxts.add(typeTxt + " " + name);
        }

        // Method header.
        c.add();
        c.add("private Object[] cifToJava(%s) {", StringUtils.join(paramTxts, ", "));
        c.indent();

        // Add result variable.
        c.add("Object[] rslt = new Object[%d];", func.getParameters().size());

        // Convert each parameter value.
        AtomicInteger nr = new AtomicInteger();
        for (int i = 0; i < func.getParameters().size(); i++) {
            c.add();
            FunctionParameter param = func.getParameters().get(i);
            DiscVariable var = param.getParameter();
            CifType ptype = var.getType();
            gencodeCifToJava(ptype, ctxt.getFuncParamMethodParamName(var), fmt("rslt[%d] = %%s;", i), nr, c, ctxt);
        }

        // Return converted values.
        c.add();
        c.add("return rslt;");

        // End of method.
        c.dedent();
        c.add("}");
    }

    /**
     * Generate code for the conversion of a CIF value for a parameters of an external user-defined Java function to a
     * Java value.
     *
     * @param type The (sub-)type of the parameter value.
     * @param srcTxt The Java code fragment of the value to convert.
     * @param tgtTxt The Java code fragment containing the Java statement to use to store the converted value. It is a
     *     format pattern with one placeholder, which should be replaced by a textual reference to the converted value.
     * @param c The code box to which to add the code.
     * @param nr The next unique number to use when generating code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeCifToJava(CifType type, String srcTxt, String tgtTxt, AtomicInteger nr, CodeBox c,
            CifCompilerContext ctxt)
    {
        if (type instanceof BoolType) {
            // Keep value unchanged.
            c.add(tgtTxt, srcTxt);
        } else if (type instanceof IntType) {
            // Keep value unchanged.
            c.add(tgtTxt, srcTxt);
        } else if (type instanceof TypeRef) {
            // Generate code for the actual type (recursively).
            gencodeCifToJava(((TypeRef)type).getType().getType(), srcTxt, tgtTxt, nr, c, ctxt);
        } else if (type instanceof RealType) {
            // Keep value unchanged.
            c.add(tgtTxt, srcTxt);
        } else if (type instanceof StringType) {
            // Keep value unchanged.
            c.add(tgtTxt, srcTxt);
        } else if (type instanceof ListType) {
            // Construct new list.
            int listNr = nr.getAndIncrement();
            c.add("%s lst%d = new Array%s(%s.size());", typeToJavaStr(type, false), listNr, typeToJavaStr(type, false),
                    srcTxt);

            // Convert and add elements.
            int elemNr = nr.getAndIncrement();
            CifType etype = ((ListType)type).getElementType();
            c.add("for (%s elem%d: %s) {", gencodeType(etype, ctxt), elemNr, srcTxt);
            c.indent();
            gencodeCifToJava(etype, "elem" + elemNr, fmt("lst%d.add(%%s);", listNr), nr, c, ctxt);
            c.dedent();
            c.add("}");

            // Add list to result.
            c.add(tgtTxt, "lst" + listNr);
        } else if (type instanceof SetType) {
            // Construct new set.
            int setNr = nr.getAndIncrement();
            c.add("%s set%d = new LinkedHash%s(%s.size());", typeToJavaStr(type, false), setNr,
                    typeToJavaStr(type, false), srcTxt);

            // Convert and add elements.
            int elemNr = nr.getAndIncrement();
            CifType etype = ((SetType)type).getElementType();
            c.add("for (%s elem%d: %s) {", gencodeType(etype, ctxt), elemNr, srcTxt);
            c.indent();
            gencodeCifToJava(etype, "elem" + elemNr, fmt("set%d.add(%%s);", setNr), nr, c, ctxt);
            c.dedent();
            c.add("}");

            // Add list to result.
            c.add(tgtTxt, "set" + setNr);
        } else if (type instanceof DictType) {
            // Construct new map.
            int mapNr = nr.getAndIncrement();
            c.add("%s map%d = new LinkedHash%s(%s.size());", typeToJavaStr(type, false), mapNr,
                    typeToJavaStr(type, false), srcTxt);

            // Convert and add entries.
            int entryNr = nr.getAndIncrement();
            CifType ktype = ((DictType)type).getKeyType();
            CifType vtype = ((DictType)type).getValueType();
            c.add("for (Entry<%s, %s> entry%d: %s.entrySet()) {", gencodeType(ktype, ctxt, true),
                    gencodeType(vtype, ctxt, true), entryNr, srcTxt);
            c.indent();

            int keyNr = nr.getAndIncrement();
            c.add("%s key%d;", typeToJavaStr(ktype, false), keyNr);
            gencodeCifToJava(ktype, fmt("entry%d.getKey()", entryNr), fmt("key%d = %%s;", keyNr), nr, c, ctxt);

            int valueNr = nr.getAndIncrement();
            c.add("%s value%d;", typeToJavaStr(vtype, false), valueNr);
            gencodeCifToJava(vtype, fmt("entry%d.getValue()", entryNr), fmt("value%d = %%s;", valueNr), nr, c, ctxt);

            c.add("map%d.put(key%d, value%d);", mapNr, keyNr, valueNr);

            c.dedent();
            c.add("}");

            // Add map to result.
            c.add(tgtTxt, "map" + mapNr);
        } else if (type instanceof TupleType) {
            // Get tuple type.
            TupleType ttype = (TupleType)type;

            // Construct new list, as Java has no tuples.
            int tplNr = nr.getAndIncrement();
            c.add("%s tpl%d = new Array%s(%d);", typeToJavaStr(type, false), tplNr, typeToJavaStr(type, false),
                    ttype.getFields().size());

            // Convert and add elements.
            for (int i = 0; i < ttype.getFields().size(); i++) {
                Field field = ttype.getFields().get(i);
                String fieldName = ctxt.getTupleTypeFieldFieldName(ttype, i);
                gencodeCifToJava(field.getType(), srcTxt + "." + fieldName, fmt("tpl%d.add(%%s);", tplNr), nr, c, ctxt);
            }

            // Add tuple (list) to result.
            c.add(tgtTxt, "tpl" + tplNr);
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Generate code for the conversion the resulting value of the invocation of an external user-defined Java function
     * to a CIF value.
     *
     * @param func The external user-defined function.
     * @param retType The return type of the function.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeJavaToCif(ExternalFunction func, CifType retType, CodeBox c, CifCompilerContext ctxt) {
        // Method header.
        c.add();
        c.add("private %s javaToCif(Object retValue) {", gencodeType(retType, ctxt));
        c.indent();

        // Add result variable.
        c.add("%s rslt;", gencodeType(retType, ctxt));
        c.add();

        // Convert return value.
        AtomicInteger nr = new AtomicInteger();
        gencodeJavaToCif(retType, "retValue", "rslt = %s;", nr, c, ctxt);

        // Return converted value.
        c.add();
        c.add("return rslt;");

        // End of method.
        c.dedent();
        c.add("}");
    }

    /**
     * Generate code for the conversion the resulting value of the invocation of an external user-defined Java function
     * to a CIF value.
     *
     * @param type The (sub-)type of the return value.
     * @param srcTxt The Java code fragment of the value to convert.
     * @param tgtTxt The Java code fragment containing the Java statement to use to store the converted value. It is a
     *     format pattern with one placeholder, which should be replaced by a textual reference to the converted value.
     * @param c The code box to which to add the code.
     * @param nr The next unique number to use when generating code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeJavaToCif(CifType type, String srcTxt, String tgtTxt, AtomicInteger nr, CodeBox c,
            CifCompilerContext ctxt)
    {
        // Generate code to check for 'null' values.
        c.add("ExtFuncs.checkJavaNullReturn(%s);", srcTxt);

        // Generate conversion code.
        if (type instanceof BoolType) {
            // Check type.
            c.add("if (!(%s instanceof Boolean)) ExtFuncs.checkJavaRetTypeFailed(%s, Boolean.class);", srcTxt, srcTxt);

            // Keep value unchanged.
            c.add(tgtTxt, "(Boolean)" + srcTxt);
        } else if (type instanceof IntType) {
            // Check type.
            c.add("if (!(%s instanceof Integer)) ExtFuncs.checkJavaRetTypeFailed(%s, Integer.class);", srcTxt, srcTxt);

            // Check range.
            IntType itype = (IntType)type;
            if (!isRangeless(itype)) {
                int lower = getLowerBound(itype);
                int upper = getUpperBound(itype);
                c.add("if ((Integer)%s < %d || %d < (Integer)%s) ExtFuncs.checkJavaIntRangeFailed((Integer)%s, %d, "
                        + "%d);", srcTxt, lower, upper, srcTxt, srcTxt, lower, upper);
            }

            // Keep value unchanged.
            c.add(tgtTxt, "(Integer)" + srcTxt);
        } else if (type instanceof TypeRef) {
            // Generate code for the actual type (recursively).
            gencodeJavaToCif(((TypeRef)type).getType().getType(), srcTxt, tgtTxt, nr, c, ctxt);
        } else if (type instanceof RealType) {
            // Check type.
            c.add("if (!(%s instanceof Double)) ExtFuncs.checkJavaRetTypeFailed(%s, Double.class);", srcTxt, srcTxt);

            // Check for NaN, -inf, and +inf.
            c.add("ExtFuncs.checkJavaDoubleReturn((Double)%s);", srcTxt);

            // Keep value unchanged.
            c.add(tgtTxt, "(Double)" + srcTxt);
        } else if (type instanceof StringType) {
            // Check type.
            c.add("if (!(%s instanceof String)) ExtFuncs.checkJavaRetTypeFailed(%s, String.class);", srcTxt, srcTxt);

            // Keep value unchanged.
            c.add(tgtTxt, "(String)" + srcTxt);
        } else if (type instanceof ListType) {
            // Check type.
            c.add("if (!(%s instanceof List)) ExtFuncs.checkJavaRetTypeFailed(%s, List.class);", srcTxt, srcTxt);

            // Check range.
            ListType ltype = (ListType)type;
            if (!isRangeless(ltype)) {
                int lower = getLowerBound(ltype);
                int upper = getUpperBound(ltype);
                c.add("if (((List<?>)%s).size() < %d || %d < ((List<?>)%s).size()) ExtFuncs.checkJavaListRangeFailed("
                        + "(List<?>)%s, %d, %d);", srcTxt, lower, upper, srcTxt, srcTxt, lower, upper);
            }

            // Construct new list.
            int listNr = nr.getAndIncrement();
            c.add("%s lst%d = new Array%s(((List<?>)%s).size());", gencodeType(type, ctxt), listNr,
                    gencodeType(type, ctxt), srcTxt);

            // Convert and add elements.
            int elemNr = nr.getAndIncrement();
            CifType etype = ((ListType)type).getElementType();
            c.add("for (Object elem%d: (List<?>)%s) {", elemNr, srcTxt);
            c.indent();
            gencodeJavaToCif(etype, "elem" + elemNr, fmt("lst%d.add(%%s);", listNr), nr, c, ctxt);
            c.dedent();
            c.add("}");

            // Add list to result.
            c.add(tgtTxt, "lst" + listNr);
        } else if (type instanceof SetType) {
            // Check type.
            c.add("if (!(%s instanceof Set)) ExtFuncs.checkJavaRetTypeFailed(%s, Set.class);", srcTxt, srcTxt);

            // Construct new set.
            int setNr = nr.getAndIncrement();
            c.add("%s set%d = new LinkedHash%s(((Set<?>)%s).size());", gencodeType(type, ctxt), setNr,
                    gencodeType(type, ctxt), srcTxt);

            // Convert and add elements.
            int elemNr = nr.getAndIncrement();
            CifType etype = ((SetType)type).getElementType();
            c.add("for (Object elem%d: (Set<?>)%s) {", elemNr, srcTxt);
            c.indent();
            gencodeJavaToCif(etype, "elem" + elemNr, fmt("set%d.add(%%s);", setNr), nr, c, ctxt);
            c.dedent();
            c.add("}");

            // Add set to result.
            c.add(tgtTxt, "set" + setNr);
        } else if (type instanceof DictType) {
            // Check type.
            c.add("if (!(%s instanceof Map)) ExtFuncs.checkJavaRetTypeFailed(%s, Map.class);", srcTxt, srcTxt);

            // Construct new map.
            int mapNr = nr.getAndIncrement();
            c.add("%s map%d = new LinkedHash%s(((Map<?, ?>)%s).size());", gencodeType(type, ctxt), mapNr,
                    gencodeType(type, ctxt), srcTxt);

            // Convert and add entries.
            int entryNr = nr.getAndIncrement();
            CifType ktype = ((DictType)type).getKeyType();
            CifType vtype = ((DictType)type).getValueType();
            c.add("for (Entry<?, ?> entry%d: ((Map<?, ?>)%s).entrySet()) {", entryNr, srcTxt);
            c.indent();

            int keyNr = nr.getAndIncrement();
            c.add("%s key%d;", gencodeType(ktype, ctxt), keyNr);
            gencodeJavaToCif(ktype, fmt("entry%d.getKey()", entryNr), fmt("key%d = %%s;", keyNr), nr, c, ctxt);

            c.add();

            int valueNr = nr.getAndIncrement();
            c.add("%s value%d;", gencodeType(vtype, ctxt), valueNr);
            gencodeJavaToCif(vtype, fmt("entry%d.getValue()", entryNr), fmt("value%d = %%s;", valueNr), nr, c, ctxt);

            c.add("map%d.put(key%d, value%d);", mapNr, keyNr, valueNr);

            c.dedent();
            c.add("}");

            // Add map to result.
            c.add(tgtTxt, "map" + mapNr);
        } else if (type instanceof TupleType) {
            // Get tuple type.
            TupleType ttype = (TupleType)type;

            // Check type (List, as Java has no tuples).
            c.add("if (!(%s instanceof List)) ExtFuncs.checkJavaRetTypeFailed(%s, List.class);", srcTxt, srcTxt);

            // Store Java list that represents the tuple.
            int tplJavaNr = nr.getAndIncrement();
            c.add("List<?> tplJava%d = (List<?>)%s;", tplJavaNr, srcTxt);

            // Check length (must be equal to number of fields of the tuple).
            c.add("if (tplJava%d.size() != %d) throw new CifSimulatorException(fmt(\"The return value of the "
                    + "external Java function contains a list of size %%d, for a tuple with %d fields.\", "
                    + "tplJava%d.size()));", tplJavaNr, ttype.getFields().size(), ttype.getFields().size(), tplJavaNr);

            // Convert field values.
            StringBuilder constructorArgs = new StringBuilder();
            for (int i = 0; i < ttype.getFields().size(); i++) {
                Field field = ttype.getFields().get(i);

                int fldJavaNr = nr.getAndIncrement();
                c.add("Object fldJava%d = tplJava%d.get(%d);", fldJavaNr, tplJavaNr, i);

                int fldCifNr = nr.getAndIncrement();
                c.add("%s fldCif%d;", gencodeType(field.getType(), ctxt), fldCifNr);

                if (constructorArgs.length() > 0) {
                    constructorArgs.append(", ");
                }
                constructorArgs.append("fldCif" + fldCifNr);

                gencodeJavaToCif(field.getType(), "fldJava" + fldJavaNr, fmt("fldCif%d = %%s;", fldCifNr), nr, c, ctxt);
                c.add();
            }

            // Construct new tuple.
            int tplCifNr = nr.getAndIncrement();
            c.add("%s tplCif%d = new %s(%s);", gencodeType(type, ctxt), tplCifNr, gencodeType(type, ctxt),
                    constructorArgs.toString());
            c.add(tgtTxt, "tplCif" + tplCifNr);
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Converts a CIF type to Java code that represents the given type. The types are converted recursively, except for
     * tuples. Java has no tuples, and we can't create a generic tuple class, since we can't parameterize a Java class
     * over a variable number of types.
     *
     * @param type The CIF type.
     * @param generic Whether the type code is used as a generic type parameter (and may thus not contain primitive
     *     types).
     * @return The Java code that represents the given type.
     */
    private static String typeToJavaStr(CifType type, boolean generic) {
        if (type instanceof BoolType) {
            return generic ? "Boolean" : "boolean";
        } else if (type instanceof IntType) {
            return generic ? "Integer" : "int";
        } else if (type instanceof TypeRef) {
            return typeToJavaStr(((TypeRef)type).getType().getType(), generic);
        } else if (type instanceof RealType) {
            return generic ? "Double" : "double";
        } else if (type instanceof StringType) {
            return "String";
        } else if (type instanceof ListType) {
            CifType etype = ((ListType)type).getElementType();
            return fmt("List<%s>", typeToJavaStr(etype, true));
        } else if (type instanceof SetType) {
            CifType etype = ((SetType)type).getElementType();
            return fmt("Set<%s>", typeToJavaStr(etype, true));
        } else if (type instanceof DictType) {
            CifType ktype = ((DictType)type).getKeyType();
            CifType vtype = ((DictType)type).getValueType();
            return fmt("Map<%s, %s>", typeToJavaStr(ktype, true), typeToJavaStr(vtype, true));
        } else if (type instanceof TupleType) {
            // Special case, since Java has no tuples.
            return "List<Object>";
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Converts a CIF type to Java code that represents the given type's Java class. The Java class only represents the
     * top level Java type, not its type parameters, or the type parameters of the type parameters, etc.
     *
     * @param type The CIF type.
     * @param generic Whether the type code is used as a generic type parameter (and may thus not contain primitive
     *     types).
     * @return The Java code that represents the given type.
     */
    private static String typeToJavaClsStr(CifType type, boolean generic) {
        if (type instanceof BoolType) {
            return generic ? "Boolean.class" : "boolean.class";
        } else if (type instanceof IntType) {
            return generic ? "Integer.class" : "int.class";
        } else if (type instanceof TypeRef) {
            return typeToJavaClsStr(((TypeRef)type).getType().getType(), generic);
        } else if (type instanceof RealType) {
            return generic ? "Double.class" : "double.class";
        } else if (type instanceof StringType) {
            return "String.class";
        } else if (type instanceof ListType) {
            return "List.class";
        } else if (type instanceof SetType) {
            return "Set.class";
        } else if (type instanceof DictType) {
            return "Map.class";
        } else if (type instanceof TupleType) {
            // Special case, since Java has no tuples.
            return "List.class";
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }
}
