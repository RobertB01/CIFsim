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

import static com.github.difflib.UnifiedDiffUtils.generateUnifiedDiff;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.tooldef.runtime.ToolDefException;
import org.eclipse.escet.tooldef.runtime.ToolDefList;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;

/** ToolDef built-in file tools. */
public class BuiltInFileTools {
    /** Constructor for the {@link BuiltInFileTools} class. */
    private BuiltInFileTools() {
        // Static class.
    }

    /**
     * Copies a file from a source location to a target location.
     *
     * @param source The absolute or relative local file system path of the source file. May contain both {@code "\"}
     *     and {@code "/"} as file separators.
     * @param target The absolute or relative local file system path of the target file. May contain both {@code "\"}
     *     and {@code "/"} as file separators.
     * @param overwrite Whether to overwrite the target file if it already exists.
     * @throws ToolDefException If the source file does not exist.
     * @throws ToolDefException If the source is a directory rather than a file, or it could not be determined whether
     *     the source path refers to a file or a directory.
     * @throws ToolDefException If the target file exists and overwriting is not allowed.
     * @throws ToolDefException If the target file exists and overwriting is allowed, but the target refers to a
     *     directory rather than a file, or it could not be determined whether the target path refers to a file or a
     *     directory.
     * @throws ToolDefException If copying the file failed due to an I/O error.
     */
    public static void cpfile(String source, String target, boolean overwrite) {
        // Get absolute paths.
        String absSource = Paths.resolve(source);
        String absTarget = Paths.resolve(target);
        Path sourcePath = java.nio.file.Paths.get(absSource);
        Path targetPath = java.nio.file.Paths.get(absTarget);

        // Check source.
        if (!Files.exists(sourcePath)) {
            String msg = fmt("Failed to copy file: source path \"%s\" does not exist.", source);
            throw new ToolDefException(msg);
        }
        if (!Files.isRegularFile(sourcePath)) {
            String msg = fmt("Failed to copy file: source path \"%s\" is not a file.", source);
            throw new ToolDefException(msg);
        }

        // Check target.
        if (Files.exists(targetPath)) {
            if (!overwrite) {
                String msg = fmt("Failed to copy file: target path \"%s\" already exists.", target);
                throw new ToolDefException(msg);
            }
            if (!Files.isRegularFile(targetPath)) {
                String msg = fmt("Failed to copy file: target path \"%s\" exists but is not a file.", target);
                throw new ToolDefException(msg);
            }
        }

        // Do actual copy.
        CopyOption[] options;
        if (overwrite) {
            options = new CopyOption[] {COPY_ATTRIBUTES, REPLACE_EXISTING};
        } else {
            options = new CopyOption[] {COPY_ATTRIBUTES};
        }

        try {
            Files.copy(sourcePath, targetPath, options);
        } catch (IOException ex) {
            String msg = fmt("Failed to copy file \"%s\" to \"%s\".", source, target);
            throw new ToolDefException(msg, ex);
        }
    }

    /**
     * Copies a directory from a source location to a target location. All files and directories in the source directory
     * are copied recursively.
     *
     * <p>
     * If the operation fails, part of the operation may have already been performed.
     * </p>
     *
     * @param source The absolute or relative local file system path of the source directory. All files and directories
     *     in the source directory are copied recursively. May contain both {@code "\"} and {@code "/"} as file
     *     separators.
     * @param target The absolute or relative local file system path of the target directory. This is the directory in
     *     which the contents of the source directory are copied. The source directory itself is not copied, only the
     *     files and directories contained in the source directory. May contain both {@code "\"} and {@code "/"} as file
     *     separators.
     * @throws ToolDefException If the source directory does not exist.
     * @throws ToolDefException If the source is a file rather than a directory, or it could not be determined whether
     *     the source path refers to a file or a directory.
     * @throws ToolDefException If the target directory already exists.
     * @throws ToolDefException If the target directory doesn't exist, but one of the ancestors is not a directory.
     * @throws ToolDefException If walking the directory (recursively) failed or encountered a file system cycle (due to
     *     symbolic links).
     * @throws ToolDefException If a file or (sub-)directory could not be copied.
     */
    public static void cpdir(String source, String target) {
        cpdir(source, target, "copy");
    }

