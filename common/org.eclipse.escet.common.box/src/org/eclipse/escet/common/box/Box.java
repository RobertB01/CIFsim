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

package org.eclipse.escet.common.box;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;

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
     * @param absPath The absolute local file system path to the file to write the output to.
     * @throws InputOutputException If writing to the file failed.
     */
    public void writeToFile(String absPath) {
        AppStream stream = new FileAppStream(absPath, absPath);

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
