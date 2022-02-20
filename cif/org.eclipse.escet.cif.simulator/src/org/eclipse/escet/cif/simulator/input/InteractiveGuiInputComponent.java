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

package org.eclipse.escet.cif.simulator.input;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.simulator.options.CifSpecOption;
import org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeEvent;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.ResetTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.cif.simulator.runtime.transitions.UndoTransition;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.widgets.Display;

/**
 * GUI input component that interactively asks the user for input, via buttons. Using the
 * {@link InteractiveAutoChooseOption} option, the interactive input component can be made semi-automatic.
 *
 * @param <S> The type of state objects to use.
 */
public final class InteractiveGuiInputComponent<S extends RuntimeState> extends InteractiveInputComponent<S> {
    /** Whether to ever choose interactively. */
    private boolean doInteractive = false;

    /** The GUI, or {@code null} if not yet or no longer available. */
    private InteractiveGuiInputEditor<S> gui = null;

    /** Is the {@link #lastTargetTime} available? */
    private boolean targetTimeAvailable = false;

    /**
     * The last chosen target time of the time transition. May be {@code null} to let the automatic input component
     * choose. Is only relevant if {@link #targetTimeAvailable} is {@code true}.
     *
     * <p>
     * The last target time choice, if applicable.
     * </p>
     */
    private ChosenTargetTime lastTargetTime = null;

    /**
     * Constructor for the {@link InteractiveGuiInputComponent}.
     *
     * @param spec The specification. The specification has not yet been {@link RuntimeSpec#init initialized}.
     */
    public InteractiveGuiInputComponent(RuntimeSpec<S> spec) {
        super(spec);
    }

