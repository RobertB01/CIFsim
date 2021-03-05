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

package org.eclipse.escet.chi.runtime.data;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Iterator;
import java.util.List;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.SelectChoice;

/** Executing process or model in a Chi simulation. Adds itself to the coordinator during construction. */
public abstract class BaseProcess extends CoreProcess<BaseProcess> {
    // TODO: This looks like the process definition name, Where did the instance name go?
    /** Name of the process. */
    public final String name;

    /** List of selection alternatives, created and owned by the process (for re-using the list). */
    public List<SelectChoice> selectList;

    /**
     * Owned by the simulator, the list of select alternatives to choose from.
     *
     * <p>
     * If {@code null}, the process can be executed, and {@link #readyChoice} refers to the choice that is selected (if
     * available). If not {@code null}, the process is blocked. It points to {@link #selectList}.
     * </p>
     */
    public List<SelectChoice> blockedChoices = null;

    /**
     * Owned by the simulator, the select alternative to run, for this process. If {@code null}, run the process from
     * the start (that is, for the first time).
     */
    public SelectChoice readyChoice = null;

    /** Timer used for delay statements. */
    public Timer delayTimer;

    /** List of child processes, used for the 'run' statement. */
    protected List<CoreProcess<BaseProcess>> chiChilds;

    /** Stack of positions from the current process. */
    public List<PositionData> positionStack;

    /** Position in the process itself (is also available as {@link #positionStack}.get(0) ). */
    public PositionData position;

    /** Kind of process. */
    public final DefinitionKind kind;

    /** Program location pointer. */
    protected int chiProgramCounter;

    /** Chi simulator instance. */
    protected ChiCoordinator chiCoordinator;

    /**
     * Constructor for the {@link BaseProcess} class.
     *
     * @param chiCoordinator Coordinator of the simulation.
     * @param kind Kind of definition.
     * @param name Name of the process.
     */
    public BaseProcess(ChiCoordinator chiCoordinator, DefinitionKind kind, String name) {
        super(RunResult.STARTED);
        this.chiCoordinator = chiCoordinator;
        this.name = name;
        selectList = list();
        delayTimer = null;
        chiChilds = null;
        chiProgramCounter = -1;
        this.kind = kind;
        position = new PositionData(kind, name);
        positionStack = list(position);
    }

    /**
     * Setup the delay timer for the delay statement.
     *
     * @param delayLength Length of the delay.
     */
    protected void setDelayTimer(double delayLength) {
        delayLength += chiCoordinator.getCurrentTime();
        delayTimer = new Timer(chiCoordinator, delayLength);
    }

    /**
     * Notify the coordinator of a new choices by a select statement.
     *
     * @param choices Alternatives to choose from.
     */
    protected void setSelect(List<SelectChoice> choices) {
        chiCoordinator.setSelect(this, choices);
    }

    /** Setup child process list for the run statement. */
    protected void clearChildProcesses() {
        if (chiChilds == null) {
            chiChilds = list();
        } else {
            chiChilds.clear();
        }
    }

    /**
     * Have all 'run' child processes finished?
     *
     * @return All 'run' child processes have finished.
     */
    public boolean allChildProcessesFinished() {
        Iterator<CoreProcess<BaseProcess>> iter = chiChilds.iterator();
        while (iter.hasNext()) {
            CoreProcess<BaseProcess> p = iter.next();
            if (p.isFinished()) {
                iter.remove();
            }
        }
        return chiChilds.isEmpty();
    }

    /**
     * Perform execution of the process (until it is blocked (again).
     *
     * @param chiChoice Select choice taken the last time (use {@code null} for the first invocation). Used for deciding
     *     where to continue the execution.
     * @return Reason for ending execution.
     */
    public abstract RunResult run(SelectChoice chiChoice);
}
