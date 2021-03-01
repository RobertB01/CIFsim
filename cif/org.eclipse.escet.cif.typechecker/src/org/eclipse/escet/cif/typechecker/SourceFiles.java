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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.escet.common.java.Assert;

/** A collection of source files. */
public class SourceFiles {
    /** Queue of unprocessed source files. Is modified in-place. */
    private final Queue<SourceFile> queue = new ArrayDeque<>();

    /**
     * Mapping from the {@link SourceFile#absPath absolute paths} of the source files to their metadata. Is modified
     * in-place.
     */
    private final Map<String, SourceFile> filesMap = mapc(1);

    /**
     * Returns the source file metadata for the source file with the given {@link SourceFile#absPath absolute path}, or
     * {@code null} if it doesn't exist in the collection.
     *
     * @param absPath The {@link SourceFile#absPath absolute path} of the source file.
     * @return The source file metadata or {@code null}.
     */
    public SourceFile get(String absPath) {
        return filesMap.get(absPath);
    }

    /**
     * Adds a new source file to the collection.
     *
     * @param sourceFile The source file metadata of the source file to add.
     */
    public void add(SourceFile sourceFile) {
        queue.add(sourceFile);
        SourceFile previousFile = filesMap.put(sourceFile.absPath, sourceFile);
        Assert.check(previousFile == null);
    }

    /**
     * Removes a new source file to the collection. Only used for post type checking use, as source files must be added
     * and removed to clean up the state of the type checker, without actually checking the file.
     *
     * @param sourceFile The source file metadata of the source file to remove.
     */
    public void remove(SourceFile sourceFile) {
        queue.remove(sourceFile);
        SourceFile previousFile = filesMap.remove(sourceFile.absPath);
        Assert.check(previousFile == sourceFile);
    }

    /**
     * Drains the internal queue of unprocessed source files into a freshly created list, and returns that list. After
     * this method returns, the internal queue is thus empty.
     *
     * @return The unprocessed source files.
     */
    public List<SourceFile> drainQueue() {
        List<SourceFile> rslt = listc(queue.size());
        for (SourceFile sourceFile: queue) {
            rslt.add(sourceFile);
        }
        queue.clear();
        return rslt;
    }
}
