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

package org.eclipse.escet.chi.runtime;

import static org.eclipse.escet.common.java.Assert.check;

import org.eclipse.escet.chi.runtime.data.BaseProcess;
import org.eclipse.escet.chi.runtime.data.Channel;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.removablelist.RemovableElement;

/**
 * Class to express which code alternative is to be executed after making a choice in a select statement.
 *
 * <p>
 * The {@link #choice} value indicates which blob of code should be executed (which is statically determined). Before
 * starting the execution, {@link SelectChoice#setVariables} is executed to assign specific values to variables of the
 * process. The default implementation (shown here) does not do anything, but derived classes may perform such actions.
 * </p>
 *
 * <p>
 * To decide: This class may also be useful for storing the guard, channel, and end/receive information.
 * </p>
 *
 * <p>
 * For the future, it may be useful to denote when the guard should be re-evaluated in this class too, to reduce the
 * amount of computations performed on it.
 * </p>
 */
public abstract class SelectChoice extends RemovableElement<SelectChoice> {
    /** Coordinator of the choice. */
    protected final ChiCoordinator chiCoordinator;

    /** Code alternative chosen in the select. */
    public final int choice;

    /** Kind of guard expression. */
    public final GuardKind guardKind;

    /** Channel to communicate with, {@code null} means the alternative has no communication requirement. */
    public final Channel channel;

    /** Kind of communication channel. */
    public final ChannelKind channelKind;

    /**
     * Retrieve the process owning this choice.
     *
     * @return The process owning this choice.
     */
    public abstract BaseProcess getProcess();

    /**
     * Choice in a select/send/receive/delay/run statement.
     *
     * @param chiCoordinator Coordinator of the Chi simulation.
     * @param guardKind Kind of guard expression.
     * @param channelKind Kind of communication channel.
     * @param channel Channel to communicate with, {@code null} means the alternative has no communication requirement.
     * @param choice Unique number to identify the blob of code to execute.
     */
    public SelectChoice(ChiCoordinator chiCoordinator, GuardKind guardKind, ChannelKind channelKind, Channel channel,
            int choice)
    {
        this.chiCoordinator = chiCoordinator;
        this.guardKind = guardKind;
        this.channelKind = channelKind;
        this.channel = channel;
        this.choice = choice;
    }

    /**
     * Get the choice.
     *
     * @return Choice number.
     */
    public int getChoice() {
        return choice;
    }

    /** Set local variables to their values that were used while constructing the choice. */
    public void setVariables() {
        // Default implementation does nothing.
    }

    /**
     * Compute the value of the guard for this select choice. May only be called when {@link #guardKind} is
     * {@link GuardKind#FUNC}, {@link GuardKind#TIMER}, or {@link GuardKind#CHILDS}. Method should be overloaded in
     * those cases.
     *
     * @return Value of the guard at this moment in the simulation.
     */
    public boolean getGuard() {
        Assert.fail("Guard cannot be evaluated.");
        return false;
    }

    /**
     * Get the value of the data being sent over the channel. Function is not called for synchronization channels.
     *
     * @return Value of the data being sent.
     */
    public Object getSendData() {
        check(false); // Should not be called.
        return null;
    }

    /**
     * Put the data being sent over the channel into the receiving process. Function is not called for synchronization
     * channels.
     *
     * @param obj Value of the data being sent.
     */
    public void putReceiveData(Object obj) {
        check(false); // Should not be called.
    }

    /**
     * Kind of guard of the select choice.
     *
     * <p>
     * The {@link #TIMER} and {@link #CHILDS} values currently imply being blocked <em>only</em> on timers or child
     * processes. There are no other guards and no channel (that is, it has {@link ChannelKind#NONE}).
     * </p>
     */
    public enum GuardKind {
        /** Guard holds. */
        TRUE,

        /** Guard does not hold. */
        FALSE,

        /** Guard needs computing. */
        FUNC,

        /** Guard needs computing, and has timers. */
        TIMER,

        /** Guard needs computing, and has child processes. */
        CHILDS;
    }

    /** Kind of communication channel. */
    public enum ChannelKind {
        /** No channel available. */
        NONE,

        /** Send channel. */
        SEND,

        /** Receive channel. */
        RECEIVE;
    }
}
