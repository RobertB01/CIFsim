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

package org.eclipse.escet.cif.plcgen.generators;

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.PlcInstantiatedFunctionBlockData;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.model.PlcModelUtils;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcFuncBlockInstanceVar;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList.PlcVarListKind;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouInstance;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcResource;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTask;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTypeDecl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Class that stores and writes generated PLC code. */
public class PlcCodeStorage {
    /** Length of a comment line that explains what the next code block is aiming to do. */
    private static final int AIM_COMMENT_LENGTH = 79;

    /** Maximum number of registered premature event loop cycle aborts. */
    private static final int MAX_LOOPS_KILLED = 9999;

    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Function application generator. */
    private final PlcFunctionAppls plcFuncAppls;

    /** Project with PLC code. */
    private final PlcProject project;

    /** PLC resource for the program. */
    private final PlcResource resource;

    /** Task running the PLC program. */
    private final PlcTask task;

    /** Main program POU. */
    private final PlcPou mainProgram;

    /** Global variable list for constants, lazily created. */
    private PlcGlobalVarList globalConstants = null;

    /** Global variable list for input variables, lazily created. */
    private PlcGlobalVarList globalInputs = null;

    /** Global variable list for output variables, lazily created. */
    private PlcGlobalVarList globalOutputs = null;

    /** Global variable list for timer variables, lazily created. */
    private PlcGlobalVarList globalTimerVars = null;

    /** The expression generator to use for generating code in the main program. Initialized lazily. */
    private ExprGenerator exprGenerator = null;

    /** The variable that tracks progress is made in performing events. Initialized lazily. */
    private PlcBasicVariable isProgressVariable = null;

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
        this.plcFuncAppls = new PlcFunctionAppls(target);
        this.maxIter = settings.maxIter;

        // Create the project and a configuration.
        project = new PlcProject(settings.projectName);
        PlcConfiguration config = new PlcConfiguration(settings.configurationName);
        project.configurations.add(config);

        // Create a resource and a task for it.
        resource = new PlcResource(settings.resourceName);
        config.resources.add(resource);
        task = new PlcTask(settings.taskName, settings.taskCycleTime, settings.taskPriority);
        resource.tasks.add(task);

        // Create the main program POU, and hook it into the task.
        mainProgram = new PlcPou("MAIN", PlcPouType.PROGRAM, null);
        project.pous.add(mainProgram);
        task.pouInstances.add(new PlcPouInstance("MAIN", mainProgram));
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
    public PlcBasicVariable getIsProgressVariable() {
        if (isProgressVariable == null) {
            isProgressVariable = getExprGenerator().getTempVariable("isProgress", PlcElementaryType.BOOL_TYPE);
        }
        return isProgressVariable;
    }

    /**
     * Add a variable to the constants table.
     *
     * @param plcVar Variable to add. Name is assumed to be unique.
     */
    public void addConstant(PlcBasicVariable plcVar) {
        Assert.check(target.supportsConstants());

        if (globalConstants == null) {
            globalConstants = new PlcGlobalVarList("CONSTANTS", PlcVarListKind.CONSTANTS);
        }
        globalConstants.variables.add(plcVar);
    }

    /**
     * Add a variable to the input variable table.
     *
     * @param variable Variable to add. Name is assumed to be unique.
     */
    public void addInputVariable(PlcBasicVariable variable) {
        if (globalInputs == null) {
            globalInputs = new PlcGlobalVarList("INPUTS", PlcVarListKind.INPUT_OUTPUT);
        }
        globalInputs.variables.add(variable);
    }

    /**
     * Add a variable to the output variable table.
     *
     * @param variable Variable to add. Name is assumed to be unique.
     */
    public void addOutputVariable(PlcBasicVariable variable) {
        if (globalOutputs == null) {
            globalOutputs = new PlcGlobalVarList("OUTPUTS", PlcVarListKind.INPUT_OUTPUT);
        }
        globalOutputs.variables.add(variable);
    }

    /**
     * Add a variable to the persistent state variable table.
     *
     * @param name Name of new variable. Name is assumed to be unique.
     * @param type Type of the new variable.
     * @return The added new variable.
     */
    public PlcBasicVariable addStateVariable(String name, PlcType type) {
        return addStateVariable(name, type, null, null);
    }

