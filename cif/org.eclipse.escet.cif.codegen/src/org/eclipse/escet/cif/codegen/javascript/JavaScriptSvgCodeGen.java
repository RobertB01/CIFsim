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
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgFile;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.svg.SvgUtils;
import org.w3c.dom.Document;

/** Type code generator for the JavaScript target language. */
public class JavaScriptSvgCodeGen extends SvgCodeGen {
    /** The generated code for the SVG image content and their wrapper elements. */
    CodeBox codeSvgContent;

    /** The generated code for toggling (enabling/disabling) visibility of SVG images. */
    CodeBox codeSvgToggles;

    /** The generated code for event handlers invoked when clicking interactive SVG elements. */
    CodeBox codeInEventHandlers;

    /**
     * The generated code for interaction, to register event listeners, and make sure interactive elements have the
     * right mouse pointer.
     */
    CodeBox codeInInteract;

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
            if (decl instanceof SvgIn) {
                file = ((SvgIn)decl).getSvgFile();
            } else if (decl instanceof SvgOut) {
                file = ((SvgOut)decl).getSvgFile();
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

            // Warn about SVG files without input/output mappings. We only check for mappings, as they can result in
            // changes to the image during simulation.
            if (imgSvgDecls.svgOuts.isEmpty() && imgSvgDecls.svgIns.isEmpty()) {
                warn("SVG file \"%s\" has no CIF/SVG input/output mappings that apply to it.", svgNormRelPath);
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
    }

    /**
     * Generate code for an SVG output mapping.
     *
     * @param svgOut The SvgOut mapping object.
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
        // Get the query selector to select the interactive element. The query selector is prefixed with the wrapper id
        // of the SVG image, to ensure that identically named elements from different images are properly selected.
        String inId = evalSvgStringExpr(svgIn.getId());
        String querySelector = fmt("#%s #%s", svgWrapElemId, inId);

        // Get the name of the event handler to generate for the SVG input mapping.
        String clickEventHandlerName = fmt("cif_svgin_%d_Click", nextSvgInId.getAndIncrement());

        // Add code to set up the event listener, and make the element behave as a link by changing to a pointer cursor.
        codeInInteract.add("var elem = document.querySelector(%s);", Strings.stringToJava(querySelector));
        codeInInteract.add("elem.addEventListener(\"click\", %s.%s);", ctxt.getPrefix(), clickEventHandlerName);
        codeInInteract.add("elem.style.cursor = \"pointer\";");

        // Add CSS styling to ensure that if an interactive SVG element is hovered, that the element itself and its
        // direct children get highlighted, by giving them a red stroke.
        codeInCss.add("#%s:hover { stroke-width: 1 !important; stroke: rgb(255, 0, 0) !important; }", inId);
        codeInCss.add("#%s:hover > * { stroke-width: 1 !important; stroke: rgb(255, 0, 0) !important; }", inId);

        // Add the event handler function, starting with the header. The body is executed conditionally, such that
        // clicking while the execution is paused has no effect.
        codeInEventHandlers.add();
        codeInEventHandlers.add("%s() { // %s", clickEventHandlerName, inId);
        codeInEventHandlers.indent();
        codeInEventHandlers.add("if (%s.playing) {", ctxt.getPrefix());
        codeInEventHandlers.indent();

        // Add the guards.
        SvgInEvent event = svgIn.getEvent();
        if (event instanceof SvgInEventSingle) {
            // Single event. Get the absolute name of the event, and its 'event allowed' variable.
            SvgInEventSingle singleEvt = (SvgInEventSingle)event;
            Event evt = ((EventExpression)singleEvt.getEvent()).getEvent();
            int eventIdx = eventMap.get(evt);
            String absEventName = getAbsName(evt);
            String eventAllowedVarName = fmt("%s.event%d_Allowed", ctxt.getPrefix(), eventIdx);

            // Enable the event when it is clicked.
            codeInEventHandlers.add("%s = true; // %s", eventAllowedVarName, absEventName);
        } else if (event instanceof SvgInEventIf) {
            // 'if/then/else' event mapping.
            SvgInEventIf ifEvent = (SvgInEventIf)event;

            // Generate code for entries.
            for (SvgInEventIfEntry entry: ifEvent.getEntries()) {
                // Get the absolute name of the event, and its 'event allowed' variable.
                Event evt = ((EventExpression)entry.getEvent()).getEvent();
                int eventIdx = eventMap.get(evt);
                String absEventName = getAbsName(evt);
                String eventAllowedName = fmt("%s.event%d_Allowed", ctxt.getPrefix(), eventIdx);

                // Add code for the entry, to enable the event when it is clicked.
                if (entry.getGuard() == null) {
                    codeInEventHandlers.add("else {");
                    codeInEventHandlers.indent();
                    codeInEventHandlers.add("%s = true; // %s", eventAllowedName, absEventName);
                    codeInEventHandlers.dedent();
                    codeInEventHandlers.add("}");
                } else {
                    codeInEventHandlers.add("%s (%s) {", entry == ifEvent.getEntries().get(0) ? "if" : "else if",
                            ctxt.exprToTarget(entry.getGuard(), null).getData());
                    codeInEventHandlers.indent();
                    codeInEventHandlers.add("%s = true; // %s", eventAllowedName, absEventName);
                    codeInEventHandlers.dedent();
                    codeInEventHandlers.add("}");
                }
            }
        } else {
            throw new RuntimeException("Unknown SVG input mapping event: " + event);
        }

        // Complete the event handler function.
        codeInEventHandlers.dedent();
        codeInEventHandlers.add("}");
        codeInEventHandlers.dedent();
        codeInEventHandlers.add("}");
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
            if (event instanceof SvgInEventSingle) {
                // Single event.
                SvgInEventSingle singleEvt = (SvgInEventSingle)event;
                Event evt = ((EventExpression)singleEvt.getEvent()).getEvent();
                int eventId = eventMap.get(evt);
                interactiveEventIndices.add(eventId);
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
