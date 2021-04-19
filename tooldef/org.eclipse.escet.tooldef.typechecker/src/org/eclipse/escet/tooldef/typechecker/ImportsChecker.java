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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newBoolType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newDoubleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newIntType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newJavaTool;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newListType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newLongType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newObjectType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newScript;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSetType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newStringType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newToolParameter;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTupleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeParam;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTypeParamRef;

import java.io.InputStream;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.common.typechecker.SemanticProblem;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;
import org.eclipse.escet.setext.runtime.SyntaxWarning;
import org.eclipse.escet.setext.runtime.Token;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;
import org.eclipse.escet.tooldef.common.ClassLoaderObtainer;
import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.common.ToolDefValidationUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.Import;
import org.eclipse.escet.tooldef.metamodel.tooldef.JavaImport;
import org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefImport;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType;
import org.eclipse.escet.tooldef.parser.ToolDefParser;
import org.eclipse.escet.tooldef.runtime.ToolDefTupleNary;
import org.eclipse.escet.tooldef.runtime.ToolDefTuplePair;

/** ToolDef import type checker. */
public class ImportsChecker {
    /** Constructor for the {@link ImportsChecker} class. */
    private ImportsChecker() {
        // Static class.
    }

    /**
     * Type check an import.
     *
     * @param imp The import.
     * @param ctxt The type checker context.
     */
    public static void tcheck(Import imp, CheckerContext ctxt) {
        if (imp instanceof ToolDefImport) {
            tcheck((ToolDefImport)imp, ctxt);
        } else if (imp instanceof JavaImport) {
            tcheck((JavaImport)imp, ctxt);
        } else {
            throw new RuntimeException("Unknown import: " + imp);
        }
    }

