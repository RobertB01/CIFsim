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

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Lists.cast;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;
import java.util.function.Supplier;

import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprAddressableResult;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprValueResult;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionAutomaton;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionEdge;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcStructProjection;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement.PlcSelectChoice;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;

/** Generator for creating PLC code to perform CIF event transitions in the PLC. */
public class DefaultTransitionGenerator implements TransitionGenerator {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /**
     * The event transitions to generate. Is {@code null} until the transitions are provided with
     * {@link #setTransitions}.
     */
    private List<CifEventTransition> eventTransitions = null;

    /** Expression generator for the main program. */
    private ExprGenerator mainExprGen;

    /** Generation of standard PLC functions. */
    private PlcFunctionAppls funcAppls;

    /**
     * Constructor of the {@link DefaultTransitionGenerator} class.
     *
     * @param target PLC target to generate code for.
     */
    public DefaultTransitionGenerator(PlcTarget target) {
        this.target = target;
    }

    @Override
    public void setTransitions(List<CifEventTransition> eventTransitions) {
        this.eventTransitions = eventTransitions;
    }

    @Override
    public void generate() {
        List<PlcStatement> statements = generateCode();
        target.getCodeStorage().addEventTransitions(statements);
    }

    /**
     * Generate PLC code for all given event transitions.
     *
     * <p>
     * The high-level entry point to generate event transition code for all events. The high level view of the code is:
     * <pre>
     * isProgress := FALSE;
     *
     * isFeasible := &lt;test-if-eventA-is-enabled&gt;;
     * IF isFeasible THEN
     *     isProgress := TRUE;
     *     &lt;perform-transition-of-eventA&gt;
     * END_IF;
     *
     * isFeasible := &lt;test-if-eventB-is-enabled&gt;;
     * IF isFeasible THEN
     *     isProgress := TRUE;
     *     &lt;perform-transition-of-eventB&gt;
     * END_IF;
     *
     * ... // Other transitions are omitted.
     * </pre> More details on the implementation of testing and performing can be found in the
     * {@link #generateEventTransition} method.
     * </p>
     *
     * @return The generated PLC event transition code.
     */
    List<PlcStatement> generateCode() {
        mainExprGen = target.getCodeStorage().getExprGenerator();
        funcAppls = new PlcFunctionAppls(target);

        PlcVariable isProgressVar = target.getCodeStorage().getIsProgressVariable();

        // As all transition code is generated in main program context, only one generated statements list exists and
        // various variables that store decisions in the process can be re-used between different events.
        List<PlcStatement> statements = list();
        for (CifEventTransition eventTransition: eventTransitions) {
            statements.addAll(generateEventTransition(eventTransition, isProgressVar));
        }
        mainExprGen.releaseTempVariable(isProgressVar);
        return statements;
    }

