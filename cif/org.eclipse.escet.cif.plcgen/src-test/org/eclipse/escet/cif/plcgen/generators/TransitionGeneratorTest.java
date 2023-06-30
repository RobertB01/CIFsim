//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Supplier;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.WarnOutput;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionAutomaton;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionEdge;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for the event transitions generator. */
@SuppressWarnings("javadoc")
public class TransitionGeneratorTest {
    private static class TestPlcTarget extends PlcBaseTarget {
        public TestPlcTarget() {
            super(PlcTargetType.IEC_61131_3);
            // Configure the target.
            String projectName = "projName";
            String configurationName = "confName";
            String resourceName = "resName";
            String plcTaskName = "taskName";
            int taskCyceTime = 1;
            int priority = 1;
            String inputPath = "input/path";
            String outputPath = "/output/path";
            PlcNumberBits intSize = PlcNumberBits.BITS_32;
            PlcNumberBits realSize = PlcNumberBits.BITS_64;
            boolean simplifyValues = false;
            ConvertEnums enumConversion = ConvertEnums.NO;
            Supplier<Boolean> shouldTerminate = () -> false;
            boolean warnOnRename = false;
            WarnOutput warnOutput = message -> { /* Do nothing. */ };

            PlcGenSettings settings = new PlcGenSettings(projectName, configurationName, resourceName, plcTaskName,
                    taskCyceTime, priority, inputPath, "/" + inputPath, outputPath, intSize, realSize, simplifyValues,
                    enumConversion, shouldTerminate, warnOnRename, warnOutput);
            setup(settings);

            // Setup the generators, part of PlcBaseTarget.generate() originally.
            nameGenerator = new DefaultNameGenerator(settings);
            codeStorage = new PlcCodeStorage(this, settings);
            typeGenerator = new DefaultTypeGenerator(this, settings);
            varStorage = new DefaultVariableStorage(this);
            cifProcessor = new CifProcessor(this, settings);
            transitionGenerator = new DefaultTransitionGenerator(this);
        }

        @Override
        public boolean supportsArrays() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public boolean supportsConstants() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public boolean supportsEnumerations() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public String getPathSuffixReplacement() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        protected Writer getPlcCodeWriter() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        protected int getMaxIntegerTypeSize() {
            return 64;
        }

        @Override
        protected int getMaxRealTypeSize() {
            return 64;
        }
    }

    /** PLC target used in the tests. */
    private TestPlcTarget target;

    /** Generator instance under test. */
    private DefaultTransitionGenerator transitionGenerator;

    private DiscVariable otherVar = newDiscVariable("otherVar", null, newIntType(), null);

    private Specification spec = newSpecification(null, List.of(otherVar), null, null, null, null, null, null,
            "spec", null);

    @BeforeEach
    public void setup() {
        target = new TestPlcTarget();
        target.getVarStorage().addStateVariable(otherVar, otherVar.getType());
        transitionGenerator = new DefaultTransitionGenerator(target);
    }

    @Test
    public void testCreateTransitionGenerator() {
        transitionGenerator.setTransitions(List.of());
        transitionGenerator.generate();
        assertTrue(true);
    }

    @Test
    /** Two syncers test. */
    public void testTwoSyncers() {
        Event event = newEvent(null, "event", null, null);
        spec.getDeclarations().add(event);

        // automaton syncer1: edge event when otherVar = 1 do otherVar := 2;
        Expression leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        Expression rightSide = newIntExpression(null, newIntType(), 1);
        Expression guard1 = newBinaryExpression(leftSide, BinaryOperator.EQUAL, null, rightSide, newBoolType());
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 2);
        Update update1 = newAssignment(leftSide, null, rightSide);
        TransitionEdge edge11 = new TransitionEdge(null, null, null, List.of(guard1), List.of(update1));

