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
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Numbers.toOrdinal;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.escet.cif.common.CifDocAnnotationFormatter;
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
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
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
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
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
     * For each automaton with at least one edge outside monitor context, the data of the variable that tracks the
     * selected edge to perform for its automaton.
     */
    private final Map<Automaton, EdgeVariableData> edgeSelectionVarData = map();

    /**
     * Edge selection variables that exist in the current scope. Only valid when code is being generated. Is
     * {@code null} otherwise.
     *
     * <p>
     * Use the {@link #getAutomatonEdgeVariable} method to query this map.
     * </p>
     */
    private Map<Automaton, PlcDataVariable> edgeSelectionVariables;

    /** Generation of standard PLC functions. */
    private final PlcFunctionAppls funcAppls;

    /**
     * Constructor of the {@link DefaultTransitionGenerator} class.
     *
     * @param target PLC target to generate code for.
     */
    public DefaultTransitionGenerator(PlcTarget target) {
        this.target = target;
        funcAppls = new PlcFunctionAppls(target);
    }

    @Override
    public void setup(List<CifEventTransition> allEventTransitions) {
        setupEdgeVariableData(allEventTransitions);
    }

    /**
     * Construct edge variable data for all automata of all event transitions.
     *
     * @param allEventTransitions All event transitions.
     */
    void setupEdgeVariableData(List<CifEventTransition> allEventTransitions) {
        // For all automata, find the maximum number of edges to examine for an event.
        //
        // Skip cases without edges, monitors don't need edge variables. This leads to not having edge variables when
        // there are no edges that need them.
        Map<Automaton, Integer> maxEventEdges = map(); // Max number of edges for an event for all automata.
        for (CifEventTransition evtTrans: allEventTransitions) {
            for (TransitionAutomaton transAut: evtTrans.senders) {
                if (!transAut.transitionEdges.isEmpty()) {
                    maxEventEdges.merge(transAut.aut, transAut.transitionEdges.size(), (x, y) -> Math.max(x, y));
                }
            }
            for (TransitionAutomaton transAut: evtTrans.receivers) {
                if (!transAut.transitionEdges.isEmpty()) {
                    maxEventEdges.merge(transAut.aut, transAut.transitionEdges.size(), (x, y) -> Math.max(x, y));
                }
            }
            for (TransitionAutomaton transAut: evtTrans.syncers) {
                if (!transAut.transitionEdges.isEmpty()) {
                    maxEventEdges.merge(transAut.aut, transAut.transitionEdges.size(), (x, y) -> Math.max(x, y));
                }
            }
            // Monitors do not need edge tracking since the first enabled edge is immediately taken.
        }

        NameGenerator nameGen = target.getNameGenerator();

        // Construct edge variables.
        edgeSelectionVarData.clear();
        for (Entry<Automaton, Integer> entry: maxEventEdges.entrySet()) {
            Automaton aut = entry.getKey();
            int numEdges = entry.getValue(); // Maximum number of different edge indices for the automaton.
            Assert.check(numEdges > 0); // Pure monitor automata are not allowed.

            // Derive a variable type for the edge variable.
            PlcType varType = PlcElementaryType.getTypeByRequiredCount(numEdges, target.getSupportedBitStringTypes());

            // Construct the edge variable itself, and store it for future use.
            String variableName = "edge_" + nameGen.generateGlobalNames(Set.of("edge_"), getAbsName(aut, false), false);
            target.getCodeStorage().setAutomatonEdgeVariableName(aut, variableName);
            edgeSelectionVarData.put(aut, new EdgeVariableData(variableName, varType));
        }
    }

    @Override
    public List<List<PlcStatement>> generate(List<List<CifEventTransition>> transSeqs,
            ExprGenerator exprGen, PlcBasicVariable isProgressVar)
    {
        // Construct the edge selection variables, convert the event transition collections in the same structure as
        // provided, and return the generated code.
        edgeSelectionVariables = createEdgeVariables(transSeqs, exprGen);
        List<List<PlcStatement>> statementSequences = transSeqs.stream()
                .map(tl -> generateCode(isProgressVar, tl, exprGen)).toList();
        edgeSelectionVariables = null;
        return statementSequences;
    }

    /**
     * Construct edge variables in the scope of the expression generator for the automata in the provided event
     * transitions. Automata that do not need an edge variable are not in the returned map.
     *
     * @param transSeqs Event transition sequences to examine.
     * @param exprGen Expression generator to add the created variables to the scope.
     * @return The constructed edge variables ordered by automaton.
     */
    private Map<Automaton, PlcDataVariable> createEdgeVariables(List<List<CifEventTransition>> transSeqs,
            ExprGenerator exprGen)
    {
        // Construct edge variable for the automata as needed.
        Map<Automaton, PlcDataVariable> edgeVariables = map();
        for (List<CifEventTransition> transSeq: transSeqs) {
            for (CifEventTransition eventTrans: transSeq) {
                addEdgeVariables(eventTrans.senders, edgeVariables);
                addEdgeVariables(eventTrans.receivers, edgeVariables);
                addEdgeVariables(eventTrans.syncers, edgeVariables);
            }
        }

        // Add the created variables to the scope.
        for (PlcDataVariable edgeVar: edgeVariables.values()) {
            exprGen.addLocalVariable(edgeVar, true);
        }

        return edgeVariables;
    }

    /**
     * Construct an edge variable for the given automaton if it needs one and doesn't have one already.
     *
     * @param transAuts Automaton transitions to analyze.
     * @param edgeVariables Already created edge variables ordered by automaton. Is updated in-plac.
     */
    private void addEdgeVariables(List<TransitionAutomaton> transAuts, Map<Automaton, PlcDataVariable> edgeVariables) {
        for (TransitionAutomaton transAut: transAuts) {
            EdgeVariableData edgeVarData = edgeSelectionVarData.get(transAut.aut);
            if (edgeVarData != null) {
                edgeVariables.computeIfAbsent(transAut.aut, aut -> edgeVarData.makeVariable(target));
            }
        }
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
     * @param isProgressVar Variable to communicate in the generated code that at least one transition has been taken in
     *     the generated statement sequence.
     * @param eventTransitions The event transitions to generate.
     * @param exprGen Expression generator for the scope of the generated code.
     * @return The generated PLC event transition code. Is empty if there are no event transitions to convert.
     */
    List<PlcStatement> generateCode(PlcBasicVariable isProgressVar, List<CifEventTransition> eventTransitions,
            ExprGenerator exprGen)
    {
        // TODO Currently code generation is straight forward, it generates correct code for the general case. There are
        // heaps of improvements possible if you recognize specific cases like 1 automaton, 1 edge, 0 senders, better
        // names for variables, etc.

        // As all transition code is generated in main program context, only one generated statements list exists and
        // various variables that store decisions in the process can be re-used between different events.
        boolean addEmptyLineBefore = false;
        List<PlcStatement> statements = list();
        for (CifEventTransition eventTransition: eventTransitions) {
            statements.addAll(generateEventTransitionCode(eventTransition, addEmptyLineBefore, isProgressVar, exprGen));
            addEmptyLineBefore = true;
        }

        // Return the generated event transition code.
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
     * @param exprGen Expression generator for the scope of the generated code.
     * @return The generated code for testing and performing the event in the PLC.
     */
    private List<PlcStatement> generateEventTransitionCode(CifEventTransition eventTransition, boolean prependEmptyLine,
            PlcBasicVariable isProgressVar, ExprGenerator exprGen)
    {
        // Both code parts visit the same automata and the check or perform the same edges. For this reason it makes
        // sense to generate both test-code and perform-code at the same time for each automaton.
        // At the end of this method both parts are combined.
        List<PlcStatement> testCode = list(); // Code that decides whether the event is enabled.
        List<PlcStatement> performCode = list(); // Code that performs the event assuming the event is enabled.
        List<PlcBasicVariable> createdTempVariables = list();

        // Obtain the 'event is enabled' variable.
        PlcBasicVariable eventEnabledVar = exprGen.getTempVariable("eventEnabled", PlcElementaryType.BOOL_TYPE);
        createdTempVariables.add(eventEnabledVar);

        // So far, no test code has been generated that may set 'eventEnabled' to false.
        boolean eventEnabledAlwaysHolds = true;

        // Generate the header of the transition code for the event. Initialize the 'event is enabled' variable.
        if (prependEmptyLine) {
            testCode.add(new PlcCommentLine(null));
        }
        testCode.add(genAnnounceEventBeingTried(eventTransition));
        testCode.add(new PlcAssignmentStatement(eventEnabledVar, new PlcBoolLiteral(true)));

        // Performing the event implies progress is made.
        performCode.add(new PlcAssignmentStatement(isProgressVar, new PlcBoolLiteral(true)));
        CifDataProvider performProvider = generateCopiedState(eventTransition.collectAssignedVariables(), performCode,
                exprGen, createdTempVariables);

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

                performProvideText = "Perform assignments of the selected providing automaton.";
                performAcceptText = "Perform assignments of the selected accepting automaton.";
            } else {
                channelValueVar = exprGen.getTempVariable("channelValue", channelType);
                createdTempVariables.add(channelValueVar);

                performProvideText = "Store the provided value and perform assignments of the selected providing "
                        + "automaton.";
                performAcceptText = "Deliver the provided value and perform assignments of the selected accepting "
                        + "automaton.";
            }

            // Handle senders.
            testCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Try to find a sender automaton that provides a value.")));
            performCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT, List.of(performProvideText)));

            generateSendReceiveCode(eventTransition.event, eventTransition.senders, testCode, performProvider,
                    performCode, TransAutPurpose.SENDER, createdTempVariables, eventEnabledVar, channelValueVar,
                    eventEnabledAlwaysHolds, exprGen);

            eventEnabledAlwaysHolds = false; // At least one sender tested; event is no longer guaranteed to be enabled.

            // Handle receivers.
            testCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Try to find a receiver automaton that accepts a value.")));
            performCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT, List.of(performAcceptText)));

            exprGen.setChannelValueVariable(channelValueVar);
            generateSendReceiveCode(eventTransition.event, eventTransition.receivers, testCode, performProvider,
                    performCode, TransAutPurpose.RECEIVER, createdTempVariables, eventEnabledVar, null,
                    eventEnabledAlwaysHolds, exprGen);
            exprGen.setChannelValueVariable(null);
        }

        // Handle syncers.
        if (!eventTransition.syncers.isEmpty()) {
            testCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Check each synchronizing automaton for having an edge with a true guard.")));
            performCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Perform the assignments of each synchronizing automaton.")));
            generateSyncCode(eventTransition.event, eventTransition.syncers, testCode, performProvider, performCode,
                    createdTempVariables, eventEnabledVar, eventEnabledAlwaysHolds, exprGen);
        }

        // Handle monitors. Only generates perform code since it doesn't influence enabledness of the event transition.
        if (!eventTransition.monitors.isEmpty()) {
            performCode.add(new PlcCommentBlock(AUTOMATA_COMMENT_STARCOUNT,
                    List.of("Perform the assignments of each optionally synchronizing automaton.")));
            generateMonitorCode(eventTransition.monitors, performProvider, performCode, createdTempVariables, exprGen);
        }

        // Construct the complete PLC code for the event transition by concatenating test and conditional perform code.
        List<PlcStatement> resultCode = testCode;
        PlcExpression guard = new PlcVarExpression(eventEnabledVar);

        resultCode.add(new PlcCommentLine(
                fmt("All checks have been done. If variable \"%s\" still holds, event \"%s\" can occur.",
                        eventEnabledVar.varName, getAbsName(eventTransition.event, false))));
        resultCode.add(generateIfGuardThenCode(guard, performCode));

        exprGen.releaseTempVariables(createdTempVariables);
        return resultCode;
    }

    /**
     * Construct a comment block stating the event being tried, together with the automata involved in the attempt.
     *
     * @param eventTrans Event transition being tried.
     * @return Comment block stating the event, and listing what is needed for the event to occur.
     */
    private PlcCommentBlock genAnnounceEventBeingTried(CifEventTransition eventTrans) {
        CifDocAnnotationFormatter eventDocFormatter, sendRecvAutDocFormatter, syncMonAutDocFormatter;
        eventDocFormatter = new CifDocAnnotationFormatter(null, null, null, List.of(""), null);
        sendRecvAutDocFormatter = new CifDocAnnotationFormatter(null, null, "     ", List.of(""), null);
        syncMonAutDocFormatter = new CifDocAnnotationFormatter(null, null, "  ", List.of(""), null);

        TextTopics topics = new TextTopics();
        topics.add("Try to perform %s.", DocumentingSupport.getDescription(eventTrans.event));
        topics.addAll(eventDocFormatter.formatDocs(eventTrans.event));

        CifType eventType = eventTrans.event.getType();
        if (eventType != null) {
            boolean transferValue = !(eventType instanceof VoidType);

            // List senders.
            topics.ensureEmptyAtEnd();
            if (eventTrans.senders.isEmpty()) {
                topics.add("- An automaton that must send a value is missing, so the event cannot occur.");
            } else {
                if (transferValue) {
                    topics.add("- One automaton must send a value.");
                } else {
                    topics.add("- One automaton must send a value (although no data is actually transferred).");
                }
                for (TransitionAutomaton transAut: eventTrans.senders) {
                    topics.add("   - Automaton \"%s\" may send a value.", getAbsName(transAut.aut, false));
                    topics.addAll(sendRecvAutDocFormatter.formatDocs(transAut.aut));
                }
            }

            // List receivers.
            topics.ensureEmptyAtEnd();
            if (eventTrans.receivers.isEmpty()) {
                topics.add("- An automaton that must receive a value is missing, so the event cannot occur.");
            } else {
                if (transferValue) {
                    topics.add("- One automaton must receive a value.");
                } else {
                    topics.add("- One automaton must receive a value (although no data is actually transferred).");
                }
                for (TransitionAutomaton transAut: eventTrans.receivers) {
                    topics.add("   - Automaton \"%s\" may receive a value.", getAbsName(transAut.aut, false));
                    topics.addAll(sendRecvAutDocFormatter.formatDocs(transAut.aut));
                }
            }
        }

        // List syncers.
        topics.ensureEmptyAtEnd();
        for (TransitionAutomaton transAut: eventTrans.syncers) {
            topics.add("- Automaton \"%s\" must always synchronize.", getAbsName(transAut.aut, false));
            topics.addAll(syncMonAutDocFormatter.formatDocs(transAut.aut));
        }

        // List monitors.
        topics.ensureEmptyAtEnd();
        for (TransitionAutomaton transAut: eventTrans.monitors) {
            topics.add("- Automaton \"%s\" may synchronize.", getAbsName(transAut.aut, false));
            topics.addAll(syncMonAutDocFormatter.formatDocs(transAut.aut));
        }

        topics.dropEmptyAtEnd();
        return new PlcCommentBlock(EVENT_COMMENT_STARCOUNT, topics.getLines());
    }

    /**
     * Generate PLC code that copies the current value of all variables that may be assigned while performing the event.
     * Also, construct a CIF data provider that redirects reading of variables to the created copies.
     *
     * @param assignedVariables Variables that may be assigned while performing the event.
     * @param codeStorage Storage for the generated PLC code.
     * @param exprGen Expression generator for the scope of the generated code.
     * @param createdTempVariables Created temporary variables to test and perform an event. Is extended in-place with
     *     introduced variables to temporarily store current state.
     * @return A CIF data provider that redirects variable value queries in expression conversion to the copied state.
     *     Is {@code null} if nothing needs to be copied.
     */
    private CifDataProvider generateCopiedState(Set<Declaration> assignedVariables, List<PlcStatement> codeStorage,
            ExprGenerator exprGen, List<PlcBasicVariable> createdTempVariables)
    {
        if (assignedVariables.isEmpty()) {
            return null;
        }

        // Sort declarations on absolute name.
        List<Declaration> assignedVarList = set2list(assignedVariables);
        Collections.sort(assignedVarList, Comparator.comparing(v -> getAbsName(v, false)));

        // Add comment to the generated code to explain the partial state copy.
        if (!assignedVariables.isEmpty()) {
            codeStorage.add(new PlcCommentLine("Make temporary copies of assigned variables to preserve the old "
                    + "values while assigning new values."));
        }

        // Generate code to copy the old value of assigned variables so they survive assignment with a new value, and
        // setup a redirect map for a new data provider, to make the expression generator use the copied state for
        // reading.
        CifDataProvider cifDataProvider = exprGen.getScopeCifDataProvider();
        Map<Declaration, PlcExpression> redirectedDecls = map();
        for (Declaration assignedVar: assignedVarList) {
            // TODO If the variable is only written, a copy is not needed.
            if (assignedVar instanceof DiscVariable dv) {
                PlcBasicVariable currentVar = exprGen.getTempVariable("current_" + getAbsName(dv, false),
                        dv.getType());
                createdTempVariables.add(currentVar);
                redirectedDecls.put(dv, new PlcVarExpression(currentVar));
                codeStorage.add(new PlcAssignmentStatement(new PlcVarExpression(currentVar),
                        cifDataProvider.getValueForDiscVar(dv)));
            } else if (assignedVar instanceof ContVariable cv) {
                PlcBasicVariable currentVar = exprGen.getTempVariable("current_" + getAbsName(cv, false),
                        target.getStdRealType());
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
     * @param purpose Purpose of the given automaton.
     * @param createdTempVariables Tracking storage of created temporary variables.
     * @param eventEnabledVar PLC variable expressing if the event is enabled.
     * @param channelValueVar PLC variable storing the channel value. Is {@code null} when not sending a value (either
     *     receiving a value or it is a void channel).
     * @param eventEnabledAlwaysHolds Whether it is known that 'eventEnabled' in tests is always true at runtime.
     * @param exprGen Expression generator for the scope of the generated code.
     */
    private void generateSendReceiveCode(Event event, List<TransitionAutomaton> autTransitions,
            List<PlcStatement> testCode, CifDataProvider performProvider, List<PlcStatement> performCode,
            TransAutPurpose purpose, List<PlcBasicVariable> createdTempVariables, PlcBasicVariable eventEnabledVar,
            PlcBasicVariable channelValueVar, boolean eventEnabledAlwaysHolds, ExprGenerator exprGen)
    {
        // Setup comment texts.
        String varPrefix, failResultText;
        if (purpose == TransAutPurpose.SENDER) {
            varPrefix = "sender";
            failResultText = "Failed to find an automaton that provides a value. Skip to the next event.";
        } else {
            varPrefix = "receiver";
            failResultText = "Failed to find an automaton that accepts a value. Skip to the next event.";
        }

        List<PlcStatement> autTestCode = list(); // Intermediate storage for test code.

        PlcBasicVariable autVar = exprGen.getTempVariable(varPrefix + "Aut", PlcElementaryType.DINT_TYPE);
        createdTempVariables.add(autVar);
        autTestCode.add(generatePlcIntAssignment(autVar, 0));

        // Outer dispatch selection statement that selects on the chosen automaton, and performs selection performing of
        // the edges within each dispatched branch. The latter is generated below.
        PlcSelectionStatement performSelectStat = new PlcSelectionStatement();
        List<PlcStatement> collectedNoUpdates = list(); // Comment lines about automata that have no updates to perform.

        // Generate code that tests and performs sending or receiving a value to the channel.
        int autIndex = 1; // Index 0 is used to denote no automaton has been selected.
        for (TransitionAutomaton transAut: autTransitions) {
            // Get the variable that stores the selected edge index for the automaton. Is 'null' if there is no edge
            // variable.
            PlcBasicVariable edgeVar = getAutomatonEdgeVariable(transAut.aut);

            // Generate edge testing code.
            autTestCode.addAll(
                    generateEdgesTestCode(event, transAut, autIndex, autVar, edgeVar, eventEnabledVar, exprGen));

            // Generate the edge selection and performing code, and add it as a branch on the automaton to
            // 'performSelectStat'.
            exprGen.setCurrentCifDataProvider(performProvider); // Switch to using stored variables state.
            String autCommentUpdateText = fmt("Automaton \"%s\" was selected.", getAbsName(transAut.aut, false));
            List<PlcStatement> updateCode = generateSelectedEdgePerformCode(transAut, edgeVar, channelValueVar,
                    autCommentUpdateText, exprGen, collectedNoUpdates);
            if (!updateCode.isEmpty()) {
                performSelectStat.condChoices
                        .add(new PlcSelectChoice(generateCompareVarWithVal(autVar, autIndex), updateCode));
            }
            exprGen.setCurrentCifDataProvider(null); // And switch back to normal variable access.

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
            PlcCommentLine explanation = new PlcCommentLine(failResultText);
            PlcAssignmentStatement assignment = generatePlcBoolAssignment(eventEnabledVar, false);
            autTestCode.add(generateIfGuardThenCode(guard, list(explanation, assignment)));
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
     * @param exprGen Expression generator for the scope of the generated code.
     */
    private void generateSyncCode(Event event, List<TransitionAutomaton> autTransitions, List<PlcStatement> testCode,
            CifDataProvider performProvider, List<PlcStatement> performCode,
            List<PlcBasicVariable> createdTempVariables, PlcBasicVariable eventEnabledVar,
            boolean eventEnabledAlwaysHolds, ExprGenerator exprGen)
    {
        List<PlcStatement> collectedNoUpdates = list(); // Comments about automata without updates.
        List<PlcStatement> collectedUpdates = list(); // Update-code of automata with updates.

        // Generate code that tests and performs synchronizing on the event.
        for (TransitionAutomaton transAut: autTransitions) {
            // Get the variable that stores the selected edge index for the automaton. Is 'null' if there is no edge
            // variable.
            PlcBasicVariable autEdgeVar = getAutomatonEdgeVariable(transAut.aut);

            // Initialize intermediate storage for test code of the automaton.
            List<PlcStatement> autTestCode = list();

            // Generate edge testing code.
            autTestCode.addAll(generateEdgesTestCode(event, transAut, -1, null, autEdgeVar, eventEnabledVar, exprGen));

            // Generate the edge selection and performing code, and add it as a branch on the automaton.
            exprGen.setCurrentCifDataProvider(performProvider); // Switch to using stored variables state.
            String autCommentUpdateText = fmt("Perform assignments of automaton \"%s\".",
                    getAbsName(transAut.aut, false));
            collectedUpdates.addAll(generateSelectedEdgePerformCode(transAut, autEdgeVar, null, autCommentUpdateText,
                    exprGen, collectedNoUpdates));
            exprGen.setCurrentCifDataProvider(null); // And switch back to normal variable access.

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
     * @param exprGen Expression generator for the scope of the generated code.
     */
    private void generateMonitorCode(List<TransitionAutomaton> autTransitions, CifDataProvider performProvider,
            List<PlcStatement> testAndPerformCode, List<PlcBasicVariable> createdTempVariables, ExprGenerator exprGen)
    {
        // Check if anything must be done at all.
        boolean updatesFound = false;
        List<PlcStatement> collectedNoUpdates = list(); // Comments about automata without updates.
        for (TransitionAutomaton transAut: autTransitions) {
            // If there is nothing to update for this automaton, create a comment saying that.
            if (!transAut.hasUpdates()) {
                collectedNoUpdates.add(new PlcCommentLine(
                        fmt("Automaton \"%s\" has no assignments to perform.", getAbsName(transAut.aut, false))));
            } else {
                updatesFound = true;
            }
        }
        if (!updatesFound) {
            testAndPerformCode
                    .add(new PlcCommentLine("There are no assignments to perform for automata that may synchronize."));
            testAndPerformCode.addAll(collectedNoUpdates);
            return;
        }

        // Regular case, updates must be done.
        exprGen.setCurrentCifDataProvider(performProvider); // Switch to using stored variables state.
        for (TransitionAutomaton transAut: autTransitions) {
            if (!transAut.hasUpdates()) { // Skip the automata that have nothing to update.
                continue;
            }

            // Updates for this automaton must be done.
            List<PlcStatement> updates = list();
            PlcSelectionStatement selStat = null;

            boolean addedAutomatonHeaderText = false;
            if (transAut.hasGuards()) {
                // Edge selection is needed. Add the automaton information above the edge selection code.
                List<String> autHeaderLines = generateAutomatonHeaderForUpdates(transAut.aut);
                if (autHeaderLines.size() == 1) {
                    updates.add(new PlcCommentLine(first(autHeaderLines)));
                } else {
                    updates.add(new PlcCommentBlock(autHeaderLines));
                }
                addedAutomatonHeaderText = true; // Don't add the automaton header again.
            }
            // else, automaton header is added as part of the comments of the first edge with updates.

            Location lastLoc = null;
            for (TransitionEdge transEdge: transAut.transitionEdges) {
                if (transEdge.hasUpdates()) {
                    // Generate the 'then' statements for the edge.
                    final boolean doAutPrint = !addedAutomatonHeaderText;
                    final boolean doLocPrint = transEdge.sourceLoc != lastLoc;
                    Supplier<List<PlcStatement>> thenStats = () -> {
                        return genMonitorUpdateEdge(transAut, transEdge, doAutPrint, doLocPrint, exprGen);
                    };

                    // Update variables that control generation of the documentation.
                    addedAutomatonHeaderText = true;
                    lastLoc = transEdge.sourceLoc;

                    // Add an "IF <guards> THEN <perform-updates>" branch.
                    selStat = exprGen.addBranch(transEdge.guards, thenStats, selStat, updates);
                }
            }

            testAndPerformCode.addAll(updates);
        }
        exprGen.setCurrentCifDataProvider(null); // And switch back to normal variable access.

        testAndPerformCode.addAll(collectedNoUpdates);
    }

    /**
     * Function to generate a test and possibly update branch for an edge in a monitor automaton.
     *
     * @param transAut Automaton information.
     * @param transEdge Edge to perform.
     * @param doAutDocPrint Whether to add documentation about the automaton above the edge updates in the PLC code.
     * @param doLocDocPrint Whether to add documentation about the location above the edge updates in the PLC code.
     * @param exprGen Expression generator for the scope of the generated code.
     * @return The generated PLC statements.
     */
    private List<PlcStatement> genMonitorUpdateEdge(TransitionAutomaton transAut, TransitionEdge transEdge,
            boolean doAutDocPrint, boolean doLocDocPrint, ExprGenerator exprGen)
    {
        List<PlcStatement> stats = list();

        TextTopics topics;
        // If requested, output documentation about the automaton.
        if (doAutDocPrint || doLocDocPrint) {
            CifDocAnnotationFormatter docFormatter = new CifDocAnnotationFormatter(null, null, null, List.of(""), null);
            topics = new TextTopics();

            if (doAutDocPrint) {
                topics.addAll(generateAutomatonHeaderForUpdates(transAut.aut));
                topics.ensureEmptyAtEnd();
            }
            if (doLocDocPrint) {
                Location loc = transEdge.sourceLoc;
                if (loc.getName() == null) {
                    topics.add("Location:");
                    topics.addAll(docFormatter.formatDocs(loc));
                } else {
                    topics.add("Location \"%s\":", loc.getName());
                    topics.addAll(docFormatter.formatDocs(loc));
                }
            }
        } else {
            topics = null;
        }

        // Generate the edge updates and return the generated code.
        stats.addAll(generateEdgeUpdates(transAut.aut, transEdge, exprGen, topics));
        return stats;
    }

    /**
     * Generate the text to describe the automaton being updated.
     *
     * @param aut Automaton to describe.
     * @return The produced text.
     */
    private List<String> generateAutomatonHeaderForUpdates(Automaton aut) {
        CifDocAnnotationFormatter docFormatter = new CifDocAnnotationFormatter(null, null, null, List.of(""), null);
        TextTopics topics = new TextTopics();
        topics.add(fmt("Perform assignments of automaton \"%s\".", getAbsName(aut, false)));
        topics.addAll(docFormatter.formatDocs(aut));
        topics.dropEmptyAtEnd();
        return topics.getLines();
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
     * not {@code null}, and sets {@code edgeVar} to the index of the edge that is found to be enabled.
     * </p>
     *
     * @param event Event being tried.
     * @param transAut Automaton to generate test code for.
     * @param autIndex Automaton index that indicates the given automaton. Has negative value if not used.
     * @param autVar PLC variable that stores the automaton found while testing for enabled edges. Is {@code null} if
     *     there is no need to store an automaton index.
     * @param edgeVar For senders, receivers and syncers the PLC variable stores the edge in the automaton indicated by
     *     {@code autVar}. For syncers, it indicates the edge in a syncer automaton, as the edge variable implicitly
     *     also indicates the automaton. Is {@code null} if there is no edge variable.
     * @param eventEnabledVar PLC variable expressing if the event is enabled.
     * @param exprGen Expression generator for the scope of the generated code.
     * @return The generated code.
     */
    private List<PlcStatement> generateEdgesTestCode(Event event, TransitionAutomaton transAut, int autIndex,
            PlcBasicVariable autVar, PlcBasicVariable edgeVar, PlcBasicVariable eventEnabledVar, ExprGenerator exprGen)
    {
        List<PlcStatement> testCode = list();
        PlcSelectionStatement selStat = null;

        // Explain the goal of testing the edges.
        testCode.add(genEdgeTestsDocumentation(event, transAut));

        // Shouldn't have edges to select without edge variable.
        Assert.implies(edgeVar == null, transAut.transitionEdges.isEmpty());

        // Generate the checks and assign their findings to edge and/or automaton variables.
        int edgeIndex = 0;
        for (TransitionEdge transEdge: transAut.transitionEdges) {
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
            selStat = exprGen.addBranch(transEdge.guards, thenStats, selStat, testCode);
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
                    "The automaton has no edge with a true guard. Skip to the next event.");
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
     * <p>
     * Should only be called when code is being generated.
     * </p>
     *
     * @param aut Automaton to use for finding the edge variable.
     * @return The edge variable. Returns {@code null} for an edge variable if there is no edge variable.
     */
    private PlcBasicVariable getAutomatonEdgeVariable(Automaton aut) {
        PlcBasicVariable edgeVariable = edgeSelectionVariables.get(aut);
        return edgeVariable;
    }

    /**
     * Data to construct an edge variable.
     *
     * @param name Name of the variable.
     * @param plcType Type of the variable in the PLC.
     */
    private record EdgeVariableData(String name, PlcType plcType) {
        /**
         * Construct an edge variable from the stored data.
         *
         * @param target PLC target to generate code for.
         * @return The created variable.
         */
        public PlcDataVariable makeVariable(PlcTarget target) {
            String targetText = target.getUsageVariableText(PlcVariablePurpose.LOCAL_VAR, name);
            return new PlcDataVariable(targetText, name, plcType, null, null);
        }
    }

    /**
     * Generate a comment block explaining the test performed for an automaton.
     *
     * @param event Event being tried.
     * @param transAut Automaton transition information to show.
     * @return The generated comment block.
     */
    private PlcCommentBlock genEdgeTestsDocumentation(Event event, TransitionAutomaton transAut) {
        TextTopics topics = new TextTopics();
        CifDocAnnotationFormatter locDocFormatter = new CifDocAnnotationFormatter(null, null, "  ", List.of(""), null);
        CifDocAnnotationFormatter edgeDocFormatter = new CifDocAnnotationFormatter(null, null, "    ", List.of(""),
                null);

        String edgePluralText = (transAut.transitionEdges.size() != 1) ? "s" : "";
        topics.add("Test edge%s of automaton \"%s\" to %s for event \"%s\".", edgePluralText,
                getAbsName(transAut.aut, false), transAut.purpose.purposeText, getAbsName(event, false));

        switch (transAut.purpose) {
            case MONITOR:
                throw new AssertionError("Unexpected test-doc request from a monitor.");
            case RECEIVER:
            case SENDER: {
                String kindText = (transAut.purpose == TransAutPurpose.SENDER) ? "sending" : "receiving";
                topics.add("At least one %s automaton must have an edge with a true guard to allow the event.",
                        kindText);
                break;
            }
            case SYNCER:
                topics.add("This automaton must have an edge with a true guard to allow the event.");
                break;
            default:
                throw new AssertionError("Unknown purpose \"" + transAut.purpose + "\" encountered.");
        }
        topics.ensureEmptyAtEnd();
        topics.add("Edge%s being tested:", edgePluralText);
        Location lastLoc = null;
        for (TransitionEdge transEdge: transAut.transitionEdges) {
            if (transEdge.sourceLoc != lastLoc) {
                lastLoc = transEdge.sourceLoc;
                if (lastLoc.getName() == null) {
                    topics.add("- Location:");
                    topics.addAll(locDocFormatter.formatDocs(lastLoc));
                } else {
                    topics.add("- Location \"%s\":", lastLoc.getName());
                    topics.addAll(locDocFormatter.formatDocs(lastLoc));
                }
            }
            topics.add("  - %s edge in the location", toOrdinal(transEdge.edgeNumber));
            topics.addAll(edgeDocFormatter.formatDocs(transEdge.edge));
        }
        if (lastLoc == null) {
            switch (transAut.purpose) {
                case RECEIVER:
                    topics.add("  - No edges found. Value cannot be accepted by this automaton.");
                    break;
                case SENDER:
                    topics.add("  - No edges found. Value cannot be provided by this automaton.");
                    break;
                case SYNCER:
                    topics.add("  - No edges found. Event \"%s\" will never occur!", getAbsName(event, false));
                    break;

                case MONITOR: // Never happens, as code above throws an exception already.
                default:
                    throw new AssertionError("Unknown purpose \"" + transAut.purpose + "\" encountered.");
            }
        }
        topics.dropEmptyAtEnd();
        return new PlcCommentBlock(topics.getLines());
    }

    /**
     * Generate PLC code that selects the edge stored in {@code edgeVar} and 'performs' the edge. If needed, compute the
     * sent value and store it into {@code channelValueVar}. Finally, perform the updates if they exist.
     *
     * @param transAut Automaton to generate perform code for.
     * @param edgeVar Variable containing the index of the selected edge to perform. Is {@code null} if there is no edge
     *     variable.
     * @param channelValueVar Variable that must be assigned the sent value of the channel by the selected edge. Use
     *     {@code null} if not in channel value context.
     * @param autCommentUpdateText Text of a header comment line above the edge perform code that states the automaton
     *     being handled in the code.
     * @param exprGen Expression generator for the scope of the generated code.
     * @param collectedNoUpdates Comments about automata that do not have updates to perform. Is extended in-place.
     * @return Generated PLC code that selects and performs the selected edge. Is empty if there is no PLC code needed
     *     to perform the edge.
     */
    private List<PlcStatement> generateSelectedEdgePerformCode(TransitionAutomaton transAut, PlcBasicVariable edgeVar,
            PlcBasicVariable channelValueVar, String autCommentUpdateText, ExprGenerator exprGen,
            List<PlcStatement> collectedNoUpdates)
    {
        // If nothing needs to be computed, tell the reviewer about it.
        List<PlcStatement> performCode = list();
        if (!transAut.hasUpdates() && channelValueVar == null) {
            collectedNoUpdates.add(new PlcCommentLine(
                    fmt("Automaton \"%s\" has no assignments to perform.", getAbsName(transAut.aut, false))));

            return performCode;
        }

        // Shouldn't have edges to select without edge variable.
        Assert.implies(edgeVar == null, transAut.transitionEdges.isEmpty());

        // Perform the selected edge, if not empty.
        PlcSelectionStatement selStat = null;
        int edgeIndex = 0;
        for (TransitionEdge transEdge: transAut.transitionEdges) {
            // Generate code that performs the edge if something needs to be done.
            if (channelValueVar != null || transEdge.hasUpdates()) {
                // State the automaton being updated or selected.
                if (autCommentUpdateText != null) {
                    performCode.add(new PlcCommentLine(autCommentUpdateText));
                    autCommentUpdateText = null;
                }

                // Construct a local function to compute the update statements to perform. This includes computing a
                // channel value if appropriate.
                Supplier<List<PlcStatement>> thenStats = () -> {
                    List<PlcStatement> thenStatements = list();

                    // Compute and assign the sent value if it exists.
                    if (channelValueVar != null) {
                        thenStatements.add(new PlcCommentLine("Compute sent channel value."));
                        genAssignExpr(new PlcVarExpression(channelValueVar), transEdge.sendValue, exprGen,
                                thenStatements);
                    }

                    // Perform the updates if they exist.
                    if (transEdge.hasUpdates()) {
                        thenStatements.addAll(generateEdgeUpdates(transAut.aut, transEdge, exprGen));
                    }
                    return thenStatements;
                };

                // Add "IF edgeVar = edgeIndex THEN <compute channelValue if needed, and perform updates>" branch.
                PlcExpression guard = generateCompareVarWithVal(edgeVar, edgeIndex);
                selStat = exprGen.addPlcBranch(List.of(new ExprValueResult(exprGen).setValue(guard)), thenStats,
                        selStat, performCode);
            }
            edgeIndex++;
        }

        return performCode;
    }

    /**
     * Generate PLC code that performs the provided updates.
     *
     * @param aut Automaton owning the given edge.
     * @param transEdge Edge with the updates to convert. Edge must have updates.
     * @param exprGen Expression generator for the scope of the generated code.
     * @return The generated statements.
     */
    private List<PlcStatement> generateEdgeUpdates(Automaton aut, TransitionEdge transEdge, ExprGenerator exprGen) {
        return generateEdgeUpdates(aut, transEdge, exprGen, null);
    }

    /**
     * Generate PLC code that performs the provided updates.
     *
     * @param aut Automaton owning the given edge.
     * @param transEdge Edge with the updates to convert. Edge must have updates.
     * @param exprGen Expression generator for the scope of the generated code.
     * @param topics If not {@code null}, already created documentation that should be inserted in a comment above the
     *     code.
     * @return The generated statements.
     */
    private List<PlcStatement> generateEdgeUpdates(Automaton aut, TransitionEdge transEdge, ExprGenerator exprGen,
            TextTopics topics)
    {
        Assert.check(transEdge.hasUpdates());

        topics = (topics == null) ? new TextTopics() : topics;

        // Construct a comment text that indicates what edge is being performed.
        String text;
        if (transEdge.sourceLoc.getName() == null) {
            text = fmt("Perform assignments of the %s edge of automaton \"%s\".", toOrdinal(transEdge.edgeNumber),
                    getAbsName(aut, false));
        } else {
            text = fmt("Perform assignments of the %s edge in location \"%s\".", toOrdinal(transEdge.edgeNumber),
                    getAbsName(transEdge.sourceLoc, false));
        }

        // Create the documentation.
        CifDocAnnotationFormatter edgeUpdateDocFormatter = new CifDocAnnotationFormatter(null, null, null, List.of(""),
                null);
        topics.ensureEmptyAtEnd();
        topics.add(text);
        topics.addAll(edgeUpdateDocFormatter.formatDocs(transEdge.edge));
        topics.dropEmptyAtEnd();

        // Store the documentation.
        List<PlcStatement> stats = list();
        if (topics.size() == 1) {
            stats.add(new PlcCommentLine(first(topics.getLines())));
        } else {
            stats.add(new PlcCommentBlock(topics.getLines()));
        }

        // Add the generated code and return everything.
        stats.addAll(generateUpdates(transEdge.updates, exprGen));
        return stats;
    }

    /**
     * Recursively convert the supplied updates to PLC code.
     *
     * @param updates Updates to convert to PLC code.
     * @param exprGen Expression generator for the scope of the generated code.
     * @return The generated statements.
     */
    private List<PlcStatement> generateUpdates(List<Update> updates, ExprGenerator exprGen) {
        List<PlcStatement> statements = list();

        for (Update upd: updates) {
            if (upd instanceof IfUpdate ipUpd) {
                genIfUpdate(ipUpd, exprGen, statements);
            } else if (upd instanceof Assignment asg) {
                genUpdateAssignment(asg.getAddressable(), asg.getValue(), exprGen, statements);
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
     * @param exprGen Expression generator for the scope of the generated code.
     * @param statements Generated PLC statements. Is extended in-place.
     */
    private void genIfUpdate(IfUpdate ifUpd, ExprGenerator exprGen, List<PlcStatement> statements) {
        PlcSelectionStatement selStat = null;
        selStat = exprGen.addBranch(ifUpd.getGuards(), () -> generateUpdates(ifUpd.getThens(), exprGen), selStat,
                statements);
        for (ElifUpdate elifUpd: ifUpd.getElifs()) {
            selStat = exprGen.addBranch(elifUpd.getGuards(), () -> generateUpdates(elifUpd.getThens(), exprGen),
                    selStat, statements);
        }
        exprGen.addBranch(null, () -> generateUpdates(ifUpd.getElses(), exprGen), selStat, statements);
    }

    /**
     * Generate PLC code for an assignment update.
     *
     * @param lhs Left side addressable to assign to.
     * @param rhs Right side value to assign.
     * @param exprGen Expression generator for the scope of the generated code.
     * @param statements Generated PLC statements. Is extended in-place.
     */
    private void genUpdateAssignment(Expression lhs, Expression rhs, ExprGenerator exprGen,
            List<PlcStatement> statements)
    {
        // TODO: Current code makes a copy of the old value for every assigned variable, even if it can be avoided.

        // Test for the simple case of a single left side variable.
        if (!(lhs instanceof TupleExpression lhsTuple)) {
            // Left side is a single destination, (possibly partially) assign the right side to it.
            ContVariable contvar;
            ExprAddressableResult lhsResult;
            if (lhs instanceof ProjectionExpression pe) {
                lhsResult = exprGen.convertProjectedAddressable(pe);
                contvar = null;
            } else if (lhs instanceof ContVariableExpression ce) {
                lhsResult = exprGen.convertVariableAddressable(lhs);
                contvar = ce.getVariable();
            } else {
                lhsResult = exprGen.convertVariableAddressable(lhs);
                contvar = null;
            }
            String varDesc = DocumentingSupport.getDescription(lhsResult.varDecl, lhsResult.isDerivativeAssigned());
            statements.add(new PlcCommentLine(fmt("Perform update of %s.", varDesc)));
            statements.addAll(lhsResult.code);
            lhsResult.releaseCodeVariables();
            genAssignExpr(lhsResult.value, rhs, exprGen, statements);
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
                genUpdateAssignment(lhsTuple.getFields().get(i), rhsTuple.getFields().get(i), exprGen, statements);
            }
            return;
        }

        // Left side is a tuple literal, right side is not. Compute the right side, then store it in a variable for
        // allowing to project on its parts.
        PlcBasicVariable rhsVariable = exprGen.getTempVariable("rightValue", rhs.getType());

        ExprValueResult rhsValueResult = exprGen.convertValue(rhs);
        statements.addAll(rhsValueResult.code);
        rhsValueResult.releaseCodeVariables();
        statements.add(new PlcAssignmentStatement(rhsVariable, rhsValueResult.value));
        rhsValueResult.releaseValueVariables();

        // Generate projected assignments using the variable.
        genUpdateAssignment(lhs, rhsVariable, List.of(), exprGen, statements);
        exprGen.releaseTempVariable(rhsVariable);
    }

    /**
     * Generate PLC code for an assignment update, with projected right side.
     *
     * @param lhs Left side to assign to.
     * @param rhsVariable Unprojected right side value in a variable to assign.
     * @param rhsProjections Projection sequence to apply to the {@code rhs} value before assigning to {@code lhs}.
     * @param exprGen Expression generator for the scope of the generated code.
     * @param statements Generated PLC statements. Is extended in-place.
     */
    private void genUpdateAssignment(Expression lhs, PlcBasicVariable rhsVariable,
            List<PlcStructProjection> rhsProjections, ExprGenerator exprGen, List<PlcStatement> statements)
    {
        // Conceptually: "lhs := rhs.rhsProjections" where lhs may be a tuple literal.

        // Test for the simple case of a single left side.
        if (!(lhs instanceof TupleExpression lhsTuple)) {
            // Left side is a single destination, the entire right side must be assigned to it.
            ExprAddressableResult lhsResult = exprGen.convertVariableAddressable(lhs);
            String varDesc = DocumentingSupport.getDescription(lhsResult.varDecl, lhsResult.isDerivativeAssigned());
            statements.add(new PlcCommentLine(fmt("Perform update of %s.", varDesc)));
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
        TupleType tupleType = (TupleType)normalizeType(lhs.getType());
        PlcStructType lhsStructType = target.getTypeGenerator().convertTupleType(tupleType);

        for (int idx = 0; idx < lhsStructType.fields.size(); idx++) {
            // Make a new projection list "valueProjections.<lhs-field>".
            List<PlcStructProjection> projs = listc(rhsProjections.size() + 1);
            projs.addAll(rhsProjections);
            projs.add(new PlcStructProjection(lhsStructType.fields.get(idx).fieldName));
            genUpdateAssignment(lhsTuple.getFields().get(idx), rhsVariable, projs, exprGen, statements);
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
     * @param exprGen Expression generator for the scope of the generated code.
     * @param statements If not {@code null}, storage for the generated code, is updated in-place. Otherwise a new list
     *     is constructed to store the resulting code. The created or updated list is also returned as return value of
     *     the method.
     * @return The supplied or newly created statements, appended with the generated assignment.
     */
    private List<PlcStatement> genAssignExpr(PlcVarExpression lhsVarExpr, Expression value, ExprGenerator exprGen,
            List<PlcStatement> statements)
    {
        statements = (statements == null) ? list() : statements;
        ExprValueResult rhsResult = exprGen.convertValue(value);
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
        PlcExpression valExpr = new PlcIntLiteral(value, variable.type);
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
        return new PlcAssignmentStatement(variable, new PlcIntLiteral(value, variable.type));
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
