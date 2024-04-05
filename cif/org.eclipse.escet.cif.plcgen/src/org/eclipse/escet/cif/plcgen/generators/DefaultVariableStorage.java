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

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.StateInitVarOrderer;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.expressions.CifDataProvider;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprValueResult;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;

/** Class for handling storage and retrieval of globally used variables in the PLC program. */
public class DefaultVariableStorage implements VariableStorage {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Function application generator for the target. */
    private final PlcFunctionAppls funcAppls;

    /** Names of converted declarations. */
    private final Map<Declaration, PlcBasicVariable> variables = map();

    /**
     * Constructor of the {@link DefaultVariableStorage} class.
     *
     * @param target PLC target to generate code for.
     */
    public DefaultVariableStorage(PlcTarget target) {
        this.target = target;
        funcAppls = new PlcFunctionAppls(target);
    }

    @Override
    public void addStateVariable(Declaration decl, CifType type) {
        PlcType varType = target.getTypeGenerator().convertType(type);
        String varName = target.getNameGenerator().generateGlobalName(decl);
        PlcBasicVariable plcVar = target.getCodeStorage().addStateVariable(varName, varType);
        variables.put(decl, plcVar);
    }

    @Override
    public void process() {
        // Construct a converter for CIF expressions.
        ExprGenerator exprGen = target.getCodeStorage().getExprGenerator();

        // Order the discrete and continuous variables on their dependencies for initialization.
        // TODO Find out what to do with algebraic variables here (they are not state, this orderer will choke on them).
        StateInitVarOrderer varOrderer = new StateInitVarOrderer();
        for (Declaration decl: variables.keySet()) {
            if (decl instanceof DiscVariable || decl instanceof ContVariable) {
                varOrderer.addObject(decl);
            }
        }

        // Generate initialization code and store it.
        List<PlcStatement> statements = list();
        // TODO Initialize the constants if not done in its declaration.
        statements.add(new PlcCommentLine("Initialize the state variables."));
        for (Declaration decl: varOrderer.computeOrder(true)) {
            ExprValueResult exprResult;
            ContVariable assignedContVar;
            if (decl instanceof DiscVariable discVar) {
                exprResult = exprGen.convertValue(first(discVar.getValue().getValues()));
                assignedContVar = null;
            } else if (decl instanceof ContVariable contVar) {
                exprResult = exprGen.convertValue(contVar.getValue());
                assignedContVar = contVar;
            } else {
                throw new AssertionError("Unexpected kind of variable " + decl);
            }
            statements.addAll(exprResult.code);
            exprGen.releaseTempVariables(exprResult.codeVariables);
            PlcVarExpression lhs = new PlcVarExpression(variables.get(decl));
            statements.add(new PlcAssignmentStatement(lhs, exprResult.value));
            exprGen.releaseTempVariables(exprResult.valueVariables);

            // For continuous variable initialization, also update its timer block.
            if (assignedContVar != null) {
                statements.addAll(target.getContinuousVariablesGenerator().getPlcTimerCodeGen(assignedContVar)
                        .generateAssignPreset());
            }
        }
        target.getCodeStorage().addStateInitialization(statements);
    }

    @Override
    public CifDataProvider getCifDataProvider() {
        return new CifDataProvider() {
            @Override
            public PlcExpression getValueForConstant(Constant constant) {
                // TODO Return the proper PLC expression for the requested constant.
                return new PlcVarExpression(new PlcDataVariable("someConstantvariable", PlcElementaryType.LREAL_TYPE));
            }

            @Override
            public PlcExpression getValueForDiscVar(DiscVariable variable) {
                PlcBasicVariable plcDiscvar = variables.get(variable);
                Assert.notNull(plcDiscvar);
                return new PlcVarExpression(plcDiscvar);
            }

            @Override
            public PlcVarExpression getAddressableForDiscVar(DiscVariable variable) {
                PlcBasicVariable plcDiscvar = variables.get(variable);
                Assert.notNull(plcDiscvar);
                return new PlcVarExpression(plcDiscvar);
            }

            @Override
            public PlcExpression getValueForContvar(ContVariable variable, boolean getDerivative) {
                if (getDerivative) {
                    return funcAppls.negateFuncAppl(new PlcRealLiteral("1.0"));
                }
                PlcBasicVariable plcContVar = variables.get(variable);
                Assert.notNull(plcContVar);
                return new PlcVarExpression(plcContVar);
            }

            @Override
            public PlcVarExpression getAddressableForContvar(ContVariable variable, boolean writeDerivative) {
                Assert.check(!writeDerivative);

                PlcBasicVariable plcContVar = variables.get(variable);
                Assert.notNull(plcContVar);
                return new PlcVarExpression(plcContVar);
            }

            @Override
            public PlcExpression getValueForInputVar(InputVariable variable) {
                PlcBasicVariable plcInpvar = variables.get(variable);
                Assert.notNull(plcInpvar);
                return new PlcVarExpression(plcInpvar);
            }

            @Override
            public PlcVarExpression getAddressableForInputVar(InputVariable variable) {
                PlcBasicVariable plcInpvar = variables.get(variable);
                Assert.notNull(plcInpvar);
                return new PlcVarExpression(plcInpvar);
            }
        };
    }
}
