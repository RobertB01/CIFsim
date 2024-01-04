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

package org.eclipse.escet.chi.codegen.classes;

import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.statements.seq.AssignmentConversions.convertAssignment;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.CodeGeneratorContext.ActiveScope;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.CommunicationStatement;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.ReceiveStatement;
import org.eclipse.escet.chi.metamodel.chi.SendStatement;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.java.Assert;

/** Class describing a selection alternative class that should be generated. */
public class SelectAlternativeClass {
    /** Name of the variable in the generated class to access the process. */
    public static final String PROCESS_VAR = "process";

    /** Constructor for the {@link SelectAlternativeClass} class. */
    private SelectAlternativeClass() {
        // Static class.
    }

    /**
     * Construct a class for a select case.
     *
     * @param selChoiceNumber Selection choice number within the declaration.
     * @param vars Local unwind variables that need to be assigned a value prior to running any evaluation code (or when
     *     the case is chosen). May be {@code null}.
     * @param guardKind Kind of guard.
     * @param guard Guard expression, may be {@code null}.
     * @param channelKind Kind of communication statement.
     * @param commStat Communication statement to wait for. May be {@code null}.
     * @param ctxt Code generator context.
     * @param processClass Class representing the process containing this select alternative.
     * @return Generated class specification.x
     */
    public static String createSelectCaseClass(int selChoiceNumber, List<VariableDeclaration> vars, String guardKind,
            Expression guard, String channelKind, CommunicationStatement commStat, CodeGeneratorContext ctxt,
            JavaFile processClass)
    {
        // Names of the variables in the process (needed to restore the saved
        // variables in this class into the process).
        List<String> processVars = list();
        if (vars != null) {
            for (VariableDeclaration v: vars) {
                processVars.add(ctxt.getDefinition(v));
            }
        }

        String name = ctxt.makeUniqueName("SelectChoice" + String.valueOf(selChoiceNumber));
        JavaClass sac = ctxt.addJavaClass(name, false, "SelectChoice", null);
        sac.addImport(Constants.SELECT_CHOICE_FQC, false);

        // Add 'spec' variable to the select choice (to access functions).
        sac.addVariable("public final " + ctxt.specName + " spec;");

        // Add process instance reference (to access its variables).
        String consParams = processClass.getClassName() + " " + PROCESS_VAR;
        sac.addVariable("private " + consParams + ";");

        // Add position object of the process.
        String posdata = Constants.POSITION_DATA_FQC;
        sac.addImport(posdata, false);
        sac.addImport("java.util.List", false);
        posdata = posdata.substring(posdata.lastIndexOf('.') + 1);
        sac.addVariable("private " + posdata + " position;");
        sac.addVariable("private List<" + posdata + "> positionStack;");

        ctxt.openScope(ActiveScope.SELECT_ALT);

        // Add local variable declarations for saving the unwind loop values.
        if (vars != null) {
            for (VariableDeclaration v: vars) {
                TypeID tid = createTypeID(v.getType(), ctxt);
                String varName = ctxt.makeUniqueName(v.getName());
                String varDecl = tid.getJavaType() + " " + varName;
                consParams += ", " + varDecl;
                String varLine = fmt("private %s;", varDecl);
                sac.addVariable(varLine);
                ctxt.addDefinition(v, varName);
            }
        }

        // Add constructor.
        String superCall;
        String parms = "ChiCoordinator chiCoordinator";
        parms += ", Channel channel";
        parms += ", " + ctxt.specName + " spec";
        parms += ", " + consParams;
        JavaMethod cM = new JavaMethod("public", null, name, parms, null);
        superCall = fmt("super(chiCoordinator, %s, %s, channel, %d);", guardKind, channelKind, selChoiceNumber);
        cM.lines.add(superCall);
        cM.lines.add("this." + PROCESS_VAR + " = " + PROCESS_VAR + ";");
        cM.lines.add("this.position = " + PROCESS_VAR + ".position;");
        cM.lines.add("this.positionStack = " + PROCESS_VAR + ".positionStack;");
        cM.lines.add("this.spec = spec;");
        sac.addImport(Constants.COORDINATOR_FQC, false);
        sac.addImport(Constants.CHANNELKIND_FQC, false);
        sac.addImport(Constants.CHANNEL_FQC, false);

        if (vars != null) {
            for (VariableDeclaration v: vars) {
                String varName = ctxt.getDefinition(v);
                cM.lines.add("this.%s = %s;", varName, varName);
            }
        }
        sac.addMethod(cM);

        // Add "getProcess" method.
        cM = new JavaMethod("public", "BaseProcess", "getProcess", null, null);
        cM.lines.add("return " + PROCESS_VAR + ";");
        sac.addImport(Constants.BASEPROCESS_FQC, false);
        sac.addMethod(cM);

        // Add 'setVariables' method if needed.
        if (vars != null) {
            JavaMethod setMethod = new JavaMethod("public", "void", "setVariables", null, null);
            for (int i = 0; i < vars.size(); i++) {
                VariableDeclaration v = vars.get(i);
                String processName = processVars.get(i);
                String varName = ctxt.getDefinition(v);
                setMethod.lines.add("this." + PROCESS_VAR + "." + processName + " = " + varName + ";");
            }
            sac.addMethod(setMethod);
        }

        // Add guard method.
        if (guard != null) {
            Assert.check(guardKind.equals("GuardKind.FUNC"));
            JavaMethod guardMethod = new JavaMethod("public", "boolean", "getGuard", null, null);
            if (vars != null) {
                guardMethod.lines.add("setVariables();");
            }
            ExpressionBase ge = convertExpression(guard, ctxt, sac);
            guardMethod.lines.add(ge.getCode());
            guardMethod.lines.add("return " + ge.getValue() + ";");
            sac.addMethod(guardMethod);
        }

        // Add send method.
        JavaMethod sendDataMethod = new JavaMethod("public", "Object", "getSendData", null, null);
        if (commStat == null || !(commStat instanceof SendStatement)) {
            // Not a send.
            sendDataMethod.lines.add("return null;");
        } else {
            // A send statement.
            if (commStat.getData() == null) {
                // No data to send (a synchronization channel).
                sendDataMethod.lines.add("return null;");
            } else {
                if (vars != null) {
                    sendDataMethod.lines.add("setVariables();");
                }
                ExpressionBase value = convertExpression(commStat.getData(), ctxt, sac);
                value.setCurrentPositionStatement(sendDataMethod.lines);
                sendDataMethod.lines.add(value.getCode());
                sendDataMethod.lines.add("return " + value.getValue() + ";");
            }
        }
        sac.addMethod(sendDataMethod);

        // Add receive method.
        JavaMethod recvDataMethod = new JavaMethod("public", "void", "putReceiveData", "Object obj", null);
        if (commStat instanceof ReceiveStatement) {
            // A receive statement.
            if (commStat.getData() != null) {
                // Receive with data.
                if (vars != null) {
                    recvDataMethod.lines.add("setVariables();");
                }

                Expression lhs = commStat.getData();
                TypeID tid = createTypeID(lhs.getType(), ctxt);

                String tName = tid.getJavaType();
                String tClassName = tid.getJavaClassType();
                recvDataMethod.lines.add(tName + " data = (" + tClassName + ")obj;");
                recvDataMethod.lines.add(convertAssignment(lhs, "data", lhs.getType(), ctxt, sac));
            }
        }
        sac.addMethod(recvDataMethod);

        ctxt.closeScope();
        return sac.getClassName();
    }
}