    /**
     * Type check a ToolDef import.
     *
     * @param imp The ToolDef import.
     * @param ctxt The type checker context.
     */
    private static void tcheck(ToolDefImport imp, CheckerContext ctxt) {
        // Get position of the import.
        Position importPos = (imp.getAsName() != null) ? imp.getAsName().position
                : (imp.getOrigName() == null) ? imp.getPosition() : imp.getOrigName().position;

        // Get root type checker context resulting from type checking the
        // ToolDef script referred to by the import.
        CheckerContext impCtxt = getScript(imp, ctxt);

        // Whole script vs parts of it.
        if (imp.getOrigName() == null) {
            // Whole script. Get import name.
            String name = null;
            if (imp.getAsName() != null) {
                name = imp.getAsName().text;
            }
            if (name == null) {
                // Derive import name from source file path.
                name = imp.getSource().text;

                // Handle different types of imports.
                if (name.startsWith("lib:")) {
                    // Library path.
                    name = name.substring("lib:".length());
                } else {
                    // Absolute/relative local file system path or ToolDef URI.

                    // Strip of the path.
                    int idx = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
                    if (idx != -1) {
                        name = name.substring(idx + 1);
                    }

                    // Strip of the file extension.
                    idx = name.lastIndexOf('.');
                    if (idx != -1) {
                        name = name.substring(0, idx);
                    }
                }

                // Check derived name.
                if (!ToolDefValidationUtils.isValidIdentifier(name)) {
                    ctxt.addProblem(Message.IMPORT_FILE_INVALID_NAME, imp.getSource().position, name,
                            imp.getSource().text);
                    throw new SemanticException();
                }
            }

            // Create new script for the imported script. Set the new name. Set
            // position information to the position information of the import,
            // to ensure we report duplicate name problems on the import.
            Script newScript = newScript(null, name, deepclone(importPos));

            // Add new script to the importing script, to ensure proper
            // containment, and proper absolute names.
            Script rootScript = (Script)ctxt.getRoot().getScope();
            rootScript.getDeclarations().add(newScript);

            // Create new context for the new script.
            CheckerContext newCtxt = new CheckerContext(ctxt, newScript);

            // Copy the importable declarations, to ensure the original script
            // remains unmodified. Also add them to the new context.
            Script impScript = (Script)impCtxt.getScope();
            Assert.check(impScript.getName() == null);
            for (PositionObject impObj: impCtxt.getImportableObjects()) {
                // Create a copy.
                PositionObject newObj = deepclone(impObj);

                // Add to new script and new context.
                newScript.getDeclarations().add((Declaration)newObj);
                try {
                    newCtxt.addDecl(newObj);
                } catch (SemanticException ex) {
                    // Shouldn't occur, as imported script has no errors.
                    throw new RuntimeException(ex);
                }
            }

            // Add new context to importing context.
            ctxt.addDecl(newCtxt);
        } else {
            // Parts of the script. Get the parts.
            List<PositionObject> objs;
            if (imp.getOrigName().text.equals("*")) {
                objs = impCtxt.getImportableObjects();
            } else {
                objs = impCtxt.getObjects(imp.getOrigName().text);
            }

            // Make sure we found something.
            if (objs == null) {
                Assert.check(!imp.getOrigName().text.equals("*"));
                ctxt.addProblem(Message.IMPORT_FILE_OBJ_NOT_FOUND, imp.getOrigName().position, imp.getOrigName().text);
                // Non-fatal problem.
                return;
            }

            if (objs.isEmpty()) {
                Assert.check(imp.getOrigName().text.equals("*"));
                ctxt.addProblem(Message.IMPORT_FILE_NO_IMPORTABLE_OBJS, imp.getOrigName().position);
                // Non-fatal problem.
                return;
            }

            // Check for supported parts.
            for (PositionObject obj: objs) {
                // Supported imported objects.
                if (obj instanceof Tool) {
                    continue;
                } else if (obj instanceof TypeDecl) {
                    continue;
                }

                // Unsupported imported objects.
                ctxt.addProblem(Message.IMPORT_FILE_OBJ_UNSUPPORTED, imp.getOrigName().position,
                        ToolDefTextUtils.getAbsDescr(obj));
                throw new SemanticException();
            }

            // Create dummy script in which to store copied objects, to ensure
            // they have proper containment.
            Script dummyScript = newScript();

            // Replace position information by the position information of the
            // import, to ensure we report duplicate name problems on the
            // import. Also rename each of the objects, if needed.
            for (int i = 0; i < objs.size(); i++) {
                // Copy the object, to ensure the original remains intact
                // in its original context, and can be used again. Put the
                // copy in the dummy script, to ensure it has proper
                // containment.
                PositionObject obj = objs.get(i);
                obj = deepclone(obj);
                dummyScript.getDeclarations().add((Declaration)obj);

                // Store the copied object.
                objs.set(i, obj);

                // Update position information.
                obj.setPosition(deepclone(importPos));

                // Rename, if needed.
                if (imp.getAsName() != null) {
                    if (obj instanceof Tool) {
                        ((Tool)obj).setName(imp.getAsName().text);
                    } else if (obj instanceof TypeDecl) {
                        ((TypeDecl)obj).setName(imp.getAsName().text);
                    } else {
                        throw new RuntimeException("Unexpected obj: " + obj);
                    }
                }
            }

            // Add imported objects to context.
            for (PositionObject obj: objs) {
                ctxt.addDecl(obj);
            }
        }
    }

