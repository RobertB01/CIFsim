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

package org.eclipse.escet.cif.codegen.simulink;

import static org.eclipse.escet.cif.codegen.c89.C89CodeGen.RESERVED_C89_WORDS;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeComputed;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.c89.typeinfos.C89TypeInfoHelper.typeGetTypePrintName;
import static org.eclipse.escet.cif.codegen.c89.typeinfos.C89TypeInfoHelper.typeUsesValues;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleOffsetOption.getSampleOffset;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleOffsetOption.offsetGetValue;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleOffsetOption.sampleOffsetIsFixed;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleOffsetOption.sampleOffsetIsValidReal;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleOffsetOption.sampleOffsetIsZero;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleTimeOption.getSampleTime;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleTimeOption.getSimulinkSampleTimeText;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleTimeOption.offsetMayBeFixed;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleTimeOption.offsetMayBeNonzero;
import static org.eclipse.escet.cif.codegen.options.SimulinkSampleTimeOption.sampleGetValue;
import static org.eclipse.escet.cif.codegen.simulink.typeinfos.SimulinkArrayTypeInfo.getElementConversionToSimulinkVector;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getName;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.cif.common.CifValueUtils.isTimeConstant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;
import static org.eclipse.escet.common.java.Strings.stringToJava;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.eclipse.escet.cif.codegen.options.SimulinkOutputsOption;
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
import org.eclipse.escet.cif.common.CifTextUtils;
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
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.common.app.framework.options.processing.PatternMatchingOptionProcessing.OptionMatcher;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Main code generator class for generating C89 Simulink code from CIF. */
public class SimulinkCodeGen extends CodeGen {
    /** Simulink code indent amount, as number of spaces. */
    public static final int INDENT = 4;

    /** Reserved words in C89. */
    public static final String[] RESERVED_SIMULINK_WORDS = RESERVED_C89_WORDS;

    /** Name of the 'initial' event in the C89 language. */
    public static final String INITIAL_EVENT_NAME = "EVT_INITIAL_";

    /** Name of the 'delay' event in the C89 language. */
    public static final String DELAY_EVENT_NAME = "EVT_DELAY_";

    /** Name of the enumeration literal names list. */
    public static final String ENUM_NAMES_LIST = "enum_names";

    /** Mapping of CIF variables and declarations to the reference code to use to refer to them in Simulink code. */
    public Map<PositionObject, String> simulinkTargetRefMap = null;

    /** Map of variables to their output port index in Simulink. */
    public Map<Declaration, Integer> outputMap;

    /** Constructor of the {@link SimulinkCodeGen} class. */
    public SimulinkCodeGen() {
        super(TargetLanguage.SIMULINK, INDENT);
    }

    @Override
    protected ExprCodeGen getExpressionCodeGenerator() {
        return new SimulinkExprCodeGen(inputVars, contVars);
    }

    @Override
    protected TypeCodeGen getTypeCodeGenerator() {
        return new SimulinkTypeCodeGen();
    }

    @Override
    protected void init() {
        super.init();
        simulinkTargetRefMap = null; // Mark as non-initialized.

        replacements.put("generated-types", "");
        replacements.put("type-support-code", "");
    }

    @Override
    protected Set<String> getReservedTargetNames() {
        Set<String> reserved = setc(RESERVED_SIMULINK_WORDS.length);
        for (int i = 0; i < RESERVED_SIMULINK_WORDS.length; i++) {
            reserved.add(RESERVED_SIMULINK_WORDS[i]);
        }
        return reserved;
    }

