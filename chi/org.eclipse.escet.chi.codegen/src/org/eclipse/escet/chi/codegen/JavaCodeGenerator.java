//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen;

import static org.eclipse.escet.chi.codegen.ProcModelFuncXperGenerator.addSpecFunctionInstances;
import static org.eclipse.escet.chi.codegen.ProcModelFuncXperGenerator.addStartup;
import static org.eclipse.escet.chi.codegen.ProcModelFuncXperGenerator.addStartupDescription;
import static org.eclipse.escet.chi.codegen.ProcModelFuncXperGenerator.transFunctionDeclaration;
import static org.eclipse.escet.chi.codegen.ProcModelFuncXperGenerator.transProcModelXperDeclaration;
import static org.eclipse.escet.chi.codegen.ProcessTypeDefinition.addProcessTypes;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createEnumTypeID;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext.ActiveScope;
import org.eclipse.escet.chi.codegen.classes.SelectAlternativeClass;
import org.eclipse.escet.chi.codegen.expressions.CodeExpression;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.codegen.statements.seq.Seq;
import org.eclipse.escet.chi.codegen.statements.switchcases.SwitchCases;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.codegen.types.TypeIDCreation;
import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Specification;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.TypeDeclaration;
import org.eclipse.escet.chi.metamodel.chi.XperDeclaration;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.java.Assert;

/**
 * Main wrapper class for the Java code generator functions.
 *
 * <p>
 * {@link #transCodeGen(String, Specification)} is the entry point of the code generator. It contains the main phases in
 * the conversion to Java code. This function also generates the 'Specification.java' file.
 * </p>
 *
 * <p>
 * The {@link CodeGeneratorContext} object serves as the global reference to generated names. It also stores generated
 * data. Enumerations are processed in {@link EnumCodeGenerator} while process, model, and function definitions are
 * handled in {@link ProcModelFuncXperGenerator}. Constants and type definitions are just regular types and expression
 * values.
 * </p>
 *
 * <p>
 * Expressions are handled in {@link ExpressionBase}. In Java, an expression is a piece of code to be executed, and a
 * Java expression to be evaluated after executing the code. {@link SimpleExpression} is the case where the code-part is
 * empty, the general case is in {@link CodeExpression}. <br>
 * Most expression conversions are forwarded towards the type they 'belong' to using the abstract
 * {@link TypeID#convertExprNode}. The advantage of this approach is that the generated code of a type and the generated
 * expression code is quite close together.
 * </p>
 *
 * <p>
 * Types are represented with the {@link TypeID} class. Such a type class can generate its Java runtime representation.
 * It also serves as an abstract interface for querying Java type names, default values, initialization, etc for the
 * expression and (process/model/function)definition code. <br>
 * There is a derived class for each type, {@link TypeID} object construction is done with
 * {@link TypeIDCreation#createTypeID(Type, CodeGeneratorContext)}. The {@link TypeIDCreation} class also has a few
 * other constructors for the more complex types.
 * </p>
 *
 * <p>
 * Statements are converted in two stages. First it is translated to sequential language code, a mix-form of
 * (sequential) Java code fragments and the original program structure encoded in {@link Seq} objects. There is a
 * derived class for each kind of sequential object. The conversion itself is coded in {@link Seq#convertStatements}.
 * <br>
 * For code that does not do communication or delay (that is, no use of 'select'), the result of the first stage is
 * sufficient for execution in Java. This is all code of functions, and code that performs computations. The result is
 * quite close to what you'd write manually. In principle, this code would also be useful for a separate thread.
 * </p>
 *
 * <p>
 * To deal with the points in the code that can cause blocking, the second stage is performed. Basically it performs an
 * inversion of control flow, where the sequential code is turned upside down to allow exit and re-entry around each
 * point of (potential) blocking. The result is an infinite loop around a main 'switch' statement where each 'case' is
 * sequential code. A program-counter is kept which case is to be executed. Jumps between different cases are done by
 * changing the program- counter and 'break'-ing to the loop, back to the switch. <br>
 * The second stage is handled by the {@link SwitchCases#convertBody(List, BehaviourDeclaration)} function. The
 * {@link SwitchCases} class also contains the code to decide what part to change to the second stage. <br>
 * The 'select' statement is central in the second stage ('delay' and plain communication are also forms of select), in
 * particular the {@link SelectAlternativeClass}. It generates the alternatives to wait for, and modifies the variables
 * and program-counter of the model/process definition code when appropriate (that is, at the moment an alternative is
 * selected).
 * </p>
 *
 * <p>
 * At the Java side, a file is represented by a {@link JavaFile}, and methods in it are stored in {@link JavaMethod}
 * objects. The Java program code is just lines of text, in a tree of {@link Box} objects to simplify code formatting in
 * the output.
 * </p>
 */
