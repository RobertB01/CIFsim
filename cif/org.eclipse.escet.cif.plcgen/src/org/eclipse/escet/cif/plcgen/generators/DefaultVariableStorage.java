//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.StateInitVarOrderer;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.plcgen.conversion.expressions.CifDataProvider;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenResult;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;

/** Class for handling storage and retrieval of globally used variables in the PLC program. */
public class DefaultVariableStorage implements VariableStorage {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Type generator. */
    private final TypeGenerator typeGen;

    /** PLC code storage and writer. */
    private final PlcCodeStorage codeStorage;

    /** Generator for obtaining clash-free names in the generated code. */
    private final NameGenerator nameGenerator;

    /** Names of converted declarations. */
    private final Map<Declaration, PlcVariable> variables = map();

    /**
     * Constructor of the {@link DefaultVariableStorage} class.
     *
     * @param target PLC target to generate code for.
     * @param typeGen Type generator.
     * @param codeStorage PLC code storage and writer.
     * @param nameGenerator Generator for obtaining clash-free names in the generated code.
     */
    public DefaultVariableStorage(PlcTarget target, TypeGenerator typeGen, PlcCodeStorage codeStorage,
            NameGenerator nameGenerator)
    {
        this.target = target;
        this.typeGen = typeGen;
        this.codeStorage = codeStorage;
        this.nameGenerator = nameGenerator;
    }

    @Override
    public void addStateVariable(Declaration decl, CifType type) {
        PlcType varType = typeGen.convertType(type);
        String varName = nameGenerator.generateGlobalName(decl);
        PlcVariable plcVar = new PlcVariable(varName, varType);
        variables.put(decl, plcVar);
        codeStorage.addStateVariable(plcVar);
    }

    @Override
    public void process() {
        // Construct a converter for CIF expressions.
        ExprGenerator exprGen = new ExprGenerator(target, getRootCifDataProvider(), typeGen, nameGenerator);

        // Order the state variables on their dependencies for initialization.
        StateInitVarOrderer varOrderer = new StateInitVarOrderer();
        for (Declaration decl: variables.keySet()) {
            // Ordering dies if you supply anything else but discrete or continuous variables.
            if (decl instanceof DiscVariable) {
                varOrderer.addObject(decl);
            }
        }

        // Generate initialization code and store it.
        List<PlcStatement> statements = list();
        statements.add(new PlcCommentLine("Initialize the state variables."));
        for (Declaration decl: varOrderer.computeOrder(true)) {
            ExprGenResult exprResult;
            if (decl instanceof DiscVariable discVar) {
                exprResult = exprGen.convertExpr(first(discVar.getValue().getValues()));
            } else if (decl instanceof InputVariable inpVar) {
                Expression defaultValue = CifValueUtils.getDefaultValue(inpVar.getType(), null);
                exprResult = exprGen.convertExpr(defaultValue);
            } else {
                throw new AssertionError("Unexpected kind of variable " + decl);
            }
            statements.addAll(exprResult.code);
            exprGen.releaseTempVariables(exprResult.codeVariables);
            PlcVarExpression lhs = new PlcVarExpression(variables.get(decl));
            statements.add(new PlcAssignmentStatement(lhs, exprResult.value));
            exprGen.releaseTempVariables(exprResult.valueVariables);
        }
        codeStorage.addStateInitialization(statements);
    }

    @Override
    public CifDataProvider getRootCifDataProvider() {
        return new CifDataProvider() {
            @Override
            public PlcExpression getExprForConstant(Constant constant) {
                // TODO Return the proper PLC expression for the requested constant.
                return new PlcVarExpression(new PlcVariable("someConstantvariable", PlcElementaryType.LREAL_TYPE));
            }

            @Override
            public PlcExpression getExprForDiscVar(DiscVariable variable) {
                PlcVariable plcDiscvar = variables.get(variable);
                Assert.notNull(plcDiscvar);
                return new PlcVarExpression(plcDiscvar);
            }

            @Override
            public PlcExpression getExprForContvar(ContVariable variable, boolean getDerivative) {
                // TODO Return the proper PLC expression for the requested continuous variable.
                return new PlcVarExpression(new PlcVariable("someContvariable", PlcElementaryType.LREAL_TYPE));
            }

            @Override
            public PlcExpression getExprForLocation(Location location) {
                // TODO Return the proper PLC expression for the requested location.
                return new PlcBoolLiteral(false);
            }

            @Override
            public PlcExpression getExprForInputVar(InputVariable variable) {
                PlcVariable plcInpvar = variables.get(variable);
                Assert.notNull(plcInpvar);
                return new PlcVarExpression(plcInpvar);
            }
        };
    }
}
