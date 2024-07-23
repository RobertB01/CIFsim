//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.intersection;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;
import org.eclipse.escet.common.multivaluetrees.VarInfo;
import org.eclipse.escet.common.multivaluetrees.VariableReplacement;
import org.eclipse.escet.common.multivaluetrees.VariableReplacementsBuilder;

/** MDD-based representation of a CIF specification, for use by MDD-based checks. */
public class CifMddSpec {
    /** Index for denoting the original value of a variable. */
    public static final int ORIGINAL_INDEX = 0;

    /** Index for denoting reading a variable. */
    public static final int READ_INDEX = 1;

    /** Index for denoting writing a variable. */
    public static final int WRITE_INDEX = 2;

    /** Number of variable indices that exist. */
    private static final int NUM_INDICES = 3;

    /** Callback that indicates whether execution should be terminated on user request. */
    private final Supplier<Boolean> shouldTerminate;

    /** Callback to send normal output to the user. */
    private final DebugNormalOutput normalOutput;

    /** Callback to send debug output to the user. */
    private final DebugNormalOutput debugOutput;

    /** Automata of the specification. */
    private List<Automaton> automata;

    /** The set of used controllable events. */
    private Set<Event> controllableEvents = set();

    /** Discrete and input variables of the specification. */
    private List<Declaration> variables;

    /** Global guard of each used controllable event. */
    private Map<Event, Node> globalGuardsByEvent = map();

    /** Global guarded update of each used controllable event, or {@code null} if computing it is disabled. */
    private Map<Event, Node> globalGuardedUpdatesByEvent;

    /** Updated variables of each used controllable event. */
    private Map<Event, Set<Declaration>> updatedVariablesByEvent = map();

    /** Builder for the MDD tree. */
    private MddSpecBuilder builder;

    /**
     * Constructor for the {@link CifMddSpec} class.
     *
     * @param computeGlobalGuardedUpdates Whether to compute global guarded updates.
     * @param shouldTerminate Callback that indicates whether execution should be terminated on user request.
     * @param normalOutput Callback to send normal output to the user.
     * @param debugOutput Callback to send debug output to the user.
     */
    public CifMddSpec(boolean computeGlobalGuardedUpdates, Supplier<Boolean> shouldTerminate,
            DebugNormalOutput normalOutput, DebugNormalOutput debugOutput)
    {
        this.globalGuardedUpdatesByEvent = computeGlobalGuardedUpdates ? map() : null;
        this.shouldTerminate = shouldTerminate;
        this.normalOutput = normalOutput;
        this.debugOutput = debugOutput;
    }

    /**
     * Extract the events and variables structure from the CIF specification, and organize it into a form usable for the
     * checks.
     *
     * @param spec Specification to analyze.
     * @return Whether the computation finished. The computation only does not finish when the user aborts the
     *     computation prematurely.
     */
    public boolean compute(Specification spec) {
        // Collect automata and controllable events.
        automata = CifCollectUtils.collectAutomata(spec, list());
        Set<Event> allControllableEvents = CifCollectUtils.collectControllableEvents(spec, set());
        if (automata.isEmpty() || allControllableEvents.isEmpty()) {
            // All MDD-based checks trivially hold.
            return true;
        }

        // Collect variables.
        variables = CifCollectUtils.collectDiscAndInputVariables(spec, list());
        if (shouldTerminate.get()) {
            return false;
        }

        // Construct the MDD tree instance.
        MddCifVarInfoBuilder cifVarInfoBuilder = new MddCifVarInfoBuilder(NUM_INDICES);
        cifVarInfoBuilder.addVariablesGroupOnVariable(variables);
        builder = new MddSpecBuilder(cifVarInfoBuilder, READ_INDEX, WRITE_INDEX);
        if (shouldTerminate.get()) {
            return false;
        }

        // Compute global guards, global guarded updates, and updated variables for each event.
        for (Automaton aut: automata) {
            debugOutput.line("Analyzing %s...", CifTextUtils.getComponentText1(aut));
            Set<Event> controllableAutEvents = intersection(CifEventUtils.getAlphabet(aut), allControllableEvents);
            if (!controllableAutEvents.isEmpty()) {
                if (!processAutomaton(aut, controllableAutEvents)) {
                    return false; // Abort requested.
                }
            }
        }

        return true;
    }

