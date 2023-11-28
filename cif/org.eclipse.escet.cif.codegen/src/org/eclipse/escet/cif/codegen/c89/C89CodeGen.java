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

package org.eclipse.escet.cif.codegen.c89;

import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeComputed;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.c89.typeinfos.C89TypeInfoHelper.typeGetTypePrintName;
import static org.eclipse.escet.cif.codegen.c89.typeinfos.C89TypeInfoHelper.typeUsesValues;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.common.CifValueUtils.getDefaultValue;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeUppercase;
import static org.eclipse.escet.common.java.Strings.spaces;
import static org.eclipse.escet.common.java.Strings.stringToJava;

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
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.cif.print.PrintFor;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.typechecker.annotations.DocAnnotationProvider;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

/** Main code generator class for generating C89 code from CIF. */
public class C89CodeGen extends CodeGen {
    /** C89 code indent amount, as number of spaces. */
    private static final int INDENT = 4;

    /** Reserved words in C89. */
    public static final String[] RESERVED_C89_WORDS = {"auto", "break", "case", "char", "const", "continue", "default",
            "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "register", "return",
            "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void",
            "volatile", "while"};

    /** Name of the 'initial' event in the C89 language. */
    public static final String INITIAL_EVENT_NAME = "EVT_INITIAL_";

    /** Name of the 'delay' event in the C89 language. */
    public static final String DELAY_EVENT_NAME = "EVT_DELAY_";

    /** Name of the 'tau' event in the C89 language. */
    public static final String TAU_EVENT_NAME = "EVT_TAU_";

    /** Name of the enumeration literal names list. */
    public static final String ENUM_NAMES_LIST = "enum_names";

    /** Constructor of the {@link C89CodeGen} class. */
    public C89CodeGen() {
        super(TargetLanguage.C89, INDENT);
    }

    @Override
    protected ExprCodeGen getExpressionCodeGenerator() {
        return new C89ExprCodeGen();
    }

    @Override
    protected TypeCodeGen getTypeCodeGenerator() {
        return new C89TypeCodeGen();
    }

    @Override
    protected void init() {
        super.init();
        replacements.put("generated-types", "");
        replacements.put("algvar-declarations", "");
        replacements.put("algvar-functions", "");
        replacements.put("constant-declarations", "");
        replacements.put("constant-definitions", "");
        replacements.put("constant-initialization", "");
        replacements.put("contvars-update", "");
        replacements.put("derivative-declarations", "");
        replacements.put("derivative-functions", "");
        replacements.put("enum-names-list", "");
        replacements.put("event-calls-code", "");
        replacements.put("event-declarations", "");
        replacements.put("event-methods-code", "");
        replacements.put("event-name-list", "");
        replacements.put("functions-code", "");
        replacements.put("functions-declarations", "");
        replacements.put("generated-types", "");
        replacements.put("initial-print-calls", "");
        replacements.put("initialize-statevars", "");
        replacements.put("inputvar-declarations", "");
        replacements.put("inputvar-definitions", "");
        replacements.put("inputvar-function-call", "");
        replacements.put("inputvar-function-declaration", "");
        replacements.put("input-vars-test-inputvalues", "");
        // replacements.put("prefix", ""); Done in base class.
        replacements.put("print-function", "");
        replacements.put("statevar-declarations", "");
        replacements.put("statevar-definitions", "");
        replacements.put("time-pre-print-call", "");
        replacements.put("time-post-print-call", "");
        replacements.put("type-support-code", "");

        String prefix = replacements.get("prefix");
        replacements.put("PREFIX", makeUppercase(prefix));
    }