    /**
     * Copies a directory from a source location to a target location. All files and directories in the source directory
     * are copied recursively.
     *
     * <p>
     * If the operation fails, part of the operation may have already been performed.
     * </p>
     *
     * @param source The absolute or relative local file system path of the source directory. All files and directories
     *     in the source directory are copied recursively. May contain both {@code "\"} and {@code "/"} as file
     *     separators.
     * @param target The absolute or relative local file system path of the target directory. This is the directory in
     *     which the contents of the source directory are copied. The source directory itself is not copied, only the
     *     files and directories contained in the source directory. May contain both {@code "\"} and {@code "/"} as file
     *     separators.
     * @param op The name of the operation to perform, e.g. {@code "copy"} or {@code "move"}.
     * @throws ToolDefException If the source directory does not exist.
     * @throws ToolDefException If the source is a file rather than a directory, or it could not be determined whether
     *     the source path refers to a file or a directory.
     * @throws ToolDefException If the target directory already exists.
     * @throws ToolDefException If the target directory doesn't exist, but one of the ancestors is not a directory.
     * @throws ToolDefException If walking the directory (recursively) failed or encountered a file system cycle (due to
     *     symbolic links).
     * @throws ToolDefException If a file or (sub-)directory could not be copied.
     */
    private static void cpdir(String source, String target, String op) {
        // Get absolute paths.
        String absSource = Paths.resolve(source);
        String absTarget = Paths.resolve(target);
        Path sourcePath = java.nio.file.Paths.get(absSource);
        Path targetPath = java.nio.file.Paths.get(absTarget);

        // Check source.
        if (!Files.exists(sourcePath)) {
            String msg = fmt("Failed to %s directory: source path \"%s\" does not exist.", op, source);
            throw new ToolDefException(msg);
        }
        if (!Files.isDirectory(sourcePath)) {
            String msg = fmt("Failed to %s directory: source path \"%s\" is not a directory.", op, source);
            throw new ToolDefException(msg);
        }

        // Check target.
        if (Files.exists(targetPath)) {
            String msg = fmt("Failed to %s directory: target path \"%s\" already exists.", op, target);
            throw new ToolDefException(msg);
        }

        // Create target directory.
        try {
            Files.createDirectories(targetPath);
        } catch (IOException ex) {
            String msg = fmt("Failed to %s directory: could not create target directory \"%s\".", op, target);
            throw new ToolDefException(msg, ex);
        }

        // Do actual copy.
        EnumSet<FileVisitOption> options = EnumSet.of(FOLLOW_LINKS);
        DirCopier copier = new DirCopier(sourcePath, targetPath);
        try {
            Files.walkFileTree(sourcePath, options, Integer.MAX_VALUE, copier);
        } catch (IOException | ToolDefException ex) {
            String msg = fmt("Failed to %s directory \"%s\" to \"%s\".", op, source, target);
            throw new ToolDefException(msg, ex);
        }
    }

    /** Recursive directory copier used by {@link #cpdir}. */
    private static class DirCopier implements FileVisitor<Path> {
        /** The target directory. Exists and is a directory. */
        private final Path target;

        /** The source directory. Exists and is a directory. */
        private final Path source;

        /**
         * Constructor for the {@link DirCopier} class.
         *
         * @param source The source directory. Exists and is a directory.
         * @param target The target directory. Exists and is a directory.
         */
        public DirCopier(Path source, Path target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            // Before visiting entries in a directory, we copy the directory
            // itself, to make sure it exists.
            CopyOption[] options = new CopyOption[] {COPY_ATTRIBUTES};
            Path newdir = target.resolve(source.relativize(dir));

            try {
                Files.copy(dir, newdir, options);
            } catch (FileAlreadyExistsException ex) {
                // Ignored.
            } catch (IOException ex) {
                String msg = fmt("Failed to create directory \"%s\".", newdir);
                throw new ToolDefException(msg, ex);
            }

            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException ex) {
            if (ex == null) {
                return FileVisitResult.CONTINUE;
            }

            String msg = fmt("Failed to copy directory \"%s\".", dir);
            throw new ToolDefException(msg, ex);
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            CopyOption[] options = new CopyOption[] {COPY_ATTRIBUTES};
            Path newfile = target.resolve(source.relativize(file));
            Files.copy(file, newfile, options);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException ex) {
            if (ex instanceof FileSystemLoopException) {
                String msg = fmt("File system cycle detected for file \"%s\".", file);
                throw new ToolDefException(msg, ex);
            }

            String msg = fmt("Failed to copy file \"%s\".", file);
            throw new ToolDefException(msg, ex);
        }
    }

