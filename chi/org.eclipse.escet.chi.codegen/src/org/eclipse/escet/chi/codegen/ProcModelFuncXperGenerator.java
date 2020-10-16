//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.chi.codegen.Constants.INDENT_SIZE;
import static org.eclipse.escet.chi.codegen.OutputPosition.genCurrentPositionStatement;
import static org.eclipse.escet.chi.codegen.java.JavaMethod.makeParameters;
import static org.eclipse.escet.chi.codegen.java.JavaParameter.convertParamsToString;
import static org.eclipse.escet.chi.codegen.statements.seq.ForLoopConversion.getElementType;
import static org.eclipse.escet.chi.codegen.statements.seq.Seq.convertStatements;
import static org.eclipse.escet.chi.codegen.statements.seq.Seq.getIteratorVariables;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createFunctionTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.dropTypeReferences;
import static org.eclipse.escet.common.java.Assert.check;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.position.common.PositionUtils.pos2str;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext.ActiveScope;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.codegen.statements.seq.Seq;
import org.eclipse.escet.chi.codegen.statements.seq.SeqCode;
import org.eclipse.escet.chi.codegen.statements.switchcases.SwitchCases;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.ForStatement;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.Unwind;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.XperDeclaration;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Code generator for processes, models, functions, and experiments. */
public class ProcModelFuncXperGenerator {
    /** Constructor for the {@link ProcModelFuncXperGenerator} class. */
    private ProcModelFuncXperGenerator() {
        // Static class.
    }

    /**
     * Generate an implementation of a process, model, or xper declaration.
     *
     * @param bd Declaration to convert to Java code.
     * @param ctxt Code generator context.
     */
    public static void transProcModelXperDeclaration(BehaviourDeclaration bd, CodeGeneratorContext ctxt) {
        String name = ctxt.getDefinition(bd);
        String baseClass = Constants.BASEPROCESS_FQC;
        int idx = baseClass.lastIndexOf('.');
        JavaClass jClass = ctxt.addJavaClass(name, false, baseClass.substring(idx + 1), null);
        jClass.addImport(baseClass, false);
        jClass.addVariable("private final " + ctxt.specName + " spec;");
        ctxt.openScope(ActiveScope.DEFINITION);

        // Add the variables in the class, and add them to the scope.
        addVariables(true, bd, null, ctxt, jClass);

        // Make the Constructor.
        transBehaviourConstructor(bd, ctxt, jClass);

        // Make runProcess function
        JavaMethod runMethod = transBehaviourStatements(bd, ctxt, jClass);
        jClass.addMethod(runMethod);
        ctxt.closeScope();
    }

    /**
     * Construct instances of each function in the specification, as part of instantiating the model.
     *
     * @param javaSpec Java class of the specification.
     * @param decls Global declarations.
     * @param ctxt Code generator context (with the java class name of the functions).
     */
    public static void addSpecFunctionInstances(JavaClass javaSpec, List<Declaration> decls,
            CodeGeneratorContext ctxt)
    {
        JavaMethod jm = new JavaMethod("public " + javaSpec.getClassName() + "(ChiCoordinator chiCoordinator)");
        for (Declaration decl: decls) {
            if (!(decl instanceof FunctionDeclaration)) {
                continue;
            }

            String fnName = ctxt.getDefinition(decl);
            String instName = "instance" + fnName;
            String line = "public final " + fnName + " " + instName + ";";
            javaSpec.addVariable(line);

            line = instName + " = new " + fnName + "(this, chiCoordinator);";
            jm.lines.add(line);
        }
        javaSpec.addMethod(jm);
        javaSpec.addImport(Constants.COORDINATOR_FQC, false);
    }

