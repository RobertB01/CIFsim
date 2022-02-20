//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.checkStaticEvaluable;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.cif.typechecker.CifTypesTypeChecker.transCifType;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.parser.ast.declarations.AConstDecl;
import org.eclipse.escet.cif.parser.ast.declarations.AConstant;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Constant declaration wrapper. */
public class ConstDeclWrap extends DeclWrap<Constant> {
    /** The CIF AST representation of the constants. */
    private final AConstDecl astDecls;

    /** The CIF AST representation of the constant. */
    private final AConstant astDecl;

    /**
     * Constructor for the {@link ConstDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecls The CIF AST representation of the constants.
     * @param astDecl The CIF AST representation of the constant.
     * @param mmDecl The CIF metamodel representation of the constant.
     */
    public ConstDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AConstDecl astDecls, AConstant astDecl,
            Constant mmDecl)
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

        // We are starting this declaration, so add it for cycle detection.
        tchecker.addToCycle(this);

        try {
            // Get the type of the declaration.
            CifType type = transCifType(astDecls.type, scope, tchecker);
            CifType ntype = CifTypeUtils.normalizeType(type);

            // Determine the type of the value.
            Expression value = transExpression(astDecl.value, type, scope, null, tchecker);
            CifType vtype = value.getType();
            CifType nvtype = CifTypeUtils.normalizeType(vtype);
            mmDecl.setValue(value);

            // Check for allowed types.
            if (CifTypeUtils.hasComponentLikeType(type)) {
                tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Constant", getAbsName(),
                        CifTextUtils.typeToStr(type));
                throw new SemanticException();
            }

            // Check for compatible types. If the type of the constant is a
            // rangeless integer type, we can derive the range from the type of
            // the value.
            if (ntype instanceof IntType && CifTypeUtils.isRangeless((IntType)ntype)) {
                // Make sure value is an integer type as well.
                if (!(nvtype instanceof IntType)) {
                    tchecker.addProblem(ErrMsg.CONST_TYPE_VALUE_MISMATCH, mmDecl.getPosition(),
                            CifTextUtils.typeToStr(vtype), getAbsName(), CifTextUtils.typeToStr(type));
                    throw new SemanticException();
                }

                // Set the type.
                mmDecl.setType(EMFHelper.deepclone(vtype));
            } else {
                // Make sure the types are compatible.
                if (!CifTypeUtils.checkTypeCompat(type, vtype, RangeCompat.CONTAINED)) {
                    tchecker.addProblem(ErrMsg.CONST_TYPE_VALUE_MISMATCH, mmDecl.getPosition(),
                            CifTextUtils.typeToStr(vtype), getAbsName(), CifTextUtils.typeToStr(type));
                    throw new SemanticException();
                }

                // Set the type.
                mmDecl.setType(type);
            }

            // Make sure constant is statically evaluable. Also evaluate it, to
            // catch run-time evaluation failures.
            checkStaticEvaluable(value, tchecker);
            try {
                CifEvalUtils.eval(value, false);
            } catch (CifEvalException e) {
                tchecker.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
                throw new SemanticException();
            }
        } finally {
            // We checked this declaration, so remove it from cycle detection.
            tchecker.removeFromCycle(this);
        }

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    @Override
    public void tcheckFull() {
        // We need the value of constants for the bounds of integer type
        // ranges. As such, we need to evaluate constants in the type checker,
        // and must thus guarantee the absence of cycles. As such, constants
        // are fully checked 'for use'.
        tcheckForUse();
    }
}
