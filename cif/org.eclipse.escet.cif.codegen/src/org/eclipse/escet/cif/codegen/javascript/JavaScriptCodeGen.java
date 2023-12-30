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
import org.eclipse.escet.cif.common.CifIntFuncUtils;
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
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** JavaScript code generator. */
public class JavaScriptCodeGen extends CodeGen {
    /** JavaScript code indent amount, as number of spaces. */
    private static final int INDENT = 4;

    /**
     * Constructor for the {@link JavaScriptCodeGen} class.
     *
     * @param language The target language. Either {@link TargetLanguage#JAVASCRIPT} or {@link TargetLanguage#HTML}.
     */
    public JavaScriptCodeGen(TargetLanguage language) {
        super(language, INDENT);
        Assert.check(language == TargetLanguage.JAVASCRIPT || language == TargetLanguage.HTML);
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
    public String getTargetRef(PositionObject obj) {
        // Get reference consisting only the variable name.
        String ref = super.getTargetRef(obj);

        // If it refers to an object in the main class, prefix the reference.
        if (obj instanceof AlgVariable) {
            return getPrefix() + "." + ref;
        } else if (obj instanceof Constant) {
            return getPrefix() + "." + ref;
        } else if (obj instanceof ContVariable) {
            return getPrefix() + "." + ref;
        } else if (obj instanceof DiscVariable dvar) {
            return CifIntFuncUtils.isFuncParamOrLocalVar(dvar) ? ref : getPrefix() + "." + ref;
        } else if (obj instanceof EnumDecl) {
            return getPrefix() + "." + ref;
        } else if (obj instanceof InputVariable) {
            return getPrefix() + "." + ref;
        } else {
            throw new AssertionError("Unexpected object: " + obj);
        }
    }

    @Override
    protected Map<String, String> getTemplates() {
        Map<String, String> templates = map();
        switch (language) {
            case JAVASCRIPT:
                // Only generate the JavaScript code.
                templates.put("utils.txt", "_utils.js");
                templates.put("class.txt", "_class.js");
                break;
            case HTML:
                // Only one HTML file is generated. The rest is inlined later on.
                templates.put("index.txt", ".html");
                break;
            default:
                throw new AssertionError("Unexpected target language: " + language);
        }
        return templates;
    }

    @Override
    protected void postGenerate(CodeContext ctxt) {
        // For HTML, generate code for updating the frequency slider UI.
        CodeBox frequencySliderCode = makeCodeBox(2);
        if (language == TargetLanguage.HTML) {
            frequencySliderCode.add("// Update frequency UI.");
            frequencySliderCode.add("var range = document.getElementById('run-frequency');");
            frequencySliderCode.add("range.value = %s.frequency;", ctxt.getPrefix());
            frequencySliderCode.add("document.getElementById('run-frequency-output').value = range.value;");
        }
        replacements.put("javascript-frequency-slider-code", frequencySliderCode.toString());

        // For HTML, log not only to the console, but also to the log panel.
        CodeBox logToPanelCode = makeCodeBox(2);
        CodeBox errorToPanelCode = makeCodeBox(2);
        if (language == TargetLanguage.HTML) {
            logToPanelCode.add("var elem = document.getElementById('log-output');");
            logToPanelCode.add("elem.innerHTML += %sUtils.escapeHtml(message) + '\\n';", ctxt.getPrefix());
            logToPanelCode.add("elem.scrollTop = elem.scrollHeight;");

            errorToPanelCode.add("var elem = document.getElementById('log-output');");
            errorToPanelCode.add(
                    "elem.innerHTML += '<span class=\"error\">' + %sUtils.escapeHtml(message) + '</span>\\n';",
                    ctxt.getPrefix());
            errorToPanelCode.add("elem.scrollTop = elem.scrollHeight;");
        }
        replacements.put("html-log-to-panel-code", logToPanelCode.toString());
        replacements.put("html-error-to-panel-code", errorToPanelCode.toString());

        // Add code for the 'getStateText' method.
        // State variables are sorted similarly to 'org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec.init'.
        CodeBox getStateTextCode = makeCodeBox(2);
        List<Declaration> sortedStateVars = stateVars.stream()
                .sorted((var1, var2) -> Strings.SORTER.compare(origDeclNames.get(var1), origDeclNames.get(var2)))
                .toList();
        for (Declaration stateVar: sortedStateVars) {
            String origName = origDeclNames.get(stateVar);
            Assert.notNull(origName);
            getStateTextCode.add("state += %sUtils.fmt(', %s=%%s', %sUtils.valueToStr(%s));", ctxt.getPrefix(),
                    origName, ctxt.getPrefix(), getTargetRef(stateVar));
            if (stateVar instanceof ContVariable) {
                getStateTextCode.add("state += %sUtils.fmt(', %s\\'=%%s', %sUtils.valueToStr(%sderiv()));",
                        ctxt.getPrefix(), origName, ctxt.getPrefix(), getTargetRef(stateVar));
            }
        }
        replacements.put("javascript-get-state-text-code", getStateTextCode.toString());

        // Finalize the replacement patterns based on the target language.
        switch (language) {
            case JAVASCRIPT:
                // Special handling for the JavaScript-only output. Some replacement patterns are not used. Remove
                // them from the replacements mapping.
                replacements.remove("html-svg-in-css");
                replacements.remove("html-svg-content");
                replacements.remove("html-svg-toggles");
                break;
            case HTML:
                // Special handling for single-file HTML output. Inline all templates into the HTML file.
                List<String> utilsLines = readTemplate("utils.txt");
                List<String> classLines = readTemplate("class.txt");
                replacements.put("html-javascript-utils-placeholder", String.join("\n", utilsLines));
                replacements.put("html-javascript-class-placeholder", String.join("\n", classLines));
                break;
            default:
                throw new AssertionError("Unexpected target language: " + language);
        }
    }

    @Override
    protected void addConstants(CodeContext ctxt) {
        CodeBox code = makeCodeBox(1);

        for (int i = 0; i < constants.size(); i++) {
            Constant constant = constants.get(i);
            String origName = origDeclNames.get(constant);
            Assert.notNull(origName);

            ExprCode constantCode = ctxt.exprToTarget(constant.getValue(), null);
            Assert.check(!constantCode.hasCode()); // JavaScript code generator never generates pre-execute code.

            code.add();
            code.add("/** Constant \"%s\". */", origName);
            code.add("%s = %s;", getTargetVariableName(constant), constantCode.getData());
        }

        replacements.put("javascript-const-decls", code.toString());
    }

    @Override
    protected void addEvents(CodeContext ctxt) {
        CodeBox code = makeCodeBox(2);

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            String name = origDeclNames.get(event);
            Assert.notNull(name);
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
            String name = getTargetVariableName(var);
            String kindCode;
            if (var instanceof DiscVariable) {
                kindCode = "Discrete";
            } else {
                kindCode = "Continuous";
            }
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);
            code.add();
            code.add("/** %s variable \"%s\". */", kindCode, origName);
            code.add("%s;", name);
        }

