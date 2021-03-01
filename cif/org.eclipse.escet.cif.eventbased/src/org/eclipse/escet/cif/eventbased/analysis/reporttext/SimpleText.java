//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.swt.custom.StyledText;

/** Plain text fragment in the report text. */
public class SimpleText extends ReportText {
    /** The text to add. */
    private final String text;

    /**
     * Constructor of the {@link SimpleText} class.
     *
     * @param text Text to add.
     */
    public SimpleText(String text) {
        this.text = text;
    }

    @Override
    public int addText(StyledText displayed, int offset) {
        displayed.append(text);
        return offset + text.length();
    }
}