    @Override
    protected Set<String> getReservedTargetNames() {
        Set<String> reserved = setc(RESERVED_C89_WORDS.length);
        for (int i = 0; i < RESERVED_C89_WORDS.length; i++) {
            reserved.add(RESERVED_C89_WORDS[i]);
        }
        return reserved;
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

        DataValue rhsValue = makeValue(rhsText);

        // Simple case, variable is fully assigned.
        if (asgn.lhsProjections == null) {
            // No left hand side projections, the right hand side is assigned to the entire variable.

            // Perform range check on the right hand side if required.
            CifType assignedType = asgn.getAssignedType();
            TypeInfo rangeCheckInfo = readCtxt.typeToTarget(assignedType);
            CodeBox rangeCode = makeCodeBox();
            rangeCheckInfo.checkRange(assignedType, asgn.valueType, rhsValue, asgn.variableType, fullVarInfo.name,
                    list(), 0, rangeCode, readCtxt);
            insertRangecheckCode(code, rangeCode);

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
        CodeBox rangeCode = makeCodeBox();
        rangeCheckInfo.checkRange(assignedType, asgn.valueType, rhsValue, asgn.variableType, fullVarInfo.name,
                rangeErrorTexts, 0, rangeCode, readCtxt);
        insertRangecheckCode(code, rangeCode);

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
            containerValue.setDataValue(makeValue(containerInfo.targetName));

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
                indexVar.setDataValue(makeComputed(indexTexts[i]));

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
            containerCode.setDataValue(makeValue(containerInfo.targetName));

            // Get the new value.
            ExprCode partCode = new ExprCode();
            if (i == last) {
                partCode.setDataValue(rhsValue);
            } else {
                partCode.setDataValue(makeValue(partVariables[i].targetName));
            }

            CodeBox modify;
            if (containerInfo.typeInfo instanceof TupleTypeInfo) {
                TupleTypeInfo tupleTi = (TupleTypeInfo)containerInfo.typeInfo;
                LhsTupleProjection tupleLhs = (LhsTupleProjection)lhsProj;

                modify = readCtxt.makeCodeBox();
                modify.add(tupleTi.modifyContainer(containerInfo, partCode, tupleLhs.fieldNumber, readCtxt));
            } else {
                Assert.check(containerInfo.typeInfo instanceof ArrayTypeInfo);
                ArrayTypeInfo arrayTi = (ArrayTypeInfo)containerInfo.typeInfo;

                ExprCode indexCode = new ExprCode();
                indexCode.setDataValue(makeComputed(indexTexts[i]));
                modify = arrayTi.modifyContainer(containerInfo, partCode, indexCode, readCtxt);
            }
            code.add(modify);
        }
    }

    /**
     * Add the generated range check code to the surrounding code.
     *
     * @param surrounding Surrounding code to add to.
     * @param rangeCode Range check code.
     */
    void insertRangecheckCode(CodeBox surrounding, CodeBox rangeCode) {
        if (rangeCode.isEmpty()) {
            return;
        }

        surrounding.add("#if CHECK_RANGES");
        surrounding.add(rangeCode);
        surrounding.add("#endif");
    }

    @Override
    public Destination makeDestination(VariableInformation varInfo) {
        return new Destination(null, varInfo.typeInfo, makeValue(varInfo.targetName));
    }

    @Override
    public DataValue makeDataValue(String value) {
        return makeValue(value);
    }

    @Override
    protected void addConstants(CodeContext ctxt) {
        CodeBox defCode = makeCodeBox();
        CodeBox declCode = makeCodeBox();
        CodeBox initCode = makeCodeBox(1);

        for (Constant constant: constants) {
            VariableWrapper varWrap = new VariableWrapper(constant, false);
            VariableInformation constInfo = ctxt.getReadVarInfo(varWrap);
            String typeName = constInfo.typeInfo.getTargetType();
            String varName = constInfo.targetName;

            // Generate definition and declaration.
            defCode.add("%s %s; /**< Constant \"%s\". */", typeName, varName, constInfo.name);
            declCode.add("extern %s %s; /**< Constant \"%s\". */", typeName, varName, constInfo.name);

            // Generate initialization.
            ExprCode constantCode = ctxt.exprToTarget(constant.getValue(), ctxt.makeDestination(constant));
            initCode.add(constantCode.getCode());
        }
        replacements.put("constant-definitions", defCode.toString());
        replacements.put("constant-declarations", declCode.toString());
        replacements.put("constant-initialization", initCode.toString());
    }