        replacements.put("javascript-state-decls", code.toString());

        // State variable initialization.
        code = makeCodeBox(2);

        for (Declaration var: stateVars) {
            String ref = getTargetRef(var);
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
            code.add("%s = %s;", ref, valueCode.getData());
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
            String name = getTargetVariableName(var);
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
            code.add("var deriv%d = %sderiv();", i, getTargetRef(var));
        }
        if (!contVars.isEmpty()) {
            code.add();
        }
        for (int i = 0; i < contVars.size(); i++) {
            ContVariable var = contVars.get(i);
            String ref = getTargetRef(var);
            code.add("%s = %s + delta * deriv%d;", ref, ref, i);
            String origName = origDeclNames.get(var);
            Assert.notNull(origName);
            code.add("%sUtils.checkReal(%s, %s);", ctxt.getPrefix(), ref, Strings.stringToJava(origName));
            code.add("if (%s == -0.0) %s = 0.0;", ref, ref);
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
            String name = getTargetVariableName(var);
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
            String name = getTargetVariableName(var);
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
        replacements.put("html-svg-content", svgCodeGen.codeSvgContent.toString());
        replacements.put("html-svg-toggles", svgCodeGen.codeSvgToggles.toString());

        // Fill the replacement patterns with generated code, for SVG input mappings.
        replacements.put("javascript-svg-in-event-handlers-code", svgCodeGen.codeInEventHandlers.toString());
        replacements.put("javascript-svg-in-interact-code", svgCodeGen.codeInInteract.toString());
        replacements.put("html-svg-in-css", svgCodeGen.codeInCss.toString());

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
            codeMethods.add("if (this.doStateOutput || this.doTransitionOutput) this.log('');");
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
                indexTexts[i] = indexVarInfo.targetRef;
                rangeErrorTexts.add(new RangeCheckErrorLevelText(true, indexVarInfo.targetVariableName));

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
            containerValue.setDataValue(new JavaScriptDataValue(containerInfo.targetRef));

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
            containerCode.setDataValue(new JavaScriptDataValue(containerInfo.targetRef));

            // Get the new value.
            ExprCode partCode = new ExprCode();
            if (i == last) {
                partCode.setDataValue(rhsValue);
            } else {
                partCode.setDataValue(new JavaScriptDataValue(partVariables[i].targetRef));
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
        DataValue dataValue = new JavaScriptDataValue(varInfo.targetRef);
        return new Destination(null, varInfo.typeInfo, dataValue);
    }

    @Override
    public DataValue makeDataValue(String value) {
        return new JavaScriptDataValue(value);
    }
}
