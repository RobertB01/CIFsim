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

package org.eclipse.escet.cif.codegen.javascript;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.SvgCodeGen;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
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
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.svg.SvgUtils;
import org.w3c.dom.Document;

/** Type code generator for the JavaScript target language. */
public class JavaScriptSvgCodeGen extends SvgCodeGen {
    /** The generated code for the SVG image content and their wrapper elements. */
    CodeBox codeSvgContent;

    /** The generated code for toggling (enabling/disabling) visibility of SVG images. */
    CodeBox codeSvgToggles;

    /** The generated code for applying SVG copy declarations. */
    CodeBox codeCopyApply;

    /** The generated code for applying SVG move declarations. */
    CodeBox codeMoveApply;

    /** The generated code for click event handlers invoked when clicking interactive SVG elements. */
    CodeBox codeInClickHandlers;

    /**
     * The generated code for setting the current SVG input event to process, for an interactive SVG input element that
     * was clicked.
     */
    CodeBox codeInEventSetters;

    /**
     * The generated code for interaction, to register click event listeners, and make sure interactive elements have
     * the right mouse pointer.
     */
    CodeBox codeInSetup;

    /** The generated code with CSS styling for SVG interaction. */
    CodeBox codeInCss;

    /** The generated code for declarations of fields to hold the elements modified by SVG output mappings. */
    CodeBox codeOutDeclarations;

    /** The generated code for assignments to fields that hold the elements modified by SVG output mappings. */
    CodeBox codeOutAssignments;

    /** The generated code for applying SVG output mappings. */
    CodeBox codeOutApply;

