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

package org.eclipse.escet.chi.runtime;

import static org.eclipse.escet.chi.runtime.data.io.ChiFileHandle.createFile;

import java.util.List;

import org.eclipse.escet.chi.runtime.SimulationResult.ExitReason;
import org.eclipse.escet.chi.runtime.data.BaseProcess;
import org.eclipse.escet.chi.runtime.data.Timer;
import org.eclipse.escet.chi.runtime.data.io.ChiFileHandle;
import org.eclipse.escet.chi.runtime.data.io.ChiReadDataFile;
import org.eclipse.escet.chi.runtime.data.random.GaussianGenerator;
import org.eclipse.escet.chi.runtime.data.random.MersenneTwister;
import org.eclipse.escet.chi.runtime.data.random.RandomGenerator;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.java.Assert;

/** Coordinate execution of one or more Chi processes. */
public class ChiCoordinator {
    /** The simulation application. */
    private final Application<?> app;

    /** Data of the currently running simulation. */
    private SimulationData currentSim;

    /** Standard output file handle. */
    private ChiFileHandle stdout;

    /** Standard input file handle, if it is opened. */
    private ChiFileHandle stdin;

    /** Common random generator to use. */
    private RandomGenerator randGen;

    /** Common Gaussian generator to use. */
    private GaussianGenerator gausGen;

    /** Initial seed of the random generator. */
    private long seed;

    /**
     * Did the user supply an initial seed? ({@code true} means it was provided, {@code false} means a seed needs to be
     * created when needed).
     */
    private final boolean seedProvided;

    /**
     * Coordinator constructor.
     *
     * @param app Simulation application.
     * @param seed Initial seed of the application.
     * @param seedProvided Whether the user provided a seed.
     */
    public ChiCoordinator(Application<?> app, long seed, boolean seedProvided) {
        this.app = app;
        stdout = createFile(null, "w", "text");
        stdin = null;

        currentSim = null;

        randGen = null;
        gausGen = null;
        this.seed = seed;
        this.seedProvided = seedProvided;
    }

    /**
     * Add a new timer to the simulation.
     *
     * @param t Timer to add.
     * @return Authorative timer for the given timer, {@code null} if the timer already expired.
     */
    public Timer addTimer(Timer t) {
        return currentSim.addTimer(t);
    }

    /**
     * Open a file.
     *
     * <p>
     * Allows opening a real file either for reading or for writing, exactly once.
     * </p>
     *
     * @param path Path to the file.
     * @param operation Allowed operation on the handle. Should be either {@code "r"} or {@code "w"}.
     * @param type Type of file IO. Current only supported type is {@code "text"}.
     * @return Handle to the opened file.
     */
    public ChiFileHandle openFile(String path, String operation, String type) {
        Assert.check(path != null);
        return currentSim.openFile(path, operation, type);
    }

    /**
     * If needed open the stdin, and return the handle.
     *
     * @return The stdin file handle.
     */
    public ChiFileHandle getStdin() {
        if (stdin == null) {
            stdin = createFile(null, "r", "text");
        }
        return stdin;
    }

    /**
     * Get stdout file handle.
     *
     * @return The stdout file handle.
     */
    public ChiFileHandle getStdout() {
        return stdout;
    }

    /** Close all files. */
    public void closeFiles() {
        currentSim.closeFiles();
    }

    /**
     * Close the given file handle file, and clean up the internal administration of opened files.
     *
     * @param handle File handle to close.
     */
    public void closeFile(ChiFileHandle handle) {
        currentSim.closeFile(handle);
    }

    /**
     * Return information about the newlines in front of the next value to read.
     *
     * <p>
     * Works only for input files.
     * </p>
     *
     * @param handle Handle of the file to inspect.
     * @return Number of newline characters in the leading whitespace if another value exists, or {@code -1} if at EOF.
     */
    public int getNewlines(ChiFileHandle handle) {
        if (handle instanceof ChiReadDataFile) {
            ChiReadDataFile rdf = (ChiReadDataFile)handle;
            return rdf.getNewlinesInfo();
        }
        String msg = "Whitespace checking can only be done on input files.";
        throw new ChiSimulatorException(msg);
    }

    /**
     * Return whether no more values start at the current line.
     *
     * <p>
     * Works only for input files.
     * </p>
     *
     * @param handle Handle of the file to inspect.
     * @return Whether no more values start at the current line.
     */
    public boolean checkEol(ChiFileHandle handle) {
        if (handle instanceof ChiReadDataFile) {
            ChiReadDataFile rdf = (ChiReadDataFile)handle;
            return rdf.getNewlinesInfo() != 0;
        }
        String msg = "End of line (EOL) checking can only be done on input files.";
        throw new ChiSimulatorException(msg);
    }

