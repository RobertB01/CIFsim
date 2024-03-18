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

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.cast;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Numbers.toOrdinal;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.expressions.CifDataProvider;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprAddressableResult;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprValueResult;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransAutPurpose;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionAutomaton;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionEdge;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcStructProjection;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentBlock;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement.PlcSelectChoice;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

/** Generator for creating PLC code to perform CIF event transitions in the PLC. */
public class DefaultTransitionGenerator implements TransitionGenerator {
    /** Number of leading and trailing {@code *} characters for the event level comment. */
    private static final int EVENT_COMMENT_STARCOUNT = 60;

    /** Number of leading and trailing {@code *} characters for automata kind level comment. */
    private static final int AUTOMATA_COMMENT_STARCOUNT = 30;

    /** PLC target to generate code for. */
    private final PlcTarget target;

    /**
     * The event transitions to generate. Is {@code null} until the transitions are provided with
     * {@link #setTransitions}.
     */
    private List<CifEventTransition> eventTransitions = null;

    /**
     * For each automaton, the variable that tracks the selected edge to perform for its automaton.
     *
     * <p>
     * Use the {@link #getAutomatonEdgeVariable} method to query this map.
     * </p>
     */
    private final Map<Automaton, PlcBasicVariable> edgeSelectionVariables = map();

