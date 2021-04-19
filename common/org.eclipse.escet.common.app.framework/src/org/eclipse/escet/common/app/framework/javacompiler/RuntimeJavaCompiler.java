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

package org.eclipse.escet.common.app.framework.javacompiler;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.eclipse.escet.common.app.framework.exceptions.DependencyException;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.javacompiler.jdt.RuntimeJavaCompilerJdtUtil;
import org.eclipse.escet.common.java.Assert;

/** Java compiler that supports run-time in-memory compilation. */
public class RuntimeJavaCompiler {
    /** Whether to enable debugging for the run-time Java compiler. */
    public static final boolean DEBUG = false;

    /** The underlying Java compiler to use. */
    private final JavaCompiler compiler;

    /** Compile options, or {@code null} for no options. */
    private final Iterable<String> options;

    /** Diagnostics that resulted from compilation. */
    private final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    /** File manager used to resolve input files and store output files. */
    private final RuntimeJavaFileManager fileManager;

    /** Collected compiler output. */
    private final CharArrayWriter output = new CharArrayWriter();

    /**
     * Constructor for the {@link RuntimeJavaCompiler} class. Uses the {@link #getJavaCompiler default} Java compiler,
     * the {@link #getDefaultCompilerOptions default} compiler options, and {@link ClassLoader#getSystemClassLoader} as
     * an additional class loader to resolve dependencies outside of the sources.
     */
    public RuntimeJavaCompiler() {
        this(getJavaCompiler(null), getDefaultCompilerOptions(), null);
    }

    /**
     * Constructor for the {@link RuntimeJavaCompiler} class. Uses the {@link #getDefaultCompilerOptions default}
     * compiler options, and {@link ClassLoader#getSystemClassLoader} as an additional class loader to resolve
     * dependencies outside of the sources.
     *
     * @param compiler The Java compiler to use.
     */
    public RuntimeJavaCompiler(JavaCompiler compiler) {
        this(compiler, getDefaultCompilerOptions(), null);
    }

    /**
     * Constructor for the {@link RuntimeJavaCompiler} class. Uses the {@link #getJavaCompiler default} Java compiler,
     * and the {@link #getDefaultCompilerOptions default} compiler options.
     *
     * @param classLoader Class loader to use to resolve dependencies outside of the sources. May be {@code null} if not
     *     available, to use {@link ClassLoader#getSystemClassLoader}.
     */
    public RuntimeJavaCompiler(ClassLoader classLoader) {
        this(getJavaCompiler(null), getDefaultCompilerOptions(), classLoader);
    }

    /**
     * Constructor for the {@link RuntimeJavaCompiler} class. Uses the {@link #getDefaultCompilerOptions default}
     * compiler options.
     *
     * @param compiler The Java compiler to use.
     * @param classLoader Class loader to use to resolve dependencies outside of the sources. May be {@code null} if not
     *     available, to use {@link ClassLoader#getSystemClassLoader}.
     */
    public RuntimeJavaCompiler(JavaCompiler compiler, ClassLoader classLoader) {
        this(compiler, getDefaultCompilerOptions(), classLoader);
    }

