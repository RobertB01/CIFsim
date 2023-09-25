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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.INT_TYPE;

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.model.PlcModelUtils;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouInstance;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcResource;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTask;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTypeDecl;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcDerivedType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Class that stores and writes generated PLC code. */
public class PlcCodeStorage {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Project with PLC code. */
    private final PlcProject project;

    /** PLC resource for the program. */
    private final PlcResource resource;

    /** Task running the PLC program. */
    private final PlcTask task;

    /** Global variable list for constants, lazily created. */
    private PlcGlobalVarList globalConstants = null;

    /** Global variable list for input variables, lazily created. */
    private PlcGlobalVarList globalInputs = null;

    /** Global variable list for output variables, lazily created. */
    private PlcGlobalVarList globalOutputs = null;

    /** Global variable list for state variables, lazily created. */
    private PlcGlobalVarList globalStateVars = null;

    /** The expression generator to use for generating code in the main program. Initialized lazily. */
    private ExprGenerator exprGenerator = null;

    /** The variable that tracks progress is made in performing events. Initialized lazily. */
    private PlcVariable isProgressVariable = null;

    /** If not {@code null}, code for initializing the state variables. */
    private List<PlcStatement> stateInitializationCode = null;

    /** If not {@code null}, code to perform one iteration of all events. */
    private List<PlcStatement> eventTransitionsIterationCode = null;

    /** If not {@code null}, code for copying I/O input to CIF state. */
    private List<PlcStatement> inputFuncCode = null;

    /** If not {@code null}, code for copying CIF state to I/O output. */
    private List<PlcStatement> outputFuncCode = null;

    /**
     * Constructor of the {@link PlcCodeStorage} class.
     *
     * @param target PLC target to generate code for.
     * @param settings Configuration to use.
     */
    public PlcCodeStorage(PlcTarget target, PlcGenSettings settings) {
        this.target = target;

        // Create project, configuration, resource, and task.
        project = new PlcProject(settings.projectName);
        PlcConfiguration config = new PlcConfiguration(settings.configurationName);
        project.configurations.add(config);

        resource = new PlcResource(settings.resourceName);
        config.resources.add(resource);

        task = new PlcTask(settings.taskName, settings.taskCycleTime, settings.taskPriority);
        resource.tasks.add(task);
    }

    /**
     * Get the expression generator to use for generating code in the main program.
     *
     * @return The expression generator to use for generating code in the main program.
     */
    public ExprGenerator getExprGenerator() {
        if (exprGenerator == null) {
            exprGenerator = new ExprGenerator(target, target.getVarStorage().getRootCifDataProvider());
        }
        return exprGenerator;
    }

    /**
     * Get the variable to set if an event transition is performed.
     *
     * @return The variable to set if an event transition is performed.
     */
    public PlcVariable getIsProgressVariable() {
        if (isProgressVariable == null) {
            isProgressVariable = getExprGenerator().getTempVariable("isProgress", PlcElementaryType.BOOL_TYPE);
        }
        return isProgressVariable;
    }

    /**
     * Add a variable to the global constants table.
     *
     * @param plcVar Variable to add. Name is assumed to be unique.
     */
    public void addConstant(PlcVariable plcVar) {
        Assert.check(target.supportsConstants());

        if (globalConstants == null) {
            globalConstants = new PlcGlobalVarList("CONSTANTS", true);
        }
        globalConstants.variables.add(plcVar);
    }

    /**
     * Add a variable to the global input variable table.
     *
     * @param variable Variable to add. Name is assumed to be unique.
     */
    public void addInputVariable(PlcVariable variable) {
        if (globalInputs == null) {
            globalInputs = new PlcGlobalVarList("INPUTS", false);
        }
        globalInputs.variables.add(variable);
    }

    /**
     * Add a variable to the global output variable table.
     *
     * @param variable Variable to add. Name is assumed to be unique.
     */
    public void addOutputVariable(PlcVariable variable) {
        if (globalOutputs == null) {
            globalOutputs = new PlcGlobalVarList("OUTPUTS", false);
        }
        globalOutputs.variables.add(variable);
    }

