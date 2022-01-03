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

package org.eclipse.escet.common.eclipse.ui;

import java.io.File;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/** File path input for the {@link ControlEditor}. */
public class FilePathEditorInput implements IEditorInput {
    /** The absolute local file system path to the input file. */
    private final String absFilePath;

    /**
     * Constructor for the {@link FilePathEditorInput} class.
     *
     * @param absFilePath The path to the input file. Must be an absolute local file system path.
     */
    public FilePathEditorInput(String absFilePath) {
        this.absFilePath = absFilePath;
        Assert.notNull(absFilePath);
        Assert.check(new File(absFilePath).isAbsolute());
    }

    /**
     * Returns the absolute local path to the input file.
     *
     * @return The absolute local path to the input file.
     */
    public String getAbsoluteFilePath() {
        return absFilePath;
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        return null;
    }

    @Override
    public boolean exists() {
        return new File(absFilePath).exists();
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return ImageDescriptor.getMissingImageDescriptor();
    }

    @Override
    public String getName() {
        return absFilePath;
    }

    @Override
    public String getToolTipText() {
        return absFilePath;
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }
}
