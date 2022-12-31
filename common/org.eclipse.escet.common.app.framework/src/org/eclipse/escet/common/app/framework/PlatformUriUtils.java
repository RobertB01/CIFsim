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

package org.eclipse.escet.common.app.framework;

import java.nio.file.Files;
import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.escet.common.java.Assert;

/**
 * Platform URI utility methods, providing purely syntactic path manipulation. Supports both local file system paths and
 * Eclipse platform URIs.
 */
public class PlatformUriUtils {
    /** Constructor for the {@link PlatformUriUtils} class. */
    private PlatformUriUtils() {
        // Static class.
    }

    /**
     * Is the given path an Eclipse platform URI?
     *
     * @param path The path. May be an absolute or relative local file system path, with both {@code \} and {@code /} as
     *     file separators. May also be an Eclipse platform URI.
     * @return {@code true} if the path is an Eclipse platform URI, {@code false} otherwise.
     */
    public static boolean isPlatformUri(String path) {
        return path.startsWith("platform:/");
    }

    /**
     * Is the given path absolute, rather than relative?
     *
     * @param path The absolute or relative path. May be an absolute or relative local file system path, with both
     *     {@code \} and {@code /} as file separators. May also be an Eclipse platform URI.
     * @return {@code true} if the path is an absolute path, {@code false} if it is relative.
     */
    public static boolean isAbsolute(String path) {
        return isPlatformUri(path) || Paths.isAbsolute(path);
    }

    /**
     * Does the given path exist?
     *
     * @param absPath The absolute path. May be an absolute local file system path, with both {@code \} and {@code /} as
     *     file separators. May also be an Eclipse platform URI.
     * @return {@code true} if the path exists, {@code false} if it doesn't exist or it could not be determined whether
     *     the path exists.
     */
    public static boolean exists(String absPath) {
        if (isPlatformUri(absPath)) {
            // Parse Eclipse platform URI.
            URI uri;
            try {
                uri = URI.createURI(absPath);
            } catch (IllegalArgumentException ex) {
                return false;
            }

            // Check existence.
            return URIConverter.INSTANCE.exists(uri, null);
        } else {
            // Absolute local file system path.
            return Files.exists(java.nio.file.Paths.get(absPath));
        }
    }

    /**
     * Resolves a path, relative to the given directory.
     *
     * <p>
     * This method operates syntactically on paths. It also does not require the files/directories to actually exist.
     * </p>
     *
     * @param path The path to resolve. May be an absolute or relative local file system path, with both {@code \} and
     *     {@code /} as file separators. May also be an Eclipse platform URI.
     * @param absDir The absolute directory path against which to resolve the given path. May be an absolute local file
     *     system path, with both {@code \} and {@code /} as file separators. May also be an Eclipse platform URI.
     * @return The absolute path that results from the resolving. May be an absolute local file system path, with only
     *     platform specific file separators. May also be an Eclipse platform URI. Is normalized (i.e. does not contain
     *     {@code "."} or {@code ".."} parts).
     */
    public static String resolve(String path, String absDir) {
        // If the path is already absolute, there is no need to resolve it
        // against the given directory.
        if (isAbsolute(path)) {
            // Normalize the path.
            if (isPlatformUri(path)) {
                return normalizePlatformUri(path);
            }
            return Paths.resolve(path, absDir);
        }

        // Not an absolute path, so it must be a relative path.
        if (isPlatformUri(absDir)) {
            // Directory against which to resolve is specified using an Eclipse
            // platform URI. Imported path is a relative path. Resolve it
            // relative within the platform location of the directory against
            // which to resolve.

            // Split platform URI into parts.
            String[] parts = splitPlatformUri(absDir);
            String platformPath = parts[2];

            // Resolve relative path, within platform location.
            platformPath = Paths.resolve(path, platformPath);
            platformPath = platformPath.replace('\\', '/');

            // Combine parts again to form an Eclipse platform URI.
            parts[2] = platformPath;
            return combinePlatformUri(parts);
        } else {
            // Resolve relative local file system path against absolute local
            // file system path.
            return Paths.resolve(path, absDir);
        }
    }

