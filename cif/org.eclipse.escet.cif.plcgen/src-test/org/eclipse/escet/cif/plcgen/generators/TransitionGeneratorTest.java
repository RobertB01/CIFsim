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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newReceivedExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Supplier;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.plcgen.PathPair;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransAutPurpose;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionAutomaton;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionEdge;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.eclipse.escet.common.java.output.BlackHoleOutputProvider;
import org.eclipse.escet.common.java.output.WarnOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for the event transitions generator. */
@SuppressWarnings("javadoc")
public class TransitionGeneratorTest {
    private static class TestPlcTarget extends PlcBaseTarget {
        public TestPlcTarget() {
            super(PlcTargetType.IEC_61131_3, ConvertEnums.KEEP);
            // Configure the target.
            String projectName = "projName";
            String configurationName = "confName";
            String resourceName = "resName";
            String plcTaskName = "taskName";
            int taskCyceTime = 1;
            int priority = 1;
            String inputPath = "input/path";
            String outputPath = "output/path";
            String ioTablePath = "io/path";
            PlcNumberBits intSize = PlcNumberBits.BITS_32;
            PlcNumberBits realSize = PlcNumberBits.BITS_64;
            boolean simplifyValues = false;
            ConvertEnums enumConversion = ConvertEnums.KEEP;
            Supplier<Boolean> shouldTerminate = () -> false;
            boolean warnOnRename = false;
            WarnOutput warnOutput = new BlackHoleOutputProvider().getWarnOutput();

            PlcGenSettings settings = new PlcGenSettings(projectName, configurationName, resourceName, plcTaskName,
                    taskCyceTime, priority, null, null, new PathPair(inputPath, "/" + inputPath),
                    new PathPair(outputPath, "/" + outputPath), new PathPair(ioTablePath, "/" + ioTablePath), intSize,
                    realSize, simplifyValues, enumConversion, shouldTerminate, warnOnRename, warnOutput);
            setup(settings);

            // Setup the generators, part of PlcBaseTarget.generate() normally.
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

    /** Variable for receivers to receive channel values. */
    private DiscVariable recVar = newDiscVariable(null, "recVar", null, newIntType(), null);

    private DiscVariable otherVar = newDiscVariable(null, "otherVar", null, newIntType(), null);

    private Specification spec = newSpecification(null, null, List.of(recVar, otherVar), null, null, null, null, null,
            null, "specification", null);

    private PlcDataVariable isProgressVar = new PlcDataVariable("isProgress", PlcElementaryType.BOOL_TYPE);

    @BeforeEach
    public void setup() {
        target = new TestPlcTarget();
        target.getVarStorage().addStateVariable(recVar, recVar.getType());
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
    public void testSingleUnconditionalSender() {
        Event event = newEvent(null, true, "sendEvent", null, newIntType());
        spec.getDeclarations().add(event);

        // Create sender edge.
        // edge sendEvent!1;
        Location aut1Loc = newLocation();
        aut1Loc.setName("aut1Loc");
        TransitionEdge sendEdge1 = new TransitionEdge(1, aut1Loc, null, newIntExpression(null, newIntType(), 1),
                List.of(), List.of());
        List<TransitionEdge> transSendEdges = List.of(sendEdge1);

        // Create sender automaton.
        Automaton aut1 = newAutomaton();
        aut1.setName("aut1");
        aut1.getLocations().add(aut1Loc);
        spec.getComponents().add(aut1);
        TransitionAutomaton sender1 = new TransitionAutomaton(aut1, TransAutPurpose.SENDER, transSendEdges);
        CifEventTransition transition = new CifEventTransition(event, List.of(sender1), List.of(), List.of(),
                List.of());

        // Generate the transition, and check that it matches expectations.
        List<PlcStatement> code = transitionGenerator.generateCode(isProgressVar, List.of(transition));
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (*************************************************************
                 * Try to perform event "sendEvent".
                 *
                 * - One automaton must send a value.
                 *    - Automaton "aut1" may send a value.
                 *
                 * - An automaton that must receive a value is missing, so the event cannot occur.
                 *************************************************************)
                eventEnabled := TRUE;
                (*******************************
                 * Try to find a sender automaton that provides a value.
                 *******************************)
                senderAut := 0;
                IF senderAut = 0 THEN
                    (***********
                     * Testing edge of automaton "aut1" to provide a value for the channel for event "sendEvent".
                     * At least one sending automaton must have an edge with a true guard to allow the event.
                     *
                     * Edge being tested:
                     * - Location "aut1Loc":
                     *   - 1st edge in the location
                     ***********)
                    IF TRUE THEN
                        senderAut := 1;
                        edge_aut1_1 := 1;
                    END_IF;
                END_IF;
                IF senderAut = 0 THEN
                    (* Failed to find an automaton that provides a value. Skip to the next event. *)
                    eventEnabled := FALSE;
                END_IF;
                (*******************************
                 * Try to find a receiver automaton that accepts a value.
                 *******************************)
                IF eventEnabled THEN
                    receiverAut := 0;
                    IF receiverAut = 0 THEN
                        (* Failed to find an automaton that accepts a value. Skip to the next event. *)
                        eventEnabled := FALSE;
                    END_IF;
                END_IF;
                (* All checks have been done. If variable "eventEnabled" still holds, event "sendEvent" can occur. *)
                IF eventEnabled THEN
                    isProgress := TRUE;
                    (*******************************
                     * Store the provided value and perform assignments of the selected providing automaton.
                     *******************************)
                    IF senderAut = 1 THEN
                        (* Automaton "aut1" was selected. *)
                        IF edge_aut1_1 = 1 THEN
                            channelValue := 1;
                        END_IF;
                    END_IF;
                    (*******************************
                     * Deliver the provided value and perform assignments of the selected accepting automaton.
                     *******************************)
                END_IF;""";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testChannelOneSenderTwoReceivers() {
        Event event = newEvent(null, true, "channelEvent", null, newIntType());
        spec.getDeclarations().add(event);

        // automaton sender1: edge channelEvent!1;
        Location sender1Loc = newLocation();
        sender1Loc.setName("sender1Loc");
        TransitionEdge sendEdge1 = new TransitionEdge(2, sender1Loc, null, newIntExpression(null, newIntType(), 1),
                List.of(), List.of());
        List<TransitionEdge> transSendEdges = List.of(sendEdge1);
        Automaton aut1 = newAutomaton();
        aut1.setName("sender1");
        aut1.getLocations().add(sender1Loc);
        spec.getComponents().add(aut1);
        TransitionAutomaton sender1 = new TransitionAutomaton(aut1, TransAutPurpose.SENDER, transSendEdges);

        // automaton receiver1: edge channelEvent do recvVar := recvVar + ?;
        Expression leftAdd = newDiscVariableExpression(null, newIntType(), recVar);
        Expression rightAdd = newReceivedExpression();
        Expression addExpr = newBinaryExpression(leftAdd, BinaryOperator.ADDITION, null, rightAdd, newIntType());
        Update recvUpd1 = newAssignment(newDiscVariableExpression(null, newIntType(), recVar), null, addExpr);
        Location receiver1Loc = newLocation();
        receiver1Loc.setName("receiver1Loc");
        TransitionEdge recvEdge1 = new TransitionEdge(3, receiver1Loc, null, null, List.of(), List.of(recvUpd1));
        Automaton aut2 = newAutomaton();
        aut2.setName("receiver1");
        aut2.getLocations().add(receiver1Loc);
        spec.getComponents().add(aut2);
        TransitionAutomaton receiver1 = new TransitionAutomaton(aut2, TransAutPurpose.RECEIVER, List.of(recvEdge1));

        // automaton receiver2: edge channelEvent do recvVar := ?;
        Update recvUpd2 = newAssignment(newDiscVariableExpression(null, newIntType(), otherVar), null,
                newReceivedExpression());
        Location receiver2Loc = newLocation();
        receiver2Loc.setName("receiver2Loc");
        TransitionEdge recvEdge2 = new TransitionEdge(4, receiver2Loc, null, null, List.of(), List.of(recvUpd2));
        Automaton aut3 = newAutomaton();
        aut3.setName("receiver2");
        aut3.getLocations().add(receiver2Loc);
        spec.getComponents().add(aut3);
        TransitionAutomaton receiver2 = new TransitionAutomaton(aut3, TransAutPurpose.RECEIVER, List.of(recvEdge2));

        CifEventTransition transition = new CifEventTransition(event, List.of(sender1), List.of(receiver1, receiver2),
                List.of(), List.of());

        // Generate the transition, and check that it matches expectations.
        List<PlcStatement> code = transitionGenerator.generateCode(isProgressVar, List.of(transition));
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (*************************************************************
                 * Try to perform event "channelEvent".
                 *
                 * - One automaton must send a value.
                 *    - Automaton "sender1" may send a value.
                 *
                 * - One automaton must receive a value.
                 *    - Automaton "receiver1" may receive a value.
                 *    - Automaton "receiver2" may receive a value.
                 *************************************************************)
                eventEnabled := TRUE;
                (*******************************
                 * Try to find a sender automaton that provides a value.
                 *******************************)
                senderAut := 0;
                IF senderAut = 0 THEN
                    (***********
                     * Testing edge of automaton "sender1" to provide a value for the channel for event "channelEvent".
                     * At least one sending automaton must have an edge with a true guard to allow the event.
                     *
                     * Edge being tested:
                     * - Location "sender1Loc":
                     *   - 2nd edge in the location
                     ***********)
                    IF TRUE THEN
                        senderAut := 1;
                        edge_sender1_1 := 1;
                    END_IF;
                END_IF;
                IF senderAut = 0 THEN
                    (* Failed to find an automaton that provides a value. Skip to the next event. *)
                    eventEnabled := FALSE;
                END_IF;
                (*******************************
                 * Try to find a receiver automaton that accepts a value.
                 *******************************)
                IF eventEnabled THEN
                    receiverAut := 0;
                    IF receiverAut = 0 THEN
                        (***********
                         * Testing edge of automaton "receiver1" to accept a value from the channel for event "channelEvent".
                         * At least one receiving automaton must have an edge with a true guard to allow the event.
                         *
                         * Edge being tested:
                         * - Location "receiver1Loc":
                         *   - 3rd edge in the location
                         ***********)
                        IF TRUE THEN
                            receiverAut := 1;
                            edge_receiver1_1 := 1;
                        END_IF;
                    END_IF;
                    IF receiverAut = 0 THEN
                        (***********
                         * Testing edge of automaton "receiver2" to accept a value from the channel for event "channelEvent".
                         * At least one receiving automaton must have an edge with a true guard to allow the event.
                         *
                         * Edge being tested:
                         * - Location "receiver2Loc":
                         *   - 4th edge in the location
                         ***********)
                        IF TRUE THEN
                            receiverAut := 2;
                            edge_receiver2_1 := 1;
                        END_IF;
                    END_IF;
                    IF receiverAut = 0 THEN
                        (* Failed to find an automaton that accepts a value. Skip to the next event. *)
                        eventEnabled := FALSE;
                    END_IF;
                END_IF;
                (* All checks have been done. If variable "eventEnabled" still holds, event "channelEvent" can occur. *)
                IF eventEnabled THEN
                    isProgress := TRUE;
                    (* Make temporary copies of assigned variables to preserve the old values while assigning new values. *)
                    current_otherVar := otherVar;
                    current_recVar := recVar;
                    (*******************************
                     * Store the provided value and perform assignments of the selected providing automaton.
                     *******************************)
                    IF senderAut = 1 THEN
                        (* Automaton "sender1" was selected. *)
                        IF edge_sender1_1 = 1 THEN
                            channelValue := 1;
                        END_IF;
                    END_IF;
                    (*******************************
                     * Deliver the provided value and perform assignments of the selected accepting automaton.
                     *******************************)
                    IF receiverAut = 1 THEN
                        (* Automaton "receiver1" was selected. *)
                        IF edge_receiver1_1 = 1 THEN
                            (* Perform assignments of the 3rd edge in location "receiver1.receiver1Loc". *)
                            recVar := current_recVar + channelValue;
                        END_IF;
                    ELSIF receiverAut = 2 THEN
                        (* Automaton "receiver2" was selected. *)
                        IF edge_receiver2_1 = 1 THEN
                            (* Perform assignments of the 4th edge in location "receiver2.receiver2Loc". *)
                            otherVar := channelValue;
                        END_IF;
                    END_IF;
                END_IF;""";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testTwoSyncers() {
        Event event = newEvent(null, null, "event", null, null);
        spec.getDeclarations().add(event);

        // automaton syncer1: edge event when otherVar = 1 do otherVar := 2;
        Expression leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        Expression rightSide = newIntExpression(null, newIntType(), 1);
        Expression guard1 = newBinaryExpression(leftSide, BinaryOperator.EQUAL, null, rightSide, newBoolType());
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 2);
        Update update1 = newAssignment(leftSide, null, rightSide);
        Location syncer1Loc = newLocation();
        syncer1Loc.setName("syncer1Loc");
        TransitionEdge edge11 = new TransitionEdge(5, syncer1Loc, null, null, List.of(guard1), List.of(update1));

        // automaton syncer1: edge event when otherVar = 2 do otherVar := 3;
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 2);
        Expression guard2 = newBinaryExpression(leftSide, BinaryOperator.EQUAL, null, rightSide, newBoolType());
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 3);
        Update update2 = newAssignment(leftSide, null, rightSide);
        TransitionEdge edge21 = new TransitionEdge(6, syncer1Loc, null, null, List.of(guard2), List.of(update2));
        Automaton aut1 = newAutomaton();
        aut1.setName("syncer1");
        aut1.getLocations().add(syncer1Loc);
        spec.getComponents().add(aut1);
        TransitionAutomaton syncer1 = new TransitionAutomaton(aut1, TransAutPurpose.SYNCER, List.of(edge11, edge21));

        // automaton syncer2: edge event when otherVar = 3 do otherVar := 4;
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 3);
        Expression guard3 = newBinaryExpression(leftSide, BinaryOperator.EQUAL, null, rightSide, newBoolType());
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 4);
        Update update3 = newAssignment(leftSide, null, rightSide);
        Location syncer2Loc = newLocation();
        syncer2Loc.setName("syncer2Loc");
        TransitionEdge edge12 = new TransitionEdge(7, syncer2Loc, null, null, List.of(guard3), List.of(update3));
        Automaton aut2 = newAutomaton();
        aut2.setName("syncer2");
        aut2.getLocations().add(syncer2Loc);
        spec.getComponents().add(aut2);
        TransitionAutomaton syncer2 = new TransitionAutomaton(aut2, TransAutPurpose.SYNCER, List.of(edge12));

