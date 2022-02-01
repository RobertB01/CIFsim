//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.scopes;

import static org.eclipse.escet.cif.common.CifLocationUtils.getPossibleInitialLocs;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlphabet;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeReceive;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeSend;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifUpdate;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfUpdate;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newMonitors;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.NO_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.cif.typechecker.ExprContext.DEFAULT_CTXT;
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.ALLOW_EVENT;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.list2set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.EventEquality;
import org.eclipse.escet.cif.common.EventRefSet;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.cif.parser.ast.ACompDecl;
import org.eclipse.escet.cif.parser.ast.ADecl;
import org.eclipse.escet.cif.parser.ast.AEquation;
import org.eclipse.escet.cif.parser.ast.automata.AAlphabetDecl;
import org.eclipse.escet.cif.parser.ast.automata.AAssignmentUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AAutomatonBody;
import org.eclipse.escet.cif.parser.ast.automata.AEdgeEvent;
import org.eclipse.escet.cif.parser.ast.automata.AEdgeEvent.Direction;
import org.eclipse.escet.cif.parser.ast.automata.AEdgeLocationElement;
import org.eclipse.escet.cif.parser.ast.automata.AElifUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AEquationLocationElement;
import org.eclipse.escet.cif.parser.ast.automata.AIfUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AInitialLocationElement;
import org.eclipse.escet.cif.parser.ast.automata.AInvariantLocationElement;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.parser.ast.automata.ALocationElement;
import org.eclipse.escet.cif.parser.ast.automata.AMarkedLocationElement;
import org.eclipse.escet.cif.parser.ast.automata.AMonitorDecl;
import org.eclipse.escet.cif.parser.ast.automata.AUpdate;
import org.eclipse.escet.cif.parser.ast.automata.AUrgentLocationElement;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ANameExpression;
import org.eclipse.escet.cif.parser.ast.expressions.ATauExpression;
import org.eclipse.escet.cif.parser.ast.tokens.AName;
import org.eclipse.escet.cif.typechecker.AssignmentUniquenessChecker;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.ExprContext;
import org.eclipse.escet.cif.typechecker.SymbolTableEntry;
import org.eclipse.escet.cif.typechecker.declwrap.EventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalEventDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FormalLocationDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.LocationDeclWrap;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Automaton scope. */
public class AutScope extends ParentScope<Automaton> {
    /** The expression type checking context to use for event references. */
    private static final ExprContext EVT_REF_CTXT = ExprContext.DEFAULT_CTXT.add(ALLOW_EVENT);

    /** The CIF AST automaton declaration object representing this scope. */
    private final ACompDecl autDecl;

    /** The list of AST locations for this automaton. */
    private final List<ALocation> astLocs;

    /**
     * Constructor for the {@link AutScope} class.
     *
     * @param obj The CIF metamodel automaton object representing this scope.
     * @param autDecl The CIF AST automaton declaration object representing this scope.
     * @param parent The parent scope.
     * @param tchecker The CIF type checker to use.
     */
    public AutScope(Automaton obj, ACompDecl autDecl, ParentScope<?> parent, CifTypeChecker tchecker) {
        super(obj, parent, tchecker);
        this.autDecl = autDecl;
        this.astLocs = ((AAutomatonBody)autDecl.body).locations;
    }

    @Override
    protected String getScopeTypeName() {
        return "aut";
    }

    @Override
    protected ComplexComponent getComplexComponent() {
        return obj;
    }

    @Override
    protected Group getGroup() {
        // Automata are not groups.
        throw new UnsupportedOperationException();
    }

    @Override
    protected ComponentDef getComponentDef() {
        // Automata are not component definitions.
        throw new UnsupportedOperationException();
    }

    @Override
    protected Automaton getAutomaton() {
        return obj;
    }

    @Override
    public List<ALocation> getAstLocs() {
        return astLocs;
    }

