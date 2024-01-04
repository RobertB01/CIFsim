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

package org.eclipse.escet.cif.codegen;

import org.eclipse.escet.common.box.CodeBox;

/** Generic if/else generator for languages with curly braced code blocks. */
public class CurlyBraceIfElseGenerator implements IfElseGenerator {
    /** Number of additional opened indents in 'else if' due to code insertion. */
    private int numberAdditionalIndents;

    @Override
    public boolean branchIsSafeScope() {
        return true;
    }

    @Override
    public void generateIf(ExprCode guard, CodeBox code) {
        code.add(guard.getCode());
        code.add("if (%s) {", guard.getData());
        code.indent();
        numberAdditionalIndents = 0;
    }

    @Override
    public void generateElseIf(ExprCode guard, CodeBox code) {
        // Generate 'else if'.
        if (guard.hasCode()) {
            // Guard has code to execute first, split the "else if" in two
            // pieces, and insert the code before the "if".
            code.dedent();
            code.add("} else {");
            code.indent();
            code.add(guard.getCode());
            code.add("if (%s) {", guard.getData());
            code.indent();

            numberAdditionalIndents++;
        } else {
            code.dedent();
            code.add("} else if (%s) {", guard.getData());
            code.indent();
        }
    }

    @Override
    public void generateElse(CodeBox code) {
        code.dedent();
        code.add("} else {");
        code.indent();
    }

    @Override
    public void generateEndIf(CodeBox code) {
        // Loop one extra time to close the last selection as well.
        while (numberAdditionalIndents >= 0) {
            code.dedent();
            code.add("}");
            numberAdditionalIndents--;
        }
    }
}