    /**
     * Constructor for the {@link RuntimeJavaCompiler} class.
     *
     * @param compiler The Java compiler to use.
     * @param options Additional compilation options, or {@code null} if no additional compilation options. These are
     *     just passed along to the Java compiler. See the 'javac' documentation for more information.
     * @param classLoader Class loader to use to resolve dependencies outside of the sources. May be {@code null} if not
     *     available, to use {@link ClassLoader#getSystemClassLoader}.
     */
    public RuntimeJavaCompiler(JavaCompiler compiler, Iterable<String> options, ClassLoader classLoader) {
        this.compiler = compiler;
        this.options = options;

        StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null);
        this.fileManager = new RuntimeJavaFileManager(manager, classLoader);
    }

    /**
     * Returns the default compiler options.
     *
     * @return The default compiler options.
     */
    public static List<String> getDefaultCompilerOptions() {
        return list("-nowarn", "-source", "11", "-target", "11");
    }

    /**
     * Returns a new instance of the requested Java compiler.
     *
     * @param name The name of the Java compiler implementation to use. Specify {@code "jdk"} to use the Java compiler
     *     provided by the JDK, see {@link ToolProvider#getSystemJavaCompiler}, which is part of JDKs but of JREs.
     *     Specify {@code "eclipse"} to use the Eclipse Compiler for Java (ecj) which is part of the Eclipse Java
     *     Development Tools (JDT). Specify {@code null} to use the default compiler.
     * @return The system Java compiler.
     * @throws DependencyException If the requested Java compiler can not be obtained, for instance because a JRE is
     *     used instead of a JDK.
     */
    public static JavaCompiler getJavaCompiler(String name) {
        if (name == null || name.equals("eclipse")) {
            return RuntimeJavaCompilerJdtUtil.createEclipseCompiler();
        } else if (name.equals("jdk")) {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                String msg = fmt(
                        "Failed to locate Java compiler. The current Java runtime appears to be a JRE "
                                + "(Java Runtime Environment), while a JDK (Java Development Kit) is required: \"%s\".",
                        System.getProperty("java.home"));
                throw new DependencyException(msg);
            }
            return compiler;
        } else {
            throw new RuntimeException("Unknown Java compiler name: " + name);
        }
    }

    /**
     * Performs run-time in-memory compilation for the given sources.
     *
     * @param sources Mapping from absolute class names to input file objects that can be used to obtain the source
     *     code.
     * @throws RuntimeJavaCompilerException If compilation failed.
     */
    public void compile(Map<String, JavaInputFileObject> sources) throws RuntimeJavaCompilerException {
        // Add sources.
        for (Entry<String, JavaInputFileObject> source: sources.entrySet()) {
            fileManager.addSourceFile(source.getKey(), source.getValue());
        }

        // Construct compilation task.
        CompilationTask task = compiler.getTask(output, fileManager, diagnostics, options, null, sources.values());

        // Run compiler.
        Boolean result = task.call();

        // Check for failure.
        if (result == null || !result) {
            throw new RuntimeJavaCompilerException(this, sources);
        }
    }

    /**
     * Returns the class loader for this run-time Java compiler. It can be used to load the classes generated by this
     * compiler.
     *
     * @return The class loader for this run-time Java compiler.
     */
    public RuntimeClassLoader getClassLoader() {
        return fileManager.getClassLoader();
    }

    /**
     * Returns the diagnostics that resulted from compilation.
     *
     * @return The diagnostics that resulted from compilation.
     */
    public DiagnosticCollector<JavaFileObject> getDiagnostics() {
        return diagnostics;
    }

    /**
     * Returns the collected compiler output.
     *
     * @return The collected compiler output.
     */
    public CharArrayWriter getOutput() {
        return output;
    }

    /**
     * Loads a class that was generated by this compiler, or accessible from the parent class loader (the one used to
     * load the dependencies).
     *
     * @param <T> Loaded class
     * @param absClassName The absolute class name of the class to load.
     * @return The loaded class.
     */
    public <T> Class<T> loadClass(String absClassName) {
        try {
            @SuppressWarnings("unchecked")
            Class<T> rslt = (Class<T>)getClassLoader().loadClass(absClassName);
            return rslt;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + absClassName, e);
        }
    }

    /**
     * Wrapper around the constructor for the {@link URI} class, which converts {@link URISyntaxException} exceptions to
     * {@link RuntimeException} exceptions.
     *
     * @param uri The textual representation of the URI.
     * @return The newly constructed {@link URI}.
     * @see URI#URI
     */
    public static URI createURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI creation failed: " + uri, e);
        }
    }

    /**
     * Creates a {@link URI} for an in-memory representation of a Java class, either in source in compiled form.
     *
     * @param absClassName The absolute class name. May contain both {@code '.'} and {@code '/'} as package separators.
     * @param kind The kind of file. Use {@link Kind#SOURCE} for Java source code, and {@link Kind#CLASS} for Java class
     *     files.
     * @return The newly constructed {@link URI}.
     */
    public static URI createMemoryURI(String absClassName, Kind kind) {
        String extension = kind.extension;
        Assert.check(extension != null && extension.length() > 0);
        String uriName = "memory:///" + absClassName.replace('.', '/') + extension;
        return createURI(uriName);
    }

    /**
     * Writes the generated class files to a JAR file.
     *
     * @param jarPath The absolute local file system path of the JAR file to write to.
     * @param mainClass The name of the main class, or {@code null} if not applicable.
     * @param resources Mapping from absolute resource paths to the data for the resources. May be {@code null} if no
     *     resources.
     * @throws InputOutputException If the file could not be created, an I/O error occurred, or the file could not be
     *     closed.
     */
    public void writeJarFile(String jarPath, String mainClass, Map<String, ByteArrayOutputStream> resources) {
        // Create manifest.
        Manifest manifest = new Manifest();
        Attributes attrs = manifest.getMainAttributes();
        attrs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        if (mainClass != null) {
            attrs.put(Attributes.Name.MAIN_CLASS, mainClass);
        }

        // Open JAR file for writing.
        try (FileOutputStream stream = new FileOutputStream(jarPath);
             JarOutputStream jarStream = new JarOutputStream(stream, manifest))
        {
            // Add class files.
            for (Entry<String, JavaClassFileObject> entry: getClassLoader().generatedClasses.entrySet()) {
                String absClassName = entry.getKey();
                byte[] byteCode = entry.getValue().getByteCodeArray();
                String entryName = absClassName.replace('.', '/') + ".class";
                Assert.check(entryName.equals(entry.getValue().getName()));
                JarEntry jarEntry = new JarEntry(entryName);
                try {
                    jarStream.putNextEntry(jarEntry);
                    jarStream.write(byteCode, 0, byteCode.length);
                    jarStream.closeEntry();
                } catch (IOException e) {
                    String msg = fmt("Failed to write class file for class \"%s\".", absClassName);
                    throw new IOException(msg, e);
                }
            }

            // Add resources.
            if (resources != null) {
                for (Entry<String, ByteArrayOutputStream> resource: resources.entrySet()) {
                    String resPath = resource.getKey();
                    byte[] resBytes = resource.getValue().toByteArray();
                    JarEntry jarEntry = new JarEntry(resPath);
                    try {
                        jarStream.putNextEntry(jarEntry);
                        jarStream.write(resBytes, 0, resBytes.length);
                        jarStream.closeEntry();
                    } catch (IOException e) {
                        String msg = fmt("Failed to write resource data for resource \"%s\".", resPath);
                        throw new IOException(msg, e);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            String msg = fmt("Could not create JAR file \"%s\".", jarPath);
            throw new InputOutputException(msg, e);
        } catch (IOException e) {
            String msg = fmt("Could not write JAR file \"%s\".", jarPath);
            throw new InputOutputException(msg, e);
        }
    }
}
