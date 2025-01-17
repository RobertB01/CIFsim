//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.checkers.checks.TypeListSizeLimitsCheck.UNLIMITED;
import static org.eclipse.escet.cif.common.CifCollectUtils.collectAutomata;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithCertainNumberOfInitLocsCheck;
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithCertainNumberOfInitLocsCheck.AllowedNumberOfInitLocs;
import org.eclipse.escet.cif.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoPureMonitorsCheck;
import org.eclipse.escet.cif.checkers.checks.EventOnlyWithControllabilityCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificIntUserDefFuncStatsCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificIntUserDefFuncStatsCheck.NoSpecificStatement;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificStdLibCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificStdLibCheck.NoSpecificStdLib;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.checkers.checks.TypeListSizeLimitsCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimConsts;
import org.eclipse.escet.cif.cif2cif.ElimLocRefExprs;
import org.eclipse.escet.cif.cif2cif.ElimStateEvtExclInvs;
import org.eclipse.escet.cif.cif2cif.RemoveAnnotations;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyOthers;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.cif2cif.SwitchesToIfs;
import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransAutPurpose;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionAutomaton;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionEdge;
import org.eclipse.escet.cif.plcgen.generators.prechecks.VarContOnlyTimers;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.output.WarnOutput;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Extracts information from the CIF input file, to be used during PLC code generation. */
public class CifProcessor {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Input CIF specification. */
    private final Specification inputSpec;

    /** Paths to the CIF specification for which to generate PLC code. */
    private final PathPair inputPaths;

    /** Whether to simplify values during pre-processing. */
    private final boolean simplifyValues;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /** Cooperative termination query function. */
    private final Termination termination;

    /**
     * Process the input CIF specification, reading it, and extracting the relevant information for PLC code generation.
     *
     * @param target PLC target to generate code for.
     * @param inputSpec Input CIF specification.
     * @param settings Configuration to use.
     */
    public CifProcessor(PlcTarget target, Specification inputSpec, PlcGenSettings settings) {
        this.target = target;
        this.inputSpec = inputSpec;
        inputPaths = settings.inputPaths;
        simplifyValues = settings.simplifyValues;
        warnOutput = settings.warnOutput;
        termination = settings.termination;
    }