    /**
     * Obtains and returns a fully parsed and type checked ToolDef script for a ToolDef import.
     *
     * @param imp The ToolDef import.
     * @param ctxt The type checker context.
     * @return The root checker context resulting from type checking the ToolDef script referred to by the import.
     */
    public static CheckerContext getScript(ToolDefImport imp, CheckerContext ctxt) {
        // Set internal developer debug mode.
        final boolean DEBUG = false;

        // Preparation.
        if (ctxt.tchecker.scripts == null) {
            ctxt.tchecker.scripts = map();
        }

        // Parse imported file.
        String relSrcRef = imp.getSource().text;
        Position sourcePos = imp.getSource().position;
        String absSrcRef;
        Script script;

        try {
            // Resolve import against current file.
            absSrcRef = ctxt.tchecker.resolveImport(relSrcRef, sourcePos, ctxt);

            // Use cache, if possible.
            CheckerContext impCtxt = ctxt.tchecker.scripts.get(absSrcRef);
            if (impCtxt != null) {
                return impCtxt;
            }

            // URI vs local file system path.
            List<SyntaxWarning> warnings;
            if (absSrcRef.startsWith("tooldef://")) {
                // Split URI with 'tooldef' scheme into parts.
                String path = absSrcRef.substring("tooldef://".length());
                int slashIdx = path.indexOf('/');
                String pluginName = (slashIdx == -1) ? "" : path.substring(0, slashIdx);
                String pluginPath = (slashIdx == -1) ? path : path.substring(slashIdx + 1);
                if (pluginName.isEmpty()) {
                    pluginName = null;
                }

                // Get class loader for plug-in.
                ClassLoaderObtainer clObtainer = new ImportClassLoaderObtainer(sourcePos, ctxt, "ToolDef script");
                ClassLoader classLoader = clObtainer.getClassLoader(pluginName);

                // Obtain resource stream.
                InputStream stream = classLoader.getResourceAsStream(pluginPath);
                if (stream == null) {
                    ClassLoader sysLoader = ClassLoader.getSystemClassLoader();
                    String loaderTxt = (classLoader == sysLoader) ? "on the system class path"
                            : fmt("in plug-in (OSGi bundle or Eclipse project) \"%s\"", pluginName);
                    ctxt.addProblem(Message.IMPORT_FILE_RESOURCE_NOT_FOUND, sourcePos, pluginPath, loaderTxt);
                    throw new SemanticException();
                }

                // Parse resource stream. Stream is always closed.
                ToolDefParser parser = new ToolDefParser();
                String src = fmt("ToolDef library \"%s\": ", relSrcRef);
                script = parser.parseStream(stream, absSrcRef, src);
                warnings = parser.getWarnings();
            } else {
                // Parse local file system file.
                ToolDefParser parser = new ToolDefParser();
                script = parser.parseFile(absSrcRef, relSrcRef);
                warnings = parser.getWarnings();
            }

            // Report warnings.
            if (warnings != null && !warnings.isEmpty()) {
                ctxt.addProblem(Message.IMPORT_FILE_SYNTAX_WARNING, sourcePos, relSrcRef);
            }
        } catch (InvalidInputException e) {
            if (DEBUG) {
                String msg = Message.IMPORT_FILE_NOT_FOUND.format(relSrcRef);
                throw new RuntimeException(msg, e);
            }

            ctxt.addProblem(Message.IMPORT_FILE_NOT_FOUND, sourcePos, relSrcRef);
            throw new SemanticException();
        } catch (InputOutputException e) {
            if (DEBUG) {
                String msg = Message.IMPORT_FILE_IO_ERROR.format(relSrcRef);
                throw new RuntimeException(msg, e);
            }

            ctxt.addProblem(Message.IMPORT_FILE_IO_ERROR, sourcePos, relSrcRef);
            throw new SemanticException();
        } catch (SyntaxException e) {
            if (DEBUG) {
                String msg = Message.IMPORT_FILE_SYNTAX_ERROR.format(relSrcRef);
                throw new RuntimeException(msg, e);
            }

            ctxt.addProblem(Message.IMPORT_FILE_SYNTAX_ERROR, sourcePos, relSrcRef);
            throw new SemanticException();
        }

        // Construct cycle information for the new type checker.
        String[][] curCycle = ctxt.tchecker.importCycle;
        String[][] impCycle;
        if (curCycle == null) {
            impCycle = new String[][] {{ctxt.tchecker.getSourceFilePath(), ctxt.tchecker.getSourceFileName()},
                    {absSrcRef, relSrcRef}};
        } else {
            impCycle = new String[curCycle.length + 1][2];
            System.arraycopy(curCycle, 0, impCycle, 0, curCycle.length);
            impCycle[impCycle.length - 1][0] = absSrcRef;
            impCycle[impCycle.length - 1][1] = relSrcRef;
        }

        // Construct new type checker.
        ToolDefTypeChecker newChecker = new ToolDefTypeChecker();
        newChecker.setSourceFilePath(absSrcRef);

        // Type check imported file.
        try {
            newChecker.builtins = ctxt.tchecker.builtins;
            newChecker.scripts = ctxt.tchecker.scripts;
            newChecker.importCycle = impCycle;
            detectImportCycle(newChecker);
            newChecker.typeCheck(script);
        } catch (ImportCycleException e) {
            // Move up the chain until we find the origin of the cycle.
            if (!e.absSrcRef.equals(ctxt.tchecker.getSourceFilePath())) {
                throw e;
            }

            // Report cycle.
            if (DEBUG) {
                String msg = Message.IMPORT_FILE_CYCLE.format(e.cycleTxt);
                throw new RuntimeException(msg);
            }

            ctxt.addProblem(Message.IMPORT_FILE_CYCLE, sourcePos, e.cycleTxt);
            throw new SemanticException();
        }

        // If type checking of the imported file failed, it fails for the
        // current type checker as well.
        if (newChecker.hasError()) {
            for (SemanticProblem problem: newChecker.getProblems()) {
                // Ignore warnings.
                if (problem.severity == SemanticProblemSeverity.WARNING) {
                    continue;
                }

                // Re-report the problem on the import.
                if (DEBUG) {
                    String msg = Message.IMPORT_FILE_SEMANTIC_ERROR.format(relSrcRef, problem.message);
                    throw new RuntimeException(msg);
                }

                ctxt.addProblem(Message.IMPORT_FILE_SEMANTIC_ERROR, sourcePos, relSrcRef, problem.message);
            }
            throw new SemanticException();
        }

        // Get root checker context resulting from type checking the ToolDef
        // script referred to by the import.
        CheckerContext rootCtxt = newChecker.rootCtxt;
        Assert.notNull(rootCtxt);

        // Add root checker context to cache, now that we know it has no errors.
        Assert.notNull(script);
        CheckerContext prevCtxt = ctxt.tchecker.scripts.put(absSrcRef, rootCtxt);
        Assert.check(prevCtxt == null);

        // Return root checker context.
        return rootCtxt;
    }

