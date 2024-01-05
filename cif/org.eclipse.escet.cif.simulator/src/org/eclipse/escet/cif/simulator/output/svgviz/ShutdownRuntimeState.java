//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.svgviz;

import java.util.List;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;

/**
 * Dummy {@link RuntimeState} instance used to request termination of the {@link SvgRenderThread}, from the
 * {@link SvgOutputComponent}.
 */
public class ShutdownRuntimeState extends RuntimeState {
    /** Singleton instance of the {@link ShutdownRuntimeState} class. */
    public static final ShutdownRuntimeState INSTANCE = new ShutdownRuntimeState();

    /** Constructor for the {@link ShutdownRuntimeState} class. */
    private ShutdownRuntimeState() {
        super(null); // No 'spec' provided.
    }

    @Override
    public double getTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAutCurLocName(int idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getStateVarNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getStateVarValue(int idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getStateVarDerValue(int idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getAlgVarNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAlgVarValue(int idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean checkInitialization() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void calcTransitions(Double endTime, Double maxDelay) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void calcTimeTransition(Double endTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transition<?> chooseTransition(RuntimeState state, List<Transition<?>> transitions,
            SimulationResult result)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChosenTargetTime chooseTargetTime(double maxTargetTime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Double getNextMaxEndTime() {
        throw new UnsupportedOperationException();
    }
}
