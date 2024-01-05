//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.subplant;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.junit.jupiter.api.Test;

/** Tests for creating partial specifications. */
@SuppressWarnings("javadoc")
public class PartialSpecsTest {
    private final DiscVariable varA = newDiscVariable(null, "a", null, null, null);

    private final DiscVariable varB = newDiscVariable(null, "b", null, null, null);

    private final DiscVariable varC = newDiscVariable(null, "c", null, null, null);

    private final DiscVariable varD = newDiscVariable(null, "d", null, null, null);

    private final DiscVariable varCa = newDiscVariable(null, "c_a", null, null, null);

    private final DiscVariable varCb = newDiscVariable(null, "c_b", null, null, null);

    private final DiscVariable varCc = newDiscVariable(null, "c_c", null, null, null);

    private final DiscVariable varCd = newDiscVariable(null, "c_d", null, null, null);

    private final Map<DiscVariable, DiscVariable> origsToCopies = Map.of(varA, varCa, varB, varCb, varC, varCc, varD,
            varCd);

    private void doTest(List<DiscVariable> varsToAdd) {
        doTest(varsToAdd, false);
    }

    private void doTest(List<DiscVariable> varsToAdd, boolean dropVarCb) {
        // Construct original specification.
        Specification origSpec = newSpecification();
        // Borrow the declarations list for defining the desired order.
        origSpec.getDeclarations().addAll(List.of(varA, varB, varC, varD));

        // Make a partial manager, and prepare it for testing the 'insertEObject' function.
        PartialSpecManager mgr = new PartialSpecManager(origSpec);
        Specification partialSpec = mgr.getPartialSpec();
        mgr.addCopiedObject(varA, varCa);
        if (!dropVarCb) {
            mgr.addCopiedObject(varB, varCb);
        }
        mgr.addCopiedObject(varC, varCc);
        mgr.addCopiedObject(varD, varCd);

        // Add all partial objects in the defined order.
        for (DiscVariable varToAdd: varsToAdd) {
            mgr.insertEObject(origSpec.getDeclarations(), partialSpec.getDeclarations(), origsToCopies.get(varToAdd));
        }

        // Verify the expected order in the partial list.
        int index = 0;
        if (varsToAdd.contains(varA)) {
            assertSame(varCa, partialSpec.getDeclarations().get(index));
            index++;
        }
        if (varsToAdd.contains(varB)) {
            assertSame(varCb, partialSpec.getDeclarations().get(index));
            index++;
        }
        if (varsToAdd.contains(varC)) {
            assertSame(varCc, partialSpec.getDeclarations().get(index));
            index++;
        }
        if (varsToAdd.contains(varD)) {
            assertSame(varCd, partialSpec.getDeclarations().get(index));
            index++;
        }
        assertEquals(index, partialSpec.getDeclarations().size()); // Number of values should also match.
    }

    @Test
    public void testSingleValue() {
        doTest(List.of(varA));
        doTest(List.of(varB));
        doTest(List.of(varC));
        doTest(List.of(varD));
    }

    @Test
    public void testDoubleValues() {
        doTest(List.of(varA, varB));
        doTest(List.of(varA, varC));
        doTest(List.of(varA, varD));
        doTest(List.of(varB, varA));
        doTest(List.of(varB, varC));
        doTest(List.of(varB, varD));
        doTest(List.of(varC, varA));
        doTest(List.of(varC, varB));
        doTest(List.of(varC, varD));
        doTest(List.of(varD, varA));
        doTest(List.of(varD, varB));
        doTest(List.of(varD, varC));
    }

    @Test
    public void testTripleValues() {
        doTest(List.of(varA, varB, varC));
        doTest(List.of(varA, varB, varD));
        doTest(List.of(varA, varC, varB));
        doTest(List.of(varA, varC, varD));
        doTest(List.of(varA, varD, varB));
        doTest(List.of(varA, varD, varC));
        doTest(List.of(varB, varA, varC));
        doTest(List.of(varB, varA, varD));
        doTest(List.of(varB, varC, varA));
        doTest(List.of(varB, varC, varD));
        doTest(List.of(varB, varD, varA));
        doTest(List.of(varB, varD, varC));
        doTest(List.of(varC, varA, varB));
        doTest(List.of(varC, varA, varD));
        doTest(List.of(varC, varB, varA));
        doTest(List.of(varC, varB, varD));
        doTest(List.of(varC, varD, varA));
        doTest(List.of(varC, varD, varB));
        doTest(List.of(varD, varA, varB));
        doTest(List.of(varD, varA, varC));
        doTest(List.of(varD, varB, varA));
        doTest(List.of(varD, varB, varC));
        doTest(List.of(varD, varC, varA));
        doTest(List.of(varD, varC, varB));
    }

    @Test
    public void testQuadrupleValues() {
        doTest(List.of(varA, varB, varC, varD));
        doTest(List.of(varA, varB, varD, varC));
        doTest(List.of(varA, varC, varB, varD));
        doTest(List.of(varA, varC, varD, varB));
        doTest(List.of(varA, varD, varB, varC));
        doTest(List.of(varA, varD, varC, varB));
        doTest(List.of(varB, varA, varC, varD));
        doTest(List.of(varB, varA, varD, varC));
        doTest(List.of(varB, varC, varA, varD));
        doTest(List.of(varB, varC, varD, varA));
        doTest(List.of(varB, varD, varA, varC));
        doTest(List.of(varB, varD, varC, varA));
        doTest(List.of(varC, varA, varB, varD));
        doTest(List.of(varC, varA, varD, varB));
        doTest(List.of(varC, varB, varA, varD));
        doTest(List.of(varC, varB, varD, varA));
        doTest(List.of(varC, varD, varA, varB));
        doTest(List.of(varC, varD, varB, varA));
        doTest(List.of(varD, varA, varB, varC));
        doTest(List.of(varD, varA, varC, varB));
        doTest(List.of(varD, varB, varA, varC));
        doTest(List.of(varD, varB, varC, varA));
        doTest(List.of(varD, varC, varA, varB));
        doTest(List.of(varD, varC, varB, varA));
    }

    @Test
    void testDuplicate() {
        assertThrows(AssertionError.class, () -> doTest(List.of(varA, varA)));
    }

    @Test
    public void testDroppedB() {
        doTest(List.of(varA, varC, varD), true);
        doTest(List.of(varA, varD, varC), true);
        doTest(List.of(varC, varA, varD), true);
        doTest(List.of(varC, varD, varA), true);
        doTest(List.of(varD, varA, varC), true);
        doTest(List.of(varD, varC, varA), true);
    }
}
