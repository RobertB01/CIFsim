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

package org.eclipse.escet.setext.runtime.expterms.tests;

import org.eclipse.escet.setext.runtime.Parser;

/**
 * Call back hook methods for:
 * <ul>
 * <li>{@link ExpectedTermsParser}</li>
 * </ul>
 */
public final class ExpectedTermsParserHooks implements ExpectedTermsParser.Hooks {
    @Override
    public void setParser(Parser<?> parser) {
        // Ignore.
    }

    @Override // S : AKW X BKW;
    public Integer parseS1(Double d2) {
        return null;
    }

    @Override // S : BKW X AKW;
    public Integer parseS2(Double d2) {
        return null;
    }

    @Override // X : CKW;
    public Double parseX1() {
        return null;
    }

    @Override // X : CKW DKW;
    public Double parseX2() {
        return null;
    }
}