    /**
     * Generate PLC code for testing and performing of the provided event transition.
     *
     * <p>
     * The most elaborate case in code generation of testing and performing a transition is with a channel event. That
     * is, an event that also transfers data from a sender to a receiver automaton. An automaton with the event
     * participates in the transitions of that event in one of the following forms:
     * <ul>
     * <li>As a sender by providing a value for the channel,</li>
     * <li>As a receiver by receiving the value of the channel,</li>
     * <li>As a syncer by synchronizing on the event without sending or receiving a value, or</li>
     * <li>As a monitor by synchronizing on the event without sending or receiving a value if the automaton can (and
     * ignoring the event while not blocking its occurrence) if it has no enabled edge for it.</li>
     * </ul>
     * Each form can have several automata.
     * </p>
     *
     * <p>
     * Generated code for an event transition contains test code and perform code for each of the available forms,
     * except that monitors test code is always empty since they do not decide whether an event is feasible. Also if an
     * event is not a channel there are no senders and receivers, and thus the test and perform code of those is empty
     * in that case.
     * </p>
     *
     * <p>
     * The general structure of code of an event transition is: <pre>
     * isFeasible := TRUE;
     *
     * &lt;find-a-sender-automaton-with-enabled-edge&gt;
     * IF NOT found THEN isFeasible := FALSE; END_IF;
     *
     * IF isFeasible THEN
     *     &lt;find-a-receiver-automaton-with-enabled-edge&gt;
     *     IF NOT found THEN isFeasible := FALSE; END_IF;
     * END_IF;
     *
     * IF isFeasible THEN
     *     &lt;find-enabled-edge-for-syncer-automaton-1&gt;
     *     IF NOT found THEN isFeasible := FALSE; END_IF;
     * END_IF;
     * IF isFeasible THEN
     *     &lt;find-enabled-edge-for-syncer-automaton-2&gt;
     *     IF NOT found THEN isFeasible := FALSE; END_IF;
     * END_IF;
     * ... // Test code of other syncer-automata omitted.
     *
     * IF isFeasible THEN
     *     isProgress := TRUE;
     *
     *     &lt;perform-edge-of-the-found-sender&gt;
     *
     *     &lt;perform-edge-of-the-found-receiver&gt;
     *
     *     &lt;perform-edge-of-sync-automaton-1&gt;
     *     &lt;perform-edge-of-sync-automaton-2&gt;
     *     ... // Perform code of other syncer-automata omitted.
     *
     *     &lt;try-to-find-and-perform-edge-of-monitor-automaton-1&gt;
     *     &lt;try-to-find-and-perform-edge-of-monitor-automaton-2&gt;
     *     ... // Try to perform code of other monitor-automata omitted.
     * END_IF;
     * </pre>
     * <ul>
     * <li>Details of test code and perform code for senders and receivers can be found at
     * {@link #generateSendReceiveCode},</li>
     * <li>Details of test code and perform code for syncers can be found at {@link #generateSyncCode}, and</li>
     * <li>Details of (try to) perform code for monitors can be found at {@link #generateSyncCode}.</li>
     * </ul>
     * </p>
     *
     * @param eventTransition Event transition to generate code for.
     * @param isProgressVar PLC variable to set if the event transition is performed.
     * @return The generated code for testing and performing the event in the PLC.
     */
    private List<PlcStatement> generateEventTransition(CifEventTransition eventTransition, PlcVariable isProgressVar) {
        List<PlcStatement> testCode = list(); // Code that decides whether the event can be performed.
        List<PlcStatement> performCode = list(); // Code that performs the event it it can be performed.
        List<PlcVariable> createdTempVariables = list();

        // Obtain and initialize the feasibility flag.
        PlcVariable isFeasibleVar = mainExprGen.getTempVariable("isFeasible", PlcElementaryType.BOOL_TYPE);
        createdTempVariables.add(isFeasibleVar);

        // So far, no test code has been generated that may set 'isFeasible' to false.
        boolean testFeasibilityAlwaysHolds = true;

        String absEventnName = getAbsName(eventTransition.event, false);
        testCode.add(new PlcCommentLine("Try to perform event \"" + absEventnName + "\"."));
        testCode.add(new PlcAssignmentStatement(isFeasibleVar, new PlcBoolLiteral(true)));

        // Performing the event implies progress is made.
        performCode.add(new PlcAssignmentStatement(isProgressVar, new PlcBoolLiteral(true)));

        // TODO Handle senders. (comes in a next commit).
        // TODO Handle receivers. (comes in a next commit).

        // Handle syncers.
        generateSyncCode(eventTransition.syncers, testCode, performCode, createdTempVariables,
                isFeasibleVar, testFeasibilityAlwaysHolds);

        // Handle monitors. Only generates perform code since it doesn't influence feasibility of the event transition.
        generateMonitorCode(eventTransition.monitors, performCode, createdTempVariables);

        // Construct the complete PLC code for the event transition by concatenating test and perform code.
        // isFeasible = TRUE; <testCode>; if (isFeasible) THEN progress := TRUE; <performCode>; END_IF
        PlcExpression guard = new PlcVarExpression(isFeasibleVar);
        testCode.add(generateIfGuardThenCode(guard, performCode));

        mainExprGen.releaseTempVariables(createdTempVariables);
        return testCode;
    }

