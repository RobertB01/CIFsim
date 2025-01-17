//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.bdd.workset.dependencies;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.bdd.conversion.CifToBddConverter;
import org.eclipse.escet.cif.bdd.settings.CifBddSettings;
import org.eclipse.escet.cif.bdd.settings.EdgeGranularity;
import org.eclipse.escet.cif.bdd.settings.ExplorationStrategy;
import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.java.BitSets;
import org.eclipse.escet.common.java.Lists;
import org.junit.jupiter.api.Test;

import com.github.javabdd.BDDFactory;
import com.github.javabdd.JFactory;

/** Tests for {@link BddBasedEdgeDependencySetCreator}. */
public class BddBasedEdgeDependencySetCreatorTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testNoEdges() {
        test("""
                plant p:
                  location:
                    initial;
                end
                """, "", false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEventsUnused() {
        test("""
                controllable e;
                plant p:
                  location:
                    initial;
                end
                """, "", false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEventsSingleSelfLoop() {
        test("""
                controllable e;
                plant p:
                  location:
                    initial;
                    edge e;
                end
                """, ".", false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEventsSingleAut() {
        test("""
                plant p:
                  controllable a, b, c, d, e;
                  location l1:
                    initial;
                    edge b goto l3;
                    edge e;
                  location l2:
                    edge d goto l4;
                  location l3:
                    edge e goto l2;
                  location l4:
                    edge a goto l5;
                  location l5:
                    edge c goto l1;
                end
                """, """
                ..1..
                ....1
                .1..1
                1....
                .1.1.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEventsTwoAutsSync() {
        test("""
                controllable a1, a2, b, c1, c2;
                plant p1:
                  location l1:
                    initial;
                    edge a1 goto l2;
                  location l2:
                    edge b goto l3;
                  location l3:
                    edge c1 goto l1;
                end
                plant p2:
                  location l1:
                    initial;
                    edge a2 goto l2;
                  location l2:
                    edge b goto l3;
                  location l3:
                    edge c2 goto l1;
                end
                """, """
                .11.1
                1.11.
                ...11
                11..1
                11.1.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEventsMonitor() {
        test("""
                controllable a, b, c;
                plant pa:
                  monitor b;
                  location l1:
                    initial;
                    edge a goto l2;
                  location l2:
                    edge b goto l3;
                  location l3:
                    edge c goto l1;
                end
                """, """
                .1.
                1.1
                11.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnGuardDiffVarSameValue() {
        test("""
                controllable a, b;
                plant pa:
                  disc int[0..5] va;
                  disc int[0..5] vb;
                  location:
                    initial;
                    edge a do va := 1;
                    edge b when vb = 1;
                end
                """, """
                .1
                1.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnGuardSameVarAndValue() {
        test("""
                controllable a, b;
                plant p:
                  disc int[0..5] v;
                  location:
                    initial;
                    edge a do v := 1;
                    edge b when v = 1;
                end
                """, """
                .1
                1.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnGuardSameVarDifferentValue() {
        test("""
                controllable a, b;
                plant p:
                  disc int[0..5] v;
                  location:
                    initial;
                    edge a do v := 1;
                    edge b when v = 2;
                end
                """, """
                ..
                1.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnGuardSameVarEqualValue() {
        test("""
                controllable a, b;
                plant p:
                  disc int[0..5] v;
                  location:
                    initial;
                    edge a do v := 1;
                    edge b when v = v;
                end
                """, """
                .1
                1.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnGuardSameVarNotValue() {
        test("""
                controllable a, b, c;
                plant p:
                  disc int[0..5] v;
                  location:
                    initial;
                    edge a do v := 1;
                    edge b when      v = 1;
                    edge c when not (v = 1);
                end
                """, """
                .1.
                1..
                1..
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnGuardSameVarOrValue() {
        test("""
                controllable a, b, c, d;
                plant p:
                  disc int[0..5] v;
                  location:
                    initial;
                    edge a do v := 1;
                    edge b when v = 0 or v = 1;
                    edge c when v = 1 or v = 2;
                    edge d when v = 0 or v = 3;
                end
                """, """
                .11.
                1.11
                11..
                11..
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnTwoValueGuardOneValue() {
        test("""
                controllable a, b, c;
                plant p:
                  disc int[0..5] v;
                  location:
                    initial;
                    edge a do v := 1;
                    edge b do v := 2;
                    edge c when v = 1;
                end
                """, """
                .11
                1..
                11.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnIncrGuardsSingleValue() {
        test("""
                controllable a, b, c, d, e, f, g;
                plant p:
                  disc int[0..3] v;
                  location:
                    initial;
                    edge a do v := v + 1;
                    edge b when v = 0;
                    edge c when v = 1;
                    edge d when v = 2;
                    edge e when v = 3;
                    edge f when v = 4;
                    edge g when v = 5;
                end
                """, """
                ..111..
                1......
                1......
                1......
                .......
                .......
                .......
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsAsgnGuardedIncrGuardsSingleValue() {
        test("""
                controllable a, b, c, d, e;
                plant p:
                  disc int[0..3] v;
                  location:
                    initial;
                    edge a when v = 1 do v := v + 1;
                    edge b when v = 0;
                    edge c when v = 1;
                    edge d when v = 2;
                    edge e when v = 3;
                end
                """, """
                ...1.
                .....
                1....
                .....
                .....
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsGuardAlgVar() {
        test("""
                controllable a, b, c;
                plant p:
                  disc int[0..5] v;
                  alg bool aa = v = 1;
                  location:
                    initial;
                    edge a do v := 0;
                    edge b do v := 1;
                    edge c when aa;
                end
                """, """
                .1.
                1.1
                11.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsGuardLocRef() {
        test("""
                controllable a, b, c;
                plant p1:
                  location l1:
                    initial;
                    edge a goto l2;
                  location l2:
                    initial;
                    edge b goto l1;
                end
                plant p2:
                  location:
                    initial;
                    edge c when p1.l1;
                end
                """, """
                .1.
                1.1
                1..
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVarsInputVar() {
        test("""
                controllable a;
                input bool i;
                plant pa:
                  location:
                    initial;
                    edge a when i;
                end
                """, """
                .1
                1.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEventsAndVars() {
        test("""
                controllable a, b, c, d, e;
                plant pa:
                  disc int[0..5] va;
                  location l1:
                    initial;
                    edge a when pb.vb = 5 goto l2;
                  location l2:
                    edge b when pb.vb = 4 goto l1;
                    edge c do va := va + 1 goto l1;
                end
                plant pb:
                  disc int[0..5] vb;
                  location l1:
                    initial;
                    edge d when pa.va = 0 goto l2;
                  location l2:
                    edge e when pa.va = 1 do vb := 5 goto l1;
                end
                """, """
                ..111
                ...11
                1...1
                111..
                1.1..
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEventsAndVarsAndSelfLoop() {
        test("""
                controllable a, b, c, d, e, f, g, h, i, j, k, l;
                plant pa:
                  location l1:
                    initial;
                    edge a goto l2;
                    edge b goto l3;
                  location l2:
                    edge c goto l4;
                  location l3:
                    edge d goto l4;
                    edge e goto l4;
                  location l4:
                    edge f;
                    edge g goto l5;
                    edge h goto l6;
                    edge i goto l6;
                  location l5:
                    edge j goto l7;
                  location l6:
                    edge k goto l7;
                  location l7;
                end
                plant pb:
                  location:
                    initial;
                    edge l;
                end
                requirement l needs pa.l4;
                """, """
                ..1........1
                ...11......1
                .....1111..1
                .....1111..1
                .....1111..1
                ......111..1
                .........1.1
                ..........11
                ..........11
                ...........1
                ...........1
                11111111111.
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOrderedEdgesNormalOrder() {
        test("""
                controllable a, b, c;
                plant p:
                  location l1:
                    initial;
                    edge a goto l2;
                  location l2:
                    edge b goto l3;
                  location l3:
                    edge c goto l1;
                end
                """, """
                .1.
                ..1
                1..
                """, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOrderedEdgesReverseOrder() {
        test("""
                controllable a, b, c;
                plant p:
                  location l1:
                    initial;
                    edge a goto l2;
                  location l2:
                    edge b goto l3;
                  location l3:
                    edge c goto l1;
                end
                """, """
                ..1
                1..
                .1.
                """, true);
    }

    /**
     * Perform a test.
     *
     * @param specTxt The CIF specification in textual syntax.
     * @param expectedFwdDeps The expected forward dependencies.
     * @param reverseEdges Whether to reverse the edges in the CIF/BDD specification.
     */
    private void test(String specTxt, String expectedFwdDeps, boolean reverseEdges) {
        // Read the CIF specification.
        CifReader reader = new CifReader();
        reader.suppressWarnings = true;
        reader.init("memory", "/memory", false);
        Specification spec = reader.read(specTxt);

        // Get settings.
        CifBddSettings settings = new CifBddSettings();
        settings.setEdgeGranularity(EdgeGranularity.PER_EVENT);
        settings.setExplorationStrategy(ExplorationStrategy.CHAINING_WORKSET);
        settings.setEdgeOrderBackward("sorted");
        settings.setEdgeOrderForward("sorted");

        // Convert to BDDs.
        BDDFactory factory = JFactory.init(100, 100);
        CifBddSpec cifBddSpec = new CifToBddConverter("Test").convert(spec, settings, factory);
        for (CifBddEdge edge: cifBddSpec.edges) {
            edge.initApply();
        }

        // Reverse the ordered edges, if requested.
        if (reverseEdges) {
            cifBddSpec.orderedEdgesBackward = Lists.reverse(cifBddSpec.orderedEdgesBackward);
            cifBddSpec.orderedEdgesForward = Lists.reverse(cifBddSpec.orderedEdgesForward);
        }

        // Compute dependency sets.
        EdgeDependencySetCreator creator = new BddBasedEdgeDependencySetCreator();
        creator.createAndStore(cifBddSpec, true);

        // Check result.
        int edgeCnt = cifBddSpec.edges.size();
        String expectedBwdDeps = invertDeps(expectedFwdDeps);
        String actualFwdDeps = cifBddSpec.worksetDependenciesForward.stream().map(b -> BitSets.bitsetToStr(b, edgeCnt))
                .collect(Collectors.joining("\n"));
        String actualBwdDeps = cifBddSpec.worksetDependenciesBackward.stream().map(b -> BitSets.bitsetToStr(b, edgeCnt))
                .collect(Collectors.joining("\n"));
        assertEquals(expectedFwdDeps.trim(), actualFwdDeps.trim());
        assertEquals(expectedBwdDeps.trim(), actualBwdDeps.trim());
    }

    /**
     * Invert dependencies.
     *
     * @param deps The dependencies.
     * @return The inverted dependencies.
     */
    private String invertDeps(String deps) {
        String[][] matrix = Arrays.stream(deps.split("\n")).map(x -> x.split("")).toList().toArray(new String[0][0]);
        String[][] inverseMatrix = new String[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                inverseMatrix[i][j] = matrix[j][i];
            }
        }
        return Arrays.stream(inverseMatrix).map(a -> String.join("", a)).collect(Collectors.joining("\n"));
    }
}
