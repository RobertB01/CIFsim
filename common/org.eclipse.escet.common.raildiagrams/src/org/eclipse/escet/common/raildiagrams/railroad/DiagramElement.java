//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.writeDumpHeaderElements;

import java.util.List;

import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.output.OutputTarget;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.solver.Variable;

/** A (rectangular) part of a railroad diagram, also acts as the edges of its contents. */
public abstract class DiagramElement {
    /** Name of the element kind. */
    public final String kindName;

    /** Identifying number of the diagram element. */
    public final int id;

    /** Sub-diagrams of the element. */
    public final List<ProxyDiagramElement> childDiagramElements = list();

    /** Variable storage, constraint storage, and solver of the variable values. */
    public final Solver solver = new Solver();

    /** Displayed graphics within the element. */
    private final List<Area> graphics = list();

    /** Variable holding the X coordinate of the left side of the diagram element. */
    public final Variable left;

    /** Variable holding the X coordinate of the right side of the diagram element. */
    public final Variable right;

    /** Variable holding the Y coordinate of the top side of the diagram element. */
    public final Variable top;

    /** Variable holding the Y coordinate of the bottom side of the diagram element. */
    public final Variable bottom;

    /** Variable holding the Y coordinate of the top side of the diagram element rail connection point. */
    public final Variable connectTop;

    /**
     * Constructor of the {@link DiagramElement} class.
     *
     * @param kindName Kind name of the element.
     * @param id Identifying number of the diagram element.
     */
    public DiagramElement(String kindName, int id) {
        this.kindName = kindName;
        this.id = id;
        left = solver.newVar("diagram.left");
        right = solver.newVar("diagram.right");
        top = solver.newVar("diagram.top");
        bottom = solver.newVar("diagram.bottom");
        connectTop = solver.newVar("diagram.connectTop");
    }

    /**
     * Add a graphic element to this diagram element.
     *
     * @param graphic The graphic element to add.
     */
    public void addGraphic(Area graphic) {
        graphics.add(graphic);
    }

    /**
     * Add all graphics to this diagram element.
     *
     * @param graphics The graphic elements to add.
     */
    public void addGraphics(Area... graphics) {
        for (Area graphic: graphics) {
            this.graphics.add(graphic);
        }
    }

    /**
     * Add a child element to this diagram element.
     *
     * @param element Element to add.
     * @param elemName Name of the element.
     * @return The proxied element with variables from the element environment. Use this for connecting the child
     *     element to other parts of the diagram here.
     */
    public ProxyDiagramElement addDiagramElement(DiagramElement element, String elemName) {
        ProxyDiagramElement proxyDiagram = new ProxyDiagramElement(element, elemName, solver);
        childDiagramElements.add(proxyDiagram);
        return proxyDiagram;
    }

    /**
     * Creates the node in the diagram.
     *
     * @param config Configuration to use.
     * @param direction Direction of flow through the node, positive is left to right, negative is right to left.
     */
    public abstract void create(Configuration config, int direction);

    /**
     * Dump relative coordinates of the element.
     *
     * @param config Configuration to use.
     */
    public void dumpElementBox(Configuration config) {
        dumpElementBox(config, "Relative", 0, 0);
    }

    /**
     * Dump coordinates of the element.
     *
     * @param config Configuration to use.
     * @param coordType Text to describe the type of coordinates.
     * @param xOffset Horizontal offset of the element in the picture.
     * @param yOffset Vertical offset of the element in the picture.
     */
    public void dumpElementBox(Configuration config, String coordType, int xOffset, int yOffset) {
        config.dbg("%s coordinates:", coordType);
        config.idbg();
        config.dbg("%s-%s: x[%d--%d], y[%d--%d], connectTop=%d", kindName, id, xOffset + solver.getVarValue(left),
                xOffset + solver.getVarValue(right), yOffset + solver.getVarValue(top),
                yOffset + solver.getVarValue(bottom), yOffset + solver.getVarValue(connectTop));
        config.idbg();
        for (ProxyDiagramElement proxy: childDiagramElements) {
            DiagramElement child = proxy.child;
            config.dbg("%s-%s: x[%d--%d], y[%d--%d], connectTop=%d", child.kindName, child.id,
                    xOffset + solver.getVarValue(proxy.left), xOffset + solver.getVarValue(proxy.right),
                    yOffset + solver.getVarValue(proxy.top), yOffset + solver.getVarValue(proxy.bottom),
                    yOffset + solver.getVarValue(proxy.connectTop));
        }
        config.ddbg();
        config.dbg();
        for (Area graphic: graphics) {
            graphic.dump(config, solver, xOffset, yOffset);
        }
        config.ddbg();
    }

    /**
     * Paint the graphic elements and the child diagrams to the graphics output.
     *
     * @param left Left position of the area that may be used for painting.
     * @param top Top position of the area that may be used for painting.
     * @param outputTarget Diagram to write.
     * @param dumpAbsCoords Whether to dump the absolute coordinates of the elements for debugging.
     * @param config Configuration to use.
     */
    public void paint(int left, int top, OutputTarget outputTarget, boolean dumpAbsCoords, Configuration config) {
        outputTarget.addDiagramElement(left, top, solver, this);

        if (dumpAbsCoords) {
            writeDumpHeaderElements(config, this, null);
            config.dbg();
            dumpElementBox(config, "Absolute", left, top);
            config.dbg();
        }

        for (Area graphic: graphics) {
            outputTarget.addGraphic(left, top, solver, graphic);
        }

        for (ProxyDiagramElement childElement: childDiagramElements) {
            int childLeft = left + solver.getVarValue(childElement.left);
            int childTop = top + solver.getVarValue(childElement.top);
            childElement.paint(childLeft, childTop, outputTarget, dumpAbsCoords, config);
        }
    }
}
