//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.asciidoc.source.checker.checks;

import org.eclipse.escet.common.asciidoc.source.checker.AsciiDocSourceCheckContext;

/** An AsciiDoc source file check. */
public abstract class AsciiDocSourceFileCheck {
    /**
     * Perform the check, adding any found problems to the context.
     *
     * @param context The check context to use.
     */
    public abstract void check(AsciiDocSourceCheckContext context);
}
