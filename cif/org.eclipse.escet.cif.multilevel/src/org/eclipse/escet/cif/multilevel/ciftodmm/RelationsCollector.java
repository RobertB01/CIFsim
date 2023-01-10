//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.ciftodmm;

import static org.eclipse.escet.cif.common.CifCollectUtils.getComplexComponentsStream;
import static org.eclipse.escet.cif.common.CifEventUtils.getAlphabet;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.cif.metamodel.java.CifWithArgWalker;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Class that collects information from the CIF specification for building DMMs. */
public class RelationsCollector extends CifWalker {
    /** Constant denoting there is no valid index available. */
    private static final int INVALID_INDEX = -1;

    /** Elements of the specification and their index. */
    private Map<PositionObject, Integer> elementsToIndex = map();

    /** Reverse storage of {@link #elementsToIndex}, from index to the element. */
    private List<PositionObject> elementsByIndex = list();

    /** Indices of elements that should not be taken into account for grouping requirements. */
    private BitSet irrelevantRequirementAccessRelations = new BitSet();

    /** Indices of plant elements. */
    private BitSet plantElementIndices = new BitSet();

    /** Indices of requirement elements. */
    private BitSet requirementElementIndices = new BitSet();

    /** Relations by group (that is, one plant and requirement element). */
    private Map<Integer, OwnedAndAccessedElements> relationsByGroup = map();

    /** Collector for collecting accessed variables and locations from expressions. */
    private ExpressionCollector exprCollector = new ExpressionCollector();

    /** Walker for collecting accessed variables and locations of an expression. */
    private static class ExpressionCollector extends CifWithArgWalker<Set<PositionObject>> {
        /**
         * Collect the referenced variables and locations from the expression.
         *
         * @param expr Expression to analyze.
         * @return The found collection of accessed variables and locations.
         */
        public Set<PositionObject> collectDecls(Expression expr) {
            Set<PositionObject> accessedElements = set();
            walkExpression(expr, accessedElements);
            return accessedElements;
        }

        @Override
        protected void preprocessDiscVariableExpression(DiscVariableExpression discVarExpr,
                Set<PositionObject> accessedElements)
        {
            accessedElements.add(discVarExpr.getVariable());
        }

        @Override
        protected void preprocessInputVariableExpression(InputVariableExpression inpVarExpr,
                Set<PositionObject> accessedElements)
        {
            accessedElements.add(inpVarExpr.getVariable());
        }

        @Override
        protected void preprocessLocationExpression(LocationExpression locExpr, Set<PositionObject> accessedElements) {
            accessedElements.add(locExpr.getLocation());
        }

        @Override
        protected void preprocessAlgVariableExpression(AlgVariableExpression algvarExpr,
                Set<PositionObject> accessedElements)
        {
            // Also search the algvar expression.
            walkExpression(algvarExpr.getVariable().getValue(), accessedElements);
        }
    }

    /**
     * Process the specification recursively, finding all elements and relations between them.
     *
     * @param spec Specification to analyze for relations.
     */
    public void collect(Specification spec) {
        getComplexComponentsStream(spec).forEach(cc -> collectComponent(cc));
    }

