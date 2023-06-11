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

package org.eclipse.escet.chi.codegen.statements.seq;

import static org.eclipse.escet.chi.codegen.statements.seq.Seq.convertStatements;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.metamodel.chi.ChiFactory;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.common.box.VBox;
import org.junit.jupiter.api.Test;

/**
 * Tests for converting Chi statements to seq statements.
 *
 * <p>
 * Many tests require a large number of objects at the right place, so it is easier to test in a Chi program.
 * </p>
 */
public class ConvertTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testBreakStatement() {
        List<Statement> stats = listc(1);
        Statement s = ChiFactory.eINSTANCE.createBreakStatement();
        stats.add(s);

        String[] expected = {"if (ALWAYS) break;"};

        checkEquality(expected, stats);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testPassStatement() {
        List<Statement> stats = listc(1);
        Statement s = ChiFactory.eINSTANCE.createPassStatement();
        stats.add(s);

        String[] expected = {""};

        checkEquality(expected, stats);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testContinueStatement() {
        List<Statement> stats = listc(1);
        Statement s = ChiFactory.eINSTANCE.createContinueStatement();
        stats.add(s);

        String[] expected = {"if (ALWAYS) continue;"};

        checkEquality(expected, stats);
    }

    /**
     * Check equality between the expected output and the generated statements (after boxing).
     *
     * @param expected Expected output.
     * @param stats Statements to convert.
     */
    private void checkEquality(String[] expected, List<Statement> stats) {
        CodeGeneratorContext ctxt = new CodeGeneratorContext("dummy_specname");
        JavaClass currentClass = new JavaClass(null, false, "foo", null, null);
        List<Seq> seqs = convertStatements(stats, ctxt, currentClass);
        VBox boxes = new VBox();
        for (Seq sq: seqs) {
            boxes.add(sq.boxify());
        }

        String es = "";
        for (String s: expected) {
            if (!es.isEmpty()) {
                es += "\n";
            }
            es += s;
        }
        String rs = "";
        for (String s: boxes.getLines()) {
            if (!rs.isEmpty()) {
                rs += "\n";
            }
            rs += s;
        }
        assertEquals(es, rs);
    }
}