    /**
     * Add a variable to the global state variable table.
     *
     * @param variable Variable to add. Name is assumed to be unique.
     */
    public void addStateVariable(PlcVariable variable) {
        if (globalStateVars == null) {
            globalStateVars = new PlcGlobalVarList("STATE", false);
        }
        globalStateVars.variables.add(variable);
    }

    /**
     * Add a type declaration to the global type declarations list.
     *
     * @param decl Declaration to add. Name is assumed to be unique.
     */
    public void addTypeDecl(PlcTypeDecl decl) {
        project.typeDecls.add(decl);
    }

    /**
     * Add code to initialize the state of the globally used variables.
     *
     * @param stateInitializationCode Code for initializing the globally used variables.
     */
    public void addStateInitialization(List<PlcStatement> stateInitializationCode) {
        Assert.check(this.stateInitializationCode == null);
        if (PlcModelUtils.isNonEmptyCode(stateInitializationCode)) {
            this.stateInitializationCode = stateInitializationCode;
        }
    }

    /**
     * Add code to (try to) perform one iteration of all event transitions. Code should update the
     * {@link #isProgressVariable} if an event is performed.
     *
     * @param eventTransitionsIterationCode Code that (tries to) perform one iteration of all event transitions. Code
     *     should update the {@link #isProgressVariable} if an event is performed.
     * @see #getIsProgressVariable
     */
    public void addEventTransitions(List<PlcStatement> eventTransitionsIterationCode) {
        Assert.check(this.eventTransitionsIterationCode == null);
        if (PlcModelUtils.isNonEmptyCode(eventTransitionsIterationCode)) {
            this.eventTransitionsIterationCode = eventTransitionsIterationCode;
        }
    }

    /**
     * Add code for copying input I/O to CIF state.
     *
     * @param inputFuncCode Code of the input function.
     */
    public void addInputFuncCode(List<PlcStatement> inputFuncCode) {
        Assert.check(this.inputFuncCode == null);
        if (PlcModelUtils.isNonEmptyCode(inputFuncCode)) {
            this.inputFuncCode = inputFuncCode;
        }
    }

    /**
     * Add code for copying CIF state to output I/O.
     *
     * @param outputFuncCode Code of the output function.
     */
    public void addOutputFuncCode(List<PlcStatement> outputFuncCode) {
        Assert.check(this.outputFuncCode == null);
        if (PlcModelUtils.isNonEmptyCode(outputFuncCode)) {
            this.outputFuncCode = outputFuncCode;
        }
    }