    @Override
    protected void addEvents(CodeContext ctxt) {
        String prefix = replacements.get("prefix");

        // Events declaration.
        CodeBox evtDeclsCode = makeCodeBox();
        evtDeclsCode.add("enum %sEventEnum_ {", prefix);
        evtDeclsCode.indent();

        GridBox evtDecls = new GridBox(3 + events.size(), 2, 0, 1);
        evtDecls.set(0, 0, INITIAL_EVENT_NAME + ",");
        evtDecls.set(0, 1, "/**< Initial step. */");
        evtDecls.set(1, 0, DELAY_EVENT_NAME + ",");
        evtDecls.set(1, 1, "/**< Delay step. */");
        evtDecls.set(2, 0, TAU_EVENT_NAME + ",");
        evtDecls.set(2, 1, "/**< Tau step. */");
        for (int i = 0; i < events.size(); i++) {
            Event evt = events.get(i);
            String origName = origDeclNames.get(evt);
            if (origName == null) {
                origName = evt.getName();
            }
            evtDecls.set(3 + i, 0, fmt("%s,", getTargetName(evt)));
            evtDecls.set(3 + i, 1, fmt("/**< Event %s. */", origName));
        }

        evtDeclsCode.add(evtDecls);
        evtDeclsCode.dedent();
        evtDeclsCode.add("};");
        evtDeclsCode.add("typedef enum %sEventEnum_ %s_Event_;", prefix, prefix);
        replacements.put("event-declarations", evtDeclsCode.toString());

        // Events name list.
        CodeBox evtNamesCode = makeCodeBox(1);

        GridBox evtNames = new GridBox(3 + events.size(), 2, 0, 1);
        evtNames.set(0, 0, "\"initial-step\",");
        evtNames.set(0, 1, "/**< Initial step. */");
        evtNames.set(1, 0, "\"delay-step\",");
        evtNames.set(1, 1, "/**< Delay step. */");
        evtNames.set(2, 0, "\"tau\",");
        evtNames.set(2, 1, "/**< Tau step. */");
        for (int i = 0; i < events.size(); i++) {
            Event evt = events.get(i);
            String origName = origDeclNames.get(evt);
            if (origName == null) {
                origName = evt.getName();
            }
            evtNames.set(3 + i, 0, fmt("\"%s\",", origName));
            evtNames.set(3 + i, 1, fmt("/**< Event %s. */", origName));
        }

        evtNamesCode.add(evtNames);
        replacements.put("event-name-list", evtNamesCode.toString());
    }

    @Override
    protected void addStateVars(CodeContext ctxt) {
        GridBox varDefCode = new GridBox(stateVars.size(), 2, 0, 1);
        GridBox varDeclCode = new GridBox(stateVars.size(), 2, 0, 1);

        // Generate state variable definitions/declarations.
        for (int i = 0; i < stateVars.size(); i++) {
            Declaration decl = stateVars.get(i);
            String typeText, kindText;
            if (decl instanceof DiscVariable) {
                DiscVariable dv = (DiscVariable)decl;
                typeText = typeToStr(dv.getType());
                kindText = "Discrete";
            } else {
                typeText = "real";
                kindText = "Continuous";
            }
            VariableInformation declVarInfo = ctxt.getWriteVarInfo(decl);
            String declaration = fmt("%s %s;", declVarInfo.typeInfo.getTargetType(), declVarInfo.targetName);
            String comment = fmt("/**< %s variable \"%s %s\". */", kindText, typeText, declVarInfo.name);

            varDefCode.set(i, 0, declaration);
            varDefCode.set(i, 1, comment);
            varDeclCode.set(i, 0, "extern " + declaration);
            varDeclCode.set(i, 1, comment);
        }
        replacements.put("statevar-definitions", varDefCode.toString());
        replacements.put("statevar-declarations", varDeclCode.toString());

        // Generate initial value assignments.
        CodeBox code = makeCodeBox(1);
        for (Declaration decl: stateVars) {
            VariableInformation declVarInfo = ctxt.getWriteVarInfo(decl);

            Expression rhs;
            if (decl instanceof DiscVariable) {
                DiscVariable dv = (DiscVariable)decl;
                Assert.check(dv.getValue() != null);
                Assert.check(dv.getValue().getValues().size() == 1);
                rhs = dv.getValue().getValues().get(0);
            } else {
                ContVariable cv = (ContVariable)decl; // Asserted above, thus never fails here.
                rhs = cv.getValue();
            }

            ExprCode initValCode = ctxt.exprToTarget(rhs, makeDestination(declVarInfo));
            code.add(initValCode.getCode());
        }
        replacements.put("initialize-statevars", code.toString());
    }