    /**
     * Process a single component, finding all elements and relations between them in the component.
     *
     * @param cc Component to analyze.
     */
    private void collectComponent(ComplexComponent cc) {
        // If the component is an automaton, register it as plant or requirement element.
        // Also get an owner index if appropriate.
        Automaton aut;
        int ownerIndex;
        boolean ownerIsRequirement;
        if (cc instanceof Automaton) {
            aut = (Automaton)cc;
            switch (aut.getKind()) {
                case PLANT:
                    ownerIndex = registerPlantElement(aut);
                    ownerIsRequirement = false;
                    break;
                case REQUIREMENT:
                    ownerIndex = registerRequirementElement(aut);
                    ownerIsRequirement = true;
                    break;
                default:
                    throw new AssertionError(fmt("Unexpected automaton kind \"%s\" found.", aut.getKind()));
            }
        } else {
            aut = null;
            ownerIndex = INVALID_INDEX;
            ownerIsRequirement = false;
        }

        // Process declarations and invariants in the component.
        collectDeclarations(cc.getDeclarations(), ownerIndex);
        collectInvariants(cc.getInvariants());

        // For automata collect at more places in the component.
        if (aut != null) {
            // Process alphabet.
            for (Event evt: getAlphabet(aut)) {
                registerAccessedRelation(evt, ownerIndex);
            }
            // Process locations.
            for (Location loc: aut.getLocations()) {
                registerOwnedRelation(loc, ownerIndex);

                // If the location is not in a requirement automaton, ignore it in requirement grouping relations.
                // Not ignoring this location will also enable merging requirements that both access the location
                // but that is fine as eventually the owning requirement is added too in that case.
                if (!ownerIsRequirement) {
                    irrelevantRequirementAccessRelations.set(getIndex(loc));
                }

                // Process initials and markeds.
                expressionAccess(loc.getInitials(), ownerIndex);
                expressionAccess(loc.getMarkeds(), ownerIndex);

                // Process edges.
                for (Edge edge: loc.getEdges()) {
                    eventAccess(edge.getEvents(), ownerIndex); // Events.
                    expressionAccess(edge.getGuards(), ownerIndex); // Guards.
                    updateAccess(edge.getUpdates(), ownerIndex); // Updates.
                }
            }
        }
    }

    /**
     * Register access to all the events at an edge by an accessing element.
     *
     * @param ees Events being accessed.
     * @param accessingGroupIndex Index of the group that accesses the events.
     * @note The accessing group may or may not own the events.
     */
    private void eventAccess(List<EdgeEvent> ees, int accessingGroupIndex) {
        if (ees != null) {
            for (EdgeEvent ee: ees) {
                eventAccess(ee, accessingGroupIndex);
            }
        }
    }

    /**
     * Register access to the event at an edge by an accessing element.
     *
     * @param ee Event being accessed.
     * @param accessingGroupIndex Index of the group that accesses the event.
     * @note The accessing group may or may not own the event.
     */
    private void eventAccess(EdgeEvent ee, int accessingGroupIndex) {
        Expression evtExpr = ee.getEvent();
        if (evtExpr instanceof EventExpression) {
            Event evt = ((EventExpression)evtExpr).getEvent();
            registerAccessedRelation(evt, accessingGroupIndex);
        }
    }

    /**
     * Register access to elements in the update at an edge by an accessing element.
     *
     * @param updates Updates to analyze for access.
     * @param accessingGroupIndex Index of the group that accesses the event.
     * @note The accessing group may or may not own the found elements in the update.
     */
    private void updateAccess(List<Update> updates, int accessingGroupIndex) {
        Deque<Update> notDoneUpdates = new ArrayDeque<>();
        notDoneUpdates.addAll(updates);

        while (!notDoneUpdates.isEmpty()) {
            Update upd = notDoneUpdates.poll();
            if (upd instanceof Assignment) {
                Assignment asg = (Assignment)upd;
                expressionAccess(asg.getAddressable(), accessingGroupIndex);
                expressionAccess(asg.getValue(), accessingGroupIndex);
            } else {
                Assert.check(upd instanceof IfUpdate);
                IfUpdate ifUpd = (IfUpdate)upd;
                expressionAccess(ifUpd.getGuards(), accessingGroupIndex);
                notDoneUpdates.addAll(ifUpd.getThens());
                notDoneUpdates.addAll(ifUpd.getElses());
                for (ElifUpdate elif: ifUpd.getElifs()) {
                    expressionAccess(elif.getGuards(), accessingGroupIndex);
                    notDoneUpdates.addAll(elif.getThens());
                }
            }
        }
    }

    /**
     * Register access to the variables and locations in the expressions.
     *
     * @param exprs Expressions to analyze.
     * @param accessingGroupIndex Index of an element that accesses the variables or locations.
     * @note The accessing group may or may not own the variables or locations.
     */
    private void expressionAccess(List<Expression> exprs, int accessingGroupIndex) {
        for (Expression expr: exprs) {
            expressionAccess(expr, accessingGroupIndex);
        }
    }