    /** Perform any additional processing to make the generated PLC program ready. */
    public void finishPlcProgram() {
        // Add all created variable tables.
        addGlobalVariableTable(globalConstants);
        addGlobalVariableTable(globalInputs);
        addGlobalVariableTable(globalOutputs);
        addGlobalVariableTable(globalStateVars);

        // Create code file for program, with header etc.
        PlcPou main = new PlcPou("MAIN", PlcPouType.PROGRAM, null);
        project.pous.add(main);

        // Global variable list of the main program. Note that the Siemens target currently requires the "TIMERS" name.
        PlcGlobalVarList mainVariables = new PlcGlobalVarList("TIMERS", false);
        addGlobalVariableTable(mainVariables);

        // Prepare adding code to the program.
        CodeBox box = main.body;
        boolean boxNeedsEmptyLine = false;
        int commentLength = 79; // Length of a comment header line.

        ExprGenerator exprGen = getExprGenerator();
        ModelTextGenerator textGenerator = target.getModelTextGenerator();

        // Add input code if it exists.
        if (inputFuncCode != null) {
            generateCommentHeader("Read input from sensors.", '-', commentLength, boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            textGenerator.toText(inputFuncCode, box, main.name, false);
        }

        // Add initialization code if it exists.
        if (stateInitializationCode != null) {
            generateCommentHeader("Initialize state.", '-', commentLength, boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            // Insert code to create the initial state.
            PlcVariable firstFlag = exprGen.makeLocalVariable("firstRun", PlcElementaryType.BOOL_TYPE, null,
                    new PlcBoolLiteral(true));
            mainVariables.variables.add(firstFlag);

            box.add("IF %s THEN", firstFlag.name);
            box.indent();
            box.add("%s := FALSE;", firstFlag.name);
            box.add();
            textGenerator.toText(stateInitializationCode, box, main.name, false);
            box.dedent();
            box.add("END_IF;");
        }

        // Add event transitions code.
        if (eventTransitionsIterationCode != null) {
            generateCommentHeader("Process all events.", '-', commentLength, boxNeedsEmptyLine, box);

            String progressVarName = getIsProgressVariable().name;
            box.add("%s := TRUE;", progressVarName);
            box.add("WHILE %s DO", progressVarName);
            box.indent();
            box.add("%s := FALSE;", progressVarName);
            box.add();
            target.getModelTextGenerator().toText(eventTransitionsIterationCode, box, main.name, false);
            box.dedent();
            box.add("END_WHILE;");
        }
        boxNeedsEmptyLine = true;

        // Generate output code if it exists. */
        if (outputFuncCode != null) {
            generateCommentHeader("Write output to actuators.", '-', commentLength, boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            textGenerator.toText(outputFuncCode, box, main.name, false);
        }

        exprGen.releaseTempVariable(isProgressVariable); // isProgress variable is no longer needed.

        // Add main program timer variables.
        PlcType tonType = new PlcDerivedType("TON");
        mainVariables.variables.add(exprGen.makeLocalVariable("timer0", tonType));
        mainVariables.variables.add(exprGen.makeLocalVariable("timer1", tonType));
        mainVariables.variables.add(exprGen.makeLocalVariable("curTimer", INT_TYPE, null, new PlcIntLiteral(0)));

        // Add temporary variables of the main program code.
        main.tempVars = exprGen.getCreatedTempVariables();

        // Add program to task.
        task.pouInstances.add(new PlcPouInstance("MAIN", main));
    }

    /**
     * Generate a comment header line possibly after an empty line.
     *
     * @param text Text describing what code lines will come next.
     * @param dashChar The character to use as filler, {@code '-'} or {@code '='} are likely useful.
     * @param length Expected length of comment line, may get longer if the text is long.
     * @param addEmptyLine Whether to generate an empty line first.
     * @param box Storage for the generated lines.
     */
    private void generateCommentHeader(String text, char dashChar, int length, boolean addEmptyLine, CodeBox box) {
        // Construct header line. As it is mostly constant data, code will be much more efficient than it seems.
        char[] pre = {'(', '*', ' ', dashChar, dashChar, dashChar, ' '};
        char[] post = {dashChar, dashChar, dashChar, ' ', '*', ')'};
        char[] afterText = {' '};

        int layoutLength = pre.length + afterText.length + post.length;
        length = Math.max(length, layoutLength + text.length());

        char[] line = new char[length];
        Arrays.fill(line, dashChar);
        System.arraycopy(pre, 0, line, 0, pre.length);
        System.arraycopy(text.toCharArray(), 0, line, pre.length, text.length());
        System.arraycopy(afterText, 0, line, pre.length + text.length(), afterText.length);
        System.arraycopy(post, 0, line, line.length - post.length, post.length);

        if (addEmptyLine) {
            box.add();
        }
        box.add(new String(line));
    }

    /**
     * Add the given variable table to the PLC code if the table exists.
     *
     * @param varTable Variable table to add if it exists (is non-{@code null}).
     */
    private void addGlobalVariableTable(PlcGlobalVarList varTable) {
        if (varTable != null) {
            resource.globalVarLists.add(varTable);
        }
    }

    /**
     * Write the project to the output.
     *
     * @note Depending on the actual write implementation a single file or a directory may be written.
     */
    public void writeOutput() {
        target.writeOutput(project);
    }
}
