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

package org.eclipse.escet.common.eclipse.ui;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.java.Assert;

/**
 * Base class for multiple files pop-up menu command handlers, which execute application framework applications. Note
 * that this handler must be coupled to a menu contribution that is only enabled when at least one file is available.
 *
 * <p>
 * The files on which to execute the application are obtained from the active menu selection or the active editor input.
 * As such, this handler must be coupled to a menu contribution that is only enabled when a pop-up menu is shown for a
 * project/package explorer file, or an editor for a compatible file type.
 * </p>
 *
 * @see Application
 */
public abstract class MultiFileCommandHandler extends BaseFileCommandHandler {
    @Override
    protected void checkFileCount(List<IFile> files) {
        Assert.check(files.size() >= 1);
    }
}
