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

import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.AInvariant;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.parser.ast.tokens.AName;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifEventRefTypeChecker;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;

/** Invariant declaration wrapper. */
public class InvDeclWrap extends DeclWrap<Invariant> {
    /** The CIF AST representation of the invariant declaration. */
    private final AInvariant astInv;

    /** The CIF AST name of the referenced event, or {@code null}. */
    private final AName event;

    /**
     * Constructor for the {@link InvDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astInv The CIF AST representation of the invariant.
     * @param event The CIF AST name of the referenced event, or {@code null}.
     * @param mmInv The CIF metamodel representation of the invariant.
     */
    public InvDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AInvariant astInv, AName event, Invariant mmInv) {
        super(tchecker, scope, mmInv);
        this.astInv = astInv;
        this.event = event;
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
        if (getName() != null) {
            checkName();
        }

        // This declaration is now checked 'for use'.
        status = CheckStatus.USE;
    }

    @Override
    public void tcheckFull() {
        tcheckForUse();

        // Process predicate.
        AExpression astPred = astInv.predicate;
        Expression pred = transExpression(astPred, BOOL_TYPE_HINT, scope, null, tchecker);
        mmDecl.setPredicate(pred);

        // Check predicate type.
        CifType t = pred.getType();
        CifType nt = CifTypeUtils.normalizeType(t);
        if (!(nt instanceof BoolType)) {
            tchecker.addProblem(ErrMsg.INV_NON_BOOL, astPred.position, CifTextUtils.typeToStr(t));
            // Non-fatal error.
        }

        // Check event reference.
        if (event != null) {
            Expression eventRef = CifEventRefTypeChecker.checkEventRef(event, scope, tchecker);
            mmDecl.setEvent(eventRef);
        }

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }
}