    /**
     * Generate code for SVG visualization and interaction.
     *
     * @param ctxt The code generation context.
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification.
     * @param svgDecls The CIF/SVG declarations of the specification.
     * @param events The events of the specification.
     */
    void genCodeCifSvg(CodeContext ctxt, String cifSpecFileDir, List<IoDecl> svgDecls, List<Event> events) {
        // Get event to index mapping.
        Map<Event, Integer> eventMap = mapc(events.size());
        for (int i = 0; i < events.size(); i++) {
            eventMap.put(events.get(i), i);
        }

        // Create mappings between absolute and relative SVG paths.
        Map<String, String> svgPathsAbsToNormRel = map();
        Map<String, String> svgPathsRelToAbs = map();
        for (IoDecl decl: svgDecls) {
            // Get SVG file declaration.
            SvgFile file = null;
            if (decl instanceof SvgCopy svgCopy) {
                file = svgCopy.getSvgFile();
            } else if (decl instanceof SvgMove svgMove) {
                file = svgMove.getSvgFile();
            } else if (decl instanceof SvgIn svgIn) {
                file = svgIn.getSvgFile();
            } else if (decl instanceof SvgOut svgOut) {
                file = svgOut.getSvgFile();
            } else {
                throw new AssertionError("Unexpected CIF/SVG declaration: " + decl);
            }

            // Add paths to the path mappings, overriding entries in case of multiple CIF/SVG declarations for the same
            // SVG file.
            String svgRelPath = file.getPath();
            String svgAbsPath = Paths.resolve(svgRelPath, cifSpecFileDir);
            String svgNormRelPath = Paths.getRelativePath(svgAbsPath, cifSpecFileDir);
            svgPathsAbsToNormRel.put(svgAbsPath, svgNormRelPath);
            svgPathsRelToAbs.put(svgRelPath, svgAbsPath);
        }

        // Initialize some data structures to use during code generation:
        // - svgAbsPathsToWrapperElemIds: Per absolute path of an SVG image, the id of its wrapper element.
        // - svgOutElemQueriesToFields: Per SVG output element selector query, the field that holds the element.
        // - nextSvgInId: The next unique id for an SVG input mapping.
        Map<String, String> svgAbsPathsToWrapperElemIds = map();
        Map<String, String> svgOutElemQueriesToFields = map();
        AtomicInteger nextSvgInId = new AtomicInteger();

        // Generate code per SVG image.
        int svgImageNumber = 0;
        int declsProcessed = 0;
        for (Entry<String, String> entry: svgPathsAbsToNormRel.entrySet()) {
            // Get the paths of this SVG image.
            String svgAbsPath = entry.getKey();
            String svgNormRelPath = entry.getValue();

            // Get the SVG image number. Number from one onwards.
            svgImageNumber++;

            // Load the SVG file.
            Document document = SvgUtils.loadSvgFile(svgAbsPath);

            // Convert the SVG file to suitable text that can be embedded in an HTML file.
            TransformerFactory transFactory = TransformerFactory.newInstance();
            StringWriter svgContentWriter = new StringWriter();
            try {
                Transformer transformer = transFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.transform(new DOMSource(document.getDocumentElement()), new StreamResult(svgContentWriter));
            } catch (TransformerException e) {
                throw new RuntimeException("Failed to transform SVG document to text: " + svgNormRelPath, e);
            }
            String svgContent = svgContentWriter.toString();

            // Add a 'div' in the HTML that wraps the SVG image content. The 'div' gets a unique id for this SVG image.
            // Store the wrapper id for later access.
            String svgWrapperElementId = fmt("cif-svg-wrapper-%d", svgImageNumber);
            svgAbsPathsToWrapperElemIds.put(svgAbsPath, svgWrapperElementId);
            codeSvgContent.add("<div id=\"%s\" class=\"svg-visible\">%s</div>", svgWrapperElementId, svgContent);

            // If there are multiple SVG images, add a checkbox for each of them to toggle their visibility.
            if (svgPathsAbsToNormRel.size() > 1) {
                codeSvgToggles.add("<input type=\"checkbox\" onclick=\"toggleSVG('%s');\" checked>Image %d</input>",
                        svgWrapperElementId, svgImageNumber);
            }

            // Get CIF/SVG declarations for this SVG image.
            CifSvgDecls imgSvgDecls = filterAndGroup(svgDecls, svgAbsPath, svgPathsRelToAbs);
            declsProcessed += imgSvgDecls.size();

            // Warn about SVG files without input/output mappings. We only check for mappings, as they can result in
            // changes to the image during simulation.
            if (imgSvgDecls.svgOuts.isEmpty() && imgSvgDecls.svgIns.isEmpty()) {
                warn("SVG file \"%s\" has no CIF/SVG input/output mappings that apply to it.", svgNormRelPath);
            }

            // Generate code for SVG copy declarations.
            for (SvgCopy svgCopy: imgSvgDecls.svgCopies) {
                String svgWrapElemId = svgAbsPathsToWrapperElemIds
                        .get(svgPathsRelToAbs.get(svgCopy.getSvgFile().getPath()));
                gencodeSvgCopy(svgCopy, svgWrapElemId, ctxt);
            }

            // Generate code for SVG move declarations.
            for (SvgMove svgMove: imgSvgDecls.svgMoves) {
                String svgWrapElemId = svgAbsPathsToWrapperElemIds
                        .get(svgPathsRelToAbs.get(svgMove.getSvgFile().getPath()));
                gencodeSvgMove(svgMove, svgWrapElemId, ctxt);
            }

            // Generate code for SVG input mappings.
            for (SvgIn svgIn: imgSvgDecls.svgIns) {
                String svgWrapElemId = svgAbsPathsToWrapperElemIds
                        .get(svgPathsRelToAbs.get(svgIn.getSvgFile().getPath()));
                gencodeSvgIn(svgIn, svgWrapElemId, nextSvgInId, eventMap, ctxt);
            }

            // Generate code for SVG output mappings.
            for (SvgOut svgOut: imgSvgDecls.svgOuts) {
                String svgWrapElemId = svgAbsPathsToWrapperElemIds
                        .get(svgPathsRelToAbs.get(svgOut.getSvgFile().getPath()));
                gencodeSvgOut(svgOut, svgWrapElemId, svgOutElemQueriesToFields, ctxt);
            }
        }

        // Make sure all CIF/SVG declarations were processed.
        Assert.areEqual(declsProcessed, svgDecls.size());
    }

