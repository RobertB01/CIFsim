//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.varorder.helper;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisInputVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.junit.Test;

/** Tests for {@link VarOrdererHelper}. */
public class VarOrdererHelperTest {
    @SuppressWarnings("javadoc")
    @Test
    public void testReorder() {
        // Create CIF specification.
        Specification spec = newSpecification();
        InputVariable va = newInputVariable("a", null, newIntType(0, null, 0));
        InputVariable vb = newInputVariable("b", null, newIntType(0, null, 0));
        InputVariable vc = newInputVariable("c", null, newIntType(0, null, 0));
        InputVariable vd = newInputVariable("d", null, newIntType(0, null, 0));
        InputVariable ve = newInputVariable("e", null, newIntType(0, null, 0));
        spec.getDeclarations().add(va);
        spec.getDeclarations().add(vb);
        spec.getDeclarations().add(vc);
        spec.getDeclarations().add(vd);
        spec.getDeclarations().add(ve);

        // Create synthesis variables.
        SynthesisVariable a = new SynthesisInputVariable(va, newIntType(0, null, 0), 1, 0, 0);
        SynthesisVariable b = new SynthesisInputVariable(vb, newIntType(0, null, 0), 1, 0, 0);
        SynthesisVariable c = new SynthesisInputVariable(vc, newIntType(0, null, 0), 1, 0, 0);
        SynthesisVariable d = new SynthesisInputVariable(vd, newIntType(0, null, 0), 1, 0, 0);
        SynthesisVariable e = new SynthesisInputVariable(ve, newIntType(0, null, 0), 1, 0, 0);
        SynthesisVariable[] variables = {a, b, c, d, e};

        // Reorder the variables.
        int[] newIndices = {0, 4, 1, 2, 3}; // For each variable in 'variables', its new 0-based index.
        VarOrdererHelper helper = new VarOrdererHelper(spec, variables);
        List<SynthesisVariable> ordered = helper.reorderForNewIndices(newIndices);

        // Check the result. Invariant: ordered[newIndices[i]] == variables[i].
        assertSame(a, ordered.get(0));
        assertSame(c, ordered.get(1));
        assertSame(d, ordered.get(2));
        assertSame(e, ordered.get(3));
        assertSame(b, ordered.get(4));
    }
}