    /**
     * // Function code will be added in a next commit. You may want to skip reading this until then.
     * Generate code to test and perform a transition for a sender or a receiver automaton.
     *
     * <p>
     * The objective of test code for both senders and receivers is to find an automaton with an enabled edge. In the
     * perform code the enabled edge is then taken. For communication from test code to perform code, a
     * {@code senderAut} respectively {@code receiverAut} variable exists to capture the found automata, and a
     * {@code senderEdge} respectively {@code reciverEdge} variable exists to capture the found edge within the found
     * automata.
     * </p>
     * <p>
     * For the senders automata the generated test code looks like <pre>
     * senderAut := 0;
     *
     * IF &lt;edge-1-of-sender-1-is-enabled&gt; THEN senderAut := 1; senderEdge := 1;
     * ELSE IF &lt;edge-2-of-sender-1-is-enabled&gt; THEN senderAut := 1; senderEdge := 2;
     * ... // Other edge tests of sender-1 omitted.
     *
     * IF senderAut == 0 THEN
     *     IF &lt;edge-1-of-sender-2-is-enabled&gt; THEN senderAut := 2; senderEdge := 1;
     *     ELSE IF &lt;edge-2-of-sender-2-is-enabled&gt; THEN senderAut := 2; senderEdge := 2;
     *     ... // Other edge tests of sender-2 omitted.
     * END_IF;
     *
     * // Test code of other sender automata omitted.
     *
     * IF senderAut == 0 THEN
     *     isFeasible := FALSE;
     * END_IF;
     * </pre> The receivers test code uses the {@code receiverAut} and {@code reciverEdge} variables and tests for
     * feasibility first, but looks the same otherwise.
     * </p>
     *
     * <p>
     * The perform code is a selection statement to select on the value of the {@code senderAut} respectively
     * {@code receiverAut} variable. Within each branch, a selection is done on the value of the {@code senderEdge}
     * respectively {@code receiverEdge} variable. Once arrived at the chosen edge of the chosen automaton, the sender
     * computes the sent value and stores it into a (type-specific) {@code channelValue} variable and then performs the
     * updates of its selected edge. The chosen edge of the receiver only performs its updates, as usage of the sent
     * value is "hidden" in them.
     * </p>
     *
     * @param autTransitions Automaton transitions to convert.
     * @param testCode Storage for generated test code. Is updated in-place.
     * @param performCode Storage for generated perform code. Is updated in-place.
     * @param varPrefix Prefix of locally created temporary variables.
     * @param createdTempVariables Tracking storage of created temporary variables.
     * @param isFeasibleVar PLC variable expressing if the event is feasible.
     * @param channelValueVar PLC variable storing the channel value. Is {@code null} when not sending a value (either
     *     receiving a value or it is a void channel).
     * @param testFeasibilityAlwaysHolds Whether it is known that 'isFeasibility' in tests is always true at runtime.
     */
    private void generateSendReceiveCode(List<TransitionAutomaton> autTransitions, List<PlcStatement> testCode,
            List<PlcStatement> performCode, String varPrefix,
            List<PlcVariable> createdTempVariables, PlcVariable isFeasibleVar, PlcVariable channelValueVar,
            boolean testFeasibilityAlwaysHolds)
    {
        // Added now to make the {@link } happy, content comes in a next commit.
    }

