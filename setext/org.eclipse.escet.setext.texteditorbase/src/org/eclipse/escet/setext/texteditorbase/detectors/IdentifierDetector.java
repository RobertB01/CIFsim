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

package org.eclipse.escet.setext.texteditorbase.detectors;

import org.eclipse.escet.setext.texteditorbase.rules.IdentifiersRule;
import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Detects identifiers (regular expression {@code [A-Za-z_][A-Za-z_0-9]*}).
 *
 * @see IdentifiersRule
 */
public class IdentifierDetector implements IWordDetector {
    @Override
    public boolean isWordStart(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_';
    }

    @Override
    public boolean isWordPart(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_' || (c >= '0' && c <= '9');
    }
}
