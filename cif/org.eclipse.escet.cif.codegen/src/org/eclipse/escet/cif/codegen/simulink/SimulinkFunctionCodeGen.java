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

package org.eclipse.escet.cif.codegen.simulink;

import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeReference;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.c89.typeinfos.C89TypeInfoHelper.typeUsesValues;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.CurlyBraceIfElseGenerator;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.FunctionCodeGen;
import org.eclipse.escet.cif.codegen.IfElseGenerator;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.common.FuncLocalVarOrderer;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** C89 code generator for CIF functions. */
public class SimulinkFunctionCodeGen extends FunctionCodeGen {
    /**
     * If set, generate functions only available in the current source file, else generate globally accessible
     * functions.
     */
    public final boolean genLocalFunctions;

    /**
     * Constructor for {@link SimulinkFunctionCodeGen} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param function Function to convert.
     */
    public SimulinkFunctionCodeGen(boolean genLocalFunctions, InternalFunction function) {
        super(function);
        this.genLocalFunctions = genLocalFunctions;
    }

    /**
     * Generate the Java code for a function.
     *
     * @param declCode Destination of the generated function declaration code.
     * @param defCode Destination of the generated function definition code.
     * @param ctxt Code context of the function.
     */
    public void generate(CodeBox declCode, CodeBox defCode, CodeContext ctxt) {
        int varBase = ctxt.reserveTempVariables(); // Create new scope for temp variables.

        int paramCount = function.getParameters().size();

        // Get variable information about the formal parameters, while counting
        // number of reference parameters.
        int refParamCount = 0;
        VariableInformation[] paramVars = new VariableInformation[paramCount];
        for (int i = 0; i < paramCount; i++) {
            paramVars[i] = ctxt.getWriteVarInfo(function.getParameters().get(i).getParameter());
            if (!typeUsesValues(paramVars[i].typeInfo)) {
                refParamCount++;
            }
        }

        // Order local variables by their initialization interdependencies.
        List<DiscVariable> localVars = function.getVariables();
        localVars = new FuncLocalVarOrderer().computeOrder(localVars);
        Assert.notNull(localVars);

        // Construct space for the local variable information, and local copies of the reference parameters.
        VariableInformation[] localVarInfos = new VariableInformation[refParamCount + localVars.size()];
        int localIndex = 0;

        CodeBox vardeclCode = ctxt.makeCodeBox();

        // Use the original parameter variable as local value variable, and add place holder for the parameter.
        for (int i = 0; i < paramCount; i++) {
            if (!typeUsesValues(paramVars[i].typeInfo)) {
                VariableInformation paramVar = paramVars[i];
                // Move formal parameter to the local variables.
                localVarInfos[localIndex] = paramVar;
                localIndex++;

                // Construct a new temporary variable for the moved parameter
                paramVars[i] = ctxt.makeTempVariable(paramVar);

                paramVar.isReference = false;

                // Generate copy of the data into the local variable, through the reference.
                DataValue src = makeReference(paramVars[i].targetName);
                Destination dest = new Destination(null, paramVar.typeInfo, makeValue(paramVar.targetName));
                paramVar.typeInfo.declareInit(vardeclCode, src, dest);
            }
        }

        // Retrieve local variable information
        for (DiscVariable var: localVars) {
            VariableInformation localVar = ctxt.getWriteVarInfo(var);
            localVarInfos[localIndex] = localVar;
            localIndex++;

            // Create and initialize the local variable.
            vardeclCode.add("%s %s;", localVar.typeInfo.getTargetType(), localVar.targetName);
            Destination dest = new Destination(null, localVar.typeInfo, makeValue(localVar.targetName));
            Assert.notNull(var.getValue());
            Assert.check(var.getValue().getValues().size() == 1);
            ExprCode initCode = ctxt.exprToTarget(var.getValue().getValues().get(0), dest);
            vardeclCode.add(initCode.getCode());
        }
        Assert.check(localIndex == localVarInfos.length);

        // Generate function header.
        TypeInfo returnTi = ctxt.typeToTarget(getReturnType());
        StringBuilder fnHeader = new StringBuilder();

        // Function always returns a value, since there is nothing left after the function returns.
        fnHeader.append(returnTi.getTargetType());
        fnHeader.append(" ");

        String funcName = ctxt.getFunctionName(function);
        fnHeader.append(funcName);
        fnHeader.append("(SimStruct *sim_struct");
        for (int i = 0; i < paramCount; i++) {
            fnHeader.append(", ");

            if (typeUsesValues(paramVars[i].typeInfo)) {
                fnHeader.append(paramVars[i].typeInfo.getTargetType());
                fnHeader.append(' ');
                fnHeader.append(paramVars[i].targetName);
            } else {
                fnHeader.append(paramVars[i].typeInfo.getTargetType());
                fnHeader.append("* ");
                fnHeader.append(paramVars[i].targetName);
            }
        }
        fnHeader.append(")");
        String header = fnHeader.toString();
        if (genLocalFunctions) {
            declCode.add("static %s;", header);
            defCode.add("static " + header + " {");
        } else {
            declCode.add("extern %s;", header);
            defCode.add(header + " {");
        }

        defCode.indent();
        defCode.add("struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);");
        defCode.add();
        if (localVarInfos.length > 0) {
            defCode.add(vardeclCode);
            defCode.add();
        }

        this.addFuncStatements(function.getStatements(), defCode, ctxt);

        defCode.add("assert(0); /* Falling through the end of the function. */");

        defCode.dedent();
        defCode.add("}");

        ctxt.unreserveTempVariables(varBase); // Free the temp variables scope.
    }

    @Override
    protected IfElseGenerator getIfElseFuncGenerator() {
        return new CurlyBraceIfElseGenerator();
    }

    @Override
    protected void generateBreakFuncStatement(CodeBox code) {
        code.add("break;");
    }

    @Override
    protected void generateContinueFuncStatement(CodeBox code) {
        code.add("continue;");
    }

    @Override
    protected void generateReturnFuncStatement(Expression retValue, CodeBox code, boolean safeScope, CodeContext ctxt) {
        // Always return the value itself.
        ExprCode retCode = ctxt.exprToTarget(retValue, null);

        CodeBox exprCode = retCode.getCode();
        if (exprCode.isEmpty()) {
            safeScope = true; // No temporaries created, ignore safe scope.
        }

        if (!safeScope) {
            ctxt.addUpdatesBeginScope(code);
        }
        code.add(exprCode);
        code.add("return %s;", retCode.getData());
        if (!safeScope) {
            ctxt.addUpdatesEndScope(code);
        }
    }

    @Override
    protected boolean generateWhileFuncStatement(ExprCode guardCode, CodeBox code, boolean safeScope) {
        if (guardCode.hasCode()) {
            code.add("for (;;) {");
            code.indent();
            code.add(guardCode.getCode());
            code.add("if (%s) break;", guardCode.getData());
            code.add();
            safeScope = false;
        } else {
            code.add("while (%s) {", guardCode.getData());
            code.indent();
        }
        return safeScope;
    }

    @Override
    protected void generateEndWhileFuncStatement(CodeBox code) {
        code.dedent();
        code.add("}");
    }
}
