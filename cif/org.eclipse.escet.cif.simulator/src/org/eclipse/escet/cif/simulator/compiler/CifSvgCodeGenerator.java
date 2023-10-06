//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.apache.commons.text.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgCopy;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgMove;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Strings;

/** CIF/SVG declarations code generator. */
public class CifSvgCodeGenerator {
    /** Constructor for the {@link CifSvgCodeGenerator} class. */
    private CifSvgCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the CIF/SVG declarations of a CIF specification.
     *
     * @param spec The CIF specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeCifSvg(Specification spec, CifCompilerContext ctxt) {
        // Collect SVG files and the declarations that apply to them.
        Map<String, CifSvgDecls> decls = map();
        collect(spec, null, ctxt.getSpecFileDir(), decls);

        // Generate code per SVG file.
        Set<Entry<String, CifSvgDecls>> entries = decls.entrySet();
        int idx = -1;
        for (Entry<String, CifSvgDecls> entry: entries) {
            String svgAbsPath = entry.getKey();
            CifSvgDecls cifSvgDecls = entry.getValue();
            idx++;
            gencodeCifSvg(svgAbsPath, idx, cifSvgDecls, ctxt);
        }

        // Let context know about the number of SVG files.
        ctxt.svgFileCount = idx + 1;
    }