    /** Setup the {@link #simulinkTargetRefMap} and {@link #outputMap} for generating Simulink code. */
    private void setupVarMaps() {
        if (simulinkTargetRefMap != null) {
            return;
        }

        simulinkTargetRefMap = map();
        outputMap = map();
        OptionMatcher outputVarMatcher = SimulinkOutputsOption.getMatcher();

        List<Pair<String, String>> modeReport = list();
        List<Pair<String, String>> outputReport = list();
        List<Pair<String, String>> cstateReport = list();

        List<Pair<Declaration, String>> reportSection = list(); // Pairs of declaration and name.

        int i = 0;
        int outputIndex = 0;
        for (DiscVariable lpVar: lpVariables) {
            simulinkTargetRefMap.put(lpVar, fmt("modes[%d]", i));
            reportLine(lpVar, i, modeReport);
            addDeclarationToSection(outputVarMatcher, lpVar, reportSection);
            i++;
        }
        replacements.put("number-of-modes", str(i));
        writeReport("mode-report", modeReport);
        outputIndex = moveSection(reportSection, outputIndex, outputMap, outputReport);

        cstateReport.add(Pair.pair("time", "1"));

        i = 1; // cstate[0] is time.
        for (ContVariable cVar: contVars) {
            simulinkTargetRefMap.put(cVar, fmt("cstate[%d]", i));
            reportLine(cVar, i, cstateReport);
            addDeclarationToSection(outputVarMatcher, cVar, reportSection);
            i++;
        }
        replacements.put("number-of-contvars", str(i));
        writeReport("cstate-report", cstateReport);
        outputIndex = moveSection(reportSection, outputIndex, outputMap, outputReport);

        for (Constant cVar: constants) {
            simulinkTargetRefMap.put(cVar, fmt("work->%s", super.getTargetRef(cVar)));
        }
        for (InputVariable inpVar: inputVars) {
            simulinkTargetRefMap.put(inpVar, fmt("work->%s", super.getTargetRef(inpVar)));
        }

        for (Declaration stateVar: stateVars) {
            if (!(stateVar instanceof DiscVariable discVar)) {
                continue;
            }
            if (simulinkTargetRefMap.containsKey(discVar)) {
                continue; // Modes have already been added.
            }

            simulinkTargetRefMap.put(discVar, fmt("work->%s", super.getTargetRef(discVar)));
            CifType type = discVar.getType();
            if (SimulinkTypeUtils.isSimulinkCompatibleType(type)) {
                addDeclarationToSection(outputVarMatcher, discVar, reportSection);
            } else {
                String origName = origDeclNames.get(discVar);
                Assert.notNull(origName);
                String msg = fmt("Discrete variable \"%s\" has non-Simulink-compatible type \"%s\", and will therefore "
                        + "be omitted from the output.", origName, CifTextUtils.typeToStr(type));
                OutputProvider.warn(msg);
            }
        }
        outputIndex = moveSection(reportSection, outputIndex, outputMap, outputReport);

        for (AlgVariable algVar: algVars) {
            CifType type = algVar.getType();
            if (SimulinkTypeUtils.isSimulinkCompatibleType(type)) {
                addDeclarationToSection(outputVarMatcher, algVar, reportSection);
            } else {
                String origName = origDeclNames.get(algVar);
                Assert.notNull(origName);
                String msg = fmt("Algebraic variable \"%s\" has non-Simulink-compatible type \"%s\", and will "
                        + "therefore be omitted from the output.", origName, CifTextUtils.typeToStr(type));
                OutputProvider.warn(msg);
            }
        }
        outputIndex = moveSection(reportSection, outputIndex, outputMap, outputReport);

        replacements.put("number-of-outputs", str(outputIndex));
        writeReport("output-report", outputReport);
    }

    /**
     * Add a declaration to a section of the output.
     *
     * @param optionMatcher Matcher object to decide whether the declaration should be available.
     * @param decl Declaration to (possibly) add.
     * @param outputSection Collected output until now (added in-place).
     */
    private void addDeclarationToSection(OptionMatcher optionMatcher, Declaration decl,
            List<Pair<Declaration, String>> outputSection)
    {
        if (optionMatcher == null) {
            return; // Nothing gets added to the output.
        }

        String origName = origDeclNames.get(decl);
        Assert.notNull(origName);
        if (optionMatcher.matchName(origName, false)) {
            outputSection.add(Pair.pair(decl, origName));
        }
    }

    /**
     * Sort entries in the output section, and move them to the output map and the output report.
     *
     * @param outputSection Collected entries so far. Gets cleared during the call.
     * @param startIndex First free index in the output map.
     * @param outputMap Outputs added so far, gets updated during the call.
     * @param outputReport Entries in the report so far, gets updated during the call.
     * @return The next first free index in the output map.
     */
    private int moveSection(List<Pair<Declaration, String>> outputSection, int startIndex,
            Map<Declaration, Integer> outputMap, List<Pair<String, String>> outputReport)
    {
        if (outputSection.isEmpty()) {
            return startIndex; // Quick exit if there is nothing to do.
        }

        Collections.sort(outputSection, new DeclarationCompator());
        for (Pair<Declaration, String> p: outputSection) {
            outputReport.add(Pair.pair(p.right, str(startIndex + 1))); // Matlab is 1-based.
            outputMap.put(p.left, startIndex);
            startIndex++;
        }
        outputSection.clear();
        return startIndex;
    }

    /** Comparator object for sorting output sections. */
    private static class DeclarationCompator implements Comparator<Pair<Declaration, String>> {
        @Override
        public int compare(Pair<Declaration, String> p1, Pair<Declaration, String> p2) {
            return p1.right.compareTo(p2.right);
        }
    }

    /**
     * Add a line about the given object being at the given index position in the given report.
     *
     * @param obj Object to add.
     * @param destIndex Index position of the object (0-based).
     * @param report Report collecting the indices.
     */
    private void reportLine(PositionObject obj, int destIndex, List<Pair<String, String>> report) {
        String origName = origDeclNames.get(obj);
        if (origName == null) {
            // New object, introduced by preprocessing and/or linearization.
            origName = getName(obj);
        }

        report.add(Pair.pair(origName, str(destIndex + 1))); // Convert to 1-based for matlab output.
    }

