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

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.makeInitialUppercase;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifDocAnnotationFormatter;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.model.PlcModelUtils;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcConfiguration;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDeclaredType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcGlobalVarList.PlcVarListKind;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouInstance;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPouType;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcProject;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcResource;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcTask;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcFuncBlockType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.output.WarnOutput;

/** Class that stores and writes generated PLC code. */
public class PlcCodeStorage {
    /** Length of a comment line that explains what the next code block is aiming to do. */
    private static final int AIM_COMMENT_LENGTH = 79;

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

    /** Text lines of the PLC program header. */
    private final List<String> programHeaderTextLines;

    /** Documentation data about complex components, or {@code null} if not yet set. */
    private Map<ComplexComponent, ComponentDocData> componentDatas = null;

    /** If not {@code null}, code for initializing the state variables. */
    private List<PlcStatement> stateInitializationCode = null;

    /**
     * If not {@code null}, code to update the remaining time of the continuous variables just before the non-first time
     * of the event transitions.
     */
    private List<PlcStatement> updateContVarsRemainingTimeCode = null;

    /** If not {@code null}, code to perform one iteration of every uncontrollable event. */
    private List<PlcStatement> uncontrollableEventTransitionsCode = null;

    /** If not {@code null}, code to perform one iteration of every controllable event. */
    private List<PlcStatement> controllableEventTransitionsCode = null;

    /**
     * Maximum number of iterations for performing uncontrollable events in a single cycle, or {@code null} if
     * unrestricted.
     */
    private Integer maxUncontrollableLimit;