    /**
     * Analyze one automaton and add its information to the global collections.
     *
     * @param aut Automaton to analyze.
     * @param controllableAutEvents Controllable events of the automaton.
     * @return Whether the computation finished. The computation only does not finish when the user aborts the
     *     computation prematurely.
     */
    private boolean processAutomaton(Automaton aut, Set<Event> controllableAutEvents) {
        Tree tree = builder.tree;

        // Guards and guarded updates of the automaton.
        Map<Event, Node> autGuards = mapc(controllableAutEvents.size());
        Map<Event, Node> autGuardedUpdates = (globalGuardedUpdatesByEvent == null) ? null
                : mapc(controllableAutEvents.size());

        debugOutput.inc();
        // Initialize the automaton data for all automata events, and extend the global data for new events.
        for (Event evt: controllableAutEvents) {
            debugOutput.line("Initializing the automaton data for event \"%s\"...", CifTextUtils.getAbsName(evt));
            autGuards.put(evt, Tree.ZERO);
            if (autGuardedUpdates != null) {
                autGuardedUpdates.put(evt, Tree.ZERO);
            }

            if (!controllableEvents.contains(evt)) {
                controllableEvents.add(evt);
                globalGuardsByEvent.put(evt, Tree.ONE);
                if (globalGuardedUpdatesByEvent != null) {
                    globalGuardedUpdatesByEvent.put(evt, Tree.ONE);
                }
                updatedVariablesByEvent.put(evt, set());
            }
            if (shouldTerminate.get()) {
                debugOutput.dec();
                return false;
            }
        }

        // Process the locations and edges.
        for (Location loc: aut.getLocations()) {
            debugOutput.line("Processing edges from %s...", CifTextUtils.getLocationText2(loc));
            for (Edge edge: loc.getEdges()) {
                // Filter on relevant events.
                Set<Event> controllableEdgeEvents = intersection(CifEventUtils.getEvents(edge), controllableAutEvents);
                if (controllableEdgeEvents.isEmpty()) {
                    continue;
                }

                // Compute guard of the edge.
                Node guard = computeGuard(edge);
                if (shouldTerminate.get()) {
                    debugOutput.dec();
                    return false;
                }

                // Compute update of the edge.
                Node update = computeUpdate(edge, controllableEdgeEvents);
                if (shouldTerminate.get()) {
                    debugOutput.dec();
                    return false;
                }

                // Compute combined guard and update of the edge.
                Node guardedUpdate = (autGuardedUpdates == null) ? null : tree.conjunct(guard, update);
                if (shouldTerminate.get()) {
                    debugOutput.dec();
                    return false;
                }

                // Add the guard and guarded update as alternative to the relevant events of the edge.
                for (Event evt: controllableEdgeEvents) {
                    Node autGuard = autGuards.get(evt);
                    autGuards.put(evt, tree.disjunct(autGuard, guard));
                    if (shouldTerminate.get()) {
                        debugOutput.dec();
                        return false;
                    }

                    if (autGuardedUpdates != null) {
                        Node autGuardedUpdate = autGuardedUpdates.get(evt);
                        autGuardedUpdates.put(evt, tree.disjunct(autGuardedUpdate, guardedUpdate));
                        if (shouldTerminate.get()) {
                            debugOutput.dec();
                            return false;
                        }
                    }
                }
            }
        }

        // At global level, guards and updates of each event must synchronize between participating automata.
        for (Event autEvent: controllableAutEvents) {
            debugOutput.line("Updating global guards and updates for event \"%s\"...",
                    CifTextUtils.getAbsName(autEvent));
            Node globGuard = globalGuardsByEvent.get(autEvent);
            globalGuardsByEvent.put(autEvent, tree.conjunct(globGuard, autGuards.get(autEvent)));
            if (shouldTerminate.get()) {
                debugOutput.dec();
                return false;
            }

            if (autGuardedUpdates != null && globalGuardedUpdatesByEvent != null) {
                Node globalGuardedUpdate = globalGuardedUpdatesByEvent.get(autEvent);
                globalGuardedUpdatesByEvent.put(autEvent,
                        tree.conjunct(globalGuardedUpdate, autGuardedUpdates.get(autEvent)));
                if (shouldTerminate.get()) {
                    debugOutput.dec();
                    return false;
                }
            }
        }

        debugOutput.dec();
        return true;
    }

