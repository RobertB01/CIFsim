//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.runtime.ode;

import org.apache.commons.math3.ode.events.EventHandler;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;

/**
 * CIF ODE solver state event.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class OdeStateEvent<S extends RuntimeState> implements EventHandler {
    /** The ODE solver to use. */
    private final OdeSolver<S> solver;

    /**
     * Constructor for the {@link OdeStateEvent} class.
     *
     * @param solver The ODE solver to use.
     */
    public OdeStateEvent(OdeSolver<S> solver) {
        this.solver = solver;
    }

    /**
     * Returns an end-user readable textual representation of the guard predicate.
     *
     * @return An end-user readable textual representation of the guard predicate.
     */
    public abstract String getPredText();

    /**
     * Evaluates the guard predicate that represents this state event.
     *
     * @param state The state to use to evaluate the guard predicate.
     * @return {@code true} if the guard predicate evaluates to {@code true}, {@code false} otherwise.
     */
    protected abstract boolean eval(S state);

    @Override
    public final void init(double t0, double[] y0, double t) {
        // Since we only have one instance of the solver, and we don't have
        // any other state in this class, there is no need for initialization.
        // Reuse of this event handler is thus fully supported.
    }

    @Override
    public final double g(double t, double[] y) {
        // Check for application termination.
        solver.ctxt.checkTermination();

        // Get the full state, updated for the current time value, and the
        // current values of the continuous variables.
        solver.checkValues(t, y);
        S state = solver.updateState(t, y);

        if (solver.debug) {
            String valuesTxt = solver.valuesToStr(t, y, false);
            solver.dbg.printfln("ODE solver: evaluating guard \"%s\": %s", getPredText(), valuesTxt);
        }

        // Evaluate the predicate. We return -1.0 for false, and +1.0 for
        // true. This makes sure that we get a zero crossing (root) when
        // the guard changes value.
        boolean b = eval(state);
        double rslt = b ? 1.0 : -1.0;

        if (solver.debug) {
            solver.dbg.printfln("ODE solver: evaluated guard \"%s\": %s.", getPredText(), b);
        }

        // Return the predicate evaluation result.
        return rslt;
    }

    @Override
    public final Action eventOccurred(double t, double[] y, boolean increasing) {
        if (solver.debug) {
            String valuesTxt = solver.valuesToStr(t, y, false);
            solver.dbg.printfln("ODE solver: state event detected for guard \"%s\": %s", getPredText(), valuesTxt);
        }

        return Action.STOP;
    }

    @Override
    public final void resetState(double t, double[] y) {
        throw new UnsupportedOperationException();
    }
}
