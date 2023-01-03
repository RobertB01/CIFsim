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

package org.eclipse.escet.common.app.framework.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;

/** {@link AppStream} that writes to a file. */
public class FileAppStream extends AppStream {
    /** The absolute or relative local file system path to the file to which to write the output. */
    private final String relPath;

    /** The underlying stream to use. */
    private final OutputStream stream;

    /**
     * Constructor for the {@link FileAppStream} class. Overwrites the file if it already exists.
     *
     * @param path The absolute or relative local file system path to the file to which to write the output. The
     *     absolute path is resolved using {@link Paths#resolve}.
     * @throws InputOutputException If the file exists but is a directory rather than a regular file, does not exist but
     *     cannot be created, or cannot be opened for any other reason.
     */
    public FileAppStream(String path) {
        this(path, false);
    }

    /**
     * Constructor for the {@link FileAppStream} class. Overwrites the file if it already exists.
     *
     * @param relPath The absolute or relative local file system path to the file to which to write the output.
     * @param absPath The absolute local file system path to the file to which to write the output.
     * @throws InputOutputException If the file exists but is a directory rather than a regular file, does not exist but
     *     cannot be created, or cannot be opened for any other reason.
     */
    public FileAppStream(String relPath, String absPath) {
        this(relPath, absPath, false);
    }

    /**
     * Constructor for the {@link FileAppStream} class.
     *
     * @param path The absolute or relative local file system path to the file to which to write the output. The
     *     absolute path is resolved using {@link Paths#resolve}.
     * @param append Whether to append to the file if it already exists ({@code true}) or overwrite it if it already
     *     exists ({@code false}).
     * @throws InputOutputException If the file exists but is a directory rather than a regular file, does not exist but
     *     cannot be created, or cannot be opened for any other reason.
     */
    public FileAppStream(String path, boolean append) {
        this(path, Paths.resolve(path), append);
    }

    /**
     * Constructor for the {@link FileAppStream} class.
     *
     * @param relPath The absolute or relative local file system path to the file to which to write the output.
     * @param absPath The absolute local file system path to the file to which to write the output.
     * @param append Whether to append to the file if it already exists ({@code true}) or overwrite it if it already
     *     exists ({@code false}).
     * @throws InputOutputException If the file exists but is a directory rather than a regular file, does not exist but
     *     cannot be created, or cannot be opened for any other reason.
     */
    public FileAppStream(String relPath, String absPath, boolean append) {
        this.relPath = relPath;

        FileOutputStream fileStream;
        try {
            fileStream = new FileOutputStream(absPath, append);
        } catch (FileNotFoundException ex) {
            String msg = fmt("Failed to open file \"%s\" for writing, as it already exists as a directory, "
                    + "can not be created, or can not be opened for writing.", relPath);
            throw new InputOutputException(msg, ex);
        }

        this.stream = new BufferedOutputStream(fileStream);
    }

    @Override
    protected void writeImpl(byte b) {
        try {
            stream.write(b);
        } catch (IOException ex) {
            String msg = fmt("Failed to write to file \"%s\".", relPath);
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void writeImpl(byte[] bytes, int offset, int length) {
        try {
            stream.write(bytes, offset, length);
        } catch (IOException ex) {
            String msg = fmt("Failed to write to file \"%s\".", relPath);
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void flushImpl() {
        try {
            stream.flush();
        } catch (IOException ex) {
            String msg = fmt("Failed to flush file \"%s\".", relPath);
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void closeImpl() {
        try {
            stream.close();
        } catch (IOException ex) {
            String msg = fmt("Failed to close file \"%s\".", relPath);
            throw new InputOutputException(msg, ex);
        }
    }
}