    /**
     * Generate code for an SVG copy declaration.
     *
     * @param svgCopy The SVG copy declaration.
     * @param svgWrapElemId The id of the SVG image wrapper element.
     * @param ctxt The code context to use.
     */
    private void gencodeSvgCopy(SvgCopy svgCopy, String svgWrapElemId, CodeContext ctxt) {
        String copyId = evalSvgStringExpr(svgCopy.getId());
        String pre = (svgCopy.getPre() == null) ? "" : evalSvgStringExpr(svgCopy.getPre());
        String post = (svgCopy.getPost() == null) ? "" : evalSvgStringExpr(svgCopy.getPost());
        codeCopyApply.add("applySvgCopy(%s, %s, %s, %s);", Strings.stringToJava(svgWrapElemId),
                Strings.stringToJava(copyId), Strings.stringToJava(pre), Strings.stringToJava(post));
    }

    /**
     * Generate code for an SVG move declaration.
     *
     * @param svgMove The SVG move declaration.
     * @param svgWrapElemId The id of the SVG image wrapper element.
     * @param ctxt The code context to use.
     */
    private void gencodeSvgMove(SvgMove svgMove, String svgWrapElemId, CodeContext ctxt) {
        String moveId = evalSvgStringExpr(svgMove.getId());
        double x = evalSvgNumberExpr(svgMove.getX());
        double y = evalSvgNumberExpr(svgMove.getY());
        codeMoveApply.add("applySvgMove(%s, %s, %s, %s);", Strings.stringToJava(svgWrapElemId),
                Strings.stringToJava(moveId), x, y);
    }

    /**
     * Generate code for an SVG output mapping.
     *
     * @param svgOut The SVG output mapping.
     * @param svgWrapElemId The id of the SVG image wrapper element.
     * @param svgOutElemQueriesToFields Per SVG output element selector query, the field that holds the element. Is
     *     extended in-place.
     * @param ctxt The code context to use.
     */
    private void gencodeSvgOut(SvgOut svgOut, String svgWrapElemId, Map<String, String> svgOutElemQueriesToFields,
            CodeContext ctxt)
    {
        // Generate code for selecting the SVG element in the HTML document and assigning it to a field:
        // - The query selector is prefixed with the wrapper id of the SVG image, to ensure that identically named
        // elements from different images are properly selected.
        // - We use a mapping to generate only one field per SVG element.
        String outId = evalSvgStringExpr(svgOut.getId());
        String querySelector = fmt("#%s #%s", svgWrapElemId, outId);
        String fieldName = svgOutElemQueriesToFields.get(querySelector);
        if (fieldName == null) {
            fieldName = fmt("outElem%d", svgOutElemQueriesToFields.size());
            codeOutDeclarations.add(fmt("%s;", fieldName));
            codeOutAssignments.add("%s.%s = document.querySelector(%s);", ctxt.getPrefix(), fieldName,
                    Strings.stringToJava(querySelector));
            svgOutElemQueriesToFields.put(querySelector, fieldName);
        }

        // Generate code for evaluation of the value.
        ExprCode exprCode = ctxt.exprToTarget(svgOut.getValue(), null);
        codeOutApply.add("var value = %s;", exprCode.getData());

        // Generate code for the update of the SVG image.
        if (svgOut.getAttr() != null) {
            // Non-text mapping.
            switch (svgOut.getAttr()) {
                // Apply as CSS style property.
                // TODO: This list of CSS style properties is incomplete.
                case "fill":
                case "visibility":
                case "opacity":
                    codeOutApply.add("%s.%s.style.%s = value;", ctxt.getPrefix(), fieldName, svgOut.getAttr());
                    break;

                // Apply as SVG presentation attribute.
                default:
                    codeOutApply.add("%s.%s.setAttribute(%s, value);", ctxt.getPrefix(), fieldName,
                            Strings.stringToJava(svgOut.getAttr()));
                    break;
            }
        } else {
            // Text mapping.
            codeOutApply.add("if (%s.%s.hasChildNodes()) {", ctxt.getPrefix(), fieldName);
            codeOutApply.indent();
            codeOutApply.add("%s.%s.childNodes[0].textContent = value;", ctxt.getPrefix(), fieldName);
            codeOutApply.dedent();
            codeOutApply.add("} else {");
            codeOutApply.indent();
            codeOutApply.add("%s.%s.textContent = value;", ctxt.getPrefix(), fieldName);
            codeOutApply.dedent();
            codeOutApply.add("}");
        }
    }

