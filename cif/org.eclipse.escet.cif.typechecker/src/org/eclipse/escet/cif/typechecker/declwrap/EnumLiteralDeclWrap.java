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

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.parser.ast.tokens.AAnnotatedIdentifier;
import org.eclipse.escet.cif.typechecker.CheckStatus;
import org.eclipse.escet.cif.typechecker.CifAnnotationsTypeChecker;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;

/** Enumeration literal declaration wrapper. */
public class EnumLiteralDeclWrap extends DeclWrap<EnumLiteral> {
    /** The CIF AST representation of the enumeration literal. */
    private final AAnnotatedIdentifier astDecl;

    /**
     * Constructor for the {@link EnumLiteralDeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param astDecl The CIF AST representation of the enumeration literal.
     * @param mmDecl The CIF metamodel representation of the enumeration literal.
     */
    public EnumLiteralDeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, AAnnotatedIdentifier astDecl,
            EnumLiteral mmDecl)
    {
        super(tchecker, scope, mmDecl);
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
        List<Annotation> annos = CifAnnotationsTypeChecker.transAnnotations(astDecl.annotations, scope, tchecker);
        mmDecl.getAnnotations().addAll(annos);

        // This declaration is now fully checked.
        status = CheckStatus.FULL;
    }
}
