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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InvalidModelException;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Class for computing and representing an initial state.
 *
 * <p>
 * Class may start with {@code null} values for locations and variables, to denote the value has not been derived yet.
 * </p>
 */
public class InitialState extends BaseState {
    /** The temporary specification to hold the {@link #funcs}. */
    private final Specification funcsSpec = newSpecification();

    /** Default function values for variables with function types. */
    private List<InternalFunction> funcs = list();

    /**
     * Mapping from automata to their initial locations. May be an incomplete mapping, to let this class determine the
     * initial location.
     */
    public Map<Automaton, Location> autLocs;

    /**
     * Mapping from discrete variables to their initial value expressions. May be an incomplete mapping, to let this
     * class determine the initial values.
     */
    public Map<DiscVariable, Expression> varVals;

    /**
     * Constructor of the {@link InitialState} class.
     *
     * @param explorer Managing object of the exploration.
     */
    public InitialState(Explorer explorer) {
        super(explorer, new Location[explorer.automata.length], new Object[explorer.variables.length]);
    }

    @Override
    public Object getVarValue(PositionObject var) {
        // If already initialized, return previously computed value.
        int index = explorer.indices.get(var);
        if (values[index] != null) {
            return values[index];
        }

        // Variable not initialized, derive its initial value first.
        if (var instanceof ContVariable) {
            ContVariable cv = (ContVariable)var;
            if (cv.getValue() != null) {
                try {
                    values[index] = eval(cv.getValue(), null);
                    return values[index];
                } catch (CifEvalException ex) {
                    String msg = fmt("Failed to compute initial value of variable \"%s\".",
                            CifTextUtils.getAbsName(cv));
                    throw new InvalidModelException(msg, ex);
                }
            }
            values[index] = 0.0;
            return values[index];
        }

        if (var instanceof DiscVariable) {
            DiscVariable dv = (DiscVariable)var;
            Expression value = varVals.get(dv);
            if (value != null) {
                // Explicit initialization expression provided.
                Assert.check(dv.getValue().getValues().size() != 1);
                try {
                    values[index] = eval(value, null);
                    return values[index];
                } catch (CifEvalException ex) {
                    String msg = fmt("Failed to compute initial value \"%s\" of variable \"%s\".",
                            CifTextUtils.exprToStr(value), CifTextUtils.getAbsName(dv));
                    throw new InvalidModelException(msg, ex);
                }
            } else if (dv.getValue() != null) {
                // Must have a single initial value that we can use.
                Assert.check(dv.getValue().getValues().size() == 1);
                try {
                    values[index] = eval(dv.getValue().getValues().get(0), null);
                    return values[index];
                } catch (CifEvalException ex) {
                    String msg = fmt("Failed to compute the initial value of variable \"%s\".",
                            CifTextUtils.getAbsName(dv));
                    throw new InvalidModelException(msg, ex);
                }
            } else {
                // Use default initial value for its data type.
                Expression e = CifValueUtils.getDefaultValue(dv.getType(), funcs);
                try {
                    values[index] = eval(e, null);
                    return values[index];
                } catch (CifEvalException ex) {
                    String msg = fmt("Failed to compute default initial value of variable \"%s\".",
                            CifTextUtils.getAbsName(var));
                    throw new InvalidModelException(msg, ex);
                }
            }
        }

        String msg = fmt("Unknown kind of variable %s requested.", var);
        throw new RuntimeException(msg);
    }

    @Override
    public void setVarValue(PositionObject var, Object value) {
        // Should never occur, as this state uses only initial values.
        throw new UnsupportedOperationException();
    }