    /**
     * Convert the guard of an edge to an MDD relation.
     *
     * @param edge Edge to use.
     * @return The guard as an MDD tree.
     */
    private Node computeGuard(Edge edge) {
        Node guard = Tree.ONE;
        for (Expression grd: edge.getGuards()) {
            Node node = builder.getExpressionConvertor().convert(grd).get(1);
            if (shouldTerminate.get()) {
                return guard;
            }

            guard = builder.tree.conjunct(guard, node);
            if (shouldTerminate.get()) {
                return guard;
            }
        }
        return guard;
    }

    /**
     * Convert updates of an edge to an MDD relation, and mark variables assigned by these updates as being updated by
     * the events on the edge. If computing the global guarded updates is disabled, the update is not computed, and only
     * the variables are marked.
     *
     * @param edge Edge to use.
     * @param controllableEdgeEvents The controllable events of the edge.
     * @return The computed MDD update relation. The result should get connected to the edge guard. {@code null} is
     *     returned if computing the global guarded updates is disabled.
     */
    private Node computeUpdate(Edge edge, Set<Event> controllableEdgeEvents) {
        Tree tree = builder.tree;

        // Collect assigned variables, and assign RHS values to the LHS variables of the updates.
        Node updateNode = (globalGuardedUpdatesByEvent == null) ? null : Tree.ONE;
        Set<Declaration> assignedVariables = set();
        for (Update upd: edge.getUpdates()) {
            Assert.check(upd instanceof Assignment);
            Assignment asg = (Assignment)upd;
            Assert.check(asg.getAddressable() instanceof DiscVariableExpression);
            Declaration lhs = ((DiscVariableExpression)asg.getAddressable()).getVariable();
            assignedVariables.add(lhs);

            if (updateNode != null) {
                Node asgNode = builder.getExpressionConvertor().convertAssignment(lhs, asg.getValue());
                if (shouldTerminate.get()) {
                    return updateNode;
                }
                updateNode = tree.conjunct(updateNode, asgNode);
                if (shouldTerminate.get()) {
                    return updateNode;
                }
            }
        }

        // Add identity updates for all the non-assigned variables.
        if (updateNode != null) {
            for (Declaration otherVariable: variables) {
                if (!assignedVariables.contains(otherVariable)) {
                    VarInfo[] vinfos = builder.cifVarInfoBuilder.getVarInfos(otherVariable);
                    updateNode = tree.conjunct(updateNode, tree.identity(vinfos[READ_INDEX], vinfos[WRITE_INDEX]));
                    if (shouldTerminate.get()) {
                        return updateNode;
                    }
                }
            }
        }

        // Mark the assigned variables as being updated by the event.
        for (Event evt: controllableEdgeEvents) {
            updatedVariablesByEvent.get(evt).addAll(assignedVariables);
        }

        return updateNode;
    }

    /**
     * Construct an MDD variable replacement description for performing updates.
     *
     * @return The MDD variable replacement description ready for use.
     */
    public VariableReplacement[] createVarUpdateReplacements() {
        VariableReplacementsBuilder<Declaration> replBuilder = new VariableReplacementsBuilder<>(
                builder.cifVarInfoBuilder);

        for (Declaration updatedVar: variables) {
            replBuilder.addReplacement(updatedVar, READ_INDEX, WRITE_INDEX);
        }
        return replBuilder.getReplacements();
    }

