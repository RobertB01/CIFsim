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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dodbg;
import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.writeDumpHeaderElements;

import java.awt.Color;

import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.config.FontData;
import org.eclipse.escet.common.raildiagrams.config.NameKind;
import org.eclipse.escet.common.raildiagrams.config.TextSizeOffset;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TextArea;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;
import org.eclipse.escet.common.raildiagrams.util.Position2D;
import org.eclipse.escet.common.raildiagrams.util.Size2D;

/** Node displaying the name of a branch for referral from the explanatory text. */
public class BranchLabelNode extends DiagramElement {
    /** Text of the label. */
    public final String labelText;

    /**
     * Constructor of the {@link BranchLabelNode} class.
     *
     * @param labelText Text of the label.
     * @param id Identifying number of the diagram element.
     */
    public BranchLabelNode(String labelText, int id) {
        super("branch-label", id);
        this.labelText = labelText;
    }

    @Override
    public void create(Configuration config, int direction) {
        double railwidth = config.getRailWidth();
        double labelLeftPadding = config.getRealValue("branch-label.padding.left");
        double labelTopPadding = config.getRealValue("branch-label.padding.top");
        double labelRightPadding = config.getRealValue("branch-label.padding.right");
        double labelBottomPadding = config.getRealValue("branch-label.padding.bottom");
        double minWidth = config.getRealValue("branch-label.min-width");
        Color textColor = config.getRgbColor("branch-label.text.color");
        Color railColor = config.getRailColor();

        // Cannot (horizontally) center a diagram element, so compute manually.
        FontData font = config.getFont(NameKind.LABEL);
        TextSizeOffset textSizeOffset = config.getTextSizeOffset(labelText, NameKind.LABEL);
        Size2D textSize = textSizeOffset.size;
        Position2D textOffset = textSizeOffset.offset;

        if (dodbg() && config.getDebugSetting(DebugDisplayKind.STRUCTURE)) {
            writeDumpHeaderElements(this, null);
            dbg();
        }

        // Compute size of the box holding the text, and the position of the text in it.
        double labelWidth;
        double horOffset;
        if (minWidth <= labelLeftPadding + labelRightPadding + textSize.width) {
            // Padding and text decides.
            labelWidth = labelLeftPadding + labelRightPadding + textSize.width;
            horOffset = labelLeftPadding;
        } else {
            // minWidth decides, center the text in the remaining space.
            labelWidth = minWidth;
            double remaining = minWidth - (labelLeftPadding + labelRightPadding + textSize.width);
            horOffset = labelLeftPadding + remaining / 2;
        }
        textOffset = new Position2D(textOffset.x + horOffset, textOffset.y + labelTopPadding);
        textSize = new Size2D(labelWidth, labelTopPadding + textSize.height + labelBottomPadding);
        TextArea textArea = new TextArea(solver, "label.text", labelText, textColor, font, textOffset, textSize);
        addGraphic(textArea);

        HorLine rail = new HorLine(solver, "label.rail", railColor, railwidth);
        addGraphic(rail);
        solver.addEq(left, 0, rail.left);
        solver.addEq(left, 0, textArea.left);
        solver.addEq(rail.right, 0, right);
        solver.addEq(textArea.right, 0, right);

        solver.addEq(top, 0, textArea.top);
        solver.addEq(textArea.bottom, 0, rail.top);
        solver.addEq(connectTop, 0, rail.top);
        solver.addEq(bottom, 0, rail.bottom);

        solver.solve("branch-label", config);

        if (dodbg()) {
            writeDumpHeaderElements(this, null);
            dbg();
            if (config.getDebugSetting(DebugDisplayKind.EQUATIONS)) {
                solver.dumpRelations();
                dbg();
            }

            if (config.getDebugSetting(DebugDisplayKind.REL_COORDINATES)) {
                dumpElementBox();
                textArea.dump(solver, 0, 0);
                rail.dump(solver, 0, 0);
                dbg();
            }
        }
    }
}
