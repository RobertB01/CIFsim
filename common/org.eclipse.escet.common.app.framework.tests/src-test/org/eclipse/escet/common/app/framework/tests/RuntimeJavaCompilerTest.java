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

package org.eclipse.escet.common.app.framework.tests;

import static org.eclipse.escet.common.java.Maps.map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.app.framework.javacompiler.JavaCharSeqInputFileObject;
import org.eclipse.escet.common.app.framework.javacompiler.JavaInputFileObject;
import org.eclipse.escet.common.app.framework.javacompiler.ResourceClassLoader;
import org.eclipse.escet.common.app.framework.javacompiler.RuntimeJavaCompiler;
import org.eclipse.escet.common.app.framework.javacompiler.RuntimeJavaCompilerException;
import org.junit.Test;

/** Tests for the {@link RuntimeJavaCompiler}. */
public abstract class RuntimeJavaCompilerTest {
    /** The run-time Java compiler to use. */
    private final RuntimeJavaCompiler compiler = newCompiler(getClass().getClassLoader());

    /** The absolute class name of the class to compile (1). */
    private static final String ABS_CLS_NAME1 = "my.pkg.MyClass";

    /** The source code of the class to compile (1). */
    private static final String SRC1 = String.join("\n", //
            "package my.pkg;", //
            "public class MyClass implements " + RuntimeJavaCompilerTest.class.getName() + ".MyInterface {", //
            "    @Override", //
            "    public String getText() {", //
            "        return \"abc\";", //
            "    }", //
            "}" //
    );

    /** The absolute class name of the class to compile (2). */
    private static final String ABS_CLS_NAME2 = "my.pkg.MyClass2";

    /** The source code of the class to compile (2). */
    private static final String SRC2 = String.join("\n", //
            "package my.pkg;", //
            "public class MyClass2 implements " + RuntimeJavaCompilerTest.class.getName() + ".MyInterface {", //
            "    @Override", //
            "    public String getText() {", //
            "        return \"def\";", //
            "    }", //
            "}" //
    );

    /** The absolute class name of the class to compile (3). */
    private static final String ABS_CLS_NAME3 = "my.pkg.MyClass";

    /** The source code of the class to compile (3). */
    private static final String SRC3 = String.join("\n", //
            "package my.pkg;", //
            "public class MyClass implements " + RuntimeJavaCompilerTest.class.getName() + ".MyInterface {", //
            "    @Override", //
            "    public String getText() {", //
            "        return \"ghi\";", //
            "    }", //
            "}" //
    );

    /** The absolute class name of the class to compile (4). */
    private static final String ABS_CLS_NAME4 = "my.pkg.MyClass";

    /** The source code of the class to compile (4). */
    private static final String SRC4 = String.join("\n", //
            "package my.pkg;", //
            "public class MyClass implements " + RuntimeJavaCompilerTest.class.getName() + ".MyInterface {", //
            "    @Override", //
            "    public String getText() {", //
            "        return new MyInnerClass().getText();", //
            "    }", //
            "", //
            "    public class MyInnerClass implements " + RuntimeJavaCompilerTest.class.getName() + ".MyInterface {", //
            "        @Override", //
            "        public String getText() {", //
            "            return \"jkl\";", //
            "        }", //
            "    }", //
            "}" //
    );

    /** The absolute class name of the class to compile (5). */
    private static final String ABS_CLS_NAME5 = "my.pkg.MyClass";

    /** The source code of the class to compile (5). */
    private static final String SRC5 = String.join("\n", //
            "package my.pkg;", //
            "public class MyClass implements " + RuntimeJavaCompilerTest.class.getName() + ".MyInterface {", //
            "    @Override", //
            "    public String getText() {", //
            "        return new MyInnerClass().getText();", //
            "    }", //
            "", //
            "    public static class MyInnerClass implements " + RuntimeJavaCompilerTest.class.getName()
                    + ".MyInterface {", //
            "        @Override", //
            "        public String getText() {", //
            "            return \"jkl\";", //
            "        }", //
            "    }", //
            "}"//
    );

    /** The absolute class name of the class to compile (6). */
    private static final String ABS_CLS_NAME6 = "my.pkg.MyClass2";

    /** The source code of the class to compile (6). */
    private static final String SRC6 = String.join("\n", //
            "package my.pkg;",
            "public class MyClass2 implements " + RuntimeJavaCompilerTest.class.getName() + ".MyInterface {", //
            "    @Override", //
            "    public String getText() {", //
            "        return new MyClass().getText();", //
            "    }", //
            "}" //
    );

    /** The absolute class name of the class to compile (7). */
    private static final String ABS_CLS_NAME7 = "my.pkg.MyClass";