    /**
     * Check whether no more values exist in remainder of the file.
     *
     * <p>
     * Works only for input files.
     * </p>
     *
     * @param handle Handle of the file to inspect.
     * @return Whether EOF will be found before starting to read the next value.
     */
    public boolean checkEof(ChiFileHandle handle) {
        if (handle instanceof ChiReadDataFile) {
            ChiReadDataFile rdf = (ChiReadDataFile)handle;
            return rdf.getNewlinesInfo() < 0;
        }
        String msg = "End of file (EOF) checking can only be done on input files.";
        throw new ChiSimulatorException(msg);
    }

    /**
     * Get a new random generator.
     *
     * @return A new random generator.
     */
    public RandomGenerator getFreshGenerator() {
        if (randGen == null) {
            if (!seedProvided) {
                seed = System.currentTimeMillis() + System.identityHashCode(this);
                // We really don't want to use 0 as seed value.
                if (seed == 0) {
                    seed = 1;
                }
            }
            randGen = new MersenneTwister(seed);

            // Store the new seed as an application property, for crash logs.
            AppEnv.setProperty("org.eclipse.escet.chi.runtime.seed", Long.toString(seed));
        }
        return randGen; // The same MT for every generator.
    }

    /**
     * Get a new Gaussian generator.
     *
     * <p>
     * Note that querying a Gaussian generator implies having constructed at least one, which in turn implies that at
     * least one random generator is constructed.
     * </p>
     *
     * @return A new Gaussian generator.
     */
    public GaussianGenerator getGaussianGenerator() {
        if (gausGen == null) {
            gausGen = new GaussianGenerator(this);
        }
        return gausGen;
    }

    /**
     * Setup a selection of choices for the running process.
     *
     * @param proc Process setting up the choices.
     * @param choices Alternatives to choose between.
     */
    public void setSelect(BaseProcess proc, List<SelectChoice> choices) {
        currentSim.setSelect(proc, choices);
    }

    /**
     * A process is requesting termination of the simulation.
     *
     * @param exitValue Value to use for the exit, or {@code null}.
     */
    public void setTerminateAll(Object exitValue) {
        currentSim.setTerminateAll(exitValue, ExitReason.EXITED);
    }

    /**
     * Is the simulation in the process of terminating it?
     *
     * @throws TerminatedException If the simulation is shutting down.
     */
    public void testTerminating() {
        currentSim.testTerminating();
    }

    /**
     * Add a new process to the list of ready to run processes.
     *
     * @param newProcess Process to add.
     */
    public void addProcess(BaseProcess newProcess) {
        currentSim.addProcess(newProcess);
    }

    /**
     * Get the current simulation time.
     *
     * @return The current simulation time.
     */
    public double getCurrentTime() {
        return currentSim.getCurrentTime();
    }

    /**
     * Perform a Chi simulation (one model or experiment simulation).
     *
     * @param rootProcess Model or experiment to run.
     * @return Result of the simulation.
     */
    public SimulationResult run(BaseProcess rootProcess) {
        // Setup a new simulation with the root process.
        Assert.check(currentSim == null || currentSim.simState == SimulationState.RUNNING);
        currentSim = new SimulationData(app, this, currentSim);
        addProcess(rootProcess);

        // Run the simulation.
        Assert.check(currentSim.simState == SimulationState.STARTING);
        stdout.flush();
        try {
            currentSim.run();
        } finally {
            stdout.flush();
        }

        // Store the result.
        SimulationResult simResult = new SimulationResult(rootProcess.kind, randGen != null, seed,
                currentSim.exitReason, currentSim.getCurrentTime(), currentSim.exitValue);

        // Pop the simulation again, and return.
        currentSim = currentSim.parentSim;
        return simResult;
    }

    /**
     * Finish the simulation, pops the current simulation and return its exit value. Note this value is only non-null
     * for models with a non-void exit type that terminated by using the 'exit' statement.
     *
     * @return The exit value of the last run simulation, or {@code null}.
     */
    public Object finishSimulation() {
        Assert.check(currentSim.simState == SimulationState.FINISHED);
        Object exitValue = currentSim.exitValue;
        currentSim = currentSim.parentSim;
        return exitValue;
    }

    /**
     * Execute a 'model' (with non-void exit type) as part of an experiment.
     *
     * @param model Model instance to execute.
     * @return Exit value of the model execution.
     * @throws ChiSimulatorException If no valid exit value is obtained.
     */
    public Object runSubSimulation(BaseProcess model) {
        SimulationResult simResult = run(model);
        testTerminating(); // Quit if termination was requested by the user.
        if (simResult.exitValue != null) {
            return simResult.exitValue;
        }

        // No 'exit' called.
        switch (simResult.exitReason) {
            case DEADLOCKED: {
                String msg = "Model terminated due to deadlock (causing infinite delay).";
                throw new ChiSimulatorException(msg);
            }
            case FINISHED: {
                String msg = "All processes ended without using 'exit'.";
                throw new ChiSimulatorException(msg);
            }
            case ABORTED: // Already handled above.
            case ERROR: // Never happens here, has been propagated already.
            case EXITED: // Should already be handled above.
            default:
                Assert.fail("Unexpected fail reason.");
                return null; // Not reached.
        }
    }
}
