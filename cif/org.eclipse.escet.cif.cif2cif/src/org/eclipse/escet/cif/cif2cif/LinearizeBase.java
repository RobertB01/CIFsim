//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.common.CifValueUtils.createConjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.createDisjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.makeFalse;
import static org.eclipse.escet.cif.common.CifValueUtils.makeTrue;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlphabet;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInvariant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTauExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newVariableValue;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifEventUtils.Alphabets;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifSortUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.java.CifConstructors;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that linearizes the CIF specification. It performs process-algebraic linearization, thereby
 * eliminating parallel composition and event synchronization.
 *
 * <p>
 * This transformation can only be applied to specifications with at least one automaton.
 * </p>
 *
 * <p>
 * Component definitions/instantiations are eliminated using the "elim-comp-def-inst" CIF to CIF transformation.
 * Automaton 'self' references are eliminated using the "elim-self" CIF to CIF transformation. 'switch' expressions are
 * converted to 'if' expressions using the "switches-to-ifs" CIF to CIF transformation. Equations are eliminated using
 * the "elim-equations" CIF to CIF transformation. Casts from automata to string values are eliminated using the
 * "elim-aut-casts" CIF to CIF transformation.
 * </p>
 *
 * <p>
 * The structure of the model is kept intact as much as possible, to allow objects to retain their absolute identities
 * (absolute names). Since all automata are linearized into a single automaton, the original automata are replaced by
 * groups. These replacement groups will contain as much as possible all declarations, invariants, etc of the original
 * automata. There are exceptions, such as the discrete and continuous variables (they can only be written by the new
 * linearized automaton) and the locations (there will be only one location, in the new linearized automaton).
 * </p>
 *
 * <p>
 * A location pointer variable is introduced for each original automaton that has at least two locations, and the use of
 * locations in expressions is eliminated, by replacing such uses with location pointer references or {@code true} for
 * reference to automata with exactly one location. See also the {@link ElimLocRefExprs} transformation (and its
 * {@link #lpIntroducer} instance), which is used for this.
 * </p>
 *
 * <p>
 * One new automaton is created, called "M". If all original automata have the same kind, the new automaton gets this
 * kind as well. Otherwise, it has kind {@link SupKind#NONE}. The alphabet of this new automaton is the union of
 * alphabets of the original automata. All discrete and continuous variables from the original automata are moved to the
 * new automaton. They are renamed to their absolute names, with all "." characters replaced by "_" characters. One
 * location, named "L", is added. This location is both initial and marked. The initialization predicates, invariants,
 * and marker predicates from locations are merged together. They restrict the initialization and marker predicates of
 * location "L".
 * </p>
 *
 * <p>
 * For the "tau" event, a self-loop is created per original "tau" edge, regardless of choice or no choice mode. For the
 * other events, derived classes determine how the self-loops are created. Monitors are taken into account when merging
 * the guards of the edges (for all events), resulting in simpler guard predicates. Communication is eliminated
 * altogether, and events no longer have data types after linearization. For edges with receives, the 'received value'
 * is replaced (in the updates) by the 'send value'. Currently, no steps are taken to optimize these replacements. If
 * anything is replaced in an assignment, tuple field projections are replaced by tuple index projections, to ensure
 * that we don't project tuple literals with field names.
 * </p>
 *
 * <p>
 * Events and automata are sorted in the same order as the simulator sorts them, before using them for linearization.
 * The output self loop edges are also sorted based on the events that occur on them, with 'tau' events at the end. That
 * way, subsequent code generators that work on linearized output, can generate code that executed events/transitions in
 * the same order as the simulator would choose transitions with automatic/first mode enabled. This leads to better
 * control over the order of the generated code with respect to simulation.
 * </p>
 *
 * <p>
 * If the original automata contain urgent locations and/or urgent edges, a discrete boolean variable "u" is added.
 * Initially, it is "true", and it must always remain so ("invariant u;"). We add self loops (event "tau"), with as
 * guard the urgent locations and guards of urgent edges, such that the edge can be taken if the system is in an urgent
 * location, or an urgent edge is enabled (guard wise). However, these edges update "u" to "false", which violates the
 * target location invariant, meaning we can never take these edges in a transition. Since the edge is also urgent, it
 * means that if the edge is enabled guard wise, time may not progress, thus ensuring the urgency behavior of the
 * original urgent locations and edges.
 * </p>
 *
 * <p>
 * Since some declarations are moved/merged, and some new names are introduced, renaming may be necessary to ensure
 * unique names within a single scope. Enumeration literals representing original locations, that are created as values
 * for the location pointer variables, are added to their original scope and thus keep their original names.
 * </p>
 *
 * <p>
 * This transformation generates non-optimized expressions (mostly predicates). Apply the {@link SimplifyValues}
 * transformation after this transformation, to obtain simpler, more readable results.
 * </p>
 */
public abstract class LinearizeBase extends CifWalker implements CifToCifTransformation {
    /**
     * Mapping from location pointer variables to their absolute names, excluding the name of the automaton they are
     * defined in, including any groups of which the automaton is a part, and including the variable name itself. Filled
     * in-place by {@link #lpIntroducer} when introducing location pointer variables.
     */
    private final Map<DiscVariable, String> absLpNamesMap = map();

    /**
     * Transformation used to introduce location pointer variables, and later to create proper expressions to refer to
     * locations.
     *
     * <p>
     * Notes:
     * <ul>
     * <li>We don't use a prefix for location pointer variables. This ensures they are named after the original
     * component. They may then have the same name as automata in the root of the specification, which can cause scope
     * absolute textual references to be used to refer to anything in these automata from the automaton that contains
     * the identically-named location pointer variables (e.g. '.autname.varname', note the dot at the start).</li>
     * <li>We use an {@code "LPE_"} prefix for location pointer enumerations to make it clear what the enumeration
     * represents.</li>
     * <li>We use an empty prefix for enumeration literals as they replace the original locations, and thus there are no
     * conflicts, and we can keep their original names intact.</li>
     * <li>There is no conflict between location pointer variables and enumeration declarations, as location pointer
     * variables will be moved to the new linearized automaton, while the enumerations will remain in the groups that
     * replace the original automata.</li>
     * <li>We don't add initialization predicates to the location, for initialization of the location pointer variables.
     * We do that as part of the linearization instead, to avoid duplication.</li>
     * <li>We don't optimize. This ensures location pointer variables will be present for all automata with at least two
     * locations.</li>
     * <li>We don't allow the optimization of initialization of location pointers, by analyzing declarations (used for
     * instance in initialization predicates) to see whether they have constant values. Allowing it would mean we can't
     * easily modify the linearization result, e.g. similar to when constants are inlined.</li>
     * <li>We don't add additional location pointer guards on the edges. We do that as part of the linearization
     * instead.</li>
     * </ul>
     * </p>
     */
    protected final ElimLocRefExprs lpIntroducer = new ElimLocRefExprs("", "LPE_", "", false, false, false,
            absLpNamesMap, false, false);

    /**
     * Per automaton, all the alphabets. The automata are sorted in ascending order based on their absolute names
     * (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     */
    protected List<Alphabets> alphabets;

    @Override
    public void transform(Specification spec) {
        // Remove position information, for performance.
        new RemovePositionInfo().transform(spec);

        // Eliminate component definitions/instantiations, to make sure we
        // get actual automata, which we can linearize. Also makes scoping
        // rules trivial.
        new ElimComponentDefInst().transform(spec);

        // Get sorted events. Do this after elimination of component/definition
        // instantiation, to get concrete events.
        List<Event> sortedEvents = list();
        CifCollectUtils.collectEvents(spec, sortedEvents);
        CifSortUtils.sortCifObjects(sortedEvents);

        // Get sorted automata. Do this after elimination of
        // component/definition instantiation, to get concrete automata.
        // We sort them in the same order as the simulator sorts them. This
        // ensures that we can combine edges etc also in the same order.
        // That way, subsequent code generators that work on linearized
        // output, can also generate code that is based on the same order
        // as the simulator would choose with automatic/first mode enabled.
        List<Automaton> auts = list();
        CifCollectUtils.collectAutomata(spec, auts);
        CifSortUtils.sortCifObjects(auts);

        // Eliminate automaton 'self' references.
        new ElimSelf().transform(spec);

        // Convert 'switch' expressions to 'if' expressions.
        new SwitchesToIfs().transform(spec);

        // Eliminate equations, to make sure there are no equations left in
        // the locations, as the locations will disappear during linearization.
        new ElimEquations().transform(spec);

        // Eliminate automaton to string casts. This may introduce new location
        // reference expressions.
        new ElimAutCasts().transform(spec);

        // Introduce location pointer variables for all automata with at least
        // two locations, and eliminate location references from expressions.
        lpIntroducer.transform(spec);

        // Require at least one automaton.
        if (auts.isEmpty()) {
            String msg = "Linearization of CIF specifications without automata is currently not supported.";
            throw new UnsupportedException(msg);
        }

        // Get names in use in specification.
        Set<String> specNames = CifScopeUtils.getSymbolNamesForScope(spec, null);

        // Create new/merged automaton.
        Automaton aut = createAutomaton(spec, specNames);
        Set<String> autNames = set();

        // Merge kinds (if all the same).
        aut.setKind(mergeAutKinds(auts));

        // Cache alphabets of all automata. Getting the alphabet can be
        // expensive if it is not explicitly specified. The send/receive
        // alphabets can't be explicitly specified.
        alphabets = CifEventUtils.getAllAlphabets(auts, null);

        // Merge alphabets (union). All events for which a send or receive
        // is present on an edge, are also added, as channels are eliminated
        // to regular events.
        aut.setAlphabet(mergeAlphabets(sortedEvents));

        // Move discrete and continuous variables (and give them absolute
        // names). Since location pointer variables were already introduced,
        // these are moved as well. For simplicity, we move all continuous
        // variables, even the ones that are not assigned new values by
        // updates on edges.
        moveDiscAndContVars(aut, auts, autNames);

        // Merge initialization predicates, invariants, and marker predicates
        // of the locations.
        mergeLocInvInitMarked(aut, auts);

        // Create new/merged location.
        Location loc = createLocation(aut, autNames);

        // Create edges. Treat 'tau' as a special case, since monitors don't
        // affect 'tau' events, and 'tau' doesn't synchronize. As part of the
        // creation of the edges, monitors are eliminated.
        createEdges(auts, aut, loc);
        mergeTauEdges(auts, aut, loc);

        // Sort edges based on sorted events, in same order as the simulator
        // sorts them. This ensures that we can output the self loops also in
        // that order. That way, subsequent code generators that work on
        // linearized output, can also generate code in the same order as the
        // simulator would choose with automatic/first mode enabled.
        //
        // We can't sort in an EList, as it does not allow duplicates, so we
        // sort in a copy of the list.
        List<Edge> edges = copy(loc.getEdges());
        Collections.sort(edges, new EdgeSorter(sortedEvents));
        loc.getEdges().clear();
        loc.getEdges().addAll(edges);

        // Remove channel data types.
        removeChannelDataTypes(sortedEvents);

        // Handle urgency (for locations and edges).
        handleUrgency(auts, aut, autNames);

        // Convert automata to groups. This removes the original automata. We
        // do this here at the end, and not earlier, to keep them contained
        // somewhere in the specification.
        convertAutomataToGroups(auts);
    }

    /**
     * Creates a new automaton, which will contain the merger of the original automata. The automaton is also added to
     * the given specification.
     *
     * @param spec The specification.
     * @param specNames The names already in use in the specification.
     * @return The newly created automaton, with unique name.
     */
    private Automaton createAutomaton(Specification spec, Set<String> specNames) {
        // Create and add merged automaton.
        Automaton aut = newAutomaton();
        spec.getComponents().add(aut);

        // Set unique automaton name. We use a fixed name here, as CIF to CIF
        // transformations have no options.
        String name = "M"; // 'M' for 'Merged'.
        if (specNames.contains(name)) {
            String oldName = name;
            name = CifScopeUtils.getUniqueName(name, specNames, Collections.emptySet());
            warn("Merged automaton \"%s\" is renamed to \"%s\".", oldName, name);
        }
        specNames.add(name);
        aut.setName(name);

        // Return new/merged automaton.
        return aut;
    }

    /**
     * Merge automaton supervisory kinds.
     *
     * @param auts The automata for which to merge the supervisory kinds.
     * @return The supervisory kind that all the automata share, or {@code SupKind#NONE} if they don't all have the same
     *     kind.
     */
    private SupKind mergeAutKinds(List<Automaton> auts) {
        // Merge kinds (keep only if all the same kind).
        SupKind mergedKind = null;
        boolean first = true;
        for (Automaton aut: auts) {
            if (first) {
                mergedKind = aut.getKind();
                first = false;
            } else if (mergedKind != aut.getKind()) {
                mergedKind = SupKind.NONE;
            } // else: same kind, nothing to update.
        }
        Assert.notNull(mergedKind);
        return mergedKind;
    }

    /**
     * Merge alphabets from the given automata. All events for which a send or receive is present on an edge, are also
     * added, as channels are eliminated to regular events. Thus, the merged alphabet is the union of the regular
     * alphabets, send alphabets, and receive alphabets, of all the automata.
     *
     * @param sortedEventDecls The event declarations from the specification, sorted in ascending order based on their
     *     absolute names (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @return The merged alphabets.
     */
    private Alphabet mergeAlphabets(List<Event> sortedEventDecls) {
        // Merge regular alphabets, send alphabets, and receive alphabets.
        Set<Event> alphabetEvents = set();
        for (Alphabets autAlphabets: alphabets) {
            alphabetEvents.addAll(autAlphabets.syncAlphabet);
            alphabetEvents.addAll(autAlphabets.sendAlphabet);
            alphabetEvents.addAll(autAlphabets.recvAlphabet);
        }

        // Get alphabet events in sorted order.
        List<Event> events = copy(sortedEventDecls);
        events.retainAll(alphabetEvents);

        // Create new alphabet.
        Alphabet alphabet = newAlphabet();

        // Add event references to the alphabet.
        List<Expression> eventRefs = alphabet.getEvents();
        for (Event event: events) {
            EventExpression eventRef = newEventExpression();
            eventRef.setEvent(event);
            eventRef.setType(newBoolType());
            eventRefs.add(eventRef);
        }

        // Return new/merged alphabet.
        return alphabet;
    }

    /**
     * Creates a new location, for the merged automaton. The location is also added to the given automaton.
     *
     * @param aut The merged automaton. Is modified in-place.
     * @param autNames The names already in use in the automaton. Is modified in-place.
     * @return The newly created location, with unique name.
     */
    private Location createLocation(Automaton aut, Set<String> autNames) {
        // Create and add location.
        Location loc = newLocation();
        aut.getLocations().add(loc);

        // Set unique location name. We use a fixed name here, as CIF to CIF
        // transformations have no options.
        String name = "L"; // 'L' for 'Location'.
        if (autNames.contains(name)) {
            String oldName = name;
            name = CifScopeUtils.getUniqueName(name, autNames, Collections.emptySet());
            warn("Merged location \"%s\" is renamed to \"%s\".", oldName, name);
        }
        autNames.add(name);
        loc.setName(name);

        // Set initial and marked to 'true'. They are restricted to the proper
        // values by initialization and marker predicates from the
        // components.
        loc.getInitials().add(makeTrue());
        loc.getMarkeds().add(makeTrue());

        // Return new/merged location.
        return loc;
    }

    /**
     * Merges the discrete and continuous variables from the original automata into the new automaton.
     *
     * @param mergedAut The new/merged automaton. Is modified in-place.
     * @param auts The original automata, sorted in ascending order based on their absolute names (without escaping).
     *     See also {@link CifSortUtils#sortCifObjects}. Are modified in-place.
     * @param autNames The names already in use in the new/merged automaton. Is modified in-place.
     */
    private void moveDiscAndContVars(Automaton mergedAut, List<Automaton> auts, Set<String> autNames) {
        // Gather discrete and continuous variable declarations, and give them absolute names.
        List<Declaration> decls = list();
        Set<String> declNames = set();
        for (Automaton aut: auts) {
            for (Declaration decl: aut.getDeclarations()) {
                // Consider only discrete and continuous variables.
                if (!(decl instanceof DiscVariable) && !(decl instanceof ContVariable)) {
                    continue;
                }

                // Add declaration.
                decls.add(decl);

                // Rename declaration to absolute name (candidate for now).
                // For location pointer variables we can use the name that has
                // been decided by the location pointer introducer.
                String name = absLpNamesMap.get(decl);
                if (name == null) {
                    name = CifTextUtils.getAbsName(decl, false);
                }
                name = name.replace(".", "_");
                decl.setName(name);

                // Add name to set of candidate names.
                declNames.add(name);
            }
        }

        // One by one, move the declarations to the new automaton.
        // Also give them their final unique names.
        List<Declaration> newDecls = mergedAut.getDeclarations();
        for (Declaration decl: decls) {
            // Move declaration.
            newDecls.add(decl);

            // Rename if necessary.
            String name = decl.getName();
            if (autNames.contains(name)) {
                String oldName = name;
                name = CifScopeUtils.getUniqueName(name, autNames, declNames);
                decl.setName(name);
                warn("Declaration \"%s\" is renamed to \"%s\".", oldName, name);
            }
            autNames.add(name);
        }
    }

    /**
     * Merges invariants, initialization predicates, and marker predicates, from the locations of the original automata
     * to the new/merged automaton.
     *
     * @param auts The original automata, sorted in ascending order based on their absolute names (without escaping).
     *     See also {@link CifSortUtils#sortCifObjects}. Are modified in-place.
     * @param mergedAut The new/merged automaton. Is modified in-place.
     */
    private void mergeLocInvInitMarked(Automaton mergedAut, List<Automaton> auts) {
        // Invariants in locations have 'true' default, and use conjunctions
        // to combine (within a location). Invariants in automata also have
        // 'true' default and also use conjunctions to combine (within an
        // automaton).
        for (Automaton aut: auts) {
            for (Location loc: aut.getLocations()) {
                for (Invariant inv: copy(loc.getInvariants())) {
                    // Modify 'inv' to 'loc => inv'.
                    Expression lexpr = lpIntroducer.createLocRef(loc);

                    BinaryExpression bexpr = newBinaryExpression();
                    bexpr.setOperator(BinaryOperator.IMPLICATION);
                    bexpr.setLeft(lexpr);
                    bexpr.setRight(inv.getPredicate());
                    bexpr.setType(newBoolType());

                    inv.setPredicate(bexpr);

                    // Move invariant to merged automaton.
                    mergedAut.getInvariants().add(inv);
                }
            }
        }

        // Initialization predicates in locations have 'false' default, and use
        // conjunctions to combine (within a location). Initialization
        // predicates in automata have 'true' default, and use conjunctions to
        // combine (within an automaton). We get the most intuitive results if
        // we combine the combined initialization predicates of the location
        // with the location itself using a conjunction, and then combine
        // multiple locations using disjunctions. That is, we get
        // '(loc1 and init1) or (loc2 and init2) or ...' for each automaton.
        for (Automaton aut: auts) {
            List<Expression> inits = list();

            for (Location loc: aut.getLocations()) {
                // Add 'loc and init'.
                Expression init = loc.getInitials().isEmpty() ? makeFalse() : createConjunction(loc.getInitials());

                Expression lexpr = lpIntroducer.createLocRef(loc);
                inits.add(createConjunction(list(lexpr, init)));
            }

            mergedAut.getInitials().add(createDisjunction(inits));
        }

        // Marker predicates in locations have 'false' default, and use
        // conjunctions to combine (within a location). Marker predicates in
        // automata have 'true' default, and use conjunctions to combine
        // (within an automaton). We get the most intuitive results if we
        // combine the combined marker predicate of the location with the
        // location itself using a conjunction, and then combine multiple
        // locations using disjunctions. That is, we get
        // '(loc1 and marker1) or (loc2 and marker2) or ...' for each
        // automaton.
        for (Automaton aut: auts) {
            List<Expression> markers = list();

            for (Location loc: aut.getLocations()) {
                // Add 'loc and marker'.
                Expression marker = loc.getMarkeds().isEmpty() ? makeFalse() : createConjunction(loc.getMarkeds());

                Expression lexpr = lpIntroducer.createLocRef(loc);
                markers.add(createConjunction(list(lexpr, marker)));
            }

            mergedAut.getMarkeds().add(createDisjunction(markers));
        }
    }

    /**
     * Merges all 'tau' edges of the different automata together into new edges for the given merged location. One new
     * edge is created per original 'tau' edge.
     *
     * <p>
     * Note that 'tau' can never be a monitor event.
     * </p>
     *
     * @param auts The original automata, sorted in ascending order based on their absolute names (without escaping).
     *     See also {@link CifSortUtils#sortCifObjects}.
     * @param mergedAut The merged automaton.
     * @param mergedLoc The merged location. Is modified in-place.
     */
    private void mergeTauEdges(List<Automaton> auts, Automaton mergedAut, Location mergedLoc) {
        // Process all edges with a 'tau' event.
        for (Automaton aut: auts) {
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    // Skip non-tau edges.
                    boolean isTauEdge = false;
                    if (edge.getEvents().isEmpty()) {
                        isTauEdge = true;
                    }
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        if (edgeEvent.getEvent() instanceof TauExpression) {
                            isTauEdge = true;
                            break;
                        }
                    }
                    if (!isTauEdge) {
                        continue;
                    }

                    // Combine guards, and add location pointer guard.
                    Expression newGuard = createConjunction(
                            concat(lpIntroducer.createLocRef(loc), deepclone(edge.getGuards())));

                    // Copy updates (to preserve them in case of multiple
                    // events on the edge). Location pointer update should
                    // already be present, if needed.
                    List<Update> newUpdates = deepclone(edge.getUpdates());

                    // Create new edge.
                    Edge newEdge = newEdge();
                    newEdge.getGuards().add(newGuard);
                    newEdge.getUpdates().addAll(newUpdates);

                    // Set 'tau' event, for clarity. We could omit this, as
                    // no events means 'tau' as well.
                    TauExpression tauRef = newTauExpression();
                    tauRef.setType(newBoolType());
                    EdgeEvent edgeEvent = newEdgeEvent();
                    edgeEvent.setEvent(tauRef);
                    newEdge.getEvents().add(edgeEvent);

                    // Add new edge.
                    mergedLoc.getEdges().add(newEdge);
                }
            }
        }
    }

    /**
     * Creates the edges for the given merged location, based on the edges of the original automata.
     *
     * <p>
     * Event 'tau' is treated as a special case by the {@link #mergeTauEdges} method, since monitors don't affect 'tau'
     * events, and 'tau' doesn't synchronize. That is, this method ignores 'tau' events. This method handles all other
     * events, including channels.
     * </p>
     *
     * @param auts The original automata, sorted in ascending order based on their absolute names (without escaping).
     *     See also {@link CifSortUtils#sortCifObjects}.
     * @param mergedAut The merged automaton.
     * @param mergedLoc The merged location. Is modified in-place.
     */
    protected abstract void createEdges(List<Automaton> auts, Automaton mergedAut, Location mergedLoc);

    /**
     * Creates unique copies of the updates and replaces all occurrences of the 'received' value in them by the given
     * 'send' value.
     *
     * @param updates The updates.
     * @param sendValue The 'send' value, or {@code null} if not applicable.
     * @return The copies of the updates, with all necessary replacements applied.
     */
    protected static List<Update> replaceUpdates(List<Update> updates, Expression sendValue) {
        // Create unique copies of the updates.
        List<Update> rslt = deepclone(updates);

        // If no 'send value', then no 'received value', so no replacements
        // needed, and a unique copy is enough.
        if (sendValue == null) {
            return rslt;
        }

        // Create update expression 'received value' replacer.
        UpdateExprReplacer replacer = new UpdateExprReplacer(sendValue);

        // Apply replacements in each of the updates.
        for (Update update: rslt) {
            replaceUpdate(update, replacer);
        }

        // Return replaced copies of the updates.
        return rslt;
    }

    /**
     * Replaces all occurrences of the 'received value' in the update, by the 'send value', recursively.
     *
     * @param update The update. Is modified in-place.
     * @param replacer The replacer to use to replace 'received value' expressions by copies of the 'send value'
     *     expression.
     */
    private static void replaceUpdate(Update update, UpdateExprReplacer replacer) {
        // 'if' update.
        if (update instanceof IfUpdate) {
            IfUpdate iupdate = (IfUpdate)update;
            for (Expression guard: iupdate.getGuards()) {
                replaceUpdateExpr(guard, replacer);
            }
            for (Update child: iupdate.getThens()) {
                replaceUpdate(child, replacer);
            }
            for (ElifUpdate elif: iupdate.getElifs()) {
                for (Expression guard: elif.getGuards()) {
                    replaceUpdateExpr(guard, replacer);
                }
                for (Update child: elif.getThens()) {
                    replaceUpdate(child, replacer);
                }
            }
            for (Update child: iupdate.getElses()) {
                replaceUpdate(child, replacer);
            }
            return;
        }

        // Assignment. Replace not only in the value, but also in the
        // addressable, as the addressable may contain projections that
        // contain the 'received' value.
        Assignment asgn = (Assignment)update;
        replaceUpdateExpr(asgn.getAddressable(), replacer);
        replaceUpdateExpr(asgn.getValue(), replacer);
    }

    /**
     * Replaces all occurrences of the 'received value' in the expression, by the given 'send value'.
     *
     * @param expr The expression in which to look for 'received value' expressions. Is modified in-place.
     * @param replacer The replacer to use to replace 'received value' expressions by copies of the 'send value'
     *     expression.
     */
    private static void replaceUpdateExpr(Expression expr, UpdateExprReplacer replacer) {
        // Reset 'replaced' status.
        replacer.replaced = false;
        replacer.replace(expr);

        // If we replace '?' in '?[a]' by '(1,2)', we get '(1,2)[a]', which
        // is invalid. Therefore, if we replaced anything, replace tuple field
        // projection by tuple index projection. Note that we do this even if
        // it is unnecessary, as it is difficult to decide when it is needed,
        // and when it is not needed. For instance: '(?, y)[0][a]' also
        // projects '?', but the projection is not on '?' directly.
        if (replacer.replaced) {
            new ElimTupleFieldProjs().transform(expr);
        }
    }

    /**
     * Retrieve the variables introduced for the location pointers of the original automata.
     *
     * @return The variables that represent the locations of the original automata.
     */
    public List<DiscVariable> getLPVariables() {
        List<DiscVariable> lpVariables = listc(absLpNamesMap.size());
        lpVariables.addAll(absLpNamesMap.keySet());
        return lpVariables;
    }

    /** Replacer to use to replace 'received value' expressions by copies of the 'send value' expression. */
    private static class UpdateExprReplacer extends CifWalker {
        /** The 'send value'. */
        public final Expression sendValue;

        /** Whether any 'received value' expressions have been replaced. */
        public boolean replaced = false;

        /**
         * Constructor for the {@link UpdateExprReplacer} class.
         *
         * @param sendValue The 'send value'.
         */
        public UpdateExprReplacer(Expression sendValue) {
            this.sendValue = sendValue;
        }

        /**
         * Replaces 'received value' expressions anywhere in the given expression, by copies of the {@link #sendValue}
         * expression.
         *
         * @param expr The expression in which to recursively look for 'received value' expressions.
         */
        public void replace(Expression expr) {
            walkExpression(expr);
        }

        @Override
        protected void walkReceivedExpression(ReceivedExpression expr) {
            Expression replacement = deepclone(sendValue);
            EMFHelper.updateParentContainment(expr, replacement);
            replaced = true;
        }
    }

    /**
     * Remove the data types from all channels.
     *
     * @param events The events that may be channels. Are modified in-place.
     */
    private void removeChannelDataTypes(List<Event> events) {
        for (Event event: events) {
            event.setType(null);
        }
    }

    /**
     * Handles urgency, for locations and edges.
     *
     * @param auts The original automata, sorted in ascending order based on their absolute names (without escaping).
     *     See also {@link CifSortUtils#sortCifObjects}.
     * @param mergedAut The new/merged automaton. Is modified in-place.
     * @param autNames The names already in use in the automaton. Is modified in-place.
     */
    private void handleUrgency(List<Automaton> auts, Automaton mergedAut, Set<String> autNames) {
        // Initialize urgency conditions, which will become guards.
        List<Expression> guards = list();

        // Add guard conditions.
        for (Automaton aut: auts) {
            for (Location loc: aut.getLocations()) {
                // Add condition for urgent locations.
                if (loc.isUrgent()) {
                    // Add 'lp = loc'.
                    guards.add(lpIntroducer.createLocRef(loc));
                }

                for (Edge edge: loc.getEdges()) {
                    // Add condition for urgent edges.
                    if (edge.isUrgent()) {
                        // Add 'lp = loc and guard1 and guard2 and ...'
                        Expression lexpr = lpIntroducer.createLocRef(loc);
                        List<Expression> eguards = deepclone(edge.getGuards());
                        guards.add(createConjunction(concat(lexpr, eguards)));
                    }
                }
            }
        }

        // If no urgency, then we are done.
        if (guards.isEmpty()) {
            return;
        }

        // Add 'disc bool u = true' variable.
        VariableValue uvalue = newVariableValue();
        uvalue.getValues().add(makeTrue());

        DiscVariable u = newDiscVariable();
        u.setType(newBoolType());
        u.setValue(uvalue);

        String name = "u";
        if (autNames.contains(name)) {
            String oldName = name;
            name = CifScopeUtils.getUniqueName(name, autNames, Collections.emptySet());
            warn("Variable \"%s\", introduced during linearization to enforce urgency, is renamed to \"%s\".", oldName,
                    name);
        }
        u.setName(name);
        autNames.add(name);

        mergedAut.getDeclarations().add(u);

        // Add state invariant 'u'. The invariant is added as plant invariant.
        // This ensures that tools that require a kind can support linearized
        // specifications. It is not added as a requirement invariant, as we
        // don't want synthesis to modify the model due to urgency.
        DiscVariableExpression uref = newDiscVariableExpression();
        uref.setType(newBoolType());
        uref.setVariable(u);

        Invariant inv = newInvariant();
        inv.setPredicate(uref);
        inv.setSupKind(SupKind.PLANT);
        inv.setInvKind(InvKind.STATE);

        mergedAut.getInvariants().add(inv);

        // Add self loop: 'when guards now do u := false'. We use disjunction
        // of all collected guards, as if any of these conditions does not
        // hold, we have an urgent state.
        Assignment uasgn = newAssignment();
        uasgn.setAddressable(deepclone(uref));
        uasgn.setValue(makeFalse());

        Edge edge = newEdge();
        edge.getGuards().add(createDisjunction(guards));
        edge.setUrgent(true);
        edge.getUpdates().add(uasgn);

        mergedAut.getLocations().get(0).getEdges().add(edge);
    }

    /**
     * Convert automata to groups.
     *
     * @param auts The automata to convert to groups. The automata and their parents are modified in-place.
     */
    private void convertAutomataToGroups(List<Automaton> auts) {
        for (Automaton aut: auts) {
            // Replace automaton by a new group.
            Group group = CifConstructors.newGroup();
            EMFHelper.updateParentContainment(aut, group);

            // Group gets same name as original automaton. This keeps the
            // absolute names of the component and its children intact.
            group.setName(aut.getName());

            // Move all that is contained in the automaton to the group.
            group.getDeclarations().addAll(aut.getDeclarations());
            group.getInitials().addAll(aut.getInitials());
            group.getInvariants().addAll(aut.getInvariants());
            group.getIoDecls().addAll(aut.getIoDecls());
            group.getMarkeds().addAll(aut.getMarkeds());

            // Equations have already been eliminated.
            Assert.check(aut.getEquations().isEmpty());

            // The following aspects of automata are considered elsewhere:
            // - alphabet
            // - kind
            // - locations
            // - monitors
        }
    }

    /** Sorter to sort edges based on the order of some given events. Edges with 'tau' events are put at the end. */
    private static class EdgeSorter implements Comparator<Edge> {
        /** Mapping from events to the sort order. */
        private final Map<Event, Integer> order;

        /**
         * Constructor for the {@link EdgeSorter} class.
         *
         * @param events The events, in the desired order.
         */
        public EdgeSorter(List<Event> events) {
            // Ensure maximum integer value can be used for 'tau'.
            Assert.check(events.size() < Integer.MAX_VALUE);

            // Create order mapping.
            order = mapc(events.size());
            for (int i = 0; i < events.size(); i++) {
                order.put(events.get(i), i);
            }
        }

        @Override
        public int compare(Edge edge1, Edge edge2) {
            // Get event references.
            Assert.check(edge1.getEvents().size() == 1);
            Assert.check(edge2.getEvents().size() == 1);
            Expression eventRef1 = first(edge1.getEvents()).getEvent();
            Expression eventRef2 = first(edge2.getEvents()).getEvent();

            // Get orders for the events.
            int order1;
            if (eventRef1 instanceof TauExpression) {
                order1 = Integer.MAX_VALUE;
            } else {
                Event event1 = ((EventExpression)eventRef1).getEvent();
                order1 = order.get(event1);
            }

            int order2;
            if (eventRef2 instanceof TauExpression) {
                order2 = Integer.MAX_VALUE;
            } else {
                Event event2 = ((EventExpression)eventRef2).getEvent();
                order2 = order.get(event2);
            }

            // Compare orders.
            return Integer.compare(order1, order2);
        }
    }
}