    /** The source code of the class to compile (7). */
    private static final String SRC7 = String.join("\n", //
            "package my.pkg;", //
            "import java.io.InputStream;", //
            "import java.io.IOException;", //
            "import java.nio.charset.Charset;", //
            "public class MyClass {", //
            "    public String getText() throws IOException {", //
            "       String path = getClass().getName().replace('.', '/') + \".dat\";", //
            "       InputStream stream = getClass().getClassLoader().getResourceAsStream(path);", //
            "       byte[] bytes = new byte[1024];", //
            "       int cnt = stream.read(bytes, 0, bytes.length);", //
            "       return new String(bytes, 0, cnt, Charset.forName(\"UTF-8\"));", //
            "    }", //
            "}" //
    );

    /** The absolute resource name of the resource to use (7). */
    private static final String ABS_RES_NAME7 = "my/pkg/MyClass.dat";

    /** The source code of the resource to use (7). */
    private static final String RES7 = "resource text 7";

    /** Compile, load, instantiate, execute. */
    @Test
    public void testSimple() {
        compile(compiler, new String[] {ABS_CLS_NAME1, SRC1});
        Class<MyInterface> loadedClass = compiler.loadClass(ABS_CLS_NAME1);
        MyInterface my = instantiate(loadedClass);
        assertEquals("abc", my.getText());
    }

    /** Compile, load, instantiate, execute (twice, same class). */
    @Test
    public void testSameClassSameCompiler() {
        compile(compiler, new String[] {ABS_CLS_NAME1, SRC1});
        Class<MyInterface> loadedClass1 = compiler.loadClass(ABS_CLS_NAME1);
        MyInterface my1 = instantiate(loadedClass1);
        assertEquals("abc", my1.getText());

        compile(compiler, new String[] {ABS_CLS_NAME1, SRC1});
        Class<MyInterface> loadedClass2 = compiler.loadClass(ABS_CLS_NAME1);
        MyInterface my2 = instantiate(loadedClass2);
        assertEquals("abc", my2.getText());

        assertSame(loadedClass1, loadedClass2);
        assertNotSame(my1, my2);
    }

    /** Compile, load, instantiate, execute (two different classes, same compiler). */
    @Test
    public void testDifferentClassesSameCompiler() {
        compile(compiler, new String[] {ABS_CLS_NAME1, SRC1});
        Class<MyInterface> loadedClass1 = compiler.loadClass(ABS_CLS_NAME1);
        MyInterface my1 = instantiate(loadedClass1);
        assertEquals("abc", my1.getText());

        compile(compiler, new String[] {ABS_CLS_NAME2, SRC2});
        Class<MyInterface> loadedClass2 = compiler.loadClass(ABS_CLS_NAME2);
        MyInterface my2 = instantiate(loadedClass2);
        assertEquals("def", my2.getText());

        assertNotSame(loadedClass1, loadedClass2);
        assertNotSame(my1, my2);
    }

    /** Compile, load, instantiate, execute (two different classes, same name, different compilers). */
    @Test
    public void testSameNameDifferentCompilers() {
        compile(compiler, new String[] {ABS_CLS_NAME1, SRC1});
        Class<MyInterface> loadedClass1 = compiler.loadClass(ABS_CLS_NAME1);
        MyInterface my1 = instantiate(loadedClass1);
        assertEquals("abc", my1.getText());

        ClassLoader dependencyLoader = getClass().getClassLoader();
        RuntimeJavaCompiler compiler2 = newCompiler(dependencyLoader);
        compile(compiler2, new String[] {ABS_CLS_NAME3, SRC3});
        Class<MyInterface> loadedClass2 = compiler2.loadClass(ABS_CLS_NAME3);
        MyInterface my2 = instantiate(loadedClass2);
        assertEquals("ghi", my2.getText());

        assertEquals(loadedClass1.getName(), loadedClass1.getName());
        assertNotSame(loadedClass1, loadedClass2);
        assertNotSame(my1, my2);
    }

    /** Compile, load, instantiate, execute (inner class). */
    @Test
    public void testInnerClass() {
        compile(compiler, new String[] {ABS_CLS_NAME4, SRC4});
        Class<MyInterface> loadedClass1 = compiler.loadClass(ABS_CLS_NAME4);
        MyInterface my1 = instantiate(loadedClass1);
        assertEquals("jkl", my1.getText());
    }

    /** Compile, load, instantiate, execute (static inner class). */
    @Test
    public void testInnerClassStatic() {
        compile(compiler, new String[] {ABS_CLS_NAME5, SRC5});
        Class<MyInterface> loadedClass1 = compiler.loadClass(ABS_CLS_NAME5);
        MyInterface my1 = instantiate(loadedClass1);
        assertEquals("jkl", my1.getText());

        Class<MyInterface> loadedClass2 = compiler.loadClass(ABS_CLS_NAME5 + "$MyInnerClass");
        MyInterface my2 = instantiate(loadedClass2);
        assertEquals("jkl", my2.getText());
    }