    /**
     * Returns a path to the given target file or directory, relative to the given directory. A relative path is
     * returned, unless it doesn't exist, in which case an absolute path is returned instead. An example of the latter
     * is Windows paths with different drive prefixes.
     *
     * <p>
     * This method operates syntactically on paths. It also does not require the files/directories to actually exist.
     * </p>
     *
     * @param absTgtPath The absolute path to the target file or directory. This path is turned into a relative path.
     *     May be an absolute local file system path, with both {@code \} and {@code /} as file separators. May also be
     *     an Eclipse platform URI. Must be normalized (i.e. must not contain {@code "."} or {@code ".."} parts).
     * @param absRelDir The absolute path to the directory against which to generate a relative path. The resulting path
     *     is relative to this directory, if possible. May be an absolute local file system path, with both {@code \}
     *     and {@code /} as file separators. May also be an Eclipse platform URI. Must be normalized (i.e. must not
     *     contain {@code "."} or {@code ".."} parts).
     * @return A path to the given target file or directory, relative to the given directory if possible, and an
     *     absolute path otherwise. May be an absolute or relative local file system path, or an Eclipse platform URI.
     *     The result uses {@code /} as file separator, regardless of the file separators used in the input.
     */
    public static String getRelativePath(String absTgtPath, String absRelDir) {
        if (isPlatformUri(absTgtPath)) {
            // Target path is an Eclipse platform URI.
            if (isPlatformUri(absRelDir)) {
                // Both paths are Eclipse platform URIs.

                // Split Eclipse platform URIs into parts.
                String[] tgtParts = splitPlatformUri(absTgtPath);
                String[] relParts = splitPlatformUri(absRelDir);

                // If in same platform location, make it relative.
                if (Objects.equals(tgtParts[0], tgtParts[0]) && Objects.equals(tgtParts[1], tgtParts[1])) {
                    // Use relativized path, within platform location.
                    String p = Paths.getRelativePath(tgtParts[2], relParts[2]);
                    p = p.replace('\\', '/');
                    return p;
                }
            }

            // Can't relativize. Normalize target Eclipse platform URI again,
            // just in case.
            return normalizePlatformUri(absTgtPath);
        } else if (isPlatformUri(absRelDir)) {
            // Directory against which to resolve is an Eclipse platform URI.
            // Target path is an absolute local file system path. Can't
            // relativize.

            // Return target path. Normalize again, just in case.
            return Paths.join(absTgtPath, ".");
        } else {
            // Neither is an Eclipse platform URI, so both must be local file
            // system paths.
            return Paths.getRelativePath(absTgtPath, absRelDir);
        }
    }

    /**
     * Normalizes an Eclipse platform URI.
     *
     * @param uri The Eclipse platform URI.
     * @return The normalized Eclipse platform URI.
     */
    public static String normalizePlatformUri(String uri) {
        String[] parts = splitPlatformUri(uri);
        return combinePlatformUri(parts);
    }

    /**
     * Splits an Eclipse platform URI into parts:
     * <ul>
     * <li>The prefix, e.g. 'platform:/resource/'. Always ends with a '/'.</li>
     * <li>The project/bundle name, if applicable, or {@code null} otherwise.</li>
     * <li>The path within the platform location. Always starts with a '/'.</li>
     * </ul>
     *
     * @param uri The Eclipse platform URI.
     * @return The parts.
     * @see #combinePlatformUri
     */
    public static String[] splitPlatformUri(String uri) {
        // Precondition check.
        Assert.check(isPlatformUri(uri));

        // Get prefix and determine if we need to find a project/bundle name.
        String prefix;
        boolean hasName;
        if (uri.startsWith("platform:/resource/")) {
            prefix = "platform:/resource/";
            hasName = true;
        } else if (uri.startsWith("platform:/plugin/")) {
            prefix = "platform:/plugin/";
            hasName = true;
        } else if (uri.startsWith("platform:/fragment/")) {
            prefix = "platform:/fragment/";
            hasName = true;
        } else if (uri.startsWith("platform:/meta/")) {
            prefix = "platform:/meta/";
            hasName = true;
        } else {
            // platform:/meta
            // platform:/config
            // platform:/base
            prefix = "platform:/";
            hasName = false;
        }

        // Split Eclipse platform URI into parts.
        String remainder = uri.substring(prefix.length());
        int idx = remainder.indexOf('/');
        String name = (idx == -1) ? remainder : remainder.substring(0, idx);
        String path = (idx == -1) ? "/" : remainder.substring(idx);
        if (!hasName) {
            prefix += name + "/";
            name = null;
        }
        Assert.check(prefix.endsWith("/"));
        Assert.check(path.startsWith("/"));

        // Normalize path, within platform location.
        path = Paths.join(path, ".");
        path = path.replace('\\', '/');

        // Return the parts.
        return new String[] {prefix, name, path};
    }

    /**
     * Combines Eclipse platform URI parts together to get a complete Eclipse platform URI.
     *
     * @param parts The parts.
     * @return The complete Eclipse platform URI.
     * @see #splitPlatformUri
     */
    public static String combinePlatformUri(String[] parts) {
        Assert.check(parts.length == 3);
        Assert.notNull(parts[0]);
        Assert.notNull(parts[2]);
        if (parts[1] == null) {
            return parts[0] + parts[2];
        }
        return parts[0] + parts[1] + parts[2];
    }
}