    /**
     * Process the input CIF specification, extracting the relevant information for PLC code generation.
     *
     * @return Results of processing the CIF specification.
     */
    public CifProcessorResults process() {
        // Process the CIF specification.
        widenSpec(inputSpec);
        preCheckSpec(inputSpec, inputPaths.systemPath, termination);
        normalizeSpec(inputSpec);

        // While collecting the information for performing events, also store the information by component to enable
        // generating documentation for them in the PLC code.
        Map<ComplexComponent, ComponentDocData> componentDatas = map();

        // Collect the declarations of the specification.
        List<DiscVariable> discVariables = list();
        List<InputVariable> inputVariables = list();
        List<EnumDecl> enumDecls = list();
        List<ContVariable> contVariables = list();
        List<Constant> constants = list();
        for (Declaration decl: CifCollectUtils.collectDeclarations(inputSpec, list())) {
            // Store the found declaration in the data of its complex component.
            ComponentDocData compData = componentDatas.computeIfAbsent((ComplexComponent)decl.eContainer(),
                    comp -> new ComponentDocData(comp));

            // Tell other generators about the declaration.
            if (decl instanceof DiscVariable discVar) {
                compData.variables.add(decl);
                discVariables.add(discVar);
            } else if (decl instanceof InputVariable inpVar) {
                compData.variables.add(decl);
                inputVariables.add(inpVar);
            } else if (decl instanceof EnumDecl enumDecl) {
                enumDecls.add(enumDecl);
            } else if (decl instanceof ContVariable contVar) {
                compData.variables.add(decl);
                contVariables.add(contVar);
            } else if (decl instanceof Constant constant) {
                if (target.supportsConstant(constant)) {
                    constants.add(constant);
                }
                // Else ignore it: the expression generator will inline it when used.
            }

            // TODO Extend allowed initial values by computing at runtime.
        }

        // Collect edge transitions from automata.
        Map<Event, CifEventTransition> eventTransitions = map();
        for (Automaton aut: collectAutomata(inputSpec, list())) {
            // Classify the role of the automaton, per relevant event.
            Map<Event, AutomatonRoleInfo> autRoleInfoPerEvent = classifyAutomatonRole(aut);

            // Store event information in the component data.
            ComponentDocData compData = componentDatas.computeIfAbsent(aut,
                    comp -> new ComponentDocData(comp));
            compData.addEvents(autRoleInfoPerEvent.keySet());

            // Merge the found data into the collection.
            for (AutomatonRoleInfo autRoleInfo: autRoleInfoPerEvent.values()) {
                CifEventTransition eventTrans = eventTransitions.computeIfAbsent(autRoleInfo.event,
                        evt -> new CifEventTransition(evt));

                if (autRoleInfo.isSenderAutomaton()) {
                    eventTrans.senders
                            .add(new TransitionAutomaton(aut, TransAutPurpose.SENDER, autRoleInfo.getSenderEdges()));
                } else if (autRoleInfo.isReceiverAutomaton()) {
                    eventTrans.receivers.add(
                            new TransitionAutomaton(aut, TransAutPurpose.RECEIVER, autRoleInfo.getReceiverEdges()));
                } else if (autRoleInfo.isSyncerAutomaton()) {
                    eventTrans.syncers
                            .add(new TransitionAutomaton(aut, TransAutPurpose.SYNCER, autRoleInfo.getSyncerEdges()));
                } else if (autRoleInfo.isMonitorAutomaton()) {
                    eventTrans.monitors
                            .add(new TransitionAutomaton(aut, TransAutPurpose.MONITOR, autRoleInfo.getMonitorEdges()));
                } else {
                    throw new AssertionError("Undecided automaton role.");
                }
            }
        }

        // Return the results.
        List<CifEventTransition> cifEventTransitions = listc(eventTransitions.values().size());
        cifEventTransitions.addAll(eventTransitions.values());
        return new CifProcessorResults(componentDatas, discVariables, inputVariables, enumDecls, contVariables,
                constants, cifEventTransitions, new CifObjectFinder(inputSpec));
    }

    /**
     * Return the {@link AutomatonRoleInfo} for the given event from {@code autRoleInfoPerEvent}, possibly after
     * creating it.
     *
     * @param autRoleInfoPerEvent Available {@link AutomatonRoleInfo}s. May be expanded in-place.
     * @param event Event for which to obtain the automaton role information.
     * @return The automaton role information for the event.
     */
    private static AutomatonRoleInfo getAutRoleInfo(Map<Event, AutomatonRoleInfo> autRoleInfoPerEvent, Event event) {
        return autRoleInfoPerEvent.computeIfAbsent(event, evt -> new AutomatonRoleInfo(evt));
    }

