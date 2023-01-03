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

package org.eclipse.escet.cif.typechecker.declwrap;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newVariableValue;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.cif.typechecker.CifTypesTypeChecker.transCifType;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.declarations.ADiscVariable;
import org.eclipse.escet.cif.parser.ast.declarations.ADiscVariableDecl;
import org.eclipse.escet.cif.parser.ast.declarations.AVariableValue;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.FunctionScope;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticException;

/**
 * Function variable (local variable of an internal user-defined function) declaration wrapper.
 *
 * @see DiscVariableDeclWrap
 */
public class FuncVariableDeclWrap extends DeclWrap<DiscVariable> {
    /** The CIF AST representation of the function variables. */
    private final ADiscVariableDecl astDecls;

    /** The CIF AST representation of the function variable. */
    private final ADiscVariable astDecl;

    /**
     * Constructor for the {@link FuncVariableDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecls The CIF AST representation of the function variables.
     * @param astDecl The CIF AST representation of the function variable.
     * @param mmDecl The CIF metamodel representation of the function variable.
     */
    public FuncVariableDeclWrap(CifTypeChecker tchecker, FunctionScope scope, ADiscVariableDecl astDecls,
            ADiscVariable astDecl, DiscVariable mmDecl)
    {
        super(tchecker, scope, mmDecl);
        this.astDecls = astDecls;
        this.astDecl = astDecl;
    }

    @Override
    public String getName() {
        return mmDecl.getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(mmDecl);
    }

    @Override
    public void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // Get the type of the declaration.
        tchecker.addToCycle(this);

        CifType type;
        try {
            type = transCifType(astDecls.type, scope, tchecker);
        } finally {
            tchecker.removeFromCycle(this);
        }

        // Check for allowed types.
        if (CifTypeUtils.hasComponentLikeType(type)) {
            tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Variable", getAbsName(),
                    CifTextUtils.typeToStr(type));
            throw new SemanticException();
        }

        // Set the type.
        mmDecl.setType(type);

        // This declaration is now checked 'for use'.
        status = CheckStatus.USE;
    }

    @Override
    public void tcheckFull() {
        // First, check 'for use', and make sure we haven't checked it before.
        tcheckForUse();
        if (isCheckedFull()) {
            return;
        }

        // Check the initial value.
        typeCheckVarValue();

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    /** Type check the initial value of the function variable. */
    private void typeCheckVarValue() {
        // Variable values. Skip if default value.
        AVariableValue values1 = astDecl.value;
        if (values1 == null) {
            return;
        }

        // Construct VariableValue.
        VariableValue values2 = newVariableValue();
        values2.setPosition(values1.createPosition());
        mmDecl.setValue(values2);

        // Exactly one value. Get value.
        List<Expression> valueList = values2.getValues();
        List<AExpression> astValues = values1.values;
        if (astValues == null) {
            astValues = list();
        }
        Assert.check(astValues.size() == 1);
        AExpression value1 = astValues.get(0);

        // Type check value.
        CifType varType = mmDecl.getType();
        Expression value2 = transExpression(value1, varType, scope, null, tchecker);
        valueList.add(value2);

        // Check types.
        CifType valueType = value2.getType();
        if (!CifTypeUtils.checkTypeCompat(varType, valueType, RangeCompat.CONTAINED)) {
            tchecker.addProblem(ErrMsg.DISC_VAR_TYPE_VALUE_MISMATCH, value1.position, CifTextUtils.typeToStr(valueType),
                    "the", "", getAbsName(), CifTextUtils.typeToStr(varType));
            // Non-fatal error.
        }
    }
}
