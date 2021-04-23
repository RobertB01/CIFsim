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

package org.eclipse.escet.common.raildiagrams.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import org.eclipse.escet.common.raildiagrams.Configuration;
import org.eclipse.escet.common.raildiagrams.FontData;
import org.eclipse.escet.common.raildiagrams.NameKind;
import org.eclipse.escet.common.raildiagrams.Position2D;
import org.eclipse.escet.common.raildiagrams.Size2D;
import org.eclipse.escet.common.raildiagrams.TextSizeOffset;
import org.eclipse.escet.common.raildiagrams.solver.Solver;

/** Area with some text in it. */
public class TextArea extends Area {
    /** Text to display. */
    public final String text;

    /** Color of the text. */
    public final Color color;

    /** Font information. */
    public final FontData font;

    /** Position to print the text relative to the top-left corner of this box. */
    public final Position2D offset;

    /**
     * Constructor of the {@link TextArea} class.
     *
     * @param solver Constraint solver that computes position of the text area.
     * @param prefix Name prefix for new variables, used for debugging.
     * @param text Text to display in the output.
     * @param color Color of the text.
     * @param font Font information.
     * @param offset Position to print the text relative to the top-left corner of this box.
     * @param size Size of the box.
     */
    public TextArea(Solver solver, String prefix, String text, Color color, FontData font, Position2D offset,
            Size2D size)
    {
        super(solver, prefix);
        this.text = text;
        this.color = color;
        this.font = font;
        this.offset = offset;

        solver.addEq(left, size.width - 1, right);
        solver.addEq(top, size.height - 1, bottom);
    }

    @Override
    public void paint(double baseLeft, double baseTop, Solver solver, Graphics2D gd) {
        double x = baseLeft + solver.getVarValue(left);
        double y = baseTop + solver.getVarValue(top);

        gd.setColor(color);
        gd.setFont(font.font);
        gd.drawString(text, (int)(x + offset.x), (int)(y + offset.y));
    }

    /**
     * Construct a text box.
     *
     * @param solver Constraint problem solver for the box.
     * @param prefix Unique prefix for variables.
     * @param config Configuration of the railroad generator.
     * @param nameKind Kind of name
     * @param text Text to display.
     * @return The constructed box with text.
     */
    public static TextArea makeTextArea(Solver solver, String prefix, Configuration config, NameKind nameKind,
            String text)
    {
        FontData font = config.getFont(nameKind);
        Color textColor = config.getTextColor(nameKind);
        TextSizeOffset textSizeOffset = config.getTextSizeOffset(text, nameKind);
        Size2D textSize = textSizeOffset.size;
        Position2D textOffset = textSizeOffset.offset;
        TextArea textArea = new TextArea(solver, prefix, text, textColor, font, textOffset, textSize);
        return textArea;
    }
}
