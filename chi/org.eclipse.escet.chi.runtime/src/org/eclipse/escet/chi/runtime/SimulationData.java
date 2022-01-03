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

package org.eclipse.escet.chi.runtime;

import static org.eclipse.escet.chi.runtime.data.io.ChiFileHandle.createFile;
import static org.eclipse.escet.chi.runtime.data.random.IntegerUniformDistribution.drawIntegerUniform;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.escet.chi.runtime.SelectChoice.GuardKind;
import org.eclipse.escet.chi.runtime.SimulationResult.ExitReason;
import org.eclipse.escet.chi.runtime.data.BaseProcess;
import org.eclipse.escet.chi.runtime.data.Channel;
import org.eclipse.escet.chi.runtime.data.CoreProcess.RunResult;
import org.eclipse.escet.chi.runtime.data.PositionData;
import org.eclipse.escet.chi.runtime.data.Timer;
import org.eclipse.escet.chi.runtime.data.io.ChiFileHandle;
import org.eclipse.escet.chi.runtime.data.random.RandomGenerator;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.removablelist.RemovableList;

/**
 * Data of a simulation.
 *
 * <p>
 * The process administration is organized as follows:
 * <ul>
 * <li>{@link #currentProcess} is the currently running process.</li>
 * <li>{@link #readyProcesses} is the list ready-to-run processes.</li>
 * <li>{@link #blockedProcesses} is the list blocked processes.</li>
 * </ul>
 * </p>
 * <p>
 * Each blocked process has a list select choices it is blocked on, in {@link BaseProcess#blockedChoices}. These choices
 * may include alternatives with a {@link GuardKind#FALSE} guard.
 * </p>
 *
 * <p>
 * For faster finding new processes to run, the blocked choices of each blocked process are examined and
 * <ul>
 * <li>Choices with a {@link GuardKind#FALSE} guard are skipped,</li>
 * <li>Remaining choices without communication are added to the {@link #nocommChoices} list.</li>
 * <li>Remaining choices are added as sender or receiver choice to the channel they want to communicate on.</li>
 * </ul>
 * Channels with both sender and receiver choices are added to the {@link #commChoices} list.
 * </p>
 */
public class SimulationData {
    /** The simulation application. */
    private final Application<?> app;

    /** Coordinator of the experiment. */
    private final ChiCoordinator coord;

    /** State of the simulation. */
    public SimulationState simState = SimulationState.STARTING;

    /**
     * Parent simulation data (which is suspended until this simulation has ended). {@code null} if no parent available.
     */
    public final SimulationData parentSim;

    /** Current time. */
    private double simulationTime = 0.0;

    /**
     * Existing timers.
     *
     * <p>
     * Since it is a set, only one timer for every timeout-value is stored.
     * </p>
     */
    private TreeSet<Timer> timers = new TreeSet<>();

    /** Reason for ending the simulation. Is {@code null} if the simulation is still running. */
    public ExitReason exitReason = null;

    /** Currently executing process. */
    private BaseProcess currentProcess = null;

    /** Ready to run processes. */
    private RemovableList<BaseProcess> readyProcesses = new RemovableList<>();

    /** Blocked processes. */
    private RemovableList<BaseProcess> blockedProcesses = new RemovableList<>();

    /** Select choices that perform communication, ordered by channel. */
    private RemovableList<Channel> commChoices = new RemovableList<>();

    /** Select choices without communication. */
    private RemovableList<SelectChoice> nocommChoices = new RemovableList<>();

    /** Select choices about child processes. */
    private RemovableList<SelectChoice> runChoices = new RemovableList<>();

    /** Whether to re-evaluate the {@link #runChoices} list again. */
    private boolean inspectRunChoices = true;

    /**
     * Temporary list of send select choices, while searching for communications.
     *
     * <p>
     * Only used in {@link #searchReceiverChoices}, but kept as a global variable to avoid constructing many lists.
     * </p>
     */
    private List<SelectChoice> viableSenders = list();

    /** Random number generator for the starting point in searching blocked processes. */
    private final RandomGenerator selaltGen;

    /** Files that have been opened in this simulation. */
    private Set<ChiFileHandle> openedFiles = set();

    /** SVG output stream, may be {@code null}. */
    private ChiSvgOutput svgOutput = null;

