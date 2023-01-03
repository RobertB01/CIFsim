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

package org.eclipse.escet.cif.simulator.runtime.ode;

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.sampling.FixedStepHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepNormalizer;
import org.apache.commons.math3.ode.sampling.StepNormalizerBounds;
import org.apache.commons.math3.ode.sampling.StepNormalizerMode;
import org.eclipse.escet.cif.simulator.CifSimulatorContext;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;

/**
 * CIF ODE solver.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class OdeSolver<S extends RuntimeState>
        implements FirstOrderDifferentialEquations, StepHandler, FixedStepHandler
{
    /** Simulator context. */
    public CifSimulatorContext ctxt;

    /** Whether to debug the ODE solver. */
    public boolean debug;

    /** The stream to write all debug output to. Is {@code null} if {@link #debug} is {@code false}. */
    public AppStream dbg;

    /**
     * Whether a dummy continuous variable is used, due to the CIF specification not having any continuous variables
     * (except variable 'time').
     */
    protected final boolean useDummyVar;

    /** The state at the start of the time transition. Only valid during {@link #solveIVP}. */
    private S state0;

    /**
     * A copy of {@link #state0}, that can be updated in-place, and used to evaluate guards and derivatives. Is
     * {@code null} until the copy is made.
     */
    private S state0copy;

    /** The trajectories computed so far. Only valid during {@link #solveIVP}. */
    private Trajectories trajectories;

    /**
     * Constructor for the {@link OdeSolver} class.
     *
     * @param useDummyVar Whether a dummy continuous variable is used, due to the CIF specification not having any
     *     continuous variables (except variable 'time').
     */
    protected OdeSolver(boolean useDummyVar) {
        this.useDummyVar = useDummyVar;
    }

    /**
     * Creates an array with the initial values of the continuous variables (excluding variable 'time'). If there are no
     * such variables, a dummy variable with initial value {@code 0.0} should be used.
     *
     * @param state The state at the start of the time transition.
     * @return The initial values of the continuous variables.
     */
    protected abstract double[] initY(S state);

    /**
     * Checks the values of the continuous variables and variable 'time' for invalid values. Invalid values include
     * {@code NaN}, {@code +inf}, and {@code -inf}.
     *
     * @param time The value of variable 'time'.
     * @param values The values of the continuous variables.
     * @throws UnsupportedException If the value of one of the variables is invalid.
     */
    public abstract void checkValues(double time, double[] values);

    /**
     * Checks the value of a continuous variable for invalid values. Invalid values include {@code NaN}, {@code +inf},
     * and {@code -inf}.
     *
     * @param value The value of the continuous variable.
     * @return {@code true} if the value is valid, {@code false} otherwise.
     * @see #throwValueError
     */
    protected boolean isValidValue(double value) {
        return !Double.isInfinite(value) && !Double.isNaN(value);
    }

    /**
     * Throws an exception for an invalid value of a continuous variable. Invalid values include {@code NaN},
     * {@code +inf}, and {@code -inf}.
     *
     * @param value The value of the continuous variable.
     * @param varName The absolute name of the continuous variable.
     * @throws UnsupportedException Always thrown.
     * @see #isValidValue
     */
    public void throwValueError(double value, String varName) {
        String valueTxt;
        if (Double.isNaN(value)) {
            valueTxt = "NaN";
        } else if (value == Double.POSITIVE_INFINITY) {
            valueTxt = "+inf";
        } else {
            Assert.check(value == Double.NEGATIVE_INFINITY);
            valueTxt = "-inf";
        }

        String msg = fmt("The value of variable \"%s\" has become \"%s\", which is not supported. This may indicate an "
                + "overflow. It might be possible to prevent this by setting a maximum delay for time transitions, or "
                + "by shortening it. Alternatively, try restricting passage of time in the CIF specification itself.",
                varName, valueTxt);
        throw new UnsupportedException(msg);
    }

    /**
     * Solve the 'Initial Value Problem' (IVP), and return the calculated trajectories of the continuous variables.
     *
     * @param state The state at the start of the time transition.
     * @param events The ODE state events to use to determine the maximum delay interval.
     * @param maxTime The maximum end time of the time transition.
     * @return The trajectories of the continuous variables, or {@code null} if no time transition is possible.
     */
    public Trajectories solveIVP(S state, List<OdeStateEvent<S>> events, double maxTime) {
        // Store state.
        double time0 = state.getTime();
        state0 = state;
        state0copy = null;

        if (debug) {
            dbg.printfln("ODE solver: ODE solver started.");
            dbg.printfln("ODE solver: initial state: %s", state0.toSingleLineString(null, false, false));
        }

        // Initialize 'y'.
        double[] y = initY(state);

        if (debug) {
            if (useDummyVar) {
                dbg.printfln("ODE solver: no continuous variables, using dummy variable.");
            } else {
                dbg.printfln("ODE solver: %d continuous variable(s).", y.length);
            }
        }

        // Initialize trajectories.
        trajectories = new Trajectories();

        // Add initial values to the trajectories.
        addToTrajs(time0, y);

        // Construct integrator.
        int intNumSteps = IntegratorNumStepsOption.getIntNumSteps();
        double intMinStep = IntegratorMinStepOption.getIntMinStep();
        double intMaxStep = IntegratorMaxStepOption.getIntMaxStep();
        double intAbsTol = IntegratorAbsTolOption.getIntAbsTol();
        double intRelTol = IntegratorRelTolOption.getIntRelTol();
        IntegratorAlgo intAlgo = IntegratorAlgoOption.getIntAlgo();
        FirstOrderIntegrator integrator = intAlgo.create(intNumSteps, intMinStep, intMaxStep, intAbsTol, intRelTol);

        if (debug) {
            dbg.printfln("ODE solver: integrator algorithm: %s.", intAlgo.name);
            dbg.printfln("ODE solver: integrator minimum step size: %s.", realToStr(intMinStep));
            dbg.printfln("ODE solver: integrator maximum step size: %s.", realToStr(intMaxStep));
            dbg.printfln("ODE solver: integrator absolute tolerance: %s.", realToStr(intAbsTol));
            dbg.printfln("ODE solver: integrator relative tolerance: %s.", realToStr(intRelTol));
            dbg.printfln("ODE solver: integrator number of steps: %d.", intNumSteps);
        }

        // Add step handler to integrator.
        Double outStep = OdeSolverOutStepOption.getSolverOutStep();
        StepHandler stepHandler = (outStep == null) ? this
                : new StepNormalizer(outStep, this, StepNormalizerMode.MULTIPLES, StepNormalizerBounds.LAST);
        integrator.addStepHandler(stepHandler);

        if (debug) {
            String stepTxt = (outStep == null) ? "disabled" : realToStr(outStep);
            dbg.printfln("ODE solver: fixed output step size: %s.", stepTxt);
        }

        // Add event handlers to integrator.
        double rootAbsTol = RootFinderAbsTolOption.getRootAbsTol();
        double rootRelTol = RootFinderRelTolOption.getRootRelTol();
        double rootMaxChk = RootFinderMaxCheckOption.getRootMaxChk();
        int rootMaxIter = RootFinderMaxIterOption.getRootMaxIter();
        RootFinderAlgo rootAlgo = RootFinderAlgoOption.getRootAlgo();

        if (debug) {
            dbg.printfln("ODE solver: root finder algorithm: %s.", rootAlgo.name);
            dbg.printfln("ODE solver: root finder maximum check interval: %s.", realToStr(rootMaxChk));
            dbg.printfln("ODE solver: root finder maximum iterations: %d.", rootMaxIter);
            dbg.printfln("ODE solver: root finder absolute tolerance: %s.", realToStr(rootAbsTol));
            dbg.printfln("ODE solver: root finder relative tolerance: %s.", realToStr(rootRelTol));
        }

        if (debug) {
            dbg.printfln("ODE solver: state event guard predicates: %d.", events.size());
        }

        for (int i = 0; i < events.size(); i++) {
            OdeStateEvent<S> event = events.get(i);
            BaseSecantSolver solver = rootAlgo.create(rootAbsTol, rootRelTol);
            integrator.addEventHandler(event, rootMaxChk, rootAbsTol, rootMaxIter, solver);

            if (debug) {
                dbg.printfln("ODE solver: state event guard predicate (%d/%d): %s.", i + 1, events.size(),
                        event.getPredText());
            }
        }

        // Correct the maximum end time, if needed.
        double tEnd = maxTime;
        if (tEnd - time0 < intAbsTol) {
            // End time too close to start time to start integration. Just
            // request a slightly longer trajectory.
            double newEnd;
            double delta = intAbsTol;
            while (true) {
                newEnd = time0 + delta;
                if (newEnd - time0 >= intAbsTol) {
                    break;
                }
                delta *= 2;
            }
            tEnd = newEnd;
        }

        if (debug) {
            dbg.printfln("ODE solver: maximum end time (original): %s.", realToStr(maxTime));
            dbg.printfln("ODE solver: maximum end time (corrected): %s.", realToStr(tEnd));
        }

        // Perform integration using the ODE solver.
        if (debug) {
            dbg.printfln("ODE solver: trajectories calculation started.");
        }

        double tLast;
        try {
            tLast = integrator.integrate(this, time0, y, tEnd, y);
        } catch (MathIllegalArgumentException e) {
            String msg = "ODE solver failed to compute the trajectories of the continuous variables. The problem "
                    + "(your specification) may be too complicated. You could try modifying the ODE solver options.";
            throw new UnsupportedException(msg, e);
        } catch (MathIllegalStateException e) {
            String msg = "ODE solver failed to compute the trajectories of the continuous variables. The problem "
                    + "(your specification) may be too complicated. You could try modifying the ODE solver options.";
            throw new UnsupportedException(msg, e);
        }
        Assert.check(tLast >= time0);
        Assert.check(tLast == trajectories.getLastTime());

        if (debug) {
            dbg.printfln("ODE solver: trajectories calculation finished.");
            dbg.printfln("ODE solver: trajectories end time: %s.", realToStr(tLast));
        }

        // Reset fields, to allow garbage collection. Keep local copies if
        // needed.
        Trajectories trajs = trajectories;
        state0 = null;
        state0copy = null;
        trajectories = null;

        // Return the calculated trajectories.
        if (debug) {
            dbg.printfln("ODE solver: trajectories contain %d time point(s).", trajs.getCount());
            dbg.printfln("ODE solver: a time transition is%s possible.", (trajs.getCount() < 2) ? " not" : "");
            dbg.printfln("ODE solver: ODE solver finished.");
            dbg.println();
        }

        if (trajs.getCount() < 2) {
            return null;
        }
        return trajs;
    }

    /**
     * Converts a value of variable 'time' and values of the continuous variables (or their derivatives) to an end-user
     * readable representation.
     *
     * @param time The value of variable 'time'.
     * @param y The values of the continuous variables or their derivatives.
     * @param derivatives Whether the given values represent the values of the continuous variables ({@code false}) or
     *     their derivatives ({@code true}).
     * @return The end-user readable representation.
     */
    public String valuesToStr(double time, double[] y, boolean derivatives) {
        StringBuilder txt = new StringBuilder();
        txt.append("time=");
        txt.append(realToStr(time));
        for (int i = 0; i < y.length; i++) {
            txt.append(", ");
            txt.append(getContVarName(i));
            if (derivatives) {
                txt.append("'");
            }
            txt.append("=");
            txt.append(realToStr(y[i]));
        }
        return txt.toString();
    }

    /**
     * Adds a time point to the trajectories, after making sure all values are valid.
     *
     * @param time The value of variable 'time'.
     * @param y The values of the continuous variables.
     */
    private void addToTrajs(double time, double[] y) {
        checkValues(time, y);
        trajectories.add(time, y);

        if (debug) {
            String valuesTxt = valuesToStr(time, y, false);
            dbg.printfln("ODE solver: add to trajectories: %s", valuesTxt);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Implementation of the {@link FirstOrderDifferentialEquations} interface.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns the number of continuous variables (excluding variable 'time') in the specification. If there are no
     * continuous variables, value {@code 1} (for a {@link #useDummyVar dummy} continuous variable) should be returned.
     *
     * @return The number of continuous variables (excluding variable 'time') in the specification, or {@code 1}.
     */
    @Override
    public abstract int getDimension();

    /**
     * Returns the absolute name of the continuous variable with the given index.
     *
     * @param idx The 0-based index of the continuous variable. May be {@code 0} for the {@link #useDummyVar dummy}
     *     continuous variable.
     * @return The absolute name of the continuous variable.
     */
    protected abstract String getContVarName(int idx);

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        // Check for application termination.
        ctxt.checkTermination();

        // Get the full state, updated for the current time value, and the
        // current values of the continuous variables. Note that 'yDot' should
        // only be used to store the newly computed derivative values, and
        // should explicitly not be used to evaluate the derivatives.
        checkValues(t, y);
        S state = updateState(t, y);

        if (debug) {
            String valuesTxt = valuesToStr(t, y, false);
            dbg.printfln("ODE solver: computing derivatives: %s", valuesTxt);
        }

        // Compute the derivatives (in-place).
        try {
            computeDerivatives(state, yDot);
        } catch (CifSimulatorException e) {
            String msg = fmt("Evaluation of a derivative at time %s failed.", realToStr(t));
            throw new CifSimulatorException(msg, e, state);
        }

        if (debug) {
            String valuesTxt = valuesToStr(t, y, true);
            dbg.printfln("ODE solver: computed derivatives: %s", valuesTxt);
        }
    }

    /**
     * Makes a new state, from the given state and the new values for the continuous variables and variable 'time'.
     *
     * @param state The initial state of the time transition, or a copy of it.
     * @param t The value for variable 'time'.
     * @param y The values of the continuous variables.
     * @param copy Whether to copy the given initial state, leaving it intact ({@code true}) or update it in-place
     *     ({@code false}).
     * @return The new state.
     */
    public abstract S makeState(S state, double t, double[] y, boolean copy);

    /**
     * Updates {@link #state0copy}, with new values for the continuous variables and variable 'time'. Also initializes
     * {@link #state0copy} from {@link #state0} first, if needed. Note that this method may only be called during
     * execution of the {@link #solveIVP}, as afterwards {@link #state0} and {@link #state0copy} are no longer
     * available.
     *
     * @param t The value for variable 'time'.
     * @param y The values of the continuous variables.
     * @return The updated state.
     */
    public S updateState(double t, double[] y) {
        if (state0copy != null) {
            return makeState(state0copy, t, y, false);
        }

        state0copy = makeState(state0, t, y, true);
        return state0copy;
    }

    /**
     * Computes the derivatives of the continuous variables, for the given state. If the specification has no continuous
     * variables, a dummy variable with derivative {@code 1.0} should be used.
     *
     * @param state The state to use to compute the derivatives.
     * @param yDot The array in which to store the computed derivatives. Is modified in-place.
     */
    protected abstract void computeDerivatives(S state, double[] yDot);

    ///////////////////////////////////////////////////////////////////////////
    // Implementation of the {@link StepHandler} and {@link FixedStepHandler}
    // interfaces.
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void init(double t0, double[] y0, double t) {
        // We don't support resetting and reusing this step handler. Note
        // however that we create a fresh step handler (this class) for each
        // problem. As such, we never reset, except for once before
        // integration. The step handler is already initialized, so for this
        // one time occurrence, we don't need to do anything.
    }

    @Override
    public void handleStep(StepInterpolator interpolator, boolean isLast) {
        // Get current values.
        double t = interpolator.getCurrentTime();
        double[] y = interpolator.getInterpolatedState();

        // Add step values to the trajectories.
        addToTrajs(t, y);
    }

    @Override
    public void handleStep(double t, double[] y, double[] yDot, boolean isLast) {
        // Add step values to the trajectories.
        addToTrajs(t, y);
    }
}
