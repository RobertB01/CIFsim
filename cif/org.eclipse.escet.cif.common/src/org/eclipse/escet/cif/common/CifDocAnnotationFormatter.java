//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;

/**
 * Formatter helper class for {@code @doc} annotations.
 *
 * <p>
 * Often, the text of {@code @doc} annotations needs to be formatted to fit in a larger documentation context. This
 * class gives assistance in performing that task.
 * </p>
 * <p>
 * The class supports adding general header and footer lines, as well as separator lines before and after the text lines
 * of each {@code @doc} annotation. Finally, each line of the {@code @doc} annotation can be prefixed by text.
 * </p>
 * <p>
 * As an example, assume that the parameters of this class are set to:
 * <table>
 * <tr>
 * <td>headerLines:</td>
 * <td>{@code list("HEAD")}</td>
 * </tr>
 * <tr>
 * <td>preDocLines:</td>
 * <td>{@code list("PRE")}</td>
 * </tr>
 * <tr>
 * <td>docLinePrefix:</td>
 * <td>{@code "# "}</td>
 * </tr>
 * <tr>
 * <td>postDocLines:</td>
 * <td>{@code list("POST")}</td>
 * </tr>
 * <tr>
 * <td>footerLines:</td>
 * <td>{@code list("FOOT")}</td>
 * </tr>
 * </table>
 * <ul>
 * <li>Formatted output for an object without {@code @doc} annotations then produces <pre> HEAD
 * FOOT</pre></li>
 * <li>Formatted output for an object with {@code @doc("abc\ndef") @doc("xyz")} annotations then produces <pre> HEAD
 * PRE
 * # abc
 * # def
 * POST
 * PRE
 * # xyz
 * POST
 * FOOT</pre></li>
 * </ul>
 * </p>
 */
public class CifDocAnnotationFormatter {
    /** Lines above all doc annotation text of an object. */
    private final List<String> headerLines;

    /** Lines above the text of a single {@code @doc} annotation. */
    private final List<String> preDocLines;

    /** Prefix to insert before a line from an {@code @doc} annotation. */
    private final String docLinePrefix;

    /** Lines below the text of a single {@code @doc} annotation. */
    private final List<String> postDocLines;

    /** Lines below all doc annotation text of an object. */
    private final List<String> footerLines;

    /**
     * Constructor of the {@link CifDocAnnotationFormatter} class.
     *
     * @param headerLines Lines above all the formatted doc annotation text of an object. Value {@code null} can be used
     *     for not having header lines.
     * @param preDocLines Lines above the formatted text of a single {@code @doc} annotation. Value {@code null} can be
     *     used for not having pre-doc lines.
     * @param docLinePrefix Prefix to insert before a formatted line from an {@code @doc} annotation. Value {@code null}
     *     can be used for not having a doc prefix.
     * @param postDocLines Lines below the formatted text of a single {@code @doc} annotation. Value {@code null} can be
     *     used for not having post-doc lines.
     * @param footerLines Lines below all the formatted doc annotation text of an object. Value {@code null} can be used
     *     for not having footer lines.
     */
    public CifDocAnnotationFormatter(List<String> headerLines, List<String> preDocLines,
            String docLinePrefix, List<String> postDocLines, List<String> footerLines)
    {
        this.headerLines = (headerLines == null) ? List.of() : headerLines;
        this.preDocLines = (preDocLines == null) ? List.of() : preDocLines;
        this.docLinePrefix = (docLinePrefix == null) ? "" : docLinePrefix;
        this.postDocLines = (postDocLines == null) ? List.of() : postDocLines;
        this.footerLines = (footerLines == null) ? List.of() : footerLines;
    }

    /**
     * Test whether the given CIF object has {@code @doc} annotations.
     *
     * @param obj CIF object to test for having {@code @doc} annotations.
     * @return Whether documentation text was found.
     */
    public boolean hasDocs(AnnotatedObject obj) {
        return !CifDocAnnotationUtils.getDocs(obj).isEmpty();
    }

    /**
     * Perform formatting of the given {@code @doc} annotation texts.
     *
     * @param docBlocks {@code @doc} documentation lines to format.
     * @return The formatted text.
     */
    public List<String> formatDocs(List<String> docBlocks) {
        List<String> lines = list();
        lines.addAll(headerLines);
        for (String docBlock: docBlocks) {
            lines.addAll(preDocLines);
            for (String line: docBlock.split("\\r?\\n")) {
                lines.add(docLinePrefix + line);
            }
            lines.addAll(postDocLines);
        }
        lines.addAll(footerLines);

        return lines;
    }

    /**
     * Retrieve the {@code @doc} annotations of the provided object, perform formatting of it and return the result.
     *
     * @param obj CIF object to query for its {@code @doc} annotations.
     * @return The formatted text.
     */
    public List<String> formatDocs(AnnotatedObject obj) {
        return formatDocs(CifDocAnnotationUtils.getDocs(obj));
    }
}
