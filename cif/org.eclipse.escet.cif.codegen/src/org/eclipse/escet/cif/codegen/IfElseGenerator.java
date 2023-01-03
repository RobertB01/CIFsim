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

package org.eclipse.escet.cif.codegen;

import org.eclipse.escet.common.box.CodeBox;

/** Interface for generating the if/else selections. */
public interface IfElseGenerator {
    /**
     * Is a branch a safe scope for creating temporary variables?
     *
     * @return Whether a branch is a safe scope for creating temporary variables.
     */
    public abstract boolean branchIsSafeScope();

    /**
     * Generate an 'if' code line.
     *
     * @param guard Guard to test at run-time.
     * @param code Destination of the generated code.
     */
    public abstract void generateIf(ExprCode guard, CodeBox code);

    /**
     * Generate an 'else if' code line.
     *
     * @param guard Guard to test at run-time.
     * @param code Destination of the generated code.
     */
    public abstract void generateElseIf(ExprCode guard, CodeBox code);

    /**
     * Generate an 'else' code line.
     *
     * @param code Destination of the generated code.
     */
    public abstract void generateElse(CodeBox code);

    /**
     * Close the 'if' statement.
     *
     * @param code Destination of the generated code.
     */
    public abstract void generateEndIf(CodeBox code);
}