    @Override
    public void addChildScope(SymbolScope<?> scope) {
        // Automata have no child scopes.
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean isSubScope() {
        return true;
    }

    @Override
    protected boolean isRootScope() {
        return false;
    }

    @Override
    public String getName() {
        return obj.getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(obj);
    }

    @Override
    public String getAbsText() {
        return fmt("automaton \"%s\"", getAbsName());
    }

    @Override
    protected void tcheckScopeFull() {
        typeCheckAutomaton((AAutomatonBody)autDecl.body, obj, this, tchecker);
    }

    /**
     * Type checks an automaton.
     *
     * @param astBody The CIF AST representation of the automaton body.
     * @param mmAut The CIF metamodel representation of the automaton.
     * @param autScope The scope of the automaton.
     * @param tchecker The CIF type checker to use.
     */
    @SuppressWarnings("null")
    public static void typeCheckAutomaton(AAutomatonBody astBody, Automaton mmAut, ParentScope<?> autScope,
            CifTypeChecker tchecker)
    {
        // Process alphabet.
        int alphabetCount = 0;
        AAlphabetDecl astAlpha = null;
        EventRefSet alphaSet = null;
        for (ADecl decl: astBody.decls) {
            if (decl instanceof AAlphabetDecl) {
                // No duplicate alphabet definitions.
                alphabetCount++;
                if (alphabetCount > 1) {
                    tchecker.addProblem(ErrMsg.AUT_DUPL_ALPHABET, astAlpha.position, CifTextUtils.getAbsName(mmAut));
                    tchecker.addProblem(ErrMsg.AUT_DUPL_ALPHABET, decl.position, CifTextUtils.getAbsName(mmAut));
                    throw new SemanticException();
                }

                // Convert alphabet.
                List<AName> events = ((AAlphabetDecl)decl).events;
                if (events == null) {
                    events = list();
                }

                astAlpha = ((AAlphabetDecl)decl);
                Alphabet alpha = newAlphabet();
                mmAut.setAlphabet(alpha);

                alphaSet = new EventRefSet(EventEquality.PESSIMISTIC);

                EventRefSet alphaSetOpt = new EventRefSet(EventEquality.OPTIMISTIC);

                for (AName event1: events) {
                    // Resolve to event.
                    SymbolTableEntry entry = autScope.resolve(event1.position, event1.name, tchecker, autScope);
                    EventParameter param;
                    Event event2;
                    if (entry instanceof EventDeclWrap) {
                        event2 = ((EventDeclWrap)entry).getObject();
                        param = null;
                    } else if (entry instanceof FormalEventDeclWrap) {
                        param = ((FormalEventDeclWrap)entry).getObject();
                        event2 = param.getEvent();
                    } else {
                        tchecker.addProblem(ErrMsg.RESOLVE_NON_EVENT, event1.position, entry.getAbsName());
                        throw new SemanticException();
                    }

                    // Event parameter must allow synchronization.
                    if (param != null && !CifEventUtils.eventParamSupportsSync(param)) {
                        tchecker.addProblem(ErrMsg.ALPHABET_NON_SYNC_PARAM, event1.position,
                                CifTextUtils.getAbsName(event2), CifTextUtils.getAbsName(mmAut));
                        throw new SemanticException();
                    }

                    // Get event reference expression.
                    Expression eventExpr = autScope.resolveAsExpr(event1.name, event1.position, "", tchecker);

                    // Duplicate event? Optimistic equality may lead to
                    // false positives.
                    Expression duplicate = alphaSetOpt.add(eventExpr);
                    if (duplicate != null) {
                        tchecker.addProblem(ErrMsg.ALPHABET_DUPL_EVENT, eventExpr.getPosition(),
                                CifTextUtils.getAbsName(event2), CifTextUtils.getAbsName(mmAut));
                        tchecker.addProblem(ErrMsg.ALPHABET_DUPL_EVENT, duplicate.getPosition(),
                                CifTextUtils.getAbsName(event2), CifTextUtils.getAbsName(mmAut));
                        // Non-fatal error.
                    }

                    // Add event to alphabet.
                    alphaSet.add(eventExpr);
                    alpha.getEvents().add(eventExpr);
                }
            }
        }

        // Copy alphabet (event reference) set, to keep original set. The
        // fixed set is pessimistic, like the original set.
        EventRefSet alphaSetFixed = (alphaSet == null) ? null : new EventRefSet(alphaSet);

        // Process monitors.
        int monitorCount = 0;
        AMonitorDecl astMonitor = null;
        EventRefSet monitorSet = null;
        for (ADecl decl: astBody.decls) {
            if (decl instanceof AMonitorDecl) {
                // No duplicate monitor definitions.
                monitorCount++;
                if (monitorCount > 1) {
                    tchecker.addProblem(ErrMsg.AUT_DUPL_MONITOR, astMonitor.position, CifTextUtils.getAbsName(mmAut));
                    tchecker.addProblem(ErrMsg.AUT_DUPL_MONITOR, decl.position, CifTextUtils.getAbsName(mmAut));
                    throw new SemanticException();
                }

                // Convert monitors.
                astMonitor = (AMonitorDecl)decl;
                Monitors mmMonitors = newMonitors();
                mmAut.setMonitors(mmMonitors);

                monitorSet = new EventRefSet(EventEquality.OPTIMISTIC);

                // Process monitor events.
                for (AName event1: astMonitor.events) {
                    // Resolve to event.
                    SymbolTableEntry entry = autScope.resolve(event1.position, event1.name, tchecker, autScope);
                    Event event2;
                    if (entry instanceof EventDeclWrap) {
                        event2 = ((EventDeclWrap)entry).getObject();
                    } else if (entry instanceof FormalEventDeclWrap) {
                        event2 = ((FormalEventDeclWrap)entry).getObject().getEvent();
                    } else {
                        tchecker.addProblem(ErrMsg.RESOLVE_NON_EVENT, event1.position, entry.getAbsName());
                        throw new SemanticException();
                    }

                    // Get event reference expression.
                    Expression eventExpr = autScope.resolveAsExpr(event1.name, event1.position, "", tchecker);

                    // Duplicate event? Optimistic equality may lead to
                    // false positives.
                    Expression duplicate = monitorSet.add(eventExpr);
                    if (duplicate != null) {
                        tchecker.addProblem(ErrMsg.MONITORS_DUPL_EVENT, eventExpr.getPosition(),
                                CifTextUtils.getAbsName(event2), CifTextUtils.getAbsName(mmAut));
                        tchecker.addProblem(ErrMsg.MONITORS_DUPL_EVENT, duplicate.getPosition(),
                                CifTextUtils.getAbsName(event2), CifTextUtils.getAbsName(mmAut));
                        // Non-fatal error.
                    }

                    // Add event to monitors.
                    mmMonitors.getEvents().add(eventExpr);
                }
            }
        }

        // Initialize default alphabet.
        EventRefSet defaultAlpha = new EventRefSet(EventEquality.PESSIMISTIC);

        // Process locations.
        for (int i = 0; i < astBody.locations.size(); i++) {
            ALocation loc1 = astBody.locations.get(i);

            // Add location if nameless. Note that named locations are already
            // present, as they were added when creating the symbol table.
            if (loc1.name == null) {
                // Nameless location must be only location in automaton (no
                // other named or nameless locations).
                if (!mmAut.getLocations().isEmpty()) {
                    tchecker.addProblem(ErrMsg.NAMELESS_LOC_NOT_ALONE, loc1.position, CifTextUtils.getAbsName(mmAut));
                    throw new SemanticException();
                }

                // Add nameless location.
                Location loc2 = newLocation();
                loc2.setPosition(loc1.position);
                mmAut.getLocations().add(loc2);
            }

            // Get metamodel location.
            Location loc2 = mmAut.getLocations().get(i);
            typeCheckLocation(loc1, loc2, alphaSet, alphaSetFixed, defaultAlpha, autScope, tchecker);
        }

        // Check for events in the alphabet beyond the default alphabet
        // (the events on the edges, used to synchronize). Pessimistic equality
        // may lead to false positives.
        if (alphaSet != null && !alphaSet.isEmpty()) {
            for (Expression eventRef: alphaSet) {
                Expression uref = CifTypeUtils.unwrapExpression(eventRef);
                Event event = (Event)CifScopeUtils.getRefObjFromRef(uref);

                boolean monitored = monitorSet != null && (monitorSet.isEmpty() || monitorSet.contains(eventRef));
                if (monitored) {
                    // Monitoring event that is not on any edge (essentially
                    // self looped in every location).
                    tchecker.addProblem(ErrMsg.MONITOR_EVENT_NO_EDGE, eventRef.getPosition(),
                            CifTextUtils.getAbsName(mmAut), CifTextUtils.getAbsName(event));
                    // Non-fatal error.
                } else {
                    // Globally disabled.
                    tchecker.addProblem(ErrMsg.ALPHABET_DISABLED_EVENT, eventRef.getPosition(),
                            CifTextUtils.getAbsName(mmAut), CifTextUtils.getAbsName(event));
                    // Non-fatal error.
                }
            }
        }

        // Get actual alphabet, with pessimistic equality notion.
        EventRefSet alphabet = (alphaSetFixed == null) ? defaultAlpha : alphaSetFixed;

        // Check for monitor events not in the alphabet. Pessimistic equality
        // may lead to false positives.
        if (monitorSet != null) {
            for (Expression eventRef: monitorSet) {
                if (!alphabet.contains(eventRef)) {
                    Expression uref = CifTypeUtils.unwrapExpression(eventRef);
                    PositionObject evt = CifScopeUtils.getRefObjFromRef(uref);
                    tchecker.addProblem(ErrMsg.MONITOR_EVENT_NOT_IN_ALPHABET, eventRef.getPosition(),
                            CifTextUtils.getAbsName(evt), CifTextUtils.getAbsName(mmAut));
                    // Non-fatal error.
                }
            }
        }

        // Check for useless monitoring (we have a monitor, and we monitor
        // the entire alphabet, but the alphabet is empty).
        if (monitorSet != null && monitorSet.isEmpty() && alphabet.isEmpty()) {
            tchecker.addProblem(ErrMsg.MONITOR_EMPTY_ALPHABET, astMonitor.position, autScope.getAbsName());
            // Non-fatal problem.
        }

        // Check for initial locations.
        Set<Location> initialLocs = getPossibleInitialLocs(mmAut, false);
        if (initialLocs.isEmpty()) {
            tchecker.addProblem(ErrMsg.AUT_NO_INIT_LOC, mmAut.getPosition(), CifTextUtils.getAbsName(mmAut));
            // Non-fatal problem.
        }

        // Check for unreachable locations. Note that if the automaton has a
        // single location, then if it is not an initial location, the check
        // above reported that already. If it is the initial location, it is
        // reachable. As such, skip automata with only one location.
        if (mmAut.getLocations().size() > 1) {
            Set<Location> unreachables = getUnreachableLocs(mmAut, initialLocs);
            for (Location loc: unreachables) {
                // The location has a name, as the automaton has at least two
                // locations.
                tchecker.addProblem(ErrMsg.LOC_UNREACHABLE, loc.getPosition(), loc.getName(),
                        CifTextUtils.getAbsName(mmAut));
                // Non-fatal problem.
            }
        }
    }

    /**
     * Type checks a location.
     *
     * @param astLoc The CIF AST representation of the location.
     * @param mmLoc The CIF metamodel representation of the location.
     * @param alphaSet The alphabet, as a set of event reference expressions, or {@code null} if not specified. This set
     *     is to be modified in-place, by removing all event references occurring on the edges. Must use the
     *     {@link EventEquality#PESSIMISTIC pessimistic} equality notion.
     * @param alphaSetFixed The alphabet, as a set of event reference expressions, or {@code null} if not specified.
     *     This set is not to be modified. Must use the {@link EventEquality#PESSIMISTIC pessimistic} equality notion.
     * @param defaultAlpha The default alphabet of the automaton, to be extended in-place. Must use the
     *     {@link EventEquality#PESSIMISTIC pessimistic} equality notion.
     * @param autScope The scope of the automaton.
     * @param tchecker The CIF type checker to use.
     */
    private static void typeCheckLocation(ALocation astLoc, Location mmLoc, EventRefSet alphaSet,
            EventRefSet alphaSetFixed, EventRefSet defaultAlpha, ParentScope<?> autScope, CifTypeChecker tchecker)
    {
        if (astLoc.elements == null) {
            return;
        }

        // Process all but the edges.
        Position urgPos = null;
        for (ALocationElement elem: astLoc.elements) {
            if (elem instanceof AInitialLocationElement) {
                List<AExpression> preds = ((AInitialLocationElement)elem).preds;
                if (preds == null) {
                    BoolType type = newBoolType();
                    type.setPosition(elem.position);

                    BoolExpression pred = newBoolExpression();
                    pred.setValue(true);
                    pred.setPosition(elem.position);
                    pred.setType(type);

                    mmLoc.getInitials().add(pred);
                } else {
                    for (AExpression pred: preds) {
                        Expression pred2 = transExpression(pred, BOOL_TYPE_HINT, autScope, null, tchecker);

                        CifType t = pred2.getType();
                        CifType nt = CifTypeUtils.normalizeType(t);
                        if (!(nt instanceof BoolType)) {
                            tchecker.addProblem(ErrMsg.INIT_NON_BOOL, pred.position, CifTextUtils.typeToStr(t));
                            // Non-fatal error.
                        } else {
                            // Add initial predicate only if it has the correct
                            // type, to ensure we can get the possible initial
                            // locations.
                            mmLoc.getInitials().add(pred2);
                        }
                    }
                }
            } else if (elem instanceof AInvariantLocationElement) {
                AInvariantLocationElement invElem = (AInvariantLocationElement)elem;
                ParentScope.tcheckInvs(invElem.kind, invElem.invariants, autScope, tchecker, mmLoc.getInvariants());
            } else if (elem instanceof AEquationLocationElement) {
                // Skip checking of equations for variables that are not in
                // scope, if any of the variables failed checking, as equations
                // may be incomplete in that case. If this is the case, the
                // equations have already been set to 'null'.
                if (autScope.mmEquations != null) {
                    List<AEquation> eqns = ((AEquationLocationElement)elem).equations;
                    for (AEquation astEqn: eqns) {
                        Equation eqn = autScope.mmEquations.get(astEqn);
                        if (eqn != null) {
                            mmLoc.getEquations().add(eqn);
                        } else {
                            tchecker.addProblem(ErrMsg.EQN_VAR_NOT_IN_SCOPE, astEqn.position, astEqn.variable.id,
                                    autScope.getAbsText());
                            // Non-fatal error.
                        }
                    }
                }
            } else if (elem instanceof AMarkedLocationElement) {
                List<AExpression> preds = ((AMarkedLocationElement)elem).preds;
                if (preds == null) {
                    BoolType type = newBoolType();
                    type.setPosition(elem.position);

                    BoolExpression pred = newBoolExpression();
                    pred.setValue(true);
                    pred.setPosition(elem.position);
                    pred.setType(type);

                    mmLoc.getMarkeds().add(pred);
                } else {
                    for (AExpression pred: preds) {
                        Expression pred2 = transExpression(pred, BOOL_TYPE_HINT, autScope, null, tchecker);
                        mmLoc.getMarkeds().add(pred2);

                        CifType t = pred2.getType();
                        CifType nt = CifTypeUtils.normalizeType(t);
                        if (!(nt instanceof BoolType)) {
                            tchecker.addProblem(ErrMsg.MARKED_NON_BOOL, pred.position, CifTextUtils.typeToStr(t));
                            // Non-fatal error.
                        }
                    }
                }
            } else if (elem instanceof AUrgentLocationElement) {
                mmLoc.setUrgent(true);
                if (urgPos == null) {
                    // First urgent location element.
                    urgPos = elem.position;
                } else {
                    // Duplicate urgent location element.
                    String locTxt = CifTextUtils.getLocationText2(mmLoc);
                    tchecker.addProblem(ErrMsg.LOC_DUPL_URGENT, urgPos, locTxt);
                    tchecker.addProblem(ErrMsg.LOC_DUPL_URGENT, elem.position, locTxt);
                    // Non-fatal problem.
                }
            } else if (elem instanceof AEdgeLocationElement) {
                // Skip for now.
            } else {
                throw new RuntimeException("Unknown loc elem: " + elem);
            }
        }

        // Process edges.
        for (ALocationElement elem: astLoc.elements) {
            if (elem instanceof AEdgeLocationElement) {
                typeCheckEdge(mmLoc, (AEdgeLocationElement)elem, alphaSet, alphaSetFixed, defaultAlpha, autScope,
                        tchecker);
            }
        }
    }

    /**
     * Type checks an edge.
     *
     * @param loc The CIF metamodel representation of the source location.
     * @param astEdge The CIF AST representation of the edge.
     * @param alphaSet The alphabet, as a set of event reference expressions, or {@code null} if not specified. This set
     *     is to be modified in-place, by removing all event references occurring on the edges. Must use the
     *     {@link EventEquality#PESSIMISTIC pessimistic} equality notion.
     * @param alphaSetFixed The alphabet, as a set of event reference expressions, or {@code null} if not specified.
     *     This set is not to be modified. Must use the {@link EventEquality#PESSIMISTIC pessimistic} equality notion.
     * @param defaultAlpha The default alphabet of the automaton, to be extended in-place. Must use the
     *     {@link EventEquality#PESSIMISTIC pessimistic} equality notion.
     * @param autScope The scope of the automaton.
     * @param tchecker The CIF type checker to use.
     */
    @SuppressWarnings("null")
    private static void typeCheckEdge(Location loc, AEdgeLocationElement astEdge, EventRefSet alphaSet,
            EventRefSet alphaSetFixed, EventRefSet defaultAlpha, ParentScope<?> autScope, CifTypeChecker tchecker)
    {
        // Construct and add edge.
        Edge edge = newEdge();
        edge.setPosition(astEdge.position);
        loc.getEdges().add(edge);

        // Process urgency.
        edge.setUrgent(astEdge.coreEdge.urgentPos != null);
        if (loc.isUrgent() && edge.isUrgent()) {
            String locTxt = CifTextUtils.getLocationText2(loc);
            tchecker.addProblem(ErrMsg.EDGE_URG_LOC_URG, astEdge.coreEdge.urgentPos, locTxt);
            // Non-fatal problem.
        }

        // Resolve target location and set it.
        Location targetLoc = null;
        if (astEdge.target != null) {
            // Resolve the location. No convoluted references check as syntax only allows a single identifier for
            // target locations of edges.
            SymbolTableEntry entry = autScope.resolve(astEdge.target.position, astEdge.target.id, tchecker, null);
            if (entry instanceof LocationDeclWrap) {
                targetLoc = ((LocationDeclWrap)entry).getObject();
            } else if (entry instanceof FormalLocationDeclWrap) {
                tchecker.addProblem(ErrMsg.EDGE_TGT_FORMAL_PARAM, astEdge.target.position, entry.getAbsName(),
                        autScope.getAbsName());
                // Non-fatal problem.
            } else {
                tchecker.addProblem(ErrMsg.EDGE_NON_LOC_TARGET, astEdge.target.position, entry.getAbsName());
                throw new SemanticException();
            }
            edge.setTarget(targetLoc);
        }

        // Check target location scoping. This is a paranoia check only, as it
        // should hold by construction, as we can only specify an identifier
        // in the ASCII syntax, not a general name. We also skip the check for
        // location parameters, which can be referred to with an identifier.
        if (targetLoc != null) {
            // Paranoia check only.
            Automaton srcLocAut = (Automaton)loc.eContainer();
            Automaton tgtLocAut = (Automaton)targetLoc.eContainer();
            Assert.check(srcLocAut == tgtLocAut);
        }

        // Guards.
        List<Expression> guards = edge.getGuards();
        for (AExpression g: astEdge.coreEdge.guards) {
            Expression guard = transExpression(g, BOOL_TYPE_HINT, autScope, null, tchecker);
            CifType t = guard.getType();
            CifType nt = CifTypeUtils.normalizeType(t);
            if (!(nt instanceof BoolType)) {
                tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                // Non-fatal error.
            }
            guards.add(guard);
        }

        // Events.
        EventRefSet otherEventRefs = new EventRefSet(EventEquality.OPTIMISTIC);
        boolean hasReceive = false;
        boolean hasNonReceive = false;
        for (AEdgeEvent astEdgeEvent: astEdge.coreEdge.events) {
            // Get event reference expression.
            AExpression astEventRef = astEdgeEvent.eventRef;

            // Check for 'tau'.
            boolean isTau;
            if (astEventRef instanceof ATauExpression) {
                isTau = true;
            } else if (astEventRef instanceof ANameExpression) {
                ANameExpression nameExpr = (ANameExpression)astEventRef;
                isTau = false;
                Assert.check(!nameExpr.derivative);
            } else {
                String msg = "Unexpected event ref: " + astEventRef;
                throw new RuntimeException(msg);
            }

            // Resolve to event reference expression.
            Expression eventRef = transExpression(astEventRef, BOOL_TYPE_HINT, autScope, EVT_REF_CTXT, tchecker);

            // Get event and absolute name.
            Expression uEventRef = CifTypeUtils.unwrapExpression(eventRef);
            Event event;
            String absName;
            EventParameter param = null;
            if (uEventRef instanceof TauExpression) {
                event = null;
                absName = "tau";
            } else if (uEventRef instanceof EventExpression) {
                event = ((EventExpression)uEventRef).getEvent();
                absName = CifTextUtils.getAbsName(event);
                if (event.eContainer() instanceof EventParameter) {
                    param = (EventParameter)event.eContainer();
                }
            } else {
                // Reference expression that is not an event reference.
                PositionObject evt = CifScopeUtils.getRefObjFromRef(uEventRef);
                tchecker.addProblem(ErrMsg.RESOLVE_NON_EVENT, astEventRef.position, CifTextUtils.getAbsName(evt));
                throw new SemanticException();
            }

            // Get event type info.
            CifType eventType = null;
            if (event != null) {
                eventType = event.getType();
            }
            boolean isChannel = eventType != null;
            boolean isVoid = eventType instanceof VoidType;

            // Get direction information.
            Direction direction = astEdgeEvent.direction;
            boolean isComm = direction != Direction.NONE;

            if (direction == Direction.RECEIVE) {
                hasReceive = true;
            } else {
                hasNonReceive = true;
            }

            // Only allow communication for channels.
            if (isComm && !isChannel) {
                String dirTxt = (direction == Direction.SEND) ? "send" : "receive";
                tchecker.addProblem(ErrMsg.CHANNEL_COMM_NON_CHAN, astEdgeEvent.position, dirTxt, absName);
                throw new SemanticException();
            }

            // Check event parameter usage restrictions.
            if (param != null) {
                String problem = null;
                switch (direction) {
                    case SEND:
                        if (!CifEventUtils.eventParamSupportsSend(param)) {
                            problem = "send (!)";
                        }
                        break;

                    case RECEIVE:
                        if (!CifEventUtils.eventParamSupportsRecv(param)) {
                            problem = "receive (?)";
                        }
                        break;

                    case NONE:
                        if (!CifEventUtils.eventParamSupportsSync(param)) {
                            problem = "synchronization (~)";
                        }
                        break;
                }

                if (problem != null) {
                    tchecker.addProblem(ErrMsg.EDGE_EVT_PARAM_ILLEGAL_USE, astEdgeEvent.position, absName, problem);
                    // Non-fatal problem.
                }
            }

            // Type check send value.
            boolean valueMandatory = direction == Direction.SEND && isChannel && !isVoid;
            boolean valueForbidden = direction != Direction.SEND || !isChannel || isVoid;

            Expression value = null;
            if (astEdgeEvent.value != null) {
                if (valueForbidden) {
                    Assert.check(isChannel && isVoid);
                    tchecker.addProblem(ErrMsg.CHANNEL_VOID_WITH_VALUE, astEdgeEvent.position, absName);
                    // Non-fatal error.
                } else {
                    value = transExpression(astEdgeEvent.value, eventType, autScope, null, tchecker);

                    // Compatible types for send value and channel.
                    CifType valueType = value.getType();
                    if (!CifTypeUtils.checkTypeCompat(eventType, valueType, RangeCompat.CONTAINED)) {
                        tchecker.addProblem(ErrMsg.CHANNEL_SEND_TYPE_MISMATCH, astEdgeEvent.position,
                                CifTextUtils.typeToStr(valueType), absName, CifTextUtils.typeToStr(eventType));
                        // Non-fatal error.
                    }
                }
            } else if (valueMandatory) {
                tchecker.addProblem(ErrMsg.CHANNEL_NON_VOID_NEED_VALUE, astEdgeEvent.position, absName,
                        CifTextUtils.typeToStr(eventType));
                // Non-fatal error.
            }

            // Create edge event.
            EdgeEvent edgeEvent = null;
            switch (direction) {
                case NONE:
                    edgeEvent = newEdgeEvent();
                    break;
                case SEND:
                    edgeEvent = newEdgeSend();
                    break;
                case RECEIVE:
                    edgeEvent = newEdgeReceive();
                    break;
            }
            edgeEvent.setPosition(astEdgeEvent.position);
            edgeEvent.setEvent(eventRef);
            if (direction == Direction.SEND) {
                ((EdgeSend)edgeEvent).setValue(value);
            }

            // Add event to edge.
            edge.getEvents().add(edgeEvent);

            // Duplicate event on edge? Optimistic equality may lead to
            // false positives. Since different uses on a single edge are
            // checked elsewhere, we only need to report for duplicates within
            // a single use here. We check duplicate synchronization uses and
            // duplicate receive uses. Duplicate send uses may be useful, if
            // different values are being sent.
            if (direction != Direction.SEND) {
                Expression duplicate = otherEventRefs.get(eventRef);
                if (duplicate != null) {
                    tchecker.addProblem(ErrMsg.EDGE_DUPL_EVENT, eventRef.getPosition(), absName);
                    tchecker.addProblem(ErrMsg.EDGE_DUPL_EVENT, duplicate.getPosition(), absName);
                    // Non-fatal error.
                }
                otherEventRefs.add(eventRef);
            }

            // Event in alphabet? Pessimistic equality may lead to false
            // positives.
            if (alphaSet != null && !isTau && !isComm) {
                // Make sure event is in alphabet.
                boolean found = alphaSetFixed.contains(eventRef);
                if (!found) {
                    Expression uref = CifTypeUtils.unwrapExpression(eventRef);
                    PositionObject evt = CifScopeUtils.getRefObjFromRef(uref);
                    tchecker.addProblem(ErrMsg.EVENT_NOT_IN_ALPHABET, astEventRef.position,
                            CifTextUtils.getAbsName(evt), autScope.getAbsName());
                    // Non-fatal error.
                }

                // Remove event reference from 'alphaSet', for always blocked
                // events checking, later on.
                alphaSet.remove(eventRef);
            }

            // Add to default alphabet.
            if (!isTau && !isComm) {
                defaultAlpha.add(eventRef);
            }
        }

        // If one of the events is a receive, they all must be receives.
        if (hasReceive && hasNonReceive) {
            for (EdgeEvent edgeEvent: edge.getEvents()) {
                if (edgeEvent instanceof EdgeReceive) {
                    continue;
                } else if (edgeEvent instanceof EdgeSend) {
                    tchecker.addProblem(ErrMsg.EDGE_RCV_EXPECTED, edgeEvent.getPosition(), "Sending over a channel");
                    // Non-fatal problem.
                } else {
                    Assert.check(edgeEvent.getClass() == EdgeEventImpl.class);
                    tchecker.addProblem(ErrMsg.EDGE_RCV_EXPECTED, edgeEvent.getPosition(),
                            "Synchronizing over an event");
                    // Non-fatal problem.
                }
            }
        }

        // In case of multiple receives, the types of the channels must match.
        CifType channelType = null;
        Position firstReceivePos = null;
        if (hasReceive) {
            for (EdgeEvent edgeEvent: edge.getEvents()) {
                // Skip non-receive edge events.
                if (!(edgeEvent instanceof EdgeReceive)) {
                    continue;
                }

                // Skip 'tau'. Is invalid in this context, but we continued
                // checking above.
                Expression eventRef = edgeEvent.getEvent();
                if (eventRef instanceof TauExpression) {
                    continue;
                }

                // Get type.
                eventRef = CifTypeUtils.unwrapExpression(eventRef);
                Event event = ((EventExpression)eventRef).getEvent();
                CifType eventType = event.getType();

                // Set/check type.
                if (channelType == null) {
                    // Set type and position.
                    channelType = eventType;
                    firstReceivePos = edgeEvent.getPosition();
                } else {
                    // Check type equality.
                    if (!CifTypeUtils.checkTypeCompat(channelType, eventType, RangeCompat.EQUAL)) {
                        tchecker.addProblem(ErrMsg.CHANNEL_RCVS_TYPE_MISMATCH, firstReceivePos,
                                CifTextUtils.typeToStr(channelType), CifTextUtils.typeToStr(eventType));
                        tchecker.addProblem(ErrMsg.CHANNEL_RCVS_TYPE_MISMATCH, edgeEvent.getPosition(),
                                CifTextUtils.typeToStr(channelType), CifTextUtils.typeToStr(eventType));
                        // Non-fatal error.
                    }
                }
            }
        }

        // Get update expression type checking context.
        ExprContext context = DEFAULT_CTXT;
        if (hasReceive) {
            context = context.setReceiveType(channelType);
        }

        // Updates.
        List<Update> updates = edge.getUpdates();
        for (AUpdate update1: astEdge.coreEdge.updates) {
            Update update2 = typeCheckUpdate(update1, autScope, context, tchecker);
            updates.add(update2);
        }

        // Check for assignments to unique parts of variables, in the updates.
        Map<Declaration, Set<Pair<Position, List<Object>>>> asgnMap = map();
        AssignmentUniquenessChecker.checkUniqueAsgns(updates, asgnMap, tchecker, ErrMsg.DUPL_VAR_ASGN_EDGE);
    }

    /**
     * Type checks an addressable.
     *
     * @param astAddr The CIF AST representation of the addressable.
     * @param scope The scope of the automaton.
     * @param context The expression type checking context to use, or {@code null} for the default context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel representation of the addressable.
     */
    private static Expression typeCheckAddressable(AExpression astAddr, ParentScope<?> scope, ExprContext context,
            CifTypeChecker tchecker)
    {
        // Type check addressable expression.
        Expression addr = transExpression(astAddr, NO_TYPE_HINT, scope, context, tchecker);

        // Make sure we refer to local discrete and/or continuous variables.
        for (Expression expr: CifAddressableUtils.getRefExprs(addr)) {
            // Get variable.
            Expression uexpr = CifTypeUtils.unwrapExpression(expr);
            Declaration var;
            if (uexpr instanceof DiscVariableExpression) {
                var = ((DiscVariableExpression)uexpr).getVariable();
            } else if (uexpr instanceof ContVariableExpression) {
                var = ((ContVariableExpression)uexpr).getVariable();
            } else if (uexpr instanceof ReceivedExpression) {
                throw new RuntimeException("Parser doesn't allow this.");
            } else {
                // Reference to wrong kind of object.
                PositionObject obj = CifScopeUtils.getRefObjFromRef(uexpr);
                tchecker.addProblem(ErrMsg.RESOLVE_NON_ASGN_VAR, expr.getPosition(), CifTextUtils.getAbsName(obj));
                throw new SemanticException();
            }

            // Check variable scoping: disallow non-local variables.
            EObject varParent = var.eContainer();
            Assert.check(varParent instanceof ComplexComponent);
            Automaton curAut = scope.getAutomaton();
            if (varParent != curAut) {
                // This should actually hold by construction for discrete
                // variables, as we can only specify an identifier in the
                // ASCII syntax (not a general name), and ancestor scopes
                // can't declare discrete variables. For continuous
                // variables however, ancestor scopes can define them as
                // well.
                tchecker.addProblem(ErrMsg.ASGN_NON_LOCAL_VAR, expr.getPosition(), CifTextUtils.getAbsName(var),
                        CifTextUtils.getAbsName(curAut));
                // Non-fatal error.
            }

            // String projections are not allowed as addressables.
            PositionObject varAncestor = (PositionObject)expr.eContainer();
            while (varAncestor instanceof ProjectionExpression) {
                ProjectionExpression proj = (ProjectionExpression)varAncestor;
                CifType type = proj.getChild().getType();
                CifType ntype = CifTypeUtils.normalizeType(type);
                if (ntype instanceof StringType) {
                    tchecker.addProblem(ErrMsg.ASGN_STRING_PROJ, varAncestor.getPosition(),
                            CifTextUtils.getAbsName(var));
                    // Non-fatal error.
                }
                varAncestor = (PositionObject)varAncestor.eContainer();
            }
        }

        // Return metamodel representation of the addressable expression.
        return addr;
    }

    /**
     * Type checks an update.
     *
     * @param astUpdate The CIF AST representation of the update.
     * @param scope The scope of the automaton.
     * @param context The expression type checking context to use, or {@code null} for the default context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel representation of the update.
     */
    private static Update typeCheckUpdate(AUpdate astUpdate, ParentScope<?> scope, ExprContext context,
            CifTypeChecker tchecker)
    {
        if (astUpdate instanceof AAssignmentUpdate) {
            return typeCheckAssignment((AAssignmentUpdate)astUpdate, scope, context, tchecker);
        } else if (astUpdate instanceof AIfUpdate) {
            return typeCheckIfUpdate((AIfUpdate)astUpdate, scope, context, tchecker);
        } else {
            throw new RuntimeException("Unknown update: " + astUpdate);
        }
    }

    /**
     * Type checks an assignment update.
     *
     * @param astUpdate The CIF AST representation of the update.
     * @param scope The scope of the automaton.
     * @param context The expression type checking context to use, or {@code null} for the default context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel representation of the update.
     */
    private static Assignment typeCheckAssignment(AAssignmentUpdate astUpdate, ParentScope<?> scope,
            ExprContext context, CifTypeChecker tchecker)
    {
        // Construct assignment.
        Assignment asgn = newAssignment();
        asgn.setPosition(astUpdate.position);

        // Type check and set addressable expression.
        Expression addr = typeCheckAddressable(astUpdate.addressable, scope, context, tchecker);
        asgn.setAddressable(addr);

        // Type check and set value.
        Expression value = transExpression(astUpdate.value, addr.getType(), scope, context, tchecker);
        asgn.setValue(value);

        // Compatible types for addressable and value.
        CifType valueType = value.getType();
        CifType addrType = addr.getType();
        if (!CifTypeUtils.checkTypeCompat(addrType, valueType, RangeCompat.OVERLAP)) {
            tchecker.addProblem(ErrMsg.ASGN_TYPE_VALUE_MISMATCH, astUpdate.position, CifTextUtils.typeToStr(valueType),
                    CifTextUtils.typeToStr(addrType));
            // Non-fatal error.
        }

        // Return metamodel representation of the assignment.
        return asgn;
    }

    /**
     * Type checks an 'if' update.
     *
     * @param astUpdate The CIF AST representation of the update.
     * @param scope The scope of the automaton.
     * @param context The expression type checking context to use, or {@code null} for the default context.
     * @param tchecker The CIF type checker to use.
     * @return The CIF metamodel representation of the update.
     */
    private static IfUpdate typeCheckIfUpdate(AIfUpdate astUpdate, ParentScope<?> scope, ExprContext context,
            CifTypeChecker tchecker)
    {
        // Construct 'if' update.
        IfUpdate update = newIfUpdate();
        update.setPosition(astUpdate.position);

        // Guards.
        List<Expression> guards = update.getGuards();
        for (AExpression g: astUpdate.guards) {
            Expression guard = transExpression(g, BOOL_TYPE_HINT, scope, context, tchecker);
            CifType t = guard.getType();
            CifType nt = CifTypeUtils.normalizeType(t);
            if (!(nt instanceof BoolType)) {
                tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                // Non-fatal error.
            }
            guards.add(guard);
        }

        // Thens.
        List<Update> thens = update.getThens();
        for (AUpdate then1: astUpdate.thens) {
            Update then2 = typeCheckUpdate(then1, scope, context, tchecker);
            thens.add(then2);
        }

        // Elses.
        List<Update> elses = update.getElses();
        for (AUpdate else1: astUpdate.elses) {
            Update else2 = typeCheckUpdate(else1, scope, context, tchecker);
            elses.add(else2);
        }

        // Elifs.
        List<ElifUpdate> elifs = update.getElifs();
        for (AElifUpdate elif1: astUpdate.elifs) {
            ElifUpdate elif2 = newElifUpdate();
            elif2.setPosition(elif1.position);
            elifs.add(elif2);

            // Guards.
            guards = elif2.getGuards();
            for (AExpression g: elif1.guards) {
                Expression guard = transExpression(g, BOOL_TYPE_HINT, scope, context, tchecker);
                CifType t = guard.getType();
                CifType nt = CifTypeUtils.normalizeType(t);
                if (!(nt instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                    // Non-fatal error.
                }
                guards.add(guard);
            }

            // Elif/thens.
            List<Update> elifThens = elif2.getThens();
            for (AUpdate then1: elif1.thens) {
                Update then2 = typeCheckUpdate(then1, scope, context, tchecker);
                elifThens.add(then2);
            }
        }

        // Return metamodel representation of the 'if' update.
        return update;
    }

    /**
     * Returns the unreachable locations of an automaton, given the possible initial locations.
     *
     * @param aut The automaton for which to check for unreachable locations.
     * @param initialLocs The locations of {@code aut} that could be the initial locations.
     * @return The unreachable locations.
     * @see CifLocationUtils#getPossibleInitialLocs
     */
    private static Set<Location> getUnreachableLocs(Automaton aut, Set<Location> initialLocs) {
        // Initialization.
        Set<Location> unreachables = list2set(aut.getLocations());
        unreachables.removeAll(initialLocs);

        Deque<Location> queue = new ArrayDeque<>(initialLocs);

        // Process until queue is empty.
        while (!queue.isEmpty()) {
            // Get next location to process.
            Location loc = queue.pop();

            // Process target locations.
            for (Edge edge: loc.getEdges()) {
                Location target = edge.getTarget();
                if (target == null) {
                    continue;
                }

                boolean todo = unreachables.remove(target);
                if (todo) {
                    queue.add(target);
                }
            }
        }

        // Return the unreachable locations.
        return unreachables;
    }
}
