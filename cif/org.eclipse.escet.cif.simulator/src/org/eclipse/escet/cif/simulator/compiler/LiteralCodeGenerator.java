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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.LITERAL_READER_CLS_NAME;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.common.TypeEqHashWrap;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DictPair;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.RealExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SetExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Strings;

/** Literal code generator. */
public class LiteralCodeGenerator {
    /** Constructor for the {@link LiteralCodeGenerator} class. */
    private LiteralCodeGenerator() {
        // Static class.
    }

    /**
     * Is the given type serializable? That is, can literal values of that type be serialized? A type is serializable if
     * and only if it {@link CifTypeUtils#supportsValueEquality supports value equality}.
     *
     * @param type The type.
     * @return {@code true} if the type is serializable, {@code false} otherwise.
     */
    public static boolean isSerializableType(CifType type) {
        return CifTypeUtils.supportsValueEquality(type);
    }

    /**
     * Is the given expression serializable as a literal?
     *
     * @param expr The expression.
     * @return {@code true} if the expression is serializable as a literal, {@code false} otherwise.
     */
    public static boolean isSerializableLiteral(Expression expr) {
        // Simple literals.
        if (expr instanceof BoolExpression) {
            return true;
        }
        if (expr instanceof IntExpression) {
            return true;
        }
        if (expr instanceof RealExpression) {
            return true;
        }
        if (expr instanceof StringExpression) {
            return true;
        }
        if (expr instanceof EnumLiteralExpression) {
            return true;
        }

        // Container literals.
        if (expr instanceof ListExpression) {
            for (Expression child: ((ListExpression)expr).getElements()) {
                if (!isSerializableLiteral(child)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof SetExpression) {
            for (Expression child: ((SetExpression)expr).getElements()) {
                if (!isSerializableLiteral(child)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof TupleExpression) {
            for (Expression child: ((TupleExpression)expr).getFields()) {
                if (!isSerializableLiteral(child)) {
                    return false;
                }
            }
            return true;
        } else if (expr instanceof DictExpression) {
            for (DictPair pair: ((DictExpression)expr).getPairs()) {
                if (!isSerializableLiteral(pair.getKey())) {
                    return false;
                }
                if (!isSerializableLiteral(pair.getValue())) {
                    return false;
                }
            }
            return true;
        }

        // Casts.
        if (expr instanceof CastExpression) {
            CastExpression cexpr = (CastExpression)expr;
            Expression child = cexpr.getChild();
            if (!isSerializableLiteral(child)) {
                return false;
            }

            CifType nctype = CifTypeUtils.normalizeType(child.getType());
            CifType nrtype = CifTypeUtils.normalizeType(expr.getType());
            if (CifTypeUtils.checkTypeCompat(nctype, nrtype, RangeCompat.EQUAL)) {
                // Ignore.
                return true;
            } else if (nctype instanceof IntType && nrtype instanceof RealType) {
                // Support int to real casts.
                return true;
            } else {
                // Unsupported cast.
                return false;
            }
        }

        // Operators.
        if (expr instanceof UnaryExpression) {
            UnaryExpression uexpr = (UnaryExpression)expr;
            Expression child = uexpr.getChild();
            if (!isSerializableLiteral(child)) {
                return false;
            }

            switch (uexpr.getOperator()) {
                case NEGATE:
                    return true; // -5
                case PLUS:
                    return true; // +5
                default:
                    return false; // Other computations unsupported.
            }
        }

        // Not a literal, or not serializable.
        return false;
    }

    /**
     * Generated code for a literal. The literal is written to a data file (resource). The returned Java code can be
     * used to read the data file (resource) at runtime.
     *
     * @param literal The literal.
     * @param ctxt The compiler context to use.
     * @return Java code to read the data file (resource) at runtime.
     * @see #isSerializableLiteral
     */
    public static String gencodeLiteral(Expression literal, CifCompilerContext ctxt) {
        // Get output writer.
        String path = ctxt.getLiteralDataFileName();
        path = CifCompilerContext.PACKAGE + "/" + path;
        ByteArrayOutputStream stream = ctxt.addResourceFile(path);
        Charset charset = Charset.forName("UTF-8");
        Writer writer = new OutputStreamWriter(stream, charset);

        // Write literal.
        try {
            writeLiteral(literal, writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to compile literal.", ex);
        }

        // Return code to read the literal.
        String methodName = ctxt.getLiteralReadMethodName(literal.getType());
        String loaderCode = "SPEC.resourceClassLoader";
        String pathCode = Strings.stringToJava(path);
        return fmt("%s(%s, %s)", methodName, loaderCode, pathCode);
    }

    /**
     * Writes a literal expression.
     *
     * @param expr The literal expression.
     * @param writer The writer to use to write the literal.
     * @throws IOException In case of I/O failure.
     * @see #isSerializableLiteral
     */
    private static void writeLiteral(Expression expr, Writer writer) throws IOException {
        // Simple literals.
        if (expr instanceof BoolExpression) {
            boolean value = ((BoolExpression)expr).isValue();
            writer.append(value ? "true" : "false");
            return;
        } else if (expr instanceof IntExpression) {
            int value = ((IntExpression)expr).getValue();
            writer.append(Integer.toString(value));
            return;
        } else if (expr instanceof RealExpression) {
            writer.append(((RealExpression)expr).getValue());
            return;
        } else if (expr instanceof StringExpression) {
            String value = ((StringExpression)expr).getValue();
            writer.append("\"");
            writer.append(Strings.escape(value));
            writer.append("\"");
            return;
        } else if (expr instanceof EnumLiteralExpression) {
            EnumLiteralExpression lexpr = (EnumLiteralExpression)expr;
            String name = lexpr.getLiteral().getName();
            writer.write(name);
            return;
        }

        // Container literals.
        if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            writer.append("[");
            boolean first = true;
            for (Expression child: lexpr.getElements()) {
                if (!first) {
                    writer.append(", ");
                }
                first = false;
                writeLiteral(child, writer);
            }
            writer.append("]");
            return;
        } else if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            writer.append("{");
            boolean first = true;
            for (Expression child: sexpr.getElements()) {
                if (!first) {
                    writer.append(", ");
                }
                first = false;
                writeLiteral(child, writer);
            }
            writer.append("}");
            return;
        } else if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            writer.append("(");
            boolean first = true;
            for (Expression child: texpr.getFields()) {
                if (!first) {
                    writer.append(", ");
                }
                first = false;
                writeLiteral(child, writer);
            }
            writer.append(")");
            return;
        } else if (expr instanceof DictExpression) {
            DictExpression dexpr = (DictExpression)expr;
            writer.append("{");
            boolean first = true;
            for (DictPair pair: dexpr.getPairs()) {
                if (!first) {
                    writer.append(", ");
                }
                first = false;
                writeLiteral(pair.getKey(), writer);
                writer.append(":");
                writeLiteral(pair.getValue(), writer);
            }
            writer.append("}");
            return;
        }

        // Casts.
        if (expr instanceof CastExpression) {
            writeLiteral(((CastExpression)expr).getChild(), writer);
            return;
        }

        // Operators.
        if (expr instanceof UnaryExpression) {
            boolean negate = false;
            while (expr instanceof UnaryExpression) {
                UnaryExpression uexpr = (UnaryExpression)expr;
                UnaryOperator op = uexpr.getOperator();
                if (op == UnaryOperator.NEGATE) {
                    negate = !negate;
                }
                expr = uexpr.getChild();
            }
            if (negate) {
                writer.append("-");
            }
            writeLiteral(expr, writer);
            return;
        }

        // Unsupported.
        throw new RuntimeException("Unsupported literal expr: " + expr);
    }

    /**
     * Generate code for reading literals.
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeLiteralReaders(CifCompilerContext ctxt) {
        // If no literal types, no read code to generate.
        Map<TypeEqHashWrap, String> literalTypes = ctxt.literalTypes;
        if (literalTypes.isEmpty()) {
            return;
        }

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile(LITERAL_READER_CLS_NAME);
        file.imports.add("org.eclipse.escet.common.app.framework.exceptions.InputOutputException");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Literal reader. */");
        h.add("public final class %s {", LITERAL_READER_CLS_NAME);

        // Add body.
        CodeBox c = file.body;

        // Add methods for reading literals of different types.
        boolean first = true;
        for (Entry<TypeEqHashWrap, String> entry: literalTypes.entrySet()) {
            if (!first) {
                c.add();
            }
            first = false;

            CifType type = entry.getKey().type;
            String methodName = entry.getValue();

            // read*(ClassLoader, String).
            c.add("// %s", CifTextUtils.typeToStr(type));
            c.add("public static %s %s(ClassLoader loader, String path) {", gencodeType(type, ctxt), methodName);
            c.indent();

            c.add("try {");
            c.indent();
            c.add("try (LiteralStream stream = new LiteralStream(loader, path)) {");
            c.indent();
            c.add("return %s(stream);", methodName);
            c.dedent();
            c.add("}");
            c.dedent();
            c.add("} catch (InputOutputException ex) {");
            c.indent();
            c.add("// Currently literal reading is only used internally,");
            c.add("// so we expect no exceptions here.");
            c.add("throw new RuntimeException(\"Failed to read literal of type \\\"%s\\\".\", ex);",
                    StringEscapeUtils.escapeJava(CifTextUtils.typeToStr(type)));
            c.dedent();
            c.add("}");

            c.dedent();
            c.add("}");

            // read*(String).
            c.add();
            c.add("// %s", CifTextUtils.typeToStr(type));
            c.add("public static %s %s(String valueText) {", gencodeType(type, ctxt), methodName);
            c.indent();

            c.add("try {");
            c.indent();
            c.add("try (LiteralStream stream = new LiteralStream(valueText)) {");
            c.indent();
            c.add("return %s(stream);", methodName);
            c.dedent();
            c.add("}");
            c.dedent();
            c.add("} catch (InputOutputException ex) {");
            c.indent();
            c.add("throw new InputOutputException(\"Failed to read literal of type \\\"%s\\\".\", ex);",
                    StringEscapeUtils.escapeJava(CifTextUtils.typeToStr(type)));
            c.dedent();
            c.add("}");

            c.dedent();
            c.add("}");

            // read*(LiteralStream).
            c.add();
            c.add("// %s", CifTextUtils.typeToStr(type));
            c.add("public static %s %s(LiteralStream stream) {", gencodeType(type, ctxt), methodName);
            c.indent();

            c.add("try {");
            c.indent();
            c.add("return %sInternal(stream);", methodName);
            c.dedent();
            c.add("} catch (InputOutputException ex) {");
            c.indent();
            c.add("throw new InputOutputException(\"Failed to read literal of type \\\"%s\\\".\", ex);",
                    StringEscapeUtils.escapeJava(CifTextUtils.typeToStr(type)));
            c.dedent();
            c.add("}");

            c.dedent();
            c.add("}");

            // readInternal*(LiteralStream).
            c.add();
            c.add("// %s", CifTextUtils.typeToStr(type));
            c.add("private static %s %sInternal(LiteralStream stream) {", gencodeType(type, ctxt), methodName);
            c.indent();

            type = CifTypeUtils.normalizeType(type);
            if (type instanceof IntType) {
                IntType itype = (IntType)type;
                int lower = itype.getLower();
                int upper = itype.getUpper();

                String readName = "RuntimeLiteralReader.readIntLiteral";
                c.add("int rslt = %s(stream);", readName);

                c.add("if (rslt < %d) {", lower);
                c.indent();
                c.add("throw new InputOutputException(fmt(\"Expected an integer value that is at least %d, but found "
                        + "integer value %%d.\", rslt));", lower);
                c.dedent();
                c.add("}");

                c.add("if (rslt > %d) {", upper);
                c.indent();
                c.add("throw new InputOutputException(fmt(\"Expected an integer value that is at most %d, but found "
                        + "integer value %%d.\", rslt));", upper);
                c.dedent();
                c.add("}");

                c.add("return rslt;");
            } else if (type instanceof EnumType) {
                EnumType etype = (EnumType)type;
                String className = ctxt.getEnumClassName(etype.getEnum());
                String readName = "RuntimeLiteralReader.readEnumLiteral";
                c.add("return %s(stream, %s.class);", readName, className);
            } else if (type instanceof ListType) {
                ListType ltype = (ListType)type;
                CifType elemType = ltype.getElementType();

                int size = (ltype.getLower() == null) ? 100 : ltype.getLower();
                c.add("%s rslt = new ArrayList<%s>(%d);", gencodeType(type, ctxt), gencodeType(elemType, ctxt, true),
                        size);

                c.add("stream.expectCharacter('[');");
                c.add("while (true) {");
                c.indent();
                c.add("if (stream.matchCharacter(']')) break;");
                c.add("%s elem = %s(stream);", gencodeType(elemType, ctxt), ctxt.getLiteralReadMethodName(elemType));
                c.add("rslt.add(elem);");
                c.add("int c = stream.expectCharacter(',', ']');");
                c.add("if (c == ',') continue;");
                c.add("if (c == ']') break;");
                c.dedent();
                c.add("}");

                if (ltype.getLower() != null) {
                    c.add("if (rslt.size() < %d) {", ltype.getLower());
                    c.indent();
                    c.add("throw new InputOutputException(fmt(\"Expected a list with at least %d elements, but found "
                            + "a list with %%d elements.\", rslt.size()));", ltype.getLower());
                    c.dedent();
                    c.add("}");
                }

                if (ltype.getUpper() != null) {
                    c.add("if (rslt.size() > %d) {", ltype.getUpper());
                    c.indent();
                    c.add("throw new InputOutputException(fmt(\"Expected a list with at most %d elements, but found "
                            + "a list with %%d elements.\", rslt.size()));", ltype.getUpper());
                    c.dedent();
                    c.add("}");
                }

                c.add("return rslt;");
            } else if (type instanceof SetType) {
                SetType stype = (SetType)type;
                CifType elemType = stype.getElementType();

                c.add("%s rslt = new LinkedHashSet<%s>();", gencodeType(type, ctxt), gencodeType(elemType, ctxt, true));

                c.add("stream.expectCharacter('{');");
                c.add("while (true) {");
                c.indent();
                c.add("if (stream.matchCharacter('}')) break;");
                c.add("%s elem = %s(stream);", gencodeType(elemType, ctxt), ctxt.getLiteralReadMethodName(elemType));
                c.add("rslt.add(elem);");
                c.add("int c = stream.expectCharacter(',', '}');");
                c.add("if (c == ',') continue;");
                c.add("if (c == '}') break;");
                c.dedent();
                c.add("}");
                c.add("return rslt;");
            } else if (type instanceof TupleType) {
                TupleType ttype = (TupleType)type;
                List<Field> fields = ttype.getFields();

                StringBuilder args = new StringBuilder();
                c.add("stream.expectCharacter('(');");
                for (int i = 0; i < fields.size(); i++) {
                    CifType fieldType = fields.get(i).getType();
                    if (i > 0) {
                        c.add("stream.expectCharacter(',');");
                        args.append(", ");
                    }

                    c.add("%s elem%d = %s(stream);", gencodeType(fieldType, ctxt), i,
                            ctxt.getLiteralReadMethodName(fieldType));
                    args.append("elem");
                    args.append(i);
                }
                c.add("stream.expectCharacter(')');");

                String className = ctxt.getTupleTypeClassName(ttype);
                c.add("return new %s(%s);", className, args.toString());
            } else if (type instanceof DictType) {
                DictType dtype = (DictType)type;
                CifType keyType = dtype.getKeyType();
                CifType valueType = dtype.getValueType();

                c.add("%s rslt = new LinkedHashMap<%s, %s>();", gencodeType(type, ctxt),
                        gencodeType(keyType, ctxt, true), gencodeType(valueType, ctxt, true));

                c.add("stream.expectCharacter('{');");
                c.add("while (true) {");
                c.indent();
                c.add("if (stream.matchCharacter('}')) break;");
                c.add("%s key = %s(stream);", gencodeType(keyType, ctxt), ctxt.getLiteralReadMethodName(keyType));
                c.add("stream.expectCharacter(':');");
                c.add("%s value = %s(stream);", gencodeType(valueType, ctxt), ctxt.getLiteralReadMethodName(valueType));
                c.add("rslt.put(key, value);");
                c.add("int c = stream.expectCharacter(',', '}');");
                c.add("if (c == ',') continue;");
                c.add("if (c == '}') break;");
                c.dedent();
                c.add("}");
                c.add("return rslt;");
            } else {
                throw new RuntimeException("Unexpected type: " + type);
            }

            c.dedent();
            c.add("}");
        }
    }
}