    /**
     * Computes the differences between two files.
     *
     * @param file1 The absolute or relative local file system path of the first file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param file2 The absolute or relative local file system path of the second file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param output Specify whether/where to write a unified diff if the files differ. Use {@code ""} to not write a
     *     unified diff, {@code "-"} to write the unified diff to stdout, or otherwise an absolute or relative local
     *     file system path of the file to which to write the unified diff. May contain both {@code "\"} and {@code "/"}
     *     as file separators.
     * @param missingAsEmpty Treat a missing first/second file as empty ({@code true}) or as an error ({@code false}).
     * @param warnOnDiff Emit a warning for differing files ({@code true}) or not ({@code false}). If a warning is
     *     emitted to stderr, the unified diff (if enabled) is printed first.
     * @param failOnDiff Treat differing files as an error ({@code true}) or not ({@code false}). If an error is
     *     emitted, the unified diff (if enabled) and warning (if enabled) are printed first.
     * @return {@code true} if the files differ, {@code false} otherwise.
     * @throws ToolDefException If either the first or second file doesn't exist and {@code missingAsEmpty} is disabled.
     * @throws ToolDefException If the first or second file is not a file but a directory, or it could not be determined
     *     whether it is a file or a directory.
     * @throws ToolDefException If an I/O error occurs.
     * @throws ToolDefException If the {@code out} file exists but is a directory rather than a regular file, does not
     *     exist but cannot be created, cannot be opened for writing for any other reason, an I/O error occurs while
     *     writing to it, or the file can not be closed.
     * @throws ToolDefException If the files differ and {@code failOnDiff} is enabled.
     */
    public static boolean diff(String file1, String file2, String output, boolean missingAsEmpty, boolean warnOnDiff,
            boolean failOnDiff)
    {
        // Get absolute paths.
        String absFile1 = Paths.resolve(file1);
        String absFile2 = Paths.resolve(file2);
        Path path1 = java.nio.file.Paths.get(absFile1);
        Path path2 = java.nio.file.Paths.get(absFile2);

        // Read lines from first file.
        List<String> lines1;
        if (Files.exists(path1)) {
            lines1 = readlines(file1);
        } else if (missingAsEmpty) {
            lines1 = list();
        } else {
            String msg = fmt("Failed to diff file \"%s\": file not found.", file1);
            throw new ToolDefException(msg);
        }

        // Read lines from second file.
        List<String> lines2;
        if (Files.exists(path2)) {
            lines2 = readlines(file2);
        } else if (missingAsEmpty) {
            lines2 = list();
        } else {
            String msg = fmt("Failed to diff file \"%s\": file not found.", file2);
            throw new ToolDefException(msg);
        }

        // Perform diff.
        Patch<String> patchResult = DiffUtils.diff(lines1, lines2);
        boolean sameFiles = patchResult.getDeltas().isEmpty();

        // Print diff if files differ and diff printing is not disabled.
        if (!sameFiles && !output.equals("")) {
            try {
                // Get stream.
                AppStream stream;
                boolean toFile;

                if (output.equals("-")) {
                    stream = AppEnv.getStreams().out;
                    toFile = false;
                } else {
                    stream = new FileAppStream(output);
                    toFile = true;
                }

                // Print to stream.
                try {
                    boolean oldConvert = stream.getConvertNewLines();
                    stream.setConvertNewLines(false);
                    for (String line: generateUnifiedDiff(file1, file2, lines1, patchResult, 3)) {
                        stream.println(line);
                    }
                    stream.flush();
                    stream.setConvertNewLines(oldConvert);
                } finally {
                    if (toFile) {
                        stream.close();
                    }
                }
            } catch (ToolDefException | InputOutputException ex) {
                String msg = fmt("Failed to write diff to \"%s\".", output);
                throw new ToolDefException(msg, ex);
            }
        }

        // Warn if files differ.
        if (!sameFiles && warnOnDiff) {
            String msg = fmt("Files \"%s\" and \"%s\" differ.", file1, file2);
            OutputProvider.warn(msg);
        }

        // Error if files differ.
        if (!sameFiles && failOnDiff) {
            String msg = fmt("Files \"%s\" and \"%s\" differ.", file1, file2);
            throw new ToolDefException(msg);
        }

        // Indicate whether files differ.
        return !sameFiles;
    }

    /**
     * Does a file or directory with the given path exist?
     *
     * @param path The absolute or relative local file system path of the file or directory. May contain both
     *     {@code "\"} and {@code "/"} as file separators.
     * @return {@code true} if the file or directory exists, {@code false} otherwise.
     */
    public static boolean exists(String path) {
        Path absPath = java.nio.file.Paths.get(Paths.resolve(path));
        return Files.exists(absPath);
    }

    /**
     * Checks whether a file is newer (was modified at a later date/time) than a reference file.
     *
     * @param path The absolute or relative local file system path of the file for which to check whether it is newer
     *     than the reference file. May contain both {@code "\"} and {@code "/"} as file separators.
     * @param refpath The absolute or relative local file system path of the reference file. May contain both
     *     {@code "\"} and {@code "/"} as file separators.
     * @param allowNonExisting Whether to allow the first file to not exist ({@code true}) or consider it an error if
     *     the first file does not exist ({@code false}).
     * @param sameAsNewer Whether to treat files with the same last change date as being the same ({@code false}) or as
     *     newer ({@code true}).
     * @return {@code false} if the first file does not exist (if allowed by enabling {@code allowNonExisting}), if the
     *     first file is older than the reference file, or if the first file has the same last change date as the
     *     reference file and {@code sameAsNewer} is disabled. {@code true} if the first file is newer than the
     *     reference file, or if the first file has the same last change date as the reference file and
     *     {@code sameAsNewer} is enabled.
     * @throws ToolDefException If the first file does not exist and {@code allowNonExisting} is disabled, if the
     *     reference file does not exist, if any of the files is a directory rather than a file, or if the last change
     *     date/time of a file could not be determined.
     */
    public static boolean filenewer(String path, String refpath, boolean allowNonExisting, boolean sameAsNewer) {
        return filenewer(path, list(refpath), allowNonExisting, sameAsNewer);
    }