    /**
     * Collects the SVG files and their declarations, from the given component, recursively.
     *
     * @param comp The component.
     * @param svgAbsPath The absolute local file system path of the currently active SVG file declaration (from the
     *     parent component), or {@code null} if not available/applicable.
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification.
     * @param decls The mapping from absolute local file system paths of SVG files to their CIF/SVG declarations. Is
     *     modified in-place.
     */
    private static void collect(ComplexComponent comp, String svgAbsPath, String cifSpecFileDir,
            Map<String, CifSvgDecls> decls)
    {
        // Get SVG file declaration that applies to this component.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            if (ioDecl instanceof SvgFile) {
                svgAbsPath = collect((SvgFile)ioDecl, cifSpecFileDir, decls);
                break; // At most one per scope.
            }
        }

        // Add CIF/SVG declarations of this scope.
        for (IoDecl ioDecl: comp.getIoDecls()) {
            if (ioDecl instanceof SvgCopy) {
                SvgCopy svgCopy = (SvgCopy)ioDecl;
                String declSvgAbsPath = svgAbsPath;
                if (svgCopy.getSvgFile() != null) {
                    declSvgAbsPath = collect(svgCopy.getSvgFile(), cifSpecFileDir, decls);
                }
                decls.get(declSvgAbsPath).svgCopies.add(svgCopy);
            } else if (ioDecl instanceof SvgMove) {
                SvgMove svgMove = (SvgMove)ioDecl;
                String declSvgAbsPath = svgAbsPath;
                if (svgMove.getSvgFile() != null) {
                    declSvgAbsPath = collect(svgMove.getSvgFile(), cifSpecFileDir, decls);
                }
                decls.get(declSvgAbsPath).svgMoves.add(svgMove);
            } else if (ioDecl instanceof SvgOut) {
                SvgOut svgOut = (SvgOut)ioDecl;
                String declSvgAbsPath = svgAbsPath;
                if (svgOut.getSvgFile() != null) {
                    declSvgAbsPath = collect(svgOut.getSvgFile(), cifSpecFileDir, decls);
                }
                decls.get(declSvgAbsPath).svgOuts.add(svgOut);
            } else if (ioDecl instanceof SvgIn) {
                SvgIn svgIn = (SvgIn)ioDecl;
                String declSvgAbsPath = svgAbsPath;
                if (svgIn.getSvgFile() != null) {
                    declSvgAbsPath = collect(svgIn.getSvgFile(), cifSpecFileDir, decls);
                }
                decls.get(declSvgAbsPath).svgIns.add(svgIn);
            }
        }

        // Collect recursively for child components.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collect((ComplexComponent)child, svgAbsPath, cifSpecFileDir, decls);
            }
        }
    }

    /**
     * Collects the SVG file declaration.
     *
     * @param svgFile The SVG file declaration.
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification.
     * @param decls The mapping from absolute local file system paths of SVG files to their CIF/SVG declarations. Is
     *     modified in-place.
     * @return The absolute local file system path of the SVG file.
     */
    private static String collect(SvgFile svgFile, String cifSpecFileDir, Map<String, CifSvgDecls> decls) {
        // Get absolute local file system path of the SVG file.
        String svgAbsPath = Paths.resolve(svgFile.getPath(), cifSpecFileDir);

        // Add SVG file, if not yet present.
        if (!decls.containsKey(svgAbsPath)) {
            decls.put(svgAbsPath, new CifSvgDecls());
        }

        // Return absolute path.
        return svgAbsPath;
    }

    /**
     * Generate Java code for the CIF/SVG declarations of a CIF specification, for a single SVG file.
     *
     * @param svgAbsPath The absolute local file system path of the SVG file.
     * @param idx The 0-based index of the SVG file in the CIF specification.
     * @param cifSvgDecls The CIF/SVG declarations.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeCifSvg(String svgAbsPath, int idx, CifSvgDecls cifSvgDecls, CifCompilerContext ctxt) {
        // Warn about SVG files without input/output mappings. We only check
        // for mappings, as they can result in changes to the image during
        // simulation.
        String svgRelPath = Paths.getRelativePath(svgAbsPath, ctxt.getSpecFileDir());
        if (cifSvgDecls.svgOuts.isEmpty() && cifSvgDecls.svgIns.isEmpty()) {
            warn("SVG file \"%s\" has no CIF/SVG input/output mappings that apply to it.", svgRelPath);
        }

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("CifSvg" + str(idx));
        file.imports.add("org.w3c.dom.Element");
        file.imports.add("org.w3c.dom.Text");
        file.imports.add("org.w3c.dom.svg.SVGStylable");
        file.imports.add("org.eclipse.escet.common.svg.SvgUtils");

        // Add header.
        CodeBox h = file.header;
        h.add("/** CIF/SVG mappings. */");
        h.add("public final class CifSvg%d extends RuntimeCifSvgDecls {", idx);

        // Add body.
        CodeBox c = file.body;

        // Evaluate ids etc.
        List<String> copyIds = listc(cifSvgDecls.svgCopies.size());
        List<String> copyPres = listc(cifSvgDecls.svgCopies.size());
        List<String> copyPosts = listc(cifSvgDecls.svgCopies.size());
        List<String> moveIds = listc(cifSvgDecls.svgMoves.size());
        List<Double> moveXs = listc(cifSvgDecls.svgMoves.size());
        List<Double> moveYs = listc(cifSvgDecls.svgMoves.size());
        List<String> outIds = listc(cifSvgDecls.svgOuts.size());
        List<String> inIds = listc(cifSvgDecls.svgIns.size());
        for (SvgCopy svgCopy: cifSvgDecls.svgCopies) {
            copyIds.add(evalSvgStringExpr(svgCopy.getId()));
            copyPres.add((svgCopy.getPre() == null) ? "" : evalSvgStringExpr(svgCopy.getPre()));
            copyPosts.add((svgCopy.getPost() == null) ? "" : evalSvgStringExpr(svgCopy.getPost()));
        }
        for (SvgMove svgMove: cifSvgDecls.svgMoves) {
            moveIds.add(evalSvgStringExpr(svgMove.getId()));
            moveXs.add(evalSvgNumberExpr(svgMove.getX()));
            moveYs.add(evalSvgNumberExpr(svgMove.getY()));
        }
        for (SvgOut svgOut: cifSvgDecls.svgOuts) {
            outIds.add(evalSvgStringExpr(svgOut.getId()));
        }
        for (SvgIn svgIn: cifSvgDecls.svgIns) {
            inIds.add(evalSvgStringExpr(svgIn.getId()));
        }

        // Add mapping from interactive SVG element ids to input mapping
        // indices.
        c.add("public static final Map<String, Integer> ID_TO_INPUT_IDX;");
        c.add();
        c.add("static {");
        c.indent();
        c.add("ID_TO_INPUT_IDX = mapc(%d);", cifSvgDecls.svgIns.size());
        for (int i = 0; i < cifSvgDecls.svgIns.size(); i++) {
            c.add("ID_TO_INPUT_IDX.put(%s, %d);", Strings.stringToJava(inIds.get(i)), i);
        }
        c.dedent();
        c.add("}");
        c.add();

        // Add fields for the elements and text nodes of the output mappings.
        for (int i = 0; i < cifSvgDecls.svgOuts.size(); i++) {
            SvgOut svgOut = cifSvgDecls.svgOuts.get(i);
            c.add("private Element outElem%d;", i);

            if (svgOut.getAttr() != null) {
                // Generate style fields as well. May or may not be used at
                // runtime.
                c.add("private SVGStylable outStyle%d;", i);
            } else {
                c.add("private Text outText%d;", i);
            }

            c.add();
        }

        // Add 'getSvgRelPath' method.
        c.add("@Override");
        c.add("public String getSvgRelPath() {");
        c.indent();
        c.add("return %s;", Strings.stringToJava(svgRelPath));
        c.dedent();
        c.add("}");
        c.add();

        // Add 'getSvgAbsPath' method.
        c.add("@Override");
        c.add("public String getSvgAbsPath() {");
        c.indent();
        c.add("return %s;", Strings.stringToJava(svgAbsPath));
        c.dedent();
        c.add("}");
        c.add();

        // Add 'getCopyCount' method.
        c.add("@Override");
        c.add("protected int getCopyCount() {");
        c.indent();
        c.add("return %d;", copyIds.size());
        c.dedent();
        c.add("}");
        c.add();

        // Add 'applyCopy' method.
        c.add("@Override");
        c.add("protected boolean applyCopy(int idx) {");
        c.indent();
        c.add("switch (idx) {");
        c.indent();
        for (int i = 0; i < copyIds.size(); i++) {
            c.add("case %d: return applyCopy(%s, %s, %s);", i, Strings.stringToJava(copyIds.get(i)),
                    Strings.stringToJava(copyPres.get(i)), Strings.stringToJava(copyPosts.get(i)));
        }
        c.dedent();
        c.add("}");
        c.add("throw new RuntimeException(\"Unknown idx: \" + idx);");
        c.dedent();
        c.add("}");
        c.add();

        // Add 'applyMoves' method.
        c.add("@Override");
        c.add("protected void applyMoves() {");
        c.indent();
        for (int i = 0; i < moveIds.size(); i++) {
            c.add("applyMove(%s, %s, %s);", Strings.stringToJava(moveIds.get(i)),
                    CifSimulatorMath.realToStr(moveXs.get(i)), CifSimulatorMath.realToStr(moveYs.get(i)));
        }
        if (!moveIds.isEmpty()) {
            c.add();
            c.add("if (debug) dbg.println();");
        }
        c.dedent();
        c.add("}");
        c.add();

        // Add 'initCaches' method.
        c.add();
        c.add("@Override");
        c.add("protected void initCaches() {");
        c.indent();
        for (int i = 0; i < cifSvgDecls.svgOuts.size(); i++) {
            SvgOut svgOut = cifSvgDecls.svgOuts.get(i);
            c.add("outElem%d = document.getElementById(%s);", i, Strings.stringToJava(outIds.get(i)));

            if (svgOut.getAttr() != null) {
                c.add("outStyle%d = SvgUtils.isCssAttr(outElem%d, %s) ? (SVGStylable)outElem%d : null;", i, i,
                        Strings.stringToJava(svgOut.getAttr()), i);
            } else {
                c.add("outText%d = SvgUtils.getTextNode(outElem%d);", i, i);
            }
        }
        c.dedent();
        c.add("}");
        c.add();

        // Add 'applyOutputInternal' method.
        c.add();
        c.add("@Override");
        c.add("protected void applyOutputInternal(RuntimeState _state) {");
        c.indent();
        c.add("State state = (State)_state;");
        for (int i = 0; i < cifSvgDecls.svgOuts.size(); i++) {
            c.add("applyOutput%d(state);", i);
        }
        if (!cifSvgDecls.svgOuts.isEmpty()) {
            c.add();
            c.add("if (debug) dbg.println();");
        }
        c.dedent();
        c.add("}");
        c.add();

        // Add 'applyOutput*' methods.
        for (int i = 0; i < cifSvgDecls.svgOuts.size(); i++) {
            SvgOut svgOut = cifSvgDecls.svgOuts.get(i);

            c.add();
            c.add("private void applyOutput%d(State state) {", i);
            c.indent();
            ExprCodeGeneratorResult result = gencodeSvgOut(svgOut, i, outIds.get(i), c, ctxt);
            c.dedent();
            c.add("}");

            // Add potential extra expression evaluation methods.
            result.addExtraMethods(c);
        }
        c.add();

        // Add 'getInteractiveIds' method. Only called once, so no need to
        // use a static field to 'cache' the returned data.
        c.add();
        c.add("@Override");
        c.add("public Set<String> getInteractiveIds() {");
        c.indent();

        List<String> interactiveIds = listc(cifSvgDecls.svgIns.size());
        for (String inId: inIds) {
            interactiveIds.add(Strings.stringToJava(inId));
        }

        if (interactiveIds.isEmpty()) {
            c.add("return Collections.emptySet();");
        } else {
            c.add("return set(%s);", String.join(", ", interactiveIds));
        }

        c.dedent();
        c.add("}");
        c.add();

        // Get events and event to index mapping.
        List<Event> events = ctxt.getEvents();
        Map<Event, Integer> eventMap = mapc(events.size());
        for (int i = 0; i < events.size(); i++) {
            eventMap.put(events.get(i), i);
        }

        // Add 'getInteractiveEvents' method. Only called once, so no need to
        // use a static field to 'cache' the returned data.
        c.add();
        c.add("@Override");
        c.add("public boolean[] getInteractiveEvents() {");
        c.indent();
        gencodeGetInteractiveEvents(cifSvgDecls.svgIns, events, eventMap, c, ctxt);
        c.dedent();
        c.add("}");
        c.add();

        // Add 'applyInput' method.
        c.add();
        c.add("@Override");
        c.add("public int applyInput(String id, RuntimeState _state) {");
        c.indent();
        List<ExprCodeGeneratorResult> svgInsExprResults = gencodeSvgIns(cifSvgDecls.svgIns, inIds, eventMap, c, ctxt);
        c.dedent();
        c.add("}");

        // Add potential extra expression evaluation methods.
        for (ExprCodeGeneratorResult svgInsExprResult: svgInsExprResults) {
            svgInsExprResult.addExtraMethods(c);
        }
    }

    /**
     * Generates code for the given output mapping.
     *
     * @param svgOut The output mapping.
     * @param idx The 0-based index of the output mapping, in the sequence of output mappings.
     * @param id The SVG element id of the mapping.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     * @return The {@code ExprCodeGeneratorResult} for the generated Java code.
     */
    private static ExprCodeGeneratorResult gencodeSvgOut(SvgOut svgOut, int idx, String id, CodeBox c,
            CifCompilerContext ctxt)
    {
        // Start of 'try'.
        c.add("try {");
        c.indent();

        // Get type of value.
        Expression value = svgOut.getValue();
        CifType type = value.getType();
        CifType ntype = normalizeType(type);

        // Generate code for evaluation of the value.
        c.add("%s value;", gencodeType(ntype, ctxt));
        c.add("try {");
        c.indent();
        ExprCodeGeneratorResult result = gencodeExpr(value, ctxt, "state");
        c.add("value = %s;", result);
        c.dedent();
        c.add("} catch (CifSimulatorException e) {");
        c.indent();
        c.add("throw new CifSimulatorException(\"Evaluation of SVG output mapping value \\\"%s\\\" failed.\", e, "
                + "state);", escapeJava(exprToStr(value)));
        c.dedent();
        c.add("}");

        // Generate code to obtain actual text.
        c.add();
        if (ntype instanceof StringType) {
            c.add("String txt = value;");
        } else {
            c.add("String txt = runtimeToString(value);");
        }

        // Generate code for the update of the SVG document. Also includes
        // the debugging output.
        if (svgOut.getAttr() != null) {
            // Attribute.
            c.add("if (outStyle%d == null) {", idx);
            c.indent();
            c.add("outElem%d.setAttribute(%s, txt);", idx, Strings.stringToJava(svgOut.getAttr()));
            c.add("if (debug) dbg.println(fmt(\"SVG output (\\\"%%s\\\") id \\\"%s\\\" attr \\\"%s\\\" (SVG attr): "
                    + "\\\"%%s\\\"\", getSvgRelPath(), txt));", escapeJava(id), escapeJava(svgOut.getAttr()));
            c.dedent();
            c.add("} else {");
            c.indent();
            c.add("outStyle%d.getStyle().setProperty(%s, txt, \"\");", idx, Strings.stringToJava(svgOut.getAttr()));
            c.add("if (debug) dbg.println(fmt(\"SVG output (\\\"%%s\\\") id \\\"%s\\\" attr \\\"%s\\\" (CSS attr): "
                    + "\\\"%%s\\\"\", getSvgRelPath(), txt));", escapeJava(id), escapeJava(svgOut.getAttr()));
            c.dedent();
            c.add("}");
        } else {
            // Text.
            c.add("outText%d.setNodeValue(txt);", idx);
            c.add("if (debug) dbg.println(fmt(\"SVG output (\\\"%%s\\\") id \\\"%s\\\" text: \\\"%%s\\\"\", "
                    + "getSvgRelPath(), txt));", escapeJava(id));
        }

        // End of 'try'.
        c.dedent();
        c.add("} catch (CifSimulatorException e) {");
        c.indent();
        String attrText;
        if (svgOut.getAttr() != null) {
            attrText = fmt("attribute \\\"%s\\\"", escapeJava(svgOut.getAttr()));
        } else {
            attrText = "the text";
        }
        c.add("throw new CifSimulatorException(fmt(\"Evaluation of the SVG output mapping (\\\"%%s\\\") for %s of the "
                + "SVG element with id \\\"%s\\\" failed.\", getSvgRelPath()), e, state);", attrText, escapeJava(id));
        c.dedent();
        c.add("}");

        return result;
    }

    /**
     * Generates code for the 'getInteractiveEvents' method, for the input mappings.
     *
     * @param svgIns The input mappings.
     * @param events The events of the specification.
     * @param eventMap Mapping from events to their 0-based index.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeGetInteractiveEvents(List<SvgIn> svgIns, List<Event> events,
            Map<Event, Integer> eventMap, CodeBox c, CifCompilerContext ctxt)
    {
        // Get interactive events.
        List<Boolean> interactiveEvents = listc(events.size());
        for (int i = 0; i < events.size(); i++) {
            interactiveEvents.add(false);
        }

        for (SvgIn svgIn: svgIns) {
            SvgInEvent event = svgIn.getEvent();
            if (event instanceof SvgInEventSingle) {
                SvgInEventSingle singleEvt = (SvgInEventSingle)event;
                Event evt = ((EventExpression)singleEvt.getEvent()).getEvent();
                interactiveEvents.set(eventMap.get(evt), true);
            } else if (event instanceof SvgInEventIf) {
                SvgInEventIf ifEvent = (SvgInEventIf)event;
                for (SvgInEventIfEntry entry: ifEvent.getEntries()) {
                    Event evt = ((EventExpression)entry.getEvent()).getEvent();
                    interactiveEvents.set(eventMap.get(evt), true);
                }
            } else {
                throw new RuntimeException("Unknown svgin event: " + event);
            }
        }

        // Generate 'return' statement.
        String boolValues = interactiveEvents.stream().map(b -> b.toString()).collect(Collectors.joining(", "));
        c.add("return new boolean[] {%s};", boolValues);
    }

    /**
     * Generates code for the application of the input mappings.
     *
     * @param svgIns The input mappings.
     * @param inIds The SVG element ids of the mappings.
     * @param eventMap Mapping from events to their 0-based index.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     * @return The {@code ExprCodeGeneratorResult}s for the generated Java code.
     */
    private static List<ExprCodeGeneratorResult> gencodeSvgIns(List<SvgIn> svgIns, List<String> inIds,
            Map<Event, Integer> eventMap, CodeBox c, CifCompilerContext ctxt)
    {
        c.add("State state = (State)_state;");
        c.add("int idx = ID_TO_INPUT_IDX.get(id);");
        c.add("boolean g;");
        c.add("switch (idx) {");
        c.indent();

        List<ExprCodeGeneratorResult> guardResults = list();
        for (int i = 0; i < svgIns.size(); i++) {
            SvgIn svgIn = svgIns.get(i);
            String id = inIds.get(i);
            c.add("case %d:", i);
            c.indent();

            SvgInEvent event = svgIn.getEvent();
            if (event instanceof SvgInEventSingle) {
                // Single event.
                SvgInEventSingle singleEvt = (SvgInEventSingle)event;
                Event evt = ((EventExpression)singleEvt.getEvent()).getEvent();
                c.add("return %d; // %s", eventMap.get(evt), getAbsName(evt));
            } else if (event instanceof SvgInEventIf) {
                // 'if/then/else' event mapping.
                SvgInEventIf ifEvent = (SvgInEventIf)event;

                // Generate 'try' for input mapping runtime failure.
                c.add("try {");
                c.indent();

                // Generate code for entries.
                boolean hasElse = false;
                for (SvgInEventIfEntry entry: ifEvent.getEntries()) {
                    Event evt = ((EventExpression)entry.getEvent()).getEvent();
                    int eventIdx = eventMap.get(evt);
                    String eventName = getAbsName(evt);

                    if (entry.getGuard() == null) {
                        hasElse = true;
                        c.add("return %d; // %s", eventIdx, eventName);
                    } else {
                        // Evaluate guard.
                        c.add("try {");
                        c.indent();
                        ExprCodeGeneratorResult result = gencodeExpr(entry.getGuard(), ctxt, "state");
                        c.add("g = %s;", result);
                        guardResults.add(result);
                        c.dedent();
                        c.add("} catch (CifSimulatorException e) {");
                        c.indent();
                        c.add("throw new CifSimulatorException(\"Evaluation of guard value \\\"%s\\\" failed.\", e, "
                                + "state);", escapeJava(exprToStr(entry.getGuard())));
                        c.dedent();
                        c.add("}");

                        // Return event index.
                        c.add("if (g) return %d; // %s", eventIdx, eventName);
                        c.add();
                    }
                }

                // Generate code for incomplete mapping.
                if (!hasElse) {
                    c.add("throw new CifSimulatorException(\"Incomplete SVG input mapping: none of the guards are "
                            + "satisfied (evaluate to value \\\"true\\\").\", state);");
                }

                // Generate 'catch' for input mapping runtime failure.
                c.dedent();
                c.add("} catch (CifSimulatorException e) {");
                c.indent();
                c.add("throw new CifSimulatorException(fmt(\"Evaluation of the SVG input mapping (\\\"%%s\\\") for "
                        + "the SVG element with id \\\"%s\\\" failed.\", getSvgRelPath()), e, state);", escapeJava(id));
                c.dedent();
                c.add("}");
            } else {
                throw new RuntimeException("Unknown svgin event: " + event);
            }

            c.dedent();
            c.add();
        }

        c.add("default:");
        c.indent();
        c.add("throw new RuntimeException(\"Unknown input mapping idx: \" + idx);");
        c.dedent();

        c.dedent();
        c.add("}");

        return guardResults;
    }

    /**
     * Evaluates a CIF expression that can be statically evaluated for use by a CIF/SVG declaration.
     *
     * @param expr The expression to evaluate. The expression must have a string type.
     * @return The text resulting from evaluation of the expression.
     */
    private static String evalSvgStringExpr(Expression expr) {
        try {
            return (String)CifEvalUtils.eval(expr, false);
        } catch (CifEvalException e) {
            // Shouldn't happen, as type checker already evaluated it.
            throw new RuntimeException(e);
        }
    }

    /**
     * Evaluates a CIF expression that can be statically evaluated for use by a CIF/SVG declaration.
     *
     * @param expr The expression to evaluate. The expression must have an integer or real type.
     * @return The number resulting from evaluation of the expression.
     */
    private static double evalSvgNumberExpr(Expression expr) {
        try {
            Object rslt = CifEvalUtils.eval(expr, false);
            if (rslt instanceof Integer) {
                return (int)rslt;
            }
            if (rslt instanceof Double) {
                return (double)rslt;
            }
            throw new RuntimeException("Number expected: " + rslt);
        } catch (CifEvalException e) {
            // Shouldn't happen, as type checker already evaluated it.
            throw new RuntimeException(e);
        }
    }

    /** CIF/SVG declarations sorted out per type, for a single SVG file. */
    private static class CifSvgDecls {
        /** SVG copy declarations. */
        public final List<SvgCopy> svgCopies = list();

        /** SVG move declarations. */
        public final List<SvgMove> svgMoves = list();

        /** SVG output mapping declarations. */
        public final List<SvgOut> svgOuts = list();

        /** SVG input mapping declarations. */
        public final List<SvgIn> svgIns = list();
    }
}
