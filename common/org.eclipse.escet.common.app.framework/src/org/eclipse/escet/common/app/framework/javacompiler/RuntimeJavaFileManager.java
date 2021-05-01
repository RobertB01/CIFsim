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

import static org.eclipse.escet.common.java.Maps.map;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.framework.wiring.BundleWiring;

/** File manager used by the {@link RuntimeJavaCompiler}. */
@SuppressWarnings("restriction")
public class RuntimeJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    /**
     * Class loader to use to resolve dependencies outside of the sources. Generated output classes will be available
     * through this class loader as well.
     */
    private final RuntimeClassLoader classLoader;

    /** Absolute class name to input file object mapping. */
    private final Map<String, JavaInputFileObject> sources;

    /**
     * Constructor for the {@link RuntimeJavaCompiler} class.
     *
     * @param fileManager File manager to defer to for additional sources.
     * @param classLoader Class loader to use to resolve dependencies outside of the sources. May be {@code null} if not
     *     available, to use {@link ClassLoader#getSystemClassLoader}.
     */
    public RuntimeJavaFileManager(JavaFileManager fileManager, ClassLoader classLoader) {
        super(fileManager);
        this.classLoader = new RuntimeClassLoader(classLoader);
        this.sources = map();
    }

    /**
     * Adds a source file to the file manager.
     *
     * @param absClassName The absolute class name.
     * @param source The input file object that can be used to retrieve the source code.
     */
    public void addSourceFile(String absClassName, JavaInputFileObject source) {
        absClassName = normalizeName(absClassName);
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeJavaFileManager.addSourceFile: absClassName=" + absClassName);
        }
        sources.put(absClassName, source);
    }

    /**
     * Returns the class loader for this file manager. It can be used to resolve the classes generated by the run-time
     * Java compiler.
     *
     * @return The class loader for this file manager.
     */
    public RuntimeClassLoader getClassLoader() {
        return getClassLoader(null);
    }

    /**
     * Returns the class loader for this file manager. It can be used to resolve the classes generated by the run-time
     * Java compiler.
     *
     * @param location The location to return the class loader for. This parameter is ignored.
     * @return The class loader for this file manager.
     */
    @Override
    public RuntimeClassLoader getClassLoader(Location location) {
        return classLoader;
    }

    @Override
    public boolean hasLocation(Location location) {
        if (location == StandardLocation.SOURCE_PATH) {
            return true;
        }
        if (location == StandardLocation.CLASS_PATH) {
            return true;
        }
        if (location == StandardLocation.CLASS_OUTPUT) {
            return true;
        }
        return super.hasLocation(location);
    }

    @Override
    public boolean isSameFile(FileObject f1, FileObject f2) {
        if (f1 instanceof JavaInputFileObject) {
            return f1.equals(f2);
        }
        if (f1 instanceof JavaClassFileObject) {
            return f1.equals(f2);
        }
        if (f1 instanceof OsgiClassFileObject) {
            return f1.equals(f2);
        }
        return super.isSameFile(f1, f2);
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeJavaFileManager.getJavaFileForInput: location=" + location + " className="
                    + className + " kind=" + kind);
        }

        // Source file.
        if (location == StandardLocation.SOURCE_PATH && kind == Kind.SOURCE) {
            String normalizedClassName = normalizeName(className);
            for (JavaInputFileObject file: sources.values()) {
                if (file.getKind() != Kind.SOURCE) {
                    continue;
                }
                if (file.absClassName.equals(normalizedClassName)) {
                    if (RuntimeJavaCompiler.DEBUG) {
                        System.out.println("RuntimeJavaFileManager.getJavaFileForInput: rslt=" + file);
                    }
                    return file;
                }
            }
        }

        // Already compiled class.
        if (location == StandardLocation.CLASS_PATH && kind == Kind.CLASS) {
            String normalizedClassName = normalizeName(className);
            for (JavaClassFileObject file: classLoader.generatedClasses.values()) {
                if (file.getKind() != Kind.CLASS) {
                    continue;
                }
                if (file.absClassName.equals(normalizedClassName)) {
                    if (RuntimeJavaCompiler.DEBUG) {
                        System.out.println("RuntimeJavaFileManager.getJavaFileForInput: rslt=" + file);
                    }
                    return file;
                }
            }
        }

        // Special case for OSGi class loaders. Ideally, the Java compiler API
        // would use the class loaders to resolve dependencies during
        // compilation. In reality however, it uses the file managers for this.
        // The default file manager only uses the system class path, so to
        // support Eclipse bundles, we need to do some additional work here.
        if (location == StandardLocation.CLASS_PATH && kind == Kind.CLASS) {
            // Get the dependency class loader, if any.
            ClassLoader dependencyLoader = classLoader.getParent();

            // If we have a dependency class loader, and it is an OSGi class
            // loader, to some additional work.
            if (dependencyLoader != null && dependencyLoader instanceof BundleReference) {
                // Get resources from OSGi class loader.
                String normalizedClassName = normalizeName(className);
                String pkgName = getPackageName(normalizedClassName);
                String resPkgName = pkgName.replace('.', '/');
                String resName = getClassName(normalizedClassName) + ".class";

                BundleReference ref = (BundleReference)dependencyLoader;
                Bundle bundle = ref.getBundle();
                BundleWiring wiring = bundle.adapt(BundleWiring.class);
                Collection<String> resources;
                resources = wiring.listResources(resPkgName, resName, 0);

                // Process results.
                if (!resources.isEmpty()) {
                    // Get resource path.
                    Assert.check(resources.size() == 1);
                    String resource = resources.iterator().next();

                    // Get class URI.
                    URI uri;
                    try {
                        uri = dependencyLoader.getResource(resource).toURI();
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    // Return first match.
                    return new OsgiClassFileObject(normalizedClassName, uri, dependencyLoader);
                }
            }
        }

        // Defer the request.
        if (super.hasLocation(location)) {
            return super.getJavaFileForInput(location, className, kind);
        }

        // Not found.
        return null;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
            throws IOException
    {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeJavaFileManager.getJavaFileForOutput: location=" + location + " className="
                    + className + " kind=" + kind + " sibling=" + sibling);
        }

        if (location == StandardLocation.CLASS_OUTPUT && kind == Kind.CLASS) {
            className = normalizeName(className);
            JavaClassFileObject rslt = new JavaClassFileObject(className);
            if (RuntimeJavaCompiler.DEBUG) {
                System.out.println("RuntimeJavaFileManager.getJavaFileForOutput: rslt=" + rslt);
            }
            classLoader.add(className, rslt);
            return rslt;
        }

        // Defer the request.
        if (super.hasLocation(location)) {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }

        // Not found.
        return null;
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        String rslt;
        if (file instanceof JavaInputFileObject) {
            rslt = ((JavaInputFileObject)file).absClassName;
        } else if (file instanceof JavaClassFileObject) {
            rslt = ((JavaClassFileObject)file).absClassName;
        } else if (file instanceof OsgiClassFileObject) {
            rslt = ((OsgiClassFileObject)file).absClassName;
        } else if (super.hasLocation(location)) {
            rslt = super.inferBinaryName(location, file);
        } else {
            return null;
        }

        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeJavaFileManager.inferBinaryName: location=" + location + " file=" + file
                    + " file.getClass()=" + file.getClass() + " rslt=" + rslt);
        }

        return rslt;
    }

    /**
     * Returns the absolute name of the Java class for the given Java file object.
     *
     * @param file The Java file object.
     * @return The absolute name of the Java class.
     */
    public String getAbsClassname(JavaFileObject file) {
        if (file instanceof JavaInputFileObject) {
            return ((JavaInputFileObject)file).absClassName;
        } else if (file instanceof JavaClassFileObject) {
            return ((JavaClassFileObject)file).absClassName;
        } else if (file instanceof OsgiClassFileObject) {
            return ((OsgiClassFileObject)file).absClassName;
        } else {
            String name = file.getName();
            Assert.check(!(name.contains("/") && name.contains(".")));
            return file.getName().replace('/', '.');
        }
    }

    /**
     * Returns the absolute name of the Java package for the given Java file object.
     *
     * @param file The Java file object.
     * @return The absolute name of the Java package.
     */
    public String getPackageName(JavaFileObject file) {
        String absClassName = getAbsClassname(file);
        return getPackageName(absClassName);
    }

    /**
     * Returns the absolute name of the Java package for the given absolute Java class name.
     *
     * @param absClassName The absolute Java class name.
     * @return The absolute name of the Java package.
     */
    public String getPackageName(String absClassName) {
        int idx = absClassName.lastIndexOf('.');
        String pkgName = (idx == -1) ? "" : absClassName.substring(0, idx);
        return pkgName;
    }

    /**
     * Returns the name of the Java class for the given absolute Java class name.
     *
     * @param absClassName The absolute Java class name.
     * @return The name of the Java class.
     */
    public String getClassName(String absClassName) {
        int idx = absClassName.lastIndexOf('.');
        return (idx == -1) ? absClassName : absClassName.substring(idx + 1);
    }

    /**
     * Is the given Java file object located in the given package? This is a syntactic check.
     *
     * @param file The Java file object.
     * @param packageName The absolute package name. Must contain only {@code '.'} as package separators, not
     *     {@code '/'}.
     * @param recursive Whether to look recursively in sub-packages of the package with the given name ({@code true}) or
     *     only look in the package with the given name.
     * @return {@code true} if the given Java file object is located in the given package, {@code false} otherwise.
     */
    public boolean inPackage(JavaFileObject file, String packageName, boolean recursive) {
        // Get package names.
        String filePkgName = getPackageName(file);

        // Check for non-recursive package match.
        if (filePkgName.equals(packageName)) {
            return true;
        }
        if (!recursive) {
            return false;
        }

        // Check for recursive package match.
        if (filePkgName.startsWith(packageName + ".")) {
            return true;
        }
        return false;
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
            throws IOException
    {
        if (RuntimeJavaCompiler.DEBUG) {
            System.out.println("RuntimeJavaFileManager.list: location=" + location + " packageName=" + packageName
                    + " kinds=" + kinds + " recurse=" + recurse);
        }

        // Normalize package name.
        String normalizedPkgName = normalizeName(packageName);

        // Initialize results.
        List<JavaFileObject> rslt = Lists.list();

        // Add our own source files, if applicable.
        if (location == StandardLocation.SOURCE_PATH && kinds.contains(Kind.SOURCE)) {
            for (JavaInputFileObject file: sources.values()) {
                if (file.getKind() != Kind.SOURCE) {
                    continue;
                }
                if (inPackage(file, normalizedPkgName, recurse)) {
                    rslt.add(file);
                }
            }
        }

        // Add our own generated class files, if applicable.
        if (location == StandardLocation.CLASS_PATH && kinds.contains(Kind.CLASS)) {
            for (JavaClassFileObject file: classLoader.generatedClasses.values()) {
                if (file.getKind() != Kind.CLASS) {
                    continue;
                }
                if (inPackage(file, normalizedPkgName, recurse)) {
                    rslt.add(file);
                }
            }
        }

        // Added deferred results.
        if (super.hasLocation(location)) {
            Iterable<JavaFileObject> subResults;
            subResults = super.list(location, packageName, kinds, recurse);
            for (JavaFileObject file: subResults) {
                rslt.add(file);
            }
        }

        // Special case for OSGi class loaders. Ideally, the Java compiler API
        // would use the class loaders to resolve dependencies during
        // compilation. In reality however, it uses the file managers for this.
        // The default file manager only uses the system class path, so to
        // support Eclipse bundles, we need to do some additional work here.
        if (location == StandardLocation.CLASS_PATH && kinds.contains(Kind.CLASS)) {
            // Get the dependency class loader, if any.
            ClassLoader dependencyLoader = classLoader.getParent();

            // If we have a dependency class loader, and it is an OSGi class
            // loader, to some additional work.
            if (dependencyLoader != null && dependencyLoader instanceof BundleReference) {
                // Get resources from OSGi class loader.
                String resourcePkgName = normalizedPkgName.replace('.', '/');
                BundleReference ref = (BundleReference)dependencyLoader;
                Bundle bundle = ref.getBundle();
                BundleWiring wiring = bundle.adapt(BundleWiring.class);
                Collection<String> resources;
                resources = wiring.listResources(resourcePkgName, "*.class",
                        recurse ? BundleWiring.LISTRESOURCES_RECURSE : 0);

                // Add the class file resources.
                for (String resource: resources) {
                    // Get absolute class name.
                    Assert.check(resource.endsWith(".class"));
                    int extLength = Kind.CLASS.extension.length();
                    String absClassName = resource.substring(0, resource.length() - extLength).replace('/', '.');

                    // Get class URI.
                    URI uri;
                    try {
                        uri = dependencyLoader.getResource(resource).toURI();
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    // Add file object.
                    rslt.add(new OsgiClassFileObject(absClassName, uri, dependencyLoader));
                }
            }
        }

        // Debug results.
        if (RuntimeJavaCompiler.DEBUG) {
            for (JavaFileObject file: rslt) {
                System.out.println("  --> " + file);
            }
        }

        // Return combined results.
        return rslt;
    }

    /**
     * Returns a normalized Java class or package name. The Java compiler API supports both {@code '.'} and {@code '/'}
     * characters, see {@link JavaFileManager}. This method normalizes to {@code '.'} characters.
     *
     * @param name The absolute Java class or package name.
     * @return The normalized absolute Java class or package name.
     * @see JavaFileManager
     */
    public static String normalizeName(String name) {
        Assert.check(!name.contains("\\"));
        Assert.check(!(name.contains("/") && name.contains(".")));
        String rslt = name.replace("/", ".");
        return rslt;
    }
}