    /**
     * Convert a function declaration to a Java class.
     *
     * @param fn Function declaration to convert.
     * @param ctxt Code generator context.
     */
    public static void transFunctionDeclaration(FunctionDeclaration fn, CodeGeneratorContext ctxt) {
        String fnName = ctxt.getDefinition(fn);

        String callText = "";
        for (VariableDeclaration arg: fn.getVariables()) {
            if (!arg.isParameter()) {
                continue;
            }
            if (!callText.isEmpty()) {
                callText += ", ";
            }
            callText += "p_" + arg.getName();
        }
        TypeID fnTid = createFunctionTypeID(fn, ctxt);
        String baseClass = fnTid.getJavaType();
        JavaClass funcClass = ctxt.addJavaClass(fnName, false, baseClass, null);
        JavaMethod jm;

        // Constructor.
        String args = ctxt.specName + " spec, ChiCoordinator chiCoordinator";
        jm = new JavaMethod("public " + fnName + "(" + args + ")");
        jm.lines.add("super(spec, chiCoordinator);");
        funcClass.addMethod(jm);
        funcClass.addImport(Constants.COORDINATOR_FQC, false);

        // public RET compute(PARAMS)
        TypeID retTid = createTypeID(fn.getReturnType(), ctxt);
        String pTxt = convertParamsToString(makeParameters(fn.getVariables(), ctxt));
        // Prepend the position stack argument to the function application.
        String posdata = Constants.POSITION_DATA_FQC;
        posdata = posdata.substring(posdata.lastIndexOf('.') + 1);

        if (pTxt.isEmpty()) {
            pTxt = "List<" + posdata + "> positionStack";
        } else {
            pTxt = "List<" + posdata + "> positionStack, " + pTxt;
        }
        jm = new JavaMethod("public " + retTid.getJavaType() + " compute(" + pTxt + ")");

        funcClass.addImport("java.util.List", false);
        funcClass.addImport(Constants.POSITION_DATA_FQC, false);

        ctxt.startNewDeclaration();
        ctxt.openScope(ActiveScope.DEFINITION);

        addVariables(false, fn, jm, ctxt, funcClass);

        // Construct a local position data object.
        String posDefKind = Constants.DEFINITION_KIND_FQC;
        funcClass.addImport(posDefKind, false);
        int idx = posDefKind.lastIndexOf('.');
        posDefKind = posDefKind.substring(idx + 1) + ".FUNCTION";
        jm.lines.add(fmt("%s position = new %s(%s, \"%s\");", posdata, posdata, posDefKind, fn.getName()));
        jm.lines.add("if (positionStack != null) positionStack.add(position);");
        jm.lines.add();

        List<Seq> seqs = Seq.convertVarInitialization(fn, ctxt, funcClass);
        seqs.addAll(convertStatements(fn.getStatements(), ctxt, funcClass));

        // Add an exception for falling through the end of a function.
        List<String> lines = list();
        lines.add(genCurrentPositionStatement(fn));
        lines.add(fmt("throw new ChiSimulatorException"
                + "(\"Reached the end of function \\\"%s\\\" without returning a value.\");", fn.getName()));
        funcClass.addImport(Constants.CHI_SIMULATOR_EXCEPTION_FQC, false);
        Seq sc = new SeqCode(lines, fn);
        seqs.add(sc);

        ctxt.closeScope();
        ctxt.stopNewDeclaration();

        // Convert to box format.
        for (Seq seq: seqs) {
            jm.lines.add(seq.boxify());
        }
        funcClass.addMethod(jm);
    }

    /**
     * Construct local variables of the class.
     *
     * <p>
     * Also adds the types of the variables to the context.
     * </p>
     *
     * @param declParms Iff set, create variables for the parameters too.
     * @param bd Chi declaration getting translated.
     * @param jm Method to add the variables to. If {@code null} use 'ctxt'.
     * @param ctxt Code generator context.
     * @param currentClass Target class for the code generation.
     */
    private static void addVariables(boolean declParms, BehaviourDeclaration bd, JavaMethod jm,
            CodeGeneratorContext ctxt, JavaClass currentClass)
    {
        List<PositionObject> localVars = list();
        // Type casting VarDeclaration -> PositionObject, one object at a time
        for (VariableDeclaration vd: bd.getVariables()) {
            localVars.add(vd);
        }
        List<PositionObject> iterVars;
        iterVars = getIteratorVariables(bd.getStatements());

        VBox b = (jm == null) ? null : new VBox();
        Set<String> names = set();

        boolean added = false;
        added |= transBehaviourVariables(declParms, names, localVars, b, ctxt, currentClass);
        added |= transBehaviourVariables(declParms, names, iterVars, b, ctxt, currentClass);
        if (jm != null && added) {
            jm.lines.add(b);
        }
    }

