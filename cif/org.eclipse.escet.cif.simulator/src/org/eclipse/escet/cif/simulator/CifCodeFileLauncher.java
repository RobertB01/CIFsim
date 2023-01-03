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

package org.eclipse.escet.cif.simulator;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorLauncher;

/** Eclipse UI launcher for '.cifcode' files. */
public class CifCodeFileLauncher implements IEditorLauncher {
    @Override
    public void open(IPath file) {
        // Store given file path in the execution event.
        Map<String, String> params = map();
        params.put(IPath.class.getName(), file.toString());
        ExecutionEvent event = new ExecutionEvent(null, params, null, null);

        // Construct handler and let it handler the launch.
        CifSimulatorCommandHandler handler = new CifSimulatorCommandHandler();
        try {
            handler.execute(event);
        } catch (ExecutionException ex) {
            Class<?> cls = handler.getApplicationClass();
            String msg = "Execution of application failed: " + cls;
            throw new RuntimeException(msg, ex);
        }
    }
}