        CifEventTransition transition = new CifEventTransition(event, List.of(), List.of(), List.of(syncer1, syncer2),
                List.of());

        // Generate the transition, and check that it matches expectations.
        List<PlcStatement> code = transitionGenerator.generateCode(isProgressVar, List.of(transition));
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (*************************************************************
                 * Try to perform event "event".
                 *
                 * - Automaton "syncer1" must always synchronize.
                 * - Automaton "syncer2" must always synchronize.
                 *************************************************************)
                eventEnabled := TRUE;
                (*******************************
                 * Check each synchronizing automaton for having an edge with a true guard.
                 *******************************)
                (***********
                 * Testing edges of automaton "syncer1" to synchronize for event "event".
                 * This automaton must have an edge with a true guard to allow the event.
                 *
                 * Edges being tested:
                 * - Location "syncer1Loc":
                 *   - 5th edge in the location
                 *   - 6th edge in the location
                 ***********)
                IF otherVar = 1 THEN
                    edge_syncer1_1 := 1;
                ELSIF otherVar = 2 THEN
                    edge_syncer1_1 := 2;
                ELSE
                    (* The automaton has no edge with a true guard. Skip to the next event. *)
                    eventEnabled := FALSE;
                END_IF;
                IF eventEnabled THEN
                    (***********
                     * Testing edge of automaton "syncer2" to synchronize for event "event".
                     * This automaton must have an edge with a true guard to allow the event.
                     *
                     * Edge being tested:
                     * - Location "syncer2Loc":
                     *   - 7th edge in the location
                     ***********)
                    IF otherVar = 3 THEN
                        edge_syncer2_1 := 1;
                    ELSE
                        (* The automaton has no edge with a true guard. Skip to the next event. *)
                        eventEnabled := FALSE;
                    END_IF;
                END_IF;
                (* All checks have been done. If variable "eventEnabled" still holds, event "event" can occur. *)
                IF eventEnabled THEN
                    isProgress := TRUE;
                    (* Make temporary copies of assigned variables to preserve the old values while assigning new values. *)
                    current_otherVar := otherVar;
                    (*******************************
                     * Perform the assignments of each synchronizing automaton.
                     *******************************)
                    (* Perform assignments of automaton "syncer1". *)
                    IF edge_syncer1_1 = 1 THEN
                        (* Perform assignments of the 5th edge in location "syncer1.syncer1Loc". *)
                        otherVar := 2;
                    ELSIF edge_syncer1_1 = 2 THEN
                        (* Perform assignments of the 6th edge in location "syncer1.syncer1Loc". *)
                        otherVar := 3;
                    END_IF;
                    (* Perform assignments of automaton "syncer2". *)
                    IF edge_syncer2_1 = 1 THEN
                        (* Perform assignments of the 7th edge in location "syncer2.syncer2Loc". *)
                        otherVar := 4;
                    END_IF;
                END_IF;""";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testOneMonitor() {
        Event event = newEvent(null, null, "event", null, null);
        spec.getDeclarations().add(event);

        // automaton monitor: edge event when otherVar = 1 do otherVar := 2;
        Expression leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        Expression rightSide = newIntExpression(null, newIntType(), 1);
        Expression guard1 = newBinaryExpression(leftSide, BinaryOperator.EQUAL, null, rightSide, newBoolType());
        leftSide = newDiscVariableExpression(null, newIntType(), otherVar);
        rightSide = newIntExpression(null, newIntType(), 2);
        Update update1 = newAssignment(leftSide, null, rightSide);
        Location aut2Loc = newLocation();
        aut2Loc.setName("aut2Loc");
        TransitionEdge edge = new TransitionEdge(8, aut2Loc, null, null, List.of(guard1), List.of(update1));
        Automaton aut2 = newAutomaton();
        aut2.setName("monitor");
        aut2.getLocations().add(aut2Loc);
        spec.getComponents().add(aut2);
        TransitionAutomaton monitor = new TransitionAutomaton(aut2, TransAutPurpose.MONITOR, List.of(edge));

        CifEventTransition transition = new CifEventTransition(event, List.of(), List.of(), List.of(),
                List.of(monitor));

        // Generate the transition, and check that it matches expectations.
        List<PlcStatement> code = transitionGenerator.generateCode(isProgressVar, List.of(transition));
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (*************************************************************
                 * Try to perform event "event".
                 *
                 * - Automaton "monitor" may synchronize.
                 *************************************************************)
                eventEnabled := TRUE;
                (* All checks have been done. If variable "eventEnabled" still holds, event "event" can occur. *)
                IF eventEnabled THEN
                    isProgress := TRUE;
                    (* Make temporary copies of assigned variables to preserve the old values while assigning new values. *)
                    current_otherVar := otherVar;
                    (*******************************
                     * Perform the assignments of each optionally synchronizing automaton.
                     *******************************)
                    (* Perform assignments of automaton "monitor". *)
                    IF current_otherVar = 1 THEN
                        (* Perform assignments of the 8th edge in location "monitor.aut2Loc". *)
                        otherVar := 2;
                    END_IF;
                END_IF;""";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testMultiAssignUnfold() {
        Event event = newEvent(null, null, "event", null, null);
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
        Expression rightSide = newTupleExpression(
                List.of(newIntExpression(null, newIntType(), 1), newIntExpression(null, newIntType(), 2)), null,
                tupleType2);

        // Build the transition.
        Update update = newAssignment(leftSide, null, rightSide);
        Location aut2Loc = newLocation();
        aut2Loc.setName("aut2Loc");
        TransitionEdge edge = new TransitionEdge(9, aut2Loc, null, null, List.of(), List.of(update));
        Automaton aut2 = newAutomaton();
        aut2.getLocations().add(aut2Loc);
        aut2.setName("aut");
        spec.getComponents().add(aut2);
        TransitionAutomaton aut = new TransitionAutomaton(aut2, TransAutPurpose.SYNCER, List.of(edge));

        CifEventTransition transition = new CifEventTransition(event, List.of(), List.of(), List.of(aut), List.of());

        // Generate the transition, and check that it matches expectations.
        List<PlcStatement> code = transitionGenerator.generateCode(isProgressVar, List.of(transition));
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (*************************************************************
                 * Try to perform event "event".
                 *
                 * - Automaton "aut" must always synchronize.
                 *************************************************************)
                eventEnabled := TRUE;
                (*******************************
                 * Check each synchronizing automaton for having an edge with a true guard.
                 *******************************)
                (***********
                 * Testing edge of automaton "aut" to synchronize for event "event".
                 * This automaton must have an edge with a true guard to allow the event.
                 *
                 * Edge being tested:
                 * - Location "aut2Loc":
                 *   - 9th edge in the location
                 ***********)
                IF TRUE THEN
                    edge_aut_1 := 1;
                ELSE
                    (* The automaton has no edge with a true guard. Skip to the next event. *)
                    eventEnabled := FALSE;
                END_IF;
                (* All checks have been done. If variable "eventEnabled" still holds, event "event" can occur. *)
                IF eventEnabled THEN
                    isProgress := TRUE;
                    (* Make temporary copies of assigned variables to preserve the old values while assigning new values. *)
                    current_otherVar := otherVar;
                    (*******************************
                     * Perform the assignments of each synchronizing automaton.
                     *******************************)
                    (* Perform assignments of automaton "aut". *)
                    IF edge_aut_1 = 1 THEN
                        (* Perform assignments of the 9th edge in location "aut.aut2Loc". *)
                        otherVar := 1;
                        otherVar := 2;
                    END_IF;
                END_IF;""";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testMultiAssignProject() {
        Event event = newEvent(null, null, "event", null, null);
        spec.getDeclarations().add(event);

        // Unpack "true" tuple.
        // edge event do otherVar, otherVar := true;

        List<Field> fields1 = List.of(newField("f1", null, newIntType()), newField("f2", null, newIntType()));
        TupleType tupleType1 = newTupleType(fields1, null);
        Expression leftSide = newTupleExpression(List.of(newDiscVariableExpression(null, newIntType(), otherVar),
                newDiscVariableExpression(null, newIntType(), otherVar)), null, tupleType1);
        Expression rightSide = newBoolExpression(null, newBoolType(), true);
        Update update = newAssignment(leftSide, null, rightSide);
        Location aut2Loc = newLocation();
        aut2Loc.setName("autLoc");
        TransitionEdge edge = new TransitionEdge(10, aut2Loc, null, null, List.of(), List.of(update));
        Automaton aut2 = newAutomaton();
        aut2.setName("aut");
        aut2.getLocations().add(aut2Loc);
        spec.getComponents().add(aut2);
        TransitionAutomaton aut = new TransitionAutomaton(aut2, TransAutPurpose.SYNCER, List.of(edge));

        CifEventTransition transition = new CifEventTransition(event, List.of(), List.of(), List.of(aut), List.of());

        // Generate the transition, and check that it matches expectations.
        List<PlcStatement> code = transitionGenerator.generateCode(isProgressVar, List.of(transition));
        ModelTextGenerator textGen = new ModelTextGenerator();
        String actualText = textGen.toString(code, "noPou", true);
        String expectedText = """
                (*************************************************************
                 * Try to perform event "event".
                 *
                 * - Automaton "aut" must always synchronize.
                 *************************************************************)
                eventEnabled := TRUE;
                (*******************************
                 * Check each synchronizing automaton for having an edge with a true guard.
                 *******************************)
                (***********
                 * Testing edge of automaton "aut" to synchronize for event "event".
                 * This automaton must have an edge with a true guard to allow the event.
                 *
                 * Edge being tested:
                 * - Location "autLoc":
                 *   - 10th edge in the location
                 ***********)
                IF TRUE THEN
                    edge_aut_1 := 1;
                ELSE
                    (* The automaton has no edge with a true guard. Skip to the next event. *)
                    eventEnabled := FALSE;
                END_IF;
                (* All checks have been done. If variable "eventEnabled" still holds, event "event" can occur. *)
                IF eventEnabled THEN
                    isProgress := TRUE;
                    (* Make temporary copies of assigned variables to preserve the old values while assigning new values. *)
                    current_otherVar := otherVar;
                    (*******************************
                     * Perform the assignments of each synchronizing automaton.
                     *******************************)
                    (* Perform assignments of automaton "aut". *)
                    IF edge_aut_1 = 1 THEN
                        (* Perform assignments of the 10th edge in location "aut.autLoc". *)
                        rightValue := TRUE;
                        otherVar := rightValue.field1;
                        otherVar := rightValue.field2;
                    END_IF;
                END_IF;""";
        assertEquals(expectedText, actualText);
    }
}
