//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifCollectUtils.collectAutomata;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithOneInitLocCheck;
import org.eclipse.escet.cif.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoTauCheck;
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
import org.eclipse.escet.cif.cif2cif.EnumsToConsts;
import org.eclipse.escet.cif.cif2cif.EnumsToInts;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyOthers;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.WarnOutput;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionAutomaton;
import org.eclipse.escet.cif.plcgen.generators.CifEventTransition.TransitionEdge;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;

/** Extracts information from the CIF input file, to be used during PLC code generation. */
public class CifProcessor {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** User-specified path to the CIF specification for which to generate PLC code. */
    private final String inputPath;

    /** Absolute path to the CIF specification for which to generate PLC code. */
    private final String absInputPath;

    /** Whether to simplify values during pre-processing. */
    private final boolean simplifyValues;

    /** How to treat enumerations. */
    private final ConvertEnums enumConversion;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /**
     * Process the input CIF specification, reading it, and extracting the relevant information for PLC code generation.
     *
     * @param target PLC target to generate code for.
     * @param settings Configuration to use.
     */
    public CifProcessor(PlcTarget target, PlcGenSettings settings) {
        this.target = target;
        inputPath = settings.inputPath;
        absInputPath = settings.absInputPath;
        simplifyValues = settings.simplifyValues;
        enumConversion = settings.enumConversion;
        warnOutput = settings.warnOutput;
    }

    /** Process the input CIF specification, extracting the relevant information for PLC code generation. */
    public void process() {
        // Read CIF specification.
        Specification spec = new CifReader().init(inputPath, absInputPath, false).read();
        widenSpec(spec);
        preCheckSpec(spec, absInputPath);
        normalizeSpec(spec);

        // Convert the discrete and input variables as well as enumeration declarations throughout the specification.
        for (Declaration decl: CifCollectUtils.collectDeclarations(spec, list())) {
            if (decl instanceof DiscVariable discVar) {
                target.getVarStorage().addStateVariable(decl, discVar.getType());
            } else if (decl instanceof InputVariable inpVar) {
                target.getVarStorage().addStateVariable(decl, inpVar.getType());
            } else if (decl instanceof EnumDecl enumDecl) {
                target.getTypeGenerator().convertEnumDecl(enumDecl);
            }

            // TODO Constants.
            // TODO Initial value -> precheckers restrict to constant initial value.
            // TODO Extend allowed initial values by computing at runtime.
        }

        // Collect events / automata / edges and pass them to the transitions generator.
        Map<Event, CifEventTransition> eventTransitions = map();
        for (Automaton aut: collectAutomata(spec, list())) {
            // Get the events and their edges from a single automaton.
            Map<Event, AutomatonEventTransition> autEventUsage = getAutomatonEventUsage(aut);

            // Merge the found data into the collection.
            for (AutomatonEventTransition autEventTransitions: autEventUsage.values()) {
                CifEventTransition eventTrans = eventTransitions.computeIfAbsent(autEventTransitions.event,
                        evt -> new CifEventTransition(evt, list(), list(), list(), list()));

                autEventTransitions.checkNumberOfEdgeKinds();

                if (!autEventTransitions.sendEdges.isEmpty()) {
                    eventTrans.senders.add(new TransitionAutomaton(aut, autEventTransitions.sendEdges));
                }
                if (!autEventTransitions.receiveEdges.isEmpty()) {
                    eventTrans.receivers.add(new TransitionAutomaton(aut, autEventTransitions.receiveEdges));
                }
                if (!autEventTransitions.syncEdges.isEmpty()) {
                    eventTrans.syncers.add(new TransitionAutomaton(aut, autEventTransitions.syncEdges));
                }
                if (!autEventTransitions.monitorEdges.isEmpty()) {
                    eventTrans.monitors.add(new TransitionAutomaton(aut, autEventTransitions.monitorEdges));
                }
            }
        }

        // Construct the result to pass into the transition generator,
        List<CifEventTransition> cifEventTransitions = listc(eventTransitions.values().size());
        cifEventTransitions.addAll(eventTransitions.values());

        // And give the result to the transition generator.
        target.getTransitionGenerator().setTransitions(cifEventTransitions);
    }

    /**
     * Return the {@link AutomatonEventTransition} of the given event from {@code eventTransitions}, possibly after
     * creating it.
     *
     * @param eventTransitions Available {@link AutomatonEventTransition}s. May be expanded in-place.
     * @param event Event to use for retrieving the associated automaton event transition.
     * @return The automaton event transition of the event.
     */
    private static AutomatonEventTransition getEventTransition(Map<Event, AutomatonEventTransition> eventTransitions,
            Event event)
    {
        return eventTransitions.computeIfAbsent(event, evt -> new AutomatonEventTransition(evt));
    }