    @Override
    protected void addContVars(CodeContext ctxt) {
        CodeBox derivDeclCode = makeCodeBox();
        CodeBox derivDefCode = makeCodeBox();

        // Construct derivative computation functions.
        boolean first = true;
        for (ContVariable cv: contVars) {
            VariableInformation cvVarInfo = ctxt.getWriteVarInfo(cv);

            if (!first) {
                derivDefCode.add();
            }
            first = false;

            String header = fmt("RealType %sderiv(void)", cvVarInfo.targetName);
            derivDeclCode.add("%s;", header);

            derivDefCode.add("/** Derivative of \"%s\". */", cvVarInfo.name);
            derivDefCode.add("%s {", header);
            derivDefCode.indent();
            ExprCode derCode = ctxt.exprToTarget(cv.getDerivative(), null);
            derivDefCode.add(derCode.getCode());
            derivDefCode.add("return %s;", derCode.getData());
            derivDefCode.dedent();
            derivDefCode.add("}");
        }
        replacements.put("derivative-declarations", derivDeclCode.toString());
        replacements.put("derivative-functions", derivDefCode.toString());

        // Continuous variables update.

        if (contVars.isEmpty()) {
            replacements.put("contvars-update", "");
            return;
        }

        CodeBox code = makeCodeBox(2);
        // Store current derivative values.
        for (int i = 0; i < contVars.size(); i++) {
            ContVariable cv = contVars.get(i);
            VariableInformation cvVarInfo = ctxt.getWriteVarInfo(cv);
            code.add("RealType deriv%d = %sderiv();", i, cvVarInfo.targetName);
        }
        code.add();

        // Update the continuous variables.
        for (int i = 0; i < contVars.size(); i++) {
            ContVariable var = contVars.get(i);
            String name = getTargetName(var);
            String origName = origDeclNames.get(var);
            code.add("errno = 0;");
            code.add("%s = UpdateContValue(%s + delta * deriv%d, %s, errno == 0);", name, name, i,
                    stringToJava(origName));
        }
        replacements.put("contvars-update", code.toString());
    }

    @Override
    protected void addAlgVars(CodeContext ctxt) {
        CodeBox defCode = makeCodeBox();
        CodeBox declCode = makeCodeBox();

        // Construct algebraic functions.
        boolean first = true;
        for (AlgVariable algVar: algVars) {
            VariableInformation algVarInfo = ctxt.getWriteVarInfo(algVar);

            if (!first) {
                defCode.add();
            }
            first = false;

            // Always return the actual data to the caller (like a function call),
            // since the 'return' will destroy any local data.
            TypeInfo ti = ctxt.typeToTarget(algVar.getType());
            String header = fmt("%s %s(void)", ti.getTargetType(), algVarInfo.targetName);
            declCode.add("%s;", header);

            defCode.add("/**");
            defCode.add(" * Algebraic variable %s = %s;\n", algVarInfo.name, exprToStr(algVar.getValue()));
            defCode.add(" */");
            defCode.add("%s {", header);
            defCode.indent();
            ExprCode valueCode = ctxt.exprToTarget(algVar.getValue(), null);
            defCode.add(valueCode.getCode());
            defCode.add("return %s;", valueCode.getData());
            defCode.dedent();
            defCode.add("}");
        }
        if (!algVars.isEmpty()) {
            declCode.add();
        }

        replacements.put("algvar-declarations", declCode.toString());
        replacements.put("algvar-functions", defCode.toString());
    }