    /** Generator for obtaining clash-free names in the generated code. */
    private NameGenerator nameGen;

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
     * isProgress := FALSE; // Generated in PlcCodeStorage.
     *
     * eventEnabled := &lt;test-if-eventA-is-enabled&gt;;
     * IF eventEnabled THEN
     *     isProgress := TRUE;
     *     &lt;perform-transition-of-eventA&gt;
     * END_IF;
     *
     * eventEnabled := &lt;test-if-eventB-is-enabled&gt;;
     * IF eventEnabled THEN
     *     isProgress := TRUE;
     *     &lt;perform-transition-of-eventB&gt;
     * END_IF;
     *
     * ... // Other transitions are omitted.
     * </pre> More details on the implementation of testing and performing can be found in the
     * {@link #generateEventTransitionCode} method.
     * </p>
     *
     * @return The generated PLC event transition code.
     */
    List<PlcStatement> generateCode() {
        // TODO Currently code generation is straight forward, it generates correct code for the general case. There are
        // heaps of improvements possible if you recognize specific cases like 1 automaton, 1 edge, 0 senders, better
        // names for variables, etc.
        nameGen = target.getNameGenerator();
        mainExprGen = target.getCodeStorage().getExprGenerator();
        funcAppls = new PlcFunctionAppls(target);

        // Set up selected edge tracking variables for the automata.
        edgeSelectionVariables.clear();
        for (CifEventTransition evtTrans: eventTransitions) {
            for (TransitionAutomaton transAut: evtTrans.senders) {
                ensureEdgeVariable(transAut.aut);
            }
            for (TransitionAutomaton transAut: evtTrans.receivers) {
                ensureEdgeVariable(transAut.aut);
            }
            for (TransitionAutomaton transAut: evtTrans.syncers) {
                ensureEdgeVariable(transAut.aut);
            }
            // Monitors do not need edge tracking since the first enabled edge is immediately taken.
        }

        // Variable that tracks whether at least one event was performed in the current event loop cycle.
        PlcBasicVariable isProgressVar = target.getCodeStorage().getIsProgressVariable();

        // As all transition code is generated in main program context, only one generated statements list exists and
        // various variables that store decisions in the process can be re-used between different events.
        boolean addEmptyLineBefore = false;
        List<PlcStatement> statements = list();
        for (CifEventTransition eventTransition: eventTransitions) {
            statements.addAll(generateEventTransitionCode(eventTransition, addEmptyLineBefore, isProgressVar));
            addEmptyLineBefore = true;
        }

        // Release all temporary variables, and return the generated event transition code.
        mainExprGen.releaseTempVariable(isProgressVar);
        mainExprGen.releaseTempVariables(edgeSelectionVariables.values());
        edgeSelectionVariables.clear();

        return statements;
    }

    /**
     * Ensure that a variable for tracking the selected edge for the 'aut' automaton exists in
     * {@link #edgeSelectionVariables} after the call.
     *
     * @param aut Automaton that must have or get a variable for tracking the selected edge of the automaton.
     */
    private void ensureEdgeVariable(Automaton aut) {
        if (edgeSelectionVariables.containsKey(aut)) {
            return;
        }
        String edgeVariableName = nameGen.generateGlobalName("edge_" + aut.getName(), false);

        // TODO: Use a smaller integer for edge indexing.
        PlcBasicVariable autVar = mainExprGen.getTempVariable(edgeVariableName, PlcElementaryType.DINT_TYPE);
        edgeSelectionVariables.put(aut, autVar);
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
     * <li>As a monitor by synchronizing on the event without sending or receiving a value, if the automaton has an
     * enabled edge for it, or ignoring the event while not blocking its occurrence if it has no enabled edge for
     * it.</li>
     * </ul>
     * Each form can have several automata.
     * </p>
     *
     * <p>
     * Generated code for an event transition contains test code and perform code for each of the available forms,
     * except that monitors test code is always empty since they do not decide whether an event is enabled. Also if an
     * event is not a channel there are no senders and receivers, and thus the test and perform code of those is empty
     * in that case.
     * </p>
     *
     * <p>
     * The general structure of code of an event transition is: <pre>
     * &lt;test-code&gt;; IF eventEnabled THEN &lt;perform-code&gt;; END_IF;
     * </pre>
     * </p>
     *
     * <p>
     * The {@code test-code} block visits the involved automata of an event, checks for enabled edges and sets the
     * {@code eventEnabled} variable to communicate its result to the {@code perform-code}. It has the following
     * structure: <pre>
     * eventEnabled := TRUE;
     *
     * &lt;find-a-sender-automaton-with-enabled-edge&gt;
     * IF NOT found THEN eventEnabled := FALSE; END_IF;
     *
     * IF eventEnabled THEN
     *     &lt;find-a-receiver-automaton-with-enabled-edge&gt;
     *     IF NOT found THEN eventEnabled := FALSE; END_IF;
     * END_IF;
     *
     * IF eventEnabled THEN
     *     &lt;find-enabled-edge-for-syncer-automaton-1&gt;
     *     IF NOT found THEN eventEnabled := FALSE; END_IF;
     * END_IF;
     * IF eventEnabled THEN
     *     &lt;find-enabled-edge-for-syncer-automaton-2&gt;
     *     IF NOT found THEN eventEnabled := FALSE; END_IF;
     * END_IF;
     * ... // Test edge code of other syncer-automata omitted.
     * </pre>
     * </p>
     * <p>
     * The {@code perform-code} block assumes that the event is enabled. It contains code to perform the updates of the
     * edges found by the {@code test-code}. In addition the {@code perform-code} tests edges of monitor automata and
     * also executes them if an enabled edge is found. The {@code perform-code} has the following structure: <pre>
     * isProgress := TRUE;
     *
     * &lt;perform-edge-of-the-found-sender&gt;
     *
     * &lt;perform-edge-of-the-found-receiver&gt;
     *
     * &lt;perform-edge-of-sync-automaton-1&gt;
     * &lt;perform-edge-of-sync-automaton-2&gt;
     * ... // Perform edge code of other syncer-automata omitted.
     *
     * &lt;try-to-find-and-perform-edge-of-monitor-automaton-1&gt;
     * &lt;try-to-find-and-perform-edge-of-monitor-automaton-2&gt;
     * ... // Try to perform edge code of other monitor-automata omitted.
     * </pre>
     * <ul>
     * <li>Details of test code and perform code for senders and receivers can be found at
     * {@link #generateSendReceiveCode},</li>
     * <li>Details of test code and perform code for syncers can be found at {@link #generateSyncCode}, and</li>
     * <li>Details of (try to) perform code for monitors can be found at {@link #generateMonitorCode}.</li>
     * </ul>
     * </p>
     *
     * @param eventTransition Event transition to generate code for.
     * @param prependEmptyLine Whether to insert an empty line before the event transition.
     * @param isProgressVar PLC variable to set if the event transition is performed.
     * @return The generated code for testing and performing the event in the PLC.
     */
    private List<PlcStatement> generateEventTransitionCode(CifEventTransition eventTransition, boolean prependEmptyLine,
            PlcBasicVariable isProgressVar)
    {
        // Both code parts visit the same automata and the check or perform the same edges. For this reason it makes
        // sense to generate both test-code and perform-code at the same time for each automaton.
        // At the end of this method both parts are combined.
        List<PlcStatement> testCode = list(); // Code that decides whether the event is enabled.
        List<PlcStatement> performCode = list(); // Code that performs the event assuming the event is enabled.
        List<PlcBasicVariable> createdTempVariables = list();

        // Obtain the 'event is enabled' variable.
        PlcBasicVariable eventEnabledVar = mainExprGen.getTempVariable("eventEnabled", PlcElementaryType.BOOL_TYPE);
        createdTempVariables.add(eventEnabledVar);

        // So far, no test code has been generated that may set 'eventEnabled' to false.
        boolean eventEnabledAlwaysHolds = true;

        // Generate the header of the transition code for the event. Initialize the 'event is enabled' variable.
        if (prependEmptyLine) {
            testCode.add(new PlcCommentLine(null));
        }

        // Announce that the event is tried in the PLC code, and start with assuming that the event is enabled.
        testCode.add(genAnnounceEventBeingTried(eventTransition));
        testCode.add(new PlcAssignmentStatement(eventEnabledVar, new PlcBoolLiteral(true)));

        // Performing the event implies progress is made.
        performCode.add(new PlcAssignmentStatement(isProgressVar, new PlcBoolLiteral(true)));
        CifDataProvider performProvider = generateCopiedState(eventTransition.collectAssignedVariables(), performCode,
                createdTempVariables);

        // Generate channel communication if the event is a channel.
        CifType channelType = eventTransition.event.getType();
        if (channelType != null) {
            // Event is a channel, generate senders and receivers code.

            // A void channel does not transport data, 'channelValueVar' is 'null' in that case.
            channelType = normalizeType(channelType);
            PlcBasicVariable channelValueVar;
            String performProvideText, performAcceptText;
            if (channelType instanceof VoidType) {
                channelValueVar = null;

                performProvideText = "perform updates of the selected providing automaton.";
                performAcceptText = "perform updates of the selected accepting automaton.";
            } else {
                channelValueVar = mainExprGen.getTempVariable("channelValue", channelType);
                createdTempVariables.add(channelValueVar);

                performProvideText = "Store the provided value and perform updates of the selected providing "
                        + "automaton.";
                performAcceptText = "Deliver the provided value and perform updates of the selected accepting "
                        + "automaton.";
            }

            // Handle senders.
            testCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Try to find an automaton that provides a value.")));
            performCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT, List.of(performProvideText)));

            generateSendReceiveCode(eventTransition.event, eventTransition.senders, testCode, performProvider,
                    performCode, TransAutPurpose.SENDER, createdTempVariables, eventEnabledVar, channelValueVar,
                    eventEnabledAlwaysHolds);

            eventEnabledAlwaysHolds = false; // At least one sender tested; event is no longer guaranteed to be enabled.

            // Handle receivers.
            testCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Try to find an automaton that accepts a value.")));
            performCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT, List.of(performAcceptText)));

            mainExprGen.setChannelValueVariable(channelValueVar);
            generateSendReceiveCode(eventTransition.event, eventTransition.receivers, testCode, performProvider,
                    performCode, TransAutPurpose.RECEIVER, createdTempVariables, eventEnabledVar, null,
                    eventEnabledAlwaysHolds);
            mainExprGen.setChannelValueVariable(null);
        }

        // Handle syncers.
        if (!eventTransition.syncers.isEmpty()) {
            testCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Check each synchronizing automaton for having an edge with a true guard.")));
            performCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Perform the assignments of each synchronizing automaton.")));
            generateSyncCode(eventTransition.event, eventTransition.syncers, testCode, performProvider, performCode,
                    createdTempVariables, eventEnabledVar, eventEnabledAlwaysHolds);
        }

        // Handle monitors. Only generates perform code since it doesn't influence enabledness of the event transition.
        if (!eventTransition.monitors.isEmpty()) {
            performCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Perform the assignments of each optionally synchronizing automaton.")));
            generateMonitorCode(eventTransition.monitors, performProvider, performCode, createdTempVariables);
        }

        // Construct the complete PLC code for the event transition by concatenating test and conditional perform code.
        List<PlcStatement> resultCode = testCode;
        PlcExpression guard = new PlcVarExpression(eventEnabledVar);

        resultCode.add(new PlcCommentLine(
                fmt("All checks have been done. If variable \"%s\" still holds, event \"%s\" can occur.",
                        eventEnabledVar.varName, getAbsName(eventTransition.event, false))));
        resultCode.add(generateIfGuardThenCode(guard, performCode));

        mainExprGen.releaseTempVariables(createdTempVariables);
        return resultCode;
    }

    /**
     * Construct a comment block stating the event being tried, together with the automata involved in the attempt.
     *
     * @param eventTrans Event transition being tried.
     * @return Comment block stating the event, and listing what is needed for the event to occur.
     */
    private PlcCommentBlock genAnnounceEventBeingTried(CifEventTransition eventTrans) {
        CodeBox box = new MemoryCodeBox();
        box.add("Try to perform event \"%s\".", getAbsName(eventTrans.event, false));

        CifType eventType = eventTrans.event.getType();
        if (eventType != null) {
            boolean transferValue = !(eventType instanceof VoidType);

            // List senders.
            box.add();
            if (eventTrans.senders.isEmpty()) {
                box.add("- An automaton that must send a value is missing, event cannot occur.");
            } else {
                if (transferValue) {
                    box.add("- One automaton must send a value.");
                } else {
                    box.add("- One automaton must send a value (although no data is actually transferred).");
                }
                for (TransitionAutomaton transAut: eventTrans.senders) {
                    box.add("   - Automaton \"%s\" may send a value.", getAbsName(transAut.aut, false));
                }
            }

            // List receivers.
            box.add();
            if (eventTrans.receivers.isEmpty()) {
                box.add("- An automaton that must accept a value is missing, event cannot occur.");
            } else {
                if (transferValue) {
                    box.add("- One automaton must accept a value.");
                } else {
                    box.add("- One automaton must accept a value (although no data is actually transferred).");
                }
                for (TransitionAutomaton transAut: eventTrans.receivers) {
                    box.add("   - Automaton \"%s\" may accept a value.", getAbsName(transAut.aut, false));
                }
            }
        }

        // List syncers,
        if (!eventTrans.syncers.isEmpty()) {
            box.add();
            for (TransitionAutomaton transAut: eventTrans.syncers) {
                box.add("- Automaton \"%s\" must always synchronize.", getAbsName(transAut.aut, false));
            }
        }

        // List monitors,
        if (!eventTrans.monitors.isEmpty()) {
            box.add();
            for (TransitionAutomaton transAut: eventTrans.monitors) {
                box.add("- Automaton \"%s\" may synchronize.", getAbsName(transAut.aut, false));
            }
        }

        return new PlcCommentBlock(EVENT_COMMENT_STARCOUNT, box.getLines());
    }

    /**
     * Generate PLC code that copies the current value of all variables that may be assigned while performing the event.
     * Also, construct a CIF data provider that redirects reading of variables to the created copies.
     *
     * @param assignedVariables Variables that may be assigned while performing the event.
     * @param codeStorage Storage for the generated PLC code.
     * @param createdTempVariables Created temporary variables to test and perform an event. Is extended in-place with
     *     introduced variables to temporarily store current state.
     * @return A CIF data provider that redirects variable value queries in expression conversion to the copied state.
     *     Is {@code null} if nothing needs to be copied.
     */
    private CifDataProvider generateCopiedState(Set<Declaration> assignedVariables, List<PlcStatement> codeStorage,
            List<PlcBasicVariable> createdTempVariables)
    {
        if (assignedVariables.isEmpty()) {
            return null;
        }

        // Sort declarations on absolute name.
        List<Declaration> assignedVarList = set2list(assignedVariables);
        Collections.sort(assignedVarList, (a, b) -> getAbsName(a, false).compareTo(getAbsName(b, false)));

        if (!assignedVariables.isEmpty()) {
            codeStorage.add(new PlcCommentLine("Make temporary copies of assigned variables to preserve the old "
                    + "values while assigning new values."));
        }

        // Generate code to copy the old value of assigned variables so they survive assignment with a new value, and
        // setup a redirect map for a new data provider, to make the expression generator use the copied state for
        // reading.
        CifDataProvider cifDataProvider = mainExprGen.getScopeCifDataProvider();
        Map<Declaration, PlcExpression> redirectedDecls = map();
        for (Declaration assignedVar: assignedVarList) {
            // TODO If the variable is only written, a copy is not needed.
            if (assignedVar instanceof DiscVariable dv) {
                PlcBasicVariable currentVar = mainExprGen.getTempVariable("current_" + getAbsName(dv, false),
                        dv.getType());
                createdTempVariables.add(currentVar);
                redirectedDecls.put(dv, new PlcVarExpression(currentVar));
                codeStorage.add(new PlcAssignmentStatement(new PlcVarExpression(currentVar),
                        cifDataProvider.getValueForDiscVar(dv)));
            } else if (assignedVar instanceof ContVariable cv) {
                PlcBasicVariable currentVar = mainExprGen.getTempVariable("current_" + getAbsName(cv, false),
                        target.getRealType());
                createdTempVariables.add(currentVar);
                redirectedDecls.put(cv, new PlcVarExpression(currentVar));
                codeStorage.add(new PlcAssignmentStatement(new PlcVarExpression(currentVar),
                        cifDataProvider.getValueForContvar(cv, false)));
            } else {
                throw new AssertionError("Unexpected kind of assigned variable \"" + assignedVar + "\".");
            }
        }
        return new TransitionDataProvider(redirectedDecls, cifDataProvider);
    }

    /** CIF data provider that redirects value access of state variables. */
    private static class TransitionDataProvider extends CifDataProvider {
        /** The collection of redirected PLC state variable values. */
        private final Map<Declaration, PlcExpression> redirectedDecls;

        /** Original CIF data provider. */
        private final CifDataProvider cifDataProvider;

        /**
         * Constructor of the {@link TransitionDataProvider} class.
         *
         * @param redirectedDecls The collection of redirected PLC variable values.
         * @param cifDataProvider Original CIF data provider.
         */
        public TransitionDataProvider(Map<Declaration, PlcExpression> redirectedDecls,
                CifDataProvider cifDataProvider)
        {
            this.redirectedDecls = redirectedDecls;
            this.cifDataProvider = cifDataProvider;
        }

        @Override
        public PlcExpression getValueForInputVar(InputVariable variable) {
            return cifDataProvider.getValueForInputVar(variable);
        }

        @Override
        public PlcExpression getValueForDiscVar(DiscVariable variable) {
            return redirectedDecls.getOrDefault(variable, cifDataProvider.getValueForDiscVar(variable));
        }

        @Override
        public PlcExpression getValueForContvar(ContVariable variable, boolean getDerivative) {
            if (getDerivative) { // Derivatives can't be assigned.
                return cifDataProvider.getValueForContvar(variable, getDerivative);
            } else {
                return redirectedDecls.getOrDefault(variable,
                        cifDataProvider.getValueForContvar(variable, getDerivative));
            }
        }

        @Override
        public PlcExpression getValueForConstant(Constant constant) {
            return cifDataProvider.getValueForConstant(constant);
        }

        @Override
        public PlcVarExpression getAddressableForDiscVar(DiscVariable variable) {
            return cifDataProvider.getAddressableForDiscVar(variable);
        }

        @Override
        public PlcVarExpression getAddressableForContvar(ContVariable variable, boolean writeDerivative) {
            return cifDataProvider.getAddressableForContvar(variable, writeDerivative);
        }

        @Override
        public PlcVarExpression getAddressableForInputVar(InputVariable variable) {
            return cifDataProvider.getAddressableForInputVar(variable);
        }
    }

    /**
     * Generate code to test and perform a transition for a sender or a receiver automaton.
     *
     * <p>
     * The objective of test code for both senders and receivers is to find an automaton with an enabled edge. In the
     * perform code the enabled edge is then taken. For communication from test code to perform code, a
     * {@code senderAut} respectively {@code receiverAut} variable exists to capture the found automata. To capture the
     * selected edge for both the sender and the receiver automaton, the {@code edgeVar} of the selected automaton is
     * used.
     * </p>
     * <p>
     * For the senders automata the generated test code looks like: <pre>
     * senderAut := 0;
     *
     * IF senderAut = 0 THEN
     *     IF &lt;edge-1-of-sender-1-is-enabled&gt; THEN senderAut := 1; edgeVar_of_sender_1 := 1;
     *     ELSE IF &lt;edge-2-of-sender-1-is-enabled&gt; THEN senderAut := 1; edgeVar_of_sender_1 := 2;
     *     ... // Other edge tests of sender-1 omitted.
     * END_IF;
     *
     * IF senderAut = 0 THEN
     *     IF &lt;edge-1-of-sender-2-is-enabled&gt; THEN senderAut := 2; edgeVar_of_sender_2 := 1;
     *     ELSE IF &lt;edge-2-of-sender-2-is-enabled&gt; THEN senderAut := 2; edgeVar_of_sender_2 := 2;
     *     ... // Other edge tests of sender-2 omitted.
     * END_IF;
     *
     * // Test code of other sender automata omitted.
     *
     * IF senderAut = 0 THEN
     *     eventEnabled := FALSE;
     * END_IF;
     * </pre> The receivers test code uses the {@code receiverAut} and their {@code edgeVar} variables. It tests for
     * enabledness first, but looks the same otherwise.
     * </p>
     *
     * <p>
     * The perform code is a selection statement to select on the value of the {@code senderAut} respectively
     * {@code receiverAut} variable. Within each branch, a selection is done on the value of the {@code edgeVar}
     * variable of the automaton. Once arrived at the chosen edge of the chosen automaton, the sender computes the sent
     * value and stores it into a (type-specific) {@code channelValue} variable and then performs the updates of its
     * selected edge. The chosen edge of the receiver only performs its updates, as usage of the sent value is "hidden"
     * in them.
     * </p>
     *
     * @param event Event being dealt with.
     * @param autTransitions Automaton transitions to convert.
     * @param testCode Storage for generated test code. Is updated in-place.
     * @param performProvider CIF data provider that redirects reads of possibly modified variables to safe copies
     *     during perform code execution. Can be {@code null} to disable redirection.
     * @param performCode Storage for generated perform code. Is updated in-place.
     * @param purpose Purposes of the given automaton.
     * @param createdTempVariables Tracking storage of created temporary variables.
     * @param eventEnabledVar PLC variable expressing if the event is enabled.
     * @param channelValueVar PLC variable storing the channel value. Is {@code null} when not sending a value (either
     *     receiving a value or it is a void channel).
     * @param eventEnabledAlwaysHolds Whether it is known that 'eventEnabled' in tests is always true at runtime.
     */
    private void generateSendReceiveCode(Event event, List<TransitionAutomaton> autTransitions,
            List<PlcStatement> testCode, CifDataProvider performProvider, List<PlcStatement> performCode,
            TransAutPurpose purpose, List<PlcBasicVariable> createdTempVariables, PlcBasicVariable eventEnabledVar,
            PlcBasicVariable channelValueVar, boolean eventEnabledAlwaysHolds)
    {
        // Setup comment texts.
        String varPrefix, failResultText;
        if (purpose == TransAutPurpose.SENDER) {
            varPrefix = "sender";
            failResultText = "Failed to find an automaton that provides a value, skip to next event.";
        } else {
            varPrefix = "receiver";
            failResultText = "Failed to find an automaton that accepts a value, skip to next event.";
        }

        List<PlcStatement> autTestCode = list(); // Intermediate storage for test code.

        // TODO: Use a smaller integer for automaton indexing.
        PlcBasicVariable autVar = mainExprGen.getTempVariable(varPrefix + "Aut", PlcElementaryType.DINT_TYPE);
        createdTempVariables.add(autVar);
        autTestCode.add(generatePlcIntAssignment(autVar, 0));

        // Outer dispatch selection statement that selects on the chosen automaton, and performs selection performing of
        // the edges within each dispatched branch. The latter is generated below.
        PlcSelectionStatement performSelectStat = new PlcSelectionStatement();
        List<PlcStatement> collectedNoUpdates = list(); // Comment lines about automata that have no updates to perform.

        // Generate code that tests and performs sending or receiving a value to the channel.
        int autIndex = 1; // Index 0 is used to denote no automaton has been selected.
        for (TransitionAutomaton transAut: autTransitions) {
            // Get the variable that stores the selected edge index for the automaton.
            PlcBasicVariable edgeVar = getAutomatonEdgeVariable(transAut.aut);

            // Generate edge testing code.
            autTestCode.addAll(generateEdgesTestCode(event, transAut, autIndex, autVar, edgeVar, eventEnabledVar));

            // Generate the edge selection and performing code, and add it as a branch on the automaton to
            // 'performSelectStat'.
            mainExprGen.setCurrentCifDataProvider(performProvider); // Switch to using stored variables state.
            List<PlcStatement> innerPerformCode = list(
                    new PlcCommentLine(fmt("Automaton \"%s\" was selected.", getAbsName(transAut.aut, false))));
            innerPerformCode.addAll(generateAutPerformCode(transAut, edgeVar, channelValueVar));

            if (innerPerformCode.size() == 1) { // No updates need to be done.
                collectedNoUpdates.add(new PlcCommentLine(
                        fmt("Automaton \"%s\" has no assignments to perform.", getAbsName(transAut.aut, false))));
            } else {
                performSelectStat.condChoices
                        .add(new PlcSelectChoice(generateCompareVarWithVal(autVar, autIndex), innerPerformCode));
            }
            mainExprGen.setCurrentCifDataProvider(null); // And switch back to normal variable access.

            autIndex++;
        }

        // Only add the 'performSelectStat' if at least one edge has code to perform on a taken transition.
        if (!performSelectStat.condChoices.isEmpty()) {
            performCode.add(performSelectStat);
        }

        // In test code, if none of the automaton tests succeeds, the event is not enabled.
        // IF autVar = 0 THEN eventEnabled := FALSE; END_IF;
        {
            PlcExpression guard = generateCompareVarWithVal(autVar, 0);
            PlcCommentLine epxlanation = new PlcCommentLine(failResultText);
            PlcAssignmentStatement assignment = generatePlcBoolAssignment(eventEnabledVar, false);
            autTestCode.add(generateIfGuardThenCode(guard, list(epxlanation, assignment)));
        }

        // If enabledness is known to hold, the generated test code can be used as-is. Otherwise, running the generated
        // test code only makes sense after verifying that the event is indeed enabled.
        if (eventEnabledAlwaysHolds) {
            testCode.addAll(autTestCode);
        } else {
            PlcExpression guard = new PlcVarExpression(eventEnabledVar);
            testCode.add(generateIfGuardThenCode(guard, autTestCode));
        }
    }

    /**
     * Generate code to test and perform a transition for syncer automata.
     *
     * <p>
     * Each syncer automaton of the event must have an enabled edge. Each syncer automaton uses its own {@code edgeVar}
     * variable for storing its selected edge. Also, failing to find an enabled edge for a syncer automaton immediately
     * blocks the entire event from happening and thus immediately modifies {@code eventEnabled} as the last
     * {@code ELSE} block in finding an enabled edge.
     * </p>
     *
     * <p>
     * This leads to test code like <pre>
     * IF eventEnabled THEN
     *     IF &lt;edge-1-of-syncer-1-is-enabled&gt; THEN edgeVar_of_syncer-1 := 1;
     *     ELSE IF &lt;edge-2-of-syncer-1-is-enabled&gt; THEN edgeVar_of_syncer-1 := 2;
     *     ... // Other edge tests of syncer-1 omitted.
     *     ELSE eventEnabled := FALSE
     *     END_IF;
     * END_IF;
     *
     * // Test code for other syncer automata omitted.
     * </pre> Performing the chosen edge is implemented by selection on the value of an {@code edgeVar} variable of the
     * automaton. Within that branch, the updates of the chosen edge are then performed.
     * </p>
     *
     * @param event Event being dealt with.
     * @param autTransitions Automaton transitions to convert.
     * @param testCode Storage for generated test code. Is updated in-place.
     * @param performProvider CIF data provider that redirects reads of possibly modified variables to safe copies
     *     during perform code execution. Can be {@code null} to disable redirection.
     * @param performCode Storage for generated perform code. Is updated in-place.
     * @param createdTempVariables Tracking storage of created temporary variables.
     * @param eventEnabledVar PLC variable expressing if the event is enabled.
     * @param eventEnabledAlwaysHolds Whether it is known that 'eventEnabled' in tests is always true at runtime.
     */
    private void generateSyncCode(Event event, List<TransitionAutomaton> autTransitions, List<PlcStatement> testCode,
            CifDataProvider performProvider, List<PlcStatement> performCode,
            List<PlcBasicVariable> createdTempVariables, PlcBasicVariable eventEnabledVar,
            boolean eventEnabledAlwaysHolds)
    {
        List<PlcStatement> collectedNoUpdates = list();
        List<PlcStatement> collectedUpdates = list();

        // Generate code that tests and performs synchronizing on the event.
        for (TransitionAutomaton transAut: autTransitions) {
            // Get the variable that stores the selected edge index for the automaton.
            PlcBasicVariable autEdgeVar = getAutomatonEdgeVariable(transAut.aut);

            // Initialize intermediate storage for test code of the automaton.
            List<PlcStatement> autTestCode = list();

            // Generate edge testing code.
            autTestCode.addAll(generateEdgesTestCode(event, transAut, -1, null, autEdgeVar, eventEnabledVar));

            // Generate the edge selection and performing code, and add it as a branch on the automaton.
            mainExprGen.setCurrentCifDataProvider(performProvider); // Switch to using stored variables state.
            List<PlcStatement> updates = generateAutPerformCode(transAut, autEdgeVar, null);
            if (updates.isEmpty()) {
                collectedNoUpdates.add(new PlcCommentLine(
                        fmt("Automaton \"%s\" has no assignments to perform.", getAbsName(transAut.aut, false))));
            } else {
                collectedUpdates.add(new PlcCommentLine(
                        fmt("Perform assignments of automaton \"%s\".", getAbsName(transAut.aut, false))));
                collectedUpdates.addAll(updates);
            }
            mainExprGen.setCurrentCifDataProvider(null); // And switch back to normal variable access.

            // If enabledness is known to hold, the generated test code can be used as-is. Otherwise, running the
            // generated test code only makes sense after verifying that the event is indeed enabled.
            if (eventEnabledAlwaysHolds) {
                testCode.addAll(autTestCode);
            } else {
                PlcExpression guard = new PlcVarExpression(eventEnabledVar);
                testCode.add(generateIfGuardThenCode(guard, autTestCode));
            }
            eventEnabledAlwaysHolds = false;
        }

        if (collectedUpdates.isEmpty()) {
            performCode.add(new PlcCommentLine(
                    "There are no assignments to perform for automata that must always synchronize."));
        }
        performCode.addAll(collectedUpdates);
        performCode.addAll(collectedNoUpdates);
    }

    /**
     * Generate code to test and perform a transition for a monitor automaton.
     *
     * <p>
     * As monitor automata do not influence enabledness of an event, there is no test code to construct. Instead,
     * finding an enabled edge and performing it is combined in the perform code. It generates the following for each
     * monitor automaton: <pre>
     * IF eventEnabled THEN
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
     * @param performProvider CIF data provider that redirects reads of possibly modified variables to safe copies
     *     during perform code execution. Can be {@code null} to disable redirection.
     * @param testAndPerformCode Storage for generated test and perform code. Is updated in-place.
     * @param createdTempVariables Tracking storage of created temporary variables.
     */
    private void generateMonitorCode(List<TransitionAutomaton> autTransitions, CifDataProvider performProvider,
            List<PlcStatement> testAndPerformCode, List<PlcBasicVariable> createdTempVariables)
    {
        // TODO: Do not allow only monitors for an event, as it may completely disable progress of time.

        List<PlcStatement> collectedNoUpdates = list(); // Comments about automata without updates.
        List<PlcStatement> collectedUpdates = list(); // Edge selections of automata with updates.

        mainExprGen.setCurrentCifDataProvider(performProvider); // Switch to using stored variables state.
        for (TransitionAutomaton transAut: autTransitions) {
            List<PlcStatement> updates = list();
            PlcSelectionStatement selStat = null;

            for (TransitionEdge edge: transAut.transitionEdges) {
                if (!edge.updates.isEmpty()) {
                    Supplier<List<PlcStatement>> thenStats = () -> { return generateUpdates(edge.updates); };
                    // Add an "IF <guards> THEN <perform-updates>" branch.
                    selStat = mainExprGen.addBranch(edge.guards, thenStats, selStat, updates);
                }
            }

            collectedUpdates.addAll(updates);
            if (updates.isEmpty()) {
                collectedNoUpdates.add(new PlcCommentLine(
                        fmt("Automaton \"%s\" has no assignments to perform.", getAbsName(transAut.aut, false))));
            }
        }
        mainExprGen.setCurrentCifDataProvider(null); // And switch back to normal variable access.

        if (collectedUpdates.isEmpty()) {
            testAndPerformCode.add(new PlcCommentLine(
                    "There are no assignments to perform for automata that must always synchronize."));
        }
        testAndPerformCode.addAll(collectedUpdates);
        testAndPerformCode.addAll(collectedNoUpdates);
    }

    /**
     * Generate PLC code that tests for an enabled edge in the given automaton.
     *
     * <p>
     * The edge test code generator is shared between syncers and senders/receivers automata. The different behavior is
     * controlled by means of the {@code autVar} variable.
     * <ul>
     * <li>If it is {@code null}, a test for a syncer automaton should be generated, the automaton must always have an
     * enabled edge or the transition is not possible.</li>
     * <li>If is not {@code null}, a test is generated for a receiver or sender automaton. The {@code autVar} contains
     * the elected automaton (with {@code 0} meaning that no automaton has been selected yet). In this case an
     * additional test for {@code autVar} being {@code 0} is generated as additional pre-condition.</li>
     * </ul>
     * </p>
     *
     * <p>
     * If an enabled edge is found at PLC runtime, the code sets {@code autVar} to {@code autIndex} if {@code autVar} is
     * not {@code null}, and sets {@code edgeVar} to the 1-based index of the edge that is found to be enabled.
     * </p>
     *
     * @param event Event being tried.
     * @param transAut Automaton to generate test code for.
     * @param autIndex Automaton index that indicates the given automaton. Has negative value if not used.
     * @param autVar PLC variable that stores the automaton found while testing for enabled edges. Is {@code null} if
     *     there is no need to store an automaton index.
     * @param edgeVar For senders, receivers and syncers the PLC variable stores the edge in the automaton indicated by
     *     {@code autVar}. For syncers, it indicates the edge in a syncer automaton, as the edge variable implicitly
     *     also indicates the automaton. Is 1-based to be consistent in how numbers are assigned.
     * @param eventEnabledVar PLC variable expressing if the event is enabled.
     * @return The generated code.
     */
    private List<PlcStatement> generateEdgesTestCode(Event event, TransitionAutomaton transAut, int autIndex,
            PlcBasicVariable autVar, PlcBasicVariable edgeVar, PlcBasicVariable eventEnabledVar)
    {
        List<PlcStatement> testCode = list();
        PlcSelectionStatement selStat = null;

        // Explain the goal of testing the edges.
        testCode.add(genEdgeTestsDocumentation(event, transAut));

        // Generate the checks and assign their findings to edge and/or automaton variables.
        int edgeIndex = 1;
        for (TransitionEdge edge: transAut.transitionEdges) {
            final int finalEdgeIndex = edgeIndex; // Java wants a copy.
            Supplier<List<PlcStatement>> thenStats = () -> {
                if (autVar != null) {
                    // autVar := autIndex; edgeVar := edgeIndex;
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
            // IF autVar = 0 THEN <test edges of this automaton> END_IF;
            PlcExpression guard = generateCompareVarWithVal(autVar, 0);
            return List.of(generateIfGuardThenCode(guard, testCode));
        } else {
            // If no enabled edge was found for this automaton, the event is immediately not enabled.
            PlcCommentLine explainFail = new PlcCommentLine(
                    "The automaton has no edge with a true guard, skip to the next event.");
            if (selStat == null) {
                testCode.add(explainFail);
                testCode.add(generatePlcBoolAssignment(eventEnabledVar, false));
            } else {
                selStat.elseStats.add(explainFail);
                selStat.elseStats.add(generatePlcBoolAssignment(eventEnabledVar, false));
            }
            return testCode;
        }
    }

    /**
     * Get the variable that tracks the selected edge of the provided automaton.
     *
     * @param aut Automaton to use for finding the edge variable.
     * @return The edge variable.
     */
    private PlcBasicVariable getAutomatonEdgeVariable(Automaton aut) {
        PlcBasicVariable edgeVariable = edgeSelectionVariables.get(aut);
        Assert.notNull(edgeVariable);
        return edgeVariable;
    }

    /**
     * Generate a comment block explaining the test performed at an automaton.
     *
     * @param event Event being tried.
     * @param transAut Automation transition information to show.
     * @return The generated comment block.
     */
    private PlcCommentBlock genEdgeTestsDocumentation(Event event, TransitionAutomaton transAut) {
        CodeBox box = new MemoryCodeBox();
        String edgePluralText = (transAut.transitionEdges.size() != 1) ? "s" : "";
        box.add("Testing edge%s of automaton \"%s\" to %s at event \"%s\".", edgePluralText,
                getAbsName(transAut.aut, false), transAut.purpose.purposeText, getAbsName(event, false));

        switch (transAut.purpose) {
            case MONITOR:
                throw new AssertionError("Unexpected test-doc request from a monitor.");
            case RECEIVER:
            case SENDER: {
                String kindText = (transAut.purpose == TransAutPurpose.SENDER) ? "sending" : "receiving";
                box.add("One %s automaton must have an edge with a true guard to allow the event.", kindText);
                break;
            }
            case SYNCER:
                box.add("This automaton must have an edge with a true guard to allow the event.");
                break;
            default:
                throw new AssertionError("Unknown purpose \"" + transAut.purpose + "\" encountered.");
        }
        box.add();
        box.add("Edge%s being tested:", edgePluralText);
        Location lastLoc = null;
        for (TransitionEdge transEdge: transAut.transitionEdges) {
            if (transEdge.sourceLoc != lastLoc) {
                lastLoc = transEdge.sourceLoc;
                if (lastLoc.getName() == null) {
                    box.add("  - Location:");
                } else {
                    box.add("  - Location \"%s\":", lastLoc.getName());
                }
            }
            box.add("     - %s edge in the location", toOrdinal(transEdge.edgeNumber));
        }
        if (lastLoc == null) {
            box.add("  - No edges found, event \"%s\" will never occur!:", getAbsName(event, false));
        }
        return new PlcCommentBlock(box.getLines());
    }

    /**
     * Generate PLC code that selects the edge stored in {@code edgeVar} and 'performs' the edge. If needed, compute the
     * sent value and store it into {@code channelValueVar}. Finally, perform the updates if they exist.
     *
     * @param transAut Automaton to generate perform code for.
     * @param edgeVar Variable containing the 1-based index of the selected edge to perform.
     * @param channelValueVar Variable that must be assigned the sent value of the channel by the selected edge. Use
     *     {@code null} if not in channel value context.
     * @return Generated PLC code that selects and performs the selected edge. Is empty if there is no PLC code needed
     *     to perform the edge.
     */
    private List<PlcStatement> generateAutPerformCode(TransitionAutomaton transAut, PlcBasicVariable edgeVar,
            PlcBasicVariable channelValueVar)
    {
        List<PlcStatement> performCode = list();
        PlcSelectionStatement selStat = null;

        // Perform the selected edge, if not empty.
        int edgeIndex = 1;
        for (TransitionEdge edge: transAut.transitionEdges) {
            // Generate code that performs the edge if something needs to be done.
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
                // Add "IF edgeVar = edgeIndex THEN <compute channelValue if needed, and perform updates>" branch.
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
     * @return The generated statements.
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
     * Generate PLC code for an 'if' update.
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
     * Generate PLC code for an assignment update.
     *
     * @param lhs Left side addressable to assign to.
     * @param rhs Right side value to assign.
     * @param statements Generated PLC statements. Is extended in-place.
     */
    private void genUpdateAssignment(Expression lhs, Expression rhs, List<PlcStatement> statements) {
        // TODO: Current code makes a copy of the old value for every assigned variable, even if it can be avoided.

        // Test for the simple case of a single left side variable.
        if (!(lhs instanceof TupleExpression lhsTuple)) {
            // Left side is a single destination, (possibly partially) assign the right side to it.
            ContVariable contvar;
            ExprAddressableResult lhsResult;
            if (lhs instanceof ProjectionExpression pe) {
                lhsResult = mainExprGen.convertProjectedAddressable(pe);
                contvar = null;
            } else if (lhs instanceof ContVariableExpression ce) {
                lhsResult = mainExprGen.convertVariableAddressable(lhs);
                contvar = ce.getVariable();
            } else {
                lhsResult = mainExprGen.convertVariableAddressable(lhs);
                contvar = null;
            }
            statements.addAll(lhsResult.code);
            lhsResult.releaseCodeVariables();
            genAssignExpr(lhsResult.value, rhs, statements);
            lhsResult.releaseValueVariables();

            // For continuous variable assignment, also update its timer block.
            if (contvar != null) {
                statements.addAll(
                        target.getContinuousVariablesGenerator().getPlcTimerCodeGen(contvar).generateAssignPreset());
            }
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
        PlcBasicVariable rhsVariable = mainExprGen.getTempVariable("rightValue", rhs.getType());

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
     * Generate PLC code for an assignment update, with projected right side.
     *
     * @param lhs Left side to assign to.
     * @param rhsVariable Unprojected right side value in a variable to assign.
     * @param rhsProjections Projection sequence to apply to the {@code rhs} value before assigning to {@code lhs}.
     * @param statements Generated PLC statements. Is extended in-place.
     */
    private void genUpdateAssignment(Expression lhs, PlcBasicVariable rhsVariable,
            List<PlcStructProjection> rhsProjections, List<PlcStatement> statements)
    {
        // Conceptually: "lhs := rhs.rhsProjections" where lhs may be a tuple literal.

        // Test for the simple case of a single left side.
        if (!(lhs instanceof TupleExpression lhsTuple)) {
            // Left side is a single destination, the entire right side must be assigned to it.
            ExprAddressableResult lhsResult = mainExprGen.convertVariableAddressable(lhs);
            statements.addAll(lhsResult.code);
            lhsResult.releaseCodeVariables();

            // Append rhs projections after the rhs value so only a part of the rhs is assigned to the lhs.
            PlcVarExpression rhsValue = new PlcVarExpression(rhsVariable, cast(rhsProjections));
            statements.add(new PlcAssignmentStatement(lhsResult.value, rhsValue));
            lhsResult.releaseValueVariables();

            // For continuous variable assignment, also update its timer block.
            if (lhs instanceof ContVariableExpression cve) {
                statements.addAll(target.getContinuousVariablesGenerator().getPlcTimerCodeGen(cve.getVariable())
                        .generateAssignPreset());
            }
            return;
        }

        // Left side is a tuple literal, right side is a single value, split into more partial assignments. This must be
        // done recursively due to nesting of tuples at the left side, for example:
        // ((u, v), w) := x
        // -> (u, v) := x.a and w := x.b
        // -> u := x.a.p and v := x.a.q and w := x.b
        // and then each assignment can be converted. Note how the sequence of projections at the right side gets
        // extended on each recursion level. This is handled in the "projs" variable below.
        PlcType lhsType = target.getTypeGenerator().convertType(lhs.getType());
        PlcStructType lhsStructType = target.getTypeGenerator().getStructureType(lhsType);

        for (int idx = 0; idx < lhsStructType.fields.size(); idx++) {
            // Make a new projection list "valueProjections.<lhs-field>".
            List<PlcStructProjection> projs = listc(rhsProjections.size() + 1);
            projs.addAll(rhsProjections);
            projs.add(new PlcStructProjection(lhsStructType.fields.get(idx).fieldName));
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
     * Generate an expression that compares the given variable with the given literal value using equality.
     *
     * @param variable Variable to compare.
     * @param value Literal value to compare against.
     * @return The generated expression.
     */
    private PlcExpression generateCompareVarWithVal(PlcBasicVariable variable, int value) {
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
    private PlcAssignmentStatement generatePlcIntAssignment(PlcBasicVariable variable, int value) {
        return new PlcAssignmentStatement(variable, new PlcIntLiteral(value));
    }

    /**
     * Generate an assignment statement of the given value to the given variable.
     *
     * @param variable Variable to assign.
     * @param value Value to assign to the variable.
     * @return The generated statement.
     */
    private PlcAssignmentStatement generatePlcBoolAssignment(PlcBasicVariable variable, boolean value) {
        return new PlcAssignmentStatement(variable, new PlcBoolLiteral(value));
    }
}
