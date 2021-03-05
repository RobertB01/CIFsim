//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.ALLOW_DIST;
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
import org.eclipse.escet.cif.typechecker.ExprContext;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.typechecker.SemanticException;

/**
 * Discrete variable declaration wrapper. This class is not used for function parameters or local variables of
 * functions.
 *
 * @see FuncParamDeclWrap
 * @see FuncVariableDeclWrap
 */
public class DiscVariableDeclWrap extends DeclWrap<DiscVariable> {
    /** The expression type checking context to use for the initial values. */
    private static final ExprContext INIT_VALUE_CTXT = ExprContext.DEFAULT_CTXT.add(ALLOW_DIST);

    /** The CIF AST representation of the discrete variables. */
    private final ADiscVariableDecl astDecls;

    /** The CIF AST representation of the discrete variable. */
    private final ADiscVariable astDecl;

    /**
     * Constructor for the {@link DiscVariableDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecls The CIF AST representation of the discrete variables.
     * @param astDecl The CIF AST representation of the discrete variable.
     * @param mmDecl The CIF metamodel representation of the discrete variable.
     */
    public DiscVariableDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, ADiscVariableDecl astDecls,
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
        // This symbol table entry is only used for discrete variables, and not
        // for function parameters, etc.
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
            tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Discrete variable", getAbsName(),
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

        // Check the initial values.
        typeCheckVarValues();

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    /** Type check the initial values of the discrete variable. */
    private void typeCheckVarValues() {
        // Variable values. Skip if default value.
        AVariableValue values1 = astDecl.value;
        if (values1 == null) {
            return;
        }

        // Construct VariableValue.
        VariableValue values2 = newVariableValue();
        values2.setPosition(values1.position);
        mmDecl.setValue(values2);

        // At least one value. Check all of them.
        CifType varType = mmDecl.getType();
        List<Expression> valueList = values2.getValues();
        List<AExpression> astValues = values1.values;
        if (astValues == null) {
            astValues = list();
        }
        for (AExpression value1: astValues) {
            Expression value2 = transExpression(value1, varType, scope, INIT_VALUE_CTXT, tchecker);
            valueList.add(value2);

            // Check types.
            CifType valueType = value2.getType();
            if (!CifTypeUtils.checkTypeCompat(varType, valueType, RangeCompat.CONTAINED)) {
                String articleText = (astValues.size() == 1) ? "the" : "an";
                tchecker.addProblem(ErrMsg.DISC_VAR_TYPE_VALUE_MISMATCH, value1.position,
                        CifTextUtils.typeToStr(valueType), articleText, "discrete ", getAbsName(),
                        CifTextUtils.typeToStr(varType));
                // Non-fatal error.
            }
        }
    }
}