    @Override
    protected void addInputVars(CodeContext ctxt) {
        String prefix = replacements.get("prefix");

        // Generate state variable definitions/declarations.
        CodeBox varDefCode = new MemoryCodeBox();
        CodeBox varDeclCode = new MemoryCodeBox();
        for (int i = 0; i < inputVars.size(); i++) {
            InputVariable var = inputVars.get(i);
            String typeText = typeToStr(var.getType());
            VariableInformation declVarInfo = ctxt.getWriteVarInfo(var);
            String declaration = fmt("%s %s;", declVarInfo.typeInfo.getTargetType(), declVarInfo.targetName);
            String doc = DocAnnotationProvider.getDoc(var);

            for (boolean isDecl: new boolean[] {false, true}) {
                CodeBox code = isDecl ? varDeclCode : varDefCode;
                String fullDeclaration = isDecl ? "extern " + declaration : declaration;
                code.add();
                if (doc == null) {
                    code.add("/** Input variable \"%s %s\". */", typeText, declVarInfo.name);
                } else {
                    code.add("/**");
                    code.add(" * Input variable \"%s %s\".", typeText, declVarInfo.name);
                    code.add(" *");
                    for (String line: doc.split("\\r?\\n")) {
                        code.add(" * %s", line);
                    }
                    code.add(" */");
                }
                code.add(fullDeclaration);
            }
        }
        replacements.put("inputvar-definitions", varDefCode.toString());
        replacements.put("inputvar-declarations", varDeclCode.toString());

        String fillInputDeclaration;
        String fillInputCall;
        if (inputVars.isEmpty()) {
            fillInputDeclaration = "";
            fillInputCall = "";
        } else {
            fillInputDeclaration = fmt("extern void %s_AssignInputVariables();", prefix);
            fillInputCall = spaces(INDENT) + fmt("%s_AssignInputVariables();", prefix);
        }
        replacements.put("inputvar-function-declaration", fillInputDeclaration);
        replacements.put("inputvar-function-call", fillInputCall);

        // Assign input variables.
        CodeBox inputTestCode = makeCodeBox(1);
        if (!inputVars.isEmpty()) {
            boolean first = true;
            List<InternalFunction> funcs = list();
            for (InputVariable var: inputVars) {
                VariableInformation inputVar = ctxt.getWriteVarInfo(var);
                Expression value = getDefaultValue(var.getType(), funcs);
                Destination dest = new Destination(null, inputVar.typeInfo, makeValue(inputVar.targetName));
                ExprCode initCode = ctxt.exprToTarget(value, dest);
                if (!first) {
                    inputTestCode.add();
                }
                first = false;
                inputTestCode.add("/* Input variable \"%s\". */", inputVar.name);
                inputTestCode.add(initCode.getCode());
            }
            Assert.check(funcs.isEmpty());
        }
        replacements.put("input-vars-test-inputvalues", inputTestCode.toString());
    }

    @Override
    protected void addFunctions(CodeContext ctxt) {
        CodeBox declCode = makeCodeBox(0);
        CodeBox defCode = makeCodeBox(0);

        boolean first = true;
        for (InternalFunction func: functions) {
            if (!first) {
                defCode.add();
            }
            first = false;

            C89FunctionCodeGen funcGen = new C89FunctionCodeGen(false, func);
            funcGen.generate(declCode, defCode, ctxt);
        }
        replacements.put("functions-declarations", declCode.toString());
        replacements.put("functions-code", defCode.toString());
    }

    @Override
    protected void addEnum(EnumDecl enumDecl, CodeContext ctxt) {
        // Generated by the type code generator.
        CifType enumType = newEnumType(enumDecl, null);
        ctxt.typeToTarget(enumType);

        CodeBox enumNames = ctxt.makeCodeBox();
        enumNames.add("const char *%s[] = {", ENUM_NAMES_LIST);
        enumNames.indent();
        for (EnumLiteral eLit: enumDecl.getLiterals()) {
            enumNames.add("%s,", stringToJava(eLit.getName()));
        }
        enumNames.dedent();
        enumNames.add("};");

        replacements.put("enum-names-list", enumNames.toString());
    }

