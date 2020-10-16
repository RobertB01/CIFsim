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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** Functions for manipulating XML DOM trees. */
public class XmlUtils {
    /** Constructor of the {@link XmlUtils} class. */
    private XmlUtils() {
        // Static class.
    }

    /**
     * Get the single direct element child from the parent with the given tag.
     *
     * @param parent Parent node to search.
     * @param tagName Name of the tag to look for.
     * @return Found child element node.
     */
    public static Element getSingleElementByName(Element parent, String tagName) {
        Element result = null;

        Node n = parent.getFirstChild();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(tagName)) {
                if (result == null) {
                    result = (Element)n;
                } else {
                    String msg = fmt("Expected single node '%s', but found more.", tagName);
                    throw new AssertionError(msg);
                }
            }
            n = n.getNextSibling();
        }
        if (result == null) {
            String msg = fmt("Expected one element named '%s', but didn't find any.", tagName);
            throw new AssertionError(msg);
        }
        return result;
    }

    /**
     * Get direct child element nodes with a given tag name.
     *
     * @param parent Parent element node to search.
     * @param tagName Tag name to look for.
     * @return Found direct child element nodes.
     */
    public static List<Element> getElementsByName(Element parent, String tagName) {
        List<Element> results = list();

        Node n = parent.getFirstChild();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(tagName)) {
                results.add((Element)n);
            }
            n = n.getNextSibling();
        }
        return results;
    }
}
