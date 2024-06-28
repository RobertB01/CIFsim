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

package org.eclipse.escet.common.box;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.java.PathPair;
import org.eclipse.escet.common.java.exceptions.InputOutputException;

/** Box base class. */
public abstract class Box {
    /**
     * Converts the box contents to lines of text.
     *
     * @return The contents of the box as lines of text.
     */
    public abstract List<String> getLines();

    /**
     * Writes the box contents to a stream. Each line is terminated by a new line.
     *
     * @param stream The stream to write to.
     */
    public void write(AppStream stream) {
        for (String line: getLines()) {
            line = StringUtils.stripEnd(line, null);
            stream.println(line);
        }
    }

    /**
     * Writes the box contents to a file.
     *
     * @param pathPair The relative or absolute local file system path and the absolute local file system path to the
     *     file to write the output to.
     * @throws InputOutputException If writing to the file failed.
     */
    public void writeToFile(PathPair pathPair) {
        writeToFile(pathPair.userPath, pathPair.systemPath);
    }

    /**
     * Writes the box contents to a file.
     *
     * @param relPath The relative or absolute local file system path to the file to write the output to.
     * @param absPath The absolute local file system path to the file to write the output to.
     * @throws InputOutputException If writing to the file failed.
     */
    public void writeToFile(String relPath, String absPath) {
        AppStream stream = new FileAppStream(relPath, absPath);

        InputOutputException ex = null;
        try {
            write(stream);
        } catch (InputOutputException e) {
            ex = e;
        } finally {
            try {
                stream.close();
            } catch (InputOutputException e) {
                if (ex == null) {
                    ex = e;
                }
            }
        }

        if (ex != null) {
            throw ex;
        }
    }

    /**
     * Converts the box contents to a {@link StringBuilder}. Each line is terminated by a new line, except for the last
     * line.
     *
     * @return The textual representation of the box in a {@link StringBuilder}.
     */
    public StringBuilder toBuilder() {
        StringBuilder rslt = new StringBuilder();
        List<String> lines = getLines();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) {
                rslt.append("\n");
            }
            String line = lines.get(i);
            rslt.append(StringUtils.stripEnd(line, null));
        }
        return rslt;
    }

    /**
     * Converts the box contents to a {@link String}. Each line is terminated by a new line, except for the last line.
     */
    @Override
    public String toString() {
        return toBuilder().toString();
    }
}
