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

package org.eclipse.escet.cif.cif2yed;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.eclipse.escet.cif.cif2yed.CifToYedColors.COMP_BG_OPENED_COLOR;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.cif2yed.options.SyntaxHighlightingOption;
import org.eclipse.escet.cif.cif2yed.options.TransparentEdgeLabelsOption;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.texteditor.CifCodeHighlighter;
import org.eclipse.escet.cif.texteditor.CifTextEditorLightTheme;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * CIF to yEd transformation.
 *
 * @see <a href="http://graphml.graphdrawing.org/primer/graphml-primer.html">GraphML Primer</a>
 * @see <a href="http://www.yworks.com/xml/schema/graphml/1.1/doc/index.html">GraphML Schema Reference (yFiles for
 *     Java)</a>
 * @see <a href="http://yed.yworks.com/support/qa/16/how-can-i-achieve-different-text-stylings-within-one-label">How can
 *     I achieve different text stylings within one label?</a>
 */
public abstract class CifToYedDiagram {
    /** GraphML namespace. */
    private static final String GRAPHML_NS = "http://graphml.graphdrawing.org/xmlns";

    /** GraphML schema location. */
    private static final String GRAPHML_SCHEMA_LOC = "http://graphml.graphdrawing.org/xmlns";

    /** yWorks GraphML namespace. */
    private static final String Y_NS = "http://www.yworks.com/xml/graphml";

    /** yWorks GraphML schema location. */
    private static final String Y_SCHEMA_LOC = "http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd";

    /** Whether to apply syntax highlighting. */
    protected boolean highlight;

    /** CIF code highlighter, available if and only if {@link #highlight} is {@code true}. */
    private CifCodeHighlighter highlighter;

    /**
     * Whether to use a transparent background for edge labels ({@code true}), or a solid white background
     * ({@code false}).
     */
    private boolean useTransparentEdgeLabels;

    /** The XML document, or {@code null} if not yet or no longer available. */
    protected Document doc;

    /** The root 'graph' element, or {@code null} if not available. */
    protected Element rootGraph;

    /** Graphics context for buffered image for text width computing. */
    private Graphics graphics;

    /** Font metrics for text width computing. */
    private FontMetrics metrics;

