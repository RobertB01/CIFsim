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

package org.eclipse.escet.cif.simulator.compiler;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.exprsToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.CONT_SUB_STATE_FIELD_NAME;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.INPUT_SUB_STATE_FIELD_NAME;
import static org.eclipse.escet.cif.simulator.compiler.DefaultValueCodeGenerator.getDefaultValueCode;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodePreds;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.truncate;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.StateInitOrderer;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.simulator.options.CifSpecInitOption;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** State and sub-state initialization code generator. */
public class StateInitCodeGenerator {
    /** Constructor for the {@link StateInitCodeGenerator} class. */
    private StateInitCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the initialization of the runtime state of the specification.
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeStateInit(CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("StateInit");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Runtime state initializer. */");
        h.add("public final class StateInit extends RuntimeStateInit {");

        // Add body.
        CodeBox c = file.body;

        // Generate code for methods.
        gencodeInitState(c, ctxt);
        gencodeInitOpt(c, ctxt);
    }

    /**
     * Generate Java code for the 'initState' method.
     *
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeInitState(CodeBox c, CifCompilerContext ctxt) {
        // Get objects to initialize. This includes the state variables (discrete, input and continuous, except for
        // variable 'time'), and the automata.
        List<Declaration> variables = ctxt.getStateVars();
        List<Automaton> automata = ctxt.getAutomata();
        List<PositionObject> objs = concat(variables, automata);

        // Order state variables and automata by their initialization
        // interdependencies. Also gives us the potential initial locations.
        objs = new StateInitOrderer().computeOrder(objs, false);
        Assert.notNull(objs);

        // Add 'initState' method.
        c.add();
        c.add("public void initState(RuntimeSpec<?> spec, State state) {");
        c.indent();

        // Make initialization values provided by the option available.
        c.add("processInitOption(spec, state);");

        // Initialize 'time'.
        c.add("state.%s.time = 0.0;", CONT_SUB_STATE_FIELD_NAME);

        // Initialize automata to '-1' indicating unknown initial location.
        // It won't match any location index, so it can be used in evaluation
        // to compare to location indices. For automata references, the orderer
        // will ensure that the automaton is handled first, before the
        // automaton reference, and we thus never have '-1' any more.
        c.add();
        for (Automaton aut: automata) {
            c.add("state.%s.%s = -1;", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
        }

        // Add sub method calls.
        c.add();
        int maxObjCount = 100;
        int subMethodCount = (int)Math.ceil(objs.size() / (double)maxObjCount);
        for (int i = 0; i < subMethodCount; i++) {
            c.add("initState%d(state, optionVarValues, optionLocIndices);", i);
        }

        // Close 'initState' method.
        c.dedent();
        c.add("}");

        // Generate code for sub-methods.
        c.add();
        c.add("private static void initState0(State state, Object[] optValues, Integer[] optLocIndices) {");
        c.indent();
        c.add("boolean b;");
        List<ExprCodeGeneratorResult> exprResults = list();
        for (int i = 0; i < objs.size(); i++) {
            // New sub method.
            if ((i > 0) && (i % maxObjCount == 0)) {
                c.dedent();
                c.add("}");

                c.add();
                c.add("private static void initState%d(State state, Object[] optValues, Integer[] optLocIndices) {",
                        i / maxObjCount);
                c.indent();
                c.add("boolean b;");
            }

            // New object initialization.
            c.add();
            PositionObject obj = objs.get(i);
            if (obj instanceof DiscVariable) {
                // Generate code to initialize the variable.
                exprResults.addAll(gencodeInitDiscVar(variables, (DiscVariable)obj, c, ctxt));
            } else if (obj instanceof InputVariable) {
                // Generate code to initialize the variable.
                gencodeInitInputVar(variables, (InputVariable)obj, c, ctxt);
            } else if (obj instanceof ContVariable) {
                // Generate code to initialize the variable.
                exprResults.add(gencodeInitContVar((ContVariable)obj, c, ctxt));
            } else if (obj instanceof Location) {
                // Generate code to initialize the automaton. If this location
                // is an initial location, the initial location of the
                // automaton is set to the index of this location, and the
                // automaton and location are initialized. In case of multiple
                // initial locations, the automaton is already initialized,
                // and this can be report here, as unsupported.
                Location loc = (Location)obj;
                exprResults.add(gencodeInitLoc(automata, loc, c, ctxt));
            } else if (obj instanceof Automaton) {
                // Generate code to check for initialization of the automaton.
                // If not initialized, initialization has failed.
                gencodeInitAut((Automaton)obj, c, ctxt);
            } else {
                throw new RuntimeException("Unexpected obj: " + obj);
            }
        }
        c.dedent();
        c.add("}");

        // Add potential extra expression evaluation methods.
        for (ExprCodeGeneratorResult exprResult: exprResults) {
            exprResult.addExtraMethods(c);
        }
    }

    /**
     * Generate Java code for the initialization of the runtime state of the given discrete variable.
     *
     * @param vars The state variables.
     * @param var The discrete variable.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     * @return The {@code ExprCodeGeneratorResult}s for the generated Java code.
     */
    private static List<ExprCodeGeneratorResult> gencodeInitDiscVar(List<Declaration> vars, DiscVariable var, CodeBox c,
            CifCompilerContext ctxt)
    {
        // Get potential initial values ('null' for default initial value).
        List<Expression> initValues;
        VariableValue values = var.getValue();
        if (values == null) {
            initValues = list((Expression)null);
        } else {
            initValues = values.getValues();
        }

        // Add initialization code, in local scope.
        c.add("{");
        c.indent();

        int varIdx = vars.indexOf(var);
        Assert.check(varIdx >= 0);
        c.add("Object optValue = optValues[%d];", varIdx);
        String typeCode = gencodeType(var.getType(), ctxt);
        c.add("%s v;", typeCode);
        c.add("b = false;"); // No initial value set yet.

        // Generate code for each of the potential initial values.
        String subStateName = ctxt.getAutSubStateFieldName((Automaton)var.eContainer());
        String varName = ctxt.getDiscVarFieldName(var);
        String fieldTxt = fmt("state.%s.%s", subStateName, varName);
        List<ExprCodeGeneratorResult> exprResults = list();
        for (Expression initValue: initValues) {
            // Evaluate initial value.
            if (initValue == null) {
                // Default initial value.
                ExprCodeGeneratorResult result = getDefaultValueCode(var.getType(), ctxt);
                c.add("v = %s;", result);
                exprResults.add(result);
            } else {
                // User-specified initial value. For the wrapped exception, we
                // don't provide the state, as during initialization the state
                // may be incomplete.
                c.add("try {");
                c.indent();

                ExprCodeGeneratorResult result = gencodeExpr(initValue, ctxt, "state");
                c.add("v = %s;", result);
                exprResults.add(result);

                c.dedent();
                c.add("} catch (CifSimulatorException e) {");
                c.indent();
                c.add("throw new CifSimulatorException(\"Evaluation of initial value \\\"%s\\\" of discrete variable "
                        + "\\\"%s\\\" failed.\", e);", escapeJava(truncate(exprToStr(initValue), 1000)),
                        getAbsName(var));
                c.dedent();
                c.add("}");
            }

            // Check and set initial value of variable.
            c.add("if (optValue == null || optValue.equals(v)) {");
            c.indent();

            c.add("if (!b) {");
            c.indent();

            c.add("%s = v;", fieldTxt);
            c.add("b = true;"); // Initial value set.

            c.dedent();
            c.add("} else {");
            c.indent();

            c.add("throw new UnsupportedException(\"Discrete variable \\\"%s\\\" has multiple possible initial values, "
                    + "which is currently not supported by the CIF simulator. "
                    + "Restrict the initialization using the appropriate simulation option.\");", getAbsName(var));

            c.dedent();
            c.add("}");
            c.dedent();
            c.add("}");
        }

        // If uninitialized, initialize with option value if 'any' value.
        boolean any = values != null && values.getValues().isEmpty();
        if (any) {
            c.add("if (optValue != null) {");
            c.indent();
            c.add("%s = (%s)optValue;", fieldTxt, typeCode);
            c.dedent();
            c.add("} else {");
            c.indent();
            c.add("warn(\"Discrete variable \\\"%s\\\" is not initialized.\");", getAbsName(var));
            c.add("throw new SimulatorExitException(SimulationResult.INIT_FAILED);");
            c.dedent();
            c.add("}");
        }

        // Check for conflicting initialization problem, if not 'any'.
        if (!any) {
            c.add("if (!b) {");
            c.indent();
            c.add("warn(\"Discrete variable \\\"%s\\\" could not be initialized, due to conflicting initial values "
                    + "being provided by the declaration of the variable and the simulation option.\");",
                    getAbsName(var));
            c.add("throw new SimulatorExitException(SimulationResult.INIT_FAILED);");
            c.dedent();
            c.add("}");
        }

        // Close local scope.
        c.dedent();
        c.add("}");

        return exprResults;
    }

