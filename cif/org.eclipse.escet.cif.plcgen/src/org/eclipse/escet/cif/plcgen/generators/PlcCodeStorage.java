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

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
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
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Class that stores and writes generated PLC code. */
public class PlcCodeStorage {
    /** Maximum number of registered premature event loop cycle aborts. */
    private static final int MAX_LOOPS_KILLED = 9999;

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

    /** Global variable list for timer variables, lazily created. */
    private PlcGlobalVarList globalTimerVars = null;

    /** The expression generator to use for generating code in the main program. Initialized lazily. */
    private ExprGenerator exprGenerator = null;

    /** The variable that tracks progress is made in performing events. Initialized lazily. */
    private PlcVariable isProgressVariable = null;

    /** If not {@code null}, code for initializing the state variables. */
    private List<PlcStatement> stateInitializationCode = null;

    /**
     * If not {@code null}, code to update the remaining time of the continuous variables just before the non-first time
     * of the event transitions.
     */
    private List<PlcStatement> updateContVarsRemainingTimeCode = null;

    /** If not {@code null}, code to perform one iteration of all events. */
    private List<PlcStatement> eventTransitionsIterationCode = null;

    /** Maximum number of iterations for performing events in a single cycle, or {@code null} if unrestricted. */
    private Integer maxIter;

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
        this.maxIter = settings.maxIter;

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
            exprGenerator = new ExprGenerator(target, target.getVarStorage().getCifDataProvider());
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
     * Add a variable to the global timer variables table.
     *
     * @param variable Variable to add. Name is assumed to be unique.
     */
    public void addTimerVariable(PlcVariable variable) {
        if (globalTimerVars == null) {
            // Global variable list of timer-related data of the main program. Note that the Siemens target currently
            // requires the "TIMERS" name.
            globalTimerVars = new PlcGlobalVarList("TIMERS", false);
        }
        globalTimerVars.variables.add(variable);
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
     * Store code that updates the remaining time of the continuous variables before the event transitions in the PLC
     * cycle.
     *
     * @param updateContVarsRemainingTimeCode Statements to execute for updating remaining time of the continuous
     *     variables.
     */
    public void storeUpdateContvarsRemainingTimeCode(List<PlcStatement> updateContVarsRemainingTimeCode) {
        this.updateContVarsRemainingTimeCode = updateContVarsRemainingTimeCode;
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
    @SuppressWarnings("null")
    public void finishPlcProgram() {
        // Continuous variables are state so they are also initialized. Therefore needing to update the
        // remaining time of continuous variables means there is also state initialization code. That is, having
        // continuous variables is a subset within having state.
        Assert.implies(updateContVarsRemainingTimeCode != null, stateInitializationCode != null);

        PlcFunctionAppls funcAppls = new PlcFunctionAppls(target);
        ExprGenerator exprGen = getExprGenerator();
        ModelTextGenerator textGenerator = target.getModelTextGenerator();

        // The "firstRun" boolean is needed in state initialization, but creation has been moved to here before
        // pushing the variable tables to the output.
        PlcVariable firstRun = null;
        if (stateInitializationCode != null) {
            firstRun = exprGen.makeLocalVariable("firstRun", PlcElementaryType.BOOL_TYPE, null,
                    new PlcBoolLiteral(true));
            addTimerVariable(firstRun);
        }

        // Construct loop and killed counters.
        PlcVariable loopCount = null;
        PlcVariable loopsKilled = null;
        if (maxIter != null) {
            // Construct a "loopsKilled" variable, ensure the maximum value fits in the type.
            loopsKilled = exprGen.makeLocalVariable("loopsKilled", PlcElementaryType.INT_TYPE);
            Assert.check(MAX_LOOPS_KILLED + 1 <= 0x7FFF); // One more for "min(killed + 1, max_value)".

            // Construct a "loopCount" variable, limit the maximum number of iterations to the type.
            PlcElementaryType loopCountType = target.getIntegerType();
            loopCount = exprGen.makeLocalVariable("loopCount", loopCountType);
            int bitSize = PlcElementaryType.getSizeOfIntType(loopCountType);
            maxIter = switch (bitSize) {
                case 64, 32 -> maxIter; // Java int size is 32 bit, all values of maxIter fit.
                case 16 -> Math.min(maxIter, 0x7FFF);
                case 8 -> Math.min(maxIter, 0x7F);
                default -> throw new AssertionError("Unexpected loopCount bit-size " + bitSize + " found.");
            };

            addTimerVariable(loopCount);
            addTimerVariable(loopsKilled);
        }

        // Add all created variable tables.
        addGlobalVariableTable(globalConstants);
        addGlobalVariableTable(globalInputs);
        addGlobalVariableTable(globalOutputs);
        addGlobalVariableTable(globalStateVars);
        addGlobalVariableTable(globalTimerVars);

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

        // Add input code if it exists.
        if (inputFuncCode != null) {
            generateCommentHeader("Read input from sensors.", '-', commentLength, boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            textGenerator.toText(inputFuncCode, box, main.name, false);
        }

        // Add initialization code if it exists.
        if (stateInitializationCode != null) {
            String headerText = (updateContVarsRemainingTimeCode == null) ? "Initialize state."
                    : "Initialize state or update continuous variables.";
            generateCommentHeader(headerText, '-', commentLength, boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            // Insert code to create the initial state with the "firstRun" boolean to run it only once.
            // The variable is added above, before the variable tables are pushed to the output.
            //
            box.add("IF %s THEN", firstRun.name);
            box.indent();
            box.add("%s := FALSE;", firstRun.name);
            if (loopsKilled != null) {
                box.add("%s := 0;", loopsKilled.name);
            }
            box.add();
            textGenerator.toText(stateInitializationCode, box, main.name, false);
            box.dedent();
            if (updateContVarsRemainingTimeCode != null) {
                box.add("ELSE");
                box.indent();
                textGenerator.toText(updateContVarsRemainingTimeCode, box, main.name, false);
                box.dedent();
            }
            box.add("END_IF;");
        }

        // Add event transitions code.
        if (eventTransitionsIterationCode != null) {
            generateCommentHeader("Process all events.", '-', commentLength, boxNeedsEmptyLine, box);

            PlcVariable progressVar = getIsProgressVariable();
            box.add("%s := TRUE;", progressVar.name);

            // Start the event processing loop.
            if (loopCount == null) {
                // Unrestricted looping, no need to count loops.
                box.add("(* Perform events until none can be done anymore. *)");
                box.add("WHILE %s DO", progressVar.name);
                box.indent();
            } else {
                // Generate condition "progress AND loopCount < max".
                PlcExpression progressCond = new PlcVarExpression(progressVar);
                PlcExpression maxIterCond = funcAppls.lessThanFuncAppl(new PlcVarExpression(loopCount),
                        new PlcIntLiteral(maxIter));
                PlcExpression whileCond = funcAppls.andFuncAppl(progressCond, maxIterCond);

                // Restricted looping code.
                box.add("(* Perform events until none can be done anymore. *)");
                box.add("(* Track the number of iterations and abort if there are too many. *)");
                box.add("%s := 0;", loopCount.name);
                box.add("WHILE %s DO", textGenerator.toString(whileCond));
                box.indent();
                box.add("%s := %s + 1;", loopCount.name, loopCount.name);
            }

            // Construct the while body with event processing.
            box.add("%s := FALSE;", progressVar.name);
            box.add();
            textGenerator.toText(eventTransitionsIterationCode, box, main.name, false);
            box.dedent();
            box.add("END_WHILE;");

            // Update "loopsKilled" afterwards if appropriate.
            if (loopsKilled != null) {
                // IF loopCount >= MAX_ITER THEN loopsKilled := MIN(loopsKilled + 1, MAX_LOOPS_KILLED); END_IF;
                PlcExpression reachedMaxLoopCond = funcAppls.greaterEqualFuncAppl(new PlcVarExpression(loopCount),
                        new PlcIntLiteral(maxIter));
                PlcExpression incKilled = funcAppls.addFuncAppl(new PlcVarExpression(loopsKilled),
                        new PlcIntLiteral(1));
                PlcExpression limitedIncrementKilled = funcAppls.minFuncAppl(incKilled,
                        new PlcIntLiteral(MAX_LOOPS_KILLED));

                box.add("(* Register the first %d aborted loops. *)", MAX_LOOPS_KILLED);
                box.add("IF %s THEN", textGenerator.toString(reachedMaxLoopCond));
                box.indent();
                box.add("%s := %s;", loopsKilled.name, textGenerator.toString(limitedIncrementKilled));
                box.dedent();
                box.add("END_IF;");
            }
        }
        boxNeedsEmptyLine = true;

        // Generate output code if it exists. */
        if (outputFuncCode != null) {
            generateCommentHeader("Write output to actuators.", '-', commentLength, boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            textGenerator.toText(outputFuncCode, box, main.name, false);
        }

        exprGen.releaseTempVariable(isProgressVariable); // isProgress variable is no longer needed.

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
