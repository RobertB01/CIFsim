//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.expressions.CifDataProvider;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcFuncApplStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;

/**
 * Code generator for continuous variables handling.
 *
 * <p>
 * We restrict continuous variables to timers, as that is the primary use of CIF continuous variables in controllers.
 * Setting a value for the delay should be possible, as well as detecting that the delay has reached its end. A
 * secondary use is to check how much time is still remaining (which seems more relevant than how long ago the timer has
 * been started).
 * </p>
 * <p>
 * In the PLC, TON timers are used. One TON block for each continuous CIF variable. A TON timer can be started by
 * calling it with a preset time value (in seconds). There is a second way to call the TON block to just update output
 * for passed time since the last call. After each call the TON block outputs its current value. That value starts at
 * {@code 0} when setting a value, and increases with time until it reaches the preset value after 'preset value'
 * seconds. The output does not increase further after reaching the preset value even if more time passes.
 * </p>
 * <p>
 * To query remaining time of a running timer in CIF, it's simplest for the users if they can state such a query as
 * {@code val <= N} to query whether remaining time is equal or less than {@code N} seconds. Testing for timeout then
 * becomes {@code val <= 0}. This implies CIF continuous variables must have derivative {@code -1} and assigned values
 * to continuous variables must be non-negative.
 * </p>
 * <p>
 * As the PLC output value goes up, in the implementation the remaining time in the PLC must be computed by subtracting
 * the output value from the last preset value of the TON block. Since the output value stops increasing when the
 * timeout has been reached, a comparison in CIF against purely negative values will fail in the implementation. To
 * ensure the CIF notion of continuous variables is maintained in the PLC implementation, only comparisons of continuous
 * variables against a single contiguous range with at least one non-negative value is allowed.
 * </p>
 * <p>
 * Translation of continuous variables is as follows. For each continuous variable there is a preset variable {@code P},
 * a state variable for the remaining time {@code R}, and a {@code TON} variable containing the timer instance.
 * <ul>
 * <li>Before the input phase, the {@code R} variable is updated by calling
 * {@code TON(PT := P, IN := TRUE, Q => B, ET => V);} and computing {@code R := SEL(B, P - V, 0.0);} with temporary
 * variables {@code V and B}.</li>
 * <li>Obtaining the remaining time of the timer reads variable {@code R}.</li>
 * <li>Assigning a new value to the continuous variable changes {@code R} and {@code P}, and resets the timer with
 * {@code TON(PT := P, IN := FALSE);} followed by starting the timer with {@code TON(PT := P, IN := TRUE);}.</li>
 * <li>Comparing the continuous variable against a value {@code V} using comparison operator {@code c} generates the
 * infix expression {@code R c V}.</li>
 * </ul>
 * The effect of the above is that for the transitions, time has stopped but all timers keep running.
 * </p>
 */
public class DefaultContinuousVariablesGenerator implements ContinuousVariablesGenerator {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Continuous variables in the specification with its code generator. */
    private final Map<ContVariable, PlcTimerCodeGenerator> timers = map();

    /**
     * Constructor of the {@link DefaultContinuousVariablesGenerator} class.
     *
     * @param target PLC target to generate code for.
     */
    public DefaultContinuousVariablesGenerator(PlcTarget target) {
        this.target = target;
    }

    @Override
    public void addVariable(ContVariable contVar) {
        CifDataProvider cifDataProvider = target.getCodeStorage().getExprGenerator().getScopeCifDataProvider();
        PlcTimerGen timerData = new PlcTimerGen(target, contVar,
                cifDataProvider.getAddressableForContvar(contVar, false));
        timers.put(contVar, timerData);
    }

    @Override
    public void process() {
        // Add the timer PLC variables to the output.
        for (PlcTimerCodeGenerator timer: timers.values()) {
            timer.addInstanceVariables();
        }

        // Generate code to update continuous state variables at the start of a PLC cycle.
        //
        // For the first cycle, timers are initialized as part of state initialization. For the non-first cycle however,
        // remaining time should be updated.

        // Generate the code.
        List<PlcStatement> updateContVarsRemainingTime = timers.values().stream()
                .flatMap(timer -> timer.generateRemainingUpdate().stream()).toList();

        // Store the generated code in code storage.
        PlcCodeStorage codeStorage = target.getCodeStorage();
        if (!updateContVarsRemainingTime.isEmpty()) {
            codeStorage.storeUpdateContvarsRemainingTimeCode(updateContVarsRemainingTime);
        }
    }

    @Override
    public PlcTimerCodeGenerator getPlcTimerCodeGen(ContVariable cvar) {
        PlcTimerCodeGenerator timerCodegen = timers.get(cvar);
        Assert.notNull(timerCodegen);
        return timerCodegen;
    }

