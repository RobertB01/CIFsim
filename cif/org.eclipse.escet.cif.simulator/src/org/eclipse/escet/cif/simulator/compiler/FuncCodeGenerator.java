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

package org.eclipse.escet.cif.simulator.compiler;

import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.exprsToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleType;
import static org.eclipse.escet.cif.common.CifValueUtils.makeTuple;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFuncType;
import static org.eclipse.escet.cif.simulator.compiler.AssignmentCodeGenerator.gencodeAssignment;
import static org.eclipse.escet.cif.simulator.compiler.DefaultValueCodeGenerator.getDefaultValueCode;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodePreds;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.common.CifExtFuncUtils;
import org.eclipse.escet.cif.common.FuncLocalVarOrderer;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Function code generator. */
public class FuncCodeGenerator {
    /** Constructor for the {@link FuncCodeGenerator} class. */
    private FuncCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the functions of the component (recursively).
     *
     * @param comp The component.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeFuncs(ComplexComponent comp, CifCompilerContext ctxt) {
        // Skip automata, as they can't define functions.
        if (comp instanceof Automaton) {
            return;
        }

        // Generate locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Function) {
                gencodeFunc((Function)decl, ctxt);
            }
        }

        // Generate recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                gencodeFuncs((ComplexComponent)child, ctxt);
            }
        }
    }

    /**
     * Generate Java code for the given function.
     *
     * <p>
     * Note that the function may be a function generated for the default value of a function type, and may thus not be
     * contained in the specification. In other words, it has no parent, no absolute name, etc.
     * </p>
     *
     * @param func The function.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeFunc(Function func, CifCompilerContext ctxt) {
        // Add new code file.
        String className = ctxt.getFuncClassName(func);
        JavaCodeFile file = ctxt.addCodeFile(className);

        // Get return type.
        CifType retType = makeTupleType(deepclone(func.getReturnTypes()));

        // Get function type.
        FuncType funcType = newFuncType();
        for (FunctionParameter param: func.getParameters()) {
            CifType paramType = param.getParameter().getType();
            funcType.getParamTypes().add(deepclone(paramType));
        }
        funcType.setReturnType(retType);

        // Get absolute name.
        String absName = getAbsName(func);

        // Add header.
        CodeBox h = file.header;
        h.add("/** Function \"%s\". */", absName);
        h.add("public final class %s extends %s {", className, ctxt.getFuncTypeClassName(funcType));

        // Add body.
        CodeBox c = file.body;

        // Add singleton instance field.
        c.add("public static final %s %s = new %s();", className, ctxt.getFuncFieldName(func), className);

        // Add fields for external functions.
        if (func instanceof ExternalFunction) {
            gencodeClassFields((ExternalFunction)func, c);
        }

        // Add private constructor.
        c.add();
        c.add("private %s() {", className);
        c.indent();
        c.add("// Private constructor to force use of singleton instance.");
        c.dedent();
        c.add("}");

        // Generate function parameters.
        List<String> paramTxts = listc(func.getParameters().size());
        for (FunctionParameter param: func.getParameters()) {
            DiscVariable var = param.getParameter();
            String typeTxt = gencodeType(var.getType(), ctxt);
            String name = ctxt.getFuncParamMethodParamName(var);
            paramTxts.add(typeTxt + " " + name);
        }

        // Add 'evalFunc' method.
        c.add();
        c.add("@Override");
        c.add("public %s evalFunc(%s) {", gencodeType(retType, ctxt), StringUtils.join(paramTxts, ", "));
        c.indent();

        // Start of 'try'.
        c.add("try {");
        c.indent();

        // Generate code for body.
        if (func instanceof InternalFunction) {
            gencodeBody((InternalFunction)func, c, ctxt);
        } else {
            gencodeBody((ExternalFunction)func, retType, c, ctxt);
        }

        // End of 'try'.
        c.dedent();
        c.add("} catch (StackOverflowError e) {");
        c.indent();
        c.add("throw new CifSimulatorException(\"Stack overflow during evaluation of function \\\"%s\\\".\");",
                absName);
        c.dedent();
        c.add("} catch (CifSimulatorException e) {");
        c.indent();
        c.add("throw new CifSimulatorException(\"Evaluation of function \\\"%s\\\" failed.\", e);", absName);
        c.dedent();
        c.add("}");

        // End of method.
        c.dedent();
        c.add("}");

        // Generate additional methods for external functions.
        if (func instanceof ExternalFunction) {
            gencodeAdditionalMethods((ExternalFunction)func, retType, c, ctxt);
        }

        // Add 'toString' method.
        c.add();
        c.add("@Override");
        c.add("public String toString() {");
        c.indent();
        c.add("return \"%s\";", absName);
        c.dedent();
        c.add("}");
    }

    /**
     * Generates class fields for the generated class for an external user-defined function.
     *
     * @param func The external user-defined function.
     * @param c The code box to which to add the code.
     */
    private static void gencodeClassFields(ExternalFunction func, CodeBox c) {
        String extRef = func.getFunction();
        String langName = CifExtFuncUtils.getLangName(extRef);

        if (langName.equals("java")) {
            ExtJavaFuncCodeGenerator.gencodeClassFields(func, c);
        } else {
            throw new RuntimeException("Unknown language: " + langName);
        }
    }

    /**
     * Generates additional methods for the generated class for an external user-defined function.
     *
     * @param func The external user-defined function.
     * @param retType The return type of the function.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeAdditionalMethods(ExternalFunction func, CifType retType, CodeBox c,
            CifCompilerContext ctxt)
    {
        String extRef = func.getFunction();
        String langName = CifExtFuncUtils.getLangName(extRef);

        if (langName.equals("java")) {
            ExtJavaFuncCodeGenerator.gencodeAdditionalMethods(func, retType, c, ctxt);
        } else {
            throw new RuntimeException("Unknown language: " + langName);
        }
    }

    /**
     * Generate Java code for the body of the evaluation method for the given internal function.
     *
     * <p>
     * Note that the function may be a function generated for the default value of a function type, and may thus not be
     * contained in the specification. In other words, it has no parent, no absolute name, etc.
     * </p>
     *
     * @param func The internal function.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeBody(InternalFunction func, CodeBox c, CifCompilerContext ctxt) {
        // Order local variables by their initialization interdependencies.
        List<DiscVariable> localVars = func.getVariables();
        localVars = new FuncLocalVarOrderer().computeOrder(localVars);
        Assert.notNull(localVars);

        // Generate variable 'b', for evaluation of predicates. Reused for
        // guards of if/elif/while/etc.
        c.add("boolean b; // temp var for pred eval rslts");

        // Generate code for the local variables of the function.
        for (DiscVariable var: localVars) {
            // Special case for default initial value.
            if (var.getValue() == null) {
                c.add("%s %s = %s;", gencodeType(var.getType(), ctxt), ctxt.getFuncLocalVarName(var),
                        getDefaultValueCode(var.getType(), ctxt));
                continue;
            }

            // Generic case.
            Assert.check(var.getValue().getValues().size() == 1);
            Expression value = first(var.getValue().getValues());

            // Generate code for the local variable declaration.
            c.add("%s %s;", gencodeType(var.getType(), ctxt), ctxt.getFuncLocalVarName(var));

            // Generate code for the initialization of the local variable.
            c.add("try {");
            c.indent();

            c.add("%s = %s;", ctxt.getFuncLocalVarName(var), gencodeExpr(value, ctxt, null));

            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of the initial value of variable \\\"%s\\\" failed.\", "
                    + "e);", getAbsName(var));
            c.dedent();
            c.add("}");
        }
        if (!localVars.isEmpty()) {
            c.add();
        }

        // Generate statements.
        gencodeStatements(func.getStatements(), c, ctxt);

        // Generate 'throw' statement at the end of the body, to ensure we
        // don't get compilation errors, due to Java thinking that the method
        // may not return a value for all code paths.
        c.add("throw new RuntimeException(\"no return at end of func\");");
    }

    /**
     * Generate Java code for the given statements of an internal function.
     *
     * @param statements The statements.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeStatements(List<FunctionStatement> statements, CodeBox c, CifCompilerContext ctxt) {
        for (FunctionStatement statement: statements) {
            gencodeStatement(statement, c, ctxt);
        }
    }

    /**
     * Generate Java code for the given statement of an internal function.
     *
     * @param statement The statement.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeStatement(FunctionStatement statement, CodeBox c, CifCompilerContext ctxt) {
        if (statement instanceof AssignmentFuncStatement) {
            AssignmentFuncStatement asgn = (AssignmentFuncStatement)statement;
            gencodeAssignment(asgn.getAddressable(), asgn.getValue(), null, c, ctxt, null);
        } else if (statement instanceof BreakFuncStatement) {
            // We generate 'if (true) ' to avoid unreachable statements in the
            // Java code, leading to compilation errors.
            c.add("if (true) break;");
        } else if (statement instanceof ContinueFuncStatement) {
            // We generate 'if (true) ' to avoid unreachable statements in the
            // Java code, leading to compilation errors.
            c.add("if (true) continue;");
        } else if (statement instanceof IfFuncStatement) {
            IfFuncStatement istat = (IfFuncStatement)statement;

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // If guards.
            c.add("b = %s;", gencodePreds(istat.getGuards(), ctxt, null));

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of \\\"if\\\" statement guard(s) \\\"%s\\\" failed.\", "
                    + "e);", escapeJava(exprsToStr(istat.getGuards())));
            c.dedent();
            c.add("}");

            // If statements.
            c.add("if (b) {");
            c.indent();
            gencodeStatements(istat.getThens(), c, ctxt);
            c.dedent();

            // Elifs.
            for (ElifFuncStatement elif: istat.getElifs()) {
                c.add("} else {");
                c.indent();

                // Start of 'try'.
                c.add("try {");
                c.indent();

                // Elif guards.
                c.add("b = %s;", gencodePreds(elif.getGuards(), ctxt, null));

                // End of 'try'.
                c.dedent();
                c.add("} catch (CifSimulatorException e) {");
                c.indent();
                c.add("throw new CifSimulatorException(\"Evaluation of \\\"elif\\\" statement guard(s) \\\"%s\\\" "
                        + "failed.\", e);", escapeJava(exprsToStr(elif.getGuards())));
                c.dedent();
                c.add("}");

                // Elif statements.
                c.add("if (b) {");
                c.indent();
                gencodeStatements(elif.getThens(), c, ctxt);
                c.dedent();
            }

            // Else.
            if (!istat.getElses().isEmpty()) {
                c.add("} else {");
                c.indent();
                gencodeStatements(istat.getElses(), c, ctxt);
                c.dedent();
            }

            // Close elifs.
            for (int i = 0; i < istat.getElifs().size(); i++) {
                c.add("}");
                c.dedent();
            }

            // Close if.
            c.add("}");
        } else if (statement instanceof ReturnFuncStatement) {
            ReturnFuncStatement rstat = (ReturnFuncStatement)statement;
            Expression retValue = makeTuple(deepclone(rstat.getValues()));

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Actual return statement code. We generate 'if (true) ' to avoid
            // unreachable statements in the Java code, leading to compilation
            // errors.
            c.add("if (true) return %s;", gencodeExpr(retValue, ctxt, null));

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of return value \\\"%s\\\" failed.\", e);",
                    escapeJava(exprToStr(retValue)));
            c.dedent();
            c.add("}");
        } else if (statement instanceof WhileFuncStatement) {
            WhileFuncStatement wstat = (WhileFuncStatement)statement;

            c.add("while (true) {");
            c.indent();
            c.add("SPEC.ctxt.checkTermination();");

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // While guards.
            c.add("b = %s;", gencodePreds(wstat.getGuards(), ctxt, null));

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of \\\"while\\\" statement condition(s) \\\"%s\\\" "
                    + "failed.\", e);", escapeJava(exprsToStr(wstat.getGuards())));
            c.dedent();
            c.add("}");

            // Exit while.
            c.add("if (!b) break;");

            // While statements.
            gencodeStatements(wstat.getStatements(), c, ctxt);

            // End of while.
            c.dedent();
            c.add("}");
        } else {
            throw new RuntimeException("Unknown func stat: " + statement);
        }
    }

    /**
     * Generate Java code for the body of the evaluation method for the given external function.
     *
     * @param func The external function.
     * @param retType The return type of the function.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeBody(ExternalFunction func, CifType retType, CodeBox c, CifCompilerContext ctxt) {
        String extRef = func.getFunction();
        String langName = CifExtFuncUtils.getLangName(extRef);

        if (langName.equals("java")) {
            ExtJavaFuncCodeGenerator.gencodeBody(func, retType, c, ctxt);
        } else {
            throw new RuntimeException("Unknown language: " + langName);
        }
    }

    /**
     * Generate Java code for the given function type.
     *
     * @param funcType The function type.
     * @param className The name of the class to generate for this function type.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeFuncType(FuncType funcType, String className, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile(className);

        // Add header.
        CodeBox h = file.header;
        h.add("/** Function type \"%s\". */", typeToStr(funcType));
        h.add("public abstract class %s implements RuntimeToStringable {", className);

        // Add body.
        CodeBox c = file.body;

        // Generate function parameters.
        List<String> paramTxts = listc(funcType.getParamTypes().size());
        List<CifType> paramTypes = funcType.getParamTypes();
        for (int paramIdx = 0; paramIdx < paramTypes.size(); paramIdx++) {
            CifType paramType = paramTypes.get(paramIdx);
            String typeTxt = gencodeType(paramType, ctxt);
            String name = "p_" + paramIdx;
            paramTxts.add(typeTxt + " " + name);
        }

        // Add 'evalFunc' method.
        c.add("public abstract %s evalFunc(%s);", gencodeType(funcType.getReturnType(), ctxt),
                StringUtils.join(paramTxts, ", "));
    }
}