    /**
     * Is the given location an initial location of its automaton.
     *
     * @param loc Location to investigate.
     * @return {@code true} if the location is the initial location, {@code false} otherwise.
     */
    private boolean isInitialLoc(Location loc) {
        List<Expression> initialExprs = explorer.initialLocs.get(loc);
        if (initialExprs == null || initialExprs.isEmpty()) {
            return false;
        }

        for (Expression expr: initialExprs) {
            boolean val;
            try {
                val = (Boolean)eval(expr, null);
            } catch (CifEvalException ex) {
                String msg = fmt("Failed to compute initialization predicate \"%s\" in %s.",
                        CifTextUtils.exprToStr(expr), CifTextUtils.getLocationText2(loc));
                throw new InvalidModelException(msg, ex);
            }

            if (!val) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Expression getAlgExpression(AlgVariable algVar) {
        CollectedAlgVariable cav = explorer.algVariables.get(algVar);
        if (cav.value != null) {
            return cav.value;
        }

        Assert.check(cav.autIndex >= 0);
        Location curLoc = getCurrentLocation(cav.autIndex);
        return cav.getExpression(curLoc);
    }

    @Override
    public boolean isInitial() {
        return true;
    }

    @Override
    public boolean isMarked() {
        // Check global marker predicates.
        for (Expression expr: explorer.markeds) {
            try {
                if (!(Boolean)eval(expr, null)) {
                    return false;
                }
            } catch (CifEvalException ex) {
                String msg = fmt("Failed to compute marker predicate \"%s\" for initial state.",
                        CifTextUtils.exprToStr(expr));
                throw new InvalidModelException(msg, ex);
            }
        }

        // Compute all marker predicates at the initial locations. If they all
        // hold, the state is marked.
        for (int autIndex = 0; autIndex < explorer.automata.length; autIndex++) {
            Location loc = getCurrentLocation(autIndex);
            if (loc.getMarkeds().isEmpty()) {
                return false;
            }
            for (Expression expr: loc.getMarkeds()) {
                try {
                    if (!(Boolean)eval(expr, null)) {
                        return false;
                    }
                } catch (CifEvalException ex) {
                    String msg = fmt("Failed to compute marker predicate \"%s\" in %s, for initial state.",
                            CifTextUtils.exprToStr(expr), CifTextUtils.getLocationText2(loc));
                    throw new InvalidModelException(msg, ex);
                }
            }
        }

        return true;
    }

    @Override
    public Location getCurrentLocation(int autIndex) {
        // If automaton is already initialized, return initial location.
        if (locations[autIndex] != null) {
            return locations[autIndex];
        }

        // No initial location found yet, find it.
        Automaton aut = explorer.automata[autIndex];
        Location initLoc = autLocs.get(aut);
        if (initLoc != null) {
            // A candidate initial location is given. Try it.
            if (isInitialLoc(initLoc)) {
                locations[autIndex] = initLoc;
                return initLoc;
            }
        } else {
            // Try each location of the automaton. There can be only one
            // potential initial location, as otherwise we would have been
            // given an explicit candidate.
            for (Location loc: aut.getLocations()) {
                if (isInitialLoc(loc)) {
                    locations[autIndex] = loc;
                    return loc;
                }
            }
        }

        // No initial location.
        NoInitialStateReason reason = new NoInitialLocReason(aut);
        throw new NoInitialStateException(reason);
    }

    /**
     * Compute all data of the initial state, and verify that all initialization predicates and invariants hold.
     *
     * @return The reason the initial state is invalid (and thus doesn't exist), or {@code null} if it is valid (it does
     *     exist).
     */
    public NoInitialStateReason computeCompletely() {
        // Evaluate all variables.
        for (PositionObject po: explorer.variables) {
            getVarValue(po);
        }

        // Ensure the default value functions are contained in a specification and have a name.
        for (InternalFunction func: funcs) {
            funcsSpec.getDeclarations().add(func);
            func.setName("*");
        }

        // Get initial locations of all the automata.
        for (int i = 0; i < explorer.automata.length; i++) {
            getCurrentLocation(i);
        }

        // Check global initialization predicates.
        List<Expression> globalInits = explorer.initialLocs.get(null);
        if (globalInits != null) {
            for (Expression expr: globalInits) {
                boolean val;
                try {
                    val = (boolean)eval(expr, null);
                } catch (CifEvalException ex) {
                    String msg = fmt("Failed to compute initialization predicate \"%s\" in initial state.",
                            CifTextUtils.exprToStr(expr));
                    throw new InvalidModelException(msg, ex);
                }
                if (!val) {
                    // Initialization predicate doesn't hold.
                    return new NoInitialInitPredReason(expr);
                }
            }
        }

        // Check state invariants.
        NoInitialStateReason reason = checkStateInvs(explorer.stateInvs.get(null));
        if (reason != null) {
            return reason;
        }
        for (Location loc: locations) {
            reason = checkStateInvs(explorer.stateInvs.get(loc));
            if (reason != null) {
                return reason;
            }
        }
        return null;
    }

    /**
     * Verify that the given state invariants hold.
     *
     * @param ci State invariants to check.
     * @return The reason the initial state is invalid (due an invariant that doesn't hold), or {@code null} if all the
     *     invariant hold.
     */
    private NoInitialInvReason checkStateInvs(CollectedInvariants ci) {
        if (ci == null) {
            return null;
        }

        NoInitialInvReason reason = null;
        reason = checkStateInvs(ci.getNoneInvariants());
        if (reason == null) {
            reason = checkStateInvs(ci.getPlantInvariants());
        }
        if (reason == null) {
            reason = checkStateInvs(ci.getRequirementInvariants());
        }
        if (reason == null) {
            reason = checkStateInvs(ci.getSupervisorInvariants());
        }
        return reason;
    }

    /**
     * Verify that the given state invariants hold. If not report the first failing invariant.
     *
     * @param invs State invariants to check.
     * @return The reason the initial state is invalid (due an invariant that doesn't hold), or {@code null} if all the
     *     invariant hold.
     */
    private NoInitialInvReason checkStateInvs(List<Invariant> invs) {
        for (Invariant inv: invs) {
            boolean val;
            try {
                val = (boolean)eval(inv.getPredicate(), null);
            } catch (CifEvalException ex) {
                String msg = fmt("Failed to compute invariant \"%s\" in initial state.",
                        CifTextUtils.invToStr(inv, false));
                throw new InvalidModelException(msg, ex);
            }

            if (!val) {
                return new NoInitialInvReason(inv);
            }
        }
        return null;
    }
}