    /**
     * Generate code to test and perform a transition for syncer automata.
     *
     * <p>
     * Each syncer automaton of the event must have an enabled edge, thus each syncer automaton has its own
     * {@code autEdge} variable for storing its selected edge. Also, failing to find an enabled edge for a syncer
     * automaton immediately blocks the entire event from happening and thus immediately modifies {@code isFeasible} as
     * the last {@code ELSE} block in finding an enabled edge.
     * </p>
     *
     * <p>
     * This leads to test code like <pre>
     * IF isFeasible THEN
     *     IF &lt;edge-1-of-syncer-1-is-enabled&gt; THEN aut1Edge := 1;
     *     ELSE IF &lt;edge-2-of-syncer-1-is-enabled&gt; THEN aut1Edge := 2;
     *     ... // Other edge tests of syncer-1 omitted.
     *     ELSE isFeasible := FALSE
     *     END_IF;
     * END_IF;
     *
     * // Test code for other syncer automata omitted.
     * </pre> Performing the chosen edge is implemented by first selecting on the value of an {@code autEdge} variable.
     * Within that branch, the updates of the chosen edge are then performed.
     * </p>
     *
     * @param autTransitions Automaton transitions to convert.
     * @param testCode Storage for generated test code. Is updated in-place.
     * @param performCode Storage for generated perform code. Is updated in-place.
     * @param createdTempVariables Tracking storage of created temporary variables.
     * @param isFeasibleVar PLC variable expressing if the event is feasible.
     * @param testFeasibilityAlwaysHolds Whether it is known that 'isFeasibility' in tests is always true at runtime.
     */
    private void generateSyncCode(List<TransitionAutomaton> autTransitions, List<PlcStatement> testCode,
            List<PlcStatement> performCode, List<PlcVariable> createdTempVariables,
            PlcVariable isFeasibleVar, boolean testFeasibilityAlwaysHolds)
    {
        // Generate code that tests and performs synchronizing on the event.
        for (TransitionAutomaton transAut: autTransitions) {
            List<PlcStatement> autTestCode = list(); // Intermediate Storage for test code of the automaton.

            // Each automaton has an edge variable storing the selected edge of that automaton during testing.
            // TODO: Use a smaller integer for automaton and edge indexing.
            PlcVariable autEdgeVar = mainExprGen.getTempVariable("syncAutEdge", PlcElementaryType.DINT_TYPE);

            // Generate edge testing code.
            autTestCode.addAll(generateEdgesTestCode(transAut, -1, null, autEdgeVar, isFeasibleVar));

            // Generate the edge selection and performing code, and add it as a branch on the automaton to
            // 'performSelectStat'.
            performCode.addAll(generateAutPerformCode(transAut, autEdgeVar, null));

            // If feasibility is known to hold, the generated test code can be used as-is. Otherwise, running the
            // generated test code only makes sense after verifying that feasibility is still true.
            if (testFeasibilityAlwaysHolds) {
                testCode.addAll(autTestCode);
            } else {
                PlcExpression guard = new PlcVarExpression(isFeasibleVar);
                testCode.add(generateIfGuardThenCode(guard, autTestCode));
            }
            testFeasibilityAlwaysHolds = false;
        }
    }

    /**
     * Generate code to test and perform a transition for a monitor automaton.
     *
     * <p>
     * As monitor automata do not influence feasibility of an event, there is no test code to construct. Instead,
     * finding an enabled edge and performing it is combined in the perform code. It generates the following for each
     * monitor automaton: <pre>
     * IF isFeasible THEN
     *     IF &lt;edge-1-of-monitor-is-enabled&gt; THEN
     *         &lt;perform-edge-1-of-monitor&gt;
     *     ELSE IF &lt;edge-2-of-monitor-is-enabled&gt; THEN
     *         &lt;perform-edge-2-of-monitor&gt;
     *     ... // Other tests are omitted,
     *     END_IF;
     * END_IF;
     * </pre> Note that if no edge is enabled, no updates are performed.
     * </p>
     *
     * @param autTransitions Automaton transitions to convert.
     * @param testAndPerformCode Storage for generated test and perform code. Is updated in-place.
     * @param createdTempVariables Tracking storage of created temporary variables.
     */
    private void generateMonitorCode(List<TransitionAutomaton> autTransitions,
            List<PlcStatement> testAndPerformCode, List<PlcVariable> createdTempVariables)
    {
        // TODO: Do not allow only monitors for an event, as it may completely disable progress of time.

        for (TransitionAutomaton transAut: autTransitions) {
            PlcSelectionStatement selStat = null;

            for (TransitionEdge edge: transAut.transitionEdges) {
                if (!edge.updates.isEmpty()) {
                    Supplier<List<PlcStatement>> thenStats = () -> { return generateUpdates(edge.updates); };
                    // Add an "IF <guards> THEN <perform-updates>" branch.
                    selStat = mainExprGen.addBranch(edge.guards, thenStats, selStat, testAndPerformCode);
                }
            }
        }
    }