    /**
     * Detects an import cycle.
     *
     * @param tchecker The ToolDef type checker to use.
     * @throws ImportCycleException If a cycle is detected.
     */
    private static void detectImportCycle(ToolDefTypeChecker tchecker) {
        String[][] cycle = tchecker.importCycle;
        Assert.notNull(cycle);

        // Detect cycle.
        String tcheckerAbsRef = cycle[cycle.length - 1][0];
        Assert.check(tcheckerAbsRef.equals(tchecker.getSourceFilePath()));
        for (int i = 0; i < cycle.length - 1; i++) {
            if (cycle[i][0].equals(tcheckerAbsRef)) {
                // Cycle found.
                StringBuilder txt = new StringBuilder();
                for (int j = i + 1; j < cycle.length; j++) {
                    if (txt.length() > 0) {
                        txt.append(" -> ");
                    }
                    txt.append("\"" + cycle[j][1] + "\"");
                }
                throw new ImportCycleException(tcheckerAbsRef, txt.toString());
            }
        }
    }

    /**
     * Type check a Java import.
     *
     * @param imp The Java import.
     * @param ctxt The type checker context.
     */
    private static void tcheck(JavaImport imp, CheckerContext ctxt) {
        // Get class loader.
        String pluginName = imp.getPluginName() == null ? null : imp.getPluginName().text;
        Position pluginPos = imp.getPluginName() == null ? null : imp.getPluginName().position;
        ClassLoaderObtainer clObtainer = new ImportClassLoaderObtainer(pluginPos, ctxt, "Java code");
        ClassLoader classLoader = clObtainer.getClassLoader(pluginName);

        // Get class and method names.
        String methodAbsName = imp.getMethodName().text;
        int dotIdx = methodAbsName.lastIndexOf('.');
        String classAbsName = (dotIdx == -1) ? "" : methodAbsName.substring(0, dotIdx);
        String methodName = (dotIdx == -1) ? methodAbsName : methodAbsName.substring(dotIdx + 1);

        // Load the class.
        Class<?> cls;
        try {
            cls = classLoader.loadClass(classAbsName);
        } catch (ClassNotFoundException ex) {
            ClassLoader sysLoader = ClassLoader.getSystemClassLoader();
            String loaderTxt = (classLoader == sysLoader) ? "on the system class path"
                    : fmt("in plug-in (OSGi bundle or Eclipse project) \"%s\"", pluginName);
            ctxt.addProblem(Message.IMPORT_JAVA_CLASS_NOT_FOUND, imp.getMethodName().position, classAbsName, loaderTxt);
            throw new SemanticException();
        }
        Assert.notNull(cls);

        // Get methods, and filter for name and being static. We support
        // public, private, protected, default, etc methods.
        Method[] declaredMethods = cls.getDeclaredMethods();
        int matchingNameCnt = 0;
        List<Method> methods = list();
        for (Method method: declaredMethods) {
            if (!methodName.equals(method.getName())) {
                continue;
            }
            matchingNameCnt++;

            if (!Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            methods.add(method);
        }

        // Make sure we found at least one matching method.
        if (methods.isEmpty()) {
            ctxt.addProblem(Message.IMPORT_JAVA_METHOD_NOT_FOUND, imp.getMethodName().position,
                    (matchingNameCnt == 0) ? "" : "static ", methodName, classAbsName);
            throw new SemanticException();
        }

        // Create dummy specification in which to store the imported tools,
        // to ensure they have proper containment.
        Script dummyScript = newScript();

        // Add Java methods to the context.
        Token ctxtToken = imp.getAsName();
        if (ctxtToken == null) {
            ctxtToken = imp.getMethodName();
        }
        for (Method method: methods) {
            // Convert Java method to ToolDef tool signature.
            JavaTool tool = convertJavaMethod(imp, method, ctxt);

            // Add to dummy script for proper containment.
            dummyScript.getDeclarations().add(tool);

            // Add to context.
            ctxt.addDecl(tool);
        }
    }

    /**
     * Converts a Java method to a ToolDef tool.
     *
     * @param imp The ToolDef import that imports the Java method.
     * @param method A resolved Java method.
     * @param ctxt The type checker context.
     * @return A ToolDef tool representation of the Java method.
     */
    private static JavaTool convertJavaMethod(JavaImport imp, Method method, CheckerContext ctxt) {
        // Get method name.
        String methodName = imp.getMethodName().text;
        int dotIdx = methodName.lastIndexOf('.');
        if (dotIdx != -1) {
            methodName = methodName.substring(dotIdx + 1);
        }

        // Get tool name and position.
        String toolName = methodName;
        if (imp.getAsName() != null) {
            toolName = imp.getAsName().text;
        }

        Position toolPos = imp.getMethodName().position;
        if (imp.getAsName() != null) {
            toolPos = imp.getAsName().position;
        }

        // Get type parameters.
        TypeVariable<Method>[] javaTypeParams = method.getTypeParameters();
        List<TypeParam> typeParams = listc(javaTypeParams.length);
        Map<TypeVariable<Method>, TypeParam> typeParamMap = mapc(javaTypeParams.length);
        for (TypeVariable<Method> javaTypeParam: javaTypeParams) {
            // Check.
            boolean boundOk = javaTypeParam.getBounds().length == 0
                    || (javaTypeParam.getBounds().length == 1 && javaTypeParam.getBounds()[0] == Object.class);
            if (!boundOk) {
                ctxt.addProblem(Message.IMPORT_JAVA_TYPE_PARAM_BOUNDED, toolPos, imp.getMethodName().text,
                        javaTypeParam.getName());
                throw new SemanticException();
            }

            // Convert.
            TypeParam typeParam = newTypeParam(javaTypeParam.getName(), null);

            // Store.
            typeParams.add(typeParam);
            TypeParam prev = typeParamMap.put(javaTypeParam, typeParam);
            Assert.check(prev == null);
        }

        // Get return type.
        ToolDefType retType = convertJavaType(method.getGenericReturnType(), "return", toolPos, typeParamMap, ctxt,
                false, true);

        // Get parameter types.
        Parameter[] javaParams = method.getParameters();
        List<ToolParameter> params = listc(javaParams.length);
        for (int i = 0; i < javaParams.length; i++) {
            Parameter javaParam = javaParams[i];
            params.add(convertJavaParam(i, javaParam, toolPos, typeParamMap, ctxt));
        }

        // Create ToolDef tool.
        String pluginName = imp.getPluginName() == null ? null : imp.getPluginName().text;
        return newJavaTool(method, imp.getMethodName().text, toolName, params, pluginName, deepclone(toolPos),
                (retType == null) ? null : list(retType), typeParams);
    }

    /**
     * Converts a Java type to a ToolDef type.
     *
     * @param type The java type to convert.
     * @param useText A text describing the use of the type. Must be either {@code "parameter"} or {@code "return"}.
     * @param pos The position on which to report problems.
     * @param typeParamMap Mapping from Java type parameters to ToolDef type parameters.
     * @param ctxt The type checker context.
     * @param isVariadic Whether this type is the top level type of a variadic parameter, for which we need to convert
     *     'x[]' to 'list x'.
     * @param isReturn Whether this type is the top level type of a return value, and may thus be 'void'.
     * @return The ToolDef type, or {@code null} for 'void'.
     */
    private static ToolDefType convertJavaType(Type type, String useText, Position pos,
            Map<TypeVariable<Method>, TypeParam> typeParamMap, CheckerContext ctxt, boolean isVariadic,
            boolean isReturn)
    {
        if (type instanceof WildcardType) {
            // Unsupported.
            ctxt.addProblem(Message.IMPORT_JAVA_TYPE_WILDCARD, pos, useText);
            throw new SemanticException();
        } else if (type instanceof TypeVariable) {
            // For static methods, the only type parameters that can be used
            // are those of the method. Type parameters of the class cannot be
            // used. Therefore, the type parameter must be present in the type
            // parameter mapping.
            TypeVariable<?> typeVar = (TypeVariable<?>)type;
            TypeParam typeParam = typeParamMap.get(typeVar);
            Assert.notNull(typeParam);
            return newTypeParamRef(false, null, typeParam);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType)type;

            // Convert arguments.
            Type[] javaArgTypes = pType.getActualTypeArguments();
            List<ToolDefType> argTypes = listc(javaArgTypes.length);
            for (Type javaArgType: javaArgTypes) {
                argTypes.add(convertJavaType(javaArgType, useText, pos, typeParamMap, ctxt, false, false));
            }

            // Convert raw type.
            Type rawType = pType.getRawType();
            if (rawType == List.class) {
                Assert.check(argTypes.size() == 1);
                return newListType(argTypes.get(0), true, null);
            } else if (rawType == Set.class) {
                Assert.check(argTypes.size() == 1);
                return newSetType(argTypes.get(0), true, null);
            } else if (rawType == Map.class) {
                Assert.check(argTypes.size() == 2);
                return newMapType(argTypes.get(0), true, null, argTypes.get(1));
            } else if (rawType == ToolDefTuplePair.class) {
                Assert.check(argTypes.size() == 2);
                return newTupleType(argTypes, true, null);
            } else if (rawType == ToolDefTupleNary.class) {
                Assert.check(argTypes.size() == 2);
                ToolDefType prefixType = argTypes.get(0);
                ToolDefType remainderType = argTypes.get(1);

                List<ToolDefType> fieldTypes = list();
                fieldTypes.add(prefixType);

                Assert.check(remainderType instanceof TupleType);
                fieldTypes.addAll(((TupleType)remainderType).getFields());

                return newTupleType(fieldTypes, true, null);
            } else {
                ctxt.addProblem(Message.IMPORT_JAVA_TYPE_UNSUPPORTED, pos, rawType.toString(), useText);
                throw new SemanticException();
            }
        } else if (type instanceof GenericArrayType) {
            // Special case for variadic parameters.
            if (isVariadic) {
                GenericArrayType gaType = (GenericArrayType)type;
                Type componentType = gaType.getGenericComponentType();
                ToolDefType elemType = convertJavaType(componentType, useText, pos, typeParamMap, ctxt, false, false);
                return newListType(elemType, false, null);
            }

            // Unsupported generic array type.
            ctxt.addProblem(Message.IMPORT_JAVA_TYPE_ARRAY, pos, useText);
            throw new SemanticException();
        } else if (type instanceof Class) {
            // Supported non-generic types.
            if (type == boolean.class) {
                return newBoolType(false, null);
            } else if (type == Boolean.class) {
                return newBoolType(true, null);
            } else if (type == int.class) {
                return newIntType(false, null);
            } else if (type == Integer.class) {
                return newIntType(true, null);
            } else if (type == long.class) {
                return newLongType(false, null);
            } else if (type == Long.class) {
                return newLongType(true, null);
            } else if (type == double.class) {
                return newDoubleType(false, null);
            } else if (type == Double.class) {
                return newDoubleType(true, null);
            } else if (type == String.class) {
                return newStringType(true, null);
            } else if (type == Object.class) {
                return newObjectType(true, null);
            }

            // Special case for 'void' as return type.
            if (type == void.class && isReturn) {
                return null;
            }

            // Supported generic types, used as non-generic types.
            if (type == List.class || type == Set.class || type == Map.class || type == ToolDefTuplePair.class
                    || type == ToolDefTupleNary.class)
            {
                ctxt.addProblem(Message.IMPORT_JAVA_TYPE_NON_GENERIC, pos, type.toString(), useText);
                throw new SemanticException();
            }

            // Non-generic array type.
            if (((Class<?>)type).isArray()) {
                // Special case for variadic parameters.
                if (isVariadic) {
                    Class<?> cls = (Class<?>)type;
                    Class<?> componentCls = cls.getComponentType();
                    ToolDefType elemType = convertJavaType(componentCls, useText, pos, typeParamMap, ctxt, false,
                            false);
                    return newListType(elemType, false, null);
                }

                // Unsupported non-generic array type.
                ctxt.addProblem(Message.IMPORT_JAVA_TYPE_ARRAY, pos, useText);
                throw new SemanticException();
            }

            // Unsupported types.
            ctxt.addProblem(Message.IMPORT_JAVA_TYPE_UNSUPPORTED, pos, type.toString(), useText);
            throw new SemanticException();
        } else {
            throw new RuntimeException("Unknown Java type: " + type);
        }
    }