    /**
     * Register access to the variables and locations in the expression.
     *
     * @param expr Expression to analyze.
     * @param accessingGroupIndex Index of an element that accesses the variables or locations.
     * @note The accessing group may or may not own the variables or locations.
     */
    private void expressionAccess(Expression expr, int accessingGroupIndex) {
        for (PositionObject element: exprCollector.collectDecls(expr)) {
            registerAccessedRelation(element, accessingGroupIndex);
        }
    }

    /**
     * Process the declarations of a complex component and register relevant facts.
     *
     * @param decls Declarations to process.
     * @param groupIndex Either {@link #INVALID_INDEX} or the unique index number of the containing component.
     */
    private void collectDeclarations(List<Declaration> decls, int groupIndex) {
        for (Declaration decl: decls) {
            // Algebraic variables are searched.
            // Constants are irrelevant.
            // Continuous variables are not allowed.
            // Enumeration declarations are irrelevant.
            // Events are processed elsewhere.
            // Functions are irrelevant.
            // Type declarations are irrelevant.

            if (decl instanceof InputVariable) {
                registerPlantElement(decl);

                // Input variables are not owned.
                int declIndex = getIndex(decl);
                registerAccessedRelation(decl, declIndex);
            } else if (decl instanceof DiscVariable) {
                // Discrete variables can only occur in an owning automaton.
                registerOwnedRelation(decl, groupIndex);

                // A discrete variable must be owned by a requirement to be considered for merging requirements. Note
                // that keeping this relation will also enable merging two requirements that both only access the
                // variable but that is fine as at some point the requirement with ownership of the variable will be
                // merged into the group as well.
                if (!isRequirementElement(groupIndex)) {
                    int declIndex = getIndex(decl);
                    irrelevantRequirementAccessRelations.set(declIndex);
                }
            }
        }
    }

    /**
     * Process the given invariants as requirement elements, and set their local access relations.
     *
     * @param invariants Invariants to process.
     */
    private void collectInvariants(List<Invariant> invariants) {
        // Pre-checking already eliminated state invariants.
        for (Invariant inv: invariants) {
            Assert.areEqual(SupKind.REQUIREMENT, inv.getSupKind());
            int index = registerRequirementElement(inv);

            Event evt = ((EventExpression)inv.getEvent()).getEvent();
            registerAccessedRelation(evt, index);
            expressionAccess(inv.getPredicate(), index);
        }
    }

    /**
     * Register an element as a plant element.
     *
     * @param element Element to register.
     * @return The unique index of the element.
     */
    private int registerPlantElement(PositionObject element) {
        Assert.check(element instanceof Automaton || element instanceof InputVariable);
        int elementIndex = getIndex(element);
        plantElementIndices.set(elementIndex);
        constructEmptyGroupRelations(elementIndex);
        return elementIndex;
    }

    /**
     * Is the element at the given index a plant element?
     *
     * @param index Index to check.
     * @return Whether the element at the index is considered to be a plant element.
     */
    public boolean isPlantElement(int index) {
        return plantElementIndices.get(index);
    }

    /**
     * Register an element as a requirement element.
     *
     * @param element Element to register.
     * @return The unique index of the element.
     */
    private int registerRequirementElement(PositionObject element) {
        Assert.check(element instanceof Automaton || element instanceof Invariant);
        int elementIndex = getIndex(element);
        requirementElementIndices.set(elementIndex);
        constructEmptyGroupRelations(elementIndex);
        return elementIndex;
    }

    /**
     * Is the element at the given index a requirement element?
     *
     * @param index Index to check.
     * @return Whether the element at the index is considered to be a requirement element.
     */
    public boolean isRequirementElement(int index) {
        return requirementElementIndices.get(index);
    }

