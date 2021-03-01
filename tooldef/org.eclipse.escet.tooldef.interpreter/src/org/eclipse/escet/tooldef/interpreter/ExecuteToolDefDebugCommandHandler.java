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

package org.eclipse.escet.tooldef.interpreter;

import org.eclipse.core.resources.IFile;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.eclipse.ui.SingleFileCommandHandler;

/** Handler for the default Eclipse UI command for the ToolDef interpreter. */
public class ExecuteToolDefDebugCommandHandler extends SingleFileCommandHandler {
    @Override
    protected Class<? extends Application<?>> getApplicationClass() {
        return ToolDefInterpreterApp.class;
    }

    @Override
    protected String[] getCommandLineArgs(IFile file) {
        return new String[] {getFileName(file), "--option-dialog=yes"};
    }
}
