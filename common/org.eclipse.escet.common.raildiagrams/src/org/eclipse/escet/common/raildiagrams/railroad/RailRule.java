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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.raildiagrams.graphics.TextArea.makeTextArea;

import java.awt.Color;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.Configuration;
import org.eclipse.escet.common.raildiagrams.NameKind;
import org.eclipse.escet.common.raildiagrams.Size2D;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TextArea;

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
     */
    public RailRule(String ruleName, DiagramElement rootNode) {
        this.ruleName = ruleName;
        this.rootNode = rootNode;
    }

    @Override
    public void create(Configuration config, int direction) {
        double leftRulePadding = config.getRealValue("rule.padding.left");
        double topRulePadding = config.getRealValue("rule.padding.top");
        double bottomRulePadding = config.getRealValue("rule.padding.bottom");
        double rightRulePadding = config.getRealValue("rule.padding.right");
        double diagramTopPadding = config.getRealValue("rule.diagram.padding.top");
        double diagramIndent = config.getRealValue("rule.diagram.padding.left");
        double leadWidth = config.getRealValue("rule.diagram.lead.width");
        double trailWidth = config.getRealValue("rule.diagram.trail.width");
        double railWidth = config.getRailWidth();
        Color railColor = config.getRailColor();

        // Add header with the rule name.
        String prefix = "rule";
        String text = config.getNameText(ruleName, NameKind.HEADER);
        TextArea tbox = makeTextArea(solver, prefix + ".name.txt", config, NameKind.HEADER, text);
        addGraphic(tbox);
        solver.addEq(left, leftRulePadding, tbox.left);
        solver.addEq(top, topRulePadding, tbox.top);
        solver.addEq(connectTop, 0, top); // Initialize unused connectTop variable.
        solver.addLe(tbox.right, rightRulePadding, right);

        // Add the diagram.
        idbg();
        rootNode.create(config, direction);
        ddbg();
        ProxyDiagramElement rootProxy = addDiagramElement(rootNode, "root-node");
        HorLine leadLine = new HorLine(solver, "diagram.lead", railColor, railWidth);
        rootProxy.connectLeft(solver, leadLine);
        solver.addEq(leadLine.left, leadWidth, leadLine.right);
        solver.addEq(left, diagramIndent, leadLine.left);
        HorLine trailLine = new HorLine(solver, "diagram.lead", railColor, railWidth);
        rootProxy.connectRight(solver, trailLine);
        solver.addEq(trailLine.left, trailWidth, trailLine.right);
        solver.addLe(trailLine.right, rightRulePadding, right); // Title may be longer than the diagram.
        addGraphics(leadLine, trailLine);

        solver.addEq(tbox.bottom, diagramTopPadding, rootProxy.top);
        solver.addEq(rootProxy.bottom, bottomRulePadding, bottom);

        solver.solve("rule");
    }

    /**
     * Get the width of the diagram.
     *
     * @return Width of the diagram.
     */
    public Size2D getSize() {
        double width = solver.getVarValue(right) - solver.getVarValue(left) + 1;
        double height = solver.getVarValue(bottom) - solver.getVarValue(top) + 1;

        Assert.check(width > 1 && height > 1); // Rule has at least a name text.
        return new Size2D(width, height);
    }
}