    /**
     * Add a variable to persistent state variable table.
     *
     * @param name Name of new variable. Name is assumed to be unique.
     * @param type Type of the new variable.
     * @param address If not {@code null}, the I/O address of the new variable.
     * @param initValue If not {@code null}, the initial value of the new variable.
     * @return The added new variable.
     */
    public PlcBasicVariable addStateVariable(String name, PlcType type, String address, PlcExpression initValue) {
        PlcBasicVariable plcVar = new PlcDataVariable(target.getStateVariablePrefix(), name, type, address, initValue);
        mainProgram.localVars.add(plcVar);
        return plcVar;
    }

    /**
     * Add a temporary variable to the program (valid for a single PLC cycle).
     *
     * @param variable Variable to add. Name is assumed to be unique.
     */
    public void addTempVariable(PlcBasicVariable variable) {
        mainProgram.tempVars.add(variable);
    }

    /**
     * Add a variable to the timer variables table.
     *
     * @param varName Name of the variable instance containing the instantiated TON function block. Name is assumed to
     *     be unique.
     * @return The instantiated function TON function block.
     */
    public PlcInstantiatedFunctionBlockData addTimerVariable(String varName) {
        if (globalTimerVars == null) {
            // S7 needs timer function blocks as a separate list. Other timer related data should be stored in other
            // variable lists.
            globalTimerVars = new PlcGlobalVarList("TIMERS", PlcVarListKind.TIMERS);
        }

        PlcFunctionBlockDescription tonFuncDescr = plcFuncAppls.makeTonBlock(varName);
        PlcFuncBlockInstanceVar timerVar = new PlcFuncBlockInstanceVar(varName, tonFuncDescr);
        globalTimerVars.variables.add(timerVar);
        return new PlcInstantiatedFunctionBlockData(tonFuncDescr, timerVar);
    }

    /**
     * Add a type declaration to the type declarations list.
     *
     * @param decl Declaration to add. Name is assumed to be unique.
     */
    public void addTypeDecl(PlcTypeDecl decl) {
        project.typeDecls.add(decl);
    }

