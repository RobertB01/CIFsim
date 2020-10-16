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

package org.eclipse.escet.setext.runtime.exceptions;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Locale;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Exception indicating a scanning (lexing) error occurred. */
public class ScanException extends SyntaxException {
    /** The Unicode code point at which scanning failed, or {@code -1} for premature end of input. */
    private final int codePoint;

    /**
     * Constructor for the {@link ScanException} class.
     *
     * @param codePoint The Unicode code point at which scanning failed, or {@code -1} for premature end of input.
     * @param position The position information (possibly including source information) for the scan error.
     */
    public ScanException(int codePoint, Position position) {
        super(position);
        this.codePoint = codePoint;
    }

    /**
     * Returns the Unicode code point at which scanning failed, or {@code -1} for premature end of input.
     *
     * @return The Unicode code point at which scanning failed, or {@code -1}.
     */
    public int getCodePoint() {
        return codePoint;
    }

    @Override
    public String getMessage() {
        String src = getPosition().getSource();
        if (src == null) {
            src = "";
        }

        if (codePoint == -1) {
            return fmt("%sScanning failed at line %d, column %d, due to premature end of input.", src,
                    getPosition().getStartLine(), getPosition().getStartColumn());
        }

        String codePointTxt = Strings.isGraphicCodePoint(codePoint) ? Strings.codePointToStr(codePoint) : "";

        return fmt("%sScanning failed for character \"%s\" (Unicode U+%s) at line %d, column %d.", src, codePointTxt,
                Integer.toHexString(codePoint).toUpperCase(Locale.US), getPosition().getStartLine(),
                getPosition().getStartColumn());
    }
}