    /**
     * Generate PLC code that tests for an enabled edge in the given automaton.
     *
     * <p>
     * The edge test code generator is shared between syncers and senders/receivers automata. The different behavior is
     * controlled by means of the {@code autVar} variable.
     * <ul><li>If it is {@code null}, a test for a syncer automaton should be generated, the automaton must always have
     * an enabled edge or the transition is not feasible.</li>
     * <li>If is not {@code null}, a test is generated for a receiver or sender automaton. The {@code autVar} contains
     * the elected automaton (with {@code 0} meaning that no automaton has been selected yet). In this case an
     * additional test for {@code autVar} being {@code 0} is generated as additional pre-condition.</li></ul>
     * </p>
     *
     * <p>
     * If an enabled edge is found at PLC runtime, the code sets {@code autVar} to {@code autIndex} if
     * {@code autVar} is not {@code null}, and sets {@code edgeVar} to the 1-based index of the edge that is found to be
     * enabled.
     * </p>
     *
     * @param transAut Automaton to generate test code for.
     * @param autIndex Automaton index that indicates the given automaton.
     * @param autVar PLC variable that stores the automaton found while testing for enabled edges.
     * @param edgeVar PLC variable that stores the edge in the automaton indicated by {@code autVar}. Is 1-based to be
     *     consistent in how numbers are assigned.
     * @param isFeasibleVar PLC variable expressing if the event is feasible.
     * @return The generated code.
     */
    private List<PlcStatement> generateEdgesTestCode(TransitionAutomaton transAut, int autIndex, PlcVariable autVar,
            PlcVariable edgeVar, PlcVariable isFeasibleVar)
    {
        List<PlcStatement> testCode = list();
        PlcSelectionStatement selStat = null;

        int edgeIndex = 1;
        for (TransitionEdge edge: transAut.transitionEdges) {
            final int finalEdgeIndex = edgeIndex; // Java wants a copy.
            Supplier<List<PlcStatement>> thenStats = () -> {
                if (autVar != null) {
                    // autVar := autIndes; edgeVar := edgeIndex;
                    return List.of(generatePlcIntAssignment(autVar, autIndex),
                            generatePlcIntAssignment(edgeVar, finalEdgeIndex));
                } else {
                    // edgeVar := edgeIndex;
                    return List.of(generatePlcIntAssignment(edgeVar, finalEdgeIndex));
                }
            };
            // Add "IF <guards> THEN <then-stats>" branch.
            selStat = mainExprGen.addBranch(edge.guards, thenStats, selStat, testCode);
            edgeIndex++;
        }

        if (autVar != null) {
            // Construct the complete test code:
            // IF autVar == 0 THEN <test edges of this automaton> END_IF
            PlcExpression guard = generateCompareVarWithVal(autVar, 0);
            return List.of(generateIfGuardThenCode(guard, testCode));
        } else {
            // If no enabled edge was found for this automaton, the event is immediately not feasible.
            if (selStat == null) {
                testCode.add(generatePlcBoolAssignment(isFeasibleVar, false));
            } else {
                selStat.elseStats.add(generatePlcBoolAssignment(isFeasibleVar, false));
            }
            return testCode;
        }
    }

    /**
     * Generate PLC code that selects the edge stored in {@code edgeVar} and 'performs' the edge. If needed compute the
     * sent value and store it into {@code channelValueVar}. Finally, perform the updates if they exist.
     *
     * @param transAut Sender automaton to generate perform code for.
     * @param edgeVar Variable containing the 1-based index of the selected edge to perform.
     * @param channelValueVar Variable that must be assigned the sent value of the channel by the selected edge. Use
     *     {@code null} if not in channel value context.
     * @return Generated PLC code that selects and performs the selected edge. Is extended in-place. Does not change if
     *     there is no PLC code needed to perform the edge.
     */
    private List<PlcStatement> generateAutPerformCode(TransitionAutomaton transAut, PlcVariable edgeVar,
            PlcVariable channelValueVar)
    {
        List<PlcStatement> performCode = list();
        PlcSelectionStatement selStat = null;

        int edgeIndex = 1;
        for (TransitionEdge edge: transAut.transitionEdges) {
            // Generate code that performs the edge if something need to be done.
            if (channelValueVar != null || !edge.updates.isEmpty()) {
                Supplier<List<PlcStatement>> thenStats = () -> {
                    List<PlcStatement> thenStatements = list();

                    // Compute and assign the sent value if it exists.
                    if (channelValueVar != null) {
                        genAssignExpr(new PlcVarExpression(channelValueVar), edge.sendValue, thenStatements);
                    }

                    // Perform the updates.
                    thenStatements.addAll(generateUpdates(edge.updates));
                    return thenStatements;
                };
                // Add "IF edgeVar == edgeIndex THEN <compute channelValue if needed, and perform updates>" branch.
                PlcExpression guard = generateCompareVarWithVal(edgeVar, edgeIndex);
                selStat = mainExprGen.addPlcBranch(List.of(new ExprValueResult(mainExprGen).setValue(guard)), thenStats,
                        selStat, performCode);
            }
            edgeIndex++;
        }
        return performCode;
    }