    /**
     * Classify the {@link AutomatonRole role of the automaton} for the different events, based on the edges that exist
     * in the automaton, and the automaton's monitor declaration (if present).
     *
     * @param aut Automaton to analyze.
     * @return An {@link AutomatonRoleInfo} for every event that is in the automaton's alphabet, or for which the
     *     automaton is a sender or receiver.
     */
    private Map<Event, AutomatonRoleInfo> classifyAutomatonRole(Automaton aut) {
        Map<Event, AutomatonRoleInfo> autRoleInfoPerEvent = map();

        // Explicit alphabet definition.
        if (aut.getAlphabet() != null) {
            for (Expression expr: aut.getAlphabet().getEvents()) {
                EventExpression eve = (EventExpression)expr;
                getAutRoleInfo(autRoleInfoPerEvent, eve.getEvent()); // Create an entry.
            }
        }

        // Consider all the edges of the automaton for the classification.
        for (Location loc: aut.getLocations()) {
            int edgeNum = 1;
            for (Edge edge: loc.getEdges()) {
                Location destLoc = CifEdgeUtils.getTarget(edge);

                Assert.check(!edge.getEvents().isEmpty()); // Pre-condition violation.
                for (EdgeEvent ee: edge.getEvents()) {
                    Expression eventRef = ee.getEvent();
                    Assert.check(!(eventRef instanceof TauExpression)); // Pre-condition violation.
                    EventExpression eve = (EventExpression)eventRef;
                    AutomatonRoleInfo autRoleInfo = getAutRoleInfo(autRoleInfoPerEvent, eve.getEvent());

                    if (ee instanceof EdgeSend es) {
                        TransitionEdge te = new TransitionEdge(edge, edgeNum, loc, destLoc, es.getValue(),
                                edge.getGuards(), edge.getUpdates());
                        autRoleInfo.addEdge(te, AutomatonRole.SENDER);
                    } else if (ee instanceof EdgeReceive) {
                        TransitionEdge te = new TransitionEdge(edge, edgeNum, loc, destLoc, null,
                                edge.getGuards(), edge.getUpdates());
                        autRoleInfo.addEdge(te, AutomatonRole.RECEIVER);
                    } else {
                        // Automaton synchronizes on the event. Whether it is a monitor is decided below.
                        TransitionEdge te = new TransitionEdge(edge, edgeNum, loc, destLoc, null,
                                edge.getGuards(), edge.getUpdates());
                        autRoleInfo.addEdge(te, AutomatonRole.SYNCER_OR_MONITOR);
                    }
                }
                edgeNum++;
            }
        }

        // Handle monitors: if the automaton monitors a event, we can classify it as a monitor, resolving the 'syncer or
        // monitor' ambiguity.
        if (aut.getMonitors() != null) {
            Monitors mons = aut.getMonitors();
            if (mons.getEvents().isEmpty()) {
                // The automaton monitors all events in its alphabet.
                for (AutomatonRoleInfo autRoleInfo: autRoleInfoPerEvent.values()) {
                    autRoleInfo.setIsMonitor(true);
                }
            } else {
                // The automaton monitors some explicit events.
                Set<Event> monitorEvents = mons.getEvents().stream().map(expr -> ((EventExpression)expr).getEvent())
                        .collect(Sets.toSet());
                for (AutomatonRoleInfo autRoleInfo: autRoleInfoPerEvent.values()) {
                    autRoleInfo.setIsMonitor(monitorEvents.contains(autRoleInfo.event));
                }
            }
        }

        // Automata that do not send or receive over the channel event, and also don't monitor the event, are a syncer
        // automaton for the event.
        for (AutomatonRoleInfo autRoleInfo: autRoleInfoPerEvent.values()) {
            if (EnumSet.of(AutomatonRole.UNKNOWN, AutomatonRole.SYNCER_OR_MONITOR).contains(autRoleInfo.autRole)) {
                autRoleInfo.setIsSyncer();
            }
        }

        // Ensure that we have fully classified the role of the automaton, for all relevant events, before returning it
        // to the caller.
        for (AutomatonRoleInfo autRoleInfo: autRoleInfoPerEvent.values()) {
            autRoleInfo.checkAutRoleIsDecided();
        }
        return autRoleInfoPerEvent;
    }

    /** Information used to classify the {@link AutomatonRole role of an automaton}, for a certain event. */
    private static class AutomatonRoleInfo {
        /** The event for which to classify the role of the automaton. */
        public final Event event;

        /**
         * Role of the automaton with respect to the {@link #event}. This may be modified multiple times during the
         * classification process.
         */
        private AutomatonRole autRole;

        /** Edges of the automaton for the {@link #event}. */
        private final List<TransitionEdge> edges = list();

