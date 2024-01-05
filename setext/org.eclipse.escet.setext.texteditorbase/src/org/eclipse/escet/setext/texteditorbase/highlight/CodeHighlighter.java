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

package org.eclipse.escet.setext.texteditorbase.highlight;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Iterator;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.texteditorbase.ColorManager;
import org.eclipse.escet.setext.texteditorbase.scanners.GenericPartitionScanner;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TypedRegion;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

/**
 * Code highlighter for SeText based languages. Uses code from the Eclipse text editor for a specific language, to
 * highlight code for that language.
 *
 * <p>
 * This class is not used by the text editors themselves. That is, this class is used to highlight code in-memory, and
 * uses functionality provided by the text editor framework for consistent highlighting and reuse of the highlighting
 * code.
 * </p>
 */
public abstract class CodeHighlighter implements AutoCloseable {
    /** Whether to print debug output. */
    private static final boolean DEBUG = false;

    /** The color manager to use, or {@code null} if not available. */
    protected ColorManager colorManager;

    /** The presentation reconciler to use, or {@code null} if not yet available. */
    private IPresentationReconciler presentationReconciler;

    /** The partition scanner to use, or {@code null} if not yet available. */
    private GenericPartitionScanner partitionScanner;

    /**
     * Returns the presentation reconciler to use.
     *
     * @return The presentation reconciler to use.
     */
    protected abstract IPresentationReconciler obtainPresentationReconciler();

    /**
     * Returns the partition scanner to use.
     *
     * @return The partition scanner to use.
     */
    protected abstract GenericPartitionScanner obtainPartitionScanner();

    /** Constructor for the {@link CodeHighlighter} class. */
    protected CodeHighlighter() {
        colorManager = new ColorManager();
    }

    /**
     * Converts code in a specific language to HTML code. The code in the specific language is highlighted (or styled)
     * and the highlighted code is represented as HTML code.
     *
     * @param code The code to highlight.
     * @return The HTML code.
     */
    public String toHtml(String code) {
        // Get document.
        IDocument doc = new ReadOnlyDocument(code);

        // Get presentation reconciler.
        if (presentationReconciler == null) {
            presentationReconciler = obtainPresentationReconciler();
        }

        // Get partition scanner.
        if (partitionScanner == null) {
            partitionScanner = obtainPartitionScanner();
        }
        partitionScanner.setRange(doc, 0, code.length());

        // Initialize presentation.
        TextPresentation presentation = new TextPresentation();

        // Initialize HTML code.
        StringBuilder html = new StringBuilder();
        html.append("<html>");

        // Process CIF code.
        String curContentType = null;
        int curOffset = 0;
        int curLength = 0;
        while (true) {
            // Get next token.
            IToken token = partitionScanner.nextToken();
            int offset = partitionScanner.getTokenOffset();
            int length = partitionScanner.getTokenLength();

            // Debug the token.
            if (DEBUG) {
                String txt = "TOKEN: " + token;
                txt += " data=" + token.getData();
                txt += " offset=" + offset;
                txt += " length=" + length;
                if (token.isEOF()) {
                    txt += " [EOF]";
                }
                if (token.isUndefined()) {
                    txt += " [UNDEFINED]";
                }
                if (token.isWhitespace()) {
                    txt += " [WHITESPACE]";
                }
                if (token.isOther()) {
                    txt += " [OTHER]";
                }
                System.out.println(txt);
            }

            // Stop at end-of-file.
            if (token.isEOF()) {
                break;
            }

            // We expect a content/partition type token.
            Assert.check(!token.isUndefined());
            Assert.check(!token.isWhitespace());
            Assert.check(token.isOther());

            // Get content/partition type.
            Object data = token.getData();
            String contentType = (String)data;
            if (contentType == null) {
                contentType = IDocument.DEFAULT_CONTENT_TYPE;
            }

            // Process region.
            if (contentType.equals(curContentType)) {
                // More content of the same content type. Merge new region with
                // current region.
                curLength += length;
            } else {
                // Update presentation for current region, if available.
                if (curContentType != null) {
                    IPresentationRepairer repairer = presentationReconciler.getRepairer(curContentType);
                    repairer.setDocument(doc);
                    ITypedRegion region = new TypedRegion(curOffset, curLength, curContentType);
                    repairer.createPresentation(presentation, region);
                }

                // Start a new region.
                curContentType = contentType;
                curOffset = offset;
                curLength = length;
            }
        }

        // Process last region, if available.
        if (curContentType != null) {
            IPresentationRepairer repairer = presentationReconciler.getRepairer(curContentType);
            repairer.setDocument(doc);
            ITypedRegion region = new TypedRegion(curOffset, curLength, curContentType);
            repairer.createPresentation(presentation, region);
        }

        // Merge presentation ranges with same style.
        presentation = mergeRanges(presentation);

        // Add styled text regions to the HTML code.
        Iterator<StyleRange> iter = presentation.getAllStyleRangeIterator();
        while (iter.hasNext()) {
            // Get next style range.
            StyleRange range = iter.next();
            if (DEBUG) {
                System.out.println("RANGE: " + range);
            }

            // Query supported styles.
            boolean italic = (range.fontStyle & SWT.ITALIC) > 0;
            boolean bold = (range.fontStyle & SWT.BOLD) > 0;
            Color color = range.foreground;
            boolean styled = italic || bold || (color != null);

            // Add 'span' for style, if applicable.
            if (styled) {
                html.append("<span style=\"");
                if (italic) {
                    html.append("text-style:italic;");
                }
                if (bold) {
                    html.append("text-weight:bold;");
                }
                if (color != null) {
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    String hex = fmt("%02x%02x%02x", red, green, blue);
                    html.append(fmt("color:#%s;", hex));
                }
                html.append("\">");
            }

            // Add code for the style range, as HTML code.
            int start = range.start;
            int end = range.start + range.length;
            String rangeCode = code.substring(start, end);
            rangeCode = StringEscapeUtils.escapeHtml4(rangeCode);
            rangeCode = rangeCode.replace(" ", "&nbsp;");
            rangeCode = rangeCode.replace("\t", "&nbsp;");
            rangeCode = rangeCode.replace("\r", "");
            rangeCode = rangeCode.replace("\n", "<br>");
            html.append(rangeCode);

            // Add end of 'span', if applicable.
            if (styled) {
                html.append("</span>");
            }
        }

        // Return the complete HTML code.
        html.append("</html>");
        return html.toString();
    }

    /**
     * Merges adjacent styled ranges with the same style together to a single styled range.
     *
     * @param presentation The text presentation with styled ranges.
     * @return A new text presentation with merged styled ranges.
     */
    private static TextPresentation mergeRanges(TextPresentation presentation) {
        TextPresentation result = new TextPresentation();
        Iterator<StyleRange> iter = presentation.getAllStyleRangeIterator();
        StyleRange current = null;
        while (iter.hasNext()) {
            StyleRange range = iter.next();
            if (current != null && range.similarTo(current)) {
                // Merge ranges with same style.

                // Make sure the ranges to merge are adjacent.
                int afterPrevIdx = current.start + current.length;
                int startRangeIdx = range.start;
                Assert.check(afterPrevIdx == startRangeIdx);

                // Merge ranges.
                current.length += range.length;
            } else {
                // First range, or range with different style.
                result.addStyleRange(range);
                current = range;
            }
        }
        return result;
    }

    @Override
    public void close() {
        if (colorManager != null) {
            colorManager = null;
        }
    }
}
