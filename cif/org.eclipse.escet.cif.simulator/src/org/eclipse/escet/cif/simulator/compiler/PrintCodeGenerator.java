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

import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.EVENT;
import static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.FINAL;
import static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.INITIAL;
import static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.TIME;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFile;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Lists;
import org.eclipse.escet.common.java.Strings;

/** Print I/O declarations code generator. */
public class PrintCodeGenerator {
    /** Constructor for the {@link PrintCodeGenerator} class. */
    private PrintCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the print I/O declarations of a CIF specification.
     *
     * @param spec The CIF specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodePrint(Specification spec, CifCompilerContext ctxt) {
        // Collect files/targets and the print declarations that apply to them.
        Map<String, List<Print>> decls = map();
        collect(":stdout", ctxt.getSpecFileDir(), decls);
        collect(spec, ":stdout", ctxt.getSpecFileDir(), decls);

        // Generate code per file/target.
        int idx = -1;
        for (Entry<String, List<Print>> entry: decls.entrySet()) {
            // Get absolute path (or special target), and print declarations
            // that apply to it.
            String absPath = entry.getKey();
            List<Print> prints = entry.getValue();

            // If the special stdout target has no print declarations that
            // apply to it, don't generate a class for it. Note that stdout is
            // always collected. This is a performance optimization.
            if (absPath.equals(":stdout") && prints.isEmpty()) {
                continue;
            }

            // Generate a class.
            idx++;
            gencodePrints(absPath, idx, prints, ctxt);
        }

        // Let context know about the number of files/targets/classes.
        ctxt.printFileCount = idx + 1;
    }