        /**
         * Constructor of the {@link AutomatonRoleInfo} class.
         *
         * @param event The event for which to classify the role of the automaton.
         */
        public AutomatonRoleInfo(Event event) {
            this.event = event;
            autRole = (event.getType() == null) ? AutomatonRole.SYNCER_OR_MONITOR : AutomatonRole.UNKNOWN;
        }

        /**
         * Consider an edge for classification.
         *
         * @param transEdge Edge to consider.
         * @param edgeAutRole The role of the automaton as implied by the edge.
         */
        public void addEdge(TransitionEdge transEdge, AutomatonRole edgeAutRole) {
            // Check for allowed roles implied by edges. Note that in an edge context it is not possible to decide
            // between a syncer and a monitor.
            Assert.check(EnumSet.of(AutomatonRole.SENDER, AutomatonRole.RECEIVER, AutomatonRole.SYNCER_OR_MONITOR)
                    .contains(edgeAutRole));

            // Add the edge.
            edges.add(transEdge);

            // Update the classification of the automaton's role.
            switch (autRole) {
                case UNKNOWN:
                    autRole = edgeAutRole; // This role implied by this edge is the only information we have, so far.
                    break;
                case MONITOR:
                case SYNCER:
                case SYNCER_OR_MONITOR:
                    Assert.areEqual(edgeAutRole, AutomatonRole.SYNCER_OR_MONITOR); // Should not be sender/receiver.
                    break;
                case SENDER:
                case RECEIVER:
                    // Once the automaton is a sender or receiver, all edges must imply that.
                    Assert.check(edgeAutRole.equals(autRole));
                    break;
                default:
                    throw new AssertionError("Unexpected automaton role \"" + autRole + "\".");
            }
        }

        /**
         * Update the classification of the automaton's role, based on whether the automaton monitors the {@link #event}
         * or not.
         *
         * @param isMonitor {@code true} if the automaton is a monitor automaton for the {@link #event}, {@code false}
         *     otherwise.
         */
        public void setIsMonitor(boolean isMonitor) {
            // If already a sender or receiver, it can not also be a monitor.
            if (EnumSet.of(AutomatonRole.SENDER, AutomatonRole.RECEIVER).contains(autRole)) {
                Assert.check(!isMonitor);
                return; // Completely done with sender and receiver.
            }

            // Resolve ambiguity between syncer and monitor.
            AutomatonRole resultRole = isMonitor ? AutomatonRole.MONITOR : AutomatonRole.SYNCER;
            if (autRole == resultRole) {
                return; // Do nothing if the classification is already correct.
            } else if (EnumSet.of(AutomatonRole.SYNCER_OR_MONITOR, AutomatonRole.UNKNOWN).contains(autRole)) {
                autRole = resultRole; // Resolve the ambiguity if it exists.
                return;
            }

            // The information is contradicting in some way.
            throw new AssertionError("Encountered unexpected automaton role \"" + autRole + "\".");
        }

        /** Update the classification of the automaton's role to being a syncer automaton. */
        public void setIsSyncer() {
            Assert.check(EnumSet.of(AutomatonRole.UNKNOWN, AutomatonRole.SYNCER_OR_MONITOR).contains(autRole));
            autRole = AutomatonRole.SYNCER;
        }

        /** Check that the automaton's role has been fully decided (there are no remaining ambiguities). */
        private void checkAutRoleIsDecided() {
            Assert.check(EnumSet
                    .of(AutomatonRole.SENDER, AutomatonRole.RECEIVER, AutomatonRole.SYNCER, AutomatonRole.MONITOR)
                    .contains(autRole));
        }

        /**
         * Is the automaton a {@link AutomatonRole#SENDER sender} automaton?
         *
         * @return Whether the automaton is a sender automaton.
         */
        public boolean isSenderAutomaton() {
            checkAutRoleIsDecided();
            return autRole == AutomatonRole.SENDER;
        }

