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

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.CodeGen;
import org.eclipse.escet.cif.codegen.CurlyBraceIfElseGenerator;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprCodeGen;
import org.eclipse.escet.cif.codegen.IfElseGenerator;
import org.eclipse.escet.cif.codegen.TypeCodeGen;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.options.JavaPackageOption;
import org.eclipse.escet.cif.codegen.options.TargetLanguage;
import org.eclipse.escet.cif.codegen.typeinfos.ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.codegen.typeinfos.TupleTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.codegen.updates.tree.LhsListProjection;
import org.eclipse.escet.cif.codegen.updates.tree.LhsProjection;
import org.eclipse.escet.cif.codegen.updates.tree.LhsTupleProjection;
import org.eclipse.escet.cif.codegen.updates.tree.SingleVariableAssignment;
import org.eclipse.escet.cif.common.CifDocAnnotationUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.JavaCodeUtils;
import org.eclipse.escet.common.java.Strings;

/** Java code generator. */
public class JavaCodeGen extends CodeGen {
    /** Java code indent amount, as number of spaces. */
    private static final int INDENT = 4;

    /** Constructor for the {@link JavaCodeGen} class. */
    public JavaCodeGen() {
        super(TargetLanguage.JAVA, INDENT);
    }

    @Override
    protected ExprCodeGen getExpressionCodeGenerator() {
        return new JavaExprCodeGen();
    }

    @Override
    protected TypeCodeGen getTypeCodeGenerator() {
        return new JavaTypeCodeGen();
    }

    @Override
    protected void init() {
        super.init();
        replacements.put("java-package", JavaPackageOption.getPackage());
        replacements.put("java-tuples-code", "");
    }

    @Override
    protected Set<String> getReservedTargetNames() {
        return JavaCodeUtils.JAVA_IDS;
    }

    @Override
    protected Map<String, String> getTemplates() {
        Map<String, String> templates = map();
        templates.put("main.txt", ".java");
        templates.put("test.txt", "Test.java");
        return templates;
    }

    @Override
    protected void addConstants(CodeContext ctxt) {
        CodeBox code = makeCodeBox(1);

        for (int i = 0; i < constants.size(); i++) {
            Constant constant = constants.get(i);
            String origName = origDeclNames.get(constant);
            Assert.notNull(origName);

            List<String> docs = CifDocAnnotationUtils.getDocs(constant);

            ExprCode constantCode = ctxt.exprToTarget(constant.getValue(), null);
            Assert.check(!constantCode.hasCode()); // Java code generator never generates pre-execute code.

            code.add();
            if (docs.isEmpty()) {
                code.add("/** Constant \"%s\". */", origName);
            } else {
                code.add("/**");
                code.add(" * Constant \"%s\".", origName);
                for (String doc: docs) {
                    code.add(" *");
                    code.add(" * <p>");
                    for (String line: doc.split("\\r?\\n")) {
                        code.add(" * %s", line);
                    }
                    code.add(" * </p>");
                }
                code.add(" */");
            }
            code.add("public static final %s %s = %s;", typeToJava(constant.getType(), ctxt), getTargetRef(constant),
                    constantCode.getData());
        }

        replacements.put("java-const-decls", code.toString());
    }

    @Override
    protected void addEvents(CodeContext ctxt) {
        CodeBox code = makeCodeBox(2);

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            String name = origDeclNames.get(event);
            Assert.notNull(name);
            code.add("%s,", Strings.stringToJava(name));
        }

