//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.analysis.reporttext;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;

/** Class for printing colored text. */
public class BackgroundColoredText extends ReportText {
    /** Color of the text. */
    private Color color;

    /** Text to print. */
    private String text;

    /**
     * Constructor of the {@link BackgroundColoredText} class.
     *
     * @param text Text to print.
     * @param color Color of the text (owned by the application).
     */
    public BackgroundColoredText(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public int addText(StyledText displayed, int offset) {
        displayed.append(text);
        StyleRange style = new StyleRange();
        style.background = color;
        style.start = offset;
        style.length = text.length();
        displayed.setStyleRange(style);
        return offset + text.length();
    }
}
