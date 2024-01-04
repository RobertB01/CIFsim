//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.setext.texteditorbase.rules.IntNumberRule;
import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Detects integer numbers (regular expression {@code [0-9]+}).
 *
 * @see IntNumberRule
 */
public class IntNumberDetector implements IWordDetector {
    @Override
    public boolean isWordStart(char c) {
        return c >= '0' && c <= '9';
    }

    @Override
    public boolean isWordPart(char c) {
        return c >= '0' && c <= '9';
    }
}