    @Override
    public void init() {
        // Perform initialization common to all interactive input components.
        super.init();

        // Show and initialize GUI, unless we never choose interactively.
        if (!autoTime) {
            doInteractive = true;
        }
        if (!autoTimeDur) {
            doInteractive = true;
        }
        for (int i = 0; i < autoEvents.length; i++) {
            if (autoEvents[i]) {
                continue;
            }
            boolean isTau = (i == autoEvents.length - 1);
            if (!isTau || spec.hasTauEdge()) {
                doInteractive = true;
                break;
            }
        }
        if (isResetEnabled) {
            doInteractive = true;
        }
        if (isUndoEnabled) {
            doInteractive = true;
        }
        if (doInteractive) {
            // Show GUI.
            String path = CifSpecOption.getCifSpecPath();
            gui = ControlEditor.show(path, InteractiveGuiInputEditor.class, "show the GUI input component");

            // Initialize GUI;
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (!gui.isAvailable()) {
                        return;
                    }
                    gui.init(spec, InteractiveGuiInputComponent.this);
                }
            });
        }
    }

    @Override
    protected boolean needAutomaticInputComponent() {
        // Always needed, either for the semi-automatic choices, or otherwise
        // for choosing interactively to let the automatic input component
        // choose.
        return true;
    }

    @Override
    protected Transition<S> chooseTransitionInteractive(S state, final List<Transition<S>> transitions) {
        // Paranoia checking.
        Assert.check(doInteractive);

        // Ask user for a choice.
        targetTimeAvailable = false;
        lastTargetTime = null;

        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                gui.choice.set(null);
                if (!gui.isAvailable()) {
                    gui.ready.set(true);
                    return;
                }
                gui.ready.set(false);
                gui.chooseTransition(state, transitions);
            }
        });

        // Wait for the user to make a choice.
        waitForChoice();
        if (!gui.isAvailable()) {
            throw new SimulatorExitException(SimulationResult.USER_TERMINATED);
        } else {
            // Get choice.
            InteractiveGuiInputChoice choice = gui.choice.get();

            // Check for no choice (GUI closed).
            if (!choice.hasTransitionChoice()) {
                throw new SimulatorExitException(SimulationResult.USER_TERMINATED);
            }

            // Handle history transitions.
            if (choice.resetChosen) {
                @SuppressWarnings("unchecked")
                S target = (S)history.reset();
                return new ResetTransition<>(state, target);
            } else if (choice.undoCount > 0) {
                @SuppressWarnings("unchecked")
                S target = (S)history.undo(choice.undoCount);
                return new UndoTransition<>(state, target, choice.undoCount);
            }

            // Choose directly if explicit transition chosen.
            if (choice.transIdx != -1) {
                Assert.check(!choice.timeChosen);
                return transitions.get(choice.transIdx);
            }

            // Get the automatic transition choices for the event.
            List<Transition<S>> autoTrans = listc(transitions.size());
            for (Transition<S> transition: transitions) {
                if (transition instanceof EventTransition) {
                    RuntimeEvent<?> event;
                    event = ((EventTransition<?>)transition).event;
                    if (event.idx == choice.eventIdx) {
                        autoTrans.add(transition);
                    }
                } else if (transition instanceof TimeTransition) {
                    if (choice.timeChosen) {
                        Assert.check(!targetTimeAvailable);
                        targetTimeAvailable = true;
                        lastTargetTime = choice.targetTime;
                        autoTrans.add(transition);
                    }
                } else {
                    throw new RuntimeException("Unknown trans: " + transition);
                }
            }

            // If there is only one transition, the choice is obvious.
            // Otherwise, let the automatic input component choose.
            Assert.check(!autoTrans.isEmpty());
            if (autoTrans.size() == 1) {
                return first(autoTrans);
            } else {
                return autoInput.chooseTransition(state, autoTrans, null);
            }
        }
    }

    @Override
    protected HistoryTransition<S> chooseTransitionInteractive(S state, SimulationResult result) {
        // Paranoia checking.
        Assert.check(doInteractive);

        // Ask user for a choice.
        targetTimeAvailable = false;
        lastTargetTime = null;

        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                gui.choice.set(null);
                if (!gui.isAvailable()) {
                    gui.ready.set(true);
                    return;
                }
                gui.ready.set(false);
                gui.chooseNoTransition(state);
            }
        });

        // Wait for the user to make a choice.
        waitForChoice();
        if (!gui.isAvailable()) {
            throw new SimulatorExitException(result);
        } else {
            // Get choice.
            InteractiveGuiInputChoice choice = gui.choice.get();

            // Check for no choice (GUI closed).
            if (!choice.hasNoTransitionChoice()) {
                throw new SimulatorExitException(result);
            }

            // Handle history transitions.
            if (choice.resetChosen) {
                @SuppressWarnings("unchecked")
                S target = (S)history.reset();
                return new ResetTransition<>(state, target);
            } else if (choice.undoCount > 0) {
                @SuppressWarnings("unchecked")
                S target = (S)history.undo(choice.undoCount);
                return new UndoTransition<>(state, target, choice.undoCount);
            }

            // Should not get here. All choices should have been handled above.
            throw new RuntimeException("Unhandled choice.");
        }
    }

    @Override
    protected ChosenTargetTime chooseTargetTimeInteractive(final S state, final double maxTargetTime) {
        // Paranoia checking.
        Assert.check(doInteractive);

        // If already available, no need to ask.
        if (targetTimeAvailable) {
            // Get last choice, and make it unavailable.
            ChosenTargetTime rslt = lastTargetTime;
            targetTimeAvailable = false;
            lastTargetTime = null;

            // Actual choice available.
            if (rslt != null) {
                return rslt;
            }

            // Defer to automatic input component.
            return autoInput.chooseTargetTime(state, maxTargetTime);
        }

        // Ask user for a choice.
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                gui.choice.set(null);
                if (!gui.isAvailable()) {
                    gui.ready.set(true);
                    return;
                }
                gui.ready.set(false);
                gui.chooseDelay(state, maxTargetTime);
            }
        });

        // Wait for the user to make a choice.
        waitForChoice();
        if (!gui.isAvailable()) {
            throw new SimulatorExitException(SimulationResult.USER_TERMINATED);
        } else {
            // Get choice.
            InteractiveGuiInputChoice choice = gui.choice.get();

            // Check for no choice (GUI closed).
            if (!choice.hasDelayChoice()) {
                throw new SimulatorExitException(SimulationResult.USER_TERMINATED);
            }

            // Actual choice available.
            if (choice.targetTime != null) {
                return choice.targetTime;
            }

            // Defer to automatic input component.
            return autoInput.chooseTargetTime(state, maxTargetTime);
        }
    }

    /**
     * Wait to be notified that the choice has been made by the user, the GUI has been closed, or the simulation has
     * been terminated.
     */
    private void waitForChoice() {
        synchronized (gui.ready) {
            while (!gui.ready.get()) {
                // Wait for GUI input.
                try {
                    gui.ready.wait(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Check for termination.
                try {
                    spec.ctxt.checkTermination();
                } catch (SimulatorExitException e) {
                    // Simulation has been terminated. Disable the GUI buttons.
                    Display.getDefault().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            gui.resetAndDisableUI();
                        }
                    });

                    throw e;
                }
            }
        }
    }
}