    /** PLC code generation support class for a single continuous variable. */
    public static class PlcTimerGen implements PlcTimerCodeGenerator {
        /** PLC target to generate code for. */
        private final PlcTarget target;

        /** Function applications generator. */
        private final PlcFunctionAppls plcFuncAppls;

        /** Continuous variable being handled here. */
        public final ContVariable contVar;

        /** The PLC state variable for the continuous variable. */
        public final PlcVarExpression plcContVar;

        /** Parameter description of the TON function block. */
        private final PlcFunctionBlockDescription tonFuncBlock;

        /** PLC variable representing the TON timer. */
        private final PlcVariable timerVar;

        /** PLC variable storing the last set preset value. */
        private final PlcVariable presetVar;

        /**
         * Constructor of the {@link PlcTimerGen} class.
         *
         * @param target PLC target to generate code for.
         * @param contVar Continuous variable being handled here.
         * @param plcContVar The PLC state variable for the continuous variable.
         */
        public PlcTimerGen(PlcTarget target, ContVariable contVar, PlcVarExpression plcContVar) {
            this.target = target;
            plcFuncAppls = new PlcFunctionAppls(target);
            this.contVar = contVar;
            this.plcContVar = plcContVar;

            // Construct the variables needed for the timer. Relate all variables to the continuous variable.
            NameGenerator nameGen = target.getNameGenerator();
            String cvarName = getAbsName(contVar, false);
            String name = nameGen.generateGlobalName("ton_" + cvarName, false);
            tonFuncBlock = PlcFunctionBlockDescription.makeTonBlock(name);
            timerVar = new PlcVariable(name, tonFuncBlock.funcBlockType);

            name = nameGen.generateGlobalName("preset_" + cvarName, false);
            presetVar = new PlcVariable(name, PlcElementaryType.TIME_TYPE);
        }

        @Override
        public void addInstanceVariables() {
            PlcCodeStorage codeStorage = target.getCodeStorage();
            codeStorage.addTimerVariable(timerVar);
            codeStorage.addTimerVariable(presetVar);
        }

        @Override
        public List<PlcStatement> generateRemainingUpdate() {
            ExprGenerator exprGen = target.getCodeStorage().getExprGenerator();
            PlcVariable v = exprGen.getTempVariable("curValue", target.getRealType());
            PlcVariable b = exprGen.getTempVariable("timeOut", PlcElementaryType.BOOL_TYPE);

            List<PlcStatement> statements = listc(3);
            statements.add(new PlcCommentLine("Update remaining time of \"" + plcContVar.variable.name + "\"."));

            // Get the current timer state information by calling TON(PT := P, IN := TRUE, Q => B, ET => V);
            List<PlcNamedValue> arguments = List.of(new PlcNamedValue("PT", new PlcVarExpression(presetVar)),
                    new PlcNamedValue("IN", new PlcBoolLiteral(true)), new PlcNamedValue("Q", new PlcVarExpression(b)),
                    new PlcNamedValue("ET", new PlcVarExpression(v)));
            statements.add(new PlcFuncApplStatement(plcFuncAppls.funcBlockAppl(tonFuncBlock, arguments)));

            // Compute updated remaining time R := SEL(B, P - V, 0.0);
            PlcExpression subExpr = plcFuncAppls.subtractFuncAppl(new PlcVarExpression(presetVar),
                    new PlcVarExpression(v));
            subExpr = plcFuncAppls.selFuncAppl(new PlcVarExpression(b), subExpr, new PlcRealLiteral("0.0"));
            statements.add(new PlcAssignmentStatement(plcContVar, subExpr));

            return statements;
        }

        @Override
        public List<PlcStatement> generateAssignPreset() {
            // Assign new value to P and R;
            List<PlcStatement> statements = listc(4);
            statements.add(new PlcCommentLine("Reset timer of \"" + plcContVar.variable.name + "\"."));
            statements.add(new PlcAssignmentStatement(presetVar, plcContVar));

            // Reset the timer with TON(PT := P, IN := FALSE);
            List<PlcNamedValue> arguments = List.of(new PlcNamedValue("PT", new PlcVarExpression(presetVar)),
                    new PlcNamedValue("IN", new PlcBoolLiteral(false)));
            statements.add(new PlcFuncApplStatement(plcFuncAppls.funcBlockAppl(tonFuncBlock, arguments)));

            // Start the timer with TON(PT := P, IN := TRUE);
            arguments = List.of(new PlcNamedValue("PT", new PlcVarExpression(presetVar)),
                    new PlcNamedValue("IN", new PlcBoolLiteral(true)));
            statements.add(new PlcFuncApplStatement(plcFuncAppls.funcBlockAppl(tonFuncBlock, arguments)));

            return statements;
        }
    }
}
