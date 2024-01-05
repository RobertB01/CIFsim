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

package org.eclipse.escet.cif.codegen.java;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.CurlyBraceIfElseGenerator;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.FunctionCodeGen;
import org.eclipse.escet.cif.codegen.IfElseGenerator;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.common.FuncLocalVarOrderer;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Function code generator for the Java target language. */
public class JavaFunctionCodeGen extends FunctionCodeGen {
    /**
     * Constructor of the {@link JavaFunctionCodeGen} class.
     *
     * @param function Function to translate.
     */
    public JavaFunctionCodeGen(InternalFunction function) {
        super(function);
    }

    /**
     * Generate the Java code for a function.
     *
     * @param code Destination of the generated code.
     * @param ctxt Code context of the function.
     */
    public void generate(CodeBox code, CodeContext ctxt) {
        String funcName = ctxt.getFunctionName(function);

        // Generate function parameters.
        List<FunctionParameter> params = function.getParameters();
        List<String> paramTxts = listc(params.size());
        for (FunctionParameter param: params) {
            DiscVariable var = param.getParameter();
            VariableInformation varInfo = ctxt.getReadVarInfo(new VariableWrapper(var, false));
            String name = varInfo.targetRef;
            TypeInfo typeInfo = ctxt.typeToTarget(var.getType());
            String typeTxt = typeInfo.getTargetType();
            paramTxts.add(typeTxt + " " + name);
        }

        // Add method.
        String origFuncName = ctxt.getOrigFunctionName(function);
        if (origFuncName == null) {
            // Function created by preprocessing or linearization.
            origFuncName = function.getName();
        }
        code.add();
        code.add("/**");
        code.add(" * Evaluation for function \"%s\".", origFuncName);
        code.add(" *");
        for (int i = 0; i < params.size(); i++) {
            DiscVariable param = params.get(i).getParameter();
            VariableInformation varInfo = ctxt.getReadVarInfo(new VariableWrapper(param, false));
            code.add(" * @param %s Function parameter \"%s\".", varInfo.targetRef, varInfo.name);
        }
        code.add(" * @return The return value of the function.");
        code.add(" */");

        TypeInfo retTi = ctxt.typeToTarget(getReturnType());
        code.add("public static %s %s(%s) {", retTi.getTargetType(), funcName, String.join(", ", paramTxts));
        code.indent();

        addFunctionBody(function, code, ctxt);

        code.dedent();
        code.add("}");
    }

    /**
     * Add code for the body of the given function.
     *
     * @param func The internal user-defined function.
     * @param code The code. Is extended in-place.
     * @param ctxt The code generation context.
     */
    private void addFunctionBody(InternalFunction func, CodeBox code, CodeContext ctxt) {
        // Order local variables by their initialization interdependencies.
        List<DiscVariable> localVars = func.getVariables();
        localVars = new FuncLocalVarOrderer().computeOrder(localVars);
        Assert.notNull(localVars);

        // Generate code for the local variables of the function.
        for (DiscVariable var: localVars) {
            // Check assumptions.
            Assert.check(var.getValue() != null);
            Assert.check(var.getValue().getValues().size() == 1);
            Expression value = first(var.getValue().getValues());
            ExprCode valueCode = ctxt.exprToTarget(value, null);
            code.add(valueCode.getCode());

            // Generate code for the local variable declaration.
            Destination dest = ctxt.makeDestination(var);
            TypeInfo varTi = ctxt.typeToTarget(var.getType());
            varTi.declareInit(code, valueCode.getRawDataValue(), dest);
        }
        if (!localVars.isEmpty()) {
            code.add();
        }

        // Generate statements.
        addFuncStatements(func.getStatements(), code, ctxt);

        // Generate 'throw' statement at the end of the body, to ensure we
        // don't get compilation errors, due to Java thinking that the method
        // may not return a value for all code paths.
        code.add("throw new RuntimeException(\"no return at end of func\");");
    }

    @Override
    protected IfElseGenerator getIfElseFuncGenerator() {
        return new CurlyBraceIfElseGenerator();
    }

    @Override
    protected void generateBreakFuncStatement(CodeBox code) {
        // We generate 'if (true) ' to avoid unreachable statements in the
        // Java code, leading to compilation errors.
        code.add("if (true) break;");
    }

    @Override
    protected void generateContinueFuncStatement(CodeBox code) {
        // We generate 'if (true) ' to avoid unreachable statements in the
        // Java code, leading to compilation errors.
        code.add("if (true) continue;");
    }

    @Override
    protected void generateReturnFuncStatement(Expression retValue, CodeBox code, boolean safeScope, CodeContext ctxt) {
        ExprCode retCode = ctxt.exprToTarget(retValue, null);
        code.add(retCode.getCode());

        // Actual return statement code. We generate 'if (true) ' to avoid
        // unreachable statements in the Java code, leading to compilation
        // errors.
        code.add("if (true) return %s;", retCode.getData());
    }

    @Override
    protected boolean generateWhileFuncStatement(ExprCode guardCode, CodeBox code, boolean safeScope) {
        // We generate 'if (true) ' to avoid unreachable statements in the
        // Java code, leading to compilation errors.
        if (!guardCode.hasCode()) {
            code.add("if (true) while (%s) {", guardCode.getData());
            code.indent();
            return safeScope;
        } else {
            code.add("if (true) while (true) {");
            code.indent();
            code.add(guardCode.getCode());
            code.add("if (!(%s)) break;", guardCode.getData());
            return false;
        }
    }

    @Override
    protected void generateEndWhileFuncStatement(CodeBox code) {
        code.dedent();
        code.add("}");
    }
}
