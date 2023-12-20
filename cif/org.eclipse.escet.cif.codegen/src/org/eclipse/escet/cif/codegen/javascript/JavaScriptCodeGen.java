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

package org.eclipse.escet.cif.codegen.javascript;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.text.TextStringBuilder;
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
import org.eclipse.escet.cif.codegen.options.CodePrefixOption;
import org.eclipse.escet.cif.codegen.options.TargetLanguage;
import org.eclipse.escet.cif.codegen.options.TargetLanguageOption;
import org.eclipse.escet.cif.codegen.typeinfos.ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.codegen.typeinfos.TupleTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.codegen.updates.tree.LhsListProjection;
import org.eclipse.escet.cif.codegen.updates.tree.LhsProjection;
import org.eclipse.escet.cif.codegen.updates.tree.LhsTupleProjection;
import org.eclipse.escet.cif.codegen.updates.tree.SingleVariableAssignment;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
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
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.typechecker.annotations.builtin.DocAnnotationProvider;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** JavaScript code generator. */
public class JavaScriptCodeGen extends CodeGen {
    /** JavaScript code indent amount, as number of spaces. */
    private static final int INDENT = 4;

    /** Constructor for the {@link JavaScriptCodeGen} class. */
    public JavaScriptCodeGen() {
        super(TargetLanguageOption.getLanguage(), INDENT);

        // Unless we're generating JavaScript only, add any required UI code for the HTML page to the JavaScript export.
        // Ideally, there wouldn't be any UI code in the JavaScript class, though this is preferable to making calls
        // to assumed HTML elements and JavaScript functions outside of the class scope.
        if (language == TargetLanguage.HTML) {
            CodeBox frequencySliderCode = makeCodeBox(2);
            frequencySliderCode.add("var range = document.getElementById('run-frequency');");
            frequencySliderCode.add("range.value = %s.frequency;", CodePrefixOption.getPrefix());
            frequencySliderCode.add("document.getElementById('run-frequency-output').value = range.value;");
            replacements.put("javascript-frequency-slider-code", frequencySliderCode.toString());

            CodeBox logCallCode = makeCodeBox(2);
            logCallCode.add("log(" + CodePrefixOption.getPrefix()
                    + "Utils.fmt(\"Transition: event %s\", this.getEventName(idx)))");
            replacements.put("infoevent-log-call-code", logCallCode.toString());
        } else if (language == TargetLanguage.JAVASCRIPT) {
            replacements.put("javascript-frequency-slider-code", "");

            CodeBox logCallCode = makeCodeBox(2);
            logCallCode.add("console.log(" + CodePrefixOption.getPrefix()
                    + "Utils.fmt(\"Transition: event %s\", this.getEventName(idx)))");
            replacements.put("infoevent-log-call-code", logCallCode.toString());
        }
    }

    @Override
    protected ExprCodeGen getExpressionCodeGenerator() {
        return new JavaScriptExprCodeGen();
    }

    @Override
    protected TypeCodeGen getTypeCodeGenerator() {
        return new JavaScriptTypeCodeGen();
    }

    @Override
    protected void init() {
        super.init();
        replacements.put("javascript-tuples-code", "");
    }

    @Override
    protected Set<String> getReservedTargetNames() {
        return JavaScriptCodeUtils.JAVASCRIPT_IDS;
    }

    @Override
    protected Map<String, String> getTemplates() {
        // For the HTML export option, we generate all templates, and merge them before writing.
        // For the JavaScript export option, we only generate the JavaScript templates.
        Map<String, String> templates = map();
        templates.put("utils.txt", "_utils.js");
        templates.put("class.txt", "_class.js");
        if (language == TargetLanguage.HTML) {
            templates.put("index.txt", ".html");
        }
        return templates;
    }

