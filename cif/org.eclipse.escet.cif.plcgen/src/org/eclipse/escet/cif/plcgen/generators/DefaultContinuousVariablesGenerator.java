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

import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.expressions.CifDataProvider;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
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

        /** TON function block instance variable. */
        public final PlcDataVariable tonVar;

        /** The PLC state variable for the continuous variable. */
        public final PlcVarExpression plcContVar;

        /** PLC variable storing the last set preset value. */
        private final PlcBasicVariable presetVar;

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
            PlcCodeStorage codeStorage = target.getCodeStorage();

            String baseName = nameGen.generateGlobalNames(Set.of("ton_", "preset_"), contVar);
            tonVar = new PlcDataVariable("", "ton_" + baseName, plcFuncAppls.getTonFuncBlockType(), null, null);
            codeStorage.addTimerVariable(tonVar);
            presetVar = codeStorage.addStateVariable("preset_" + baseName, PlcElementaryType.TIME_TYPE);
        }

        @Override
        public List<PlcStatement> generateRemainingUpdate() {
            ExprGenerator exprGen = target.getCodeStorage().getExprGenerator();
            PlcBasicVariable v = exprGen.getTempVariable("curValue", PlcElementaryType.TIME_TYPE);
            PlcBasicVariable b = exprGen.getTempVariable("timeOut", PlcElementaryType.BOOL_TYPE);

            List<PlcStatement> statements = listc(3);
            statements.add(new PlcCommentLine(
                    fmt("Update remaining time of %s.", DocumentingSupport.getDescription(contVar))));

            // Get the current timer state information by calling TON(PT := P, IN := TRUE, Q => B, ET => V);
            List<PlcNamedValue> arguments = List.of(new PlcNamedValue("PT", new PlcVarExpression(presetVar)),
                    new PlcNamedValue("IN", new PlcBoolLiteral(true)), new PlcNamedValue("Q", new PlcVarExpression(b)),
                    new PlcNamedValue("ET", new PlcVarExpression(v)));
            statements.add(new PlcFuncApplStatement(plcFuncAppls.funcBlockAppl(tonVar, arguments)));

            // Compute updated remaining time as a real in seconds.
            // Conceptually: R := SEL(B, P - V, 0.0);
            // Additional concerns:
            // - P and V are of type TIME, thus P - V must be converted to real seconds.
            // - As the conversion is a computation with reals, ensure the result is never negative.
            PlcExpression subExpr = plcFuncAppls.subtractFuncAppl(new PlcVarExpression(presetVar),
                    new PlcVarExpression(v));
            subExpr = atLeast0(timeToReal(subExpr, (PlcElementaryType)plcContVar.type));

            // Apply the selection.
            PlcExpression selExpr = plcFuncAppls.selFuncAppl(new PlcVarExpression(b), subExpr,
                    target.makeStdReal("0.0"));
            statements.add(new PlcAssignmentStatement(plcContVar, selExpr));

            return statements;
        }

        @Override
        public List<PlcStatement> generateAssignPreset() {
            // Assign new value to P and R;
            List<PlcStatement> statements = listc(4);
            statements.add(new PlcCommentLine("Reset timer of \"" + plcContVar.variable.varName + "\"."));
            statements.add(new PlcAssignmentStatement(presetVar, realToTime(plcContVar)));

            // Reset the timer with TON(PT := P, IN := FALSE);
            List<PlcNamedValue> arguments = List.of(new PlcNamedValue("PT", new PlcVarExpression(presetVar)),
                    new PlcNamedValue("IN", new PlcBoolLiteral(false)));
            statements.add(new PlcFuncApplStatement(plcFuncAppls.funcBlockAppl(tonVar, arguments)));

            // Start the timer with TON(PT := P, IN := TRUE);
            arguments = List.of(new PlcNamedValue("PT", new PlcVarExpression(presetVar)),
                    new PlcNamedValue("IN", new PlcBoolLiteral(true)));
            statements.add(new PlcFuncApplStatement(plcFuncAppls.funcBlockAppl(tonVar, arguments)));

            return statements;
        }

        /**
         * Convert a {@code TIME} value to a real value in seconds.
         *
         * @param timeMillis Value to convert.
         * @param destRealType The real type to convert to.
         * @return The conversion expression.
         */
        private PlcExpression timeToReal(PlcExpression timeMillis, PlcElementaryType destRealType) {
            // Get the largest available integer type to get maximum accuracy.
            PlcElementaryType maxIntType = last(target.getSupportedIntegerTypes());

            // TIME are integer milliseconds while reals are seconds. Therefore:
            // 1. Cast to integer.
            // 2. Cast to real.
            // 3. Divide by 1000.0.
            PlcExpression intMillis = plcFuncAppls.castFunctionAppl(timeMillis, maxIntType);
            PlcExpression realMillis = plcFuncAppls.castFunctionAppl(intMillis, destRealType);
            PlcExpression real1000 = new PlcRealLiteral("1000.0", destRealType);
            return plcFuncAppls.divideFuncAppl(realMillis, real1000);
        }

        /**
         * Convert a value interpreted as real value in seconds to {@code TIME} type.
         *
         * @param realSecs Value to convert.
         * @return The conversion expression.
         */
        private PlcExpression realToTime(PlcExpression realSecs) {
            // Get the largest available integer type to get maximum accuracy.
            PlcElementaryType maxIntType = last(target.getSupportedIntegerTypes());

            // Reals are seconds, while PLC TIME are integer milliseconds. Therefore:
            // 1. Multiply by 1000.0.
            // 2. Cast to integer.
            // 3. Cast to time.
            PlcExpression real1000 = new PlcRealLiteral("1000.0", realSecs.type);
            PlcExpression realMillis = plcFuncAppls.multiplyFuncAppl(realSecs, real1000);
            PlcExpression intMillis = plcFuncAppls.castFunctionAppl(realMillis, maxIntType);
            return plcFuncAppls.castFunctionAppl(intMillis, PlcElementaryType.TIME_TYPE);
        }

        /**
         * Ensure a real value is at least 0.0.
         *
         * @param value Value to force to non-negative.
         * @return The expression that computes the answer.
         */
        private PlcExpression atLeast0(PlcExpression value) {
            if (PlcElementaryType.isRealType(value.type)) {
                PlcExpression zero = new PlcRealLiteral("0.0", value.type);
                return plcFuncAppls.maxFuncAppl(value, zero);
            } else {
                throw new AssertionError("Unexpected type encountered: \"" + value.type + "\".");
            }
        }
    }
}