    /**
     * Construct an MDD tree with identity equations between {@code #ORIGINAL_INDEX} and {@code #READ_INDEX} for all
     * variables.
     *
     * <p>
     * In further use, the tree operations change {@link #READ_INDEX} variables but not {@link #ORIGINAL_INDEX}
     * variables. This makes it feasible to check that variable values are treated equally in both branches of for
     * instance the confluence check.
     * </p>
     *
     * @return MDD tree with identity equations between {@code #ORIGINAL_INDEX} and {@code #READ_INDEX} for all
     *     variables.
     */
    public Node computeOriginalToReadIdentity() {
        Node result = Tree.ONE;
        for (int idx = variables.size() - 1; idx >= 0; idx--) {
            VarInfo[] vinfos = builder.cifVarInfoBuilder.getVarInfos(variables.get(idx));
            result = builder.tree.identity(vinfos[ORIGINAL_INDEX], vinfos[READ_INDEX], result);
            if (shouldTerminate.get()) {
                return result;
            }
        }
        return result;
    }

    /**
     * Get the variables in the MDD tree that represent non-original values.
     *
     * @return Variable in the MDD tree for non-original values.
     */
    public VarInfo[] getNonOriginalVariables() {
        int numVariables = builder.cifVarInfoBuilder.varInfos.size() / NUM_INDICES;
        VarInfo[] nonOriginalsVarInfos = new VarInfo[numVariables * (NUM_INDICES - 1)];
        int nextFree = 0;
        for (VarInfo vinfo: builder.cifVarInfoBuilder.varInfos) {
            if (vinfo != null && vinfo.useKind != ORIGINAL_INDEX) {
                nonOriginalsVarInfos[nextFree] = vinfo;
                nextFree++;
            }
        }
        Assert.areEqual(nextFree, nonOriginalsVarInfos.length);
        return nonOriginalsVarInfos;
    }

    /**
     * Returns the callback that indicates whether execution should be terminated on user request.
     *
     * @return The callback.
     */
    public Supplier<Boolean> getShouldTerminate() {
        return shouldTerminate;
    }

    /**
     * Returns the callback to send debug output to the user.
     *
     * @return The callback.
     */
    public DebugNormalOutput getDebugOutput() {
        return debugOutput;
    }

    /**
     * Returns the callback to send normal output to the user.
     *
     * @return The callback.
     */
    public DebugNormalOutput getNormalOutput() {
        return normalOutput;
    }

    /**
     * Get the automata of the specification.
     *
     * @return The automata of the specification.
     */
    public List<Automaton> getAutomata() {
        return Collections.unmodifiableList(automata);
    }

    /**
     * Get the used controllable events of the specification.
     *
     * @return The used controllable events of the specification.
     */
    public Set<Event> getControllableEvents() {
        return Collections.unmodifiableSet(controllableEvents);
    }

    /**
     * Get the global guard for each used controllable event.
     *
     * @return The global guard for each used controllable event.
     */
    public Map<Event, Node> getGlobalGuardsByEvent() {
        return Collections.unmodifiableMap(globalGuardsByEvent);
    }

    /**
     * Get the global guarded updates of each used controllable event, if computing it is enabled.
     *
     * @return The global guarded updates of each used controllable event.
     * @throws IllegalStateException If computing global guarded updates is disabled.
     */
    public Map<Event, Node> getGlobalGuardedUpdatesByEvent() {
        if (globalGuardedUpdatesByEvent == null) {
            throw new IllegalStateException("Computing global guarded updates is disabled.");
        }
        return Collections.unmodifiableMap(globalGuardedUpdatesByEvent);
    }

    /**
     * Get the updated variables of each used controllable event.
     *
     * @return The updated variables of each used controllable event.
     */
    public Map<Event, Set<Declaration>> getUpdatedVariablesByEvent() {
        return Collections.unmodifiableMap(updatedVariablesByEvent);
    }

    /**
     * Get the builder of the MDD trees.
     *
     * @return The builder of the MDD trees.
     */
    public MddSpecBuilder getBuilder() {
        return builder;
    }
}
