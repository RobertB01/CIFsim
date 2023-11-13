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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.common.CifValueUtils.isTriviallyFalse;
import static org.eclipse.escet.cif.common.CifValueUtils.isTriviallyTrue;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumDecl;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteral;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteralExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newVariableValue;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.types.ComponentType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that eliminates location reference expressions.
 *
 * <p>
 * For each automaton for which a location is referenced (or for all automata, depending on the transformation
 * settings), a location pointer variable is introduced, with as value the current location. The values are part of a
 * new enumeration that has a value for each location of the automaton. For automata with exactly one location, no
 * location pointer variable is introduced.
 * </p>
 *
 * <p>
 * To initialize the new location pointer variable, initialization predicates are added to all locations that could
 * potentially be initial locations. For automata with exactly one initial state, the initial value of the location
 * pointer variable is set, instead of using initialization predicates.
 * </p>
 *
 * <p>
 * All edges in the automaton that change the current location of that automaton get an additional assignment to update
 * the location pointer variable.
 * </p>
 *
 * <p>
 * All location reference expressions are changed to equality binary expressions for variable reference expressions that
 * reference the new location pointer variable. If the location of an automation with exactly one location is
 * referenced, the reference is replaced by 'true'.
 * </p>
 *
 * <p>
 * 'switch' expressions with an automaton (self) reference as control value, are updated to use location pointers and
 * their corresponding enumeration values. For automata with exactly one location, the switch case is dropped and the
 * first case value is used.
 * </p>
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * This transformation may introduce new enumerations. To eliminate them, apply the {@link EnumsToInts} or
 * {@code EnumsToConsts} transformation after this transformation.
 * </p>
 */
public class ElimLocRefExprs extends CifWalker implements CifToCifTransformation, LocationPointerManager {
    /**
     * The function to use to produce a candidate name for a new location pointer variable, given the automaton for
     * which it is created. The candidate name is subject to renaming in case of naming conflicts with other
     * declarations.
     */
    private final Function<Automaton, String> varNamingFunction;

    /**
     * The function to use to produce a candidate name for a new location pointer enumeration declaration, given the
     * automaton for which it is created. The candidate name is subject to renaming in case of naming conflicts with
     * other declarations.
     */
    private final Function<Automaton, String> enumNamingFunction;

    /**
     * The function to use to produce a candidate name for a new location pointer enumeration literal, given the
     * location for which it is created. The candidate name is subject to renaming in case of naming conflicts with
     * other declarations. Should only be invoked for locations with a name.
     */
    private final Function<Location, String> litNamingFunction;

    /**
     * Whether to perform an optimized transformation (only add location pointer variables to automata for which a
     * location is referenced in an expression).
     */
    private final boolean optimized;

    /** Whether to consider the names of the locations of the automaton for renaming to ensure unique names. */
    private final boolean considerLocsForRename;

    /**
     * Whether to add initialization predicates for the initialization of the introduced location pointer variables.
     * Note that if the automaton has exactly one initial location, the location pointer variable is initialized in its
     * declaration, regardless of the value of this variable.
     */
    private final boolean addInitPreds;

    /**
     * Mapping from location pointer variables to the absolute names (without keyword escaping) of the original automata
     * for which they were created. May be {@code null} to not construct this mapping. This mapping is for use after the
     * transformation.
     */
    private final Map<DiscVariable, String> lpVarToAbsAutNameMap;

    /**
     * Whether to allow optimization of initialization of location pointers, by analyzing declarations (used for
     * instance in initialization predicates) to see whether they have constant values.
     *
     * @see CifValueUtils#hasSingleValue(Expression, boolean, boolean)
     */
    private final boolean optInits;

    /**
     * Whether to add equality binary expressions that reference the new location pointer variable to the guards of
     * edges.
     */
    private final boolean addEdgeGuards;