    /**
     * Generate PLC code that performs the provided updates.
     *
     * @param updates Updates to convert to PLC code.
     * @return The generated statements. Is extended in-place.
     */
    private List<PlcStatement> generateUpdates(List<Update> updates) {
        List<PlcStatement> statements = list();

        for (Update upd: updates) {
            if (upd instanceof IfUpdate ipUpd) {
                genIfUpdate(ipUpd, statements);
            } else if (upd instanceof Assignment asg) {
                genUpdateAssignment(asg.getAddressable(), asg.getValue(), statements);
            } else {
                throw new AssertionError("Unexpected kind of update \"" + upd + "\" found.");
            }
        }
        return statements;
    }

    /**
     * Generate PLC code to perform one group of the available updates.
     *
     * @param ifUpd The available groups of updates with their conditions to convert.
     * @param statements Generated PLC statements. Is extended in-place.
     */
    private void genIfUpdate(IfUpdate ifUpd, List<PlcStatement> statements) {
        PlcSelectionStatement selStat = null;
        selStat = mainExprGen.addBranch(ifUpd.getGuards(), () -> generateUpdates(ifUpd.getThens()), selStat,
                statements);
        for (ElifUpdate elifUpd: ifUpd.getElifs()) {
            selStat = mainExprGen.addBranch(elifUpd.getGuards(), () -> generateUpdates(elifUpd.getThens()), selStat,
                    statements);
        }
        mainExprGen.addBranch(null, () -> generateUpdates(ifUpd.getElses()), selStat, statements);
    }

    /**
     * Convert an update assignment to PLC code.
     *
     * @param lhs Left side to assign to.
     * @param rhs Right side value to assign.
     * @param statements Generated PLC statements. Is extended in-place.
     */
    private void genUpdateAssignment(Expression lhs, Expression rhs, List<PlcStatement> statements) {
        // TODO: Unfold "a, b := x,y" cases, decide the order of updates for minimal temporary current lhs value copies.

        // Test for the simple case of a single left side value.
        if (!(lhs instanceof TupleExpression lhsTuple)) {
            // Left side is a single destination, assign the riht side to it.
            ExprAddressableResult lhsResult = mainExprGen.convertAddressable(lhs);
            statements.addAll(lhsResult.code);
            lhsResult.releaseCodeVariables();
            genAssignExpr(lhsResult.value, rhs, statements);
            lhsResult.releaseValueVariables();
            return;
        }

        // Left side is a tuple literal. If the right side is also a tuple, do pairwise assignment.
        if (rhs instanceof TupleExpression rhsTuple) {
            Assert.check(lhsTuple.getFields().size() == rhsTuple.getFields().size());
            for (int i = 0; i < lhsTuple.getFields().size(); i++) {
                genUpdateAssignment(lhsTuple.getFields().get(i), rhsTuple.getFields().get(i), statements);
            }
            return;
        }

        // Left side is a tuple literal, right side is not. Compute the right side, then store it in a variable for
        // allowing to project on its parts.
        PlcVariable rhsVariable = mainExprGen.getTempVariable("rightValue", rhs.getType());

        ExprValueResult rhsValueResult = mainExprGen.convertValue(rhs);
        statements.addAll(rhsValueResult.code);
        rhsValueResult.releaseCodeVariables();
        statements.add(new PlcAssignmentStatement(rhsVariable, rhsValueResult.value));
        rhsValueResult.releaseValueVariables();

        // Generate projected assignments using the variable.
        genUpdateAssignment(lhs, rhsVariable, List.of(), statements);
        mainExprGen.releaseTempVariable(rhsVariable);
    }