    /**
     * Converts a Java method parameter to a ToolDef tool parameter.
     *
     * @param idx The 0-based index of the parameter into the parameters of the method.
     * @param param The type parameter information.
     * @param pos The position on which to report problems.
     * @param typeParamMap Mapping from Java type parameters to ToolDef type parameters.
     * @param ctxt The type checker context.
     * @return The ToolDef tool parameter.
     */
    private static ToolParameter convertJavaParam(int idx, Parameter param, Position pos,
            Map<TypeVariable<Method>, TypeParam> typeParamMap, CheckerContext ctxt)
    {
        // Get the name of the method parameter. We ask the name via
        // reflection. We get the actual name if the method was compiled using
        // Java 8 or later, and the compiler option to store parameter names
        // was enabled. If it was compiled using Java 7 or lower, or if the
        // compiler option was disabled, a default name is used (arg0, arg1,
        // arg2, etc).
        String name = param.getName();

        // Convert parameter type.
        Type javaType = param.getParameterizedType();
        boolean variadic = param.isVarArgs();
        ToolDefType type = convertJavaType(javaType, "parameter", pos, typeParamMap, ctxt, variadic, false);
        Assert.notNull(type);

        // Create tool parameter.
        return newToolParameter(name, deepclone(pos), type, null, variadic);
    }
}
