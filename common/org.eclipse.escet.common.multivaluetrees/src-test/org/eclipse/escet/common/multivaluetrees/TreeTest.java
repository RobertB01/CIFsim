//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.multivaluetrees;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TreeTest {
    public static SimpleVariable v1;

    public static SimpleVariable v2;

    public static SimpleVariable v3;

    public static SimpleVariable v4;

    public static SimpleVarInfoBuilder svb;

    static {
        v1 = new SimpleVariable("v1", 5, 4); // int[5..8] v1
        v2 = new SimpleVariable("v2", 5, 4); // int[5..8] v2
        v3 = new SimpleVariable("v3", 5, 4); // int[5..8] v3
        v4 = new SimpleVariable("v4", 5, 4); // int[5..8] v4

        svb = new SimpleVarInfoBuilder(1);
        svb.addVariable(v1);
        svb.addVariable(v2);
        svb.addVariable(v3);
        svb.addVariable(v4);
    }

    @Test
    public void equalityValueTest() {
        Tree t = new Tree();
        VarInfo vi3 = svb.getVarInfo(v3, 0);

        Node vi3eq5 = t.buildEqualityValue(vi3, 5);
        assertSame(vi3eq5.varInfo, vi3);
        assertEquals(vi3eq5.childs.length, 4);
        assertSame(vi3eq5.childs[0], Tree.ONE);
        assertSame(vi3eq5.childs[1], Tree.ZERO);
        assertSame(vi3eq5.childs[2], Tree.ZERO);
        assertSame(vi3eq5.childs[3], Tree.ZERO);
    }

    @Test
    public void equalityIndexTest() {
        Tree t = new Tree();
        VarInfo vi3 = svb.getVarInfo(v3, 0);

        Node vi3eq6 = t.buildEqualityIndex(vi3, 1);
        assertSame(vi3eq6.varInfo, vi3);
        assertEquals(vi3eq6.childs.length, 4);
        assertSame(vi3eq6.childs[0], Tree.ZERO);
        assertSame(vi3eq6.childs[1], Tree.ONE);
        assertSame(vi3eq6.childs[2], Tree.ZERO);
        assertSame(vi3eq6.childs[3], Tree.ZERO);
    }

    @Test
    public void equalityChainTest() {
        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        VarInfo vi3 = svb.getVarInfo(v3, 0);

        Node vi3eq6 = t.buildEqualityIndex(vi3, 1); // v3 == 6
        Node chain = t.buildEqualityIndex(vi1, 0, vi3eq6); // v1 == 5 && v3 == 6

        assertSame(chain.varInfo, vi1);
        assertEquals(chain.childs.length, 4);
        assertSame(chain.childs[0], vi3eq6);
        assertSame(chain.childs[1], Tree.ZERO);
        assertSame(chain.childs[2], Tree.ZERO);
        assertSame(chain.childs[3], Tree.ZERO);

        assertSame(vi3eq6.varInfo, vi3);
        assertEquals(vi3eq6.childs.length, 4);
        assertSame(vi3eq6.childs[0], Tree.ZERO);
        assertSame(vi3eq6.childs[1], Tree.ONE);
        assertSame(vi3eq6.childs[2], Tree.ZERO);
        assertSame(vi3eq6.childs[3], Tree.ZERO);
    }

    @Test
    public void conjunctFalseTrueTests() {
        Tree t = new Tree();
        Node n;

        n = t.conjunct(Tree.ONE, Tree.ONE);
        assertSame(n, Tree.ONE);

        n = t.conjunct(Tree.ZERO, Tree.ONE);
        assertSame(n, Tree.ZERO);

        n = t.conjunct(Tree.ONE, Tree.ZERO);
        assertSame(n, Tree.ZERO);

        n = t.conjunct(Tree.ZERO, Tree.ZERO);
        assertSame(n, Tree.ZERO);
    }

    @Test
    public void conjunctEqualNodesTests() {
        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        VarInfo vi3 = svb.getVarInfo(v3, 0);

        Node vi1eq5 = t.buildEqualityIndex(vi1, 0); // v1 == 5
        Node vi1eq6 = t.buildEqualityIndex(vi1, 1); // v1 == 6
        Node vi3eq6 = t.buildEqualityIndex(vi3, 1); // v3 == 6

        Node n = t.conjunct(vi1eq5, vi1eq5); // v1 == 5 && v1 == 5 -> v1 == 5
        assertSame(n, vi1eq5);

        n = t.conjunct(vi1eq5, vi3eq6); // v1 == 5 && v3 == 6 -> merged in one tree.
        assertSame(vi1, n.varInfo);
        assertSame(n.childs[0], vi3eq6);
        assertSame(vi3eq6.childs[1], Tree.ONE);

        n = t.conjunct(vi3eq6, vi1eq5); // v3 == 6 && v1 == 5 -> merged in one tree, v1 on top.
        assertSame(vi1, n.varInfo);
        assertSame(n.childs[0], vi3eq6);
        assertSame(vi3eq6.childs[1], Tree.ONE);

        n = t.conjunct(vi1eq5, vi1eq6); // v1 == 5 && v1 == 6 -> false
        assertSame(n, Tree.ZERO);
    }

    @Test
    public void disjunctFalseTrueTests() {
        Tree t = new Tree();
        Node n;

        n = t.disjunct(Tree.ONE, Tree.ONE);
        assertSame(n, Tree.ONE);

        n = t.disjunct(Tree.ZERO, Tree.ONE);
        assertSame(n, Tree.ONE);

        n = t.disjunct(Tree.ONE, Tree.ZERO);
        assertSame(n, Tree.ONE);

        n = t.disjunct(Tree.ZERO, Tree.ZERO);
        assertSame(n, Tree.ZERO);
    }

    @Test
    public void disjunctEqualNodesTests() {
        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        VarInfo vi3 = svb.getVarInfo(v3, 0);

        Node vi1eq5 = t.buildEqualityIndex(vi1, 0); // v1 == 5
        Node vi1eq6 = t.buildEqualityIndex(vi1, 1); // v1 == 6
        Node vi3eq6 = t.buildEqualityIndex(vi3, 1); // v3 == 6

        Node n = t.disjunct(vi1eq5, vi1eq5); // v1 == 5 || v1 == 5 -> v1 == 5
        assertSame(n, vi1eq5);

        n = t.disjunct(vi1eq5, vi3eq6); // v1 == 5 || v3 == 6 -> merged in one tree.
        assertSame(vi1, n.varInfo);
        assertSame(n.childs[0], Tree.ONE);
        assertSame(n.childs[1], vi3eq6);
        assertSame(n.childs[2], vi3eq6);
        assertSame(n.childs[3], vi3eq6);
        assertSame(vi3eq6.childs[1], Tree.ONE);

        n = t.disjunct(vi3eq6, vi1eq5); // v3 == 6 || v1 == 5 -> merged in one tree, v1 on top.
        assertSame(vi1, n.varInfo);
        assertSame(n.childs[0], Tree.ONE);
        assertSame(n.childs[1], vi3eq6);
        assertSame(n.childs[2], vi3eq6);
        assertSame(n.childs[3], vi3eq6);
        assertSame(vi3eq6.childs[1], Tree.ONE);

        n = t.disjunct(vi1eq5, vi1eq6); // v1 == 5 || v1 == 6
        assertSame(vi1, n.varInfo);
        assertSame(n.childs[0], Tree.ONE);
        assertSame(n.childs[1], Tree.ONE);
        assertSame(n.childs[2], Tree.ZERO);
        assertSame(n.childs[3], Tree.ZERO);
    }

    @Test
    public void invertTests() {
        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        Node vi1eq6 = t.buildEqualityIndex(vi1, 1); // v1 == 6

        Node n = t.invert(Tree.ONE);
        assertSame(n, Tree.ZERO);

        n = t.invert(Tree.ZERO);
        assertSame(n, Tree.ONE);

        n = t.invert(vi1eq6);
        assertSame(vi1, n.varInfo);
        assertSame(n.childs[0], Tree.ONE);
        assertSame(n.childs[1], Tree.ZERO);
        assertSame(n.childs[2], Tree.ONE);
        assertSame(n.childs[3], Tree.ONE);
    }

    @Test
    public void compositeInvertTest() {
        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        VarInfo vi2 = svb.getVarInfo(v2, 0);

        Node vi1eq6 = t.buildEqualityIndex(vi1, 1); // v1 == 6
        Node vi2eq8 = t.buildEqualityValue(vi2, 8); // v2 == 8

        Node n = t.disjunct(vi1eq6, vi2eq8); // v1 == 6 || v2 == 8
        // Verify disjunction result.
        Node sub = n.childs[0];
        assertSame(vi1, n.varInfo);
        assertSame(sub, vi2eq8);
        assertSame(n.childs[0], sub);
        assertSame(n.childs[1], Tree.ONE);
        assertSame(n.childs[2], sub);
        assertSame(n.childs[3], sub);
        assertSame(sub.childs[0], Tree.ZERO);
        assertSame(sub.childs[1], Tree.ZERO);
        assertSame(sub.childs[2], Tree.ZERO);
        assertSame(sub.childs[3], Tree.ONE);

        // Invert, and check again.
        n = t.invert(n);
        sub = n.childs[0];
        assertSame(vi1, n.varInfo);
        assertSame(vi2, sub.varInfo);
        assertSame(n.childs[0], sub);
        assertSame(n.childs[1], Tree.ZERO);
        assertSame(n.childs[2], sub);
        assertSame(n.childs[3], sub);
        assertSame(sub.childs[0], Tree.ONE);
        assertSame(sub.childs[1], Tree.ONE);
        assertSame(sub.childs[2], Tree.ONE);
        assertSame(sub.childs[3], Tree.ZERO);
    }

    @Test
    public void abstractTest() {
        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        VarInfo vi2 = svb.getVarInfo(v2, 0);
        VarInfo vi3 = svb.getVarInfo(v3, 0);

        VarInfo[] abstractions = new VarInfo[1];
        abstractions[0] = vi1;

        Node vi1eq5 = t.buildEqualityIndex(vi1, 0); // v1 == 5
        Node vi1eq6 = t.buildEqualityIndex(vi1, 1); // v1 == 6
        Node vi2eq6 = t.buildEqualityIndex(vi2, 1); // v2 == 6
        Node vi3eq6 = t.buildEqualityIndex(vi3, 1); // v3 == 6

        Node n = t.variableAbstractions(Tree.ONE, abstractions);
        assertSame(n, Tree.ONE);

        n = t.variableAbstractions(Tree.ZERO, abstractions);
        assertSame(n, Tree.ZERO);

        // Abstract from v1.
        n = t.variableAbstractions(vi1eq6, abstractions);
        assertSame(n, Tree.ONE);

        // expr = (v1==5 && v2==6) || (v1==6 && v3==6).
        Node v1v2 = t.conjunct(vi1eq5, vi2eq6);
        Node v1v3 = t.conjunct(vi1eq6, vi3eq6);
        Node expr = t.disjunct(v1v2, v1v3);

        n = t.variableAbstractions(expr, abstractions);
        assertSame(n, t.disjunct(vi2eq6, vi3eq6));

        // Multiple abstractions.
        VarInfo[] abstractions2 = new VarInfo[2];
        abstractions2[0] = vi1;
        abstractions2[1] = vi2;

        n = t.variableAbstractions(expr, abstractions2);
        assertSame(n, Tree.ONE);
    }

    @Test
    public void assignTests() {
        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        VarInfo vi2 = svb.getVarInfo(v2, 0);
        VarInfo vi3 = svb.getVarInfo(v3, 0);

        Node vi1eq6 = t.buildEqualityIndex(vi1, 1); // v1 == 6
        Node vi2eq8 = t.buildEqualityValue(vi2, 8); // v2 == 8

        Node n = t.assign(vi1eq6, vi1, 0); // Set v1 to 5.
        assertSame(n, Tree.ZERO);
        n = t.assign(vi1eq6, vi1, 1); // Set v1 to 6.
        assertSame(n, Tree.ONE);

        Node v1v2 = t.conjunct(vi1eq6, vi2eq8); // v1 == 6 && v2 == 8
        n = t.assign(v1v2, vi1, 1); // Select on v1 == 6
        assertSame(vi2, n.varInfo); // Returns v2 == 8
        assertSame(n.childs[3], Tree.ONE);

        n = t.assign(v1v2, vi2, 3); // Select on v2 == 8
        assertSame(vi1, n.varInfo); // Returns v1 == 6
        assertSame(n.childs[1], Tree.ONE);

        n = t.assign(v1v2, vi3, 2); // Select on v3 == 7
        assertSame(n, v1v2); // Returns the same v1=6 && v2 == 8 (v3 was 'true' for any of its values).
    }

    /** Adjacent replace test with 'old' variable at top-level. */
    @Test
    public void replaceAdjacentVarsOldNewTest() {
        final boolean DEBUG = false;

        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        VarInfo vi2 = svb.getVarInfo(v2, 0);
        VarInfo vi3 = svb.getVarInfo(v3, 0);
        VarInfo vi4 = svb.getVarInfo(v4, 0);

        VariableReplacement[] replacements = new VariableReplacement[1];
        replacements[0] = new VariableReplacement(vi2, vi3); // vi1 = 'v2' = 'a', vi2 = 'v3' = 'a+'

        Node vi1eq5 = t.buildEqualityValue(vi1, 5); // v1 == 5
        Node vi2eq6 = t.buildEqualityValue(vi2, 6); // v2 == 6
        Node vi2eq8 = t.buildEqualityValue(vi2, 8); // v2 == 8
        Node vi3eq8 = t.buildEqualityValue(vi3, 8); // v3 == 8
        Node vi4eq7 = t.buildEqualityValue(vi4, 7); // v4 == 7

        Node n, m;
        n = t.adjacentReplacements(Tree.ZERO, replacements);
        assertSame(n, Tree.ZERO);

        // Try all 16 combinations of [vi0Eq5, vi1Eq6, vi2Eq8, vi3Eq7]
        for (int useV1 = 0; useV1 < 2; useV1++) {
            for (int useV2 = 0; useV2 < 2; useV2++) {
                for (int useV3 = 0; useV3 < 2; useV3++) {
                    for (int useV4 = 0; useV4 < 2; useV4++) {
                        n = Tree.ONE;
                        m = Tree.ONE;
                        if (useV4 == 1) {
                            n = t.conjunct(vi4eq7, n);
                            m = t.conjunct(vi4eq7, m);
                        }
                        if (useV3 == 1) {
                            n = t.conjunct(vi3eq8, n);
                            m = t.conjunct(vi2eq8, m);
                        }
                        if (useV2 == 1) {
                            n = t.conjunct(vi2eq6, n);
                            /* v2 can only be from v3. */
                        }
                        if (useV1 == 1) {
                            n = t.conjunct(vi1eq5, n);
                            m = t.conjunct(vi1eq5, m);
                        }

                        if (DEBUG) {
                            System.out.printf("Input node:\n%s\n", t.dumpGraph(n));
                            System.out.printf("Expected output node:\n%s\n", t.dumpGraph(m));
                        }
                        Node p = t.adjacentReplacements(n, replacements);
                        if (DEBUG) {
                            System.out.printf("Real output node:\n%s\n", t.dumpGraph(p));
                            System.out.printf("\n\n");
                        }
                        assertSame(p, m);
                    }
                }
            }
        }
    }

    /** Adjacent replace test with 'new' variable at top-level. */
    @Test
    public void replaceAdjacentVarsNewOldTest() {
        final boolean DEBUG = false;

        Tree t = new Tree();
        VarInfo vi1 = svb.getVarInfo(v1, 0);
        VarInfo vi2 = svb.getVarInfo(v2, 0);
        VarInfo vi3 = svb.getVarInfo(v3, 0);
        VarInfo vi4 = svb.getVarInfo(v4, 0);

        VariableReplacement[] replacements = new VariableReplacement[1];
        replacements[0] = new VariableReplacement(vi3, vi2); // v3 = 'a', 'v2' = 'a+'.

        Node vi1eq5 = t.buildEqualityValue(vi1, 5); // v1 == 5
        Node vi2eq6 = t.buildEqualityValue(vi2, 6); // v2 == 6
        Node vi3eq6 = t.buildEqualityValue(vi3, 6); // v3 == 6
        Node vi3eq8 = t.buildEqualityValue(vi3, 8); // v3 == 8
        Node vi4eq7 = t.buildEqualityValue(vi4, 7); // v4 == 7

        Node n, m;
        n = t.adjacentReplacements(Tree.ZERO, replacements);
        assertSame(n, Tree.ZERO);

        // Try all 16 combinations of [vi1Eq5, vi2Eq6, vi3Eq8, vi4Eq7]
        for (int useV1 = 0; useV1 < 2; useV1++) {
            for (int useV2 = 0; useV2 < 2; useV2++) {
                for (int useV3 = 0; useV3 < 2; useV3++) {
                    for (int useV4 = 0; useV4 < 2; useV4++) {
                        n = Tree.ONE;
                        m = Tree.ONE;
                        if (useV4 == 1) {
                            n = t.conjunct(vi4eq7, n);
                            m = t.conjunct(vi4eq7, m);
                        }
                        if (useV3 == 1) {
                            n = t.conjunct(vi3eq8, n);
                            /* v3 van only be derived from v2. */
                        }
                        if (useV2 == 1) {
                            n = t.conjunct(vi2eq6, n);
                            m = t.conjunct(vi3eq6, m);
                        }
                        if (useV1 == 1) {
                            n = t.conjunct(vi1eq5, n);
                            m = t.conjunct(vi1eq5, m);
                        }
                        if (DEBUG) {
                            System.out.printf("Input node:\n%s\n", t.dumpGraph(n));
                            System.out.printf("Expected output node:\n%s\n", t.dumpGraph(m));
                        }
                        Node p = t.adjacentReplacements(n, replacements);
                        if (DEBUG) {
                            System.out.printf("Real output node:\n%s\n", t.dumpGraph(p));
                            System.out.printf("\n\n");
                        }
                        assertSame(p, m);
                    }
                }
            }
        }
    }

    @Test
    public void replaceOldNewTest() {
        final boolean DEBUG = false;

        SimpleVariable v1 = new SimpleVariable("v1", 5, 5); // int[5..9] v1
        SimpleVariable v2 = new SimpleVariable("v2", 5, 5); // int[5..9] v2
        SimpleVariable v3 = new SimpleVariable("v3", 5, 5); // int[5..9] v3
        SimpleVariable v4 = new SimpleVariable("v4", 5, 5); // int[5..9] v4
        SimpleVariable v5 = new SimpleVariable("v5", 5, 5); // int[5..9] v4
        SimpleVarInfoBuilder svb2 = new SimpleVarInfoBuilder(1);
        svb2.addVariable(v1);
        svb2.addVariable(v2);
        svb2.addVariable(v3);
        svb2.addVariable(v4);
        svb2.addVariable(v5);
        Tree t = new Tree();
        VarInfo vi1 = svb2.getVarInfo(v1, 0);
        VarInfo vi2 = svb2.getVarInfo(v2, 0); // old var
        VarInfo vi3 = svb2.getVarInfo(v3, 0);
        VarInfo vi4 = svb2.getVarInfo(v4, 0); // new var
        VarInfo vi5 = svb2.getVarInfo(v5, 0);

        Node vi1eq5 = t.buildEqualityValue(vi1, 5); // v1 == 5
        Node vi2eq6 = t.buildEqualityValue(vi2, 6); // v2 == 6
        Node vi2eq8 = t.buildEqualityValue(vi2, 8); // v2 == 8 new value
        Node vi3eq7 = t.buildEqualityValue(vi3, 7); // v3 == 7
        Node vi4eq8 = t.buildEqualityValue(vi4, 8); // v4 == 8
        Node vi5eq9 = t.buildEqualityValue(vi5, 9); // v5 == 9

        for (int useV1 = 0; useV1 < 2; useV1++) {
            for (int useV2 = 0; useV2 < 2; useV2++) {
                for (int useV3 = 0; useV3 < 2; useV3++) {
                    for (int useV4 = 0; useV4 < 2; useV4++) {
                        for (int useV5 = 0; useV5 < 2; useV5++) {
                            Node n = Tree.ONE;
                            Node m = Tree.ONE;
                            if (useV5 == 1) {
                                n = t.conjunct(n, vi5eq9);
                                m = t.conjunct(m, vi5eq9);
                            }
                            if (useV4 == 1) {
                                n = t.conjunct(n, vi4eq8);
                                m = t.conjunct(m, vi2eq8);
                            }
                            if (useV3 == 1) {
                                n = t.conjunct(n, vi3eq7);
                                m = t.conjunct(m, vi3eq7);
                            }
                            if (useV2 == 1) {
                                n = t.conjunct(n, vi2eq6);
                                /* Is created from v4. */
                            }
                            if (useV1 == 1) {
                                n = t.conjunct(n, vi1eq5);
                                m = t.conjunct(m, vi1eq5);
                            }
                            if (DEBUG) {
                                System.out.printf("Input node:\n%s\n", t.dumpGraph(n));
                                System.out.printf("Expected output node:\n%s\n", t.dumpGraph(m));
                            }
                            Node p = t.replace(n, vi2, vi4);
                            if (DEBUG) {
                                System.out.printf("Real output node:\n%s\n", t.dumpGraph(p));
                                System.out.printf("\n\n");
                            }
                            assertSame(p, m);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void replaceNewOldTest() {
        final boolean DEBUG = false;

        SimpleVariable v1 = new SimpleVariable("v1", 5, 5); // int[5..9] v1
        SimpleVariable v2 = new SimpleVariable("v2", 5, 5); // int[5..9] v2
        SimpleVariable v3 = new SimpleVariable("v3", 5, 5); // int[5..9] v3
        SimpleVariable v4 = new SimpleVariable("v4", 5, 5); // int[5..9] v4
        SimpleVariable v5 = new SimpleVariable("v5", 5, 5); // int[5..9] v4
        SimpleVarInfoBuilder svb2 = new SimpleVarInfoBuilder(1);
        svb2.addVariable(v1);
        svb2.addVariable(v2);
        svb2.addVariable(v3);
        svb2.addVariable(v4);
        svb2.addVariable(v5);
        Tree t = new Tree();
        VarInfo vi1 = svb2.getVarInfo(v1, 0);
        VarInfo vi2 = svb2.getVarInfo(v2, 0); // old var
        VarInfo vi3 = svb2.getVarInfo(v3, 0);
        VarInfo vi4 = svb2.getVarInfo(v4, 0); // new var
        VarInfo vi5 = svb2.getVarInfo(v5, 0);

        Node vi1eq5 = t.buildEqualityValue(vi1, 5); // v1 == 5
        Node vi2eq6 = t.buildEqualityValue(vi2, 6); // v2 == 6
        Node vi3eq7 = t.buildEqualityValue(vi3, 7); // v3 == 7
        Node vi4eq6 = t.buildEqualityValue(vi4, 6); // v4 == 6 new value
        Node vi4eq8 = t.buildEqualityValue(vi4, 8); // v4 == 8
        Node vi5eq9 = t.buildEqualityValue(vi5, 9); // v5 == 9

        for (int useV1 = 0; useV1 < 2; useV1++) {
            for (int useV2 = 0; useV2 < 2; useV2++) {
                for (int useV3 = 0; useV3 < 2; useV3++) {
                    for (int useV4 = 0; useV4 < 2; useV4++) {
                        for (int useV5 = 0; useV5 < 2; useV5++) {
                            Node n = Tree.ONE;
                            Node m = Tree.ONE;
                            if (useV5 == 1) {
                                n = t.conjunct(n, vi5eq9);
                                m = t.conjunct(m, vi5eq9);
                            }
                            if (useV4 == 1) {
                                n = t.conjunct(n, vi4eq8);
                                /* Is created from v2. */
                            }
                            if (useV3 == 1) {
                                n = t.conjunct(n, vi3eq7);
                                m = t.conjunct(m, vi3eq7);
                            }
                            if (useV2 == 1) {
                                n = t.conjunct(n, vi2eq6);
                                m = t.conjunct(m, vi4eq6);
                            }
                            if (useV1 == 1) {
                                n = t.conjunct(n, vi1eq5);
                                m = t.conjunct(m, vi1eq5);
                            }
                            if (DEBUG) {
                                System.out.printf("Input node:\n%s\n", t.dumpGraph(n));
                                System.out.printf("Expected output node:\n%s\n", t.dumpGraph(m));
                            }
                            Node p = t.replace(n, vi4, vi2);
                            if (DEBUG) {
                                System.out.printf("Real output node:\n%s\n", t.dumpGraph(p));
                                System.out.printf("\n\n");
                            }
                            assertSame(p, m);
                        }
                    }
                }
            }
        }
    }
}