    /**
     * Generate Java code for the initialization of the runtime state of the given input variable.
     *
     * @param vars The state variables.
     * @param var The input variable.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeInitInputVar(List<Declaration> vars, InputVariable var, CodeBox c,
            CifCompilerContext ctxt)
    {
        // Add initialization code, in local scope.
        c.add("{");
        c.indent();

        int varIdx = vars.indexOf(var);
        Assert.check(varIdx >= 0);
        c.add("Object optValue = optValues[%d];", varIdx);
        String typeCode = gencodeType(var.getType(), ctxt);

        // Get sub-state name.
        String subStateName = INPUT_SUB_STATE_FIELD_NAME;

        // Generate code for the initial value.
        String varName = ctxt.getInputVarFieldName(var);
        String fieldTxt = fmt("state.%s.%s", subStateName, varName);

        // The initial value must be provided via the simulation option.
        c.add("if (optValue != null) {");
        c.indent();
        c.add("%s = (%s)optValue;", fieldTxt, typeCode);
        c.dedent();
        c.add("} else {");
        c.indent();
        c.add("warn(\"No initial value provided for input variable \\\"%s\\\", via the simulation option.\");",
                getAbsName(var));
        c.add("throw new SimulatorExitException(SimulationResult.INIT_FAILED);");
        c.dedent();
        c.add("}");

        // Close local scope.
        c.dedent();
        c.add("}");
    }

    /**
     * Generate Java code for the initialization of the runtime state of the given continuous variable.
     *
     * @param var The continuous variable.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     * @return The {@code ExprCodeGeneratorResult} for the generated Java code.
     */
    private static ExprCodeGeneratorResult gencodeInitContVar(ContVariable var, CodeBox c, CifCompilerContext ctxt) {
        // Get sub-state name.
        String subStateName;
        if (var.eContainer() instanceof Automaton) {
            subStateName = ctxt.getAutSubStateFieldName((Automaton)var.eContainer());
        } else {
            subStateName = CONT_SUB_STATE_FIELD_NAME;
        }

        // Add initialization assignment.
        String varName = ctxt.getContVarFieldName(var);
        Expression valueExpr = var.getValue();
        if (valueExpr == null) {
            // Default initial value.
            c.add("state.%s.%s = 0.0;", subStateName, varName);
            return new ExprCodeGeneratorResult("0.0", newRealType());
        } else {
            // User-specified initial value. For the wrapped exception, we
            // don't provide the state, as during initialization the state
            // may be incomplete.
            c.add("try {");
            c.indent();

            ExprCodeGeneratorResult result = gencodeExpr(valueExpr, ctxt, "state");
            c.add("state.%s.%s = %s;", subStateName, varName, result);

            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of initial value \\\"%s\\\" of continuous variable "
                    + "\\\"%s\\\" failed.\", e);", escapeJava(truncate(exprToStr(valueExpr), 1000)), getAbsName(var));
            c.dedent();
            c.add("}");
            return result;
        }
    }

    /**
     * Generate Java code for the initialization of the runtime state of an automaton, for the given location.
     *
     * @param automata The automata.
     * @param loc The location.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     * @return The {@code ExprCodeGeneratorResult} for the generated Java code.
     */
    private static ExprCodeGeneratorResult gencodeInitLoc(List<Automaton> automata, Location loc, CodeBox c,
            CifCompilerContext ctxt)
    {
        // Evaluate initialization predicates. For the wrapped exception, we
        // don't provide the state, as during initialization the state may be
        // incomplete.
        List<Expression> initials = loc.getInitials();
        c.add("try {");
        c.indent();
        ExprCodeGeneratorResult exprResult = gencodePreds(initials, ctxt, "state", "false");
        c.add("b = %s;", exprResult);
        c.dedent();
        c.add("} catch (CifSimulatorException e) {");
        c.indent();
        c.add("throw new CifSimulatorException(\"Evaluation of initialization predicates \\\"%s\\\" of %s failed.\", "
                + "e);", escapeJava(truncate(exprsToStr(initials), 1000)),
                escapeJava(CifTextUtils.getLocationText2(loc)));
        c.dedent();
        c.add("}");

        // Check and set initial location of automaton.
        Automaton aut = CifLocationUtils.getAutomaton(loc);
        int autIdx = automata.indexOf(aut);
        int locIdx = aut.getLocations().indexOf(loc);
        Assert.check(autIdx >= 0);
        Assert.check(locIdx >= 0);
        c.add("if (b && (optLocIndices[%d] == null || optLocIndices[%d].equals(%d))) {", autIdx, autIdx, locIdx);
        c.indent();

        String fieldTxt = fmt("state.%s.%s", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
        c.add("if (%s == -1) {", fieldTxt);
        c.indent();

        c.add("%s = %d;", fieldTxt, locIdx);

        c.dedent();
        c.add("} else {");
        c.indent();

        c.add("throw new UnsupportedException(\"Automaton \\\"%s\\\" has multiple possible initial locations, which "
                + "is currently not supported by the CIF simulator. Restrict the initialization using the appropriate "
                + "simulation option.\");", getAbsName(aut));

        c.dedent();
        c.add("}");
        c.dedent();
        c.add("}");

        return exprResult;
    }

    /**
     * Generate Java code for the initialization of the runtime state of the given automaton. The automaton is assumed
     * to have already been initialized by the code generated for the potential initial locations. What remains is to
     * check if it has been initialized.
     *
     * @param aut The automaton.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeInitAut(Automaton aut, CodeBox c, CifCompilerContext ctxt) {
        // Detect uninitialized automaton.
        c.add("if (state.%s.%s == -1) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
        c.indent();

        // Get automaton index.
        int autIdx = ctxt.getAutomata().indexOf(aut);
        Assert.check(autIdx >= 0);

        // Warn about option value for non-initial location.
        c.add("if (optLocIndices[%d] != null) {", autIdx);
        c.indent();
        c.add("warn(\"Automaton \\\"%s\\\" could not be initialized, as the location provided by the simulation "
                + "option is not an initial location.\");", getAbsName(aut));
        c.dedent();

        // Warn about no initial location.
        c.add("} else {");
        c.indent();
        c.add("warn(\"Automaton \\\"%s\\\" is not initialized.\");", getAbsName(aut));
        c.dedent();
        c.add("}");

        // No initial location does not implicate an invalid model. It just
        // means that the initial state does not exist, and thus we have an
        // initialization failure, similar to an initialization predicate
        // evaluating to false in the initial state.
        c.add("throw new SimulatorExitException(SimulationResult.INIT_FAILED);");
        c.dedent();
        c.add("}");
    }

    /**
     * Generate Java code for reading values of discrete and input variables provided by the {@link CifSpecInitOption}.
     *
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeInitOpt(CodeBox c, CifCompilerContext ctxt) {
        // Get state variables (excluding variable 'time').
        List<Declaration> stateVars = ctxt.getStateVars();

        // Add 'processVarValue' method.
        c.add();
        c.add("@Override");
        c.add("protected Object processVarValue(RuntimeStateObjectMeta obj,");
        c.add("                                 String valueTxt)");
        c.add("{");
        c.indent();
        c.add("switch (obj.idx) {");
        c.indent();

        for (int i = 0; i < stateVars.size(); i++) {
            // Skip continuous variables.
            Declaration decl = stateVars.get(i);
            if (decl instanceof ContVariable) {
                continue;
            }

            // Must be a discrete variable or an input variable.
            CifType type;
            String objectType;
            if (decl instanceof DiscVariable) {
                DiscVariable var = (DiscVariable)decl;
                type = var.getType();
                objectType = "discrete";
            } else {
                InputVariable var = (InputVariable)decl;
                type = var.getType();
                objectType = "input";
            }

            // Add case.
            c.add("case %d: {", i);
            c.indent();

            // Handle (non-)serializable data types.
            if (!LiteralCodeGenerator.isSerializableType(type)) {
                String typeTxt = CifTextUtils.typeToStr(type);
                c.add("String msg = \"Specifying initialization using the simulation option is not supported for "
                        + "%s variable \\\"%s\\\", as its type \\\"%s\\\" is not serializable.\";", objectType,
                        CifTextUtils.getAbsName(decl), StringEscapeUtils.escapeJava(typeTxt));
                c.add("throw new UnsupportedException(msg);");
            } else {
                c.add("try {");
                c.indent();
                String readName = ctxt.getLiteralReadMethodName(type);
                c.add("return %s(valueTxt);", readName);
                c.dedent();
                c.add("} catch (InputOutputException ex) {");
                c.indent();
                c.add("String msg = \"Failed to read value of type \\\"%s\\\".\";", CifTextUtils.typeToStr(type));
                c.add("throw new InputOutputException(msg, ex);");
                c.dedent();
                c.add("}");
            }

            // Done with this variable.
            c.dedent();
            c.add("}");
        }

        c.add("default:");
        c.indent();
        c.add("throw new RuntimeException(\"Invalid state var idx: \" + obj.idx);");
        c.dedent();

        c.dedent();
        c.add("}");
        c.dedent();
        c.add("}");
    }
}