    /** Compile, load, instantiate, execute (static inner class). */
    @Test
    public void testDependencyPreviousCompile() {
        compile(compiler, new String[] {ABS_CLS_NAME1, SRC1});
        compiler.loadClass(ABS_CLS_NAME1);

        compile(compiler, new String[] {ABS_CLS_NAME6, SRC6});
        Class<MyInterface> loadedClass = compiler.loadClass(ABS_CLS_NAME6);
        MyInterface my = instantiate(loadedClass);
        assertEquals("abc", my.getText());
    }

    /** Compile, load, instantiate, execute (get resource at runtime). */
    @Test
    public void testGetResourceAtRuntime() {
        // Create resource class loader.
        ResourceClassLoader resLoader = new ResourceClassLoader();
        Charset charset = Charset.forName("UTF-8");
        resLoader.resources.put(ABS_RES_NAME7, RES7.getBytes(charset));

        // Compile and get class loader.
        RuntimeJavaCompiler compiler2 = newCompiler(resLoader);
        compile(compiler2, new String[] {ABS_CLS_NAME7, SRC7});

        // Load class.
        Class<?> loadedClass = compiler2.loadClass(ABS_CLS_NAME7);

        // Instantiate class.
        Object instance;
        try {
            instance = loadedClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException ex) {
            throw new RuntimeException("Failed to create instance.", ex);
        }

        // Invoke method.
        Method method;
        try {
            method = loadedClass.getMethod("getText");
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException("Failed to get method.", ex);
        }

        Object rslt;
        try {
            rslt = method.invoke(instance);
        } catch (Throwable ex) {
            throw new RuntimeException("Failed to invoke method.", ex);
        }

        // Check result.
        assertEquals(RES7, rslt);
    }

    /** Test the getResourceAsStream method by obtaining byte code. */
    @Test
    public void testGetClassAsStream() {
        compile(compiler, new String[] {ABS_CLS_NAME1, SRC1});
        InputStream s = compiler.getClassLoader().getResourceAsStream(ABS_CLS_NAME1.replace('.', '/') + ".class");
        assertNotNull(s);
    }

    /**
     * Returns the name of the Java compiler to use. See {@link RuntimeJavaCompiler#getJavaCompiler} for details on the
     * supported names.
     *
     * @return The name of the Java compiler to use.
     */
    protected abstract String getCompilerName();

    /**
     * Returns a new run-time Java compiler.
     *
     * @param dependencyLoader Class loader to use to resolve dependencies outside of the sources, or {@code null} if
     *     not available.
     * @return A new run-time Java compiler.
     */
    private RuntimeJavaCompiler newCompiler(ClassLoader dependencyLoader) {
        // Get compiler name/options.
        String name = getCompilerName();
        List<String> options = RuntimeJavaCompiler.getDefaultCompilerOptions();

        // Disable annotation processing. JDT compiler uses non-OSGi-compatible class loading via reflection to load
        // the annotation processor cloass. This leads to class not found errors for the Maven build.
        options.add("-proc:none");

        // Create the Java compiler.
        return new RuntimeJavaCompiler(name, options, dependencyLoader);
    }

    /**
     * Compiles the given sources using the given compiler.
     *
     * @param compiler The compiler to use for compilation.
     * @param input The sources to compile. The number of entries must be even, where each two inputs are the absolute
     *     class name and the corresponding source code.
     */
    private void compile(RuntimeJavaCompiler compiler, String[] input) {
        // Get sources.
        Map<String, JavaInputFileObject> sources = map();
        assertTrue(input.length % 2 == 0);
        for (int i = 0; i < input.length; i += 2) {
            String absClassName = input[i];
            String sourceCode = input[i + 1];
            JavaInputFileObject file = new JavaCharSeqInputFileObject(absClassName, sourceCode);
            sources.put(absClassName, file);
        }

        // Compile the sources.
        try {
            compiler.compile(sources);
        } catch (RuntimeJavaCompilerException e) {
            throw new RuntimeException("Compilation failed.", e);
        }
    }

    /**
     * Instantiates the run-time compiled class.
     *
     * @param cls The class to instantiate.
     * @return The new instance of the class.
     */
    private static MyInterface instantiate(Class<MyInterface> cls) {
        MyInterface rslt;
        try {
            rslt = cls.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create instance.", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to create instance.", e);
        } catch (SecurityException e) {
            throw new RuntimeException("Failed to create instance.", e);
        }
        return rslt;
    }

    /** Test interface for the generated class. */
    public interface MyInterface {
        /**
         * Returns some text.
         *
         * @return Some text.
         */
        public String getText();
    }
}