    @Override
    protected void addConstants(CodeContext ctxt) {
        CodeBox code = makeCodeBox(1);

        for (int i = 0; i < constants.size(); i++) {
            Constant constant = constants.get(i);
            String origName = origDeclNames.get(constant);
            if (origName == null) {
                // Created by preprocessing or linearization.
                origName = constant.getName();
            }

            ExprCode constantCode = ctxt.exprToTarget(constant.getValue(), null);
            Assert.check(!constantCode.hasCode()); // JavaScript code generator never generates pre-execute code.

            code.add();
            code.add("/** Constant \"%s\". */", origName);
            code.add("%s = %s;", getTargetName(constant), constantCode.getData());
        }

        replacements.put("javascript-const-decls", code.toString());
    }

    @Override
    protected void addEvents(CodeContext ctxt) {
        CodeBox code = makeCodeBox(2);

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            String name = origDeclNames.get(event);
            String line = (i == events.size() - 1) ? "%s" : "%s,";
            code.add(line, Strings.stringToJava(name));
        }

        replacements.put("javascript-event-name-code", code.toString());
    }

    @Override
    protected void addStateVars(CodeContext ctxt) {
        // State variable declarations.
        CodeBox code = makeCodeBox(1);

        for (Declaration var: stateVars) {
            String name = getTargetName(var);
            String kindCode;
            if (var instanceof DiscVariable) {
                kindCode = "Discrete";
            } else {
                kindCode = "Continuous";
            }
            String origName = origDeclNames.get(var);
            if (origName == null) {
                // New variable introduced by preprocessing or linearization.
                origName = CifTextUtils.getName(var);
            }
            code.add();
            code.add("/** %s variable \"%s\". */", kindCode, origName);
            code.add("%s;", name);
        }

        replacements.put("javascript-state-decls", code.toString());

        // State variable initialization.
        code = makeCodeBox(2);

        for (Declaration var: stateVars) {
            String name = getTargetName(var);
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
            code.add("%s.%s = %s;", ctxt.getPrefix(), name, valueCode.getData());
        }

        if (stateVars.isEmpty()) {
            code.add("// No state variables, except variable 'time'.");
        }

        replacements.put("javascript-state-init", code.toString());
    }

    @Override
    protected void addContVars(CodeContext ctxt) {
        // Derivative code.
        CodeBox code = makeCodeBox(1);

        for (ContVariable var: contVars) {
            String name = getTargetName(var);
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);
            code.add();
            code.add("/**");
            code.add(" * Evaluates derivative of continuous variable \"%s\".", origName);
            code.add(" *");
            code.add(" * @return The evaluation result.");
            code.add(" */");
            code.add("%sderiv() {", name);
            code.indent();
            Expression deriv = var.getDerivative();
            Assert.notNull(deriv);
            ExprCode derivCode = ctxt.exprToTarget(deriv, null);
            code.add(derivCode.getCode());
            code.add("return %s;", derivCode.getData());
            code.dedent();
            code.add("}");
        }

        replacements.put("javascript-deriv-code", code.toString());

        // Continuous variables update code.
        code = makeCodeBox(6);

        for (int i = 0; i < contVars.size(); i++) {
            ContVariable var = contVars.get(i);
            code.add("var deriv%d = %s.%sderiv();", i, ctxt.getPrefix(), getTargetName(var));
        }
        if (!contVars.isEmpty()) {
            code.add();
        }
        for (int i = 0; i < contVars.size(); i++) {
            ContVariable var = contVars.get(i);
            String name = getTargetName(var);
            code.add("%s.%s = %s.%s + delta * deriv%d;", ctxt.getPrefix(), name, ctxt.getPrefix(), name, i);
            String origName = origDeclNames.get(var);
            code.add("%sUtils.checkReal(%s.%s, %s);", ctxt.getPrefix(), ctxt.getPrefix(), name,
                    Strings.stringToJava(origName));
            code.add("if (%s.%s == -0.0) %s.%s = 0.0;", ctxt.getPrefix(), name, ctxt.getPrefix(), name);
        }

        if (contVars.isEmpty()) {
            code.add("// No continuous variables, except variable 'time'.");
        }

        replacements.put("javascript-cont-upd-code", code.toString());
    }

    @Override
    protected void addAlgVars(CodeContext ctxt) {
        CodeBox code = makeCodeBox(1);

        for (AlgVariable var: algVars) {
            String name = getTargetName(var);
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);
            code.add();
            code.add("/**");
            code.add(" * Evaluates algebraic variable \"%s\".", origName);
            code.add(" *");
            code.add(" * @return The evaluation result.");
            code.add(" */");
            code.add("%s() {", name);
            code.indent();
            Expression value = var.getValue();
            Assert.notNull(value);
            ExprCode valueCode = ctxt.exprToTarget(value, null);
            code.add(valueCode.getCode());
            code.add("return %s;", valueCode.getData());
            code.dedent();
            code.add("}");
        }

        replacements.put("javascript-alg-var-code", code.toString());
    }

    @Override
    protected void addInputVars(CodeContext ctxt) {
        // Input variable declarations.
        CodeBox code = makeCodeBox(1);

        for (InputVariable var: inputVars) {
            String name = getTargetName(var);
            List<String> docs = DocAnnotationProvider.getDocs(var);
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
            code.add("%s;", name);
        }

        replacements.put("javascript-state-input", code.toString());
    }

    @Override
    protected void addFunctions(CodeContext ctxt) {
        CodeBox code = makeCodeBox(1);

        for (InternalFunction func: functions) {
            JavaScriptFunctionCodeGen funcGen = new JavaScriptFunctionCodeGen(func);
            funcGen.generate(code, ctxt);
        }
        replacements.put("javascript-funcs-code", code.toString());
    }

    @Override
    protected void addEnum(EnumDecl enumDecl, CodeContext ctxt) {
        CodeBox code = makeCodeBox(2);

        List<EnumLiteral> lits = enumDecl.getLiterals();
        for (int i = 0; i < lits.size(); i++) {
            String name = lits.get(i).getName();
            String line = fmt("_%s: Symbol(\"%s\")", name, name);
            line += (i == lits.size() - 1) ? "" : ",";
            code.add(line);
        }

        replacements.put("javascript-enum-lits-code", code.toString());
    }

    @Override
    protected void addPrints(CodeContext ctxt) {
        // As the code runs forever, we never have a 'final' transition.

        CodeBox code = makeCodeBox(2);

        // 0+ evt
        // -1 tau
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
                            printConds.add("(idx >= -1)");
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
                        code.add("var text = %s;", txtCode.getData());
                    } else {
                        code.add("var value = %s;", txtCode.getData());
                        code.add("var text = %sUtils.valueToStr(value);", ctxt.getPrefix());
                    }

                    String target = print.getFile().getPath();
                    target = Strings.stringToJava(target);
                    code.add("this.infoPrintOutput(text, %s);", target);

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

        replacements.put("javascript-print-code", code.toString());
    }

    @Override
    protected void addSvgDecls(CodeContext ctxt, String cifSpecFileDir) {
        // Initialize replacement texts.
        JavaScriptSvgCodeGen svgCodeGen = new JavaScriptSvgCodeGen();
        svgCodeGen.codeSvgContent = makeCodeBox(5);
        svgCodeGen.codeSvgToggles = makeCodeBox(2);
        svgCodeGen.codeInEventHandlers = makeCodeBox(1);
        svgCodeGen.codeInInteract = makeCodeBox(2);
        svgCodeGen.codeInCss = makeCodeBox(3);
        svgCodeGen.codeOutDeclarations = makeCodeBox(1);
        svgCodeGen.codeOutAssignments = makeCodeBox(2);
        svgCodeGen.codeOutApply = makeCodeBox(2);

        // Generate code for SVG visualization and interaction.
        svgCodeGen.genCodeCifSvg(ctxt, cifSpecFileDir, svgDecls, events);

        // Fill the replacement patterns with generated code, for the SVG images.
        if (language == TargetLanguage.HTML) {
            replacements.put("html-svg-content", svgCodeGen.codeSvgContent.toString());
            replacements.put("html-svg-toggles", svgCodeGen.codeSvgToggles.toString());
        }

        // Fill the replacement patterns with generated code, for SVG input mappings.
        replacements.put("javascript-svg-in-event-handlers-code", svgCodeGen.codeInEventHandlers.toString());
        replacements.put("javascript-svg-in-interact-code", svgCodeGen.codeInInteract.toString());
        if (language == TargetLanguage.HTML) {
            replacements.put("html-svg-in-css", svgCodeGen.codeInCss.toString());
        }

        // Fill the replacement patterns with generated code, for SVG output mappings.
        replacements.put("javascript-svg-out-declarations", svgCodeGen.codeOutDeclarations.toString());
        replacements.put("javascript-svg-out-assignments-code", svgCodeGen.codeOutAssignments.toString());
        replacements.put("javascript-svg-out-apply-code", svgCodeGen.codeOutApply.toString());
    }

    @Override
    protected void addEdges(CodeContext ctxt) {
        // Create codeboxes to hold generated code.
        CodeBox codeCalls = makeCodeBox(3);
        CodeBox codeMethods = makeCodeBox(1);
        CodeBox codeSvgInputAllowedVarDecls = makeCodeBox(1);
        CodeBox codeSvgInputAllowedVarInit = makeCodeBox(2);

        // Collect the SVG input declarations.
        List<SvgIn> svgIns = svgDecls.stream().filter(decl -> decl instanceof SvgIn).map(decl -> (SvgIn)decl).toList();

        // Get the indices of the interactive events, the events coupled to SVG input mappings.
        Set<Integer> interactiveEventIndices = JavaScriptSvgCodeGen.getInteractiveEventIndices(svgIns, events);

        // Generate code, per edge.
        for (int i = 0; i < edges.size(); i++) {
            // Get edge.
            Edge edge = edges.get(i);

            // Get event.
            Assert.check(edge.getEvents().size() == 1);
            Expression eventRef = first(edge.getEvents()).getEvent();
            Event event = (eventRef instanceof TauExpression) ? null : ((EventExpression)eventRef).getEvent();
            int eventIdx = (event == null) ? -1 : events.indexOf(event);
            String eventName = (event == null) ? "tau" : origDeclNames.get(event);
            Assert.notNull(eventName);

            // Add call code.
            codeCalls.add("// Event \"%s\".", eventName);
            codeCalls.add("if (this.execEvent%d()) continue;", i);
            codeCalls.add();

            // Add method code, starting with the header.
            codeMethods.add();
            codeMethods.add("/**");
            codeMethods.add(" * Execute code for event \"%s\".", eventName);
            codeMethods.add(" *");
            codeMethods.add(" * @return 'true' if the event was executed, 'false' otherwise.");
            codeMethods.add(" */");
            codeMethods.add("execEvent%d() {", i);
            codeMethods.indent();

            // Get guard. After linearization, there is at most one
            // (linearized) guard. There may not be a guard, due to value
            // simplification. We don't try to detect always 'true' guards,
            // as that is hard to do, in general.
            List<Expression> guards = edge.getGuards();
            Assert.check(guards.size() <= 1);
            Expression guard = guards.isEmpty() ? null : first(guards);

            // Add guard code.
            if (guard != null) {
                ExprCode guardCode = ctxt.exprToTarget(guard, null);
                codeMethods.add(guardCode.getCode());
                codeMethods.add("var guard = %s;", guardCode.getData());
                codeMethods.add("if (!guard) return false;");
                codeMethods.add();
            }

            // Add code to disable events that are associated to SVG input mappings, to ensure they can't occur until
            // the corresponding SVG element is clicked.
            if (interactiveEventIndices.contains(eventIdx)) {
                // Add variable declaration.
                codeSvgInputAllowedVarDecls.add("event%d_Allowed; // %s", eventIdx, eventName);

                // Add code to initialize the variable.
                codeSvgInputAllowedVarInit.add("%s.event%d_Allowed = false; // %s", ctxt.getPrefix(), eventIdx,
                        eventName);

                // Add code to test whether the event is enabled.
                codeMethods.add("if (!%s.event%d_Allowed) return false;", ctxt.getPrefix(), eventIdx);

                // Add code to reset the variable in case we perform a transition for the event.
                codeMethods.add("%s.event%d_Allowed = false;", ctxt.getPrefix(), eventIdx);
                codeMethods.add();
            }

            // Add pre-transition callbacks code.
            codeMethods.add("if (this.doInfoPrintOutput) this.printOutput(%d, true);", eventIdx);
            codeMethods.add("if (this.doInfoEvent) this.infoEvent(%d, true);", eventIdx);
            codeMethods.add();

            // Add code for updates.
            if (!edge.getUpdates().isEmpty()) {
                addUpdates(edge.getUpdates(), codeMethods, ctxt);
            }

            // Add post-transition callbacks code.
            codeMethods.add();
            codeMethods.add("if (this.doInfoEvent) this.infoEvent(%d, false);", eventIdx);
            codeMethods.add("if (this.doInfoPrintOutput) this.printOutput(%d, false);", eventIdx);
            codeMethods.add("return true;");

            // Method code done.
            codeMethods.dedent();
            codeMethods.add("}");
        }

        // Fill the replacement patterns with generated code.
        replacements.put("javascript-event-calls-code", codeCalls.toString());
        replacements.put("javascript-event-methods-code", codeMethods.toString());
        replacements.put("javascript-event-allowed-declarations", codeSvgInputAllowedVarDecls.toString());
        replacements.put("javascript-event-allowed-init-code", codeSvgInputAllowedVarInit.toString());
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

        DataValue rhsValue = new JavaScriptDataValue(rhsText);

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
                indexTexts[i] = indexVarInfo.targetName;
                rangeErrorTexts.add(new RangeCheckErrorLevelText(true, indexVarInfo.targetName));

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
            containerValue.setDataValue(new JavaScriptDataValue(containerInfo.targetName));

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
                indexVar.setDataValue(new JavaScriptDataValue(indexTexts[i]));

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
            containerCode.setDataValue(new JavaScriptDataValue(containerInfo.targetName));

            // Get the new value.
            ExprCode partCode = new ExprCode();
            if (i == last) {
                partCode.setDataValue(rhsValue);
            } else {
                partCode.setDataValue(new JavaScriptDataValue(partVariables[i].targetName));
            }

            CodeBox modify;
            if (containerInfo.typeInfo instanceof TupleTypeInfo) {
                TupleTypeInfo tupleTi = (TupleTypeInfo)containerInfo.typeInfo;
                LhsTupleProjection tupleLhs = (LhsTupleProjection)lhsProj;

                modify = readCtxt.makeCodeBox();
                modify.add("%s = %s.copy();", containerInfo.targetName, containerInfo.targetName);
                modify.add(tupleTi.modifyContainer(containerInfo, partCode, tupleLhs.fieldNumber, readCtxt));
            } else {
                Assert.check(containerInfo.typeInfo instanceof ArrayTypeInfo);
                ArrayTypeInfo arrayTi = (ArrayTypeInfo)containerInfo.typeInfo;

                ExprCode indexCode = new ExprCode();
                indexCode.setDataValue(new JavaScriptDataValue(indexTexts[i]));
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
    public Destination makeDestination(VariableInformation varInfo) {
        DataValue dataValue = new JavaScriptDataValue(varInfo.targetName);
        return new Destination(null, varInfo.typeInfo, dataValue);
    }

    @Override
    public DataValue makeDataValue(String value) {
        return new JavaScriptDataValue(value);
    }

    /**
     * Write the code files to disk.
     *
     * @param path The absolute or relative local file system path to the output directory to which the code files will
     *     be written.
     */
    @Override
    protected void write(String path) {
        // For the HTML export option, we merge templates using this method.
        // For JavaScript exports, we only export the JavaScript templates via the normal method.
        if (language != TargetLanguage.HTML) {
            super.write(path);
        }

        // Get template names.
        Map<String, String> templates = getTemplates();

        // Create output directory, if it doesn't exist yet.
        String absPath = Paths.resolve(path);
        Path nioAbsPath = java.nio.file.Paths.get(absPath);
        if (!Files.isDirectory(nioAbsPath)) {
            try {
                Files.createDirectories(nioAbsPath);
            } catch (IOException ex) {
                String msg = fmt("Failed to create output directory \"%s\" for the generated code.", path);
                throw new InputOutputException(msg, ex);
            }
        }

        // Replace placeholders in templates. Collect the results as StringBuilders, so that we can merge the results.
        boolean[] used = new boolean[replacements.size()];
        Map<String, TextStringBuilder> stringBuilders = map();
        for (Entry<String, String> template: templates.entrySet()) {
            // Write code.
            String resName = getResourceName(template.getKey());
            Set<Entry<String, String>> replaces = replacements.entrySet();
            ClassLoader classLoader = getClass().getClassLoader();
            TextStringBuilder stringBuilder = new TextStringBuilder();
            stringBuilders.put(template.getKey(), stringBuilder);
            try (InputStream fstream = classLoader.getResourceAsStream(resName);
                 InputStream istream = new BufferedInputStream(fstream);)
            {
                // Process all lines of the template.
                LineIterator lines = IOUtils.lineIterator(istream, "UTF-8");
                while (lines.hasNext()) {
                    // Read the next line.
                    String line = lines.nextLine();

                    // Apply replacements. We skip this for empty lines, as then there is nothing to replace.
                    if (!line.isEmpty()) {
                        // Repeatedly apply replacements, as the replacement may also require replacements.
                        while (true) {
                            boolean anythingReplaced = false;

                            // Apply each replacement, one by one.
                            int i = 0;
                            for (Entry<String, String> replace: replaces) {
                                // Get marker to replace, and replacement text.
                                String name = replace.getKey();
                                String text = replace.getValue();
                                String marker = fmt("${%s}", name);

                                // If we will replace anything, mark that.
                                if (!used[i] && line.contains(marker)) {
                                    used[i] = true;
                                    anythingReplaced = true;
                                }
                                i++;

                                // Perform replacement.
                                line = line.replace(marker, text);
                            }

                            // Stop once no more replacements are possible for the line.
                            if (!anythingReplaced) {
                                break;
                            }
                        }
                    }

                    // Output the line.
                    stringBuilder.appendln(line);
                }
            } catch (IOException ex) {
                // Should not have a read error for templates.
                String msg = "Template read error: " + resName;
                throw new RuntimeException(msg, ex);
            }
        }

        // Make sure all replacements are used.
        int i = 0;
        for (Entry<String, String> replace: replacements.entrySet()) {
            if (!used[i]) {
                String msg = "Unused replacement: " + replace.getKey();
                throw new RuntimeException(msg);
            }
            i++;
        }

        // For HTML exports, we merge in the the utils class common to all exports, and the unique class generated using
        // the provided CIF model, into the HTML file.

        // Get output file path for the HTML template.
        String htmlFileName = ".html";
        htmlFileName = replacements.get("prefix") + htmlFileName;
        String htmlFilePath = Paths.resolve(path, htmlFileName);
        String htmlAbsFilePath = Paths.resolve(htmlFilePath);

        // Replace the placeholders in the HTML file with the generated JavaScript code.
        TextStringBuilder htmlStringBuilder = stringBuilders.get("index.txt");
        htmlStringBuilder.replaceAll("${html-javascript-utils-class-placeholder}",
                stringBuilders.get("utils.txt").toString());
        htmlStringBuilder.replaceAll("${html-javascript-class-placeholder}",
                stringBuilders.get("class.txt").toString());

        // Write to file.
        try {
            FileUtils.writeStringToFile(new File(htmlAbsFilePath), htmlStringBuilder.toString(),
                    StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new InputOutputException("Write error while exporting, file: " + htmlAbsFilePath, ex);
        }
    }
}
