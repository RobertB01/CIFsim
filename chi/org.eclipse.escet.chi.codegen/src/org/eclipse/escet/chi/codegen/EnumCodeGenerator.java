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

package org.eclipse.escet.chi.codegen;

import static org.eclipse.escet.chi.codegen.Constants.INDENT_SIZE;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.java.JavaEnum;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.EnumValue;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;

/** Code generator for Chi enum declarations. */
public class EnumCodeGenerator {
    /** Constructor for the {@link EnumCodeGenerator} class. */
    private EnumCodeGenerator() {
        // Static class.
    }

    /** 'Position' while reading an enumeration literal value. */
    public static class EnumReadStage {
        /**
         * Enumeration value found at this point. Value {@code null} means no valid enumeration value has been found
         * yet.
         */
        public String value;

        /** Number of read-ahead characters needed to decide whether this value is really the answer. */
        int markCount;

        /** Continuations to get to the next stage. */
        public List<EnumReadContinuation> continuations;

        /**
         * Constructor for the {@link EnumReadStage} class.
         *
         * @param value Enumeration value that has been recognized at this node in the tree. {@code null} means there is
         *     no such enum value here.
         */
        public EnumReadStage(String value) {
            this.value = value;
            continuations = list();
        }

        /**
         * Count how many look-ahead characters are needed to decide whether this stage is final.
         *
         * @return Number of characters to look ahead.
         */
        public int setupMarkCount() {
            int num = 0;
            for (EnumReadContinuation erc: continuations) {
                int val = erc.stage.setupMarkCount();
                if (val > num) {
                    num = val;
                }
            }
            markCount = num + 1;

            if (value != null) {
                return 0;
            }
            return num + 1;
        }

        /**
         * Generate a box representation of the read code tree.
         *
         * @param streamName Name of the input stream.
         * @param level Recursion level.
         * @return Box representation of the read code.
         */
        public Box boxify(String streamName, int level) {
            VBox vb = new VBox(INDENT_SIZE);
            if (continuations.isEmpty()) {
                // Leaf node.
                Assert.notNull(value);
                vb.add("return EV_" + value + ";");
                return vb;
            }

            // There are continuations.
            if (value != null) {
                // enum has a value here, mark the stream to return here.
                vb.add(fmt("%s.markStream(%d);", streamName, markCount));
            }
            String karName = fmt("kar_%d", level); // Name of the variable.
            if (level == 1) {
                // At the top level, skip white space first.
                vb.add("int " + karName + ";");
                vb.add("do {");
                vb.add(fmt("    %s = %s.read();", karName, streamName));
                vb.add(fmt("} while (%s == ' ' || %s == '\\t' || %s == '\\n' || %s == '\\r');", karName, karName,
                        karName, karName));
            } else {
                vb.add("int " + karName + " = " + streamName + ".read();");
            }
            level++;
            for (EnumReadContinuation erc: continuations) {
                vb.add("if (" + karName + " == '" + erc.next + "') {");
                vb.add(erc.stage.boxify(streamName, level));
                vb.add("}");
            }
            if (value != null) {
                // Since above no return was performed, this value is
                // the best we can find.
                vb.add(fmt("%s.resetStream();", streamName));
                vb.add("return EV_" + value + ";");
            }
            return vb;
        }
    }

    /** Continuations from a read stage to a next read stage. */
    public static class EnumReadContinuation {
        /** Character to read next. */
        public final String next;

        /** Newly reached read stage after reading the character. */
        public final EnumReadStage stage;

        /**
         * Constructor of the {@link EnumReadContinuation} class.
         *
         * @param next Character to read before going to the next position.
         * @param stage Next position in the tree.
         */
        public EnumReadContinuation(String next, EnumReadStage stage) {
            this.next = next;
            this.stage = stage;
        }
    }

    /**
     * Construct a tree with read stages for the given enum declaration.
     *
     * @param ed Given enum declaration.
     * @return Tree of read stages.
     */
    private static EnumReadStage constructReadStage(EnumDeclaration ed) {
        EnumReadStage root = new EnumReadStage(null);
        for (EnumValue ev: ed.getValues()) {
            String valText = ev.getName();
            EnumReadStage current = root;
            for (int i = 0; i < valText.length(); i++) {
                EnumReadStage next = null;
                String s = valText.substring(i, i + 1);
                for (EnumReadContinuation erc: current.continuations) {
                    if (erc.next.equals(s)) {
                        next = erc.stage;
                        break;
                    }
                }
                if (next == null) {
                    next = new EnumReadStage(null);
                    EnumReadContinuation erc = new EnumReadContinuation(s, next);
                    current.continuations.add(erc);
                }
                current = next;
            }
            Assert.check(current.value == null);
            current.value = valText;
        }
        return root;
    }

    /**
     * Generate an enumeration definition.
     *
     * @param tid Type id of the enumeration.
     * @param ed Enumeration definition.
     * @param ctxt Code generator context.
     */
    public static void transEnumDeclaration(TypeID tid, EnumDeclaration ed, CodeGeneratorContext ctxt) {
        String enumName = "ChiEnum_" + ed.getName();
        JavaEnum jc = new JavaEnum(enumName);
        ctxt.addTypeName(tid, enumName);

        for (EnumValue ev: ed.getValues()) {
            jc.addValue("EV_" + ev.getName());
        }

        JavaMethod func;
        // Add write function.
        func = new JavaMethod("public void write(ChiFileHandle stream)");
        jc.addImport(Constants.CHI_FILE_HANDLE_FQC, false);
        // The actual write code.
        func.lines.add("switch (this) {");
        func.lines.indent();
        for (EnumValue ev: ed.getValues()) {
            String valName = "EV_" + ev.getName();
            func.lines.add("case %s: stream.write(\"%s\"); break;", valName, ev.getName());
        }
        func.lines.dedent();
        func.lines.add("}");
        jc.addMethod(func);

        func = new JavaMethod("public String toString()");
        func.lines.add("ChiWriteMemoryFile mem = new ChiWriteMemoryFile();");
        func.lines.add("write(mem);");
        func.lines.add("return mem.getData();");
        jc.addMethod(func);
        jc.addImport(Constants.CHI_WRITE_MEMORY_FILE_FQC, false);

        // Add Read function.
        func = new JavaMethod(
                "public static " + enumName + " read(ChiCoordinator chiCoordinator, ChiFileHandle stream)");
        jc.addImport(Constants.COORDINATOR_FQC, false);
        EnumReadStage root = constructReadStage(ed);
        root.setupMarkCount();
        func.lines.dedent();
        func.lines.add(root.boxify("stream", 1));
        func.lines.add("    throw new ChiSimulatorException(\"Could not find a literal enum value at the input.\");");
        jc.addMethod(func);
        jc.addImport(Constants.CHI_SIMULATOR_EXCEPTION_FQC, false);

        ctxt.addClass(jc);
    }
}
