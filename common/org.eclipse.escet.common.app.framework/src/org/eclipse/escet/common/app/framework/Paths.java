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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** Application framework specific path handling. */
public class Paths {
    /** Constructor for the {@link Paths} class. */
    private Paths() {
        // Private constructor to make this class a static class.
    }

    /**
     * Returns the application's current working directory. This is an absolute local file system path. The path
     * contains separators for the current platform.
     *
     * @return The application's current working directory.
     * @see AppEnv#getProperty
     * @see File#separatorChar
     */
    public static String getCurWorkingDir() {
        String rslt = AppEnv.getProperty("user.dir");
        Assert.notNull(rslt);
        Assert.check(new File(rslt).isAbsolute());
        return rslt;
    }

    /**
     * Sets the application's current working directory.
     *
     * @param path The absolute local file system path that is the new current working directory of the current
     *     application. The path must contain separators for the current platform.
     * @return The previous current working directory of the application.
     *
     * @see AppEnv#setProperty
     * @see File#separatorChar
     */
    public static String setCurWorkingDir(String path) {
        Assert.notNull(path);
        Assert.check(new File(path).isAbsolute());
        Assert.check(path.indexOf(getNonPlatformSeparator()) == -1);
        return (String)AppEnv.setProperty("user.dir", path);
    }

    /**
     * Is the given path an absolute local file system path, rather than a relative local file system path?
     *
     * @param path The absolute or relative local file system path.
     * @return {@code true} if the path is an absolute local file system path, {@code false} if the path is a relative
     *     local file system path.
     */
    public static boolean isAbsolute(String path) {
        // Linux and macOS absolute path, for all supported path separators.
        if (path.startsWith("\\")) {
            return true;
        }
        if (path.startsWith("/")) {
            return true;
        }

        // Microsoft Windows absolute path, for all supported path separators.
        if (path.length() >= 3 && path.charAt(1) == ':') {
            char c3 = path.charAt(2);
            if (c3 == '\\' || c3 == '/') {
                return true;
            }
        }

        // Not an absolute path.
        return false;
    }

    /**
     * Resolves an absolute or relative local file system path, relative to the current working directory of the
     * application.
     *
     * @param path The absolute or relative local file system path to resolve. It may contain both {@code \} and
     *     {@code /} as separators.
     * @return The absolute path that results from the resolving. Contains only platform specific path separators.
     * @see #getCurWorkingDir
     */
    public static String resolve(String path) {
        return resolve(path, getCurWorkingDir());
    }

    /**
     * Resolves an absolute or relative local file system path, relative to the given current working directory.
     *
     * @param path The absolute or relative local file system path to resolve. It may contain both {@code \} and
     *     {@code /} as separators.
     * @param curWorkingDir The absolute current working directory against which to resolve relative local file system
     *     paths.
     * @return The absolute path that results from the resolving. Contains only platform specific path separators.
     * @see #getCurWorkingDir
     */
    public static String resolve(String path, String curWorkingDir) {
        // If path is absolute, no need to resolve it against the current
        // working directory.
        if (isAbsolute(path)) {
            // Join with '.'. The '.' is removed, but as a side effect, the
            // path is normalized and gets platform specific separators.
            return join(path, ".");
        }

        // Not an absolute path, so it must be a relative path.
        return join(curWorkingDir, path);
    }

