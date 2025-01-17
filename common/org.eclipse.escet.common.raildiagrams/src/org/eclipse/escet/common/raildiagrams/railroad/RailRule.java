//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.raildiagrams.graphics.TextArea.makeTextArea;
import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.writeDumpHeaderElements;

import java.awt.Color;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.config.NameKind;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TextArea;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;
import org.eclipse.escet.common.raildiagrams.util.Size2D;

/** Diagram of a production rule. */
public class RailRule extends DiagramElement {
    /** Optional name of the diagram, may be {@code null}. */
    public final String ruleName;

    /** Railroad diagram of the rule. */
    public final DiagramElement rootNode;

    /**
     * Constructor of the {@link RailRule} class.
     *
     * @param ruleName Optional name of the diagram.
     * @param rootNode Diagram to draw.
     * @param id Identifying number of the diagram element.
     */
    public RailRule(String ruleName, DiagramElement rootNode, int id) {
        super("rule", id);
        this.ruleName = ruleName;
        this.rootNode = rootNode;
    }

    @Override
    public void create(Configuration config, int direction) {
        int leftRulePadding = config.getIntValue("rule.padding.left");
        int topRulePadding = config.getIntValue("rule.padding.top");
        int bottomRulePadding = config.getIntValue("rule.padding.bottom");
        int rightRulePadding = config.getIntValue("rule.padding.right");
        int diagramTopPadding = config.getIntValue("rule.diagram.padding.top");
        int diagramIndent = config.getIntValue("rule.diagram.padding.left");
        int leadWidth = config.getIntValue("rule.diagram.lead.width");
        int trailWidth = config.getIntValue("rule.diagram.trail.width");
        int railWidth = config.getRailWidth();
        Color railColor = config.getRailColor();

        // Add header with the rule name.
        String prefix = kindName;
        String text = config.getNameText(ruleName, NameKind.HEADER);
        TextArea tbox = makeTextArea(solver, prefix + ".name.txt", config, NameKind.HEADER, text);
        addGraphic(tbox);
        solver.addEq(left, leftRulePadding, tbox.left);
        solver.addEq(top, topRulePadding, tbox.top);
        solver.addEq(connectTop, 0, top); // Initialize unused connectTop variable.
        solver.addLe(tbox.right, rightRulePadding, right);

        // Add the diagram.
        rootNode.create(config, direction);
        ProxyDiagramElement rootProxy = addDiagramElement(rootNode, "root-node");
        HorLine leadLine = new HorLine(solver, "diagram.lead", railColor, railWidth);
        rootProxy.connectLeft(solver, leadLine);
        solver.addEq(leadLine.left, leadWidth - 1, leadLine.right);
        solver.addEq(left, diagramIndent, leadLine.left);
        HorLine tailLine = new HorLine(solver, "diagram.tail", railColor, railWidth);
        rootProxy.connectRight(solver, tailLine);
        solver.addEq(tailLine.left, trailWidth - 1, tailLine.right);
        solver.addLe(tailLine.right, rightRulePadding, right); // Title may be longer than the diagram.
        addGraphics(leadLine, tailLine);

        solver.addEq(tbox.bottom, diagramTopPadding, rootProxy.top);
        solver.addEq(rootProxy.bottom, bottomRulePadding, bottom);

        solver.solve("rule", config);

        boolean dumpEquations = config.getDebugSetting(DebugDisplayKind.EQUATIONS);
        boolean dumpRelCoords = config.getDebugSetting(DebugDisplayKind.REL_COORDINATES);
        if ((dumpEquations || dumpRelCoords) && config.dodbg()) {
            writeDumpHeaderElements(config, this, list(rootNode));
            config.dbg();

            if (dumpEquations) {
                solver.dumpRelations(config);
                config.dbg();
            }
            if (dumpRelCoords) {
                dumpElementBox(config);
                config.dbg();
            }
        }
    }

    /**
     * Get the width of the diagram.
     *
     * @return Width of the diagram.
     */
    public Size2D getSize() {
        int width = solver.getVarValue(right) - solver.getVarValue(left) + 1;
        int height = solver.getVarValue(bottom) - solver.getVarValue(top) + 1;

        Assert.check(width > 1 && height > 1); // Rule has at least a name text.
        return new Size2D(width, height);
    }
}
