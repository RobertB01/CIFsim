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

package org.eclipse.escet.setext.texteditorbase;

import org.eclipse.escet.setext.texteditorbase.rules.RegExRule;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.RuleBasedScanner;

/**
 * Extension of the {@link RuleBasedScanner} class, exposing some of the internal implementation details. This class is
 * to be used instead of the {@link RuleBasedScanner} class, if a {@link RegExRule} is used.
 */
public class RuleBasedScannerEx extends RuleBasedScanner {
    /**
     * Returns the underlying document that is being scanned.
     *
     * @return The underlying document that is being scanned.
     */
    public IDocument getInternalDocument() {
        return fDocument;
    }

    /**
     * Returns the 0-based offset into the document of the next character to be read.
     *
     * @return the 0-based offset into the document of the next character to be read.
     * @see #getInternalDocument
     */
    public int getInternalOffset() {
        return fOffset;
    }

    /**
     * Returns the length of the document, taking into account the offset of the next character to read.
     *
     * @return The length of the document, taking into account the offset of the next character to read.
     * @see #getInternalDocument
     * @see #getInternalOffset
     */
    public int getInternalLength() {
        return fRangeEnd - fOffset;
    }
}
