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

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.parser.ast.declarations.ATypeDef;
import org.eclipse.escet.cif.parser.ast.declarations.ATypeDefDecl;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifAnnotationsTypeChecker;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Type declaration wrapper. */
public class TypeDeclWrap extends DeclWrap<TypeDecl> {
    /** The CIF AST representation of the type declarations. */
    private final ATypeDefDecl astDecls;

    /** The CIF AST representation of the type declaration. */
    private final ATypeDef astDecl;

    /**
     * Constructor for the {@link TypeDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecls The CIF AST representation of the type declarations.
     * @param astDecl The CIF AST representation of the type declaration.
     * @param mmDecl The CIF metamodel representation of the type declaration.
     */
    public TypeDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, ATypeDefDecl astDecls, ATypeDef astDecl,
            TypeDecl mmDecl)
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
            CifType type = transCifType(astDecl.type, scope, tchecker);

            // Check for allowed types.
            if (CifTypeUtils.hasComponentLikeType(type)) {
                tchecker.addProblem(ErrMsg.DECL_INVALID_TYPE, type.getPosition(), "Type declaration", getAbsName(),
                        CifTextUtils.typeToStr(type));
                throw new SemanticException();
            }

            // Set the type.
            mmDecl.setType(type);
        } finally {
            // We checked this declaration, so remove it from cycle detection.
            tchecker.removeFromCycle(this);
        }

        // This declaration is now checked 'for use'.
        status = CheckStatus.USE;
    }

    @Override
    public void tcheckFull() {
        // Check for use first, and make sure not already fully checked.
        tcheckForUse();
        if (isCheckedFull()) {
            return;
        }

        // Type check and add the annotations.
        List<Annotation> annos = CifAnnotationsTypeChecker.transAnnotations(astDecls.annotations, scope, tchecker);
        mmDecl.getAnnotations().addAll(annos);

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }
}
