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

import static org.eclipse.escet.cif.typechecker.CifTypesTypeChecker.transCifType;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.declarations.AInputVariableDecl;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Input variable declaration wrapper. */
public class InputVariableDeclWrap extends DeclWrap<InputVariable> {
    /** The CIF AST representation of the input variables. */
    private final AInputVariableDecl astDecls;

    /**
     * Constructor for the {@link InputVariableDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecls The CIF AST representation of the input variables.
     * @param mmDecl The CIF metamodel representation of the input variable.
     */
    public InputVariableDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AInputVariableDecl astDecls,
            InputVariable mmDecl)
    {
        super(tchecker, scope, mmDecl);
        this.astDecls = astDecls;
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
            tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Input variable", getAbsName(),
                    CifTextUtils.typeToStr(type));
            throw new SemanticException();
        }

        // Set the type of the input variable.
        mmDecl.setType(type);

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }

    @Override
    public void tcheckFull() {
        // The 'for use' check already fully checks the input variable.
        tcheckForUse();
    }
}
