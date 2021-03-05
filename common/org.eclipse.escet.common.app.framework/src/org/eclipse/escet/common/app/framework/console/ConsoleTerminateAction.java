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

package org.eclipse.escet.common.app.framework.console;

import org.eclipse.escet.common.app.framework.Activator;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.jface.action.Action;

/** Console terminate action that can be used to terminate the running {@link Application}. */
public class ConsoleTerminateAction extends Action {
    /** The console related to this action. */
    private final Console console;

    /**
     * Constructor for the {@link ConsoleTerminateAction} class.
     *
     * @param console The console related to this action.
     */
    public ConsoleTerminateAction(Console console) {
        super("Terminate");
        this.console = console;
        setToolTipText("Terminate");
        setImageDescriptor(Activator.getImageDescriptor("icons/terminate_button.png"));
    }

    @Override
    public void run() {
        Application<?> application = console.getApplication();
        if (application != null) {
            application.terminate();
        }
    }
}
