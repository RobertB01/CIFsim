//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.typeinfos;

/** Text to output in case of a range check failure. */
public class RangeCheckErrorLevelText {
    /** Whether the supplied text is in fact an integer variable that needs to be printed. */
    public final boolean isIntVariable;

    /**
     * Text to use for error reporting.
     *
     * <p>
     * It is either a fieldname of a tuple or a variable expressing the index in an array.
     * </p>
     */
    public final String text;

    /**
     * Constructor for {@link RangeCheckErrorLevelText} class.
     *
     * @param isIntVariable Whether the supplied text is in fact an integer variable that needs to be printed.
     * @param text Text to use for error reporting.
     */
    public RangeCheckErrorLevelText(boolean isIntVariable, String text) {
        this.isIntVariable = isIntVariable;
        this.text = text;
    }
}
