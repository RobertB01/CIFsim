//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.eclipse.ui;

import static org.eclipse.escet.common.java.Lists.first;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.java.Assert;

/**
 * Base class for single file pop-up menu command handlers, which execute application framework applications. Note that
 * this handler must be coupled to a menu contribution that is only enabled when exactly one file is available.
 *
 * <p>
 * The file on which to execute the application is obtained from the active menu selection or the active editor input.
 * As such, this handler must be coupled to a menu contribution that is only enabled when a pop-up menu is shown for a
 * project/package explorer file, or an editor for a compatible file type.
 * </p>
 *
 * @see Application
 */
public abstract class SingleFileCommandHandler extends BaseFileCommandHandler {
    /**
     * Returns the command line arguments for the application.
     *
     * <p>
     * An example of an implementation of this method could be:
     *
     * <pre>
     *   return new String[] {getFileName(file), "--option-dialog=yes"};
     * </pre>
     * </p>
     *
     * @param file The file that this action operates on.
     * @return The command line arguments for the application.
     */
    protected abstract String[] getCommandLineArgs(IFile file);

    @Override
    protected String[] getCommandLineArgs(List<IFile> files, String workingDir) {
        return getCommandLineArgs(first(files));
    }

    @Override
    protected void checkFileCount(List<IFile> files) {
        Assert.check(files.size() == 1);
    }
}