    /**
     * Value returned with 'exit'. For simulations without exit type or with a 'void' exit type, this value is always
     * {@code null}. Otherwise, it will be non-null only when the model exits with an 'exit' statement.
     */
    public Object exitValue = null;

    /**
     * Coordinator data constructor. Contains data for a single simulation.
     *
     * @param app Simulation application.
     * @param coord Coordinator of the experiment.
     * @param parentSim Parent simulation ({@code null} if no parent exists).
     */
    public SimulationData(Application<?> app, ChiCoordinator coord, SimulationData parentSim) {
        this.app = app;
        this.coord = coord;
        selaltGen = coord.getFreshGenerator();
        this.parentSim = parentSim;
    }

    /**
     * Add a new timer to the simulation.
     *
     * @param t Timer to add.
     * @return The authorative timer for the provided timer, {@code null} if the timer already expired.
     */
    public Timer addTimer(Timer t) {
        if (t.endTime <= simulationTime) {
            return null; // Already expired.
        }
        // Most models involving time are stochastic, which makes most timers
        // unique. Therefore, first try to add the timer.
        if (timers.add(t)) {
            // Timer got added, it is also its authorative timer.
            return t;
        }
        // Timer already present, get the authorative timer instead.
        Timer authTimer = timers.floor(t);
        Assert.check(authTimer != null && authTimer != t);
        return authTimer;
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
        // SVG file open request.
        if (path.startsWith("SVG:")) {
            if (!operation.equals("w")) {
                String msg = "SVG output must be opened for write-only.";
                throw new ChiSimulatorException(msg);
            }
            Assert.check(type.equals("text"));

            if (svgOutput == null) {
                svgOutput = new ChiSvgOutput(coord, path);
                svgOutput.openCalled();
                return svgOutput;
            }
            if (!svgOutput.filename.equals(path)) {
                String msg = fmt("SVG output of file \"%s\" requested, but already opened file \"%s\".", path,
                        svgOutput.filename);
                throw new ChiSimulatorException(msg);
            }
            svgOutput.openCalled();
            return svgOutput;
        }

        // Normal file open request.
        ChiFileHandle cfh = createFile(path, operation, type);
        if (isFileOpened(cfh)) {
            throw new ChiSimulatorException("File \"" + path + "\" is already opened for IO.");
        }
        openedFiles.add(cfh);
        return cfh;
    }

    /**
     * Is the provided file in use already?
     *
     * @param handle File handle to test.
     * @return Whether the provided file handle is already open.
     */
    protected boolean isFileOpened(ChiFileHandle handle) {
        if (openedFiles.contains(handle)) {
            return true;
        }
        if (parentSim == null) {
            return false;
        }
        return parentSim.isFileOpened(handle);
    }

    /** Close all files. */
    public void closeFiles() {
        for (ChiFileHandle cfh: openedFiles) {
            cfh.close();
        }
        if (svgOutput != null) {
            svgOutput.closeDown();
        }
    }

    /**
     * Close the given file handle file, and clean up the internal administration of opened files.
     *
     * @param handle File handle to close.
     */
    public void closeFile(ChiFileHandle handle) {
        handle.close();
        // Some streams ignore the 'close', and stay open.
        if (handle.isClosed()) {
            openedFiles.remove(handle);
        }
    }

    /**
     * Setup a selection of choices for the running process.
     *
     * @param proc Process setting up the choices.
     * @param choices Alternatives to choose between.
     */
    public void setSelect(BaseProcess proc, List<SelectChoice> choices) {
        Assert.check(currentProcess == proc);
        currentProcess.blockedChoices = choices;
    }

    /**
     * A process is requesting termination of the simulation.
     *
     * @param exitValue Value to use for the exit, or {@code null}.
     * @param reason Reason why the simulation ends.
     */
    public void setTerminateAll(Object exitValue, ExitReason reason) {
        this.exitReason = reason;
        if (this.exitValue == null) {
            this.exitValue = exitValue;
        }
    }

    /**
     * Is the simulation in the process of terminating it?
     *
     * @throws TerminatedException If the simulation is shutting down.
     */
    public void testTerminating() {
        if (exitReason == null) {
            if (!app.getAppEnvData().isTerminationRequested()) {
                return;
            }
            setTerminateAll(null, ExitReason.ABORTED);
        }
        throw new TerminatedException();
    }

