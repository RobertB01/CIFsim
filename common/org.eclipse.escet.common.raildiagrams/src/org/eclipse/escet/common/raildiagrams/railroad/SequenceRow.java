//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.railroad;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.escet.common.raildiagrams.Configuration;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.solver.Variable;
import org.eclipse.escet.common.java.ReverseListIterator;

/** Row of child diagrams in a sequence. */
public class SequenceRow {
    /** Elements of the the row. */
    public final List<DiagramElement> elements;

    /** Proxies of the child elements, always in proper order for flow in the diagram. */
    private List<ProxyDiagramElement> proxies;

    /** Whether the row has a successor row. */
    public boolean hasNextRow;

    /** Left-most used position of the row elements. */
    public Variable left;

    /** Right-most used position of the row elements. */
    public Variable right;

    /** top-most used position of the row elements. */
    public Variable top;

    /** Top of the entry and exit point at the left and right sides of the row. */
    public Variable connect;

    /** Bottom-most used position of the row elements. */
    public Variable bottom;

    /**
     * Constructor of the {@link SequenceRow} class.
     *
     * @param elements
     */
    public SequenceRow(List<DiagramElement> elements) {
        this.elements = elements;
    }

    public void create(Configuration config, int direction, DiagramElement parent, String rowText) {
        Solver solver = parent.solver;

        left = solver.newVar(fmt("row-%s-left", rowText));
        right = solver.newVar(fmt("row-%s-right", rowText));
        top = solver.newVar(fmt("row-%s-top", rowText));
        connect = solver.newVar(fmt("row-%s-connect", rowText));
        bottom = solver.newVar(fmt("row-%s-bottom", rowText));

        // Walk through the row of elements such that the flow is correct.
        Iterator<DiagramElement> childIter;
        if (direction > 0) {
            childIter = elements.iterator();
        } else {
            childIter = new ReverseListIterator<DiagramElement>(elements);
        }

        proxies = listc(elements.size());
        ProxyDiagramElement prevProxy = null;
        while (childIter.hasNext()) {
            DiagramElement elem = childIter.next();
            elem.create(config, direction);
            ProxyDiagramElement proxy = parent.addDiagramElement(elem, "row-" + rowText);
            solver.addEq(proxy.connectTop, 0, connect);
            solver.addLe(top, 0, proxy.top);
            solver.addLe(proxy.bottom, 0, bottom);

            if (prevProxy != null)
                solver.addEq(prevProxy.right, 0, proxy.left);
            prevProxy = proxy;

            proxies.add(proxy);
        }

        solver.addEq(left, 0, first(proxies).left);
        solver.addEq(right, 0, last(proxies).right);
    }

    /** Get the left-most diagram proxy. */
    public ProxyDiagramElement getLeftProxy() {
        return first(proxies);
    }

    /** Get the right-most diagram proxy. */
    public ProxyDiagramElement getRightProxy() {
        return last(proxies);
    }
}