    /**
     * Construct empty element relations for the given new (plant or requirement) element.
     *
     * @param groupIndex Unique index of the group element.
     */
    private void constructEmptyGroupRelations(int groupIndex) {
        OwnedAndAccessedElements groupRelations = new OwnedAndAccessedElements(groupIndex);
        OwnedAndAccessedElements prevGroupRelations = relationsByGroup.put(groupIndex, groupRelations);
        Assert.check(prevGroupRelations == null); // Should be a new entry.
    }

    /**
     * Register ownership of the given element by its group.
     *
     * @param element Element to register as owned.
     * @param groupIndex The unique index of the group element.
     */
    private void registerOwnedRelation(PositionObject element, int groupIndex) {
        Assert.check(groupIndex != INVALID_INDEX);
        OwnedAndAccessedElements groupRelations = relationsByGroup.get(groupIndex);
        groupRelations.setOwnedRelation(getIndex(element));
    }

    /**
     * Register access of the given element by a group.
     *
     * @param element Element to register as accessed.
     * @param groupIndex The unique index of a group accessing the element.
     * @note The accessing group may or may not own the element.
     */
    private void registerAccessedRelation(PositionObject element, int groupIndex) {
        Assert.check(groupIndex != INVALID_INDEX);
        OwnedAndAccessedElements groupRelations = relationsByGroup.get(groupIndex);
        int elementIndex = getIndex(element);
        groupRelations.setAccessedRelation(elementIndex);

        // Mark events accessed by plants as ignored for requirement grouping.
        if (isPlantElement(groupIndex) && (element instanceof Event)) {
            irrelevantRequirementAccessRelations.set(elementIndex);
        }
    }

    /**
     * Retrieve the unique element index number for the given element.
     *
     * <p>
     * If it is not found, a new element is registered.
     * </p>
     *
     * @param element Element to find or add in the registered elements.
     * @return Unique index number of the given element.
     */
    public int getIndex(PositionObject element) {
        // Try finding a previous registration.
        Integer storedIndex = elementsToIndex.get(element);
        if (storedIndex != null) {
            return storedIndex;
        }

        // Construct a new registration.
        int newIndex = elementsToIndex.size();
        elementsToIndex.put(element, newIndex); // Store the element and its index.
        elementsByIndex.add(element); // Store the new element at its index;

        // Register input variables as elements that should be ignored in shared access between requirements.
        if (element instanceof InputVariable) {
            irrelevantRequirementAccessRelations.set(newIndex);
        }

        return newIndex;
    }

    /**
     * Retrieve a collected element by its index.
     *
     * @param index Index of the element to retrieve.
     * @return The retrieved element.
     */
    public PositionObject getElement(int index) {
        return elementsByIndex.get(index);
    }

    /**
     * Retrieve the relations of a specific group element.
     *
     * @param groupIndex Index of the group element.
     * @return All relations of the group.
     */
    public OwnedAndAccessedElements getGroupRelations(int groupIndex) {
        OwnedAndAccessedElements oar = relationsByGroup.get(groupIndex);
        Assert.notNull(oar);
        return oar;
    }

    /**
     * Construct the final plant groups from the groups with one plant.
     *
     * @return The created plant groups of the specification.
     */
    public List<OwnedAndAccessedElements> computePlantGroups() {
        List<OwnedAndAccessedElements> plantRelations = plantElementIndices.stream()
                .mapToObj(index -> relationsByGroup.get(index)).collect(Collectors.toList());
        return DisjunctGroupsBuilder.createPlantGroups(plantRelations);
    }

    /**
     * Construct the final requirement groups from the groups with one requirement.
     *
     * @return The created requirement groups of the specification.
     */
    public List<OwnedAndAccessedElements> computeRequirementGroups() {
        List<OwnedAndAccessedElements> requirementRelations = requirementElementIndices.stream()
                .mapToObj(index -> relationsByGroup.get(index)).collect(Collectors.toList());
        return DisjunctGroupsBuilder.createRequirementGroups(requirementRelations,
                irrelevantRequirementAccessRelations);
    }
}