public abstract class JavaCodeGenerator {
    /**
     * Transform Chi output model to a collection of Java class source files.
     *
     * @param inPath Name of the Chi model filename (used for naming the specification class).
     * @param spec Type-checked Chi specification.
     * @return Code generator context containing the Java source file texts.
     */
    public static CodeGeneratorContext transCodeGen(String inPath, Specification spec) {
        String chiName = "Specification";
        CodeGeneratorContext ctxt = new CodeGeneratorContext(chiName);
        JavaClass javaSpec = ctxt.addJavaClass(chiName, false, "ChiSpecification", null);
        javaSpec.addImport(Constants.CHI_SPEC_FQC, false);

        // Code fragments for instantiating a model.
        List<Box> startups = list();
        // Model descriptions.
        List<Box> descriptions = list();

        ctxt.openScope(ActiveScope.GLOBALS); // No root scope above the specification.
        for (Declaration decl: spec.getDeclarations()) {
            String name;
            if (decl instanceof ConstantDeclaration) {
                continue;
            } else if (decl instanceof EnumDeclaration) {
                EnumDeclaration ed = (EnumDeclaration)decl;
                // Querying the enumeration type id is sufficient for
                // constructing it.
                createEnumTypeID(ed, ctxt);
                continue;
            } else if (decl instanceof TypeDeclaration) {
                continue;
            } else if (decl instanceof XperDeclaration) {
                name = "ChiXper" + decl.getName();
            } else if (decl instanceof ModelDeclaration) {
                name = "ChiModel" + decl.getName();
            } else if (decl instanceof ProcessDeclaration) {
                name = "ChiProcess" + decl.getName();
            } else if (decl instanceof FunctionDeclaration) {
                name = "ChiFunction" + decl.getName();
            } else {
                Assert.fail("Unknown type of declaration encountered.");
                continue;
            }
            name = ctxt.makeUniqueName(name);
            ctxt.addDefinition(decl, name);
        }

        addSpecFunctionInstances(javaSpec, spec.getDeclarations(), ctxt);
        addProcessTypes(spec.getDeclarations(), javaSpec, ctxt);

        // Specification main class.
        // Generate enum declarations.
        for (Declaration decl: spec.getDeclarations()) {
            if (decl instanceof ConstantDeclaration) {
                // Pass (Not needed).
            } else if (decl instanceof EnumDeclaration) {
                // Pass (already done).
            } else if (decl instanceof TypeDeclaration) {
                // Pass (type declarations are all eliminated).
            } else if (decl instanceof ModelDeclaration) {
                ModelDeclaration md = (ModelDeclaration)decl;
                transProcModelXperDeclaration(md, ctxt);
                Box startup = addStartup(md, javaSpec, ctxt);
                startups.add(startup);
                Box desc = addStartupDescription(md, javaSpec, ctxt);
                descriptions.add(desc);
            } else if (decl instanceof XperDeclaration) {
                XperDeclaration xd = (XperDeclaration)decl;
                transProcModelXperDeclaration(xd, ctxt);
                Box startup = addStartup(xd, javaSpec, ctxt);
                startups.add(startup);
                Box desc = addStartupDescription(xd, javaSpec, ctxt);
                descriptions.add(desc);
            } else if (decl instanceof ProcessDeclaration) {
                ProcessDeclaration pd = (ProcessDeclaration)decl;
                transProcModelXperDeclaration(pd, ctxt);
            } else if (decl instanceof FunctionDeclaration) {
                FunctionDeclaration td = (FunctionDeclaration)decl;
                transFunctionDeclaration(td, ctxt);
            } else {
                Assert.fail("Unknown type of declaration encountered.");
            }
        }

        // Process type enumerations
        ctxt.generateProcessTypes();

        // 'getStartups'
        JavaMethod method = new JavaMethod("public", "StartupDescription[]", "getStartups", "", null);
        method.lines.add("return new StartupDescription[] {");
        method.lines.indent();
        for (Box desc: descriptions) {
            method.lines.add(desc);
        }
        method.lines.dedent();
        method.lines.add("};");
        javaSpec.addMethod(method);

        // 'startStartup'
        int longestName = 0; // Length of longest model/xper name.
        for (Declaration decl: spec.getDeclarations()) {
            if (decl instanceof ModelDeclaration || decl instanceof XperDeclaration) {
                int nameLength = decl.getName().length();
                if (nameLength > longestName) {
                    longestName = nameLength;
                }
            }
        }
        method = new JavaMethod("public", "BaseProcess", "startStartup", "ChiCoordinator chiCoordinator, String arg",
                null);
        javaSpec.addImport(Constants.BASEPROCESS_FQC, false);
        javaSpec.addImport(Constants.COORDINATOR_FQC, false);

        if (longestName > 0) {
            // There are models/experiments present in the specification.
            method.lines.add("ChiReadMemoryFile handle = new ChiReadMemoryFile(arg);");
            javaSpec.addImport(Constants.CHI_READ_MEMORY_FILE_FQC, false);

            method.lines.add("handle.skipWhitespace();");

            // + 2 due to optional ' ', and the '('.
            method.lines.add("handle.markStream(%d);", longestName + 2);

            for (Box start: startups) {
                method.lines.add(start);
            }
        } else {
            Assert.check(startups.isEmpty());
        }

        method.lines.add("throw new ChiSimulatorException(\"Unrecognized command line.\");");
        javaSpec.addImport(Constants.CHI_SIMULATOR_EXCEPTION_FQC, false);
        javaSpec.addMethod(method);

        // 'main' method to launch the class directly.
        method = new JavaMethod("public static", "void", "main", "String[] cmdArgs", null);
        method.lines.add(fmt("%s app = new %s(null);", Constants.SIMULATOR_APPLICATION_CLASSNAME,
                Constants.SIMULATOR_APPLICATION_CLASSNAME));
        method.lines.add("String[] args = new String[cmdArgs.length + 1];");
        method.lines.add("System.arraycopy(cmdArgs, 0, args, 1, cmdArgs.length);");
        method.lines.add("args[0] = \"--run-specification-class=" + chiName + "\";");
        method.lines.add("int ret = app.run(args, true);");
        method.lines.add("System.exit(ret);");
        javaSpec.addImport(Constants.SIMULATOR_APPLICATION_FQC, false);
        javaSpec.addMethod(method);

        ctxt.closeScope();
        return ctxt;
    }
}