    @Override
    protected void addPrints(CodeContext ctxt) {
        CodeBox code = makeCodeBox();
        code.add("#if PRINT_OUTPUT");
        String prefix = replacements.get("prefix");
        code.add("static void PrintOutput(%s_Event_ event, BoolType pre) {", prefix);
        code.indent();

        // Local text string buffer variable.
        VariableInformation txtVarInfo = ctxt.makeTempVariable(newStringType(), "text_var");

        // Global code layout:
        // if (pre) {
        // forall Print: if (Print.whenPre && Print.fors) print(Print.textPre);
        // } else {
        // forall Print: if (Print.whenPost && Print.fors) print(Print.textPost);
        // }
        List<CodeBox> preCodes = listc(printDecls.size());
        List<CodeBox> postCodes = listc(printDecls.size());
        for (Print print: printDecls) {
            String targetFile = stringToJava(print.getFile().getPath());

            CodeBox printCode = genPrint(print.getWhenPre(), print.getFors(), print.getTxtPre(), txtVarInfo, targetFile,
                    ctxt);
            if (printCode != null) {
                preCodes.add(printCode);
            }
            printCode = genPrint(print.getWhenPost(), print.getFors(), print.getTxtPost(), txtVarInfo, targetFile,
                    ctxt);
            if (printCode != null) {
                postCodes.add(printCode);
            }
        }

        if (!preCodes.isEmpty() || !postCodes.isEmpty()) {
            // Construct temporary string variable for generating output lines.
            code.add("%s %s;", txtVarInfo.typeInfo.getTargetType(), txtVarInfo.targetName);
            code.add();

            if (!preCodes.isEmpty()) {
                code.add("if (pre) {");
                code.indent();
                boolean first = true;
                for (CodeBox box: preCodes) {
                    if (!first) {
                        code.add();
                    }
                    first = false;
                    code.add(box);
                }
                code.dedent();
            }
            if (!postCodes.isEmpty()) {
                code.add(preCodes.isEmpty() ? "if (!pre) {" : "} else {");
                code.indent();
                boolean first = true;
                for (CodeBox box: postCodes) {
                    if (!first) {
                        code.add();
                    }
                    first = false;
                    code.add(box);
                }
                code.dedent();
            }
            code.add("}");
        }
        code.dedent();
        code.add("}");
        code.add("#endif");

        replacements.put("print-function", code.toString());
    }

    /**
     * Generate the event conditions for performing a print command.
     *
     * @param eventVar Variable name holding the event.
     * @param fors Conditions of the print.
     * @return A string with a test when the print command may be performed (wrt the event condition). {@code null} if
     *     always {@code false}.
     */
    private String makeForPrintConditions(String eventVar, List<PrintFor> fors) {
        if (fors.isEmpty()) {
            return "TRUE";
        }

        List<String> conds = listc(fors.size());
        for (PrintFor pf: fors) {
            switch (pf.getKind()) {
                case EVENT:
                    conds.add(fmt("%s >= %s", eventVar, TAU_EVENT_NAME));
                    break;

                case FINAL:
                    // Never happens as program never terminates.
                    break;

                case INITIAL:
                    conds.add(fmt("%s == %s", eventVar, INITIAL_EVENT_NAME));
                    break;

                case NAME: {
                    Expression eventRef = pf.getEvent();
                    Assert.check(eventRef instanceof EventExpression);
                    Event event = ((EventExpression)eventRef).getEvent();
                    conds.add(fmt("%s == %s", eventVar, getTargetName(event)));
                    break;
                }

                case TIME:
                    conds.add(fmt("%s == %s", eventVar, DELAY_EVENT_NAME));
                    break;

                default:
                    Assert.fail("Unexpected kind of print event.");
                    break;
            }
        }

        if (conds.isEmpty()) {
            return null; // false.
        }
        if (conds.size() == 1) {
            return conds.get(0);
        }
        return String.join(" || ", conds);
    }