    /**
     * Output a vector report to the replacement texts.
     *
     * @param replName Name of the replacement text.
     * @param report Report to format and add to the {@link #replacements}.
     */
    private void writeReport(String replName, List<Pair<String, String>> report) {
        if (report.isEmpty()) {
            replacements.put(replName, "No variables are available here.");
            return;
        }

        GridBox reportLines = new GridBox(report.size(), 2, 0, 1);
        int row = 0;
        for (Pair<String, String> pair: report) {
            reportLines.set(row, 0, pair.left);
            reportLines.set(row, 1, pair.right);
            row++;
        }
        replacements.put(replName, reportLines.toString());
    }

    @Override
    public String getTargetRef(PositionObject obj) {
        if (simulinkTargetRefMap == null) {
            setupVarMaps();
        }

        String result = simulinkTargetRefMap.get(obj);
        if (result != null) {
            return result;
        }

        result = super.getTargetRef(obj);
        simulinkTargetRefMap.put(obj, result);
        return result;
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
            containerValue.setDataValue(makeValue(containerInfo.targetRef));

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
            containerCode.setDataValue(makeValue(containerInfo.targetRef));

            // Get the new value.
            ExprCode partCode = new ExprCode();
            if (i == last) {
                partCode.setDataValue(rhsValue);
            } else {
                partCode.setDataValue(makeValue(partVariables[i].targetRef));
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
        return new Destination(null, varInfo.typeInfo, makeValue(varInfo.targetRef));
    }

    @Override
    public DataValue makeDataValue(String value) {
        return makeValue(value);
    }

    /**
     * Generate the pre-amble code to make Simulink storage structures available.
     *
     * @param code Destination for writing the pre-amble.
     * @param clearInputs Whether to generate code for clearing the input flags.
     */
    private void addPreamble(CodeBox code, boolean clearInputs) {
        code.add("struct WorkStruct *work = ssGetPWorkValue(sim_struct, 0);");
        code.add("int_T *modes = ssGetModeVector(sim_struct);");
        code.add("real_T *cstate = ssGetContStates(sim_struct);");
        if (clearInputs && !inputVars.isEmpty()) {
            code.add("ClearInputFlags(work);");
        }
    }

    @Override
    protected void addConstants(CodeContext ctxt) {
        CodeBox defCode = makeCodeBox(1);
        CodeBox initCode = makeCodeBox(1);

        for (Constant constant: constants) {
            VariableWrapper varWrap = new VariableWrapper(constant, false);
            VariableInformation constInfo = ctxt.getReadVarInfo(varWrap);
            String typeName = constInfo.typeInfo.getTargetType();
            String varName = getTargetVariableName(constant);

            List<String> docs = CifDocAnnotationUtils.getDocs(constant);

            // Generate definition and declaration.
            defCode.add();
            if (docs.isEmpty()) {
                defCode.add("/** Constant \"%s\". */", constInfo.name);
            } else {
                defCode.add("/**");
                defCode.add(" * Constant \"%s\".", constInfo.name);
                for (String doc: docs) {
                    defCode.add(" *");
                    for (String line: doc.split("\\r?\\n")) {
                        defCode.add(" * %s", line);
                    }
                }
                defCode.add(" */");
            }
            defCode.add("%s %s;", typeName, varName);

            // Generate initialization.
            ExprCode constantCode = ctxt.exprToTarget(constant.getValue(), ctxt.makeDestination(constant));
            initCode.add(constantCode.getCode());
        }
        replacements.put("constant-definitions", defCode.toString());
        replacements.put("constant-initialization", initCode.toString());
    }

    @Override
    protected void addEvents(CodeContext ctxt) {
        String prefix = replacements.get("prefix");

        // Events declaration.
        CodeBox evtDeclsCode = makeCodeBox();
        evtDeclsCode.add("enum %sEventEnum_ {", prefix);
        evtDeclsCode.indent();

        evtDeclsCode.add("/** Initial step. */");
        evtDeclsCode.add(INITIAL_EVENT_NAME + ",");

        evtDeclsCode.add();
        evtDeclsCode.add("/** Delay step. */");
        evtDeclsCode.add(DELAY_EVENT_NAME + ",");

        for (int i = 0; i < events.size(); i++) {
            Event evt = events.get(i);
            String origName = origDeclNames.get(evt);
            Assert.notNull(origName);
            List<String> docs = CifDocAnnotationUtils.getDocs(evt);

            evtDeclsCode.add();
            if (docs.isEmpty()) {
                evtDeclsCode.add(fmt("/** Event \"%s\". */", origName));
            } else {
                evtDeclsCode.add("/**");
                evtDeclsCode.add(" * Event \"%s\".", origName);
                for (String doc: docs) {
                    evtDeclsCode.add(" *");
                    for (String line: doc.split("\\r?\\n")) {
                        evtDeclsCode.add(" * %s", line);
                    }
                }
                evtDeclsCode.add(" */");
            }
            evtDeclsCode.add(fmt("%s,", getTargetRef(evt)));
        }

        evtDeclsCode.dedent();
        evtDeclsCode.add("};");
        evtDeclsCode.add("typedef enum %sEventEnum_ %s_Event_;", prefix, prefix);
        replacements.put("event-declarations", evtDeclsCode.toString());

        // Events name list.
        CodeBox evtNamesCode = makeCodeBox(1);

        GridBox evtNames = new GridBox(2 + events.size(), 2, 0, 1);
        evtNames.set(0, 0, "\"initial-step\",");
        evtNames.set(0, 1, "/**< Initial step. */");
        evtNames.set(1, 0, "\"delay-step\",");
        evtNames.set(1, 1, "/**< Delay step. */");
        for (int i = 0; i < events.size(); i++) {
            Event evt = events.get(i);
            String origName = origDeclNames.get(evt);
            Assert.notNull(origName);
            evtNames.set(2 + i, 0, fmt("\"%s\",", origName));
            evtNames.set(2 + i, 1, fmt("/**< Event \"%s\". */", origName));
        }

        evtNamesCode.add(evtNames);
        replacements.put("event-name-list", evtNamesCode.toString());
    }

    @Override
    protected void addStateVars(CodeContext ctxt) {
        // Add common pre-amble to access Simulink data.
        CodeBox preAmble = makeCodeBox(1);
        addPreamble(preAmble, true);
        replacements.put("preamble", preAmble.toString());

        // Generate state variable definitions/declarations.
        Set<DiscVariable> lpVarSet = setc(lpVariables.size());
        lpVarSet.addAll(lpVariables);

        CodeBox varDefCode = makeCodeBox();
        boolean first = true;
        for (Declaration decl: stateVars) {
            if (lpVarSet.contains(decl)) {
                continue;
            }

            if (decl instanceof DiscVariable) {
                DiscVariable dv = (DiscVariable)decl;
                String typeText = typeToStr(dv.getType());

                VariableInformation declVarInfo = ctxt.getWriteVarInfo(decl);
                String declaration = fmt("%s %s;", declVarInfo.typeInfo.getTargetType(),
                        declVarInfo.targetVariableName);
                List<String> docs = CifDocAnnotationUtils.getDocs(dv);

                if (!first) {
                    varDefCode.add();
                }
                first = false;

                if (docs.isEmpty()) {
                    varDefCode.add("/** Discrete variable \"%s %s\". */", typeText, declVarInfo.name);
                } else {
                    varDefCode.add("/**");
                    varDefCode.add(" * Discrete variable \"%s %s\".", typeText, declVarInfo.name);
                    for (String doc: docs) {
                        varDefCode.add(" *");
                        for (String line: doc.split("\\r?\\n")) {
                            varDefCode.add(" * %s", line);
                        }
                    }
                    varDefCode.add(" */");
                }
                varDefCode.add(declaration);
            }
        }
        CodeBox code = makeCodeBox(1);
        code.add(varDefCode);
        replacements.put("discvar-definitions", code.toString());

        // Generate initial value assignments.
        code = makeCodeBox(1);
        for (Declaration decl: stateVars) {
            VariableInformation declVarInfo = ctxt.getWriteVarInfo(decl);

            Expression rhs;
            if (decl instanceof DiscVariable) {
                DiscVariable dv = (DiscVariable)decl;
                Assert.check(dv.getValue() != null);
                Assert.check(dv.getValue().getValues().size() == 1);
                rhs = dv.getValue().getValues().get(0);
            } else {
                Assert.check(decl instanceof ContVariable);
                ContVariable cvar = (ContVariable)decl;
                rhs = cvar.getValue();
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
        CodeBox assignDerivCode = makeCodeBox(1);

        assignDerivCode.add("derivs[0] = 1.0;");

        // Construct derivative computation functions.
        int i = 0;
        for (ContVariable cv: contVars) {
            VariableInformation cvVarInfo = ctxt.getWriteVarInfo(cv);

            if (i != 0) {
                derivDefCode.add();
            }

            String header = fmt("static real_T deriv%02d(SimStruct *sim_struct)", i + 1);
            derivDeclCode.add("%s;", header);

            derivDefCode.add("/** Derivative of \"%s\". */", cvVarInfo.name);
            derivDefCode.add("%s {", header);
            derivDefCode.indent();
            addPreamble(derivDefCode, false);
            derivDefCode.add();
            ExprCode derCode = ctxt.exprToTarget(cv.getDerivative(), null);
            derivDefCode.add(derCode.getCode());
            derivDefCode.add("return %s;", derCode.getData());
            derivDefCode.dedent();
            derivDefCode.add("}");

            assignDerivCode.add("derivs[%d] = deriv%02d(sim_struct);", i + 1, i + 1);
            i++;
        }
        replacements.put("derivative-declarations", derivDeclCode.toString());
        replacements.put("derivative-functions", derivDefCode.toString());
        replacements.put("assign-derivatives", assignDerivCode.toString());
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
            String header = fmt("static %s %s(SimStruct *sim_struct)", ti.getTargetType(), algVarInfo.targetRef);
            declCode.add("%s;", header);

            List<String> docs = CifDocAnnotationUtils.getDocs(algVar);
            if (docs.isEmpty()) {
                defCode.add("/** Algebraic variable %s = %s. */", algVarInfo.name, exprToStr(algVar.getValue()));
            } else {
                defCode.add("/**");
                defCode.add(" * Algebraic variable %s = %s.", algVarInfo.name, exprToStr(algVar.getValue()));
                for (String doc: docs) {
                    defCode.add(" *");
                    for (String line: doc.split("\\r?\\n")) {
                        defCode.add(" * %s", line);
                    }
                }
                defCode.add(" */");
            }

            defCode.add("%s {", header);
            defCode.indent();
            addPreamble(defCode, false);
            defCode.add();
            ExprCode valueCode = ctxt.exprToTarget(algVar.getValue(), null);
            defCode.add(valueCode.getCode());
            defCode.add("return %s;", valueCode.getData());
            defCode.dedent();
            defCode.add("}");
        }
        if (!algVars.isEmpty()) {
            declCode.add();
        }

        replacements.put("algvar-inline-declarations", declCode.toString());
        replacements.put("algvar-inline-functions", defCode.toString());
    }

    @Override
    protected void addInputVars(CodeContext ctxt) {
        CodeBox varDefCode = new MemoryCodeBox();
        CodeBox inputFlagsCode = makeCodeBox();
        CodeBox inputClearCode = makeCodeBox(1);
        List<Pair<String, String>> inputReport = list();

        // Generate state variable definitions/declarations.
        boolean first = true;
        for (int i = 0; i < inputVars.size(); i++) {
            InputVariable var = inputVars.get(i);
            String typeText = typeToStr(var.getType());
            VariableInformation declVarInfo = ctxt.getWriteVarInfo(var);
            String declaration = fmt("%s %s;", declVarInfo.typeInfo.getTargetType(), declVarInfo.targetVariableName);
            List<String> docs = CifDocAnnotationUtils.getDocs(var);

            if (!first) {
                varDefCode.add();
            }
            first = false;

            if (docs.isEmpty()) {
                varDefCode.add("/** Input variable \"%s %s\". */", typeText, declVarInfo.name);
            } else {
                varDefCode.add("/**");
                varDefCode.add(" * Input variable \"%s %s\".", typeText, declVarInfo.name);
                for (String doc: docs) {
                    varDefCode.add(" *");
                    for (String line: doc.split("\\r?\\n")) {
                        varDefCode.add(" * %s", line);
                    }
                }
                varDefCode.add(" */");
            }
            varDefCode.add(declaration);
            reportLine(var, i, inputReport);

            String flagName = fmt("input_loaded%02d", i);
            inputFlagsCode.add("unsigned char %s;", flagName);
            inputClearCode.add("work->%s = FALSE;", flagName);
        }
        CodeBox indentBox = makeCodeBox(1);
        indentBox.add(varDefCode);
        indentBox.add();
        indentBox.add(inputFlagsCode);
        replacements.put("inputvar-definitions", indentBox.toString());
        replacements.put("clear-inputflags-code", inputClearCode.toString());
        writeReport("input-report", inputReport);

        // Generate dimension setting of the input ports.
        CodeBox inputDimsCode = makeCodeBox(1);
        for (int i = 0; i < inputVars.size(); i++) {
            InputVariable decl = inputVars.get(i);
            int rows, columns;
            rows = SimulinkTypeUtils.getRowCount(decl.getType());
            if (rows == 0) {
                rows = 1; // Singular type is a 1-length vector.
                columns = 0;
            } else {
                columns = SimulinkTypeUtils.getColumnCount(decl.getType());
            }

            if (columns > 0) {
                inputDimsCode.add("ssSetInputPortMatrixDimensions(sim_struct, %d, %d, %d);", i, rows, columns);
            } else {
                inputDimsCode.add("ssSetInputPortWidth(sim_struct, %d, %d);", i, rows);
            }
        }
        replacements.put("number-of-inputs", str(inputVars.size()));
        replacements.put("set-input-ports-dimensions", inputDimsCode.toString());

        // Setup sample time.
        String sampleTime = getSimulinkSampleTimeText();
        if (sampleTime == null) {
            throw new UnsupportedException(fmt("Invalid sample time option value '%s' found.", getSampleTime()));
        } else {
            replacements.put("sample-time", sampleTime);
        }

        // Setup sample offset and check against sample time.
        String sampleOffset;
        if (sampleOffsetIsFixed()) {
            sampleOffset = "FIXED_IN_MINOR_STEP_OFFSET";
            if (!offsetMayBeFixed()) {
                String msg = "Fixed sample offset option value is not allowed for the provided sample time.";
                throw new UnsupportedException(msg);
            }
        } else if (sampleOffsetIsZero()) {
            sampleOffset = "0.0";
        } else if (sampleOffsetIsValidReal()) {
            sampleOffset = getSampleOffset();
            if (!offsetMayBeNonzero()) {
                String msg = "Sample offset option may not be a non-zero real value for the provided sample time.";
                throw new UnsupportedException(msg);
            } else {
                // Sample time and sample offset are both real numbers, check that the offset is within the sample time.
                double stime = sampleGetValue();
                double offset = offsetGetValue();
                if (stime <= offset) {
                    String msg = "Sample offset (found value %f) must be less than sample time (found value %f).";
                    throw new UnsupportedException(fmt(msg, offset, stime));
                }
            }
        } else {
            throw new UnsupportedException("sample offset option value not recognized.");
        }
        replacements.put("sample-offset", sampleOffset);

        addOutput(ctxt);
    }

    /**
     * Generate code for Simulink output.
     *
     * @param ctxt The code generation context.
     */
    private void addOutput(CodeContext ctxt) {
        setupVarMaps();

        // Construct dimensions of the output.
        CodeBox outputDimsCode = makeCodeBox(1);
        for (Entry<Declaration, Integer> entry: outputMap.entrySet()) {
            Declaration d = entry.getKey();
            int rows, columns;
            if (d instanceof ContVariable) {
                rows = 1;
                columns = 0;
            } else if (d instanceof AlgVariable) {
                AlgVariable aVar = (AlgVariable)d;
                rows = SimulinkTypeUtils.getRowCount(aVar.getType());
                if (rows == 0) {
                    rows = 1; // Singular type is a 1-length vector.
                    columns = 0;
                } else {
                    columns = SimulinkTypeUtils.getColumnCount(aVar.getType());
                }
            } else {
                Assert.check(d instanceof DiscVariable);
                DiscVariable dVar = (DiscVariable)d;
                rows = SimulinkTypeUtils.getRowCount(dVar.getType());
                if (rows == 0) {
                    rows = 1; // Singular type is a 1-length vector.
                    columns = 0;
                } else {
                    columns = SimulinkTypeUtils.getColumnCount(dVar.getType());
                }
            }

            int idx = entry.getValue();
            if (columns > 0) {
                outputDimsCode.add("ssSetOutputPortMatrixDimensions(sim_struct, %d, %d, %d);", idx, rows, columns);
            } else {
                outputDimsCode.add("ssSetOutputPortWidth(sim_struct, %d, %d);", idx, rows);
            }
        }
        replacements.put("set-output-ports-dimensions", outputDimsCode.toString());

        // mdlOutput: Write variables to output ports.
        CodeBox outputCode = makeCodeBox(1);
        outputCode.add("real_T *y;");
        int index = 0;
        for (Entry<Declaration, Integer> entry: outputMap.entrySet()) {
            if (index > 0) {
                outputCode.add();
            }

            Declaration d = entry.getKey();
            outputCode.add("y = ssGetOutputPortSignal(sim_struct, %d);", entry.getValue());

            VariableInformation varInfo = ctxt.getReadVarInfo(new VariableWrapper(d, false));
            String varAccess = varInfo.targetRef;
            if (normalizeType(varInfo.typeInfo.cifType) instanceof ListType) {
                if (d instanceof AlgVariable) { // Algebraic variables are functions, make a temporary variable.
                    String dest = fmt("tmp%d", index);
                    outputCode.add("%s %s = %s(sim_struct);", varInfo.typeInfo.getTargetType(), dest, varAccess);
                    varAccess = dest;
                }
                outputCode.add("%sTypeToSimulink(y, &%s);", varInfo.typeInfo.getTypeName(), varAccess);
            } else {
                // Singular type.
                if (d instanceof AlgVariable) {
                    varAccess += "(sim_struct)"; // Algebraic variables are functions.
                }
                outputCode.add("*y = %s;", getElementConversionToSimulinkVector(varInfo.typeInfo, varAccess));
            }
            index++;
        }
        replacements.put("write-output", outputCode.toString());
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

            SimulinkFunctionCodeGen funcGen = new SimulinkFunctionCodeGen(true, func);
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
        enumNames.add("static const char *%s[] = {", ENUM_NAMES_LIST);
        enumNames.indent();
        List<EnumLiteral> eLits = enumDecl.getLiterals();
        for (int i = 0; i < eLits.size(); i++) {
            if (i > 0) {
                enumNames.add();
            }

            EnumLiteral lit = eLits.get(i);
            List<String> docs = CifDocAnnotationUtils.getDocs(lit);
            String name = lit.getName();

            if (docs.isEmpty()) {
                enumNames.add("/** Literal \"%s\". */", name);
            } else {
                enumNames.add("/**");
                enumNames.add(" * Literal \"%s\".", name);
                for (String doc: docs) {
                    enumNames.add(" *");
                    for (String line: doc.split("\\r?\\n")) {
                        enumNames.add(" * %s", line);
                    }
                }
                enumNames.add(" */");
            }

            enumNames.add("%s,", stringToJava(name));
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
            code.add("%s %s;", txtVarInfo.typeInfo.getTargetType(), txtVarInfo.targetRef);
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

    @Override
    protected void addSvgDecls(CodeContext ctxt, String cifSpecFileDir) {
        // All CIF/SVG declarations should have been removed from the model.
        Assert.check(svgDecls.isEmpty());
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
                    conds.add(fmt("%s > %s", eventVar, DELAY_EVENT_NAME));
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
                    conds.add(fmt("%s == %s", eventVar, getTargetRef(event)));
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
     * @param ctxt The code generation context.
     * @return {@code null} if no output is ever generated, else the code to generate output.
     */
    private CodeBox genPrint(Expression whenPred, List<PrintFor> fors, Expression txtExpr,
            VariableInformation txtVarInfo, String targetFile, CodeContext ctxt)
    {
        if (txtExpr == null) {
            return null;
        }
        if (!targetFile.equals(":stdout:")) {
            return null; // Simulink only outputs to stdout!
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
                    valueCode.add(fmt("%s %s = %s;", ti.getTargetType(), tempVar.targetRef, dataValue.getData()));
                    valueText = "&" + tempVar.targetRef;
                }
            }
            valueCode.add("%s(%s, %s.data, 0, MAX_STRING_SIZE);", typeGetTypePrintName(ti, true), valueText,
                    txtVarInfo.targetRef);
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
        result.add("ssPrintf(\"%s\\n\", %s.data);", txtVarInfo.targetRef);
        if (!unconditional) {
            result.dedent();
            result.add("}");
        }
        return result;
    }

    @Override
    protected void addEdges(CodeContext ctxt) {
        Assert.check(svgInEdges.isEmpty());

        CodeBox codeGuardFuncs = makeCodeBox(); // Functions computing time-dependent guards.
        CodeBox codeZeroCross = makeCodeBox(1); // Calls in zero-crossings compute code.

        CodeBox codeCallsUncontrollables = makeCodeBox(2); // Calls to try to perform the uncontrollable edges.
        CodeBox codeCallsControllables = makeCodeBox(2); // Calls to try to perform the controllable edges.
        CodeBox codeMethods = makeCodeBox(); // Edge execution functions.

        AtomicInteger numTimeDependentGuards = new AtomicInteger(0);
        int edgeIdx = 0;
        edgeIdx = addEdges(uncontrollableEdges, edgeIdx, codeCallsUncontrollables, codeMethods, codeGuardFuncs,
                codeZeroCross, numTimeDependentGuards, ctxt);
        edgeIdx = addEdges(controllableEdges, edgeIdx, codeCallsControllables, codeMethods, codeGuardFuncs,
                codeZeroCross, numTimeDependentGuards, ctxt);

        replacements.put("number-of-time-dependent-guards", str(numTimeDependentGuards));
        replacements.put("zero-crossings-compute", codeZeroCross.toString());
        replacements.put("guard-functions", codeGuardFuncs.toString());

        if (numTimeDependentGuards.get() == 0) {
            replacements.put("define-mdlZeroCrossings", "#undef MDL_ZERO_CROSSINGS");
        } else {
            replacements.put("define-mdlZeroCrossings", "#define MDL_ZERO_CROSSINGS");
        }

        replacements.put("edge-calls-code-uncontrollables", codeCallsUncontrollables.toString());
        replacements.put("edge-calls-code-controllables", codeCallsControllables.toString());
        replacements.put("edge-methods-code", codeMethods.toString());

        // 'Initial' calls.
        CodeBox code = makeCodeBox(2);
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

        code = makeCodeBox(2);
        code.add("#if PRINT_OUTPUT");
        code.indent();
        code.add("/* post-timestep print. */");
        code.add("PrintOutput(%s, FALSE);", DELAY_EVENT_NAME);
        code.dedent();
        code.add("#endif");
        replacements.put("time-post-print-call", code.toString());
    }

    /**
     * Generate code for the given edges.
     *
     * @param edges The edges for which to generate code.
     * @param edgeIdx The edge index to use for the first edge.
     * @param codeCalls The code storage for calls.
     * @param codeMethods The code storage for methods.
     * @param codeGuardFuncs The code storage for guard functions.
     * @param codeZeroCross The code storage zero-crossing computations.
     * @param numTimeDependentGuards The number of time-dependent guards. Is modified in-place.
     * @param ctxt The code generation context.
     * @return The edge index to use for the next edge, after the edges given to this method.
     */
    private int addEdges(List<Edge> edges, int edgeIdx, CodeBox codeCalls, CodeBox codeMethods, CodeBox codeGuardFuncs,
            CodeBox codeZeroCross, AtomicInteger numTimeDependentGuards, CodeContext ctxt)
    {
        for (int i = 0; i < edges.size(); i++, edgeIdx++) {
            Edge edge = edges.get(i);

            // Get guard. After linearization, there is at most one
            // (linearized) guard. There may not be a guard, due to value
            // simplification. We don't try to detect always 'true' guards,
            // as that is hard to do, in general.
            List<Expression> guards = edge.getGuards();
            Assert.check(guards.size() <= 1);
            Expression guard = guards.isEmpty() ? null : first(guards);

            ExprCode guardCode;
            if (guard == null) {
                guardCode = null;
            } else {
                guardCode = ctxt.exprToTarget(guard, null);
                if (!isTimeConstant(guard, false)) {
                    // Guard to check in zero crossings as well as in event processing, fold it in
                    // a separate function.
                    String guardFuncName = fmt("GuardEval%02d", i);
                    codeZeroCross.add("zcSignals[%d] = %s(sim_struct);", numTimeDependentGuards.get(), guardFuncName);

                    if (!codeGuardFuncs.isEmpty()) {
                        codeGuardFuncs.add();
                    }
                    codeGuardFuncs.add("static BoolType %s(SimStruct *sim_struct) {", guardFuncName);
                    codeGuardFuncs.indent();
                    addPreamble(codeGuardFuncs, false);
                    codeGuardFuncs.add();
                    codeGuardFuncs.add(guardCode.getCode());
                    codeGuardFuncs.add("return %s;", guardCode.getData());
                    codeGuardFuncs.dedent();
                    codeGuardFuncs.add("}");

                    numTimeDependentGuards.incrementAndGet();

                    guardCode = new ExprCode();
                    guardCode.setDataValue(makeComputed(guardFuncName + "(sim_struct)"));
                }
            }

            // Get event.
            Assert.check(edge.getEvents().size() == 1);
            Expression eventRef = first(edge.getEvents()).getEvent();
            Event event = ((EventExpression)eventRef).getEvent();

            String eventName = origDeclNames.get(event);
            Assert.notNull(eventName);
            String eventTargetName = getTargetRef(event);

            // Construct the call to try executing the event.
            codeCalls.add(
                    "if (execEdge%d(sim_struct)) continue; /* (Try to) perform edge with index %d and event \"%s\". */",
                    edgeIdx, edgeIdx, eventName);

            // Add method code.

            // Header.
            List<String> docs = CifDocAnnotationUtils.getDocs(event);
            codeMethods.add();
            codeMethods.add("/**");
            codeMethods.add(" * Execute code for edge with index %d and event \"%s\".", edgeIdx, eventName);
            for (String doc: docs) {
                codeMethods.add(" *");
                for (String line: doc.split("\\r?\\n")) {
                    codeMethods.add(" * %s", line);
                }
            }
            codeMethods.add(" *");
            codeMethods.add(" * @return Whether the edge was performed.");
            codeMethods.add(" */");
            codeMethods.add("static BoolType execEdge%d(SimStruct *sim_struct) {", edgeIdx);
            codeMethods.indent();
            addPreamble(codeMethods, false);
            codeMethods.add();

            // Add edge code.
            if (guardCode != null) {
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
            codeMethods.add();
            if (!edge.getUpdates().isEmpty()) {
                addUpdates(edge.getUpdates(), codeMethods, ctxt);
                codeMethods.add();
            }
            if (!printDecls.isEmpty()) {
                codeMethods.add("#if PRINT_OUTPUT");
                codeMethods.indent();
                codeMethods.add("PrintOutput(%s, FALSE);", eventTargetName);
                codeMethods.dedent();
                codeMethods.add("#endif");
            }
            codeMethods.add("return TRUE;");
            codeMethods.dedent();
            codeMethods.add("}");
        }

        return edgeIdx;
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
    protected void addSpec(CodeContext ctxt) {
        List<String> docs = CifDocAnnotationUtils.getDocs(spec);
        CodeBox specCommentsBox = makeCodeBox(0);
        for (String doc: docs) {
            specCommentsBox.add(" *");
            for (String line: doc.split("\\r?\\n")) {
                specCommentsBox.add(" * %s", line);
            }
        }
        String specCommentsCode = specCommentsBox.toString();
        if (!specCommentsCode.isEmpty()) {
            specCommentsCode += "\n";
        }
        replacements.put("spec-comments", specCommentsCode);
    }

    @Override
    protected Map<String, String> getTemplates() {
        Map<String, String> templates = map();
        templates.put("sfunction.c", ".c");
        templates.put("report.txt", "_report.txt");
        return templates;
    }
}