    /**
     * The phase of the transformation. The first phase (value {@code 1}) is to find the automata for which locations
     * are referenced in expressions. The second phase (value {@code 2}) is to perform the actual transformation. Value
     * is {@code 0} until transformation is started.
     */
    private int phase;

    /**
     * Mapping from automata to their newly created location pointer variables.
     *
     * <p>
     * For {@link #optimized} mode, the keys are added during {@link #phase} 1, with {@code null} values, and the actual
     * values are added during {@link #phase} 2. For non-optimized mode, the keys and values are all added during
     * {@link #phase} 2.
     * </p>
     */
    private Map<Automaton, DiscVariable> autToVarMap = map();

    /**
     * Mapping from automata to their newly created enumerations.
     *
     * <p>
     * The mapping is filled during {@link #phase} 2.
     * </p>
     */
    private Map<Automaton, EnumDecl> autToEnumMap = map();

    /**
     * Constructor for the {@link ElimLocRefExprs} class:
     * <ul>
     * <li>Uses {@code "LP"} as candidate name for location pointer variables.</li>
     * <li>Uses {@code "LPE"} as candidate name for location pointer enumeration declarations.</li>
     * <li>Uses the location name prefixed with {@code "LOC_"} as candidate name for location pointer enumeration
     * literals.</li>
     * <li>Considers the names of the locations of the automaton for renaming to ensure unique names.</li>
     * <li>Adds initialization predicates for the initialization of the location pointer variables.</li>
     * <li>Performs an optimized transformation (only adds location pointer variables to automata for which a location
     * is referenced in an expression).</li>
     * <li>Does not construct a mapping location pointer variables to the absolute names (without keyword escaping) of
     * the original automata for which they were created, for use after the transformation.</li>
     * <li>Allows optimization of initialization of location pointers, by analyzing declarations (used for instance in
     * initialization predicates) to see whether they have constant values.</li>
     * <li>Does not add equality binary expressions that reference the new location pointer to the guards of edges.</li>
     * </ul>
     */
    public ElimLocRefExprs() {
        this(a -> "LP", a -> "LPE", l -> "LOC_" + l.getName(), true, true, true, null, true, false);
    }

    /**
     * Constructor for the {@link ElimLocRefExprs} class.
     *
     * @param varNamingFunction The function to use to produce a candidate name for a new location pointer variable,
     *     given the automaton for which it is created. The candidate name is subject to renaming in case of naming
     *     conflicts with other declarations.
     * @param enumNamingFunction The function to use to produce a candidate name for a new location pointer enumeration
     *     declaration, given the automaton for which it is created. The candidate name is subject to renaming in case
     *     of naming conflicts with other declarations.
     * @param litNamingFunction The function to use to produce a candidate name for a new location pointer enumeration
     *     literal, given the location for which it is created. The candidate name is subject to renaming in case of
     *     naming conflicts with other declarations. Is only invoked for locations with a name.
     * @param considerLocsForRename Whether to consider the names of the locations of the automaton for renaming to
     *     ensure unique names.
     * @param addInitPreds Whether to add initialization predicates for the initialization the introduced location
     *     pointer variables. Note that if the automaton has exactly one initial location, the location pointer variable
     *     is initialized in its declaration, regardless of the value of this parameter.
     * @param optimized Whether to perform an optimized transformation (only add location pointer variables to automata
     *     for which a location is referenced in an expression).
     * @param lpVarToAbsAutNameMap Mapping from location pointer variables to the absolute names (without keyword
     *     escaping) of the original automata for which they were created. May be {@code null} to not construct this
     *     mapping. If provided, the mapping is modified in-place. This mapping is for use after the transformation.
     * @param optInits Whether to allow optimization of initialization of location pointers, by analyzing declarations
     *     (used for instance in initialization predicates) to see whether they have constant values.
     * @param addEdgeGuards Whether to add equality binary expressions that reference the new location pointer variable
     *     to the guards of edges. Note that only if {@code optimized} is disabled, these expressions are created for
     *     all edges in the specification (of automata with at least two locations).
     */
    public ElimLocRefExprs(Function<Automaton, String> varNamingFunction,
            Function<Automaton, String> enumNamingFunction, Function<Location, String> litNamingFunction,
            boolean considerLocsForRename, boolean addInitPreds, boolean optimized,
            Map<DiscVariable, String> lpVarToAbsAutNameMap, boolean optInits, boolean addEdgeGuards)
    {
        this.varNamingFunction = varNamingFunction;
        this.enumNamingFunction = enumNamingFunction;
        this.litNamingFunction = litNamingFunction;
        this.considerLocsForRename = considerLocsForRename;
        this.addInitPreds = addInitPreds;
        this.optimized = optimized;
        this.lpVarToAbsAutNameMap = lpVarToAbsAutNameMap;
        this.optInits = optInits;
        this.addEdgeGuards = addEdgeGuards;
    }