    /**
     * Add a new process to the list of ready to run processes.
     *
     * @param newProcess Process to add.
     */
    public void addProcess(BaseProcess newProcess) {
        Assert.check(newProcess != null);
        Assert.check(currentProcess == null || currentProcess != newProcess);
        for (int i = 0; i < readyProcesses.size(); i++) {
            Assert.check(readyProcesses.get(i) != newProcess);
        }
        for (int i = 0; i < blockedProcesses.size(); i++) {
            Assert.check(blockedProcesses.get(i) != newProcess);
        }

        if (exitReason == null) {
            // Only add new processes if not terminating the simulation.
            readyProcesses.add(newProcess);
        }
    }

    /**
     * Get the current simulation time.
     *
     * @return The current simulation time.
     */
    public double getCurrentTime() {
        return simulationTime;
    }

    /**
     * Return whether the simulation has blocked processes (which indicates existence of deadlock in a terminated
     * simulation).
     *
     * @return Whether the simulation has blocked processes.
     */
    public boolean endedWithDeadlock() {
        return !blockedProcesses.isEmpty();
    }

    /**
     * Construct a text describing the call stack that was saved.
     *
     * @return Text with a human-readable description of the call stack.
     */
    private String dumpStackPositions() {
        if (currentProcess.positionStack == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        int i = currentProcess.positionStack.size() - 1;
        while (i >= 0) {
            PositionData pd = currentProcess.positionStack.get(i);
            sb.append((first) ? "\nat " : "\ncalled from ");
            sb.append(pd.getPosition());
            sb.append(".");
            first = false;
            i--;
        }
        return sb.toString();
    }

    /** Execute ready-to-run processes until all are blocked. */
    private void executeReadyProcesses() {
        for (;;) {
            currentProcess = readyProcesses.poll();
            if (currentProcess == null) {
                break;
            }
            Assert.check(currentProcess.blockedChoices == null);
            SelectChoice currentChoice = currentProcess.readyChoice;
            currentProcess.readyChoice = null;
            RunResult state;
            try {
                state = currentProcess.run(currentChoice);
            } catch (ChiSimulatorException e) {
                setTerminateAll(null, ExitReason.ERROR);
                String msg = fmt("Runtime error while executing \"%s\".%s", currentProcess.name, dumpStackPositions());
                throw new ChiSimulatorException(msg, e);
            } catch (StackOverflowError e) {
                setTerminateAll(null, ExitReason.ERROR);
                String msg = fmt("Too many nested function invocations while executing \"%s\".%s", currentProcess.name,
                        dumpStackPositions());
                throw new ChiSimulatorException(msg, e);
            } catch (TerminatedException e) {
                state = RunResult.TERMINATED;
            }

            currentProcess.setState(state);
            if (state == RunResult.BLOCKED) {
                blockProcess(currentProcess);
                // Optionally, one can check the select choices to see
                // whether it can continue running (as well as awake other
                // processes).
                continue;
            }
            Assert.check(state == RunResult.FINISHED || state == RunResult.TERMINATED);
            // Don't save a finished process, it will disappear by itself
            // after all instance references have died.
            currentProcess = null;
            inspectRunChoices = true; // A parent process may become available.

            if (exitReason != null) {
                break;
            }
        }
    }

    /**
     * Process has become blocked, add the process to the blocked administration ({@link #blockedProcesses},
     * {@link #nocommChoices}, {@link #commChoices} and the channels).
     *
     * @param proc Process that became blocked.
     */
    private void blockProcess(BaseProcess proc) {
        blockedProcesses.add(proc);

        Assert.check(proc.blockedChoices != null);
        for (SelectChoice choice: proc.blockedChoices) {
            switch (choice.guardKind) {
                case FALSE:
                    break; // Never add.

                case CHILDS:
                    runChoices.add(choice);
                    inspectRunChoices = true; // New choice needs evaluating.
                    break;

                case TIMER: {
                    SelectDelay delayCh = (SelectDelay)choice;
                    Timer authTimer = delayCh.getTimer().authorativeTimer;
                    if (authTimer != null) {
                        authTimer.choices.add(delayCh);
                        break;
                    }
                }
                // If timer expired, add the (ready-to-run) choice to the
                // nocommChoices.

                // $FALL-THROUGH$
                case FUNC:
                case TRUE:
                    switch (choice.channelKind) {
                        case NONE:
                            nocommChoices.add(choice);
                            break;

                        case RECEIVE: {
                            Channel ch = choice.channel;
                            ch.receivers.add(choice);
                            if (ch.getListIndex() < 0 && !ch.senders.isEmpty()) {
                                // Adding caused sender/receiver pair(s).
                                commChoices.add(ch);
                            }
                            break;
                        }

                        case SEND: {
                            Channel ch = choice.channel;
                            ch.senders.add(choice);
                            if (ch.getListIndex() < 0 && !ch.receivers.isEmpty()) {
                                // Adding caused sender/receiver pair(s).
                                commChoices.add(ch);
                            }
                            break;
                        }

                        default:
                            Assert.fail("Unexpected channel kind found.");
                    }
                    break;

                default:
                    Assert.fail("Encountered unknown guard kind.");
                    break;
            }
        }
    }

    /**
     * Process has become unblocked, remove the process from the blocked administration ({@link #blockedProcesses},
     * {@link #nocommChoices}, {@link #commChoices} and the channels).
     *
     * @param proc Process that became unblocked.
     * @return Number of select choices that were unblocked.
     */
    private int unblockProcess(BaseProcess proc) {
        proc.remove();

        int unblockCount = 0;
        for (SelectChoice choice: proc.blockedChoices) {
            switch (choice.guardKind) {
                case FALSE:
                    break;

                case CHILDS:
                case TIMER:
                    choice.remove();
                    unblockCount++;
                    break;

                case FUNC:
                case TRUE:
                    switch (choice.channelKind) {
                        case NONE:
                            choice.remove();
                            unblockCount++;
                            break;

                        case RECEIVE: {
                            choice.remove();
                            unblockCount++;
                            Channel ch = choice.channel;
                            if (ch.getListIndex() >= 0 && ch.receivers.isEmpty()) {
                                // Removal caused having no receivers.
                                ch.remove();
                            }
                            break;
                        }

                        case SEND: {
                            choice.remove();
                            unblockCount++;
                            Channel ch = choice.channel;
                            if (ch.getListIndex() >= 0 && ch.senders.isEmpty()) {
                                // Removal caused having no senders.
                                ch.remove();
                            }
                            break;
                        }

                        default:
                            Assert.fail("Unexpected channel kind found.");
                    }
                    break;

                default:
                    Assert.fail("Encountered unknown guard kind.");
                    break;
            }
        }
        return unblockCount;
    }

    /**
     * Awake processes waiting on expiration of the provided timer.
     *
     * @param authTimer Authorative timer that expired.
     * @return Whether any processes were awakened.
     */
    private boolean awakeTimerChoices(Timer authTimer) {
        int size = authTimer.choices.size();
        switch (size) {
            case 0:
                return false;

            case 1: {
                SelectChoice choice = authTimer.choices.get(0);
                BaseProcess proc = choice.getProcess();
                unblockProcess(proc);
                proc.readyChoice = choice;
                proc.blockedChoices = null;
                readyProcesses.add(proc);
                return true;
            }

            default: {
                // More than one process, shift and copy the list to get more
                // random process selection behaviour.
                List<SelectChoice> choices = listc(size);
                int idx = drawIntegerUniform(selaltGen, 0, size);
                for (int count = 0; count < size; count++) {
                    choices.add(authTimer.choices.get(idx));
                    idx++;
                    if (idx == size) {
                        idx = 0;
                    }
                }

                // Awake all processes.
                for (SelectChoice choice: choices) {
                    BaseProcess proc = choice.getProcess();
                    unblockProcess(proc);
                    proc.readyChoice = choice;
                    proc.blockedChoices = null;
                    readyProcesses.add(proc);
                }
                return true;
            }
        }
    }

    /** Perform a Chi simulation of a model or experiment. */
    public void run() {
        simState = SimulationState.RUNNING;
        exitValue = null;
        try {
            for (;;) {
                // 1. Execute ready-to-run processes as long as possible.
                executeReadyProcesses();
                if (exitReason != null) {
                    // Simulation is shutting down, clean out all processes.
                    readyProcesses.clear();
                    blockedProcesses.clear();
                    commChoices.clear();
                    nocommChoices.clear();
                    runChoices.clear();
                    break;
                }

                // The ready to run queue is empty.
                // 2. Check whether all processes are really blocked.
                if (findNonblocked()) {
                    continue;
                }

                // 3.Find the max delay length and delay, as often as needed to
                // get ready to run processes again.
                boolean foundReadyProcesses = false;
                while (!timers.isEmpty()) {
                    // Get the first timer, and expire it.
                    Timer authTimer = timers.pollFirst();
                    boolean expired = authTimer.isReady();
                    if (!expired) {
                        simulationTime = authTimer.endTime;
                    }

                    // Awake processes that waited on the timer.
                    if (awakeTimerChoices(authTimer)) {
                        foundReadyProcesses = true;
                        break;
                    }

                    // Time did not progress and no new processes
                    // -> Nothing has changed, expire next timer.
                    if (expired) {
                        continue;
                    }

                    if (findNonblocked()) {
                        foundReadyProcesses = true;
                        break;
                    }
                }
                if (foundReadyProcesses) {
                    continue;
                }

                // 4. No timers to finish any more.
                // => Infinite delay => Done!
                exitReason = endedWithDeadlock() ? ExitReason.DEADLOCKED : ExitReason.FINISHED;
                break;
            }
        } catch (ArithmeticException exc) {
            String msg = "An arithmetic exception occurred.";
            throw new ChiSimulatorException(msg, exc);
        } catch (InvalidInputException exc) {
            String msg = "Invalid input was encountered.";
            throw new ChiSimulatorException(msg, exc);
        } catch (TerminatedException exc) {
            return; // Simulation has ended.
        } finally {
            closeFiles(); // Close all opened files (except stdio).
            simState = SimulationState.FINISHED;
        }

        Assert.check(readyProcesses.isEmpty());
    }

    /**
     * Evaluate the guard of a select choice.
     *
     * @param choice Select choice with the guard to evaluate.
     * @return Value of the guard.
     */
    private boolean evalGuard(SelectChoice choice) {
        if (choice.guardKind == GuardKind.TRUE) {
            return true;
        }
        boolean result;
        try {
            result = choice.getGuard();
        } catch (ChiSimulatorException ex) {
            setTerminateAll(null, ExitReason.ERROR);
            String msg = fmt("Runtime error while executing \"%s\".", choice.getProcess().name);
            throw new ChiSimulatorException(msg, ex);
        }
        return result;
    }

    /**
     * Communicate between two process.
     *
     * @param sendChoice Select choice of the sender.
     * @param recvChoice Select choice of the receiver.
     */
    private void communicate(SelectChoice sendChoice, SelectChoice recvChoice) {
        Object data;
        try {
            data = sendChoice.getSendData();
        } catch (ChiSimulatorException ex) {
            setTerminateAll(null, ExitReason.ERROR);
            String msg = fmt("Runtime error while sending from \"%s\".", sendChoice.getProcess().name);
            throw new ChiSimulatorException(msg, ex);
        }

        try {
            recvChoice.putReceiveData(data);
        } catch (ChiSimulatorException ex) {
            setTerminateAll(null, ExitReason.ERROR);
            String msg = fmt("Runtime error while receiving at \"%s\".", sendChoice.getProcess().name);
            throw new ChiSimulatorException(msg, ex);
        }
    }

    /**
     * Test whether the given select choice is a good choice to serve as starting point for searching the
     * {@link #nocommChoices} and {@link #commChoices}.
     *
     * @param choice Select choice to examine.
     * @return Whether the give select choice is in one of the lists.
     */
    private boolean isGoodChoice(SelectChoice choice) {
        switch (choice.guardKind) {
            case FALSE:
            case CHILDS:
            case TIMER:
                return false;

            case FUNC:
            case TRUE:
                switch (choice.channelKind) {
                    case NONE:
                        return true;

                    case SEND:
                        return false; // Never select a send.

                    case RECEIVE:
                        return choice.channel.getListIndex() >= 0;

                    default:
                        Assert.fail("Unexpected channel kind found.");
                        return false; // Never reached.
                }

            default:
                Assert.fail("Unexpected guard kind found.");
                return false; // Never reached.
        }
    }

    /**
     * Get a select choice in the blocked processes that can serve as starting point for finding a non-blocked process.
     * If no starting point can be found, return {@code null}.
     *
     * @return A select choice to start the search, or {@code null}.
     */
    private SelectChoice getSearchStartingPoint() {
        for (int count = 0; count < 3; count++) {
            BaseProcess proc;
            if (blockedProcesses.size() == 1) {
                proc = blockedProcesses.get(0);
            } else {
                proc = blockedProcesses.get(drawIntegerUniform(selaltGen, 0, blockedProcesses.size()));
            }

            // Find select choice in the process with a non-false guard.
            switch (proc.blockedChoices.size()) {
                case 0:
                    break;

                case 1: {
                    SelectChoice choice = proc.blockedChoices.get(0);
                    if (isGoodChoice(choice)) {
                        return choice;
                    }
                    break;
                }

                default: {
                    SelectChoice choice = proc.blockedChoices
                            .get(drawIntegerUniform(selaltGen, 0, proc.blockedChoices.size()));
                    if (isGoodChoice(choice)) {
                        return choice;
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * Search the select choices without communication for a true guard.
     *
     * @param start First choice to try.
     * @return Whether a ready-to-run process was found.
     */
    private boolean searchNocommChoices(int start) {
        for (int count = 0; count < nocommChoices.size(); count++) {
            SelectChoice choice = nocommChoices.get(start);
            if (evalGuard(choice)) {
                // Guard is true, not blocked on a channel -> it can run!
                BaseProcess proc = choice.getProcess();
                unblockProcess(proc);
                proc.readyChoice = choice;
                proc.blockedChoices = null;
                readyProcesses.add(proc);
                return true;
            }
            start++;
            if (start == nocommChoices.size()) {
                start = 0;
            }
        }
        return false;
    }

    /**
     * Find a partner select choice for process proc with a true guard. Collect all partner choices for future use (in
     * case this search fails). Quit searching when a good partner select choice has been found.
     *
     * @param allOthers The complete list of partner select choices.
     * @param trueOthers The list of partner select choices with a true guard (copied from allOthers in-place after
     *     verifying the guard). Changed in-place, should be empty when the method is called.
     * @param proc Forbidden process to consider as partner process.
     * @return A select choice that can function as partner choice for communicating with proc, or {@code null} if no
     *     such choice can be found.
     */
    private SelectChoice buildOther(RemovableList<SelectChoice> allOthers, List<SelectChoice> trueOthers,
            BaseProcess proc)
    {
        // Construct start index.
        int start;
        if (allOthers.size() == 1) {
            start = 0;
        } else {
            start = drawIntegerUniform(selaltGen, 0, allOthers.size());
        }

        for (int count = 0; count < allOthers.size(); count++) {
            SelectChoice other = allOthers.get(start);
            if (evalGuard(other)) {
                trueOthers.add(other);
                if (other.getProcess() != proc) {
                    return other;
                }
            }
            start++;
            if (start == allOthers.size()) {
                start = 0;
            }
        }
        return null;
    }

    /**
     * Find a partner select choice for process proc.
     *
     * @param trueOthers The list of partner select choices with a true guard.
     * @param proc Forbidden process to consider as partner process.
     * @return A select choice that can function as partner choice for communicating with proc, or {@code null} if no
     *     such choice can be found.
     */
    private SelectChoice findOther(List<SelectChoice> trueOthers, BaseProcess proc) {
        for (SelectChoice other: trueOthers) {
            if (other.getProcess() != proc) {
                return other;
            }
        }
        return null;
    }

    /**
     * Search the channels for a partner of a receive select choice.
     *
     * @param chStart Index of the first channel to try.
     * @param recvStart Index of the first receive choice to try with the first channel.
     * @return Whether communication is possible between (any) two processes.
     */
    private boolean searchReceiverChoices(int chStart, int recvStart) {
        for (int chanCount = 0; chanCount < commChoices.size(); chanCount++) {
            Channel chan = commChoices.get(chStart);
            if (!chan.senders.isEmpty()) {
                // Found a channel, check receivers.
                boolean hasCachedSenders = false;
                for (int rcvCnt = 0; rcvCnt < chan.receivers.size(); rcvCnt++) {
                    SelectChoice recvChoice = chan.receivers.get(recvStart);
                    if (evalGuard(recvChoice)) {
                        BaseProcess recvProc = recvChoice.getProcess();

                        // Found a receiver, find a matching sender.
                        SelectChoice sendChoice;
                        if (!hasCachedSenders) {
                            viableSenders.clear();
                            sendChoice = buildOther(chan.senders, viableSenders, recvProc);
                            hasCachedSenders = true;
                        } else {
                            sendChoice = findOther(viableSenders, recvProc);
                        }

                        if (sendChoice != null) {
                            communicate(sendChoice, recvChoice);

                            // Receiver is ready to run. Added in reverse,
                            // sending process is executed first.
                            unblockProcess(recvProc);
                            recvProc.readyChoice = recvChoice;
                            recvProc.blockedChoices = null;
                            readyProcesses.add(recvProc);

                            // Sender is ready to run.
                            BaseProcess sendProc = sendChoice.getProcess();
                            unblockProcess(sendProc);
                            sendProc.readyChoice = sendChoice;
                            sendProc.blockedChoices = null;
                            readyProcesses.add(sendProc);
                            return true;
                        }
                    }
                    recvStart++;
                    if (recvStart == chan.receivers.size()) {
                        recvStart = 0;
                    }
                }
            }
            recvStart = 0;
            chStart++;
            if (chStart == commChoices.size()) {
                chStart = 0;
            }
        }
        return false;
    }

    /**
     * Walk through the {@link #runChoices} list, and awake any process that was blocked on (now) terminated children.
     *
     * <p>
     * To reduce the number of times processing the {@link #runChoices} list, this method walks through the entire list,
     * awaking every process that has become ready to run.
     * </p>
     *
     * @return Whether any process was awoken.
     */
    private boolean searchRunchoices() {
        boolean foundRunnable = false;
        int idx = 0;
        while (idx < runChoices.size()) {
            // Actually this should be a SelectWaitRunChilds object, but this
            // information is not needed in runtime.
            SelectChoice choice = runChoices.get(idx);
            // Note that the SelectWaitRunChilds.getGuard safety is used here.
            if (!choice.getGuard()) {
                idx++;
                continue;
            }

            // All children have terminated -> ready to run.
            //
            // Blocked on child processes is a guard notion. Currently, the
            // select choice does not block on anything else. In theory, one
            // could add other guards, or block on communication as well.
            BaseProcess proc = choice.getProcess();

            // The select choice at 'idx' is being freed by 'unblockProcess',
            // so 1 is expected as number of freed select choices. In that
            // case, the 'runChoices' list replaces the 'idx' entry with a new
            // entry, so no need to advance 'idx'. If there are more, they
            // could be run choices already checked. To be safe, check
            // everything again.
            if (unblockProcess(proc) > 1) {
                idx = 0;
            }

            proc.readyChoice = choice;
            proc.blockedChoices = null;
            readyProcesses.add(proc);
            foundRunnable = true;
        }
        return foundRunnable;
    }

    /**
     * Try to find a non-blocked process.
     *
     * @return {@code true} if a non-blocked process has been found, else {@code false}.
     */
    private boolean findNonblocked() {
        if (blockedProcesses.isEmpty()) {
            return false;
        }

        // Child processes typically stay blocked until the end, not much use
        // to add runChoices to the random selection, or to search it every
        // time. Therefore, it is done as high priority case whenever a
        // process terminated. This may result in having several new processes
        // ready-to-run without randomizing the order. However, the restarted
        // processes may provide more select choices later on.
        if (inspectRunChoices) {
            inspectRunChoices = false;
            if (searchRunchoices()) {
                return true;
            }
        }

        SelectChoice choice = getSearchStartingPoint();
        if (choice == null) {
            return searchNocommChoices(0) || searchReceiverChoices(0, 0);
        }

        switch (choice.channelKind) {
            case NONE:
                return searchNocommChoices(choice.getListIndex()) || searchReceiverChoices(0, 0);

            case RECEIVE:
                return searchReceiverChoices(choice.channel.getListIndex(), choice.getListIndex())
                        || searchNocommChoices(0);

            case SEND:
            default:
                Assert.fail(fmt("Unexpected channel kind encountered: %s.", choice.channelKind));
                return false; // Never reached.
        }
    }
}
