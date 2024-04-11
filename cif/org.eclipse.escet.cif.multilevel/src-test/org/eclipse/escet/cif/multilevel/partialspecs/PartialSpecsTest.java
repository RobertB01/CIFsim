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

package org.eclipse.escet.cif.multilevel.partialspecs;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newGroup;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInvariant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.emf.EMFPrettyPrinter.printPrettyTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
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

    /** Requirement linking a discrete variable to a location, forcing both to be replaced by an input variable. */
    @Test
    public void testToInputVar() {
        // automaton x:
        //   location Y;
        // group Z:
        //   disc bool b;
        // invariant Y = b;  <-- Element to copy to the partial specification.
        Specification spec = newSpecification();

        Automaton x = newAutomaton();
        spec.getComponents().add(x);
        x.setName("X");
        Location y = newLocation();
        y.setName("Y");
        x.getLocations().add(y);

        Group z = newGroup();
        spec.getComponents().add(z);
        z.setName("Z");
        DiscVariable b = newDiscVariable();
        b.setName("b");
        b.setType(newBoolType());
        z.getDeclarations().add(b);

        Invariant i = newInvariant();
        spec.getInvariants().add(i);
        Expression yRef = newLocationExpression(y, null, newBoolType());
        Expression bRef = newDiscVariableExpression(null, newBoolType(), b);
        i.setPredicate(newBinaryExpression(yRef, BinaryOperator.EQUAL, null, bRef, newBoolType()));

        // Construct partial specification.
        PartialSpecsBuilder partialBuilder = new PartialSpecsBuilder(spec);
        Specification partialSpec = partialBuilder.createPartialSpecification(List.of(i));

        // Dump the copied tree and compare.
        String actual = printPrettyTree(partialSpec);
        String expected = """
                [1] Specification:
                  - Reference List<Component> components = [<<2>>, <<3>>]
                  - Reference List<Invariant> invariants = [<<4>>]

                [2] Group:
                  - Reference List<Declaration> declarations = [<<5>>]
                  - Attribute CifIdentifier name = X

                [3] Group:
                  - Reference List<Declaration> declarations = [<<6>>]
                  - Attribute CifIdentifier name = Z

                [4] Invariant:
                  - Attribute InvKind invKind = State
                  - Reference Expression predicate = <<7>>
                  - Attribute SupKind supKind = None

                [5] InputVariable:
                  - Attribute CifIdentifier name = Y
                  - Reference CifType type = <<8>>

                [6] InputVariable:
                  - Attribute CifIdentifier name = b
                  - Reference CifType type = <<9>>

                [7] BinaryExpression:
                  - Reference Expression left = <<10>>
                  - Attribute BinaryOperator operator = Equal
                  - Reference Expression right = <<11>>
                  - Reference CifType type = <<12>>

                [8] BoolType:

                [9] BoolType:

                [10] InputVariableExpression:
                  - Reference CifType type = <<13>>
                  - Reference InputVariable variable = <<5>>

                [11] InputVariableExpression:
                  - Reference CifType type = <<14>>
                  - Reference InputVariable variable = <<6>>

                [12] BoolType:

                [13] BoolType:

                [14] BoolType:
                """;
        assertEquals(expected, actual);
    }
}
