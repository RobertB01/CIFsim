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

package org.eclipse.escet.common.svg;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.eclipse.escet.common.java.Assert;

/** Scalable Vector Graphics (SVG) name related utility methods. */
public class SvgNameUtils {
    /** XML 'NameStartChar' regular expression. See {@link #isValidSvgName}. */
    private static final String NAME_START_CHAR_RE = "[:A-Z_a-z]";

    /** XML 'NameChar' regular expression. See {@link #isValidSvgName}. */
    private static final String NAME_CHAR_RE = "[:A-Z_a-z\\-.0-9]";

    /** XML 'Name' regular expression. See {@link #isValidSvgName}. */
    private static final Pattern NAME_PATTERN = Pattern.compile(fmt("%s(%s)*", NAME_START_CHAR_RE, NAME_CHAR_RE));

    /** Constructor for the {@link SvgNameUtils} class. */
    private SvgNameUtils() {
        // Static class.
    }

    /**
     * Mapping from SVG element names to the attributes that are allowed to be defined on them. Both SVG presentation
     * attributes and CSS style attributes are included.
     *
     * <p>
     * This information can be used to prevent accidentally setting attributes on the wrong elements, setting attributes
     * that have no effect on that particular element, typos in attribute names, etc.
     * </p>
     *
     * <p>
     * This information is taken from the SVG specification, 'SVG 1.1 (Second Edition) - 16 August 2011', appendices L,
     * M, and N.
     * </p>
     *
     * <p>
     * The SVG 1.1 standard <strong>allows</strong> all CSS attributes/properties on a lot of elements. It does specify
     * that a lot of them only have <strong>effect</strong> on certain elements. That information we use here. However,
     * according to the standard, the 'visibility' property has no effect on 'g' elements. We do use that. It works in
     * Batik and Firefox (and probably other libraries as well). So, we allow 'visibility' on all elements that also
     * support 'display'.
     * </p>
     *
     * <p>
     * For SVG 1.2, this will need to be updated, see:
     * <ul>
     * <li><a href="http://www.w3.org/TR/SVG12/">http://www.w3.org/TR/SVG12/</a></li>
     * <li><a href=
     * "http://www.w3.org/TR/SVGTiny12/elementTable.html">http://www.w3.org/TR/SVGTiny12/elementTable.html</a></li>
     * <li><a href=
     * "http://www.w3.org/TR/SVGTiny12/attributeTable.html">http://www.w3.org/TR/SVGTiny12/attributeTable.html</a></li>
     * <li><a href=
     * "http://www.w3.org/TR/SVGMobile12/attributeTable.html">http://www.w3.org/TR/SVGMobile12/attributeTable.html</a></li>
     * <li><a href=
     * "http://www.w3.org/Graphics/SVG/1.2/rng/Full-1.2/">http://www.w3.org/Graphics/SVG/1.2/rng/Full-1.2/</a></li>
     * <li><a href="http://www.w3.org/TR/2004/WD-SVG12-20041027/">http://www.w3.org/TR/2004/WD-SVG12-20041027/</a></li>
     * <li><a href=
     * "http://dev.w3.org/SVG/modules/integration/master/attribute_table.html">http://dev.w3.org/SVG/modules/integration/master/attribute_table.html</a></li>
     * </ul>
     * </p>
     *
     * @see <a href="http://www.w3.org/TR/SVG11/eltindex.html">Appendix L: Element Index</a>
     * @see <a href="http://www.w3.org/TR/SVG11/attindex.html">Appendix M: Attribute Index</a>
     * @see <a href="http://www.w3.org/TR/SVG11/propidx.html">Appendix N: Property Index</a>
     * @see #getDefinedAttrs
     */
    private static final Map<String, Set<String>> SVG_ELEM_ATTR_MAP;

