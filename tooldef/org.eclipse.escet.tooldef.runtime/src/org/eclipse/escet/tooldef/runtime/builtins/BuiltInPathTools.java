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

package org.eclipse.escet.tooldef.runtime.builtins;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.tooldef.runtime.ToolDefException;

/** ToolDef built-in path tools. */
public class BuiltInPathTools {
    /** Constructor for the {@link BuiltInPathTools} class. */
    private BuiltInPathTools() {
        // Static class.
    }

    /**
     * Returns an absolute local file system path, given an absolute or relative local file system path. Relative paths
     * are resolved against the {@link #curdir current working directory}.
     *
     * @param path The absolute or relative local file system path. May contain both {@code "\"} and {@code "/"} as file
     *     separators.
     * @return The absolute local file system path. The path contains file separators for the current platform.
     * @see #curdir
     */
    public static String abspath(String path) {
        return abspath(path, curdir());
    }

    /**
     * Returns an absolute local file system path, given an absolute or relative local file system path. Relative paths
     * are resolved against a given working directory.
     *
     * @param path The absolute or relative local file system path. May contain both {@code "\"} and {@code "/"} as file
     *     separators.
     * @param workdir The absolute local file system path of the working directory against which to resolve relative
     *     paths. May contain both {@code "\"} and {@code "/"} as file separators.
     * @return The absolute local file system path. The path contains file separators for the current platform.
     * @see #curdir
     */
    public static String abspath(String path, String workdir) {
        return Paths.resolve(path, workdir);
    }

    /**
     * Returns the base name of the given absolute or relative local file system path. The base name of a file or
     * directory is the name of that file or directory. In other words, returns the last part of the given path.
     *
     * @param path The absolute or relative local file system path to the file or directory for which to return the base
     *     name. May contain both {@code "\"} and {@code "/"} as file separators. Must not end with {@code "\"} or
     *     {@code "/"}.
     * @return The base name.
     * @throws ToolDefException If the path ends with {@code "\"} or {@code "/"}.
     */
    public static String basename(String path) {
        // Check for directory ending with file separator.
        if (path.endsWith("\\") || path.endsWith("/")) {
            String msg = fmt("Failed to get base name: path \"%s\" ends with \"\\\" or \"/\".", path);
            throw new ToolDefException(msg);
        }

        // Return the base name.
        return Paths.getFileName(path);
    }

    /**
     * Changes the {@link #curdir current working directory} to the directory referred to by the given path.
     *
     * @param path The absolute or relative local file system path to the new current working directory. Relative paths
     *     are resolved against the {@link #curdir current working directory}. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @throws ToolDefException If the given path does not exist, refers to a file rather than a directory, or it could
     *     not be determined whether the path refers to a file or a directory.
     */
    public static void chdir(String path) {
        // Get new working directory.
        String absPath = abspath(path, curdir());
        Path workdir = java.nio.file.Paths.get(absPath);

        // Check new working directory.
        if (!Files.exists(workdir)) {
            String msg = fmt("Failed to change current working directory: path \"%s\" does not exist.", path);
            throw new ToolDefException(msg);
        }
        if (!Files.isDirectory(workdir)) {
            String msg = fmt("Failed to change current working directory: path \"%s\" is not a directory.", path);
            throw new ToolDefException(msg);
        }

        // Set new working directory.
        Paths.setCurWorkingDir(absPath);
    }

    /**
     * Modifies a path such that it ends with a new extension, removing an old extension if it exists.
     *
     * @param path The absolute or relative local file system path to modify. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param oldext The old extension that can be removed (case insensitive, no "." at the start). Use {@code null} to
     *     not remove an old extension.
     * @param newext The new extension to use (case sensitive, no "." at the start). Use {@code null} to not add a new
     *     extension.
     * @return The changed path, ending with the new extension (if any).
     */
    public static String chfileext(String path, String oldext, String newext) {
        return Paths.pathChangeExtension(path, oldext, newext);
    }

    /**
     * Returns the script execution's current working directory, as an absolute local file system path. The path
     * contains file separators for the current platform.
     *
     * @return The script execution's current working directory.
     */
    public static String curdir() {
        return Paths.getCurWorkingDir();
    }

    /**
     * Returns the absolute directory path of the directory that contains the given file or directory.
     *
     * @param path The absolute local file system path that refers to a file or directory. May contain both {@code "\"}
     *     and {@code "/"} as file separators. Must not end with {@code "\"} or {@code "/"}.
     * @return The absolute directory path of the directory that contains the given file or directory. May contain both
     *     {@code "\"} and {@code "/"} as file separators.
     * @throws ToolDefException If the given path is not an absolute local file system path, or if the paths ends with
     *     {@code "\"} or {@code "/"}.
     */
    public static String dirname(String path) {
        // Check for absolute local file system path.
        if (!path.contains("\\") && !path.contains("/")) {
            String msg = fmt("Failed to get directory name: path \"%s\" is not an absolute local file system path.",
                    path);
            throw new ToolDefException(msg);
        }

        // Check for directory ending with file separator.
        if (path.endsWith("\\") || path.endsWith("/")) {
            String msg = fmt("Failed to get directory name: path \"%s\" ends with \"\\\" or \"/\".", path);
            throw new ToolDefException(msg);
        }

        // Return the directory name.
        return Paths.getAbsFilePathDir(path);
    }

    /**
     * Returns the file extension of the given file, or {@code ""} if the file has no file extension.
     *
     * @param path The absolute or relative local file system path to the file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @return The file extension, or {@code ""}.
     */
    public static String fileext(String path) {
        return Paths.getExtension(path);
    }

    /**
     * Does the given file have the given file extension?
     *
     * @param path The absolute or relative local file system path to the file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param ext The file extension to check for (case sensitive, no "." at the start).
     * @return {@code true} if the file has the given file extension, {@code false} otherwise.
     */
    public static boolean hasfileext(String path, String ext) {
        String realExt = fileext(path).toLowerCase(Locale.US);
        return realExt.equals(ext.toLowerCase(Locale.US));
    }

    /**
     * Joins paths together. If no paths are given, an empty string is returned. If one path is given, the path is
     * returned.
     *
     * @param paths The paths to join together. The first path may be an absolute or relative local file system path.
     *     The remaining paths must be relative local file system paths. All paths may contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @return The joined path. The path contains file separators for the current platform.
     */
    public static String pathjoin(List<String> paths) {
        if (paths.size() == 0) {
            return "";
        }
        if (paths.size() == 1) {
            return paths.get(0);
        }
        return Paths.join(paths.toArray(new String[paths.size()]));
    }

    /**
     * Returns the absolute local file system path to the script being executed. The path contains file separators for
     * the current platform.
     *
     * @return The absolute local file system path to the script being executed.
     */
    public static String scriptpath() {
        final String propName = "org.eclipse.escet.tooldef.interpreter.scriptpath";
        return AppEnv.getProperty(propName);
    }
}