    /**
     * Collects the files/targets and the print declarations that apply to them, from the given component, recursively.
     *
     * @param comp The component.
     * @param absPath The absolute local file system path of the currently active file/target (from the parent
     *     component).
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification.
     * @param decls The mapping from absolute local file system paths of files/targets to their print declarations. Is
     *     modified in-place.
     */
    private static void collect(ComplexComponent comp, String absPath, String cifSpecFileDir,
            Map<String, List<Print>> decls)
    {
        // Get print file declaration that applies to this component.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            if (ioDecl instanceof PrintFile) {
                PrintFile printFile = (PrintFile)ioDecl;
                absPath = collect(printFile.getPath(), cifSpecFileDir, decls);
                break; // At most one per scope.
            }
        }

        // Add print declarations of this scope.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            if (ioDecl instanceof Print) {
                Print print = (Print)ioDecl;
                String declAbsPath = absPath;
                if (print.getFile() != null) {
                    declAbsPath = collect(print.getFile().getPath(), cifSpecFileDir, decls);
                }
                decls.get(declAbsPath).add(print);
            }
        }

        // Collect recursively for child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collect((ComplexComponent)child, absPath, cifSpecFileDir, decls);
            }
        }
    }

    /**
     * Collects the print file declaration.
     *
     * @param relPath The absolute or relative local file system path of the print file declaration.
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification.
     * @param decls The mapping from absolute local file system paths of files/targets to their print declarations. Is
     *     modified in-place.
     * @return The absolute local file system path of the print file declaration.
     */
    private static String collect(String relPath, String cifSpecFileDir, Map<String, List<Print>> decls) {
        // Get absolute local file system path of the file/target.
        String absPath = relPath.startsWith(":") ? relPath : Paths.resolve(relPath, cifSpecFileDir);

        // Add file/target, if not yet present.
        if (!decls.containsKey(absPath)) {
            decls.put(absPath, Lists.<Print>list());
        }

        // Return absolute path.
        return absPath;
    }

    /**
     * Generate Java code for the print declarations of a CIF specification, for a single file/target.
     *
     * @param absPath The absolute local file system path of the file/target.
     * @param idx The 0-based index of the file/target in the CIF specification.
     * @param prints The print declarations for this file/target.
     * @param ctxt The compiler context to use.
     */
    private static void gencodePrints(String absPath, int idx, List<Print> prints, CifCompilerContext ctxt) {
        // Get relative path for file/target.
        String relPath;
        if (absPath.startsWith(":")) {
            relPath = absPath;
        } else {
            relPath = Paths.getRelativePath(absPath, ctxt.getSpecFileDir());
        }

        // Warn about files/targets without print declarations. This method is
        // never invoked for stdout, if it has no print declarations in the CIF
        // specification.
        if (prints.isEmpty()) {
            warn("Print file \"%s\" has no print declarations that apply to it.", relPath);
        }

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("CifPrint" + str(idx));
        file.imports.add("static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.*");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Print declarations. */");
        h.add("public final class CifPrint%d extends RuntimePrintDecls {", idx);

        // Add body.
        CodeBox c = file.body;

        // Add 'EVENT_BITS*' fields.
        boolean[] eventBits = new boolean[prints.size()];
        boolean anyBits = false;
        for (int i = 0; i < prints.size(); i++) {
            Print print = prints.get(i);
            boolean bits = gencodeEventBits(print, i, c, ctxt);
            eventBits[i] = bits;
            if (bits) {
                anyBits = true;
            }
        }

        // Add constructor.
        if (anyBits) {
            c.add();
            c.add("public CifPrint%d() {", idx);
            c.indent();
            for (int i = 0; i < prints.size(); i++) {
                if (eventBits[i]) {
                    c.add("initEventBits%d();", i);
                }
            }
            c.dedent();
            c.add("}");
        }

        // Add 'getRelPath' method.
        c.add();
        c.add("@Override");
        c.add("public String getRelPath() {");
        c.indent();
        c.add("return %s;", Strings.stringToJava(relPath));
        c.dedent();
        c.add("}");

        // Add 'getAbsPath' method.
        c.add();
        c.add("@Override");
        c.add("public String getAbsPath() {");
        c.indent();
        c.add("return %s;", Strings.stringToJava(absPath));
        c.dedent();
        c.add("}");

        // Add 'print' method.
        c.add();
        c.add("@Override");
        c.add("public void print(RuntimeState preState, RuntimeState postState, PrintTransitionKind kind, "
                + "int eventIdx) {");
        c.indent();
        c.add("printPre(preState, postState, kind, eventIdx);");
        c.add("printPost(preState, postState, kind, eventIdx);");
        c.add("stream.flush();");
        c.dedent();
        c.add("}");

        // Add 'printPre' method.
        c.add();
        c.add("private void printPre(RuntimeState preState, RuntimeState postState, PrintTransitionKind kind, "
                + "int eventIdx) {");
        c.indent();
        for (int i = 0; i < prints.size(); i++) {
            Print print = prints.get(i);
            if (print.getTxtPre() != null) {
                c.add("print%d(true, preState, postState, kind, eventIdx);", i);
            }
        }
        c.dedent();
        c.add("}");

        // Add 'printPost' method.
        c.add();
        c.add("private void printPost(RuntimeState preState, RuntimeState postState, PrintTransitionKind kind, "
                + "int eventIdx) {");
        c.indent();
        for (int i = 0; i < prints.size(); i++) {
            Print print = prints.get(i);
            if (print.getTxtPost() != null) {
                c.add("print%d(false, preState, postState, kind, eventIdx);", i);
            }
        }
        c.dedent();
        c.add("}");

        // Add 'print*' methods.
        for (int i = 0; i < prints.size(); i++) {
            Print print = prints.get(i);

            c.add();
            c.add("private void print%d(boolean pre, RuntimeState _preState, RuntimeState _postState, "
                    + "PrintTransitionKind kind, int eventIdx) {", i);
            c.indent();
            c.add("State preState = (State)_preState;");
            c.add("State postState = (State)_postState;");
            c.add();
            gencodePrint(print, i, c, ctxt);
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Generate Java code for the 'event bits' for a single print declaration.
     *
     * @param print The print declaration.
     * @param idx The 0-based index of the print declaration.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     * @return Whether event bits were generated for this print declaration ({@code true}) or not ({@code false}).
     */
    private static boolean gencodeEventBits(Print print, int idx, CodeBox c, CifCompilerContext ctxt) {
        // Gather specific events.
        List<Event> filterEvents = list();
        for (PrintFor printFor: print.getFors()) {
            Expression eventRef = printFor.getEvent();
            if (eventRef == null) {
                continue;
            }
            Event event = ((EventExpression)eventRef).getEvent();
            filterEvents.add(event);
        }

        // If no specific events, then no fields.
        if (filterEvents.isEmpty()) {
            return false;
        }

        // Get all events from the specification.
        List<Event> events = ctxt.getEvents();

        // Add 'EVENT_BITS*' field.
        c.add();
        c.add("private final BitSet EVENT_BITS%d = new BitSet(%d);", idx, events.size());

        // Add 'initBits*' method.
        c.add();
        c.add("private void initEventBits%d() {", idx);
        c.indent();
        for (Event event: filterEvents) {
            int eventIdx = events.indexOf(event);
            c.add("EVENT_BITS%d.set(%d);", idx, eventIdx);
        }
        c.dedent();
        c.add("}");

        // We generated code for the event bits.
        return true;
    }

    /**
     * Generate Java code for the method for a single print declaration.
     *
     * @param print The print declaration.
     * @param idx The 0-based index of the print declaration.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodePrint(Print print, int idx, CodeBox c, CifCompilerContext ctxt) {
        // Start with all runtime kinds.
        Set<PrintTransitionKind> kinds = set();
        for (PrintTransitionKind kind: PrintTransitionKind.values()) {
            kinds.add(kind);
        }

        // Remove matching runtime kinds.
        boolean allEvents = false;
        for (PrintFor printFor: print.getFors()) {
            // Switch on metamodel kinds, and remove runtime kinds.
            switch (printFor.getKind()) {
                case NAME:
                    kinds.remove(EVENT);
                    break;
                case EVENT:
                    kinds.remove(EVENT);
                    allEvents = true;
                    break;
                case TIME:
                    kinds.remove(TIME);
                    break;
                case INITIAL:
                    kinds.remove(INITIAL);
                    break;
                case FINAL:
                    kinds.remove(FINAL);
                    break;
            }
        }

        // Handle default 'for'.
        if (print.getFors().isEmpty()) {
            kinds.remove(INITIAL);
            kinds.remove(EVENT);
            kinds.remove(TIME);
            allEvents = true;
        }

        // Generate code to skip non-matching kinds.
        for (PrintTransitionKind kind: kinds) {
            c.add("if (kind == %s) return;", kind.name());
        }

        // Filter on events, if applicable (at least one name and no 'event').
        if (!kinds.contains(EVENT) && !allEvents) {
            c.add("if (kind == EVENT && !EVENT_BITS%d.get(eventIdx)) return;", idx);
        }

        // Filter on 'pre' state, if applicable.
        Expression whenPre = print.getWhenPre();
        if (whenPre != null) {
            c.add();
            c.add("boolean whenPre;");
            c.add("try {");
            c.indent();
            c.add("whenPre = %s;", gencodeExpr(whenPre, ctxt, "preState"));
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of print declaration \\\"when pre\\\" filter "
                    + "\\\"%s\\\" failed.\", e, preState);", escapeJava(exprToStr(whenPre)));
            c.dedent();
            c.add("}");
            c.add("if (!whenPre) return;");
        }

        // Filter on 'post' state, if applicable.
        Expression whenPost = print.getWhenPost();
        if (whenPost != null) {
            c.add();
            c.add("boolean whenPost;");
            c.add("try {");
            c.indent();
            c.add("whenPost = %s;", gencodeExpr(whenPost, ctxt, "postState"));
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of print declaration \\\"when post\\\" filter "
                    + "\\\"%s\\\" failed.\", e, postState);", escapeJava(exprToStr(whenPost)));
            c.dedent();
            c.add("}");
            c.add("if (!whenPost) return;");
        }

        // Case for 'pre' text.
        c.add();
        c.add("String txt;");
        c.add("if (pre) {");
        c.indent();

        Expression txtPre = print.getTxtPre();
        if (txtPre == null) {
            c.add("throw new RuntimeException(); // Should't get here.");
        } else {
            CifType type = txtPre.getType();
            CifType ntype = normalizeType(type);

            // Evaluate text value expression.
            c.add("%s value;", gencodeType(ntype, ctxt));
            c.add("try {");
            c.indent();
            c.add("value = %s;", gencodeExpr(txtPre, ctxt, "preState"));
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of print declaration \\\"pre\\\" text \\\"%s\\\" "
                    + "failed.\", e, preState);", escapeJava(exprToStr(txtPre)));
            c.dedent();
            c.add("}");

            // Obtain actual text.
            c.add();
            if (ntype instanceof StringType) {
                c.add("txt = value;");
            } else {
                c.add("txt = runtimeToString(value);");
            }
        }

        // Case for 'post' text.
        c.dedent();
        c.add("} else {");
        c.indent();

        Expression txtPost = print.getTxtPost();
        if (txtPost == null) {
            c.add("throw new RuntimeException(); // Should't get here.");
        } else {
            CifType type = txtPost.getType();
            CifType ntype = normalizeType(type);

            // Evaluate text value expression.
            c.add("%s value;", gencodeType(ntype, ctxt));
            c.add("try {");
            c.indent();
            c.add("value = %s;", gencodeExpr(txtPost, ctxt, "postState"));
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of print declaration \\\"post\\\" text \\\"%s\\\" "
                    + "failed.\", e, postState);", escapeJava(exprToStr(txtPost)));
            c.dedent();
            c.add("}");

            // Obtain actual text.
            c.add();
            if (ntype instanceof StringType) {
                c.add("txt = value;");
            } else {
                c.add("txt = runtimeToString(value);");
            }
        }

        // Obtained text.
        c.dedent();
        c.add("}");

        // Do actual printing.
        c.add();
        c.add("stream.println(txt);");
    }
}