    /**
     * Joins two or more paths together.
     *
     * @param paths The paths to join together. The first path may be an absolute local file system path, with platform
     *     separators, or a relative local file system path with either {@code \} or {@code /} as separators. All
     *     remaining paths must be relative local file system paths with either {@code \} or {@code /} as separators.
     * @return The joined path. Contains only platform specific separators.
     * @see File#separatorChar
     */
    public static String join(String... paths) {
        // Precondition checks.
        Assert.check(paths.length > 1);

        // Replace all separators by '/', and split on '/'.
        List<String> segments = list();
        boolean first = true;
        for (String path: paths) {
            // Separator normalization for platform independence.
            path = path.replace('\\', '/');

            // Strip of the leading separators for the first path, which may
            // be absolute.
            int cnt = 0;
            if (first) {
                while (path.startsWith("/")) {
                    cnt++;
                    path = path.substring(1);
                }
            }
            first = false;

            // If the first one, and we removed the absolute prefix, add it
            // again, as separate segment.
            if (cnt > 0) {
                segments.add(Strings.duplicate("/", cnt));
                cnt = 0;
            }

            // Split the path into segments.
            for (String segment: path.split("[/]")) {
                segments.add(segment);
            }
        }

        // Normalize segments.
        int segmentIdx = 0;
        while (segmentIdx < segments.size()) {
            // Get segment.
            String segment = segments.get(segmentIdx);

            // Get rid of empty segments.
            if (segment.isEmpty()) {
                segments.remove(segmentIdx);
                continue;
            }

            // Get rid of current directory segment.
            if (segment.equals(".")) {
                // Remove the segment, but keep the current segment index.
                segments.remove(segmentIdx);
                continue;
            }

            // See if we can get rid of parent directory segment.
            boolean removeDotDot = true;
            if (removeDotDot && !segment.equals("..")) {
                // Is not a '..', so not a parent directory segment.
                removeDotDot = false;
            }
            if (removeDotDot && segmentIdx == 0) {
                // First segment of relative path can't be removed, as there
                // is no previous segment.
                removeDotDot = false;
            }
            if (removeDotDot && segments.get(segmentIdx - 1).equals("..")) {
                // Previous segment is also a parent directory segment, so
                // can't remove both of them, as then we lose two of them.
                removeDotDot = false;
            }
            if (removeDotDot && segmentIdx == 1) {
                String prevSegment = segments.get(0);
                if (prevSegment.startsWith("/") || (prevSegment.length() == 2 && prevSegment.charAt(1) == ':')) {
                    // Previous segment is root of file system, so can't remove
                    // it.
                    removeDotDot = false;
                }
            }

            if (removeDotDot) {
                segmentIdx--;
                segments.remove(segmentIdx);
                segments.remove(segmentIdx);
                continue;
            }

            // Move on to next segment.
            segmentIdx++;
        }

        // Make sure we have at least one segment.
        if (segments.isEmpty()) {
            segments.add(".");
        }

        // Join segments together.
        StringBuilder joined = new StringBuilder(segments.get(0));
        for (int i = 1; i < segments.size(); i++) {
            String segment = segments.get(i);
            char lastChar = joined.charAt(joined.length() - 1);
            if (lastChar != '/') {
                joined.append("/");
            }
            joined.append(segment);
        }
        String rslt = joined.toString();

        // Make sure Windows drive root paths end with a separator.
        if (rslt.length() == 2 && rslt.charAt(1) == ':') {
            rslt += "/";
        }

        // Return joined absolute or relative local file system path, with
        // platform specific separators.
        return rslt.replace('/', getPlatformSeparator());
    }

    /**
     * Constructs a Java file URI from an absolute local file system path.
     *
     * @param absPath The absolute local file system path, with platform specific separators.
     * @return The Java file URI for the given path.
     * @see File#separatorChar
     */
    public static java.net.URI createJavaURI(String absPath) {
        String fileUriPath = URI.createFileURI(absPath).toString();
        java.net.URI rslt;
        try {
            rslt = new java.net.URI(fileUriPath);
        } catch (URISyntaxException e) {
            String msg = fmt("Invalid absolute local file system path: \"%s\".", absPath);
            throw new RuntimeException(msg, e);
        }
        return rslt;
    }

    /**
     * Returns an EMF URI, which has very little to do with EMF, except that the {@link URI} class is defined by EMF.
     * Such URIs can for instance be consumed by EMF related classes, for instance to load resources that are instances
     * of an Ecore.
     *
     * <p>
     * The following special handling of platform paths is used:
     * <ul>
     * <li>Relative local file system paths are resolved to absolute local file system paths using the {@link #resolve}
     * method.</li>
     * <li>{@code platform:/auto/bundle.name/some/path} paths are resolved in the workspace first
     * ({@code platform:/resource/bundle.name/some/path}). If they can not be resolved in the workspace, they are
     * resolved as plug-in paths ({@code platform:/plugin/bundle.name/some/path}). If they can not be found in the
     * plug-ins either, this method fails.</li>
     * </ul>
     * </p>
     *
     * @param resourcePath A resource path. This may either be an absolute or relative local file system path, or an
     *     Eclipse platform URI.
     * @return A freshly created {@link URI} instance.
     * @throw InvalidInputException If a {@code platform:/auto/...} path can not be resolved in the workspace or in the
     *     plug-ins.
     *
     * @see <a href=
     *     "http://help.eclipse.org/ganymede/index.jsp?topic=/org.eclipse.platform.doc.isv/reference/misc/platform-scheme-uri.html"
     *     >Platform URLs in Eclipse</a>
     */
    public static URI createEmfURI(String resourcePath) {
        // Construct URI.
        URI resourceURI = URI.createURI(resourcePath);

        // Detect platform paths.
        if (resourceURI.isPlatform()) {
            // Special handling of 'platform:/auto/...' paths.
            if (resourceURI.segment(0).equals("auto")) {
                String[] segments = resourceURI.segments();
                URIHandler handler = new URIHandlerImpl();

                // Try as workspace path.
                resourceURI = resourceURI.trimSegments(segments.length);
                segments[0] = "resource";
                resourceURI = resourceURI.appendSegments(segments);
                if (handler.exists(resourceURI, Collections.emptyMap())) {
                    // Exists as workspace path.
                    return resourceURI;
                }

                // Try as plug-in path.
                resourceURI = resourceURI.trimSegments(segments.length);
                segments[0] = "plugin";
                resourceURI = resourceURI.appendSegments(segments);
                if (handler.exists(resourceURI, Collections.emptyMap())) {
                    // Exists as plug-in path.
                    return resourceURI;
                }

                // Could not be resolved in the workspace or plug-ins.
                String msg = fmt("Could not find resource \"%s\": resource could not be resolved in the workspace, "
                        + "and also not in the plug-ins.", resourcePath);
                throw new InvalidInputException(msg);
            }

            // Other platform URIs.
            return resourceURI;
        }

        // Not a platform URI, so create a file URI. We first get rid of any
        // 'file:' prefixes, etc. Then we obtain the absolute path, from which
        // we construct a file URI again.
        String filePath = resourcePath;
        if (filePath.startsWith("file:")) {
            filePath = filePath.substring("file:".length());
        }
        filePath = Paths.resolve(filePath);
        resourceURI = URI.createFileURI(filePath);
        return resourceURI;
    }