    /**
     * Construct local variables of the class (for parameters iff 'declParms', local variables, Chi iterator variables,
     * and for-statements (iterator variables of the container). Also adds the types of the variables to the context.
     *
     * @param declParms Also create parameter declarations.
     * @param names Names already created in the scope of the class.
     * @param objects Objects of the class that need a variable.
     * @param b Box to add the generated code to. If {@code null}, use 'ctxt' instead.
     * @param ctxt Code generator context.
     * @param currentClass Target class for the code generation.
     * @return The 'b' box got extra contents during execution of the method.
     */
    private static boolean transBehaviourVariables(boolean declParms, Set<String> names, List<PositionObject> objects,
            VBox b, CodeGeneratorContext ctxt, JavaClass currentClass)
    {
        boolean added = false; // Something got added to the 'b' box.
        for (PositionObject obj: objects) {
            String name, typeText;

            // Decide type of object.
            VariableDeclaration vd = null;
            if (obj instanceof VariableDeclaration) {
                // It's a variable.
                vd = (VariableDeclaration)obj;

                // Construct name.
                name = vd.isParameter() ? "p_" : "v_";
                name += vd.getName();
                if (names.contains(name)) {
                    name = ctxt.makeUniqueName(vd.isParameter() ? "p" : "v");
                    Assert.check(!names.contains(name));
                }
                TypeID tid = createTypeID(vd.getType(), ctxt);
                typeText = tid.getJavaType();
                int idx = typeText.lastIndexOf('.');
                if (idx != -1 && !typeText.startsWith(ctxt.specName)) {
                    currentClass.addImport(typeText, false);
                    typeText = typeText.substring(idx + 1);
                }
            } else {
                // It's a for/unwind loop.
                Type sourceType;
                if (obj instanceof ForStatement) {
                    ForStatement fs = (ForStatement)obj;
                    check(fs.getUnwinds().size() == 1);
                    Unwind unw = first(fs.getUnwinds());
                    sourceType = dropTypeReferences(unw.getSource().getType());
                } else {
                    check(obj instanceof Unwind);
                    Unwind unw = (Unwind)obj;
                    sourceType = dropTypeReferences(unw.getSource().getType());
                }

                // Construct name.
                name = ctxt.makeUniqueName("for");
                currentClass.addImport("java.util.Iterator", false);

                // Construct type.
                TypeID tid = getElementType(sourceType, ctxt);
                typeText = tid.getJavaClassType();
                int idx = typeText.lastIndexOf('.');
                if (idx != -1 && !typeText.startsWith(ctxt.specName)) {
                    currentClass.addImport(typeText, false);
                    typeText = typeText.substring(idx + 1);
                }
                typeText = "Iterator<" + typeText + ">";
            }

            // Add it to the scope.
            names.add(name);
            ctxt.addDefinition(obj, name);

            // Skip declaration of variables for the parameters if requested.
            if (vd != null && vd.isParameter() && !declParms) {
                continue;
            }

            // Add variable to the class or the method.
            String varText = typeText + " " + name + "; // " + pos2str(obj);
            if (b != null) {
                b.add(varText);
                added = true;
            } else {
                currentClass.addVariable("public " + varText);
            }
        }
        return added;
    }

    /**
     * Construct the constructor method of the class.
     *
     * @param bd Process or model being translated.
     * @param ctxt Code generator context containing the environment and the generated code.
     * @param currentClass Target class for the code generator.
     */
    private static void transBehaviourConstructor(BehaviourDeclaration bd, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        String argText = ctxt.specName + " spec, ChiCoordinator chiCoordinator";
        currentClass.addImport(Constants.COORDINATOR_FQC, false);
        for (VariableDeclaration vd: bd.getVariables()) {
            if (!vd.isParameter()) {
                continue;
            }
            String name = ctxt.getDefinition(vd);
            TypeID tid = createTypeID(vd.getType(), ctxt);
            argText += ", " + tid.getJavaType() + " " + name;
        }
        JavaMethod method = new JavaMethod("public", null, currentClass.getClassName(), argText, null);

        // Initialize super class.
        String posDefKind = Constants.DEFINITION_KIND_FQC;
        currentClass.addImport(posDefKind, false);
        int idx = posDefKind.lastIndexOf('.');
        String line;
        if (bd instanceof XperDeclaration) {
            line = posDefKind.substring(idx + 1) + ".XPER";
        } else if (bd instanceof ModelDeclaration) {
            line = posDefKind.substring(idx + 1) + ".MODEL";
        } else if (bd instanceof ProcessDeclaration) {
            line = posDefKind.substring(idx + 1) + ".PROCESS";
        } else {
            Assert.fail("Encountered unexpected kind of behaviour kind");
            line = "FAIL";
        }
        method.lines.add("super(chiCoordinator, %s, \"%s\");", line, bd.getName());
        method.lines.add("this.spec = spec;");

        // Copy parameters into the process.
        for (VariableDeclaration vd: bd.getVariables()) {
            if (vd.isParameter()) {
                String name = ctxt.getDefinition(vd);
                method.lines.add(fmt("this.%s = %s;", name, name));
            }
        }
        currentClass.addMethod(method);
    }

