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

import java.awt.Color;

import org.eclipse.escet.common.raildiagrams.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;

/** Empty node, that is, "()" in the input file. */
public class EmptyNode extends DiagramElement {
    @Override
    public void create(Configuration config, int direction) {
        double railwidth = config.getRailWidth();
        Color color = config.getRailColor();
        double minWidth = config.getRealValue("empty.width");
        HorLine hline = new HorLine(solver, "empty", color, railwidth);
        addGraphic(hline);

        solver.addEq(hline.left, minWidth, hline.right);
        solver.addEq(top, 0, hline.top);
        solver.addEq(bottom, 0, hline.bottom);
        solver.addEq(left, 0, hline.left);
        solver.addEq(right, 0, hline.right);
        solver.addEq(connectTop, 0, hline.top);

        solver.solve("empty-node");
    }
}