    /**
     * Returns an EMF platformURI, which has very little to do with EMF, except that the {@link URI} class is defined by
     * EMF. Such URIs can for instance be consumed by EMF related classes, for instance to load resources that are
     * instances of an Ecore.
     *
     * <p>
     * Uses the {@link #createEmfURI(String)} method with {@code "platform:/auto/<bundleName>/<localPath>"} as resource
     * path, to obtain the result of this method.
     * </p>
     *
     * @param bundleName The bundle name.
     * @param localPath The local path within the bundle.
     * @return The platform {@link URI} for the given bundle and local path.
     * @exception IllegalArgumentException If the path could not be resolved in the workspace, and also not in the
     *     plug-ins.
     */
    public static URI createEmfURI(String bundleName, String localPath) {
        return createEmfURI("platform:/auto/" + bundleName + "/" + localPath);
    }

    /**
     * Returns the separator that is the separator for the current platform.
     *
     * @return The separator that is the separator for the current platform.
     * @see File#separatorChar
     */
    public static char getPlatformSeparator() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return '\\';
        }
        return '/';
    }

    /**
     * Returns the separator that is <em>not</em> the separator for the current platform.
     *
     * @return The separator that is <em>not</em> the separator for the current platform.
     * @see File#separatorChar
     */
    public static char getNonPlatformSeparator() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return '/';
        }
        return '\\';
    }

    /**
     * Returns the file extension of the given file, or {@code ""} if the file has no file extension.
     *
     * @param path The absolute or relative local file system path to the file. May contain both {@code "\"} and
     *     {@code "/"} as path separators.
     * @return The file extension, or {@code ""}.
     */
    public static String getExtension(String path) {
        // Find last path separator.
        int idx = Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/'));

        // Cut of the directory, making sure only the file name remains.
        path = (idx == -1) ? path : path.substring(idx + 1);

        // Look for file extension separator.
        idx = path.lastIndexOf(".");

        // Return file extension.
        return (idx == -1) ? "" : path.substring(idx + 1);
    }

    /**
     * Does the path have the given extension? Is checked case insensitively.
     *
     * @param path The path to test.
     * @param ext The extension to test (no "." at the start).
     * @return Whether the given path ends with the extension.
     */
    public static boolean pathHasExtension(String path, String ext) {
        String fullExt = "." + ext.toLowerCase(Locale.US);
        return path.toLowerCase(Locale.US).endsWith(fullExt);
    }

    /**
     * Modifies a path such that it ends with a new extension, removing an old extension if it exists.
     *
     * @param path The path to change.
     * @param oldExt The old extension that can be removed (case insensitive, no "." at the start). Use {@code null} to
     *     not remove an old extension.
     * @param newExt The new extension to use (case sensitive, no "." at the start). Use {@code null} to not add a new
     *     extension.
     * @return The changed path, ending with the new extension (if any).
     */
    public static String pathChangeExtension(String path, String oldExt, String newExt) {
        if (oldExt != null && pathHasExtension(path, oldExt)) {
            path = Strings.slice(path, 0, -oldExt.length() - 1);
        }

        if (newExt != null) {
            path += "." + newExt;
        }
        return path;
    }

    /**
     * Returns a path to the given target file or directory, relative to the given directory. The input paths must be
     * absolute local file system paths, and must be normalized (i.e. must not contain {@code "."} or {@code ".."}
     * parts).
     *
     * <p>
     * This method operates syntactically on paths. It does not require the application framework. It also does not
     * require the files/directories to actually exist.
     * </p>
     *
     * <p>
     * If the paths are Windows paths, with different drive prefixes, the target path is returned as an absolute path.
     * </p>
     *
     * @param absTgtPath The absolute local file system path to the target file or directory. This path is turned into a
     *     relative path.
     * @param absRelDir The absolute local file system path to the 'relative' directory. The resulting path is relative
     *     to this directory.
     * @return A relative local file system path to the given target file or directory. The result uses {@code /} as
     *     directory separator, regardless of the directory separators used in the input.
     */
    public static String getRelativePath(String absTgtPath, String absRelDir) {
        // Normalize path separators.
        absTgtPath = absTgtPath.replace('\\', '/');
        absRelDir = absRelDir.replace('\\', '/');

        // Remove empty parts.
        while (absTgtPath.contains("//")) {
            absTgtPath = absTgtPath.replace("//", "/");
        }
        while (absRelDir.contains("//")) {
            absRelDir = absRelDir.replace("//", "/");
        }

        // Split to sequences of path parts. Removes the last part if empty,
        // thereby getting rid of trailing path separators. Handle '/' paths
        // as a special case.
        String[] absTargetParts = absTgtPath.equals("/") ? new String[] {""} : absTgtPath.split("/");
        String[] absRelDirParts = absRelDir.equals("/") ? new String[] {""} : absRelDir.split("/");

        // Detect common prefix. After the loop, 'idx' is the index of the part
        // directly after the last common part. If no common parts, 'idx' is 0.
        // Note that 'idx' may not exist for one of the paths.
        int idx = 0;
        while (idx < absTargetParts.length && idx < absRelDirParts.length
                && absTargetParts[idx].equals(absRelDirParts[idx]))
        {
            idx++;
        }

        // Special case for no common directories, and the first entries are
        // (different) Windows drives. In this case, return the target as an
        // absolute path.
        if (idx == 0 && absTargetParts.length > 0 && absRelDirParts.length > 0 && absTargetParts[0].endsWith(":")
                && absRelDirParts[0].endsWith(":"))
        {
            return (absTargetParts.length == 1 && absTargetParts[0].endsWith(":")) ? absTargetParts[0] + "/"
                    : StringUtils.join(absTargetParts, '/');
        }

        // Walk up to common directory, from absolute relative directory.
        List<String> rsltParts = list();
        for (int i = idx; i < absRelDirParts.length; i++) {
            rsltParts.add("..");
        }

        // Walk down to the target, from the common directory.
        for (int i = idx; i < absTargetParts.length; i++) {
            rsltParts.add(absTargetParts[i]);
        }

        // If both directories are the same, make sure we don't end up with an
        // empty result.
        if (rsltParts.isEmpty()) {
            rsltParts.add(".");
        }

        // Join and return the result using '/' separators.
        return (rsltParts.size() == 1 && rsltParts.get(0).endsWith(":")) ? rsltParts.get(0) + "/"
                : StringUtils.join(rsltParts, '/');
    }

    /**
     * Returns the absolute directory path of the directory that contains the given file.
     *
     * @param absFilePath The absolute path to the file for which to return the absolute directory path. May contain
     *     both {@code /} and {@code \} as path separators.
     * @return The absolute directory path of the directory that contains the given file.
     */
    public static String getAbsFilePathDir(String absFilePath) {
        // Start with absolute file path.
        String rslt = absFilePath;

        // Find last path separator.
        int idx = Math.max(rslt.lastIndexOf('\\'), rslt.lastIndexOf('/'));
        Assert.check(idx >= 0);

        // Cut off file name and last path separator.
        rslt = rslt.substring(0, idx);

        // Fix files in root of file system or drive.
        if (rslt.isEmpty()) {
            // Root of file system (Linux/Mac).
            rslt = "/";
        } else if (rslt.endsWith(":") && !rslt.contains("\\") && !rslt.contains("/")) {
            // Root of drive (Windows).
            rslt += "\\";
        }

        // Return absolute directory path.
        return rslt;
    }

    /**
     * Returns the file name of the file, to which the given absolute or relative local file system path refers. That
     * is, returns the last part of the path.
     *
     * @param filePath The absolute or relative local file system path to the file for which to return the file name.
     *     May contain both {@code /} and {@code \} as path separators.
     * @return The file name.
     */
    public static String getFileName(String filePath) {
        // Find last path separator.
        int idx = Math.max(filePath.lastIndexOf('\\'), filePath.lastIndexOf('/'));
        return (idx == -1) ? filePath : filePath.substring(idx + 1);
    }
}
