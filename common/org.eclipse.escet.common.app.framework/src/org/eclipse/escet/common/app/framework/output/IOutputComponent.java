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

package org.eclipse.escet.common.app.framework.output;

/**
 * Interface for all output components. Derived interfaces may add application specific events. All CIF simulator output
 * components should implement this interface. This includes for instance, visualizers, debuggers, and loggers.
 *
 * <p>
 * The output format of output components is explicitly left to implementations, to allow console output, file output,
 * graphical window output, etc.
 * </p>
 */
public interface IOutputComponent {
    /**
     * Process debug output.
     *
     * @param msg The debug output message.
     * @param indent The indentation level.
     */
    public void dbg(String msg, int indent);

    /**
     * Process 'normal' output.
     *
     * @param msg The 'normal' output message.
     * @param indent The indentation level.
     */
    public void out(String msg, int indent);

    /**
     * Process warning output.
     *
     * @param msg The warning message.
     * @param indent The indentation level.
     */
    public void warn(String msg, int indent);

    /**
     * Process fatal error output.
     *
     * @param msg The fatal error message.
     */
    public void err(String msg);

    /**
     * Handler for the initialization event. As part of registration of the output component with the application's
     * output provider, the output component will be asked to initialize itself. It may be assumed that option
     * processing has completed.
     */
    public void initialize();

    /**
     * Handler for the cleanup event. Just before termination of the application, all output components will be asked to
     * cleanup after themselves. They should release all their resources.
     *
     * <p>
     * A {@link StreamOutputComponent} for console output is always present as first output component, unless output
     * components were prepended rather than appended, during {@link OutputProvider#register(IOutputComponent, boolean)
     * registration}). Since components are cleaned up in reverse order of registration (once again, assuming only
     * appending), it may be assumed that the {@link StreamOutputComponent} has not yet been cleaned up, assuming the
     * output component being cleaned up has been appended rather than prepended, during registration.
     * </p>
     *
     * <p>
     * It is allowed to throw exceptions after cleanup, but cleanup should still be fully performed. That is, if an
     * exception occurs during cleanup, you should continue to clean up as much as possible. Once cleanup has been
     * completed, re-throw the first that occurred. The application framework will ensure that if exceptions are throw
     * from the {@link #cleanup} method, the remaining output components will still be cleaned up.
     * </p>
     *
     * <p>
     * Cleanup should be allowed to be performed multiple times.
     * </p>
     *
     * <p>
     * This method may only be called by the {@link OutputProvider}, to ensure that exceptions are properly reported,
     * and the remaining output components are still cleaned up if exceptions are thrown.
     * </p>
     */
    public void cleanup();
}
