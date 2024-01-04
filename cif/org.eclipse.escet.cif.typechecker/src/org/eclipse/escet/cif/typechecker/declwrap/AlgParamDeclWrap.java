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

import static org.eclipse.escet.cif.typechecker.CifTypesTypeChecker.transCifType;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.AAlgParameter;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.typechecker.SemanticException;

/**
 * Algebraic parameter declaration wrapper.
 *
 * @see FuncParamDeclWrap
 */
public class AlgParamDeclWrap extends DeclWrap<AlgParameter> {
    /** The CIF AST representation of the algebraic parameter. */
    private final AAlgParameter astDecl;

    /**
     * Constructor for the {@link AlgParamDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecl The CIF AST representation of the algebraic parameter.
     * @param mmDecl The CIF metamodel representation of the algebraic parameter.
     */
    public AlgParamDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AAlgParameter astDecl, AlgParameter mmDecl) {
        super(tchecker, scope, mmDecl);
        this.astDecl = astDecl;
    }

    @Override
    public String getName() {
        return mmDecl.getVariable().getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(mmDecl.getVariable());
    }

    @Override
    public void tcheckForUseImpl() {
        // Check for reserved names.
        checkName();

        // Get the type of the declaration. Resolve it in the parent scope
        // of the component definition, not in the parent scope of the
        // algebraic parameter.
        tchecker.addToCycle(this);

        CifType type;
        try {
            type = transCifType(astDecl.type, scope.getParent(), tchecker);
        } finally {
            tchecker.removeFromCycle(this);
        }

        // Check for allowed types.
        if (CifTypeUtils.hasComponentLikeType(type)) {
            tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Algebraic parameter", getAbsName(),
                    CifTextUtils.typeToStr(type));
            throw new SemanticException();
        }

        // Set the type.
        mmDecl.getVariable().setType(type);

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    @Override
    public void tcheckFull() {
        // The 'for use' check already fully checks the algebraic parameter.
        tcheckForUse();
    }
}
