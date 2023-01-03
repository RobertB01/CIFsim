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

import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.writeDumpHeaderElements;

import java.awt.Color;

import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;

/** Empty node, that is, "()" in the input file. */
public class EmptyNode extends DiagramElement {
    /**
     * Constructor of the {@link EmptyNode} class.
     *
     * @param id Identifying number of the diagram element.
     */
    public EmptyNode(int id) {
        super("empty", id);
    }

    @Override
    public void create(Configuration config, int direction) {
        int railwidth = config.getRailWidth();
        Color color = config.getRailColor();
        int minWidth = config.getIntValue("empty.width");
        HorLine hline = new HorLine(solver, "empty", color, railwidth);
        addGraphic(hline);

        solver.addEq(hline.left, minWidth - 1, hline.right);

        solver.addEq(top, 0, hline.top);
        solver.addEq(bottom, 0, hline.bottom);
        solver.addEq(left, 0, hline.left);
        solver.addEq(right, 0, hline.right);
        solver.addEq(connectTop, 0, hline.top);

        solver.solve("empty-node", config);

        boolean dumpEquations = config.getDebugSetting(DebugDisplayKind.EQUATIONS);
        boolean dumpRelCoords = config.getDebugSetting(DebugDisplayKind.REL_COORDINATES);
        if ((dumpEquations || dumpRelCoords) && config.dodbg()) {
            writeDumpHeaderElements(config, this, null);
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
}