        replacements.put("java-event-name-code", code.toString());
    }

    @Override
    protected void addStateVars(CodeContext ctxt) {
        // State variable declarations.
        CodeBox code = makeCodeBox(1);

        for (Declaration var: stateVars) {
            String name = getTargetRef(var);
            String typeCode;
            String kindCode;
            if (var instanceof DiscVariable) {
                typeCode = typeToJava(((DiscVariable)var).getType(), ctxt);
                kindCode = "Discrete";
            } else {
                typeCode = "double";
                kindCode = "Continuous";
            }
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);
            List<String> docs = CifDocAnnotationUtils.getDocs(var);

            code.add();
            if (docs.isEmpty()) {
                code.add("/** %s variable \"%s\". */", kindCode, origName);
            } else {
                code.add("/**");
                code.add(" * %s variable \"%s\".", kindCode, origName);
                for (String doc: docs) {
                    code.add(" *");
                    code.add(" * <p>");
                    for (String line: doc.split("\\r?\\n")) {
                        code.add(" * %s", line);
                    }
                    code.add(" * </p>");
                }
                code.add(" */");
            }
            code.add("public %s %s;", typeCode, name);
        }

        replacements.put("java-state-decls", code.toString());

        // State variable initialization.
        code = makeCodeBox(2);

        for (Declaration var: stateVars) {
            String name = getTargetRef(var);
            Expression value;
            if (var instanceof DiscVariable) {
                DiscVariable v = (DiscVariable)var;
                Assert.check(v.getValue() != null);
                Assert.check(v.getValue().getValues().size() == 1);
                value = first(v.getValue().getValues());
            } else {
                Assert.check(var instanceof ContVariable);
                ContVariable v = (ContVariable)var;
                value = v.getValue();
            }
            ExprCode valueCode = ctxt.exprToTarget(value, null);
            code.add(valueCode.getCode());
            code.add("%s = %s;", name, valueCode.getData());
        }

        if (stateVars.isEmpty()) {
            code.add("// No state variables, except variable 'time'.");
        }

        replacements.put("java-state-init", code.toString());
    }

    @Override
    protected void addContVars(CodeContext ctxt) {
        // Derivative code.
        CodeBox code = makeCodeBox(1);

        for (ContVariable var: contVars) {
            String name = getTargetRef(var);
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);
            code.add();
            code.add("/**");
            code.add(" * Evaluates derivative of continuous variable \"%s\".", origName);
            code.add(" *");
            code.add(" * @return The evaluation result.");
            code.add(" */");
            code.add("public double %sderiv() {", name);
            code.indent();
            Expression deriv = var.getDerivative();
            Assert.notNull(deriv);
            ExprCode derivCode = ctxt.exprToTarget(deriv, null);
            code.add(derivCode.getCode());
            code.add("return %s;", derivCode.getData());
            code.dedent();
            code.add("}");
        }

        replacements.put("java-deriv-code", code.toString());

        // Continuous variables update code.
        code = makeCodeBox(3);

        for (int i = 0; i < contVars.size(); i++) {
            ContVariable var = contVars.get(i);
            code.add("double deriv%d = %sderiv();", i, getTargetRef(var));
        }
        if (!contVars.isEmpty()) {
            code.add();
        }
        for (int i = 0; i < contVars.size(); i++) {
            ContVariable var = contVars.get(i);
            String name = getTargetRef(var);
            code.add("%s = %s + delta * deriv%d;", name, name, i);
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);
            code.add("checkDouble(%s, %s);", name, Strings.stringToJava(origName));
            code.add("if (%s == -0.0) %s = 0.0;", name, name);
        }

        if (contVars.isEmpty()) {
            code.add("// No continuous variables, except variable 'time'.");
        }

        replacements.put("java-cont-upd-code", code.toString());
    }

    @Override
    protected void addAlgVars(CodeContext ctxt) {
        CodeBox code = makeCodeBox(1);

        for (AlgVariable var: algVars) {
            String typeCode = typeToJava(var.getType(), ctxt);
            String name = getTargetRef(var);
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);
            List<String> docs = CifDocAnnotationUtils.getDocs(var);
            code.add();
            code.add("/**");
            code.add(" * Evaluates algebraic variable \"%s\".", origName);
            for (String doc: docs) {
                code.add(" *");
                code.add(" * <p>");
                for (String line: doc.split("\\r?\\n")) {
                    code.add(" * %s", line);
                }
                code.add(" * </p>");
            }
            code.add(" *");
            code.add(" * @return The evaluation result.");
            code.add(" */");
            code.add("public %s %s() {", typeCode, name);
            code.indent();
            Expression value = var.getValue();
            Assert.notNull(value);
            ExprCode valueCode = ctxt.exprToTarget(value, null);
            code.add(valueCode.getCode());
            code.add("return %s;", valueCode.getData());
            code.dedent();
            code.add("}");
        }

        replacements.put("java-alg-var-code", code.toString());
    }

    @Override
    protected void addInputVars(CodeContext ctxt) {
        // Input variable declarations.
        CodeBox code = makeCodeBox(1);

        for (InputVariable var: inputVars) {
            String name = getTargetRef(var);
            String typeCode = typeToJava(var.getType(), ctxt);
            List<String> docs = CifDocAnnotationUtils.getDocs(var);
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);

            code.add();
            if (docs.isEmpty()) {
                code.add("/** Input variable \"%s\". */", origName);
            } else {
                code.add("/**");
                code.add(" * Input variable \"%s\".", origName);
                for (String doc: docs) {
                    code.add(" *");
                    code.add(" * <p>");
                    for (String line: doc.split("\\r?\\n")) {
                        code.add(" * %s", line);
                    }
                    code.add(" * </p>");
                }
                code.add(" */");
            }
            code.add("public %s %s;", typeCode, name);
        }

        replacements.put("java-state-input", code.toString());

        // Input variable value update test code.
        code = makeCodeBox(2);

        if (inputVars.isEmpty()) {
            code.add("// No input variables.");
        } else {
            code.add("// Assign default values to the inputs, for testing.");
            List<InternalFunction> funcs = list();
            for (InputVariable var: inputVars) {
                String name = getTargetRef(var);
                CifType type = var.getType();
                Expression value = CifValueUtils.getDefaultValue(type, funcs);
                ExprCode valueCode = ctxt.exprToTarget(value, null);
                code.add(valueCode.getCode());
                code.add("%s = %s;", name, valueCode.getData());
            }
            Assert.check(funcs.isEmpty());
        }

        replacements.put("java-test-input-upd", code.toString());
    }

    @Override
    protected void addFunctions(CodeContext ctxt) {
        CodeBox code = makeCodeBox(1);

        for (InternalFunction func: functions) {
            JavaFunctionCodeGen funcGen = new JavaFunctionCodeGen(func);
            funcGen.generate(code, ctxt);
        }
        replacements.put("java-funcs-code", code.toString());
    }

    @Override
    protected void addEnum(EnumDecl enumDecl, CodeContext ctxt) {
        CodeBox code = makeCodeBox(2);

        List<EnumLiteral> lits = enumDecl.getLiterals();
        for (int i = 0; i < lits.size(); i++) {
            if (i > 0) {
                code.add();
            }

            EnumLiteral lit = lits.get(i);
            List<String> docs = CifDocAnnotationUtils.getDocs(lit);
            String name = lit.getName();

            if (docs.isEmpty()) {
                code.add("/** Literal \"%s\". */", name);
            } else {
                code.add("/**");
                code.add(" * Literal \"%s\".", name);
                for (String doc: docs) {
                    code.add(" *");
                    code.add(" * <p>");
                    for (String line: doc.split("\\r?\\n")) {
                        code.add(" * %s", line);
                    }
                    code.add(" * </p>");
                }
                code.add(" */");
            }

            String line = fmt("_%s", name);
            line += (i == lits.size() - 1) ? ";" : ",";
            code.add(line);
        }

        replacements.put("java-enum-lits-code", code.toString());
    }

    @Override
    protected void addPrints(CodeContext ctxt) {
        // As the code runs forever, we never have a 'final' transition.

        CodeBox code = makeCodeBox(2);

        // 0+ evt
        // -2 time
        // -3 init

        for (Print print: printDecls) {
            // Add transition filter code.
            List<PrintFor> printFors = print.getFors();
            List<String> printConds = list();
            if (printFors.isEmpty()) {
                // Default is 'initial, event, time'. This excludes only
                // 'final', which can't occur, as the code runs forever.
                printConds.add("(true)");
            } else {
                for (PrintFor printFor: printFors) {
                    switch (printFor.getKind()) {
                        case EVENT:
                            printConds.add("(idx >= 0)");
                            break;

                        case FINAL:
                            // Ignored.
                            continue;

                        case INITIAL:
                            printConds.add("(idx == -3)");
                            break;

                        case NAME:
                            Expression eventRef = printFor.getEvent();
                            Assert.check(eventRef instanceof EventExpression);
                            Event event = ((EventExpression)eventRef).getEvent();
                            int idx = events.indexOf(event);
                            Assert.check(idx >= 0);
                            printConds.add(fmt("(idx == %d)", idx));
                            break;

                        case TIME:
                            printConds.add("(idx == -2)");
                            break;
                    }
                }
            }

            String printCond;
            if (printConds.isEmpty()) {
                printCond = "(false)";
            } else if (printConds.size() == 1) {
                printCond = first(printConds);
            } else {
                printCond = "(" + String.join(" || ", printConds) + ")";
            }

            code.add("if %s {", printCond);
            code.indent();

            // State filter and text output code.
            boolean[] preAndPost = {true, false};
            for (boolean pre: preAndPost) {
                // Get pre/post state filtering condition matching the pre/post
                // state text to print. Mixing pre and post is not supported
                // (precondition).
                Expression whenPred = pre ? print.getWhenPre() : print.getWhenPost();

                Expression txtExpr = pre ? print.getTxtPre() : print.getTxtPost();

                if (txtExpr != null) {
                    code.add("if (%spre) {", (pre ? "" : "!"));
                    code.indent();

                    if (whenPred != null) {
                        ExprCode whenCode = ctxt.exprToTarget(whenPred, null);
                        code.add(whenCode.getCode());
                        code.add("if (%s) {", whenCode.getData());
                        code.indent();
                    }

                    ExprCode txtCode = ctxt.exprToTarget(txtExpr, null);
                    code.add(txtCode.getCode());
                    CifType valueType = txtExpr.getType();
                    valueType = CifTypeUtils.normalizeType(valueType);
                    if (valueType instanceof StringType) {
                        code.add("String text = %s;", txtCode.getData());
                    } else {
                        code.add("Object value = %s;", txtCode.getData());
                        code.add("String text = valueToStr(value);");
                    }

                    String target = print.getFile().getPath();
                    target = Strings.stringToJava(target);
                    code.add("infoPrintOutput(text, %s);", target);

                    if (whenPred != null) {
                        code.dedent();
                        code.add("}");
                    }

                    code.dedent();
                    code.add("}");
                }
            }

            // End transition filter code.
            code.dedent();
            code.add("}");
        }

        if (printDecls.isEmpty()) {
            code.add("// No print declarations.");
        }

        replacements.put("java-print-code", code.toString());
    }

    @Override
    protected void addSvgDecls(CodeContext ctxt, String cifSpecFileDir) {
        // All CIF/SVG declarations should have been removed from the model.
        Assert.check(svgDecls.isEmpty());
    }

    @Override
    protected void addEdges(CodeContext ctxt) {
        CodeBox codeCallsUncontrollables = makeCodeBox(3);
        CodeBox codeCallsControllables = makeCodeBox(3);
        CodeBox codeMethods = makeCodeBox(1);

        for (boolean controllable: List.of(false, true)) {
            List<Edge> edges = controllable ? controllableEdges : uncontrollableEdges;
            CodeBox codeCalls = controllable ? codeCallsControllables : codeCallsUncontrollables;
            int edgeOffset = controllable ? uncontrollableEdges.size() : 0;

        for (int i = 0; i < edges.size(); i++) {
            // Get edge.
            Edge edge = edges.get(i);
            int edgeIdx = edgeOffset + i;

            // Get event.
            Assert.check(edge.getEvents().size() == 1);
            Expression eventRef = first(edge.getEvents()).getEvent();
            Event event = ((EventExpression)eventRef).getEvent();
            int eventIdx = events.indexOf(event);
            String eventName = origDeclNames.get(event);
            Assert.notNull(eventName);

            // Add call code.
            codeCalls.add("// Event \"%s\".", eventName);
            codeCalls.add("if (execEvent%d()) continue;", edgeIdx);
            codeCalls.add();

            // Add method code.

            // Header.
            List<String> docs = CifDocAnnotationUtils.getDocs(event);
            codeMethods.add();
            codeMethods.add("/**");
            codeMethods.add(" * Execute code for event \"%s\".", eventName);
            for (String doc: docs) {
                codeMethods.add(" *");
                codeMethods.add(" * <p>");
                for (String line: doc.split("\\r?\\n")) {
                    codeMethods.add(" * %s", line);
                }
                codeMethods.add(" * </p>");
            }
            codeMethods.add(" *");
            codeMethods.add(" * @return {@code true} if the event was executed, {@code false} otherwise.");
            codeMethods.add(" */");
            codeMethods.add("private boolean execEvent%d() {", edgeIdx);
            codeMethods.indent();

            // Get guard. After linearization, there is at most one
            // (linearized) guard. There may not be a guard, due to value
            // simplification. We don't try to detect always 'true' guards,
            // as that is hard to do, in general.
            List<Expression> guards = edge.getGuards();
            Assert.check(guards.size() <= 1);
            Expression guard = guards.isEmpty() ? null : first(guards);

            // Add event code.
            if (guard != null) {
                ExprCode guardCode = ctxt.exprToTarget(guard, null);
                codeMethods.add(guardCode.getCode());
                codeMethods.add("boolean guard = %s;", guardCode.getData());
                codeMethods.add("if (!guard) return false;");
                codeMethods.add();
            }
            codeMethods.add("if (doInfoPrintOutput) printOutput(%d, true);", eventIdx);
            codeMethods.add("if (doInfoEvent) infoEvent(%d, true);", eventIdx);
            codeMethods.add();
            if (!edge.getUpdates().isEmpty()) {
                addUpdates(edge.getUpdates(), codeMethods, ctxt);
            }
            codeMethods.add();
            codeMethods.add("if (doInfoEvent) infoEvent(%d, false);", eventIdx);
            codeMethods.add("if (doInfoPrintOutput) printOutput(%d, false);", eventIdx);
            codeMethods.add("return true;");

            // Method code done.
            codeMethods.dedent();
            codeMethods.add("}");
        }
        }

        replacements.put("java-event-calls-code-uncontrollables", codeCallsUncontrollables.toString());
        replacements.put("java-event-calls-code-controllables", codeCallsControllables.toString());
        replacements.put("java-event-methods-code", codeMethods.toString());
    }

    @Override
    protected IfElseGenerator getIfElseUpdateGenerator() {
        return new CurlyBraceIfElseGenerator();
    }

    @Override
    public void performSingleAssign(CodeBox code, SingleVariableAssignment asgn, Expression value, CodeContext readCtxt,
            CodeContext writeCtxt)
    {
        Assert.check(asgn.rhsProjections == null);
        Assert.check(asgn.lhsProjections == null);
        Assert.check(!asgn.needsRangeBoundCheck());

        VariableInformation varInfo = writeCtxt.getWriteVarInfo(asgn.variable);
        ExprCode assignment = readCtxt.exprToTarget(value, writeCtxt.makeDestination(varInfo));
        code.add(assignment.getCode());
        Assert.check(!assignment.hasDataValue());
    }

    @Override
    public void performAssign(CodeBox code, SingleVariableAssignment asgn, String rhsText, CodeContext readCtxt,
            CodeContext writeCtxt)
    {
        VariableInformation fullVarInfo = writeCtxt.getWriteVarInfo(asgn.variable);

        DataValue rhsValue = new JavaDataValue(rhsText);

        // Simple case, variable is fully assigned.
        if (asgn.lhsProjections == null) {
            // No left hand side projections, the right hand side is assigned to the entire variable.

            // Perform range check on the right hand side if required.
            CifType assignedType = asgn.getAssignedType();
            TypeInfo rangeCheckInfo = readCtxt.typeToTarget(assignedType);
            rangeCheckInfo.checkRange(assignedType, asgn.valueType, rhsValue, asgn.variableType, fullVarInfo.name,
                    list(), 0, code, readCtxt);

            fullVarInfo.typeInfo.storeValue(code, rhsValue, writeCtxt.makeDestination(fullVarInfo));
            return;
        }

        // x.a.b.c = y
        //
        // int index_a = a // Skipped if a is a field name of a tuple.
        // int index_b = b
        // int index_c = c
        // T1 s = project(x, index_a)
        // T2 t = project(s, index_b)
        // t = modify(t, index_c, y)
        // s = modify(s, index_b, t)
        // x = modify(x, index_a, s)

        // Construct values for the indices. Tuple indices are simple constants,
        // list indices get temporary variables containing the computed index.
        String[] indexTexts = new String[asgn.lhsProjections.length];
        List<RangeCheckErrorLevelText> rangeErrorTexts = list();

        for (int i = 0; i < asgn.lhsProjections.length; i++) {
            LhsProjection lhsProj = asgn.lhsProjections[i];
            if (lhsProj instanceof LhsTupleProjection) {
                LhsTupleProjection tupleProj = (LhsTupleProjection)lhsProj;
                indexTexts[i] = Integer.toString(tupleProj.fieldNumber);
                rangeErrorTexts.add(new RangeCheckErrorLevelText(false, tupleProj.getSelectedFieldName()));
            } else {
                LhsListProjection listProj = (LhsListProjection)lhsProj;

                // Construct a variable for the index.
                VariableInformation indexVarInfo = writeCtxt.makeTempVariable(newIntType(), "index");
                indexTexts[i] = indexVarInfo.targetRef;
                rangeErrorTexts.add(new RangeCheckErrorLevelText(true, indexVarInfo.targetRef));

                // Compute the index value.
                ExprCode indexCode = readCtxt.exprToTarget(listProj.index, null);

                // Assign the index value to the variable.
                indexVarInfo.typeInfo.declareInit(code, indexCode.getRawDataValue(),
                        writeCtxt.makeDestination(indexVarInfo));
            }
        }

        // Perform range check on the right hand side if required.
        CifType assignedType = asgn.getAssignedType();
        TypeInfo rangeCheckInfo = readCtxt.typeToTarget(assignedType);
        rangeCheckInfo.checkRange(assignedType, asgn.valueType, rhsValue, asgn.variableType, fullVarInfo.name,
                rangeErrorTexts, 0, code, readCtxt);

        // Assign projected parts of the left hand side to temporary variables.
        // Note that the last left hand side projected part is not created,
        // since the right hand side will overwrite it completely.
        int last = asgn.lhsProjections.length - 1;
        VariableInformation[] partVariables = new VariableInformation[last];
        for (int i = 0; i < last; i++) {
            LhsProjection lhsProj = asgn.lhsProjections[i];

            // Construct type and variable name.
            CifType elementType = normalizeType(lhsProj.getPartType());
            partVariables[i] = readCtxt.makeTempVariable(elementType, "part");

            // Container to project from.
            VariableInformation containerInfo = (i == 0)
                    ? readCtxt.getReadVarInfo(new VariableWrapper(asgn.variable, false)) : partVariables[i - 1];
            ExprCode containerValue = new ExprCode();
            containerValue.setDataValue(new JavaDataValue(containerInfo.targetRef));

            // Construct projection call.
            ExprCode projectRhs;
            if (containerInfo.typeInfo instanceof TupleTypeInfo) {
                TupleTypeInfo tupleTi = (TupleTypeInfo)containerInfo.typeInfo;
                LhsTupleProjection tupleLhs = (LhsTupleProjection)lhsProj;

                projectRhs = tupleTi.getProjectedValue(containerValue, tupleLhs.fieldNumber, null, readCtxt);
            } else {
                Assert.check(containerInfo.typeInfo instanceof ArrayTypeInfo);
                ArrayTypeInfo arrayTi = (ArrayTypeInfo)containerInfo.typeInfo;

                ExprCode indexVar = new ExprCode();
                indexVar.setDataValue(new JavaDataValue(indexTexts[i]));

                projectRhs = arrayTi.getProjectedValue(containerValue, indexVar, null, readCtxt);
            }

            // Generate declaration + initialization of the part variable.
            code.add(projectRhs.getCode());
            partVariables[i].typeInfo.declareInit(code, projectRhs.getRawDataValue(),
                    readCtxt.makeDestination(partVariables[i]));
        }

        // Update the container inside out, by modifying the parts.
        for (int i = last; i >= 0; i--) {
            LhsProjection lhsProj = asgn.lhsProjections[i];

            // Get the container to modify.
            // Assigned variable uses write context!
            VariableInformation containerInfo = (i == 0)
                    ? writeCtxt.getReadVarInfo(new VariableWrapper(asgn.variable, false)) : partVariables[i - 1];

            ExprCode containerCode = new ExprCode();
            containerCode.setDataValue(new JavaDataValue(containerInfo.targetRef));

            // Get the new value.
            ExprCode partCode = new ExprCode();
            if (i == last) {
                partCode.setDataValue(rhsValue);
            } else {
                partCode.setDataValue(new JavaDataValue(partVariables[i].targetRef));
            }

            CodeBox modify;
            if (containerInfo.typeInfo instanceof TupleTypeInfo) {
                TupleTypeInfo tupleTi = (TupleTypeInfo)containerInfo.typeInfo;
                LhsTupleProjection tupleLhs = (LhsTupleProjection)lhsProj;

                modify = readCtxt.makeCodeBox();
                modify.add("%s = %s.copy();", containerInfo.targetRef, containerInfo.targetRef);
                modify.add(tupleTi.modifyContainer(containerInfo, partCode, tupleLhs.fieldNumber, readCtxt));
            } else {
                Assert.check(containerInfo.typeInfo instanceof ArrayTypeInfo);
                ArrayTypeInfo arrayTi = (ArrayTypeInfo)containerInfo.typeInfo;

                ExprCode indexCode = new ExprCode();
                indexCode.setDataValue(new JavaDataValue(indexTexts[i]));
                modify = arrayTi.modifyContainer(containerInfo, partCode, indexCode, readCtxt);
            }
            code.add(modify);
        }
    }

    @Override
    protected void addUpdatesBeginScope(CodeBox code) {
        code.add("{");
        code.indent();
    }

    @Override
    protected void addUpdatesEndScope(CodeBox code) {
        code.dedent();
        code.add("}");
    }

    @Override
    protected void addSpec(CodeContext ctxt) {
        List<String> docs = CifDocAnnotationUtils.getDocs(spec);
        CodeBox classJavaDoc = makeCodeBox(0);
        if (docs.isEmpty()) {
            classJavaDoc.add("/** ${prefix} code generated from a CIF specification. */");
        } else {
            classJavaDoc.add("/**");
            classJavaDoc.add(" * ${prefix} code generated from a CIF specification.");
            for (String doc: docs) {
                classJavaDoc.add(" *");
                classJavaDoc.add(" * <p>");
                for (String line: doc.split("\\r?\\n")) {
                    classJavaDoc.add(" * %s", line);
                }
                classJavaDoc.add(" * </p>");
            }
            classJavaDoc.add(" */");
        }
        replacements.put("java-class-javadoc", classJavaDoc.toString());
    }

    @Override
    public Destination makeDestination(VariableInformation varInfo) {
        DataValue dataValue = new JavaDataValue(varInfo.targetRef);
        return new Destination(null, varInfo.typeInfo, dataValue);
    }

    @Override
    public DataValue makeDataValue(String value) {
        return new JavaDataValue(value);
    }

    /**
     * Get the Java type for the given CIF type.
     *
     * @param type CIF type to convert.
     * @param ctxt The code generation context.
     * @return The name of the converted CIF type.
     */
    private String typeToJava(CifType type, CodeContext ctxt) {
        return ctxt.typeToTarget(type).getTargetType();
    }
}
