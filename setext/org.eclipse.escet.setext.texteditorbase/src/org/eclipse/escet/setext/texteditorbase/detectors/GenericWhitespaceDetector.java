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

package org.eclipse.escet.setext.texteditorbase.detectors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/** Generic whitespace detector. Detects: spaces, tabs, and new lines (carriage returns, and line feeds). */
public class GenericWhitespaceDetector implements IWhitespaceDetector {
    @Override
    public boolean isWhitespace(char c) {
        return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
    }
}