    /**
     * Construct code to print output.
     *
     * @param whenPred Condition when to print the text.
     * @param fors More condition when to print the text.
     * @param txtExpr Text to print (only used for checking existence of output).
     * @param txtVarInfo Temporary string variable to use for generating the print text.
     * @param targetFile String denoting the target of the output.
     * @param ctxt Code generation context.
     * @return {@code null} if no output is ever generated, else the code to generate output.
     */
    private CodeBox genPrint(Expression whenPred, List<PrintFor> fors, Expression txtExpr,
            VariableInformation txtVarInfo, String targetFile, CodeContext ctxt)
    {
        if (txtExpr == null) {
            return null;
        }

        String forConds = makeForPrintConditions("event", fors);
        if (forConds == null) {
            return null;
        }

        // Construct text to output in the temporary variable.
        CodeBox valueCode = ctxt.makeCodeBox();
        CifType valueType = normalizeType(txtExpr.getType());
        if (valueType instanceof StringType) {
            // Special case for strings
            ExprCode strCode = ctxt.exprToTarget(txtExpr, makeDestination(txtVarInfo));
            valueCode.add(strCode.getCode());
        } else {
            TypeInfo ti = ctxt.typeToTarget(valueType);
            ExprCode strCode = ctxt.exprToTarget(txtExpr, null);
            valueCode.add(strCode.getCode());

            // Compute the text to use to denote the vlaue to print in the target language.
            DataValue dataValue = strCode.getRawDataValue();
            String valueText;
            if (typeUsesValues(ti)) {
                // Value should be a data value.
                valueText = dataValue.getData();
            } else {
                // Needs to be a reference.
                if (dataValue.canBeReferenced()) {
                    valueText = dataValue.getReference();
                } else {
                    // Ugh, need to make a temporary variable first.
                    VariableInformation tempVar = ctxt.makeTempVariable(ti, "print_temp");
                    valueCode.add(fmt("%s %s = %s;", ti.getTargetType(), tempVar.targetName, dataValue.getData()));
                    valueText = "&" + tempVar.targetName;
                }
            }
            valueCode.add("%s(%s, %s.data, 0, MAX_STRING_SIZE);", typeGetTypePrintName(ti, true), valueText,
                    txtVarInfo.targetName);
        }
        // Construct code with condition, and output generation.
        CodeBox result = ctxt.makeCodeBox();

        boolean unconditional = forConds.equals("TRUE");
        if (whenPred != null) {
            ExprCode whenCondCode = ctxt.exprToTarget(whenPred, null);
            result.add(whenCondCode.getCode());

            if (unconditional) {
                forConds = whenCondCode.getData();
                unconditional = false;
            } else {
                forConds = fmt("(%s) && (%s)", forConds, whenCondCode.getData());
            }
        }

        if (!unconditional) {
            result.add("if (%s) {", forConds);
            result.indent();
        }
        result.add(valueCode);
        String prefix = replacements.get("prefix");
        result.add("%s_PrintOutput(%s.data, %s);", prefix, txtVarInfo.targetName, targetFile);
        if (!unconditional) {
            result.dedent();
            result.add("}");
        }
        return result;
    }