    /**
     * Generate code for an SVG input mapping.
     *
     * @param svgIn The SVG input mapping.
     * @param svgWrapElemId The id of the SVG image wrapper element.
     * @param nextSvgInId The next unique id for an SVG input mapping. Is modified in-place.
     * @param eventMap Per CIF event, its corresponding index.
     * @param ctxt The code context to use.
     */
    private void gencodeSvgIn(SvgIn svgIn, String svgWrapElemId, AtomicInteger nextSvgInId,
            Map<Event, Integer> eventMap, CodeContext ctxt)
    {
        // Get unique id for the SVG input mapping.
        int uniqueId = nextSvgInId.getAndIncrement();

        // Get the query selector to select the interactive element. The query selector is prefixed with the wrapper id
        // of the SVG image, to ensure that identically named elements from different images are properly selected.
        String inId = evalSvgStringExpr(svgIn.getId());
        String querySelector = fmt("#%s #%s", svgWrapElemId, inId);

        // Get the name of the JavaScript click event handler function to generate for the SVG input mapping.
        String clickEventHandlerName = fmt("cif_svgin_%d_Click", uniqueId);

        // Get the name of the JavaScript event setter function to generate for the SVG input mapping.
        String eventSetterName = fmt("cif_svgin_%d_EventSetter", uniqueId);

        // Add code to set up the click event listener, and make the element behave as a link by changing to a pointer
        // cursor.
        codeInSetup.add("var elem = document.querySelector(%s);", Strings.stringToJava(querySelector));
        codeInSetup.add("elem.addEventListener(\"click\", %s.%s);", ctxt.getPrefix(), clickEventHandlerName);
        codeInSetup.add("elem.style.cursor = \"pointer\";");

        // Add CSS styling to ensure that if an interactive SVG element is hovered, that the element itself and its
        // direct children get highlighted, by giving them a red stroke.
        codeInCss.add("%s:hover { stroke-width: 1 !important; stroke: rgb(255, 0, 0) !important; }", querySelector);
        codeInCss.add("%s:hover > * { stroke-width: 1 !important; stroke: rgb(255, 0, 0) !important; }", querySelector);

        // Add the click event handler function. The body is executed conditionally, such that clicking while the
        // execution is paused has no effect.
        codeInClickHandlers.add();
        codeInClickHandlers.add("%s() { // %s", clickEventHandlerName, inId);
        codeInClickHandlers.indent();
        codeInClickHandlers.add("if (%s.playing) {", ctxt.getPrefix());
        codeInClickHandlers.indent();
        codeInClickHandlers.add("%s.svgInQueue.push(%s.%s);", ctxt.getPrefix(), ctxt.getPrefix(), eventSetterName);
        codeInClickHandlers.dedent();
        codeInClickHandlers.add("}");
        codeInClickHandlers.dedent();
        codeInClickHandlers.add("}");

        // Add the event setter function.
        codeInEventSetters.add();
        codeInEventSetters.add("%s() { // %s", eventSetterName, inId);
        codeInEventSetters.indent();
        codeInEventSetters.add("%s.svgInId = '%s';", ctxt.getPrefix(), inId);

        SvgInEvent event = svgIn.getEvent();
        if (event != null) {
            if (event instanceof SvgInEventSingle) {
                // Single event. Get the event index and its absolute name.
                SvgInEventSingle singleEvt = (SvgInEventSingle)event;
                Event evt = ((EventExpression)singleEvt.getEvent()).getEvent();
                int eventIdx = eventMap.get(evt);
                String absEventName = getAbsName(evt);

                // Set the current SVG input event to allow.
                codeInEventSetters.add("%s.svgInEvent = %d; // %s", ctxt.getPrefix(), eventIdx, absEventName);
            } else if (event instanceof SvgInEventIf) {
                // 'if/then/else' event mapping.
                SvgInEventIf ifEvent = (SvgInEventIf)event;

                // Generate code for entries.
                for (SvgInEventIfEntry entry: ifEvent.getEntries()) {
                    // Get the event index and its absolute name.
                    Event evt = ((EventExpression)entry.getEvent()).getEvent();
                    int eventIdx = eventMap.get(evt);
                    String absEventName = getAbsName(evt);

                    // Add code for the entry, setting the current SVG input event to allow.
                    if (entry.getGuard() == null) {
                        codeInEventSetters.add("else {");
                        codeInEventSetters.indent();
                        codeInEventSetters.add("%s.svgInEvent = %d; // %s", ctxt.getPrefix(), eventIdx, absEventName);
                        codeInEventSetters.dedent();
                        codeInEventSetters.add("}");
                    } else {
                        codeInEventSetters.add("%s (%s) {", entry == ifEvent.getEntries().get(0) ? "if" : "else if",
                                ctxt.exprToTarget(entry.getGuard(), null).getData());
                        codeInEventSetters.indent();
                        codeInEventSetters.add("%s.svgInEvent = %d; // %s", ctxt.getPrefix(), eventIdx, absEventName);
                        codeInEventSetters.dedent();
                        codeInEventSetters.add("}");
                    }
                }
            } else {
                throw new RuntimeException("Unknown SVG input mapping event: " + event);
            }
        }

        codeInEventSetters.dedent();
        codeInEventSetters.add("}");

        // Add the updates.
        List<Update> updates = svgIn.getUpdates();
        if (!updates.isEmpty()) {
            // TODO: Generate code for updates.
        }
    }