    /**
     * Convert an update assignment with projected right side to PLC code.
     *
     * @param lhs Left side to assign to.
     * @param rhsVariable Unprojected right side value in a variable to assign.
     * @param rhsProjections Projection sequence to apply to the {@code rhs} value before assigning to {@code lhs}.
     * @param statements Generated PLC statements. Is extended in-place.
     */
    private void genUpdateAssignment(Expression lhs, PlcVariable rhsVariable, List<PlcStructProjection> rhsProjections,
            List<PlcStatement> statements)
    {
        // Conceptually: "lhs := rhs.rhasProjections" where lhs may be a tuple literal.

        // Test for the simple case of a single left side.
        if (!(lhs instanceof TupleExpression lhsTuple)) {
            // Left side is a single destination, the entire right side must be assigned to it.
            ExprAddressableResult lhsResult = mainExprGen.convertAddressable(lhs);
            statements.addAll(lhsResult.code);
            lhsResult.releaseCodeVariables();

            // Append rhs projections after the rhs value so only a part of the rhs is assigned to the lhs.
            PlcVarExpression rhsValue = new PlcVarExpression(rhsVariable, cast(rhsProjections));
            statements.add(new PlcAssignmentStatement(lhsResult.value, rhsValue));
            lhsResult.releaseValueVariables();
            return;
        }

        // Left side is a tuple literal, right side is a single value, split into more partial assignments.
        PlcType lhsType = target.getTypeGenerator().convertType(lhs.getType());
        PlcStructType lhsStructType = target.getTypeGenerator().getStructureType(lhsType);

        for (int idx = 0; idx < lhsStructType.fields.size(); idx++) {
            // Make a new projection list "valueProjections.<lhs-field>".
            List<PlcStructProjection> projs = listc(rhsProjections.size() + 1);
            projs.addAll(rhsProjections);
            projs.add(new PlcStructProjection(lhsStructType.fields.get(idx).name));
            genUpdateAssignment(lhsTuple.getFields().get(idx), rhsVariable, projs, statements);
        }
    }

    /**
     * Construct a selection statement with one alternative.
     *
     * @param guard Condition that must hold to perform the statements.
     * @param statements Statements to conditionally execute.
     * @return The generated selection statement.
     */
    private PlcSelectionStatement generateIfGuardThenCode(PlcExpression guard, List<PlcStatement> statements) {
        PlcSelectionStatement selStat = new PlcSelectionStatement();
        selStat.condChoices.add(new PlcSelectChoice(guard, statements));
        return selStat;
    }

    /**
     * Assign a CIF expression value to (a type-compatible) PLC variable.
     *
     * @param lhsVarExpr Variable to assign to.
     * @param value Value to assign.
     * @param statements If not {@code null}, storage for the generated code, is updated in-place. Otherwise a new list
     *     is constructed to store the resulting code. The created or updated list is also returned as return value of
     *     the method.
     * @return The supplied or newly created statements, appended with the generated assignment.
     */
    private List<PlcStatement> genAssignExpr(PlcVarExpression lhsVarExpr, Expression value,
            List<PlcStatement> statements)
    {
        statements = (statements == null) ? list() : statements;
        ExprValueResult rhsResult = mainExprGen.convertValue(value);
        statements.addAll(rhsResult.code);
        rhsResult.releaseCodeVariables();
        statements.add(new PlcAssignmentStatement(lhsVarExpr, rhsResult.value));
        rhsResult.releaseValueVariables();
        return statements;
    }

    /**
     * Generate an expression that compares the given variable with the given literal value.
     *
     * @param variable Variable to compare.
     * @param value Literal value to compare against.
     * @return The generated expression.
     */
    private PlcExpression generateCompareVarWithVal(PlcVariable variable, int value) {
        PlcExpression varExpr = new PlcVarExpression(variable);
        PlcExpression valExpr = new PlcIntLiteral(value);
        return funcAppls.equalFuncAppl(varExpr, valExpr);
    }

    /**
     * Generate an assignment statement of the given value to the given variable.
     *
     * @param variable Variable to assign.
     * @param value Value to assign to the variable.
     * @return The generated statement.
     */
    private PlcAssignmentStatement generatePlcIntAssignment(PlcVariable variable, int value) {
        return new PlcAssignmentStatement(variable, new PlcIntLiteral(value));
    }

    /**
     * Generate an assignment statement of the given value to the given variable.
     *
     * @param variable Variable to assign.
     * @param value Value to assign to the variable.
     * @return The generated statement.
     */
    private PlcAssignmentStatement generatePlcBoolAssignment(PlcVariable variable, boolean value) {
        return new PlcAssignmentStatement(variable, new PlcBoolLiteral(value));
    }
}