    @Override
    protected void addEdges(CodeContext ctxt) {
        CodeBox codeCalls = makeCodeBox(2);
        CodeBox codeMethods = makeCodeBox(0);
        String prefix = replacements.get("prefix");

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            // Get event.
            Assert.check(edge.getEvents().size() == 1);
            Expression eventRef = first(edge.getEvents()).getEvent();
            Event event = (eventRef instanceof TauExpression) ? null : ((EventExpression)eventRef).getEvent();

            String eventName, eventTargetName;
            if (event == null) {
                eventName = "tau";
                eventTargetName = TAU_EVENT_NAME;
            } else {
                eventName = origDeclNames.get(event);
                eventTargetName = getTargetName(event);
            }

            // Construct the call to try executing the event.
            codeCalls.add("if (execEvent%d()) continue;  /* (Try to) perform event \"%s\". */", i, eventName);

            // Add method code.

            // Header.
            codeMethods.add();
            codeMethods.add("/**");
            codeMethods.add(" * Execute code for event \"%s\".", eventName);
            codeMethods.add(" *");
            codeMethods.add(" * @return Whether the event was performed.");
            codeMethods.add(" */");
            codeMethods.add("static BoolType execEvent%d(void) {", i);
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
                codeMethods.add("BoolType guard = %s;", guardCode.getData());
                codeMethods.add("if (!guard) return FALSE;");
                codeMethods.add();
            }
            if (!printDecls.isEmpty()) {
                codeMethods.add("#if PRINT_OUTPUT");
                codeMethods.indent();
                codeMethods.add("PrintOutput(%s, TRUE);", eventTargetName);
                codeMethods.dedent();
                codeMethods.add("#endif");
            }
            codeMethods.add("#if EVENT_OUTPUT");
            codeMethods.indent();
            codeMethods.add("%s_InfoEvent(%s, TRUE);", prefix, eventTargetName);
            codeMethods.dedent();
            codeMethods.add("#endif");
            codeMethods.add();
            if (!edge.getUpdates().isEmpty()) {
                addUpdates(edge.getUpdates(), codeMethods, ctxt);
                codeMethods.add();
            }
            codeMethods.add("#if EVENT_OUTPUT");
            codeMethods.indent();
            codeMethods.add("%s_InfoEvent(%s, FALSE);", prefix, eventTargetName);
            codeMethods.dedent();
            codeMethods.add("#endif");
            if (!printDecls.isEmpty()) {
                codeMethods.add("#if PRINT_OUTPUT");
                codeMethods.indent();
                codeMethods.add("PrintOutput(%s, FALSE);", eventTargetName);
                codeMethods.dedent();
                codeMethods.add("#endif");
            }
            codeMethods.add("return TRUE;");

            // Method code done.
            codeMethods.dedent();
            codeMethods.add("}");
        }

        replacements.put("event-calls-code", codeCalls.toString());
        replacements.put("event-methods-code", codeMethods.toString());

        // 'Initial' calls.
        CodeBox code = makeCodeBox(1);
        code.add("#if PRINT_OUTPUT");
        code.indent();
        code.add("/* pre-initial and post-initial prints. */");
        code.add("PrintOutput(%s, TRUE);", INITIAL_EVENT_NAME);
        code.add("PrintOutput(%s, FALSE);", INITIAL_EVENT_NAME);
        code.dedent();
        code.add("#endif");
        replacements.put("initial-print-calls", code.toString());

        code = makeCodeBox(1);
        code.add("#if PRINT_OUTPUT");
        code.indent();
        code.add("/* pre-timestep print. */");
        code.add("PrintOutput(%s, TRUE);", DELAY_EVENT_NAME);
        code.dedent();
        code.add("#endif");
        replacements.put("time-pre-print-call", code.toString());

        code = makeCodeBox(1);
        code.add("#if PRINT_OUTPUT");
        code.indent();
        code.add("/* post-timestep print. */");
        code.add("PrintOutput(%s, FALSE);", DELAY_EVENT_NAME);
        code.dedent();
        code.add("#endif");
        replacements.put("time-post-print-call", code.toString());
    }

    @Override
    protected IfElseGenerator getIfElseUpdateGenerator() {
        return new CurlyBraceIfElseGenerator();
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
    protected Map<String, String> getTemplates() {
        Map<String, String> templates = map();
        templates.put("compile.sh", "_compile.sh");
        templates.put("library.h", "_library.h");
        templates.put("library.c", "_library.c");
        templates.put("engine.h", "_engine.h");
        templates.put("engine.c", "_engine.c");
        templates.put("test_code.c", "_test_code.c");
        templates.put("readme.txt", "_readme.txt");
        return templates;
    }
}