    /**
     * Gets the indices of the interactive events, the events coupled to SVG input mappings.
     *
     * @param svgIns The SVG input mappings.
     * @param events The events of the specification.
     * @return The indices of the interactive events.
     */
    static Set<Integer> getInteractiveEventIndices(List<SvgIn> svgIns, List<Event> events) {
        // Get event to index mapping.
        Map<Event, Integer> eventMap = mapc(events.size());
        for (int i = 0; i < events.size(); i++) {
            eventMap.put(events.get(i), i);
        }

        // Get the indices of the interactive events.
        Set<Integer> interactiveEventIndices = set();
        for (SvgIn svgIn: svgIns) {
            SvgInEvent event = svgIn.getEvent();
            if (event == null) {
                // Input mapping without event.
            } else if (event instanceof SvgInEventSingle) {
                // Single event.
                SvgInEventSingle singleEvt = (SvgInEventSingle)event;
                Event evt = ((EventExpression)singleEvt.getEvent()).getEvent();
                int eventIdx = eventMap.get(evt);
                interactiveEventIndices.add(eventIdx);
            } else if (event instanceof SvgInEventIf) {
                // 'if/then/else' event mapping.
                SvgInEventIf ifEvent = (SvgInEventIf)event;
                for (SvgInEventIfEntry entry: ifEvent.getEntries()) {
                    Event evt = ((EventExpression)entry.getEvent()).getEvent();
                    int eventIdx = eventMap.get(evt);
                    interactiveEventIndices.add(eventIdx);
                }
            } else {
                throw new RuntimeException("Unknown SVG input mapping event: " + event);
            }
        }

        return interactiveEventIndices;
    }
}
