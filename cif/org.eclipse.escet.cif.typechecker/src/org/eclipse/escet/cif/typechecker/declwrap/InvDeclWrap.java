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

package org.eclipse.escet.cif.typechecker.declwrap;

import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifEventRefTypeChecker;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.java.Assert;

/** Invariant declaration wrapper. */
public class InvDeclWrap extends DeclWrap<Invariant> {
    /** The necessary information to type check the invariant. */
    private final InvariantInfo invariantInfo;

    /**
     * Constructor for the {@link InvDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this invariant.
     * @param invariantInfo The necessary information to type check the invariant.
     */
    public InvDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, InvariantInfo invariantInfo) {
        super(tchecker, scope, invariantInfo.mmInv);
        this.invariantInfo = invariantInfo;
        Assert.check(mmDecl.getName() != null);
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

        // This declaration is now checked 'for use'.
        status = CheckStatus.USE;
    }

    @Override
    public void tcheckFull() {
        // Do the 'for use' check.
        tcheckForUse();

        // Do the 'full' check.
        tcheckFull(tchecker, scope, invariantInfo);

        // This invariant is now fully checked.
        status = CheckStatus.FULL;
    }

    /**
     * Fully type checks the invariant, by processing the predicate and the optional event reference.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this invariant.
     * @param invariantInfo The necessary information to type check the invariant.
     */
    public static void tcheckFull(CifTypeChecker tchecker, ParentScope<?> scope, InvariantInfo invariantInfo) {
        // Process predicate.
        AExpression astPred = invariantInfo.astInv.predicate;
        Expression pred = transExpression(astPred, BOOL_TYPE_HINT, scope, null, tchecker);
        invariantInfo.mmInv.setPredicate(pred);

        // Check predicate type.
        CifType t = pred.getType();
        CifType nt = CifTypeUtils.normalizeType(t);
        if (!(nt instanceof BoolType)) {
            tchecker.addProblem(ErrMsg.INV_NON_BOOL, astPred.position, CifTextUtils.typeToStr(t));
            // Non-fatal error.
        }

        // Check event reference.
        if (invariantInfo.event != null) {
            Expression eventRef = CifEventRefTypeChecker.checkEventRef(invariantInfo.event, scope, tchecker);
            invariantInfo.mmInv.setEvent(eventRef);
        }
    }
}