    /**
     * Checks whether a file is newer (was modified at a later date/time) than some reference files. The minimum
     * modification time difference that can be detected is 1 millisecond.
     *
     * @param path The absolute or relative local file system path of the file for which to check whether it is newer
     *     than the reference files. May contain both {@code "\"} and {@code "/"} as file separators.
     * @param refpaths The absolute or relative local file system path of the reference files. May contain both
     *     {@code "\"} and {@code "/"} as file separators.
     * @param allowNonExisting Whether to allow the first file to not exist ({@code true}) or consider it an error if
     *     the first file does not exist ({@code false}).
     * @param sameAsNewer Whether to treat files with the same last change date as being the same ({@code false}) or as
     *     newer ({@code true}).
     * @return {@code false} if the first file does not exist (if allowed by enabling {@code allowNonExisting}), if the
     *     first file is older than any the reference files, or if the first file has the same last change date as any
     *     of the reference files and {@code sameAsNewer} is disabled. {@code true} if the first file is newer than all
     *     of the reference files, if the first file has the same last change date as some of the reference files and
     *     {@code sameAsNewer} is enabled and is newer than all of the other reference files, or if the first file has
     *     the same last change date as all the reference files and {@code sameAsNewer} is enabled.
     * @throws ToolDefException If the first file does not exist and {@code allowNonExisting} is disabled, if any of the
     *     reference files does not exist, if any of the files is a directory rather than a file, if for any of the
     *     paths it couldn't be determined whether the path refers to a file or a directory, or if the last change
     *     date/time of any of the files could not be determined.
     */
    public static boolean filenewer(String path, List<String> refpaths, boolean allowNonExisting, boolean sameAsNewer) {
        // Get absolute paths.
        Path absPath = java.nio.file.Paths.get(Paths.resolve(path));
        List<Path> absRefPaths = listc(refpaths.size());
        for (String refpath: refpaths) {
            absRefPaths.add(java.nio.file.Paths.get(Paths.resolve(refpath)));
        }

        // Make sure first file exist.
        if (!allowNonExisting && !Files.exists(absPath)) {
            String msg = fmt("Failed to determine if file is newer: file \"%s\" does not exist.", path);
            throw new ToolDefException(msg);
        }
        if (Files.exists(absPath) && !Files.isRegularFile(absPath)) {
            String msg = fmt("Failed to determine if file is newer: path \"%s\" is not a file.", path);
            throw new ToolDefException(msg);
        }

        // Make sure reference files exist.
        for (int i = 0; i < refpaths.size(); i++) {
            if (!Files.exists(absRefPaths.get(i))) {
                String msg = fmt("Failed to determine if file is newer: file \"%s\" does not exist.", refpaths.get(i));
                throw new ToolDefException(msg);
            }
            if (!Files.isRegularFile(absRefPaths.get(i))) {
                String msg = fmt("Failed to determine if file is newer: path \"%s\" is not a file.", refpaths.get(i));
                throw new ToolDefException(msg);
            }
        }

        // Special case for non-existing first file.
        if (!Files.exists(absPath) && allowNonExisting) {
            return false;
        }

        // Check times.
        FileTime filetime;
        try {
            filetime = Files.getLastModifiedTime(absPath);
        } catch (IOException ex) {
            String msg = fmt(
                    "Failed to determine if file is newer: failed to get the last modified time of file \"%s\".", path);
            throw new ToolDefException(msg, ex);
        }

        // If a file is copied, it may loose modification time precision. To still obtain equal modification times in
        // such cases, convert to milliseconds.
        long filetimeMillis = filetime.toMillis();

        for (int i = 0; i < refpaths.size(); i++) {
            FileTime reftime;
            try {
                reftime = Files.getLastModifiedTime(absRefPaths.get(i));
            } catch (IOException ex) {
                String msg = fmt("Failed to determine if file is newer: failed to get the last modified time of "
                        + "file \"%s\".", refpaths.get(i));
                throw new ToolDefException(msg, ex);
            }

            long reftimeMillis = reftime.toMillis();

            if (filetimeMillis < reftimeMillis) {
                return false;
            }
            if (filetimeMillis == reftimeMillis && !sameAsNewer) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the size of the file, in bytes.
     *
     * @param path The absolute or relative local file system path of the file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param missingAsZero Whether to return {@code 0} if the file does not exist ({@code true}) or consider it an
     *     error if the file does not exist ({@code false}).
     * @return The size of the file in bytes, or {@code 0} if the file is missing and {@code missingAsZero} is enabled.
     * @throws ToolDefException If the file does not exist and {@code missingAsZero} is disabled, the file is a
     *     directory rather than a file, it could not be determined whether the path refers to a file or a directory, or
     *     the size of the file could not be determined due to an I/O error.
     */
    public static long filesize(String path, boolean missingAsZero) {
        // Get absolute path.
        Path absPath = java.nio.file.Paths.get(Paths.resolve(path));

        // Check file.
        if (!Files.exists(absPath)) {
            if (missingAsZero) {
                return 0;
            }

            String msg = fmt("Failed to get size of file: file \"%s\" does not exist.", path);
            throw new ToolDefException(msg);
        }
        if (!Files.isRegularFile(absPath)) {
            String msg = fmt("Failed to get size of file: path \"%s\" is not a file.", path);
            throw new ToolDefException(msg);
        }

        // Get file size.
        try {
            return Files.size(absPath);
        } catch (IOException ex) {
            String msg = fmt("Failed to get size of file \"%s\".", path);
            throw new ToolDefException(msg, ex);
        }
    }

    /**
     * Searches a directory for files and/or directories matching a pattern.
     *
     * @param path The absolute or relative local file system path of the directory in which to search. The directory
     *     itself is never returned. May contain both {@code "\"} and {@code "/"} as file separators.
     * @param pattern The pattern to use to match files/directories. Is a Java NIO glob pattern. Pattern {@code "*"}
     *     matches all files and directories.
     * @param recursive Whether to recursively look in sub-directories.
     * @param files Whether to match files.
     * @param dirs Whether to match directories.
     * @return The local file system paths of the matched files and directories, relative against the given root
     *     directory from which searching started.
     * @throws ToolDefException If the directory is not found, is a file rather than a directory, if the pattern is
     *     invalid, if walking the directory (recursively) failed or encountered a file system cycle (due to symbolic
     *     links), or if finding failed due to an I/O error.
     * @see <a href="http://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob">What Is a Glob?</a>
     */
    public static List<String> find(String path, String pattern, boolean recursive, boolean files, boolean dirs) {
        // Get absolute root path.
        Path root = java.nio.file.Paths.get(Paths.resolve(path));

        // Check root path.
        if (!Files.exists(root)) {
            String msg = fmt("Failed to find in directory: root path \"%s\" does not exist.", path);
            throw new ToolDefException(msg);
        }
        if (!Files.isDirectory(root)) {
            String msg = fmt("Failed to find in directory: root path \"%s\" is not a directory.", path);
            throw new ToolDefException(msg);
        }

        // Create path matcher from pattern.
        PathMatcher matcher;
        try {
            matcher = root.getFileSystem().getPathMatcher("glob:" + pattern);
        } catch (PatternSyntaxException ex) {
            String msg = fmt("Failed to find in directory \"%s\": invalid pattern \"%s\".", path, pattern);
            throw new ToolDefException(msg, ex);
        }

        // Find files/directories.
        EnumSet<FileVisitOption> options = EnumSet.of(FOLLOW_LINKS);
        Finder finder = new Finder(root, matcher, recursive, files, dirs);
        try {
            Files.walkFileTree(root, options, Integer.MAX_VALUE, finder);
        } catch (IOException | ToolDefException ex) {
            String msg = fmt("Failed to find in directory \"%s\".", path);
            throw new ToolDefException(msg, ex);
        }

        // Return sorted results for more deterministic output.
        return BuiltInDataTools.sorted(finder.results);
    }

    /** Recursive file/directory finder used by {@link #find}. */
    private static class Finder implements FileVisitor<Path> {
        /** The root directory. Exists and is a directory. */
        private final Path root;

        /** The path matcher to use. */
        private final PathMatcher matcher;

        /** Whether to recursively look in sub-directories. */
        private final boolean recursive;

        /** Whether to match files. */
        private final boolean files;

        /** Whether to match directories. */
        private final boolean dirs;

        /** The results found so far. */
        public List<String> results = new ToolDefList<>();

        /**
         * Constructor for the {@link Finder} class.
         *
         * @param root The root directory. Must exists and be a directory.
         * @param matcher The path matcher to use.
         * @param recursive Whether to recursively look in sub-directories.
         * @param files Whether to match files.
         * @param dirs Whether to match directories.
         */
        public Finder(Path root, PathMatcher matcher, boolean recursive, boolean files, boolean dirs) {
            this.root = root;
            this.matcher = matcher;
            this.recursive = recursive;
            this.files = files;
            this.dirs = dirs;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            // Process the directory.
            if (dirs && !dir.equals(root)) {
                if (matcher.matches(dir.getFileName())) {
                    String rslt = root.relativize(dir).toString();
                    results.add(rslt);
                }
            }

            // Should the children be visited?
            if (dir.equals(root)) {
                return CONTINUE;
            }
            return recursive ? CONTINUE : SKIP_SUBTREE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException ex) {
            if (ex == null) {
                return FileVisitResult.CONTINUE;
            }

            String msg = fmt("Failed to find in directory \"%s\".", dir);
            throw new ToolDefException(msg, ex);
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            // Process the directory.
            if (files) {
                if (matcher.matches(file.getFileName())) {
                    String rslt = root.relativize(file).toString();
                    results.add(rslt);
                }
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException ex) {
            if (ex instanceof FileSystemLoopException) {
                String msg = fmt("File system cycle detected for file \"%s\".", file);
                throw new ToolDefException(msg, ex);
            }

            String msg = fmt("Failed to find for file \"%s\".", file);
            throw new ToolDefException(msg, ex);
        }
    }

    /**
     * Does a directory with the given path exist?
     *
     * @param path The absolute or relative local file system path of the directory. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @return {@code true} if the directory exists, {@code false} if it doesn't exist or is not a directory.
     */
    public static boolean isdir(String path) {
        Path absPath = java.nio.file.Paths.get(Paths.resolve(path));
        return Files.isDirectory(absPath);
    }

    /**
     * Does a file with the given path exist?
     *
     * @param path The absolute or relative local file system path of the file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @return {@code true} if the file exists, {@code false} if it doesn't exist or is not a file.
     */
    public static boolean isfile(String path) {
        Path absPath = java.nio.file.Paths.get(Paths.resolve(path));
        return Files.isRegularFile(absPath);
    }

    /**
     * Creates the given directory, and optionally its parents as needed.
     *
     * @param path The absolute or relative local file system path of the directory to create. May contain both
     *     {@code "\"} and {@code "/"} as file separators.
     * @param force Whether to skip creating the directory if it already exists ({@code true}) or fail if it already
     *     exists ({@code false}).
     * @param parents Whether to allow creating parents as needed ({@code true}) or fail if the parent directory does
     *     not exist ({@code false}).
     * @throws ToolDefException If the directory already exists and {@code force} is disabled, or creating the directory
     *     or any of its parents fails due to an I/O error.
     */
    public static void mkdir(String path, boolean force, boolean parents) {
        // Get absolute path.
        Path absPath = java.nio.file.Paths.get(Paths.resolve(path));

        // Check path.
        if (Files.isDirectory(absPath)) {
            if (force) {
                return;
            }

            String msg = fmt("Failed to create directory: directory \"%s\" already exists.", path);
            throw new ToolDefException(msg);
        }

        // Create directory/directories.
        try {
            if (parents) {
                Files.createDirectories(absPath);
            } else {
                Files.createDirectory(absPath);
            }
        } catch (IOException ex) {
            String msg = fmt("Failed to create directory \"%s\".", path);
            throw new ToolDefException(msg, ex);
        }
    }

    /**
     * Moves a file from a source location to a target location. This can be used to rename a file and/or move it to
     * another directory.
     *
     * @param source The absolute or relative local file system path of the source file. May contain both {@code "\"}
     *     and {@code "/"} as file separators.
     * @param target The absolute or relative local file system path of the target file. May contain both {@code "\"}
     *     and {@code "/"} as file separators.
     * @param overwrite Whether to overwrite the target file if it already exists.
     * @throws ToolDefException If the source file does not exist.
     * @throws ToolDefException If the source is a directory rather than a file, or it could not be determined whether
     *     the source path refers to a file or a directory.
     * @throws ToolDefException If the target file exist and overwriting is not allowed.
     * @throws ToolDefException If the target file exists and overwriting is allowed, but the target refers to a
     *     directory rather than a file, or it could not be determined whether the target path refers to a file or a
     *     directory.
     * @throws ToolDefException If moving the file fails due to an I/O error.
     */
    public static void mvfile(String source, String target, boolean overwrite) {
        // Get absolute paths.
        String absSource = Paths.resolve(source);
        String absTarget = Paths.resolve(target);
        Path sourcePath = java.nio.file.Paths.get(absSource);
        Path targetPath = java.nio.file.Paths.get(absTarget);

        // Check source.
        if (!Files.exists(sourcePath)) {
            String msg = fmt("Failed to move file: source path \"%s\" does not exist.", source);
            throw new ToolDefException(msg);
        }
        if (!Files.isRegularFile(sourcePath)) {
            String msg = fmt("Failed to move file: source path \"%s\" is not a file.", source);
            throw new ToolDefException(msg);
        }

        // Check target.
        if (Files.exists(targetPath)) {
            if (!overwrite) {
                String msg = fmt("Failed to move file: target path \"%s\" already exists.", target);
                throw new ToolDefException(msg);
            }
            if (!Files.isRegularFile(targetPath)) {
                String msg = fmt("Failed to move file: target path \"%s\" exists but is not a file.", target);
                throw new ToolDefException(msg);
            }
        }

        // Do actual move.
        CopyOption[] options;
        if (overwrite) {
            options = new CopyOption[] {REPLACE_EXISTING};
        } else {
            options = new CopyOption[] {};
        }

        try {
            Files.move(sourcePath, targetPath, options);
        } catch (IOException ex) {
            String msg = fmt("Failed to move file \"%s\" to \"%s\".", source, target);
            throw new ToolDefException(msg, ex);
        }
    }

    /**
     * Moves a directory from a source location to a target location. The directory and all files and directories in it
     * are moved recursively.
     *
     * <p>
     * The operation is implemented as a copy from source to target, followed by a remove of the source. If the
     * operation fails, part of the operation may have already been performed.
     * </p>
     *
     * @param source The absolute or relative local file system path of the source directory. The directory itself and
     *     all files and directories in it are moved recursively. May contain both {@code "\"} and {@code "/"} as file
     *     separators.
     * @param target The absolute or relative local file system path of the target directory. This is the directory into
     *     which the contents of the source directory are moved. May contain both {@code "\"} and {@code "/"} as file
     *     separators.
     * @throws ToolDefException If the source directory does not exist.
     * @throws ToolDefException If the source is a file rather than a directory, or it could not be determined whether
     *     the source path refers to a file or a directory.
     * @throws ToolDefException If the target directory already exists.
     * @throws ToolDefException If the target directory doesn't exist but one of the ancestors is not a directory.
     * @throws ToolDefException If walking the directory (recursively) failed or encountered a file system cycle (due to
     *     symbolic links).
     * @throws ToolDefException If a file or (sub-)directory could not be copied or removed.
     */
    public static void mvdir(String source, String target) {
        cpdir(source, target, "move");
        rmdir(source, false, "move");
    }

    /**
     * Read lines of text from a file.
     *
     * @param path The absolute or relative local file system path of the file to read. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @return The lines of text from the file.
     * @throws ToolDefException If the file does not exist, is a directory rather than a file, it could not be
     *     determined whether the path refers to a file or a directory, for some other reason could not be opened for
     *     reading, an I/O error occurred, or could not be closed.
     */
    public static List<String> readlines(String path) {
        // Get absolute local file system path.
        String abspath = Paths.resolve(path);

        // Read the lines.
        List<String> lines = new ToolDefList<>();
        try (FileReader fileReader = new FileReader(abspath, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(fileReader))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException ex) {
            String msg = fmt(
                    "Failed to read file \"%s\": the file does not exist, is a directory rather than a file, or "
                            + "for some other reason could not be opened for reading.",
                    path);
            throw new ToolDefException(msg, ex);
        } catch (IOException ex) {
            String msg = fmt("Failed to read file \"%s\": an I/O error occurred.", path);
            throw new ToolDefException(msg, ex);
        }

        // Return the read lines.
        return lines;
    }

    /**
     * Removes a file.
     *
     * @param path The absolute or relative local file system path of the file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param force Whether to ignore non-existing files ({@code true}) or consider it an error ({@code false}).
     * @return {@code true} if the file was removed, {@code false} if it could not be removed because it did not exist
     *     and {@code force} is enabled.
     * @throws ToolDefException If the file does not exist and {@code force} is disabled, if the file is a directory
     *     rather than a file, it could not be determined whether the path refers to a file or a directory, an I/O error
     *     occurs, or the file could not be removed for some other reason.
     */
    public static boolean rmfile(String path, boolean force) {
        // Get absolute path.
        Path absPath = java.nio.file.Paths.get(Paths.resolve(path));

        // Check file.
        if (!force && !Files.exists(absPath)) {
            String msg = fmt("Failed to remove file: file \"%s\" does not exist.", path);
            throw new ToolDefException(msg);
        }
        if (!force && !Files.isRegularFile(absPath)) {
            String msg = fmt("Failed to remove file: path \"%s\" is not a file.", path);
            throw new ToolDefException(msg);
        }

        // Remove file.
        try {
            if (force) {
                return Files.deleteIfExists(absPath);
            } else {
                Files.delete(absPath);
                return true;
            }
        } catch (IOException ex) {
            String msg = fmt("Failed to remove file \"%s\".", path);
            throw new ToolDefException(msg, ex);
        }
    }

    /**
     * Removes a directory, recursively.
     *
     * @param path The absolute or relative local file system path of the directory. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param force Whether to ignore non-existing directories ({@code true}) or consider it an error ({@code false}).
     * @return {@code true} if the directory was removed, {@code false} if it could not be removed because it did not
     *     exist and {@code force} is enabled.
     * @throws ToolDefException If the directory does not exist and {@code force} is disabled, if the directory is a
     *     file rather than a directory, it could not be determined whether the path refers to a file or a directory, an
     *     I/O error occurs, the directory or one of its sub-files or sub-directories could not be removed for some
     *     other reason, or walking the directory (recursively) failed or encountered a file system cycle (due to
     *     symbolic links).
     */
    public static boolean rmdir(String path, boolean force) {
        return rmdir(path, force, "remove");
    }

    /**
     * Removes a directory, recursively.
     *
     * @param path The absolute or relative local file system path of the directory. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param force Whether to ignore non-existing directories ({@code true}) or consider it an error ({@code false}).
     * @param op The name of the operation to perform, e.g. {@code "remove"} or {@code "move"}.
     * @return {@code true} if the directory was removed, {@code false} if it could not be removed because it did not
     *     exist and {@code force} is enabled.
     * @throws ToolDefException If the directory does not exist and {@code force} is disabled, if the directory is a
     *     file rather than a directory, it could not be determined whether the path refers to a file or a directory, an
     *     I/O error occurs, the directory or one of its sub-files or sub-directories could not be removed for some
     *     other reason, or walking the directory (recursively) failed or encountered a file system cycle (due to
     *     symbolic links).
     */
    private static boolean rmdir(String path, boolean force, String op) {
        // Get absolute path.
        Path absPath = java.nio.file.Paths.get(Paths.resolve(path));

        // Check directory.
        if (!Files.exists(absPath)) {
            if (force) {
                return false;
            }

            String msg = fmt("Failed to %s directory: directory \"%s\" does not exist.", op, path);
            throw new ToolDefException(msg);
        }
        if (!force && !Files.isDirectory(absPath)) {
            String msg = fmt("Failed to %s directory: path \"%s\" is not a directory.", op, path);
            throw new ToolDefException(msg);
        }

        // Do actual remove.
        DirRemover remover = new DirRemover();
        EnumSet<FileVisitOption> options = EnumSet.noneOf(FileVisitOption.class);
        try {
            Files.walkFileTree(absPath, options, Integer.MAX_VALUE, remover);
        } catch (IOException | ToolDefException ex) {
            String msg = fmt("Failed to %s directory \"%s\".", op, path);
            throw new ToolDefException(msg, ex);
        }
        return true;
    }

    /** Recursive directory remover used by {@link #rmdir}. */
    private static class DirRemover implements FileVisitor<Path> {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException ex) {
            // Loop detection.
            if (ex instanceof FileSystemLoopException) {
                String msg = fmt("File system cycle detected for file \"%s\".", file);
                throw new ToolDefException(msg, ex);
            }

            // Try to delete the file anyway, even if its attributes could not
            // be read, since delete-only access is theoretically possible.
            try {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            } catch (IOException ex2) {
                String msg = fmt("Failed to remove file \"%s\".", file);
                throw new ToolDefException(msg, ex2);
            }
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException ex) {
            if (ex == null) {
                try {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } catch (IOException ex2) {
                    String msg = fmt("Failed to remove directory \"%s\".", dir);
                    throw new ToolDefException(msg, ex2);
                }
            } else {
                // Directory iteration failed.
                String msg = fmt("Failed to remove directory \"%s\".", dir);
                throw new ToolDefException(msg, ex);
            }
        }
    }

    /**
     * Writes text to a file.
     *
     * @param path The absolute or relative local file system path of the file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param text The text to write to the file.
     * @param append Whether to append the text to the file if it already exists ({@code true}), or overwrite the file
     *     if it already exists ({@code false}).
     */
    public static void writefile(String path, String text, boolean append) {
        try (FileAppStream stream = new FileAppStream(path, append)) {
            stream.print(text);
        } catch (InputOutputException ex) {
            String msg = fmt("Failed to write to file \"%s\".", path);
            throw new ToolDefException(msg, ex);
        }
    }

    /**
     * Writes lines of text to a file.
     *
     * @param path The absolute or relative local file system path of the file. May contain both {@code "\"} and
     *     {@code "/"} as file separators.
     * @param lines The lines of text to write to the file.
     * @param append Whether to append the lines text to the file if it already exists ({@code true}), or overwrite the
     *     file if it already exists ({@code false}).
     * @throws ToolDefException If the path exists but is a directory rather than a regular file, the file does not
     *     exist, but cannot be created, the file could not be opened for writing for any other reason, writing to the
     *     file failed due to an I/O error, or closing the file failed.
     */
    public static void writefile(String path, List<String> lines, boolean append) {
        try (FileAppStream stream = new FileAppStream(path, append)) {
            for (String line: lines) {
                stream.println(line);
            }
        } catch (InputOutputException ex) {
            String msg = fmt("Failed to write to file \"%s\".", path);
            throw new ToolDefException(msg, ex);
        }
    }
}
