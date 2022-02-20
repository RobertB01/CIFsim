//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.cif2mcrl2.tree.CombinedTextNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.ElementaryTextNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.TextNode;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;

/** Helper class for interpreting the instance tree provided by the user. */
public class InstanceTreeHelper {
    /** Constructor of the {@link InstanceTreeHelper} class. */
    private InstanceTreeHelper() {
        // Static class.
    }

    /**
     * Parse the instance tree text string, getting a tree of nodes to combine.
     *
     * @param text Text to parse.
     * @return Tree of nodes to instantiate.
     */
    public static TextNode parseTreeText(String text) {
        InstanceTreeScanner scanner = new InstanceTreeScanner(text);
        return parseTreeText(scanner, true);
    }

    /**
     * Parse the instance tree text string, getting a tree of nodes to combine.
     *
     * @param scanner Scanner of the instance tree text.
     * @param outer If set, the call parses at the outermost level (and expects EOF at the end). If unset, it is a parse
     *     at an inner level (expecting an open at the start and a close parenthesis at the end).
     * @return The parse result.
     */
    private static TextNode parseTreeText(InstanceTreeScanner scanner, boolean outer) {
        if (!outer) { // At an inner level, check for the open parenthesis.
            Assert.check(scanner.peekParOpen());
            scanner.scanNext();
        }

        // Collect nodes (elementary as well as combined).
        List<TextNode> nodes = list();
        while (true) {
            if (scanner.peekParOpen()) {
                nodes.add(parseTreeText(scanner, false));
                continue;
            }

            String name = scanner.peekName();
            if (name != null) {
                nodes.add(new ElementaryTextNode(name));
                scanner.scanNext();
                continue;
            }

            break;
        }

        TextNode n;
        switch (nodes.size()) {
            case 0:
                String msg = "Missing node name in the instance tree option text.";
                throw new InvalidInputException(msg);
            case 1:
                n = nodes.get(0);
                break;
            default:
                n = new CombinedTextNode(nodes);
                break;
        }

        if (outer) {
            if (scanner.peekEOF()) {
                return n;
            }

            Assert.check(scanner.peekParClose());
            String msg = "Unexpected ')' at top-level in the instance tree option text.";
            throw new InvalidInputException(msg);
        } else {
            if (scanner.peekParClose()) {
                scanner.scanNext();
                return n;
            }

            Assert.check(scanner.peekEOF());
            String msg = "Missing ')' in the instance tree option text.";
            throw new InvalidInputException(msg);
        }
    }
}
