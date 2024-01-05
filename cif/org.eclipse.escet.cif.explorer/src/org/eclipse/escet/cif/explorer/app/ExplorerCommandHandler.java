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

package org.eclipse.escet.cif.explorer.app;

import org.eclipse.core.resources.IFile;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.eclipse.ui.SingleFileCommandHandler;

/** Handler for Eclipse UI command for untimed CIF exploration. */
public class ExplorerCommandHandler extends SingleFileCommandHandler {
    @Override
    protected String[] getCommandLineArgs(IFile file) {
        return new String[] {getFileName(file), "--option-dialog=yes"};
    }

    @Override
    protected Class<? extends Application<?>> getApplicationClass() {
        return ExplorerApplication.class;
    }
}