    /**
     * Collect the edges that exist in the given automaton for every event of the automaton, and classify them as
     * sender, receiver, syncer, or monitor.
     *
     * @param aut Automaton to analyze.
     * @return A {@link CifEventTransition} for every event that the given automaton can perform.
     */
    private Map<Event, AutomatonEventTransition> getAutomatonEventUsage(Automaton aut) {
        Map<Event, AutomatonEventTransition> eventUsage = map();

        // Explicit alphabet definition.
        if (aut.getAlphabet() != null) {
            for (Expression expr: aut.getAlphabet().getEvents()) {
                EventExpression eve = (EventExpression)expr;
                getEventTransition(eventUsage, eve.getEvent()); // Create an entry.
            }
        }

        // Walk through locations, collecting locations, edges, and event-usage.
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                Location destLoc = edge.getTarget() == null ? loc : edge.getTarget();

                Assert.check(!edge.getEvents().isEmpty()); // Pre-condition violation.
                for (EdgeEvent ee: edge.getEvents()) {
                    Expression eventRef = ee.getEvent();
                    Assert.check(!(eventRef instanceof TauExpression)); // Pre-condition violation.
                    EventExpression eve = (EventExpression)eventRef;
                    AutomatonEventTransition autEventTrans = getEventTransition(eventUsage, eve.getEvent());

                    if (ee instanceof EdgeSend es) {
                        TransitionEdge te = new TransitionEdge(loc, destLoc, es.getValue(), edge.getGuards(),
                                edge.getUpdates());
                        autEventTrans.sendEdges.add(te);
                    } else if (ee instanceof EdgeReceive) {
                        TransitionEdge te = new TransitionEdge(loc, destLoc, null, edge.getGuards(), edge.getUpdates());
                        autEventTrans.receiveEdges.add(te);
                    } else {
                        // Event could be a monitored. If so, it will be moved below.
                        TransitionEdge te = new TransitionEdge(loc, destLoc, null, edge.getGuards(), edge.getUpdates());
                        autEventTrans.syncEdges.add(te);
                    }
                }
            }
        }

        // Monitors. Above all non-channel events are collected in "syncers". Move events over to "monitors" if
        // necessary.
        if (aut.getMonitors() != null) {
            Monitors mons = aut.getMonitors();
            if (mons.getEvents().isEmpty()) {
                // Move all syncers to monitors.
                for (AutomatonEventTransition autEventTrans: eventUsage.values()) {
                    autEventTrans.monitorEdges.addAll(autEventTrans.syncEdges);
                    autEventTrans.syncEdges.clear();
                }
            } else {
                // Only change the mentioned events to monitors.
                //
                // It might happen that no edge was found previously, which means the monitor gets lost. That is not a
                // problem as a monitor without edges is never performing the event anyway.
                for (Expression expr: mons.getEvents()) {
                    EventExpression ee = (EventExpression)expr;
                    AutomatonEventTransition autEventTrans = eventUsage.get(ee.getEvent());
                    autEventTrans.monitorEdges.addAll(autEventTrans.syncEdges);
                    autEventTrans.syncEdges.clear();
                }
            }
        }

        return eventUsage;
    }

    /**
     * Edge transitions for an event in a single automaton.
     *
     * <p>
     * Note that CIF semantics force that at most one of the transition edges lists is non-empty.
     * </p>
     */
    private static class AutomatonEventTransition {
        /** The event performed by all edges in the instance. */
        public final Event event;

        /** Edges that send a value. */
        public final List<TransitionEdge> sendEdges = list();

        /** Edges that receive a value. */
        public final List<TransitionEdge> receiveEdges = list();

        /** Edges that must synchronize. */
        public final List<TransitionEdge> syncEdges = list();

        /** Edges that may synchronize. */
        public final List<TransitionEdge> monitorEdges = list();

        /** Verify that the automaton indeed uses the event consistently. */
        public void checkNumberOfEdgeKinds() {
            int numEdgeKinds = (sendEdges.isEmpty() ? 0 : 1) + (receiveEdges.isEmpty() ? 0 : 1)
                    + (syncEdges.isEmpty() ? 0 : 1) + (monitorEdges.isEmpty() ? 0 : 1);

            // Allow zero edge kinds for the case that the event is listed in the automaton but it has no edges with it.
            Assert.check(numEdgeKinds <= 1);
        }

        /**
         * Constructor of the {@link AutomatonEventTransition} class.
         *
         * @param event Event described in the instance.
         */
        public AutomatonEventTransition(Event event) {
            this.event = event;
        }
    }

    /**
     * Eliminate CIF features to widen the set of accepted specifications.
     *
     * @param spec Specification to widen.
     */
    private void widenSpec(Specification spec) {
        // Eliminate component definition/instantiation, to avoid having to handle them.
        new ElimComponentDefInst().transform(spec);

        // Eliminate state/event exclusion invariants, to avoid having to handle them.
        // TODO For tracability, it might be better to keep this, and convert it to an additional test in the event
        // function labeled with the invariant.
        new ElimStateEvtExclInvs().transform(spec);

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
            warnOutput.warn("The specification contains CIF/SVG input declarations. These will be ignored.");
        }
    }

    /**
     * Verify that the specification only contains supported language constructs.
     *
     * @param spec Specification to check.
     * @param absSpecPath The absolute local file system path to the CIF file to check.
     */
    private void preCheckSpec(Specification spec, String absSpecPath) {
        PlcGenPreChecker checker = new PlcGenPreChecker(target.supportsArrays());
        checker.reportPreconditionViolations(spec, absSpecPath, "CIF PLC code generator");
    }

    /** CIF PLC code generator precondition checker. Does not support component definition/instantiation. */
    private static class PlcGenPreChecker extends CifPreconditionChecker {
        /**
         * Constructor of the {@link PlcGenPreChecker} class.
         *
         * @param supportArrays Whether the target supports arrays.
         */
        public PlcGenPreChecker(boolean supportArrays) {
            super(
                    // At least one automaton.
                    new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

                    // No initialization predicates in components, ignoring initialization predicates that trivially
                    // hold.
                    new CompNoInitPredsCheck(true),

                    // Discrete variables must have a single initial value.
                    new VarNoDiscWithMultiInitValuesCheck(),

                    // Automata must have a single initial location.
                    new AutOnlyWithOneInitLocCheck(),

                    // Disallow state invariants, except ones that never block behavior.
                    new InvNoSpecificInvsCheck() //
                            .ignoreNeverBlockingInvariants() //
                            .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.STATE,
                                    NoInvariantPlaceKind.ALL_PLACES),

                    // Disallow tau events.
                    new EventNoTauCheck(),

                    // Disallow equations.
                    // TODO This may be too strict. Consider what equations should be allowed more closely.
                    new EqnNotAllowedCheck(),

                    // No urgency.
                    new LocNoUrgentCheck(), //
                    new EdgeNoUrgentCheck(),

                    // Disallow external user-defined functions, and only allow internal user-defined functions with at
                    // least one parameter.
                    new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.EXTERNAL, NoSpecificUserDefFunc.NO_PARAMETER),

                    // Limit internal user-defined function assignments and disallow the 'continue' statement.
                    //
                    // We use CifAddressableUtils.getRefs in the code generation, which doesn't properly handle
                    // multi-assignments to different non-overlapping parts of the same variable.
                    new FuncNoSpecificIntUserDefFuncStatsCheck( //
                            NoSpecificStatement.ASSIGN_MULTI_PARTS_SAME_VAR, //
                            NoSpecificStatement.CONTINUE),

                    // Disallow various types completely and function types in non-call context.
                    new TypeNoSpecificTypesCheck( //
                            NoSpecificType.DICT_TYPES, //
                            NoSpecificType.DIST_TYPES, //
                            NoSpecificType.FUNC_TYPES_AS_DATA, //
                            NoSpecificType.SET_TYPES, //
                            NoSpecificType.STRING_TYPES, //
                            (supportArrays ? NoSpecificType.LIST_TYPES_NON_ARRAY : NoSpecificType.LIST_TYPES)),

                    // Allow only casting to the same type and int to real, allow projection only on tuples and arrays,
                    // forbid string, set, and dictionary literals and time, forbid slicing, and function references
                    // outside call context.
                    new ExprNoSpecificExprsCheck( //
                            NoSpecificExpr.CAST_EXPRS_FROM_STRING, //
                            NoSpecificExpr.CAST_EXPRS_TO_STRING, //
                            NoSpecificExpr.DICT_LITS, //
                            NoSpecificExpr.FUNC_REFS_USER_DEF_AS_DATA, //
                            NoSpecificExpr.PROJECTION_EXPRS_DICTS, //
                            NoSpecificExpr.PROJECTION_EXPRS_LISTS_NON_ARRAY, //
                            NoSpecificExpr.PROJECTION_EXPRS_STRINGS, //
                            NoSpecificExpr.SET_LITS, //
                            NoSpecificExpr.STRING_LITS, //
                            NoSpecificExpr.SLICE_EXPRS, //
                            NoSpecificExpr.TIME_VAR_REFS),

                    // Disallow sampling.
                    new ExprNoSpecificUnaryExprsCheck(NoSpecificUnaryOp.SAMPLE),

                    // Disallow element of, and subset operators. Allow conjunction and disjunction only on booleans,
                    // allow equality only on booleans, integers, reals and enums, allow addition and subtraction only
                    // on integers and reals.
                    new ExprNoSpecificBinaryExprsCheck( //
                            NoSpecificBinaryOp.ADDITION_LISTS, //
                            NoSpecificBinaryOp.ADDITION_STRINGS, //
                            NoSpecificBinaryOp.ADDITION_DICTS, //
                            NoSpecificBinaryOp.ELEMENT_OF, //
                            NoSpecificBinaryOp.EQUAL_DICT, //
                            NoSpecificBinaryOp.EQUAL_LIST, //
                            NoSpecificBinaryOp.EQUAL_SET, //
                            NoSpecificBinaryOp.EQUAL_STRING, //
                            NoSpecificBinaryOp.EQUAL_TUPLE, //
                            NoSpecificBinaryOp.SUBSET, //
                            NoSpecificBinaryOp.SUBTRACTION_DICTS, //
                            NoSpecificBinaryOp.SUBTRACTION_LISTS, //
                            NoSpecificBinaryOp.SUBTRACTION_SETS, //
                            NoSpecificBinaryOp.CONJUNCTION_SETS, //
                            NoSpecificBinaryOp.DISJUNCTION_SETS, //
                            NoSpecificBinaryOp.UNEQUAL_DICT, //
                            NoSpecificBinaryOp.UNEQUAL_LIST, //
                            NoSpecificBinaryOp.UNEQUAL_SET, //
                            NoSpecificBinaryOp.UNEQUAL_STRING, //
                            NoSpecificBinaryOp.UNEQUAL_TUPLE),

                    // Limit standard library functions.
                    new FuncNoSpecificStdLibCheck( //
                            NoSpecificStdLib.STD_LIB_STOCHASTIC_GROUP, //
                            NoSpecificStdLib.STD_LIB_ACOSH, //
                            NoSpecificStdLib.STD_LIB_ASINH, //
                            NoSpecificStdLib.STD_LIB_ATANH, //
                            NoSpecificStdLib.STD_LIB_COSH, //
                            NoSpecificStdLib.STD_LIB_SINH, //
                            NoSpecificStdLib.STD_LIB_TANH, //
                            NoSpecificStdLib.STD_LIB_CEIL, //
                            NoSpecificStdLib.STD_LIB_DELETE, //
                            NoSpecificStdLib.STD_LIB_EMPTY, //
                            NoSpecificStdLib.STD_LIB_FLOOR, //
                            NoSpecificStdLib.STD_LIB_FORMAT, //
                            NoSpecificStdLib.STD_LIB_POP, //
                            NoSpecificStdLib.STD_LIB_ROUND, //
                            NoSpecificStdLib.STD_LIB_SCALE, //
                            NoSpecificStdLib.STD_LIB_SIGN, //
                            NoSpecificStdLib.STD_LIB_SIZE)
            //
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

        // Replace locations in expressions by explicit variables.
        new ElimLocRefExprs(a -> "", // Candidate name for location pointer variables.
                a -> "location", // Candidate name for location pointer enumeration.
                loc -> loc.getName(), // Candidate name for location pointer literals.
                false, // Do not rename locations to ensure unique names.
                true, // Add initialization predicates for the initialization of the location pointer variables.
                false, // Also generate unused location pointers.
                null, // Map of location pointer variables to their automaton.
                true, // Optimize of initialization of location pointers.
                true // Add location pointer expressions to guards.
        ).transform(spec);

        // Simplify the specification, to increase the supported subset. Since simplification of values fills in all
        // constants, we can also remove the constants. However, this may lead to large amounts of duplication for
        // constants with large literal values. Therefore, it is an option. We could always use less expensive variants
        // of value simplification, in the future.
        if (simplifyValues) {
            new SimplifyValues().transform(spec);
            new ElimConsts().transform(spec);
        }

        // If requested, convert enumerations.
        if (enumConversion == ConvertEnums.INTS) {
            new EnumsToInts().transform(spec);
        } else if (enumConversion == ConvertEnums.CONSTS) {
            // This transformation introduces new constants that are intentionally not removed if simplify values is
            // enabled.
            new EnumsToConsts().transform(spec);
        } else if (!target.supportsEnumerations()) {
            // Enumerations are not converted.
            String msg = fmt("Enumerations are not converted, while this is required for %s code. Please set the "
                    + "\"Convert enumerations\" option accordingly.", target.getTargetType().dialogText);
            throw new InvalidInputException(msg);
        }
    }
}