    /**
     * Transform a CIF specification into a yEd/GraphML XML document.
     *
     * @param spec The CIF specification.
     * @param absSpecPath The absolute local file system path to the CIF file.
     * @param termination Cooperative termination query function.
     * @return The yEd/GraphML XML document.
     * @throws UnsupportedException If an unsupported feature is found.
     */
    public Document transform(Specification spec, String absSpecPath, Termination termination) {
        // Options.
        Assert.check(highlighter == null);
        highlight = SyntaxHighlightingOption.applyHighlighting();
        if (highlight) {
            AppEnv.checkGuiAvailable("apply syntax highlighting in the CIF to yEd transformer");
            highlighter = new CifCodeHighlighter(new CifTextEditorLightTheme());
        }
        Assert.ifAndOnlyIf(highlight, highlighter != null);

        useTransparentEdgeLabels = TransparentEdgeLabelsOption.isEnabled();

        // Actual transformation.
        try {
            // Create document.
            doc = createYedXmlDocument();

            // Get and configure root element.
            Element root = doc.getDocumentElement();
            root.setAttribute("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
            root.setAttribute("xmlns:y", Y_NS);
            root.setAttribute("xsi:schemaLocation", GRAPHML_SCHEMA_LOC + " " + Y_SCHEMA_LOC);

            // Add 'key' elements.
            Element ngKeyElem = doc.createElement("key");
            root.appendChild(ngKeyElem);
            ngKeyElem.setAttribute("for", "node");
            ngKeyElem.setAttribute("id", "ng");
            ngKeyElem.setAttribute("yfiles.type", "nodegraphics");

            Element egKeyElem = doc.createElement("key");
            root.appendChild(egKeyElem);
            egKeyElem.setAttribute("for", "edge");
            egKeyElem.setAttribute("id", "eg");
            egKeyElem.setAttribute("yfiles.type", "edgegraphics");

            Element deKeyElem = doc.createElement("key");
            root.appendChild(deKeyElem);
            deKeyElem.setAttribute("for", "node");
            deKeyElem.setAttribute("id", "dn");
            deKeyElem.setAttribute("attr.name", "description");
            deKeyElem.setAttribute("attr.type", "string");

            // Initialize.
            rootGraph = null;

            BufferedImage img = new BufferedImage(1, 1, TYPE_INT_RGB);
            graphics = img.getGraphics();
            metrics = graphics.getFontMetrics();

            // Add content from CIF model.
            addSpec(spec, absSpecPath, root, termination);

            // Cleanup.
            graphics.dispose();

            // Return yEd/GraphML XML document.
            return doc;
        } finally {
            // Cleanup.
            if (highlighter != null) {
                highlighter.close();
                highlighter = null;
            }
        }
    }

    /**
     * Add a CIF specification to the yEd/GraphML diagram.
     *
     * @param spec The CIF specification.
     * @param absSpecPath The absolute local file system path to the CIF file.
     * @param root The root 'graphml' XML element to which to add new elements.
     * @param termination Cooperative termination query function.
     * @throws UnsupportedException If an unsupported feature is found.
     */
    protected abstract void addSpec(Specification spec, String absSpecPath, Element root, Termination termination);

    /**
     * Constructs a new yEd/GraphML XML document.
     *
     * @return A new yEd/GraphML XML document.
     */
    private Document createYedXmlDocument() {
        // Create document builder.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        // Create and return document.
        DOMImplementation domImpl = builder.getDOMImplementation();
        Document doc = domImpl.createDocument(GRAPHML_NS, "graphml", null);
        doc.setXmlVersion("1.0");
        doc.setXmlStandalone(false);
        return doc;
    }

    /**
     * Adds a background color to the edge label, if applicable. If a transparent edge label background is requested,
     * does nothing.
     *
     * @param label The label element to which to add the background color.
     */
    protected void addEdgeLabelBackground(Element label) {
        if (useTransparentEdgeLabels) {
            return;
        }
        label.setAttribute("backgroundColor", COMP_BG_OPENED_COLOR.color);
    }

    /**
     * Converts CIF code to syntax highlighted HTML code. If the {@link SyntaxHighlightingOption} is disabled, does not
     * perform any highlighting, and returns the code as plain text (as provided).
     *
     * @param code The CIF code to highlight.
     * @return The HTML code.
     */
    protected String highlight(String code) {
        if (!highlight) {
            return code;
        }
        return highlighter.toHtml(code);
    }

    /**
     * Guess the width in pixels of rendered text. Uses a minimum width of 30.
     *
     * @param text The text.
     * @param margin The margin in pixels, to use on either side.
     * @return The width in pixels.
     */
    protected double guessTextWidth(String text, int margin) {
        return guessTextSize(text, margin).getWidth();
    }

    /**
     * Guess the size in pixels of the (the bounding rectangle) of rendered text. Uses a minimum width and height of 30.
     *
     * @param text The text.
     * @param margin The margin in pixels, to use on all sides.
     * @return The bounds in pixels.
     */
    protected Rectangle2D guessTextSize(String text, int margin) {
        Rectangle2D.Double size = new Rectangle2D.Double();
        String[] lines = StringUtils.splitPreserveAllTokens(text, '\n');
        for (String line: lines) {
            Rectangle2D rect = metrics.getStringBounds(line, graphics);
            size.width = Math.max(size.width, rect.getWidth());
            size.height += rect.getHeight() + 1;
        }
        size.width += margin * 2;
        size.height += margin * 2;
        if (size.width < 30) {
            size.width = 30;
        }
        if (size.height < 30) {
            size.height = 30;
        }
        return size;
    }
}
