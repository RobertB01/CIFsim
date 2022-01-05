//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

/** Piece of text in the analysis report area. */
public abstract class ReportText {
    /**
     * Add the text and its styles to the report widget.
     *
     * @param displayed Report widget to use.
     * @param offset Current length of the text.
     * @return New length of the text.
     */
    public abstract int addText(StyledText displayed, int offset);
}