        // automaton syncer1: edge event when otherVar = 2 do otherVar := 3;
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 2);
        Expression guard2 = newBinaryExpression(leftSide, BinaryOperator.EQUAL, null, rightSide, newBoolType());
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 3);
        Update update2 = newAssignment(leftSide, null, rightSide);
        TransitionEdge edge21 = new TransitionEdge(null, null, null, List.of(guard2), List.of(update2));
        TransitionAutomaton syncer1 = new TransitionAutomaton(null, List.of(edge11, edge21));

        // automaton syncer2: edge event when otherVar = 3 do otherVar := 4;
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 3);
        Expression guard3 = newBinaryExpression(leftSide, BinaryOperator.EQUAL, null, rightSide, newBoolType());
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 4);
        Update update3 = newAssignment(leftSide, null, rightSide);
        TransitionEdge edge12 = new TransitionEdge(null, null, null, List.of(guard3), List.of(update3));
        TransitionAutomaton syncer2 = new TransitionAutomaton(null, List.of(edge12));

        CifEventTransition transition = new CifEventTransition(event, List.of(), List.of(), List.of(syncer1, syncer2),
                List.of());

        // Generate the transition, and check that it matches expectations.
        transitionGenerator.setTransitions(List.of(transition));
        List<PlcStatement> code = transitionGenerator.generateCode();
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (* Try to perform event "event". *)
                isFeasible := TRUE;
                IF otherVar = 1 THEN
                    syncAutEdge := 1;
                ELSIF otherVar = 2 THEN
                    syncAutEdge := 2;
                ELSE
                    isFeasible := FALSE;
                END_IF;
                IF isFeasible THEN
                    IF otherVar = 3 THEN
                        syncAutEdge__1 := 1;
                    ELSE
                        isFeasible := FALSE;
                    END_IF;
                END_IF;
                IF isFeasible THEN
                    isProgress := TRUE;
                    IF syncAutEdge = 1 THEN
                        otherVar := 2;
                    ELSIF syncAutEdge = 2 THEN
                        otherVar := 3;
                    END_IF;
                    IF syncAutEdge__1 = 1 THEN
                        otherVar := 4;
                    END_IF;
                END_IF;""";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void multiAssignUnfoldTest() {
        Event event = newEvent(null, "event", null, null);
        spec.getDeclarations().add(event);

        // Pairwise assignment.
        // edge event do otherVar, otherVar := 1, 2;

        // Left tuple type.
        List<Field> fields1 = List.of(newField("f1", null, newIntType()), newField("f2", null, newIntType()));
        TupleType tupleType1 = newTupleType(fields1, null);

        // Right tuple type.
        List<Field> fields2 = List.of(newField("r1", null, newIntType()), newField("r2", null, newIntType()));
        TupleType tupleType2 = newTupleType(fields2, null);
        Expression leftSide = newTupleExpression(List.of(newDiscVariableExpression(null, newIntType(), otherVar),
                newDiscVariableExpression(null, newIntType(), otherVar)), null, tupleType1);
        Expression rightSide = newTupleExpression(List.of(newIntExpression(null, newIntType(), 1),
                                                          newIntExpression(null, newIntType(), 2)), null, tupleType2);

        // Build the transition.
        Update update = newAssignment(leftSide, null, rightSide);
        TransitionEdge edge = new TransitionEdge(null, null, null, List.of(), List.of(update));
        TransitionAutomaton aut = new TransitionAutomaton(null, List.of(edge));

        CifEventTransition transition = new CifEventTransition(event, List.of(), List.of(), List.of(aut),
                List.of());

        // Generate the transition, and check that it matches expectations.
        transitionGenerator.setTransitions(List.of(transition));
        List<PlcStatement> code = transitionGenerator.generateCode();
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (* Try to perform event "event". *)
                isFeasible := TRUE;
                IF TRUE THEN
                    syncAutEdge := 1;
                ELSE
                    isFeasible := FALSE;
                END_IF;
                IF isFeasible THEN
                    isProgress := TRUE;
                    IF syncAutEdge = 1 THEN
                        otherVar := 1;
                        otherVar := 2;
                    END_IF;
                END_IF;""";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void multiAssignProjectTest() {
        Event event = newEvent(null, "event", null, null);
        spec.getDeclarations().add(event);

        // Unpack "true" tuple.
        // edge event do otherVar, otherVar := true;

        List<Field> fields1 = List.of(newField("f1", null, newIntType()), newField("f2", null, newIntType()));
        TupleType tupleType1 = newTupleType(fields1, null);
        Expression leftSide = newTupleExpression(List.of(newDiscVariableExpression(null, newIntType(), otherVar),
                newDiscVariableExpression(null, newIntType(), otherVar)), null, tupleType1);
        Expression rightSide = newBoolExpression(null, newBoolType(), true);
        Update update = newAssignment(leftSide, null, rightSide);
        TransitionEdge edge = new TransitionEdge(null, null, null, List.of(), List.of(update));
        TransitionAutomaton aut = new TransitionAutomaton(null, List.of(edge));

        CifEventTransition transition = new CifEventTransition(event, List.of(), List.of(), List.of(aut),
                List.of());

        // Generate the transition, and check that it matches expectations.
        transitionGenerator.setTransitions(List.of(transition));
        List<PlcStatement> code = transitionGenerator.generateCode();
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (* Try to perform event "event". *)
                isFeasible := TRUE;
                IF TRUE THEN
                    syncAutEdge := 1;
                ELSE
                    isFeasible := FALSE;
                END_IF;
                IF isFeasible THEN
                    isProgress := TRUE;
                    IF syncAutEdge = 1 THEN
                        rightValue := TRUE;
                        otherVar := rightValue.field1;
                        otherVar := rightValue.field2;
                    END_IF;
                END_IF;""";
        assertEquals(expectedText, actualText);
    }
}