    /**
     * Returns the unique location pointer variable for the given automaton. If the automation has exactly one location,
     * {@code null} is returned.
     *
     * <p>
     * The {@link #autToVarMap} is used. If no entry is present in that mapping, a new variable is created and added to
     * the mapping, before returning it.
     * </p>
     *
     * <p>
     * Newly created variables are given a proposed name, but they may be renamed later on, if necessary to avoid naming
     * conflicts.
     * </p>
     *
     * @param aut The automaton for which to return the unique location pointer variable.
     * @return The unique location pointer variable, or {@code null}.
     */
    private DiscVariable getLocPointerVar(Automaton aut) {
        if (aut.getLocations().size() == 1) {
            // Exactly one location, don't create a location pointer.
            return null;
        }

        DiscVariable var = autToVarMap.get(aut);
        if (var == null) {
            // Create type of location pointer variable.
            EnumDecl enumDecl = getLocPointerEnum(aut);
            EnumType enumType = newEnumType();
            enumType.setEnum(enumDecl);

            // Get name for the new location pointer variable.
            String varName = varNamingFunction.apply(aut);

            // Create location pointer variable.
            var = newDiscVariable();
            var.setValue(newVariableValue());
            var.setName(varName);
            var.setType(enumType);
            autToVarMap.put(aut, var);

            // Store absolute automaton name (without keyword escaping)
            // for use after the transformation, if requested.
            if (lpVarToAbsAutNameMap != null) {
                lpVarToAbsAutNameMap.put(var, CifTextUtils.getAbsName(aut, false));
            }
        }
        return var;
    }

    /**
     * Returns the unique enumeration from which to use the literals as values for the location pointer variable for the
     * given automaton.
     *
     * <p>
     * The {@link #autToEnumMap} is used. If no entry is present in that mapping, a new enumeration is created and added
     * to the mapping, before returning it. If the automaton has exactly one location, {@code null} is returned.
     * </p>
     *
     * <p>
     * Newly created enumerations and enumeration literals are given a proposed name, but they may be renamed later on,
     * if necessary to avoid naming conflicts.
     * </p>
     *
     * @param aut The automaton for which to return the unique enumeration.
     * @return The unique enumeration, or {@code null}.
     */
    private EnumDecl getLocPointerEnum(Automaton aut) {
        if (aut.getLocations().size() == 1) {
            // Exactly one location, don't create a location-pointer enumeration.
            return null;
        }

        EnumDecl enumDecl = autToEnumMap.get(aut);
        if (enumDecl == null) {
            // Get name for the new location pointer enumeration.
            String enumName = enumNamingFunction.apply(aut);

            // Create enum.
            enumDecl = newEnumDecl();
            enumDecl.setName(enumName);

            // Create literals.
            List<EnumLiteral> literals = enumDecl.getLiterals();
            for (Location loc: aut.getLocations()) {
                String name = loc.getName();
                Assert.notNull(name);

                String litName = litNamingFunction.apply(loc);

                EnumLiteral literal = newEnumLiteral();
                literal.setName(litName);
                literals.add(literal);
            }
            autToEnumMap.put(aut, enumDecl);
        }
        return enumDecl;
    }

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating the use of locations in expressions from a CIF specification with component "
                    + "definitions is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Phase 1: Find out for which automata locations are referenced. If
        // not optimized, skip this.
        phase = 1;
        if (optimized) {
            walkSpecification(spec);
        }