    /**
     * Maximum number of iterations for performing controllable events in a single cycle, or {@code null} if
     * unrestricted.
     */
    private Integer maxControllableLimit;

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
        this.maxUncontrollableLimit = limitMaxIter(settings.maxUncontrollableLimit, "uncontrollable",
                settings.warnOutput);
        this.maxControllableLimit = limitMaxIter(settings.maxControllableLimit, "controllable", settings.warnOutput);
        this.programHeaderTextLines = settings.programHeaderTextLines;

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
     * Limit the given events iteration limit to the capabilities of the target. Give a warning if the count gets
     * reduced.
     *
     * @param specifiedLimit Limit as given by the user.
     * @param eventKind Kind of events that get affected by a change in the limit.
     * @param warnOutput Callback to send warnings to the user.
     * @return Limit that is feasible for the target.
     */
    private Integer limitMaxIter(Integer specifiedLimit, String eventKind, WarnOutput warnOutput) {
        if (specifiedLimit == null) {
            return specifiedLimit; // Infinite limit always works.
        }

        // Compute the maximum feasible limit that can be checked.
        PlcElementaryType loopCountType = target.getStdIntegerType();
        int feasibleLimit = switch (loopCountType.bitSize) {
            case 64, 32 -> specifiedLimit; // Java int size is 32 bit, all values of the limit fit.
            case 16 -> Math.min(specifiedLimit, 0x7FFF);
            default -> throw new AssertionError("Unexpected loopCount bit-size " + loopCountType.bitSize + " found.");
        };

        // Give a warning if the limit was reduced.
        if (specifiedLimit != feasibleLimit) {
            warnOutput.line("Maximum iteration limit for %s events was reduced from %d to %d, as the PLC's integer "
                    + "type does not support larger values.", eventKind, specifiedLimit, feasibleLimit);
        }

        return feasibleLimit;
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
    public void addConstant(PlcDataVariable plcVar) {
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
    public void addInputVariable(PlcDataVariable variable) {
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
    public void addOutputVariable(PlcDataVariable variable) {
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
    public PlcDataVariable addStateVariable(String name, PlcType type) {
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
    public PlcDataVariable addStateVariable(String name, PlcType type, String address, PlcExpression initValue) {
        PlcDataVariable plcVar = new PlcDataVariable(target.getStateVariablePrefix(), name, type, address, initValue);
        mainProgram.localVars.add(plcVar);
        return plcVar;
    }

    /**
     * Add a temporary variable to the program (valid for a single PLC cycle).
     *
     * @param variable Variable to add. Name is assumed to be unique.
     */
    public void addTempVariable(PlcDataVariable variable) {
        mainProgram.tempVars.add(variable);
    }

    /**
     * Add a variable to the timer variables table.
     *
     * @param variable Variable to add.
     */
    public void addTimerVariable(PlcDataVariable variable) {
        if (globalTimerVars == null) {
            // S7 needs timer function blocks as a separate list. Other timer related data should be stored in other
            // variable lists.
            globalTimerVars = new PlcGlobalVarList("TIMERS", PlcVarListKind.TIMERS);
        }
        Assert.check(variable.type instanceof PlcFuncBlockType);
        globalTimerVars.variables.add(variable);
    }

    /**
     * Add a declared type to the declared types list.
     *
     * @param declaredType Type to declare. Name of the type is assumed to be unique.
     */
    public void addDeclaredType(PlcDeclaredType declaredType) {
        project.declaredTypes.add(declaredType);
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
        // No code added yet?
        Assert.areEqual(null, uncontrollableEventTransitionsCode);
        Assert.areEqual(null, controllableEventTransitionsCode);

        // Add the given code if it has content.
        if (PlcModelUtils.isNonEmptyCode(uncontrollableEventTransitionsIterationCode)) {
            uncontrollableEventTransitionsCode = uncontrollableEventTransitionsIterationCode;
        }
        if (PlcModelUtils.isNonEmptyCode(controllableEventTransitionsIterationCode)) {
            controllableEventTransitionsCode = controllableEventTransitionsIterationCode;
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
        PlcDataVariable loopCount = null;
        PlcBasicVariable loopsKilled = null;
        if (maxUncontrollableLimit != null || maxControllableLimit != null) {
            // Construct a "loopsKilled" variable, ensure the maximum value fits in the type.
            String name = nameGen.generateGlobalName("loopsKilled", false);
            loopsKilled = addStateVariable(name, PlcElementaryType.INT_TYPE);
            Assert.check(MAX_LOOPS_KILLED + 1 <= 0x7FFF); // One more for "min(killed + 1, max_value)".

            // Construct a "loopCount" variable, finite upper bounds on the loop have already been limited to fit in the
            // standard integer PLC type.
            PlcElementaryType loopCountType = target.getStdIntegerType();
            loopCount = exprGen.makeLocalVariable("loopCount", loopCountType);
            addTempVariable(loopCount);
        }

        // Add all created variable tables.
        addGlobalVariableTable(globalConstants);
        addGlobalVariableTable(globalInputs);
        addGlobalVariableTable(globalOutputs);
        addGlobalVariableTable(globalTimerVars);

        // Prepare adding code to the program.
        boolean isProperPlcBody = false; // Tracks whether a proper PLC statement is added to the main program.
        CodeBox box = mainProgram.body;
        addProgramHeader(box);
        box.add();
        box.add(generateComponentDocumentation());

        // Add input code if it exists.
        //
        // Both state initialization and state update use the sensor information. For best results, that information
        // should be consistent and it should be as recent as possible. Therefore, all PLC inputs should be read at the
        // same time, and it should be done just before state computation.
        if (inputFuncCode != null) {
            box.add();
            generateCommentHeader("Read PLC inputs.", '-', box);
            textGenerator.toText(inputFuncCode, box, mainProgram.name, false);
            isProperPlcBody = isProperPlcBody || hasProperPlcStatement(inputFuncCode);
        }

        // Add initialization code if it exists.
        if (stateInitializationCode != null) {
            String headerText = (updateContVarsRemainingTimeCode == null) ? "Initialize state."
                    : "Initialize state or update continuous variables.";
            box.add();
            generateCommentHeader(headerText, '-', box);

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

            isProperPlcBody = true;
        }

        // Add event transitions code.
        generateEventTransitionsCode(uncontrollableEventTransitionsCode, maxUncontrollableLimit, "uncontrollable",
                loopCount, loopsKilled, box);
        isProperPlcBody = isProperPlcBody || hasProperPlcStatement(uncontrollableEventTransitionsCode);

        generateEventTransitionsCode(controllableEventTransitionsCode, maxControllableLimit, "controllable",
                loopCount, loopsKilled, box);
        isProperPlcBody = isProperPlcBody || hasProperPlcStatement(controllableEventTransitionsCode);

        // Generate output code if it exists.
        //
        // All outputs of the PLC are updated at the same time to send a consistent state to the controlled system.
        // To address the requirement that a safety output may be written only once in a PLC cycle, the code below that
        // generates the PLC output code is executed only one time during the PLC code generation process.
        // Secondary advantages of this solution are that it is easier to get it correctly implemented in the generator
        // and it becomes easier to verify the safety requirement in a review of the generated PLC code.
        if (outputFuncCode != null) {
            box.add();
            generateCommentHeader("Write PLC outputs.", '-', box);
            textGenerator.toText(outputFuncCode, box, mainProgram.name, false);
            isProperPlcBody = isProperPlcBody || hasProperPlcStatement(outputFuncCode);
        }

        if (!isProperPlcBody) {
            box.add("(* Nothing to do. *) ;");
        }

        exprGen.releaseTempVariable(isProgressVariable); // isProgress variable is no longer needed.

        // Add temporary variables of the main program code.
        mainProgram.tempVars.addAll(exprGen.getCreatedTempVariables());
    }

    /**
     * Generate event transitions code.
     *
     * @param eventTransitionsIterationCode If not {@code null}, code to try to perform each event once.
     * @param maxIter Maximum number of loop iterations before aborting the loop. The value {@code null} means
     *     'infinite'.
     * @param eventKind The kind of events that are tried to perform.
     * @param loopCount PLC variable containing the number of loops performed. If {@code null}, the loop count is not
     *     recorded.
     * @param loopsKilled PLC variable containing the number of loops that were aborted due to the loop count limit
     *     since the start of the PLC. If {@code null}, the loop count is not recorded.
     * @param box Destination of the generated code.
     */
    private void generateEventTransitionsCode(List<PlcStatement> eventTransitionsIterationCode, Integer maxIter,
            String eventKind, PlcBasicVariable loopCount, PlcBasicVariable loopsKilled, CodeBox box)
    {
        if (eventTransitionsIterationCode == null) {
            return;
        }

        ModelTextGenerator textGenerator = target.getModelTextGenerator();
        PlcFunctionAppls funcAppls = new PlcFunctionAppls(target);

        box.add();
        generateCommentHeader("Process " + eventKind + " events.", '-', box);

        PlcBasicVariable progressVar = getIsProgressVariable();
        box.add("%s := TRUE;", progressVar.varRefText);

        // Start the event processing loop.
        if (maxIter == null) {
            // Unrestricted looping, no need to count loops.
            box.add("(* Perform events until none can be done anymore. *)");
            box.add("WHILE %s DO", progressVar.varRefText);
            box.indent();
        } else {
            Assert.notNull(loopCount); // Counter is needed if there is a finite maximum loop count.

            // Generate condition "progress AND loopCount < max".
            PlcExpression progressCond = new PlcVarExpression(progressVar);
            PlcExpression maxIterCond = funcAppls.lessThanFuncAppl(new PlcVarExpression(loopCount),
                    target.makeStdInteger(maxIter));
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
        if (maxIter != null && loopsKilled != null) {
            // If maximum iterations is infinite, the loop never aborts and loopsKilled is never changed.

            Assert.notNull(loopCount); // Technically already implied in the previous code block, but doesn't hurt.

            // IF loopCount >= MAX_ITER THEN loopsKilled := MIN(loopsKilled + 1, MAX_LOOPS_KILLED); END_IF;
            PlcExpression reachedMaxLoopCond = funcAppls.greaterEqualFuncAppl(new PlcVarExpression(loopCount),
                    target.makeStdInteger(maxIter));
            PlcExpression incKilled = funcAppls.addFuncAppl(new PlcVarExpression(loopsKilled),
                    new PlcIntLiteral(1, loopsKilled.type));
            PlcExpression limitedIncrementKilled = funcAppls.minFuncAppl(incKilled,
                    new PlcIntLiteral(MAX_LOOPS_KILLED, loopsKilled.type));

            box.add("(* Register the first %d aborted loops. *)", MAX_LOOPS_KILLED);
            box.add("IF %s THEN", textGenerator.toString(reachedMaxLoopCond));
            box.indent();
            box.add("%s := %s;", loopsKilled.varRefText, textGenerator.toString(limitedIncrementKilled));
            box.dedent();
            box.add("END_IF;");
        }
    }

    /**
     * Generate a comment header line possibly after an empty line.
     *
     * @param text Text describing what code lines will come next.
     * @param dashChar The character to use as filler, {@code '-'} or {@code '='} are likely useful.
     * @param box Storage for the generated lines.
     */
    private void generateCommentHeader(String text, char dashChar, CodeBox box) {
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
     * Copy the program header text lines into a PLC comment block.
     *
     * @param box Destination of the text lines.
     */
    private void addProgramHeader(CodeBox box) {
        boolean first = true;
        for (String line: programHeaderTextLines) {
            String prefix = first ? "(*" : " *";
            first = false;

            if (line.isEmpty()) {
                box.add(prefix);
            } else {
                box.add(prefix + " " + line);
            }
        }
        if (!first) {
            box.add(" *)");
        }
    }

    /**
     * Store the given component documentation data.
     *
     * @param componentDatas Data to store.
     */
    public void addComponentDatas(Map<ComplexComponent, ComponentDocData> componentDatas) {
        this.componentDatas = componentDatas;
    }

    /**
     * Set the name of the edge selection variable of an automaton.
     *
     * @param aut Automaton that is related to the given edge selection variable.
     * @param edgeVariableName Name of the edge selection variable.
     */
    public void setAutomatonEdgeVariableName(Automaton aut, String edgeVariableName) {
        ComponentDocData compData = componentDatas.computeIfAbsent(aut, c -> new ComponentDocData(c));
        compData.edgeVariableName = edgeVariableName;
    }

    /**
     * Generate documentation for the generated PLC code about the CIF complex components.
     *
     * @return The created lines of text.
     */
    private List<String> generateComponentDocumentation() {
        // Construct @doc annotation formatters, one for automata and one for the information inside automata.
        CifDocAnnotationFormatter autDocFormatter = new CifDocAnnotationFormatter(null, null, " * ", List.of(" *"),
                null);
        CifDocAnnotationFormatter subDocFormatter = new CifDocAnnotationFormatter(null, null, " *   ", List.of(" *"),
                null);

        TextTopics topics = new TextTopics(" *");

        // Order the components by name.
        List<ComponentDocData> compDatas = listc(componentDatas.size());
        compDatas.addAll(componentDatas.values());
        Collections.sort(compDatas, Comparator.comparing(cd -> cd.getComponentName()));

        // Open the comment, and add a header.
        topics.add("(*------------------------------------------------------");
        topics.add(" * Model overview:");

        // Add the documentation text of each component.
        boolean allComponentsEmpty = true;
        for (ComponentDocData compData: compDatas) {
            if (compData.isEmpty()) {
                continue; // Nothing to say about this component, skip it.
            }
            allComponentsEmpty = false;

            // Sort the content of the component for a nicer output.
            compData.sortData();

            // Print a line describing the component.
            topics.ensureEmptyAtEnd();
            topics.add(" * ----");
            topics.add(" * %s:", makeInitialUppercase(DocumentingSupport.getDescription(compData.component)));
            topics.addAll(autDocFormatter.formatDocs(compData.component));

            // List the variables.
            topics.ensureEmptyAtEnd();
            if (compData.isEmptyVariables()) {
                topics.add(" * - No variables in this component.");
            } else {
                for (Declaration var: compData.variables) {
                    topics.add(" * - %s.", makeInitialUppercase(DocumentingSupport.getDescription(var)));
                    topics.addAll(subDocFormatter.formatDocs(var));
                }
                if (compData.edgeVariableName != null) {
                    topics.ensureEmptyAtEnd();
                    topics.add(" * - PLC edge selection variable \"%s\".", compData.edgeVariableName);
                }
            }

            // If printing a group, there is nothing more to list here.
            if (!(compData.component instanceof Automaton)) {
                continue;
            }

            // List the uncontrollable events of the component.
            topics.ensureEmptyAtEnd();
            if (compData.uncontrollableEvents.isEmpty()) {
                topics.add(" * - No use of uncontrollable events.");
            } else {
                for (Event evt: compData.uncontrollableEvents) {
                    topics.add(" * - %s.", makeInitialUppercase(DocumentingSupport.getDescription(evt)));
                    topics.addAll(subDocFormatter.formatDocs(evt));
                }
            }

            // List the controllable events of the component.
            topics.ensureEmptyAtEnd();
            if (compData.controllableEvents.isEmpty()) {
                topics.add(" * - No use of controllable events.");
            } else {
                for (Event evt: compData.controllableEvents) {
                    topics.add(" * - %s.", makeInitialUppercase(DocumentingSupport.getDescription(evt)));
                    topics.addAll(subDocFormatter.formatDocs(evt));
                }
            }
        }
        if (allComponentsEmpty) {
            // Completely empty model information.
            topics.ensureEmptyAtEnd();
            topics.add(" * No groups or automata to report.");
        }
        topics.dropEmptyAtEnd();
        topics.add(" *------------------------------------------------------ *)");

        return topics.getLines();
    }

    /**
     * Test whether the given code has at least one proper PLC statement.
     *
     * @param statements Statements to check.
     * @return Whether the statements have at least one proper PLC statement.
     */
    private boolean hasProperPlcStatement(List<PlcStatement> statements) {
        if (statements == null) {
            // Code block doesn't exist, definitely no proper PLC statement here.
            return false;
        }

        // Test all provided statements, and return the result.
        for (PlcStatement stat: statements) {
            if (stat.isProperPlcStatement()) {
                return true;
            }
        }
        return false;
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