    /**
     * Add code to initialize the state of CIF variables.
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
     * @param uncontrollableEventTransitionsIterationCode Code that (tries to) perform one iteration of all
     *     uncontrollable event transitions. Code should update the {@link #isProgressVariable} if an event is
     *     performed.
     * @param controllableEventTransitionsIterationCode Code that (tries to) perform one iteration of all controllable
     *     event transitions. Code should update the {@link #isProgressVariable} if an event is performed.
     * @see #getIsProgressVariable
     */
    public void addEventTransitions(List<PlcStatement> uncontrollableEventTransitionsIterationCode,
            List<PlcStatement> controllableEventTransitionsIterationCode)
    {
        Assert.check(this.eventTransitionsIterationCode == null);
        List<PlcStatement> eventTransitionsIterationCode = uncontrollableEventTransitionsIterationCode;
        eventTransitionsIterationCode.addAll(controllableEventTransitionsIterationCode);
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

        ExprGenerator exprGen = getExprGenerator();
        ModelTextGenerator textGenerator = target.getModelTextGenerator();
        NameGenerator nameGen = target.getNameGenerator();

        // The "firstRun" boolean is needed in state initialization, but creation has been moved to here before
        // pushing the variable tables to the output.
        PlcBasicVariable firstRun = null;
        if (stateInitializationCode != null) {
            String name = nameGen.generateGlobalName("firstRun", false);
            firstRun = addStateVariable(name, PlcElementaryType.BOOL_TYPE, null, new PlcBoolLiteral(true));
        }

        // Construct loop and killed counters.
        PlcBasicVariable loopCount = null;
        PlcBasicVariable loopsKilled = null;
        if (maxIter != null) {
            // Construct a "loopsKilled" variable, ensure the maximum value fits in the type.
            String name = nameGen.generateGlobalName("loopsKilled", false);
            loopsKilled = addStateVariable(name, PlcElementaryType.INT_TYPE);
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
            addTempVariable(loopCount);
        }

        // Add all created variable tables.
        addGlobalVariableTable(globalConstants);
        addGlobalVariableTable(globalInputs);
        addGlobalVariableTable(globalOutputs);
        addGlobalVariableTable(globalTimerVars);

        // Prepare adding code to the program.
        CodeBox box = mainProgram.body;
        boolean boxNeedsEmptyLine = false;

        // Add input code if it exists.
        if (inputFuncCode != null) {
            generateCommentHeader("Read input from sensors.", '-', boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            textGenerator.toText(inputFuncCode, box, mainProgram.name, false);
        }

        // Add initialization code if it exists.
        if (stateInitializationCode != null) {
            String headerText = (updateContVarsRemainingTimeCode == null) ? "Initialize state."
                    : "Initialize state or update continuous variables.";
            generateCommentHeader(headerText, '-', boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            // Insert code to create the initial state with the "firstRun" boolean to run it only once.
            // The variable is added above, before the variable tables are pushed to the output.
            //
            box.add("IF %s THEN", firstRun.varRefText);
            box.indent();
            box.add("%s := FALSE;", firstRun.varRefText);
            if (loopsKilled != null) {
                box.add("%s := 0;", loopsKilled.varRefText);
            }
            box.add();
            textGenerator.toText(stateInitializationCode, box, mainProgram.name, false);
            box.dedent();
            if (updateContVarsRemainingTimeCode != null) {
                box.add("ELSE");
                box.indent();
                textGenerator.toText(updateContVarsRemainingTimeCode, box, mainProgram.name, false);
                box.dedent();
            }
            box.add("END_IF;");
        }

        // Add event transitions code.
        boxNeedsEmptyLine = generateEventTransitionsCode(loopCount, loopsKilled, box, boxNeedsEmptyLine);

        // Generate output code if it exists. */
        if (outputFuncCode != null) {
            generateCommentHeader("Write output to actuators.", '-', boxNeedsEmptyLine, box);
            boxNeedsEmptyLine = true;

            textGenerator.toText(outputFuncCode, box, mainProgram.name, false);
        }

        exprGen.releaseTempVariable(isProgressVariable); // isProgress variable is no longer needed.

        // Add temporary variables of the main program code.
        mainProgram.tempVars = exprGen.getCreatedTempVariables();
    }

    /**
     * Generate event transitions code.
     *
     * @param loopCount PLC variable containing the number of loops performed. If {@code null}, the loop count is not
     *     recorded.
     * @param loopsKilled PLC variable containing the number of loops that were aborted due to the loop count limit
     *     since the start of the PLC. If {@code null}, the loop count is not recorded.
     * @param box Destination of the generated code.
     * @param boxNeedsEmptyLine Whether an empty line should be inserted in the box output before generating more code.
     * @return Whether an empty line should be inserted in the box output before generating more code.
     */
    private boolean generateEventTransitionsCode(PlcBasicVariable loopCount, PlcBasicVariable loopsKilled, CodeBox box,
            boolean boxNeedsEmptyLine)
    {
        if (eventTransitionsIterationCode == null) {
            return boxNeedsEmptyLine;
        }

        ModelTextGenerator textGenerator = target.getModelTextGenerator();
        PlcFunctionAppls funcAppls = new PlcFunctionAppls(target);

            generateCommentHeader("Process all events.", '-', boxNeedsEmptyLine, box);

            PlcBasicVariable progressVar = getIsProgressVariable();
            box.add("%s := TRUE;", progressVar.varRefText);

            // Start the event processing loop.
            if (loopCount == null) {
                // Unrestricted looping, no need to count loops.
                box.add("(* Perform events until none can be done anymore. *)");
                box.add("WHILE %s DO", progressVar.varRefText);
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
                box.add("%s := 0;", loopCount.varRefText);
                box.add("WHILE %s DO", textGenerator.toString(whileCond));
                box.indent();
                box.add("%s := %s + 1;", loopCount.varRefText, loopCount.varRefText);
            }

            // Construct the while body with event processing.
            box.add("%s := FALSE;", progressVar.varRefText);
            box.add();
            textGenerator.toText(eventTransitionsIterationCode, box, mainProgram.name, false);
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
                box.add("%s := %s;", loopsKilled.varRefText, textGenerator.toString(limitedIncrementKilled));
                box.dedent();
                box.add("END_IF;");
            }
        return true;
    }

    /**
     * Generate a comment header line possibly after an empty line.
     *
     * @param text Text describing what code lines will come next.
     * @param dashChar The character to use as filler, {@code '-'} or {@code '='} are likely useful.
     * @param addEmptyLine Whether to generate an empty line first.
     * @param box Storage for the generated lines.
     */
    private void generateCommentHeader(String text, char dashChar, boolean addEmptyLine, CodeBox box) {
        // Construct header line. As it is mostly constant data, code will be much more efficient than it seems.
        char[] pre = {'(', '*', ' ', dashChar, dashChar, dashChar, ' '};
        char[] post = {dashChar, dashChar, dashChar, ' ', '*', ')'};
        char[] afterText = {' '};

        int layoutLength = pre.length + afterText.length + post.length;
        int length = Math.max(AIM_COMMENT_LENGTH, layoutLength + text.length());

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