    static {
        // Read data from file.
        ClassLoader loader = SvgNameUtils.class.getClassLoader();
        String resName = SvgNameUtils.class.getPackageName().replace(".", "/") + "/svg_1_1_elem_attr_data.txt";
        List<String> lines;
        try (InputStream stream = new BufferedInputStream(loader.getResourceAsStream(resName))) {
            lines = IOUtils.readLines(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SVG 1.1 element/attribute mapping data.", e);
        }

        // Process data from file.
        Pattern svgElemAttrNameSyntaxPattern = Pattern.compile("[a-zA-Z0-9\\-:]+");
        Map<String, Set<String>> mapping = map();
        for (String line: lines) {
            if (line.isBlank() || line.startsWith("#")) {
                // Skip empty/comment lines.
            } else {
                // Split element and attribute.
                int idx = line.indexOf(" / ");
                Assert.check(idx > 0);
                String elemName = line.substring(0, idx);
                String attrName = line.substring(idx + " / ".length());
                Assert.check(svgElemAttrNameSyntaxPattern.matcher(elemName).matches(), elemName);
                Assert.check(svgElemAttrNameSyntaxPattern.matcher(attrName).matches(), attrName);

                // Add to mapping.
                Set<String> attrs = mapping.get(elemName);
                if (attrs == null) {
                    attrs = set();
                    mapping.put(elemName, attrs);
                }
                attrs.add(attrName);
            }
        }

        // Store mapping in static variable.
        SVG_ELEM_ATTR_MAP = Collections.unmodifiableMap(mapping);
    }

    /**
     * Returns the attributes that are defined in the SVG 1.1 standard as being allowed for elements with the given
     * name. Both SVG presentation attributes and CSS style attributes are returned. If no element with the given name
     * is specified in the standard, {@code null} is returned.
     *
     * <p>
     * The SVG 1.1 standard <strong>allows</strong> all CSS attributes/properties on a lot of elements. It does specify
     * that a lot of them only have <strong>effect</strong> on certain elements. That information we use here. However,
     * according to the standard, the 'visibility' property has no effect on 'g' elements. We do use that. It works in
     * Batik and Firefox (and probably other libraries as well). So, we allow 'visibility' on all elements that also
     * support 'display'.
     * </p>
     *
     * @param elemName The name of the SVG element. Note that this is not the 'id' of the element, but the name (for
     *     instance: 'rect', 'g').
     * @return The allowed attributes, or {@code null}.
     */
    public static Set<String> getDefinedAttrs(String elemName) {
        return SVG_ELEM_ATTR_MAP.get(elemName);
    }

    /**
     * Checks an SVG name for validity.
     *
     * <p>
     * The SVG standard builds on the XML standard, which defines the valid syntax for names ('Name' symbol). The
     * <a href="http://www.w3.org/TR/REC-xml/#NT-Name">XML standard</a> defines the syntax of the 'Name' symbol (using
     * EBNF notation) as the following (all non-ASCII characters are omitted):
     *
     * <pre>
     *  [4]  NameStartChar ::= ":" | [A-Z] | "_" | [a-z]
     *  [4a] NameChar      ::= NameStartChar | "-" | "." | [0-9]
     *  [5]  Name          ::= NameStartChar (NameChar)*
     * </pre>
     * </p>
     *
     * @param name The name to check.
     * @return {@code true} if the name is valid, {@code false} otherwise.
     */
    public static boolean isValidSvgName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Checks an SVG name prefix for validity. Valid prefixes are: valid SVG names and empty prefixes.
     *
     * @param prefix The prefix to check.
     * @return {@code true} if the prefix is valid, {@code false} otherwise.
     */
    public static boolean isValidSvgPrefixName(String prefix) {
        return prefix.isEmpty() || isValidSvgName(prefix);
    }

    /**
     * Checks an SVG name postfix for validity. Valid postfixes are: postfixes that are valid SVG names after prefixing
     * them with {@code "_"}, as well as empty postfixes.
     *
     * @param postfix The postfix to check.
     * @return {@code true} if the postfix is valid, {@code false} otherwise.
     */
    public static boolean isValidSvgPostfixName(String postfix) {
        return postfix.isEmpty() || isValidSvgName("_" + postfix);
    }
}