        // Phase 2: Actual transformation.
        phase = 2;
        walkSpecification(spec);
    }

    @Override
    protected void preprocessLocationExpression(LocationExpression locRef) {
        if (phase == 1) {
            // Phase 1: Add automaton.
            Location loc = locRef.getLocation();
            Automaton aut = (Automaton)loc.eContainer();

            if (aut.getLocations().size() != 1) {
                // Only add automata with at least two locations, as for automata with exactly one location, no location
                // pointer is created.
                autToVarMap.put(aut, null);
            }
        } else {
            // Phase 2: Replace reference by equality over location pointer or 'true' if exactly one location.
            Location loc = locRef.getLocation();

            // Replace.
            Expression pred = createLocRef(loc);
            EMFHelper.updateParentContainment(locRef, pred);
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton aut) {
        if (phase == 2) {
            // Skip automata for which no locations are referenced, if we use optimized mode.
            if (optimized && !autToVarMap.containsKey(aut)) {
                return;
            }

            // Skip automata with exactly one location.
            if (aut.getLocations().size() == 1) {
                return;
            }

            // Get location pointer variable and enumeration.
            DiscVariable var = getLocPointerVar(aut);
            EnumDecl enumDecl = getLocPointerEnum(aut);

            // Rename location pointer variable, enumeration, and literals,
            // if needed.
            renameIfNeeded(aut, var, enumDecl);

            // Add location pointer variable and enumeration. We do this
            // after renaming, as otherwise the names already exist.
            aut.getDeclarations().add(var);
            aut.getDeclarations().add(enumDecl);

            // Ensure proper initialization of the location pointer variable.
            addInits(aut, var, enumDecl);

            // Add guards to edges, if requested.
            if (addEdgeGuards) {
                addEdgeGuards(aut, var, enumDecl);
            }

            // Add updates to edges, if needed.
            addUpdates(aut, var, enumDecl);
        }
    }

    /**
     * Rename location pointer variable, enumeration, and literals, if needed.
     *
     * @param aut The automaton that declares the variable and enumeration.
     * @param var The variable.
     * @param enumDecl The enumeration.
     */
    private void renameIfNeeded(Automaton aut, DiscVariable var, EnumDecl enumDecl) {
        // Get used names and names to avoid, for possible renamings.
        Set<String> usedNames = CifScopeUtils.getSymbolNamesForScope(aut, null);
        if (!considerLocsForRename) {
            for (Location loc: aut.getLocations()) {
                if (loc.getName() == null) {
                    continue;
                }
                usedNames.remove(loc.getName());
            }
        }

        Set<String> avoidNames = setc(aut.getLocations().size() + 2);
        avoidNames.add(var.getName());
        avoidNames.add(enumDecl.getName());
        for (EnumLiteral literal: enumDecl.getLiterals()) {
            avoidNames.add(literal.getName());
        }

        // Rename location pointer variable, if needed.
        if (usedNames.contains(var.getName())) {
            String oldName = var.getName();
            String name = CifScopeUtils.getUniqueName(oldName, usedNames, avoidNames);
            var.setName(name);
            warn("Location pointer variable \"%s\" for automaton \"%s\" is renamed to \"%s\".", oldName,
                    CifTextUtils.getAbsName(aut), name);
        }
        usedNames.add(var.getName());

        // Rename enumeration, if needed.
        if (usedNames.contains(enumDecl.getName())) {
            String oldName = enumDecl.getName();
            String name = CifScopeUtils.getUniqueName(oldName, usedNames, avoidNames);
            enumDecl.setName(name);
            warn("Enumeration \"%s\", introduced as the type of the location pointer variable for automaton \"%s\", "
                    + "is renamed to \"%s\".", oldName, CifTextUtils.getAbsName(aut), name);
        }
        usedNames.add(enumDecl.getName());

        // Rename enumeration literals, if needed.
        for (EnumLiteral lit: enumDecl.getLiterals()) {
            if (usedNames.contains(lit.getName())) {
                String oldName = lit.getName();
                String name = CifScopeUtils.getUniqueName(oldName, usedNames, avoidNames);
                lit.setName(name);
                warn("Enumeration literal \"%s\", introduced as a value of the location pointer variable for automaton "
                        + "\"%s\", is renamed to \"%s\".", oldName, CifTextUtils.getAbsName(aut), name);
            }
            usedNames.add(lit.getName());
        }
    }

    /**
     * Adds initialization predicates to the possible initial locations of the given automaton, to initialize the
     * location pointer variable of that automaton. For automata with exactly one initial state, the initial value of
     * the location pointer variable is set, instead of using initialization predicates.
     *
     * @param aut The automaton that declares the variable and enumeration.
     * @param var The location pointer variable.
     * @param enumDecl The enumeration.
     */
    private void addInits(Automaton aut, DiscVariable var, EnumDecl enumDecl) {
        List<Location> locs = aut.getLocations();

        // If exactly one location has trivially true initialization, and all
        // the other have trivially false ones, set the initial value of the
        // location pointer variable.
        int initIdx = -1;
        for (int idx = 0; idx < locs.size(); idx++) {
            // Get initials.
            Location loc = locs.get(idx);
            List<Expression> initials = loc.getInitials();

            // Check for trivially false, and skip.
            if (initials.isEmpty() || isTriviallyFalse(initials, true, optInits)) {
                continue;
            }

            // Check for trivially true.
            if (!initials.isEmpty() && isTriviallyTrue(initials, true, optInits)) {
                if (initIdx == -1) {
                    // First trivially true initial location.
                    initIdx = idx;
                    continue;
                } else {
                    // Multiple trivially true initial locations.
                    initIdx = -1;
                    break;
                }
            }

            // Not trivially true and not trivially false.
            initIdx = -1;
            break;
        }

        if (initIdx != -1) {
            // Set initial value to the proper enumeration literal.
            EnumLiteral literal = enumDecl.getLiterals().get(initIdx);

            EnumLiteralExpression litRef = newEnumLiteralExpression();
            litRef.setLiteral(literal);
            litRef.setType(deepclone(var.getType()));

            VariableValue value = newVariableValue();
            value.getValues().add(litRef);

            var.setValue(value);
            return;
        }

        // Not the simple case. Add initialization predicates to the locations.
        if (!addInitPreds) {
            return;
        }
        for (int idx = 0; idx < locs.size(); idx++) {
            // Get location.
            Location loc = locs.get(idx);

            // Skip locations that are definitely not initial ones.
            List<Expression> initials = loc.getInitials();
            if (initials.isEmpty() || isTriviallyFalse(initials, true, optInits)) {
                continue;
            }

            // Create initialization predicate for the location pointer
            // variable.
            BinaryExpression pred = createEquality(var, enumDecl, idx);

            // Add initialization predicate.
            loc.getInitials().add(pred);
        }
    }

    /**
     * Adds equality binary expression that references the location pointer, as guards to the edges of the given
     * automaton.
     *
     * @param aut The automaton.
     * @param var The location pointer variable.
     * @param enumDecl The enumeration.
     */
    private void addEdgeGuards(Automaton aut, DiscVariable var, EnumDecl enumDecl) {
        List<Location> locs = aut.getLocations();
        for (int idx = 0; idx < locs.size(); idx++) {
            // Get location.
            Location loc = locs.get(idx);

            // Process all edges.
            for (Edge edge: loc.getEdges()) {
                // Add guard.
                Expression guard = createEquality(var, enumDecl, idx);
                edge.getGuards().add(0, guard);
            }
        }
    }

    /**
     * Adds updates to the edges of the given automaton, to update the location pointer variable of that automaton, if
     * needed.
     *
     * @param aut The automaton that declares the variable and enumeration.
     * @param var The location pointer variable.
     * @param enumDecl The enumeration.
     */
    private void addUpdates(Automaton aut, DiscVariable var, EnumDecl enumDecl) {
        List<Location> locs = aut.getLocations();
        for (int idx = 0; idx < locs.size(); idx++) {
            // Get location.
            Location loc = locs.get(idx);

            // Process all edges.
            for (Edge edge: loc.getEdges()) {
                // Skip self-loops.
                if (edge.getTarget() == null) {
                    continue;
                }
                if (edge.getTarget() == loc) {
                    continue;
                }

                // Add assignment.
                Update asgn = createLocUpdate(edge.getTarget());
                edge.getUpdates().add(asgn);
            }
        }
    }

    /**
     * Creates an equality binary expression for the given location pointer variable and enumeration literal.
     *
     * @param var The location pointer variable.
     * @param enumDecl The enumeration.
     * @param idx The 0-based index of the enumeration literal into the enumeration.
     * @return The newly created '{@code var = lit}' expression.
     */
    private BinaryExpression createEquality(DiscVariable var, EnumDecl enumDecl, int idx) {
        DiscVariableExpression varRef = newDiscVariableExpression();
        varRef.setVariable(var);
        varRef.setType(deepclone(var.getType()));

        EnumLiteral literal = enumDecl.getLiterals().get(idx);

        EnumLiteralExpression litRef = newEnumLiteralExpression();
        litRef.setLiteral(literal);
        litRef.setType(deepclone(var.getType()));

        BinaryExpression pred = newBinaryExpression();
        pred.setOperator(BinaryOperator.EQUAL);
        pred.setLeft(varRef);
        pred.setRight(litRef);
        pred.setType(newBoolType());

        return pred;
    }

    /**
     * Creates an expression for the given location. If there are multiple locations, this is a '{@code var = lit}'
     * binary expression. If there is exactly one location, this is a '{@code true}' boolean expression.
     *
     * <p>
     * This method is exposed in the public API to allow using it also after the transformation has finished, to create
     * additional references to locations, using proper expressions.
     * </p>
     *
     * @return The newly created '{@code var = lit}' expression or '{@code true}' expression.
     */
    @Override
    public Expression createLocRef(Location loc) {
        // For automata with exactly one location, the automaton is always in that location, create true expression.
        Automaton aut = (Automaton)loc.eContainer();
        if (aut.getLocations().size() == 1) {
            return CifValueUtils.makeTrue();
        }

        // For automata with multiple locations, get the location pointer variable and create an equality expression.
        DiscVariable var = getLocPointerVar(aut);
        EnumDecl enumDecl = getLocPointerEnum(aut);

        // Get location index.
        int idx = aut.getLocations().indexOf(loc);

        // Create and return expression.
        return createEquality(var, enumDecl, idx);
    }

    @Override
    public Update createLocUpdate(Location loc) {
        // For automata with exactly one location, no location pointer updates have to be created, since there is no
        // location pointer.
        Automaton aut = (Automaton)loc.eContainer();
        if (aut.getLocations().size() == 1) {
            return null;
        }

        // For automata with multiple locations, get the location pointer variable and create update.
        DiscVariable var = getLocPointerVar(aut);
        EnumDecl enumDecl = getLocPointerEnum(aut);

        // Create variable reference.
        DiscVariableExpression varRef = newDiscVariableExpression();
        varRef.setVariable(var);
        varRef.setType(deepclone(var.getType()));

        // Create enumeration literal reference for the location.
        int targetIdx = aut.getLocations().indexOf(loc);
        EnumLiteralExpression litRef = newEnumLiteralExpression();
        litRef.setLiteral(enumDecl.getLiterals().get(targetIdx));
        litRef.setType(deepclone(var.getType()));

        // Create and return assignment.
        Assignment asgn = newAssignment();
        asgn.setAddressable(varRef);
        asgn.setValue(litRef);
        return asgn;
    }

    @Override
    protected void postprocessSwitchExpression(SwitchExpression switchExpr) {
        // This method is done in post processing rather than pre processing,
        // to ensure the control value expression is not modified until after
        // the cases have been processed.

        // Don't do anything special for phase 1.
        if (phase == 1) {
            return;
        }

        // Phase 2. Detect normal/special case. Special case only if 'switch'
        // expression control value is an automaton (self) reference.
        Expression value = switchExpr.getValue();
        boolean isAutRef = CifTypeUtils.isAutRefExpr(value);
        if (!isAutRef) {
            return;
        }

        // Special case detected. Change automaton reference to a location
        // pointer. First get the location pointer variable for the automaton.
        ComponentType compType = (ComponentType)value.getType();
        Component comp = compType.getComponent();
        Automaton aut = CifScopeUtils.getAutomaton(comp);

        // Special case, automaton has exactly one location. Drop the entire switch expression, and replace it by the
        // value of the first switch case. First value is either the only location name of the automaton, or it is the
        // 'else' switch case.
        if (aut.getLocations().size() == 1) {
            SwitchCase firstCase = first(switchExpr.getCases());
            EMFHelper.updateParentContainment(switchExpr, firstCase.getValue());
            return;
        }

        // Replace automaton (self) reference by location pointer reference.
        DiscVariable var = getLocPointerVar(aut);
        DiscVariableExpression varRef = newDiscVariableExpression();
        varRef.setVariable(var);
        varRef.setType(deepclone(var.getType()));
        EMFHelper.updateParentContainment(value, varRef);
    }

    @Override
    protected void preprocessSwitchCase(SwitchCase cse) {
        // Don't do anything special for phase 1.
        if (phase == 1) {
            return;
        }

        // Don't do anything special for 'else' cases.
        Expression key = cse.getKey();
        if (key == null) {
            return;
        }

        // Phase 2. Detect normal/special case. Special case only if 'switch'
        // expression control value is an automaton (self) reference.
        SwitchExpression switchExpr = (SwitchExpression)cse.eContainer();
        Expression value = switchExpr.getValue();
        boolean isAutRef = CifTypeUtils.isAutRefExpr(value);
        if (!isAutRef) {
            return;
        }

        // Special case detected. For 'switch' expressions with a control
        // value that refers to an automaton, the 'else' case is optional. For
        // 'switch' expressions with 'normal' control values, the 'else' case
        // is mandatory. Therefore, if this is the last case of the 'switch',
        // change the key to 'else'.
        if (last(switchExpr.getCases()) == cse) {
            cse.setKey(null);
            return;
        }

        // Change location reference to a location pointer value reference.
        // First, get the location and the automaton.
        Assert.check(key instanceof LocationExpression);
        Location loc = ((LocationExpression)key).getLocation();
        Automaton aut = (Automaton)loc.eContainer();

        // Special case, reference to an automaton with one location and an 'else' case. The switch expression will be
        // dropped during postprocessing.
        if (aut.getLocations().size() == 1) {
            return;
        }

        // Get enum literal.
        int idx = aut.getLocations().indexOf(loc);
        EnumDecl enumDecl = getLocPointerEnum(aut);
        EnumLiteral literal = enumDecl.getLiterals().get(idx);

        // Replace location reference by enumeration literal reference.
        DiscVariable var = getLocPointerVar(aut);
        EnumLiteralExpression litRef = newEnumLiteralExpression();
        litRef.setLiteral(literal);
        litRef.setType(deepclone(var.getType()));
        EMFHelper.updateParentContainment(key, litRef);
    }
}