        /**
         * Is the automaton a {@link AutomatonRole#RECEIVER receiver} automaton?
         *
         * @return Whether the automaton is a receiver automaton.
         */
        public boolean isReceiverAutomaton() {
            checkAutRoleIsDecided();
            return autRole == AutomatonRole.RECEIVER;
        }

        /**
         * Is the automaton a {@link AutomatonRole#SYNCER syncer} automaton?
         *
         * @return Whether the automaton is a syncer automaton.
         */
        public boolean isSyncerAutomaton() {
            checkAutRoleIsDecided();
            return autRole == AutomatonRole.SYNCER;
        }

        /**
         * Is the automaton a {@link AutomatonRole#MONITOR monitor} automaton?
         *
         * @return Whether the automaton is a monitor automaton.
         */
        public boolean isMonitorAutomaton() {
            checkAutRoleIsDecided();
            return autRole == AutomatonRole.MONITOR;
        }

        /**
         * Get the edges of the {@link AutomatonRole#SENDER sender} automaton. May only be called when
         * {@link #isSenderAutomaton} returns {@code true}.
         *
         * @return The edges of the automaton, for the {@link #event}.
         */
        public List<TransitionEdge> getSenderEdges() {
            Assert.check(isSenderAutomaton());
            return edges;
        }

        /**
         * Get the edges of the {@link AutomatonRole#RECEIVER receiver} automaton. May only be called when
         * {@link #isReceiverAutomaton} returns {@code true}.
         *
         * @return The edges of the automaton, for the {@link #event}.
         */
        public List<TransitionEdge> getReceiverEdges() {
            Assert.check(isReceiverAutomaton());
            return edges;
        }

        /**
         * Get the edges of the {@link AutomatonRole#SYNCER syncer} automaton. May only be called when
         * {@link #isSyncerAutomaton} returns {@code true}.
         *
         * @return The edges of the automaton, for the {@link #event}.
         */
        public List<TransitionEdge> getSyncerEdges() {
            Assert.check(isSyncerAutomaton());
            return edges;
        }

        /**
         * Get the edges of the {@link AutomatonRole#MONITOR monitor} automaton. May only be called when
         * {@link #isMonitorAutomaton} returns {@code true}.
         *
         * @return The edges of the automaton, for the {@link #event}.
         */
        public List<TransitionEdge> getMonitorEdges() {
            Assert.check(isMonitorAutomaton());
            return edges;
        }
    }

    /**
     * The role of an automaton with respect to a certain event. For a certain event, an automaton can only have a
     * single role, but there may be ambiguity while the automaton is being classified, i.e., its role is being
     * determined.
     */
    public static enum AutomatonRole {
        /** The role of the automaton is not yet known. */
        UNKNOWN,

        /** The automaton is a sender automaton, that sends values over the channel. */
        SENDER,

        /** The automaton is a receiver automaton, that receives values from the channel. */
        RECEIVER,

        /** The automaton is a syncer automaton, that synchronizes on the event, but does not monitor it. */
        SYNCER,

        /** The automaton is a monitor automaton, that synchronizes on the event, and monitors it. */
        MONITOR,

        /** The automaton is either a {@link #SYNCER syncer} or a {@link #MONITOR monitor}. */
        SYNCER_OR_MONITOR;
    }