    /**
     * Add a code fragment to the specification for starting a model or experiment.
     *
     * @param bd Model or xper entry to add.
     * @param javaSpec Specification java class.
     * @param ctxt Code generator context.
     * @return Test to startup the model or experiment.
     */
    public static Box addStartup(BehaviourDeclaration bd, JavaFile javaSpec, CodeGeneratorContext ctxt) {
        String name = bd.getName();
        final String handle = "handle";

        // Outline of generated code:
        //
        // if (readStartupPrefix(handle, "<modelname>") {
        // // Matched the model, read parameters.
        // T1 x1 = <readfunc1>(handle);
        // readValueSeparator(handle);
        // T2 x2 = <readfunc2>(handle);
        // ...
        // Tn xn = <readfuncn>(handle);
        // readModelSuffix(handle);
        // return new <modelname>(x1, x2, ..., xn);
        // }
        //
        VBox vb = new VBox();
        vb.add("if (readStartupPrefix(" + handle + ", \"" + name + "\")) {");

        VBox vc = new VBox(INDENT_SIZE);
        int n = 1;
        String actParms = "";
        for (VariableDeclaration vd: bd.getVariables()) {
            if (!vd.isParameter()) {
                continue;
            }
            if (n != 1) {
                vc.add("readValueSeparator(" + handle + ");");
            }

            TypeID tid = createTypeID(vd.getType(), ctxt);
            String line = fmt("%s x%d = %s;", tid.getJavaType(), n, tid.getReadName(handle, javaSpec));
            vc.add(line);
            actParms += fmt(", x%d", n);
            n++;
        }
        vc.add("readStartupSuffix(" + handle + ");");

        String line = fmt("return new %s(this, chiCoordinator%s);", ctxt.getDefinition(bd), actParms);
        vc.add(line);

        vb.add(vc);
        vb.add("}");
        return vb;
    }

    /**
     * Add a code fragment to describe the model or experiment to the user.
     *
     * @param bd Model or xper entry to add.
     * @param javaSpec Specification java class.
     * @param ctxt Code generator context.
     * @return Code to generate a model or experiment description.
     */
    public static Box addStartupDescription(BehaviourDeclaration bd, JavaFile javaSpec, CodeGeneratorContext ctxt) {
        List<String> parms = list();
        for (VariableDeclaration vd: bd.getVariables()) {
            if (!vd.isParameter()) {
                continue;
            }
            TypeID tid = createTypeID(vd.getType(), ctxt);
            String fpText = fmt("new ParameterDescription(\"%s\", \"%s\")", tid.getTypeText(), vd.getName());
            parms.add(fpText);
        }

        String pd = "new ParameterDescription[] {";
        boolean first = true;
        for (String fp: parms) {
            if (!first) {
                pd += ", ";
            }
            first = false;
            pd += fp;
        }
        pd += "}";

        String line = "new StartupDescription(\"" + bd.getName() + "\", ";
        line += (bd instanceof ModelDeclaration) ? "true, " : "false, ";
        line += pd + "), ";
        Box b = new TextBox(line);
        return b;
    }

    /**
     * Construct the main 'run' method of the process/model class.
     *
     * @param bd Process or model being translated.
     * @param ctxt Code generator context.
     * @param currentClass Target class for the code generator.
     * @return Java implementation of the behavior.
     */
    private static JavaMethod transBehaviourStatements(BehaviourDeclaration bd, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        JavaMethod jrm = new JavaMethod("public", "RunResult", "run", "SelectChoice chiChoice",
                "ChiSimulatorException");

        currentClass.addImport(Constants.SELECT_CHOICE_FQC, false);
        currentClass.addImport(Constants.CHI_SIMULATOR_EXCEPTION_FQC, false);
        ctxt.startNewDeclaration();
        List<Seq> seqs = Seq.convertVarInitialization(bd, ctxt, currentClass);
        seqs.addAll(convertStatements(bd.getStatements(), ctxt, currentClass));
        ctxt.stopNewDeclaration();
        // Flatten the statements to a switch for cooperative multi-tasking.
        SwitchCases sc = new SwitchCases();
        sc.convertBody(seqs, bd);

        // Copy needed imports from the switch cases.
        currentClass.addImports(sc.getNeededImport());
        // Convert to box format.
        jrm.lines.add(sc.boxify());
        return jrm;
    }
}