    /**
     * Eliminate CIF features to widen the set of accepted specifications.
     *
     * @param spec Specification to widen.
     */
    private void widenSpec(Specification spec) {
        // Remove all non-documentation annotations, to avoid having to deal with.
        new RemoveAnnotations("doc").transform(spec);

        // Eliminate component definition/instantiation, to avoid having to handle them.
        new ElimComponentDefInst().transform(spec);

        // Eliminate state/event exclusion invariants, to avoid having to handle them.
        // TODO For traceability, it might be better to keep this, and convert it to an additional test in the event
        // function labeled with the invariant.
        new ElimStateEvtExclInvs(warnOutput).transform(spec);

        // Simplify the specification, to increase the supported subset. Since simplification of values fills in all
        // constants, we can also remove the constants. However, this may lead to large amounts of duplication for
        // constants with large literal values. Therefore, it is an option. We could always use less expensive variants
        // of value simplification, in the future.
        if (simplifyValues) {
            new SimplifyValues().transform(spec);
            new ElimConsts().transform(spec);
        }

        // Simplify the specification, to reduce PLC code size.
        new SimplifyOthers().transform(spec);

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            warnOutput.line("The specification contains CIF/SVG input declarations. These will be ignored.");
        }
    }

    /**
     * Verify that the specification only contains supported language constructs.
     *
     * @param spec Specification to check.
     * @param absSpecPath The absolute local file system path to the CIF file to check.
     * @param termination Cooperative termination query function.
     */
    private void preCheckSpec(Specification spec, String absSpecPath, Termination termination) {
        PlcGenPreChecker checker = new PlcGenPreChecker(target.supportsArrays(), termination);
        checker.reportPreconditionViolations(spec, absSpecPath, "CIF PLC code generator");
    }

    /** CIF PLC code generator precondition checker. Does not support component definition/instantiation. */
    private static class PlcGenPreChecker extends CifPreconditionChecker {
        /**
         * Constructor of the {@link PlcGenPreChecker} class.
         *
         * @param supportArrays Whether the target supports arrays.
         * @param termination Cooperative termination query function.
         */
        public PlcGenPreChecker(boolean supportArrays, Termination termination) {
            super(termination,

                    // At least one automaton.
                    new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

                    // No initialization predicates in components, ignoring initialization predicates that trivially
                    // hold.
                    new CompNoInitPredsCheck(true),

                    // Discrete variables must have a single initial value.
                    new VarNoDiscWithMultiInitValuesCheck(),

                    // Automata must have a single initial location.
                    new AutOnlyWithCertainNumberOfInitLocsCheck(AllowedNumberOfInitLocs.EXACTLY_ONE),

                    // Disallow state invariants, except ones that never block behavior.
                    new InvNoSpecificInvsCheck()
                            .ignoreNeverBlockingInvariants()
                            .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.STATE,
                                    NoInvariantPlaceKind.ALL_PLACES),

                    // Disallow events without controllability and pure monitors. Note that the latter is necessary but
                    // not sufficient. The finite response property of the controller-check covers it, but that is not
                    // checked in PLCgen.
                    new EventOnlyWithControllabilityCheck(),
                    new EventNoPureMonitorsCheck(),

                    // Disallow equations.
                    // TODO This may be too strict. Consider what equations should be allowed more closely.
                    new EqnNotAllowedCheck(),

                    // No urgency.
                    new LocNoUrgentCheck(),
                    new EdgeNoUrgentCheck(),

                    // Disallow external user-defined functions, and only allow internal user-defined functions with at
                    // least one parameter.
                    // TODO Implement internal user-defined functions with at least one parameter.
                    new FuncNoSpecificUserDefCheck(
                            NoSpecificUserDefFunc.EXTERNAL,
                            NoSpecificUserDefFunc.INTERNAL, // Temporary addition until they are implemented.
                            NoSpecificUserDefFunc.NO_PARAMETER),

                    // Disallow the 'continue' statement.
                    new FuncNoSpecificIntUserDefFuncStatsCheck(NoSpecificStatement.CONTINUE),

                    // Disallow various types completely and function types in non-call context.
                    new TypeNoSpecificTypesCheck(
                            NoSpecificType.DICT_TYPES,
                            NoSpecificType.DIST_TYPES,
                            NoSpecificType.FUNC_TYPES_AS_DATA,
                            NoSpecificType.SET_TYPES,
                            NoSpecificType.STRING_TYPES,
                            (supportArrays ? NoSpecificType.LIST_TYPES_NON_ARRAY : NoSpecificType.LIST_TYPES))
                                    .ignoreAnnotations(),

                    // Disallow the empty array type (with only value [], as PLC arrays have an inclusive non-negative
                    // upper-bound.
                    new TypeListSizeLimitsCheck(1, UNLIMITED, UNLIMITED, UNLIMITED),

                    // Allow only casting to the same type and int to real, allow projection only on tuples and arrays,
                    // forbid string, set, and dictionary literals and time, forbid slicing, and function references
                    // outside call context.
                    new ExprNoSpecificExprsCheck(
                            NoSpecificExpr.CAST_EXPRS_FROM_STRING,
                            NoSpecificExpr.CAST_EXPRS_TO_STRING,
                            NoSpecificExpr.DICT_LITS,
                            NoSpecificExpr.FUNC_REFS_USER_DEF_AS_DATA,
                            NoSpecificExpr.PROJECTION_EXPRS_DICTS,
                            NoSpecificExpr.PROJECTION_EXPRS_LISTS_NON_ARRAY,
                            NoSpecificExpr.PROJECTION_EXPRS_STRINGS,
                            NoSpecificExpr.SET_LITS,
                            NoSpecificExpr.STRING_LITS,
                            NoSpecificExpr.SLICE_EXPRS,
                            NoSpecificExpr.SWITCH_EXPRS_LIST_ARRAY,
                            NoSpecificExpr.SWITCH_EXPRS_TUPLE,
                            NoSpecificExpr.TIME_VAR_REFS)
                                    .ignoreAnnotations(),

                    // Disallow sampling.
                    new ExprNoSpecificUnaryExprsCheck(NoSpecificUnaryOp.SAMPLE).ignoreAnnotations(),

                    // Disallow element of, and subset operators. Allow conjunction and disjunction only on booleans,
                    // allow equality only on booleans, integers, reals and enums, allow addition and subtraction only
                    // on integers and reals.
                    new ExprNoSpecificBinaryExprsCheck(
                            NoSpecificBinaryOp.ADDITION_LISTS,
                            NoSpecificBinaryOp.ADDITION_STRINGS,
                            NoSpecificBinaryOp.ADDITION_DICTS,
                            NoSpecificBinaryOp.ELEMENT_OF,
                            NoSpecificBinaryOp.EQUAL_DICT,
                            NoSpecificBinaryOp.EQUAL_LIST,
                            NoSpecificBinaryOp.EQUAL_SET,
                            NoSpecificBinaryOp.EQUAL_STRING,
                            NoSpecificBinaryOp.EQUAL_TUPLE,
                            NoSpecificBinaryOp.SUBSET,
                            NoSpecificBinaryOp.SUBTRACTION_DICTS,
                            NoSpecificBinaryOp.SUBTRACTION_LISTS,
                            NoSpecificBinaryOp.SUBTRACTION_SETS,
                            NoSpecificBinaryOp.CONJUNCTION_SETS,
                            NoSpecificBinaryOp.DISJUNCTION_SETS,
                            NoSpecificBinaryOp.UNEQUAL_DICT,
                            NoSpecificBinaryOp.UNEQUAL_LIST,
                            NoSpecificBinaryOp.UNEQUAL_SET,
                            NoSpecificBinaryOp.UNEQUAL_STRING,
                            NoSpecificBinaryOp.UNEQUAL_TUPLE)
                                    .ignoreAnnotations(),

                    // Limit standard library functions.
                    new FuncNoSpecificStdLibCheck(
                            NoSpecificStdLib.ALL_STOCHASTIC,
                            NoSpecificStdLib.ACOSH,
                            NoSpecificStdLib.ASINH,
                            NoSpecificStdLib.ATANH,
                            NoSpecificStdLib.COSH,
                            NoSpecificStdLib.SINH,
                            NoSpecificStdLib.TANH,
                            NoSpecificStdLib.CBRT,
                            NoSpecificStdLib.CEIL,
                            NoSpecificStdLib.DELETE,
                            NoSpecificStdLib.EMPTY,
                            NoSpecificStdLib.FLOOR,
                            NoSpecificStdLib.FORMAT,
                            NoSpecificStdLib.POP,
                            NoSpecificStdLib.POWER,
                            NoSpecificStdLib.ROUND,
                            NoSpecificStdLib.SCALE,
                            NoSpecificStdLib.SIGN,
                            NoSpecificStdLib.SIZE)
                                    .ignoreAnnotations(),

                    // Limit use of continuous variables.
                    new VarContOnlyTimers()

            );
        }
    }

    /**
     * Normalize CIF features to reduce the number of cases to deal with in the generator.
     *
     * @param spec Specification to normalize.
     */
    private void normalizeSpec(Specification spec) {
        // Add default initial values, to simplify the code generation.
        new AddDefaultInitialValues().transform(spec);

        // Transform 'switch' expressions to 'if' expressions, such that we only have to handle 'if' expressions.
        new SwitchesToIfs().transform(spec);

        // Replace locations in expressions by explicit variables.
        new ElimLocRefExprs(a -> "", // Candidate name for location pointer variables.
                a -> "location", // Candidate name for location pointer enumeration.
                loc -> loc.getName(), // Candidate name for location pointer literals.
                false, // Do not rename locations to ensure unique names.
                true, // Add initialization predicates for the initialization of the location pointer variables.
                false, // Also generate unused location pointers.
                null, // Map of location pointer variables to their automaton.
                true, // Optimize of initialization of location pointers.
                true, // Add location pointer expressions to guards.
                true, // Copy the annotations of automata to their newly created location pointer enumerations.
                true // Copy the annotations of the locations to their newly created enumerations literals.
        ).transform(spec);

        // Simplify the specification, to increase the supported subset. Since simplification of values fills in all
        // constants, we can also remove the constants. However, this may lead to large amounts of duplication for
        // constants with large literal values. Therefore, it is an option. We could always use less expensive variants
        // of value simplification, in the future.
        if (simplifyValues) {
            new SimplifyValues().transform(spec);
            new ElimConsts().transform(spec);
        }
    }

    /**
     * Results of the {@link CifProcessor} generator.
     *
     * @param componentDatas Data for generating model view documentation about complex components.
     * @param discVariables Discrete variables of the specification.
     * @param inputVariables Input variables of the specification.
     * @param enumDecls Enumeration declarations of the specification.
     * @param contVariables Continuous variables of the specification.
     * @param constants Constants of the specification if they are needed by the target, otherwise an empty list.
     * @param cifEventTransitions Edge transitions ordered by event and automaton.
     * @param cifObjectFinder Finder to get CIF objects from the input specification from their absolute name.
     */
    public record CifProcessorResults(
            Map<ComplexComponent, ComponentDocData> componentDatas, List<DiscVariable> discVariables,
            List<InputVariable> inputVariables, List<EnumDecl> enumDecls, List<ContVariable> contVariables,
            List<Constant> constants, List<CifEventTransition> cifEventTransitions, CifObjectFinder cifObjectFinder)
    {
    }

    /** Find CIF objects by their absolute name. */
    public static class CifObjectFinder {
        /** Specification to search for the CIF object. */
        private final Specification spec;

        /**
         * Constructor of the {@link CifObjectFinder}.
         *
         * @param spec Specification to search for the CIF object.
         */
        public CifObjectFinder(Specification spec) {
            Assert.notNull(spec);
            this.spec = spec;
        }

        /**
         * Try to match the given absolute name to a CIF object in the specification.
         *
         * @param absName The absolute name of the CIF object to find.
         * @return The found CIF object.
         * @throws IllegalArgumentException If no CIF object with the given name is available.
         */
        public PositionObject findCifObjectByAbsName(String absName) {
            PositionObject obj = spec; // Object to refine by following the parts of the absolute name.
            for (String namePart: absName.split("\\.")) {
                obj = CifScopeUtils.getObject(obj, namePart);
            }
            return obj;
        }
    }
}
